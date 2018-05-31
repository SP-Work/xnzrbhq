package com.otitan.xnbhq.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式工具类
 */
@SuppressLint("SimpleDateFormat")
public class UtilTime {


	private static SimpleDateFormat formatter;

	public static enum TimeType{
		/** 天地图矢量 */
		TIMETYPE,//yyyy/MM/dd/HH/mm/ss
		/** 天地图影像  */
		TIMETYPE1,//yyyy-MM-dd HH:mm:ss
		/** 天地图矢量标注 */
		TIMETYPE2//yyyy-MM-dd
	}

	/**
	 * 获取系统当前时间转化为20160120151643不带分隔符形式
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSystemtime1() {
		formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String[] split = str.split("/");
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < split.length; i++) {
			buff.append(split[i]);
		}
		return buff.toString();
	}

	/**
	 * 获取系统当前时间转化为2016-01-20 15:16:43
	 */
	public static String getSystemtime2() {
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;

	}

	/**
	 * 获取系统当前时间转化为2016-01-20
	 */
	public static String getSystemtime3() {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;

	}

}
