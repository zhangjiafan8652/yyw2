package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.lidroid.jxutils.http.Jxutilsinit;
import com.yayawan.proxy.YYWActivityStub;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.utils.Handle;
import com.yayawan.utils.Yayalog;

public class ActivityStubImpl implements YYWActivityStub {

	public static Activity mactivity;

	@Override
	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	//启动activity  的oncreate
	public void launcherOncreate(Activity paramActivity){
			
	}

	//启动activity  的onNewIntent
	public void launcherOnNewIntent(Intent intent){
			
	}
	
	@Override
	public void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
		Jxutilsinit.isdebug=true;
		
		Yayalog.loger("oncreate");
		DgameSdk.initSdk(paramActivity);
		Handle.active_handler(paramActivity);
		

	}

	@Override
	public void onResume(Activity paramActivity) {
		mactivity = paramActivity;
		DgameSdk.init(mactivity);

		Yayalog.loger("onresume");
	}

	@Override
	public void onPause(Activity paramActivity) {

		//DgameSdk.stop(paramActivity);
		

		Yayalog.loger("onpause");
	}

	@Override
	public void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("onrestart");
	}

	@Override
	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("onstop");
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("ondestroy");
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initSdk(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("KgameSdksdk：initsdk");
		DgameSdk.initSdk(paramActivity);
	}

	public void payumenSucceed(String money) {
		Yayalog.loger("KgameSdksdk：payumenSucceed");
	}

	@Override
	public void onStart(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void launchActivityOnCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchActivityonOnNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		
	};

}
