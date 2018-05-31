package com.otitan.xnbhq.entity;

import java.io.Serializable;

/**
 * Created by otitan_li on 2018/5/30.
 * MobileInfo 设备信息
 */

public class MobileInfo implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSBH() {
        return SBH;
    }

    public void setSBH(String SBH) {
        this.SBH = SBH;
    }

    public String getSYZNAME() {
        return SYZNAME;
    }

    public void setSYZNAME(String SYZNAME) {
        this.SYZNAME = SYZNAME;
    }

    public String getSYZPHONE() {
        return SYZPHONE;
    }

    public void setSYZPHONE(String SYZPHONE) {
        this.SYZPHONE = SYZPHONE;
    }

    public String getDQXXID() {
        return DQXXID;
    }

    public void setDQXXID(String DQXXID) {
        this.DQXXID = DQXXID;
    }

    public String getDJTIME() {
        return DJTIME;
    }

    public void setDJTIME(String DJTIME) {
        this.DJTIME = DJTIME;
    }

    public String getSBMC() {
        return SBMC;
    }

    public void setSBMC(String SBMC) {
        this.SBMC = SBMC;
    }

    public String getXLH() {
        return XLH;
    }

    public void setXLH(String XLH) {
        this.XLH = XLH;
    }

    private String id;
    private String SBH;
    private String SYZNAME;
    private String SYZPHONE;
    private String DQXXID;
    private String DJTIME;
    private String SBMC;
    private String XLH;

    public MobileInfo(){

    }
    public MobileInfo(String id,String sbh,String sysname,String sysphone,String dqid,String jdtime,String sbmc,String xlh){
        this.id = id;
        this.SBH = sbh;
        this.SYZNAME = sysname;
        this.SYZPHONE = sysphone;
        this.DQXXID = dqid;
        this.DJTIME = jdtime;
        this.SBMC = sbmc;
        this.XLH = xlh;
    }


}
