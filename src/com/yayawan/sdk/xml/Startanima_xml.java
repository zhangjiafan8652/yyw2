package com.yayawan.sdk.xml;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yayawan.utils.DeviceUtil;

public class Startanima_xml extends Basexml implements Layoutxml {

	private ImageView iv_loading;
	private ImageView iv_text;

	public Startanima_xml(Activity activity) {
		super(activity);
	}

	public Startanima_xml(Context activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {
		LinearLayout linearLayout = new LinearLayout(mContext);
		new android.widget.AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);

		int oneheight = 0;
		int twoheight = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			oneheight=120;
			twoheight=300;
		} else if ("portrait".equals(orientation)) {
			oneheight=375;
			twoheight=675;
		}

		linearLayout.setBackgroundColor(Color.WHITE);

		linearLayout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout rl_comtent = new LinearLayout(mActivity);
		machineFactory.MachineView(rl_comtent, MATCH_PARENT,MATCH_PARENT,
				mLinearLayout);
		rl_comtent.setGravity(Gravity.CENTER_HORIZONTAL);
		rl_comtent.setOrientation(LinearLayout.VERTICAL);

		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		int height = wm.getDefaultDisplay().getHeight();

		iv_loading = new ImageView(mContext);
		machineFactory.MachineView(iv_loading, 750, 750, 0, mLinearLayout, 0,
				oneheight, 0, 0, 100);

		// System.out.println(DisplayUtils.getHeightPx(mActivity)+"+++++++++++++");
		iv_text = new ImageView(mContext);
		machineFactory.MachineView(iv_text, WRAP_CONTENT, WRAP_CONTENT, 0,
				mLinearLayout, 0, twoheight, 0, 0, 100);
		
		
		String banhaoxinxi=	DeviceUtil.getGameInfoNeed(mActivity, "banhaoxinxi");
		
		TextView tv_agree = new TextView(mContext);
		machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
				banhaoxinxi, 30, mLinearLayout, 6, 0, 0, 0);
		tv_agree.setTextColor(Color.GRAY);
		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
		
		rl_comtent.addView(iv_loading);
		rl_comtent.addView(iv_text);
		
		rl_comtent.addView(tv_agree);

		linearLayout.addView(rl_comtent);
		return linearLayout;
	}

	public ImageView getIv_loading() {
		return iv_loading;
	}

	public void setIv_loading(ImageView iv_loading) {
		this.iv_loading = iv_loading;
	}

	public ImageView getIv_text() {
		return iv_text;
	}

	public void setIv_text(ImageView iv_text) {
		this.iv_text = iv_text;
	}

}
