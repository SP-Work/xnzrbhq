package com.otitan.xnbhq.entity;

/**
 * 多级菜单内容信息实体类
 */
public class YzlTree {
	/** ID*/
	private int ID;
	/** 名称*/
	private String NAME;
	/** 代码*/
	private String DLDM;
	/** 级别*/
	private int DLLEVEL;
	/** 父节点代码*/
	private String DLPARENTDM;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getDLDM() {
		return DLDM;
	}
	public void setDLDM(String dLDM) {
		DLDM = dLDM;
	}
	public int getDLLEVEL() {
		return DLLEVEL;
	}
	public void setDLLEVEL(int dLLEVEL) {
		DLLEVEL = dLLEVEL;
	}
	public String getDLPARENTDM() {
		return DLPARENTDM;
	}
	public void setDLPARENTDM(String dLPARENTDM) {
		DLPARENTDM = dLPARENTDM;
	}
}
