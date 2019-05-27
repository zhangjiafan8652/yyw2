package com.yayawan.sdk.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.yayawan.sdk.xml.Update_xml;
import com.yayawan.sdk.xml.Tip_xml;



public class TipDialog extends Dialog {
	private Context mContext;

	private LayoutInflater inflater;

	private LayoutParams lp;

	private TextView mMessage;

	private Button mSubmit;

	private Button mCancel;

	private Activity mActivity;

	private TextView tv_titile;

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public LayoutParams getLp() {
		return lp;
	}

	public void setLp(LayoutParams lp) {
		this.lp = lp;
	}

	public TextView getmMessage() {
		return mMessage;
	}

	public void setmMessage(TextView mMessage) {
		this.mMessage = mMessage;
	}

	public Button getmSubmit() {
		return mSubmit;
	}

	public void setmSubmit(Button mSubmit) {
		this.mSubmit = mSubmit;
	}

	public Button getmCancel() {
		return mCancel;
	}

	public void setmCancel(Button mCancel) {
		this.mCancel = mCancel;
	}

	public Activity getmActivity() {
		return mActivity;
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public TextView getTv_titile() {
		return tv_titile;
	}

	public void setTv_titile(TextView tv_titile) {
		this.tv_titile = tv_titile;
	}

	public TipDialog(Context context) {
		// super(context, ResourceUtil.getStyleId(context,
		// "CustomProgressDialog"));
		super(context);
		this.mContext = context;
		mActivity = (Activity) context;
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Tip_xml update_xml = new Tip_xml(mActivity);
		View view = update_xml.initViewxml();
		tv_titile = update_xml.getTv_titile();
		mMessage = update_xml.getTv_message();
		mSubmit = update_xml.getBt_mok();
		mCancel = update_xml.getBt_mCancel();

		setContentView(view);

		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = (float) 0.5; // 设置背景遮盖
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);

	}

	public void setMessage(String message) {
		mMessage.setText(message);
	}

	public void setSubmit(String name,
			android.view.View.OnClickListener listener) {
		mSubmit.setText(name);
		mSubmit.setOnClickListener(listener);
	}

	public void setCancle(String name,
			android.view.View.OnClickListener listener) {
		mCancel.setText(name);
		mCancel.setOnClickListener(listener);
	}

}
