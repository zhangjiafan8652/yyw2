package com.yayawan.sdk.utils;

import java.lang.reflect.Field;

import javax.crypto.spec.IvParameterSpec;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.yayawan.sdk.login.Help_dissmiss_dialog;
import com.yayawan.sdk.login.StopManagerWarning_dialog;
import com.yayawan.sdk.login.StopManagerWarning_dialog.StopManagerWarningLinstener;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.ShakeListener.OnShakeListener;
import com.yayawan.sdk.xml.DisplayUtils;
import com.yayawan.sdk.xml.GetAssetsutils;

import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

/**
 * 
 * @author zjf
 * 
 */
public class LogoWindowfuben {

	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	Context con;
	public boolean isadd = false;

	static Activity mactivity;
	private int screenHeigh;
	private int screenwith;

	private static LogoWindowfuben mLogowindow;
	
	private static boolean iscanyingcang =true;
	 public static String img_icon_sdk="yaya1_acountmanagericon.png";
	
	public static String img_icon_sdk_ban="yaya1_acountmanagericontouming.png";
	
	public static String img_icon_nosdk="yaya1_acountmanagericon_nosdk.png";
	
	public static String img_icon_nosdk_ban="yaya1_acountmanagericontouming_nosdk.png";

	public static LogoWindowfuben getInstants(Activity mactivity) {
		if (mLogowindow == null) {
			Yayalog.loger("重新new了mlogowindow");

			mLogowindow = new LogoWindowfuben(mactivity);

		} else {
			Yayalog.loger("mlo不为空");
		}
		// mLogowindow.mactivity
		return mLogowindow;

	}

	private LogoWindowfuben(Activity co) {

		mactivity = co;

		createView();

	}

	private static Handler mhandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 1:
				if (myviewicon != null && params.x == 0) {
					
					if (DgameSdk.sdktype==1) {
						//半透明隐藏图标
						myviewicon.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile(img_icon_nosdk,
										mactivity));
					}else {
						//半透明隐藏图标
						myviewicon.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile(img_icon_sdk,
										mactivity));
					}
					
				}
			case 523:
				
				
				if (iscanyingcang) {
					params.x = 0;
					params.y = 100;
					Yayalog.loger("我要更新ui去隐藏了" +
							"");
					wm.updateViewLayout(myview, params);
					//myview.setBackgroundColor(Color.GRAY);
					
					if (DgameSdk.sdktype==1) {
						//半透明隐藏图标
						myviewicon.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile(img_icon_nosdk_ban,
										mactivity));
					}else {
						//半透明隐藏图标
						myviewicon.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile(img_icon_sdk_ban,
										mactivity));
					}
					
					
					myviewiconmanager.setVisibility(View.GONE);
					
				}
				
			
				
				
			case 521:
				// createView();
				//Yayalog.loger("我在接到了消息：hasWindowFocus"
				//		+ mactivity.hasWindowFocus() + "hasview:" + hasview);
				if (mactivity.hasWindowFocus() && !hasview) {
					addView();
					Yayalog.loger("mactivity.hasWindowFocus()+添加了view");
				} else {
					//Yayalog.loger("我在发消息");
					if (hasview) {

					} else {
						mhandler.sendEmptyMessageDelayed(521, 500);
					}

				}

				break;

			default:
				break;
			}
		}
	};
	private boolean ishelpshow = false;

	private ShakeListener shakeListener;
	private static ImageView myviewiconmanager;
	private static ImageView myviewicon;
	private static RelativeLayout myview;
	Help_dissmiss_dialog help_dissmiss_dialog;
	
	
		
	private void createView() {

		Yayalog.loger("兔子oncreate");
		DisplayMetrics dm = new DisplayMetrics();

		mactivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeigh = dm.widthPixels;
		screenwith = dm.heightPixels;
		if (DeviceUtil.isLandscape(mactivity)) {
			
			if (screenHeigh<screenwith) {
				
			}else {
				screenHeigh=screenwith;
			}
			
		}else {
			if (screenHeigh<screenwith) {
				screenHeigh=screenwith;
			}else {
				
			}
			try {
				Yayalog.loger(getStatusBarHeight(mactivity)+"+++++++++++getStatusBarHeight+++++++++");
				screenHeigh= screenHeigh+getStatusBarHeight(mactivity)+0;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
		wm = ((WindowManager) mactivity.getSystemService("window"));
		if (myview == null) {

			 myview = new RelativeLayout(mactivity);
			 LayoutParams layoutParams = new LinearLayout.LayoutParams(-2,
						machSize(150));
			 layoutParams.setMargins(0, 0, 0, 0);
			 myview.setLayoutParams(layoutParams);
			
			myviewicon = new ImageView(mactivity);
			// 创建时设置view的正常参数
		
			
			//小助手icon
			
			myviewicon.setLayoutParams(new RelativeLayout.LayoutParams(-2,
					machSize(150)));

			
		
			//整个小助手
			myviewiconmanager = new ImageView(mactivity);
			// 创建时设置view的正常参数
		
			myviewiconmanager.setLayoutParams(new LinearLayout.LayoutParams(machSize(200),
					machSize(200)));

			if (DgameSdk.sdktype==1) {
				//半透明隐藏图标
				myviewicon.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile(img_icon_nosdk,
								mactivity));
			}else {
				
				myviewicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						img_icon_sdk, mactivity));
				
			}
		
			myview.addView(myviewicon);
			
			myview.setOnTouchListener(new OnTouchListener() {

				float x;
				float y;

				private float mTouchX;
				private float mTouchY;

				private float mTempX;
				private float mTempY;
				private float mdownTempX;
				private float mdownTempY;
				private long ontouchtime;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					iscanyingcang=false;
					
					x = event.getRawX();
					y = event.getRawY(); // statusBarHeight是系统状态栏的高度

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
						// 获取相对View的坐标，即以此View左上角为原点
						mTouchX = event.getX();
						mTouchY = event.getY();

						mdownTempX = event.getRawX();
						mdownTempY = event.getRawY();

						ontouchtime = System.currentTimeMillis();
						

				
						
						break;

					case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作

						
						
						updateViewPosition();

						int distance_x = (int) event.getRawX()
								- (int) mdownTempX;
						int distance_y = (int) event.getRawY()
								- (int) mdownTempY;

						
						if (Math.abs(distance_x) > 5){
							if (DgameSdk.sdktype==1) {
								//不透明图标
								myviewicon.setImageBitmap(GetAssetsutils
										.getImageFromAssetsFile(img_icon_nosdk,
												mactivity));
							}else {
								//不透明图标
								myviewicon.setImageBitmap(GetAssetsutils
										.getImageFromAssetsFile(img_icon_sdk,
												mactivity));
								
							}
							
						}
				
						
						if (Math.abs(distance_x) > 60
								&& Math.abs(distance_y) > 60) {

							if (!ishelpshow) {

								 help_dissmiss_dialog = new Help_dissmiss_dialog(mactivity);
								 help_dissmiss_dialog.dialogShow();
								//ToastUtil.showSuccess(mactivity, "xianshil");
								ishelpshow = true;

							}
					
						}
						break;

					case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作

						iscanyingcang=true;
						
						int rawx=(int) event.getRawX();
						int rawy=(int) event.getRawY();
						distance_x = (int) event.getRawX() - (int) mdownTempX;
						distance_y = (int) event.getRawY() - (int) mdownTempY;
						
						
						
						if (Math.abs(distance_x) > 5){
							if (DgameSdk.sdktype==1) {
								//半透明隐藏图标
								myviewicon.setImageBitmap(GetAssetsutils
										.getImageFromAssetsFile(img_icon_nosdk,
												mactivity));
							}else {
								//半透明隐藏图标
								myviewicon.setImageBitmap(GetAssetsutils
										.getImageFromAssetsFile(img_icon_sdk,
												mactivity));
								
							}
							
						}
						
						
						if (Math.abs(distance_x) <= 70
								&& Math.abs(distance_y) <= 70) {

						
								int tempx=(int) event.getX();
								Yayalog.loger("打开账户");
								if (DgameSdk.sdktype==1) {
									//不透明图标
									myviewicon.setImageBitmap(GetAssetsutils
											.getImageFromAssetsFile(img_icon_nosdk,
													mactivity));
								}else {
									//不透明图标
									myviewicon.setImageBitmap(GetAssetsutils
											.getImageFromAssetsFile(img_icon_sdk,
													mactivity));
									
								}
								onClick(1);
						
							
			
						} else {
							
							if ((rawy > (DisplayUtils.getHeightPx(mactivity)-machSize(200)))&&(rawx > machSize(100))) {
								
								   //关闭小助手
									gotoStopManager();
								
							} else if (event.getRawX() < machSize(150)) {
								updateViewPosition1();
							}else {
								mhandler.sendEmptyMessageDelayed(523, 500);
							}
							
						}
						if (help_dissmiss_dialog!=null) {
							help_dissmiss_dialog.dialogDismiss();
							ishelpshow = false;
						}
						
						break;
					}
					return true;
				}

				private void updateViewPosition() {

					params.x = (int) (x - mTouchX);

					params.y = (int) (screenHeigh - y - mTouchY);

					wm.updateViewLayout(myview, params);

				}

				private void updateViewPosition1() {

					params.x = 0;

					params.y = (int) (screenHeigh - y - mTouchY);

					// myview.setX(-machSize(30));
					// myview.setRotation(50);
					// myview.setAlpha(100);
					Yayalog.loger("我要更新ui去隐藏了" +
							"");
					wm.updateViewLayout(myview, params);
					//myview.setBackgroundColor(Color.GRAY);
					
					
					if (DgameSdk.sdktype==1) {
						//半透明隐藏图标
						myviewicon.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile(img_icon_nosdk_ban,
										mactivity));
					}else {
						//半透明隐藏图标
						
						myviewicon.setImageBitmap(GetAssetsutils
								.getImageFromAssetsFile(img_icon_sdk_ban,
										mactivity));
						
					}
					
					myviewiconmanager.setVisibility(View.GONE);
					// myview.startAnimation(alphaAnimation);

				}

			});

		}

		params = new WindowManager.LayoutParams();
		params.format = PixelFormat.RGBA_8888;
		params.type = 1000;
		params.flags = 40;
		// params.flags |= 262144;
		// params.flags |= 512;
		params.gravity = 83;
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.alpha = (float) 1;

		params.x = 0;
		if (DeviceUtil.isLandscape(mactivity)) {
			params.y = machSize(200);
		} else {
			params.y = machSize(700);
		}

		// 摇一摇监听..分别在oncreate和destory中开始监听和关闭监听
		shakeListener = new ShakeListener(mactivity);
		shakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				Yayalog.loger("再要");
				 DgameSdk.init(mactivity);
			}
		});

	}
	StopManagerWarning_dialog mStopManagerWarning_dialog;
	protected void gotoStopManager() {
		// TODO Auto-generated method stub
		Yayalog.loger("gotoStopManager");
		if (ViewConstants.isshowmanagertip==1) {
			
			 mStopManagerWarning_dialog=	new StopManagerWarning_dialog(mactivity);
			 mStopManagerWarning_dialog.setmStopManagerWarningLinstener(new StopManagerWarningLinstener() {
				
				@Override
				public void sucee() {
					// TODO Auto-generated method stub
					 DgameSdk.stop(mactivity);
						// 这里关闭了永久隐藏
					 iscanyingcang=false;
					 mStopManagerWarning_dialog.dialogDismiss();
				}
				
				@Override
				public void cancel() {
					// TODO Auto-generated method stub
					mStopManagerWarning_dialog.dialogDismiss();
					mhandler.sendEmptyMessageDelayed(523, 3000);
				}
			});
			 mStopManagerWarning_dialog.dialogShow();
		}else {
			 DgameSdk.stop(mactivity);
				// 这里关闭了永久隐藏
			 iscanyingcang=false;
		}
		
	}
	
	// 添加
		private static int getStatusBarHeight(Context context) {
			int statusBarHeight = 0;
			int resourceId=context.getResources().getIdentifier("status_bar_height","dimen","android");
		
			if (resourceId>0) {
				statusBarHeight=	context.getResources().getDimensionPixelSize(resourceId);
			}
			
			return statusBarHeight;
		
		}



	// 添加
	private static void addView() {

		wm.addView(myview, params);
		hasview = true;
	}

	private void onClick(int i) {
		// TODO Auto-generated method stub
		// if (id == ResourceUtil.getId(mContext, "iv_floating_icon")) {
		// YayaWan.stop(mactivity);
		// 打开选择窗口
		 DgameSdk.accountManager(mactivity,i);
	}

	public void start() {
		// 停止摇一摇监听
		Yayalog.loger("开始监听暂停");
		shakeListener.stop();
		// System.out.println("1");
		
		
		mhandler.sendEmptyMessageAtTime(521, 1500);

	}

	public static boolean hasview = false;

	public void Stop() {
		// 开始摇一摇监听
		shakeListener.start();
		Yayalog.loger("暂停监听开始");
		if (hasview) {

			Yayalog.loger("暂停监听开始mLogowindow = null");

			wm.removeView(myview);
			mLogowindow = null;
			hasview = false;

		}
	}

	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	private int machSize(int size) {

		int dealWihtSize = DisplayUtils.dealWihtSize(size, mactivity);

		return dealWihtSize;
	}

	/**
	 * 获取某个view的x位置
	 * 
	 * @param view
	 * @return
	 */
	public int getViewX(View view) {
		int[] locations = new int[2];
		view.getLocationOnScreen(locations);
		int kx = locations[0];// 获取组件当前位置的横坐标
		return kx;
	}

	
	
	
}
