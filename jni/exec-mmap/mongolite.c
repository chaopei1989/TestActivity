#include "mongolite.h"
#include "help.h"
#include <sys/mman.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

char path[30];

static int checkDB(char* pck) {
	memset(path, 0, sizeof(path));
	sprintf(path, "/data/data/%s/files/mongo.db", pck);
	LOGD("access file(%s)", path);
	if (0 != access(path, R_OK|W_OK)) {
		LOGERRNO("access file");
		return -1;
	} else {
		LOGD("access file success");
	}
}

void init() {
	int fd;
	checkDB("com.zero");
	mongo_stat* ms;
	if (-1 == (fd = open(path, O_RDWR, 0666))) {
		// 打开/创建失败
		LOGERRNO("file open failed");
		exit(100);
	} else {
		LOGD("file open success");
	}
	ms = (mongo_stat*) mmap(NULL, 1024 * 1024, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
	if (MAP_FAILED == ms) {
		LOGERRNO("mmap failed");
		exit(101);
	} else {
		LOGD("mmap success");
	}
	LOGD("uid=%d, pid=%d, rw=%d", ms->uid, ms->pid, ms->rw);
	ms->uid = ms->pid = ms->rw = 8;
	msync(NULL, sizeof(mongo_stat), MS_SYNC);
	munmap(ms, sizeof(mongo_stat));
	close(fd);
}
