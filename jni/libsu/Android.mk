LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
TOOLS := \
     cmds/mount \
     cmds/kill \
     cmds/rm \
     cmds/chown \
     cmds/chmod \
     cmds/mv \
	 cmds/dd \
	 cmds/ls \
	 cmds/cat \
	 cmds/ps \
     cmds/directsyscall \
     cmds/chattr \

LOCAL_SRC_FILES:= \
     cmds/toolbox.c \
     cmds/e2fs_lib.c \
     $(patsubst %,%.c,$(TOOLS))

LOCAL_MODULE :=libsu 
LOCAL_SRC_FILES +=su.c suutil.c data.c
LOCAL_LDLIBS := -llog 

#LOCAL_CFLAGS := -std=c99
#打开DEBUG开关会导致日志输出
#LOCAL_CFLAGS+= -DDEBUG 
#LOCAL_CPPFLAGS+= -DDEBUG 

LOCAL_SHARED_LIBRARIES := \
	liblog 

LOCAL_MODULE_PATH := $(TARGET_OUT_OPTIONAL_EXECUTABLES)
LOCAL_MODULE_TAGS := debug,eng
LOCAL_PRELINK_MODULE := false

#LOCAL_C_INCLUDES+=/home/charles/android4.0/ics-org/system/core/include
LOCAL_C_INCLUDES += /home/ylee/android-4.0.4_r2.1/system/core/include

include $(BUILD_EXECUTABLE)
#include $(BUILD_SHARED_LIBRARY)
#continue to compile rt_daemon
include $(CLEAR_VARS)

LOCAL_SRC_FILES:=rtdaemon.c suutil.c data.c
LOCAL_SHARED_LIBRARIES:=libcutils 

LOCAL_LDLIBS := -llog

LOCAL_CFLAGS+= -DDEBUG -DRTDAEMON
LOCAL_CPPFLAGS+= -DDEBUG -DRTDAEMON

#LOCAL_C_INCLUDES:= \
		$(call include-path-for,system-core)/cutils
LOCAL_C_INCLUDES+=/home/ylee/android-4.0.4_r2.1/system/core/include
LOCAL_MODULE:=rtdaemon
include $(BUILD_EXECUTABLE)

