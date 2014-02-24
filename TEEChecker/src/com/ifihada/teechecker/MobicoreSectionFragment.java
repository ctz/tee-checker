package com.ifihada.teechecker;

import java.io.File;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class MobicoreSectionFragment extends PreferenceFragment
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
    CheckBoxPreference mcSupp = ((CheckBoxPreference) findPreference("mobicore-present"));
    CheckBoxPreference mcAvail = ((CheckBoxPreference) findPreference("mobicore-available"));
    CheckBoxPreference mcClient = ((CheckBoxPreference) findPreference("mobicore-client"));
    EditTextPreference mcVersion = ((EditTextPreference) findPreference("mobicore-version"));
  
    String mobicoreDetectResult = detectMobicore();
    boolean hasClientLib = findClientLib();
    boolean mobicoreAvailable = checkMobicoreAvailable();
    String mobicoreVersion = getVersionInfo();
    
    mcSupp.setChecked(mobicoreDetectResult != null);
    mcSupp.setSummary(mobicoreDetectResult != null ? mobicoreDetectResult : "");
    
    if (mobicoreVersion != null)
      mcVersion.setSummary(mobicoreVersion);
    
    if (mobicoreDetectResult == null)
    {
      mcAvail.setEnabled(false);
      mcClient.setEnabled(false);
	  mcVersion.setEnabled(false);
    } else {
      mcAvail.setChecked(mobicoreAvailable);
      mcClient.setChecked(hasClientLib);
      
      if (!hasClientLib)
        mcVersion.setEnabled(false);
    }
    
    mcClient.setChecked(hasClientLib);
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
  
  private boolean checkMobicoreAvailable()
  {
    File f = new File("/dev/mobicore");
    if (f.canRead() && f.canWrite())
      return true;
    f = new File("/dev/mobicore-user");
    if (f.canRead() && f.canWrite())
      return true;
    return false;    
  }
  
  private boolean findClientLib()
  {
    try
    {
      System.loadLibrary("McClient");
      return true;
    } catch (UnsatisfiedLinkError e) {
      Log.e(TAG, "cannot find McClient", e);
      return false;
    }
  }
  
  private String getVersionInfo()
  {
    if (findClientLib())
      return MobiCore.getVersion();
    else
      return null;
  }
}
