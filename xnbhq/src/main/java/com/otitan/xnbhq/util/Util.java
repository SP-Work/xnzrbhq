package com.otitan.xnbhq.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;
import jsqlite.Exception;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

import com.otitan.xnbhq.MyApplication;

/**
 * Created by li on 2016/5/26.
 * Util工具类
 */
public class Util {

	/**四舍五入数据，保留两位小数*/
	public static String round(String str){
		if(str == null || str.trim().equals("")){
			return "";
		}
		if(str.trim().equals("0")){
			return "0";
		}
		return new BigDecimal(str.trim()).setScale(6, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**根据县乡村的代码获取县乡村列表*/
	public static Map<String, String> getXXCDomain(Context context,String key,String upStr,String tbname){
		Map<String, String> map = new HashMap<>();
		Map<String, String> values = new HashMap<>();
		if(tbname.equals("xian")){
			values = getXianValue(context);
		}else if(tbname.equals("xiang")){
			map = getXiangValue(context);
			for(String str : map.keySet()){
				if(str.contains(upStr)){
					values.put(str,map.get(str));
				}
			}
		}else if(tbname.equals("cun")){
			map = getCunValue(context);
			for(String str : map.keySet()){
				if(str.contains(upStr)){
					values.put(str,map.get(str));
				}
			}
		}
		return values;
	}

	/**获取县乡村的代码值*/
	public static String getXXCValue(Context context,String key,String upStr,Map<String, String> map){

		String value = key;
		String str = map.get(key);
		if(str != null){
			value = str;
		}else{
			if(!key.contains(upStr)){
				String str1 = map.get(upStr+key);
				if(str1 != null){
					value = str1;
				}
			}
		}
		return value;
	}

	/**获取所有县名称及县代码*/
	public static Map<String,String> getXianValue(Context context) {
		final Map<String,String> map = new HashMap<>();
		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");// "kaiyangxian.sqlite"
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from xian";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					map.put(data[5], data[4]);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**获取所有xiang名称xiang代码*/
	public static Map<String,String> getXiangValue(Context context) {
		final HashMap<String, String> map = new HashMap<String, String>();

		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");// "kaiyangxian.sqlite"
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from xiang";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6

					map.put(data[5]+data[9],data[10]);

					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**获取所有村名称及村代码*/
	public static Map<String,String> getCunValue(Context context) {
		final Map<String,String> map = new HashMap<String, String>();
		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");// "kaiyangxian.sqlite"
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from cun";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					map.put(data[3]+data[4]+data[5], data[6]);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**复制文件到平板中*/
	public static void copyFile(Context context,String fileDir,String assetPath, String filename){
		try {
			InputStream db = context.getResources().getAssets().open(assetPath);
			FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
			byte[] buffer = new byte[8129];
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**检查String字符串是否是纯数字*/
	public static boolean CheckStrIsDouble(String str){
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 判断当前手机是否有ROOT权限 */
	public static boolean isRoot() throws Exception{
		boolean bool = false;

		if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
			bool = false;
		} else {
			bool = true;
		}
		Log.d("===============", "bool = " + bool);
		return bool;
	}

	/*dp转化为px*/
	public static int dp2px(Context mContext,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

	/**

	 * 获取当前 jvm 的内存信息

	 *

	 * @return

	 */

	public static String toMemoryInfo() {

		Runtime currRuntime = Runtime.getRuntime ();

		int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);

		int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);

		return nFreeMemory + "M/" + nTotalMemory +"M(free/total)" ;

	}

	/** int颜色值 转化为rgb值并含有透明度 */
	public static int getColor(int color, int tmd) {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		int tt = tmd*255/100;
		return Color.argb(255-tt, red, green, blue);
	}


}
