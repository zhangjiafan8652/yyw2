package com.yayawan.sdk.login;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.yayawan.common.CommonData;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.GameApitest;
import com.yayawan.proxy.YYcontants;
import com.yayawan.sdk.bean.Question;
import com.yayawan.sdk.callback.ExitdialogCallBack;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.utils.Util;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.FileIOUtils;
import com.yayawan.utils.Sputils;
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
		//textlog="测试顺序：打开游戏，登陆，支付，按home键回到手机桌面，再点开游戏，点击小助手，点击切换账号，用新账号登陆，最后按返回键，把检查结果截图。"+"\r\n"+"\r\n";
		
		initlogic();
		
	}

	
	 /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int ho_height ;
		int po_height ;
		if (YYcontants.ISDEBUG) {
			 ho_height = 942;
			 po_height = 942;
		}else {
			 ho_height = 580;			
			 po_height = 580;
		}
		
	
		int ho_with = 942;
		
		int po_with = 942;

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
		machineFactory.MachineTextView(mTextView, MATCH_PARENT, MATCH_PARENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);

		// webview
		lv_helpcontent = new WebView(mActivity);
		if (YYcontants.ISDEBUG) {
			machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, 750,
					0, mLinearLayout, 0, 0, 0, 0, 100);
		}else {
			machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, 450,
					0, mLinearLayout, 0, 0, 0, 0, 100);
		}

		// button的退出
				bt_mlogin = new Button(mActivity);
				bt_mlogin = machineFactory.MachineButton(bt_mlogin, MATCH_PARENT, 150, 1,
						"退出游戏", 40, mLinearLayout, 0, 0, 0, 0);
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
//		if (YYcontants.ISDEBUG) {
//			ll_content.addView(sl_content);
//			sll_content.addView(mTextView);
//			
//			//ll_content.addView(bt_mlogin);
//
//			// baselin.addView(rl_title);
//			baselin.addView(ll_content);
//			baselin.addView(bt_mlogin);
//		}else {
			ll_content.addView(lv_helpcontent);
			ll_content.addView(bt_mlogin);

			// baselin.addView(rl_title);
			baselin.addView(ll_content);
	//	}
				
		
		

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

	
 static String textlog="测试顺序：打开游戏，登陆，支付，按home键回到手机桌面，再点开游戏，点击小助手，点击切换账号，用新账号登陆，最后按返回键，把检查结果截图和游戏包一起提交给我们。<hr/> "+"\r\n";
 String standard = "<html> \n" +
         "<head> \n" +
         "<style type=\"text/css\"> \n" +
         "body {font-size:10px;}\n" +
         "</style> \n" +
         "</head> \n" +
         "<body>" +
         " <meta name=\"content-type\" content=\"text/html; charset=utf-8\"><meta http-equlv=\"Content-Type\" content=\"text/html;charset=utf-8\">"
         +" neirong "+
         "</body>" +
         "</html>";

	private void initlogic() {
		textlog="测试顺序：打开游戏，登陆，支付，按home键回到手机桌面，再点开游戏，点击小助手，点击切换账号，用新账号登陆，最后按返回键，把检查结果截图和游戏包一起提交给我们。<hr/> "+"\r\n";
		 
		if (YYcontants.ISDEBUG) {
			
			String localtestlog ="";
			for (String key  : GameApitest.mTestdatas.keySet()) {
				
				Yayalog.loger("key::::::::"+key+"         value:"+ GameApitest.mTestdatas.get(key));
				localtestlog=localtestlog+""+"key::"+key+"  value:"+ GameApitest.mTestdatas.get(key);
				
			}
			
			//String localtestlog =Sputils.getSPstring(getPackageName(mContext), "", mContext);
			textlog=textlog+" 测试结果 （窗口可下滑）<hr/>"+"\r\n";
			if (localtestlog.contains("YYApplicationoncreate="+Util.getPackageName(mActivity))) {
				textlog=textlog+"Application="+Util.getPackageName(mActivity)+": 接口测试通过<hr/> "+" \r\n";
			}else {
				textlog=textlog+"Application: 接口测试不通过（请检查是否接入application接口）<hr/> "+" \r\n";
			}
			
			if (localtestlog.contains("launchActivityOnCreate")) {
				textlog=textlog+"launchActivityOnCreate: 接口测试通过 <hr/> "+" \r\n";
			}else {
				textlog=textlog+"launchActivityOnCreate: 接口测试不通过（非必要接口，如果游戏启动项为主游戏acitivity则不需要接入）<hr/> "+"\r\n";
			}
			
			if (localtestlog.contains("anim")) {
				textlog=textlog+"anim: 接口测试通过 <hr/> "+"\r\n";
			}else {
				textlog=textlog+"anim: 接口测试不通过（闪屏未接入）<hr/> "+"\r\n";
			}
			
			if (localtestlog.contains("initSdk")) {
				textlog=textlog+"initSdk: 接口测试通过 <hr/> "+"\r\n";
			}else {
				textlog=textlog+"initSdk: 接口测试不通过(请检查是否调用初始化接口) <hr/> "+"\r\n";
			}
			if (localtestlog.contains("onCreate")) {
				textlog=textlog+"onCreate: 接口测试通过 <hr/>"+"\r\n";
			}else {
				textlog=textlog+"onCreate: 接口测试不通过(请检查是否调用初始化接口) <hr/>"+"\r\n";
			}
			if (localtestlog.contains("onResume")) {
				textlog=textlog+"onResume: 接口测试通过  <hr/>"+"\r\n";
			}else {
				textlog=textlog+"onResume: 接口测试不通过  <hr/>"+"\r\n";
			}
			if (localtestlog.contains("onPause")) {
				textlog=textlog+"onPause: 接口测试通过  <hr/> "+"\r\n";
			}else {
				textlog=textlog+"onPause: 接口测试不通过  <hr/> "+"\r\n";
			}
			if (localtestlog.contains("onStop")) {
				textlog=textlog+"onStop: 接口测试通过  <hr/> "+"\r\n";
			}else {
				textlog=textlog+"onStop: 接口测试不通过（测试顺序不对，没有按home键返回桌面或者没有接入） <hr/> "+"\r\n";
			}
			if (localtestlog.contains("onRestart")) {
				textlog=textlog+"onRestart: 接口测试通过  <hr/> "+"\r\n";
			}else {
				textlog=textlog+"onRestart: 接口测试不通过（测试顺序不对，没有按home键返回桌面或者没有接入） <hr/> "+"\r\n";
			}
			if (localtestlog.contains("pay")) {
				textlog=textlog+"pay: 接口测试通过  <hr/> "+"\r\n";
			}else {
				textlog=textlog+"pay: 接口测试不通过 <hr/> "+"\r\n";
			}
			if (localtestlog.contains("exit")) {
				textlog=textlog+"exit: 接口测试通过  <hr/> "+"\r\n";
			}else {
				textlog=textlog+"exit: 接口测试不通过  <hr/> "+"\r\n";
			}
			if (localtestlog.contains("logout")) {
				textlog=textlog+"logout: 接口测试通过  <hr/> "+"\r\n";
			}else {
				textlog=textlog+"logout: 接口测试不通过（非必要接口。游戏内自己的切换账号按钮，如果游戏内没有，则无需接入） <hr/> "+"\r\n";
			}
//			if (localtestlog.contains("setData")) {
//				textlog=textlog+"玩家数据: "+YYWMain.mRole.toString();
//			}else {
//				textlog=textlog+"玩家数据: 接口测试不通过（请检查是否接入玩家数据接口） <hr/> "+"\r\n";
//			}
			
			if (localtestlog.contains("roleinfo1")) {
				textlog=textlog+"玩家数据1: "+GameApitest.mTestdatas.get("roleinfo1")+" <hr/> "+"\r\n";
			}else {
				textlog=textlog+"角色登陆数据: 接口测试不通过（请检查是否调用setdata中ext=1的接口） <hr/> "+"\r\n";
			}
			if (localtestlog.contains("roleinfo2")) {
				textlog=textlog+"玩家数据2: "+GameApitest.mTestdatas.get("roleinfo2")+" <hr/> "+"\r\n";
			}else {
				textlog=textlog+"角色创建数据: 接口测试不通过（请检查是否调用setdata中ext=2的接口） <hr/> "+"\r\n";
			}
			if (localtestlog.contains("roleinfo3")) {
				textlog=textlog+"玩家数据3: "+GameApitest.mTestdatas.get("roleinfo3")+" <hr/> "+"\r\n";
			}else {
				textlog=textlog+"角色升级数据: 接口测试不通过（请检查是否调用setdata中ext=3的接口） <hr/> "+"\r\n";
			}
			//textlog=textlog+localtestlog;
		
			WebSettings settings = lv_helpcontent.getSettings();
			settings.setSupportZoom(true); // 支持缩放
			settings.setBuiltInZoomControls(false); // 启用内置缩放装置
			settings.setJavaScriptEnabled(true); // 启用JS脚本
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 关闭webview中缓存	
			settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
			settings.setDefaultTextEncodingName("utf-8"); //设置文本编码
			Yayalog.loger("ni..."+html);
			String zuizhongneirong=standard.replace("neirong", textlog);
			//System.out.println(zuizhongneirong);
			//lv_helpcontent.loadData(Html.fromHtml(zuizhongneirong).toString(), "text/html; charset=UTF-8", null);
			lv_helpcontent.loadDataWithBaseURL(null, zuizhongneirong, "text/html","UTF-8", null);
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
			if (YYcontants.ISDEBUG) {
				lv_helpcontent.loadData(textlog, mimeType, enCoding);
			}else {
				lv_helpcontent.loadUrl(CommonData.exiturl);
			}
			
		}
		
		//lv_helpcontent.loadData(html, "text/html; charset=UTF-8", null);
	}
	String mimeType = "text/html";
	String enCoding = "utf-8";
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
