package com.otitan.xnbhq.entity;

import java.io.Serializable;

/**
 * Created by otitan_li on 2018/4/10.
 * 紧急事件现场信息
 */

public class Emergency implements Serializable {

    public String getXJ_JD() {
        return XJ_JD;
    }

    public void setXJ_JD(String XJ_JD) {
        this.XJ_JD = XJ_JD;
    }

    public String getXJ_WD() {
        return XJ_WD;
    }

    public void setXJ_WD(String XJ_WD) {
        this.XJ_WD = XJ_WD;
    }

    public String getXJ_SJMC() {
        return XJ_SJMC;
    }

    public void setXJ_SJMC(String XJ_SJMC) {
        this.XJ_SJMC = XJ_SJMC;
    }

    public String getXJ_MSXX() {
        return XJ_MSXX;
    }

    public void setXJ_MSXX(String XJ_MSXX) {
        this.XJ_MSXX = XJ_MSXX;
    }

    public String getXJ_ZPDZ() {
        return XJ_ZPDZ;
    }

    public void setXJ_ZPDZ(String XJ_ZPDZ) {
        this.XJ_ZPDZ = XJ_ZPDZ;
    }

    public String getXJ_SBBH() {
        return XJ_SBBH;
    }

    public void setXJ_SBBH(String XJ_SBBH) {
        this.XJ_SBBH = XJ_SBBH;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    /**经度*/
    private String XJ_JD;
    /**纬度*/
    private String XJ_WD;
    /**事件名称*/
    private String XJ_SJMC;
    /**描述信息*/
    private String XJ_MSXX;
    /**现场照片*/
    private String XJ_ZPDZ;
    /**设备编号*/
    private String XJ_SBBH;
    /**备注*/
    private String REMARK;


    public Emergency(){

    }

    public Emergency(String lon, String lat, String msxx,String sjmc, String image,String sbh, String remark){
        this.XJ_JD = lon;
        this.XJ_WD= lat;
        this.XJ_MSXX = msxx;
        this.XJ_SJMC = sjmc;
        this.XJ_ZPDZ = image;
        this.XJ_SBBH = sbh;
        this.REMARK = remark;
    }


}
