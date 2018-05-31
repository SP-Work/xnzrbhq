package com.otitan.xnbhq.presenter;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteDirection;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.mview.INavigatView;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/5/9.
 *
 * 导航Presenter
 */

public class NavigationPresenter {

    private Context mContext;
    private MapView mapView;
    private INavigatView iNavigatView;

    public NavigationPresenter(Context ctx, INavigatView navigatView){
        this.mContext = ctx;
        this.mapView = navigatView.getMapView();
        this.iNavigatView = navigatView;
    }

    /** 初始化导航数据 */
    public RouteTask initRoutAndGeocoding() {
        // We will spin off the initialization in a new thread
        String networkPath = "/otitan/wu.geodatabase";
        String networkName = "roads_ND";
        // Attempt to load the local geocoding and routing data
        try {
            String path = MyApplication.resourcesManager.getNavigationDataPath(networkPath);
            RouteTask mRouteTask = RouteTask.createLocalRouteTask(path, networkName);
            return mRouteTask;
        } catch (Exception e) {
            ToastUtil.setToast(mContext, "Error while initializing :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private int routeHandle = -1;
    /** 导航结果路线勾绘 */
    public void drawLineToMap(Point point, Polyline polyline_nav) {
        if (iNavigatView.getGraphicLayer() == null) {
            iNavigatView.addGraphicLayer();
        }
        PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.nav_route_result_end_point));
        picSymbol.setOffsetY(18);
        Graphic graphic = new Graphic(point, picSymbol);
        iNavigatView.getGraphicLayer().addGraphic(graphic);
        StopGraphic stop = new StopGraphic(graphic);
        iNavigatView.getmStops().addFeature(stop);

        // 路径导航
        // Return default behavior if we did not initialize properly.
        if (iNavigatView.getRouteTask() == null) {
            ToastUtil.setToast(mContext, "导航出错");
            return;
        }

        try {
            // Set the correct input spatial reference on the stops and the
            // desired output spatial reference on the RouteParameters object.
            SpatialReference mapRef = iNavigatView.getSpatialReference();
            RouteParameters params = iNavigatView.getRouteTask().retrieveDefaultRouteTaskParameters();

            params.setOutSpatialReference(mapRef);
            iNavigatView.getmStops().setSpatialReference(mapRef);

            // Set the stops and since we want driving directions,
            // returnDirections==true
            params.setStops(iNavigatView.getmStops());
            params.setReturnDirections(true);
            params.setDirectionsLanguage("zh-CN");

            // Perform the solve
            RouteResult results = iNavigatView.getRouteTask().solve(params);

            // Grab the results; for offline routing, there will only be one
            // result returned on the output.
            Route result = results.getRoutes().get(0);

            // Remove any previous route Graphics
            if (routeHandle != -1)
                iNavigatView.getGraphicLayer().removeGraphic(routeHandle);

            // Add the route shape to the graphics layer
            final Geometry geom = result.getRouteGraphic().getGeometry();

            Polyline polyline = (Polyline) geom;
            int count = polyline.getPointCount();
            for (int i = 0; i < count; i++) {
                polyline_nav.lineTo(polyline.getPoint(i));
            }
            polyline_nav.lineTo(point);

            // simpleLineSymbol=new SimpleLineSymbol(Color.BLUE,5);
            routeHandle = iNavigatView.getGraphicLayer().addGraphic(new Graphic(
                    polyline_nav, new SimpleLineSymbol(Color.BLUE, 5)));

            List<RouteDirection> directions = result.getRoutingDirections();
            List<String> formattedDirections = new ArrayList<String>();
            for (int i = 0; i < directions.size(); i++) {
                RouteDirection direction = directions.get(i);
                formattedDirections.add(String.format(
                        "%s\nGo %.2f %s For %.2f Minutes", direction.getText(),
                        direction.getLength(), params.getDirectionsLengthUnit()
                                .name(), direction.getMinutes()));
            }

            Polygon geometry = GeometryEngine.buffer(polyline_nav, iNavigatView.getSpatialReference(), 500, null);
            mapView.setExtent(geometry);

            // Add a summary String
            formattedDirections.add(0,String.format("Total time: %.2f Mintues",result.getTotalMinutes()));

        } catch (Exception e) {
            ToastUtil.setToast(mContext, "Solve Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
