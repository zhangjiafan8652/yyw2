package com.yayawan.sdk.login;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yayawan.common.CommonData;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.GameApitest;
import com.yayawan.proxy.YYcontants;
import com.yayawan.sdk.bean.Question;
import com.yayawan.sdk.callback.ExitdialogCallBack;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.FileIOUtils;
import com.yayawan.utils.Yayalog;

public class Exit_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private WebView lv_helpcontent;
	private ProgressBar pb_mPb;
	private ArrayList<Question> mQuestionList;
	private String html;
	private Button bt_mlogin;
	protected static final int SHOWCONTENT = 3;
	public ExitdialogCallBack mExitdialogcallback;
	private TextView mTextView;
	public Exit_dialog(Activity activity) {
		super(activity);
	}
	public Exit_dialog(Activity activity,String html,ExitdialogCallBack mexitdialogcallback) {
		
		super(activity);
		Yayalog.loger("wo"+html);
		this.html=html;
		mExitdialogcallback=mexitdialogcallback;
		textlog="测试顺序：打开游戏，登陆，支付，按home键回到手机桌面，再点开游戏，点击小助手，点击切换账号，用新账号登陆，最后按返回键，把检查结果截图。"+"\r\n"+"\r\n";
		
		initlogic();
		
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int ho_height ;
		int po_height ;
		if (YYcontants.ISDEBUG) {
			 ho_height = 628;
			 po_height = 628;
		}else {
			 ho_height = 384;			
			 po_height = 384;
		}
		
	
		int ho_with = 628;
		
		int po_with = 628;

		int height = 0;
		int with = 0;
		int pad = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
			with = ho_with;

		} else if ("portrait".equals(orientation)) {
			height = po_height;
			with = po_with;
		}

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, mLinearLayout, 2,
				0);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);



	
		mTextView=new TextView(mActivity);
		machineFactory.MachineTextView(mTextView, MATCH_PARENT, 528, 0, "", 18, mLinearLayout, 0, 0, 0, 0);

		// webview
		lv_helpcontent = new WebView(mActivity);
		machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, 300,
				0, mLinearLayout, 0, 0, 0, 0, 100);
		

		// button的退出
				bt_mlogin = new Button(mActivity);
				bt_mlogin = machineFactory.MachineButton(bt_mlogin, MATCH_PARENT, 84, 1,
						"退出游戏", 25, mLinearLayout, 0, 0, 0, 0);
				bt_mlogin.setTextColor(Color.WHITE);
				bt_mlogin.setBackgroundColor(Color.BLACK);
				bt_mlogin.setPadding(0, 0, 0, 0);
				bt_mlogin.setGravity(Gravity_CENTER);
				bt_mlogin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mExitdialogcallback.goExit();
						dialogDismiss();
					}
				});
		
		//ll_content.addView(rl_title);
		if (YYcontants.ISDEBUG) {
			ll_content.addView(mTextView);
		}else {
			ll_content.addView(lv_helpcontent);
		}
				
		
		ll_content.addView(bt_mlogin);

		// baselin.addView(rl_title);
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

		
	}

	
 static String textlog="测试顺序：打开游戏，登陆，支付，按home键回到手机桌面，再点开游戏，点击小助手，点击切换账号，用新账号登陆，最后按返回键，把检查结果截图。"+"\r\n"+"\r\n";
	
	private void initlogic() {
		if (YYcontants.ISDEBUG) {
			String localtestlog = FileIOUtils.readFile2String(GameApitest.DB_DIRPATH);
			textlog=textlog+" 测试结果"+"\r\n";
			if (localtestlog.contains("Application")) {
				textlog=textlog+"Application: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"Application: 接口测试不通过（请检查是否接入application接口）"+"\r\n";
			}
			
			if (localtestlog.contains("launchActivityOnCreate")) {
				textlog=textlog+"launchActivityOnCreate: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"launchActivityOnCreate: 接口测试不通过（非必要接口，如果游戏启动项为主游戏acitivity则不需要接入）"+"\r\n";
			}
			
			if (localtestlog.contains("anim")) {
				textlog=textlog+"anim: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"anim: 接口测试不通过（闪屏未接入）"+"\r\n";
			}
			
			if (localtestlog.contains("initSdk")) {
				textlog=textlog+"initSdk: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"initSdk: 接口测试不通过"+"\r\n";
			}
			if (localtestlog.contains("onCreate")) {
				textlog=textlog+"onCreate: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"onCreate: 接口测试不通过"+"\r\n";
			}
			if (localtestlog.contains("onResume")) {
				textlog=textlog+"onResume: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"onResume: 接口测试不通过"+"\r\n";
			}
			if (localtestlog.contains("onPause")) {
				textlog=textlog+"onPause: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"onPause: 接口测试不通过"+"\r\n";
			}
			if (localtestlog.contains("onStop")) {
				textlog=textlog+"onStop: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"onStop: 接口测试不通过（测试顺序不对，没有按home键返回桌面或者没有接入）"+"\r\n";
			}
			if (localtestlog.contains("onRestart")) {
				textlog=textlog+"onRestart: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"onRestart: 接口测试不通过（测试顺序不对，没有按home键返回桌面或者没有接入）"+"\r\n";
			}
			if (localtestlog.contains("pay")) {
				textlog=textlog+"pay: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"pay: 接口测试不通过"+"\r\n";
			}
			if (localtestlog.contains("exit")) {
				textlog=textlog+"exit: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"exit: 接口测试不通过"+"\r\n";
			}
			if (localtestlog.contains("logout")) {
				textlog=textlog+"logout: 接口测试通过"+"\r\n";
			}else {
				textlog=textlog+"logout: 接口测试不通过（非必要接口。游戏内自己的切换账号按钮，如果游戏内没有，则无需接入）"+"\r\n";
			}
			if (localtestlog.contains("setData1")) {
				textlog=textlog+"玩家数据: "+YYWMain.mRole.toString();
			}else {
				textlog=textlog+"玩家数据: 接口测试不通过（请检查是否接入玩家数据接口）"+"\r\n";
			}
			mTextView.setText(textlog);
		}else {
			WebSettings settings = lv_helpcontent.getSettings();
			settings.setSupportZoom(true); // 支持缩放
			settings.setBuiltInZoomControls(false); // 启用内置缩放装置
			settings.setJavaScriptEnabled(true); // 启用JS脚本
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 关闭webview中缓存
			
			settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
			Yayalog.loger("ni..."+html);
			//lv_helpcontent.loadUrl("http://danjiyou.duapp.com/Home/Blog/index");
			settings.setDefaultTextEncodingName("utf-8"); //设置文本编码
			
			lv_helpcontent.loadUrl(CommonData.exiturl);
		}
		
		//lv_helpcontent.loadData(html, "text/html; charset=UTF-8", null);
	}
	
	 /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
