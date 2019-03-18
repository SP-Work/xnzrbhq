package com.otitan.xnbhq.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by sp on 2019/3/14.
 * 样地属性
 */
@Entity
public class LineInfo {

    @Id
    private long ID;

    private String CountyName; // 县名称

    private String CountyCode; // 县代码

    private String TownName; // 乡镇名称

    private String TownCode; // 乡镇代码

    private String VillageName; // 村名称

    private String RouteNum; // 踏查路线编号

    private String People; // 调查人

    private String Date; // 调查日期

    private String Distance; // 调查长度

    private String Area; // 调查面积

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String countyName) {
        CountyName = countyName;
    }

    public String getCountyCode() {
        return CountyCode;
    }

    public void setCountyCode(String countyCode) {
        CountyCode = countyCode;
    }

    public String getTownName() {
        return TownName;
    }

    public void setTownName(String townName) {
        TownName = townName;
    }

    public String getTownCode() {
        return TownCode;
    }

    public void setTownCode(String townCode) {
        TownCode = townCode;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getRouteNum() {
        return RouteNum;
    }

    public void setRouteNum(String routeNum) {
        RouteNum = routeNum;
    }

    public String getPeople() {
        return People;
    }

    public void setPeople(String people) {
        People = people;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }
}
