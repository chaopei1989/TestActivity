#include "data.h"
#include "su.h"

struct operation{
	int index;
	int offset;
};

#define BOOTCLASSPATHFLAG 1
#define LD_LIBRARY_PATHFLAG 2
#define PATHFLAG 4

extern char** environ;

const struct operation pairs[]={
	{65,-97},{97,(-97+128)},
	{66,-71},{98,(-71+128)},
	{67,-39},{99,(-39+128)},
	{68,-6},{100,(-6+128)},
	{69,5},{101,(5+128)},
	{121,0},{122,128},
};

//执行失败返回-1000，收到这个值表示数据不合法，程序应当终止
static int findoffset(int index){
	int len = sizeof(pairs)/sizeof(struct operation);
	int i = 0;
	while(i<len){
		if(pairs[i].index == index)
			return pairs[i].offset;
		i++;
	}
	return -1000;
}


static int extractjar(char *path){
	char *data = XDATA;
	//data数据采用的索引+数据的格式，即data中2个字节描述
	//一个1个数据字节，data的数据格式是[索引字节][数据字节]...[索引字节][数据字节]
	//索引字节定义了针对数据字节的操作，例如如果索引字节的值122，那么在提取数据字节的时候
	//就应该将数据字节的值+128 在释放，
	//具体索引操作定义如下,前边表示索引值，后变表示现有数据字节与原数据字节的偏移量
	//{65,-97},{97,-97+128}
	//{66,-71},{98,-71+128}
	//{67,-39},{99,-39+128}
	//{{68,-6},{100,-6+128}
	//{69,+5},{101,5+128}
	//{121,0},{122,+128}
	

	//开始创建文件
	int fd = open(path,O_CREAT|O_RDWR|O_TRUNC);
	char buff[1];
	if(fd <0)
		goto clean;

	int len = strlen(data);
	int flag = 1;//1表示当前处理的位是索引，0表示当前处理的字节是数据字节
	int index = 121;
	int pos = 0;
	
	int total = 0;
	while(pos<len){
		int val = 0;

		if(flag == 1)//处理的是索引位，只需记录操作码
		{
			flag = 0;
			index = data[pos];
		}else{
			flag = 1;
			//数据位赋值
			val = data[pos];
			int offset = findoffset(index);
			if(offset == -1000)
				goto clean;
			val += offset;
			buff[0] = val;
			write(fd,buff,1);
			total++;
		}
		pos++;	
	}
	
	close(fd);
	return 0;
clean:close(fd);
	return -1;
}

//验证已有的jar包与我们释放出去的jar包是否一致
static int validatejar(char* path){
	struct stat st;
	if(stat(path,&st)<0)
		return -1;
	char *data = XDATA;
	if(st.st_size*2 != strlen(data))
		return -1;
	//开始创建文件
	int fd = open(path,O_RDONLY);
	char buff[1];
	if(fd <0)
		goto clean;

	int len = strlen(data);
	int flag = 1;//1表示当前处理的位是索引，0表示当前处理的字节是数据字节
	int index = 121;
	int pos = 0;
	
	while(pos<len){
		int val = 0;

		if(flag == 1)//处理的是索引位，只需记录操作码
		{
			flag = 0;
			index = data[pos];
		}else{
			flag = 1;
			//数据位赋值
			val = data[pos];
			int offset = findoffset(index);
			if(offset == -1000)
				goto clean;
			val += offset;
			buff[0] = 0;//
			if(readx(fd,buff,1)>0){
				if(val != buff[0])
					goto clean;
			}else
				goto clean;
		}
		pos++;	
	}
	
	close(fd);
	return 0;
clean:close(fd);
	return -1;
}


//去除空白字符
static char* skipblank(char* original){
	char* temp = original;
	while(isblank(*(temp++))){
	}
	if(temp != original)
		temp--;
	return temp;
}

//取出字符串尾部的回车符
static void tripret(char* original){
	PLOGV("start to trip value %s",original);
	if(original == NULL)
		return;
	if(strlen(original) == 0)
		return;
	char* end = original+strlen(original)-1;
	if(*end == '\n')
		*end = 0;
}


int _setenv(int flag,char** envp){
	FILE* fp = fopen("/init.rc","r");
	if(fp<0)
		return -1;
	char buff[2048];
	int toflag = 0;
	while(toflag<flag){
		memset(buff,0,sizeof(buff));
		if(fgets(buff,sizeof(buff),fp)==NULL)
			break;
		int index = 0;
		char* temp =skipblank(buff);
		if(strncasecmp("export",temp,strlen("export"))==0){
			//跳过export这个变量
			char* name=temp+strlen("export")+1;
			name =skipblank(name);
			if(strncasecmp("BOOTCLASSPATH",name,strlen("BOOTCLASSPATH"))==0){
				if(BOOTCLASSPATHFLAG&flag == 0)
					continue;
				char* value = name+strlen("BOOTCLASSPATH")+1;
				value = skipblank(value);
				tripret(value);

				if(value !=NULL)
				snprintf(envp[2],1024,"%s=%s","BOOTCLASSPATH",value);

				toflag|=BOOTCLASSPATHFLAG;
			}else if(strncasecmp("LD_LIBRARY_PATH",name,strlen("LD_LIBRARY_PATH"))==0){
				if(LD_LIBRARY_PATHFLAG&flag == 0)
					continue;
				char* value = name+strlen("LD_LIBRARY_PATH")+1;
				value = skipblank(value);
				tripret(value);

				if(value != NULL)
				snprintf(envp[0],256,"%s=%s","LD_LIBRARY_PATH",value);

				toflag|=LD_LIBRARY_PATHFLAG;
			}else if(strncasecmp("PATH",name,strlen("PATH"))==0){
				if(PATHFLAG&flag == 0)
					continue;
				char* value = name+strlen("PATH")+1;
				value = skipblank(value);
				tripret(value);
				
				if(value !=NULL)
				snprintf(envp[1],256,"%s=%s","PATH",value);

				toflag|=PATHFLAG;
			}
		}
	}
	fclose(fp);
	return 0;
}

//判断环境变量是否为空;
static int isenvempty(char* key){
	char *env = getenv(key);
	if(env ==NULL)
		return 1;
	if(strcmp(env,".")==0)
		return 1;
	return 0;
}
//检查签名是否合法
int checksignature(uid_t uid,char *jar_path){
	/*
	PLOGV("uid:%d,euid:%d,gid:%d,egid:%d",getuid(),geteuid(),getgid(),getegid());
	//重新设置euid 和egid ,防止有些授权管理出现漏洞
	setresgid(0,0,0);
	setresuid(0,0,0);*/

	if(validatejar(jar_path)!=0){
		PLOGV("auth.jar is invalid=============");
		//如果发现当前的jar包与我们所期待的不一致，先删除这个jar包 然后释放
		unlink(jar_path);
		if(extractjar(jar_path)!=0){
			PLOGE("extract jar to %s failed",jar_path);
			unlink(jar_path);
			return -1;
		}
		//更该jar包的读写属性
		if(chmod(jar_path,0644)<0){
			PLOGE("can't chmod for %s",jar_path);
			return -1;
		}
	}

	char *debug = "false";
#ifdef DEBUG
	debug = "true";//
#endif
	char *args[6];
//	char *envp[3];
	char classpath[256];
	char exec[1024];

	snprintf(classpath,256,"CLASSPATH=%s",jar_path);
	putenv(classpath);
	snprintf(exec,1024,"exec /system/bin/app_process /system/bin com.qihoo360.security.Authencation %d %s %s",uid,debug,"exit");

	args[0] ="/system/bin/sh";
	args[1] ="-c";
	args[2] =exec;
	args[3] =(char*)NULL;

/*	char* envp[5];
	char env0[256],env1[256],env2[1024];
	memset(env0,0,sizeof(env0));
	memset(env1,0,sizeof(env1));
	memset(env2,0,sizeof(env2));
	envp[0] = env0;//ld_lib
	envp[1] = env1;//path
	envp[2] = env2;//bootclas

	_setenv(7,envp);//先把所有相关的环境变量从init.rc中load处来
   //检查是否已经自己设置国环境变量，如果设置国，则用设置的值来替代	
	if(!isenvempty("LD_LIBRARY_PATH")){
		memset(envp[0],0,256);
		snprintf(envp[0],256,"%s=%s","LD_LIBRARY_PATH",getenv("LD_LIBRARY_PATH"));
	}
	if(!isenvempty("BOOTCLASSPATH")){
		memset(envp[2],0,1024);
		snprintf(envp[2],1024,"%s=%s","BOOTCLASSPATH",getenv("BOOTCLASSPATH"));
	}
	if(!isenvempty("PATH")){
		memset(envp[1],0,256);
		snprintf(envp[1],256,"%s=%s","PATH",getenv("PATH"));
	}
	//如果此时环境便令还为空,则使用默认
	if(envp[0] == NULL)
		envp[0] = DEFAULT_LD_LIBRARY_PATH;
	if(envp[1] == NULL)
		envp[1] = DEFAULT_PATH;
	if(envp[2] ==NULL)
		envp[2] = DEFAULT_BOOTCLASSPATH;
	//设置环境变量
	putenv(envp[0]);
	putenv(envp[2]);

    envp[3] =classpath;
    envp[4] = (char*)0;*/
	int status = _system(args[0],args,environ);	
	if(status!=-1)
		status = WEXITSTATUS(status);
	PLOGV("_system exit status is %d",status);
	if(status != 100){
		memset(exec,0,sizeof(exec));
		snprintf(exec,1024,"exec /system/bin/app_process /system/bin com.qihoo360.security.Authencation %d %s %s",uid,debug,"stream");
		status = _system2(args[0],args,environ);
		PLOGV("_system2 exit status is %d",status);
	}

	if(status == 100)
		return 0;
	return -1;
}

