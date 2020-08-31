package com.yayawan.implyy;

import java.lang.reflect.Method;
import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;
import com.yayawan.sdk.bean.Order;
import com.yayawan.sdk.bean.User;
import com.yayawan.sdk.callback.KgameSdkPaymentCallback;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.ViewConstants;
import com.yayawan.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class ChargerImplyylianhe implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {
	}

	private YYWOrder morder;

	// 渠道登陆，联合id idtoken支付
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				morder = order;
				pay_run(paramActivity,order);
				Yayalog.loger("lianhe");

			}

		});

	}

	public String orderId;

	

	private void pay_run(final Activity paramActivity,final YYWOrder morder) {

		Order order2 = new Order();

		order2.orderId = morder.orderId;
		order2.goods = morder.goods;
		order2.money = morder.money;
		order2.ext =  morder.ext;
		AgentApp.mUser = new User();
		AgentApp.mUser.setUid(new BigInteger(YYWMain.mUser.yywuid));
		AgentApp.mUser.setUser_uid((YYWMain.mUser.yywuid));
		AgentApp.mUser.setUserName(YYWMain.mUser.yywusername);
		AgentApp.mUser.setToken(YYWMain.mUser.yywtoken);

		Yayalog.loger("yylianhe支付传入的uid：" + AgentApp.mUser.toString());
		
		if (DeviceUtil.isXiaomi(paramActivity)) {
			DgameSdk.GreenblueP(paramActivity, order2, 1,new KgameSdkPaymentCallback() {

	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
	            public void onCancel() {
	                if (YYWMain.mPayCallBack != null) {
	                    YYWMain.mPayCallBack.onPayCancel("cancel", "");
	                }
	                checkOrderStatus(paramActivity,  morder.orderId,morder.money+"");
	            }

	            @Override
	            public void onError(int arg0) {
	                if (YYWMain.mPayCallBack != null) {
	                    YYWMain.mPayCallBack.onPayFailed("failed", "");
	                }
	                checkOrderStatus(paramActivity,  morder.orderId,morder.money+"");
	            }

	            @Override
	            public void onSuccess(User user, Order order, int arg2) {
	                if (YYWMain.mPayCallBack != null) {
	                    YYWUser yywUser = new YYWUser();

	                    yywUser.uid = user.uid + "";
	                    yywUser.icon = user.icon;
	                    yywUser.body = user.body;
	                    yywUser.lasttime = user.lasttime;
	                    yywUser.money = user.money;
	                    yywUser.nick = user.nick;
	                    yywUser.password = user.password;
	                    yywUser.phoneActive = user.phoneActive;
	                    yywUser.success = user.success;
	                    yywUser.token = user.token;
	                    yywUser.userName = user.userName;

	                    YYWOrder yywOrder = new YYWOrder();

	                    yywOrder.orderId = order.orderId;
	                    yywOrder.ext = order.ext;
	                    yywOrder.gameId = order.gameId;
	                    yywOrder.goods = order.goods;
	                    yywOrder.id = order.id;
	                    yywOrder.mentId = order.mentId;
	                    yywOrder.money = order.money;
	                    yywOrder.paytype = order.paytype;
	                    yywOrder.serverId = order.serverId;
	                    yywOrder.status = order.status;
	                    yywOrder.time = order.time;
	                    yywOrder.transNum = order.transNum;
	                    
	                    pushQQdata(order.money + "",order.id);
	                    pushUmengdata(order.money/100+"");
	                    
	                    YYWMain.mPayCallBack.onPaySuccess(yywUser, yywOrder, "success");
	                }
	            }

	        });
		}else {
		DgameSdk.payment(paramActivity, order2, false,
				new KgameSdkPaymentCallback() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onCancel() {
						if (YYWMain.mPayCallBack != null) {
							YYWMain.mPayCallBack.onPayCancel("cancel", "");
						}
						
						checkOrderStatus(paramActivity,  morder.orderId,morder.money+"");
					}

					@Override
					public void onError(int arg0) {
						if (YYWMain.mPayCallBack != null) {
							YYWMain.mPayCallBack.onPayFailed("failed", "");
						}
						checkOrderStatus(paramActivity,  morder.orderId,morder.money+"");
					}

					@Override
					public void onSuccess(User user, Order order, int arg2) {
						if (YYWMain.mPayCallBack != null) {
							YYWUser yywUser = new YYWUser();

							yywUser.uid = user.uid + "";
							yywUser.icon = user.icon;
							yywUser.body = user.body;
							yywUser.lasttime = user.lasttime;
							yywUser.money = user.money;
							yywUser.nick = user.nick;
							yywUser.password = user.password;
							yywUser.phoneActive = user.phoneActive;
							yywUser.success = user.success;
							yywUser.token = user.token;
							yywUser.userName = user.userName;

							YYWOrder yywOrder = new YYWOrder();

							yywOrder.orderId = order.orderId;
							yywOrder.ext = order.ext;
							yywOrder.gameId = order.gameId;
							yywOrder.goods = order.goods;
							yywOrder.id = order.id;
							yywOrder.mentId = order.mentId;
							yywOrder.money = order.money;
							yywOrder.paytype = order.paytype;
							yywOrder.serverId = order.serverId;
							yywOrder.status = order.status;
							yywOrder.time = order.time;
							yywOrder.transNum = order.transNum;
							
							pushQQdata(order.money + "",order.id);
							pushUmengdata(order.money / 100 + "");
							
							

							YYWMain.mPayCallBack.onPaySuccess(yywUser,
									yywOrder, "success");
						}
					}

				});

		}
		
		
	}

	// 支付成功上报
	public void pushUmengdata(String money) {
		try {
			Class<?> subclass = Class
					.forName("com.yayawan.impl.ActivityStubImpl");
			Method[] methods = subclass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].equals("payumenSucceed")) {
					new com.yayawan.impl.ActivityStubImpl()
							.payumenSucceed(money);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Yayalog.loger("未找到ActivityStubImpl");
		}
	}
	
	// 支付成功调用应用宝上报
	public static void pushQQdata(String money,String orderid) {
		
		Yayalog.loger("pushQQdata+++++++++++++++++++++");
			try {
				Class<?> subclass = Class
						.forName("com.yayawan.impl.ActivityStubImpl");
				Method[] methods = subclass.getMethods();
				for (int i = 0; i < methods.length; i++) {
					Yayalog.loger("methoth："+methods[i]);
					if (methods[i].toString().contains("pSucc")) {
						Yayalog.loger("找到了pSucc++++++++++++++++++++++");
						com.yayawan.impl.ActivityStubImpl.pSucc(money,orderid);
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Yayalog.loger("未找到pSucc++++++++++++++++++++++");
			}
	}
	

	/**
	 * 查询订单状态
	 * @param money
	 * @param orderid
	 */
	public static void checkOrderStatus( Activity paramActivity,final String orderid,final String money){
		HttpUtils mhttputils=new HttpUtils();
		
		RequestParams requestParams = new RequestParams();
		
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(paramActivity));
		
		requestParams.addBodyParameter("orderid", orderid);
		Yayalog.loger("查询订单状态接口");
		Yayalog.loger("app_id", DeviceUtil.getAppid(paramActivity));
	
		Yayalog.loger("orderid", orderid);
		
		mhttputils.send(HttpMethod.POST, ViewConstants.orderstatus,requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String re=arg0.result;
				Yayalog.loger("查询订单状态返回："+re);
				try {
					JSONObject jsonObject = new JSONObject(re);
					int sta=jsonObject.optInt("status");
					if (sta==3) {
						Yayalog.loger("上报订单状态sta："+sta);
						pushQQdata( money, orderid);
					}else {
						Yayalog.loger("上报订单状态为失败："+sta);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
}
