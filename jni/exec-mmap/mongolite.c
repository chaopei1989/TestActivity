#include "mongolite.h"
#include "help.h"
#include <sys/mman.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

char path[30];

static int checkDB(char* pck){
	memset(path, 0, sizeof(path));
	sprintf(path, "/data/data/%s/files/mongo.db", pck);
	if (0 != access(path, F_OK)) {
		return -1;
	}
}

void init(){
	int fd;
	if (-1 == checkDB("com.zero")) {
		LOGD("file(%s) not found, create", path);
	}
	mongo_stat* ms;
	if (-1 == (fd = open(path, O_WRONLY|O_CREAT|O_TRUNC, 0666))) {
		// 打开/创建失败
		LOGERRNO("file open failed");
		exit(100);
	}
	ms = (mongo_stat*) mmap(NULL, sizeof(mongo_stat), PROT_READ|PROT_WRITE, MAP_SHARED, fd, 0);
	if (MAP_FAILED == ms) {
		LOGERRNO("mmap failed");
		exit(101);
	}
	LOGD("uid=%d, pid=%d, rw=%d",ms->uid, ms->pid, ms->rw);
	ms->uid = ms->pid = ms->rw = 8;
	msync(NULL, sizeof(mongo_stat), MS_SYNC);
	munmap(ms,sizeof(mongo_stat));
	close(fd);
}
