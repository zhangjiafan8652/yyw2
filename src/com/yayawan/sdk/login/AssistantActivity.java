package com.yayawan.sdk.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yayawan.sdk.webview.AndroidBug5497Workaround;
import com.yayawan.sdk.webview.IWebPageView;
import com.yayawan.sdk.webview.MyJavascriptInterface;
import com.yayawan.sdk.webview.MyWebChromeClient;
import com.yayawan.sdk.webview.MyWebViewClient;
import com.yayawan.sdk.xml.Assistant_xml;
import com.yayawan.sdk.xml.SmallHelp_xml;
import com.yayawan.utils.Yayalog;

public class AssistantActivity extends Activity implements IWebPageView {

  
    private WebView webView;
 
    // 加载视频相关
    private MyWebChromeClient mWebChromeClient;
    // 网页链接
    private String mUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //防止输入法遮挡文字。
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
        Assistant_xml smallHelp_xml = new Assistant_xml(this);
		setContentView(smallHelp_xml.initViewxml());
		AndroidBug5497Workaround.assistActivity(this);
		this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);  
		
		webView = smallHelp_xml.getWv_mWeiboview();
       
        getIntentData();
        initTitle();
        initWebView();
        Yayalog.loger("开始聊天："+mUrl);
        webView.loadUrl(mUrl+"&k=123");
       // getDataFromBrowser(getIntent());
    }

    private void getIntentData() {
    	
        mUrl = getIntent().getStringExtra("mUrl");
        
    }


    private void initTitle() {
     
      
     
        initToolBar();
    }

    private void initToolBar() {
     
      
     
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "NewApi"})
    private void initWebView() {
      
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
//        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        webView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        webView.addJavascriptInterface(new MyJavascriptInterface(this), "injectedObject");
        webView.setWebViewClient(new MyWebViewClient(this));
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return handleLongImage();
            }
        });

    }

    @Override
    public void hindProgressBar() {
      
    }

    @Override
    public void showWebView() {
        
    }

    @Override
    public void hindWebView() {
      
    }

    @Override
    public void fullViewAddView(View view) {
      
    }

    @Override
    public void showVideoFullView() {
       
    }

    @Override
    public void hindVideoFullView() {
        
    }

    @Override
    public void startProgress(int newProgress) {
       
    }

    public void setTitle(String mTitle) {
       
    }

    /**
     * android与js交互：
     * 前端注入js代码：不能加重复的节点，不然会覆盖
     * 前端调用js代码
     */
    @Override
    public void addImageClickListener() {
        loadImageClickJS();
        loadTextClickJS();
        loadCallJS();
    }

    /**
     * 前端注入JS：
     * 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
     */
    private void loadImageClickJS() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"));}" +
                "}" +
                "})()");
    }

    /**
     * 前端注入JS：
     * 遍历所有的<li>节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
     */
    private void loadTextClickJS() {
        webView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"li\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }

    /**
     * 传应用内的数据给html，方便html处理
     */
    private void loadCallJS() {
        // 无参数调用
        webView.loadUrl("javascript:javacalljs()");
        // 传递参数调用
        webView.loadUrl("javascript:javacalljswithargs('" + "android传入到网页里的数据，有参" + "')");
    }

   

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
            mWebChromeClient.mUploadMessage(intent, resultCode);
        } else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
        }
    }


    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    /**
     * 作为三方浏览器打开传过来的值
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                Log.e("data", text);
                String url = scheme + "://" + host + path;
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直接通过三方浏览器打开时，回退到首页
     */
    public void handleFinish() {
       
       
    }

    /**
     * 长按图片事件处理
     */
    private boolean handleLongImage() {
      
        return false;
    }

   
    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        webView.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
       
        if (webView != null) {
          
            webView.removeAllViews();
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    /**
     * 打开网页:
     *
     * @param mContext 上下文
     * @param mUrl     要加载的网页url
     * @param mTitle   标题
     */
    public static void loadUrl(Context mContext, String mUrl, String mTitle) {
      
    }

    public static void loadUrl(Context mContext, String mUrl) {
      
    }
}

