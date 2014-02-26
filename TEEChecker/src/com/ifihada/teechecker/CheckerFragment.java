package com.ifihada.teechecker;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class CheckerFragment extends PreferenceFragment
{
  protected void result(String key, boolean checked, String summary)
  {
    CheckBoxPreference cbp = ((CheckBoxPreference) findPreference(key));
    cbp.setChecked(checked);
    cbp.setSummary(summary != null ? summary : "");
    CheckerResults.registerResult(key, checked);
  }
  
  protected void result(String key, String result)
  {
    EditTextPreference etp = ((EditTextPreference) findPreference(key));
    etp.setSummary(result != null ? result : "");
    CheckerResults.registerResult(key, result);
  }
  
  protected void notAvailable(String key)
  {
    findPreference(key).setEnabled(false);
  }
}
