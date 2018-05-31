package com.otitan.xnbhq.util;

import com.esri.core.geometry.SpatialReference;

/**
 * Created by otitan_li on 2018/4/28.
 * SpatialUtil
 */

public class SpatialUtil {

    public static SpatialReference defalutSpatialReference(){
        return SpatialReference.create(3857);
    }

    public static SpatialReference wgs_4326(){
        return SpatialReference.create(4326);
    }

    public static SpatialReference wgs_3857(){
        return SpatialReference.create(3857);
    }

    public static SpatialReference wgs_2343(){
        return SpatialReference.create(2343);
    }

}
