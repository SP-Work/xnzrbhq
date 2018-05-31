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
 * 林权改革
 */
public class LqggActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mLqgg_back;
    RadioButton rbGgmj;
    RadioButton rbSjnh;
    RadioButton rbSjrk;
    RadioButton rbSlbx;
    RadioButton rbLqdy;
    RadioButton rbLxzz;
    TextView tvTime;
    TextView tvContent;
    WebView lqggWebview;


    VHTableView lqggTabview;
    Echart echart;
    Context mContext;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_lqgg,null);
        setContentView(childview);

        mContext = LqggActivity.this;

        initview();

        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.lqgg);
        echart = optionImpl.initWebView(mContext, lqggWebview, type,handler);
        keyType = getResources().getString(R.string.lgmj);
        echart.setYwType(keyType);

    }

    private void initview() {

        mLqgg_back =(ImageView) childview.findViewById(R.id.lqgg_back);
        rbGgmj =(RadioButton) childview.findViewById(R.id.rb_ggmj);
        rbSjnh =(RadioButton) childview.findViewById(R.id.rb_sjnh);
        rbSjrk =(RadioButton)childview.findViewById(R.id.rb_sjrk);
        rbSlbx =(RadioButton) childview.findViewById(R.id.rb_slbx);
        rbLqdy =(RadioButton) childview.findViewById(R.id.rb_lqdy);
        rbLxzz =(RadioButton) childview.findViewById(R.id.rb_lxzz);
        tvTime =(TextView) childview.findViewById(R.id.tv_time);
        tvContent =(TextView) childview.findViewById(R.id.tv_content);
        lqggWebview =(WebView) childview.findViewById(R.id.lqgg_webview);

        mLqgg_back.setOnClickListener(this);
        rbGgmj.setOnClickListener(this);
        rbSjnh.setOnClickListener(this);
        rbSjrk.setOnClickListener(this);
        rbSlbx.setOnClickListener(this);
        rbLqdy.setOnClickListener(this);
        rbLxzz.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));

        lqggTabview = (VHTableView) findViewById(R.id.lqgg_tabview);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.lqgg_back) {
            this.finish();

        } else if (i == R.id.rb_ggmj) {
            gotoHtml(R.string.lgmj);

        } else if (i == R.id.rb_sjnh) {
            gotoHtml(R.string.sjnh);

        } else if (i == R.id.rb_sjrk) {
            gotoHtml(R.string.sjrk);

        } else if (i == R.id.rb_slbx) {
            gotoHtml(R.string.slbx);

        } else if (i == R.id.rb_lqdy) {
            gotoHtml(R.string.lqdy);

        } else if (i == R.id.rb_lxzz) {
            gotoHtml(R.string.lxzz);

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
        lqggWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        lqggWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.lqgg){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initLqggTabView(lqggTabview,list);
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
                if(keyType.contains("面积")){
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万亩");
                }else if(keyType.contains("涉及")){
                    tvContent.setText(gzsh+"林权改革"+keyType+"："+ Util.round(cfxe)+"万户");
                }else if(keyType.contains("保险")){
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万元");
                }else{
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万元");
                }
                break;
            }
        }
    }
}
