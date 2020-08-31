package com.yayawan.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.common.CommonData;
import com.yayawan.main.YYWMain;

/**
 * @作者: zhangjiafan
 * 
 */

public class CrashHandler implements UncaughtExceptionHandler {

    private Context mContext;
    private static CrashHandler mInstance;
    private UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private ExecutorService executors = Executors.newSingleThreadExecutor();
    private Map<String, String> mInfo = new HashMap<String, String>();
    private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

    /**
     * 私有构造方法
     */
    private CrashHandler() {

    }

    /**
     * 单例模式
     */
    public static CrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (CrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 1. 收集错误信息
     * 2. 保存错误信息
     * 3. 上传到服务器
     */
    @Override
    public void uncaughtException(Thread t, final Throwable e) {
        if (e == null) {
            // 未处理，调用系统默认的处理器处理
            if (defaultUncaughtExceptionHandler != null) {
                defaultUncaughtExceptionHandler.uncaughtException(t, e);
            }
        } else {
            // 人为处理异常
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "程序异常，请联系客服~~", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
            collectErrorInfo();
           // System.err.println(e.getMessage());
            saveErrorInfo(e);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
           // Process.killProcess(Process.myPid());
          //  System.exit(1);
        }
    }

    private void saveErrorInfo(Throwable e) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfo.entrySet()) {
            String keyName = entry.getKey();
            String value = entry.getValue();
            stringBuffer.append(keyName + "=" + value + "\n");
        }
        stringBuffer.append("\n-----Crash jiafan Log Begin-----\n");
        Log.e("jiafan err", "\n-----Crash jiafan Log Begin-----\n");
       
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(writer);
            cause = e.getCause();
        }
        writer.close();
        String string = stringWriter.toString();
        Log.e("jiafan err", string);
        stringBuffer.append(string);
        stringBuffer.append("\n-----Crash jiafan Log End-----");
       
        Log.e("jiafan err", "\n-----Crash jiafan Log End-----");
        String format = dateFormat.format(new Date());
      
        String fileName = "crash-" + format+""+getAppName(mContext) + ".log";
        
        
        sendCrashLogToService(mContext, stringBuffer.toString());//发送错误日志到服务端
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
     //   Yayalog.loger(stringBuffer.toString());
        
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        	//Context.getExternalCacheDir():// /storage/emulated/0/Android/data/com.learn.test/cache
            String path = mContext.getExternalCacheDir() + File.separator + "GameCrash";
            Yayalog.loger("崩溃日志路径："+path);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fou = null;
            try {
                fou = new FileOutputStream(new File(path, fileName));
                fou.write(stringBuffer.toString().getBytes());
                fou.flush();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (fou != null) {
                        fou.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送错误日志到服务端
     * @param mc
     * @param msg
     */
    public static void sendCrashLogToService(Context mc,String msg){
    	
    	HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		
	
		
		requestParams.addBodyParameter("game_id",
				DeviceUtil.getAppid(mc));
//		if (YYWMain.mUser.yywuid!=null) {
//			requestParams.addBodyParameter("uid", YYWMain.mUser.yywuid);
//		}else {
			requestParams.addBodyParameter("uid", "1");
	//	}
		//https://rest.yayawan.com/side/sdk_log_received/
		requestParams.addBodyParameter("packagename", mc.getPackageName());
		requestParams.addBodyParameter("ver_code",
				DeviceUtil.getVersionCode(mc));
		requestParams.addBodyParameter("ver_name",DeviceUtil.getVersionName(mc));
		requestParams.addBodyParameter("os","1");
		requestParams.addBodyParameter("note",msg);
		
		Yayalog.loger("开始上报错误："+ViewConstants.errmsgurl);
		httpUtils.send(HttpMethod.POST, ViewConstants.errmsgurl, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String result) {
						// TODO Auto-generated method stub
					Yayalog.loger("错误上报成功："+ result);
					//	System.out.println("错误上报成功：" + result.result);
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						// TODO Auto-generated method stub
						
						Yayalog.loger("错误上报成功：");

						

					}
				});

    }
    
    /**

     * 获取应用程序名称

     */

    public static synchronized String getAppName(Context context) {

        try {

            PackageManager packageManager = context.getPackageManager();

            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);

            int labelRes = packageInfo.applicationInfo.labelRes;

            return context.getResources().getString(labelRes);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "获取游戏名字失败";

    }
    private void collectErrorInfo() {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (info != null) {
                String versionName = TextUtils.isEmpty(info.versionName) ? "未设置版本名称" : info.versionName;
                String versionCode = info.versionCode + "";
                mInfo.put("versionName", versionName);
                mInfo.put("versionCode", versionCode);
            }
            // 获取 Build 类中所有的公共属性
            Field[] fields = Build.class.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    mInfo.put(field.getName(), field.get(null).toString());
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
