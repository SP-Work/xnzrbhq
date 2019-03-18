package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Group;
import com.otitan.xnbhq.entity.LineInfo;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

/**
 * Created by sp on 2019/3/12.
 * 有害动物调查
 */
public class HarmfulDialog extends Dialog {

    @BindView(R.id.et_countyName)
    EditText mEt_countyName;
    @BindView(R.id.et_countyCode)
    EditText mEt_countyCode;
    @BindView(R.id.et_townName)
    EditText mEt_townName;
    @BindView(R.id.et_townCode)
    EditText mEt_townCode;
    @BindView(R.id.et_villageName)
    EditText mEt_villageName;
    @BindView(R.id.et_routeNumberDefault)
    EditText mEt_routeNumberDefault;
    @BindView(R.id.et_routeNumber)
    EditText mEt_routeNumber;
    @BindView(R.id.et_people)
    EditText mEt_people;
    @BindView(R.id.et_date)
    EditText mEt_date;

    private Context mContext;

    public HarmfulDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_harmful);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);

        Box<Group> groupBox = MyApplication.boxStore.boxFor(Group.class);
        List<Group> groups = groupBox.query().build().find();
        if (groups.size() == 0) {
            ToastUtil.setToast(mContext, "请选择调查组");
            return;
        }
        mEt_people.setText(groups.get(0).getMember());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String today = format.format(new Date(System.currentTimeMillis()));
        mEt_date.setText(today);
    }

    @OnClick({R.id.iv_close, R.id.tv_determine, R.id.tv_cancel})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_determine:
                getLineInfo();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private void getLineInfo() {
        String countyName = mEt_countyName.getText().toString().trim();
        String countyCode = mEt_countyCode.getText().toString().trim();
        String townName = mEt_townName.getText().toString().trim();
        String townCode = mEt_townCode.getText().toString().trim();
        String villageName = mEt_villageName.getText().toString().trim();
        String routeNumber = mEt_routeNumber.getText().toString().trim();
        String people = mEt_people.getText().toString().trim();
        String date = mEt_date.getText().toString().trim();

        if (people.isEmpty()) {
            ToastUtil.setToast(mContext, "请选择调查组");
            return;
        }

        Box<LineInfo> lineInfoBox = MyApplication.boxStore.boxFor(LineInfo.class);
        LineInfo lineInfo = new LineInfo();
        lineInfo.setCountyName(countyName);
        lineInfo.setCountyCode(countyCode);
        lineInfo.setTownName(townName);
        lineInfo.setTownCode(townCode);
        lineInfo.setVillageName(villageName);
        lineInfo.setRouteNum(routeNumber);
        lineInfo.setPeople(people);
        lineInfo.setDate(date);
        lineInfoBox.put(lineInfo);

        ToastUtil.setToast(mContext, "保存样地属性");
        dismiss();
    }
}
