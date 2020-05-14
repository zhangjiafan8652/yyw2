package com.yayawan.proxy;

import java.io.IOException;
import java.io.InputStream;

import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Yayalog;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VersionnumberView {

	private WindowManager wm;
	private WindowManager.LayoutParams params;
	Context con;
	boolean isadd = false;
	public Activity mActivity;
	private RelativeLayout myview;
	ViewGroup vgr;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				vgr.removeView(myview);
				//myview.setVisibility(View.GONE);
				isadd=false;
				myview=null;
				break;
			}
		}
	};

	
	long time=0;
	public VersionnumberView(Activity co) {

		this.con = co;
		this.mActivity=co;
		
		try {
			
			 String banhaoxinxitime=DeviceUtil.getGameInfo(mActivity, "banhaoxinxitime");
			 
			 time=Long.parseLong(banhaoxinxitime);
			 if (time==0) {
				
			}else {
				Yayalog.loger("开始闪版号信息");
				createView();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	private void createView() {

		if (myview == null) {

			myview = new RelativeLayout(this.con);
			myview.setBackgroundColor(Color.parseColor("#000000"));
			myview.setLayoutParams(new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			//myview.setOrientation(RelativeLayout.VERTICAL);
			myview.setGravity(Gravity.CENTER);

			ImageView iv = new ImageView(this.con);
			// iv.setLayoutParams(new LayoutParams(source))

			AssetManager assetManager = con.getAssets();

			InputStream istr = null;
			try {
				if (DeviceUtil.isLandscape(mActivity)) {
					istr = assetManager.open("yayaassets/comlogolan.png");
				}else {
					istr = assetManager.open("yayaassets/comlogopor.png");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap bitmap = BitmapFactory.decodeStream(istr);
			

			iv.setImageBitmap(bitmap);

			iv.setScaleType(ImageView.ScaleType.FIT_XY);

			iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			
			TextView mytexview = new TextView(this.con);
			String banhaoxinxi=DeviceUtil.getGameInfo(mActivity, "banhaoxinxi");
			mytexview.setText(banhaoxinxi);
			mytexview.setTextSize(11);
			mytexview.setTextColor(Color.BLACK);
			RelativeLayout.LayoutParams tp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			//tp.addRule(RelativeLayout.CENTER_VERTICAL);
			tp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mytexview.setLayoutParams(tp);
			mytexview.setGravity(Gravity.CENTER);
			myview.addView(iv);
			myview.addView(mytexview);

			
			isadd = true;
		}
		
		 vgr=(ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
		 vgr.addView(myview);
		
		
		// String banhaoxinxitime=DeviceUtil.getGameInfo(mActivity, "banhaoxinxitime");
		mHandler.sendEmptyMessageDelayed(1, time*1000);
	}
}
