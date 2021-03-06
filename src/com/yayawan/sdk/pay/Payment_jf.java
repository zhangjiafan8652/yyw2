package com.yayawan.sdk.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yayawan.sdk.bean.BillResult;
import com.yayawan.sdk.bean.Order;
import com.yayawan.sdk.bean.PayResult;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkCallback;
import com.yayawan.sdk.callback.KgameSdkPaymentCallback;
import com.yayawan.sdk.login.BaseView;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.pay.xml.Bluep_paymentxml;
import com.yayawan.sdk.utils.DialogUtil;
import com.yayawan.sdk.utils.ToastUtil;
import com.yayawan.sdk.utils.Util;
import com.yayawan.sdk.webview.MyWebViewClient;
import com.yayawan.utils.ViewConstants;

public class Payment_jf extends BaseView implements KgameSdkPaymentCallback {

	private Bluep_paymentxml mThisview;
	private PayResult mFirstResult;
	private KgameSdkPaymentCallback mPaymentCallback;
	private KgameSdkCallback mCallback;
	private Order mOrder;
	private ImageView mBack;
	private BillResult mBillResult;
	private WebView mWebView;
	private ProgressBar mLoading;
	private static final int BILLRESULT = 5;

	private static final int DATAERROR = 17;

	private static final int NETERROR = 18;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			DialogUtil.dismissDialog();
			switch (msg.what) {
			case BILLRESULT:
				if (mBillResult != null) {
					// mpDialog.dismiss();
					// if (mpDialog.isShowing()) {
					// mpDialog.dismiss();
					// }
					if (mBillResult.success == 1) {
						// 第二次确认操作失败
						onError(0);
						ToastUtil.showError(mContext, mBillResult.error_msg);
					} else if (mBillResult.success == 0) {

						// 支付操作成功等待到账
						onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
						ToastUtil.showSuccess(mContext, mBillResult.body);
					}
					mActivity.finish();
				}

				break;

			case DATAERROR:
				Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();
				mActivity.finish();
				break;
			default:
				Toast.makeText(mContext, "数据异常，请再次支付。", Toast.LENGTH_LONG)
						.show();

				mActivity.finish();

				break;
			}

		}

	};

	public Payment_jf(Activity mContext) {
		super(mContext);
	}

	@Override
	public View initRootview() {
		mThisview = new Bluep_paymentxml(mActivity);
		return mThisview.initViewxml();
	}

	@Override
	public void initView() {
		mFirstResult = (PayResult) mActivity.getIntent().getSerializableExtra(
				"result");
		Bundle bundle = Util.parseUrl(mFirstResult.action);
		AgentApp.mPayOrder.id = bundle.getString("req_id");
		mPaymentCallback = DgameSdk.mPaymentCallback;
	}

	@Override
	public void logic() {

		mCallback = DgameSdk.mCallback;
		// mPaymentCallback = (KgameSdkPaymentCallback) getIntent()
		// .getSerializableExtra("callback");
		mOrder = DgameSdk.mPayOrder;

		mBack = mThisview.getIv_back();
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getbill();
			}
		});

		mWebView = mThisview.getWv_mWebview();
		mLoading = mThisview.getPb_loading();
		mWebView.getSettings().setSavePassword(false);

		mWebView.getSettings().setSaveFormData(false);

		mWebView.getSettings().setAllowFileAccess(true);

		mWebView.getSettings().setBuiltInZoomControls(false);

		mWebView.getSettings().setDatabaseEnabled(true);

		mWebView.getSettings().setDomStorageEnabled(true);

		mWebView.getSettings().setAppCacheMaxSize(12582912L);

		mWebView.getSettings().setAppCacheEnabled(true);

		mWebView.getSettings().setCacheMode(-1);

		mWebView.getSettings().setSupportZoom(false);

		mWebView.getSettings().setDefaultTextEncodingName("utf-8");

		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);

		mWebView.getSettings().setBlockNetworkImage(false);

		// mWebView.getSettings().setUseWideViewPort(true);

		mWebView.getSettings().setLoadWithOverviewMode(true);

		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setBackgroundColor(-1);
		mWebView.setVisibility(0);
		mWebView.requestFocus();

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String paramString1,
					String paramString2, JsResult paramJsResult) {
				// Util.alert(paramAnonymousWebView.getContext(),
				// paramAnonymousString2);
				Toast.makeText(view.getContext(), paramString2,
						Toast.LENGTH_SHORT).show();
				paramJsResult.confirm();
				return true;
			}

			@Override
			public void onReceivedTitle(WebView paramAnonymousWebView,
					String paramAnonymousString) {
				super.onReceivedTitle(paramAnonymousWebView,
						paramAnonymousString);
			}

			@Override
			public void onExceededDatabaseQuota(String url,
					String databaseIdentifier, long currentQuota,
					long estimatedSize, long totalUsedQuota,
					WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(estimatedSize * 2);
			}

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				Log.d("MyApplication", message + " -- From line " + lineNumber
						+ " of " + sourceID);
			}

			@Override
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication",
						cm.message() + " -- From line " + cm.lineNumber()
								+ " of " + cm.sourceId());
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress != 100) {
					mLoading.setVisibility(View.VISIBLE);
					mWebView.setVisibility(View.INVISIBLE);
				} else {
					mLoading.setVisibility(View.INVISIBLE);
					mWebView.setVisibility(View.VISIBLE);
				}
				super.onProgressChanged(view, newProgress);
			}

		});

		mWebView.setWebViewClient(new WebViewClient() {
			
			
			
			@Override
			public void onLoadResource(WebView view, String url) {
				Log.i("onLoadResource", "onLoadResource:" + url);
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mWebView.getSettings().setBlockNetworkImage(false);
				CookieSyncManager.getInstance().sync();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("shouldOverrideUrlLoading", url);
				if (url.startsWith("yayapayment://success")) {
					view.cancelLongPress();
					view.stopLoading();
					String str = url.replace("yayapayment", "http");
					Bundle localBundle = Util.parseUrl(str);
					int status = Integer.valueOf(
							localBundle.getString("status")).intValue();
					if (status == 0) {

						onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
					} else {
						onError(status);
					}
					// finish();
					return true;
				}
				if (url.startsWith("yayapayment://cancel")) {
					view.cancelLongPress();
					view.stopLoading();
					onCancel();
					return true;
				}

				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				if (url.startsWith("http")) {
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.e("eee", "Error: " + description);
				super.onReceivedError(view, errorCode, description, failingUrl);
				view.loadData("<html><body></body></html>", "text/html",
						"UTF-8");
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				MyWebViewClient.onReceivedSslError(view, handler, error, "");
			}

		});

		mWebView.loadUrl(mFirstResult.action);
	}

	private void getbill() {
		// 查询支付结果
		DialogUtil.showDialog(mContext, "支付结果确认中...");
		// 查询订单状态
	
	}

	@Override
	public void onError(int paramInt) {

		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		// System.out.println("我是来过这里的..哈哈哈");
		mPaymentCallback = null;
		// mActivity.setResult(ResultCode.ERROR);
		mActivity.finish();
		if (ViewConstants.mPayActivity != null) {
			ViewConstants.mPayActivity.finish();
		}
	}

	@Override
	public void onCancel() {

		// mActivity.setResult(ResultCode.CANCEL);
		mActivity.finish();
		if (ViewConstants.mPayActivity != null) {
			ViewConstants.mPayActivity.finish();
		}
	}

	@Override
	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
			// System.out.println("这是张珈凡,支付成功了");
		}
		mPaymentCallback = null;
		// mActivity.setResult(ResultCode.ERROR);
		mActivity.finish();
		if (ViewConstants.mPayActivity != null) {
			ViewConstants.mPayActivity.finish();
		}

	}
}
