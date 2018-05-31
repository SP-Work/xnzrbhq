package com.otitan.xnbhq.entity;

/**
 * Created by li on 2017/3/14.
 *
 * 枚举条目
 */

public enum ActionMode {
    /** 空间统计-多斑测量面积 */
    MODE_XBQD,
    /** 空间统计-多斑测量面积 */
    MODE_DBCEMJ,
    /** 空间统计 */
    MODE_TONGJI,
    /** 小班查询 */
    MODE_XBSEARCHZDY,
    /** 小班查询 */
    MODE_XBSEARCHJD,
    /** 小班属性编辑 */
    MODE_attribute_edit,
    /** 小班属性查询 */
    MODE_ISEARCHE,
    /** 小班添加 */
    MODE_EDIT_ADD,
    /** 添加共边小班 */
    MODE_EDIT_ADD_GB,
    /** 复制小班 */
    MODE_EDIT_COPY,
    /** GPS勾绘添加小班 */
    MODE_EDIT_ADD_GPS,
    /** 测距 */
    MODE_CEJU,
    /** 测面 */
    MODE_CEMIAN,
    /** 新增小班 */
    MODE_edit,
    /** 小班查询 */
    MODE_searchXB,
    /** 小地名查询 */
    MODE_searchXDM,
    /** 轨迹 */
    MODE_GUIJI,
    /** 足迹 */
    MODE_ZUJI,
    /** 选择小班 */
    MODE_SELECT,
    /** 修斑 */
    MODE_XIUBAN,
    /** 切割 */
    MODE_QIEGE,
    /** 数据下载 */
    MODE_DOWNLOAD,
    /** 数据上传 */
    MODE_DATAUP,
    /** 路径导航 */
    MODE_NAVIGATION,
    /** 小班路径导航 */
    MODE_XBNAVIGATION,
    /** 添加小地名 */
    MODE_ADD_ADDRESS,
    /** 点击地图取坐标 */
    MODE_ADD_CALLOUT,
    /**属性标注*/
    MODE_ADD_LABLE,
    /** null */
    MODE_NULL
}
