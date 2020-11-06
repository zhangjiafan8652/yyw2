package com.yayawan.impl;





import com.bun.miitmdid.core.JLibrary;
import com.lidroid.jxutils.http.Jxutilsinit;

import com.yayawan.proxy.GameApitest;



import com.yayawan.proxy.YYWApplication;
import com.yayawan.proxy.YYcontants;
import com.yayawan.sdk.utils.Util;
import com.yayawan.utils.CrashHandler;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.PermissionUtils;

import android.Manifest;

import android.content.Context;

public class YYApplication extends YYWApplication {

	public static Context mContext;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		YYcontants.ISDEBUG=DeviceUtil.isDebug(this);
		
		CrashHandler.getInstance().init(this);
		//初始化千骐千果sdk
				
		//CommonData.initCommonData(getApplicationContext());
		mContext = getApplicationContext();
	
			
		if (PermissionUtils.checkPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)) {
			Jxutilsinit.init(getApplicationContext());
		}
		
		try {
			JLibrary.InitEntry(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GameApitest.getGameApitestInstants(getApplicationContext()).sendTest("YYApplicationoncreate="+Util.getPackageName(getApplicationContext()));
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		YYWApplication.mContext = mContext;
	}
}
