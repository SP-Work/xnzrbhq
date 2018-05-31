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

/**
 * 森林防火
 */
public class SlfhActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mSlfhback;
    WebView mSlfhwebview;
    RadioButton mRbhzcs;
    RadioButton mRbhzmj;
    RadioButton mRbssmj;
    RadioButton mRbjftr;
    TextView tvTime;
    TextView tvContent;

    private VHTableView mSlfhtabview;
    Context mContext;
    Echart mEchart;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_slfh_zhjc,null);
        setContentView(childview);
        mContext = SlfhActivity.this;

        initview();
        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String Slfh = getResources().getString(R.string.slfh);
        mEchart = optionImpl.initWebView(mContext, mSlfhwebview, Slfh, handler);
        keyType = getResources().getString(R.string.hzcs);
        mEchart.setYwType(keyType);

    }

    private void initview() {

        mSlfhback = (ImageView) childview.findViewById(R.id.slfh_back);
        mSlfhwebview = (WebView) childview.findViewById(R.id.slfh_webview);
        mRbhzcs = (RadioButton) childview.findViewById(R.id.rb_hzcs);
        mRbhzmj = (RadioButton) childview.findViewById(R.id.rb_hzmj);
        mRbssmj = (RadioButton) childview.findViewById(R.id.rb_ssmj);
        mRbjftr = (RadioButton) childview.findViewById(R.id.rb_jftr);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvContent = (TextView) childview.findViewById(R.id.tv_content);

        mSlfhback.setOnClickListener(this);
        mRbhzcs.setOnClickListener(this);
        mRbhzmj.setOnClickListener(this);
        mRbssmj.setOnClickListener(this);
        mRbjftr.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));

        mSlfhtabview = (VHTableView) childview.findViewById(R.id.slfh_tabview);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.slfh_back) {
            this.finish();

        } else if (i == R.id.rb_hzcs) {
            gotoHtml(R.string.hzcs);

        } else if (i == R.id.rb_hzmj) {
            gotoHtml(R.string.hzmj);

        } else if (i == R.id.rb_ssmj) {
            gotoHtml(R.string.ssmj);

        } else if (i == R.id.rb_jftr) {
            gotoHtml(R.string.jftr);

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
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type) {
        keyType = mContext.getResources().getString(type);
        mEchart.setYwType(keyType);
        mSlfhwebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
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
        mEchart.setTime(timeid);
        tvTime.setText(txt);
        String dbname = mEchart.getName();
        mSlfhwebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == R.string.slfh) {
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initSlfhTabView(mSlfhtabview, list);
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
            String gzsh = mEchart.getName();
            if(gzsh.equals(dqmc.trim())){
                String cfxe = map.get(keyType);
                if(keyType.contains("面积")){
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"公顷");
                }else if(keyType.contains("次数")){
                    tvContent.setText(gzsh+keyType+"："+ Util.rounding(cfxe)+"次");
                }else{
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万元");
                }
                break;
            }
        }
    }
}
