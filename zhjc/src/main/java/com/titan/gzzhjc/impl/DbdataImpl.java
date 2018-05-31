package com.titan.gzzhjc.impl;

import android.content.Context;

import com.titan.gzzhjc.R;
import com.titan.gzzhjc.dao.DbdateDao;
import com.titan.gzzhjc.util.ResourcesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;

/**
 * Created by li on 2017/3/5
 *
 * 向数据集合中添加数据
 */

public class DbdataImpl implements DbdateDao {

    String databaseName = "";
    private static DbdataImpl instance;
    private static Context mContext;
    public  static  String[] colums = null;
    public static String[] columnames=null;

    public static DbdataImpl getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new DbdataImpl(context);
        }
        return instance;
    }

    public DbdataImpl(Context context) {
        try {
            databaseName = ResourcesManager.getInstance(context).getFilePath("/zhjc/db.sqlite");
            //databaseName =context.getFilesDir().getPath()+ "/db.sqlite";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 林业基础
     */
    @Override
    public List<Map<String, String>> getLyjcData(String name, int time,String selfild,int formtype) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            if(formtype == 0){
                sql = "select GZLYWCQK.DQNAME 地区名称,"+selfild+" from  " +
                        "TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                        "("+sql1+") or TAB_DQ.ID = ("+ sql1 +") order by GZLYWCQK.DQCODE;";
            }else{
                sql = "select DQNAME 地区名称,"+selfild+" from " +
                        "GZLYWCQK where DQCODE = '" + formtype+"' order by DQCODE;";
            }

        }else {
            if(formtype == 0){
                sql = "select GZLYJCB.DQNAME 地区名称,"+selfild+" from  " +
                        "TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                        "("+sql1+") or TAB_DQ.ID = ("+ sql1 +") order by GZLYJCB.DQCODE;";
            }else{
                sql = "select DQNAME 地区名称,"+selfild+" from " +
                        "GZLYJCB where DQCODE = '" +formtype+"' order by DQCODE;";
            }
        }
        return getSqlData(sql);
    }

    /**
     * 营造林
     */
    @Override
    public List<Map<String, String>> getYzlData(String name,int time,String selfield) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,"+selfield +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
            /*sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.YZLGJMJHJ 报国家合计面积（万亩）,GZLYWCQK.GJRGZL 报国家人工造林面积（万亩）," +
                    "GZLYWCQK.GJFSYL 报国家封山育林面积（万亩）,GZLYWCQK.YZLSZFHJ 报省政府合计面积（万亩）,GZLYWCQK.YZLRGZL 报省政府人工造林面积（万亩）," +
                    "GZLYWCQK.YZLFSYL 报省政府封山育林面积（万亩）,GZLYWCQK.YZLMJSLFY 封山育林总面积（万亩）,GZLYWCQK.YZLTRZJGJHJ 报国家合计资金（万元）," +
                    "GZLYWCQK.YZLTRZJGJZY 报国家中央资金（万元）,GZLYWCQK.YZLTRZJGJSJ 报国家省级投资（万元）,GZLYWCQK.YZLTRZFHJ 报省政府合计资金（万元）," +
                    "GZLYWCQK.YZLTRZFZY 报省政府中央资金（万元）,GZLYWCQK.YZLTRZFSJ 报省政府省级投资（万元）,GZLYWCQK.YZLZJSLFY 森林抚育合计资金（万元） " +
                    "from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";*/
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,"+selfield+" from TAB_DQ " +
                    "INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
            /*sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.YZLMJHJ 报国家合计面积（万亩）,GZLYJCB.YZLMJRGZL 报国家人工造林面积（万亩）," +
                    "GZLYJCB.YZLMJFSYL 报国家封山育林面积（万亩）,GZLYJCB.YZLZJHJ 报省政府合计面积（万亩）,GZLYJCB.YZLZJRGZL 报省政府人工造林面积（万亩）," +
                    "GZLYJCB.YZLZJFSYL 报省政府封山育林面积（万亩）,GZLYJCB.YZLMJSLFY 封山育林总面积（万亩）,GZLYJCB.YZLTRZJJ 报国家合计资金（万元）," +
                    "GZLYJCB.YZLTRZJZYTZ 报国家中央资金（万元）,GZLYJCB.YZLTRZJSJTZ 报国家省级投资（万元）,GZLYJCB.YZLTRZJHJ 报省政府合计资金（万元）," +
                    "GZLYJCB.ZFYZLTRZJZYTZ 报省政府中央资金（万元）,GZLYJCB.ZFYZLTRZJSJTZ 报省政府省级投资（万元）,GZLYJCB.YZLZJSLFY 森林抚育合计资金（万元） " +
                    "from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";*/
        }
        return getSqlData(sql);
    }

    /**
     * 生态护林员
     */
    @Override
    public List<Map<String, String>> getSthlyData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.STHLY 合计 from TAB_DQ INNER JOIN GZLYWCQK ON " +
                    "TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.STHLY 合计 from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 种苗生产
     */
    @Override
    public List<Map<String, String>> getZmscData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.ZMSCHJ 合计,GZLYWCQK.ZMSCLGM 裸根苗,GZLYWCQK.ZMSCRQM 容器苗," +
                    "GZLYWCQK.ZMSCLHM 绿化苗 from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.ZMSCHJ 合计,GZLYJCB.ZMSCLGM 裸根苗,GZLYJCB.ZMSCRQM 容器苗,GZLYJCB.ZMSCLHM 绿化苗 from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 林业有害生物
     */
    @Override
    public List<Map<String, String>> getYhswData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.BCHFSMJ 发生面积,GZLYWCQK.BCHFZMJ 防治面积," +
                    "GZLYWCQK.BCHFZJF 防治经费,GZLYWCQK.BCHJYMJ 苗圃检疫面积 from TAB_DQ INNER " +
                    "JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.BCHFZJYFSMJ 发生面积,GZLYJCB.BCHFZJYFZMJ 防治面积," +
                    "GZLYJCB.BCHFZJYFZJF 防治经费,GZLYJCB.BCHFZJYJYMJ 苗圃检疫面积 from TAB_DQ INNER JOIN GZLYJCB " +
                    "ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 获取公园林场统计数据
     * @param locname
     * @param type
     * @return
     */
    @Override
    public List<Map<String, String>> getGyLc(String locname, int type) {
        String tablename="";
        switch (type){
            case 1:
                tablename="ZRBHQ";
                break;
            case 2:
                tablename="SLGY";
                break;
            case 3:
                tablename="SDGY";
                break;
            case 4:
                tablename="GYLC";
                break;
        }
        String sql = "";
        String sql0 = "select id from tab_dq where dqname = '"+locname+"'";
        if(locname.equals("贵州省")){
            sql="select a.pid,a.dqname 地区名称,b.count 个数,b.area 面积（公顷） from (select id,pid,dqname,count(*) " +
                    "count ,sum(AREA) area from (select * from TAB_DQ m inner join "+tablename+" n on n.DQCODE = m.DQCODE) " +
                    "group by pid) as b,TAB_DQ a where a.id = b.pid;";
        }else{
            sql = "select a.dqname 地区名称,count(*) 个数 ,sum(AREA) 面积（公顷）,a.DQCODE from (select * from TAB_DQ m inner " +
                    "join "+tablename+" n on n.DQCODE = m.DQCODE where m.PID=("+sql0+")) a group by a.DQCODE;";
        }

        return getSqlData(sql);
    }

    /**
     * 获取林情数据
     * @param locname
     * @return
     */
    @Override
    public List<Map<String, String>> getGzLq(String locname,int time) {
        final List<Map<String,String>> mapList = new ArrayList<>();
        try {
            //String dapath=mContext.getFilesDir().getPath()+"/db.sqlite";
            //String databaseName = ResourcesManager.getInstance(mContext).getDataBase("db.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new jsqlite.Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
            Callback callback = new Callback() {
                @Override
                public void columns(String[] strings) {
                    columnames=strings;
                }

                @Override
                public void types(String[] strings) {

                }

                @Override
                public boolean newrow(String[] data) {
                    Map<String,String> map=new HashMap();
                    map.put("DQNAME",data[0]);
                    //国土面积
                    String s = data[1] != null ? map.put("GTMJ", data[1]) : map.put("GTMJ", "0");
                    //林地面积
                    String s1 = data[2] != null ? map.put("LDMJ", data[2]) : map.put("LDMJ", "0");
                    //森林面积
                    String s2 = data[3] != null ? map.put("SLMJ", data[3]) : map.put("SLMJ", "0");
                    //蓄积
                    String s3 = data[4] != null ? map.put("SLXJ", data[4]) : map.put("SLXJ", "0");
                    //森林覆盖率
                    String s4 = data[5] != null ? map.put("SLFGL", data[5]) : map.put("SLFGL", "0");
                    //生态红线
                    String s5 = data[6] != null ? map.put("STHX", data[6]) : map.put("STHX", "0");
                    //林业工作站
                    String s6 = data[7] != null ? map.put("LYGZZ", data[7]) : map.put("LYGZZ", "0");
                    mapList.add(map);
                    return false;
                }
            };
            String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + locname + "'";
            String sql = "";
            if(time == R.string.time1){
                sql = "select GZLYJCB.DQNAME 地区名称,GTMJ 国土面积（万亩）,LDMJ 林地面积（万亩）,SLMJ 森林面积（万亩）," +
                        "SLXJ 森林蓄积（万立方米）,SLFGL 森林覆盖率（8）,STHX 生态红线（万亩）,LYGZZ 林业工作站（个） from  " +
                        "TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                        "("+sql1+") or TAB_DQ.ID = ("+ sql1 +") order by GZLYJCB.DQCODE;";
            }else {
                sql = "select GZLYJCB.DQNAME 地区名称,GTMJ 国土面积（万亩）,LDMJ 林地面积（万亩）,SLMJ 森林面积（万亩）," +
                        "SLXJ 森林蓄积（万立方米）,SLFGL 森林覆盖率（8）,STHX 生态红线（万亩）,LYGZZ 林业工作站（个） from  " +
                        "TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                        "("+sql1+") or TAB_DQ.ID = ("+ sql1 +") order by GZLYJCB.DQCODE;";
            }
            db.exec(sql,callback);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return mapList;
        }
        return mapList;
    }


    /**
     * 获取林场公园统计数据
     * @param locname
     * @return
     */
    @Override
    public List<Map<String, String>> getGyLcData(String locname,int type) {
         final List<Map<String,String>> mapList = new ArrayList<>();
        String typename="ZRBHQ";
        switch (type){
            case 1:
                typename="ZRBHQ";
                break;
            case 2:
                typename="SLGY";
                break;
            case 3:
                typename="SDGY";
                break;
            case 4:
                typename="GYLC";
                break;
        }

        try {
            //String dapath=mContext.getFilesDir().getPath()+ "/db.sqlite";
            //String databaseName = ResourcesManager.getInstance(mContext).getDataBase("db.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new jsqlite.Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
            Callback callback = new Callback() {
                @Override
                public void columns(String[] strings) {
                    columnames=strings;
                }

                @Override
                public void types(String[] strings) {

                }

                @Override
                public boolean newrow(String[] data) {
                    Map<String,String> map=new HashMap();

                    map.put("DQNAME",data[0]);
                    //单位名称
                    String s = data[1] != null ? map.put("NAME", data[1]) : map.put("NAME", " ");
                    //等级
                    String s1 = data[2] != null ? map.put("RANK", data[2]) : map.put("RANK", " ");
                    //面积
                    String s2 = data[3] != null ? map.put("AREA", data[3]) : map.put("AREA", " ");
                    //主管部门
                    String s3 = data[4] != null ? map.put("DEPARTMENT", data[4]) : map.put("DEPARTMENT", " ");
                    //经度
                    String s4 = data[5] != null ? map.put("LON", data[5]) : map.put("LON", " ");
                    //纬度
                    String s5 = data[6] != null ? map.put("LAT", data[6]) : map.put("LAT", " ");
                    mapList.add(map);
                    return false;
                }
            };
            String sql="";
            if(locname.equals("贵州省")){
               sql = "select t.DQNAME 地区名称,t.NAME 名称,t.RANKCODE 等级,t.AREA 面积,t.DEPARTMENT 主管部门,t.LON 经度,t.LAT 纬度 from "+typename+" t";
            }else {
                sql = "select t.DQNAME 地区名称,t.NAME 名称,t.RANKCODE 等级,t.AREA 面积,t.DEPARTMENT 主管部门,t.LON 经度,t.LAT 纬度 from "+typename+" t inner join tab_dq on t.DQCODE = tab_dq.DQCODE where tab_dq.PID = (select id from tab_dq where dqname='"+locname+"');";
                //sql = "select t.DQNAME 地区名称,t.NAME 名称,t.RANK 等级,t.AREA 面积,t.DEPARTMENT 主管部门,t.LON 经度,t.LAT 纬度 from "+typename+" t where t.DQNAME="+locname+"";
            }
            db.exec(sql,callback);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mapList;
    }



    /**
     * 林木采伐
     */
    @Override
    public List<Map<String, String>> getLmcfxeData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.SLCF 采伐量,GZLYWCQK.CFXE 采伐限额 from TAB_DQ INNER JOIN GZLYWCQK " +
                    "ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.NSLCFL 采伐量,GZLYJCB.NSLCFXE 采伐限额 from TAB_DQ INNER " +
                    "JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 退耕还林
     */
    @Override
    public List<Map<String, String>> getTghlData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.TGHLMJ 退耕地 from TAB_DQ INNER JOIN GZLYWCQK " +
                    "ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.KTGDZYHJ 可退耕地,GZLYJCB.KTGDZYESW 可退耕地25°以上," +
                    "GZLYJCB.KTGDZYSW 可退耕地15～25°,GZLYJCB.TGHLMJJH 退耕地,GZLYJCB.NDDDWC 年底调度完成, GZLYJCB.NDDDWCL 年底调度完成率 from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }

        return getSqlData(sql);
    }

    /**
     * 保护区公园
     */
    @Override
    public List<Map<String, String>> getBhqgyData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "select GZBHQGY.DQNAME 地区名称,GZBHQGY.KTGDZY 可退耕地,GZBHQGY.TGHL 退耕地 from TAB_DQ INNER JOIN GZBHQGY ON TAB_DQ.DQCODE = GZBHQGY.DQCODE where TAB_DQ.PID = " +
                "(" + sql1 + ") or TAB_DQ.ID =(" + sql1 + "') order by GZBHQGY.DQCODE;";
        return getSqlData(sql);
    }

    /**
     * 林权改革
     */
    @Override
    public List<Map<String, String>> getLqggData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.LQLGMJ 林改面积,GZLYWCQK.LQSJNH 涉及农户,GZLYWCQK.LQSJRK 涉及人口," +
                    "GZLYWCQK.LQSJSLBX 森林保险,GZLYWCQK.LQDY 林权抵押,GZLYWCQK.LQLXZZ 林下种植" +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else {
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.LGMJ 林改面积,GZLYJCB.LGNH 涉及农户,GZLYJCB.LGRK 涉及人口," +
                    "GZLYJCB.LGSLBX 森林保险,GZLYJCB.LGLQDY 林权抵押,GZLYJCB.LGLXZZ 林下种植" +
                    " from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 石漠化
     */
    @Override
    public List<Map<String, String>> getShmhData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.SMHZLMJ 石漠化治理面积" +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.SMHMJ 石漠化面积,GZLYJCB.SMHZLMJ 石漠化治理面积" +
                    " from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 林业产业
     */
    @Override
    public List<Map<String, String>> getLycyData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.LYCYCZHJ 合计,GZLYWCQK.LYCYCZDYCY 第一产业," +
                    "GZLYWCQK.LYCYCZDECY 第二产业,GZLYWCQK.LYCYCZDSCY 第三产业" +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else {
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.LYCYCZHJ 合计,GZLYJCB.LYCYCZDYCY 第一产业,GZLYJCB.LYCYCZDECY 第二产业,GZLYJCB.LYCYCZDSCY 第三产业" +
                    " from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }

        return getSqlData(sql);
    }

    /**
     * 管护人员
     */
    @Override
    public List<Map<String, String>> getGhryData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称 " +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.GHRYHJ 合计,GZLYJCB.GHRYGYL 公益林,GZLYJCB.GHRYTBGC 天保工程,GZLYJCB.GHRYTGHL 退耕还林,GZLYJCB.GHRYHLFH 护林防火,GZLYJCB.GHRYYZL 营造林" +
                    " from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 公益林
     */
    public List<Map<String, String>> getgylData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.GYLBCZJHJ 公益林资金,GZLYWCQK.GYLBCZJGJJ 国家补偿资金," +
                    "GZLYWCQK.GYLBCZJDF 地方补偿资金" +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.GYLMJHJ 公益林面积,GZLYJCB.GYLMJGJ 国家级公益林面积," +
                    "GZLYJCB.GYLMJDF 地方公益林面积,GZLYJCB.GYLBCZJHJ 公益林资金,GZLYJCB.GYLBCZJGJ 国家补偿资金," +
                    "GZLYJCB.GYLBCZJDF 地方补偿资金" +
                    " from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }

    /**
     * 森林防火
     */
    public List<Map<String, String>> getslfhData(String name,int time) {
        String sql1 = "select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='" + name + "'";
        String sql = "";
        if(time == R.string.time1){
            sql = "select GZLYWCQK.DQNAME 地区名称,GZLYWCQK.SLFHHZCS 火灾次数,GZLYWCQK.SLFHHZMJ 火灾面积," +
                    "GZLYWCQK.SLFHSSMJ 损失面积,GZLYWCQK.SLFHJFTR 经费投入" +
                    " from TAB_DQ INNER JOIN GZLYWCQK ON TAB_DQ.DQCODE = GZLYWCQK.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYWCQK.DQCODE;";
        }else{
            sql = "select GZLYJCB.DQNAME 地区名称,GZLYJCB.SLFHHZCS 火灾次数,GZLYJCB.SLFHHZMJ 火灾面积,GZLYJCB.SLFHSSMJ 损失面积,GZLYJCB.SLFHJFTR 经费投入" +
                    " from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                    "(" + sql1 + ") or TAB_DQ.ID = (" + sql1 + ") order by GZLYJCB.DQCODE;";
        }
        return getSqlData(sql);
    }




    /*公共部分提取*/
    public List<Map<String, String>> getSqlData(String sql) {
        final List<Map<String, String>> list = new ArrayList<>();
        try {
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new jsqlite.Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
            Callback callback = new Callback() {
                @Override
                public void columns(String[] strings) {
                    colums = strings;
                }

                @Override
                public void types(String[] strings) {

                }

                @Override
                public boolean newrow(String[] data) {
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] != null) {
                            map.put(colums[i], data[i]);
                        } else {
                            map.put(colums[i], "");
                        }
                    }
                    list.add(map);
                    return false;
                }
            };
            db.exec(sql, callback);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }


}
