#include "apue.h"
#include <errno.h>
#include <syslog.h>
static int log_to_stderr = 0;

static void
log_doit(int errnoflag,int priority,const char *fmt,va_list ap)
{
	int errno_save;
	char buf[MAXLINE];

	errno_save = errno;
	vsnprintf(buf,MAXLINE,fmt,ap);

	if(errnoflag)
	{
		snprintf(buf+strlen(buf),MAXLINE-strlen(buf),": %s",strerror(errno_save));
	}
	strcat(buf,"\n");
	if(log_to_stderr)
	{
		fflush(stdout);
		fputs(buf,stderr);
		fflush(stderr);
	}
	else
	{
		syslog(priority,buf);
	}
}


