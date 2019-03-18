package com.otitan.xnbhq.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by sp on 2019/3/12.
 * 踏查点
 */
@Entity
public class CheckPoint {

    @Id
    private long ID;

    private String Name; // 踏查点名称

    private String Lon; // 经度

    private String Lat; // 纬度

    private String Altitude; // 海拔

    private String Time; // 时间

    private String Forest; // 林分

    private String Origin; // 起源

    private String ClassNumber; // 小班号

    private String Area; // 面积

    private String PicPath; // 现场照片

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getAltitude() {
        return Altitude;
    }

    public void setAltitude(String altitude) {
        Altitude = altitude;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getForest() {
        return Forest;
    }

    public void setForest(String forest) {
        Forest = forest;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getClassNumber() {
        return ClassNumber;
    }

    public void setClassNumber(String classNumber) {
        ClassNumber = classNumber;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }
}
