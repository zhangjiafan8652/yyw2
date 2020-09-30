package com.yayawan.sdk.utils;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkUserCallback;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.other.JFupdateUtils;

import com.yayawan.sdk.xml.Basexml;
import com.yayawan.utils.ViewConstants;

public abstract class Basedialogview extends Basexml {

	public KgameSdkUserCallback mUserCallback;
	protected SharedPreferences mSp;

	
	protected boolean dialogisshow=false;

	public Basedialogview(Activity activity) {
		super(activity);
		mUserCallback = DgameSdk.mUserCallback;
		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);

		createDialog(mActivity);
		
		
		ViewConstants.mDialogs.add(dialog);
	}

	
	public  Dialog dialog;
	protected LinearLayout baselin;
	protected LinearLayout ll_mDele;

	public abstract void createDialog(Activity mActivity);
	
	public  void logic(Activity mActivity){
		
	}

	public void dialogShow() {
		
		if (dialog != null) {
			dialog.show();
			
			//logic(mActivity);
		}
	}

	public  void dialogDismiss() {

		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void allDismiss() {
		for (int i = 0; i < ViewConstants.mDialogs.size(); i++) {
			
			if (ViewConstants.mDialogs.get(i) != null) {
				ViewConstants.mDialogs.get(i).dismiss();
			}

			// ViewConstants.mDialogs.remove(0);
		}

		ViewConstants.mDialogs.clear();
	}

	public void onSuccess(User paramUser, int paramInt) {

		
		
		
		DgameSdk.loginSucce(paramUser, paramInt);
		// mActivity.finish();

		for (int i = 0; i < ViewConstants.mDialogs.size(); i++) {
			if (ViewConstants.mDialogs.get(i) != null) {
				ViewConstants.mDialogs.get(i).dismiss();
			}

			// ViewConstants.mDialogs.remove(0);
		}

		ViewConstants.mDialogs.clear();
		// dialog.dismiss();
	}

	public void onError(int paramInt) {
		if (mUserCallback != null) {
			mUserCallback.onError(paramInt);
		}
		mUserCallback = null;
	}

	public void onCancel() {
		if (mUserCallback != null) {
			mUserCallback.onCancel();
		}
		mUserCallback = null;

	}

	public void onLogout() {
		if (mUserCallback != null) {
			
			mUserCallback.onLogout();
		}
		mUserCallback = null;
		// dialogDismiss();
		dialogDismiss();

	}

}
