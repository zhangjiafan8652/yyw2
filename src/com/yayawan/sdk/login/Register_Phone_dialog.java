package com.yayawan.sdk.login;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.sdk.bean.Result;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.db.UserDao;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.AuthNumReceiver;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.utils.CodeCountDown;
import com.yayawan.sdk.utils.CounterDown;
import com.yayawan.sdk.utils.ToastUtil;
import com.yayawan.sdk.utils.Utilsjf;

import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class Register_Phone_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageView iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private ImageView ib_mAgreedbox;
	private Button bt_mOk;
	
	private String mPhoneNum;
	private String mCode;

	private static final int AUTHCODE = 5;
	protected static final int ERROR = 11;
	protected static final int LOGINSECURITYRESULT = 8;

	private ImageView ib_mNotAgreedbox;

	private CounterDown mCountDown;
	private EditText et_mPassword;
	private Button bt_mPhoneRegister;
	private Button bt_mAccountRegister;
	private ImageView iv_mUn_icon;
	private ImageView iv_mSecurity_icon;
	private ImageView iv_mPassword_icon;
	private LinearLayout ll_mTitelimage;
	private RelativeLayout rl_mFind;

	public Register_Phone_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(final Activity mActivity) {

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		
		int height = 842;
		int with = 1000;

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
	
		baselin.setGravity(Gravity.CENTER_VERTICAL);
		baselin.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackgroundpink.9.png",mActivity));
		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		
		//================================头部 开始===========================
		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title,
				MATCH_PARENT, 140, 0, mLinearLayout, 0, 0, 0, 0, 100);
		//rl_title.setBackgroundColor(Color.parseColor("#fffff3"));
		rl_title.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 50, MATCH_PARENT, 0,
				mRelativeLayout,30, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageView(mActivity);
		machineFactory.MachineView(iv_mPre, 50, 50, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya1_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		// 头部icon
		ImageView iv_icon = new ImageView(mActivity);
		machineFactory.MachineView(iv_icon,520, 150, 0, mRelativeLayout, 440, 20, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		
		//如果是sdktpye为1的话，就隐藏背景
		
		iv_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya1_register.png", mActivity));		
		
		// TODO
		rl_title.addView(iv_icon);

		rl_title.addView(ll_mPre);
		//================================头部结束===========================

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mActivity);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				MATCH_PARENT, MATCH_PARENT, 0, mLinearLayout, 0, 0, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		//ll_content1.setBackgroundDrawable(GetAssetsutils
			//	.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		// 手机号码输入列
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout)machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 120, 0, "LinearLayout", 20, 16, 20, 0, 100);
		ll_phone.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		ll_phone.setGravity(Gravity.CENTER);

		// phone 的icon
		iv_mUn_icon = new ImageView(mActivity);
		iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 235,
				120, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya1_phonenumbericon.png", mActivity));

		// username的edtext
		et_mPhone = new EditText(mActivity);
		et_mPhone = machineFactory.MachineEditText(et_mPhone, 0, 95, 1,
						"请输入手机号", 34, mLinearLayout, 0, 4, 0, 0);
		et_mPhone.setTextColor(Color.BLACK);
		et_mPhone.setHintTextColor(Color.parseColor("#b4b4b4"));
		et_mPhone.setBackgroundColor(Color.TRANSPARENT);

			

				// TODO
		ll_phone.addView(iv_mUn_icon);
	    ll_phone.addView(et_mPhone);
		
		
				//验证码输入列	
		LinearLayout ll_mSecurityandbutton = new LinearLayout(mActivity);
		ll_mSecurityandbutton = (LinearLayout) machineFactory.MachineView(ll_mSecurityandbutton,
						MATCH_PARENT, 120, 0, "LinearLayout", 20, 15, 20, 0, 100);
		ll_mSecurityandbutton.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		// 设置验证码输入框和获取验证码button
		LinearLayout ll_mSecurity = new LinearLayout(mActivity);
		ll_mSecurity = (LinearLayout) machineFactory.MachineView(ll_mSecurity,
						680, 135, 0, "LinearLayout", 0, 0, 0, 0, 100);
		ll_mSecurity.setOrientation(LinearLayout.HORIZONTAL);
				
		//ll_mSecurity.setBackgroundDrawable(GetAssetsutils
			//					.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

		ll_mSecurity.setGravity(Gravity.CENTER);

		// username 的icon
		iv_mSecurity_icon = new ImageView(mActivity);
		iv_mSecurity_icon = (ImageView) machineFactory.MachineView(iv_mSecurity_icon, 235,
				120,  0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mSecurity_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
								"yaya1_verifycode.png", mActivity));

		// username的edtext
		et_mSecurity = new EditText(mActivity);
						et_mSecurity = machineFactory.MachineEditText(et_mSecurity, 0, 135, 1,
								"请输入验证码", 34, mLinearLayout, 0, 4, 0, 0);
						et_mSecurity.setTextColor(Color.BLACK);
						et_mSecurity.setHintTextColor(Color.parseColor("#b4b4b4"));
						et_mSecurity.setBackgroundColor(Color.TRANSPARENT);

						// TODO
						ll_mSecurity.addView(iv_mSecurity_icon);
						ll_mSecurity.addView(et_mSecurity);
						
						// 获取验证码按钮
						bt_mGetsecurity = new Button(mActivity);
						bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 224,
								68, 0, "", 28, mLinearLayout, 20, 30, 0, 0);
						bt_mGetsecurity.setTextColor(Color.WHITE);
						bt_mGetsecurity.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_getverifycodebutton.png", mActivity));
						
						
						bt_mGetsecurity.setPadding(0, 0, 0, 0);
						bt_mGetsecurity.setGravity(Gravity.CENTER);
						
				
						ll_mSecurityandbutton.addView(ll_mSecurity);
						ll_mSecurityandbutton.addView(bt_mGetsecurity);
			

						// 密码输入列
						LinearLayout ll_mPassword = new LinearLayout(mActivity);
						ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
								MATCH_PARENT,120, 0, "LinearLayout", 20, 15, 20, 0, 100);
						ll_mPassword.setBackgroundDrawable(GetAssetsutils
								.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
						ll_mPassword.setGravity(Gravity.CENTER);

								// username 的icon
						iv_mPassword_icon = new ImageView(mActivity);
						iv_mPassword_icon = (ImageView) machineFactory.MachineView(iv_mPassword_icon, 235,
								120, 0, mLinearLayout, 20, 0, 0, 0, 100);
						iv_mPassword_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
										"yaya1_setphoneregisterpassword.png", mActivity));

								// username的edtext
								et_mPassword = new EditText(mActivity);
								et_mPassword = machineFactory.MachineEditText(et_mPassword, 0, 135, 1,
										"请设置密码（6-20位字母或者数字）", 34, mLinearLayout, 0, 4, 0, 0);
								et_mPassword.setTextColor(Color.BLACK);
								et_mPassword.setHintTextColor(Color.parseColor("#b4b4b4"));
								et_mPassword.setBackgroundColor(Color.TRANSPARENT);

							

								// TODO
								ll_mPassword.addView(iv_mPassword_icon);
								ll_mPassword.addView(et_mPassword);
						

		

		
		
	

		// 确定按钮
	
		bt_mOk = new Button(mActivity);
		bt_mOk = machineFactory.MachineButton(bt_mOk, 380,
				120, 0, "", 32, mLinearLayout, 306, 20, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		//yaya1_register.png
		bt_mOk.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_registerbut.png", mActivity));
		
		bt_mOk.setGravity(Gravity_CENTER);
		
		
		
		
		
		//========================================================
		

		//找回密码列
		rl_mFind = new RelativeLayout(mActivity);
		rl_mFind = (RelativeLayout) machineFactory.MachineView(rl_mFind,
				MATCH_PARENT, 130, 0, mLinearLayout, 0, 21, 0, 0, 100);
		rl_mFind.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		// 横版手机登录按钮
		
		// 条款
				LinearLayout ll_clause = new LinearLayout(mActivity);
				machineFactory.MachineView(ll_clause, 450, MATCH_PARENT, mRelativeLayout,
						4, 0);
				ll_clause.setGravity(Gravity.CENTER_VERTICAL);

				// 同意服务条款
				ib_mAgreedbox = new ImageView(mActivity);
				machineFactory.MachineView(ib_mAgreedbox, 70, 70, mLinearLayout, 2, 0);
				ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya_checkedbox.png", mActivity));
				ib_mAgreedbox.setBackgroundDrawable(null);

				// 不同意服务条款
				ib_mNotAgreedbox = new ImageView(mActivity);
				machineFactory.MachineView(ib_mNotAgreedbox, 70, 70, mLinearLayout, 2,
						0);
				ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya_uncheckedbox.png", mActivity));
				ib_mNotAgreedbox.setBackgroundDrawable(null);
				ib_mNotAgreedbox.setVisibility(View.GONE);
				

				TextView tv_agree = new TextView(mActivity);
				machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
						"同意《用户协议》", 32, mLinearLayout, 6, 0, 0, 0);
				tv_agree.setTextColor(Color.parseColor("#b4b4b4"));
				tv_agree.setGravity(Gravity.CENTER_VERTICAL);
				tv_agree.setClickable(true);
				tv_agree.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						YYprotocol_ho_dialog yYprotocol_ho_dialog = new YYprotocol_ho_dialog(
								mActivity);
						yYprotocol_ho_dialog.dialogShow();
					}
				});

		// TODO
		ll_clause.addView(ib_mAgreedbox);
		ll_clause.addView(ib_mNotAgreedbox);
		ll_clause.addView(tv_agree);
		ib_mAgreedbox.setClickable(true);
		ib_mAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mAgreedbox.setVisibility(View.GONE);
				ib_mNotAgreedbox.setVisibility(View.VISIBLE);
			}
		});
		ib_mNotAgreedbox.setClickable(true);
		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {

					@Override
				public void onClick(View v) {
					ib_mNotAgreedbox.setVisibility(View.GONE);
					ib_mAgreedbox.setVisibility(View.VISIBLE);
				}
			});
				
		
		

		LinearLayout ll_zhanwei2 = new LinearLayout(mActivity);
		ll_zhanwei2 = (LinearLayout) machineFactory.MachineView(ll_zhanwei2, 270,
				MATCH_PARENT, mRelativeLayout);
		// button的登录按钮
		// 用戶名注冊
		bt_mAccountRegister = new Button(mActivity);
		machineFactory.MachineButton(bt_mAccountRegister, 235, 80, 0, "", 28,
				mRelativeLayout, 720, 20, 0, 0);
		bt_mAccountRegister.setTextColor(Color.WHITE);
		
		bt_mAccountRegister.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya1_acountregisterbutton.png", mActivity));
		bt_mAccountRegister.setGravity(Gravity_CENTER);
		// 点击事件..点击打开账号注册窗口
		bt_mAccountRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AcountRegister_dialog acountRegister_ho_dialog = new AcountRegister_dialog(
						mActivity);
				acountRegister_ho_dialog.dialogShow();
			}
		});

		// TODO
		rl_mFind.addView(ll_clause);
		rl_mFind.addView(ll_zhanwei2);
		rl_mFind.addView(bt_mAccountRegister);
		
		
		
		
		//========================================================
		
		//========================================================
		
		
		// TODO
		ll_content1.addView(ll_phone);
		ll_content1.addView(ll_mSecurityandbutton);
		ll_content1.addView(ll_mPassword);
		//ll_content1.addView(ll_clause);
		
		// ll_content1.addView(ll_clause);
		ll_content1.addView(bt_mOk);
		
		ll_content1.addView(rl_mFind);

		ll_content.addView(rl_title);

		ll_content.addView(ll_content1);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 1f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initLogic();
	}

	private String mPassword;

	private void initLogic() {

		
		mCountDown = CounterDown.getInstance(mActivity);
		
		mCountDown.setView(bt_mGetsecurity);
		// 获取验证码
		bt_mGetsecurity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mPhoneNum = et_mPhone.getText().toString().trim();
				
				  if (ib_mAgreedbox.getVisibility() == View.GONE) {
				  Toast.makeText(mActivity, "请同意服务协议", Toast.LENGTH_SHORT)
				  .show(); 
				  return;
				  }
				 
				if (mPhoneNum.equals("")) {
					Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNum.length() < 11) {
					Toast.makeText(mActivity, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else {

					Utilsjf.creDialogpro(mActivity, "正在获取验证码...");

					RequestParams rps = new RequestParams();
					rps.addBodyParameter("type", 2 + "");
					
					
					rps.addBodyParameter("mobile", mPhoneNum);
					rps.addBodyParameter("uuid", DeviceUtil.getUUID(mActivity));
					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST, ViewConstants.getphonecode,
							rps, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "验证码发送失败，请检查网络",
											0).show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									
									Yayalog.loger(result.result);
									try {
										JSONObject jsonObject = new JSONObject(result.result);
										String errmsg = jsonObject.getString("err_msg");
										if (errmsg.equals("success")) {
											mCountDown.startCounter();
											Toast.makeText(mActivity, "验证码已经发送", 0)
											.show();
										}else{
											Toast.makeText(mActivity, errmsg, 0)
											.show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									

								}
							});

				}
			}
		});

		// 获取到验证码后点击注册

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhoneNum = et_mPhone.getText().toString().trim();
				mCode = et_mSecurity.getText().toString().trim();
				mPassword = et_mPassword.getText().toString().trim();
				if (mPhoneNum.equals("")) {
					Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNum.length() < 11) {
					Toast.makeText(mActivity, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else if (mCode.equals("")) {
					Toast.makeText(mActivity, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在登录...");
					// 验证码登录
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					rps.addBodyParameter("imei", DeviceUtil.getIMEI(mActivity));
					rps.addBodyParameter("mobile", mPhoneNum);
					rps.addBodyParameter("password", mPassword);
					rps.addBodyParameter("code", mCode);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.phoneregister, rps,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "注册失败，请检查网络是否畅通",
											0).show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();

									Yayalog.loger("手机注册结果" + result.result);
									//Toast.makeText(mActivity, "注册成功", 0).show();

									User user = parserPhoneRegisterResult(result.result);
									if (user == null) {
										return;
									}
									AgentApp.mUser = user;
									Yayalog.loger("手机注册结果"
											+ AgentApp.mUser.toString());
									// System.out.println("手机注册接口返回数据"+loginuser.secret);
									// 将base64加密的用户信息保存到数据库
									UserDao.getInstance(mActivity).writeUser(
											user.userName, user.password,
											user.secret);
									// System.out.println("手机得到的账号密码是0"+loginuser);
									user.password = "";
									user.secret = "";
									// 开启悬浮窗服务
									// YayaWan.init(mActivity);

									allDismiss();

									Login_success_dialog login_success_dialog = new Login_success_dialog(
											mActivity);
									login_success_dialog.dialogShow();
								}

							});
				}
			}
		});

	}

	/**
	 * 解析手机注册后的结果
	 * 
	 * @param result
	 */
	private User parserPhoneRegisterResult(String result) {
		// TODO Auto-generated method stub[
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!result.contains("success")) {
				String errmsg = jsonObject.optString("err_msg");
				Toast.makeText(mActivity, errmsg, 0).show();
				return null;
			}
			JSONObject datas = jsonObject.getJSONObject("data");
			User user = new User();
			user.setPhone(datas.optString("mobile"));
			user.setUserName(datas.optString("mobile"));
			user.setToken(datas.optString("token"));
			user.setPassword(mPassword);
			// new BigInteger(datas.optString("uid"));
			user.setUid(new BigInteger(datas.optString("uid")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
