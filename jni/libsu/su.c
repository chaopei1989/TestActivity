#include "su.h"
#include <poll.h>
#include <sys/prctl.h>
#include <sys/inotify.h>

#define SOCKET_NAME_TEMPLATE "socket_templateXXXXXX"
#define TERMINATION_STATUS 88
#define MAX_PARAM 10
#define RW_TIMEOUT 3
//返回结果需要一定的长度限制，这里定义为20K
#define MAX_OUTPUT_BYTES 1024*1024
#define MAX_STREAM_EXEC_TIMEOUT 90
typedef void (*HANDLER)(int);


struct myuser{
	uid_t uid;
	struct myuser* next;
};

//保存当前所有合法的用户列表
static struct myuser* user_list = NULL;


typedef enum{SOCKET_PERMANENT,SOCKET_TEMP} sock_type; 
typedef enum{STREAM,INT,SYSCALL_INT,UNDEFINED} result_type;

static int setprocattrcon(char* context,
                          pid_t pid, char *attr)
{
        char *path;
        int fd, rc;
        pid_t tid;
        ssize_t ret;
        int errno_hold;

        if (pid > 0)
                rc = asprintf(&path, "/proc/%d/attr/%s", pid, attr);
        else {
                tid = gettid();
                rc = asprintf(&path, "/proc/self/task/%d/attr/%s", tid, attr);
        }
        if (rc < 0)
                return -1;

        fd = open(path, O_RDWR);
        free(path);
        if (fd < 0)
                return -1;
        if (context)
                do {
                        ret = write(fd, context, strlen(context) + 1);
                } while (ret < 0 && errno == EINTR);
        else
                do {
                        ret = write(fd, NULL, 0);       /* clear */
                } while (ret < 0 && errno == EINTR);
        errno_hold = errno;
        close(fd);
        errno = errno_hold;
        if (ret < 0)
                return -1;
        else
                return 0;
}


static int getprocattrcon(char** context,
                          pid_t pid, const char *attr)
{
        char *path, *buf;
        size_t size;
        int fd, rc;
        ssize_t ret;
        pid_t tid;
        int errno_hold;

        if (pid > 0)
                rc = asprintf(&path, "/proc/%d/attr/%s", pid, attr);
        else {
                tid = gettid();
                rc = asprintf(&path, "/proc/self/task/%d/attr/%s", tid, attr);
        }
        if (rc < 0)
                return -1;

        fd = open(path, O_RDONLY);
        free(path);
        if (fd < 0)
                return -1;

        size = 4098 ;
        buf = malloc(size);
        if (!buf) {
                ret = -1;
                goto out;
        }
        memset(buf, 0, size);

        do {
                ret = read(fd, buf, size - 1);
        } while (ret < 0 && errno == EINTR);
        if (ret < 0)
                goto out2;

        if (ret == 0) {
                *context = NULL;
                goto out2;
        }

        *context = strdup(buf);
        if (!(*context)) {
                ret = -1;
                goto out2;
        }
        ret = 0;
      out2:
       free(buf);
      out:
        errno_hold = errno;
        close(fd);
        errno = errno_hold;
        return ret;
}



static int tfd = -1;//用在主进程中，用于记录最后请求主进程退出的客户端fd
static int sserv_fd = -1;//用于记录当前主进程中监听的服务器调节FD

/** argv[0] 的指针 */
static char * g_argv0 = NULL;
static char jar_path[1024];
static char watch_path[1024] = {0};

static int validate_pname_sticky = 1;
//向可信用户列表中添加一个
static void add_user(uid_t uid){
	struct myuser* current = user_list;
	struct myuser* parent = user_list;

	while(current!=NULL){
		parent = current;
		current = current->next;
	}
	struct myuser* new = malloc(sizeof(struct myuser));
	if(new == NULL)
		return;
	new->uid = uid;
	new->next = NULL;
	if(parent == NULL){
		//说明user_list 为空
		user_list = new;
	}else{
		parent->next = new;
	}
}

//删除一个用户
static void delete_user(uid_t uid){
	struct myuser* current = user_list;
	struct myuser* parent = user_list;
	if(user_list!=NULL&&user_list->uid==uid){
		user_list = user_list->next;
		free(parent);
		return;
	}
	while(current!=NULL){
		if(current->uid == uid){
			parent->next = current->next;
			free(current);
			return;
		}else{
			parent = current;
			current = current->next;
		}
	}
}
//寻找是否有值为uid的user,有则是该值的index，否则-1
static int find_user(uid_t uid){
	struct myuser* current = user_list;
	int index = 0;
	while(current!=NULL){
		if(current->uid == uid)
			return index;
		current = current->next;
		index++;
	}
	return -1;
}
//获取客户端套接字的pid
static pid_t getcredspid(int fd,uid_t* uid){
	if(fd < 0)
		return -1;
	struct ucred creds;
	socklen_t szcreds = sizeof(creds);
	memset(&creds,0,sizeof(creds));
	if(getsockopt(fd,SOL_SOCKET,SO_PEERCRED,&creds,&szcreds)<0){
		return -1;
	}

	*uid = creds.uid;
	return creds.pid;//说明该请求是从360卫士的进程中发出的
	
}

//提供了给客户端ping 的功能,这种功能能够再较短的时间内判断
//服务器是否可用,如果是客户端发来的ping，则返回0,否则返回一个负值 
static int do_ping(char *req,int cli_fd){
	//ping只是发送了一个回车符
	if(strcmp(req,"\n")==0){
		int res = writex(cli_fd,"\n",2);
		PLOGE("do_ping in libsu with serv_fd %d and write res %d",sserv_fd,res);
		return 0;
	}
	return -1;
}

//返回libsu的版本号，这个再重启su的时候做判断
static int do_getversion(char* req, int cli_fd){
   //getversion的命令格式是VERSION\n	
	if(strcmp(req,"VERSION\n")==0){
		char buff[16];
		snprintf(buff,sizeof(buff),"%d\n",VERSION);
		int res =writex(cli_fd,buff,strlen(buff)+1);
		PLOGE("do_getversion in libsu with serv_fd %d and write res %d",sserv_fd,res);
		return 0;
	}
	return -1;
}

//阻塞SIGALRM信号
static int blocksigalrm(){
	sigset_t newmask,oldmask,zeromask;
	sigemptyset(&newmask);
	sigemptyset(&zeromask);

	if(sigprocmask(SIG_BLOCK,&zeromask,&oldmask)<0){
		PLOGE("can't fetch old mask");
		return -1;
	}
	sigaddset(&oldmask,SIGALRM);
	if(sigprocmask(SIG_SETMASK,&oldmask,NULL)<0){
		PLOGE("can't restore old mask");
		return -1;
	}
	return 0;

}



//创建一个服务器端套套接子，PERMAMENT表示使用DOMAIN_SERVER_SOCKET_NAMEa
//TEMP表示创建一个临时的套接字
static int create_server_socket(sock_type type,char *csname){
	char buff[PATH_MAX];
	struct sockaddr_un un;
	int fd = socket(AF_LOCAL,SOCK_STREAM,0);

	if(fd < 0){
		PLOGE("can't create serversocket--");
		return -1;
	}
    memset(&un,0,sizeof(un));
	un.sun_family = AF_LOCAL;

	switch(type){
		case SOCKET_PERMANENT:
			snprintf(&un.sun_path[1],sizeof(un.sun_path)-1,"%s",DOMAIN_SERVER_SOCKET_NAME);
			un.sun_path[0]=0;
			break;
		case SOCKET_TEMP:
		   if(mktempname(buff)==0){
			   PLOGV("temp socket is %s ",buff);
			   strcpy(&un.sun_path[1],buff);
			   un.sun_path[0]=0;
		   }else{
			   PLOGE("can't create a temp socket");
			   goto clean;
		   }
			break;
		default:
			return -1;//不指出的套接字类型

	}
	//返回c层服务区器的名字
	strcpy(csname,&un.sun_path[1]);

	socklen_t start = offsetof(struct sockaddr_un,sun_path)+1+strlen(&un.sun_path[1]);
	if(bind(fd,(struct sockaddr*)&un,start)<0){
		PLOGE("can't bind socket %d ",fd);
		goto clean;
	}
    //第二个参数是日志级别
	if(listen(fd,3)<0){
		PLOGE("can't listen to socket %d ",fd);
		goto clean;
	}

	return fd;

clean:close(fd);
	  return -1;
}

/** directly copied from librpc**/
/**如果是来自客户端的请求，返回1；如果是文件被删除返回2 **/
static int wait_event(int serv_fd,int inotify_fd){
	struct timeval tv;
	fd_set fds;

	tv.tv_sec = 30;
	tv.tv_usec = 0;
	FD_ZERO(&fds);
	FD_SET(serv_fd, &fds);
	FD_SET(inotify_fd, &fds);
	//int maxfd = max(serv_fd, inotify_fd) + 1;
	int maxfd = serv_fd > inotify_fd?serv_fd:inotify_fd;
	maxfd++;
	if (select(maxfd, &fds, NULL, NULL, &tv) < 1) {
		return 0;
	}

	if (FD_ISSET(serv_fd, &fds)) return 1;
	if (FD_ISSET(inotify_fd, &fds)) return 2;

	return 0;
}

//对accept函数的封装，在里边加入了进程名检查的功能，以后还会加入对签名的检查
static int socket_accept(int serv_fd,int trycount,int* selresult){
	if(serv_fd<0){
		PLOGE("can't listen to server %d--",serv_fd);
		return -1;
	}

	//poll函数不应该这样使用，还是使用select
	/*struct timeval tv;
	tv.tv_sec = 60;
	tv.tv_usec = 0;

	fd_set fds;
	FD_ZERO(&fds);
	FD_SET(serv_fd,&fds);

	int i = 0;
	while((*selresult = select(serv_fd +1 ,&fds,NULL,NULL,&tv))<1&&i<trycount){
		i++;
		return -1;
	}*/

	int fd = -1;
	fd = accept(serv_fd,NULL,NULL);
	if(fd<0){//非法的文件描述符
		return -1;
	}

	uid_t uid = -1;
	pid_t pid = getcredspid(fd,&uid);
	if(pid <0)
		goto clean;

	int user_index = -1;
	//验证套接字请求的合法性
	if(uid == AID_ROOT){
		//ROOT用户，想干什么都型
	}else if((user_index=find_user(uid))>=0){
		PLOGV("find a trusted user at index %d",user_index);
		//表示这个用户再可信列表中，
#if 0
		if(get_360_pid(pid,validate_pname_sticky)<=0){
			//虽然在可信列表中，但包明不合法，从列表中删除
			//有可能是包卸载，新安装apk导致
			delete_user(uid);
			//报名不一样肯定返回失败
			goto clean;
		}
#endif
	}else if(checksignature(uid,jar_path)==0){
		//签名通过，而且报名合法，加到可信列表中
		//包名的验证是再jar里边做的
		PLOGV("signature for uid %d is valid",uid);
		add_user(uid);
	}else{
		PLOGE("request comes from invalid client check result is false ");
		goto clean;
	}

	struct timeval rw_tv = {RW_TIMEOUT,0};
	setsockopt(fd,SOL_SOCKET,SO_SNDTIMEO,(char *)&rw_tv,sizeof(struct timeval));
	setsockopt(fd,SOL_SOCKET,SO_RCVTIMEO,(char *)&rw_tv,sizeof(struct timeval));

	return fd;

clean:close(fd);
	  return -1;

}

//超时后会重新将进程的SIGCHLD处理函数设置为这个
static void childsigchld(int sig){
	//其实不是很必要，如果命令执行超时，则等待命令执行的进程
	//会受到超时，这是等待进程会向命令执行进程发送SIGKILL信号
	//如果命令执行进程在退出时，等待进程还在，则可以通过这种方式避免命理功能执行进程成为僵尸进程
	PLOGV("progress has been forced to close dut to timeout");
	//由于已经再sa_flag中设置了SA_NOCLDWAIT,所以没必要在等待了
	//waitpid(-1,NULL,WNOHANG);
}

//在子进程中执行的处理SIGCHLD函数
static void childsighandler(int sig){
	if(sig==SIGALRM)
		PLOGV("exec cmd timeout");
	return;
}

// 处理来自客户端的请求，其实可以将对客户端的验证放再这里来作
//但是比较担心来自恶意客户端的洪泛攻击，导致启动过多的临时进程
static int handle_request(int fd,char* req){
	struct sigaction siga;
	siga.sa_handler= childsighandler; 
	siga.sa_flags = SA_NOCLDSTOP;
	sigemptyset(&siga.sa_mask);
		
    //双回车符表示希望rt_server退出,退出码为88，主进程看到这个退出码
	//会自行退出
	/*if(strcmp(req,"\n\n")==0){
		PLOGV("receive a exit command");
		close(fd);
        exit(TERMINATION_STATUS);
	}*/

	PLOGV("execv cmd is %s",req);
	result_type rtype = UNDEFINED;
	int sock_timeout = 120;//将等待超时设置为90秒
	struct timeval tv ={RW_TIMEOUT,0};
	int sockfd[2];

	if(req[0]){
		int resulttype = req[0];
		//32代表返回整形执行结果，64代表返回流类型
		//16表示直接调用系统函数，返回整形执行结果
		if(resulttype == 64)
			rtype = STREAM;
		if(resulttype == 32)
			rtype = INT;
		if(resulttype == 16)
			rtype = SYSCALL_INT;
		//加入对超时的判断,设置超时的处理函数
		if(sigaction(SIGALRM,&siga,NULL)<0||sigaction(SIGCHLD,&siga,NULL)<0){
			PLOGE("sigaction error in grand child");	
			goto failure;
		}

		int offset = strlen(req+1)+2;//req就是命令本身,第一个字符应该被忽略所以+2
		int argc = req[offset++];//参数个数

		if(argc>MAX_PARAM||argc<0){
			PLOGE("too much or to few params for cmd %s",req);
			goto failure;
		}
		//创建流管道，将孙子进程中的输出流重定向给子进程
		if(socketpair(AF_LOCAL,SOCK_STREAM,0,sockfd)<0){
			PLOGE("can't create a socketpair");
			goto failure;
		}

		pid_t  pid_in_child = fork();

		if(pid_in_child <0){
			PLOGE("fork error while executing cmd");
			writex(fd,"-1\n",4);
			close(fd);
			exit(-1);//当前进程执行失败
		}else if(pid_in_child == 0){
			int errfd = -1;//会将孙子进程的错误流重定向到errfd
			int pipe_fds[2];
			siga.sa_handler = childsigchld;
			siga.sa_flags = SA_NOCLDWAIT;//进程结束时不要将其变成僵尸进程
			sigaction(SIGCHLD,&siga,NULL);
			pid_t gcpid = fork(); //for sumsung's kernel modifications

			if(gcpid != 0){
				exit(0);
			}
			pid_t o_pid = getppid();
			while(getppid() != 1){
				sleep(1);
			}

			if(!pipe(pipe_fds)){
				errfd = pipe_fds[1];
			}

			pid_t n_pid = getppid();
			PLOGE("opid = %d ,npid = %d",o_pid ,n_pid);
			close(sockfd[0]);//关闭读管道

			dup2(sockfd[1],STDOUT_FILENO);//将孙进程的输出里重定向到子socketpair的写管道中
			if(errfd > 0){
				dup2(errfd ,STDERR_FILENO);
			}
			char *params[MAX_PARAM+1];
			params[0] = req+1;

			int i = 1;
			while(i<argc+1){
				params[i]=req + offset;
				offset += strlen(params[i])+1;
				PLOGV("param[%d] is %s",i,params[i]);
				i++;
			}

			params[i]=NULL;//最后一个参数必须是NULL

			//解析环境变量的参数
			int env_num = req[offset++];
			//直接退出
			if(env_num>MAX_PARAM||env_num<0)
				exit(-1);	
			i = 0;
			while(i<env_num){
				char *p = req + offset;
				offset += strlen(p)+1;
				if(strchr(p,'=')==NULL)break;//其实就是检查当前是不是一条合法的环境变量
				PLOGV("env[%d] is %s",i,p);
				putenv(p);
				i++;
			}
			close(fd);//即使不管也会退出

			//如果用户直接调用的系统调用，那么直接在这里执行掉
			if(rtype == SYSCALL_INT){
				PLOGV("directsyscall started for %s",params[0]);
				int dscres = directsyscall_main(argc+1,params);
				PLOGV("diresctsyscall result is %d",dscres);
				if(dscres)
					exit(-1);
				exit(0);
			}
			//为了放置系统命令被认为篡改，所以我们优先用
			//重写过的命令，如果没有重写过的命令，才用execv
			//PLOGV("start to exec cmd_main(%d,%s)",argc+1,params[0]);
			int cmd_res = cmd_main(argc+1,params);
			if(cmd_res != -100){
				//说明这个命令已经被支持，并已经被执行
				PLOGV("cmd %s has already been supported with result = %d",params[0],cmd_res);
				exit(cmd_res);
			}
			//查看结尾的命令是不是app_process,是的话需要更改域
			char* cmdstr = strrchr(params[0] ,'/');
			if(cmdstr == NULL){
				cmdstr = params[0];
			}else{
				cmdstr++;
			}
			if(cmdstr!=NULL&&!strcmp("app_process",cmdstr)){
				int mypid = getpid();
				char* buff;
				setprocattrcon("u:r:kernel:s0" ,mypid,"current");
				/*if(!getprocattrcon(&buff,mypid ,"current")){
					PLOGE("context = %s",buff);
					PLOGE("CLASSPATH = %s",getenv("CLASSPATH"));
					if(buff)
						free(buff);
				}*/
				
				//setprocattrcon("u:r:init:s0" ,mypid,"exec");
			}
			execv(params[0],params);
			PLOGE("cmd %s is not correctly executed",params[0]);
			exit(-1);

		}else if(pid_in_child>0){
			close(sockfd[1]);//关闭写管道
			goto stream;
#if 0
			if(rtype == STREAM) goto stream;
			int status = 0;
			int result = -1;
			//10秒内要么超时，要么子程序结束退出
			sigset_t newmask,oldmask,zeromask;
			sigemptyset(&newmask);
			sigemptyset(&zeromask);
			//在调用alarm前调用，先设置对SIGALRM和SIGUSR1的阻塞
			//防止在sigsuspend时收不到信号
			sigaddset(&newmask,SIGCHLD);
			sigaddset(&newmask,SIGALRM);

			if(sigprocmask(SIG_BLOCK,&newmask,&oldmask)<0){
				PLOGE("can't set new mask ");
				goto status_failure;
			}
			//在之前的版本中，这段的实现逻辑是有问题的，如果在
			//等待之前进程就已经退出，会导致进程进入无谓的阻塞
			//所以在阻塞前检查下子进程是否已经退出
			int waitResult = -1;
			if((waitResult=waitpid(pid_in_child,&status,WNOHANG))<=0){
				alarm(sock_timeout);
				sigsuspend(&zeromask);
				alarm(0);
				waitResult = waitpid(pid_in_child,&status,WNOHANG); 
			}

			//这里就没必要再退出了,因为即使错误也可以正常走完流程
			if(sigprocmask(SIG_SETMASK,&oldmask,NULL)<0){
				PLOGE("can't restore old mask");
			}


			if(waitResult>0&&WIFEXITED(status)){
				result = WEXITSTATUS(status);
			}else{
				//因为超时或者程序异常退出
				//给子进程发送kill信号
				if(waitpid(pid_in_child,&status,WNOHANG)==0){
					siga.sa_handler = childsigchld;
					siga.sa_flags = SA_NOCLDWAIT;//子进程结束时不要将其变成僵尸进程
					sigaction(SIGCHLD,&siga,NULL);
					kill(pid_in_child,SIGKILL);
				}
				goto status_failure;
			}

			char exec_res[32];
			snprintf(exec_res,sizeof(exec_res),"%d\n",result);
			PLOGV("exec result is %s",exec_res);
			writex(fd,exec_res,strlen(exec_res));
			close(fd);
			goto success;
#endif

			//这种写法很恶心，但是比打括号来的直观
	 stream:setsockopt(sockfd[0],SOL_SOCKET,SO_RCVTIMEO,(char *)&tv,sizeof(struct timeval));
			char transferbuff[4096];
			int len = 0;
			struct timeval tv;
			tv.tv_sec = sock_timeout;
			tv.tv_usec = 0;

			fd_set fds;
			int k = 0;
			while(k < 3){
			FD_ZERO(&fds);
			FD_SET(sockfd[0],&fds);
			if(select(sockfd[0]+1,&fds,NULL,NULL,&tv)<1){
				PLOGE("can't read data from the socket pair at %d",k) ;
				//如果读取不到任何信息，直接将子进程退出	
				if(k >=2){
					kill(pid_in_child,SIGKILL);//通知子进程退出
					waitpid(pid_in_child,NULL,0);
					goto stream_failure;
				}
			}else{
				break;
			}
			k++;
			}

			//用来记录当前流命令已经执行了多长时间
			struct timeval start,end;
			gettimeofday(&start,NULL);
            
			int totalLen = 0;//当前已经返回的字节数
			int has_send_result = 0;
			while((len = readx(sockfd[0],transferbuff,sizeof(transferbuff)))>0){
				//PLOGV("result is %s",transferbuff);
				if(writex(fd,transferbuff,len)<0){
					//直接关闭客户端输出流
					kill(pid_in_child,SIGKILL);//通知子进程退出
					waitpid(pid_in_child,NULL,0);
					goto stream_failure;
				}
				has_send_result = 1 ;//已经向客户端返回了答案
				//如果返回的结果过长，则可以选择结束
				gettimeofday(&end,NULL);
				totalLen+=len;
				if(totalLen>MAX_OUTPUT_BYTES||(end.tv_sec-start.tv_sec)>MAX_STREAM_EXEC_TIMEOUT){
					writex(fd,"",1);//告诉客户端已经到文件尾
					siga.sa_handler = childsigchld;
					siga.sa_flags = SA_NOCLDWAIT;//进程结束时不要将其变成僵尸进程
					sigaction(SIGCHLD,&siga,NULL);
					kill(pid_in_child,SIGKILL);//通知子进程退出
					break;
				}
			}
			//再数据流正常关闭的情况下，SIGCHLD的信号处理方法中并没有调用wait系列的方法
			//i孙进程会变成僵尸进程，所以在这里调用waitpid,如果是被强制退出，这里将直接返回-1
			waitpid(-1,NULL,0);
			if(rtype == INT && !has_send_result){
				goto unconditional_success;
			}
			close(fd);
			close(sockfd[0]);
		}	
	}else{
		goto failure;
	}	

success:return 0;
failure:if(rtype == STREAM)//对于UNDEFINED的情形，可当作INT来处理
			goto stream_failure;
status_failure:if(rtype == INT)
				   goto unconditional_success;
		writex(fd,"-1\n",4);
		close(fd);
		close(sockfd[0]);//关闭子进程中的读管道
		return -1;//错误退出
stream_failure:close(fd);
			   close(sockfd[0]);
			   return -1;
unconditional_success:writex(fd,"0\n",3);
					  close(fd);
					  close(sockfd[0]);
					  return 0;


}

//在主进程中执行，当子进程结束执行时触发
static void mainsigchld(int sig){
	//检查子进程退出时状态
	if(sig == SIGPIPE){
		PLOGV("receive a sigpipe");
		return;
	}
	int status = 0;
	if(waitpid(-1,&status,WNOHANG)<0){
		//PLOGE("didn't get a valid child status");
		return ;
	}
	
	if(WIFEXITED(status)){
		int exit_num = WEXITSTATUS(status);
		PLOGV("rt_server revceived a exit with num %d",exit_num);
	}
	
}

//创建客户端套接字，这个套接字会被用来与服务器端练习
static int create_client_socket(char* sname){
	struct sockaddr_un addr;
	socklen_t len;

	memset(&addr,0,sizeof(addr));
	addr.sun_family = AF_LOCAL;
	strcpy(&addr.sun_path[1],sname);
	addr.sun_path[0] = 0;
	int fd = socket(AF_LOCAL,SOCK_STREAM,0);

	if(fd<0){
		PLOGE("can't create cleanup socket client");
		return -1 ;
	}

	//设置读写超时，域套接字，设置为3秒
	//int sock_timeout = RW_TIMEOUT;
	struct timeval tv = {RW_TIMEOUT,0};
	setsockopt(fd,SOL_SOCKET,SO_SNDTIMEO,(char *)&tv,sizeof(struct timeval));
	setsockopt(fd,SOL_SOCKET,SO_RCVTIMEO,(char *)&tv,sizeof(struct timeval));

	len = offsetof(struct sockaddr_un,sun_path)+1+strlen(&addr.sun_path[1]);
	if(connect(fd,(struct sockaddr *)&addr,len)<0){
		PLOGE("can't connect to server %s",sname);
		close(fd);
		return-1;	
	}
	return fd;

}

//清除残留在系统中的进程
static void cleanup_process(){
	PLOGV("send termination to old rt_server");

	int fd = create_client_socket(DOMAIN_SERVER_SOCKET_NAME);
	if(fd < 0)
		return;
	const char* term = "\n\n";
	if(writex(fd,term,sizeof(term))<0){
		goto clean;
	}
	
	//设置套接字超时，防止等待时间过长
	//int sock_timeout = RW_TIMEOUT;
	struct timeval tv ={RW_TIMEOUT,0};
	setsockopt(fd,SOL_SOCKET,SO_RCVTIMEO,(char *)&tv,sizeof(struct timeval));

	char buff[16];
	memset(buff,0,sizeof(buff));
	readx(fd,buff,sizeof(buff));//采用这种方式可以确保rt_server被关闭
	PLOGV("original rt_server send a result %s",buff);

clean:close(fd);
}

//通知java客户端 服务已经被启动
//sname代表java 服务器地址,csname代表是服务器端的地址
static int notify_java_server(char *sname,int error,char *csname){
	PLOGV("Connnect to server %s",sname);
	//在这里增加一个特别的约定，如果java端服务器地址为--,那么表示不希望返回
	//增加这个设计主要是为了能够通过init进程来启动rt_server
	if(strcmp("--",sname)==0)
		return 0;

	int i = 0;
	int fd = -1;
	//java服务器端可能还没准备好，所以尝试3次
	while((fd = create_client_socket(sname))<0&&i<3){
		i++;
		sleep(1);//不用频繁的重复请求
	}

	if(fd < 0){
		PLOGE("can't create a echo client to java server");
		return -1;
	}

	//error=0 表示传送c层服务器地址
	if(!error){
		writex(fd,csname,strlen(csname));
	}else{
		writex(fd,"\n",2);
	}
	close(fd);
	return fd;
}

//在主进程中设立针对子进程退出的处理函数
static int set_sigchldhandler_in_main(HANDLER handler,int flag){
	struct sigaction siga,oldsiga;
	siga.sa_handler = handler; 
	siga.sa_flags = flag;
	sigemptyset(&siga.sa_mask);
	return sigaction(SIGCHLD,&siga,NULL)+sigaction(SIGPIPE,&siga,NULL);
}

//SERVER进程被启动后 将不断等待和接受客户端请求,这里封装了SERVER端的运行逻辑
static int run_server(char *sname,sock_type type){
	char csname[PATH_MAX];
	int serv_fd = create_server_socket(type,csname);
	if(serv_fd<0){
		PLOGE("can't create a server");
		//告诉java端出错了
		notify_java_server(sname,1,"\n");//如果出错只返回一个回车符
		return -1;
	}

	//监听子进程的退出
	if(set_sigchldhandler_in_main(mainsigchld,SA_NOCLDSTOP)<0){
		PLOGE("can't set sigchld handler to be mainsigchld");
		return -1;
	}

	//第二个参数0表示通知给JAVA服务器一个合法的rt_server地址	
	//将对客户端的通知操作 放到其他进程去做,如果失败了，其实对
	//启动rt_server没有影响，如果成功了则希望能够尽量将结果返回给客户端
	pid_t noti_pid = fork();	
	if(noti_pid == 0){
		close(serv_fd);
		exit(notify_java_server(sname,0,csname));
	}
	//父进程会继续运行服务
	//notify_java_server(sname,0,csname);
/*	if(notify_java_server(sname,0,csname)<0){
		close(serv_fd);
		return -3;//表示是因为无法将结果返回给服务器造成失败
	}*/

	//记录当前的服务器套接字的描述符
	sserv_fd = serv_fd;
	int selresult = -1;
	//这里增加对应用程序目录的监控，当卫士被卸载时，确保RT_SERVER退出
	int notify_fd = inotify_init();
	int watch_res = inotify_add_watch(notify_fd,watch_path,IN_DELETE);
	//如果监听卫士的应用目录失败也没必要退出，因为ROOT还可以正常使用	
	//notify_fd <0  watch_res < 0
	//一直监听客户端，如果有新的请求，则启动一个新的进程来处理
	while(1){
		int wait_res = wait_event(serv_fd,notify_fd);
		if(wait_res == 2){
			//检查卫士是否被卸载		
				int  uninstall = 0;

				unsigned char buf[1024] = {0};

				// 由于可能有多个事件发生，所以要用下面的循环的方式去读
				int len, index = 0;   
				while (((len = read(notify_fd, &buf, sizeof(buf))) < 0) && (errno == EINTR));       // 读取事件
				while (index < len) 
				{
					struct inotify_event *event = (struct inotify_event *)(buf + index);
					/* 由于 IN_DELETE_SELF 收不到，所以只能用 IN_DELETE 了，但是 IN_DELETE 只能收到子目录的删除事件，所以只能靠子目录被删除之后，
					   再判断一下 /data/data/com.qihoo360.mobilesafe 目录自身是否还存在
					   当覆盖安装时，收到的消息:
					   event->mask: 0x40000200
					   event->name: lib
					   当 com.qihoo360.mobilesafe 包被卸载时，收到的消息有:
					   event->mask: 0x40000200
					   event->name: lib
					   event->mask: 0x40000200
					   event->name: files
					   event->mask: 0x40000200
					   event->name: shared_prefs
					   event->mask: 0x40000200
					   event->name: databases
					   event->mask: 0x40000200
					   event->name: cache
					   当清除数据时，收到与上面相同的消息。
					   综合起来，卸载的判断条件为:
					   收到 files 目录的消息，且 com.qihoo360.mobilesafe 已不存在
					 */
					//LOGI("event->mask: 0x%08x\n", event->mask);
					//LOGI("event->name: %s\n", event->name);
					
					if ((event->mask & IN_ISDIR) && strcmp(event->name, "files") == 0)
					{
						// files 目录被删除的时候，/data/data/com.qihoo360.mobilesafe 目录还在，所以等1秒再检查目录
						sleep(1);

						struct stat statinfo;
						if (stat(watch_path, &statinfo) < 0)
						{
							uninstall = 1;
							break;
						}
					}

					index += sizeof(struct inotify_event) + event->len;             // 指向下一个事件。
				}
				
				if(uninstall){
					//卫士已经被卸载，可以退出rt_server进程了
					PLOGE("find that main program has been uninstalled , prepare to exit");
					inotify_rm_watch(notify_fd,watch_res);
					close(sserv_fd);
					while(-1!=wait(NULL)){
					}
					exit(0);
				}
			
		}
		if(wait_res != 1){
			//程序发生意外，什么也不做
			continue;
		}

		int cli_fd = socket_accept(serv_fd,1,&selresult);//第二个参数表示失败后尝试次数

		if(cli_fd<0){
			if(type == SOCKET_TEMP && selresult == 0){
				//如果是临时套接字,并且已超时,则关闭掉这个服务器，因为临时套接字的名字是随机生成的，无法向永久套接字一样向其发送终止命令，所以只能再超时后自己关闭
				close(sserv_fd);
				while(-1!=wait(NULL)){
				}
				PLOGV("tmp_server %s ended",csname);
				exit(0);
			}
			continue;
		}

        	char req[4096];
		//这里清空req非常重要，因为read函数不会帮我们清空，这样在运行ping函数的时候
		//就会执行上一条指令
		memset(req,0,sizeof(req));
		if(readx(cli_fd,req,sizeof(req))<=0){//直接给客户端返回执行失败
			PLOGE("can't read cmd request from client");
			writex(cli_fd,"-1\n",4);
			close(cli_fd);
			continue;
		}else{
			//表明这是一个结束进程的请求
			if(strcmp(req,"\n\n")==0){
				PLOGV("met a term cmd in pid %d with fd %d",getpid(),sserv_fd);
				tfd = cli_fd; 
				close(sserv_fd);	
				writex(tfd,"-1\n",4);//让请求的进程等待
				close(tfd);
				exit(0);
			}else if(do_ping(req,cli_fd)==0){
				close(cli_fd);
				continue;
			}else if(do_getversion(req,cli_fd)==0){
				close(cli_fd);
				continue;
			}
		}

	    pid_t cld_id = fork();	
        
	    if(cld_id==0){
			//子进程中没必要做监听
			close(serv_fd);
			//设置进程名称
			char* new_name ="rt_exec";
			strncpy(g_argv0,new_name,strlen(g_argv0));
#if defined(PR_SET_NAME)
			//int setres = prctl(PR_SET_NAME,(unsigned long)new_name,0,0,0);
			//PLOGE("try to set process name with res %d",setres);
#endif
		    exit(handle_request(cli_fd,req));
			//可以在这里中止子进程了，因为他没有必要继续运行
			
            //子程序中不用监听
		}else if(cld_id<0){
			PLOGV("fork error while processing new request");	
			writex(cli_fd,"-1\n",4);//表示执行失败
			close(cli_fd);
		}else if(cld_id>0){
			//父进程中
			close(cli_fd);
		}
			
	}
	return -1;
}

static int run_tmp_server(char* sname){
	run_server(sname,SOCKET_TEMP);
	return 0;
}

//启动su -server
static void do_run_server(char* sname,char *argv[],sock_type type){
	uid_t uid = getuid();
	PLOGV("uid = %d, euid = %d", uid, geteuid());

	//安全性检查
	if(strlen(sname)>64) return;

	if (uid != AID_ROOT)
	{
		LOGE("Permission denied");
		notify_java_server(sname, 1, "\n");//如果出错只返回一个回车符
		return;
	}
	
	//重新设定LD_LIBARY_PATH环境变量的值
	resetenv();

	int result = -1 ;
	if(type == SOCKET_PERMANENT){
		//如果已经有一个进程在运行，那么通知其退出
		cleanup_process();
		//在清理掉旧的rt_server之后，会进一步检查，如果当前进程的祖父进程是360安全卫士o
		//那么因为360->su->libsu.so ,这是su产生的shell进程其实旧可以被退出了
		//如果当前进程的祖父进程是INIT,则可以跳过这一步
		//if(res != INIT)
		//	kill(getppid(),SIGKILL);

		strncpy(argv[0],"avdaemon",strlen(argv[0]));
		// strcpy(argv[0], "zygote");
		prctl(PR_SET_NAME, "zygote", 0, 0, 0);
		result =run_server(sname,type);
		//如果com.qihoo.rt_server这个固定套接字不可用
		PLOGV("start rt_tmp_server in %d",getpid());
		strncpy(argv[0],"rt_tmp_server",strlen(argv[0]));
		result =run_tmp_server(sname);

	}else if(type == SOCKET_TEMP){
		//设置进程名字为rt_server,为了标示，临时服务器被叫作rt_tmp_server
		strncpy(argv[0],"rt_tmp_server",strlen(argv[0]));
		result =run_tmp_server(sname);
	}
	//if(result<0)
		//PLOGE("rt_server can't not be launched");

}

static void do_sudo(char *sname,char* argv[]){
	return do_run_server(sname,argv,SOCKET_TEMP);
}


int main(int argc,char* argv[]){
	//setprocattrcon("u:r:kernel:s0" ,getpid() ,"current");
	PLOGV("start libsu argc = %d",argc);
	if(argc < 4) return 1;
	//setprocattrcon("u:r:kernel:s0" ,getpid() ,"current");
	// 保存 argv[0] 的指针，后面修改进程名的时候用
	g_argv0 = argv[0];
	memset(jar_path,0,sizeof(jar_path));
	strcpy(jar_path,argv[3]);

	int jar_path_len = strlen(jar_path);
	//临时ROOT的team在传入参数时多传了个&和单引号，这里就是为他们做的适配
	if(jar_path[jar_path_len -2] == '&'){
		jar_path[jar_path_len -2] = 0;
	}

	PLOGV("jar_path is %s",jar_path);
	//jar_path 一般就是应用程序额根目录/auth.jar的形式，所有这里
	//直接去jar_path的父目录作为需要watch的路径

	//因为修补3星漏洞的时候，jar包是放在files目录下的，所以多增加了一种情况
	//如果jar包的watch_path的后缀是files,那么将watch_path的目录再向上提一个目录
	char watch_temp[1024] = {0};
	strcpy(watch_temp,dirname(jar_path));
	if(strncmp("files",basename(watch_temp),5)==0){
		strcpy(watch_path,dirname(watch_temp));
	}else{
		strcpy(watch_path,watch_temp);
	}

	int i = 1;
	while(i<argc){
		if(!strcmp(argv[i],"-server")){
			//启动服务
			if(++i<argc)
				do_run_server(argv[i],argv,SOCKET_PERMANENT);
			return 0;
		}else if(!strcmp(argv[i],"-c")){
			if(++i<argc)
				do_sudo(argv[i],argv);
			return 0;
		}else{
			break;
		}
				
	}
	//说明有多余的参数 
	/*
	if(i!=argc){
		return-1;
	}
	*/

	return 2;
}
