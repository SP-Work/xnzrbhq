package com.otitan.xnbhq.entity;

import java.io.Serializable;

/**
 * Created by li on 2017/6/23.
 * 疫源疫病信息上报表实体
 */

public class YyybBean implements Serializable {

    public String getDEPPID() {
        return DEPPID;
    }

    public void setDEPPID(String DEPPID) {
        this.DEPPID = DEPPID;
    }

    public String getMONITOR() {
        return MONITOR;
    }

    public void setMONITOR(String MONITOR) {
        this.MONITOR = MONITOR;
    }

    public String getAEDTIME() {
        return AEDTIME;
    }

    public void setAEDTIME(String AEDTIME) {
        this.AEDTIME = AEDTIME;
    }

    public String getANIMALNAME() {
        return ANIMALNAME;
    }

    public void setANIMALNAME(String ANIMALNAME) {
        this.ANIMALNAME = ANIMALNAME;
    }

    public String getANIMALNUM() {
        return ANIMALNUM;
    }

    public void setANIMALNUM(String ANIMALNUM) {
        this.ANIMALNUM = ANIMALNUM;
    }

    public String getSYMPTOM() {
        return SYMPTOM;
    }

    public void setSYMPTOM(String SYMPTOM) {
        this.SYMPTOM = SYMPTOM;
    }

    public String getSUSPATHOGE() {
        return SUSPATHOGE;
    }

    public void setSUSPATHOGE(String SUSPATHOGE) {
        this.SUSPATHOGE = SUSPATHOGE;
    }

    public String getMEASURE() {
        return MEASURE;
    }

    public void setMEASURE(String MEASURE) {
        this.MEASURE = MEASURE;
    }

    public String getREMARKER() {
        return REMARKER;
    }

    public void setREMARKER(String REMARKER) {
        this.REMARKER = REMARKER;
    }

    String DEPPID;//监测单位id
    String MONITOR;//监测人员
    String AEDTIME;//监测时间
    String ANIMALNAME;//动物名称
    String ANIMALNUM;//动物数量
    String SYMPTOM;//症状
    String SUSPATHOGE;//疑似病原体
    String MEASURE;//防护措施
    String REMARKER;//备注

    public YyybBean(){

    }

    public YyybBean(String DEPPID,String MONITOR,String AEDTIME,String ANIMALNAME,String ANIMALNUM,String SYMPTOM,String SUSPATHOGE,String MEASURE,String REMARKER){
        this.DEPPID = DEPPID;
        this.MONITOR = MONITOR;
        this.AEDTIME = AEDTIME;
        this.ANIMALNAME = ANIMALNAME;
        this.ANIMALNUM = ANIMALNUM;
        this.SYMPTOM = SYMPTOM;
        this.SUSPATHOGE = SUSPATHOGE;
        this.MEASURE = MEASURE;
        this.REMARKER = REMARKER;
    }

}
