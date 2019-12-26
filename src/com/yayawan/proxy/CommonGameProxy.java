package com.yayawan.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Toast;

import com.bun.miitmdid.core.JLibrary;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;

import com.lidroid.jxutils.http.Jxutilsinit;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWApiCallback;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWLoginHandleCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.common.CommonData;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWRole;
import com.yayawan.domain.YYWUser;
import com.yayawan.impl.ActivityStubImpl;
import com.yayawan.impl.AnimationImpl;
import com.yayawan.impl.ChargerImpl;
import com.yayawan.impl.LoginImpl;
import com.yayawan.impl.UserManagerImpl;
import com.yayawan.impl.YYApplication;
import com.yayawan.implyy.ChargerImplyylianhe;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.MiitHelper.AppIdsUpdater;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.other.JFVivoupdateUtils;
import com.yayawan.sdk.other.JFnoticeUtils;
import com.yayawan.sdk.other.JFupdateUtils;
import com.yayawan.sdk.pay.GreenblueP;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Handle;
import com.yayawan.utils.JSONUtil;
import com.yayawan.utils.MD5;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

public class CommonGameProxy implements YYWGameProxy {

	private YYWLoginer mLogin;

	private YYWCharger mCharger;

	private YYWActivityStub mStub;

	private YYWUserManager mUserManager;

	private YYWAnimation mAnimation;

	private Activity mActivity;

	private int templevel;

	public CommonGameProxy() {

		this(new LoginImpl(), new ActivityStubImpl(), new UserManagerImpl(),
				new ChargerImpl());

		setAnimation(new AnimationImpl());

	}

	public CommonGameProxy(YYWLoginer login, YYWActivityStub stub,
			YYWUserManager userManager, YYWCharger charger) {
		super();
		// new CommonGameProxy();
		this.mLogin = login;
		this.mStub = stub;
		this.mUserManager = userManager;
		this.mCharger = charger;
	}

	public void setLogin(YYWLoginer login) {
		this.mLogin = login;

	}

	public void setCharger(YYWCharger charger) {
		this.mCharger = charger;
	}

	public void setStub(YYWActivityStub stub) {
		this.mStub = stub;
	}

	public void setUserManager(YYWUserManager userManager) {
		this.mUserManager = userManager;
	}

	public void setAnimation(YYWAnimation animation) {
		this.mAnimation = animation;
	}

	public static boolean ISNEWPAY = false;// 是否使用只新登陆,用于控制第三种支付方式
	
	
	public static boolean isHadmsdkverskon(){
		Field[] declaredFields = Jxutilsinit.class.getDeclaredFields();
		//Yayalog.loger("jxtuils declaredFields:"+declaredFields.toString());
		for (int i = 0; i < declaredFields.length; i++) {
			
			if (declaredFields[i].toString().contains("msdkversion")) {
				return true;
			}
					
		}
		
			return false;
		
	}
	
	

	@Override
	public void login(final Activity paramActivity,
			final YYWUserCallBack userCallBack) {
		
		
		
		mActivity = paramActivity;
		
		if (isHadmsdkverskon()) {
			Yayalog.loger("有sdkversion");
			Jxutilsinit.msdkversion=ViewConstants.SDKVERSIONCODE;
		}
		
		// YYWMain.mUserCallBack=userCallBack;
	//	ToastUtil.showSuccess(paramActivity, paramActivity.getClass().getName());
		// 检测是否调用类
		GameApitest.getGameApitestInstants(paramActivity).sendTest("login");

		if (ViewConstants.ISKGAME) {
			Yayalog.loger("kgame登陆成功不再获取uid" );
			Yayalog.loger("Kgamelogin");
			YYWMain.mUserCallBack = userCallBack;
			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
			
			
		} else {

			Yayalog.loger("UNIONlogin");
			YYWMain.mUserCallBack = new YYWUserCallBack() {

				@Override
				public void onLogout(Object paramObject) {
					// TODO Auto-generated method stub
					userCallBack.onLogout(paramObject);
				}

				@Override
				public void onLoginSuccess(final YYWUser paramUser,
						final Object paramObject) {
					Yayalog.loger("联合渠道登陆成功：" + paramUser.toString());
					Handle.active_handler(paramActivity);
					// TODO Auto-generated method stub
					Handle.login_handler(mActivity, YYWMain.mUser.uid,
							YYWMain.mUser.userName,
							new YYWLoginHandleCallback() {

								private YYWUser yywUser;

								@Override
								public void onSuccess(String response,
										String temp) {
									// TODO Auto-generated method stub
									Yayalog.loger("联合渠道登陆丫丫玩后返回数据：" + response);
									try {
										JSONObject resjson = new JSONObject(
												response);
										int err_code = resjson
												.optInt("err_code");
										if (err_code == 0) {
											JSONObject data = resjson
													.getJSONObject("data");
											String kgameuid = data
													.optString("uid");
											String kgameusername = data
													.optString("username");
											String kgametoken = data
													.optString("token");
											
											Yayalog.loger("kgameuid："
													+ kgameuid);
											// 拼接返回给cp的user开始
											yywUser = new YYWUser();
											yywUser.uid = kgameuid;
											yywUser.userName = kgameusername;
											// 丫丫玩平台的token
											yywUser.yywtoken = kgametoken;
											// 给研发的token
											yywUser.token = JSONUtil
													.formatToken(paramActivity,
															paramUser.token,
															paramUser.uid,
															paramUser.userName,
															yywUser.userName);
											// 拼接给cp的user结束
											Yayalog.loger("yywUser.uid："
													+ yywUser.uid);
											// 拼接渠道的user，当调用渠道的支付，一定使用到渠道的YYWMain.mUser.uid
											YYWMain.mUser.uid = paramUser.uid;
											// 渠道的username
											YYWMain.mUser.userName = paramUser.userName;
											// 丫丫玩的uid
											YYWMain.mUser.yywuid = yywUser.uid;

											Yayalog.loger("YYWMain.mUser.yywuid："
													+ YYWMain.mUser.yywuid);
											YYWMain.mUser.yywusername = yywUser.userName;

											YYWMain.mUser.yywtoken = yywUser.yywtoken;
											Yayalog.loger("+++++++++++++token"
													+ YYWMain.mUser.token);
											Yayalog.loger("+++++++++++++联合渠道登陆："
													+ YYWMain.mUser.toString());

											//BigInteger.valueOf(Long.parseLong(YYWMain.mUser.yywuid));
											
											AgentApp.mUser=new User();
											BigInteger aauid=new BigInteger(yywUser.uid);
											AgentApp.mUser.setUid(aauid);
											AgentApp.mUser.setToken(kgametoken);
											AgentApp.mUser.setUserName(kgameusername);
											String loginmesg=(String) paramObject;
											
											if (loginmesg!=null) {
												if (loginmesg.contains("sanfang")) {
													YYWMain.mUser.userName=kgameusername;
													YYWMain.mUser.uid=yywUser.uid;
													YYWMain.mUser.token=kgametoken;
													Yayalog.loger("三方登陆成功后返回的数据:"
															+ YYWMain.mUser.toString());
													mActivity
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															// TODO
														
															userCallBack
																	.onLoginSuccess(
																			YYWMain.mUser,
																			"onLoginSuccess");
															//mActivity.getPackageName()
															//如果包名包含vivo  进行vivo的更新
															Yayalog.loger("准备vivo的更新"+mActivity.getPackageName());
															if (mActivity.getPackageName().contains("vivo")) {
																Yayalog.loger("開始vivo的更新"+mActivity.getPackageName());
																new JFVivoupdateUtils(mActivity).startUpdate(YYWMain.mUser.yywuid);
															}

														}
													});
												}else {
													mActivity
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															// TODO
															Yayalog.loger("联合渠道登陆成功："
																	+ yywUser
																			.toString());
															userCallBack
																	.onLoginSuccess(
																			yywUser,
																			"onLoginSuccess");
															Yayalog.loger("准备vivo的更新"+mActivity.getPackageName());
															if (mActivity.getPackageName().contains("vivo")) {
																Yayalog.loger("開始vivo的更新"+mActivity.getPackageName());
																new JFVivoupdateUtils(mActivity).startUpdate(YYWMain.mUser.yywuid);
															}

														}
													});
												}
											}else {
												mActivity
												.runOnUiThread(new Runnable() {

													@Override
													public void run() {
														// TODO
														Yayalog.loger("联合渠道登陆成功："
																+ yywUser
																		.toString());
														userCallBack
																.onLoginSuccess(
																		yywUser,
																		"onLoginSuccess");

													}
												});
											}
										

										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								@Override
								public void onFail(String erro, String temp) {
									// TODO Auto-generated method stub
									userCallBack
											.onLoginFailed("登陆失败", "onFail");
								}
							});
				}

				@Override
				public void onLoginFailed(String paramString, Object paramObject) {
					// TODO Auto-generated method stub
					userCallBack.onLoginFailed("登陆失败", "onLoginFailed");
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					userCallBack.onCancel();
				}
			};

			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
		}

		// 检测是否调用类

	}

	@Override
	public void logout(Activity paramActivity, YYWUserCallBack userCallBack) {
		YYWMain.mUserCallBack = userCallBack;
		GameApitest.getGameApitestInstants(paramActivity).sendTest("logout");
		// this.mLogin.relogin(paramActivity, userCallBack, "relogin");
	}

	public void logout(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("logout");
		this.mUserManager.logout(paramActivity, null, null);
	}

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		YYWMain.mPayCallBack = payCallBack;
		Yayalog.logerlife("charge");
		YYWMain.mOrder = order;
		this.mCharger.charge(paramActivity, order, payCallBack);
	}

	@Override
	public void pay(final Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("pay");
		YYWMain.mPayCallBack = payCallBack;
		YYWMain.mOrder = order;

		if (ViewConstants.ISKGAME) {
			gotoPay(paramActivity, 0);
			Yayalog.logerlife("ISKGAMEpay");
			return;
		}
		// 判断是否小米渠道
		if (DeviceUtil.isXiaomi(paramActivity)) {
			// 判断是否选择过小米支付
			if (GreenblueP.isselectxiaomipay) {
				this.mCharger = new ChargerImpl();
				this.mCharger.pay(paramActivity, order, payCallBack);
				return;
			}
		}

		int login_type = Sputils.getSPint("login_type", 0, paramActivity);
		int login_pay_level = Sputils.getSPint("login_pay_level", 0,
				paramActivity);
		Yayalog.loger("CommonGameProxy：login_pay_level:" + "" + login_pay_level
				+ "CommonGameProxy：login_type" + login_type + ":::templevel:"
				+ templevel);

		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",
				DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("uid", YYWMain.mUser.yywuid);
		requestParams.addBodyParameter("token", YYWMain.mUser.yywtoken);
		requestParams.addBodyParameter("app_ver",
				DeviceUtil.getVersionCode(paramActivity));
		requestParams.addBodyParameter("role_level", templevel + "");

		Yayalog.loger("app_id", DeviceUtil.getAppid(paramActivity));
		Yayalog.loger("uid", YYWMain.mUser.yywuid);
		Yayalog.loger("token", YYWMain.mUser.yywtoken);
		Yayalog.loger("app_ver", DeviceUtil.getVersionCode(paramActivity));
		Yayalog.loger("role_level", templevel + "");
		httpUtils.send(HttpMethod.POST, ViewConstants.paytype, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String result) {
						// TODO Auto-generated method stub
						System.out.println("支付请求失败：" + result);
						YYWMain.mPayCallBack.onPayFailed("1", "");
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						// TODO Auto-generated method stub
						System.out.println("支付请求成功：" + result.result);
						try {
							JSONObject jsonObject = new JSONObject(
									result.result);
							int optInt = jsonObject.optInt("err_code");
							if (optInt == 0) {
								JSONObject data = jsonObject
										.getJSONObject("data");
								int toggleint = data.optInt("toggle");
								JSONArray allpaytypearray = data
										.optJSONArray("all_paytype");
								for (int i = 0; i < allpaytypearray.length(); i++) {
									JSONObject paytyp = allpaytypearray
											.getJSONObject(i);
									String paylib = MD5.MD5(paytyp
											.optString("lib"));
									String payid = paytyp.optString("id");
									// System.out.println(paylib+":"+MD5.MD5(paylib));
									// System.out.println("CommonData.bluepmd5string:"+CommonData.bluepmd5string);
									if (paylib
											.equals(CommonData.bluepmd5string)) {
										CommonData.BLUEP = Integer
												.parseInt(payid);
										Yayalog.loger("设置支付方式CommonData.bluepmd5string："
												+ payid);
									} else if (paylib
											.equals(CommonData.greenpmd5string)) {

										CommonData.GREENP = Integer
												.parseInt(payid);
										Yayalog.loger("设置支付方式CommonData.greenpmd5string："
												+ payid);
									} else if (paylib
											.equals(CommonData.yayabipaymd5string)) {
										CommonData.YAYABIPAY = Integer
												.parseInt(payid);
										// Yayalog.loger("设置支付方式CommonData.bluepmd5string："+payid);
									} else if (paylib
											.equals(CommonData.daijinjuanpaymd5string)) {
										CommonData.DAIJINJUANPAY = Integer
												.parseInt(payid);
										// Yayalog.loger("设置支付方式CommonData.bluepmd5string："+payid);
									}

								}
								gotoPay(paramActivity, toggleint);
							} else {
								gotoPay(paramActivity, 0);

							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							YYWMain.mPayCallBack.onPayFailed("1", "");
							e.printStackTrace();
						}

					}
				});

	}

	public static void sys(String name, String val) {
		System.out.println(name + ":" + val);
	}

	public void gotoPay(Activity paramActivity, int login_type) {
		switch (login_type) {
		case 0:
			Yayalog.loger("CommonGameProxy:" + "kgame支付");
			this.mCharger.pay(paramActivity, YYWMain.mOrder,
					YYWMain.mPayCallBack);
			break;

		case 1:

			Yayalog.loger("CommonGameProxy1:" + "kgamelianhe支付");
			this.mCharger = new ChargerImplyylianhe();
			this.mCharger.pay(paramActivity, YYWMain.mOrder,
					YYWMain.mPayCallBack);

			break;
		case 2:

			break;

		default:
			break;
		}
	}

	@Override
	public void manager(Activity paramActivity) {
		Yayalog.logerlife("manager");
		this.mUserManager.manager(paramActivity);

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback exitCallBack) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("exit");
		YYWMain.mExitCallback = exitCallBack;
		this.mUserManager.exit(paramActivity, exitCallBack);
	}

	@Override
	public void anim(Activity paramActivity, YYWAnimCallBack animCallback) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("anim");
		YYWMain.mAnimCallBack = animCallback;
		this.mAnimation.anim(paramActivity);

	}

	@Override
	public void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"setRoleData");
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName);
		// 设置临时的角色等级。用作支付时候判断是否切换支付
		templevel = Integer.parseInt(roleLevel);
		this.mUserManager.setRoleData(paramActivity);
	}

	// 3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,
			String ext) {

		// 设置临时的角色等级。用作支付时候判断是否切换支付
		templevel = Integer.parseInt(roleLevel);
		
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName, roleCTime, ext);

		
		
			Yayalog.loger("yingyongbao setdata1");
		if (ViewConstants.ISKGAME) {
				
		}else {
				DgameSdk.setRoleData(paramActivity, roleId,
						roleName, roleLevel,
						zoneId, zoneName,YYWMain.mUser.yywtoken,YYWMain.mUser.yywuid,ext);
		}
			
		
		if (TextUtils.isEmpty(roleName)) {
			roleName="temprolename";
		}
		
		this.mUserManager.setData(paramActivity, roleId, roleName, roleLevel,
				zoneId, zoneName, roleCTime, ext);
		
		
		
		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"setData玩家数据：" + YYWMain.mRole.toString());

	}

	@Override
	public void applicationInit(Activity paramActivity) {
		this.mStub.applicationInit(paramActivity);
	}

	boolean newactive = true;

	private int loca_login_type;

	public static int REQUEST_CODE_ASK_READ_PHONE_STATE = 3301;
	public static MiitHelper miitHelper;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(final Activity paramActivity) {

		//初始化千骐千果sdk
		CommonData.initCommonData(paramActivity);
		
		mActivity = paramActivity;
		JLibrary.InitEntry(paramActivity);
		 miitHelper = new MiitHelper(new AppIdsUpdater() {
				
				@Override
				public void OnIdsAvalid(String ids) {
					// TODO Auto-generated method stub
					Jxutilsinit.oaid=miitHelper.oaid;
					//Toast.makeText(paramActivity, "Handle.active_handler======================"+ids, 0).show();
					Handle.active_handler(paramActivity);
				}
			});
			
			miitHelper.getDeviceIds(paramActivity);
		
		// 进行检查更新
		YYcontants.ISDEBUG = DeviceUtil.isDebug(paramActivity);
		//Jxutilsinit.isdebug=true;
		
		Yayalog.setCanlog(DeviceUtil.isDebug(paramActivity));// 设置是否打log
		System.out.println("是否可以打印yayalog：" + Yayalog.canlog);
		Yayalog.loger("当前sdk版本：" + CommonData.SDKVERSION);

	

		// 获取公告
		new JFnoticeUtils().getNotice(paramActivity);
		// 获取更新
		new JFupdateUtils(paramActivity).startUpdate();

		GameApitest.getGameApitestInstants(paramActivity).sendTest("onCreate");

		// recordPoint(paramActivity);
		try {
			GameApitest.sendTest2(paramActivity);
		} catch (Exception e) {
			// TODO: handle exception
		}
		mStub.onCreate(paramActivity);

	}

	@Override
	public void onStop(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onStop");
		this.mStub.onStop(paramActivity);

	}

	@Override
	public void onResume(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onResume");

		this.mStub.onResume(paramActivity);

		//关闭android p的对话框
		DeviceUtil.closeAndroidPDialog();
		if (TextUtils.isEmpty(MiitHelper.oaid)||MiitHelper.oaid.equals("")) {
			miitHelper.getDeviceIds(paramActivity);
		}
		Jxutilsinit.oaid=MiitHelper.oaid;
		//Toast.makeText(paramActivity, "miitHelperoaid======================Jxutilsinit.oaid"+Jxutilsinit.oaid, 0).show();

	}

	@Override
	public void onPause(Activity paramActivity) {

		GameApitest.getGameApitestInstants(paramActivity).sendTest("onPause");

		this.mStub.onPause(paramActivity);

	}

	@Override
	public void onRestart(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onRestart");

		this.mStub.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onDestroy");
		this.mStub.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		Yayalog.logerlife("applicationDestroy:");
		this.mStub.applicationDestroy(paramActivity);
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		JFupdateUtils.onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"onActivityResult");
		this.mStub.onActivityResult(paramActivity, paramInt1, paramInt2,
				paramIntent);

	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		if (mActivity != null) {
			GameApitest.getGameApitestInstants(mActivity).sendTest(
					"onNewIntent");
		}

		this.mStub.onNewIntent(paramIntent);
	}

	@Override
	public void initSdk(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("initSdk");

		// 为了兼容老sdk判断是否有初始化方法再执行
		this.mStub.initSdk(paramActivity);
	}

	/**
	 * 判断本地的等级与线上设定的等级谁大
	 * 
	 * @return
	 */
	public static boolean compareLevel(String templevel, String xianshanglevel) {
		int xian = Integer.parseInt(xianshanglevel);
		Yayalog.loger("xian" + xianshanglevel);
		if (xian < 1) {
			return true;
		}
		try {
			int templ = Integer.parseInt(templevel);

			if (templ > xian) {
				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			Yayalog.loger("判断等级" + e.toString());
		}

		return false;
	}

	public void launchActivityOnCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants(paramActivity).sendTest(
				"launchActivityOnCreate");
		this.mStub.launchActivityOnCreate(paramActivity);
	}

	public void launchActivityonOnNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants().sendTest(
				"launchActivityonOnNewIntent");
		this.mStub.launchActivityonOnNewIntent(paramIntent);
	}

	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants().sendTest(
				"onRequestPermissionsResult");

		this.mStub.onRequestPermissionsResult(requestCode, permissions,
				grantResults);
	}

	public void onTouchEvent(MotionEvent event) {

		Yayalog.loger("按下了" + event.getX());

	}

	//是否验证手机
	public void isVerifyphone() {

		//Yayalog.loger("按下了" + event.getX());

	}
	
	//是否实名认证
	public void doGetVerifiedInfo(Activity arg0,YYWApiCallback myywapicallback) {

			//Yayalog.loger("按下了" + event.getX());
		Method[] methods;
		try {
			methods = Class.forName("com.yayawan.impl.UserManagerImpl")
					.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("doGetVerifiedInfo")) {
					// com.yayawan.proxy.YYWActivityStub
					Yayalog.loger(methods[i].getName());
					Yayalog.loger("有接口doGetVerifiedInfo");
					UserManagerImpl mani = (UserManagerImpl) this.mUserManager;
					mani.doGetVerifiedInfo(arg0,myywapicallback);

				}
				// System.out.println("没有初始化方法");
			//	Yayalog.loger("UserManagerImpl中没有setdata方法");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
