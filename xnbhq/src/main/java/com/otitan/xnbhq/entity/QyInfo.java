package com.otitan.xnbhq.entity;

/**
 * Created by 32 on 2017/6/27.
 *
 * 企业信息实体类
 */

public class QyInfo {

    private String DQXXID; // 地区id

    private String COMPANYPROP; // 企业性质

    private String COMPANYNAME; // 企业名称

    private String COMPANYADDR; // 企业地址

    private String BUSISCOPE; // 经营范围

    private String PROCRANGE; // 加工范围

    private String PERMIT_NUM; // 许可证号

    private String PERMIT_CZR; //持证人

    public QyInfo(String DQXXID, String COMPANYPROP, String COMPANYNAME, String COMPANYADDR, String BUSISCOPE, String PROCRANGE, String PERMIT_NUM, String PERMIT_CZR) {
        this.DQXXID = DQXXID;
        this.COMPANYPROP = COMPANYPROP;
        this.COMPANYNAME = COMPANYNAME;
        this.COMPANYADDR = COMPANYADDR;
        this.BUSISCOPE = BUSISCOPE;
        this.PROCRANGE = PROCRANGE;
        this.PERMIT_NUM = PERMIT_NUM;
        this.PERMIT_CZR = PERMIT_CZR;
    }

    public String getDQXXID() {
        return DQXXID;
    }

    public void setDQXXID(String DQXXID) {
        this.DQXXID = DQXXID;
    }

    public String getCOMPANYPROP() {
        return COMPANYPROP;
    }

    public void setCOMPANYPROP(String COMPANYPROP) {
        this.COMPANYPROP = COMPANYPROP;
    }

    public String getCOMPANYNAME() {
        return COMPANYNAME;
    }

    public void setCOMPANYNAME(String COMPANYNAME) {
        this.COMPANYNAME = COMPANYNAME;
    }

    public String getCOMPANYADDR() {
        return COMPANYADDR;
    }

    public void setCOMPANYADDR(String COMPANYADDR) {
        this.COMPANYADDR = COMPANYADDR;
    }

    public String getBUSISCOPE() {
        return BUSISCOPE;
    }

    public void setBUSISCOPE(String BUSISCOPE) {
        this.BUSISCOPE = BUSISCOPE;
    }

    public String getPROCRANGE() {
        return PROCRANGE;
    }

    public void setPROCRANGE(String PROCRANGE) {
        this.PROCRANGE = PROCRANGE;
    }

    public String getPERMIT_NUM() {
        return PERMIT_NUM;
    }

    public void setPERMIT_NUM(String PERMIT_NUM) {
        this.PERMIT_NUM = PERMIT_NUM;
    }

    public String getPERMIT_CZR() {
        return PERMIT_CZR;
    }

    public void setPERMIT_CZR(String PERMIT_CZR) {
        this.PERMIT_CZR = PERMIT_CZR;
    }
}
