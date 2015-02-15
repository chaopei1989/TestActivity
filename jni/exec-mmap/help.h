#include <android/log.h>
#include <errno.h>
#ifndef _HELP_H_
#define _HELP_H_

#define LOG_TAG "Native"
#define LOGERRNO(str) LOGE("%s, errno=%d, %s", str, errno, strerror(errno))
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

#endif
