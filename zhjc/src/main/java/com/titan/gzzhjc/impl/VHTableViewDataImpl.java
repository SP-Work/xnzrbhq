package com.titan.gzzhjc.impl;

import android.content.Context;

import com.titan.gzzhjc.R;
import com.titan.gzzhjc.dao.IVHTableViewDataDao;
import com.titan.gzzhjc.util.Util;
import com.titan.vhtableview.VHTableAdapter;
import com.titan.vhtableview.VHTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/3/8
 *
 * 表格数据加载
 */

public class VHTableViewDataImpl implements IVHTableViewDataDao {

    private static VHTableViewDataImpl instance;
    private static Context mContext;

    public static VHTableViewDataImpl getInstence(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new VHTableViewDataImpl();
        }
        return instance;
    }

    /** 林业基础 */
    @Override
    public void initLyjcTabView(VHTableView tableView, List<Map<String, String>> datas,String company,String table_title,String sqlkey) {
        /** 表格第一列 */
        ArrayList<String> title=new ArrayList<>();
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        title.add("    "+table_title+"    ");
        ArrayList<String> row=new ArrayList<>();
        String[] colums = DbdataImpl.colums;
        if(colums == null){
            return;
        }
        /** 表格每行数据 */
        row.add(sqlkey+"\n "+company);
        for (Map<String, String> map :datas) {
            String title_name = map.get(colums[0]);
            if(title_name.equals(table_title.trim())){
                title_name = mContext.getResources().getString(R.string.sthlyhj);
            }
            String title_value = "0";
            if(sqlkey.contains("覆盖率")){
                title_value = Util.round(map.get(colums[1]));
            }else if(sqlkey.contains("林业工作站")){
                title_value = Util.rounding(map.get(colums[1]));
            }else{
                title_value = Util.round(map.get(colums[1]));
            }
            if(title_name.length() < title_value.length()){
                title_name = "    "+title_name+"    ";
            }
            title.add(title_name);
            row.add(title_value);
        }
        dataList.add(row);

        /** 设置适配器 */
        VHTableAdapter tableAdapter1=new VHTableAdapter(mContext,title,dataList);
        tableView.setAdapter(tableAdapter1);
    }

    /** 营造林 */
    @Override
    public void initYzlTabView(VHTableView tableView, List<Map<String, String>> datas,String company,String sqlkey) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        titleData.add("                    "+lbmc+"                   ");
        ArrayList<String> row =new ArrayList<>();
        ArrayList<String> row1 =new ArrayList<>();
        ArrayList<String> row2 =new ArrayList<>();
        ArrayList<String> row3 =new ArrayList<>();
        ArrayList<String> row4 =new ArrayList<>();
        ArrayList<String> row5 =new ArrayList<>();
        ArrayList<String> row6 =new ArrayList<>();
        row.add(sqlkey +company);
        String[] colums = DbdataImpl.colums;
        if(colums == null){
            return;
        }
        if(colums[2] != null){
            row1.add(colums[2]+company);
        }
        if(colums[3] != null){
            row2.add(colums[3]+company);
        }
        if(colums[4] != null){
            row3.add(colums[4]+company);
        }
        if(colums[5] != null){
            row4.add(colums[5]+company);
        }
        if(colums[6] != null){
            row5.add(colums[6]+company);
        }
        if(colums[7] != null){
            row6.add(colums[7]+company);
        }

        /** 表格每行数据 */
        for(Map<String, String> map :datas){
            titleData.add("      "+map.get(colums[0])+"      ");
            row.add(Util.round(map.get(colums[1])));
            row1.add(Util.round(map.get(colums[2])));
            row2.add(Util.round(map.get(colums[3])));
            row3.add(Util.round(map.get(colums[4])));
            row4.add(Util.round(map.get(colums[5])));
            row5.add(Util.round(map.get(colums[6])));
            row6.add(Util.round(map.get(colums[7])));
        }
        dataList.add(row6);
        dataList.add(row);
        dataList.add(row1);
        dataList.add(row2);
        dataList.add(row3);
        dataList.add(row4);
        dataList.add(row5);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 生态护林员 */
    @Override
    public void initSthlyTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add(lbmc);
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.sthlyhj));
        String dqmc = mContext.getResources().getString(R.string.dqname);

        /** 表格每行数据 */
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String sthlyhj = map.get(mContext.getResources().getString(R.string.sthlyhj));

            titleData.add("       " + dqname + "      ");
            list.add(Util.rounding(sthlyhj));
        }
        dataList.add(list);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 种苗生产 */
    @Override
    public void initZmscTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("        "+lbmc+"       ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.zmschj)+"（万株）");
        list1.add(mContext.getResources().getString(R.string.lgm)+"（万株）");
        list2.add(mContext.getResources().getString(R.string.rqm)+"（万株）");
        list3.add(mContext.getResources().getString(R.string.lhm)+"（万株）");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String zmschj = map.get(mContext.getResources().getString(R.string.zmschj));
            String lgm = map.get(mContext.getResources().getString(R.string.lgm));
            String rqm = map.get(mContext.getResources().getString(R.string.rqm));
            String lhm = map.get(mContext.getResources().getString(R.string.lhm));

            titleData.add("       " + dqname + "      ");
            list.add(Util.round(zmschj));
            list1.add(Util.round(lgm));
            list2.add(Util.round(rqm));
            list3.add(Util.round(lhm));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 林业有害生物 */
    @Override
    public void initYhswTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一行 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("            "+lbmc+"             ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.fsmj)+"（万亩）");
        list1.add(mContext.getResources().getString(R.string.fzmj)+"（万亩）");
        list2.add(mContext.getResources().getString(R.string.fzjf)+"（万元）");
        list3.add(mContext.getResources().getString(R.string.jymj)+"（万亩）");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String fsmj = map.get(mContext.getResources().getString(R.string.fsmj));
            String fzmj = map.get(mContext.getResources().getString(R.string.fzmj));
            String fzjf = map.get(mContext.getResources().getString(R.string.fzjf));
            String jymj = map.get(mContext.getResources().getString(R.string.jymj));

            titleData.add("       " + dqname + "      ");
            list.add(Util.round(fsmj));
            list1.add(Util.round(fzmj));
            list2.add(Util.round(fzjf));
            list3.add(Util.round(jymj));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 管护人员 */
    @Override
    public void initGhryTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一行 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("    "+lbmc+"    ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();
        ArrayList<String> list5 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.ghryhj)+"(人)");
        list1.add(mContext.getResources().getString(R.string.ghgyl)+"(人)");
        list2.add(mContext.getResources().getString(R.string.tbgc)+"(人)");
        list3.add(mContext.getResources().getString(R.string.ghtghl)+"(人)");
        list4.add(mContext.getResources().getString(R.string.hlfh)+"(人)");
        list5.add(mContext.getResources().getString(R.string.ghyzl)+"(人)");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String ghryhj = map.get(mContext.getResources().getString(R.string.ghryhj));
            String ghgyl = map.get(mContext.getResources().getString(R.string.ghgyl));
            String tbgc = map.get(mContext.getResources().getString(R.string.tbgc));
            String ghtghl = map.get(mContext.getResources().getString(R.string.ghtghl));
            String hlfh = map.get(mContext.getResources().getString(R.string.hlfh));
            String ghyzl = map.get(mContext.getResources().getString(R.string.ghyzl));

            titleData.add("       " + dqname + "      ");
            list.add(Util.rounding(ghryhj));
            list1.add(Util.rounding(ghgyl));
            list2.add(Util.rounding(tbgc));
            list3.add(Util.rounding(ghtghl));
            list4.add(Util.rounding(hlfh));
            list5.add(Util.rounding(ghyzl));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);
        dataList.add(list4);
        dataList.add(list5);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 林业产业 */
    @Override
    public void initLycyTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一行 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("  "+lbmc+"  ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.lycyhj)+"\n（万元）");
        list1.add(mContext.getResources().getString(R.string.dycy)+"\n（万元）");
        list2.add(mContext.getResources().getString(R.string.decy)+"\n（万元）");
        list3.add(mContext.getResources().getString(R.string.dscy)+"\n（万元）");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String lycyhj = map.get(mContext.getResources().getString(R.string.lycyhj));
            String dycy = map.get(mContext.getResources().getString(R.string.dycy));
            String decy = map.get(mContext.getResources().getString(R.string.decy));
            String dscy = map.get(mContext.getResources().getString(R.string.dscy));

            titleData.add("       " + dqname + "      ");
            list.add(Util.round(lycyhj));
            list1.add(Util.round(dycy));
            list2.add(Util.round(decy));
            list3.add(Util.round(dscy));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 石漠化 */
    @Override
    public void initShmhTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一行 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("           " + lbmc  + "           ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.shmhmj)+"(万亩)");
        list1.add(mContext.getResources().getString(R.string.shmhzlmj)+"(万亩)");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String ktgd = map.get(mContext.getResources().getString(R.string.shmhmj));
            String tgd = map.get(mContext.getResources().getString(R.string.shmhzlmj));

            titleData.add("       " + dqname + "      ");
            if(ktgd != null){
                list.add(Util.round(ktgd));
            }else{
                list.add("0");
            }
            if(tgd != null){
                list1.add(Util.round(tgd));
            }else{
                list1.add("0");
            }
        }
        dataList.add(list);
        dataList.add(list1);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 林权改革 */
    @Override
    public void initLqggTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一行 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("           " +lbmc+"          ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();
        ArrayList<String> list5 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.lgmj)+"（万亩）");
        list1.add(mContext.getResources().getString(R.string.sjnh)+"（万户）");
        list2.add(mContext.getResources().getString(R.string.sjrk)+"（万人）");
        list3.add(mContext.getResources().getString(R.string.slbx)+"（万亩）");
        list4.add(mContext.getResources().getString(R.string.lqdy)+"（万亩）");
        list5.add(mContext.getResources().getString(R.string.lxzz)+"（万亩）");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String lgmj = map.get(mContext.getResources().getString(R.string.lgmj));
            String sjnh = map.get(mContext.getResources().getString(R.string.sjnh));
            String sjrk = map.get(mContext.getResources().getString(R.string.sjrk));
            String slbx = map.get(mContext.getResources().getString(R.string.slbx));
            String lqdy = map.get(mContext.getResources().getString(R.string.lqdy));
            String lxzz = map.get(mContext.getResources().getString(R.string.lxzz));

            titleData.add("       " + dqname + "      ");
            list.add(Util.round(lgmj));
            list1.add(Util.round(sjnh));
            list2.add(Util.round(sjrk));
            list3.add(Util.round(slbx));
            list4.add(Util.round(lqdy));
            list5.add(Util.round(lxzz));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);
        dataList.add(list4);
        dataList.add(list5);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 公益林 */
    @Override
    public void initGylTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("             " + lbmc + "             ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        String dqmc = mContext.getResources().getString(R.string.dqname);
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();
        ArrayList<String> list5 = new ArrayList<>();

        list.add(mContext.getResources().getString(R.string.gylmj)+"合计(万亩)");
        list1.add(mContext.getResources().getString(R.string.gjmj)+"(万亩)");
        list2.add(mContext.getResources().getString(R.string.dfmj)+"(万亩)");
        list3.add(mContext.getResources().getString(R.string.gylzj)+"合计(万元)");
        list4.add(mContext.getResources().getString(R.string.gjzj)+"(万元)");
        list5.add(mContext.getResources().getString(R.string.dfzj)+"(万元)");

        /** 表格每行数据 */
        for(Map<String,String> map : datas){
            String dqname = map.get(dqmc.trim());
            String gylmj = map.get(mContext.getResources().getString(R.string.gylmj));
            String gjmj = map.get(mContext.getResources().getString(R.string.gjmj));
            String dfmj = map.get(mContext.getResources().getString(R.string.dfmj));
            String gylzj = map.get(mContext.getResources().getString(R.string.gylzj));
            String gjzj = map.get(mContext.getResources().getString(R.string.gjzj));
            String dfzj = map.get(mContext.getResources().getString(R.string.dfzj));

            titleData.add("       "+dqname+"      ");

            list.add(Util.round(gylmj));
            list1.add(Util.round(gjmj));
            list2.add(Util.round(dfmj));
            list3.add(Util.round(gylzj));
            list4.add(Util.round(gjzj));
            list5.add(Util.round(dfzj));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);
        dataList.add(list4);
        dataList.add(list5);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext,titleData,dataList);
        tableView.setAdapter(adapter);
    }

    /** 森林防火 */
    @Override
    public void initSlfhTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("      " + lbmc + "      ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.hzcs)+"(次)");
        list1.add(mContext.getResources().getString(R.string.hzmj)+"(公顷)");
        list2.add(mContext.getResources().getString(R.string.ssmj)+"(公顷)");
        list3.add(mContext.getResources().getString(R.string.jftr)+"(万元)");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for(Map<String,String> map : datas){
            String dqname = map.get(dqmc.trim());
            String hzcs = map.get(mContext.getResources().getString(R.string.hzcs));
            String hzmj = map.get(mContext.getResources().getString(R.string.hzmj));
            String ssmj = map.get(mContext.getResources().getString(R.string.ssmj));
            String jftr = map.get(mContext.getResources().getString(R.string.jftr));

            titleData.add("       "+dqname+"      ");
            list.add(Util.rounding(hzcs));
            list1.add(Util.round(hzmj));
            list2.add(Util.round(ssmj));
            list3.add(Util.round(jftr));
        }
        dataList.add(list);
        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext,titleData,dataList);
        tableView.setAdapter(adapter);
    }

    /** 退耕还林 */
    @Override
    public void initTghlTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("               "+lbmc+"             ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();
        ArrayList<String> list5 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.ktgd)+"(万亩)");
        list1.add(mContext.getResources().getString(R.string.tgd)+"(万亩)");
        list2.add(mContext.getResources().getString(R.string.eswktgd)+"(万亩)");
        list3.add(mContext.getResources().getString(R.string.swktgd)+"(万亩)");
        list4.add(mContext.getResources().getString(R.string.ndddwc)+"(万亩)");
        list5.add(mContext.getResources().getString(R.string.ndddwcl)+"(%)");

        /** 表格每行数据 */
        String dqmc = mContext.getResources().getString(R.string.dqname);
        for (Map<String, String> map : datas) {
            String dqname = map.get(dqmc.trim());
            String ktgd = map.get(mContext.getResources().getString(R.string.ktgd));
            String tgd = map.get(mContext.getResources().getString(R.string.tgd));
            String eswktgd = map.get(mContext.getResources().getString(R.string.eswktgdf));
            String swktgd = map.get(mContext.getResources().getString(R.string.swktgdf));
            String ndddwc = map.get(mContext.getResources().getString(R.string.ndddwc));
            String ndddwcl = map.get(mContext.getResources().getString(R.string.ndddwcl));
            titleData.add("       " + dqname + "      ");
            list.add(Util.round(ktgd));
            list1.add(Util.round(tgd));
            list2.add(Util.round(eswktgd));
            list3.add(Util.round(swktgd));
            list4.add(Util.round(ndddwc));
            if(ndddwcl == null){
                ndddwc = "0";
            }
            list5.add(ndddwcl);
        }
        dataList.add(list);
        dataList.add(list2);
        dataList.add(list3);
        dataList.add(list1);
        dataList.add(list4);
        dataList.add(list5);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 林木采伐 */
    @Override
    public void initLmcfTabView(VHTableView tableView, List<Map<String, String>> datas) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("           "+lbmc+"        ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.cfxe)+"(万立方米)");
        list1.add(mContext.getResources().getString(R.string.cfl)+"(万立方米)");

        /** 表格每行数据 */
        for (Map<String, String> map : datas) {
            String dqname = map.get(mContext.getResources().getString(R.string.dqname));
            String cfxe = map.get(mContext.getResources().getString(R.string.cfxe));
            String cfl = map.get(mContext.getResources().getString(R.string.cfl));

            titleData.add("       " + dqname + "      ");
            list.add(Util.round(cfxe));
            list1.add(Util.round(cfl));
        }
        dataList.add(list);
        dataList.add(list1);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /** 保护区公园 */
    @Override
    public void initZrbhqTabView(VHTableView tableView, List<Map<String, String>> datas,String onname) {
        /** 表格第一列 */
        ArrayList<String> titleData = new ArrayList<>();
        String lbmc = mContext.getResources().getString(R.string.lbmc);
        titleData.add("           "+lbmc+"        ");
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        list.add(mContext.getResources().getString(R.string.gygs));
        list1.add(mContext.getResources().getString(R.string.totalarea));

        double sumgs = 0;
        double sumarea = 0;
        titleData.add("       " + onname + "      ");

        /** 表格每行数据 */
        for (Map<String, String> map : datas) {
            double gs = getInValue(map.get("个数"));
            sumgs = sumgs + Double.parseDouble(Util.rounding(gs+""));
            double mj = getInValue(map.get("面积（公顷）"));
            sumarea = sumarea+Double.parseDouble(Util.round(mj+""));
        }

        list.add(Util.rounding(sumgs+""));
        list1.add(Util.round(sumarea+""));

        for (Map<String, String> map : datas) {
            String dqname = map.get(mContext.getResources().getString(R.string.dqname));
            if(dqname.equals("0")){
                continue;
            }
            titleData.add("       " + dqname + "      ");

            String cfxe = map.get("个数");
            String cfl = map.get("面积（公顷）");
            list.add(Util.rounding(cfxe));
            list1.add(Util.round(cfl));
        }
        dataList.add(list);
        dataList.add(list1);

        /** 设置适配器 */
        VHTableAdapter adapter = new VHTableAdapter(mContext, titleData, dataList);
        tableView.setAdapter(adapter);
    }

    /**
     * 获取保护区数量及面积数据
     * @param str
     * @return
     */
    public double getInValue(String str){
        if(str == null){
            return 0;
        }
        return Double.parseDouble(str);
    }
}
