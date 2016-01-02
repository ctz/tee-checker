package com.ifihada.teechecker;

public class MobiCore
{
  public static String getVersion()
  {
    System.loadLibrary("mobicoreversion");
    return native_getVersion();
  }

  public static native String native_getVersion();
}
