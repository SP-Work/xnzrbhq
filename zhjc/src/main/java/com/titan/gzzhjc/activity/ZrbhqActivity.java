package com.titan.gzzhjc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.titan.gzzhjc.Echart;
import com.titan.gzzhjc.R;
import com.titan.gzzhjc.impl.OptionImpl;
import com.titan.gzzhjc.impl.VHTableViewDataImpl;
import com.titan.gzzhjc.util.ToastUtil;
import com.titan.gzzhjc.util.Util;
import com.titan.vhtableview.VHTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自然保护区
 */
public class ZrbhqActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView bhqgyBack;
    RadioButton rbGyzrbhq;
    RadioButton rbSlgy;
    RadioButton rbSdgy;
    RadioButton rbGylc;
    TextView tvContent;
    WebView zrbhqWebview;
    LinearLayout legZrbhq;
    LinearLayout legSlgy;
    LinearLayout legShdgy;
    LinearLayout legGylc;

    VHTableView zrbhqTabview;
    Echart echart;
    List<Map<String, String>> list = new ArrayList<>();
    String keyType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrbhq);

        mContext = ZrbhqActivity.this;
        initview();

        OptionImpl optionImpl = new OptionImpl();

        /** 初始化WebView */
        String type = getResources().getString(R.string.zrbhq);
        echart = optionImpl.initWebView(mContext, zrbhqWebview, type, handler);
        keyType = getResources().getString(R.string.gyzrbhq);
        echart.setYwType(keyType);
    }

    private void initview() {
        bhqgyBack =(ImageView) findViewById(R.id.bhqgy_back);
        bhqgyBack.setOnClickListener(this);
        rbGyzrbhq =(RadioButton) findViewById(R.id.rb_gyzrbhq);
        rbGyzrbhq.setOnClickListener(this);
        rbSlgy = (RadioButton) findViewById(R.id.rb_slgy);
        rbSlgy.setOnClickListener(this);
        rbSdgy = (RadioButton) findViewById(R.id.rb_sdgy);
        rbSdgy.setOnClickListener(this);
        rbGylc = (RadioButton) findViewById(R.id.rb_gylc);
        rbGylc.setOnClickListener(this);
        tvContent = (TextView) findViewById(R.id.tv_content);
        zrbhqWebview = (WebView) findViewById(R.id.zrbhq_webview);
        legZrbhq = (LinearLayout)findViewById(R.id.leg_zrbhq);
        legSlgy =(LinearLayout) findViewById(R.id.leg_slgy);
        legShdgy =(LinearLayout) findViewById(R.id.leg_shdgy);
        legGylc = (LinearLayout) findViewById(R.id.leg_gylc);

        zrbhqTabview = (VHTableView) findViewById(R.id.zrbhq_tabview);
        legZrbhq.setVisibility(View.VISIBLE);
        legSlgy.setVisibility(View.GONE);
        legShdgy.setVisibility(View.GONE);
        legGylc.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bhqgy_back) {
            this.finish();

        } else if (i == R.id.rb_gyzrbhq) {
            gotoHtml(R.string.gyzrbhq);
            legZrbhq.setVisibility(View.VISIBLE);
            legSlgy.setVisibility(View.GONE);
            legShdgy.setVisibility(View.GONE);
            legGylc.setVisibility(View.GONE);

        } else if (i == R.id.rb_slgy) {
            gotoHtml(R.string.slgy);
            legZrbhq.setVisibility(View.GONE);
            legSlgy.setVisibility(View.VISIBLE);
            legShdgy.setVisibility(View.GONE);
            legGylc.setVisibility(View.GONE);

        } else if (i == R.id.rb_sdgy) {
            gotoHtml(R.string.sdgy);
            legZrbhq.setVisibility(View.GONE);
            legSlgy.setVisibility(View.GONE);
            legShdgy.setVisibility(View.VISIBLE);
            legGylc.setVisibility(View.GONE);

        } else if (i == R.id.rb_gylc) {
            gotoHtml(R.string.gylc);
            legZrbhq.setVisibility(View.GONE);
            legSlgy.setVisibility(View.GONE);
            legShdgy.setVisibility(View.GONE);
            legGylc.setVisibility(View.VISIBLE);

        }
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type) {
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        zrbhqWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == R.string.zrbhq) {
                list = (List<Map<String, String>>) msg.obj;
                if (list == null) {
                    ToastUtil.setToast(mContext, "获取数据失败");
                } else {
                    VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                    /** 表格数据加载 */
                    viewData.initZrbhqTabView(zrbhqTabview, list,echart.getName());
                    /** 顶部描述 */
                    changContent();
                }
            }
        }
    };

    /**
     * 顶部描述
     */
    public void changContent() {
        if(list == null){
            return;
        }
        double sumgs = 0;
        double sumarea = 0;
        String gzsh = echart.getName();
        for (Map<String, String> map : list) {
            double gs = getInValue(map.get("个数"));
            sumgs = sumgs + Double.parseDouble(Util.rounding(gs+""));
            double mj = getInValue(map.get("面积（公顷）"));
            sumarea = sumarea+Double.parseDouble(Util.round(mj+""));
        }
        tvContent.setText(gzsh+keyType+"："+Util.rounding(sumgs+"")+"个,面积"+Util.round(sumarea+"")+"（公顷）");
    }

    /**
     * 获取保护区数量及面积数据
     * @param str
     * @return
     */
    public double getInValue(String str){
        if(str == null){
            return 0;
        }
        double a = Double.parseDouble(str);
        return a;
    }


}
