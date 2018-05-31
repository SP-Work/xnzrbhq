package com.otitan.xnbhq.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.MyExpandableListAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.mview.IBaseView;
import com.otitan.xnbhq.util.GeometryUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.timepaker.TimePopupWindow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by li on 2017/6/2.
 * 轨迹查询Presenter
 */

public class TrajectoryPresenter {

    private BaseActivity mContext;
    private IBaseView iBaseView;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public TrajectoryPresenter(BaseActivity context,IBaseView baseView){
        this.mContext = context;
        this.iBaseView = baseView;
    }

    /**
     * 初始化轨迹查询窗口自身轨迹查询
     */
    private List<Map<String, Object>> listExpandable = new ArrayList<>();
    private List<Map<String, Object>> listExpandable_path = new ArrayList<>();
    public void initMyTrajectorySearch(final View view) {
        final TextView startTime = (TextView) view.findViewById(R.id.startTime);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        startTime.setText(getTime(cal.getTime()));
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                initSelectTimePopuwindow(startTime, true);
            }
        });

        final TextView endTime = (TextView) view.findViewById(R.id.endTime);
        endTime.setText(getTime(new Date()));
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                initSelectTimePopuwindow(endTime, false);
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.select_lately);
        // Spinner绑定数据
        final String[] strArray = mContext.getResources().getStringArray(R.array.hour_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strArray);
        // adapter.setDropDownViewResource(android.R.unreportdata.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.myspinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       final int arg2, long arg3) {
                if (arg2 > 0) {
                    String timenearly = strArray[arg2];
                    Date dDate = new Date();
                    dDate.setHours(dDate.getHours() - Integer.parseInt(timenearly));
                    startTime.setText(getTime(dDate));
                    endTime.setText(getTime(new Date()));
                } else {
                    startTime.setText(getTime(cal.getTime()));
                    endTime.setText(getTime(new Date()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        final ExpandableListView expandable = (ExpandableListView) view.findViewById(R.id.zuji_expandableListView);
        final SeekBar processbar = (SeekBar) view.findViewById(R.id.process_bar);
        // 进度条拖动时 执行相应事件
        processbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 复写OnSeeBarChangeListener的三个方法
            // 第一个时OnStartTrackingTouch,在进度开始改变时执行
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // 第二个方法onProgressChanged是当进度发生改变时执行
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress,
                                          boolean fromUser) {
                listExpandable_path.clear();
                if (progress != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        if (listExpandable.size() > 0) {
                            listExpandable_path.add(listExpandable.get(i));
                        }
                    }
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            drawLine(listExpandable_path, progress);
                        }
                    });
                }
            }

            // 第三个是onStopTrackingTouch,在停止拖动时执行
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                listExpandable_path.clear();
                final int current = seekBar.getProgress();
                if (current != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        if (listExpandable.size() > 0) {
                            listExpandable_path.add(listExpandable.get(i));
                        }
                    }
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            drawLine(listExpandable_path, current);
                        }
                    });
                }
            }
        });

        final TextView btn_replay = (TextView) view.findViewById(R.id.btn_replay);
        // 初始化runnable开始
        mContext.hfrunnable = new Runnable() {
            @Override
            public void run() {
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        int curpro = processbar.getProgress();
                        if (curpro != processbar.getMax()) {
                            processbar.setProgress(curpro + 1);
                            mContext.timer.postDelayed(mContext.hfrunnable, 1000);/* 延迟0.5秒后继续执行 */
                        } else {
                            btn_replay.setText(" 回 放 ");/* 已执行到最后一个坐标 停止任务 */
                        }
                    }
                });
                // 要做的事情
                // handler.sendMessage(Message.obtain(handler, PlAYBACK));
            }
        };
        btn_replay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 根据按钮上的字判断当前是否在回放
                if (btn_replay.getText().toString().trim().equals("回 放")) {
                    if (listExpandable.size() > 0) {
                        // 假如当前已经回放到最后一点 置0
                        if (processbar.getProgress() == processbar.getMax()) {
                            processbar.setProgress(0);
                        }
                        btn_replay.setText(" 停 止 ");
                        mContext.timer.postDelayed(mContext.hfrunnable, 10);
                    }
                } else {
                    // 移除定时器的任务
                    mContext.timer.removeCallbacks(mContext.hfrunnable);
                    btn_replay.setText(" 回 放 ");
                }
            }
        });
        TextView guijibutton = (TextView) view.findViewById(R.id.guijibutton);
        guijibutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listExpandable.clear();
                expandable.setVisibility(View.GONE);
                String startT = startTime.getText().toString();
                String endT = endTime.getText().toString();
                try {
                    long start = format.parse(startT).getTime();
                    long end = format.parse(endT).getTime();
                    if (end > start) {
                        List<Map<String, Object>> list = DataBaseHelper.selectPointGuiji(mContext, MyApplication.macAddress, startT, endT);
                        if (list.size() <= 1){
                            ToastUtil.setToast(mContext,"无轨迹点");
                            return;
                        }
                        int length = list.size();
                        processbar.setEnabled(true);
                        btn_replay.setEnabled(true);
                        Polyline polyline = new Polyline();
                        Polygon polygon = null;
                        for (int i = length - 1; i >= 0; i--) {
                            double lon = Double.parseDouble(list.get(i).get("lon").toString());
                            double lat = Double.parseDouble(list.get(i).get("lat").toString());

                            if (i == 0) {
                                // 终点
                                Point endtPoint = new Point(lon, lat);
                                PictureMarkerSymbol endPicture = new PictureMarkerSymbol(
                                        mContext.getResources().getDrawable(R.drawable.nav_route_result_end_point));
                                endPicture.setOffsetY(18);
                                iBaseView.getGraphicLayer().addGraphic(new Graphic(endtPoint, endPicture));
                                polyline.lineTo(endtPoint);
                                listExpandable.add(list.get(i));
                                processbar.setMax(listExpandable.size());
                            } else if (i == length - 1) {
                                // 起点
                                Point startPoint = new Point(lon, lat);
                                polygon = mContext.creatPolygon(startPoint);
                                PictureMarkerSymbol startpicture = new PictureMarkerSymbol(
                                        mContext.getResources().getDrawable(R.drawable.nav_route_result_start_point));
                                startpicture.setOffsetY(18);
                                iBaseView.getGraphicLayer().addGraphic(new Graphic(startPoint, startpicture));
                                polyline.startPath(startPoint);
                                listExpandable.add(list.get(i));
                            } else {
                                Point point = new Point(lon, lat);
                                boolean flag = GeometryEngine.intersects(
                                        polygon, point,iBaseView.getSpatialReference());
                                if (!flag) {
                                    polyline.lineTo(point);
                                    polygon = mContext.creatPolygon(new Point(lon, lat));
                                    listExpandable.add(list.get(i));
                                }
                            }
                        }
                        try {
                            GeometryUtil.addPolylineToGraphicsLayer(polyline, Color.BLUE, 2, iBaseView.getGraphicLayer());
                        } catch (java.lang.Exception e) {
                            e.printStackTrace();
                        }
                        iBaseView.getMapView().setExtent(polyline);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        TextView zujibutton = (TextView) view.findViewById(R.id.zujibutton);
        zujibutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listExpandable.clear();
                try {
                    String startT = startTime.getText().toString();
                    String endT = endTime.getText().toString();
                    long start = format.parse(startT).getTime();
                    long end = format.parse(endT).getTime();
                    if (end > start) {
                        listExpandable = DataBaseHelper.selectPointGuiji(
                                mContext, MyApplication.macAddress, startT, endT);
                        if (listExpandable.size() == 0)
                            return;
                        expandable.setVisibility(View.VISIBLE);
                        processbar.setEnabled(true);
                        btn_replay.setEnabled(true);
                        int length = listExpandable.size();
                        processbar.setMax(length);
                        showExpandableListView(expandable, listExpandable);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        ImageView close_view = (ImageView) view.findViewById(R.id.close_guijisearch);
        close_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                view.setVisibility(View.GONE);
            }
        });
    }


    /**时间格式化*/
    public String getTime(Date date) {
        return format.format(date);
    }
    /**
     * 时间选择popupwindow
     */
    public void initSelectTimePopuwindow(final TextView button, boolean isBefore) {
        TimePopupWindow timePopupWindow = new TimePopupWindow(mContext, TimePopupWindow.Type.ALL);
        timePopupWindow.setCyclic(true);
        // 时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                button.setText(getTime(date));
            }
        });
        timePopupWindow.showAtLocation(button, Gravity.BOTTOM, 0, 0, new Date(), isBefore);
    }

    /**
     * 画线
     */
    private int palyGraphicID;
    private void drawLine(List<Map<String, Object>> list, int current) {
        if (listExpandable.size() > 0) {
            Point replayGeoPoint = new Point(Double.valueOf(listExpandable
                    .get(current - 1).get("lon").toString()),
                    Double.valueOf(listExpandable.get(current - 1).get("lat")
                            .toString()));
            // 添加汽车位置
            Graphic g = new Graphic(replayGeoPoint, new PictureMarkerSymbol(
                    mContext.getResources().getDrawable(R.drawable.car)));
            if (list.size() > 0) {
                iBaseView.getGraphicLayer().removeGraphic(palyGraphicID);
            }
            palyGraphicID = iBaseView.getGraphicLayer().addGraphic(g);
            if (list.size() > 0) {
                Polyline line = new Polyline();
                line.startPath(replayGeoPoint);
                for (int i = list.size() - 1; i >= 0; i--) {
                    line.lineTo(new Point(Double.valueOf(listExpandable_path
                            .get(i).get("lon").toString()), Double.valueOf(list
                            .get(i).get("lat").toString())));
                }

                try {
                    GeometryUtil.addPolylineToGraphicsLayer(line, Color.RED, 2,
                            iBaseView.getGraphicLayer());
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                }
            }
            iBaseView.getMapView().invalidate();
        }
    }

    /**
     * 展示expandablelistview数据
     */
    @SuppressLint("SimpleDateFormat")
    private void showExpandableListView(ExpandableListView expandable,List<Map<String, Object>> list) {
        int m = list.size();
        Set<String> groupSet = new HashSet<String>();
        final List<HashMap<Object, String>> childArray = new ArrayList<HashMap<Object, String>>();
        for (int i = 0; i < m; i++) {
            groupSet.add(list.get(i).get("time").toString().substring(0, 10));
        }
        final String[] groups = new String[groupSet.size()];
        final String[][] childs = new String[groups.length][];
        final String[][] lons = new String[groups.length][];
        final String[][] lats = new String[groups.length][];
        Iterator<String> iterator = groupSet.iterator();
        while (iterator.hasNext()) {
            for (int i = 0; i < groupSet.size(); i++) {
                groups[i] = iterator.next();
            }
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> dateList = new ArrayList<Date>();
        for (int i = 0; i < groups.length; i++) {
            try {
                dateList.add(df.parse(groups[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Date tempDate = null;
        for (int i = dateList.size() - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (dateList.get(j + 1).after(dateList.get(j))) {
                    tempDate = dateList.get(j);
                    dateList.set(j, dateList.get(j + 1));
                    dateList.set(j + 1, tempDate);
                }
            }
        }
        for (int i = 0; i < dateList.size(); i++) {
            groups[i] = df.format(dateList.get(i));
        }

        for (int i = 0; i < groups.length; i++) {
            int k = 0;
            for (int j = 0; j < m; j++) {
                if (list.get(j).get("time").toString().substring(0, 10)
                        .equals(groups[i])) {
                    String str = list.get(j).get("time").toString();
                    HashMap<Object, String> map = new HashMap<Object, String>();
                    map.put(groups[i], str.substring(11, str.length()));
                    map.put("lon", list.get(j).get("lon").toString());
                    map.put("lat", list.get(j).get("lat").toString());
                    childArray.add(map);
                    k++;
                }
            }
            childs[i] = new String[k];
            lons[i] = new String[k];
            lats[i] = new String[k];
        }

        for (int i = 0; i < groups.length; i++) {
            int k = 0;
            for (int j = 0; j < childArray.size(); j++) {
                if (childArray.get(j).get(groups[i]) != null) {
                    childs[i][k] = childArray.get(j).get(groups[i]);
                    lons[i][k] = childArray.get(j).get("lon");
                    lats[i][k] = childArray.get(j).get("lat");
                    k++;
                }
            }
        }

        MyExpandableListAdapter treeAadpter = new MyExpandableListAdapter(mContext, groups, childs);
        expandable.setGroupIndicator(null);
        treeAadpter.notifyDataSetChanged();
        expandable.setAdapter(treeAadpter);
        expandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        final int parent, final int children, long arg4) {
                mContext.clearGraphic();
                double lon = Double.parseDouble(lons[parent][children]);
                double lat = Double.parseDouble(lats[parent][children]);
                Point point = new Point(lon, lat);
                PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.icon_gcoding));
                pictureMarkerSymbol.setOffsetY(20);
                Graphic graphic = new Graphic(point, pictureMarkerSymbol);
                if (graphic != null) {
                    iBaseView.getGraphicLayer().addGraphic(graphic);
                }
                iBaseView.getMapView().setExtent(point);
                iBaseView.getMapView().invalidate();
                return false;
            }
        });
    }


}
