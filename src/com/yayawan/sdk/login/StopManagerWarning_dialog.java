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
import android.widget.ImageView.ScaleType;
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
import com.yayawan.utils.Sputils;
import com.yayawan.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class StopManagerWarning_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
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
	private TextView tv_hidemanager;
	private TextView tv_tip;
	private ImageView im_tip;
	private LinearLayout ll_mBut;
	private Button bt_mPhonelogin;
	private Button bt_mlogin;
	private StopManagerWarningLinstener mStopManagerWarningLinstener;
	public StopManagerWarningLinstener getmStopManagerWarningLinstener() {
		return mStopManagerWarningLinstener;
	}

	public void setmStopManagerWarningLinstener(
			StopManagerWarningLinstener mStopManagerWarningLinstener) {
		this.mStopManagerWarningLinstener = mStopManagerWarningLinstener;
	}

	public StopManagerWarning_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(final Activity mActivity) {

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
				MATCH_PARENT, 78, 0, mLinearLayout, 35, 30, 35, 0, 100);
		rl_title.setBackgroundColor(Color.parseColor("#ffffff"));

		tv_hidemanager = new TextView(mActivity);
		machineFactory.MachineTextView(tv_hidemanager, WRAP_CONTENT,
				WRAP_CONTENT, 0, "隐藏浮标", 40, mRelativeLayout, 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);
		tv_hidemanager.setTextColor(Color.parseColor("#000000"));

		rl_title.addView(tv_hidemanager);
		
		//提示
		tv_tip = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip, WRAP_CONTENT,
				WRAP_CONTENT, 0, "浮标隐藏后，摇一摇设备可重新显示浮标。是否隐藏？", 20, mRelativeLayout, 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_tip.setTextColor(Color.parseColor("#000000"));
		
		//提示图片
		im_tip = new ImageView(mActivity);
		machineFactory.MachineView(im_tip, 100, 100,0, "LinearLayout", 0, 80, 0, 0, 100);
		im_tip.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_shake.png", mActivity));
		
		
		// 条款
		LinearLayout ll_clause = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_clause,MATCH_PARENT, 50, 0,mLinearLayout,40, 30,0,0,100);
		ll_clause.setGravity(Gravity.CENTER_VERTICAL);

		// 同意服务条款
		ib_mAgreedbox = new ImageView(mActivity);
		machineFactory.MachineView(ib_mAgreedbox, 35, 35, mLinearLayout, 2, 0);
		ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkedbox.png", mActivity));
		
		ib_mAgreedbox.setVisibility(View.GONE);
		

		// 不同意服务条款
		ib_mNotAgreedbox = new ImageView(mActivity);
		machineFactory.MachineView(ib_mNotAgreedbox, 35, 35, mLinearLayout, 2,
				0);
		ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkbox.png", mActivity));
		//ib_mNotAgreedbox.setScaleType(ScaleType.CENTER_INSIDE);
		
		ib_mNotAgreedbox.setVisibility(View.VISIBLE);

		TextView tv_agree = new TextView(mActivity);
		machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
				"不再提示", 24, mLinearLayout, 12, 0, 5, 0);
		tv_agree.setTextColor(Color.parseColor("#000000"));
		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
		
		

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
				
				
				ViewConstants.isshowmanagertip=1;
			}
		});
		ib_mNotAgreedbox.setClickable(true);
		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mNotAgreedbox.setVisibility(View.GONE);
				ib_mAgreedbox.setVisibility(View.VISIBLE);
				ViewConstants.isshowmanagertip=0;
			}
		});
		//登陆页
				ll_mBut = new LinearLayout(mActivity);
				ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
						MATCH_PARENT, 78, 0, mLinearLayout, 35, 70, 35, 0, 100);

				// 横版手机登录按钮
				bt_mPhonelogin = new Button(mActivity);
				bt_mPhonelogin = machineFactory.MachineButton(bt_mPhonelogin, 0,
						MATCH_PARENT, 1, "取消", 32, mLinearLayout, 0, 0, 0, 0);
				
				bt_mPhonelogin.setTextColor(Color.parseColor("#0F5FA5"));
				bt_mPhonelogin.setGravity(Gravity_CENTER);
				bt_mPhonelogin.setBackgroundDrawable(null);
				LinearLayout ll_zhanwei = new LinearLayout(mActivity);
				ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 40,
						MATCH_PARENT, mLinearLayout);
				//#0F5FA5
				// button的登录按钮
				bt_mlogin = new Button(mActivity);
				bt_mlogin = machineFactory.MachineButton(bt_mlogin, 0, MATCH_PARENT, 1,
						"隐藏", 32, mLinearLayout, 0, 0, 0, 0);
				bt_mlogin.setBackgroundDrawable(null);
				bt_mlogin.setTextColor(Color.parseColor("#0F5FA5"));
				bt_mlogin.setGravity(Gravity_CENTER);

				// TODO
				ll_mBut.addView(bt_mPhonelogin);
				ll_mBut.addView(ll_zhanwei);
				ll_mBut.addView(bt_mlogin);

		ll_content.addView(rl_title);
		ll_content.addView(tv_tip);
		ll_content.addView(im_tip);
		ll_content.addView(ll_clause);
		ll_content.addView(ll_mBut);
		
		

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
			bt_mlogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mStopManagerWarningLinstener.sucee();
				}
			});
			
			bt_mPhonelogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mStopManagerWarningLinstener.cancel();
				}
			});
	}

	public interface StopManagerWarningLinstener {

	    public abstract void sucee();
	    

	    public abstract void cancel();
	}


}
