package com.yayawan.sdk.login;

import java.util.ArrayList;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.kkgamelib.R;
import com.yayawan.common.CommonData;
import com.yayawan.sdk.db.UserDao;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.utils.LoginUtils;
import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.Loginpo_listviewitem;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.PermissionUtils;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.SuperDialog;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;
import com.yayawan.utils.PermissionUtils.PermissionCheckCallBack;
import com.yayawan.utils.SuperDialog.onDialogClickListener;

public class Login_dialog extends Basedialogview {

	private LinearLayout ll_mUser;
	private ImageView iv_mUn_icon;
	private EditText et_mUn;
	private LinearLayout ll_mDown;
	private ImageView iv_mUn_down;
	private LinearLayout ll_mPassword;
	private ImageView iv_mPs_icon;
	private EditText et_mPs;
	private LinearLayout ll_mBut;
	private Button bt_mPhonelogin;
	private Button bt_mlogin;
	private ArrayList<String> mNames;
	private String mSelectUser;
	private String mPassword;
	private ListView lv_mHistoryuser;
	private String mName;
	protected static final int ERROR = 11;

	private TextView tv_fogetpassword;
	private RelativeLayout rl_fogetpassword;
	private Login_dialog login_ho_dialog;

	private LinearLayout ll_weibologin;
	private ImageView iv_weibologin;
	private TextView tv_weibologin;
	private LinearLayout ll_qqlogin;
	private ImageView iv_qqlogin;
	private TextView tv_qqlogin;
	private LinearLayout ll_mPasswordicon;
	private ImageView iv_mPasswordicon;
	
	private boolean isdisplaypassword=false;//是否显示password
	private TextView tv_QQlogin;
	private TextView tv_register;
	private TextView tv_clouse;
	private TextView tv_contactcustomerservice;
	private RelativeLayout rl_contactcustomerservice;
	public static boolean isshowdialog;
	String qqhao;
	private LinearLayout ll_mFind;
	private Button bt_mCustomerService;
	private Button bt_mFindPassWord;
	
	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	@Override
	public void createDialog(final Activity mActivity) {

		
		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		int height = 842;
		int with = 1000;
		
		
		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
	

		baselin.setBackgroundDrawable(GetAssetsutils
					.get9DrawableFromAssetsFile("yaya1_sdkbackgroundpink.9.png",mActivity));
			
		
		
		baselin.setGravity(Gravity.CENTER_VERTICAL);
		
		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_content, with, height, mLinearLayout, 2,
				0);
		//rl_content.setBackgroundColor(Color.parseColor("#fff1e8"));
		
	
        rl_content.setBackgroundDrawable(GetAssetsutils
								.get9DrawableFromAssetsFile("yaya1_sdkbackgroundpink.9.png",mActivity));
			
	  
		// 中间内容
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		ll_content.setBackgroundColor(Color.parseColor("#fff1e8"));
		ll_content.setBackgroundDrawable(GetAssetsutils
		.get9DrawableFromAssetsFile("yaya1_sdkbackgroundpink.9.png",mActivity));
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 头部
		LinearLayout ll_title = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_title, MATCH_PARENT, 165, mLinearLayout,0,0);
		ll_title.setGravity(Gravity_CENTER);
		ll_title.setOrientation(LinearLayout.VERTICAL);
		ll_title.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		// 头部icon
		ImageView iv_icon = new ImageView(mActivity);
		machineFactory.MachineView(iv_icon, 668, 165,
				mLinearLayout);
		
		//如果是sdktpye为1的话，就隐藏背景
		
		iv_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya1_logo.png", mActivity));		
		
		
		// TODO
		ll_title.addView(iv_icon);

		// 设置username的ll 注意背景的兼容性问题
		ll_mUser = new LinearLayout(mActivity);
		ll_mUser = (LinearLayout) machineFactory.MachineView(ll_mUser,
				MATCH_PARENT, 140, 0, "LinearLayout", 18, 20, 18, 0, 100);

		ll_mUser.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));

		ll_mUser.setGravity(Gravity.CENTER);

		// username 的icon
		iv_mUn_icon = new ImageView(mActivity);
		iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 200,
				140, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_username.png", mActivity));

		// username的edtext
		et_mUn = new EditText(mActivity);
		et_mUn = machineFactory.MachineEditText(et_mUn, 0, 140, 1,
				"请输入用户名", 30, mLinearLayout, 0, 6, 0, 0);
		et_mUn.setTextColor(Color.BLACK);
		et_mUn.setBackgroundColor(Color.TRANSPARENT);

		ll_mDown = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDown, 40, MATCH_PARENT, mLinearLayout,
				3, 10);
		ll_mDown.setGravity(Gravity_CENTER);
		ll_mDown.setClickable(true);

		// username的下拉图片
		iv_mUn_down = new ImageView(mActivity);
		iv_mUn_down = (ImageView) machineFactory.MachineView(iv_mUn_down, 40,
				40, 0, mLinearLayout, 0, 0, 5, 0, 100);
		iv_mUn_down.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_down.png", mActivity));
		iv_mUn_down.setClickable(false);

		ll_mDown.addView(iv_mUn_down);

		// TODO
		ll_mUser.addView(iv_mUn_icon);
		ll_mUser.addView(et_mUn);
		ll_mUser.addView(ll_mDown);

		// 设置password的ll 注意背景的兼容性问题
		ll_mPassword = new LinearLayout(mActivity);
		ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
				MATCH_PARENT, 140, 0, "LinearLayout",18, 20, 18, 0, 100);
		ll_mPassword.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		

		ll_mPassword.setGravity(Gravity.CENTER);

		// password 的icon
		iv_mPs_icon = new ImageView(mActivity);
		iv_mPs_icon = (ImageView) machineFactory.MachineView(iv_mPs_icon, 200,
				140, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mPs_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_password.png", mActivity));

		// password的edtext
		et_mPs = new EditText(mActivity);
		et_mPs = machineFactory.MachineEditText(et_mPs, 0, 140, 1,
				"请输入密码", 30, mLinearLayout, 0, 0, 0, 0);
		et_mPs.setBackgroundColor(Color.TRANSPARENT);
		et_mPs.setTextColor(Color.BLACK);
		et_mPs.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		// password显示的图片icon列
		ll_mPasswordicon = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPasswordicon, 45, MATCH_PARENT, mLinearLayout,
				3, 10);
		ll_mPasswordicon.setGravity(Gravity_CENTER);
		ll_mPasswordicon.setClickable(true);

		// password显示的图片icon
		iv_mPasswordicon = new ImageView(mActivity);
		iv_mPasswordicon = (ImageView) machineFactory.MachineView(iv_mPasswordicon, 45,
				45, 0, mLinearLayout, 0, 0, 5, 0, 100);
		iv_mPasswordicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_passworddisplay.png", mActivity));
		iv_mPasswordicon.setClickable(false);

		ll_mPasswordicon.addView(iv_mPasswordicon);
		
		
		// TODO
		ll_mPassword.addView(iv_mPs_icon);
		ll_mPassword.addView(et_mPs);
		ll_mPassword.addView(ll_mPasswordicon);

	
		
		//登陆按钮
		ll_mBut = new LinearLayout(mActivity);
		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
				MATCH_PARENT, 115, 0, mLinearLayout, 48, 35, 48, 0, 100);

		// 横版手机登录按钮
		bt_mPhonelogin = new Button(mActivity);
		bt_mPhonelogin = machineFactory.MachineButton(bt_mPhonelogin, 0,
				MATCH_PARENT, 1, "", 32, mLinearLayout, 0, 0, 0, 0);
		bt_mPhonelogin.setTextColor(Color.WHITE);
		//yaya1_register.png
		bt_mPhonelogin.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_registerbut.png", mActivity));
		
		bt_mPhonelogin.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 125,
				MATCH_PARENT, mLinearLayout);
		// button的登录按钮
		bt_mlogin = new Button(mActivity);
		bt_mlogin = machineFactory.MachineButton(bt_mlogin, 0, MATCH_PARENT, 1,
				"", 32, mLinearLayout, 0, 0, 0, 0);
		bt_mlogin.setTextColor(Color.WHITE);
		bt_mlogin.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_loginbutton.png", mActivity));
		
		bt_mlogin.setGravity(Gravity_CENTER);

		// TODO
		ll_mBut.addView(bt_mPhonelogin);
		ll_mBut.addView(ll_zhanwei);
		ll_mBut.addView(bt_mlogin);

		

		
		
		//找回密码列
		ll_mFind = new LinearLayout(mActivity);
		ll_mFind = (LinearLayout) machineFactory.MachineView(ll_mFind,
				MATCH_PARENT, 135, 0, mLinearLayout, 0, 80, 0, 0, 100);
		ll_mFind.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png", mActivity));
		// 横版手机登录按钮
		bt_mCustomerService = new Button(mActivity);
		bt_mCustomerService = machineFactory.MachineButton(bt_mCustomerService,245,
				85, 0, "", 32, mLinearLayout, 46, 20, 0, 0);
		bt_mCustomerService.setTextColor(Color.WHITE);
		//yaya1_lianxikefu.png
		bt_mCustomerService.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_lianxikefu.png", mActivity));
		
		bt_mCustomerService.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei2 = new LinearLayout(mActivity);
		ll_zhanwei2 = (LinearLayout) machineFactory.MachineView(ll_zhanwei2, 415,
				MATCH_PARENT, mLinearLayout);
		// button的登录按钮
		bt_mFindPassWord = new Button(mActivity);
		bt_mFindPassWord = machineFactory.MachineButton(bt_mFindPassWord,245, 80, 0,
				"", 32, mLinearLayout, 0, 20, 46, 0);
		bt_mFindPassWord.setTextColor(Color.WHITE);
		bt_mFindPassWord.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_findpassword.png", mActivity));
		
		bt_mFindPassWord.setGravity(Gravity_CENTER);

		// TODO
		ll_mFind.addView(bt_mCustomerService);
		ll_mFind.addView(ll_zhanwei2);
		ll_mFind.addView(bt_mFindPassWord);
		
		
		
		
		
		ll_content.addView(ll_title);//标题
		ll_content.addView(ll_mUser);//输入账户
		ll_content.addView(ll_mPassword);//输入密码
		
		//如果是sdktpye为1的话，就隐藏背景

		
		ll_content.addView(ll_mBut);// 登陆注册
		ll_content.addView(ll_mFind);//找回密码
		
	
		
		
		//ll_content.addView(rl_register);

		// 下拉选择历史账户
		lv_mHistoryuser = new ListView(mActivity);
		machineFactory.MachineView(lv_mHistoryuser, 700, WRAP_CONTENT, 0,
				"RelativeLayout", 20, 200, 20, 0,
				RelativeLayout.CENTER_HORIZONTAL);
		lv_mHistoryuser.setVisibility(View.GONE);

		rl_content.addView(ll_content);
		//rl_content.addView();
		
		rl_content.addView(lv_mHistoryuser);
		// ll_content.addView(chongzhihelp2);

		baselin.addView(rl_content);


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

	@Override
	public void dialogShow() {

		super.dialogShow();

	}

	
	public boolean etpasswordistext=false;
	/**
	 * 界面逻辑
	 */
	private void initlogic() {

		login_ho_dialog = this;

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

				if (AgentApp.mUser == null) {
					if (ViewConstants.TEMPLOGIN_HO != null) {
						return;
					}
					onCancel();
				}
			}
		});

		mNames = new ArrayList<String>();
		initDBData();
		// mNames

		// 忘记密码重设秘密
		bt_mFindPassWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (!(PermissionUtils.checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)&&PermissionUtils.checkPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
					SuperDialog superDialog = new SuperDialog(mActivity);
					if (DeviceUtil.isLandscape(mActivity)) {
						superDialog.setAspectRatio(0.8f);
					}
					superDialog.setTitle("亲爱的玩家");
					superDialog.setContent("请授予储存卡读写权限\r\n 读取储存卡中保存的账号密码\r\n 授权后重新打开游戏").setListener(new onDialogClickListener() {
						
						@Override
						public void click(boolean isButtonClick, int position) {
							// TODO Auto-generated method stub
							Yayalog.loger("请求权限对话框按钮按下");
							if (!(PermissionUtils.checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)&&PermissionUtils.checkPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
								PermissionUtils.checkMorePermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},new PermissionCheckCallBack() {
									
									@Override
									public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
										// TODO Auto-generated method stub
										// 用户之前已拒绝过权限申请
										//
										//PermissionUtils.requestMorePermissions(paramActivity, permission, PermissionUtils.READ_EXTERNAL_STORAGE);
									}
									
									@Override
									public void onUserHasAlreadyTurnedDown(String... permission) {
										// TODO Auto-generated method stub
										// 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
										//PermissionUtils.toAppSetting(paramActivity);
									}
									
									@Override
									public void onHasPermission() {
										// TODO Auto-generated method stub
										
									}
								});
							}
						}
					}).show();
				}else {
					ResetPassword_dialog resetPassword_ho_dialog = new ResetPassword_dialog(
							mActivity);
					resetPassword_ho_dialog.dialogShow();
				}
				
				
				

			}
		});

		// 下拉点击
		ll_mDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (View.GONE == lv_mHistoryuser.getVisibility()) {
					iv_mUn_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_up.png", mActivity));

					lv_mHistoryuser.setVisibility(View.VISIBLE);
				} else {
					iv_mUn_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_down.png", mActivity));
					lv_mHistoryuser.setVisibility(View.GONE);
				}

			}
		});

		// 登陆
		bt_mlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户名密码,实现登陆
				mName = et_mUn.getText().toString().trim();
				mPassword = et_mPs.getText().toString().trim();
				if (mName.equals("")) {
					Toast.makeText(mActivity, "用户名不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() < 4) {
					Toast.makeText(mActivity, "用户名不能小于4位", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() > 20) {
					Toast.makeText(mActivity, "用户名不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 输入的用户名和密码符合要求
					// TODO
					if (TextUtils.isEmpty(mPassword)) {
						Toast.makeText(mActivity, "密码不能为空,如忘记密码,请点击忘记密码~",
								Toast.LENGTH_SHORT).show();
						return;
					}
					
					ViewConstants.logintype=1;
					LoginUtils loginUtils = new LoginUtils(mActivity,
							login_ho_dialog, 0);
					loginUtils.login(mName, mPassword);

				}
			}
		});

		//隐藏和显示密码按钮
		ll_mPasswordicon.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (etpasswordistext) {
							etpasswordistext=false;
							String tempstring=et_mPs.getText().toString();
							et_mPs.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							et_mPs.setText(tempstring);
							iv_mPasswordicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
									"yaya1_passworddisplay.png", mActivity));
						}else{
							etpasswordistext=true;
							String tempstring=et_mPs.getText().toString();
							et_mPs.setInputType(InputType.TYPE_CLASS_TEXT);
							et_mPs.setText(tempstring);
							iv_mPasswordicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
									"yaya1_passwordhide.png", mActivity));
						}
					
					}
				});
		
		//联系客服
		 qqhao = Sputils.getSPstring("service_qq", "暂无", mActivity);
		 
		//如果是sdktpye为1的话，就隐藏背景
			if (DgameSdk.sdktype==1) {
				
				bt_mCustomerService.setOnClickListener(new OnClickListener() {
					
					@SuppressLint("NewApi") @Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						String qqhao1 = Sputils.getSPstring("service_qq", "暂无", mActivity);
						 // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
				        ClipboardManager cm = (ClipboardManager)mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
				        // 将文本内容放到系统剪贴板里。
				        cm.setText(qqhao1);
				        Toast.makeText(mActivity, "客服qq："+qqhao1+"复制成功，请到qq中添加好友", Toast.LENGTH_LONG).show();
						
					}
				});
			}else {
				
					
					bt_mCustomerService.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							//  https://rest.yayawan.com/static/chat/index.html?
							String qqhao1 = Sputils.getSPstring("service_id", "no", mActivity);
							if (qqhao1.equals("no")) {
								qqhao1= System.currentTimeMillis()+"";
								Sputils.putSPstring("service_id", qqhao1, mActivity);
							}
							String url=CommonData.BaseUrl+"static/chat/index.html?uid="+qqhao1;
							Yayalog.loger(url);
							Intent intent = new Intent(mActivity,AssistantActivity.class);
							intent.putExtra("mUrl", url);
							mActivity.startActivity(intent);
							
						}
					});
					
				
			}
		
	
		
		// 注册
		bt_mPhonelogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewConstants.logintype=2;
				//如果是空sdk 则没有手机注册
				if (DgameSdk.sdktype==1) {
					
					AcountRegister_dialog acountRegister_ho_dialog = new AcountRegister_dialog(
							mActivity);
					acountRegister_ho_dialog.dialogShow();
				}else{
					ViewConstants.logintype=2;
					Register_Phone_dialog register_ho_dialog = new Register_Phone_dialog(
							mActivity);
					register_ho_dialog.dialogShow();
					
				}
				
				

				
			}
		});
		
	

	}

	/**
	 * 加载数据库历史用户记录
	 */
	private void initDBData() {
		// 每次从数据库获取数据时都清空下列表,否则会有很多重复的数据
		if (mNames != null && mNames.size() > 0) {
			mNames.clear();
		}
		mNames = UserDao.getInstance(mActivity).getUsers();
		
		 System.out.println("wuuuuuuuuuuuuu"+mNames.size()+"mna:"+mNames.toString());
		// 默认选择列表中的第一项进行输入框填充
		if (mNames != null && mNames.size() > 0) {

			mSelectUser = mNames.get(0);
			mPassword = UserDao.getInstance(mActivity).getPassword(mSelectUser);

			String secret = UserDao.getInstance(mActivity).getSecret(
					mSelectUser);

			et_mUn.setText(mSelectUser);

			// 给一个填充密码
			if (TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(secret)) {
				et_mPs.setText("yayawan-zhang");
			} else {
				et_mPs.setText(mPassword);
			}
			// et_mPs.setText(mPassword);

		}

		UserListAdapter_jf userListAdapter = new UserListAdapter_jf(mActivity,
				mNames);
		lv_mHistoryuser.setAdapter(userListAdapter);

		if (mNames.size() < 4) {
			// lv_mHistoryuser.getLayoutParams().height = mNames.size() * 55;
			if (mNames.size() == 0) {
				et_mUn.setText("");
				et_mPs.setText("");
			}
			setListviewheight(mNames.size());
		} else {
			setListviewheight(4);
		}

		lv_mHistoryuser.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Log.e("正在点击我哦", "+++++++++++");
				mSelectUser = mNames.get(position);
				mPassword = UserDao.getInstance(mActivity).getPassword(
						mSelectUser);
				String secret = UserDao.getInstance(mActivity).getSecret(
						mSelectUser);

				et_mUn.setText(mSelectUser);
				// 给一个填充密码
				if (TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(secret)) {

					et_mPs.setText("yayawan-zhang");

				} else {

					et_mPs.setText(mPassword);
				}

				lv_mHistoryuser.setVisibility(View.GONE);
			}
		});

	}

	public void setListviewheight(int size) {
		lv_mHistoryuser.getLayoutParams().height = machSize((size * 100));
	}

	public class UserListAdapter_jf extends BaseAdapter {

		private ArrayList<String> mNames;

		private Context mContext;

		class ViewHolder {

			TextView mName;
			ImageView mDelete;
		}

		public UserListAdapter_jf(Context context, ArrayList<String> names) {
			super();
			this.mContext = context;
			this.mNames = names;
		}

		public int getCount() {
			return mNames.size();
		}

		public Object getItem(int position) {
			return mNames.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				Loginpo_listviewitem loginpo_listviewitem = new Loginpo_listviewitem(
						mContext);
				convertView = loginpo_listviewitem.initViewxml();
				holder.mName = (TextView) loginpo_listviewitem.getTextView();
				holder.mDelete = (ImageView) loginpo_listviewitem
						.getImageView();
				holder.mDelete.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya_xsishi.png", mActivity));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String name = mNames.get(position);
			holder.mName.setText(name);

			holder.mDelete.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mNames.remove(name);
					UserDao.getInstance(mContext).removeUser(name);
					UserListAdapter_jf.this.notifyDataSetChanged();
					if (mNames.size() == 0) {
						et_mUn.setText("");
						et_mPs.setText("");
						lv_mHistoryuser.setVisibility(View.GONE);
					}
				}
			});

			return convertView;
		}

	}

	public Login_dialog(Activity activity) {
		super(activity);
	}

	/**
	 * 查询数据库获取第一个有secret的账号
	 */
	public String[] getFirstuserSecreat() {

		ArrayList<String> users = UserDao.getInstance(mContext).getUsers();

		for (int i = 0; i < users.size(); i++) {
			String secret = UserDao.getInstance(mContext).getSecret(
					users.get(i));
			if (!TextUtils.isEmpty(secret)) {

				return new String[] { secret, users.get(i) };
			}
		}
		return null;
	}
	
	/**
	 * 微博登陆
	 * 
	 * @param ll_forgetpassword2
	 */
	private void setontoch2(LinearLayout ll_forgetpassword2) {
		ll_forgetpassword2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					iv_weibologin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_weibologin1.png",
									mActivity));
					break;
				case MotionEvent.ACTION_UP:

					iv_weibologin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_weibologin.png",
									mActivity));

					Intent intent = new Intent(mActivity,
							BaseLogin_Activity.class);
					intent.putExtra("url",
							ViewConstants.WEIBOLOGINURL);
					intent.putExtra("type", 4);
					intent.putExtra("screen", 1);
					ViewConstants.TEMPLOGIN_HO = dialog;
				//	mActivity.finish();
					dialog.dismiss();
					mActivity.startActivity(intent);

					break;

				default:
					break;
				}
				return true;
			}
		});
	}

	/**
	 * qq登录
	 * 
	 * @param ll_forgetpassword2
	 */
/*	private void setontoch3(LinearLayout ll_forgetpassword2) {
		ll_forgetpassword2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					iv_qqlogin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_qqlogin1.png",
									mActivity));
					break;
				case MotionEvent.ACTION_UP:
					iv_qqlogin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_qqlogin.png",
									mActivity));
					// Log.e("舍不得离我而去", "111");
					Intent intent = new Intent(mActivity,
							BaseLogin_Activity.class);
					intent.putExtra("url",
							"https://rest.yayawan.com/web/oauth/?type=qq&forward_url=sdk");
					intent.putExtra("type", 4);
					intent.putExtra("screen", 1);
					mActivity.startActivity(intent);
					//mActivity.finish();
					dialog.dismiss();
					ViewConstants.TEMPLOGIN_HO = dialog;

					break;

				default:
					break;
				}
				return true;

			}
		});
	}*/


}
