package com.ifihada.teechecker;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

public class TLKSectionFragment extends CheckerFragment
{
  private static final String TAG = "TLKSectionFragment";

  @Override
  public void onCreate(Bundle saved)
  {
    super.onCreate(saved);
    addPreferencesFromResource(R.xml.tlk_prefs);
    fill();
  }

  public void fill()
  {
    String tlkDetectResult = detectTLK();
    String tlkAvailable = checkTLKAvailable();

    result("tlk-present",
           tlkDetectResult != null,
           tlkDetectResult);
    result("tlk-available",
           tlkAvailable != null,
           tlkAvailable);

    if (tlkDetectResult == null)
    {
      notAvailable("tlk-available");
    }
  }

  /* On most devices I've seen, /dev is not executable (ie listable)
   * by normal users or apps.  However, open(2)-ing files which aren't
   * there fails with ENOENT which gives away whether a file exists.
   */
  private boolean fileProbablyExists(File f)
  {
    FileInputStream fis = null;
    try
    {
      fis = new FileInputStream(f);
      fis.close();
      return true;
    } catch (FileNotFoundException e)
    {
      Log.e(TAG, "cannot open file: " + e.toString());
      return false;
    } catch (IOException e)
    {
      Log.e(TAG, "opened file but failed after: " + e.toString());
      return true;
    }
  }

  private String checkTLKAvailable()
  {
    File f = new File("/dev/tlk_device");
    if (f.canRead() && f.canWrite())
      return "/dev/tlk_device is read-write for us";
    else
      return null;
  }

  private String detectTLK()
  {
    StringBuffer sb = new StringBuffer();

    if (new File("/dev/tlk_device").exists())
      sb.append("/dev/tlk_device exists");

    if (sb.length() > 0)
      return sb.toString();
    else
      return null;
  }
}
