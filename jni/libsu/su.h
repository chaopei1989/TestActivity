#include <sys/types.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <sys/wait.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/stat.h>
#include <unistd.h>
#include <limits.h>
#include <fcntl.h>
#include <errno.h>
#include <libgen.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <stddef.h>
#include <sys/param.h>
#include <signal.h>

#undef LOG_TAG
#define LOG_TAG "rt_server"

//这个名字若需更改，请将RootClientDynamic中的mServerName一起更改
#define DOMAIN_SERVER_SOCKET_NAME "com.qihoo.rt_server_av"
#define DEBUG
//#ifndef DEBUG
//#define LOGW(...) ((void)0)
//#define LOGE(...) ((void)0)
//#define LOGD(...) ((void)0)
//#define LOGI(...) ((void)0)
//#define LOGV(...) ((void)0)
//#define PLOGV(...) ((void)0)
//#define PLOGE(...) ((void)0)
//#else
#include <android/log.h>
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define PLOGV(fmt,args...) LOGV(fmt" in function %s()\n",##args,__FUNCTION__)
#define PLOGE(fmt,args...) LOGE(fmt" in function %s(): %s\n",##args,__FUNCTION__,strerror(errno))
//#endif

#define LD_LIBRARY_PATH "LD_LIBRARY_PATH"
//ALT_LD_LIBRARY_PATH是从java中传入的一个环境变量，保存获取ROOT权限前的
//LD_LIBRARY_PATH的值
#define ALT_LD_LIBRARY_PATH "_LD_LIBRARY_PATH"

#define DEFAULT_BOOTCLASSPATH "BOOTCLASSPATH=/system/framework/core.jar:/system/framework/core-junit.jar:/system/framework/bouncycastle.jar:/system/framework/ext.jar:/system/framework/framework.jar:/system/framework/framework-ext.jar:/system/framework/android.policy.jar:/system/framework/services.jar:/system/framework/apache-xml.jar:/system/framework/filterfw.jar:/system/framework/com.motorola.android.frameworks.jar:/system/framework/com.motorola.android.widget.jar:/system/framework/com.motorola.frameworks.core.addon.jar:/system/framework/android.supl.jar"

#define DEFAULT_LD_LIBRARY_PATH "LD_LIBRARY_PATH=/system/lib:/vendor/lib"

#define DEFAULT_PATH "PATH=/system/bin:/system/xbin:/sbin"

extern char** environ;
//避免引入一些庞大的头文件
#define AID_ROOT 0
#define AID_SHELL 2000
//init 进程是系统第一个用户进程所以pid为1
#define INIT 1
//版本号非常重要，一定要与RootClient.java中的LIBVERSION保持一致
#define VERSION 9
//sticky=0 表示INIT和卫士进程都可以验证通过，sticky=1表示只有卫士进程可以验证通过
//验证成功返回进程的pid
extern int get_360_pid(pid_t pid,int sticky);

extern uid_t _getuid(pid_t pid);

extern pid_t _getppid(pid_t pid);

extern void resetenv();

extern char* get_exec_path(char *path,int len);

extern int mktempname(char* name);
//所有被重写的命令都在这里开始执行
//有一个特殊的返回值-100，这个值说明，我们并未重写这个命令
//需要调用execv方法
extern int cmd_main(int argc,char** argv);
extern int directsyscall_main(int argc,char** argv);

//加强读和写方法的健壮性，碰到errno为EAGAIN和EINTER的情况其实应该继续读取
extern int readx(int fd,void* buff,size_t count);
extern int writex(int fd,const char* buff,size_t count);

extern int checksignature(uid_t uid,char* jar_path);

extern int _system(char* executable,char* argv[],char* envp[]);
extern int _system2(char* executable,char* argv[],char* envp[]);
