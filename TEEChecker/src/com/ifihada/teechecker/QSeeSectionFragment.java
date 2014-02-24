package com.ifihada.teechecker;

import java.io.File;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

public class QSeeSectionFragment extends PreferenceFragment
{
  private static final String TAG = "QSEESectionFragment";

  @Override
  public void onCreate(Bundle saved)
  {
    super.onCreate(saved);
    addPreferencesFromResource(R.xml.qsee_prefs);
    fill();
  }

  public void fill()
  {
    String qseeDetectResult = detectQSEE();
    boolean qseeAvailable = checkQSEEAvailable();
    
    CheckBoxPreference qsSupp = ((CheckBoxPreference) findPreference("qsee-present"));
    
    qsSupp.setChecked(qseeDetectResult != null);
    qsSupp.setSummary(qseeDetectResult != null ? qseeDetectResult : "");
    
    CheckBoxPreference qsAvail = ((CheckBoxPreference) findPreference("qsee-available"));
    if (qseeDetectResult == null)
      qsAvail.setEnabled(false);
    else
      qsAvail.setChecked(qseeAvailable);
      
  }
  
  private boolean checkQSEEAvailable()
  {
    File f = new File("/dev/qseecom");
    return f.canRead() && f.canWrite();
  }
  
  private String detectQSEE()
  {
    StringBuffer sb = new StringBuffer();
    
    if (new File("/dev/qseecom").exists())
      sb.append("/dev/qseecom exists");
    
    if (sb.length() > 0)
      return sb.toString();
    else
      return null;
  }
}
