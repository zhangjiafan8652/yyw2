package com.yayawan.proxy;




import com.yayawan.utils.CrashHandler;
import com.yayawan.utils.CrashHandler2;

import android.app.Application;
import android.content.Context;


public class YYWApplication extends Application {

	public static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//GameApitest.initOnapplication(this);
		mContext = getApplicationContext();
		GameApitest.getGameApitestInstants().sendTest(getPackageName()+"Application.oncreate");
		// System.out.println("YYApplication");
	//	CrashHandler.getInstance().init(this);
	//	CrashHandler2.getInstance().init(this);
		try {
		//	JLibrary.InitEntry(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		YYWApplication.mContext = mContext;
	}
}
