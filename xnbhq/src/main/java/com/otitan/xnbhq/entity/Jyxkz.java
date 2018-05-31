package com.otitan.xnbhq.entity;

/**
 * Created by 32 on 2017/6/27.
 *
 * 木材经营许可证查询实体类
 */

public class Jyxkz {

    private String ID;

    private String DQXXID; // 地区id

    private String PERMIT_NUM; // 许可证号

    private String COMPANYNAME; // 企业名称

    private String COMPANYADDR; // 企业地址

    private String DQNAME; // 所属地区

    private String SFZX; // 是否注销

    private String ISTG; // 是否年审

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDQXXID() {
        return DQXXID;
    }

    public void setDQXXID(String DQXXID) {
        this.DQXXID = DQXXID;
    }

    public String getPERMIT_NUM() {
        return PERMIT_NUM;
    }

    public void setPERMIT_NUM(String PERMIT_NUM) {
        this.PERMIT_NUM = PERMIT_NUM;
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

    public String getDQNAME() {
        return DQNAME;
    }

    public void setDQNAME(String DQNAME) {
        this.DQNAME = DQNAME;
    }

    public String getSFZX() {
        return SFZX;
    }

    public void setSFZX(String SFZX) {
        this.SFZX = SFZX;
    }

    public String getISTG() {
        return ISTG;
    }

    public void setISTG(String ISTG) {
        this.ISTG = ISTG;
    }
}
