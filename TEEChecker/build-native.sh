#!/bin/bash
set -e

NDK_ROOT=${1:-../../android-ndk-r9c}

if [ ! -e "$NDK_ROOT/ndk-build" ] ; then
		echo "cannot find NDK (at $NDK_ROOT)"
		echo "usage: $0 <android-ndk-root>"
		exit 1
fi

$NDK_ROOT/ndk-build

for arch in armeabi ; do
  # don't ship this, we want to find it from the device
  rm -v libs/$arch/libMcClient.so
  mv -v libs/$arch/libmobicoreversion.so app/src/main/jniLibs/$arch/
done

