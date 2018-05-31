package com.titan.gzzhjc.dao;

import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;

import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Series;
import com.titan.gzzhjc.Echart;

import java.util.List;

/**
 * Created by li on 2017/3/3
 *
 * 地图接口
 */

public interface OptionDao {

    /**/
    GsonOption getTabOption();

    Echart initWebView(Context context, WebView webView, String type, Handler handler);

    Series getMapSeries(String name, List<Data> datas);


}
