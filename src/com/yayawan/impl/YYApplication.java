package com.yayawan.impl;


import com.lidroid.jxutils.http.Jxutilsinit;
import com.yayawan.proxy.GameApitest;
import com.yayawan.proxy.YYWApplication;
import com.yayawan.proxy.YYcontants;
import com.yayawan.sdk.utils.Util;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.PermissionUtils;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.Yayalog;

import android.Manifest;
import android.app.Application;
import android.content.Context;

public class YYApplication extends YYWApplication {

	public static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		YYcontants.ISDEBUG=DeviceUtil.isDebug(this);
		mContext = getApplicationContext();
		
		
		
			
		if (PermissionUtils.checkPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)) {
			Jxutilsinit.init(getApplicationContext());
		}
			
	
		
		System.out.println("YYApplication");
		GameApitest.getGameApitestInstants(getApplicationContext()).sendTest("YYApplicationoncreate="+Util.getPackageName(getApplicationContext()));
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		YYWApplication.mContext = mContext;
	}
}
