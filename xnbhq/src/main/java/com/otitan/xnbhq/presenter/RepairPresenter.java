package com.otitan.xnbhq.presenter;

import android.content.Context;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.mview.IBaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/6/2.
 * 修班RepairPresenter
 */

public class RepairPresenter {

    private Context mContext;
    private IBaseView iBaseView;
    private BasePresenter basePresenter;

    public RepairPresenter(Context context, IBaseView baseView,BasePresenter basePresenter){
        this.mContext = context;
        this.iBaseView = baseView;
        this.basePresenter = basePresenter;
    }


    /**一条path时小班修改保存*/
    public Graphic saveXBoPathFeature(Polyline drawline, Polygon selectPpolygon) {
        Graphic reGraphic = null;
        int drawSize = drawline.getPointCount();

        int selsize = selectPpolygon.getPointCount();//98
        Point[] vertices = new Point[selsize];
        for (int i = 0; i < selsize; i++) {
            vertices[i] = selectPpolygon.getPoint(i);
        }
        //addGraphicToMap(drawline);
        //记录交点 及交点两边的点
        List<HashMap<String, Object>> nodeList = new ArrayList<HashMap<String, Object>>();
        //记录 所画线段 与交点 形成一个新的线
        List<HashMap<String, Object>> polygonList = new ArrayList<HashMap<String, Object>>();

        if (drawSize > 2) {
            boolean result0 = false;
            boolean result1 = false;
            for (int i = 0; i < drawSize - 1; i++) {
                Point point = drawline.getPoint(i);
                Point pointup = drawline.getPoint(i + 1);
                MultiPath multiPath2 = new Polyline();
                multiPath2.startPath(point);
                multiPath2.lineTo(pointup);
                //判断线段两个端点是在多边形内部或者外部
                if (i == 0) {
                    result0 = GeometryEngine.intersects(point, selectPpolygon, iBaseView.getSpatialReference());
                } else {
                    result0 = result1;
                }
                result1 = GeometryEngine.intersects(pointup, selectPpolygon, iBaseView.getSpatialReference());

                Polyline polyline = (Polyline) GeometryEngine.intersect(selectPpolygon, multiPath2, iBaseView.getSpatialReference());
                if (polyline.getPointCount() > 0) {
                    // 两点连接的线段与多边形有交点
                    if (result0 && result1) {//两点在多边形内部
                        HashMap<String, Object> map1 = new HashMap<String, Object>();
                        map1.put("point", point);
                        map1.put("jiaodian", false);
                        map1.put("index", i);
                        polygonList.add(map1);
                        if (i == drawSize - 2) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("point", pointup);
                            map.put("jiaodian", false);
                            map.put("index", i);
                            polygonList.add(map);
                        }
                    } else if (result0 && !result1) {//point点在多边形内部
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("point", polyline.getPoint(1));
                        map.put("jiaodian", true);
                        map.put("index", 1);
                        polygonList.add(map);
                        nodeList.add(map);

                        HashMap<String, Object> map1 = new HashMap<String, Object>();
                        map1.put("point", point);
                        map1.put("jiaodian", false);
                        map1.put("index", i);
                        polygonList.add(map1);

                        if (i == drawSize - 2) {
                            HashMap<String, Object> map0 = new HashMap<String, Object>();
                            map0.put("point", pointup);
                            map0.put("jiaodian", false);
                            map0.put("index", i);
                            polygonList.add(map0);
                        }
                    } else if (!result0 && result1) {//pointup点在多边形内部
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("point", polyline.getPoint(0));
                        map.put("jiaodian", true);
                        map.put("index", 0);
                        polygonList.add(map);
                        nodeList.add(map);

                        HashMap<String, Object> map1 = new HashMap<String, Object>();
                        map1.put("point", point);
                        map1.put("jiaodian", false);
                        map1.put("index", i);
                        polygonList.add(map1);

                        if (i == drawSize - 2) {
                            HashMap<String, Object> map0 = new HashMap<String, Object>();
                            map0.put("point", pointup);
                            map0.put("jiaodian", false);
                            map0.put("index", i);
                            polygonList.add(map0);
                        }
                    }
                } else {
                    // 两点连接成的线段与多边形没有交点
                    HashMap<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("point", point);
                    map1.put("jiaodian", false);
                    map1.put("index", i);//在草图中的序列
                    polygonList.add(map1);
                }
            }
        }

        int jdsize = nodeList.size();
        if (jdsize < 2) {
            return null;
        }
        //lst 记录首尾交点的
        List<HashMap<String, Object>> lst = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map0 = new HashMap<String, Object>();
        map0.put("point", nodeList.get(0).get("point"));
        map0.put("jiaodian", true);
        map0.put("index", 0);
        lst.add(map0);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("point", nodeList.get(nodeList.size() - 1).get("point"));
        map1.put("jiaodian", true);
        map1.put("index", nodeList.size() - 1);
        lst.add(map1);

        // 拥有两个以上交点
        if (jdsize > 2) {
            ArrayList<Integer> jdlist = new ArrayList<Integer>();
            int j0 = -1;
            int j1 = -1;
            int j3 = -1;
            int j4 = -1;
            for (int i = 0; i < nodeList.size(); i++) {
                for (int j = 0; j < selsize; j++) {
                    Point point = selectPpolygon.getPoint(j);
                    Point pointNext = null;
                    if (j == selsize - 1) {
                        pointNext = selectPpolygon.getPoint(0);
                    } else {
                        pointNext = selectPpolygon.getPoint(j + 1);
                    }
                    Polyline polyline = new Polyline();
                    polyline.startPath(point);
                    polyline.lineTo(pointNext);
                    boolean result = GeometryEngine.intersects((Point) nodeList.get(i).get("point"), polyline, iBaseView.getSpatialReference());
                    if (result) {
                        if (j != selsize - 1) {
                            jdlist.add(j);
                            jdlist.add(j + 1);
                        } else {
                            jdlist.add(j);
                            jdlist.add(0);
                        }
                        if (i == 0) {
                            j0 = j;
                            if (j == selsize - 1) {
                                j1 = 0;
                            } else {
                                j1 = j + 1;
                            }
                            break;
                        } else if (i == nodeList.size() - 1) {
                            j3 = j;
                            if (j == selsize - 1) {
                                j4 = 0;
                            } else {
                                j4 = j + 1;
                            }
                            break;
                        }
                    }
                }
            }

            Polyline path = new Polyline();
            Point point00 = (Point) nodeList.get(0).get("point");
            Point point11 = (Point) nodeList.get(nodeList.size() - 1).get("point");
            boolean start = false;
            boolean end = false;
            for (int n = 0; n < polygonList.size(); n++) {
                Point point2 = (Point) polygonList.get(n).get("point");
                if (point00.equals(point2)) {
                    start = true;
                    path.startPath(point00);
                    continue;
                } else if (point11.equals(point2)) {
                    end = true;
                }
                if (start) {
                    path.lineTo(point2);
                }
                if (end) {
                    break;
                }
            }

            int min = 0, max = 0;
            for (int i = 0; i < jdlist.size(); i++) {
                if (min > jdlist.get(i))
                    min = jdlist.get(i);
                if (max < jdlist.get(i))
                    max = jdlist.get(i);
            }

            if (j3 >= j1) {
                if (j4 == max) {
                    for (int i = j4; i < selsize; i++) {
                        path.lineTo(selectPpolygon.getPoint(i));
                    }
                    for (int i = 0; i < j1; i++) {
                        path.lineTo(selectPpolygon.getPoint(i));
                    }
                } else {
                    for (int i = j3; i > j0; i--) {
                        path.lineTo(selectPpolygon.getPoint(i));
                    }
                }
            } else {
                if (j1 == max) {
                    for (int i = j4; i >= 0; i--) {
                        path.lineTo(selectPpolygon.getPoint(i));
                    }
                    for (int i = selsize - 1; i > j1; i--) {
                        path.lineTo(selectPpolygon.getPoint(i));
                    }
                } else {
                    for (int i = j4; i > j1; i--) {
                        path.lineTo(selectPpolygon.getPoint(i));
                    }
                }
            }

            Polygon polygon = new Polygon();
            polygon.add(path, true);
            //addGraphicToMap(polygon);

            reGraphic = basePresenter.updateFeature(polygon, BaseActivity.selGeoFeature, BaseActivity.myLayer);

        } else if (jdsize == 2) {
            //boolean nodestar = ArcGISUtils.containsPoint(drawline.getPoint(0),vertices);
            //boolean nodeend = ArcGISUtils.containsPoint(drawline.getPoint(drawSize - 1), vertices);

            boolean nodestar = GeometryEngine.intersects(drawline.getPoint(0), selectPpolygon, iBaseView.getSpatialReference());
            boolean nodeend = GeometryEngine.intersects(drawline.getPoint(drawSize - 1), selectPpolygon, iBaseView.getSpatialReference());
            if (nodestar && nodeend) {// 此时两点在多边形内
                boolean isFirst = true;
                int j0 = -1;
                int j1 = -1;
                int j3 = -1;
                int j4 = -1;
                for (int i = 0; i < jdsize; i++) {
                    Point point = null;
                    Point pointNext = null;
                    for (int j = 0; j < selsize; j++) {
                        if (j == 0) {
                            point = selectPpolygon.getPoint(0);

                        } else {
                            point = pointNext;
                        }
                        if (j == selsize - 1) {
                            pointNext = selectPpolygon.getPoint(0);
                        } else {
                            pointNext = selectPpolygon.getPoint(j + 1);
                        }
                        Polyline polyline = new Polyline();
                        polyline.startPath(point);
                        polyline.lineTo(pointNext);
                        boolean istrue = GeometryEngine.intersects(
                                (Point) nodeList.get(i).get("point"), polyline, iBaseView.getSpatialReference());

                        if (istrue) {
                            if (isFirst) {
                                j0 = j;
                                if (j == selsize - 1) {
                                    j1 = 0;
                                } else {
                                    j1 = j + 1;
                                }
                                isFirst = false;
                            } else {
                                j3 = j;
                                if (j == selsize - 1) {
                                    j4 = 0;
                                } else {
                                    j4 = j + 1;
                                }
                            }

                            break;
                        }
                    }
                }
                //addGraphicToMap(multiPath2);
                Polyline multiPath2 = new Polyline();
                Point point00 = (Point) nodeList.get(0).get("point");
                Point point11 = (Point) nodeList.get(nodeList.size() - 1).get("point");
                boolean start = false;
                boolean end = false;
                for (int n = 0; n < polygonList.size(); n++) {
                    Point point2 = (Point) polygonList.get(n).get("point");
                    if (point00.equals(point2)) {
                        start = true;
                        multiPath2.startPath(point00);
                        continue;
                    } else if (point11.equals(point2)) {
                        end = true;
                    }
                    if (start) {
                        multiPath2.lineTo(point2);
                    }
                    if (end) {
                        break;
                    }
                }
                if (j1 > j3) {//逆向
                    for (int i = j3; i >= 0; i--) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                    for (int i = selsize - 1; i > j0; i--) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                } else {//同向
                    for (int i = j4; i < selsize; i++) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                    for (int i = 0; i < j1; i++) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                }

                Polygon polygon = new Polygon();
                polygon.add(multiPath2, true);
                //addGraphicToMap(multiPath2);

                Geometry geometry = GeometryEngine.difference(polygon, selectPpolygon, iBaseView.getSpatialReference());
                //addGraphicToMap(geometry);

                Geometry[] geometrys = new Geometry[2];
                geometrys[1] = selectPpolygon;
                geometrys[0] = geometry;
                Geometry geometry1 = GeometryEngine.union(geometrys, iBaseView.getSpatialReference());

                //addGraphicToMap(geometry1);
                reGraphic = basePresenter.updateFeature(geometry1, BaseActivity.selGeoFeature, BaseActivity.myLayer);

            } else if (!nodestar && !nodeend) {// 此时两点在多边形外部
                boolean isFirst = true;
                int j0 = -1;
                int j1 = -1;
                int j3 = -1;
                int j4 = -1;
                for (int i = 0; i < jdsize; i++) {
                    Point point = null;
                    Point pointNext = null;
                    for (int j = 0; j < selsize; j++) {

                        if (j == 0) {
                            point = selectPpolygon.getPoint(0);
                        } else {
                            point = pointNext;
                        }
                        if (j == selsize - 1) {
                            pointNext = selectPpolygon.getPoint(0);
                        } else {
                            pointNext = selectPpolygon.getPoint(j + 1);
                        }
                        Polyline polyline = new Polyline();
                        polyline.startPath(point);
                        polyline.lineTo(pointNext);
                        boolean istrue = GeometryEngine.intersects(
                                (Point) nodeList.get(i).get("point"),
                                polyline, iBaseView.getSpatialReference());
                        if (istrue) {
                            if (isFirst) {
                                j0 = j;
                                if (j == selsize - 1) {
                                    j1 = 0;
                                } else {
                                    j1 = j + 1;
                                }
                                isFirst = false;
                            } else {
                                j3 = j;
                                if (j == selsize - 1) {
                                    j4 = 0;
                                } else {
                                    j4 = j + 1;
                                }
                            }
                            break;
                        }
                    }
                }
                MultiPath multiPath2 = new Polyline();
                //addGraphicToMap(multiPath2);
                Point point00 = (Point) nodeList.get(0).get("point");
                Point point11 = (Point) nodeList.get(1).get("point");
                boolean start = false;
                boolean end = false;
                for (int n = 0; n < polygonList.size(); n++) {
                    Point point2 = (Point) polygonList.get(n).get("point");
                    if (point00.equals(point2)) {
                        start = true;
                        multiPath2.startPath(point00);
                        continue;
                    } else if (point11.equals(point2)) {
                        end = true;
                    }
                    if (start) {
                        multiPath2.lineTo(point2);
                    }
                    if (end) {
                        break;
                    }
                }
                if (j1 > j3) {//逆向
                    for (int i = j3; i >= 0; i--) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                    for (int i = selsize - 1; i > j0; i--) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                } else {
                    for (int i = j4; i < selsize; i++) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                    for (int i = 0; i < j1; i++) {
                        multiPath2.lineTo(selectPpolygon.getPoint(i));
                    }
                }

                //addGraphicToMap(multiPath2);

                Polygon polygon = new Polygon();
                polygon.add(multiPath2, true);
                Geometry geometry1 = GeometryEngine.difference(selectPpolygon, polygon, iBaseView.getSpatialReference());
                Geometry geometry2 = GeometryEngine.difference(selectPpolygon, geometry1, iBaseView.getSpatialReference());

                //addGraphicToMap(geometry1);
                //addGraphicToMap(geometry2);

                double length1 = Math.abs(geometry1.calculateLength2D());
                double length2 = Math.abs(geometry2.calculateLength2D());

                if (length1 > length2) {
                    reGraphic = basePresenter.updateFeature(geometry1, BaseActivity.selGeoFeature, BaseActivity.myLayer);
                } else {
                    reGraphic = basePresenter.updateFeature(geometry2, BaseActivity.selGeoFeature, BaseActivity.myLayer);
                }
            }
        }
        return reGraphic;
    }


}
