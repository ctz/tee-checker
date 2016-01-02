package com.ifihada.teechecker;

import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MobicoreSectionFragment extends CheckerFragment
{
  private static final String TAG = "MobicoreSectionFragment";

  @Override
  public void onCreate(Bundle saved)
  {
    super.onCreate(saved);
    addPreferencesFromResource(R.xml.mobicore_prefs);
    fill();
  }

  public void fill()
  {
    String mobicoreDetectResult = detectMobicore();
    String hasClientLib = findClientLib();
    String mobicoreAvailable = checkMobicoreAvailable();
    String mobicoreVersion = getVersionInfo();

    result("mobicore-present",
           mobicoreDetectResult != null,
           mobicoreDetectResult);
    result("mobicore-available",
           mobicoreAvailable != null,
           mobicoreAvailable);
    result("mobicore-client",
           hasClientLib != null,
           hasClientLib);
    result("mobicore-version", mobicoreVersion);

    if (mobicoreDetectResult == null)
    {
      notAvailable("mobicore-available");
      notAvailable("mobicore-client");
      notAvailable("mobicore-version");
    }

    if (hasClientLib == null)
      notAvailable("mobicore-version");
  }

  private String detectMobicore()
  {
    StringBuffer sb = new StringBuffer();
    int count = 0;

    if (new File("/dev/mobicore").exists())
    {
      sb.append("/dev/mobicore");
      count++;
    }

    if (new File("/dev/mobicore-user").exists())
    {
      if (count != 0)
        sb.append(" and ");
      sb.append("/dev/mobicore-user");
      count++;
    }

    if (count == 0)
      return null;
    if (count == 1)
      return sb.toString() + " exists";
    else
      return sb.toString() + " exist";
  }

  private String checkMobicoreAvailable()
  {
    File f = new File("/dev/mobicore");
    if (f.canRead() && f.canWrite())
      return "/dev/mobicore is read-write for us";
    f = new File("/dev/mobicore-user");
    if (f.canRead() && f.canWrite())
      return "/dev/mobicore-user is read-write for us";
    return null;
  }

  private String findClientLib()
  {
    try
    {
      System.loadLibrary("McClient");
      if (checkMobicoreAvailable() == null)
        return "libMcClient.so present. That's strange!";
      else
        return "libMcClient.so present";
    } catch (UnsatisfiedLinkError e)
    {
      Log.e(TAG, "cannot find McClient", e);
      return null;
    }
  }

  private String getVersionInfo()
  {
    if (findClientLib() != null)
      return MobiCore.getVersion();
    else
      return null;
  }
}
