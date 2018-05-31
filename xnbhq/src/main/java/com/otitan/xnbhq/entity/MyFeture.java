package com.otitan.xnbhq.entity;

import com.esri.core.geodatabase.GeodatabaseFeature;

import java.io.Serializable;

/**
 * 要素数据实体类
 */
public class MyFeture implements Serializable {
	/**
	 */
	private static final long serialVersionUID = 4534463230709147300L;
	/**要素所在图层的工程名称*/
	private String pname;
	/**要素所在图层文件路径*/
	private String path;
	/**要素所在图层,图层名称*/
	private String cname;
	/**要素*/
	private static GeodatabaseFeature feature;

	public static GeodatabaseFeature getFeature() {
		return feature;
	}

	public static void setFeature(GeodatabaseFeature feature) {
		MyFeture.feature = feature;
	}

	public static MyLayer getMyLayer() {
		return myLayer;
	}

	public static void setMyLayer(MyLayer myLayer) {
		MyFeture.myLayer = myLayer;
	}

	/**要素所在的FeatureLayer*/
	private static  MyLayer myLayer;


	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public MyFeture() {
		// TODO Auto-generated constructor stub
	}

	public MyFeture(String pname,String path,String cname,GeodatabaseFeature feature,MyLayer layer) {
		this.pname = pname;
		this.path = path;
		this.cname = cname;
		this.feature = feature;
		this.myLayer = layer;
	}


}
