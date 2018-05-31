package com.titan.gzzhjc.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.titan.gzzhjc.R;


/**
 * 贵州林情
 */
public class GzlqActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    ImageView imgBack;
    WebView gzlqImageview;
    RadioButton rbShew;
    RadioButton rbElyl;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gzlq);
        context = GzlqActivity.this;

        initview();

        url = "file:///android_asset/shew.html";
        goToHtml(url);
    }

    private void initview(){
        imgBack = (ImageView) findViewById(R.id.img_back);
        rbShew =(RadioButton) findViewById(R.id.rb_shew);
        rbElyl =(RadioButton) findViewById(R.id.rb_elyl);
        gzlqImageview =(WebView) findViewById(R.id.gzlq_imageview);

        rbShew.setOnClickListener(this);
        rbElyl.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    /**初始化控件*/
    public void initWebview(WebView gzlqImageview,String url) {
        //设置编码
        gzlqImageview.getSettings().setDefaultTextEncodingName("utf-8");
        // 设置可以支持缩放
        gzlqImageview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        //wvForest.getSettings().setBuiltInZoomControls(false);
        // 清除浏览器缓存
        gzlqImageview.clearCache(true);
        //开启脚本支持
        gzlqImageview.getSettings().setJavaScriptEnabled(true);
        gzlqImageview.getSettings().setAllowFileAccess(true);
        gzlqImageview.getSettings().setDomStorageEnabled(true);
        //跨域访问
        gzlqImageview.getSettings().setAllowUniversalAccessFromFileURLs(true);//设置后setAllowFileAccessFromFileURLs 不用设置了
        //wvForest.getSettings().setAllowFileAccessFromFileURLs(true);
        //webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        //String dqname = context.getResources().getString(R.string.gzsh);
        //gzlqImageview.addJavascriptInterface(echart, "Android");
        gzlqImageview.loadUrl(url);
        //在当前页面打开链接了
        gzlqImageview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //js加上这个就好啦！
        gzlqImageview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    /**加载html页面*/
    public void goToHtml(String url){
        initWebview(gzlqImageview,url);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.img_back) {
            finish();

        } else if (i == R.id.rb_shew) {
            url = "file:///android_asset/shew.html";
            goToHtml(url);

        } else if (i == R.id.rb_elyl) {
            url = "file:///android_asset/index.html";
            goToHtml(url);

        } else {
        }
    }
}
