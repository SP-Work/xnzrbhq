package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.JcdwBean;
import com.otitan.xnbhq.entity.YyybBean;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2017/6/22.
 * 疫源疫病信息上报 dialog
 */

public class YyybAddDialog extends Dialog implements View.OnClickListener{

    Context mContext;
    Spinner spinner;
    private ArrayList<JcdwBean> arrayList = new ArrayList<>();
    private EditText jcry_view,ysbyt_view,dwmc_view,dwsl_view,zzbx_view,fhcs_view,bz_view;
    private String jcdwId = "";

    public YyybAddDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        getJcdwData();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yyybadd);
        setCanceledOnTouchOutside(false);

        initView();

    }

    /*控件初始化*/
    private void initView(){
        spinner = (Spinner) findViewById(R.id.jcdw);
        jcry_view = (EditText) findViewById(R.id.jcry);
        ysbyt_view = (EditText) findViewById(R.id.ysbyt);
        dwmc_view = (EditText) findViewById(R.id.dwmc);
        dwsl_view = (EditText) findViewById(R.id.dwsl);
        zzbx_view = (EditText) findViewById(R.id.zzbx);
        fhcs_view = (EditText) findViewById(R.id.fhcs);
        bz_view = (EditText) findViewById(R.id.yyyb_bz);
        TextView save = (TextView) findViewById(R.id.yyyb_save);
        TextView cancle = (TextView) findViewById(R.id.yyyb_cancle);

        save.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }

    /*Spinner绑定数据*/
    private void setSpinnerAdapter(){

        String[] mItems = new String[arrayList.size()];
        for(int i= 0 ;i<arrayList.size();i++){
            mItems[i] = arrayList.get(i).getDEPARTMENT();
        }
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, mItems);
        _Adapter.setDropDownViewResource(R.layout.myspinner);
        spinner.setAdapter(_Adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jcdwId = arrayList.get(position).getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yyyb_save:
                saveData();
                break;
            case R.id.yyyb_cancle:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    /*上报数据*/
    private void saveData(){
        if(jcdwId.equals("")){
            ToastUtil.setToast(mContext,"监测单位必填");
            return;
        }
        if(TextUtils.isEmpty(jcry_view.getText())){
            ToastUtil.setToast(mContext,"检测人员必填");
            return;
        }
        String jcry = jcry_view.getText().toString().trim();
        if(TextUtils.isEmpty(ysbyt_view.getText())){
            ToastUtil.setToast(mContext,"疑似病原体必填");
            return;
        }
        String ysbyt = ysbyt_view.getText().toString().trim();
        if(TextUtils.isEmpty(dwmc_view.getText())){
            ToastUtil.setToast(mContext,"动物名称必填");
            return;
        }
        String dwmc = dwmc_view.getText().toString().trim();
        if(TextUtils.isEmpty(dwsl_view.getText())){
            ToastUtil.setToast(mContext,"动物数量必填");
            return;
        }
        String dwsl = dwsl_view.getText().toString().trim();
        /*症状表现*/
        String zzbx = zzbx_view.getText().toString().trim();
        String fhcs = fhcs_view.getText().toString().trim();
        String beizhu = bz_view.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());

        YyybBean yyybBean = new YyybBean(jcdwId,jcry,time,dwmc,dwsl,zzbx,ysbyt,fhcs,beizhu);
        Gson gson = new Gson();
        String json = gson.toJson(yyybBean);
        sendData(json);
    }
    /*发送json数据*/
    private void sendData(String json){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().addYyybJcdwData(json);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext,"保存失败");
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("1")) {
                            ToastUtil.setToast(mContext,"保存成功");
                        }else{
                            ToastUtil.setToast(mContext,"保存失败");
                        }
                    }
                });
    }
    public void getJcdwData(){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().getJcdwData();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext,"");
                    }

                    @Override
                    public void onNext(String s) {
                        if (!s.equals("无数据")) {
                            ArrayList<JcdwBean> json = new Gson().fromJson(s, new TypeToken<ArrayList<JcdwBean>>() {}.getType());
                            if(json != null && json.size() > 0){
                                arrayList.clear();
                                arrayList.addAll(json);
                                setSpinnerAdapter();
                            }
                        }
                    }
                });
    }
}
