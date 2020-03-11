package com.yayawan.sdk.login;

import java.math.BigInteger;
import java.util.UUID;
import java.util.zip.CRC32;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
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

import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.db.UserDao;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.Basedialogview;
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

public class AcountRegister_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageView iv_mPre;
	private EditText et_mUser;
	private EditText et_mPassword;
	private ImageView ib_mAgreedbox;
	private Button bt_mOk;
	private String mName;
	private String mPassword;

	private User mUser;
	private static final int REGISTER = 3;

	private static final int FETCHSMS = 4;

	protected static final int ERROR = 5;

	private ImageView ib_mNotAgreedbox;
	private Button bt_mPhoneRegister;
	private Button bt_mAccountRegister;
	private ImageView iv_mUn_icon;
	private ImageView iv_mPassword_icon;
	private ImageView iv_mRePassword_icon;
	private EditText et_mRePassword;
	private RelativeLayout rl_mFind;

	public AcountRegister_dialog(Activity activity) {
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
					"yaya1_userregistertitleicon.png", mActivity));		
		
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
		// 用户名输入列
		LinearLayout ll_mUser = new LinearLayout(mActivity);
		ll_mUser = (LinearLayout)machineFactory.MachineView(ll_mUser,
				MATCH_PARENT, 120, 0, "LinearLayout", 20, 16, 20, 0, 100);
		ll_mUser.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		ll_mUser.setGravity(Gravity.CENTER);

		// phone 的icon
		iv_mUn_icon = new ImageView(mActivity);
		iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 235,
				120, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya1_usernameicon.png", mActivity));

		// username的edtext
		et_mUser = new EditText(mActivity);
		et_mUser = machineFactory.MachineEditText(et_mUser, 0, 95, 1,
						"用户名：6-8位数字或者字母", 34, mLinearLayout, 0, 4, 0, 0);
		et_mUser.setTextColor(Color.BLACK);
		et_mUser.setHintTextColor(Color.parseColor("#b4b4b4"));
		et_mUser.setBackgroundColor(Color.TRANSPARENT);

			

				// TODO
		ll_mUser.addView(iv_mUn_icon);
		ll_mUser.addView(et_mUser);
		
		
				//验证码输入列	
		LinearLayout ll_mPassword = new LinearLayout(mActivity);
		ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
						MATCH_PARENT, 120, 0, "LinearLayout", 20, 15, 20, 0, 100);
		ll_mPassword.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		// 设置验证码输入框和获取验证码button
	
		ll_mPassword.setOrientation(LinearLayout.HORIZONTAL);
				
		//ll_mSecurity.setBackgroundDrawable(GetAssetsutils
			//					.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

		ll_mPassword.setGravity(Gravity.CENTER);

		// username 的icon
		iv_mPassword_icon = new ImageView(mActivity);
		iv_mPassword_icon = (ImageView) machineFactory.MachineView(iv_mPassword_icon, 235,
				120,  0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mPassword_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
								"yaya1_setpassword.png", mActivity));

		// username的edtext
		et_mPassword = new EditText(mActivity);
		et_mPassword = machineFactory.MachineEditText(et_mPassword, 0, 135, 1,
								"请输入验证码", 34, mLinearLayout, 0, 4, 0, 0);
		et_mPassword.setTextColor(Color.BLACK);
		et_mPassword.setHintTextColor(Color.parseColor("#b4b4b4"));
		et_mPassword.setBackgroundColor(Color.TRANSPARENT);

						// TODO
		ll_mPassword.addView(iv_mPassword_icon);
		ll_mPassword.addView(et_mPassword);
						
					


						// 重复密码输入列
						LinearLayout ll_mRePassword = new LinearLayout(mActivity);
						ll_mRePassword = (LinearLayout) machineFactory.MachineView(ll_mRePassword,
								MATCH_PARENT,120, 0, "LinearLayout", 20, 15, 20, 0, 100);
						ll_mRePassword.setBackgroundDrawable(GetAssetsutils
								.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
						ll_mRePassword.setGravity(Gravity.CENTER);

								// username 的icon
						iv_mRePassword_icon = new ImageView(mActivity);
						iv_mRePassword_icon = (ImageView) machineFactory.MachineView(iv_mRePassword_icon, 235,
								120, 0, mLinearLayout, 20, 0, 0, 0, 100);
						iv_mRePassword_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
										"yaya1_repassword.png", mActivity));

								// username的edtext
						et_mRePassword = new EditText(mActivity);
						et_mRePassword = machineFactory.MachineEditText(et_mRePassword, 0, 135, 1,
										"请设置密码（6-20位字母或者数字）", 34, mLinearLayout, 0, 4, 0, 0);
						et_mRePassword.setTextColor(Color.BLACK);
						et_mRePassword.setHintTextColor(Color.parseColor("#b4b4b4"));
						et_mRePassword.setBackgroundColor(Color.TRANSPARENT);

							

								// TODO
						ll_mRePassword.addView(iv_mRePassword_icon);
						ll_mRePassword.addView(et_mRePassword);
						

		

		
		
	

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
				"yaya1_phoneregisterbuttonicon.png", mActivity));
		bt_mAccountRegister.setGravity(Gravity_CENTER);
		// 点击事件..点击打开账号注册窗口
		bt_mAccountRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// TODO
		rl_mFind.addView(ll_clause);
		rl_mFind.addView(ll_zhanwei2);
		rl_mFind.addView(bt_mAccountRegister);
		
		
		
		
		//========================================================
		
		//========================================================
		
		
		// TODO
		ll_content1.addView(ll_mUser);
		ll_content1.addView(ll_mPassword);
		ll_content1.addView(ll_mRePassword);
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

		initlogic();
	}

	private void initlogic() {
		UUID uuid = UUID.randomUUID();
		CRC32 crc32 = new CRC32();
		crc32.update(uuid.toString().getBytes());

		et_mUser.setText("kk" + crc32.getValue());
		// et_mPassword.setText(CryptoUtil.getSeed());

		bt_mOk.setOnClickListener(new OnClickListener() {

			private String mRePassword;

			@Override
			public void onClick(View v) {
				mName = et_mUser.getText().toString().trim();
				mPassword = et_mPassword.getText().toString().trim();
				mRePassword = et_mRePassword.getText().toString().trim();
				
				  if (ib_mAgreedbox.getVisibility() == View.GONE) {
				 Toast.makeText(mContext, "请同意协议", Toast.LENGTH_SHORT)
				 .show(); return; 
				 }
				 if(!mPassword.equals(mRePassword)){
					 Yayalog.loger(mPassword);
					 Yayalog.loger(mRePassword);
					 Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT)
						.show();
					 return;
				 }
				if (mName.equals("")) {
					Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.equals("")) {
					Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() < 6) {
					Toast.makeText(mContext, "用户名不能小于六位", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() > 20) {
					Toast.makeText(mContext, "用户名不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.length() < 6) {
					Toast.makeText(mContext, "密码不能小于六位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.length() > 20) {
					Toast.makeText(mContext, "密码不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在快速注册...");
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					rps.addBodyParameter("imei", DeviceUtil.getIMEI(mActivity));
					rps.addBodyParameter("username", mName);
					rps.addBodyParameter("password", mPassword);
					rps.addBodyParameter("uuid", DeviceUtil.getUUID(mActivity));
					Yayalog.loger("app_id:" + DeviceUtil.getAppid(mActivity)
							+ "imei" + DeviceUtil.getIMEI(mActivity)
							+ "username" + mName + "password" + mPassword);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.acountregister, rps,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "注册失败，请检查网络", 0)
											.show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Yayalog.loger("注册返回信息" + result.result);
									Utilsjf.stopDialog();
									User user = parserAcountRegisterResult(result.result);
									if (user != null) {
										
										AgentApp.mUser = user;
										Yayalog.loger(AgentApp.mUser.toString());
										// 将base64加密的用户信息保存到数据库
										UserDao.getInstance(mContext)
												.writeUser(
														AgentApp.mUser
																.getUserName(),
														AgentApp.mUser.password,
														"123");
										AgentApp.mUser.password="";
										Yayalog.loger("登陆的uid："+user.toString());
										allDismiss();
										Login_success_dialog login_success_dialog = new Login_success_dialog(
												mActivity);
										login_success_dialog.dialogShow();

									}

								}
							});
				}
			}
		});

	}

	/**
	 * 解析账号注册后的结果
	 * 
	 * @param result
	 */
	private User parserAcountRegisterResult(String result) {
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
			user.setUserName(datas.optString("username"));
			user.setToken(datas.optString("token"));
			user.setPassword(mPassword);
			user.setUid(new BigInteger(datas.optString("uid")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
