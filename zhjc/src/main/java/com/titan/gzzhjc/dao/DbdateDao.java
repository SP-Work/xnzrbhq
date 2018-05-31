package com.titan.gzzhjc.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/3/5
 *
 * 数据集合
 */

public interface DbdateDao {
    //根据地区名获取贵州林场公园统计数据
    List<Map<String, String>> getGyLc(String locname, int type);
    //根据地区名获取贵州林场公园数据
    List<Map<String, String>> getGyLcData(String locname, int type);
    //根据地区名获取贵州林情数据
    List<Map<String,String>> getGzLq(String locname, int time);


    /*获取采伐限额数据 name  地区名称 coloum 查询字段 */
    List<Map<String,String>> getLmcfxeData(String name, int time);
    /*获取退耕还林数据*/
    List<Map<String,String>> getTghlData(String name, int time);
    /*获取保护区公园等数据*/
    List<Map<String,String>> getBhqgyData(String name, int time);
    /*获取林权改革数据*/
    List<Map<String,String>> getLqggData(String name, int time);
    /*获取石漠化数据*/
    List<Map<String,String>> getShmhData(String name, int time);
    /*获取林业产业数据*/
    List<Map<String,String>> getLycyData(String name, int time);
    /*获取管护人员数据*/
    List<Map<String,String>> getGhryData(String name, int time);
    /*获取有害生物数据*/
    List<Map<String,String>> getYhswData(String name, int time);
    /*获取种苗生产数据*/
    List<Map<String,String>> getZmscData(String name, int time);
    /*获取生态护林员数据*/
    List<Map<String,String>> getSthlyData(String name, int time);
    /*获取生态护林员数据*/
    List<Map<String,String>> getYzlData(String name, int time, String selfield);
    /*获取林业基础数据*/
    List<Map<String,String>> getLyjcData(String name, int time, String selfild, int formtype);

}
