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
import com.otitan.xnbhq.adapter.GroupAdapter;
import com.otitan.xnbhq.entity.Group;
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
 * Created by sp on 2019/3/11.
 * 调查组
 */
public class GroupDialog extends Dialog {

    @BindView(R.id.ll_new)
    LinearLayout mLl_new;
    @BindView(R.id.et_name)
    EditText mEt_name;
    @BindView(R.id.et_member)
    EditText mEt_member;
    @BindView(R.id.rv_group)
    RecyclerView mRv_group;

    private Context mContext;

    private Box<Group> groupBox = MyApplication.boxStore.boxFor(Group.class);

    private static final String PREFS_NAME = "MyPrefsFile";
    private static SharedPreferences titanSp;

    public GroupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_group);

        ButterKnife.bind(this);

        titanSp = mContext.getSharedPreferences(PREFS_NAME, 0);

        initView();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);

        mRv_group.setLayoutManager(new LinearLayoutManager(mContext));
        mRv_group.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        initList();
    }

    private void initList() {
        final List<Group> groupList = groupBox.query().build().find();
        if (groupList.size() == 0) {
            mLl_new.setVisibility(View.VISIBLE);
            return;
        }
        GroupAdapter groupAdapter = new GroupAdapter(mContext, groupList);
        mRv_group.setAdapter(groupAdapter);
        groupAdapter.setItemClickListener(new GroupAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long groupId = groupList.get(position).getID();
                titanSp.edit().putLong("GroupID", groupId).apply();
                ToastUtil.setToast(mContext, "已选择\n" + groupList.get(position).getName());
                dismiss();
            }
        });
    }

    @OnClick({R.id.tv_new, R.id.tv_determine, R.id.tv_cancel})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.tv_new:
                int visibility = mLl_new.getVisibility();
                if (visibility == 8) {
                    mLl_new.setVisibility(View.VISIBLE);
                    mRv_group.setVisibility(View.GONE);
                } else {
                    mLl_new.setVisibility(View.GONE);
                    mRv_group.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_determine:
                int visibility2 = mLl_new.getVisibility();
                if (visibility2 == 0) {
                    getGroupInfo();
                }
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private void getGroupInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = format.format(new Date(System.currentTimeMillis()));

        String name = mEt_name.getText().toString().trim();
        String member = mEt_member.getText().toString().trim();

        if (name.isEmpty()) {
            ToastUtil.setToast(mContext, "请输入调查组名称");
            return;
        }
        if (member.isEmpty()) {
            ToastUtil.setToast(mContext, "请输入调查组成员");
            return;
        }

        String m = "";
        if (member.contains("，")) {
            m = member.replace("，", ",");
        } else {
            m = member;
        }
        Box<Group> groupBox = MyApplication.boxStore.boxFor(Group.class);
        Group group = new Group();
        group.setName(name);
        group.setMember(m);
        group.setTime(now);
        groupBox.put(group);
        ToastUtil.setToast(mContext, "调查组创建完成");
//        dismiss();
        initList();
        mLl_new.setVisibility(View.GONE);
        mRv_group.setVisibility(View.VISIBLE);
    }
}
