#!/bin/bash

SDK_ROOT=${1:-../../android-sdk-linux}
NDK_ROOT=${2:-../../android-ndk-r9c}

if [ ! -e "$NDK_ROOT/ndk-build" ] ; then
		echo "cannot find SDK (at $SDK_ROOT) and NDK (at $NDK_ROOT)"
		echo "usage: $0 <android-sdk-root> <android-ndk-root>"
		exit 1
fi

$NDK_ROOT/ndk-build

# don't ship this, we want to find it from the device
rm libs/*/libMcClient.so

$SDK_ROOT/tools/android update project --name TEEChecker --target android-19 --path . --subprojects
ant debug
