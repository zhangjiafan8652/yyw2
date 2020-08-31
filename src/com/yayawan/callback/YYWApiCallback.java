package com.yayawan.callback;


public interface YYWApiCallback {

	public abstract void onVerifySuccess(String result);

 //   public abstract void onVerifyFailed(String paramString, Object paramObject);

    public abstract void onVerifyCancel();
}
