package com.titan.gzzhjc;

import android.webkit.JavascriptInterface;

import com.github.abel533.echarts.json.GsonOption;

/**
 * Created by whs on 2017/3/1
 */
public  interface WebAppInterface {

    /**
     * 获取
     *
     * @return
     */
    @JavascriptInterface
    public String getLineChartOptions(); /*{
        GsonOption option = markLineChartOptions();
        LogUtils.d(option.toString());
        return option.toString();
    }*/

    @JavascriptInterface
    public void showToast(String msg); /*{
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }*/
    @JavascriptInterface
    public GsonOption setGsonOption(String json);



}
