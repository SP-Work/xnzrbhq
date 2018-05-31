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
import com.titan.gzzhjc.impl.OptionImpl;
import com.titan.gzzhjc.impl.PopupWindowImpl;
import com.titan.gzzhjc.impl.VHTableViewDataImpl;
import com.titan.gzzhjc.util.Util;
import com.titan.vhtableview.VHTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 林业产业
 */
public class LycyActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mLycy_back;
    RadioButton rbLycyhj;
    RadioButton rbDycy;
    RadioButton rbDecy;
    RadioButton rbDscy;
    TextView tvTime;
    TextView tvContent;
    WebView lycyWebview;

    VHTableView lycyTabview;
    Context mContext;
    Echart echart;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_lycy,null);
        setContentView(childview);

        mContext = LycyActivity.this;

        initview();

        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.lycy);
        echart = optionImpl.initWebView(mContext, lycyWebview, type,handler);
        keyType = getResources().getString(R.string.lycyhj);
        echart.setYwType(keyType);
    }

    private void initview() {

        mLycy_back =(ImageView) childview.findViewById(R.id.lycy_back);
        rbLycyhj =(RadioButton) childview.findViewById(R.id.rb_lycyhj);
        rbDycy =(RadioButton) childview.findViewById(R.id.rb_dycy);
        rbDecy =(RadioButton) childview.findViewById(R.id.rb_decy);
        rbDscy =(RadioButton) childview.findViewById(R.id.rb_dscy);
        tvTime =(TextView) childview.findViewById(R.id.tv_time);
        tvContent =(TextView) childview.findViewById(R.id.tv_content);
        lycyWebview =(WebView) childview.findViewById(R.id.lycy_webview);

        mLycy_back.setOnClickListener(this);
        rbLycyhj.setOnClickListener(this);
        rbDecy.setOnClickListener(this);
        rbDycy.setOnClickListener(this);
        rbDscy.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));

        lycyTabview = (VHTableView) childview.findViewById(R.id.lycy_tabview);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.lycy_back) {
            this.finish();

        } else if (i == R.id.rb_lycyhj) {
            gotoHtml(R.string.lycyhj);

        } else if (i == R.id.rb_dycy) {
            gotoHtml(R.string.dycy);

        } else if (i == R.id.rb_decy) {
            gotoHtml(R.string.decy);

        } else if (i == R.id.rb_dscy) {
            gotoHtml(R.string.dscy);

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
        lycyWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        lycyWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.lycy){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initLycyTabView(lycyTabview,list);
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
                String cfxe = map.get(keyType);
                if(keyType.contains("合计")){
                    tvContent.setText(gzsh+"林业产业"+keyType+"："+ Util.round(cfxe)+"万元");
                }else{
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万元");
                }
                break;
            }
        }
    }
}
