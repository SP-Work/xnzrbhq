package com.otitan.xnbhq.entity;

/**
 * 字段实体类（舍去）
 */
public class DICYZL {

	/** 字段索引（序号）*/
	private int ID;
	/** 字段索引（序号）*/
	private String DICTYPE;
	/** 字段索引（序号）*/
	private String DICITEMID;
	/** 字段名称*/
	private String DICTYPEDES;
	/** 字段代码对应的名称*/
	private String DICNAME;
	/** 字段代码*/
	private String DICCODE;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getDICTYPE() {
		return DICTYPE;
	}
	public void setDICTYPE(String dICTYPE) {
		DICTYPE = dICTYPE;
	}
	public String getDICITEMID() {
		return DICITEMID;
	}
	public void setDICITEMID(String dICITEMID) {
		DICITEMID = dICITEMID;
	}
	public String getDICTYPEDES() {
		return DICTYPEDES;
	}
	public void setDICTYPEDES(String dICTYPEDES) {
		DICTYPEDES = dICTYPEDES;
	}
	public String getDICNAME() {
		return DICNAME;
	}
	public void setDICNAME(String dICNAME) {
		DICNAME = dICNAME;
	}
	public String getDICCODE() {
		return DICCODE;
	}
	public void setDICCODE(String dICCODE) {
		DICCODE = dICCODE;
	}
}
