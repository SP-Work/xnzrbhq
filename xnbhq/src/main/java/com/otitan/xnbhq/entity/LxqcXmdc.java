package com.otitan.xnbhq.entity;

import com.otitan.xnbhq.db.sqlite.Column;
import com.otitan.xnbhq.db.sqlite.Id;
import com.otitan.xnbhq.db.sqlite.Table;

/**
 * 连续清查 下木调查实体类
 */
@Table(name = "BUSI_LXQCXMDCB")
public class LxqcXmdc {//连续清查 下木调查表
	@Id(autoGenerate = true, name = "Id")
	private Long id;//主键
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYDH() {
		return YDH;
	}

	public void setYDH(String yDH) {
		YDH = yDH;
	}

	public String getSZMC() {
		return SZMC;
	}

	public void setSZMC(String sZMC) {
		SZMC = sZMC;
	}

	public String getGD() {
		return GD;
	}

	public void setGD(String gD) {
		GD = gD;
	}

	public String getXJ() {
		return XJ;
	}

	public void setXJ(String xJ) {
		XJ = xJ;
	}

	@Column(name = "YDH")
	private String YDH;//样地号
	@Column(name = "SZMC")
	private String SZMC;//树种名称
	@Column(name = "GD")
	private String GD;//高度
	@Column(name = "XJ")
	private String XJ;//胸径

	public LxqcXmdc() {
		// TODO Auto-generated constructor stub
	}

	public LxqcXmdc(String ydh,String szmc,String gd,String xj) {
		this.YDH = ydh;
		this.SZMC = szmc;
		this.GD = gd;
		this.XJ = xj;
	}

}
