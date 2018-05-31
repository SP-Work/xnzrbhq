package com.otitan.xnbhq.util;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * BroadcastReceiver 网络监听
 * */
public class ConnectionChangeReceiver extends BroadcastReceiver {
	public static ArrayList<netEventHandler> mListeners = new ArrayList<>();
	public static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	@Override
	public void onReceive(Context context, Intent intent) {


		if (intent.getAction().equals(NET_CHANGE_ACTION)) {
			//Application.mNetWorkState = NetUtil.getNetworkState(context);
			if (mListeners.size() > 0)// 通知接口完成加载
				for (netEventHandler handler : mListeners) {
					handler.onNetChange();
				}
		}
	}

	/**抽象接口可被实现*/
	public static abstract interface netEventHandler {
		public abstract boolean onNetChange();
	}
}
