package com.yayawan.sdk.pay;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.common.CommonData;
import com.yayawan.sdk.bean.Order;
import com.yayawan.sdk.bean.PayMethod;
import com.yayawan.sdk.bean.PayResult;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkPaymentCallback;
import com.yayawan.sdk.login.BaseView;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.pay.xml.Yayapay_mainxml_po;
import com.yayawan.sdk.pay.xml.Yayapay_mainxml_po;
import com.yayawan.sdk.utils.ToastUtil;
import com.yayawan.sdk.utils.Utilsjf;
import com.yayawan.sdk.utils.Utilsjf.PayQuesionCallBack;
import com.yayawan.sdk.webview.MyWebViewClient;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class Yayapaymain_jf extends BaseView {

	private Yayapay_mainxml_po mThisview;
	private RelativeLayout rl_mBluep;

	private KgameSdkPaymentCallback mPaymentCallback;

	private ArrayList<PayMethod> mMethods;
	private TextView tv_mMoney;
	private TextView tv_mYuanbao;
	private TextView tv_mHelp;
	private LinearLayout ll_mPre;
	private RelativeLayout rl_mlDaijinjuan;

	public Yayapaymain_jf(Activity mContext) {
		super(mContext);

	}

	@Override
	public View initRootview() {
		// 初始化支付页面
		mThisview = new Yayapay_mainxml_po(mActivity);
		return mThisview.initViewxml();

	}

	@Override
	public void initView() {

		// 获取页面的所有视图实例对象
		rl_mBluep = mThisview.getRl_mBluep();
		rl_mYinlianpay = mThisview.getRl_mYinlianpay();

		ll_mPre = mThisview.getLl_mPre();
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCancel();
			}
		});
		tv_mMoney = mThisview.getTv_mMoney();
		Order mPayOrder = AgentApp.mPayOrder;
		if (AgentApp.mPayOrder.money % 100 == 0) {
			tv_mMoney.setText("" + (AgentApp.mPayOrder.money) / 100);
		} else {
			// 除数
			BigDecimal bd = new BigDecimal(AgentApp.mPayOrder.money);
			// 被除数
			BigDecimal bd2 = new BigDecimal(100);
			// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
			BigDecimal result = bd.divide(bd2, 2, BigDecimal.ROUND_HALF_UP);
			tv_mMoney.setText(result.toString());
		}

		tv_mYuanbao = mThisview.getTv_mYuanbao();
		tv_mYuanbao.setText("" + mPayOrder.goods);

		// 微信支付
		rl_mWxpay = mThisview.getRl_mWxpay();
		
		//Y币支付
		rl_mlYaya = mThisview.getRl_mYaya();
		
		//代金券支付
		rl_mlDaijinjuan = mThisview.getRl_mDaijinjuan();
		
		
		
		if (ViewConstants.ISKGAME) {
			rl_mlYaya.setVisibility(View.VISIBLE);
		}else{
			rl_mlYaya.setVisibility(View.GONE);
		}
		
		
		//如果是 千果sdk  隐藏 代金券和Y币支付
		
		

		
				
		
		// 微信插件支付

		// 先把视图隐藏，后面逻辑部分会进行有选择显示
		tv_mHelp = mThisview.getTv_mHelp();
		tv_mHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Help_dialog help_dialog = new Help_dialog(mActivity);
				help_dialog.dialogShow();
			}
		});
	}

	public static boolean payclickcontrol = false;// 点击支付条目控制器

	@Override
	public void logic() {
		payclickcontrol = false;
		mPaymentCallback = DgameSdk.mPaymentCallback;
		//rl_mBluep.setVisibility(View.GONE);
		// TODO 支付宝支付
		rl_mBluep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;

				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

				//创建订单
				makeOrder(CommonData.BLUEP);
			}

		});

		// 微信支付 支付
		rl_mWxpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				Yayalog.loger("微信支付开始");
				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");
				//创建订单
				makeOrder(CommonData.GREENP);

			}
		});

		//Y币支付
		rl_mlYaya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;

				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

				//创建订单
				makeOrder(CommonData.YAYABIPAY);
			}

		});
		
		//代金券支付
		rl_mlDaijinjuan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (payclickcontrol) {
							return;
						}
						payclickcontrol = true;

						Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

						//创建订单
						makeOrder(CommonData.DAIJINJUANPAY);
					}

				});
		
		
		//隐藏银联支付
		rl_mYinlianpay.setVisibility(View.GONE);
		
		// 银联支付
		rl_mYinlianpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewConstants.mPayActivity = mActivity;
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				CeshiYinlian();
			}

		});
		
		

		
	}

	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
		}
		mPaymentCallback = null;
		mActivity.finish();
	}

	public void onError(int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		mPaymentCallback = null;

		// ToastUtils.showLongToast("支付出错，请尝试重新支付");
		mActivity.finish();
	}

	public void onCancel() {
		if (mPaymentCallback != null) {
			mPaymentCallback.onCancel();
		}
		mActivity.finish();
	}
	
	

	private void makeOrder(final int paytype) {

		// 进入支付流程
		RequestParams rps = new RequestParams();
		
		rps.addBodyParameter("app_id", DeviceUtil.getAppid(mActivity));
		rps.addBodyParameter("uid", AgentApp.mUser.uid + "");
		rps.addBodyParameter("token", AgentApp.mUser.token);
		rps.addBodyParameter("amount", DgameSdk.mPayOrder.money + "");
		rps.addBodyParameter("pay_type", paytype + "");
		rps.addBodyParameter("goods", AgentApp.mPayOrder.goods + "");
		rps.addBodyParameter("ext", AgentApp.mPayOrder.ext);
		rps.addBodyParameter("orderid", AgentApp.mPayOrder.orderId);
		rps.addBodyParameter("appversion", 200+"");
		
		Yayalog.loger("app_id", DeviceUtil.getAppid(mActivity));
		Yayalog.loger("uid", AgentApp.mUser.uid + "");
		Yayalog.loger("token", AgentApp.mUser.token);
		Yayalog.loger("amount", DgameSdk.mPayOrder.money + "");
		Yayalog.loger("pay_type", paytype + "");
		Yayalog.loger("ext", AgentApp.mPayOrder.ext);
		Yayalog.loger("orderid", AgentApp.mPayOrder.orderId);
		Yayalog.loger("orderid", ViewConstants.makeorder);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ViewConstants.makeorder, rps,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						payclickcontrol=false;
						Utilsjf.stopDialog();

						Toast.makeText(mActivity, "下单失败，请检查网络是否畅通", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						Yayalog.loger("下单结果" + result.result);
						// TODO Auto-generated method stub
						Utilsjf.stopDialog();
						payclickcontrol=false;
						if (paytype==CommonData.GREENP) {
							
							Yayalog.loger("sdk下单结果" + result.result);
							bluepayResult(result.result);
						}else if(paytype==CommonData.BLUEP) {
							Yayalog.loger("sdk下单结果" + result.result);
							bluepayResult(result.result);
						}else if(paytype==CommonData.DAIJINJUANPAY) {
							Yayalog.loger("DAIJINJUANPAY下单结果" + result.result);
							daijinjuanpayResult(result.result);
						}else if(paytype==CommonData.YAYABIPAY) {
							Yayalog.loger("YAYABIPAY支付结果" + result.result);
							yayapayResult(result.result);
						}
					
					}

				});

	}
	//Y币支付返回结果
	private void yayapayResult(final String result){
		if (result.contains("success")) {
			onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "平台币支付成功", 0).show();
				}
			});
		}else {
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String msg="平台币余额不足";
					try {
						JSONObject jsonObject = new JSONObject(result);
						 msg = jsonObject.optString("err_msg");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(mActivity, msg, 0).show();
				}
			});
			onCancel();
		}
	}
	//Y币支付返回结果
	private void daijinjuanpayResult(final String result){
		if (result.contains("success")) {
			onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
          mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "代金券支付成功", 0).show();
				}
			});
		}else {
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					try {
						JSONObject jsonObject = new JSONObject(result);
						String errmsg=jsonObject.optString("err_msg");
						Toast.makeText(mActivity, errmsg, 0).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO Auto-generated method stub
					
				}
			});
			onCancel();
		}
	}
	
	// 支付宝支付结果
	private void bluepayResult(String result) {
		// TODO Auto-generated method stub
		Yayalog.loger(result);
		JSONObject jsonstr = null;
		try {
			jsonstr = new JSONObject(result);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int err_code=jsonstr.optInt("err_code");
		String pay_str=jsonstr.optString("url");
		
		
        
		
	
		if(err_code==0){
			try {
				//System.out.println(pay_str);
				
				GreenP_dialog greenp_dialog = new GreenP_dialog(
						mActivity);
				greenp_dialog.dialogShow();
				WebView webView = greenp_dialog.getWebview();
				greenp_dialog.getLl_mPre().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onCancel();
					}
				});


				webView.loadUrl(pay_str);
				webView.setWebViewClient(new WebViewClient(){
					
					@Override
					public void onReceivedSslError(WebView view,
							SslErrorHandler handler, SslError error) {
						// TODO Auto-generated method stub
						MyWebViewClient.onReceivedSslError(view, handler, error, "");
					}
					
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						Yayalog.loger("重复的url:"+url);
					
						if (url.startsWith("weixin://wap/pay?")) {
		                    try {
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(url));
								mActivity.startActivity(intent);
							
							} catch (Exception e) {
								// TODO Auto-generated catch block
								onCancel();
								Yayalog.loger("未安装");
								Toast.makeText(mActivity.getApplicationContext(),
										"未安装", Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}

		                    return true;
		                }else if (parseScheme(url)) {
		                    try {
		                    	
		                        Intent intent;
		                        intent = Intent.parseUri(url,
		                                Intent.URI_INTENT_SCHEME);
		                        intent.addCategory("android.intent.category.BROWSABLE");
		                        intent.setComponent(null);
		                        // intent.setSelector(null);
		                        mActivity.startActivity(intent);
		//
		                        return true;
		                    } catch (Exception e) {
		                        e.printStackTrace();
		                    }
		                }else if (url.contains("paysuccess")) {
		                	onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
		                }else if (url.contains("payfaile")) {
		                	//onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
		                	onCancel();
		                }else {
		                	
						}
					    return super.shouldOverrideUrlLoading(view, url);
					    }
					
					@Override
					public void onPageStarted(WebView view, String url, Bitmap favicon) {
						// TODO Auto-generated method stub
						//Yayalog.loger("onPageStarted重复的url:"+url);
						super.onPageStarted(view, url, favicon);
					}
					public void onPageFinished(WebView view, String url) {
						//Yayalog.loger("onPageFinished重复的url:"+url);
					};
					
					
					});
			
			} catch (Exception e) {
				
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						onCancel();
						Toast.makeText(mActivity.getApplicationContext(),
								"网络出错，请重新支付", Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	
		
	}

	private String mhtml;
	private RelativeLayout rl_mYinlianpay;
	private RelativeLayout rl_mWxpay;
	private RelativeLayout rl_mlYaya;

	public void CeshiYinlian() {

		AgentApp.mentid = 21;
		Utilsjf.creDialogpro(mActivity, "启动银联支付...");

	}

	// 丫丫玩的微信支付id 10 不是多宝通的 丫丫玩插件支付
	private void GREENP2() {
		// TODO Auto-generated method stub
		// 查看是否安装插件，插件是否为最新版本
		Boolean checkIsPluin = checkIsPluin();

		if (checkIsPluin) {
			Utilsjf.creDialogpro(mActivity, "启动微信支付...");
			AgentApp.mentid = 10;
			// 进入支付流程

		}

	}
	
	

	/**
	 * 查看是否安装插件
	 * 
	 */
	private Boolean checkIsPluin() {
		// TODO Auto-generated method stub
		// AppInfo appInfo = AppUtils.getAppInfo(mActivity,
		// "com.yyw.greenp");

		return true;
	}



	// 丫丫玩

	
	private void BlueppayNow() {
		
	}

	private void startBluep() {
		Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

	}

	@Override
	public void startActivityForResult(Intent data, int re) {
		// System.out.println(requestCode);
		super.startActivityForResult(data, re);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) {
			return;
		}
		String respCode = data.getExtras().getString("respCode");
		// String respMsg = data.getExtras().getString("respMsg");

		if (respCode == null) {
			Yayalog.loger("rescode为空");
			return;
		}
		if (respCode.equals("00")) {
			Yayalog.loger("微信支付成功");
			onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
		}

		if (respCode.equals("02")) {
			onError(0);
		}

		if (respCode.equals("01")) {
			onError(0);
		}

		if (respCode.equals("03")) {
			onError(0);
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "正在确认支付结果，请稍候查询到账情况",
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	@Override
	public void onResume() {
		// System.out.println("mele");
	}

	/**
	 * 调起微信方法
	 * 
	 * @param pay_str
	 *            调起串
	 */

	private void PullWX(String pay_str) {
		Yayalog.loger(pay_str);
		if (isGreenAvilible()) {
			try {
				
				Uri uri = Uri.parse(pay_str);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);

				mActivity.startActivity(intent);
			} catch (Exception e) {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mActivity.getApplicationContext(),
								"网络出错，请重新支付", Toast.LENGTH_LONG).show();
					}
				});
			}
		} else {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity.getApplicationContext(), "微信未安装",
							Toast.LENGTH_LONG).show();
				}
			});

		}

	}

	// 是否安装微信
	public boolean isGreenAvilible() {
		final PackageManager packageManager = mActivity.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}

	// 新版多宝通支付成功后的弹框确认
	public void weixinNewPayCallback() {
		Utilsjf.creQuestionDialog(mActivity, new PayQuesionCallBack() {

			@Override
			public void onPaySuccess() {
				// TODO Auto-generated method stub
				Utilsjf.stopDialog();
				// 检查订单是否支付成功
				checkOrder();
			}

			@Override
			public void onPayCancel() {
				// TODO Auto-generated method stub
				Utilsjf.stopDialog();
			}
		});
	}

	/**
	 * 检测当前订单是否支付成功
	 */
	public void checkOrder() {
		Utilsjf.creDialogpro(mActivity, "验证支付结果...");
		// 查询订单状态

	}
	
	
	public boolean parseScheme(String url) {
	
		
	    if (url.contains("platformapi/startApp")||url.contains("platformapi/startapp")) {
	        return true;
	    } else if ((Build.VERSION.SDK_INT > 19)
	            && (url.contains("platformapi") && url.contains("startapp"))) {
	        return true;
	    } else {
	        return false;
	    }
	}

}
