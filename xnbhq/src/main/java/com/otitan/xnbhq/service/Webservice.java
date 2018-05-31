package com.otitan.xnbhq.service;

import android.content.Context;
import android.os.StrictMode;

import com.otitan.xnbhq.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 设置Webservice
 */
public class Webservice {
	private String methodName = null;
	private String soapAction = null;
	private String nameSpace = "http://tempuri.org/";
	private String urlWebService;
	private static int timeout = 10000;
	public static String netException = "网络异常";
	public static String fwqException = "服务器不给力";

	// private DateFormat dateFormat = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Webservice(Context context) {
		urlWebService = context.getResources().getString(R.string.webservice);
		initWebserviceTry();
	}

	/**
	 * 添加设备信息
	 *
	 * @param macAddress  设备识别号
	 * @param xlh 设备序列号
	 * @param type 设备型号
	 * @return 返回结果 1、 已录入 2、录入成功 3、录入失败
	 */
	public String addMacAddress(String macAddress, String xlh, String type) {

		methodName = "addMacAddress";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sbh", macAddress);
		soapObject.addProperty("xlh", xlh);
		//soapObject.addProperty("type", type);
		return getResult(soapObject, "addMacAddressResult");
	}

	/** 检测登录 */
	public String CheckLogin(String name, String password) {

		methodName = "CheckLogin";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("username", name);
		soapObject.addProperty("password", password);
		return getResult(soapObject, "CheckLoginResult");
	}

	/** 获取生物多样性日常管理数据 */
	public String getSwdyxRcglData() {

		methodName = "getSwdyxRcglData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxRcglDataResult");
	}

	/**
	 * 获取数据所属数据
	 * @param "common 为common时为数据所属
	 */
	public String getSjssData(String datacode) {
		methodName = "getSjssData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("datacode", datacode);
		return getResult(soapObject, "getSjssDataResult");
	}

	/**
	 * 获取行政区域数据
	 *
	 * @param num1
	 *            为1时为行政区域
	 * @return
	 */
	public String getXzqyData(String num1) {
		methodName = "getXzqyData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("num1", num1);
		return getResult(soapObject, "getXzqyDataResult");
	}

	/**
	 * 获取驯养繁殖基地基地性质数据
	 *
	 * @return
	 */
	public String getJdxzData() {
		methodName = "getJdxzData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getJdxzDataResult");
	}

	/** 添加日常巡查管理数据*/
	public String addSwdyxRcglData(String xcry, String xcbm, String xcdd,
								   String xctime, String cdrc, String cdcls, String bxcdw,
								   String bxcr, String lon, String lat, String xcjg, String sfywfxw,
								   String bz, String sjss) {
		methodName = "addSwdyxRcglData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("xcry", xcry);
		soapObject.addProperty("xcbm", xcbm);
		soapObject.addProperty("xcdd", xcdd);
		soapObject.addProperty("xctime", xctime);
		soapObject.addProperty("cdrc", cdrc);
		soapObject.addProperty("cdcls", cdcls);
		soapObject.addProperty("bxcdw", bxcdw);
		soapObject.addProperty("bxcr", bxcr);
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		soapObject.addProperty("xcjg", xcjg);
		soapObject.addProperty("sfywfxw", sfywfxw);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sjss", sjss);
		return getResult(soapObject, "addSwdyxRcglDataResult");
	}

	/**搜索日常巡查管理数据*/
	public String searchSwdyxRcglData(String xcbm, String xcdd, String bxcdw,
									  String sjss, String kssj, String jssj, String sfywfxw) {
		methodName = "searchSwdyxRcglData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("xcbm", xcbm);
		soapObject.addProperty("xcdd", xcdd);
		soapObject.addProperty("bxcdw", bxcdw);
		soapObject.addProperty("sjss", sjss);
		soapObject.addProperty("kasj", kssj);
		soapObject.addProperty("jssj", jssj);
		soapObject.addProperty("sfywfxw", sfywfxw);
		return getResult(soapObject, "searchSwdyxRcglDataResult");
	}

	/**更新日常巡查管理数据*/
	public String updateSwdyxRcglData(String id, String xcry, String xcbm,
									  String xcdd, String xctime, String cdrc, String cdcls,
									  String bxcdw, String bxcr, String lon, String lat, String xcjg,
									  String sfywfxw, String bz) {
		methodName = "updateSwdyxRcglData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("xcry", xcry);
		soapObject.addProperty("xcbm", xcbm);
		soapObject.addProperty("xcdd", xcdd);
		soapObject.addProperty("xctime", xctime);
		soapObject.addProperty("cdrc", cdrc);
		soapObject.addProperty("cdcls", cdcls);
		soapObject.addProperty("bxcdw", bxcdw);
		soapObject.addProperty("bxcr", bxcr);
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		soapObject.addProperty("xcjg", xcjg);
		soapObject.addProperty("sfywfxw", sfywfxw);
		soapObject.addProperty("bz", bz);
		return getResult(soapObject, "updateSwdyxRcglDataResult");
	}

	/**删除日常巡查管理数据*/
	public String deleteSwdyxRcglData(String id) {
		methodName = "deleteSwdyxRcglData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxRcglDataResult");
	}

	/**添加小地名*/
	public String addXdmData(String name, String type, String lon, String lat) {
		methodName = "addXdmData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("name", name);
		soapObject.addProperty("type", type);
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		return getResult(soapObject, "addXdmDataResult");
	}

	/**查询是否录入了设备使用者信息 返回 true 或 false */
	public String selMobileSysInfo(String sbh, String xlh, String type) {
		methodName = "selSBUserInfo";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sbh", sbh);
		soapObject.addProperty("xlh", xlh);
		soapObject.addProperty("sbmc", type);
		return getResult(soapObject, "selSBUserInfoResult");
	}

	/**
	 * 录入设备使用者信息
	 *
	 * @param sysname
	 *            使用者名称
	 * @param tel
	 *            使用者电话
	 * @param dw
	 *            单位
	 * @param registertime
	 *            登记时间
	 * @param sbmc
	 *            设备名称
	 * @param sbh
	 *            设备号
	 * @param bz
	 *            备注
	 * @return
	 */
	public String addMobileSysInfo(String sysname, String tel, String dw,
								   String registertime, String sbmc, String sbh, String bz) {
		methodName = "addMoblieSysInfo";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sysname", sysname);
		soapObject.addProperty("tel", tel);
		soapObject.addProperty("dw", dw);
		soapObject.addProperty("retime", registertime);
		soapObject.addProperty("sbmc", sbmc);
		soapObject.addProperty("sbh", sbh);
		soapObject.addProperty("bz", bz);

		return getResult(soapObject, "addMoblieSysInfoResult");
	}

	/**
	 * 获取设备使用者信息
	 *
	 * @param sbh
	 *            设备唯一号
	 * @return
	 */
	public String getMobileSysInfo(String sbh) {//f6:d0:10:1b:a8:7c
		methodName = "selMobileInfo";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sbh", sbh);
		return getResult(soapObject, "selMobileInfoResult");
	}

	/**更新设备使用者信息*/
	public String updateMobileSysInfo(String name, String tel,String dw, String sbmc,String sbh) {

		methodName = "updateMobileInfo";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("username", name);
		soapObject.addProperty("tel", tel);
		soapObject.addProperty("dz", dw);
		soapObject.addProperty("sbmc", sbmc);
		soapObject.addProperty("sbh", sbh);

		return getResult(soapObject, "updateMobileInfoResult");
	}

	/**获取动物扰民事件管理数据*/
	public String getSwdyxDwrmglData() {

		methodName = "getSwdyxDwrmglData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxDwrmglDataResult");
	}

	/**添加动物扰民事件*/
	public String addSwdyxDwrmgl(String name, String address, String rmdw,
								 String brr, String fstime, String sfclw, String zbr, String sjms,
								 String sjcljg, String bz, String sjss) {
		methodName = "addSwdyxDwrmgl";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("name", name);
		soapObject.addProperty("address", address);
		soapObject.addProperty("rmdw", rmdw);
		soapObject.addProperty("brr", brr);
		soapObject.addProperty("fstime", fstime);
		soapObject.addProperty("sfclw", sfclw);
		soapObject.addProperty("zbr", zbr);
		soapObject.addProperty("sjms", sjms);
		soapObject.addProperty("sjcljg", sjcljg);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sjss", sjss);
		return getResult(soapObject, "addSwdyxDwrmglResult");
	}

	/**更新扰民事件*/
	public String updateSwdyxDwrmgl(String id, String name, String address,
									String rmdw, String brr, String fstime, String sfclw, String zbr,
									String sjms, String sjcljg, String bz) {
		methodName = "updateSwdyxDwrmgl";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("name", name);
		soapObject.addProperty("address", address);
		soapObject.addProperty("rmdw", rmdw);
		soapObject.addProperty("brr", brr);
		soapObject.addProperty("fstime", fstime);
		soapObject.addProperty("sfclw", sfclw);
		soapObject.addProperty("zbr", zbr);
		soapObject.addProperty("sjms", sjms);
		soapObject.addProperty("sjcljg", sjcljg);
		soapObject.addProperty("bz", bz);
		return getResult(soapObject, "updateSwdyxDwrmglResult");
	}

	/**删除扰民事件 多个id以逗号隔开*/
	public String deleteSwdyxDwrmgl(String id) {
		methodName = "deleteSwdyxDwrmgl";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxDwrmglResult");
	}

	/**搜索动物扰民事件*/
	public String searchSwdyxDwrmData(String sjmc, String rmdw, String sfclwb,
									  String kssj, String jssj, String sjss) {
		methodName = "searchSwdyxDwrmData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sjmc", sjmc);
		soapObject.addProperty("rmdw", rmdw);
		soapObject.addProperty("sfclwb", sfclwb);
		soapObject.addProperty("kssj", kssj);
		soapObject.addProperty("jssj", jssj);
		soapObject.addProperty("sjss", sjss);
		return getResult(soapObject, "searchSwdyxDwrmDataResult");
	}

	/**获取生物多样性救护站台账管理数据*/
	public String getSwdyxJhzData() {

		methodName = "getSwdyxJhzData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxJhzDataResult");
	}

	/**更新救护站台账管理数据*/
	public String updateSwdyxJhzData(String id, String jhzm, String glry,
									 String qywz, String xxdz, String wd, String jd, String lxdh,
									 String jztj, String bz) {
		methodName = "updateSwdyxJhzData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("jhzm", jhzm);
		soapObject.addProperty("glry", glry);
		soapObject.addProperty("qywz", qywz);
		soapObject.addProperty("xxdz", xxdz);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("lxdh", lxdh);
		soapObject.addProperty("jztj", jztj);
		soapObject.addProperty("bz", bz);
		return getResult(soapObject, "updateSwdyxJhzDataResult");
	}

	/**添加救护站台账管理数据*/
	public String addSwdyxJhzData(String jhzm, String glry, String qywz,
								  String xxdz, String wd, String jd, String lxdh, String jztj,
								  String bz) {
		methodName = "addSwdyxJhzData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jhzm", jhzm);
		soapObject.addProperty("glry", glry);
		soapObject.addProperty("qywz", qywz);
		soapObject.addProperty("xxdz", xxdz);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("lxdh", lxdh);
		soapObject.addProperty("jztj", jztj);
		soapObject.addProperty("bz", bz);
		return getResult(soapObject, "addSwdyxJhzDataResult");
	}

	/**搜索救护站台账管理数据*/
	public String searchSwdyxJhzData(String jzzmc, String fzr, String xzqy) {
		methodName = "searchSwdyxJhzData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jzzmc", jzzmc);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("xzqy", xzqy);
		return getResult(soapObject, "searchSwdyxJhzDataResult");
	}

	/**获取生物多样性救护站申请表数据*/
	public String getSwdyxJhzsqbData() {

		methodName = "getSwdyxJhzsqbData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxJhzsqbDataResult");
	}

	/**删除救护站数据*/
	public String deleteSwdyxJhzData(String id) {
		methodName = "deleteSwdyxJhzData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxJhzDataResult");
	}

	/**获取驯养繁殖基地数据*/
	public String getSwdyxXyfzjdData() {

		methodName = "getSwdyxXyfzjdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxXyfzjdDataResult");
	}

	/**更新驯养繁殖基地数据*/
	public String updateSwdyxXyfzjdData(String id, String jdmc, String jdxz,
										String fzr, String lxdh, String dz, String jyfw, String jyyt,
										String xwdd, String jbr, String jd, String wd, String bz) {
		methodName = "updateSwdyxXyfzjdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("jdmc", jdmc);
		soapObject.addProperty("jdxz", jdxz);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("lxdh", lxdh);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("jyfw", jyfw);
		soapObject.addProperty("jyyt", jyyt);
		soapObject.addProperty("xwdd", xwdd);
		soapObject.addProperty("jbr", jbr);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("bz", bz);
		return getResult(soapObject, "updateSwdyxXyfzjdDataResult");
	}

	/**删除驯养繁殖基地数据 多个id以逗号隔开*/
	public String deleteSwdyxXyfzjdData(String id) {
		methodName = "deleteSwdyxXyfzjdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxXyfzjdDataResult");
	}

	/**添加驯养繁殖基地数据 多个id以逗号隔开*/
	public String addSwdyxXyfzjdData(String jdmc, String jdxz, String fzr,
									 String lxdh, String dz, String jyfw, String jyyt, String xwdd,
									 String jbr, String jd, String wd, String bz, String djsj,
									 String sjss) {
		methodName = "addSwdyxXyfzjdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jdmc", jdmc);
		soapObject.addProperty("jdxz", jdxz);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("lxdh", lxdh);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("jyfw", jyfw);
		soapObject.addProperty("jyyt", jyyt);
		soapObject.addProperty("xwdd", xwdd);
		soapObject.addProperty("jbr", jbr);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("djsj", djsj);
		soapObject.addProperty("sjss", sjss);
		return getResult(soapObject, "addSwdyxXyfzjdDataResult");
	}

	/**搜索驯养繁殖基地数据*/
	public String searchSwdyxXyfzjdData(String jdmc, String jdfzr, String jdxz,String sjss, String zckssj, String zcjssj) {
		methodName = "searchSwdyxXyfzjdData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jdmc", jdmc);
		soapObject.addProperty("jdfzr", jdfzr);
		soapObject.addProperty("jdxz", jdxz);
		soapObject.addProperty("sjss", sjss);
		soapObject.addProperty("zckssj", zckssj);
		soapObject.addProperty("zcjssj", zcjssj);
		return getResult(soapObject, "searchSwdyxXyfzjdDataResult");
	}

	/**获取经营动物单位数据*/
	public String getSwdyxJydwdwData() {
		methodName = "getSwdyxJydwdwData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxJydwdwDataResult");
	}

	/**更新 经营动物单位 数据*/
	public String updateSwdyxJydwdwData(String id, String fzr, String lxfs,
										String jd, String wd, String dz) {
		methodName = "updateSwdyxJydwdwData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("lxfs", lxfs);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		return getResult(soapObject, "updateSwdyxJydwdwDataResult");
	}

	/**删除 经营动物单位 数据*/
	public String deleteSwdyxJydwdwData(String id) {
		methodName = "deleteSwdyxJydwdwData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxJydwdwDataResult");
	}

	/**添加经营动物单位数据*/
	public String addSwdyxJydwdwglData(String fzr, String lxfs, String jd,
									   String wd, String dz) {
		methodName = "addSwdyxJydwdwglData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("lxfs", lxfs);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		return getResult(soapObject, "addSwdyxJydwdwglDataResult");
	}

	/**搜索经营动物单位数据*/
	public String searchSwdyxJydwdwData(String fzr) {
		methodName = "searchSwdyxJydwdwData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("fzr", fzr);
		return getResult(soapObject, "searchSwdyxJydwdwDataResult");
	}

	/**获取标本馆数据*/
	public String getSwdyxBbgData() {
		methodName = "getSwdyxBbgData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxBbgDataResult");
	}

	/**更新生物多样性 标本馆 数据*/
	public String updateSwdyxBbgData(String id, String mc, String fzr,
									 String jd, String wd, String lxfs, String dz) {
		methodName = "updateSwdyxBbgData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("lxfs", lxfs);
		soapObject.addProperty("dz", dz);
		return getResult(soapObject, "updateSwdyxBbgDataResult");
	}

	/**删除 标本馆 数据*/
	public String deleteSwdyxBbgData(String id) {
		methodName = "deleteSwdyxBbgData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxBbgDataResult");
	}

	/**添加 标本馆 数据*/
	public String addSwdyxBbgData(String mc, String fzr, String jd, String wd,
								  String lxfs, String dz) {
		methodName = "addSwdyxBbgData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("fzr", fzr);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("lxfs", lxfs);
		soapObject.addProperty("dz", dz);
		return getResult(soapObject, "addSwdyxBbgDataResult");
	}

	/**搜索 标本馆 数据*/
	public String searchSwdyxBbgData(String mc, String fzr) {
		methodName = "searchSwdyxBbgData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("fzr", fzr);
		return getResult(soapObject, "searchSwdyxBbgDataResult");
	}

	/**获取餐馆数据*/
	public String getSwdyxCgData() {
		methodName = "getSwdyxCgData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxCgDataResult");
	}

	/**更新餐馆数据*/
	public String updateSwdyxCgData(String id, String mc, String jd, String wd,
									String dz, String pay) {
		methodName = "updateSwdyxCgData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("pay", pay);
		return getResult(soapObject, "updateSwdyxCgDataResult");
	}

	/**删除餐馆数据*/
	public String deleteSwdyxCgData(String id) {
		methodName = "deleteSwdyxCgData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxCgDataResult");
	}

	/**添加餐馆信息*/
	public String addSwdyxCgData(String mc, String jd, String wd, String dz,
								 String pay) {
		methodName = "addSwdyxCgData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("pay", pay);
		return getResult(soapObject, "addSwdyxCgDataResult");
	}

	/**搜索餐馆信息*/
	public String searchSwdyxCgData(String mc, String pay) {
		methodName = "searchSwdyxCgData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("pay", pay);
		return getResult(soapObject, "searchSwdyxCgDataResult");
	}

	/**获取 疫源疫病监测站 信息*/
	public String getSwdyxYyybjczData() {
		methodName = "getSwdyxYyybjczData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxYyybjczDataResult");
	}

	/**更新 疫源疫病监测站 信息*/
	public String updateSwdyxYyybjczData(String id, String jd, String wd,
										 String dz, String mc, String fzr) {
		methodName = "updateSwdyxYyybjczData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("fzr", fzr);
		return getResult(soapObject, "updateSwdyxYyybjczDataResult");
	}

	/**删除 疫源疫病监测站 信息*/
	public String deleteSwdyxYyybjczData(String id) {
		methodName = "deleteSwdyxYyybjczData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxYyybjczDataResult");
	}

	/**添加 疫源疫病监测站 信息*/
	public String addSwdyxYyybjczData(String jd, String wd, String dz,
									  String mc, String fzr) {
		methodName = "addSwdyxYyybjczData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("fzr", fzr);
		return getResult(soapObject, "addSwdyxYyybjczDataResult");
	}

	/**搜索 疫源疫病监测站 信息*/
	public String searchSwdyxYyybjczData(String mc, String fzr) {
		methodName = "searchSwdyxYyybjczData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mc", mc);
		soapObject.addProperty("fzr", fzr);
		return getResult(soapObject, "searchSwdyxYyybjczDataResult");
	}

	/**获取 交易市场 信息*/
	public String getSwdyxJyscData() {
		methodName = "getSwdyxJyscData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getSwdyxJyscDataResult");
	}

	/**更新 交易市场 信息*/
	public String updateSwdyxJyscData(String id, String jd, String wd,
									  String dz, String mc) {
		methodName = "updateSwdyxJyscData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("mc", mc);
		return getResult(soapObject, "updateSwdyxJyscDataResult");
	}

	/**删除 交易市场 信息*/
	public String deleteSwdyxJyscData(String id) {
		methodName = "deleteSwdyxJyscData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteSwdyxJyscDataResult");
	}

	/**添加 交易市场 信息*/
	public String addSwdyxJyscData(String jd, String wd, String dz, String mc) {
		methodName = "addSwdyxJyscData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jd", jd);
		soapObject.addProperty("wd", wd);
		soapObject.addProperty("dz", dz);
		soapObject.addProperty("mc", mc);
		return getResult(soapObject, "addSwdyxJyscDataResult");
	}

	/**搜索 交易市场 信息*/
	public String searchSwdyxJyscData(String mc) {
		methodName = "searchSwdyxJyscData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mc", mc);
		return getResult(soapObject, "searchSwdyxJyscDataResult");
	}

	/**添加有害生物 踏查点信息*/
	public String addYhswTcdData(String dcdbh, String dclxbh, String dcr,
								 String dctime, String dcdw, String lon, String lat, String hb,
								 String xdm, String xbh, String xbmj, String jzmc, String cbtj,
								 String lfzc, String whbw, String yhswmc, String mcwhcd, String ct,
								 String mcwhmj, String ly, String bz, String city, String county,
								 String town, String village, String sczt,String sbr,String sbsj) {
		methodName = "addYhswTcdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("dcdbh", dcdbh);
		soapObject.addProperty("dclxbh", dclxbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dctime", dctime);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("cbtj", cbtj);
		soapObject.addProperty("lfzc", lfzc);
		soapObject.addProperty("whbw", whbw);
		soapObject.addProperty("yhswmc", yhswmc);
		soapObject.addProperty("mcwhcd", mcwhcd);
		soapObject.addProperty("ct", ct);
		soapObject.addProperty("mcwhmj", mcwhmj);
		soapObject.addProperty("ly", ly);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswTcdDataResult");
	}

	/**添加 有害生物踏查路线*/
	public String addYhswlxtcData(String tclxbh, String startlon,
								  String startlat) {
		methodName = "addYhswlxtcData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("tclxbh", tclxbh);
		soapObject.addProperty("startlon", startlon);
		soapObject.addProperty("startlat", startlat);
		return getResult(soapObject, "addYhswlxtcDataResult");
	}

	/**更新有害生物踏查路线信息*/
	public String updateYhswlxtcData(String tclxbh, String tcr, String tcrq,
									 String tcdw, String tcdbmj, String zdjd, String zdwd,
									 String yhswmc, String jzmc, String lfzc, String city,
									 String county, String town, String village, String bz, String sczt, String sbr, String sbsj) {
		methodName = "updateYhswlxtcData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("tclxbh", tclxbh);
		soapObject.addProperty("tcr", tcr);
		soapObject.addProperty("tcrq", tcrq);
		soapObject.addProperty("tcdw", tcdw);
		soapObject.addProperty("tcdbmj", tcdbmj);
		soapObject.addProperty("zdjd", zdjd);
		soapObject.addProperty("zdwd", zdwd);
		soapObject.addProperty("yhswmc", yhswmc);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("lfzc", lfzc);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "updateYhswlxtcDataResult");
	}
	/**获取 有害生物 踏查路线 数据*/
	public String getYhswtclxData() {
		methodName = "getYhswtclxData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswtclxDataResult");
	}
	/**删除有害生物踏查路线信息*/
	public String deleteYhswTclxData(String id) {
		methodName = "deleteYhswTclxData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteYhswTclxDataResult");
	}
	/**添加有害生物 踏查路线信息 */
	public String addYhswlxtcallData(String tclxbh, String tcr, String tcrq, String tcdw,
									 String tcdbmj, String qdjd, String qdwd, String zdjd,String zdwd,String yhswmc,
									 String jzmc,String lfzc,String city,String county,String town,String village,
									 String bz,String sczt,String sbr,String sbsj){
		methodName = "addYhswlxtcallData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("tclxbh", tclxbh);
		soapObject.addProperty("tcr", tcr);
		soapObject.addProperty("tcrq", tcrq);
		soapObject.addProperty("tcdw", tcdw);
		soapObject.addProperty("tcdbmj", tcdbmj);
		soapObject.addProperty("qdjd", qdjd);
		soapObject.addProperty("qdwd", qdwd);
		soapObject.addProperty("zdjd", zdjd);
		soapObject.addProperty("zdwd", zdwd);
		soapObject.addProperty("yhswmc", yhswmc);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("lfzc", lfzc);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswlxtcallDataResult");
	}
	/**
	 * 获取 有害生物 踏查点 数据
	 *
	 * @return
	 */
	public String getYhswtcdData() {
		methodName = "getYhswtcdData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswtcdDataResult");
	}
	/**搜索有害生物 踏查点数据*/
	public String searchYhswTcdlData(String jzmc, String sbzt) {
		methodName = "searchYhswTcdlData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswTcdlDataResult");
	}
	/**搜索有害生物 踏查路线数据*/
	public String searchYhswTclxData(String jzmc, String sbzt) {
		methodName = "searchYhswTclxData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswTclxDataResult");
	}
	/**更新有害生物踏查点信息*/

	public String updateYhswTcdData(String dcdbh, String dclxbh, String dcr,
									String dctime, String dcdw, String lon, String lat, String hb,
									String xdm, String xbh, String xbmj, String jzmc, String cbtj,
									String lfzc, String whbw, String yhswmc, String mcwhcd, String ct,
									String mcwhmj, String ly, String bz, String city, String county,
									String town, String village, String sczt) {
		methodName = "updateYhswTcdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("dcdbh", dcdbh);
		soapObject.addProperty("dclxbh", dclxbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dctime", dctime);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("cbtj", cbtj);
		soapObject.addProperty("lfzc", lfzc);
		soapObject.addProperty("whbw", whbw);
		soapObject.addProperty("yhswmc", yhswmc);
		soapObject.addProperty("mcwhcd", mcwhcd);
		soapObject.addProperty("ct", ct);
		soapObject.addProperty("mcwhmj", mcwhmj);
		soapObject.addProperty("ly", ly);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("sczt", sczt);
		return getResult(soapObject, "updateYhswTcdDataResult");
	}

	/**删除有害生物踏查点信息*/
	public String deleteYhswTcdData(String id) {
		methodName = "deleteYhswTcdData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		return getResult(soapObject, "deleteYhswTcdDataResult");
	}

	/**添加有害生物 样地虫害调查信息*/
	public String addYhswYdchdcData(String ydbh, String dcr, String dcsj,
									String dcdw, String ydxz, String ydc, String ydk, String ydmj,
									String dclx, String yddbmj, String zhmj, String hb, String ycjjd,
									String ycjwd, String xbh, String xbmj, String city, String county,
									String town, String village, String xdm, String jzmc, String chmc,
									String ct, String whbw, String whcd, String ly, String cbtj,
									String cl, String czl, String ckmd, String sl, String ybd,
									String jkzs, String swzs, String ykh, String yksd, String pw,
									String px, String gslx, String mpmc, String mpzmj, String cml,
									String zymmpz, String bz, String sczt,String sbr,String sbsj) {
		methodName = "addYhswYdchdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("ydxz", ydxz);
		soapObject.addProperty("ydc", ydc);
		soapObject.addProperty("ydk", ydk);
		soapObject.addProperty("ydmj", ydmj);
		soapObject.addProperty("dclx", dclx);
		soapObject.addProperty("yddbmj", yddbmj);
		soapObject.addProperty("zhmj", zhmj);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("ycjjd", ycjjd);
		soapObject.addProperty("ycjwd", ycjwd);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("chmc", chmc);
		soapObject.addProperty("ct", ct);
		soapObject.addProperty("whbw", whbw);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("ly", ly);
		soapObject.addProperty("cbtj", cbtj);
		soapObject.addProperty("cl", cl);
		soapObject.addProperty("czl", czl);
		soapObject.addProperty("ckmd", ckmd);
		soapObject.addProperty("sl", sl);
		soapObject.addProperty("ybd", ybd);
		soapObject.addProperty("jkzs", jkzs);
		soapObject.addProperty("swzs", swzs);
		soapObject.addProperty("ykh", ykh);
		soapObject.addProperty("yksd", yksd);
		soapObject.addProperty("pw", pw);
		soapObject.addProperty("px", px);
		soapObject.addProperty("gslx", gslx);
		soapObject.addProperty("mpmc", mpmc);
		soapObject.addProperty("mpzmj", mpzmj);
		soapObject.addProperty("cml", cml);
		soapObject.addProperty("zymmpz", zymmpz);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswYdchdcDataResult");
	}
	/**搜索有害生物 样地虫害数据*/
	public String searchYhswYdchData(String hcmc, String sbzt) {
		methodName = "searchYhswYdchData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("hcmc", hcmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswYdchDataResult");
	}
	/**添加有害生物 样地虫害详查信息 */
	public String addYhswYdchxcData(String ydbh, String ysbh, String luan, String yc, String yong, String cc, String cd, String sg, String xj, String gf) {
		methodName = "addYhswYdchxcData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("ysbh", ysbh);
		soapObject.addProperty("luan", luan);
		soapObject.addProperty("yc", yc);
		soapObject.addProperty("yong", yong);
		soapObject.addProperty("cc", cc);
		soapObject.addProperty("cd", cd);
		soapObject.addProperty("sg", sg);
		soapObject.addProperty("xj", xj);
		soapObject.addProperty("gf", gf);
		return getResult(soapObject, "addYhswYdchxcDataResult");
	}
	/**获取样地虫害调查信息*/
	public String getYhswYdchdcData() {
		methodName = "getYhswYdchdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswYdchdcDataResult");
	}
	/**更新有害生物样地虫害信息*/
	public String updateYhswYdchdcData(String ydbh, String dcr, String dcsj, String dcdw, String ydxz, String ydc, String ydk, String ydmj, String dclx, String yddbmj, String zhmj, String hb, String ycjjd, String ycjwd, String xbh, String xbmj, String city, String county, String town, String village, String xdm, String jzmc, String chmc, String ct, String whbw, String whcd, String ly, String cbtj, String cl, String czl, String ckmd, String sl, String ybd, String jkzs, String swzs, String ykh, String yksd, String pw, String px, String gslx, String mpmc, String mpzmj, String cml, String zymmpz, String bz,String sczt) {
		methodName = "updateYhswYdchdcData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("ydxz", ydxz);
		soapObject.addProperty("ydc", ydc);
		soapObject.addProperty("ydk", ydk);
		soapObject.addProperty("ydmj", ydmj);
		soapObject.addProperty("dclx", dclx);
		soapObject.addProperty("yddbmj", yddbmj);
		soapObject.addProperty("zhmj", zhmj);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("ycjjd", ycjjd);
		soapObject.addProperty("ycjwd", ycjwd);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("chmc", chmc);
		soapObject.addProperty("ct", ct);
		soapObject.addProperty("whbw", whbw);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("ly", ly);
		soapObject.addProperty("cbtj", cbtj);
		soapObject.addProperty("cl", cl);
		soapObject.addProperty("czl", czl);
		soapObject.addProperty("ckmd", ckmd);
		soapObject.addProperty("sl", sl);
		soapObject.addProperty("ybd", ybd);
		soapObject.addProperty("jkzs", jkzs);
		soapObject.addProperty("swzs", swzs);
		soapObject.addProperty("ykh", ykh);
		soapObject.addProperty("yksd", yksd);
		soapObject.addProperty("pw", pw);
		soapObject.addProperty("px", px);
		soapObject.addProperty("gslx", gslx);
		soapObject.addProperty("mpmc", mpmc);
		soapObject.addProperty("mpzmj", mpzmj);
		soapObject.addProperty("cml", cml);
		soapObject.addProperty("zymmpz", zymmpz);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		return getResult(soapObject, "updateYhswTcdDataResult");
	}
	/**更新 有害生物踏查点 上报人上报时间信息*/
	public String  updateYhswtcdsbData(String dcdbh, String sbsj, String sbr) {
		methodName = "updateYhswtcdsbData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("dcdbh", dcdbh);
		soapObject.addProperty("sbsj", sbsj);
		soapObject.addProperty("sbr", sbr);
		return getResult(soapObject, "updateYhswtcdsbDataResult");
	}
	/**获取有害生物样地虫害详查信息*/
	public String getYhswYdchxcData() {
		methodName = "getYhswYdchxcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswYdchxcDataResult");
	}
	/**添加样地病害详查信息*/
	public String  addYhswYdbhxcData(String ydbh, String ysbh, String bhfj, String sg, String xj, String gf) {
		methodName = "addYhswYdbhxcData";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("ysbh", ysbh);
		soapObject.addProperty("bhfj", bhfj);
		soapObject.addProperty("sg", sg);
		soapObject.addProperty("xj", xj);
		soapObject.addProperty("gf", gf);
		return getResult(soapObject, "addYhswYdbhxcDataResult");
	}
	/**搜索有害生物 样地病害数据*/
	public String searchYhswYdbhData(String bhmc, String sbzt) {
		methodName = "searchYhswYdbhData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("bhmc", bhmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswYdbhDataResult");
	}
	/**有害生物样地病害调查信息 添加*/
	public String addYhswYdbhdcData(String ydbh, String dcr, String dcsj, String dcdw, String ydxz, String ydc, String ydk, String ydmj,
									String dclx, String yddbmj, String zhmj, String hb, String ycjjd, String ycjwd, String xbh, String xbmj, String city, String county, String town, String village, String xdm, String jzmc,
									String bhmc, String fblx, String ly, String whcd, String cbtj, String gslx, String bgl, String bgzs, String pjxj,String sl, String ybd, String pd, String dx, String ykh, String yksd,
									String pjg, String yhzwgd,  String mpmc, String mpzmj, String cml, String zymmpz, String bz, String sczt, String sbr, String sbsj) {
		methodName = "addYhswYdbhdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("ydxz", ydxz);
		soapObject.addProperty("ydc", ydc);
		soapObject.addProperty("ydk", ydk);
		soapObject.addProperty("ydmj", ydmj);
		soapObject.addProperty("dclx", dclx);
		soapObject.addProperty("yddbmj", yddbmj);
		soapObject.addProperty("zhmj", zhmj);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("ycjjd", ycjjd);
		soapObject.addProperty("ycjwd", ycjwd);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("bhmc", bhmc);
		soapObject.addProperty("fblx", fblx);
		soapObject.addProperty("ly", ly);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("cbtj", cbtj);
		soapObject.addProperty("gslx", gslx);
		soapObject.addProperty("bgl", bgl);
		soapObject.addProperty("bgzs", bgzs);
		soapObject.addProperty("pjxj", pjxj);
		soapObject.addProperty("sl", sl);
		soapObject.addProperty("ybd", ybd);
		soapObject.addProperty("pd", pd);
		soapObject.addProperty("dx", dx);
		soapObject.addProperty("ykh", ykh);
		soapObject.addProperty("yksd", yksd);
		soapObject.addProperty("pjg", pjg);
		soapObject.addProperty("yhzwgd", yhzwgd);
		soapObject.addProperty("mpmc", mpmc);
		soapObject.addProperty("mpzmj", mpzmj);
		soapObject.addProperty("cml", cml);
		soapObject.addProperty("zymmpz", zymmpz);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswYdbhdcDataResult");
	}
	/**获取样地病害调查信息*/
	public String  getYhswYdbhdcData() {
		methodName = "getYhswYdbhdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswYdbhdcDataResult");
	}
	/**获取样地病害详查信息*/
	public String  getYhswYdbhxcData() {
		methodName = "getYhswYdbhxcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswYdbhxcDataResult");
	}
	/**有害生物有害植物调查信息 添加*/
	public String addYhswYhzwdcData(String ydbh, String dcr, String dcsj, String dcdw, String ydxz, String ydc, String ydk, String ydmj,
									String yddbmj, String zhmj, String hb, String ycjjd, String ycjwd, String xbh, String xbmj, String city, String county, String town, String village,  String jzmc,
									String yhzwmc, String yhzwgd, String yhzwdj, String whzwmc, String whbw, String whcd, String ly, String cbtj, String bhzgs, String zgzs, String whl, String whzwszzk, String pjxj, String pjg, String sl,
									String ybd, String dx, String dj, String fblx, String pd, String yksd, String bz, String sczt, String sbr, String sbsj)
	{
		methodName = "addYhswYhzwdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("ydxz", ydxz);
		soapObject.addProperty("ydc", ydc);
		soapObject.addProperty("ydk", ydk);
		soapObject.addProperty("ydmj", ydmj);
		soapObject.addProperty("yddbmj", yddbmj);
		soapObject.addProperty("zhmj", zhmj);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("ycjjd", ycjjd);
		soapObject.addProperty("ycjwd", ycjwd);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("yhzwmc", yhzwmc);
		soapObject.addProperty("yhzwgd", yhzwgd);
		soapObject.addProperty("yhzwdj", yhzwdj);
		soapObject.addProperty("whzwmc", whzwmc);
		soapObject.addProperty("whbw", whbw);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("ly", ly);
		soapObject.addProperty("cbtj", cbtj);
		soapObject.addProperty("bhzgs", bhzgs);
		soapObject.addProperty("zgzs", zgzs);
		soapObject.addProperty("whl", whl);
		soapObject.addProperty("whzwszzk", whzwszzk);
		soapObject.addProperty("pjxj", pjxj);
		soapObject.addProperty("pjg", pjg);
		soapObject.addProperty("sl", sl);
		soapObject.addProperty("ybd", ybd);
		soapObject.addProperty("dx", dx);
		soapObject.addProperty("dj", dj);
		soapObject.addProperty("fblx", fblx);
		soapObject.addProperty("pd", pd);
		soapObject.addProperty("yksd", yksd);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswYhzwdcDataResult");
	}
	/**搜索有害生物 有害植物数据*/
	public String searchYhswYhzwData(String yhzwmc, String sbzt) {
		methodName = "searchYhswYhzwData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("yhzwmc", yhzwmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswYhzwDataResult");
	}
	/**获取有害植物调查信息*/
	public String  getYhswYhzwdcData() {
		methodName = "getYhswYhzwdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswYhzwdcDataResult");
	}
	/**有害生物木材病虫害调查信息 添加*/
	public String addYhswMcbchdcData(String mcbchbh, String dcr, String dcsj, String dcdw, String bdcdw, String dcdjd, String dcdwd, String zyjgcp,
									 String xdm, String xykcmcpz, String zyjgjysz, String ccmcsl, String kcl, String ccmcpz, String bcmc, String whcd, String whbw, String ct, String city, String county, String town, String village,
									 String bz, String sczt, String sbr, String sbsj)
	{
		methodName = "addYhswMcbchdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("mcbchbh", mcbchbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("bdcdw", bdcdw);
		soapObject.addProperty("dcdjd", dcdjd);
		soapObject.addProperty("dcdwd", dcdwd);
		soapObject.addProperty("zyjgcp", zyjgcp);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("xykcmcpz", xykcmcpz);
		soapObject.addProperty("zyjgjysz", zyjgjysz);
		soapObject.addProperty("ccmcsl", ccmcsl);
		soapObject.addProperty("kcl", kcl);
		soapObject.addProperty("ccmcpz", ccmcpz);
		soapObject.addProperty("bcmc", bcmc);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("whbw", whbw);
		soapObject.addProperty("ct", ct);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswMcbchdcDataResult");
	}
	/**搜索有害生物 木材病虫害数据*/
	public String searchYhswMcbchData(String whcd, String sbzt) {
		methodName = "searchYhswMcbchData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswMcbchDataResult");
	}
	/**获取木材病虫害调查信息*/
	public String  getYhswMcbchdcData() {
		methodName = "getYhswMcbchdcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswMcbchdcDataResult");
	}
	/**有害生物诱虫灯调查信息 添加*/
	public String addYhswYcddcData(String ycdbh, String ycdmc, String dcr, String dcdw, String dcsj, String dcdjd, String dcdwd, String hb,
								   String xdm, String xbh, String xbmj, String zyhcmc, String lfzc, String hcsl, String ybd, String pjg, String pjxj, String city, String county, String town, String village,
								   String bz, String sczt, String sbr, String sbsj)
	{
		methodName = "addYhswYcddcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ycdbh", ycdbh);
		soapObject.addProperty("ycdmc", ycdmc);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdjd", dcdjd);
		soapObject.addProperty("dcdwd", dcdwd);
		soapObject.addProperty("hb", hb);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("zyhcmc", zyhcmc);
		soapObject.addProperty("lfzc", lfzc);
		soapObject.addProperty("hcsl", hcsl);
		soapObject.addProperty("ybd", ybd);
		soapObject.addProperty("pjg", pjg);
		soapObject.addProperty("pjxj", pjxj);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswYcddcDataResult");
	}
	/**获取诱虫灯调查信息*/
	public String  getYhswYcddcData() {
		methodName = "getYhswYcddcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswYcddcDataResult");
	}
	/**搜索有害生物 诱虫灯数据*/
	public String searchYhswYcdData(String ycdmc, String sbzt) {
		methodName = "searchYhswYcdData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ycdmc", ycdmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswYcdDataResult");
	}
	/**有害生物松材线虫病普查信息 添加*/
	public String addYhswSxcbpcData(String ydbh, String dcr, String dcsj, String dcdw, String ksjd,
									String kswd, String xbh,
									String xbmj, String whcd, String ksmj, String sz, String kssl, String jzmc, String pjg,
									String pjxj, String qyr, String qybw, String qysl,
									String jjsl, String xdm, String jdr, String jdrq, String jdjg, String mclbf, String city, String county, String town, String village,
									String bz, String sczt, String sbr, String sbsj)
	{
		methodName = "addYhswSxcbpcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("ydbh", ydbh);
		soapObject.addProperty("dcr", dcr);
		soapObject.addProperty("dcsj", dcsj);
		soapObject.addProperty("dcdw", dcdw);
		soapObject.addProperty("ksjd", ksjd);
		soapObject.addProperty("kswd", kswd);
		soapObject.addProperty("xbh", xbh);
		soapObject.addProperty("xbmj", xbmj);
		soapObject.addProperty("whcd", whcd);
		soapObject.addProperty("ksmj", ksmj);
		soapObject.addProperty("sz", sz);
		soapObject.addProperty("kssl", kssl);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("pjg", pjg);
		soapObject.addProperty("pjxj", pjxj);
		soapObject.addProperty("qyr", qyr);
		soapObject.addProperty("qybw", qybw);
		soapObject.addProperty("qysl", qysl);
		soapObject.addProperty("jjsl", jjsl);
		soapObject.addProperty("xdm", xdm);
		soapObject.addProperty("jdr", jdr);
		soapObject.addProperty("jdrq", jdrq);
		soapObject.addProperty("jdjg", jdjg);
		soapObject.addProperty("mclbf", mclbf);
		soapObject.addProperty("city", city);
		soapObject.addProperty("county", county);
		soapObject.addProperty("town", town);
		soapObject.addProperty("village", village);
		soapObject.addProperty("bz", bz);
		soapObject.addProperty("sczt", sczt);
		soapObject.addProperty("sbr", sbr);
		soapObject.addProperty("sbsj", sbsj);
		return getResult(soapObject, "addYhswSxcbpcDataResult");
	}
	/**获取松材线虫病普查信息*/
	public String  getYhswSxcbpcData() {
		methodName = "getYhswSxcbpcData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		return getResult(soapObject, "getYhswSxcbpcDataResult");
	}
	/**搜索有害生物 松材线虫病普查数据*/
	public String searchYhswScxcbData(String jzmc, String sbzt) {
		methodName = "searchYhswScxcbData";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("jzmc", jzmc);
		soapObject.addProperty("sbzt", sbzt);
		return getResult(soapObject, "searchYhswScxcbDataResult");
	}
	/**
	 * 录入轨迹点
	 *
	 * @param SBH
	 * @param X
	 * @param Y
	 * @param time
	 * @param xlh
	 * @param type
	 * @return
	 */
	public String upPoint(String SBH, String X, String Y, String time,
						  String xlh, String type) {
		methodName = "uPLonLat";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("SBH", SBH);
		soapObject.addProperty("LON", X);
		soapObject.addProperty("LAT", Y);
		soapObject.addProperty("time", time);
		//soapObject.addProperty("xlh", xlh);
		//soapObject.addProperty("type", type);
		return getResult(soapObject, "uPLonLatResult");
	}

	public SoapSerializationEnvelope getEnvelope(SoapObject soapObject) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		try {
			HttpTransportSE transport = new HttpTransportSE(urlWebService,timeout);
			transport.debug = true;
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			String error = e.getMessage();//failed to connect to titanah.imwork.net/36.57.183.57 (port 8099) after 10000ms
			e.printStackTrace();
			return null;
		}
		return envelope;
	}

	public void initWebserviceTry() {
		String strVer = android.os.Build.VERSION.RELEASE;
		strVer = strVer.substring(0, 3).trim();
		float fv = Float.valueOf(strVer);
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
		}
	}

	/** 获取返回结果 */
	public String getResult(SoapObject soapObject, String param) {
		String result = "";
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return fwqException;
		}
		SoapObject object = null;
		if (envelope.bodyIn instanceof SoapFault) {
			return netException;
		} else {
			object = (SoapObject) envelope.bodyIn;
			if (object != null) {
				result = object.getProperty(param).toString();
			} else {
				result = netException;
			}
		}
		return result;
	}

	/** @author suncat
	 * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
	 * @return
	 */
	public static final boolean ping() {
		try {
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				//"success";
				return true;
			} else {
				//"failed";
			}
		} catch (IOException e) {
			//"IOException";
		} catch (InterruptedException e) {
			//"InterruptedException";
		} finally {
		}
		return false;
	}

}
