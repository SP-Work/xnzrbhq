package com.otitan.xnbhq.entity;

import com.otitan.xnbhq.db.sqlite.Column;
import com.otitan.xnbhq.db.sqlite.Id;
import com.otitan.xnbhq.db.sqlite.Table;

import java.io.Serializable;

/**
 * 收藏信息实体类
 */
@Table(name = "SHOUCANG")
public class ShouCang implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**主键*/
	@Id(autoGenerate = true, name = "Id")
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMIAOSHU() {
		return MIAOSHU;
	}
	public void setMIAOSHU(String mIAOSHU) {
		MIAOSHU = mIAOSHU;
	}
	public String getLON() {
		return LON;
	}
	public void setLON(String lON) {
		LON = lON;
	}
	public String getLAT() {
		return LAT;
	}
	public void setLAT(String lAT) {
		LAT = lAT;
	}
	public String getTIME() {
		return TIME;
	}
	public void setTIME(String tIME) {
		TIME = tIME;
	}
	/**收藏描述*/
	@Column(name = "MIAOSHU")
	private String MIAOSHU="";
	/**经度*/
	@Column(name = "LON")
	private String LON="";
	/**纬度*/
	@Column(name = "LAT")
	private String LAT="";
	/**添加时间*/
	@Column(name = "TIME")
	private String TIME="";

	public ShouCang() {
		// TODO Auto-generated constructor stub
	}

	public ShouCang(String miaoshu,String lon,String lat,String time) {
		this.MIAOSHU = miaoshu;
		this.LAT = lat;
		this.LON = lon;
		this.TIME = time;
	}

}
