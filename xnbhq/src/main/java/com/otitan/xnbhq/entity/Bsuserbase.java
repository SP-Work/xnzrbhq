package com.otitan.xnbhq.entity;

/**
 * 用户信息
 */
public class Bsuserbase {
	/* 用户名 */
	private String USERCODE;
	/* 密码 这里是密文 */
	private String PASSWORD;
	/* 真实姓名 */
	private String REALNAME;
	/* 电话号码 */
	private String TELNO;
	/* 手机号码 */
	private String MOBILEPHONENO;
	/* 邮箱地址 */
	private String USEREMAIL;
	/* 性别 1男 0女 3隐藏 */
	private int USERSEX;
	/* 年龄 */
	private int USEROLD;
	/* 出生日期 */
	private String USERBIRTH;
	/* 居住地址（省） */
	private String USER_S;
	/* 民族 */
	private String USERMZ;
	/* 婚否 */
	private String USERHY;
	/* 政治面貌 */
	private String USERZZMM;
	/* 个人评价 */
	private String USERGRPJ;
	/* 居住地址（市） */
	private String USER_CITY;
	/* 居住地址（街道） */
	private String USER_JD;
	/* 0 待审核 1 启动 -1 停用 */
	private int ISACTIVE;
	/* 可登录系统IDS */
	private String SYSTEMIDS;
	/* 系统类别 */
	private int SYSTEMTYPE;
	/* 主键 */
	private int ID;
	/* 用户头像图片 */
	private String USER_IMAGE;
	/* 备注 */
	private String BZ;
	/* 是否是专家 0否1是 */
	private int ISZJ;
	/* 上次登录时间 */
	private String LASTLOGIN;
	/* 本次登录时间 */
	private String THISLOGIN;
	/* 登录次数 */
	private int LOGINTIMES;
	/* 皮肤名称 */
	private String SKINNAME;
	/* 排序 */
	private int PX;
	/* 所属部门ID */
	private int UNITID;
	/* 所属部门名称 */
	private String UNITNAME;
	/* 数据共享范围 */
	private String DATASHARE;

	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getREALNAME() {
		return REALNAME;
	}

	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}

	public String getTELNO() {
		return TELNO;
	}

	public void setTELNO(String tELNO) {
		TELNO = tELNO;
	}

	public String getMOBILEPHONENO() {
		return MOBILEPHONENO;
	}

	public void setMOBILEPHONENO(String mOBILEPHONENO) {
		MOBILEPHONENO = mOBILEPHONENO;
	}

	public String getUSEREMAIL() {
		return USEREMAIL;
	}

	public void setUSEREMAIL(String uSEREMAIL) {
		USEREMAIL = uSEREMAIL;
	}

	public int getUSERSEX() {
		return USERSEX;
	}

	public void setUSERSEX(int uSERSEX) {
		USERSEX = uSERSEX;
	}

	public int getUSEROLD() {
		return USEROLD;
	}

	public void setUSEROLD(int uSEROLD) {
		USEROLD = uSEROLD;
	}

	public String getUSERBIRTH() {
		return USERBIRTH;
	}

	public void setUSERBIRTH(String uSERBIRTH) {
		USERBIRTH = uSERBIRTH;
	}

	public String getUSER_S() {
		return USER_S;
	}

	public void setUSER_S(String uSER_S) {
		USER_S = uSER_S;
	}

	public String getUSERMZ() {
		return USERMZ;
	}

	public void setUSERMZ(String uSERMZ) {
		USERMZ = uSERMZ;
	}

	public String getUSERHY() {
		return USERHY;
	}

	public void setUSERHY(String uSERHY) {
		USERHY = uSERHY;
	}

	public String getUSERZZMM() {
		return USERZZMM;
	}

	public void setUSERZZMM(String uSERZZMM) {
		USERZZMM = uSERZZMM;
	}

	public String getUSERGRPJ() {
		return USERGRPJ;
	}

	public void setUSERGRPJ(String uSERGRPJ) {
		USERGRPJ = uSERGRPJ;
	}

	public String getUSER_CITY() {
		return USER_CITY;
	}

	public void setUSER_CITY(String uSER_CITY) {
		USER_CITY = uSER_CITY;
	}

	public String getUSER_JD() {
		return USER_JD;
	}

	public void setUSER_JD(String uSER_JD) {
		USER_JD = uSER_JD;
	}

	public int getISACTIVE() {
		return ISACTIVE;
	}

	public void setISACTIVE(int iSACTIVE) {
		ISACTIVE = iSACTIVE;
	}

	public String getSYSTEMIDS() {
		return SYSTEMIDS;
	}

	public void setSYSTEMIDS(String sYSTEMIDS) {
		SYSTEMIDS = sYSTEMIDS;
	}

	public int getSYSTEMTYPE() {
		return SYSTEMTYPE;
	}

	public void setSYSTEMTYPE(int sYSTEMTYPE) {
		SYSTEMTYPE = sYSTEMTYPE;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUSER_IMAGE() {
		return USER_IMAGE;
	}

	public void setUSER_IMAGE(String uSER_IMAGE) {
		USER_IMAGE = uSER_IMAGE;
	}

	public String getBZ() {
		return BZ;
	}

	public void setBZ(String bZ) {
		BZ = bZ;
	}

	public int getISZJ() {
		return ISZJ;
	}

	public void setISZJ(int iSZJ) {
		ISZJ = iSZJ;
	}

	public String getLASTLOGIN() {
		return LASTLOGIN;
	}

	public void setLASTLOGIN(String lASTLOGIN) {
		LASTLOGIN = lASTLOGIN;
	}

	public String getTHISLOGIN() {
		return THISLOGIN;
	}

	public void setTHISLOGIN(String tHISLOGIN) {
		THISLOGIN = tHISLOGIN;
	}

	public int getLOGINTIMES() {
		return LOGINTIMES;
	}

	public void setLOGINTIMES(int lOGINTIMES) {
		LOGINTIMES = lOGINTIMES;
	}

	public String getSKINNAME() {
		return SKINNAME;
	}

	public void setSKINNAME(String sKINNAME) {
		SKINNAME = sKINNAME;
	}

	public int getPX() {
		return PX;
	}

	public void setPX(int pX) {
		PX = pX;
	}

	public int getUNITID() {
		return UNITID;
	}

	public void setUNITID(int uNITID) {
		UNITID = uNITID;
	}

	public String getUNITNAME() {
		return UNITNAME;
	}

	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}

	public String getDATASHARE() {
		return DATASHARE;
	}

	public void setDATASHARE(String dATASHARE) {
		DATASHARE = dATASHARE;
	}

}
