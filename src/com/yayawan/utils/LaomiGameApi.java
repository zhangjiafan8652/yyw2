package com.yayawan.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.yayawan.sdk.login.SmallHelpActivity;
import com.yayawan.sdk.webview.AdvancedWebView;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.Yayalog;


public class LaomiGameApi {

  
    public Activity mActivity;

    public AdvancedWebView mWebView;

    public LaomiGameApi(Activity mactivity, AdvancedWebView webview) {
        this.mActivity = mactivity;
        this.mWebView = webview;
    }
	public LaomiGameApi(Activity mactivity) {
		this.mActivity = mactivity;

	}


//弹提示
    @JavascriptInterface
	public  void showToast(final String msg){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity,msg,Toast.LENGTH_LONG).show();
            }
        });




	}
    @JavascriptInterface
    public  void exit( ){

        mActivity.finish();
       // Toast.makeText(mActivity,msg,Toast.LENGTH_LONG).show();

     //   mWebView.loadUrl("javascript:LaomiGameCallBack.uploadresult(" + 1111 + ")");

    }
	



    @JavascriptInterface
    public void goToAssistant() {
        Yayalog.loger("gameapisetdata:");
        Intent intent = new Intent(mActivity, SmallHelpActivity.class);

        mActivity.startActivity(intent);
    }

    @JavascriptInterface
    public void saveStringToLocal( String key,String valuemsg) {
        Sputils.putSPstring(key,valuemsg,mActivity);

    }
    @JavascriptInterface
    public String getStringFromLocal( String key,String defaultvalue) {
        final String value=Sputils.getSPstring(key,defaultvalue,mActivity);

        return value;
    }


 


    @JavascriptInterface
    public String getAppid() {


        return DeviceUtil.getAppid(mActivity);
    }

    public  void shareImage( Uri uri) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        mActivity.startActivity(Intent.createChooser(intent, "分享到"));
    }
    //系统分享
    @JavascriptInterface
    public  void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        mActivity.startActivity(Intent.createChooser(intent, "分享到"));
    }





}
