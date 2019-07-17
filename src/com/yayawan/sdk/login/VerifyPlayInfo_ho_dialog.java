package com.yayawan.sdk.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import com.yayawan.main.YYWMain;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.db.UserDao;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.AuthNumReceiver;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.utils.CodeCountDown;
import com.yayawan.sdk.utils.CounterDown;
import com.yayawan.sdk.utils.Utilsjf;
import com.yayawan.sdk.utils.AuthNumReceiver.MessageListener;
import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class VerifyPlayInfo_ho_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mUsername;
	private EditText et_mNewpassword;
	private Button bt_mOk;
	private String mUserName;
	private String mCode;
	private String mNewPassword;


	protected static final int ERROR = 9;


	private CounterDown mCountDown;
	private ImageView iv_mSecurity_icon;
	private ImageView iv_mUn_icon;

	public VerifyPlayInfo_ho_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {

		//onStart();

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		int height = 560;
		int with = 630;

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
		//machineFactory.MachineView(baselin, with, height, "LinearLayout");
		//baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);
		baselin.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title,
				MATCH_PARENT, 78, 0, mLinearLayout, 35, 28, 35, 0, 100);
		rl_title.setBackgroundColor(Color.parseColor("#ffffff"));

		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 46, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mActivity);
		machineFactory.MachineView(iv_mPre, 46, 46, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya1_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		

		// 注册textview
		TextView tv_zhuce = new TextView(mActivity);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"实名认证", 44, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.parseColor("#c05011"));
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
				LinearLayout ll_content1 = new LinearLayout(mActivity);
				ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
						height, MATCH_PARENT, 0, mLinearLayout, 35, 0, 35, 0,
						LinearLayout.VERTICAL);
				ll_content1.setOrientation(LinearLayout.VERTICAL);
		
		// 身份证号码输入列
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 65, 0, "LinearLayout", 0, 30, 0, 0, 100);

		ll_phone.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

		ll_phone.setGravity(Gravity.CENTER);

				// username 的icon
				iv_mUn_icon = new ImageView(mActivity);
				iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 30,
						30, 0, mLinearLayout, 20, 0, 0, 0, 100);
				iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya1_phoneicon.png", mActivity));

				// username的edtext
				et_mPhone = new EditText(mActivity);
				et_mPhone = machineFactory.MachineEditText(et_mPhone, 0, MATCH_PARENT, 1,
						"请输入身份证", 22, mLinearLayout, 0, 4, 0, 0);
				et_mPhone.setTextColor(Color.BLACK);
				et_mPhone.setHintTextColor(Color.parseColor("#b4b4b4"));
				et_mPhone.setBackgroundColor(Color.TRANSPARENT);

			

				// TODO
				ll_phone.addView(iv_mUn_icon);
				ll_phone.addView(et_mPhone);
		
		
				//名字输入列	
				LinearLayout ll_mSecurityandbutton = new LinearLayout(mActivity);
				ll_mSecurityandbutton = (LinearLayout) machineFactory.MachineView(ll_mSecurityandbutton,
						MATCH_PARENT, 65, 0, "LinearLayout", 0, 30, 0, 0, 100);

				// 设置验证码输入框和获取验证码button
				LinearLayout ll_mSecurity = new LinearLayout(mActivity);
				ll_mSecurity = (LinearLayout) machineFactory.MachineView(ll_mSecurity,
						270, 65, 0, "LinearLayout", 0, 0, 0, 0, 100);
				ll_mSecurity.setOrientation(LinearLayout.HORIZONTAL);
				
				ll_mSecurity.setBackgroundDrawable(GetAssetsutils
								.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

				ll_mSecurity.setGravity(Gravity.CENTER);

						// username 的icon
				iv_mSecurity_icon = new ImageView(mActivity);
				iv_mSecurity_icon = (ImageView) machineFactory.MachineView(iv_mSecurity_icon, 30,
								30, 0, mLinearLayout, 20, 0, 0, 0, 100);
				iv_mSecurity_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
								"yaya1_codeicon.png", mActivity));

						// username的edtext
						et_mUsername = new EditText(mActivity);
						et_mUsername = machineFactory.MachineEditText(et_mUsername, 0, MATCH_PARENT, 1,
								"请输入姓名", 22, mLinearLayout, 0, 4, 0, 0);
						et_mUsername.setTextColor(Color.BLACK);
						et_mUsername.setHintTextColor(Color.parseColor("#b4b4b4"));
						et_mUsername.setBackgroundColor(Color.TRANSPARENT);

						// TODO
						ll_mSecurity.addView(iv_mSecurity_icon);
						ll_mSecurity.addView(et_mUsername);
						
						// 获取验证码按钮
						bt_mGetsecurity = new Button(mActivity);
						bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 270,
								MATCH_PARENT, 0, "获取验证码", 22, mLinearLayout, 20, 0, 0, 0);
						bt_mGetsecurity.setTextColor(Color.WHITE);
						bt_mGetsecurity.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
								"yaya1_loginbutton.9.png", "yaya1_loginbutton.9.png",
								mActivity));
						
						
						bt_mGetsecurity.setGravity(Gravity.CENTER);
						
				
						ll_mSecurityandbutton.addView(ll_mSecurity);
					//	ll_mSecurityandbutton.addView(bt_mGetsecurity);
		
		
		// 确定按钮
		bt_mOk = new Button(mActivity);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 78, 0, "确定", 36,
				mLinearLayout, 0, 50, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya1_loginbutton.9.png", "yaya1_loginbutton.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_content1.addView(ll_phone);
		ll_content1.addView(ll_mSecurityandbutton);
		
	
		
		ll_content1.addView(bt_mOk);

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

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {
		
		
		
	
		mCountDown = CounterDown.getInstance(mActivity);
		mCountDown.setView(bt_mGetsecurity);
		


		bt_mOk.setOnClickListener(new OnClickListener() {

			private String mIdCard;
			private String mUsername;

			@Override
			public void onClick(View v) {
				mUsername = et_mUsername.getText().toString().trim();
				mIdCard = et_mPhone.getText().toString().trim();
				if (TextUtils.isEmpty(mUsername)) {
					Toast.makeText(mActivity, "请输入姓名", Toast.LENGTH_SHORT)
							.show();
				} else if (!isIDNumber(mIdCard)) {
					Toast.makeText(mActivity, "请输入正确的身份证号", Toast.LENGTH_SHORT)
							.show();
				} else {
					//
					
					Utilsjf.creDialogpro(mActivity, "正在实名认证...");
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					
				//	rps.addBodyParameter("app_id", DeviceUtil.getAppid(mactivity));
					rps.addBodyParameter("uid", YYWMain.mUser.uid);
					rps.addBodyParameter("token", YYWMain.mUser.token);
					
					
					rps.addBodyParameter("relname", mUsername);
					rps.addBodyParameter("creditno", mIdCard);
					
					Yayalog.loger("url:"+ViewConstants.SHIMINGRENZHENG);
					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.SHIMINGRENZHENG, rps,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub

									Utilsjf.stopDialog();
									DgameSdk.mSdkApiCallback.onVerifyCancel();
									Toast.makeText(mActivity, "请检查网络是否畅通", 0)
											.show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									//找回密码：{"err_code":0,"err_msg":"success"}
									//06-24 16:05:55.045: E/yayawanYayalog(3395): 实名认证返回：{"err_code":0,"err_msg":"success"}

									Utilsjf.stopDialog();
									Yayalog.loger("实名认证返回："+result.result);
									
									try {
										JSONObject jsonObject = new JSONObject(result.result);
										
										String msg=	(String) jsonObject.optString("err_msg");
										if (msg.contains("success")) {
											DgameSdk.mSdkApiCallback.onVerifySuccess(result.result);
											dialogDismiss();
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

	}

	  public static boolean isIDNumber(String IDNumber) {
	        if (IDNumber == null || "".equals(IDNumber)) {
	            return false;
	        }
	        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
	        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
	                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
	        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
	        //^开头
	        //[1-9] 第一位1-9中的一个      4
	        //\\d{5} 五位数字           10001（前六位省市县地区）
	        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
	        //\\d{2}                    91（年份）
	        //((0[1-9])|(10|11|12))     01（月份）
	        //(([0-2][1-9])|10|20|30|31)01（日期）
	        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
	        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
	        //$结尾

	        //假设15位身份证号码:410001910101123  410001 910101 123
	        //^开头
	        //[1-9] 第一位1-9中的一个      4
	        //\\d{5} 五位数字           10001（前六位省市县地区）
	        //\\d{2}                    91（年份）
	        //((0[1-9])|(10|11|12))     01（月份）
	        //(([0-2][1-9])|10|20|30|31)01（日期）
	        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
	        //$结尾


	        boolean matches = IDNumber.matches(regularExpression);

	        //判断第18位校验值
	        if (matches) {

	            if (IDNumber.length() == 18) {
	                try {
	                    char[] charArray = IDNumber.toCharArray();
	                    //前十七位加权因子
	                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	                    //这是除以11后，可能产生的11位余数对应的验证码
	                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
	                    int sum = 0;
	                    for (int i = 0; i < idCardWi.length; i++) {
	                        int current = Integer.parseInt(String.valueOf(charArray[i]));
	                        int count = current * idCardWi[i];
	                        sum += count;
	                    }
	                    char idCardLast = charArray[17];
	                    int idCardMod = sum % 11;
	                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
	                        return true;
	                    } else {
	                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() + 
	                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
	                        return false;
	                    }

	                } catch (Exception e) {
	                    e.printStackTrace();
	                    System.out.println("异常:" + IDNumber);
	                    return false;
	                }
	            }

	        }
	        return matches;
	    }


}
