package com.yayawan.impl;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.bun.miitmdid.core.ErrorCode;

import com.bun.miitmdid.core.MdidSdk;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;

import com.yayawan.utils.Sputils;

/**
 * Created by zheng on 2019/8/22.
 */

public class MiitHelper implements IIdentifierListener {

    private AppIdsUpdater _listener;
    public MiitHelper(AppIdsUpdater callback){
        _listener=callback;
    }
    public MiitHelper(){
        
    }

    public void getDeviceIds(Context cxt){
        long timeb=System.currentTimeMillis();
        int nres = CallFromReflect(cxt);
//        int nres=DirectCall(cxt);
        long timee=System.currentTimeMillis();
        long offset=timee-timeb;
        if(nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT){//不支持的设备

        	//Toast.makeText(cxt, "miitHelperoaid======================不支持的设备", 0).show();
        }else if( nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE){//加载配置文件出错
        //	Toast.makeText(cxt, "miitHelperoaid======================加载配置文件出错", 0).show();
        }else if(nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT){//不支持的设备厂商
        	//Toast.makeText(cxt, "miitHelperoaid======================不支持的设备厂商", 0).show();
        }else if(nres == ErrorCode.INIT_ERROR_RESULT_DELAY){//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
        	//Toast.makeText(cxt, "miitHelperoaid======================获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程", 0).show();
        }else if(nres == ErrorCode.INIT_HELPER_CALL_ERROR){//反射调用出错
        	//Toast.makeText(cxt, "miitHelperoaid======================反射调用出错", 0).show();
        }
        //Log.d(getClass().getSimpleName(),"return value: "+String.valueOf(nres));

    }


    /*
    * 通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
    *
    * */
    public int CallFromReflect(Context cxt){
    	
        return MdidSdkHelper.InitSdk(cxt,true,this);
    }

    /*
    * 直接java调用，如果这样调用，在android 9以前没有题，在android 9以后会抛找不到so方法的异常
    * 解决办法是和JLibrary.InitEntry(cxt)，分开调用，比如在A类中调用JLibrary.InitEntry(cxt)，在B类中调用MdidSdk的方法
    * A和B不能存在直接和间接依赖关系，否则也会报错
    *
    * */
    public int DirectCall(Context cxt){
        MdidSdk sdk = new MdidSdk();
        return sdk.InitSdk(cxt,this);
    }
   public static String oaid="";
    String vaid="";
    String aaid="";
    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
    	//System.out.println("==============调用了suport");
        if(_supplier==null) {
            return;
        }
        
         oaid=_supplier.getOAID();
       //  vaid=_supplier.getVAID();
        // aaid=_supplier.getAAID();
        //String udid=_supplier.getUDID();
       // StringBuilder builder=new StringBuilder();
        //builder.append("support: ").append(isSupport?"true":"false").append("\n");
        //builder.append("UDID: ").append(udid).append("\n");
       // builder.append("OAID: ").append(oaid).append("\n");
       // builder.append("VAID: ").append(vaid).append("\n");
       // builder.append("AAID: ").append(aaid).append("\n");
       // String idstext=builder.toString();
        //_supplier.shutDown();
        if(_listener!=null){
            _listener.OnIdsAvalid(oaid);
        }
        
       
    }
    public interface AppIdsUpdater{
        void OnIdsAvalid(  String ids);
    }

}
