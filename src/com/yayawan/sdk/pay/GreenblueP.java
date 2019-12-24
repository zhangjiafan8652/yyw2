package com.yayawan.sdk.pay;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.sdk.bean.Order;
import com.yayawan.sdk.bean.PayResult;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkPaymentCallback;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.utils.Utilsjf;
import com.yayawan.sdk.webview.MyWebViewClient;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

public class GreenblueP {
	
	public static boolean isselectxiaomipay=false;
	

	public Activity mContext;
	public Activity mActivity;
	private KgameSdkPaymentCallback mPaymentCallback;

	public int mPaytype=1;
	/**
	 * 
	 * @param macti
	 * @param paramOrder
	 * @param paytype
	 * @param paramCallback
	 */
	public GreenblueP(Activity macti,Order paramOrder,int paytype,
			KgameSdkPaymentCallback paramCallback) {
		mPaytype=paytype;
		mPaymentCallback=paramCallback;
		AgentApp.mPayOrder=paramOrder;
		mContext = macti;
		mActivity = macti;
		// mContext
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

	public boolean parseScheme(String url) {
		System.out.println("parseScheme的url：" + url);

		if (url.contains("platformapi/startApp")
				|| url.contains("platformapi/startapp")|| url.contains("ayclient")) {
			return true;
		} else if ((Build.VERSION.SDK_INT > 19)
				&& (url.contains("platformapi") && url.contains("startapp"))) {
			return true;
		} else {
			return false;
		}
	}

	public void greenP() {
		Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

		AgentApp.mentid = mPaytype;
		WxpaynewKuaipayNow();
	}

	private PayResult mWFirstResult;

	// 微信支付 35新多宝通支付 2.24更新
	private void WxpaynewKuaipayNow() {

		// 进入支付流程
		makeOrder(AgentApp.mentid);
		
	}

	
	private void makeOrder(final int paytype) {

		// 进入支付流程
		RequestParams rps = new RequestParams();
		
		rps.addBodyParameter("app_id", DeviceUtil.getAppid(mActivity));
		rps.addBodyParameter("uid", AgentApp.mUser.uid + "");
		rps.addBodyParameter("token", AgentApp.mUser.token);
		rps.addBodyParameter("amount", DgameSdk.mPayOrder.money + "");
		rps.addBodyParameter("pay_type", paytype + "");
		rps.addBodyParameter("appversion", 100 + "");
		rps.addBodyParameter("ext", AgentApp.mPayOrder.ext);
		rps.addBodyParameter("orderid", AgentApp.mPayOrder.orderId);
		
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
						Utilsjf.stopDialog();

						Toast.makeText(mActivity, "下单失败，请检查网络是否畅通", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						Yayalog.loger("支付宝下单结果" + result.result);
						// TODO Auto-generated method stub
						Utilsjf.stopDialog();
						
						switch (paytype) {
						case 1:
							//解析支付宝下单结果
							Yayalog.loger("支付宝下单结果" + result.result);
							bluepayResult(result.result);
							break;
						case 2:
							Yayalog.loger("微信下单结果" + result.result);	
							bluepayResult(result.result);
							break;
						case 3:

							break;
						case 4:
							Yayalog.loger("微信下单结果" + result.result);
							bluepayResult(result.result);
							break;
						case 5:
							//解析支付宝下单结果
							bluepayResult(result.result);
							//bluepayResult(result.result);
							break;
						case 36:
							Yayalog.loger("微信下单结果" + result.result);
							bluepayResult(result.result);
							break;
						case 37:
							Yayalog.loger("微信下单结果" + result.result);
							bluepayResult(result.result);

						default:
							try {
								bluepayResult(result.result);
							} catch (Exception e) {
								// TODO: handle exception
							}
							

							break;
						}
					}

				});

	}
	
	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		
		dialogDismiss();
		
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
		}
		mPaymentCallback = null;
		
		Yayalog.loger("充值成功关闭了窗口33333333333");
		
		mActivity.finish();
	}
	
	
	public void dialogDismiss(){
		if (greenp_dialog!=null) {
			if (greenp_dialog.dialog!=null) {
				greenp_dialog.dialog.dismiss();
				Yayalog.loger("onSuccess 中关闭了窗口");
			}
    		
		}
	}
	
	 GreenP_dialog greenp_dialog=null;
	
	// 支付宝支付结果
		private void bluepayResult(String result) {
			// TODO Auto-generated method stub
			System.out.println(result);
			JSONObject jsonstr = null;
			try {
				jsonstr = new JSONObject(result);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int err_code=jsonstr.optInt("err_code");
			String pay_str=jsonstr.optString("url");
			
			String billid=jsonstr.optString("billid");
			AgentApp.mPayOrder.id=billid;
			
			if(err_code==0){
				try {
					System.out.println(pay_str);
					
					 greenp_dialog = new GreenP_dialog(
							mActivity);
					greenp_dialog.dialogShow();
					WebView webView = greenp_dialog.getWebview();
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
							Yayalog.loger("greenblenurl:"+url);
							
							if (url.startsWith("weixin://wap/pay?")) {
			                    try {
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_VIEW);
									intent.setData(Uri.parse(url));
									mActivity.startActivity(intent);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									Yayalog.loger("未安装微信");
									e.printStackTrace();
									 return true;
								}

			                    return true;
			                }else if (parseScheme(url)) {
			                    try {
			                    	System.out.println("准备跳转kkkkkurl："+url);
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
			                        return true;
			                    }
			                }else if (url.contains("paysuccess")) {
			                	Yayalog.loger("充值成功关闭了窗口");
			                	
			                	
			                	onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
			                	 return true;
			                }else {
			                	return super.shouldOverrideUrlLoading(view, url);
							}
						   
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
					System.out.println(e.toString());
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(mActivity.getApplicationContext(),
									"网络出错，请重新支付", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		
			
		}


}
