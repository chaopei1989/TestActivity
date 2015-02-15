/**
  *由这个文件编译出的2进制文件，只能被INIT进程启动，360主程序向这个程序发送
  *启动libsu的指令，这个程序会FORK出一个进程，从子进城中加载libsu
  *命令格式 <path to libsu> [-c|-server]  <client_socket>
  *这个命令中也实现了ping,输入是\n,返回值也是\n
  */

#include "su.h"
#include <fcntl.h>
#include <private/android_filesystem_config.h>
#include <cutils/properties.h>
#include <cutils/sockets.h>

//本进程所监听的套接字的名字
#define SOCKET_PATH "daemon_rt_server"

static void sig_handler(int sig){
	//万一主程序中的sigaction没有执行成功，那么这里可以防止其变成僵尸进程
	waitpid(-1,NULL,WNOHANG);
}

int main(int argc,char* argv[]){
	strcpy(argv[0], "zygote");
	//监听套接字，在init.rc文件中定义
	int lsocket;
	lsocket = android_get_control_socket(SOCKET_PATH);	
	if(lsocket < 0){
		PLOGE("failed to get socket from environment %s",strerror(errno));
		exit(1);
	}
	
	if(listen(lsocket,3)<0){
		PLOGE("fail to listen to server_daemon %s",strerror(errno));
		exit(1);
	}

	struct sigaction siga;
	siga.sa_handler= sig_handler;
	siga.sa_flags = SA_NOCLDSTOP;
	sigaction(SIGCHLD,&siga,NULL);

	while(1){
		int socket = accept(lsocket,NULL,NULL);

		if(socket < 0){
			PLOGE("accept failed in daemon %s",strerror(errno));
			continue;
		}
		//获取请求套接字的进程名
		struct ucred creds;
		socklen_t szcreds = sizeof(creds);
		memset(&creds,0,sizeof(szcreds));
		getsockopt(socket,SOL_SOCKET,SO_PEERCRED,&creds,&szcreds);
		
		if(get_360_pid(creds.pid,1)<=0||checksignature(creds.uid,"/data/rt_daemon.jar")!=0){
			PLOGE("invalid request comes from %d",creds.pid);
			close(socket);
			continue;
		}

		//子进程中，读取来自客户端的指令，并解析
		char req[4096];
		memset(req,0,sizeof(req));
		int len = read(socket,req,sizeof(req));
		
		if(len <1){
			close(socket);
			continue;
		}

		if(strcmp(req,"\n")==0){
			PLOGE("it is a ping action in daemon");
			write(socket,"\n",2);
			close(socket);
			continue;
		}

		//fork 进程并处理该次请求
		pid_t pid = fork();
		if(pid == 0){
			//子进程中不监听
			close(lsocket);
			//子进程中，读取来自客户端的指令，并解析

			if(len<1){
				PLOGE("invalid cmd in daemon, abandon it");
				close(socket);
				exit(-1);
			}

			//开始解析输入
			int offset = 0;
			char *cmd = req;//libsu.so的文件路径
			offset +=  strlen(req)+1;
			char *strargc = req+offset;//第一个参数是参数个数 
			int arcnt = strargc[0];

			if(arcnt > 5){
				PLOGE("invalid arg count, try again");
				exit(-1);
			}
			offset+=2;
			char* params[arcnt+2];
			
			int index = 0;
			while(index<arcnt){
				params[index] = req + offset;
				offset += strlen(params[index])+1;
				PLOGV("param[%d] is %s",index, params[index]);
				index++;
			}
			params[index] = 0;

			fcntl(socket,F_SETFD,FD_CLOEXEC);
			//给当前客户端反馈
			write(socket,"\n",2);
			
			execv(cmd,params);
			PLOGE("execute failure in daemon");
			exit(-1);
			
		}else if(pid >0){
			//什么事情都不用做
			close(socket);
		}else{
			PLOGE("fork error in daemon");
		}
	}
}

