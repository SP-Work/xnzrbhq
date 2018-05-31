package com.otitan.xnbhq.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;

import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.service.PullParseXml;

import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jsqlite.Exception;

/**
 * 二调工具类
 */
public class EDUtil {

	private static EDUtil util;
	private Context context;

	public synchronized static EDUtil getInstance(Context context) {
		if (util == null) {
			try {
				util = new EDUtil(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return util;
	}

	public static void resetUtil() {
		util = null;
	}

	private EDUtil(Context context) throws Exception {
		this.context = context;
	}

	/**
	 *  attributeName
	 *  type
	 */
	public static List<Row> getEdAttributeList(Context ctx,String attributeName, String type) {
		AssetManager asset = ctx.getAssets();
		List<Row> list = null;
		try {
			InputStream input = asset.open("ED_" + type + ".xml");
			PullParseXml parseXml = new PullParseXml();
			list = parseXml.PullParseXML(input, attributeName);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 *  attributeName
	 *  type
	 */
	public static String getEdAttributetype(Context ctx, String attributeName,
			String type) {
		AssetManager asset = ctx.getAssets();
		String fieldtype = "";
		try {
			InputStream input = asset.open("ED_" + type + ".xml");
			PullParseXml parseXml = new PullParseXml();
			fieldtype = parseXml.PullParseXMLforFeildType(input, attributeName);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return fieldtype;
	}

	/** 获取xml中配置arrays 数据 */
	public static List<Row> getAttributeList(Context ctx, String name,
			String xml) {
		AssetManager asset = ctx.getAssets();
		List<Row> list = null;
		try {
			InputStream input = asset.open(xml);
			PullParseXml parseXml = new PullParseXml();
			list = parseXml.PullParseXML(input, name);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}

	//
	@SuppressLint("SimpleDateFormat")
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	public static void isLockScreen(Activity activity) {
		KeyguardManager mKeyguardManager = (KeyguardManager) activity
				.getSystemService(Context.KEYGUARD_SERVICE);

		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
			setAlarm(activity);
		}
	}

	public static void setAlarm(Activity activity) {
		AlarmManager alarmManager = (AlarmManager) activity
				.getSystemService(Context.ALARM_SERVICE);
		int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
		long timeOrLengthofWait = 1000;
		String ALARM_ACTION = "ALARM_ACTION";
		Intent intentToFire = new Intent(ALARM_ACTION);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(activity, 0,
				intentToFire, 0);
		alarmManager.set(alarmType, timeOrLengthofWait, alarmIntent);
	}

	public static boolean IsEmpty(Object obj) {
		if (obj != null) {
			return true;
		}
		return false;
	}

	/** 流逆转 */
	public static void decript(String path) {
		try {
			byte[] buffer = File2byte(path);
			ArrayUtils.reverse(buffer);
			FileOutputStream outputStream = new FileOutputStream(new File(path));
			outputStream.write(buffer, 0, buffer.length); 
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/*检查gps状态*/
	public static void toggleGPS(Context context) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
	
}
