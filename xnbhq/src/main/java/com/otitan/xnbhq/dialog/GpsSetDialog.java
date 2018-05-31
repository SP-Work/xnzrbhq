package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.QxData;
import com.otitan.xnbhq.service.PullParseXml;
import com.otitan.xnbhq.util.ResourcesManager;
import com.titan.baselibrary.listener.CancleListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/5/31.
 * GPS参数设置dialog
 */

public class GpsSetDialog extends Dialog {

    private Context mContext;

    public GpsSetDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gps_settings);
        setCanceledOnTouchOutside(false);


        final Spinner spinner = (Spinner) findViewById(R.id.edittxt_selqx);
        final EditText dxTxt = (EditText) findViewById(R.id.edittxt_dx);
        final EditText dyTxt = (EditText) findViewById(R.id.edittxt_dy);
        final EditText dzTxt = (EditText) findViewById(R.id.edittxt_dz);
        final EditText daTxt = (EditText) findViewById(R.id.edittxt_da);
        final EditText dfTxt = (EditText) findViewById(R.id.edittxt_df);

        final List<QxData> list = new ArrayList<>();
        try
        {
            String path = ResourcesManager.otms+"/config.xml";
            path = MyApplication.resourcesManager.getFilePath(path);
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            PullParseXml parseXml = new PullParseXml();
            List<QxData> lst = parseXml.PullParseXMLQx(inputStream, "qxdata");
            if(lst != null && lst.size() > 0){
                list.addAll(lst);
            }
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
        final List<String> lst = new ArrayList<String>();
        for(QxData data : list){
            lst.add(data.getQxname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, lst);
        //设置下拉窗口样式
        adapter.setDropDownViewResource(R.layout.myspinner);
        spinner.setAdapter(adapter);
        int id = MyApplication.sharedPreferences.getInt("id", 0);
        spinner.setSelection(id);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                for(QxData data : list){
                    if(data.getQxname().equals(lst.get(arg2))){
                        dxTxt.setText(data.getDx());
                        dyTxt.setText(data.getDy());
                        dzTxt.setText(data.getDz());
                        daTxt.setText(data.getDa());
                        dfTxt.setText(data.getDf());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        TextView sure = (TextView) findViewById(R.id.gpssettings_sure);
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int id = spinner.getSelectedItemPosition();
                MyApplication.sharedPreferences.edit().putInt("id", id).apply();
                String dx = dxTxt.getText() != null ? dxTxt.getText().toString() : "0";
                MyApplication.sharedPreferences.edit().putString("dx", dx).apply();
                String dy = dyTxt.getText() != null ? dyTxt.getText() .toString() : "0";
                MyApplication.sharedPreferences.edit().putString("dy", dy).apply();
                String dz = dzTxt.getText() != null ? dzTxt.getText().toString() : "0";
                MyApplication.sharedPreferences.edit().putString("dz", dz).apply();
                String da = daTxt.getText() != null ? daTxt.getText().toString() : "-3";
                MyApplication.sharedPreferences.edit().putString("da", da).apply();
                String df = dfTxt.getText() != null ? dfTxt.getText().toString() : "0";
                MyApplication.sharedPreferences.edit().putString("df", df).apply();

                dismiss();
            }
        });

        ImageView close = (ImageView) findViewById(R.id.textView_close);
        close.setOnClickListener(new CancleListener(this));
    }
}
