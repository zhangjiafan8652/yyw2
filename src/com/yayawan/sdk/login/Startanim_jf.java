package com.yayawan.sdk.login;


import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.Theme;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yayawan.common.CommonData;
import com.yayawan.sdk.callback.KgameSdkStartAnimationCallback;
import com.yayawan.sdk.db.UserDao;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.Startanima_xml;

public class Startanim_jf extends BaseView {

	private static final int ANIMSTOP = 10; // 动画播放完成

	private KgameSdkStartAnimationCallback mStartAnimationCallback; // 动画回调

	private SharedPreferences mSp;

	private static final String ACTIVE = "active";

	protected static final int ANIMERROR = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANIMSTOP:
				// mAnim.stop();
				Editor edit = mSp.edit();
				edit.putBoolean(ACTIVE, true);
				edit.commit();
				
				mActivity.finish();
				onSuccess();
				break;
			case ANIMERROR:
				// mAnim.stop();
				
				mActivity.finish();
				
				break;

			default:
				break;
			}
		}
	};

	private Startanima_xml mThisview;
	private ImageView iv_loading;

	private ImageView iv_text;

	public Startanim_jf(Activity mContext) {
		super(mContext);
		
	}

	@Override
	public View initRootview() {
		mThisview = new Startanima_xml(mActivity);

		return mThisview.initViewxml();
	}

	@Override
	public void initView() {

		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		mStartAnimationCallback = DgameSdk.mStartAnimationCallback;
		
		//把主题换成全屏的
		Theme theme = mActivity.getTheme();
		theme.applyStyle(R.style.Theme_Holo_Light, true);
		
		iv_loading = mThisview.getIv_loading();
		iv_text = mThisview.getIv_text();
		iv_text.setVisibility(View.GONE);
		
		if (DgameSdk.sdktype==1) {
			
			iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya_ani_kongbai.png", mActivity));
		
		}else {
			if (CommonData.isqianqi) {
				iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya_ani.png", mActivity));
			}else {
				iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya_qianguo_ani.png", mActivity));
			}
			
		}
		
		
		
//		iv_text.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
//				"yaya_logotext.png", mActivity));

		// 数据库添加一列
		UserDao.getInstance(mActivity).upDateclume();

	

		/**
		 * 创建线程,给服务器发送数据激活,并延迟3秒,让动画播放完成
		 */
		new Thread() {
			@Override
			public void run() {
				try {
					long start = System.currentTimeMillis();
				
					long end = System.currentTimeMillis();

					if ((end - start) < 200) {
						Thread.sleep(200 - (end - start));
					}

					mHandler.sendEmptyMessage(ANIMSTOP);

				} catch (Exception e) {
					e.printStackTrace();
					onError();
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(mActivity, "请检查网络", Toast.LENGTH_LONG).show();
							
						}
					});
					mHandler.sendEmptyMessageDelayed(ANIMERROR,600);
				}
			}
		}.start();

	}

	@Override
	public void logic() {

	}



	public void onSuccess() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onSuccess();
		}
		mStartAnimationCallback = null;
	}

	public void onError() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onError();
		}
		mStartAnimationCallback = null;
	}

	public void onCancel() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onCancel();
		}
	}

	/**
	 * 监听用户点击返回键,
	 */
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) {
	 * 
	 * onCancel(); return true; } return super.onKeyDown(keyCode, event); }
	 */

	@Override
	public boolean onkeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			onCancel();
			return true;
		}
		return super.onkeyDown(keyCode, event);
	}

	public void onClick(View v) {

	}

}
