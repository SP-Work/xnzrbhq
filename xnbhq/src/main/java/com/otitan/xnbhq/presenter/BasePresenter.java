package com.otitan.xnbhq.presenter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Feature;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.StartActivity;
import com.otitan.xnbhq.adapter.AttributeAdapter;
import com.otitan.xnbhq.adapter.FeatureResultAdapter;
import com.otitan.xnbhq.adapter.SearchXdmAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.db.DbHelperService;
import com.otitan.xnbhq.dialog.ShouCangDialog;
import com.otitan.xnbhq.edite.activity.LineEditActivity;
import com.otitan.xnbhq.edite.activity.PointEditActivity;
import com.otitan.xnbhq.edite.activity.XbEditActivity;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.entity.Station;
import com.otitan.xnbhq.entity.XdmSearchHistory;
import com.otitan.xnbhq.googlemap.GoogleMapLayer;
import com.otitan.xnbhq.googlemap.GoogleMapLayerTypes;
import com.otitan.xnbhq.mview.IBaseView;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BaseUtil;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.SpatialUtil;
import com.otitan.xnbhq.util.SytemUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.otitan.xnbhq.util.UtilTime;

import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionFactory;
import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.proj.Projection;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/5/9.
 * basePresenter
 */

public class BasePresenter {

    private BaseActivity baseActivity;
    private IBaseView iBaseView;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public BasePresenter(BaseActivity ctx, IBaseView view){
        this.baseActivity = ctx;
        this.iBaseView = view;
    }

    /**添加tpk文件地图*/
    public TiledLayer addTitleLayer(@NonNull String type){
        if(type.contains("image")){
            String path = baseActivity.getResources().getString(R.string.image_online);
            ArcGISTiledMapServiceLayer titleLayer = new ArcGISTiledMapServiceLayer(path);
            iBaseView.getMapView().addLayer(titleLayer);
            return titleLayer;
        }

        if (type.contains("vector")) {//vector 矢量数据
            //本地文件不存在时加载在线地图
            String path = baseActivity.getResources().getString(R.string.title_online);
            ArcGISTiledMapServiceLayer titleLayer = new ArcGISTiledMapServiceLayer(path);
            iBaseView.getMapView().addLayer(titleLayer);
            return titleLayer;
        }

        return new ArcGISTiledMapServiceLayer("");
    }

    /**添加graphicLayer图层*/
    public GraphicsLayer addGraphicLayer(){
        GraphicsLayer graphicsLayer = new GraphicsLayer(GraphicsLayer.RenderingMode.STATIC);
        iBaseView.getMapView().addLayer(graphicsLayer);
        return graphicsLayer;
    }
    /** 当前点数据展示 */
    public View loadCalloutView(@NonNull final Point point) {
        DecimalFormat decimalFormat = new DecimalFormat(".000000");
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.callout_mylocation, null);
        final View wgsView = view.findViewById(R.id.callout_mylocation_wgs);
        final View xianView = view.findViewById(R.id.callout_mylocation_xian80);
        TextView wgslon = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lon);
        String lonTxt = decimalFormat.format(iBaseView.getGpsPoint().getX()) + "";
        wgslon.setText(lonTxt);
        TextView wgslat = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lat);
        String latTxt = decimalFormat.format(iBaseView.getGpsPoint().getY()) + "";
        wgslat.setText(latTxt);
        TextView xianlon = (TextView) view.findViewById(R.id.callout_mylocation_80_lon);
        String xx = decimalFormat.format(iBaseView.getGpsPoint().getX()) + "";
        xianlon.setText(xx);
        TextView xianlat = (TextView) view.findViewById(R.id.callout_mylocation_80_lat);
        String yy = decimalFormat.format(iBaseView.getGpsPoint().getY()) + "";
        xianlat.setText(yy);

        TextView wgslon1 = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lon1);
        wgslon1.setText(xiaoZhJwd(iBaseView.getGpsPoint().getX()));

        TextView wgslat1 = (TextView) view.findViewById(R.id.callout_mylocation_wgs_lat1);
        wgslat1.setText(xiaoZhJwd(iBaseView.getGpsPoint().getY()));
        //
        TextView addscpoint = (TextView) view.findViewById(R.id.addscpoint);
        addscpoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!point.isValid()){
                    ToastUtil.setToast(baseActivity, "未获取到当前位置坐标");
                    return;
                }
                ShouCangDialog dialog=new ShouCangDialog(baseActivity,point,ShouCangDialog.SqlType.ADD);
                BussUtil.setDialogParams(baseActivity, dialog, 0.7, 0.8);
            }
        });

        ImageView close = (ImageView) view.findViewById(R.id.callout_mylocation_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iBaseView.getCallout().hide();
            }
        });
        return view;
    }

    /**小数转换为度数*/
    private String xiaoZhJwd(@NonNull double dd){
        int du = (int) Math.floor(dd);
        double fff = (dd-du)*60;
        int fen =(int) Math.floor(fff);
        DecimalFormat df = new DecimalFormat("0.000");
        String miao = df.format((fff - fen)*60);
        return du+"°"+fen+"'"+miao+"\"";
    }

    /** 长点击事件展示位置信息的popuwindow */
    public View loadCalloutPopuwindow(@NonNull final Point mappoint){
        DecimalFormat decimalFormat = new DecimalFormat(".000000");
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.calloutpopuwindow, null);
        final View wgsView = view.findViewById(R.id.calloutpopuwindow_xian80);

        Point gps =(Point) GeometryEngine.project(mappoint, SpatialUtil.wgs_3857(),SpatialUtil.wgs_4326());
        //wgs84坐标
        TextView wgslon1 = (TextView) view.findViewById(R.id.calloutpopuwindow_wgs_lon);
        wgslon1.setText(xiaoZhJwd(gps.getX()));
        TextView wgslat1 = (TextView) view.findViewById(R.id.calloutpopuwindow_wgs_lat);
        wgslat1.setText(xiaoZhJwd(gps.getY()));
        //西安80坐标系
        TextView xianlon = (TextView) view.findViewById(R.id.calloutpopuwindow_80_x);
        String xx = decimalFormat.format(gps.getX()) + "";
        xianlon.setText(xx);
        TextView xianlat = (TextView) view.findViewById(R.id.calloutpopuwindow_80_y);
        String yy = decimalFormat.format(gps.getY()) + "";
        xianlat.setText(yy);

        TextView addscpoint = (TextView) view.findViewById(R.id.addscpoint);
        addscpoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ShouCangDialog dialog=new ShouCangDialog(baseActivity,mappoint,ShouCangDialog.SqlType.ADD);
                BussUtil.setDialogParams(baseActivity, dialog, 0.7, 0.8);
            }
        });
        return view;
    }

    /** 添加轨迹坐标点 */
    public void addGuijiPoint(@NonNull Point currentPoint) {
        if(!currentPoint.isValid()){
            return;
        }
        String state = "0";
        String recodeTime = format.format(new Date());
        if (MyApplication.getInstance().hasNetWork()) {// 在线保存
            Webservice webservice = new Webservice(baseActivity);
            state = webservice.upPoint(MyApplication.macAddress,currentPoint.getX() + "", currentPoint.getY() + "",format.format(new Date()), MyApplication.mobileXlh,MyApplication.mobileType);
            if (state.equals("0")) {
                // 录入失败
            } else if (state.equals("1")) {
                // 录入成功
            }
        }

        /* 离线保存 */
        DataBaseHelper.addPointGuiji(baseActivity,MyApplication.macAddress, currentPoint.getX(),currentPoint.getY(), recodeTime, state);
    }

    /** 从相册选取图片进行编辑 */
    private static final int ALBUM = 0x000002;
    public void fromAlum() {
        //系统相册
        Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //picture.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        baseActivity.startActivityForResult(picture, ALBUM);
    }

    /** 拍照 */
    public String takephoto() {
        try {
            // 指定存放拍摄照片的位置
            File file = createImageFile();

            String path = com.titan.baselibrary.util.Util.takePhoto(baseActivity,file,baseActivity.TAKE_PHOTO);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /** 存放文件位置 */
    private File createImageFile() throws IOException {
        String path = MyApplication.resourcesManager.getFolderPath("/phone");
        if(path.equals("文件夹可用地址")){
            try {
                path = MyApplication.resourcesManager.getTootPath()+ ResourcesManager.ROOT_MAPS+"/phone";
                ResourcesManager.createFolder(path);
            } catch (jsqlite.Exception e) {
                e.printStackTrace();
            }
        }
        File image = new File(path+"/"+ String.valueOf(System.currentTimeMillis()) + ".jpg");
        return image;
    }

    /** 加载小班属性数据 */
    public View loadAttributeView(List<Field> fieldList, Map<String, Object> attributes) {
        Iterator it = fieldList.iterator();
        while(it.hasNext()) {
            Field field = (Field) it.next();
            String alias = field.getAlias();
            if (alias.equals("OBJECTID") || alias.equals("GLOBALID")) {
                fieldList.remove(it);
            }
        }

        final View view = LayoutInflater.from(baseActivity).inflate(R.layout.polygon_attributeinfo, null);
        ListView attribute_listview = (ListView) view.findViewById(R.id.attribute_listview);
        AttributeAdapter adapter = new AttributeAdapter(fieldList, attributes, baseActivity, "当前图层");
        attribute_listview.setAdapter(adapter);
        ImageButton button = (ImageButton) view.findViewById(R.id.attributeclose);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                iBaseView.getCallout().hide();
            }
        });
        return view;
    }

    /**
     * pro4j进行坐标转换
     */
    public ProjCoordinate meth(double lon, double lat, double al) {
        String xx = MyApplication.sharedPreferences.getString("dx", "0");
        double x1 = 0,y1 = 0,z1 = 0 ;
        if(Util.CheckStrIsDouble(xx)){
           x1  = Double.parseDouble(xx.equals("") ? "0" : xx.trim());
        }
        String yy = MyApplication.sharedPreferences.getString("dy", "0");
        if(Util.CheckStrIsDouble(yy)){
            y1 = Double.parseDouble(yy.equals("") ? "0" : yy.trim());
        }
        String zz = MyApplication.sharedPreferences.getString("dz", "0");
        if(Util.CheckStrIsDouble(zz)){
            z1 = Double.parseDouble(zz.equals("")? "0" : zz.trim());
        }

        String[] wgs840 = new String[]{"+proj=longlat", "+ellps=WGS84", "+datum=WGS84", "+no_defs"};
        Datum datum0 = Datum.WGS84;
        Projection proj840 = ProjectionFactory.fromPROJ4Specification(wgs840);

        CoordinateReferenceSystem wgs84cs0 = new CoordinateReferenceSystem("WGS84", wgs840, datum0, proj840);
//		String[] wgs841 = new String[] { "+proj=merc", "+lon_0=0", "+k=1",
//				"+x_0=0", "+y_0=0", "+datum=WGS84", "+units=m", "+no_defs" };
//		Datum datum1 = Datum.WGS84;
//		Projection proj841 = ProjectionFactory.fromPROJ4Specification(wgs841);

//		CoordinateReferenceSystem wgs84cs1 = new CoordinateReferenceSystem(
//				"WGS84", wgs841, datum1, proj841);

//		String[] wgs842 = new String[] { "+proj=merc", "+lon_0=0", "+k=1",
//				"+x_0=0", "+y_0=0", "+datum=WGS84", "+units=m", "+no_defs" };
        // 3395 wgs1984 world wercator +proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0
        // +datum=WGS84 +units=m +no_defs
        // ,"+towgs84=22,-107.4036667,-37.915,3.961,0,0,0"
//		Datum datum2 = Datum.WGS84;
//		Projection proj842 = ProjectionFactory.fromPROJ4Specification(wgs842);
//
//		CoordinateReferenceSystem wgs84cs2 = new CoordinateReferenceSystem(
//				"WGS84", wgs842, datum2, proj842);

        // +proj=longlat +a=6378140 +b=6356755.288157528 +no_defs
        String[] xian801 = new String[]{"+proj=longlat", "+a=6378140","+b=6356755.288157528", "+no_defs"};
        Ellipsoid ellipsoid1 = new Ellipsoid("xian80", 6378140, 0.0, 298.257,"xian80");
        Projection proj801 = ProjectionFactory.fromPROJ4Specification(xian801);

        Datum datum801 = new Datum("xian80", x1, y1, z1, ellipsoid1, "xian80");
        CoordinateReferenceSystem xian80cs1 = new CoordinateReferenceSystem("xian80", xian801, datum801, proj801);

        String[] xian80 = new String[]{"+proj=tmerc", "+lat_0=0",
                "+lon_0=105", "+k=1", "+x_0=500000", "+y_0=0", "+a=6378140",
                "+b=6356755.288157528", "+units=m", "+no_defs"};
        Ellipsoid ellipsoid = new Ellipsoid("xian80", 6378140, 0.0, 298.257,
                "xian80");
        Projection proj80 = ProjectionFactory.fromPROJ4Specification(xian80);

        Datum datum80 = new Datum("xian80", x1, y1, z1, ellipsoid, "xian80");
        CoordinateReferenceSystem xian80cs = new CoordinateReferenceSystem(
                "xian80", xian80, datum80, proj80);

        ProjCoordinate src = new ProjCoordinate(lon, lat, al);
        ProjCoordinate dst = new ProjCoordinate();

        BasicCoordinateTransform transformation = new BasicCoordinateTransform(wgs84cs0, xian80cs1);
        ProjCoordinate ddd = transformation.transform(src, dst);
        //System.out.println("未投影西安80 " + ddd.x + " === " + ddd.y + "---- " + ddd.z);
        ProjCoordinate src1 = new ProjCoordinate(ddd.x, ddd.y, ddd.z);

        BasicCoordinateTransform transformation1 = new BasicCoordinateTransform(xian80cs1, xian80cs);
        ProjCoordinate ddd1 = transformation1.transform(src1, dst);
        //System.out.println("西安80 投影" + ddd1.x + " === " + ddd1.y + "---- " + ddd1.z);
        return ddd1;
    }

    /**
     * 弹出结果展示窗口
     */
    public void showListFeatureResult(final List<GeodatabaseFeature> list) {
        final Dialog dialog = new Dialog(baseActivity, R.style.Dialog);
        dialog.setContentView(R.layout.featureresult_view);
        dialog.setCanceledOnTouchOutside(true);
        final ListView listView = (ListView) dialog.findViewById(R.id.featureresult_listview);
        FeatureResultAdapter adapter = new FeatureResultAdapter(baseActivity, list, baseActivity.selMap);
        listView.setAdapter(adapter);

        BaseUtil.getIntance(baseActivity).setHeight(adapter, listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (iBaseView.getGraphicLayer() != null) {
                    iBaseView.getGraphicLayer().removeAll();
                }
                GeodatabaseFeature geofeature = baseActivity.getSelParams(list, position,baseActivity.selMap);
                if(geofeature == null){
                    return;
                }
                Intent intent = null;
                if (BaseActivity.selectGeometry.getType().equals(Geometry.Type.POINT)) {
                    intent = new Intent(baseActivity, PointEditActivity.class);
                } else if (BaseActivity.selectGeometry.getType().equals(Geometry.Type.POLYLINE)) {
                    intent = new Intent(baseActivity, LineEditActivity.class);
                } else if (BaseActivity.selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
                    intent = new Intent(baseActivity, XbEditActivity.class);
                } else {
                    intent = new Intent(baseActivity, XbEditActivity.class);
                }

                String pname = BaseActivity.myLayer.getPname();// 工程名称
                String path = BaseActivity.myLayer.getPath();
                String cname = BaseActivity.myLayer.getCname();
                MyFeture feture = new MyFeture(pname, path, cname, BaseActivity.selGeoFeature, BaseActivity.myLayer);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myfeture", feture);
                bundle.putSerializable("parent", "Base");
                bundle.putSerializable("id", BaseActivity.selGeoFeature.getId() + "");
                intent.putExtras(bundle);
                baseActivity.startActivity(intent);
                feture = null;
            }
        });

        adapter.notifyDataSetChanged();
        BussUtil.setDialogParams(baseActivity, dialog, 0.55, 0.55);
    }

    /**
     * 测量面积测量调绘方式选择
     */
    public void initCmPopuwindow() {

        final PopupWindow pop = new PopupWindow(baseActivity);

        View view = baseActivity.getLayoutInflater().inflate(R.layout.item_cemian_popuwindows, null);
        final LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup_cemian);
        pop.setWidth(MyApplication.screen.getWidthPixels() / 3);//LayoutParams.MATCH_PARENT
        pop.setHeight(MyApplication.screen.getHeightPixels() / 2);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        pop.showAtLocation(baseActivity.childview, Gravity.CENTER, 0, 0);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent_cemian);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_dbx);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_zyqx);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_freeline);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.restory();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.drawType = BaseActivity.POLYGON;
                baseActivity.activate(baseActivity.drawType);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.drawType = BaseActivity.FREEHAND_POLYGON;
                baseActivity.activate(baseActivity.drawType);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pop.dismiss();
                ll_popup.clearAnimation();
                baseActivity.drawType = BaseActivity.FREEHAND_POLYLINE;
                baseActivity.activate(baseActivity.drawType);
            }
        });
    }

    /**
     * 初始化小地名查询控件
     */
    public void initXdmView(final View xdmSearchInclude){

        final EditText searchTxt = (EditText) xdmSearchInclude.findViewById(R.id.xdm_searchText);
        CursorUtil.setEditTextLocation(searchTxt);
        final ListView listResult = (ListView) xdmSearchInclude.findViewById(R.id.listView_xdm_search);
        final DbHelperService<XdmSearchHistory> service = new DbHelperService<>(baseActivity, XdmSearchHistory.class);
        final List<XdmSearchHistory> list = service.getObjectsByWhere(null);
        final SearchXdmAdapter<XdmSearchHistory> adapter = new SearchXdmAdapter<>(list, baseActivity);
        listResult.setAdapter(adapter);
        listResult.setVisibility(View.GONE);
        searchTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listResult.setAdapter(adapter);
                listResult.setVisibility(View.VISIBLE);

                listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        String txt = list.get(arg2).getName();
                        searchTxt.setText(txt);
                        CursorUtil.setEditTextLocation(searchTxt);
                    }
                });
            }
        });

        ImageView searchView = (ImageView) xdmSearchInclude.findViewById(R.id.xdm_search_view);
        searchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(searchTxt.getText())) {
                    ToastUtil.setToast(baseActivity, "请输入查询地名");
                    return;
                }
                String searchValue = searchTxt.getText().toString();
                ArrayList<String> lst = new ArrayList<>();
                if (!lst.contains(searchValue)) {
                    XdmSearchHistory t = new XdmSearchHistory();
                    t.setName(searchTxt.getText().toString());
                    t.setTime(UtilTime.getSystemtime2());
                    boolean flag = service.add(t);
                }
                searchXdmMethod(searchValue, listResult);
            }
        });
        ImageView close = (ImageView) xdmSearchInclude.findViewById(R.id.close_xdm_search);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xdmSearchInclude.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 小地名查询
     */
    private void searchXdmMethod(final String searchTxt, final ListView listResult) {

        DbHelperService<Station> service = new DbHelperService<>(baseActivity, Station.class);
        HashMap<String, String> where = new HashMap<String, String>();
        where.put("name like", "%" + searchTxt + "%");
        final List<Station> list = service.getObjectsByWhere(where);

        //final List<HashMap<String, Object>> list = DataBaseHelper.getAddressInfo(mContext, searchTxt);
        if (list.size() == 0) {
            ToastUtil.setToast(baseActivity, "无此类地名");
            return;
        }

        SearchXdmAdapter<Station> adapter = new SearchXdmAdapter<>(list, baseActivity);
        listResult.setAdapter(adapter);

        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterview, View view,
                                    final int position, long l) {
                dwToMap(list, position);
            }
        });

    }

    /**
     * 定位到地图
     */
    private void dwToMap(final List<Station> list, final int position) {
        baseActivity.runOnUiThread(new Runnable() {
            public void run() {
                baseActivity.clearGraphic();
                Double x = Double.valueOf(list.get(position).getX());
                Double y = Double.valueOf(list.get(position).getY());
                Point point = new Point(x, y);
                Graphic graphic = new Graphic(point, baseActivity.pictureMarkerSymbol);
                iBaseView.getGraphicLayer().addGraphic(graphic);
                TextSymbol textSymbol = new TextSymbol(14,list.get(position).getName(),Color.RED);
                textSymbol.setFontFamily("DroidSansFallback.ttf");//设置字体
                textSymbol.setOffsetX(0);
                textSymbol.setOffsetY(-30);
                Graphic graphic1 = new Graphic(point,textSymbol);
                iBaseView.getGraphicLayer().addGraphic(graphic1);
                int currentLevel = baseActivity.tiledLayer.getCurrentLevel();
                if (currentLevel > 14) {
                    iBaseView.getMapView().zoomTo(point, 0);
                } else {
                    iBaseView.getMapView().zoomTo(point, 14 - currentLevel);
                }
                iBaseView.getMapView().setExtent(point);
                showAddressPopup(list.get(position).getName(), point);
            }
        });
    }

    /**
     * 搜索结果点击显示导航
     */
    private void showAddressPopup(String addressName, final Point point) {
        final View view = baseActivity.childview.findViewById(R.id.address_navigation_include);
        view.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) view.findViewById(R.id.addressviewreturn);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                view.setVisibility(View.GONE);
            }
        });

        TextView address = (TextView) view.findViewById(R.id.item_popupwindows_address);
        address.setText(addressName);
        Button daohang = (Button) view.findViewById(R.id.address_daohang);
        daohang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                baseActivity.mRouteTask = baseActivity.navigationPresenter.initRoutAndGeocoding();
                baseActivity.mStops.clearFeatures();
                Polyline polyline = new Polyline();
                if (BaseActivity.currentPoint != null && BaseActivity.currentPoint.isValid()) {
                    baseActivity.addFeatureNavi(BaseActivity.currentPoint);
                    polyline.startPath(BaseActivity.currentPoint);
                }
                baseActivity.navigationPresenter.drawLineToMap(point, polyline);
            }
        });
    }

    /**
     * 加载数据所属数据
     */
    public void uploadsjssData() {

        if (MyApplication.getInstance().hasNetWork()) {
            Webservice web = new Webservice(baseActivity);
            String datacode = "guiyangshi";
            if(StartActivity.bsuserbase != null){
                datacode = StartActivity.bsuserbase.getDATASHARE().trim();
            }
            String result = web.getSjssData(datacode);
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
            } else {
                baseActivity.sjssLlist = BussUtil.getSjssData(result);
            }
        }
    }

    /**
     * 加载驯养繁殖基地基地性质数据
     */
    public void uploadjdxzData() {
        if (MyApplication.getInstance().hasNetWork()) {
            Webservice web = new Webservice(baseActivity);
            String result = web.getJdxzData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
            } else {
                baseActivity.jdxzLlist = BussUtil.getJdxzData(result);
            }
        }
    }

    /**
     * 加载行政区域数据数据
     */
    public void uploadxzqyData() {

        if (MyApplication.getInstance().hasNetWork()) {
            Webservice web = new Webservice(baseActivity);
            String result = web.getXzqyData("1");
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
            } else {
                baseActivity.xzqyLlist = BussUtil.getSjssData(result);
            }
        }
    }

    /**
     * 加载图层为一个图层时默认选择这个图层，获取这个图层的数据
     */
    public void getMylayer() {
        BaseActivity.myLayer = BaseActivity.layerNameList.get(0);
        baseActivity.layerType = BaseActivity.myLayer.getLayer().getGeometryType();
        String layername = BaseActivity.myLayer.getLname();

        ToastUtil.setToast(baseActivity, layername);
        SytemUtil.getEditSymbo(baseActivity, BaseActivity.myLayer.getLayer());
    }

    /**
     * 新增线
     * polyline_all 绘制的线图形
     */
    public Map<String, Object> addFeatureLine(Polyline polyline_all) {
        if (polyline_all == null) {
            ToastUtil.setToast(baseActivity, "请勾绘线");
            return new HashMap<>();
        }

        int size = polyline_all.getPointCount();
        MultiPath multiPath = new Polyline();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                multiPath.startPath(polyline_all.getPoint(i));
            } else {
                multiPath.lineTo(polyline_all.getPoint(i));
            }
        }

        Geometry geom = GeometryEngine.simplify(multiPath, iBaseView.getSpatialReference());
        Map<String, Object> map = addFeatureOnLayer(geom, baseActivity.layerFeatureAts);

        baseActivity.mapRemove(new View(baseActivity));
        return map;
    }

    /**
     * 新增面
     */
    public Map<String, Object> addFeaturePolygon(Polygon polygon) {
        if (polygon == null) {
            ToastUtil.setToast(baseActivity, "请先勾绘图斑");
            return new HashMap<>();
        }
        if (polygon.isEmpty()) {
            return new HashMap<>();
        }
        if (polygon.getPointCount() < 3) {
            return new HashMap<>();
        }
        if (!polygon.isValid()) {
            return new HashMap<>();
        }

        Geometry geom = GeometryEngine.simplify(polygon, iBaseView.getSpatialReference());
        Map<String, Object> map = addFeatureOnLayer(geom, baseActivity.layerFeatureAts);
        baseActivity.undonList.add(map);
        baseActivity.mapRemove(new View(baseActivity));
        return map;
    }

    /**
     * 图形更新，
     * 勾绘后的geom
     * 更新前的feature
     * myLayer feature所在图层
     */
    public Graphic updateFeature(Geometry geom, GeodatabaseFeature selFeature, MyLayer myLayer) {
        Graphic graphic = null;
        try {
            graphic = new Graphic(geom, selFeature.getSymbol(), selFeature.getAttributes());
            if (!geom.isValid() || geom.isEmpty()) {
                return graphic;
            }
            FeatureTable featureTable = myLayer.getTable();
            featureTable.updateFeature(selFeature.getId(), graphic);
			/* 添加小班后 记录添加小班的id 备撤销时删除 */
            //recordXb(selFeature.getId(), "update", selFeature.getAttributes(), selFeature.getGeometry(), myLayer.getLayer());
        } catch (TableException e) {
            ToastUtil.setToast(baseActivity, e.getMessage());
            return null;
        }
        return graphic;
    }

    /**
     * 删除feature的方法
     * id  feature的id
     */
    public void delFeature(GeodatabaseFeature feature) {
        MyLayer myLayer = BaseUtil.getIntance(baseActivity).getFeatureInLayer(feature, BaseActivity.layerNameList,baseActivity.selMap);
        FeatureTable featureTable = myLayer.getTable();
        try {
            featureTable.deleteFeature(feature.getId());
            //recordXb(feature.getId(), "delete", feature.getAttributes(), feature.getGeometry(), myLayer.getLayer());
        } catch (TableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录变化的 以备撤销使用
     * @param id             小班id
     * @param type           修改类型 添加 更新 删除
     * @param attr           变动小班的属性信息
     * @param geom           变动小班
     * @param layer          变动小班所在图层
     */
    public void recordXb(long id, String type, Map<String, Object> attr,Geometry geom, FeatureLayer layer) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        map.put("attribute", attr);
        map.put("geometry", geom);
        map.put("layer", layer);
        baseActivity.undonList.add(map);
    }

    /**
     * 添加feature在图层上
     * geom 绘制的图形
     * selFeatureAts  图层属性
     */
    public Map<String, Object> addFeatureOnLayer(Geometry geom, Map<String, Object> selFeatureAts) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (geom.isEmpty() || !geom.isValid()) {
                return map;
            }

            Envelope envelope = new Envelope();
            geom.queryEnvelope(envelope);
            if (envelope.isEmpty() || !envelope.isValid()) {
                return map;
            }

            GeodatabaseFeatureTable table = BaseActivity.myLayer.getTable();
            GeodatabaseFeature g = table.createFeatureWithTemplate(BaseActivity.layerTemplate, geom);
            Symbol symbol = BaseActivity.myLayer.getRenderer().getSymbol(g);
            // symbol为null也可以 why？
            Map<String, Object> editAttributes = null;
            if (selFeatureAts == null) {
                editAttributes = g.getAttributes();
            } else {
                editAttributes = selFeatureAts;
            }
            //TODO
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = format.format(new Date());
            editAttributes.put("WYBH", str);
            editAttributes.put("aaa",0);

            Graphic addedGraphic = new Graphic(geom, symbol, editAttributes);
            long id = table.addFeature(addedGraphic);

            Feature feature = table.getFeature(id);
            Geometry geometry = feature.getGeometry();
            if (geometry.isEmpty() || !geometry.isValid()) {
                table.deleteFeature(id);
            } else {
                /* 添加小班后 记录添加小班的id备撤销时删除 */
                map.put("id", id);
                map.put("type", "add");
                map.put("attribute", editAttributes);
                map.put("geometry", geom);
                map.put("layer", BaseActivity.myLayer.getLayer());
            }
        } catch (TableException e) {
            e.printStackTrace();
        }
        iBaseView.getMapView().invalidate();
        return map;
    }

    /**
     * 添加feature在图层上
     */
    public Map<String, Object> addFeatureGbmOnLayer(Geometry geom, Map<String, Object> selectFeatureAts) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!geom.isValid() || geom.isEmpty()) {
                return map;
            }
            if (geom.getType() == Geometry.Type.POLYGON) {
                Polygon polygon = (Polygon) geom;
                if (polygon.getPointCount() < 3) {
                    return map;
                }
                double dd = geom.calculateArea2D();
                if (dd <= 0) {
                    return map;
                }
            } else if (geom.getType() == Geometry.Type.POLYLINE || geom.getType() == Geometry.Type.LINE) {
                Polyline polygon = (Polyline) geom;
                if (polygon.getPointCount() < 2) {
                    return map;
                }
                double dd = geom.calculateLength2D();
                if (dd <= 0) {
                    return map;
                }
            }

            GeodatabaseFeature g = BaseActivity.myLayer.getTable().createFeatureWithTemplate(BaseActivity.layerTemplate, geom);
            Symbol symbol = BaseActivity.myLayer.getRenderer().getSymbol(g);
            // symbol为null也可以 why？
            if (selectFeatureAts == null) {
                selectFeatureAts = g.getAttributes();
            }
            //TODO
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = format.format(new Date());
            selectFeatureAts.put("WYBH", str);

            Geometry[] geometries = new Geometry[BaseActivity.selGeoFeaturesList.size() + 1];
            for (int i = 0; i < BaseActivity.selGeoFeaturesList.size(); i++) {
                geometries[i] = BaseActivity.selGeoFeaturesList.get(i).getGeometry();
            }
            geometries[BaseActivity.selGeoFeaturesList.size()] = geom;

            Geometry geometry = GeometryEngine.union(geometries, iBaseView.getSpatialReference());
            for (int i = 0; i < BaseActivity.selGeoFeaturesList.size(); i++) {
                Geometry geometry2 = BaseActivity.selGeoFeaturesList.get(i).getGeometry();
                geometry = GeometryEngine.difference(geometry, geometry2, BaseActivity.spatialReference);
            }

            FeatureTable featureTable = BaseActivity.myLayer.getTable();
            Graphic addedGraphic = new Graphic(geometry, symbol, selectFeatureAts);
            long id = featureTable.addFeature(addedGraphic);


            Feature feature = featureTable.getFeature(id);
            Geometry geometry1 = feature.getGeometry();
            if (geometry1.isEmpty() || !geometry1.isValid()) {
                featureTable.deleteFeature(id);
            } else {
                /* 添加小班后 记录添加小班的id备撤销时删除 */
                map.put("id", id);
                map.put("type", "add");
                map.put("attribute", selectFeatureAts);
                map.put("geometry", geom);
                map.put("layer", BaseActivity.myLayer.getLayer());
            }
			/* 添加小班后 记录添加小班的id 备撤销时删除 */
            //recordXb(id, "add", selectFeatureAts, geometry, BaseActivity.myLayer.getLayer());
        } catch (TableException e) {
            e.printStackTrace();
            ToastUtil.setToast(baseActivity, e.getMessage());
        }

        iBaseView.getMapView().invalidate();
        return map;
    }

    /**
     * 新增点
     */
    public Map<String, Object> addFeaturePoint(Point point_all) {
        Geometry geom = GeometryEngine.simplify(point_all, iBaseView.getSpatialReference());
        if (!geom.isValid()) {
            ToastUtil.setToast(baseActivity, "未选择添加位置");
            return new HashMap<>();
        }

        String pname = BaseActivity.myLayer.getPname();
        try {
            DecimalFormat format = new DecimalFormat("0.000000");
            GeodatabaseFeatureTable table = BaseActivity.myLayer.getTable();
            GeodatabaseFeature g = table.createFeatureWithTemplate(BaseActivity.layerTemplate, geom);
            List<Field> pointFields = g.getTable().getFields();
            for (Field field : pointFields) {
                if (field.getAlias().contains("横坐标") || field.getAlias().contains("经度")) {
                    baseActivity.layerFeatureAts.put(field.getName(), format.format(point_all.getX()));
                    continue;
                } else if (field.getAlias().contains("纵坐标") || field.getAlias().contains("纬度")) {
                    baseActivity.layerFeatureAts.put(field.getName(), format.format(point_all.getY()));
                    continue;
                } else if (pname.contains("营造林") && (field.getAlias().contains("样地类型") || field.getName().equals("YDLX"))) {
                    if (iBaseView.getCurrentPoint() != null && point_all.equals(iBaseView.getCurrentPoint())) {
                        baseActivity.layerFeatureAts.put(field.getName(), 2);
                    } else {
                        baseActivity.layerFeatureAts.put(field.getName(), 1);
                    }
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> maps = addFeatureOnLayer(geom, baseActivity.layerFeatureAts);
        baseActivity.mapRemove(new View(baseActivity));
        return maps;
    }

    /**
     * 检测面上的点 是否共线
     */
    public boolean checkGeom(Polygon polygon) {
        int size = polygon.getPointCount();
        Point spoint = polygon.getPoint(0);
        Point epoint = polygon.getPoint(size - 1);
        double aa = (epoint.getY() - spoint.getY()) / (epoint.getX() - spoint.getX());
        for (int i = 0; i < size - 2; i++) {
            Point p1 = polygon.getPoint(i);
            for (int j = i + 1; j < size - 1; j++) {
                Point p2 = polygon.getPoint(j);
                double bb = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
                if (bb != aa) {
                    return false;
                }
            }
        }
        return true;
    }

}
