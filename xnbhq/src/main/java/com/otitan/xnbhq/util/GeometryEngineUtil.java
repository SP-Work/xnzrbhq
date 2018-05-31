package com.otitan.xnbhq.util;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;

/**
 * Created by otitan_li on 2018/5/4.
 * GeometryEngineUtil
 * 主要进行数据投影转换
 */

public class GeometryEngineUtil {

    public static Point getPoint3857(Point point){
        return (Point) GeometryEngine.project(point,SpatialUtil.wgs_4326(), SpatialUtil.wgs_3857());
    }

    public static Polygon getPolygon2343(Polygon polygon){
        return (Polygon) GeometryEngine.project(polygon,SpatialUtil.wgs_2343(),SpatialUtil.defalutSpatialReference());
    }

}
