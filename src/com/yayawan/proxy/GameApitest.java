package com.yayawan.proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.sdk.utils.AppInfo;
import com.yayawan.utils.FileIOUtils;
import com.yayawan.utils.FileUtils;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.Yayalog;



public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;
	public static Context mContext;

	public static String TestFilePath = "test";
	

	public static HashMap<String, String> mTestdatas = new HashMap<String, String>();
	
	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "GameTest"
			+ File.separator
			+ TestFilePath + ".txt";

	public static GameApitest getGameApitestInstants(Context mcontext) {
		mContext=mcontext;
		if (mGameapitest != null) {
			return mGameapitest;
		} else {
			mGameapitest = new GameApitest();
			return mGameapitest;
		}
	}

	public static GameApitest getGameApitestInstants(Activity mactivity) {
		
		if (mGameapitest != null) {
				return mGameapitest;
		} else {
			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}

	public static GameApitest getGameApitestInstants() {
		
		if (mGameapitest != null) {
			// mActivity = mactivity;

			return mGameapitest;
		} else {
			// mActivity = mactivity;

			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}

	public static String tempstring = "";
	
	public static String appPackageName = "";

	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

		
		
		if (!YYcontants.ISDEBUG) {
			Yayalog.loger("测试："+"不是调试模式，不能粗存调试状态");
			return;
		}else {
			
		}
		
		if (type.contains("Application")) {
			appPackageName = getPackageName(mContext);
			Sputils.putSPstring(appPackageName, "", mContext);
			
		}
		if (tempstring.contains(type)) {
			return;
		}
		tempstring = tempstring + "—temp-" + type;

		mTestdatas.put(type, "true");
		
		//	FileIOUtils.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

		if (mContext!=null) {
			
			if (!TextUtils.isEmpty(appPackageName)) {
				Yayalog.loger("测试write："+tempstring+"nowtype:"+type);
				
				//Sputils.putSPstring(appPackageName, tempstring, mContext);
				
				
			}else {
				Yayalog.loger("Application 未接入"+"");
			}
			
		}else {
			Yayalog.loger("Application 未接入"+"");
		}
			
		

	}

	public void sendTest(String type, String value) {

		
		mTestdatas.put(type, value);

	}
	
	
	
    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 过滤自定义的App和已下载的App
     * @param packageManager
     * @return
     */
    public static boolean isContentYaboxApp(PackageManager packageManager) {
        List<AppInfo> myAppInfos = new ArrayList<AppInfo>();
        List<AppInfo> mFilterApps = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤指定的app
                String tempPackageName=packageInfo.packageName;
               if (tempPackageName.contains("qihoo360.mobilesafe")) {
				return true;
			}
            }
           
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }

	public void setTestRoleData(String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,String token,String uid,String type) {
		// TODO Auto-generated method stub
	
		String rolestring= "roleId:"+roleId+" roleName:"+ roleName +" roleLevel:"+  roleLevel+" zoneId:"+zoneId  +" zoneName:"+  zoneName+" token:"+ token+" uid:"+ uid+" type:"+ type;
		
		mTestdatas.put("roleinfo"+type, rolestring);
		tempstring = tempstring + "—temp-" + type;
	}

}
