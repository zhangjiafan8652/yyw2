package com.yayawan.impl;



import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.lidroid.jxutils.http.Jxutilsinit;
import com.yayawan.common.CommonData;
import com.yayawan.proxy.GameApitest;

import com.yayawan.proxy.MiitHelper;
import com.yayawan.proxy.MiitHelper.AppIdsUpdater;
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
import android.widget.Toast;

public class YYApplication extends YYWApplication {

	public static Context mContext;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		YYcontants.ISDEBUG=DeviceUtil.isDebug(this);
		
		//初始化千骐千果sdk
				
		//CommonData.initCommonData(getApplicationContext());
		mContext = getApplicationContext();
	
			
		if (PermissionUtils.checkPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)) {
			Jxutilsinit.init(getApplicationContext());
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
