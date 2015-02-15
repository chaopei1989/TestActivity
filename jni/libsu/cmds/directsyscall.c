#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <limits.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mount.h>
#include <signal.h>
#include <dirent.h>



typedef int (*func)(int,char**);

struct func_index{
	func index;//存放整型索引
	char* cmd;//存放命令名称
};


//从这里开始，对应各项命令的具体实现
int do_chmod(int argc,char* argv[]){
	if(argc<3)
		return -1;
	char *pathname = argv[1]; 
	mode_t mode = atoi(argv[2]);
	return chmod(pathname,mode);
}

//不能删除非空文件夹
int do_rm(int argc,char* argv[]){
	if(argc<2)
		return -1;

	struct stat st;
	char *pathname = argv[1];

	if(lstat(pathname,&st)<0)
		return -1;

	if(!S_ISDIR(st.st_mode))
		return unlink(pathname);

	return rmdir(pathname);
}

int do_chown(int argc,char* argv[]){
	if(argc<4)
		return -1;

	char* path = argv[1];
	uid_t owner = atoi(argv[2]);
	gid_t group = atoi(argv[3]);

	return chown(path,owner,group);
}

int do_kill(int argc,char* argv[]){
	if(argc<3)
		return -1;

	pid_t pid = atoi(argv[1]);
	int sig = atoi(argv[2]);

	return kill(pid,sig);
}

int do_mv(int argc,char* argv[]){
	if(argc<3)
		return -1;
	char* oldpath = argv[1];
	char* newpath = argv[2];

	return rename(oldpath,newpath);
}

int do_mount(int argc,char* argv[]){
	if(argc<6)
		return -1;
	char *source = argv[1];
	char *target = argv[2];
	char *fstype = argv[3];
	long flags = atol(argv[4]);
	char *data = argv[5];
	int res = mount(source,target,fstype,flags,data);
	return res;
}

struct func_index indices[] = {
	{do_chmod,"chmod"},
	{do_chown,"chown"},
	{do_kill,"kill"},
	{do_mount,"mount"},
	{do_mv,"mv"},
	{do_rm,"rm"},
};

//找到该命令的索引值
static func find_index(char * icmd){
	int len = sizeof(indices)/sizeof(struct func_index);
	int i = 0;
	while(i<len){
		char *cmd = indices[i].cmd;
		int slen = strlen(cmd);
		if(strncmp(indices[i].cmd,icmd,slen)==0){
			return indices[i].index;
		}
		i++;
	}

	return (void*)0;
}
//argv 的第一项仍然存放命令
int directsyscall_main(int argc, char* argv[]){
	//至少应该有一项命令行参数
	if(argc <1)
		return -1;
	
	func  index = find_index(argv[0]);	
	if(index ==0)
		return -1;

	return index(argc,argv);
}


