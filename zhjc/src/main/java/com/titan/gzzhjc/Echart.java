package com.titan.gzzhjc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.github.abel533.echarts.DataZoom;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.AxisLine;
import com.github.abel533.echarts.axis.AxisTick;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.DataZoomType;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Symbol;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.MarkPoint;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.TextStyle;
import com.titan.gzzhjc.impl.DbdataImpl;
import com.titan.gzzhjc.impl.OptionImpl;
import com.titan.gzzhjc.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by whs on 2017/3/2
 *
 * 地图展示
 */

public class Echart  {

    Handler handler;
    List<Map<String,String>> list = new ArrayList<>();
    List<Map<String,String>> pointlist=new ArrayList<>();
    Context context;
    @JavascriptInterface
    public String getType() {
        return type;
    }
    @JavascriptInterface
    public void setType(String type) {
        this.type = type;
    }

    String type;
    @JavascriptInterface
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    int time;

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    int formType;

    public String getName() {
        return name;
    }
    @JavascriptInterface
    public void setName(String name) {
        this.name = name;
    }

    String name;
    public Echart(Context context,String name,String sqltype,Handler handler1) {
        this.context = context;
        this.type = sqltype;
        this.name = name;
        this.handler = handler1;
    }

    public String getYwType() {
        return ywType;
    }

    public void setYwType(String ywType) {
        this.ywType = ywType;
    }
    //数据类型
    String ywType;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    String company;

    public Echart() {

    }

    //国土地图
    @JavascriptInterface
    public String getLandMapOption(String seldqname,String uptype,int uptime){
        List<Data> datas = new ArrayList<>();
        List<Data> datas1 = new ArrayList<>();
        double max = 0;
        String value="0";
        list = getSqlData(seldqname,uptype,uptime);
        if(list==null){
            return "";
        }

        for(Map<String,String> map : list){
            if(getFormType() != 0){
                break;
            }

            String dqname = map.get(context.getResources().getString(R.string.dqname));

            if(dqname.equals(seldqname) || dqname.equals("0")){
                continue;
            }
            if(type.equals(context.getResources().getString(R.string.zrbhq))) {
                String dvalue = map.get("个数");
                value = map.get("面积（公顷）");
                Data data = new Data(dqname.trim(),dvalue.trim());
                Tooltip tooltip=new Tooltip();
                tooltip.setTrigger(Trigger.item);
                tooltip.formatter("{b}:{c}"+"公顷");
                data.setTooltip(tooltip);
                datas1.add(data);
            } else {
                value = map.get(ywType);
            }

            if(value == null || value.equals("")){
                value = "0";
            }

            double vv = Double.parseDouble(Util.round(value));
            Data data = new Data(dqname,vv);
            if(value != null){
                if(vv > max){
                    max = (int) Math.ceil(vv);
                }
            }
            datas.add(data);
        }

        GsonOption mapoption=new GsonOption();
        String danwei = "";
        if(type.equals(context.getResources().getString(R.string.lmcf))){
            danwei = "万立方米";
        }else if(type.equals(context.getResources().getString(R.string.shmh))){
            danwei = "万亩";
        }else if(type.equals(context.getResources().getString(R.string.tghl))){
            danwei = "万亩";
        }else if(ywType.equals(context.getResources().getString(R.string.gylmj))){
            danwei = "万亩";
        }else if(ywType.equals(context.getResources().getString(R.string.gylzj))){
            danwei = "万元";
        }else if(ywType.equals(context.getResources().getString(R.string.hzcs))){
            danwei = "次";
        }else if(ywType.equals(context.getResources().getString(R.string.hzmj))){
            danwei = "公顷";
        }else if(ywType.equals(context.getResources().getString(R.string.ssmj))){
            danwei = "公顷";
        }else if(ywType.equals(context.getResources().getString(R.string.jftr))){
            danwei = "万元";
        }else if(ywType.equals(context.getResources().getString(R.string.yzl_mj))){
            danwei = "万亩";
        }else if(ywType.equals(context.getResources().getString(R.string.yzl_zj))){
            danwei = "万元";
        }else if(type.equals(context.getResources().getString(R.string.sthly))){
            danwei = "人";
        }else if(type.equals(context.getResources().getString(R.string.zmsc))){
            danwei = "万株";
        }else if(type.equals(context.getResources().getString(R.string.yhsw))){
            if(ywType.contains("面积")){
                danwei = "万亩";
            }else{
                danwei = "万元";
            }
        }else if(type.equals(context.getResources().getString(R.string.ghry))){
            danwei = "人";
        }else if(type.equals(context.getResources().getString(R.string.lycy))){
            danwei = "万元";
        }else if(type.equals(context.getResources().getString(R.string.lqgg))){
            if(ywType.equals(context.getResources().getString(R.string.sjnh))){
                danwei = "万户";
            }else if(ywType.equals(context.getResources().getString(R.string.sjrk))){
                danwei = "万人";
            }else{
                danwei = "万亩";
            }
        }else if(type.equals(context.getResources().getString(R.string.zrbhq))){
            danwei = "公顷";
        }else if(type.equals(context.getResources().getString(R.string.lyjc))){
            if(ywType.equals(context.getResources().getString(R.string.xuji))){
                danwei = "万立方米";
            }else if(ywType.equals(context.getResources().getString(R.string.slfgl))){
                danwei = "%";
            }else if(ywType.equals(context.getResources().getString(R.string.lygzz))){
                danwei = "个";
            }else{
                danwei = "万亩";
            }
        }
        Tooltip tooltip=new Tooltip();
        tooltip.setTrigger(Trigger.item);
        tooltip.formatter("{b}:{c}"+danwei);
        mapoption.setTooltip(tooltip);
        //mapoption.setTooltip(new Tooltip().trigger(Trigger.item).formatter("{b}:{c}"+danwei));
        //对比图
        VisualMap visualMap=new VisualMap();
        visualMap.setMin(0);
        visualMap.setMax((int)Math.ceil(max));
        visualMap.setLeft("left");
        visualMap.setTop("bottom");
        visualMap.setText(new String[]{"高","低"});
        visualMap.setCalculable(true);
        List<VisualMap> visualMapList=new ArrayList<>();
        visualMapList.add(visualMap);
        Object[] colors = getVisualMapColorArr(type);
        visualMap.setColor(colors);
        mapoption.setVisualMap(visualMapList);

        OptionImpl optionImpl = new OptionImpl();
        List<Series> seriesList = new ArrayList<>();
        Series series = optionImpl.getMapSeries(seldqname,datas);
        if(type.equals(context.getResources().getString(R.string.zrbhq))){
            Series series1 = optionImpl.getMapSeries(seldqname,datas1);
            if(type.equals(context.getResources().getString(R.string.zrbhq))){
                if(ywType.equals(context.getResources().getString(R.string.gyzrbhq))){
                    series1.setMarkPoint(getPointSeries(seldqname,1,mapoption));
                } else if(ywType.equals(context.getResources().getString(R.string.slgy))){
                    series1.setMarkPoint(getPointSeries(seldqname,2,mapoption));
                }else if(ywType.equals(context.getResources().getString(R.string.sdgy))){
                    series1.setMarkPoint(getPointSeries(seldqname,3,mapoption));
                }else if(ywType.equals(context.getResources().getString(R.string.gylc))){
                    series1.setMarkPoint(getPointSeries(seldqname,4,mapoption));
                }
            }
            seriesList.add(series1);
        }
        seriesList.add(series);
        mapoption.setSeries(seriesList);
        return mapoption.toString();
    }

    /**
     * 获取点状数据
     * @return
     */
    public MarkPoint getPointSeries(String dqname,int sqltype,GsonOption mapoption){

        MarkPoint markPoint=new MarkPoint();
        markPoint.itemStyle().normal().borderColor("#87cefa").borderWidth(1).label().show(false);
        mapoption.setTooltip(new Tooltip().formatter("{b}"));
        List<Data> datas=new ArrayList<>();
        pointlist=DbdataImpl.getInstance(context).getGyLcData(dqname,sqltype);
        for (Map<String,String> map:pointlist){
            EchartData data=new EchartData();
            String recode = map.get("RANK");
            String imgurl =getSymbol(recode);
            String lon = map.get("LON");
            String lat = map.get("LAT");
            data.setCoord(new String[]{lon,lat});
            String name = map.get("NAME");
            data.name(name);
            if(imgurl.equals("")){
                data.setSymbol(Symbol.circle);
                data.setSymbolSize(10);
            }else{
                data.setSymbol(imgurl);
                data.setSymbolSize(new int[]{12,22});
            }
            datas.add(data);
        }

        markPoint.setData(datas);

        return markPoint;
    }

    public String getSymbol(String recode){
        List<String> list = null;
        String imgUrl = "";
        if(recode == null || recode.trim().equals("")){
            return  imgUrl;
        }
        int code = Integer.parseInt(Util.rounding(recode.trim()));
        if(ywType.equals(context.getResources().getString(R.string.gyzrbhq))){
            list = new ArrayList();
            if(code == 1){
                imgUrl = "image://file:///android_res/drawable/zrbhq_one.jpg";
                list.add(imgUrl);
            }else if(code == 2){
                imgUrl = "image://file:///android_res/drawable/zrbhq_two.jpg";
                list.add(imgUrl);
            }else if(code == 3){
                imgUrl = "image://file:///android_res/drawable/zrbhq_three.jpg";
                list.add(imgUrl);
            }else if(code == 4){
                imgUrl = "image://file:///android_res/drawable/zrbhq_four.jpg";
                list.add(imgUrl);
            }
        }else if(ywType.equals(context.getResources().getString(R.string.slgy))){
            list = new ArrayList();
            if(code == 1){
                imgUrl = "image://file:///android_res/drawable/slgy_one.jpg";
                list.add(imgUrl);
            }else if(code == 2){
                imgUrl = "image://file:///android_res/drawable/slgy_two.jpg";
                list.add(imgUrl);
            }else if(code == 3){
                imgUrl = "image://file:///android_res/drawable/slgy_three.jpg";
                list.add(imgUrl);
            }else if(code == 4){
                imgUrl = "image://file:///android_res/drawable/slgy_four.jpg";
                list.add(imgUrl);
            }
        }else if(ywType.equals(context.getResources().getString(R.string.sdgy))){
            list = new ArrayList();
            if(code == 0){
                imgUrl = "image://file:///android_res/drawable/shdgy.jpg";
                list.add(imgUrl);
            }else if(code == 1){
                imgUrl = "image://file:///android_res/drawable/sdgy_two.jpg";
                list.add(imgUrl);
            }

        }else if(ywType.equals(context.getResources().getString(R.string.gylc))){
            list = new ArrayList();
            if(code == 1){
                imgUrl = "image://file:///android_res/drawable/gylc_one.jpg";
                list.add(imgUrl);
            }else if(code == 2){
                imgUrl = "image://file:///android_res/drawable/gylc_two.jpg";
                list.add(imgUrl);
            }else if(code == 3){
                imgUrl = "image://file:///android_res/drawable/gylc_three.jpg";
                list.add(imgUrl);
            }else if(code == 4){
                imgUrl = "image://file:///android_res/drawable/gylc_four.jpg";
                list.add(imgUrl);
            }else if(code == 5){
                imgUrl = "image://file:///android_res/drawable/gylc_five.jpg";
                list.add(imgUrl);
            }
        }
        return  imgUrl;
    }

    //国土表格
    @JavascriptInterface
    public String getLandChartOption(){

        GsonOption statisticoption =new GsonOption();
        statisticoption.setTitle(new Title().text("国土面积统计图"));
        statisticoption.setTooltip(new Tooltip().trigger(Trigger.axis));

        CategoryAxis categoryAxis=new CategoryAxis().boundaryGap(false).data("贵阳市","六盘水市");
        statisticoption.xAxis(categoryAxis);

        ValueAxis valueAxis=new ValueAxis();
        statisticoption.yAxis(valueAxis);

        Bar bar =new Bar("国土面积").data(23,43,34,65,54,45);
        statisticoption.series(bar);


        return statisticoption.toString();
    }

    @JavascriptInterface
    public String getBarOption(String name){
        GsonOption baroption=new GsonOption();

        baroption.setTitle(new Title().text("柱状图"));
        baroption.setTooltip(new Tooltip().trigger(Trigger.axis));
        Axis axis = new CategoryAxis();
        List<String> data = new ArrayList<>();
        data.add("安顺市");
        data.add("铜仁市");
        axis.setData(data);

        AxisLabel axisLabel = new AxisLabel();
        TextStyle textStyle = new TextStyle();
        textStyle.setColor("red");
        axisLabel.setTextStyle(textStyle);
        axisLabel.setShow(true);
        axisLabel.setRotate(40);
        axisLabel.setInterval(0);
        ValueAxis valueAxis = new ValueAxis();
        valueAxis.setAxisLabel(axisLabel);
        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.setShow(true);
        AxisLine axisLine = new AxisLine();
        axisLine.setShow(false);
        AxisTick axisTick = new AxisTick();
        axisTick.setShow(false);
        categoryAxis.setAxisLine(axisLine);
        categoryAxis.setAxisTick(axisTick);

        List<Axis> axises = new ArrayList<>();
        axises.add(valueAxis);
        baroption.xAxis(axis);
        DataZoom dataZoom = new DataZoom();
        DataZoomType zoomType = DataZoomType.inside;
        dataZoom.setType(zoomType);
        List<DataZoom> zooms = new ArrayList<>();
        zooms.add(dataZoom);
        baroption.setDataZoom(zooms);
        baroption.setxAxis(axises);

        return baroption.toString();
    }


    @JavascriptInterface
    public String setGsonOption() {
        GsonOption option = new GsonOption();
        option.legend("高度(km)与气温(°C)变化关系");

        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar), Tool.restore, Tool.saveAsImage);

        option.calculable(true);
        option.tooltip().trigger(Trigger.axis).formatter("Temperature : <br/>{b}km : {c}°C");

        ValueAxis valueAxis = new ValueAxis();
        valueAxis.axisLabel().formatter("{value} °C");
        option.xAxis(valueAxis);

        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.axisLine().onZero(false);
        categoryAxis.axisLabel().formatter("{value} km");
        categoryAxis.boundaryGap(false);
        categoryAxis.data(0, 10, 20, 30, 40, 50, 60, 70, 80);
        option.yAxis(categoryAxis);

        Line line = new Line();
        line.smooth(true).name("高度(km)与气温(°C)变化关系").data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
        option.series(line);
        return option.toString();
    }

    /*查询2016年数据*/
    public List<Map<String,String>> getSqlData(String dqname,String uptype,int uptime){

        if(uptype.equals("undefined") && list.size() > 0 && uptype.equals(type)  && dqname.equals(name) && uptime==time){
            return list;
        }

//        if(!type.equals(context.getResources().getString(R.string.zrbhq))){
//            if(uptype.equals("undefined") && list.size() > 0 && dqname.equals(name) && uptime==time){
//                return list;
//            }
//            if(list.size() > 0 && uptype.equals(type) && dqname.equals(name) && uptime==time) {
//                return list;
//            }
//        }

        Message msg = new Message();
        DbdataImpl dbImpl = new DbdataImpl(context);
        if(type.equals(context.getResources().getString(R.string.lyjc))){
            String selfield = "";
            if(ywType.equals(context.getResources().getString(R.string.gtmj))){
                selfield = "GTMJ 国土面积 ";
            }else if(ywType.equals(context.getResources().getString(R.string.ldmj))){
                selfield = "LDMJ 林地面积 ";
            }else if(ywType.equals(context.getResources().getString(R.string.slmj))){
                selfield = "SLMJ 森林面积 ";
            }else if(ywType.equals(context.getResources().getString(R.string.xuji))){
                selfield = "SLXJ 蓄积 ";
            }else if(ywType.equals(context.getResources().getString(R.string.slfgl))){
                selfield = "SLFGL 森林覆盖率 ";
            }else if(ywType.equals(context.getResources().getString(R.string.sthx))){
                selfield = "STHX 生态红线 ";
            }else if(ywType.equals(context.getResources().getString(R.string.lygzz))){
                selfield = "LYGZZ 林业工作站 ";
            }
            //String selfield = "GTMJ 国土面积,LDMJ 林地面积,SLMJ 森林面积,SLXJ 蓄积,SLFGL 森林覆盖率,STHX 生态红线,LYGZZ 林业工作站";
            list = dbImpl.getLyjcData(dqname,time,selfield,getFormType());
            msg.what = R.string.lyjc;
        }if(type.equals(context.getResources().getString(R.string.lmcf))){
            list = dbImpl.getLmcfxeData(dqname,time);
            msg.what = R.string.lmcf;
        }else if(type.equals(context.getResources().getString(R.string.tghl))){
            list = dbImpl.getTghlData(dqname,time);
            msg.what = R.string.tghl;
        }else if(type.equals(context.getResources().getString(R.string.lqgg))){
            list = dbImpl.getLqggData(dqname,time);
            msg.what = R.string.lqgg;
        }else if(type.equals(context.getResources().getString(R.string.gyl))){
            list = dbImpl.getgylData(dqname,time);
            msg.what = R.string.gyl;
        }else if(type.equals(context.getResources().getString(R.string.slfh))){
            list = dbImpl.getslfhData(dqname,time);
            msg.what = R.string.slfh;
        }else if(type.equals(context.getResources().getString(R.string.shmh))){
            list = dbImpl.getShmhData(dqname,time);
            msg.what = R.string.shmh;
        }else if(type.equals(context.getResources().getString(R.string.lycy))){
            list = dbImpl.getLycyData(dqname,time);
            msg.what = R.string.lycy;
        }else if(type.equals(context.getResources().getString(R.string.ghry))){
            list = dbImpl.getGhryData(dqname,time);
            msg.what = R.string.ghry;
        }else if(type.equals(context.getResources().getString(R.string.yhsw))){
            list = dbImpl.getYhswData(dqname,time);
            msg.what = R.string.yhsw;
        }else if(type.equals(context.getResources().getString(R.string.zmsc))){
            list = dbImpl.getZmscData(dqname,time);
            msg.what = R.string.zmsc;
        }else if(type.equals(context.getResources().getString(R.string.sthly))){
            list = dbImpl.getSthlyData(dqname,time);
            msg.what = R.string.sthly;
        }else if(type.equals(context.getResources().getString(R.string.yzl))){
            String selfield = "";
            if(ywType.equals(context.getResources().getString(R.string.yzl_mj)) && (time == R.string.time1)){
                selfield = "YZLGJMJHJ 报国家合计面积,GJRGZL 报国家人工造林面积,GJFSYL 报国家封山育林面积," +
                        "YZLSZFHJ 报省政府合计面积,YZLRGZL 报省政府人工造林面积,YZLFSYL 报省政府封山育林面积,YZLMJSLFY 封山育林总面积 ";
            }else if(ywType.equals(context.getResources().getString(R.string.yzl_zj)) && (time == R.string.time1)){
                selfield = "YZLTRZJGJHJ 报国家合计资金,YZLTRZJGJZY 报国家中央资金,YZLTRZJGJSJ 报国家省级投资," +
                        "YZLTRZFHJ 报省政府合计资金,YZLTRZFZY 报省政府中央资金,YZLTRZFSJ 报省政府省级投资,YZLZJSLFY 森林抚育合计资金 ";
            }else if(ywType.equals(context.getResources().getString(R.string.yzl_mj)) && (time == R.string.time2)){
                selfield = "YZLMJHJ 报国家合计面积,YZLMJRGZL 报国家人工造林面积,YZLMJFSYL 报国家封山育林面积," +
                        "YZLZJHJ 报省政府合计面积,YZLZJRGZL 报省政府人工造林面积,YZLZJFSYL 报省政府封山育林面积," +
                        "YZLMJSLFY 森林抚育总面积 ";
            }else if(ywType.equals(context.getResources().getString(R.string.yzl_zj)) && (time == R.string.time2)){
                selfield ="YZLTRZJJ 报国家合计资金,YZLTRZJZYTZ 报国家中央资金,YZLTRZJSJTZ 报国家省级投资,YZLTRZJHJ 报省政府合计资金," +
                        "ZFYZLTRZJZYTZ 报省政府中央资金,ZFYZLTRZJSJTZ 报省政府省级投资,YZLZJSLFY 森林抚育合计资金";
            }

            list = dbImpl.getYzlData(dqname,time,selfield);
            msg.what = R.string.yzl;
        }else if(type.equals(context.getResources().getString(R.string.zrbhq))){
           if(ywType.equals(context.getResources().getString(R.string.gyzrbhq))){
               list = dbImpl.getGyLc(dqname,1);
           } else if(ywType.equals(context.getResources().getString(R.string.slgy))){
               list=dbImpl.getGyLc(dqname,2);
           }else if(ywType.equals(context.getResources().getString(R.string.sdgy))){
               list=dbImpl.getGyLc(dqname,3);
           }else if(ywType.equals(context.getResources().getString(R.string.gylc))){
                  list=dbImpl.getGyLc(dqname,4)   ;
           }
           msg.what=R.string.zrbhq;
        }

        msg.obj = list;
        handler.sendMessage(msg);
        setName(dqname);
        return list;
    }

    /*地图配色*/
    public String[] getVisualMapColorArr(String type){

        if (type.equals(context.getResources().getString(R.string.lyjc))){
            return new String[]{"#58C13E", "#99FF81", "#8FCD95", "#D6D6AA", "#BEBDC0"};
        } else if (type.equals(context.getResources().getString(R.string.lmcf))) {
            return new String[]{"#3EA3C1", "#81E1FF", "#8FADCD", "#CDE3F9"};
        } else if (type.equals(context.getResources().getString(R.string.tghl))) {
            return new String[]{"#FCA905", "#FFD883", "#FFF95D", "#FFFCC7"};
        } else if (type.equals(context.getResources().getString(R.string.shmh))) {
            return new String[]{"#FCA905", "#FFD883", "#FFF95D", "#FFFCC7"};
        } else if(type.equals(context.getResources().getString(R.string.slfh))){
            return new String[]{"#AE0E3D", "#FF5D60", "#FF81A6", "#FFC7CA"};
        }else if(type.equals(context.getResources().getString(R.string.zmsc))){
            return new String[]{"#13b228", "#5cd341", "#77e060","#9aea88", "#baf2ae"};
        }else if(type.equals(context.getResources().getString(R.string.yhsw))){
            return new String[]{"#08E734", "#08E734", "#ADFCBD","#ADFCBD", "#CEFDD7"};
        }else if(type.equals(context.getResources().getString(R.string.lycy))){
            return new String[]{"#58C13E", "#D6D6AA", "#8FCD95","#BEBDC0", "#99FF81"};
        }else if(type.equals(context.getResources().getString(R.string.yzl))){
            if(ywType.equals(context.getResources().getString(R.string.yzl_zj))){
                return new String[]{"#009ad6", "#145b7d", "#7bbfea","#228fbd", "#33a3dc"};
            }else{
                return new String[]{"#cde6c7", "#78a355", "#7fb80e","#b2d235", "#1d953f"};
            }
        }else{
            //其他资源配色
            return new String[]{"#58C13E", "#99FF81", "#8FCD95", "#D6D6AA", "#BEBDC0"};
        }
    }

}
