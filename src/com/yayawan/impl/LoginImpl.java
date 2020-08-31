package com.yayawan.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWLoginer;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkUserCallback;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.other.JFnewnoticeUtils;

import com.yayawan.utils.Handle;
import com.yayawan.utils.Yayalog;

public class LoginImpl implements YYWLoginer {

    @Override
    public void login(final Activity paramActivity, YYWUserCallBack userCallBack, String paramString) {

    	//下载文件
    	
    	new Handler(Looper.getMainLooper()).post(new Runnable() {

    		
    		
    		
            @Override
            public void run() {
            	

			        DgameSdk.login(paramActivity, new KgameSdkUserCallback() {

			            @Override
			            public void onSuccess(User user, int arg1) {
			                if (YYWMain.mUserCallBack != null) {
			                    YYWUser yywUser = new YYWUser();
			                    yywUser.uid = user.uid + "";
			                    yywUser.icon = user.icon;
			                    yywUser.body = user.body;
			                    yywUser.lasttime = user.lasttime;
			                    yywUser.money = user.money;
			                    yywUser.nick = user.nick;
			                    yywUser.password = user.password;
			                    yywUser.phoneActive = user.phoneActive;
			                    yywUser.success = user.success;
			                    yywUser.token = user.token;
			                    yywUser.userName = user.userName;
			                    YYWMain.mUser=yywUser;
			                    YYWMain.mUser.token=user.token;
			                    Yayalog.loger("dgame登陆成功："+YYWMain.mUser.token+"  USER:"+user.token+"yywuser："+yywUser.token);
			                    YYWMain.mUserCallBack.onLoginSuccess(yywUser, "success");
			                    Handle.login_handler(paramActivity, yywUser.uid,  yywUser.userName);
			                    
			                    paramActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
									
										new JFnewnoticeUtils().getNotice(paramActivity);
									}
								});
				                
			                }
			            }

			            @Override
			            public void onLogout() {
			            	DgameSdk.stop(paramActivity);
			                if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onLogout("logout");
			                }
			                
			                
			            }

			            @Override
			            public void onError(int arg0) {
			            	
			                if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onLoginFailed("failed", "");
			                }
			            }

			            @Override
			            public void onCancel() {
			                // TODO Auto-generated method stub
			            	if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onCancel();
			                }
			            }
			        });

            	}

    	});

    }

    @Override
    public void relogin(Activity paramActivity, YYWUserCallBack userCallBack,
            String paramString) {
    		Yayalog.loger("yaya代理sdk注销");
    		DgameSdk.logout(paramActivity);
    		
    		userCallBack.onLogout(null);
    		
    }

}
