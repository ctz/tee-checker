package com.ifihada.teechecker;

import java.io.File;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class TFSectionFragment extends PreferenceFragment
{
  private static final String TAG = "TFSectionFragment";

  @Override
  public void onCreate(Bundle saved)
  {
    super.onCreate(saved);
    addPreferencesFromResource(R.xml.tf_prefs);
    fill();
  }

  public void fill()
  {
    String tfDetectResult = detectTF();
    boolean tfAvailable = checkTFAvailable();
    String sstAvailable = checkSSTAvailable();
    
    CheckBoxPreference tfSupp = ((CheckBoxPreference) findPreference("tf-present"));
    CheckBoxPreference tfAvail = ((CheckBoxPreference) findPreference("tf-available"));
    CheckBoxPreference sstAvail = ((CheckBoxPreference) findPreference("tf-sst-available"));
    
    tfSupp.setChecked(tfDetectResult != null);
    tfSupp.setSummary(tfDetectResult != null ? tfDetectResult : "");
    
    if (tfDetectResult == null)
    {
      tfAvail.setEnabled(false);
      sstAvail.setEnabled(false);
    } else {
      tfAvail.setChecked(tfAvailable);
      sstAvail.setChecked(sstAvailable != null);
      if (sstAvailable != null)
        sstAvail.setSummary(sstAvailable);
    }
  }
  
  private boolean checkTFAvailable()
  {
    File f = new File("/dev/tf_driver");
    return f.canRead() && f.canWrite();
  }
  
  private String detectTF()
  {
    StringBuffer sb = new StringBuffer();
    
    if (new File("/dev/tf_driver").exists())
      sb.append("/dev/tf_driver exists");
    
    if (sb.length() > 0)
      return sb.toString();
    else
      return null;
  }

  private String checkSSTAvailable()
  {
    try
    {
      System.loadLibrary("tf_crypto_sst");
      return "libtf_crypto_sst.so found";
    } catch (Throwable e) {
      Log.e(TAG, "cannot find tf_crypto_sst lib", e);
      return null;
    }
  }
}
