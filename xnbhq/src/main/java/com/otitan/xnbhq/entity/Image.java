package com.otitan.xnbhq.entity;

/**
 * Created by otitan_li on 2018/4/28.
 * 照片类
 */

public class Image implements java.io.Serializable{


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    private String name;
    private String base;



}
