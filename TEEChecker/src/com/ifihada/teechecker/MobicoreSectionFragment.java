package com.ifihada.teechecker;

import java.io.File;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

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
    
    if (new File("/dev/mobicore").exists())
      sb.append("/dev/mobicore exists");
    
    if (new File("/dev/mobicore-user").exists())
    {
      if (sb.length() > 0)
        sb.append(" and ");
      sb.append("/dev/mobicore-user exists");
    }
    
    if (sb.length() > 0)
      return sb.toString();
    else
      return null;
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
      return "libMcClient.so present";
    } catch (UnsatisfiedLinkError e) {
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
