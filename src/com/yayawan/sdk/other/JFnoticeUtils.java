package com.yayawan.sdk.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.yayawan.sdk.login.Announcement_dialog;
import com.yayawan.sdk.login.StringConstants;
import com.yayawan.sdk.login.ViewConstants;
import com.yayawan.sdk.utils.AppInfo;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Handle;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.Yayalog;
import com.yayawan.utils.Yibuhttputils;


public class JFnoticeUtils {
	
	 String data;

	
	/**
	 * 获取公告信息，如果有公告则弹出公告，如果无公告就啥事都没有，一定要在主线程运行
	 * 
	 * @param mActicity
	 */
	public void getNotice(final Activity mActicity) {

		
		  
				
			
			Yayalog.loger("获取公告：" + ViewConstants.NOTICEURL);
			try {
				Yibuhttputils yibuhttputils = new Yibuhttputils() {

					@Override
					public void sucee(final String responseInfo) {
						// TODO Auto-generated method stub
						Yayalog.loger("获取公告返回：" + responseInfo);
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(responseInfo);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						 data = jsonObject.optString("data");
						if (responseInfo.contains("kefu")) {
							if (isContentYaboxApp(mActicity.getPackageManager())) {
								Yayalog.loger("安装过丫丫玩盒子：");
								return;
							}else {
								
								if(Sputils.getSPint(ViewConstants.SP_ISVIEWYAYAWANDOWNLOADBOXNOTICE, 1,mActicity)==1){
									
								
									
									 TimerTask task = new TimerTask() {
								            @Override
								            public void run() {
								              /**
								               *要执行的操作
								               */
								            	mActicity.runOnUiThread(new Runnable() {

													@Override
													public void run() {
														// TODO Auto-generated method stub
														new Announcement_dialog(mActicity,
																data).dialogShow();
														
													}
												});
								            }
								        };
								        Timer timer = new Timer();
								        timer.schedule(task, 180*1000);//3秒后执行TimeTask的run方法
							
									
								}
							}
						}else {
							if (responseInfo.contains("success")) {
								try {
									JSONObject jsonObject1 = new JSONObject(responseInfo);
									 data = jsonObject1.optString("data");

									mActicity.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											new Announcement_dialog(mActicity,
													data).dialogShow();
										}
									});

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Yayalog.loger("获取公告失败：" + e);
								}
							}
							return;
						}
						// TODO Auto-generated method stub
					

					}

					@Override
					public void faile(String err, String rescode) {
						// TODO Auto-generated method stub
						Yayalog.loger("获取公告失败：" + err);
					}
				};
				String pingjie = "app_id=" + DeviceUtil.getAppid(mActicity);
				Yayalog.loger(ViewConstants.NOTICEURL + "/?" + pingjie);
				yibuhttputils.runHttp(ViewConstants.NOTICEURL + "/?" + pingjie, "",
						Yibuhttputils.GETMETHOD, "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Yayalog.loger("获取公告发生异常：" + e.toString());
				// e.printStackTrace();
			}
			
		
		

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
               if (tempPackageName.contains("yayabox")) {
				return true;
			}
            }
           
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }
}
