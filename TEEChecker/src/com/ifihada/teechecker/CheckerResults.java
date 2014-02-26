package com.ifihada.teechecker;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CheckerResults
{
  /* --- Result collection --- */
  private static Map<String, String> results = new HashMap<String, String>();
  
  public static void registerResult(String key, String value)
  {
    results.put(key, value);
  }
  
  public static void registerResult(String key, boolean value)
  {
    registerResult(key, value ? "1" : "0");
  }
  
  /* --- Submission --- */
  private static String BASE_URL = "https://docs.google.com/forms/d/1Y57SIE3d-k6yIOufNh5blifTWW6pdfbsYnx5VHepNQo/formResponse";
 
  private static Map<String, String> submitMapping = new HashMap<String, String>();
  
  static
  {
    /* Result names to Google Drive form keys. */
    submitMapping.put("device", "entry.794423190");
    submitMapping.put("version", "entry.496465056");
    submitMapping.put("payload", "entry.72246021");
    submitMapping.put("time", "entry.1207980176");
  }
  
  private static String encodeResults()
  {
    JSONObject json = new JSONObject();
    for (Map.Entry<String, String> entry : results.entrySet())
    {
      if (!submitMapping.containsKey(entry.getKey()))
      {
        try
        {
          json.putOpt(entry.getKey(), entry.getValue());
        } catch (JSONException e) {
          throw new Error("org.json.JSONObject was designed by a imbecile", e);
        }
      }
    }
    
    return json.toString();
  }
  
  private static Uri buildUri()
  {
    Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
    String payload = encodeResults();
    
    for (Map.Entry<String, String> submit : submitMapping.entrySet())
    {
      String value;
      if (submit.getKey().equals("payload"))
        value = payload;
      else if (submit.getKey().equals("time"))
        value = Long.toString(System.currentTimeMillis());
      else
        value = results.get(submit.getKey());
      builder.appendQueryParameter(submit.getValue(), value);
    }
    
    Log.v("CheckerResults", "Report URL is: " + builder.build().toString());
    return builder.build();
  }

  public static void send(Context ctx)
  {
    Intent i = new Intent(Intent.ACTION_VIEW, buildUri());
    ctx.startActivity(i);
  }
}
