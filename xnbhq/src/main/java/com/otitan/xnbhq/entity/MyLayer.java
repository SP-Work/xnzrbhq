package com.otitan.xnbhq.entity;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.renderer.Renderer;

import java.io.Serializable;

/**
 * geodatabase数据信息实体类
 */
public class MyLayer implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 2102691223831083079L;

	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	/**工程名称*/
	private String pname;
	/**geodatabase数据名称*/
	private String cname;
	/**geodatabase数据地址*/
	private String path;
	/**geodatabase数据是否加密*/
	private boolean flag;
	/**geodatabase数据图层名称*/
	private String lname;
	/**geodatabase数据图层renderer*/
	private Renderer renderer;
	/** FeatureLayer*/
	private FeatureLayer layer;
	/** GeodatabaseFeatureTable*/
	private GeodatabaseFeatureTable table;

	public GeodatabaseFeatureTable getTable() {
		return table;
	}
	public void setTable(GeodatabaseFeatureTable table) {
		this.table = table;
	}
	public FeatureLayer getLayer() {
		return layer;
	}
	public void setLayer(FeatureLayer layer) {
		this.layer = layer;
	}
	public Renderer getRenderer() {
		return renderer;
	}
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

}
