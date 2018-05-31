package com.otitan.xnbhq.mview;

import android.view.View;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

/**
 * Created by li on 2017/5/9.
 * base基类接口
 */

public interface IBaseView {

    //获取电子底图
    TiledLayer getBaseTitleLayer();
    //获取影像底图
    TiledLayer getImgTitleLayer();
    //获取地形图底图
    TiledLayer getDxtTitleLayer();

    Callout getCallout();

    double getCurrentLon();

    double getCurrenLat();

    Point getCurrentPoint();

    Point getGpsPoint();

    GraphicsLayer getGraphicLayer();

    void removeGraphicLayer();

    void addGraphicLayer();

    SpatialReference getSpatialReference();

    View getGpsCaijiInclude();

    MapView getMapView();

    Envelope getCurrentEnvelope();

}
