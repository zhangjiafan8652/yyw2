package com.yayawan.sdk.utils;


import com.kkgame.kkgamelib.R;

import android.app.Activity;
import android.content.res.Resources.Theme;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class Adtest {

	
	public  static void windowsad(Activity mac){
		
		final WindowManager wm = (WindowManager) mac.getApplicationContext().getSystemService("window");
        WindowManager.LayoutParams para = new WindowManager.LayoutParams();
        //设置弹窗的宽高
        para.height = LayoutParams.WRAP_CONTENT;
        para.width = LayoutParams.WRAP_CONTENT;
        //期望的位图格式。默认为不透明
        para.format = 1;
        //当FLAG_DIM_BEHIND设置后生效。该变量指示后面的窗口变暗的程度。
        //1.0表示完全不透明，0.0表示没有变暗。
        para.dimAmount = 0.6f;
        para.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_DIM_BEHIND;
        //设置为系统提示      
        para.type = LayoutParams.TYPE_SYSTEM_ALERT;
        //获取要显示的View
        final View mView = LayoutInflater.from(mac.getApplicationContext()).inflate(
                        R.layout.ad, null);
        //单击View是关闭弹窗
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mView);
            }
        });
        //显示弹窗
        try {
			new Thread().sleep(5000);
			wm.addView(mView, para);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       

	}
}
