package com.otitan.xnbhq.googlemap;


import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

/**
 * @author wuxin
 *
 */
public class GoogleMapLayer extends TiledServiceLayer {

    private int GoogleMapLayerType;

    public GoogleMapLayer(int layerType) {
        super(true);
        this.GoogleMapLayerType = layerType;
        this.init();
    }

    private int minLevel = 0;
    private int maxLevel = 19;

    private double[] scales = new double[] { 591657527.591555,
            295828763.79577702, 147914381.89788899, 73957190.948944002,
            36978595.474472001, 18489297.737236001, 9244648.8686180003,
            4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
            288895.277144, 144447.638572, 72223.819286, 36111.909643,
            18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
            1128.4971760000001 };
    private double[] resolutions = new double[] { 156543.03392800014,
            78271.516963999937, 39135.758482000092, 19567.879240999919,
            9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
            1222.9924525624949, 611.49622628138, 305.748113140558,
            152.874056570411, 76.4370282850732, 38.2185141425366,
            19.1092570712683, 9.55462853563415, 4.7773142679493699,
            2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
            0.29858214164761665 };

    private Point origin = new Point(-20037508.342787, 20037508.342787);

    private int dpi = 96;

    private int tileWidth = 256;
    private int tileHeight = 256;

    private void init() {
        try {
            getServiceExecutor().submit(new Runnable() {
                public void run() {
                    GoogleMapLayer.this.initLayer();
                }
            });
        } catch (RejectedExecutionException rejectedexecutionexception) {
            Log.e("Google Map Layer", "initialization of the layer failed.",
                    rejectedexecutionexception);
        }
    }

    protected byte[] getTile(int level, int col, int row) throws Exception {
        if (level > maxLevel || level < minLevel) {
            return new byte[0];
        }

        String s = "Galileo".substring(0, ((3 * col + row) % 8));
        String url = "";

        switch (GoogleMapLayerType) {

            case GoogleMapLayerTypes.IMAGE_GOOGLE_MAP:

                url = "http://mt" + (col % 4) + ".google.com/vt/lyrs=s&hl=zh-CN&gl=cn&" +
                        "x=" + col + "&" +
                        "y=" + row + "&" +
                        "z=" + level + "&" +
                        "s=" + s;

                break;
            case GoogleMapLayerTypes.VECTOR_GOOGLE_MAP:

                url = "http://mt" + (col % 4) + ".google.com/vt/lyrs=m@158000000&hl=zh-CN&gl=cn&" +
                        "x=" + col + "&" +
                        "y=" + row + "&" +
                        "z=" + level + "&" +
                        "s=" + s;

                break;
            case GoogleMapLayerTypes.TERRAIN_GOOGLE_MAP:

                url = "http://mt" + (col % 4) + ".google.cn/vt/lyrs=t@131,r@227000000&hl=zh-CN&gl=cn&" +
                        "x=" + col + "&" +
                        "y=" + row + "&" +
                        "z=" + level + "&" +
                        "s=" + s;

                break;
            case GoogleMapLayerTypes.ANNOTATION_GOOGLE_MAP:

                url = "http://mt" + (col % 4) + ".google.com/vt/imgtp=png32&lyrs=h@169000000&hl=zh-CN&gl=cn&" +
                        "x=" + col + "&" +
                        "y=" + row + "&" +
                        "z=" + level + "&" +
                        "s=" + s;

                break;

        }
        Map<String, String> map = null;
        return com.esri.core.internal.io.handler.a.a(url, map);
    }

    protected void initLayer() {
        if (getID() == 0L) {
            nativeHandle = create();
            changeStatus(com.esri.android.map.event.OnStatusChangedListener.STATUS.fromInt(-1000));
        } else {
            this.setDefaultSpatialReference(SpatialReference.create(102113));
            this.setFullExtent(new Envelope(-22041257.773878,
                    -32673939.6727517, 22041257.773878, 20851350.0432886));
            this.setTileInfo(new TileInfo(origin, scales, resolutions,
                    scales.length, dpi, tileWidth, tileHeight));
            super.initLayer();
        }
    }

}