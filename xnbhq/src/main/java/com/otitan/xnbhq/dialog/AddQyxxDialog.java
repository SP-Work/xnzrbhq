package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.QyInfo;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2017/6/25.
 * 木材经营 企业信息添加
 */

public class AddQyxxDialog extends Dialog {

    private Context mContext;

    private Spinner mSp_ssdq;
    private Spinner mSp_qyxz;
    private EditText mEt_qymc;
    private EditText mEt_qydz;
    private EditText mEt_jyfw;
    private EditText mEt_jgfw;
    private EditText mEt_xkzh;
    private EditText mEt_chzr;

    private TextView mTv_send;
    private String dqid="1";

    private Gson gson = new Gson();
    private List<HashMap<String, String>> sjssLlist = new ArrayList<>();
    private ArrayList<String> dqdataList = new ArrayList<>();

    public AddQyxxDialog(@NonNull Context context, @StyleRes int themeResId,List<HashMap<String, String>> list) {
        super(context, themeResId);
        this.mContext = context;
        this.sjssLlist = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qyxx);

        setCanceledOnTouchOutside(false);

        getData();

        initView();

    }

    private void getData(){
        for(HashMap<String, String> map:sjssLlist){
            Object obj = map.get("DULEVE");
            if(obj != null){
                int leve = Integer.parseInt(obj.toString());
                if(leve < 5){
                    dqdataList.add(map.get("DQNAME").toString());
                }
            }
        }
    }

    private void initView() {
        // 所属地区
        mSp_ssdq = (Spinner) findViewById(R.id.sp_ssdq);
        // 企业性质
        mSp_qyxz = (Spinner) findViewById(R.id.sp_qyxz);
        // 企业名称
        mEt_qymc = (EditText) findViewById(R.id.et_qymc);
        // 企业地址
        mEt_qydz = (EditText) findViewById(R.id.et_qydz);
        // 经营范围
        mEt_jyfw = (EditText) findViewById(R.id.et_jyfw);
        // 加工范围
        mEt_jgfw = (EditText) findViewById(R.id.et_jgfw);
        // 许可证号
        mEt_xkzh = (EditText) findViewById(R.id.et_xkzh);
        // 持证人
        mEt_chzr = (EditText) findViewById(R.id.et_chzr);
        // 提交
        mTv_send = (TextView) findViewById(R.id.tv_send);

        mTv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendQyInfo();
            }
        });

        ImageView close =(ImageView) findViewById(R.id.qyxx_close);
        close.setOnClickListener(new CancleListener(this));

    }

    /** 发送企业信息 */
    private void sendQyInfo() {
        QyInfo qyInfo = getQyInfo();
        if (qyInfo == null) {
            return;
        }
        if (qyInfo.getCOMPANYNAME().equals("")) {
            ToastUtil.setToast(mContext, "企业名称不能为空");
            return;
        } else if (qyInfo.getCOMPANYADDR().equals("")) {
            ToastUtil.setToast(mContext, "企业地址不能为空");
            return;
        } else if (qyInfo.getBUSISCOPE().equals("")) {
            ToastUtil.setToast(mContext, "经营范围不能为空");
            return;
        } else if (qyInfo.getPROCRANGE().equals("")) {
            ToastUtil.setToast(mContext, "加工范围不能为空");
            return;
        } else if (qyInfo.getPERMIT_NUM().equals("")) {
            ToastUtil.setToast(mContext, "许可证号不能为空");
            return;
        } else if (qyInfo.getPERMIT_CZR().equals("")) {
            ToastUtil.setToast(mContext, "持证人不能为空");
            return;
        }
        String json = gson.toJson(qyInfo);
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().addQyData(json);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.setToast(mContext, "网络连接错误");
            }

            @Override
            public void onNext(String s) {
                if (s.equals("1")) { // s 为 1 成功；s 为 0 失败
                    ToastUtil.setToast(mContext, "信息添加成功");
                    AddQyxxDialog.this.dismiss();
                } else {
                    ToastUtil.setToast(mContext, "信息添加失败");
                }
            }
        });
    }

    /** 获取企业信息 json */
    private QyInfo getQyInfo() {

        String sDq = mSp_ssdq.getSelectedItem().toString();
        for(HashMap<String, String> map:sjssLlist){
            Object obj = map.get("DUNAME");
            if(obj != null){
                if(obj.toString().trim().equals(sDq.trim())){
                    dqid = map.get("ID").trim();
                    break;
                }
            }
        }

        String sXz = (mSp_qyxz.getSelectedItemId()+1)+"";

        String qymc = mEt_qymc.getText().toString();
        String qydz = mEt_qydz.getText().toString();
        String jyfw = mEt_jyfw.getText().toString();
        String jgfw = mEt_jgfw.getText().toString();
        String xkzh = mEt_xkzh.getText().toString();
        String chzr = mEt_chzr.getText().toString();

        QyInfo qyInfo = new QyInfo(dqid, sXz, qymc, qydz, jyfw, jgfw, xkzh, chzr);
        return qyInfo;
    }
}
