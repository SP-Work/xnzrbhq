package com.otitan.xnbhq.entity;

import java.io.Serializable;

/**
 * Created by li on 2017/6/26.
 * 木材经营运输证
 */

public class Mcjyysz implements Serializable {

    public String getDQXXID() {
        return DQXXID;
    }

    public void setDQXXID(String DQXXID) {
        this.DQXXID = DQXXID;
    }

    public String getTRANS_NUMBER() {
        return TRANS_NUMBER;
    }

    public void setTRANS_NUMBER(String TRANS_NUMBER) {
        this.TRANS_NUMBER = TRANS_NUMBER;
    }

    public String getHZ() {
        return HZ;
    }

    public void setHZ(String HZ) {
        this.HZ = HZ;
    }

    public String getTRANS_PEOPLE() {
        return TRANS_PEOPLE;
    }

    public void setTRANS_PEOPLE(String TRANS_PEOPLE) {
        this.TRANS_PEOPLE = TRANS_PEOPLE;
    }

    public String getLEAD_PEOPLE() {
        return LEAD_PEOPLE;
    }

    public void setLEAD_PEOPLE(String LEAD_PEOPLE) {
        this.LEAD_PEOPLE = LEAD_PEOPLE;
    }

    public String getYXRQ() {
        return YXRQ;
    }

    public void setYXRQ(String YXRQ) {
        this.YXRQ = YXRQ;
    }

    public String getCJ() {
        return CJ;
    }

    public void setCJ(String CJ) {
        this.CJ = CJ;
    }

    /*所在区县ID*/
    private String DQXXID;

    public String getXNAME() {
        return XNAME;
    }

    public void setXNAME(String XNAME) {
        this.XNAME = XNAME;
    }

    /*区县名称*/
    private String XNAME;
    /*运输证号*/
    private String TRANS_NUMBER;
    /*货主*/
    private String HZ;
    /*承运人*/
    private String TRANS_PEOPLE;
    /*领证人*/
    private String LEAD_PEOPLE;
    /*有效日期*/
    private String YXRQ;
    /*材积*/
    private String CJ;

    public Mcjyysz(){

    }

    public Mcjyysz(String DQXXID,String TRANS_NUMBER,String HZ,String TRANS_PEOPLE,String LEAD_PEOPLE,String YXRQ,String CJ,String XNAME){
        this.DQXXID = DQXXID;
        this.TRANS_NUMBER = TRANS_NUMBER;
        this.HZ = HZ;
        this.TRANS_PEOPLE = TRANS_PEOPLE;
        this.LEAD_PEOPLE = LEAD_PEOPLE;
        this.YXRQ = YXRQ;
        this.CJ = CJ;
        this.XNAME = XNAME;
    }


}
