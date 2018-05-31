package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.RecycJyxkzAdapter;
import com.otitan.xnbhq.entity.Jyxkz;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.otitan.xnbhq.util.DividerGridItemDecoration;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2017/6/25.
 * 木材经营许可证查询
 */

public class JyxkzDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Spinner mSp_area;
    private EditText mEt_number;
    private RecyclerView mRv_jyxkz;
    private TextView mTv_search;
    private List<Jyxkz> mDatas = new ArrayList<>();
    private RecycJyxkzAdapter adapter;
    private int lastID =0;

    public JyxkzDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jyxkzsearch);

        setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView() {
        // 所属地区
        mSp_area = (Spinner) findViewById(R.id.sp_area);
        mSp_area.setSelection(0,false);
        mSp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String key = parent.getSelectedItem().toString();
                if(position == 0){
                    adapter = new RecycJyxkzAdapter(mContext,mDatas);
                    mRv_jyxkz.setAdapter(adapter);
                    return;
                }
                ArrayList<Jyxkz> aList = new ArrayList<>();
                for(Jyxkz mcjyysz : mDatas){
                    if(mcjyysz.getDQNAME().contains(key)){
                        aList.add(mcjyysz);
                    }
                }
                adapter = new RecycJyxkzAdapter(mContext,aList);
                mRv_jyxkz.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 许可证号
        mEt_number = (EditText) findViewById(R.id.et_number);
        // 查询
        mTv_search = (TextView) findViewById(R.id.tv_search);
        // 数据列表
        mRv_jyxkz = (RecyclerView) findViewById(R.id.rv_jyxkz);

        mTv_search.setOnClickListener(this);

        // 设置适配器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRv_jyxkz.setLayoutManager(layoutManager);
        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(mContext);
        //mRv_jyxkz.addItemDecoration(itemDecoration);
        adapter = new RecycJyxkzAdapter(mContext, mDatas);
        mRv_jyxkz.setAdapter(adapter);
        mRv_jyxkz.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int count = adapter.getItemCount();
                    //isAllCount = count % 10;
                    if ((lastItem + 1) == count) {
                        //获取更多数据
                        ToastUtil.setToast(mContext,"获取更多数据");
                        lastID = Integer.parseInt(mDatas.get(mDatas.size()-1).getID().trim());
                        getJyxkzData(lastID);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItem = layoutManager.findLastVisibleItemPosition();
            }
        });


        ImageView close =(ImageView) findViewById(R.id.jyxkz_close);
        close.setOnClickListener(new CancleListener(this));

        getJyxkzData(lastID);
    }

    /** 获取网络数据 */
    private void getJyxkzData(int i) {
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().getXkzData(i);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if (!s.equals("0")) {
                    ArrayList<Jyxkz> json = new Gson().fromJson(s, new TypeToken<ArrayList<Jyxkz>>() {}.getType());
                    if(json != null && json.size() > 0){
                        mDatas.addAll(json);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 查询
            case R.id.tv_search:
                getSearchData();
                break;

            default:
                break;
        }
    }

    /** 根据条件查询数据 */
    private void getSearchData() {
        List<Jyxkz> list = new ArrayList<>();
        String sArea = mSp_area.getSelectedItem().toString();
        String sNumber = "";
        if(!TextUtils.isEmpty(mEt_number.getText())){
            sNumber = mEt_number.getText().toString().trim();
        }

        long selid = mSp_area.getSelectedItemId();
        if(selid > 0 && !sNumber.equals("")){
            for (Jyxkz jyxkz : mDatas) {
                if (jyxkz.getDQNAME().equals(sArea) && jyxkz.getPERMIT_NUM().contains(sNumber)) {
                    list.add(jyxkz);
                }
            }
            if (list.size() == 0) {
                ToastUtil.setToast(mContext, "没有符合条件的结果");
            }
            adapter = new RecycJyxkzAdapter(mContext,list);

        }else if(selid > 0 && sNumber.equals("")){
            for (Jyxkz jyxkz : mDatas) {
                if (jyxkz.getDQNAME().contains(sArea)) {
                    list.add(jyxkz);
                }
            }
            if (list.size() == 0) {
                ToastUtil.setToast(mContext, "没有符合条件的结果");
            }
            adapter = new RecycJyxkzAdapter(mContext,list);

        }else if(selid == 0 && !sNumber.equals("")){
            for (Jyxkz jyxkz : mDatas) {
                if (jyxkz.getPERMIT_NUM().contains(sNumber)) {
                    list.add(jyxkz);
                }
            }
            if (list.size() == 0) {
                ToastUtil.setToast(mContext, "没有符合条件的结果");
            }
            adapter = new RecycJyxkzAdapter(mContext,list);

        }else{
            adapter = new RecycJyxkzAdapter(mContext,mDatas);
        }
        mRv_jyxkz.setAdapter(adapter);

    }
}
