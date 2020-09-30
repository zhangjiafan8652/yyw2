package com.yayawan.sdk.pay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.yayawan.common.CommonData;
import com.yayawan.sdk.main.AgentApp;
import com.yayawan.sdk.main.DgameSdk;
import com.yayawan.sdk.pay.XiaomiPayxml.XiaomiPayListener;
import com.yayawan.sdk.pay.YingYongBaoPayxml.YingyongbaoListener;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Yayalog;

public class XiaoMipayActivity extends Activity {

	XiaomiPayxml xiaomiPayxml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Yayalog.loger(DeviceUtil.getUnionid(this));
		if (DeviceUtil.getUnionid(this).equals("2958292331")) {
			StartYingBaoPay();
		}else{
		
		xiaomiPayxml = new XiaomiPayxml(this);
		//View initViewxml = new XiaomiPayxml(this).initViewxml();
		setContentView(xiaomiPayxml.initViewxml());
		xiaomiPayxml.setPrice(Integer.parseInt(AgentApp.mPayOrder.money/100+""));
		
		String gamename=DeviceUtil.getGameInfo(getApplicationContext(), "gamename");
		String moneyname=DeviceUtil.getGameInfo(getApplicationContext(), "moneyname");
		xiaomiPayxml.setGoodsText(gamename+"-"+moneyname);
		xiaomiPayxml.addXiaomiPayListener(new XiaomiPayListener() {
			
			@Override
			public void onGoToPay(int selectpaytype) {
				// TODO Auto-generated method stub
				
				if (selectpaytype==xiaomiPayxml.BLUEP) {
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,CommonData.BLUEP , DgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else if(selectpaytype==xiaomiPayxml.GREENP){
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,CommonData.GREENP , DgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else {
					//选择了小米支付，就把界面关闭，以后都不会打开了
					GreenblueP.isselectxiaomipay=true;
					Toast.makeText(XiaoMipayActivity.this, "小米钱包初始化完毕，请重新点击商品", Toast.LENGTH_LONG).show();
					finish();
				}
				
				
				}
			});
		
		}
	}
	
	YingYongBaoPayxml yingyongbaoPayxml ;
	private void StartYingBaoPay() {
		Yayalog.loger("StartYingBaoPay");
		// TODO Auto-generated method stub
		 yingyongbaoPayxml = new YingYongBaoPayxml(this);
			//View initViewxml = new XiaomiPayxml(this).initViewxml();
		setContentView(yingyongbaoPayxml.initViewxml());
		
		
		yingyongbaoPayxml.setPrice(Integer.parseInt(AgentApp.mPayOrder.money/100+""));
		
	//	String gamename=DeviceUtil.getGameInfo(getApplicationContext(), "gamename");
		String moneyname=DeviceUtil.getGameInfo(getApplicationContext(), "moneyname");
		//yingyongbaoPayxml.setGoodsText(gamename+"-"+moneyname);
		yingyongbaoPayxml.addXiaomiPayListener(new YingyongbaoListener() {
			
			@Override
			public void onGoToPay(int selectpaytype) {
				// TODO Auto-generated method stub
				
				if (selectpaytype==yingyongbaoPayxml.BLUEP) {
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,CommonData.BLUEP , DgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else if(selectpaytype==yingyongbaoPayxml.GREENP){
					GreenblueP greenbluePay = new GreenblueP(XiaoMipayActivity.this, AgentApp.mPayOrder,CommonData.GREENP , DgameSdk.mPaymentCallback);
					greenbluePay.greenP();
				}else {
					//选择了小米支付，就把界面关闭，以后都不会打开了
					GreenblueP.isselectxiaomipay=true;
					Toast.makeText(XiaoMipayActivity.this, "支付更新完毕，请重新点击商品", Toast.LENGTH_LONG).show();
					finish();
				}
				
				
				}
			});
	}
	
	
}
