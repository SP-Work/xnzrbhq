package com.otitan.xnbhq.util;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.net.Uri;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.service.PullParseXml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 文件加密工具类
 */
public class SytemUtil {

	/** 获取attribute_yzl.xml 中配置信息*/
	public static List<Row> getAttributeList(Context ctx, String attributeName,String xmlname) {
		AssetManager asset = ctx.getAssets();
		List<Row> list = null;
		try {
			InputStream input = asset.open(xmlname);
			PullParseXml parseXml = new PullParseXml();
			list = parseXml.PullParseXML(input, attributeName);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 检测数据是是否加密 */
	public static boolean checkGeodatabase(String path) {
		byte[] buffer = File2byte1(path);
		char[] chr = getChars(buffer);
		String bufferHead = "";
		for (int i = 0; i < 6; i++) {
			bufferHead += chr[i];
		}
		if (bufferHead.equals("otitan"))
			return true;
		return false;
	}

	private static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}

	/**
	 * 文件解密
	 */
	public static void decript(String path) {
		try {
			byte[] buffer = File2byte(path);
			byte[] bufferNew = new byte[buffer.length - 6];
			//再定义一个与其等长度的byte[ ]
			int m = buffer.length - 1;
			for (int i = 0; i < bufferNew.length; i++) ///通过循环对每个流数据进行了一次改写
			{
				bufferNew[i] = buffer[m--];     ///通过顺序的调换实现了文件的加密
			}
			//ArrayUtils.reverse(bufferNew);
			FileOutputStream outputStream = new FileOutputStream(new File(path));
			outputStream.write(bufferNew, 0, bufferNew.length); // /最后将调换后的文件流重新写回；
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件加密
	 */
	public static void jiamicript(String path) {
		try {
			byte[] buffer = File2byte(path);
			byte[] bufferNew = new byte[buffer.length + 6];
			// 再定义一个与其等长度+6的byte[ ]，文件流前加otitan
			bufferNew[0] = 'o';
			bufferNew[1] = 't';
			bufferNew[2] = 'i';
			bufferNew[3] = 't';
			bufferNew[4] = 'a';
			bufferNew[5] = 'n';
			int m = buffer.length - 1;
			for (int i = 6; i < bufferNew.length; i++) // /通过循环对每个流数据进行了一次改写
			{
				bufferNew[i] = buffer[m--]; // /通过顺序的调换实现了文件的加密
			}
			//ArrayUtils.reverse(bufferNew);
			FileOutputStream outputStream = new FileOutputStream(new File(path));
			outputStream.write(bufferNew, 0, bufferNew.length); // /最后将调换后的文件流重新写回；
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件转为byte[]数组
	 */
	private static byte[] File2byte1(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[6];
			bos.write(b,0,fis.read(b));
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

	/**
	 * 文件转为byte[]数组
	 */
	private static byte[] File2byte(String filePath) {
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
			} catch (CanceledException e) {
				e.printStackTrace();
			}
		}
	}

	/** 获取FeatureLayer图层样式 */
	public static void getEditSymbo(BaseActivity activity ,FeatureLayer flayer) {
		String typeIdField = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getTypeIdField();
		if (typeIdField.equals("")) {
			List<FeatureTemplate> featureTemp = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getFeatureTemplates();

			for (FeatureTemplate featureTemplate : featureTemp) {
				GeodatabaseFeature g;
				try {
					g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
					activity.layerRenderer = flayer.getRenderer();
					BaseActivity.layerSymbol = activity.layerRenderer.getSymbol(g);
					BaseActivity.layerTemplate = featureTemplate;
					activity.layerFeatureAts = g.getAttributes();
				} catch (TableException e) {
					e.printStackTrace();
				}
			}
		} else {
			List<FeatureType> featureTypes = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				FeatureTemplate[] templates = featureType.getTemplates();
				for (FeatureTemplate featureTemplate : templates) {
					try {
						GeodatabaseFeature g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate,null);
						activity.layerRenderer = flayer.getRenderer();
						BaseActivity.layerSymbol = activity.layerRenderer.getSymbol(g);
						BaseActivity.layerTemplate = featureTemplate;
						activity.layerFeatureAts = g.getAttributes();
					} catch (TableException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}
}
