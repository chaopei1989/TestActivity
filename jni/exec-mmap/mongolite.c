#include "mongolite.h"
#include "help.h"
#include <sys/mman.h>

#include <unistd.h>
#include <fcntl.h>

static int checkDB(char* pck){
	char path[30];
	memset(path, 0, sizeof(path));
	sprintf(path, "/data/data/%s/files/mongo.db", pck);
	if (0 != access(path, F_OK)) {
		LOGD("file not found");
		return -1;
	}
}

void init(){
	if (-1 == checkDB("com.zero")) {
		mongo_stat ms;
		ms.pid = -1;
		ms.uid = -1;
		ms.rw = -1;
	}
}

