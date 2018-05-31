package com.titan.gzzhjc.impl;

import android.content.Context;
import android.os.Handler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.abel533.echarts.code.SelectedMode;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Emphasis;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.titan.gzzhjc.Echart;
import com.titan.gzzhjc.MySeries;
import com.titan.gzzhjc.R;
import com.titan.gzzhjc.dao.OptionDao;
import com.titan.gzzhjc.util.ResourcesManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by li on 2017/3/3
 *
 * WebView 地图数据加载
 */

public class OptionImpl implements OptionDao {

    @Override
    public Series getMapSeries(String name, List<Data> datas) {
        MySeries series = new MySeries();
        series.setName(name);
        series.setType(SeriesType.map);
        series.setData(datas);
        SelectedMode mode = SelectedMode.multiple;
        series.setSelectedMode(mode);
        ItemStyle itemStyle = new ItemStyle();
        Emphasis emphasis = new Emphasis();
        emphasis.setShow(true);
        itemStyle.setEmphasis(emphasis);
        Normal normal = new Normal();
        normal.setShow(true);
        itemStyle.setNormal(normal);
        series.setLabel(itemStyle);
        series.setMaptype(name);

        return series;
    }

    @Override
    public Echart initWebView(Context context, WebView wvForest, String type,Handler handler) {
        //设置编码
        wvForest.getSettings().setDefaultTextEncodingName("utf-8");
        // 设置可以支持缩放
        wvForest.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wvForest.getSettings().setBuiltInZoomControls(false);
        // 清除浏览器缓存
        wvForest.clearCache(true);
        //开启脚本支持
        wvForest.getSettings().setJavaScriptEnabled(true);
        wvForest.getSettings().setAllowFileAccess(true);
        wvForest.getSettings().setDomStorageEnabled(true);
        //跨域访问
        wvForest.getSettings().setAllowUniversalAccessFromFileURLs(true);//设置后setAllowFileAccessFromFileURLs 不用设置了
        //wvForest.getSettings().setAllowFileAccessFromFileURLs(true);
        //webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        String dqname =context.getResources().getString(R.string.gzsh);
        Echart echart = new Echart(context,dqname,type,handler);
        wvForest.addJavascriptInterface(echart, "Android");
        wvForest.loadUrl("file:///android_asset/demo.html");
        //在当前页面打开链接了
        wvForest.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //js加上这个就好啦！
        wvForest.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        return echart;
    }

    @Override
    public GsonOption getTabOption() {
        GsonOption option = new GsonOption();

        return option;
    }

    public File getFileByName(Context context, String name) {

        File file = null;
        String path = ResourcesManager.getInstance(context).getFolderPath(ResourcesManager.city);
        File[] files = new File(path).listFiles();
        for (File ff : files) {
            if (ff.getName().contains(name)) {
                file = ff;
                return ff;
            }
        }
        return file;
    }

    /**
     * 读取json文件
     */
    public String jsonRead(File file) {
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "GBK");//utf-8
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        //返回字符串
        return buffer.toString();
    }

}
