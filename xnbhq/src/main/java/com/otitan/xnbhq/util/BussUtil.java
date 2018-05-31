package com.otitan.xnbhq.util;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.esri.android.map.CalloutPopupWindow;
import com.google.gson.Gson;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Bsuserbase;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.service.PullParseXml;
import com.titan.baselibrary.util.ScreenTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务帮助类
 * */
public class BussUtil {

	/** 获取工程中xml配置文件 */
	public static List<Row> getConfigXml(Context ctx, String projectname,String name) {
		List<Row> list = new ArrayList<Row>();
		try {
			String path = MyApplication.resourcesManager.getFolderPath(ResourcesManager.otms + "/" + projectname);
			File file = new File(path, "config.xml");
			InputStream input = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();

			list = parseXml.PullParseXML(input, name);
			if(list == null){
				list = new ArrayList<Row>();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/** 获取otms文件夹下config.xml图层数据配置信息 */
	public static List<Row> getConfigXml(Context ctx,String name) {
		List<Row> list = null;
		try {
			String path = MyApplication.resourcesManager.getFolderPath(ResourcesManager.otms + "/" );
			File file = new File(path, "config.xml");
			InputStream input = new FileInputStream(file);
			PullParseXml parseXml = new PullParseXml();

			list = parseXml.PullParseXML(input, name);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 检测字符串中是否有特殊符号 */
	public static boolean stringCheck(String str) {
		String regEx = "[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*";
		if (str.replaceAll(regEx, "").length() == 0) {
			// 沒有特殊符号
			return true;
		} else {
			// 用户名中存在特殊符号
			return false;
		}
	}

	/** 检测手机号是否符合规范 */
	public static boolean checkTelNumber(String number) {
		// String regExp = "^[1]([3-9][0-9]{1}|59|58|88|89)[0-9]{8}$";
		String regExp = "^[1]([3-9][0-9])[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
		boolean flag = m.find();
		return flag;
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

	/** 关闭calloutpopuwind */
	public static void closeCalloutPopu(final View view,
			final CalloutPopupWindow popupWindow) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ImageView imageView = (ImageView) view
						.findViewById(R.id.calloutpopuwindow_close);
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						popupWindow.hide();
					}
				});
			}
		}).start();
	}

	/** 解析登录成功后返回的用户信息 */
	public static Bsuserbase getUserInfo(String str, Bsuserbase bsuserbase) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject result = jsonObject.optJSONArray("ds").optJSONObject(0);
			bsuserbase = new Bsuserbase();
			Gson gson = new Gson();
			bsuserbase = gson.fromJson(result.toString(),Bsuserbase.class);

//			if (isEmperty(result.getString("ID"))) {
//				bsuserbase.setID(Integer.parseInt(result.getString("ID")));
//			}
//			if (isEmperty(result.getString("PASSWORD"))) {
//				bsuserbase.setPASSWORD(result.getString("PASSWORD"));
//			}
//			if (isEmperty(result.getString("BZ"))) {
//				bsuserbase.setBZ(result.getString("BZ"));
//			}
//			if (isEmperty(result.getString("DATASHARE"))) {
//				bsuserbase.setDATASHARE(result.getString("DATASHARE"));
//			}
//			if (isEmperty(result.getString("ISACTIVE"))) {
//				bsuserbase.setISACTIVE(Integer.parseInt(result
//						.getString("ISACTIVE")));
//			}
//			if (isEmperty(result.getString("ISZJ"))) {
//				bsuserbase.setISZJ(Integer.parseInt(result.getString("ISZJ")));
//			}
//			if (isEmperty(result.getString("LASTLOGIN"))) {
//				bsuserbase.setLASTLOGIN(result.getString("LASTLOGIN"));
//			}
//			if (isEmperty(result.getString("LOGINTIMES"))) {
//				bsuserbase.setLOGINTIMES(Integer.parseInt(result
//						.getString("LOGINTIMES")));
//			}
//			if (isEmperty(result.getString("DATASHARE"))) {
//				bsuserbase.setDATASHARE(result.getString("DATASHARE"));
//			}
//			if (isEmperty(result.getString("UNITNAME"))) {
//				bsuserbase.setUNITNAME(result.getString("UNITNAME"));
//			}
//			if (isEmperty(result.getString("UNITID"))) {
//				bsuserbase.setUNITID(Integer.parseInt(result
//						.getString("UNITID")));
//			}
//			if (isEmperty(result.getString("PX"))) {
//				bsuserbase.setPX(Integer.parseInt(result.getString("PX")));
//			}
//			if (isEmperty(result.getString("SKINNAME"))) {
//				bsuserbase.setSKINNAME(result.getString("SKINNAME"));
//			}
//			if (isEmperty(result.getString("USER_IMAGE"))) {
//				bsuserbase.setUSER_IMAGE(result.getString("USER_IMAGE"));
//			}
//			if (isEmperty(result.getString("SYSTEMTYPE"))) {
//				bsuserbase.setSYSTEMTYPE(Integer.parseInt(result
//						.getString("SYSTEMTYPE")));
//			}
//			if (isEmperty(result.getString("SYSTEMIDS"))) {
//				bsuserbase.setSYSTEMIDS(result.getString("SYSTEMIDS"));
//			}
//			if (isEmperty(result.getString("USER_JD"))) {
//				bsuserbase.setUSER_JD(result.getString("USER_JD"));
//			}
//			if (isEmperty(result.getString("USER_CITY"))) {
//				bsuserbase.setUSER_CITY(result.getString("USER_CITY"));
//			}
//			if (isEmperty(result.getString("USERGRPJ"))) {
//				bsuserbase.setUSERGRPJ(result.getString("USERGRPJ"));
//			}
//			if (isEmperty(result.getString("USERZZMM"))) {
//				bsuserbase.setUSERZZMM(result.getString("USERZZMM"));
//			}
//			if (isEmperty(result.getString("USERHY"))) {
//				bsuserbase.setUSERHY(result.getString("USERHY"));
//			}
//			if (isEmperty(result.getString("USERMZ"))) {
//				bsuserbase.setUSERMZ(result.getString("USERMZ"));
//			}
//			if (isEmperty(result.getString("USER_S"))) {
//				bsuserbase.setUSER_S(result.getString("USER_S"));
//			}
//			if (isEmperty(result.getString("USERBIRTH"))) {
//				bsuserbase.setUSERBIRTH(result.getString("USERBIRTH"));
//			}
//			if (isEmperty(result.getString("USEROLD"))) {
//				bsuserbase.setUSEROLD(Integer.parseInt(result
//						.getString("USEROLD")));
//			}
//			if (isEmperty(result.getString("USERSEX"))) {
//				bsuserbase.setUSERSEX(Integer.parseInt(result
//						.getString("USERSEX")));
//			}
//			if (isEmperty(result.getString("USEREMAIL"))) {
//				bsuserbase.setUSEREMAIL(result.getString("USEREMAIL"));
//			}
//			if (isEmperty(result.getString("MOBILEPHONENO"))) {
//				bsuserbase.setMOBILEPHONENO(result.getString("MOBILEPHONENO"));
//			}
//			if (isEmperty(result.getString("TELNO"))) {
//				bsuserbase.setTELNO(result.getString("TELNO"));
//			}
//			if (isEmperty(result.getString("REALNAME"))) {
//				bsuserbase.setREALNAME(result.getString("REALNAME"));
//			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bsuserbase;
	}

	/** 解析webservice数据所属数据 数据 */
	public static List<HashMap<String, String>> getSjssData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/*填充map*/
	private static void fillMap(HashMap<String, String> map,JSONObject obj){
		try {
			Iterator<String> keys = obj.keys();//定义迭代器
			while(keys.hasNext()){
				String key = keys.next();
				if (isEmperty(obj.getString(key))) {
					map.put(key, obj.getString(key));
				} else {
					map.put(key, "");
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 解析webservice数据 基地性质 数据 */
	public static List<HashMap<String, String>> getJdxzData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				// 是否系统级别字典，系统级别字典 手动加入数据库或导入 不能修改，删除。 系统：0 非系统 ：1
				fillMap(map,obj);
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性日常巡检数据 */
	public static List<HashMap<String, String>> getSwdyxRcxjJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
//				if (isEmperty(obj.getString("ID"))) {
//					map.put("ID", obj.getString("ID"));
//					map.put(obj.getString("ID"), "false");
//				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性动物扰民数据 */
	public static List<HashMap<String, String>> getSwdyxDwrmJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				// 扰民事件名称
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 救护站台账管理 数据 */
	public static List<HashMap<String, String>> getSwdyxJhzJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 救护站 数据 */
	public static List<HashMap<String, String>> getSwdyxJhzsqbJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 驯养繁殖基地 数据 */
	public static List<HashMap<String, String>> getSwdyxXyfzjdJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				if (obj.getString("PROPERTY").equals("1")) {
					map.put("PROPERTY", "企业单位");
				} else if (obj.getString("PROPERTY").equals("2")) {
					map.put("PROPERTY", "个体经营");
				} else if (obj.getString("PROPERTY").equals("3")) {
					map.put("PROPERTY", "事业单位");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 经营动物单位 数据 */
	public static List<HashMap<String, String>> getSwdyxJydwdwJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 标本馆 数据 */
	public static List<HashMap<String, String>> getSwdyxBbgJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 餐馆 数据 */
	public static List<HashMap<String, String>> getSwdyxCgJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 疫源疫病监测站 数据 */
	public static List<HashMap<String, String>> getSwdyxYyybjczJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的生物多样性 交易市场 数据 */
	public static List<HashMap<String, String>> getSwdyxJyscJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// OBJECTID
				if (isEmperty(obj.getString("OBJECTID"))) {
					map.put("OBJECTID", obj.getString("OBJECTID"));
					map.put(obj.getString("OBJECTID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的 有害生物踏查点 数据 */
	public static List<HashMap<String, String>> getYhswTcdJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/** 解析webservice返回的 有害生物踏查路线 数据 */
	public static List<HashMap<String, String>> getYhswTclxJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地虫害调查 数据 */
	public static List<HashMap<String, String>> getYhswYdchdcJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地虫害详查 数据 */
	public static List<HashMap<String, String>> getYhswYdchxcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地病害调查 数据 */
	public static List<HashMap<String, String>> getYhswYdbhdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物样地病害详查 数据 */
	public static List<HashMap<String, String>> getYhswYdbhxcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}

				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物有害植物调查 数据 */
	public static List<HashMap<String, String>> getYhswYhzwdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}

				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物木材病虫害调查 数据 */
	public static List<HashMap<String, String>> getYhswMcbchdcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}

				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 有害生物诱虫灯调查 数据 */
	public static List<HashMap<String, String>> getYhswYcddcJsonData(String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}

				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 解析webservice返回的 松材线虫病普查 数据 */
	public static List<HashMap<String, String>> getYhswScxcbpcJsonData(
			String json) {
		List<HashMap<String, String>> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("ds");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				fillMap(map,obj);
				// 序号
				if (isEmperty(obj.getString("ID"))) {
					map.put("ID", obj.getString("ID"));
					map.put(obj.getString("ID"), "false");
				}

				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	/* 判断string字符是否为null 或者 "" */
	public static boolean isEmperty(String str) {
		if (str.equals("") || str == null) {
			return false;
		} else {
			return true;
		}
	}

	/* 判断Object是否为null 或者 "" */
	public static boolean objEmperty(Object str) {
		if (str == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 计算两点之间的距离
	 */
	public static Double distance(double lat1, double lng1, double lat2,double lng2) {

		Double R = 6370996.81;
		Double x = (lng2 - lng1) * Math.PI * R
				* Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
		Double y = (lat2 - lat1) * Math.PI * R / 180;
		Double distance = Math.hypot(x, y);
		return distance;
	}

	/** dialog 宽度和高度设置 */
	public static void setDialogGravity_Center(Context context, Dialog dialog,double pwidth, double mwidth) {
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		ScreenTool.Screen screen = MyApplication.screen;
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
		}
		params.height = screen.getHeightPixels()/2;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
		dialog.show();
	}

	/** dialog 宽度和高度设置 */
	public static WindowManager.LayoutParams setDialogParams(Context context, Dialog dialog,double pwidth, double mwidth) {
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		ScreenTool.Screen screen = MyApplication.screen;
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
		}
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.TOP;
		params.y = 150;
		dialog.getWindow().setAttributes(params);
		return params;
	}
	
	/** dialog 宽度和高度设置 高度为屏幕的1/2 */
	public static void setDialogParam(Context context, Dialog dialog,
			double pwidth, double mwidth,double phight,double mhight) {
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		ScreenTool.Screen screen = MyApplication.screen;
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
			params.height = (int) (screen.getHeightPixels()*phight);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
			params.height = (int) (screen.getHeightPixels()*mhight);
		}
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}
	
	/** dialog 全屏宽度和高度设置 */
	public static void setDialogParamsFull(Context context, Dialog dialog) {
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(params);
	}

	/** 监测GPS是否打开 */
	public static boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}
}
