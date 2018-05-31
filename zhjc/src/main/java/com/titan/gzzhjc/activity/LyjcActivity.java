package com.titan.gzzhjc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
 * 林业基础
 */
public class LyjcActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ghryBack;
    RadioButton rbGtmj;
    RadioButton rbLdmj;
    RadioButton rbSlmj;
    RadioButton rbXuji;
    RadioButton rbSlfgl;
    RadioButton rbSthx;
    RadioButton rbLygzz;
    TextView tvTime;
    TextView tvContent;
    WebView lyjcWebview;
    AppCompatSpinner spinnerLyjc;

    VHTableView lyjcTabview;
    Context mContext;
    Echart echart;
    List<Map<String, String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid = 0;
    String company = "";
    int select_type = 0;//0为行政区划 1为山脉 2 流域 3少数民族
    String table_title = "行政区划";
    private ImageView mIv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_lyjc, null);
        setContentView(childview);
        mContext = LyjcActivity.this;

        initview();
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.lyjc);
        echart = optionImpl.initWebView(mContext, lyjcWebview, type, handler);
        keyType = getResources().getString(R.string.gtmj);
        echart.setYwType(keyType);
        echart.setTime(timeid);
        company = mContext.getResources().getString(R.string.wanmu);
        echart.setCompany(company);
    }

    public void initview() {

        ghryBack = (ImageView) childview.findViewById(R.id.lyjc_back);
        rbGtmj = (RadioButton) childview.findViewById(R.id.rb_gtmj);
        rbLdmj = (RadioButton) childview.findViewById(R.id.rb_ldmj);
        rbSlmj = (RadioButton) childview.findViewById(R.id.rb_slmj);
        rbXuji = (RadioButton) childview.findViewById(R.id.rb_xuji);
        rbSlfgl = (RadioButton) childview.findViewById(R.id.rb_slfgl);
        rbSthx = (RadioButton) childview.findViewById(R.id.rb_sthx);
        rbLygzz = (RadioButton) childview.findViewById(R.id.rb_lygzz);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvContent = (TextView) childview.findViewById(R.id.tv_content);
        lyjcWebview = (WebView) childview.findViewById(R.id.lyjc_webview);
        spinnerLyjc = (AppCompatSpinner) childview.findViewById(R.id.spinner_lyjc);
        mIv_back = (ImageView) findViewById(R.id.lyjc_back);

        ghryBack.setOnClickListener(this);
        rbGtmj.setOnClickListener(this);
        rbLdmj.setOnClickListener(this);
        rbSlmj.setOnClickListener(this);
        rbXuji.setOnClickListener(this);
        rbSlfgl.setOnClickListener(this);
        rbSthx.setOnClickListener(this);
        rbLygzz.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        mIv_back.setOnClickListener(this);

        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));

        lyjcTabview = (VHTableView) childview.findViewById(R.id.lyjc_tabview);

        final String[] arrays = mContext.getResources().getStringArray(R.array.seltype);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(mContext,R.layout.spinner_item,arrays);
        adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        /**
         * 选择行政区划
         */
        spinnerLyjc.setAdapter(adapter);
        spinnerLyjc.setSelection(select_type);
        spinnerLyjc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_type = position;
                reInitWebData(arrays);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext,"没有选择",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ghry_back) {
            this.finish();

        } else if (i == R.id.rb_gtmj) {
            gotoHtml(R.string.gtmj);

        } else if (i == R.id.rb_ldmj) {
            gotoHtml(R.string.ldmj);

        } else if (i == R.id.rb_slmj) {
            gotoHtml(R.string.slmj);

        } else if (i == R.id.rb_xuji) {
            gotoHtml(R.string.xuji);

        } else if (i == R.id.rb_slfgl) {
            gotoHtml(R.string.slfgl);

        } else if (i == R.id.rb_sthx) {
            gotoHtml(R.string.sthx);

        } else if (i == R.id.rb_lygzz) {
            gotoHtml(R.string.lygzz);

        } else if (i == R.id.tv_time) {
            PopupWindowImpl popupWindow = new PopupWindowImpl();
            popup = popupWindow.showTimeSel(mContext, childview, this);

        } else if (i == R.id.item_time_sewq) {
            reInitWebview((Button) view);

            reInitWebview((Button) view);

        } else if (i == R.id.item_time_elyl) {
            reInitWebview((Button) view);

        } else if (i == R.id.lyjc_back) {
            finish();
        }
    }

    /**
     * Webview数据更新（根据 底部时间选择弹窗 选择的时间，加载不同数据）
     * @param button
     */
    public void reInitWebview(Button button) {
        String txt = button.getText().toString();
        if (txt.equals(mContext.getResources().getString(R.string.time1))) {
            timeid = R.string.time1;
        } else {
            timeid = R.string.time2;
        }
        echart.setFormType(select_type);
        echart.setTime(timeid);
        tvTime.setText(txt);
        String dbname = echart.getName();
        lyjcWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * 行政区划选择列表
     * @param arrays
     */
    public void reInitWebData(String[] arrays){
        if(select_type == 1 || select_type == 2){
            table_title = arrays[select_type]+"       ";
        }else{
            table_title = arrays[select_type];
        }
        echart.setTime(timeid);
        echart.setFormType(select_type);
        tvTime.setText(mContext.getResources().getString(timeid));
        String dbname = echart.getName();
        lyjcWebview.loadUrl("javascript:setchart('" + dbname + "');");
    }

    /**
     * RadioGroup 按钮选择
     * @param timetype
     */
    public void gotoHtml(int timetype) {
        keyType = mContext.getResources().getString(timetype);
        echart.setYwType(keyType);
        if (keyType.equals(mContext.getResources().getString(R.string.xuji))) {
            company = mContext.getResources().getString(R.string.wanlfm);
        } else if (keyType.equals(mContext.getResources().getString(R.string.slfgl))) {
            company = mContext.getResources().getString(R.string.baifb);
        } else if (keyType.equals(mContext.getResources().getString(R.string.lygzz))) {
            company = mContext.getResources().getString(R.string.geshu);
        } else {
            company = mContext.getResources().getString(R.string.wanmu);
        }
        echart.setCompany(company);
        lyjcWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == R.string.lyjc) {
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initLyjcTabView(lyjcTabview, list, company,table_title,keyType);
                /** 顶部描述 */
                changContent();
            }
        }
    };

    /**
     * 顶部描述
     */
    public void changContent() {
        if (list.size() == 0) {
            String gzsh = echart.getName();
            tvContent.setText(gzsh + keyType + "：" + Util.round("0") + company);
        }
        if(select_type == 0){
            for (Map<String, String> map : list) {
                String dqmc = map.get(mContext.getResources().getString(R.string.dqname));
                String gzsh = echart.getName();
                if (gzsh.equals(dqmc.trim())) {
                    String cfxe = map.get(keyType);
                    if(keyType.contains("林业工作站")){
                        tvContent.setText(gzsh + keyType + "：" + Util.rounding(cfxe) + company);
                    }else{
                        tvContent.setText(gzsh + keyType + "：" + Util.round(cfxe) + company);
                    }

                    break;
                }
            }
        }else{
            String value = "0";
            if(DbdataImpl.colums == null || DbdataImpl.colums.length == 0){
                return;
            }
            String colum_name = "";
            for (Map<String, String> map : list) {
                String key = map.get(DbdataImpl.colums[0]).trim();
                if(key.contains(table_title.trim())){
                    colum_name = key;
                    value = map.get(DbdataImpl.colums[1]);
                    break;
                }
            }
            tvContent.setText("");
            String gzsh = mContext.getResources().getString(R.string.gzsh);
            if(keyType.contains("林业工作站")){
                tvContent.setText(gzsh + colum_name.trim() + keyType+"：" + Util.rounding(value.trim()) + company);
            }else{
                tvContent.setText(gzsh + colum_name.trim() + keyType+"：" + Util.round(value.trim()) + company);
            }
        }

    }
}
