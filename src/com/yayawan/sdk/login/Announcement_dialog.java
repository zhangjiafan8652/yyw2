package com.yayawan.sdk.login;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.sdk.bean.Question;
import com.yayawan.sdk.utils.Basedialogview;
import com.yayawan.sdk.webview.AdvancedWebView;
import com.yayawan.sdk.xml.GetAssetsutils;
import com.yayawan.sdk.xml.MachineFactory;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.PermissionUtils;
import com.yayawan.utils.Sputils;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;

public class Announcement_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private AdvancedWebView lv_helpcontent;
	private ProgressBar pb_mPb;
	private ArrayList<Question> mQuestionList;
	private String html;
	private ImageView ib_mAgreedbox;
	private ImageView ib_mNotAgreedbox;
	private ImageView ib_mClosebutton;
	
	private Activity mActivity;
	protected static final int SHOWCONTENT = 3;

	public Announcement_dialog(Activity activity) {
		super(activity);
		mActivity=activity;
	}
	public Announcement_dialog(Activity activity,String html) {
		
		super(activity);
		mActivity=activity;
		Yayalog.loger("wo"+html);
		this.html=html;
		initlogic();
		
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 900;
		int ho_with = 900;
		int po_height = 900;
		int po_with = 900;

		int height = 0;
		int with = 0;
		int pad = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
			with = ho_with;

		} else if ("portrait".equals(orientation)) {
			height = po_height;
			with = po_with;
		}

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout ll_content = new RelativeLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, mLinearLayout, 2,
				0);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		//ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 144, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 144, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 60, 60, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		ll_mPre.setClickable(true);
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"游戏公告", 57, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		pb_mPb = new ProgressBar(mActivity);
		machineFactory.MachineView(pb_mPb, 60, 60, mLinearLayout, 2, 600);

		// 帮助的列表内容
		lv_helpcontent = new AdvancedWebView(mActivity);
		machineFactory.MachineView(lv_helpcontent, MATCH_PARENT, MATCH_PARENT,
				0, mLinearLayout, 0, 0, 0, 0, 100);
		

		
		// 下次不提示
		LinearLayout ll_clause = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_clause, MATCH_PARENT, 60, mLinearLayout,
				0, 0);
		ll_clause.setGravity(Gravity.CENTER_VERTICAL);
		
		ll_clause.setBackgroundColor(Color.parseColor("#11FFFFFF"));
		//ll_clause.setBackground(null);

		//new ImageView(context)
		
		// 关闭按钮
				ib_mClosebutton = new ImageView(mActivity);
				machineFactory.MachineView(ib_mClosebutton, 60, 60, mLinearLayout, 2, 7);
				//ib_mClosebutton.setScaleType(ImageView.ScaleType.FIT_CENTER);
				ib_mClosebutton.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya_x.png", mActivity));
				ib_mClosebutton.setBackgroundDrawable(null);
			
				ib_mClosebutton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		
		
		// 不再提示
		ib_mAgreedbox = new ImageView(mActivity);
		machineFactory.MachineView(ib_mAgreedbox,  50, 50, 0, mLinearLayout, 15,
				7,0,0, 0);
		ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkedboxyellow.png", mActivity));
		ib_mAgreedbox.setBackgroundDrawable(null);
		ib_mAgreedbox.setVisibility(View.GONE);
		Sputils.putSPint(ViewConstants.SP_ISVIEWYAYAWANDOWNLOADBOXNOTICE, 1, mActivity);
		//ib_mAgreedbox.setScaleType(ImageView.ScaleType.FIT_CENTER);

		// 不再提示
		ib_mNotAgreedbox = new ImageView(mActivity);
		machineFactory.MachineView(ib_mNotAgreedbox, 50, 50, 0, mLinearLayout, 15,
				7,0,0, 0);
		ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkbox.png", mActivity));
		ib_mNotAgreedbox.setBackgroundDrawable(null);
		//ib_mNotAgreedbox.setVisibility(View.GONE);

		TextView tv_agree = new TextView(mActivity);
		machineFactory.MachineTextView(tv_agree, WRAP_CONTENT, MATCH_PARENT, 0,
				"不再提示", 33, mLinearLayout, 6,5, 0, 2);
		tv_agree.setTextColor(Color.parseColor("#b4b4b4"));
		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
		
		// TODO
		ll_clause.addView(ib_mClosebutton);
		ll_clause.addView(ib_mAgreedbox);
		ll_clause.addView(ib_mNotAgreedbox);
		ll_clause.addView(tv_agree);
		ib_mAgreedbox.setClickable(true);
		ib_mAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mAgreedbox.setVisibility(View.GONE);
				ib_mNotAgreedbox.setVisibility(View.VISIBLE);
				Sputils.putSPint(ViewConstants.SP_ISVIEWYAYAWANDOWNLOADBOXNOTICE, 1, mActivity);
			}
		});
		ib_mNotAgreedbox.setClickable(true);
		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mNotAgreedbox.setVisibility(View.GONE);
				ib_mAgreedbox.setVisibility(View.VISIBLE);
				Sputils.putSPint(ViewConstants.SP_ISVIEWYAYAWANDOWNLOADBOXNOTICE, 0, mActivity);
			}
		});
		
		
		
		
		//ll_content.addView(rl_title);
		ll_content.addView(pb_mPb);
		ll_content.addView(lv_helpcontent);
		
		ll_content.addView(ll_clause);

		// baselin.addView(rl_title);
		//baselin.addView(ll_clause);
		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 1f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		
	}

	private void initlogic() {
		
		WebSettings settings = lv_helpcontent.getSettings();
		settings.setSupportZoom(false); // 支持缩放
		settings.setBuiltInZoomControls(false); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true); // 启用JS脚本
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 关闭webview中缓存
		
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		Yayalog.loger("ni..."+html);
		//lv_helpcontent.loadUrl("http://danjiyou.duapp.com/Home/Blog/index");
		settings.setDefaultTextEncodingName("utf-8"); //设置文本编码
		lv_helpcontent.setVerticalScrollBarEnabled(false);
		lv_helpcontent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            	//System.out.println("chang anle");
            	WebView.HitTestResult result = lv_helpcontent.getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                    	imgurl = result.getExtra();
//                        if (mOnSelectItemListener != null && url != null && URLUtil.isValidUrl(url)) {
//                            mOnSelectItemListener.onSelected(touchX, touchY, result.getType(), url);
//                        }
                    	   if (!(PermissionUtils.checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)&&PermissionUtils.checkPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
								PermissionUtils.requestPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionUtils.WRITE_EXTERNAL_STORAGE);
								
								
							}else {
								try {
									 new SaveImage().execute();
										
								} catch (Exception e) {
									// TODO: handle exception
								}
								
							}
                       
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;
                }
               
            	
				return true;
                }
            });
		lv_helpcontent.loadData(html, "text/html; charset=UTF-8", null);
	}
	
	private String imgurl = "";

    /***
     * 功能：用线程保存图片
     *
     * @author wangyp
     */
    private class SaveImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            //System.out.println(imgurl);
            try {
                String sdcard = Environment.getDownloadCacheDirectory().toString();
                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }
                int idx = imgurl.lastIndexOf(".");
                String ext = imgurl.substring(idx);
                file = new File(sdcard + "/Download/" + new Date().getTime()+".jpg" );
                InputStream inputStream = null;
                URL url = new URL(imgurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                if (conn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                }
                byte[] buffer = new byte[4096];
                int len = 0;
                FileOutputStream outStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                result = "图片已保存至：" + file.getAbsolutePath();
                mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mActivity, "图片已经保存到相册", 0).show();
					}
				});
                //System.out.println(result);
                Yayalog.loger(result);
            } catch (Exception e) {
                result = "保存失败！" + e.getLocalizedMessage();
                Yayalog.loger(result);
                //System.out.println(result);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
           // showToast(result);
        }
    }

}
