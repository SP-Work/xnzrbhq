package com.otitan.xnbhq.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
/**
 * Created by li on 2017/5/31.
 * 判断设备是否为大屏幕设备类
 */
public class PadUtil {

	/**
	 * 判断设备是否为大于6.0屏幕的设备
	 */
	@SuppressWarnings("unused")
	public static boolean isPad(Context context) {

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		float screenWidth = display.getWidth();
		@SuppressWarnings("deprecation")
		float screenHeight = display.getHeight();

		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		double screenInches = Math.sqrt(x + y);
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}
}
