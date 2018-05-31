package com.otitan.xnbhq.dao;

public class XuJiModel {

	/**样地号*/
	private String YDH="";
	/**样木编号*/
	private String YMBH="";
	/**树种名称*/
	private String SZMC="";
	/**胸径*/
	private double XJ=0;
	/**树高*/
	private double SG=0;
	/**单株蓄积*/
	private double DZXJ=0;
	/**该单株蓄积的株数*/
	private int ZS=0;
	/**单个样地蓄积*/
	private double YDXJ=0;
	/**实测断面积*/
	private double SCDMJ=0;

	public XuJiModel() {
	}

	public XuJiModel(String yDH, String yMBH, String sZMC, double xJ, double sG) {
		YDH = yDH;
		YMBH = yMBH;
		SZMC = sZMC;
		XJ = xJ;
		SG = sG;
	}
	/**用二元模型计算样地蓄积时，用到单株平均蓄积和株数*/
	public XuJiModel(String yDH, double dZXJ, int zS) {
		super();
		YDH = yDH;
		DZXJ = dZXJ;
		ZS = zS;
	}
	/**用二元模型计算总体蓄积时，用到样地蓄积*/
	public XuJiModel(String yDH, double yDXJ) {
		super();
		YDH = yDH;
		YDXJ = yDXJ;
	}
	/**用角规样地模型法计算各树种蓄积，用到实测断面积*/
	public XuJiModel(String sZMC, double sG, double sCDMJ) {
		super();
		SZMC = sZMC;
		SG = sG;
		SCDMJ = sCDMJ;
	}

	public double getSCDMJ() {
		return SCDMJ;
	}

	public void setSCDMJ(double sCDMJ) {
		SCDMJ = sCDMJ;
	}

	public double getDZXJ() {
		return DZXJ;
	}

	public void setDZXJ(double dZXJ) {
		DZXJ = dZXJ;
	}

	public int getZS() {
		return ZS;
	}

	public void setZS(int zS) {
		ZS = zS;
	}

	public double getYDXJ() {
		return YDXJ;
	}

	public void setYDXJ(double yDXJ) {
		YDXJ = yDXJ;
	}
	public String getYDH() {
		return YDH;
	}
	public void setYDH(String yDH) {
		YDH = yDH;
	}
	public String getYMBH() {
		return YMBH;
	}
	public void setYMBH(String yMBH) {
		YMBH = yMBH;
	}
	public String getSZMC() {
		return SZMC;
	}
	public void setSZMC(String sZMC) {
		SZMC = sZMC;
	}
	public double getXJ() {
		return XJ;
	}
	public void setXJ(double xJ) {
		XJ = xJ;
	}
	public double getSG() {
		return SG;
	}
	public void setSG(double sG) {
		SG = sG;
	}


}
