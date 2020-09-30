package com.yayawan.sdk.utils;

import com.yayawan.utils.Yayalog;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * 解决webView键盘遮挡问题的类
 * Created by zqy on 2016/11/14.
 */
public class KeyBoardListener {
    private Activity activity;
// private Handler mhanHandler;
  
  
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
  
    private static KeyBoardListener keyBoardListener;
  
  
    public static KeyBoardListener getInstance(Activity activity) {
// if(keyBoardListener==null){
        keyBoardListener=new KeyBoardListener(activity);
// }
        return keyBoardListener;
    }
  
  
    public KeyBoardListener(Activity activity) {
        super();
// TODO Auto-generated constructor stub
        this.activity = activity;
// this.mhanHandler = handler;
  
    }
  
  WebView mwebview;
  
  private LinearLayout.LayoutParams mwebviewLayoutParams;
    public void init(WebView web) {
    	mwebview=web;
  
        FrameLayout content = (FrameLayout) activity
                .findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                    	//Yayalog.loger("++mChildOfContent++++++++++++++++++++++++++");
                        possiblyResizeChildOfContent();
                    }
                });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
                .getLayoutParams();
        mwebviewLayoutParams=(LayoutParams) mwebview.getLayoutParams();
  
        temheight=mwebviewLayoutParams.height;
    }
    int temheight=1;
  
    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView()
                    .getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
// keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard
                        - heightDifference;
                
                mwebviewLayoutParams.height = usableHeightSansKeyboard
                        - heightDifference;
              //  Toast.makeText(activity, "键盘 起"+ mwebviewLayoutParams.height, 0).show();
            } else {
// keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
                mwebviewLayoutParams.height = temheight;
              //  Toast.makeText(activity, "键盘 落"+ mwebviewLayoutParams.height, 0).show();
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
            
        }
    }
  
  
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
  
  
// private void showLog(String title, String msg) {
// Log.d("Unity", title + "------------>" + msg);
// }
  
}