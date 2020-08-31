package com.yayawan.sdk.xml;



import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yayawan.sdk.utils.CornersWebView;
import com.yayawan.sdk.webview.AdvancedWebView;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;

public class SmallHelp_xml extends Basexml implements Layoutxml {

	private RelativeLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private AdvancedWebView wv_mWeiboview;
	

	private LinearLayout ll_mPre;
	private TextView tv_zhuce;
	private LinearLayout rl_mLoading;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private LinearLayout baselin;
	private LinearLayout barlin;
	private int baibianwith=31;
	private static boolean island=true;
	public LinearLayout getBaselin() {
		return baselin;
	}

	public void setBaselin(LinearLayout baselin) {
		this.baselin = baselin;
	}

	public SmallHelp_xml(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initViewxml() {

		int height = 1344;
		int with = 1344;
		if (DeviceUtil.isLandscape(mActivity)) {
			 with = 1344;
			 height=-1;
		}else {
			 with = -1;
			 height = 1344;
		}
		// 基类布局
		baseLinearLayout = new RelativeLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams;
		if (DeviceUtil.isLandscape(mActivity)) {
			 layoutParams = new ViewGroup.LayoutParams(
					with+baibianwith, height);
			 island=true;
			//baseLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		}else {
			 layoutParams = new ViewGroup.LayoutParams(
					with, height+baibianwith);
			 island=false;
			//baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		}
	
		
		baseLinearLayout.setBackgroundColor(Color.TRANSPARENT);
		baseLinearLayout.setLayoutParams(layoutParams);
		//baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.LEFT);
		
		
		
		
		
		
		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
		if (DeviceUtil.isLandscape(mActivity)) {
			machineFactory.MachineView(baselin, with+baibianwith, height,
					mLinearLayout);
			
			//baseLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		}else {
			machineFactory.MachineView(baselin, with, height+baibianwith,
					mLinearLayout);
			
			//baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		}
	
		
		
		baselin.setBackgroundColor(Color.parseColor("#44ffffff"));
		//baselin.setBackgroundDrawable(GetAssetsutils
			//	.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		//baselin.setPadding(10, 0, 10, 10);
		baselin.setGravity(Gravity.LEFT);
		
		wv_mWeiboview = new AdvancedWebView(mContext);
		
		wv_mWeiboview.setLayerType(View.

				LAYER_TYPE_SOFTWARE
				, null);
		wv_mWeiboview.setHorizontalScrollBarEnabled(false);
		wv_mWeiboview.setBackgroundColor(Color.parseColor("#22ffffff")); // 设置背景色
	//	wv_mWeiboview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		machineFactory.MachineView(wv_mWeiboview, with, height,
					mLinearLayout);
		//wv_mWeiboview.setRadius(10, 10, 10, 10);
		//链接状态布局
				rl_mLoading = new LinearLayout(mContext);
				rl_mLoading.setBackgroundColor(Color.TRANSPARENT);
				machineFactory.MachineView(rl_mLoading, with, MATCH_PARENT,
						mLinearLayout);
				rl_mLoading.setGravity(Gravity.CENTER);
				pb_mLoading = new ProgressBar(mContext);
				machineFactory.MachineView(pb_mLoading, 50, 50, 0, mLinearLayout, 0,
						100, 0, 0,0);
				
				
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

				
		/*baseLinearLayout.addView(rl_mLoading);
		baseLinearLayout.addView(wv_mWeiboview);*/
		
		baselin.addView(rl_mLoading);
		baselin.addView(wv_mWeiboview);
		
		
		
		barlin = new LinearLayout(mActivity);
		barlin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory1 = new MachineFactory(mActivity);
		if (DeviceUtil.isLandscape(mActivity)) {
			machineFactory1.MachineView(barlin, 65, MATCH_PARENT,
					mRelativeLayout,1,with-38);
			barlin.setBackgroundColor(Color.TRANSPARENT);
			
			ImageView barimageview=new ImageView(mActivity);
			MachineFactory machineFactory2 = new MachineFactory(mActivity);
			machineFactory1.MachineView(barimageview, 65, 377,
					mLinearLayout);
			barimageview.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
					"yaya_hide_bar.png", mActivity));
			barimageview.setScaleType(ScaleType.FIT_START);
			barlin.setGravity(Gravity_CENTER);
			barlin.addView(barimageview);
		}else {
			machineFactory1.MachineView(barlin, MATCH_PARENT, 65,
					mRelativeLayout,2,height);
			barlin.setBackgroundColor(Color.TRANSPARENT);
			
			ImageView barimageview=new ImageView(mActivity);
			MachineFactory machineFactory2 = new MachineFactory(mActivity);
			machineFactory1.MachineView(barimageview, 377, 65,
					mLinearLayout);
			barimageview.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
					"yaya_hide_bar_pro.png", mActivity));
			barimageview.setScaleType(ScaleType.FIT_START);
			barlin.setGravity(Gravity_CENTER);
			barlin.addView(barimageview);
		}
		
		
		baseLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewAnimOut(baseLinearLayout);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mActivity.finish();
					}
				}).start();
			
				
			}
		});
		
		baseLinearLayout.addView(baselin);
		
		baseLinearLayout.addView(barlin);
		viewAnimIn(baseLinearLayout);
		return baseLinearLayout;
	}
	
	public static  void viewAnimIn(View view){
		 // 创建 需要设置动画的 视图View

        // 组合动画设置
        AnimationSet setAnimation = new AnimationSet(true);
        // 创建组合动画对象(设置为true)

        // 设置组合动画的属性
        setAnimation.setRepeatMode(Animation.RESTART);


        // 逐个创建子动画,不作过多描述

        // 子动画1:透明度动画
        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(600);
      
        Animation translate ;
        
        if (island) {
        	  // 子动画3:平移动画
             translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,-0.5f,
                    TranslateAnimation.RELATIVE_TO_PARENT,0,
                    TranslateAnimation.RELATIVE_TO_SELF,0
                    ,TranslateAnimation.RELATIVE_TO_SELF,0);
            translate.setDuration(500);
		}else {
			 translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0,
	                    TranslateAnimation.RELATIVE_TO_PARENT,0,
	                    TranslateAnimation.RELATIVE_TO_PARENT,-0.5f
	                    ,TranslateAnimation.RELATIVE_TO_SELF,0);
	            translate.setDuration(600);
		}
      
      
        
      
        

        // 将创建的子动画添加到组合动画里
        setAnimation.addAnimation(alpha);
       
        setAnimation.addAnimation(translate);
      

        view.startAnimation(setAnimation);
	}
	
	public static  void viewAnimOut(View view){
		 // 创建 需要设置动画的 视图View

       // 组合动画设置
       AnimationSet setAnimation = new AnimationSet(true);
       // 创建组合动画对象(设置为true)

       // 设置组合动画的属性
       setAnimation.setRepeatMode(Animation.RESTART);


       // 逐个创建子动画,不作过多描述

       // 子动画1:透明度动画
       Animation alpha = new AlphaAnimation(1,0);
       alpha.setDuration(600);
     
   
       Animation translate;
    
       if (island) {
     	  // 子动画3:平移动画
    	   // 子动画3:平移动画
            translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0,
                   TranslateAnimation.RELATIVE_TO_PARENT,-1f,
                   TranslateAnimation.RELATIVE_TO_SELF,0
                   ,TranslateAnimation.RELATIVE_TO_SELF,0);
           translate.setDuration(600);
		}else {
			 translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0,
	                    TranslateAnimation.RELATIVE_TO_PARENT,0,
	                    TranslateAnimation.RELATIVE_TO_PARENT,0
	                    ,TranslateAnimation.RELATIVE_TO_PARENT,-1f);
	            translate.setDuration(600);
		}
   
       
     
       

       // 将创建的子动画添加到组合动画里
       setAnimation.addAnimation(alpha);
      
       setAnimation.addAnimation(translate);
     

       view.startAnimation(setAnimation);
	}

	public RelativeLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(RelativeLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public LinearLayout getBarlin() {
		return barlin;
	}

	public void setBarlin(LinearLayout barlin) {
		this.barlin = barlin;
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


	public ImageButton getIv_mPre() {
		return iv_mPre;
	}

	public void setIv_mPre(ImageButton iv_mPre) {
		this.iv_mPre = iv_mPre;
	}

	public AdvancedWebView getWv_mWeiboview() {
		return wv_mWeiboview;
	}

	public void setWv_mWeiboview(AdvancedWebView wv_mWeiboview) {
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
