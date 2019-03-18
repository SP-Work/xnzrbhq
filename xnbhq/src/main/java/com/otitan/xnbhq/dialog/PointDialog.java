package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.CheckPoint;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

/**
 * Created by sp on 2019/3/12.
 * 野生动物调查
 */
public class PointDialog extends Dialog {

    @BindView(R.id.et_name)
    EditText mEt_name;
    @BindView(R.id.et_lon)
    EditText mEt_lon;
    @BindView(R.id.et_lat)
    EditText mEt_lat;
    @BindView(R.id.et_altitude)
    EditText mEt_altitude;
    @BindView(R.id.et_time)
    EditText mEt_time;
    @BindView(R.id.et_forest)
    EditText mEt_forest;
    @BindView(R.id.et_origin)
    EditText mEt_origin;
    @BindView(R.id.et_number)
    EditText mEt_number;
    @BindView(R.id.et_area)
    EditText mEt_area;

    private Context mContext;

    public PointDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_point);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String today = format.format(new Date(System.currentTimeMillis()));
        mEt_time.setText(today);
    }

    @OnClick({R.id.iv_close, R.id.tv_determine, R.id.tv_cancel})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_determine:
                getPointInfo();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private void getPointInfo() {
        String name = mEt_name.getText().toString().trim();
        String lon = mEt_lon.getText().toString().trim();
        String lat = mEt_lat.getText().toString().trim();
        String altitude = mEt_altitude.getText().toString().trim();
        String time = mEt_time.getText().toString().trim();
        String forest = mEt_forest.getText().toString().trim();
        String origin = mEt_origin.getText().toString().trim();
        String number = mEt_number.getText().toString().trim();
        String area = mEt_area.getText().toString().trim();

        if (name.isEmpty()) {
            ToastUtil.setToast(mContext, "请输入踏查点名称");
            return;
        }

        Box<CheckPoint> checkPointBox = MyApplication.boxStore.boxFor(CheckPoint.class);
        CheckPoint checkPoint = new CheckPoint();
        checkPoint.setName(name);
        checkPoint.setLon(lon);
        checkPoint.setLat(lat);
        checkPoint.setAltitude(altitude);
        checkPoint.setTime(time);
        checkPoint.setForest(forest);
        checkPoint.setOrigin(origin);
        checkPoint.setName(number);
        checkPoint.setArea(area);
        checkPointBox.put(checkPoint);
        ToastUtil.setToast(mContext, "踏查点创建完成");
        dismiss();
    }
}
