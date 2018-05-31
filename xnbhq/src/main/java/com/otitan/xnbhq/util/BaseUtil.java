package com.otitan.xnbhq.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.entity.MyLayer;

import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/6/1.
 * BaseActivity对应的业务工具类
 */

public class BaseUtil {

    private Context mContext;
    private static BaseUtil instance;

    public BaseUtil(Context context){
        this.mContext = context;
    }
    public static synchronized BaseUtil getIntance(Context context){
        if(instance == null){
            instance = new BaseUtil(context);
        }
        return instance;
    }

    /**
     * 获取操作小班所在 featureLayer
     */
    public MyLayer getFeatureInLayer(GeodatabaseFeature feature,List<MyLayer> layerNameList,Map<GeodatabaseFeature, String> selMap) {
        MyLayer myLayer = null;
        for (MyLayer layer : layerNameList) {
            String name = layer.getTable().getTableName();
            String tbname = feature.getTable().getTableName();
            String cname = layer.getCname();
            String keyname = selMap.get(feature);
            if (name.equals(tbname) && cname.equals(keyname)) {
                myLayer = layer;
                SytemUtil.getEditSymbo((BaseActivity) mContext, layer.getLayer());
                break;
            }
        }
        return myLayer;
    }

    /**
     * 动态设置控件的高度
     */
    public static void setHeight(BaseAdapter adapter, ListView listview) {
        int listViewHeight = 0;
        int adaptCount = adapter.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = adapter.getView(i, null, listview);
            temp.measure(0, 0);
            listViewHeight += temp.getMeasuredHeight();
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listview.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.FILL_PARENT;
        if (listViewHeight < MyApplication.screen.getHeightPixels() * 0.55) {
            layoutParams.height = listViewHeight;
        } else {
            layoutParams.height = (int) (MyApplication.screen.getHeightPixels() * 0.55);
        }
        listview.setLayoutParams(layoutParams);
    }

    /**
     * 添加graphic到地图上
     */
    public void addGraphicToMap(Geometry geometry, GraphicsLayer graphicsLayer) {
        graphicsLayer.removeAll();
        if (geometry.getType().equals(Geometry.Type.POLYLINE)) {
            Polyline line = (Polyline) geometry;
            int size = line.getPointCount();
            for (int i = 0; i < size; i++) {
                Point point = line.getPoint(i);
                TextSymbol textSymbol = new TextSymbol(20, i + "", Color.YELLOW);
                SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(Color.YELLOW, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
                Graphic graphic = new Graphic(point, markerSymbol);
                graphicsLayer.addGraphic(graphic);

                Polyline line1 = new Polyline();
                line1.startPath(point);
                if (i < size - 1) {
                    line1.lineTo(line.getPoint(i + 1));
                    double len = line1.calculateLength2D();
                }
            }
            LineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 4);// 248,116,14
            graphicsLayer.addGraphic(new Graphic(geometry, lineSymbol));
        }

        if (geometry.getType().equals(Geometry.Type.POLYGON)) {
            Polygon polygon = (Polygon) geometry;
            for (int i = 0; i < polygon.getPointCount(); i++) {
                Point point = polygon.getPoint(i);
                TextSymbol textSymbol = new TextSymbol(20, i + "", Color.YELLOW);
                Graphic graphic = new Graphic(point, textSymbol);
                graphicsLayer.addGraphic(graphic);
            }
            LineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 4);// 248,116,14
            FillSymbol fillSymbol = new SimpleFillSymbol(Color.BLACK);
            fillSymbol.setOutline(lineSymbol);
            fillSymbol.setAlpha(50);
            graphicsLayer.addGraphic(new Graphic(geometry, fillSymbol));
        }

        if (geometry.getType().equals(Geometry.Type.POINT)) {
            SimpleMarkerSymbol textSymbol = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
            Graphic graphic = new Graphic(geometry, textSymbol);
            graphicsLayer.addGraphic(graphic);
            MarkerSymbol markerSymbol = new SimpleMarkerSymbol(Color.BLACK, 16, SimpleMarkerSymbol.STYLE.CIRCLE);
            graphicsLayer.addGraphic(new Graphic(geometry, markerSymbol));
        }
    }

    /**
     * 检查多个小班是否同属于一个图层
     */
    public static boolean checkGeoFeature(List<GeodatabaseFeature> selGeoFeaturesList) {
        boolean flag = true;
        GeodatabaseFeature feature = selGeoFeaturesList.get(0);
        String tbname = feature.getTable().getTableName();
        for (GeodatabaseFeature f : selGeoFeaturesList) {
            String tbn = f.getTable().getTableName();
            if (tbname.equals(tbn)) {
                flag = true;
            } else {
                return false;
            }
        }
        return flag;
    }


}
