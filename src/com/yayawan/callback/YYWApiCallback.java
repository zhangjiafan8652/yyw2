package com.yayawan.callback;

import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;

public interface YYWApiCallback {

	public abstract void onVerifySuccess(String result);

 //   public abstract void onVerifyFailed(String paramString, Object paramObject);

    public abstract void onVerifyCancel();
}
