package com.yayawan.sdk.pay.xml;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yayawan.common.CommonData;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.CornersLinearLayout;
import com.yayawan.sdk.xml.Basexml;
import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.Layoutxml;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

@SuppressLint("NewApi")
public class Yayapay_mainxml_po extends Basexml implements Layoutxml {

	private CornersLinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private TextView tv_mYuanbao;
	private TextView tv_mMoney;
	private RelativeLayout rl_mBluep;
	private RelativeLayout rl_mChuxuka;
	private RelativeLayout rl_mXinyongka;
	private RelativeLayout rl_mYidong;
	private RelativeLayout rl_mLiantong;
	private RelativeLayout rl_mDianxin;
	private RelativeLayout rl_mShengda;
	private RelativeLayout rl_mJunka;
	private RelativeLayout rl_mYaya;
	private RelativeLayout rl_mQbi;
	private LinearLayout ll_mPre;
	private TextView tv_mHelp;
	private TextView tv_mMoney1;
	private RelativeLayout rl_mYinlianpay;
	private RelativeLayout rl_mWxpay;
	private Button bt_mMorepay;
	private GridLayout gl_mPlaylist;
	private String mpaytostring;
	private RelativeLayout rl_mDaijinjuan;


	public TextView getTv_mMoney1() {
		return tv_mMoney1;
	}

	public void setTv_mMoney1(TextView tv_mMoney1) {
		this.tv_mMoney1 = tv_mMoney1;
	}

	public Button getBt_mMorepay() {
		return bt_mMorepay;
	}

	public void setBt_mMorepay(Button bt_mMorepay) {
		this.bt_mMorepay = bt_mMorepay;
	}

	public GridLayout getGl_mPlaylist() {
		return gl_mPlaylist;
	}

	public void setGl_mPlaylist(GridLayout gl_mPlaylist) {
		this.gl_mPlaylist = gl_mPlaylist;
	}

	public String getMpaytostring() {
		return mpaytostring;
	}

	public void setMpaytostring(String mpaytostring) {
		this.mpaytostring = mpaytostring;
	}


	public Yayapay_mainxml_po(Activity activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {
		// long currentTimeMillis = System.currentTimeMillis();

		// 基类布局
		baseLinearLayout = new CornersLinearLayout(mContext);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);

		machineFactory.MachineView(baseLinearLayout,
				ViewConstants.getHoldActivityWith(mContext),
				ViewConstants.getHoldActivityHeight(mContext), "LinearLayout");
		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

		baseLinearLayout.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
	
		
		// 设置长度需要baseliner和relative两个设置
		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);

		// ViewConstants.getHoldActivityWith(mContext)全局的所有窗口化activity的宽

		machineFactory.MachineView(rl_title,
				ViewConstants.getHoldActivityWith(mContext), 144, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#3385FF"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 144, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_RIGHT);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 67, 67, 0, mLinearLayout, 0, 0, 0,
				0, 0);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_cancel_icon.png", mActivity));

		ll_mPre.addView(iv_mPre);
		iv_mPre.setClickable(false);

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"充值", 60, mRelativeLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.parseColor("#ffffff"));
		tv_zhuce.setGravity(Gravity_CENTER);

		tv_mHelp = new TextView(mContext);
		machineFactory.MachineTextView(tv_mHelp, WRAP_CONTENT, MATCH_PARENT, 0,
				"帮助", 42, mRelativeLayout, 30, 0, 30, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);
		tv_mHelp.setTextColor(Color.parseColor("#fffffa"));
		tv_mHelp.setGravity(Gravity_CENTER);
		tv_mHelp.setClickable(true);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		//rl_title.addView(tv_mHelp);

		ScrollView sv_mContent = new ScrollView(mContext);
		machineFactory.MachineView(sv_mContent, MATCH_PARENT,
				ViewConstants.getHoldActivityHeight(mContext) - 144,
				mLinearLayout);

		LinearLayout ll_mContent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mContent, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_mContent.setOrientation(LinearLayout.VERTICAL);
		ll_mContent.setGravity(Gravity_CENTER);

		// 顶部金额的条目
		LinearLayout ll_moneyitem = new LinearLayout(mContext);
		machineFactory.MachineView(ll_moneyitem, MATCH_PARENT, 150,
				mLinearLayout);
		ll_moneyitem.setOrientation(LinearLayout.HORIZONTAL);

		// 多少元宝
		tv_mYuanbao = new TextView(mContext);
		machineFactory.MachineTextView(tv_mYuanbao, 0, MATCH_PARENT, 1,
				"300元宝", 48, mLinearLayout, 0, 0, 30, 0);
		tv_mYuanbao.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		// tv_mYuanbao.setGravity(Gravity.RIGHT);

		// 分割线
		TextView tv_hline = new TextView(mContext);
		machineFactory.MachineView(tv_hline, 2, MATCH_PARENT, mLinearLayout);
		tv_hline.setBackgroundColor(Color.parseColor("#d5d5d5"));

		// 金额多少
		tv_mMoney1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney1, WRAP_CONTENT, MATCH_PARENT,
				0, "金额:￥", 48, mLinearLayout, 30, 0, 0, 0);
		tv_mMoney1.setGravity(Gravity.CENTER_VERTICAL);

		tv_mMoney = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney, 0, MATCH_PARENT, 1, "", 48,
				mLinearLayout, 0, 0, 0, 0);
		tv_mMoney.setGravity(Gravity.CENTER_VERTICAL);
		tv_mMoney.setTextColor(Color.parseColor("#ff9900"));

		// TODO
		ll_moneyitem.addView(tv_mYuanbao);
		ll_moneyitem.addView(tv_hline);
		ll_moneyitem.addView(tv_mMoney1);
		ll_moneyitem.addView(tv_mMoney);

		TextView tv_fastpay = markView("请选择支付方式：如支付失败，请换种支付方式("+CommonData.SDKVERSION+"):");

		// 创建每种支付的布局
		rl_mBluep = createItemView("支付宝支付", "yaya_zhifu.png",
				DeviceUtil.BLUEPMSPCODE);
		rl_mYinlianpay = createItemView("银联卡支付", "yaya_yinlian.png",
				DeviceUtil.YINLIAN);


		rl_mYaya = createyayabiItemView("平台币支付", "yaya_yayabi.png",
				DeviceUtil.YAYABICODE);
		rl_mWxpay = createItemView("微信支付", "yaya_greenp.png",
				DeviceUtil.WXPAYCODE);
		rl_mDaijinjuan = createItemView("代金券支付", "yaya_daijinjuan.png",
				DeviceUtil.DAIJINJUANPAY);

	
		// TODO
		ll_mContent.addView(ll_moneyitem);
		ll_mContent.addView(createLine());
		ll_mContent.addView(tv_fastpay);

		gl_mPlaylist = new GridLayout(mContext);
		machineFactory.MachineView(gl_mPlaylist, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		gl_mPlaylist.setOrientation(GridLayout.HORIZONTAL);
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			gl_mPlaylist.setColumnCount(3);
		} else if ("portrait".equals(orientation)) {
			gl_mPlaylist.setColumnCount(2);
		}

		gl_mPlaylist.setPadding(10, 10, 10, 15);

		// 这里获取初始化的时候得到的可以开启的支付类型
		// AgentApp.mPayMethods = DeviceUtil.getYayaWanMethod(mContext);
		//
		// mpaytostring = AgentApp.mPayMethods.toString();
		// 如果支付类型存在，则加入到列表中
		Yayalog.loger("初始化得到的支付方式有：" + mpaytostring);

		gl_mPlaylist.addView(rl_mBluep);

		gl_mPlaylist.addView(rl_mWxpay);
		
		if (DgameSdk.sdktype==1) {
			
		}
		else {
			gl_mPlaylist.addView(rl_mYaya);
			
			gl_mPlaylist.addView(rl_mDaijinjuan);
			
		}
		

		
		gl_mPlaylist.addView(rl_mYinlianpay);
	
		ll_mContent.addView(gl_mPlaylist);

		sv_mContent.addView(ll_mContent);
		baseLinearLayout.addView(rl_title);
		baseLinearLayout.addView(sv_mContent);
		
		return baseLinearLayout;
	}

	public RelativeLayout getRl_mWxpay() {
		return rl_mWxpay;
	}

	public void setRl_mWxpay(RelativeLayout rl_mWxpay) {
		this.rl_mWxpay = rl_mWxpay;
	}

	/**
	 * 创建一个支付item布局
	 * 
	 * @param name
	 *            支付名字
	 * @param iconname
	 *            icon名字
	 * @param paytype
	 *            支付方式id 用来判断是否九五折
	 * @return
	 */
	private RelativeLayout createItemView(String name, String iconname,
			int paytype) {
		RelativeLayout relativeLayout = new RelativeLayout(mContext);

		machineFactory.MachineView(relativeLayout, 450, 150, 0, "GridLayout",
				22, 22, 0, 0, 0);
		relativeLayout.setGravity(Gravity_CENTER);

		ImageView iv_payicon = new ImageView(mContext);
		machineFactory.MachineView(iv_payicon, 90, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_LEFT);
		iv_payicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				iconname, mActivity));
		
		TextView tv_bluepay = new TextView(mContext);
	
		if (paytype==DeviceUtil.YAYABICODE) {
			name = name + "      \n平台币支付赠送积分";
		}else {
			name = name + "      ";
		}
		
	
		machineFactory.MachineTextView(tv_bluepay, WRAP_CONTENT, MATCH_PARENT,
				0, name, 33, mRelativeLayout, 100, 0, 0, 0);
		tv_bluepay.setGravity(Gravity.CENTER_VERTICAL);
		// TODO
		relativeLayout.addView(iv_payicon);
		relativeLayout.addView(tv_bluepay);

		relativeLayout.setBackground(GetAssetsutils.get9DrawableFromAssetsFile(
				"yaya_paynormal_bg.9.png", mContext));
		return relativeLayout;

	}
	
	
	/**
	 * 创建一个支付item布局
	 * 
	 * @param name
	 *            支付名字
	 * @param iconname
	 *            icon名字
	 * @param paytype
	 *            支付方式id 用来判断是否九五折
	 * @return
	 */
	private RelativeLayout createItemView(String name, String iconname,
			int paytype,String iconurl,String subertext,String per) {
		RelativeLayout relativeLayout = new RelativeLayout(mContext);

		machineFactory.MachineView(relativeLayout, 450, 150, 0, "GridLayout",
				22, 22, 0, 0, 0);
		relativeLayout.setGravity(Gravity_CENTER);

		ImageView iv_payicon = new ImageView(mContext);
		machineFactory.MachineView(iv_payicon, 90, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_LEFT);
		iv_payicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				iconname, mActivity));
		
		TextView tv_bluepay = new TextView(mContext);
	
		if (paytype==DeviceUtil.YAYABICODE) {
			name = name + "      \n平台币支付赠送积分";
		}else {
			name = name + "      ";
		}
		
	
		machineFactory.MachineTextView(tv_bluepay, WRAP_CONTENT, MATCH_PARENT,
				0, name, 33, mRelativeLayout, 100, 0, 0, 0);
		tv_bluepay.setGravity(Gravity.CENTER_VERTICAL);
		// TODO
		relativeLayout.addView(iv_payicon);
		relativeLayout.addView(tv_bluepay);

		relativeLayout.setBackground(GetAssetsutils.get9DrawableFromAssetsFile(
				"yaya_paynormal_bg.9.png", mContext));
		return relativeLayout;

	}
	
	
	
	
	/**
	 * 创建一个丫丫币支付item布局
	 * 
	 * @param name
	 *            支付名字
	 * @param iconname
	 *            icon名字
	 * @param paytype
	 *            支付方式id 用来判断是否九五折
	 * @return
	 */
	private RelativeLayout createyayabiItemView(String name, String iconname,
			int paytype) {
		RelativeLayout relativeLayout = new RelativeLayout(mContext);

		machineFactory.MachineView(relativeLayout, 450, 150, 0, "GridLayout",
				22, 22, 0, 0, 0);
		relativeLayout.setGravity(Gravity_CENTER);

		ImageView iv_payicon = new ImageView(mContext);
		machineFactory.MachineView(iv_payicon, 90, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_LEFT);
		iv_payicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				iconname, mActivity));

		TextView tv_bluepay = new TextView(mContext);
		name = name + "";
	
		machineFactory.MachineTextView(tv_bluepay, WRAP_CONTENT, MATCH_PARENT,
				0, name, 33, mRelativeLayout, 110, 0, 0, 0);
		
		tv_bluepay.setGravity(Gravity.CENTER_VERTICAL);
		
		
		TextView tv_tip = new TextView(mContext);
		
		String nametip =  "       可获得积分";
		machineFactory.MachineTextView(tv_tip, WRAP_CONTENT, MATCH_PARENT,
				0, nametip, 18, mRelativeLayout, 60, 107, 0, 0);
		
		tv_tip.setGravity(Gravity.CENTER_VERTICAL);
		tv_tip.setTextColor(Color.parseColor("#2B2B2B"));
		// TODO
		relativeLayout.addView(iv_payicon);
		relativeLayout.addView(tv_bluepay);
		relativeLayout.addView(tv_tip);

		relativeLayout.setBackground(GetAssetsutils.get9DrawableFromAssetsFile(
				"yaya_paynormal_bg.9.png", mContext));
		return relativeLayout;

	}


	private TextView markView(String name) {
		TextView textview = new TextView(mContext);
		machineFactory.MachineTextView(textview, MATCH_PARENT, 105, 0, name, 30,
				mLinearLayout, 30, 0, 0, 0);
		textview.setTextColor(Color.parseColor("#737373"));
		textview.setGravity(Gravity.CENTER_VERTICAL);
		return textview;
	}

	private TextView createLine() {
		// 水平分割线
		TextView tv_vline = new TextView(mContext);
		machineFactory.MachineView(tv_vline, MATCH_PARENT, 2, mLinearLayout);
		tv_vline.setBackgroundColor(Color.parseColor("#d5d5d5"));
		return tv_vline;
	}

	public CornersLinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(CornersLinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public ImageButton getIv_mPre() {
		return iv_mPre;
	}

	public void setIv_mPre(ImageButton iv_mPre) {
		this.iv_mPre = iv_mPre;
	}

	public TextView getTv_mYuanbao() {
		return tv_mYuanbao;
	}

	public void setTv_mYuanbao(TextView tv_mYuanbao) {
		this.tv_mYuanbao = tv_mYuanbao;
	}

	public TextView getTv_mMoney() {
		return tv_mMoney;
	}

	public void setTv_mMoney(TextView tv_mMoney) {
		this.tv_mMoney = tv_mMoney;
	}

	public RelativeLayout getRl_mBluep() {
		return rl_mBluep;
	}

	public void setRl_mBluep(RelativeLayout rl_mBluep) {
		this.rl_mBluep = rl_mBluep;
	}

	public RelativeLayout getRl_mChuxuka() {
		return rl_mChuxuka;
	}

	public void setRl_mChuxuka(RelativeLayout rl_mChuxuka) {
		this.rl_mChuxuka = rl_mChuxuka;
	}

	public RelativeLayout getRl_mXinyongka() {
		return rl_mXinyongka;
	}

	public void setRl_mXinyongka(RelativeLayout rl_mXinyongka) {
		this.rl_mXinyongka = rl_mXinyongka;
	}

	public RelativeLayout getRl_mYidong() {
		return rl_mYidong;
	}

	public void setRl_mYidong(RelativeLayout rl_mYidong) {
		this.rl_mYidong = rl_mYidong;
	}

	public RelativeLayout getRl_mLiantong() {
		return rl_mLiantong;
	}

	public void setRl_mLiantong(RelativeLayout rl_mLiantong) {
		this.rl_mLiantong = rl_mLiantong;
	}

	public RelativeLayout getRl_mDianxin() {
		return rl_mDianxin;
	}

	public void setRl_mDianxin(RelativeLayout rl_mDianxin) {
		this.rl_mDianxin = rl_mDianxin;
	}

	public RelativeLayout getRl_mShengda() {
		return rl_mShengda;
	}

	public void setRl_mShengda(RelativeLayout rl_mShengda) {
		this.rl_mShengda = rl_mShengda;
	}

	public RelativeLayout getRl_mJunka() {
		return rl_mJunka;
	}

	public void setRl_mJunka(RelativeLayout rl_mJunka) {
		this.rl_mJunka = rl_mJunka;
	}

	public RelativeLayout getRl_mYaya() {
		return rl_mYaya;
	}

	public RelativeLayout getRl_mDaijinjuan() {
		return rl_mDaijinjuan;
	}

	public void setRl_mDaijinjuan(RelativeLayout rl_mDaijinjuan) {
		this.rl_mDaijinjuan = rl_mDaijinjuan;
	}

	public void setRl_mYaya(RelativeLayout rl_mYaya) {
		this.rl_mYaya = rl_mYaya;
	}

	public RelativeLayout getRl_mQbi() {
		return rl_mQbi;
	}

	public void setRl_mQbi(RelativeLayout rl_mQbi) {
		this.rl_mQbi = rl_mQbi;
	}

	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

	public TextView getTv_mHelp() {
		return tv_mHelp;
	}

	public void setTv_mHelp(TextView tv_mHelp) {
		this.tv_mHelp = tv_mHelp;
	}

	public RelativeLayout getRl_mYinlianpay() {
		return rl_mYinlianpay;
	}

	public void setRl_mYinlianpay(RelativeLayout rl_mYinlianpay) {
		this.rl_mYinlianpay = rl_mYinlianpay;
	}

}
