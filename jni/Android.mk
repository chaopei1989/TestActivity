# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := deamon
LOCAL_CFLAGS += -fPIE
LOCAL_LDFLAGS += -fPIE -pie
LOCAL_SRC_FILES := exec-jni/deamon.c exec-jni/apue.c
LOCAL_LDLIBS += -llog
LOCAL_FORCE_STATIC_EXECUTABLE := true
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := mongo
LOCAL_CFLAGS += -fPIE
LOCAL_LDFLAGS += -fPIE -pie
LOCAL_SRC_FILES := exec-mmap/mmain.c exec-mmap/mongolite.c
LOCAL_LDLIBS += -llog
LOCAL_FORCE_STATIC_EXECUTABLE := true
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := inject   
LOCAL_SRC_FILES := inject-jni/inject.c
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog  
LOCAL_FORCE_STATIC_EXECUTABLE := true
TARGET_CFLAGS += -mthumb-interwork
LOCAL_CFLAGS := -DHAVE_NEON=1
LOCAL_ARM_NEON  := true
LOCAL_SRC_FILES += inject-jni/shellcode.s.neon
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := test
LOCAL_SRC_FILES := lib-jni/hello-jni.c lib-jni/extra.c
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)
  
include $(CLEAR_VARS)  
LOCAL_LDLIBS += -llog   
LOCAL_MODULE    := injectso  
LOCAL_SRC_FILES := injectso-jni/hello.c  
include $(BUILD_SHARED_LIBRARY)  


include $(CLEAR_VARS)
TOOLS := \
     libsu/cmds/mount \
     libsu/cmds/kill \
     libsu/cmds/rm \
     libsu/cmds/chown \
     libsu/cmds/chmod \
     libsu/cmds/mv \
	 libsu/cmds/dd \
	 libsu/cmds/ls \
	 libsu/cmds/cat \
	 libsu/cmds/ps \
     libsu/cmds/directsyscall \
     libsu/cmds/chattr \

LOCAL_SRC_FILES:= \
     libsu/cmds/toolbox.c \
     libsu/cmds/e2fs_lib.c \
     $(patsubst %,%.c,$(TOOLS))

LOCAL_MODULE :=libsu 
LOCAL_SRC_FILES +=libsu/su.c libsu/suutil.c libsu/data.c
LOCAL_LDLIBS := -llog 

#LOCAL_CFLAGS := -std=c99
#打开DEBUG开关会导致日志输出
LOCAL_CFLAGS+= -DDEBUG 
LOCAL_CPPFLAGS+= -DDEBUG 

LOCAL_SHARED_LIBRARIES := \
	liblog 

LOCAL_MODULE_PATH := $(TARGET_OUT_OPTIONAL_EXECUTABLES)
LOCAL_MODULE_TAGS := debug,eng
LOCAL_PRELINK_MODULE := false
include $(BUILD_EXECUTABLE)





ifeq ($(TARGET_BUILD_APPS),)

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	aidl_language_l.l \
	aidl_language_y.y \
	aidl.cpp \
	aidl_language.cpp \
	options.cpp \
	search_path.cpp \
	AST.cpp \
	Type.cpp \
	generate_java.cpp \
	generate_java_binder.cpp \
	generate_java_rpc.cpp

LOCAL_CFLAGS := -g
LOCAL_MODULE := aidl

include $(BUILD_HOST_EXECUTABLE)
endif # TARGET_BUILD_APPS
