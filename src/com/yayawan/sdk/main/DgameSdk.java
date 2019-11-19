package com.yayawan.sdk.main;

import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.permissonutils.Permission_dialog;
import com.jf.permissonutils.Permission_dialog.PermissionDialogClickCallBack;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWApiCallback;
import com.yayawan.common.CommonData;
import com.yayawan.domain.YYWUser;
import com.yayawan.impl.UserManagerImpl;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.bean.Order;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.ExitdialogCallBack;
import com.yayawan.sdk.callback.KgameSdkApiCallBack;
import com.yayawan.sdk.callback.KgameSdkCallback;
import com.yayawan.sdk.callback.KgameSdkPaymentCallback;
import com.yayawan.sdk.callback.KgameSdkStartAnimationCallback;
import com.yayawan.sdk.callback.KgameSdkUpdateCallback;
import com.yayawan.sdk.callback.KgameSdkUserCallback;
import com.yayawan.sdk.db.UserDao;
import com.yayawan.sdk.login.BaseLogin_Activity;
import com.yayawan.sdk.login.Exit_dialog;
import com.yayawan.sdk.login.SmallHelpActivity;
import com.yayawan.sdk.login.Startlogin_dialog;
import com.yayawan.sdk.login.TipDialog;
import com.yayawan.sdk.login.VerifyPlayInfo_ho_dialog;
import com.yayawan.sdk.pay.XiaoMipayActivity;
import com.yayawan.sdk.utils.LogoWindow;
import com.yayawan.sdk.utils.ToastUtil;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.MD5;
import com.yayawan.utils.PermissionUtils;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.SuperDialog;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;
import com.yayawan.utils.PermissionUtils.PermissionCheckCallBack;
import com.yayawan.utils.SuperDialog.onDialogClickListener;

/**
 * sdk调用入口
 * @author zhangjiafan
 *
 */
public class DgameSdk {

	public static KgameSdkUserCallback mUserCallback; // 登录的回调

	public static KgameSdkPaymentCallback mPaymentCallback; // 支付回调

	public static KgameSdkStartAnimationCallback mStartAnimationCallback; // 开始动画回调

	public static KgameSdkCallback mCallback;

	public static KgameSdkUpdateCallback mUpdateCallback;
	
	public static KgameSdkApiCallBack mSdkApiCallback;

	public static Order mPayOrder; // 订单

	private static Intent intent;
	
	
	public static int  sdktype=0;

	/**
	 * 动画的调用
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void animation(Activity paramActivity,
			KgameSdkStartAnimationCallback paramCallback) {
		mStartAnimationCallback = paramCallback;
		Yayalog.loger("kgameanim");		
		String gameInfo = DeviceUtil.getGameInfo(paramActivity, "sdktype");
		
			Intent intent = new Intent(paramActivity.getApplicationContext(),
			BaseLogin_Activity.class);
			intent.putExtra("type", ViewConstants.STARTANIMATION);
		    paramActivity.startActivityForResult(intent, 10200);
	
		

	}

	/**
	 * 登录接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void login(final Activity paramActivity,
			KgameSdkUserCallback paramCallback) {
		
		
		
		
		
		
	
		
		
		
		
		Yayalog.loger("kgamesdklogin");
		mUserCallback = paramCallback;
		ViewConstants.mMainActivity = paramActivity;
		Startlogin_dialog startlogin_dialog = new Startlogin_dialog(
				paramActivity);

		startlogin_dialog.dialogShow();

	}

	
	public static void loginSucce(User user,int type){
		System.out.println("dgamesdk login loginSucce++++++++++");
		Yayalog.loger("dgamesdk login loginSucce++++++++++");
//		sys
		if (mUserCallback!=null) {
			mUserCallback.onSuccess(user, type);
			
		}
		
		
		if (!(PermissionUtils.checkPermission(ViewConstants.mMainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
				&&PermissionUtils.checkPermission(ViewConstants.mMainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				&&PermissionUtils.checkPermission(ViewConstants.mMainActivity, Manifest.permission.READ_PHONE_STATE))) {
		
		
					Yayalog.loger("请求权限对话框按钮按下");
					PermissionUtils.checkMorePermissions(ViewConstants.mMainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},new PermissionCheckCallBack() {
						
						@Override
						public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
							// TODO Auto-generated method stub
							// 用户之前已拒绝过权限申请
							//
							//PermissionUtils.requestMorePermissions(ViewConstants.mMainActivity,  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 1);
							 ActivityCompat.requestPermissions((Activity) ViewConstants.mMainActivity,  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 1);
						}
						
						@Override
						public void onUserHasAlreadyTurnedDown(String... permission) {
							// TODO Auto-generated method stub
							// 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
							
						}
						
						@Override
						public void onHasPermission() {
							// TODO Auto-generated method stub
							
						}
					});
				
	
					
			
		}
		
		
	}
	
	
	

	/**
	 * 支付接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void payment(final Activity paramActivity, final Order paramOrder,
			final Boolean issinglepay, final KgameSdkPaymentCallback paramCallback) {

		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Yayalog.loger("kgamesdk:payment");
		//如果没有实名认证，则需要实名认证
		if (!ViewConstants.relname_valid) {
			
			if (DeviceUtil.isDebug(paramActivity)) {
				
				tipDialogShow(paramActivity, paramOrder, issinglepay,
						paramCallback);
				
			}else {
				
				
			  String istiprenzheng=	Sputils.getSPstring("istiprenzheng", "no", paramActivity);
			  
			  if (istiprenzheng.equals("yes")) {
				  
				  
				  gotoPayment( paramActivity,  paramOrder,
							 issinglepay,  paramCallback);
				
			  	}else {
			  	  Sputils.putSPstring("istiprenzheng", "yes", paramActivity);
			  	  tipDialogShow(paramActivity, paramOrder, issinglepay,
							paramCallback);
				 
				}
				
				
			}
			
			
			
		}else {
			gotoPayment( paramActivity,  paramOrder,
					 issinglepay,  paramCallback);
		}
		

	}

	private static void tipDialogShow(final Activity paramActivity,
			final Order paramOrder, final Boolean issinglepay,
			final KgameSdkPaymentCallback paramCallback) {
		final TipDialog tipDialog = new TipDialog(paramActivity);
		tipDialog.getTv_titile().setText("实名认证");
		tipDialog.getmMessage().setText("亲爱的玩家，您还没有实名认证哦~！");
		tipDialog.getmCancel().setText("去认证");
		tipDialog.getmSubmit().setText("继续支付");
		
		
		tipDialog.setCancle("去认证", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				accountManager(paramActivity);
				tipDialog.dismiss();
			}
		});
		tipDialog.setSubmit("继续支付", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoPayment( paramActivity,  paramOrder,
						 issinglepay,  paramCallback);
				tipDialog.dismiss();
			}
		});
		tipDialog.show();
	}

	//支付开始
	public static void gotoPayment(Activity paramActivity, Order paramOrder,
			Boolean issinglepay, KgameSdkPaymentCallback paramCallback) {

		Yayalog.loger("kgamesdk:payment");
		
			if (AgentApp.mUser == null) {
				Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
						Toast.LENGTH_SHORT).show();
				return;
			}
			mPaymentCallback = paramCallback;
			mPayOrder = paramOrder;
			AgentApp.mPayOrder = paramOrder;
			
			initSdkpaytype(paramActivity);
		
		

	}

	/**
	 * 个人中心
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void accountManager(Activity paramActivity) {
		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(paramActivity.getApplicationContext(),
				SmallHelpActivity.class);

		intent.putExtra("type", ViewConstants.ACCOUNTMANAGER);
		paramActivity.startActivityForResult(intent,10020);

	}

	/**
	 * 设置角色id
	 * 
	 * @param paramActivity
	 * @param roleId
	 * @param roleName
	 * @param roleLevel
	 * @param zoneId
	 * @param zoneName
	 */
	public static void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub

		Yayalog.loger("kgamesdksetRoleData"+AgentApp.mUser.token+"--"+AgentApp.mUser.uid);
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("token", AgentApp.mUser.token+"");
		requestParams.addBodyParameter("uid", AgentApp.mUser.uid+"");
		requestParams.addBodyParameter("role_id", roleId);
		requestParams.addBodyParameter("role_name",roleName);
		requestParams.addBodyParameter("role_level",roleLevel);
		requestParams.addBodyParameter("zone_id",zoneId);
		requestParams.addBodyParameter("zone_name", zoneName);
		Yayalog.loger("app_id", DeviceUtil.getAppid(paramActivity));
		Yayalog.loger("token", AgentApp.mUser.token+"");
		Yayalog.loger("uid", AgentApp.mUser.uid+"");
		Yayalog.loger("role_id", roleId);
		Yayalog.loger("role_name",roleName);
		Yayalog.loger("role_level",roleLevel);
		Yayalog.loger("zone_id",zoneId);
		Yayalog.loger("zone_name", zoneName);
		Yayalog.loger("zone_name", ViewConstants.SETROLEDATAURL);
		httpUtils.send(HttpMethod.POST, ViewConstants.SETROLEDATAURL, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳游戏数据失败:"+arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳遊戲數據成功:"+arg0.result);
			}
		});
		
	}

	/**
	 * 设置角色id
	 * 
	 * @param paramActivity
	 * @param roleId
	 * @param roleName
	 * @param roleLevel
	 * @param zoneId
	 * @param zoneName
	 */
	public static void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,String token,String uid) {
		// TODO Auto-generated method stub

		Yayalog.loger("设置角色信息token："+token+"--"+uid);
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("token", token+"");
		requestParams.addBodyParameter("uid", uid+"");
		requestParams.addBodyParameter("role_id", roleId);
		requestParams.addBodyParameter("role_name",roleName);
		requestParams.addBodyParameter("role_level",roleLevel);
		requestParams.addBodyParameter("zone_id",zoneId);
		requestParams.addBodyParameter("zone_name", zoneName);
		
		
		Yayalog.loger("app_id", DeviceUtil.getAppid(paramActivity));
		Yayalog.loger("token", token+"");
		Yayalog.loger("uid", uid+"");
		Yayalog.loger("role_id", roleId);
		Yayalog.loger("role_name",roleName);
		Yayalog.loger("role_level",roleLevel);
		Yayalog.loger("zone_id",zoneId);
		Yayalog.loger("zone_name", zoneName);
		Yayalog.loger("zone_name", ViewConstants.SETROLEDATAURL);
		httpUtils.send(HttpMethod.POST, ViewConstants.SETROLEDATAURL, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳游戏数据失败:"+arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳遊戲數據成功:"+arg0.result);
			}
		});
		
	}

	
	/**
	 * 设置角色id
	 * 
	 * @param paramActivity
	 * @param roleId
	 * @param roleName
	 * @param roleLevel
	 * @param zoneId
	 * @param zoneName
	 */
	public static void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,String token,String uid,String type) {
		// TODO Auto-generated method stub

		Yayalog.loger("设置角色信息token："+token+"--"+uid);
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("token", token+"");
		requestParams.addBodyParameter("uid", uid+"");
		requestParams.addBodyParameter("role_id", roleId);
		requestParams.addBodyParameter("role_name",roleName);
		requestParams.addBodyParameter("role_level",roleLevel);
		requestParams.addBodyParameter("zone_id",zoneId);
		requestParams.addBodyParameter("zone_name", zoneName);
		requestParams.addBodyParameter("type", type);
		
		
		Yayalog.loger("app_id", DeviceUtil.getAppid(paramActivity));
		Yayalog.loger("token", token+"");
		Yayalog.loger("uid", uid+"");
		Yayalog.loger("role_id", roleId);
		Yayalog.loger("role_name",roleName);
		Yayalog.loger("role_level",roleLevel);
		Yayalog.loger("zone_id",zoneId);
		Yayalog.loger("zone_name", zoneName);
		Yayalog.loger("zone_name", ViewConstants.SETROLEDATAURL);
		httpUtils.send(HttpMethod.POST, ViewConstants.SETROLEDATAURL, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳游戏数据失败:"+arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳遊戲數據成功:"+arg0.result);
			}
		});
		
	}
	
	/**
	 * 初始化sdk
	 * 
	 * @param activity
	 */
	public static void initSdk(Activity activity) {

		// 工具类初始化，在支付安装插件时候用到
		
		String gameInfo = DeviceUtil.getGameInfo(activity, "sdktype");
		
		sdktype=Integer.parseInt(gameInfo);
		ViewConstants.ISKGAME=true;
		
	}
	
	/**
	 * 初始化sdk
	 * 
	 * @param activity
	 */
	public static void initSdk(Activity activity,int type) {

		// 工具类初始化，在支付安装插件时候用到
		
		String gameInfo = DeviceUtil.getGameInfo(activity, "sdktype");
		
		sdktype=Integer.parseInt(gameInfo);
		//ViewConstants.ISKGAME=true;
		
	}
	

	//初始化sdk支付方式
	private static void initSdkpaytype(final Activity activity) {
		// TODO Auto-generated method stub
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(activity));
		requestParams.addBodyParameter("uid", YYWMain.mUser.uid);
		requestParams.addBodyParameter("token", YYWMain.mUser.token);
	
		Yayalog.loger("app_id", DeviceUtil.getAppid(activity));
		Yayalog.loger("uid", YYWMain.mUser.uid);
		Yayalog.loger("token", YYWMain.mUser.token);
	
		httpUtils.send(HttpMethod.POST, ViewConstants.paytype,requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String result) {
				// TODO Auto-generated method stub
				
				YYWMain.mPayCallBack.onPayFailed("1", "");
			}

			@Override
			public void onSuccess(ResponseInfo<String> result) {
				// TODO Auto-generated method stub
			Yayalog.loger("初始化支付方式："+result.result);
				try {
					JSONObject jsonObject = new JSONObject(result.result);
					int optInt = jsonObject.optInt("err_code");
					if (optInt==0) {
						JSONObject data=jsonObject.getJSONObject("data");
						int toggleint =data.optInt("toggle");
						JSONArray allpaytypearray =data.optJSONArray("all_paytype");
						for (int i = 0; i < allpaytypearray.length(); i++) {
							JSONObject paytyp=allpaytypearray.getJSONObject(i);
						    String paylib=	MD5.MD5(paytyp.optString("lib"));
						    String payid=	paytyp.optString("id");
						//	System.out.println(paylib+":"+MD5.MD5(paylib));
							//System.out.println("CommonData.bluepmd5string:"+CommonData.bluepmd5string);
						    if (paylib.equals(CommonData.bluepmd5string)) {
						    	CommonData.BLUEP=Integer.parseInt(payid);
						    	Yayalog.loger("设置支付方式CommonData.bluepmd5string："+payid);
						    }else if(paylib.equals(CommonData.greenpmd5string)) {
								
						    	CommonData.GREENP=Integer.parseInt(payid);
						    	Yayalog.loger("设置支付方式CommonData.greenpmd5string："+payid);
							}else if(paylib.equals(CommonData.yayabipaymd5string)) {
								CommonData.YAYABIPAY=Integer.parseInt(payid);
								//Yayalog.loger("设置支付方式CommonData.bluepmd5string："+payid);
							}else if(paylib.equals(CommonData.daijinjuanpaymd5string)) {
								CommonData.DAIJINJUANPAY=Integer.parseInt(payid);
								//Yayalog.loger("设置支付方式CommonData.bluepmd5string："+payid);
							}
						}
						
					}else {
					
						
					}
					intent = new Intent(activity, BaseLogin_Activity.class);
					intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
					activity.startActivity(intent);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					YYWMain.mPayCallBack.onPayFailed("1", "");
					e.printStackTrace();
				}
				
			}
		});
		

	}

	/**
	 * 开启悬浮窗
	 * 
	 * @param activity
	 */
	public static void init(Activity activity) {

		
			LogoWindow.getInstants(activity).start();

		
		
	}

	/**
	 * 关闭悬浮窗
	 * 
	 * @param activity
	 */
	public static void stop(Activity activity) {

		LogoWindow.getInstants(activity).Stop();

	}

	/**
	 * 更新
	 * 
	 * @param activity
	 */
	public static void update(Activity activity,
			KgameSdkUpdateCallback updateCallback) {
		mUpdateCallback = updateCallback;
		// UpdateUtil.isUpdate(activity);

	}

	/**
	 * 注销账号
	 * 
	 * @param activity
	 */
	public static void logout(Activity activity) {
		Yayalog.loger("yayasdk注销");
		Sputils.putSPint("ischanageacount", 0, ViewConstants.mMainActivity);
		// KgameSdk.mUserCallback.onLogout();

	}

	/**
	 * 设置渠道id
	 * 
	 * @param activity
	 */
	public static void setSourceID(String sourceId) {

		AgentApp.mSourceId = sourceId;
	}

	public static String getSdkversion() {

		return ViewConstants.SDKVERSION;
	}

	
	/**
	 * 退出登录
	 * 
	 * @param activitiy
	 * @param onexit
	 */
	public static void ExitgameShowDialog(Activity activitiy, final KgameSdkCallback onexit) {

		if (DeviceUtil.isDebug(activitiy)) {
			Exitgame(activitiy, onexit);
			return;
		}
		
		Dialog dialog = new AlertDialog.Builder(activitiy).setTitle("退出游戏提示")

		.setMessage("是否退出游戏？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						onexit.onSuccess(null, 1);
						
					}
				})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}). create();

		dialog.show();
		
	}
	/**
	 * 退出登录
	 * 
	 * @param activitiy
	 * @param onexit
	 */
	public static void Exitgame(Activity activitiy,
			final KgameSdkCallback onexit) {
		
		if (DgameSdk.sdktype==1) {
			//onexit.onSuccess(null, 1);
			
			Dialog dialog = new AlertDialog.Builder(activitiy).setTitle("退出游戏提示")

					.setMessage("是否退出游戏？")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									onexit.onSuccess(null, 1);
									
								}
							})
							.setNeutralButton("取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}). create();

					dialog.show();
			
		}else {
			 Exit_dialog exit_dialog = new Exit_dialog(activitiy, "这个废弃",new ExitdialogCallBack() {
					
					@Override
					public void goExit() {
						// TODO Auto-generated method stub
						onexit.onSuccess(null, 1);
						//this.dialogDismiss();
					}
				});
				exit_dialog.dialogShow();
		}
		
				//dialog.show();
	}
	
	/**
	 * 功能
	 * @param paramActivity
	 * @param paramOrder
	 * @param paramCallback
	 */
	public static void GreenblueP(Activity paramActivity, Order paramOrder,int paytype,
			KgameSdkPaymentCallback paramCallback){
		mPaymentCallback = paramCallback;
		AgentApp.mPayOrder = paramOrder;
		
		DgameSdk.mPayOrder=paramOrder;
		Intent intent = new Intent(paramActivity,
				XiaoMipayActivity.class);

		//intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
		paramActivity.startActivity(intent);
	}

	public static int managertype=1;
	/**
	 * 
	 * @param mactivity
	 * @param i
	 */
	public static void accountManager(Activity mactivity, int i) {
		// TODO Auto-generated method stub
		if (AgentApp.mUser == null) {
			Toast.makeText(mactivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
		managertype=i;
		Intent intent = new Intent(mactivity.getApplicationContext(),
				SmallHelpActivity.class);

		intent.putExtra("type", ViewConstants.ACCOUNTMANAGER);
		mactivity.startActivityForResult(intent,10020);
	}
	
	/**
	 * 
	 * @param mactivity
	 * @param i
	 */
	public static void addPaomadengView(Activity mactivity) {
		// TODO Auto-generated method stub
		ViewGroup rootview =(ViewGroup) mactivity.findViewById(android.R.id.content);
		TextView textView = new TextView(mactivity);
		MachineFactory machineFactory = new MachineFactory(mactivity);
		machineFactory.MachineView(textView,-1 , 200, "LinearLayout");
		textView.setText("hhahahhhhhhhhhhhhfdsaaaaaaaaaaaaaaaaaaaaaaaaaaaaahhhhhhhhhhhhhh");
		textView.setTextColor(Color.RED );
		rootview.addView(textView);
	}
	
	//是否实名认证
	public static void doGetVerifiedInfo(final Activity mactivity,KgameSdkApiCallBack myywapicallback) {

			
			mSdkApiCallback=myywapicallback;
			//请求查看是否实名认证
			HttpUtils httpUtils = new HttpUtils();
			RequestParams requestParams = new RequestParams();
			requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(mactivity));
			requestParams.addBodyParameter("uid", YYWMain.mUser.uid);
			requestParams.addBodyParameter("token", YYWMain.mUser.token);
		
			Yayalog.loger("app_id", DeviceUtil.getAppid(mactivity));
			Yayalog.loger("uid", YYWMain.mUser.uid);
			Yayalog.loger("token", YYWMain.mUser.token);
		
			httpUtils.send(HttpMethod.POST, ViewConstants.SHIMINGRENZHENG,requestParams, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String result) {
					// TODO Auto-generated method stub
					
					//YYWMain.mPayCallBack.onPayFailed("1", "");
					mSdkApiCallback.onVerifyCancel();
				}

				@Override
				public void onSuccess(ResponseInfo<String> result) {
					// TODO Auto-generated method stub
				Yayalog.loger("实名认证返回DgameSdk："+result.result);
					try {
						JSONObject jsonObject = new JSONObject(result.result);
						int optInt = jsonObject.optInt("err_code");
						if (optInt==11001) {
							
							//未实名认证，开启实名认证窗口
							
							VerifyPlayInfo_ho_dialog verifyPlayInfo_ho_dialog = new VerifyPlayInfo_ho_dialog(mactivity);
							verifyPlayInfo_ho_dialog.dialogShow();
							
						}else {
						
//							06-24 16:54:40.012: E/yayawanYayalog(3612): 实名认证返回DgameSdk：{"err_code":0,"err_msg":"success","bday":"19920115"}

							//实名认证成功
							DgameSdk.mSdkApiCallback.onVerifySuccess(result.result);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						YYWMain.mPayCallBack.onPayFailed("1", "");
						e.printStackTrace();
					}
					
				}
			});
			
		}
}
