package com.yayawan.sdk.db;

import java.io.File;

import com.yayawan.sdk.login.ViewConstants;

import android.os.Environment;

/**
 * 外部数据库,
 */
public class SDBHelper {

	
	public static String Rootpath=ViewConstants.dbpath;
	
    public static String DB_DIR = Environment.getExternalStorageDirectory()
            .getPath()
            + File.separator
            + Rootpath
            + File.separator
            + "com.yayawan.sdk.account.db";
    static {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            DB_DIR = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + Rootpath + File.separator
                    + "com.yayawan.sdk.account.db";
        } else {
            DB_DIR = Environment.getRootDirectory().getPath() + File.separator
                    + Rootpath + File.separator
                    + "com.yayawan.sdk.account.db";
        }

        File dbFolder = new File(DB_DIR);
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
    }
}
