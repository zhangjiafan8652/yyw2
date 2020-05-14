package com.yayawan.sdk.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;
import java.io.File;

public class FileManager
{
  private Context context;
  
  public FileManager(Context paramContext)
  {
    this.context = paramContext;
  }
  
  private void install(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return;
    }
    if (!new File(paramString).exists()) {
      return;
    }
    this.install1(paramString);
  }
  public  void install1(String file){
  	File file2 = new File(file);
  }
//安装应用
  private  void installApk2(File apk) {
  	
      Intent intent = new Intent(Intent.ACTION_VIEW);
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
          intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
      } else {//Android7.0之后获取uri要用contentProvider
          Uri uri = getUriFromFile(context, apk);
          intent.setDataAndType(uri, "application/vnd.android.package-archive");
          intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      }

      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
  }
  public static Uri getUriFromFile(Context context, File file) {
      Uri imageUri;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          imageUri = DgameFileProvider.getUriForFile(context,
                  "yyw.yayabox.com.yayabox.fileprovider", file);//通过FileProvider创建一个content类型的Uri
      } else {
          imageUri = Uri.fromFile(file);
      }
      return imageUri;
  }
  
  public String getExtension(String paramString)
  {
    int i = paramString.lastIndexOf(".");
    if ((i != -1) && (i != paramString.length() - 1)) {
      return paramString.substring(i + 1).toLowerCase();
    }
    return "unknown";
  }
  
  public boolean isApk(String paramString)
  {
    return "apk".equals(paramString);
  }
  
  public boolean isMusic(String paramString)
  {
    boolean bool;
    if ((!"mp3".equals(paramString)) && (!"flac".equals(paramString)) && (!"aac".equals(paramString)) && (!"amr".equals(paramString)) && (!"wav".equals(paramString)) && (!"ogg".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isRar(String paramString)
  {
    return "rar".equals(paramString);
  }
  
  public boolean isVideo(String paramString)
  {
    boolean bool;
    if ((!"mp4".equals(paramString)) && (!"avi".equals(paramString)) && (!"3gp".equals(paramString)) && (!"wmv".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isZip(String paramString)
  {
    boolean bool;
    if ((!"zip".equals(paramString)) && (!"gz".equals(paramString)) && (!"7z".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void open(String paramString1, String paramString2)
  {
    int i = paramString2.lastIndexOf(".");
    if ((i != -1) && (i != paramString2.length() - 1))
    {
      paramString1 = paramString2.substring(i + 1).toLowerCase();
      if (isApk(paramString1))
      {
        install(paramString2);
      }
     
      
      return;
    }
  }
}
