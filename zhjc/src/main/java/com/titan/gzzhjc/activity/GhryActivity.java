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
 * 管护人员
 */
public class GhryActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvContent;
    WebView ghryWebview;
    TextView tvTime;
    VHTableView ghryTabview;
    Context mContext;
    Echart echart;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_ghry,null);
        setContentView(childview);

        mContext = GhryActivity.this;

        initview();

        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.ghry);
        echart = optionImpl.initWebView(mContext, ghryWebview, type,handler);
        keyType = getResources().getString(R.string.ghryhj);
        echart.setYwType(keyType);
        echart.setTime(timeid);
    }

    private void initview() {
        ImageView mGhry_back = (ImageView) childview.findViewById(R.id.ghry_back);
        mGhry_back.setOnClickListener(this);
        RadioButton rbGhryhj = (RadioButton) childview.findViewById(R.id.rb_ghryhj);
        rbGhryhj.setOnClickListener(this);
        RadioButton rbGhgyl =(RadioButton) childview.findViewById(R.id.rb_ghgyl);
        rbGhgyl.setOnClickListener(this);
        RadioButton rbTbgc =(RadioButton) childview.findViewById(R.id.rb_tbgc);
        rbTbgc.setOnClickListener(this);
        RadioButton rbGhtghl = (RadioButton) childview.findViewById(R.id.rb_ghtghl);
        rbGhtghl.setOnClickListener(this);
        RadioButton rbGlfh = (RadioButton) childview.findViewById(R.id.rb_glfh);
        rbGlfh.setOnClickListener(this);
        RadioButton rbGhyzl = (RadioButton) childview.findViewById(R.id.rb_ghyzl);
        rbGhyzl.setOnClickListener(this);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));

        tvContent =(TextView) childview.findViewById(R.id.tv_content);
        ghryWebview =(WebView) childview.findViewById(R.id.ghry_webview);
        ghryTabview = (VHTableView) childview.findViewById(R.id.ghry_tabview);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ghry_back) {
            this.finish();

        } else if (i == R.id.rb_ghryhj) {
            gotoHtml(R.string.ghryhj);

        } else if (i == R.id.rb_ghgyl) {
            gotoHtml(R.string.ghgyl);

        } else if (i == R.id.rb_tbgc) {
            gotoHtml(R.string.tbgc);

        } else if (i == R.id.rb_ghtghl) {
            gotoHtml(R.string.ghtghl);

        } else if (i == R.id.rb_glfh) {
            gotoHtml(R.string.hlfh);

        } else if (i == R.id.rb_ghyzl) {
            gotoHtml(R.string.ghyzl);

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
        ghryWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        ghryWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.ghry){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initGhryTabView(ghryTabview,list);
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
                    tvContent.setText(gzsh+"管护人员"+keyType+"："+ Util.rounding(cfxe)+"人");
                }else{
                    tvContent.setText(gzsh+keyType+"："+ Util.rounding(cfxe)+"人");
                }
                break;
            }
        }
    }
}
