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
 * 公益林
 */
public class GylActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mGylback;
    WebView mGylwebview;
    RadioButton mRbgylmj;
    RadioButton mRbgylzj;
    TextView tvTime;
    TextView tvContent;

    Context mContext;
    Echart mEchart;
    VHTableView mGyltabview;
    PopupWindowCustom popup;
    View childview;
    List<Map<String, String>> list = new ArrayList<>();
    String keyType = "";
    int timeid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_gyl_zhjc, null);
        setContentView(childview);

        mContext = GylActivity.this;

        initview();

        /** 初始化WebView */
        OptionImpl option = new OptionImpl();
        String Gyl = getResources().getString(R.string.gyl);
        mEchart = option.initWebView(mContext, mGylwebview, Gyl, handler);
        keyType = getResources().getString(R.string.gylmj);
        mEchart.setYwType(keyType);
    }

    private void initview() {
        mGylback = (ImageView)childview.findViewById(R.id.gyl_back);
        mGylback.setOnClickListener(this);
        mRbgylmj = (RadioButton) childview.findViewById(R.id.rb_gylmj);
        mRbgylmj.setOnClickListener(this);
        mRbgylzj =(RadioButton) childview.findViewById(R.id.rb_gylzj);
        mRbgylzj.setOnClickListener(this);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this);

        mGylwebview =(WebView) childview.findViewById(R.id.gyl_webview);

        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));
        mGyltabview = (VHTableView) childview.findViewById(R.id.gyl_tabview);
        tvContent = (TextView) childview.findViewById(R.id.tv_content);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.gyl_back) {
            this.finish();

        } else if (i == R.id.rb_gylmj) {
            gotoHtml(R.string.gylmj);

        } else if (i == R.id.rb_gylzj) {
            gotoHtml(R.string.gylzj);

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
        mEchart.setTime(timeid);
        tvTime.setText(txt);
        String dbname = mEchart.getName();
        mGylwebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type) {
        keyType = mContext.getResources().getString(type);
        mEchart.setYwType(keyType);
        mGylwebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == R.string.gyl) {
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initGylTabView(mGyltabview, list);
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
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万亩");
                }else{
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万元");
                }
                break;
            }
        }
    }
}
