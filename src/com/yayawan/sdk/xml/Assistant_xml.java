package com.yayawan.sdk.xml;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yayawan.sdk.utils.CornersWebView;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;

public class Assistant_xml extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private CornersWebView wv_mWeiboview;
	
	private int webviewmaginbottom=0;
	public int getWebviewmaginbottom() {
		return webviewmaginbottom;
	}

	public void setWebviewmaginbottom(int webviewmaginbottom) {
		this.webviewmaginbottom = webviewmaginbottom;
	}

	private LinearLayout ll_mPre;
	private TextView tv_zhuce;
	private LinearLayout rl_mLoading;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private LinearLayout baselin;

	public Assistant_xml(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	public Assistant_xml(Activity activity,int mwebviewmaginbottom) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.webviewmaginbottom=mwebviewmaginbottom;
	}

	@SuppressLint("NewApi") @Override
	public View initViewxml() {

		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		baseLinearLayout.setBackgroundColor(Color.TRANSPARENT);
		baseLinearLayout.setLayoutParams(layoutParams);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER);

		
		
		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		//baselin.setBackgroundDrawable(GetAssetsutils
			//	.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		//baselin.setPadding(10, 0, 10, 10);
		baselin.setGravity(Gravity.CENTER);
		
		baselin.setBackgroundColor(Color.WHITE);
		wv_mWeiboview = new CornersWebView(mContext);
		//wv_mWeiboview.setBackgroundColor(Color.RED);
		wv_mWeiboview.setLayerType(View.LAYER_TYPE_SOFTWARE
				, null);
		machineFactory.MachineView(wv_mWeiboview, MATCH_PARENT, MATCH_PARENT,
					mLinearLayout,4,60);
		//wv_mWeiboview.setRadius(10, 10, 10, 10);
		//链接状态布局
				rl_mLoading = new LinearLayout(mContext);
				rl_mLoading.setBackgroundColor(Color.WHITE);
				machineFactory.MachineView(rl_mLoading, MATCH_PARENT, MATCH_PARENT,
						mLinearLayout,4,webviewmaginbottom);
				rl_mLoading.setGravity(Gravity.CENTER);
				pb_mLoading = new ProgressBar(mContext);
				machineFactory.MachineView(pb_mLoading, 50, 50, 0, mLinearLayout, 0,
						250, 0, 0,0);
				
				
				bt_mReload = new Button(mContext);
				machineFactory.MachineButton(bt_mReload, 350, 96, 0, "连接失败,点击重新连接", 28,
						mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
				bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
						"yaya1_loginbutton.9.png", "yaya1_loginbutton.9.png", mActivity));
				bt_mReload.setTextColor(Color.WHITE);
				
				bt_mReload.setVisibility(View.GONE);
				//TODO
				rl_mLoading.addView(pb_mLoading);
				rl_mLoading.addView(bt_mReload);

				
	
		baselin.addView(wv_mWeiboview);
		baseLinearLayout.addView(baselin);
		return baseLinearLayout;
	}

	public LinearLayout getRl_mLoading() {
		return rl_mLoading;
	}

	public void setRl_mLoading(LinearLayout rl_mLoading) {
		this.rl_mLoading = rl_mLoading;
	}

	public ProgressBar getPb_mLoading() {
		return pb_mLoading;
	}

	public void setPb_mLoading(ProgressBar pb_mLoading) {
		this.pb_mLoading = pb_mLoading;
	}

	public Button getBt_mReload() {
		return bt_mReload;
	}

	public void setBt_mReload(Button bt_mReload) {
		this.bt_mReload = bt_mReload;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public ImageButton getIv_mPre() {
		return iv_mPre;
	}

	public void setIv_mPre(ImageButton iv_mPre) {
		this.iv_mPre = iv_mPre;
	}

	public CornersWebView getWv_mWeiboview() {
		return wv_mWeiboview;
	}

	public void setWv_mWeiboview(CornersWebView wv_mWeiboview) {
		this.wv_mWeiboview = wv_mWeiboview;
	}


	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

	public TextView getTv_zhuce() {
		return tv_zhuce;
	}

	public void setTv_zhuce(TextView tv_zhuce) {
		this.tv_zhuce = tv_zhuce;
	}
	
	

}
