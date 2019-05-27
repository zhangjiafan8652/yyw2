package com.yayawan.sdk.login;

import android.R.color;
import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.utils.ToastUtil;
import com.yayawan.sdk.xml.DisplayUtils;
import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;



public class Help_dissmiss_dialog extends Basedialogview {

	private static User mUser;

	protected static final int CLOSE = 111;

	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			// TODO
			switch (msg.what) {

			case CLOSE:

				dialogDismiss();
				
				break;

			default:
				break;
			}
		}

	};
	private TextView tv_message1;
	private TextView tv_userid;
	private Button bt_change;

	public Help_dissmiss_dialog(Activity activity) {
		super(activity);

	}

	/**
	 * 控制dialog三秒消失
	 */
	@Override
	public void dialogShow() {

		//int sPint = Sputils.getSPint("help_dissmiss_show", 0, mActivity);

		if (ViewConstants.ISSHOWDISMISSHELP) {
			dialog.show();

			new Thread(new Runnable() {

				@Override
				public void run() {

					mHandler.sendEmptyMessageDelayed(CLOSE, 3000);
				}
			}).start();
		}

		

	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		int ho_height = 150;
		int ho_with = 1280;
		int po_height = 100;
		int po_with = 600;

		int height = 0;
		int with = 0;
		int bt_with = 0;
		int bt_textsize = 0;
		int tv_textsize = 0;
		int maginbut = 0;
		// 设置横竖屏
		
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {

			height = machSize(130);
			with = 1280;
			bt_with = 200;
			bt_textsize = 28;
			tv_textsize = 36;
			maginbut = 500;
		} else if ("portrait".equals(orientation)) {

			height = machSize(150);
			with = 720;
			bt_with = 200;
			bt_textsize = 28;
			tv_textsize = 32;
			maginbut = 900;
		}
		int screenheight=0;
		if (DeviceUtil.isLandscape(mActivity)) {
			screenheight = DisplayUtils.getHeightPx(mActivity);
		} else  {
			   // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
			screenheight = DisplayUtils.getHeightPx(mActivity);

		}
	
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with,MATCH_PARENT , "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		//baselin.setBackgroundColor(Color.RED);
		// 中间内容
		
		RelativeLayout Rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(Rl_content, with, MATCH_PARENT, 0, mLinearLayout,
				0, 0, 0, 0, 100);
		
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, 0, mRelativeLayout,
				0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		ll_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mActivity));
		ll_content.setGravity(Gravity.CENTER_VERTICAL);
		ll_content.setBackgroundColor(Color.parseColor("#CCffffff"));
		
		//ll_content.setBackgroundColor(Color.GREEN);

		LinearLayout ll_textline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_textline, with-bt_with, height, 1,
				mLinearLayout);
		ll_textline.setGravity(Gravity.CENTER_VERTICAL);
		//ll_textline.setBackgroundColor(Color.RED);

		tv_message1 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_message1, MATCH_PARENT, height,
				0, "拖到此处隐藏", tv_textsize, mLinearLayout, 10, 0, 0,
				0);
		tv_message1.setTextColor(Color.parseColor("#000000"));
        tv_message1.setGravity(Gravity_CENTER);
       // tv_message1.setBackgroundColor(Color.BLUE);

		// TODO
		ll_textline.addView(tv_message1);
	

		bt_change = new Button(mActivity);
		machineFactory.MachineButton(bt_change, bt_with, 80, 0, "不再提示",
				bt_textsize, mLinearLayout, 20, 0, 0, 0);
		bt_change.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_greenbut.9.png", mContext));
		bt_change.setTextColor(Color.WHITE);
		

		// TODO
		//ll_content.addView(bt_change);
		ll_content.addView(ll_textline);
		
		Rl_content.addView(ll_content);
		baselin.addView(Rl_content);
		dialog.setContentView(baselin);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0f; // 设置背景色对比度

		// lp.y = 60;

		dialogWindow.setAttributes(lp);

		dialog.setCanceledOnTouchOutside(true);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		// dialog.setCanceledOnTouchOutside(true);

		initlog();

	}

	private void initlog() {

		bt_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ViewConstants.ISSHOWDISMISSHELP=false;
				dialogDismiss();
			}
		});
	}

}
