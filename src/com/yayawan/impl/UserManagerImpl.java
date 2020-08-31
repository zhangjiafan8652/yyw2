package com.yayawan.impl;



import android.app.Activity;

import com.yayawan.callback.YYWApiCallback;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.common.CommonData;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWUserManager;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkApiCallBack;
import com.yayawan.sdk.callback.KgameSdkCallback;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.utils.Yayalog;

public class UserManagerImpl implements YYWUserManager {

	@Override
	public void manager(Activity paramActivity) {
		// TODO Auto-generated method stub
		DgameSdk.accountManager(paramActivity);
	}

	@Override
	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		//KgameSdk.stop(paramActivity);
		DgameSdk.logout(paramActivity);
	}

	@Override
	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if (CommonData.sdkid.equals("qianqi_app_id")) {
					DgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

						@Override
						public void onSuccess(User paramUser, int paramInt) {
							// TODO Auto-generated method stub
							callback.onExit();
						}

						@Override
						public void onError(int paramInt) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onLogout() {
							// TODO Auto-generated method stub

						}

					});
					
					
				}else {
					DgameSdk.ExitgameShowDialog(paramActivity, new KgameSdkCallback() {

						@Override
						public void onSuccess(User paramUser, int paramInt) {
							// TODO Auto-generated method stub
							callback.onExit();
						}

						@Override
						public void onError(int paramInt) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onLogout() {
							// TODO Auto-generated method stub

						}

					});
					
					
				}
				
				
			}
		});

	}

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub
		DgameSdk.setRoleData(arg0, YYWMain.mRole.getRoleId(),
				YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
				YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
	}

	// 3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,
			String ext) {
		
		Yayalog.loger("调用了impl中的usermanagerimpl中的setdata方法");
	
		//if (Integer.parseInt(ext)==1) {
					
	//	}
		
		DgameSdk.setRoleData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName,  AgentApp.mUser.token,  AgentApp.mUser.uid+"", ext);

	
	}
	
	//是否实名认证
	public void doGetVerifiedInfo(Activity arg0,final YYWApiCallback myywapicallback) {

			//Yayalog.loger("按下了" + event.getX());
		
		DgameSdk.doGetVerifiedInfo(arg0, new KgameSdkApiCallBack() {
			
			@Override
			public void onVerifySuccess(String result) {
				// TODO Auto-generated method stub
				myywapicallback.onVerifySuccess(result);
			}
			
			@Override
			public void onVerifyCancel() {
				// TODO Auto-generated method stub
				myywapicallback.onVerifyCancel();
			}
		});
	}

}
