/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#include <android/log.h>
#include "apue.h"
#define LOG_TAG "Native"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */

void exec_std() {
	char buf[] = "ls\n";
	pid_t pid;
	int status;
	LOGD("%% start");
	if ('\n' == buf[strlen(buf) - 1]) { //如果最后一个字符为\n时，换为null(0), 由于execlp接收的参数末尾必须为null字符
		buf[strlen(buf) - 1] = 0;
	}
	if (0 > (pid = fork())) { // fork失败
		LOGE("fork error");
	} else if (0 == pid) { // 子进程
		LOGD("I'm child, pid = %d, parent pid = %d", getpid(), getppid());
		execlp(buf, buf, (char*) 0); //????
		exit(127);
	}

	// 父进程
	if (0 > (pid = waitpid(pid, &status, 0))) {
		LOGE("waitpid error");
	}
	LOGD("%% end");
}

int main() {
//	while(1)
//	{
//	exec_std();
		LOGD("DEAMON Checking my pid = %d, parent pid = %d", getpid(), getppid());
		sleep(5);
//	}
	return 0;
}
