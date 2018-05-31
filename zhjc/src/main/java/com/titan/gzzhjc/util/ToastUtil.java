package com.titan.gzzhjc.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.titan.gzzhjc.R;

public class ToastUtil {

	@SuppressLint("ShowToast")
	public static Toast makeText(Context context, CharSequence text,int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context, R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		textView.setTextSize(18);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}

	@SuppressLint("ShowToast")
	public static Toast makeTextLeft(Context context, CharSequence text,int duration){
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context, R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.LEFT, 100, 120);
		return result;
	}

	/**
	 * 提示
	 * @param context
	 * @param text
	 */
	public static void setToast(final Context context, final String text) {
		ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 左侧提示
	 * @param context
	 * @param text
	 */
	public static void setToatLeft(final Context context, final String text){
		ToastUtil.makeTextLeft(context, text,Toast.LENGTH_SHORT).show();
	}
}
