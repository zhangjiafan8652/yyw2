package com.yayawan.sdk.callback;

public interface KgameSdkApiCallBack {

	public abstract void onVerifySuccess(String result);

 //   public abstract void onVerifyFailed(String paramString, Object paramObject);

    public abstract void onVerifyCancel();
}
