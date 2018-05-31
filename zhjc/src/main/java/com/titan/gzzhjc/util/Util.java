package com.titan.gzzhjc.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Util {

	/**四舍五入数据，保留两位小数*/
	public static String round(String str){
		if(str == null || str.trim().equals("")){
			return "";
		}
		if(str.trim().equals("0")){
            return "0";
        }
		return new BigDecimal(str.trim()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**数据四舍五入取整*/
	public static String rounding(String str){
		if(str == null || str.trim().equals("")){
			return "";
		}
		if(str.trim().equals("0")){
			return "0";
		}
		return new BigDecimal(str.trim()).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**保留两位小数*/
	/*public static String roundTwo(String str){
		if(str == null || str.trim().equals("")){
			return "";
		}
		if(str.trim().equals("0")){
			return "0";
		}
		return new BigDecimal(str.trim()).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
	}*/

	/**小数转经纬度*/
	public static String xiaoZhJwd(double dd) {
		int du = (int) Math.floor(dd);
		double fff = (dd - du) * 60;
		int fen = (int) Math.floor(fff);
		DecimalFormat df = new DecimalFormat("0.000");
		String miao = df.format((fff - fen) * 60);
		String str = du + "°" + fen + "'" + miao + "\"";
		return str;
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
//		Pattern pattern = Pattern.compile("^[+-//d.]");//"^[+-\d]"  "[0-9]{1,}"
//		Matcher matcher = pattern.matcher((CharSequence)str);
//		boolean result=matcher.matches();
//		return result;
	}

	/** 判断当前手机是否有ROOT权限 */
	public static boolean isRoot(){
		boolean bool = false;

		try{
			if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
				bool = false;
			} else {
				bool = true;
			}
			Log.d("===============", "bool = " + bool);
		} catch (Exception e) {
			Log.d("==============", e.getMessage());
		}
		return bool;
	}

	/** 把TextView的光标放置在最后 */
	public static void setTextViewCursorLocation(TextView et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** 把edittext的光标放置在最后 */
	public static void setEditTextCursorLocation(EditText et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	public static int dp2px(Context mContext,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

	/*获取两点之间的距离*/
	public static Double Distance(double lat1, double lng1, double lat2,
								  double lng2) {
		Double R = 6370996.81;
		Double x = (lng2 - lng1) * Math.PI * R
				* Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
		Double y = (lat2 - lat1) * Math.PI * R / 180;

		Double distance = Math.hypot(x, y);

		return distance;
	}

	/** 打开GPS*/
	public static void openGps(Context context){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		boolean flag = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!flag){
			/* 下面代码作用 是如果GPS开启则关闭GPS 如果GPS关闭则开启GPS*/
			Intent gpsIntent = new Intent();
			gpsIntent.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
			gpsIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
			} catch (PendingIntent.CanceledException e) {
				e.printStackTrace();
			}
		}
	}

	/** 获取唯一识表示 mac地址 */
	public static String getWifiMacAddress(Context context) {
		String macAddress = "";
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiMgr.isWifiEnabled()) {
			// 如果wifi打开
			// wifiMgr.setWifiEnabled(false);
		} else {
			// 如果wifi关闭
			wifiMgr.setWifiEnabled(true);
		}
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			// 如果wifi关闭的情况下 可能获取不到
			macAddress = info.getMacAddress();
		}
		return macAddress;
	}
	/*获取两个点的中点*/
//	public static Point getMidPoint(Point p1, Point p2){
//		double lon = (p1.getX()+p2.getX())/2;
//		double lat = (p1.getY()+p2.getY())/2;
//		Point point = new Point(lon,lat);
//		return point;
//	}


}
