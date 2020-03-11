package com.yayawan.sdk.utils;

import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.utils.Yayalog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterDown {

	private static CounterDown mCounterDown ;
	private static Activity mActivity;
	private CounterDown() {
		
	}

	public static Button mView;
	private static long countertime = 60000;

	public static boolean mCanstart=false;
	
	private  Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 111:

				if (countertime > 1) {
					if (mView != null) {
						countertime = countertime - 1000;
						mView.setText("重新获取(" + ((countertime / 1000))
								+ ")");
						
						
						mHandler.sendEmptyMessageDelayed(111, 1000);
					}
				} else {
					countertime = 60000;
					if (mView != null) {
						mView.setEnabled(true);
						mCanstart=true;
						mView.setText("");
						mView.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile("yaya1_getverifycodebutton.png", mActivity));
						
					}
				}
				break;

			default:
				break;
			}

		}
	};

	public static CounterDown getInstance(Activity mctivity) {
		mActivity=mctivity;
		if (mCounterDown!=null) {
			return mCounterDown;
		}else {
			mCounterDown=new CounterDown();
		}
		return mCounterDown;
	}

	public void setView(Button view) {
		mView = view;
		Yayalog.loger(countertime+"");
		if (countertime > 59000) {
			mView.setEnabled(true);
			mCanstart=true;
		} else {
			mView.setText("重新获取(" + countertime / 1000 + ")");
			mView.setBackgroundDrawable(null);
			mView.setBackgroundColor(Color.GRAY);
			mView.setEnabled(false);
			mCanstart=false;
		}
	}

	
	public void startCounter() {
		Yayalog.loger(countertime+"");
		if (mCanstart) {
			mView.setEnabled(false);
			mCanstart=false;
			mView.setText("重新获取(" + countertime / 1000 + ")");
			mView.setBackgroundDrawable(null);
			
			mView.setBackgroundColor(Color.GRAY);
			mHandler.sendEmptyMessageDelayed(111, 1000);
		}
		
	}
}
