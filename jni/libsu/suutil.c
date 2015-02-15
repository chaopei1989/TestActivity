#include "su.h"

uid_t _getuid(pid_t pid){

	uid_t result = -1;

	char buf[256];
	snprintf(buf,sizeof(buf),"/proc/%d/status",pid);

	FILE *fp;
	if((fp = fopen(buf,"r")) !=NULL){
		// 文件格式:
		// Name:    o360.mobilesafe
		// State:   S (sleeping)
		// Tgid:    368
		// Pid: 368
		// PPid:    94
		// TracerPid:   0
		// Uid: 10104   10104   10104   10104
		// Gid: 10104   10104   10104   10104
		// 为了通用，这里最多读10行，只要有一行是 Uid: 开头即可
		int i = 0;
		while((i++)<10){
			if(fgets(buf,sizeof(buf),fp)){
				if(strncasecmp("Uid:",buf,4)==0){
					sscanf(buf+4,"%d",&result);
					break;
				}
			}else
				break;
		}
		fclose(fp);
	}
	return result;

}


//查询指定pid 的父进程 pid
pid_t _getppid(pid_t pid){
	pid_t  result = -1;

	char buf[256];
	snprintf(buf,sizeof(buf),"/proc/%d/status",pid);

	FILE* fp;

	if((fp = fopen(buf,"r")) != NULL){
		//文件格式:
		//Tgid: 368
		//PPid: 94
		//这里同样最多只读取10行
		int i = 0;
		while((i++)<10){
			if(fgets(buf,sizeof(buf),fp)){
				if(strncasecmp("PPid:",buf,5) == 0){
					sscanf(buf+5,"%d",&result);
					break;
				}
			}else
				break;
		}
		fclose(fp);
	}
    return result;
} 

//获取程序的执行目录
char* get_exec_path(char *path,int len){
	if(readlink("/proc/self/exe",path,len)!=-1){
		strncpy(path,(const char*)dirname(path),len);
		return path;
	}
	return NULL;

}

//重新设置环境变量
void resetenv(){
    PLOGV("uid:%d,euid:%d,gid:%d,egid:%d",getuid(),geteuid(),getgid(),getegid());
    //重新设置euid 和egid ,防止有些授权管理出现漏洞
    setresgid(0,0,0);
    setresuid(0,0,0);
	char* env_ld = getenv(LD_LIBRARY_PATH);
	char* env_ald = getenv(ALT_LD_LIBRARY_PATH);
	if(env_ld == NULL&&env_ald!=NULL){
		setenv(LD_LIBRARY_PATH,env_ald,1);
	}

	if(env_ald == NULL){
		setenv(LD_LIBRARY_PATH,"/system/lib:/vendor/lib",1);
	}
}


//判断当前进程是否是mobilesafe的进程
static int is_mobilesafe_process(pid_t pid){
	static const char* ALLOWED_PACKAGE_NAME_PREFIX = "com.qihoo360.mobilesafe";
	static const char* DAMN_ABOARD = "com.qihoo.security";
	char buffer[1024];
	memset(buffer,0,sizeof(buffer));

	snprintf(buffer,sizeof(buffer),"/proc/%d/cmdline",pid);

	int fd = open(buffer,O_RDONLY); 
	if(fd>=0){
		int len = read(fd,buffer,sizeof(buffer)-1);
		close(fd);

		if(len>0){
			buffer[len] = 0;
			char* caller_process_name = buffer;
            //PLOGV("pkg name is %s",buffer);
			if(strncmp(caller_process_name,ALLOWED_PACKAGE_NAME_PREFIX,strlen(ALLOWED_PACKAGE_NAME_PREFIX ))==0)
				return 0;
			if(strncmp(caller_process_name,DAMN_ABOARD,strlen(DAMN_ABOARD))==0)
				return 0;

		}
	}
	return -1;
}
//只允许360开头的包名调用本程序，
//对程序签名的验证也应该再这里进行
pid_t get_360_pid(pid_t pid,int sticky){
	if(is_mobilesafe_process(pid)==0)
		return pid;
	return -1;
}

//创建一个临时的文件名
int mktempname(char* name){
	struct timeval tv;
	gettimeofday(&tv,NULL);
	snprintf(name,PATH_MAX,"%s-%ld-%ld",DOMAIN_SERVER_SOCKET_NAME,tv.tv_sec,tv.tv_usec);
	return 0;
}


int readx(int fd,void* buff,size_t count){
	int len = 0;
	while((len = read(fd,buff,count))<0){
		if(errno == EINTR||errno == EAGAIN)
			continue;
		break;
	}
	return len;
}

int writex(int fd,const char* buff,size_t count){
	int len = 0;
	while((len = write(fd,buff,count))<0){
		if(errno == EINTR||errno == EAGAIN)
			continue;
		break;
	}
	return len;
}


int _system(char* executable,char* argv[],char* envp[]){
    sigset_t newmask,oldmask,zeromask;
    sigemptyset(&newmask);
    sigemptyset(&zeromask);
	sigaddset(&zeromask,SIGCHLD);
    if(sigprocmask(SIG_BLOCK,&zeromask,&oldmask)<0){
        PLOGE("can't fetch old mask");
        return -1; 
    }

	pid_t pid = fork();
	int status = -1;
	if(pid<0){
		return -1;
	}else if(pid == 0){
		execve(executable,argv,envp);
		PLOGE("can't exec %s",executable);
		exit(-1);
	}else{
		int res = waitpid(pid,&status,0);
		PLOGV("_system exec res is %d with status is %d if exit %d",res,status,WIFEXITED(status));
	}
	//SIGCHLD信号会被阻塞，直到命理执行完毕
    if(sigprocmask(SIG_UNBLOCK,&zeromask,NULL)<0){
          PLOGE("can't restore old mask");
    }
	return status;
}

int _system2(char* executable,char* argv[],char* envp[]){
    sigset_t newmask,oldmask,zeromask;
    sigemptyset(&newmask);
    sigemptyset(&zeromask);
    sigaddset(&zeromask,SIGCHLD);
    if(sigprocmask(SIG_BLOCK,&zeromask,&oldmask)<0){
        LOGE("can't fetch old mask");
        return -1; 
    }   

	int sockfd[2];//创建套接字队，用以从签名验证进程中读取结果
	if(socketpair(AF_LOCAL,SOCK_STREAM,0,sockfd)<0){
		return -1;
	}
	//忽略SIGPIPE信号的影响，避免造成进程退出
	signal(SIGPIPE,SIG_IGN);

    pid_t pid = fork();
    int status = -1; 
    if(pid<0){
		close(sockfd[0]);
		close(sockfd[1]);
		//SIGCHLD信号会被阻塞，直到命理执行完毕
		if(sigprocmask(SIG_UNBLOCK,&zeromask,NULL)<0)
			 LOGE("can't restore old mask");
        return -1; 
    }else if(pid == 0){ 
		int pipe_fds[2];
		int errfd = -1;
		if(!pipe(pipe_fds)){
			errfd = pipe_fds[1];
		}

		pid_t gcpid = fork();
		if(gcpid != 0){
			exit(0);
		}
		while(getppid() != 1){
			sleep(1);
		}
		close(sockfd[0]);
		dup2(sockfd[1],STDOUT_FILENO);
		if(errfd > 0)
			dup2(errfd ,STDERR_FILENO);

        execve(executable,argv,envp);
        PLOGE("can't exec %s",executable);
		exit(-1);
    }else{
       // int res = waitpid(pid,&status,0);
       // LOGV("_system exec res is %d with status is %d if exit %d",res,status,WIFEXITED(status));
		close(sockfd[1]);//关闭写管道，这里只读
		char result[32];
		memset(result,0,sizeof(result));
		
		struct timeval tv;
		tv.tv_sec = 15;
		tv.tv_usec = 0;

		fd_set fds;
		int k = 0;
		while(k < 3){
		FD_ZERO(&fds);
		FD_SET(sockfd[0],&fds);
		if(select(sockfd[0]+1,&fds,NULL,NULL,&tv)<1){
			if(k >=2){
				close(sockfd[0]);
				goto failure;
			}
		}else{
			break;
		}
		k++;
		}

		int len = readx(sockfd[0],result,sizeof(result));
		LOGV("check signature result is %s",result);
		if(len>0){
			status = atoi(result);	
		}
		close(sockfd[0]);
    }   
    //SIGCHLD信号会被阻塞，直到命理执行完毕
failure:if(sigprocmask(SIG_UNBLOCK,&zeromask,NULL)<0){
          LOGE("can't restore old mask");
    }   
		//避免子进程变成僵尸进程
	if(pid > 0)
		waitpid(pid, NULL, 0);
    return status;
}
