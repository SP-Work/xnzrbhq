package com.otitan.xnbhq.entity;

import java.io.Serializable;

/**
 * Created by li on 2017/6/23.
 *
 * 疫源疫病信息实体类
 */

public class JcdwBean implements Serializable {

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String DEPARTMENT;//名称
    String ID;//名称对应id


}
