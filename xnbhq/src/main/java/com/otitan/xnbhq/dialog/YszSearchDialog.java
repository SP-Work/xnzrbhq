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
import com.otitan.xnbhq.adapter.RecycYszAdapter;
import com.otitan.xnbhq.entity.Mcjyysz;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.otitan.xnbhq.util.DividerGridItemDecoration;
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
 * 木材经营运输证查询
 */

public class YszSearchDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private ArrayList<Mcjyysz> arrayList = new ArrayList<>();
    private int pageIndex = 1;
    private RecycYszAdapter recycYszAdapter;
    private TextView nodata_textView;
    private List<HashMap<String, String>> sjssLlist = new ArrayList<>();
    private EditText searchHuozhu;
    private RecyclerView recyclerView;

    public YszSearchDialog(@NonNull Context context, @StyleRes int themeResId,List<HashMap<String, String>> sjss ) {
        super(context, themeResId);
        this.mContext = context;
        this.sjssLlist = sjss;
        getYszData(pageIndex);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yszsearch);
        setCanceledOnTouchOutside(false);

        initView();

    }
    /*控件初始化*/
    private void initView(){
        nodata_textView = (TextView) findViewById(R.id.ysz_nodataview);
        recyclerView = (RecyclerView) findViewById(R.id.ysz_rcycle);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        //layoutManager.setOrientation(OrientationHelper.VERTICAL);
        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration(mContext);
        recyclerView.addItemDecoration(itemDecoration);
        recycYszAdapter = new RecycYszAdapter(arrayList,sjssLlist);
        recyclerView.setAdapter(recycYszAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int count = recycYszAdapter.getItemCount();
                    //isAllCount = count % 10;
                    if ((lastItem + 1) == count) {
                        //获取更多数据
                        ToastUtil.setToast(mContext,"获取更多数据");
                        pageIndex++;
                        getYszData(pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItem = layoutManager.findLastVisibleItemPosition();
                //第一次获取数据，若没有，进度条会一直转，需手动隐藏
            }
        });

        //关闭按钮
        ImageView close =(ImageView) findViewById(R.id.ysz_close);
        close.setOnClickListener(new CancleListener(this));

        TextView search =(TextView) findViewById(R.id.search_btn);
        search.setOnClickListener(this);

        searchHuozhu =(EditText) findViewById(R.id.huohzu_edittext);

        Spinner spinner =(Spinner) findViewById(R.id.jcdw_view);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //选择单位后 更新adapter
                String key = parent.getSelectedItem().toString();
                if(position == 0){
                    recycYszAdapter = new RecycYszAdapter(arrayList,sjssLlist);
                    recyclerView.setAdapter(recycYszAdapter);
                    showView(arrayList);
                    return;
                }
                ArrayList<Mcjyysz> aList = new ArrayList<>();
                for(Mcjyysz mcjyysz : arrayList){
                    if(mcjyysz.getXNAME().contains(key)){
                        aList.add(mcjyysz);
                    }
                }
                recycYszAdapter = new RecycYszAdapter(aList,sjssLlist);
                recyclerView.setAdapter(recycYszAdapter);
                showView(aList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /*获取运输证台账数据*/
    private void getYszData(int pageIndex){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().getYszData(pageIndex+"");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext,"网络问题,请检查网络");
                    }

                    @Override
                    public void onNext(String s) {
                        if (!s.equals("0")) {
                            ArrayList<Mcjyysz> json = new Gson().fromJson(s, new TypeToken<ArrayList<Mcjyysz>>() {}.getType());
                            if(json != null && json.size() > 0){
                                //arrayList.clear();
                                arrayList.addAll(json);
                                recycYszAdapter.notifyDataSetChanged();
                                showView(arrayList);
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn:
                //查询更新adapter
                if(TextUtils.isEmpty(searchHuozhu.getText())){
                    return;
                }
                String txt = searchHuozhu.getText().toString().trim();
                search(txt);
                break;
            default:
                break;
        }
    }

    /*查询按钮查询方法*/
    private void search(String key){
        if(key.equals("")){
            recycYszAdapter = new RecycYszAdapter(arrayList,sjssLlist);
            recyclerView.setAdapter(recycYszAdapter);
            showView(arrayList);
            return;
        }
        ArrayList<Mcjyysz> aList = new ArrayList<>();
        for(Mcjyysz mcjyysz : arrayList){
            if(mcjyysz.getHZ().contains(key)){
                aList.add(mcjyysz);
            }
        }

        recycYszAdapter = new RecycYszAdapter(aList,sjssLlist);
        recyclerView.setAdapter(recycYszAdapter);
        showView(aList);
    }

    /*显示或者隐藏无数据控件*/
    private void showView(ArrayList<Mcjyysz> aList){
        if(aList.size() > 0){
            nodata_textView.setVisibility(View.GONE);
        }else{
            nodata_textView.setVisibility(View.VISIBLE);
        }
    }
}
