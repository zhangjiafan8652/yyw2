package com.yayawan.sdk.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yayawan.sdk.login.AssistantActivity;

/**
 * Created by jingbin on 2016/11/17.
 * 监听网页链接:
 * - 根据标识:打电话、发短信、发邮件
 * - 进度条的显示
 * - 添加javascript监听
 * - 唤起京东，支付宝，微信原生App
 */
public class MyWebViewClient extends WebViewClient {

    private IWebPageView mIWebPageView;
    private AssistantActivity mActivity;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        mActivity = (AssistantActivity) mIWebPageView;

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Log.e("jing", "----url:" + url);
    	
    
    	
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        if (url.startsWith("http:") || url.startsWith("https:")) {
            // 可能有提示下载Apk文件
            if (url.contains(".apk")) {
               // handleOtherwise(mActivity, url);
                return true;
            }
            return false;
        }
      //  Toast.makeText(mActivity, ""+url, 0).show();
        handleOtherwise(mActivity, url);
        return true;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
      
        // html加载完成之后，添加监听图片的点击js函数
      
        super.onPageFinished(view, url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String
            failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (errorCode == 404) {
            //用javascript隐藏系统定义的404页面信息
            String data = "Page NO FOUND！";
            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
        }
    }

    // 视频全屏播放按返回页面被放大的问题
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            view.setInitialScale((int) (oldScale / newScale * 100)); //异常放大，缩回去。
        }
    }

    /**
     * 网页里可能唤起其他的app
     */
    private void handleOtherwise(Activity activity, String url) {
       
        if (url.contains("closeconversation")) {
        	// Toast.makeText(mActivity, ""+url, 0).show();
        	activity.finish();
		}
    }

    private void startActivity(String url) {
        try {

            // 用于DeepLink测试
            if (url.startsWith("will://")) {
                Uri uri = Uri.parse(url);
                Log.e("---------scheme", uri.getScheme() + "；host: " + uri.getHost() + "；Id: " + uri.getPathSegments().get(0));
            }

            Intent intent1 = new Intent();
            intent1.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent1.setData(uri);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
    		SslError error) {
    	// TODO Auto-generated method stub
    	onReceivedSslError(view, handler, error,null);
    }
    
    public static void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error,String temp) {
       // final SslErrorHandler mHandler ;
       // mHandler= handler;
   
    	 AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
    	    builder.setMessage("SSL认证失败，是否继续访问？");
    	    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	            handler.proceed();
    	        }
    	    });
    	    
    	    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	            handler.cancel();
    	        }
    	    });
    	    
    	    AlertDialog dialog = builder.create();

        
  
    }
}
