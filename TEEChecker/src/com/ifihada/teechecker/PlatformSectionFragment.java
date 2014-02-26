package com.ifihada.teechecker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.security.IKeystoreService;
import android.security.KeyChain;

public class PlatformSectionFragment extends CheckerFragment
{
  private static final String TAG = "PlatformSectionFragment";

  @Override
  public void onCreate(Bundle saved)
  {
    super.onCreate(saved);
    addPreferencesFromResource(R.xml.platform_prefs);
    fill();
  }

  public void fill()
  {
    result("device", String.format("%s %s (%s branded)", Build.MANUFACTURER, Build.MODEL, Build.BRAND));
    result("version", String.format("%s (%s)", Build.VERSION.RELEASE, Build.DISPLAY));
    
    result("keystore-supported", hasKeystoreApi(), "");
    result("keystore-keytypes", getUsableTypes());
    result("keystore-hw", hasKeystoreHardware(), "");
    result("keystore-hw-keytypes", getHardwareTypes());
  }

  private IKeystoreService getKeystore()
  {
    try
    {
      Class serviceMgrClass = Class.forName("android.os.ServiceManager");
      Method getService = serviceMgrClass.getMethod("getService", String.class);
      IBinder binder = (IBinder) getService.invoke(null, "android.security.keystore");
      IKeystoreService ks = IKeystoreService.Stub.asInterface(binder);
      return ks;
    } catch (Exception e)
    {
      Log.wtf(TAG, "hasKeystore reflection failed", e);
      return null;
    }
  }

  private static final String[] knownKeyTypes = new String[] { "RSA", "DSA", "EC", "ECDSA", "ECDH", "AES", "DES", "DES3" };

  private String join(List<String> strs)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strs.size(); i++)
    {
      sb.append(strs.get(i));
      if (i != strs.size() - 1)
        sb.append(", ");
    }
    return sb.toString();
  }

  private String getHardwareTypes()
  {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
      return "(unavailable)";

    ArrayList<String> usable = new ArrayList<String>();
    for (String kt : knownKeyTypes)
    {
      if (isKeyTypeHardwareBacked(kt))
        usable.add(kt);
    }
    
    if (usable.size() == 0)
      return "(none)";

    return join(usable);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  private String getUsableTypes()
  {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
      return "(unavailable)";

    ArrayList<String> usable = new ArrayList<String>();
    for (String kt : knownKeyTypes)
    {
      if (KeyChain.isKeyAlgorithmSupported(kt))
        usable.add(kt);
    }
    
    if (usable.size() == 0)
      return "(none)";

    return join(usable);
  }

  private boolean hasKeystoreApi()
  {
    IKeystoreService svc = getKeystore();

    if (svc == null)
      return false;

    try
    {
      return svc.test() != 0;
    } catch (RemoteException e)
    {
      Log.wtf(TAG, "test() failed", e);
    } catch (NoSuchMethodError e)
    {
      Log.wtf(TAG, "test() does not exist", e);
    }

    return false;
  }
  
  private boolean isKeyTypeHardwareBacked(String kt)
  {
    IKeystoreService svc = getKeystore();

    if (svc == null)
      return false;
    
    try
    {
      return svc.is_hardware_backed(kt) != 0;
    } catch (RemoteException e)
    {
      Log.wtf(TAG, "is_hardware_backed("+kt+") failed", e);
    } catch (NoSuchMethodError e)
    {
      Log.wtf(TAG, "is_hardware_backed("+kt+") does not exist", e);
    }

    return false;
  }

  private boolean hasKeystoreHardware()
  {
    IKeystoreService svc = getKeystore();

    if (svc == null)
      return false;

    try
    {
      return svc.is_hardware_backed() != 0;
    } catch (RemoteException e)
    {
      Log.wtf(TAG, "is_hardware_backed() failed", e);
    } catch (NoSuchMethodError e)
    {
      Log.wtf(TAG, "is_hardware_backed() does not exist", e);
    }
    
    for (String kt : knownKeyTypes)
      if (isKeyTypeHardwareBacked(kt))
        return true;
    
    return false;
  }
}
