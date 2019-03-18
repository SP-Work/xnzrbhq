package com.otitan.xnbhq;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDex;

import com.otitan.xnbhq.entity.MyObjectBox;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ConnectionChangeReceiver;
import com.otitan.xnbhq.util.NetUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.SytemUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.ZIPUtil;
import com.titan.baselibrary.crash.CrashHandler;
import com.titan.baselibrary.util.MobileInfoUtil;
import com.titan.baselibrary.util.ScreenTool;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * 自定义Application
 */
public class MyApplication extends Application {

	public static MyApplication mApplication;
	public static ResourcesManager resourcesManager;
	public static SharedPreferences sharedPreferences;
	public static ScreenTool.Screen screen;
	/** 移动设备唯一号 */
	public static String macAddress;
	/** 移动设备序列号 */
	public static String mobileXlh;
	/** 移动设备型号 */
	public static String mobileType;
	/** 注册网络 */
	private ConnectionChangeReceiver mNetworkStateReceiver;

	private static MyApplication instance = null;

	public static BoxStore boxStore = null;

	public static MyApplication getInstance(){
		return instance;
	}

	@SuppressLint("HandlerLeak")
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				ToastUtil.setToast(instance,"网络未连接");
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		/** Bugly SDK初始化
		 * 参数1：上下文对象
		 * 参数2：APPID，平台注册时得到,注意替换成你的appId
		 * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
		 * 发布新版本时需要修改以及bugly isbug需要改成false等部分
		 * 腾讯bugly 在android 4.4版本上有bug 启动报错
		 */
		//CrashReport.initCrashReport(this, "e76ea6310a", true);
		instance = this;
		/** 注册网络监听 */
		//initNetworkReceiver();

		mApplication = this;
		sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		try {
			resourcesManager = ResourcesManager.getInstance(this);
			/* 创建系统存放数据的默认文件夹 */
			//resourcesManager.createFolder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* 获取屏幕分辨率 */
		screen = ScreenTool.getScreenPix(this);
		/* 打开GPS */
		SytemUtil.openGps(this);
		/** 获取设备信息 */
		new MyAsyncTask().execute("getMbInfo");
		/* 自动注册设备信息到数据库 */
		if (netWorkTip()) {
			new MyAsyncTask().execute("addMacAddress");
		}

		new MyAsyncTask().execute("copyDatabase");

		/*异常本地记录*/
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);

		ZXingLibrary.initDisplayOpinion(this);

		//ObjectBox初始化
		boxStore = MyObjectBox.builder().androidContext(this).build();
		if (BuildConfig.DEBUG) {
			//打开调试信息
			new AndroidObjectBrowser(boxStore).start(this);
		}
	}

	/**
	 *使用了项目分包 需要重写attachBaseContext
	 */
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	/**
	 * 获取设备信息
	 */
	@SuppressLint("HardwareIds")
	public void getMbInfo() {
		/* 获取mac地址 作为设备唯一号 */
		String mac = MobileInfoUtil.getMAC(this);
		if (mac != null && !mac.equals("")) {
			macAddress = mac;
			sharedPreferences.edit().putString("mac", macAddress).apply();
		}
		mobileXlh = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
		mobileType = android.os.Build.MANUFACTURER + "——"
				+ android.os.Build.MODEL;// SM-P601 型号
		// android.os.Build.MANUFACTURER;// samsung 厂商
	}

	/**
	 * 添加设备信息到后台数据库
	 */
	public void addMacAddress() {
		Webservice webservice = new Webservice(this);
		String result = webservice.addMacAddress(macAddress, mobileXlh,mobileType);

		if (result.equals(Webservice.netException)) {
			// 网络异常
			sharedPreferences.edit().putBoolean(macAddress, false).apply();
		} else if (result.equals("已录入")) {
			// 设备信息已经录入
			sharedPreferences.edit().putBoolean(macAddress, true).apply();
		} else if (result.equals("录入成功")) {
			// 设备信息录入成功
			sharedPreferences.edit().putBoolean(macAddress, true).apply();
		} else if (result.equals("录入失败")) {
			// 设备信息录入失败
			sharedPreferences.edit().putBoolean(macAddress, false).apply();
		}

	}

	/**
	 * 异步类
	 */
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(final String... params) {
			if (params[0].equals("addMacAddress")) {
				addMacAddress();
			} else if (params[0].equals("getMbInfo")) {
				getMbInfo();
			} else if (params[0].equals("copyDatabase")) {
				String path;
				try {
					path = MyApplication.resourcesManager.getTootPath();
					copyDatabase(path, "maps.zip", "maps.zip");
				} catch (jsqlite.Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	/** 注册网络监听 实时监控网络状态 */
//	public void initNetworkReceiver() {
//		ConnectionChangeReceiver.mListeners.add(instance);
//		mNetworkStateReceiver = new ConnectionChangeReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		registerReceiver(mNetworkStateReceiver, filter);
//	}

	/**检查网络连接是否正常*/
    public boolean netWorkTip(){
        if (NetUtil.getNetworkState(instance) == NetUtil.NETWORN_NONE) {
            //Toast.makeText(instance,"网络未连接",Toast.LENGTH_SHORT).show();
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
            return false;
        } else {
            return true;
        }
    }
    /**检查网络连接是否正常*/
    public boolean hasNetWork(){
        if (NetUtil.getNetworkState(instance) == NetUtil.NETWORN_NONE) {
            return false;
        } else {
            return true;
        }
    }

	/** 复制默认数据到本地 */
	private void copyDatabase(String fileDir,String assetPath, String filename) {
		File file = new File(fileDir + "/" + filename);
		if (file.exists() && file.isFile()) {
			UnZipTFolder(fileDir, filename);
			return;
		}
		try {
			InputStream db = getResources().getAssets().open(assetPath);
			if(db == null){
				return;
			}
			FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
			byte[] buffer = new byte[1024];//8129
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

		UnZipTFolder(fileDir, filename);
	}
	/**解压文件并删除zip文件*/
	public void UnZipTFolder(String fileDir,String filename){
		try {
			//ZIPUtil.UnZipFolder(fileDir+"/"+filename, fileDir+"/");
			ZIPUtil.unzip(fileDir+"/"+filename, fileDir+"/");
		} catch (Exception e) {//java.io.FileNotFoundException: /storage/emulated/0/maps.zip: open failed: ENOENT (No such file or directory)
			e.printStackTrace();
		}
		//删除本地的zip压缩文件
		new File(fileDir+"/"+filename).delete();
	}

}
