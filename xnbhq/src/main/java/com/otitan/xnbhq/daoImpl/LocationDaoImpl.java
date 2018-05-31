package com.otitan.xnbhq.daoImpl;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.dao.ILocationDao;

/**
 * Created by li on 2017/3/14.
 *
 * 定位工具类
 */

public class LocationDaoImpl implements ILocationDao {

    @Override
    public void initLocation(Context context, LocationClient client, BaseActivity.MyLocationListenner listenner) {
        client = new LocationClient(context);
        client.registerLocationListener(listenner);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型bd09ll gcj02 GCJ-02
        option.setScanSpan(1000);
        client.setLocOption(option);
        client.start();
    }
}
