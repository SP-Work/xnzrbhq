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
 * 生态护林员
 */
public class SthlyActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView sthlyIsBack;
    RadioButton rbSthlyhj;
    TextView sthlyTimeTextview;
    TextView sthlyTimeContentView;
    WebView sthlyWebview;

    VHTableView sthlyTabview;
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
        childview = getLayoutInflater().inflate(R.layout.activity_sthly,null);
        setContentView(childview);

        mContext = SthlyActivity.this;

        initView();

        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.sthly);
        echart = optionImpl.initWebView(mContext, sthlyWebview, type,handler);
        keyType = getResources().getString(R.string.sthlyhj);
        echart.setYwType(keyType);
    }

    public void initView(){

        sthlyIsBack = (ImageView) childview.findViewById(R.id.sthly_isBack);
        rbSthlyhj = (RadioButton) childview.findViewById(R.id.rb_sthlyhj);
        sthlyTimeTextview = (TextView) childview.findViewById(R.id.sthly_time_textview);
        sthlyTimeContentView = (TextView) childview.findViewById(R.id.sthly_time_content_view);
        sthlyWebview = (WebView) childview.findViewById(R.id.sthly_webview);

        sthlyIsBack.setOnClickListener(this);
        rbSthlyhj.setOnClickListener(this);
        sthlyTimeTextview.setOnClickListener(this);
        timeid = R.string.time2;
        sthlyTimeTextview.setText(getResources().getString(timeid));
        sthlyTabview = (VHTableView) childview.findViewById(R.id.sthly_tabview);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sthly_isBack) {
            this.finish();

        } else if (i == R.id.rb_sthlyhj) {
            gotoHtml(R.string.sthlyhj);

        } else if (i == R.id.sthly_time_textview) {
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
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        sthlyWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
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
        sthlyTimeTextview.setText(txt);
        String dbname = echart.getName();
        sthlyWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.sthly){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initSthlyTabView(sthlyTabview,list);
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
                sthlyTimeContentView.setText(gzsh+"生态护林员"+keyType+"："+ Util.rounding(cfxe)+"人");
                break;
            }
        }
    }

}
