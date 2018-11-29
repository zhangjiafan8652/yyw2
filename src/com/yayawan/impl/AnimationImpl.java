package com.yayawan.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWAnimation;
import com.yayawan.sdk.callback.KgameSdkStartAnimationCallback;
import com.yayawan.sdk.main.DgameSdk;

public class AnimationImpl implements YYWAnimation {

	@Override
	public void anim(final Activity paramActivity) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "播放动画", Toast.LENGTH_SHORT).show();

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				DgameSdk.animation(paramActivity,
						new KgameSdkStartAnimationCallback() {

							@Override
							public void onSuccess() {
								if (YYWMain.mAnimCallBack != null) {
									YYWMain.mAnimCallBack.onAnimSuccess(
											"success", "");
								}
							}

							@Override
							public void onError() {
								if (YYWMain.mAnimCallBack != null) {
									YYWMain.mAnimCallBack.onAnimFailed(
											"failed", "");
								}
							}

							@Override
							public void onCancel() {
								if (YYWMain.mAnimCallBack != null) {
									YYWMain.mAnimCallBack.onAnimCancel(
											"cancel", "");
								}
							}
						});

			}

		});

	}

}
