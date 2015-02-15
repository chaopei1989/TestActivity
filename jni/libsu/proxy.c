#include <unistd.h>
#include <stdlib.h>
#include "su.h"

int main(int argc , char* argv[]){ 
	putenv("LD_LIBRARY_PATH=/system/lib:/vendor/lib");
	putenv("_LD_LIBRARY_PATH=/system/lib:/vendor/lib");
	execl("/data/local/tmp/libsu.so" , "libsu.so" ,"-server" , "aa" ,"/data/local/tmp/auth.jar" ,(char *)NULL);
	PLOGE("execl failed");
	return 0;
}
