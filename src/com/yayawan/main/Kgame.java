package com.yayawan.main;

import com.yayawan.proxy.CommonGameProxy;





public class Kgame {

	private static final CommonGameProxy mInstance = new CommonGameProxy();

	/**
	 * @param args
	 */
	public static CommonGameProxy getInstance() {
		// TODO Auto-generated method stub
		
		return mInstance;
	}

}
