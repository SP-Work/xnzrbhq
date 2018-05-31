package com.titan.gzzhjc.dao;


import com.titan.vhtableview.VHTableView;

import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/3/8.
 *
 * 表格数据接口
 */

public interface IVHTableViewDataDao {

    void initLmcfTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initTghlTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initLqggTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initGylTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initSlfhTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initYzlTabView(VHTableView tableView, List<Map<String, String>> datas, String company, String sqlkey);

    void initSthlyTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initZmscTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initYhswTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initGhryTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initLycyTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initShmhTabView(VHTableView tableView, List<Map<String, String>> datas);

    void initZrbhqTabView(VHTableView tableView, List<Map<String, String>> datas, String name);

    void initLyjcTabView(VHTableView tableView, List<Map<String, String>> datas, String company, String table_title, String sqlkey);
}
