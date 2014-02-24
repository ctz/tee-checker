LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := mcclient
LOCAL_SRC_FILES := libMcClient.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := mobicoreversion
LOCAL_SRC_FILES := check.c
LOCAL_SHARED_LIBRARIES := mcclient

include $(BUILD_SHARED_LIBRARY)

