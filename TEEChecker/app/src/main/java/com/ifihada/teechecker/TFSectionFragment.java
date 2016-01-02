package com.ifihada.teechecker;

import java.io.File;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class TFSectionFragment extends CheckerFragment
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
    String tfAvailable = checkTFAvailable();
    String sstAvailable = checkSSTAvailable();

    result("tf-present",
           tfDetectResult != null,
           tfDetectResult);
    result("tf-available",
           tfAvailable != null,
           tfAvailable);
    result("tf-sst-available",
           sstAvailable != null,
           sstAvailable);

    if (tfDetectResult == null)
    {
      notAvailable("tf-available");
      notAvailable("tf-sst-available");
    }
  }

  private String checkTFAvailable()
  {
    File f = new File("/dev/tf_driver");
    if (f.canRead() && f.canWrite())
      return "/dev/tf_driver is read-write for us";
    else
      return null;
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
    } catch (Throwable e)
    {
      Log.e(TAG, "cannot find tf_crypto_sst lib", e);
      return null;
    }
  }
}
