package com.yayawan.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.common.CommonData;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.bean.PayMethod;
import com.yayawan.sdk.login.SmallHelpActivity;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.MD5;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangjiafan on 2018/6/13.
 */

public class AndroidDelegate {
   
    private Activity mActivity;
    public AndroidDelegate (Activity mactivity){
        mActivity=mactivity;
    }
   
    
    
    @JavascriptInterface
    public String getVersion(){
      

        return  ViewConstants.SDKVERSION;
    }
    
    @JavascriptInterface
    public boolean GoToQQ(String qq){
        Yayalog.loger("qq号:"+qq+"...token:");

        if(isQQClientAvailable(mActivity)){
        	if (qq.equals("4000042115")) {
        		qq="938189213";
			}
			if (qq.equals("暂无")) {
				
			}else {
				 String url="mqqwpa://im/chat?chat_type=crm&uin="+qq+"&version=1&src_type=web&web_src=http:://wpa.b.qq.com";

				//String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqhao;
				mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
        }else{
            Toast.makeText(mActivity,"请安装QQ客户端",Toast.LENGTH_SHORT).show();
        }
        //mActivity.toTakePhoto(uid,token);

        return  true;
    }
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
    //window.androidDelegate.ClickImg(this.url);
 
    @JavascriptInterface
    public String getGameInfo(String key){
    	
    	return DeviceUtil.getGameInfo(mActivity,key);
    }
    
    @JavascriptInterface
    public String getAppid(){
    	
    	return  DeviceUtil.getAppid(mActivity);
    }
    
    @JavascriptInterface
    public String getUid(){
    	
    	return  YYWMain.mUser.uid;
    }
    
    @JavascriptInterface
    public String getToken(){
    	
    	return  YYWMain.mUser.token;
    }
    
    @JavascriptInterface
    public String httpPost(String url,String parm){
        Yayalog.loger("qq号:"+url+"...parm:"+parm);

        
        JSONObject mjson=  new JSONObject(parm);
        Iterator<String> keys = mjson.keys();

        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        
        while (keys.hasNext()) {  
        	String str = keys.next(); 
        	requestParams.addBodyParameter(str,mjson.getString(str));
        	//System.out.println(str);  
        } 
      
        
		

	
		httpUtils.send(HttpMethod.POST, url,requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String result) {
				// TODO Auto-generated method stub
				
				YYWMain.mPayCallBack.onPayFailed("1", "");
			}

			@Override
			public void onSuccess(ResponseInfo<String> result) {
				// TODO Auto-generated method stub
			}

        return  "";
    }

   

    /**
     * 获取SDCard的目录路径功能
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }
}
