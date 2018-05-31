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
 * 退耕还林
 */
public class TghlActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mTghl_back;
    RadioButton rbKtgd;
    RadioButton rbTgd;
    TextView tvTime;
    TextView tvContent;
    WebView tghlWebview;


    VHTableView tghlTabview;
    Context mContext;
    Echart echart;
    PopupWindowCustom popup;
    View childview;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    int timeid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_tghl,null);
        setContentView(childview);

        mContext = TghlActivity.this;

        initview();
        initWebview();
    }

    private void initview() {

        mTghl_back = (ImageView) childview.findViewById(R.id.tghl_back);
        rbKtgd = (RadioButton) childview.findViewById(R.id.rb_ktgd);
        rbTgd = (RadioButton) childview.findViewById(R.id.rb_tgd);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvContent = (TextView) childview.findViewById(R.id.tv_content);
        tghlWebview = (WebView) childview.findViewById(R.id.tghl_webview);

        mTghl_back.setOnClickListener(this);
        rbKtgd.setOnClickListener(this);
        rbTgd.setOnClickListener(this);
        tghlTabview = (VHTableView) childview.findViewById(R.id.tghl_tabview);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));
    }

    /**
     * 初始化WebView
     */
    public void initWebview(){
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.tghl);
        echart = optionImpl.initWebView(mContext, tghlWebview,type ,handler);
        keyType = getResources().getString(R.string.ktgd);
        echart.setYwType(keyType);
        echart.setTime(timeid);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tghl_back) {
            this.finish();

        } else if (i == R.id.rb_ktgd) {
            gotoHtml(R.string.ktgd);

        } else if (i == R.id.rb_tgd) {
            gotoHtml(R.string.tgd);

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
    public void reInitWebview(Button button){
        String txt = button.getText().toString();
        if(txt.equals(mContext.getResources().getString(R.string.time1))){
            timeid = R.string.time1;
        }else{
            timeid = R.string.time2;
        }
        echart.setTime(timeid);
        tvTime.setText(txt);
        String dbname = echart.getName();
        tghlWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        tghlWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.tghl){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initTghlTabView(tghlTabview,list);
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
                tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万亩");
                break;
            }
        }
    }
}
