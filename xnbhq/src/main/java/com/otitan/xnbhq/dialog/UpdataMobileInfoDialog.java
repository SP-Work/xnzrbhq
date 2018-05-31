package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.MobileInfo;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by li on 2017/6/1.
 * 修改设备信息
 */

public class UpdataMobileInfoDialog extends Dialog {

    private Context mContext;

    public UpdataMobileInfoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_mobileinfo);

        final String res = getMobileInfo();
        if(res.trim().equals("")){
            ToastUtil.setToast(mContext,"数据访问异常");
            return;
        }
        Gson gson = new Gson();
        List<MobileInfo> list = gson.fromJson(res.toString(), new TypeToken<List<MobileInfo>>() {}.getType());
        MobileInfo mobileInfo = list.get(0);
        final EditText nameTxt = (EditText) findViewById(R.id.mobile_name_text);
        final EditText telTxt = (EditText) findViewById(R.id.mobile_tel_text);
        final EditText addressTxt = (EditText) findViewById(R.id.mobile_dz_text);
        final EditText timeTxt = (EditText) findViewById(R.id.mobile_time_text);
        final EditText mb_nameTxt = (EditText) findViewById(R.id.sb_name_text);
        final EditText bzTxt = (EditText) findViewById(R.id.beizhu_text);
        TextView sbhTxt = (TextView) findViewById(R.id.sbh_text);
        sbhTxt.setText(MyApplication.macAddress);
        nameTxt.setText(mobileInfo.getSYZNAME().trim());
        telTxt.setText(mobileInfo.getSYZPHONE().trim());
        addressTxt.setText("");
        timeTxt.setText(mobileInfo.getDJTIME().trim());
        mb_nameTxt.setText(mobileInfo.getSBMC().trim());
        //bzTxt.setText(res.getString("BZ"));
        Button button = (Button) findViewById(R.id.mobile_info_btn_sure);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Webservice webservice = new Webservice(mContext);
                String result = webservice.updateMobileSysInfo(nameTxt.getText().toString(),
                        telTxt.getText().toString(), addressTxt
                                .getText().toString(), mb_nameTxt
                                .getText().toString(),MyApplication.macAddress);
                if (result.equals("true")) {
                    ToastUtil.setToast(mContext, "更新成功");
                    dismiss();
                } else {
                    ToastUtil.setToast(mContext, "更新失败");
                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.mobile_info_close);
        close.setOnClickListener(new CancleListener(this));

    }

    /** 解析json数据 */
    public String getMobileInfo() {
        String res = "";
        try {
            final Webservice webservice = new Webservice(mContext);
            String result = webservice.getMobileSysInfo(MyApplication.macAddress);
            if (result.equals(Webservice.netException)) {
                ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                ToastUtil.setToast(mContext, result);
            } else {
                JSONObject object = new JSONObject(result);
                res = object.optJSONArray("ds").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
