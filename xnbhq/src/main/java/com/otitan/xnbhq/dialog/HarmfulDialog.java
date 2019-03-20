package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.HarmfulAdapter;
import com.otitan.xnbhq.entity.Group;
import com.otitan.xnbhq.entity.Group_;
import com.otitan.xnbhq.entity.LineInfo;
import com.otitan.xnbhq.entity.LineInfo_;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.gzzhjc.entity.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

/**
 * Created by sp on 2019/3/12.
 * 有害动物调查
 */
public class HarmfulDialog extends Dialog {

    @BindView(R.id.rv_line)
    RecyclerView mRv_line;
    @BindView(R.id.ll_new)
    LinearLayout mLl_new;
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

    Box<LineInfo> lineInfoBox = MyApplication.boxStore.boxFor(LineInfo.class);

    private static final String PREFS_NAME = "MyPrefsFile";
    private static SharedPreferences titanSp;

    public HarmfulDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_harmful);

        ButterKnife.bind(this);

        titanSp = mContext.getSharedPreferences(PREFS_NAME, 0);

        initView();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);

        if (isGroup()) {
            ToastUtil.setToast(mContext, "请选择调查组");
            return;
        }
        Box<Group> groupBox = MyApplication.boxStore.boxFor(Group.class);
        List<Group> groups = groupBox.query().equal(Group_.ID, titanSp.getLong("GroupID", -1)).build().find();
        mEt_people.setText(groups.get(0).getMember());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String today = format.format(new Date(System.currentTimeMillis()));
        mEt_date.setText(today);

        mRv_line.setLayoutManager(new LinearLayoutManager(mContext));
        mRv_line.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        initList();
    }

    private boolean isGroup() {
        long groupId = titanSp.getLong("GroupID", -1);
        if (groupId == -1) {
            return true;
        }
        return false;
    }

    private void initList() {
        final List<LineInfo> lineInfoList = lineInfoBox.query().build().find();
        if (lineInfoList.size() == 0) {
            mLl_new.setVisibility(View.VISIBLE);
            return;
        }
        HarmfulAdapter harmfulAdapter = new HarmfulAdapter(mContext, lineInfoList);
        mRv_line.setAdapter(harmfulAdapter);
        harmfulAdapter.setItemClickListener(new HarmfulAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long pointId = lineInfoList.get(position).getID();
                titanSp.edit().putLong("LineID", pointId).apply();
                ToastUtil.setToast(mContext, "已选择\n" + lineInfoList.get(position).getRouteNum() + " 号样线");
                dismiss();
            }
        });
    }

    @OnClick({R.id.tv_add, R.id.tv_determine, R.id.tv_cancel})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                if (isGroup()) {
                    ToastUtil.setToast(mContext, "请选择调查组");
                    return;
                }
                int visibility = mLl_new.getVisibility();
                if (visibility == 8) {
                    mLl_new.setVisibility(View.VISIBLE);
                    mRv_line.setVisibility(View.GONE);
                } else {
                    mLl_new.setVisibility(View.GONE);
                    mRv_line.setVisibility(View.VISIBLE);
                }
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

        initList();
        mRv_line.setVisibility(View.VISIBLE);
        mLl_new.setVisibility(View.GONE);
    }
}
