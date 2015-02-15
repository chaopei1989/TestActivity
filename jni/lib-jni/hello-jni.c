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

#include "extra.h"
#include "apue.h"
#include "com_zero_test_jni_HelloJni.h"
#include <android/log.h>
#include <unistd.h>
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
jstring
Java_com_zero_test_jni_HelloJni_nativeStringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #define ABI "armeabi-v7a/NEON"
    #else
      #define ABI "armeabi-v7a"
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__mips__)
   #define ABI "mips"
#else
   #define ABI "unknown"
#endif

    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}

jstring
Java_com_zero_test_jni_HelloJni_nativeMyFirstNDKfunction(JNIEnv *env, jobject thiz){
	if(4 == testMyFunction("asdf")){
		LOGE("JNI_日志 Java_com_zero_test_jni_HelloJni_myFirstNDKfunction");
		return (*env)->NewStringUTF(env, "MyFirstNDKfunction == 4");
	} else{
		LOGE("JNI_日志 Java_com_zero_test_jni_HelloJni_myFirstNDKfunction");
		return (*env)->NewStringUTF(env, "MyFirstNDKfunction != 4");
	}
}

void
Java_com_zero_test_jni_HelloJni_printls(JNIEnv *env, jobject thiz, jstring path){
	DIR *dp;
	struct dirent *dirp;

	if(NULL == (dp = opendir("/")))
	{
		LOGE("can't open root path");
	}
	while(NULL != (dirp = readdir(dp)))
	{
		LOGD("%s",dirp->d_name);
	}
	closedir(dp);
}

void
Java_com_zero_test_jni_HelloJni_nativeGetPid
  (JNIEnv *env, jobject thiz){
	LOGD("%d",getpid());
}

jint
Java_com_zero_test_jni_HelloJni_nativeGetArgMax
  (JNIEnv *env, jobject thiz){
	LOGD("ARG_MAX=%d (exec函数的参数最大长度(字节数))",sysconf(_SC_ARG_MAX));
	LOGD("ATEXIT=%d (可用atexit函数登记的最大函数个数)",sysconf(_SC_ATEXIT_MAX));
	LOGD("CHILD_MAX=%d (每个实际用户ID的最大进程数)",sysconf(_SC_CHILD_MAX));
	LOGD("clock ticks/second=%d (每秒时钟滴答数)",sysconf(_SC_CLK_TCK));
	LOGD("COLL_WEIGHTS_MAX=%d (在本地定义文件中可以赋予LC_COLLATE顺序关键字项的最大权重数)",sysconf(_SC_COLL_WEIGHTS_MAX));
//	LOGD("HOST_NAME_MAX=%d (gethostname函数返回的主机名最大长度)",sysconf(_SC_HOST_NAME_MAX));
	LOGD("IOV_MAX=%d (readv或writev函数可以使用的iovec结构的最大数)",sysconf(_SC_IOV_MAX));
	LOGD("ARG_MAX=%d (exec函数的参数最大长度(字节数))",sysconf(_SC_ARG_MAX));
	LOGD("ARG_MAX=%d (exec函数的参数最大长度(字节数))",sysconf(_SC_ARG_MAX));
	LOGD("ARG_MAX=%d (exec函数的参数最大长度(字节数))",sysconf(_SC_ARG_MAX));
	LOGD("ARG_MAX=%d (exec函数的参数最大长度(字节数))",sysconf(_SC_ARG_MAX));
	LOGD("ARG_MAX=%d (exec函数的参数最大长度(字节数))",sysconf(_SC_ARG_MAX));

	return 0;
}
