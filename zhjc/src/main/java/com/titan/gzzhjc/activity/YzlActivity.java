package com.titan.gzzhjc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.titan.gzzhjc.Echart;
import com.titan.gzzhjc.R;
import com.titan.gzzhjc.customview.PopupWindowCustom;
import com.titan.gzzhjc.impl.DbdataImpl;
import com.titan.gzzhjc.impl.OptionImpl;
import com.titan.gzzhjc.impl.PopupWindowImpl;
import com.titan.gzzhjc.impl.VHTableViewDataImpl;
import com.titan.gzzhjc.util.Util;
import com.titan.vhtableview.VHTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 营造林
 */
public class YzlActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mYzlback;
    RadioButton rbMj;
    RadioButton rbZj;
    TextView tvTime;
    TextView tvContent;
    WebView yzlWebview;


    VHTableView yzlTabview;
    Context mContext;
    Echart echart;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid = 0;
    String company = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_yzl_zhjc,null);
        setContentView(childview);

        mContext = YzlActivity.this;

        initview();

        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.yzl);
        echart = optionImpl.initWebView(mContext, yzlWebview, type,handler);
        keyType = getResources().getString(R.string.yzl_mj);
        echart.setYwType(keyType);
        company = getResources().getString(R.string.wanmu);
        echart.setCompany(company);
        echart.setTime(timeid);
    }

    private void initview() {

        mYzlback = (ImageView) childview.findViewById(R.id.yzl_back_view);
        rbMj = (RadioButton) childview.findViewById(R.id.rb_mj);
        rbZj = (RadioButton) childview.findViewById(R.id.rb_zj);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvContent = (TextView) childview.findViewById(R.id.tv_content);
        yzlWebview = (WebView) childview.findViewById(R.id.yzl_webview);

        mYzlback.setOnClickListener(this);
        rbMj.setOnClickListener(this);
        rbZj.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));
        yzlTabview = (VHTableView) childview.findViewById(R.id.yzl_tabview);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.yzl_back_view) {
            this.finish();

        } else if (i == R.id.rb_mj) {
            gotoHtml(R.string.yzl_mj);

        } else if (i == R.id.rb_zj) {
            gotoHtml(R.string.yzl_zj);

        } else if (i == R.id.tv_time) {
            PopupWindowImpl popupWindow = new PopupWindowImpl();
            popup = popupWindow.showTimeSel(mContext, childview, this);

        } else if (i == R.id.item_time_sewq) {
            reInitWebview((Button) view);

            reInitWebview((Button) view);

        } else if (i == R.id.item_time_elyl) {
            reInitWebview((Button) view);

        } else {

        }
    }

    /**
     * Webview数据更新（根据 底部时间选择弹窗 选择的时间，加载不同数据）
     * @param button
     */
    public void reInitWebview(Button button) {
        String txt = button.getText().toString();
        if(txt.equals(mContext.getResources().getString(R.string.time1))){
            timeid = R.string.time1;
        }else{
            timeid = R.string.time2;
        }
        echart.setTime(timeid);
        tvTime.setText(txt);
        String dbname = echart.getName();
        yzlWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        if(keyType.equals(mContext.getResources().getString(R.string.yzl_zj))){
            company = mContext.getResources().getString(R.string.wanyuan);
        }else{
            company = mContext.getResources().getString(R.string.wanmu);
        }
        echart.setCompany(company);
        yzlWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.yzl){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initYzlTabView(yzlTabview,list,company,keyType);
                /** 顶部描述 */
                changContent();
            }
        }
    };
    /**
     * 顶部描述
     */
    public void changContent(){
        for(Map<String, String> map : list){
            String dqmc = map.get(mContext.getResources().getString(R.string.dqname));
            String gzsh = echart.getName();
            if(gzsh.equals(dqmc.trim())){
                String mj1 = map.get(DbdataImpl.colums[1]);
//                if(keyType.contains("面积")){
//                    double m1 = Double.parseDouble(Util.round(mj1));
//                    tvContent.setText(gzsh+keyType+"："+ Util.round(mj1)+company);
//                }else{
//                    double m1 = Double.parseDouble(Util.round(mj1));
//
//                }
                tvContent.setText(gzsh+keyType+"："+ Util.round(mj1)+company);
                break;
            }
        }
    }

}
