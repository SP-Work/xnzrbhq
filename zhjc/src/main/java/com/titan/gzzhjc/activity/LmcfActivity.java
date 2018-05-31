package com.titan.gzzhjc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
 * 林木采伐
 */
public class LmcfActivity extends AppCompatActivity implements View.OnClickListener {


    Context context;
    View childview;
    VHTableView lmcfTabview;
    Echart echart;
    PopupWindowCustom popup;
    String keyType;
    List<Map<String,String>> list = new ArrayList<>();
    int timeid = 0;

    ImageView lmcfIsBack;
    RadioButton rbCfxe;
    RadioButton rbCfl;
    TextView timeTextview;
    TextView contentView;
    WebView lmcfWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = LayoutInflater.from(this).inflate(R.layout.activity_lmcf, null);
        setContentView(childview);

        context = LmcfActivity.this;

        initView();

        initWebview();
    }

    public void initView() {

        lmcfIsBack =(ImageView) childview.findViewById(R.id.lmcf_isBack);
        rbCfxe =(RadioButton) childview.findViewById(R.id.rb_cfxe);
        rbCfl =(RadioButton) childview.findViewById(R.id.rb_cfl);
        timeTextview =(TextView) childview.findViewById(R.id.time_textview);
        contentView =(TextView) childview.findViewById(R.id.content_view);
        lmcfWebview =(WebView) childview.findViewById(R.id.lmcf_webview);

        lmcfIsBack.setOnClickListener(this);
        rbCfxe.setOnClickListener(this);
        rbCfl.setOnClickListener(this);
        timeTextview.setOnClickListener(this);
        lmcfTabview = (VHTableView) childview.findViewById(R.id.lmcf_tabview);
        timeid = R.string.time2;
        timeTextview.setText(context.getResources().getString(timeid));
    }

    /**
     * 初始化WebView
     */
    public void initWebview(){
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.lmcf);
        echart = optionImpl.initWebView(context, lmcfWebview, type,handler);
        keyType = getResources().getString(R.string.cfxe);
        echart.setYwType(keyType);
        echart.setTime(timeid);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.lmcf_isBack) {
            this.finish();

        } else if (i == R.id.rb_cfl) {
            gotoHtml(R.string.cfl);

        } else if (i == R.id.rb_cfxe) {
            gotoHtml(R.string.cfxe);

        } else if (i == R.id.time_textview) {
            PopupWindowImpl popupWindow = new PopupWindowImpl();
            popup = popupWindow.showTimeSel(context, childview, this);

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
     * @param view
     */
    public void reInitWebview(Button view){
        String txt = view.getText().toString();
        if(txt.equals(context.getResources().getString(R.string.time1))){
            timeid = R.string.time1;
        }else{
            timeid = R.string.time2;
        }
        echart.setTime(timeid);
        timeTextview.setText(txt);
        String dbname = echart.getName();
        lmcfWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = context.getResources().getString(type);
        echart.setYwType(keyType);
        lmcfWebview.loadUrl("javascript:setchart('" + context.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.lmcf){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(context);
                /** 表格数据加载 */
                viewData.initLmcfTabView(lmcfTabview,list);
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
            String dqmc = map.get(context.getResources().getString(R.string.dqname));
            String gzsh = echart.getName();
            if(gzsh.equals(dqmc.trim())){
                String cfxe = map.get(keyType);
                contentView.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万m³");
                break;
            }
        }
    }

}
