package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.AddLayerAdapter;
import com.otitan.xnbhq.mview.ILayerControlView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sp on 2018/3/20.
 * 添加图层
 */

public class AddLayerDialog extends Dialog {

    private Context mContext;

    Map<String, List<File>> layerMap = new HashMap<>();
    List<String> layerList = new ArrayList<>();

    private ILayerControlView controlView;

    //public Map<String, ArcGISLocalTiledLayer> imgTileLayerMap = new HashMap<>();

    /**
     * 自定义Dialog监听器
     */
    public interface MyListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void addFile(List<File> file, Map<String, List<File>> map);
    }

    private MyListener myListener;

    public AddLayerDialog(@NonNull Context context, @StyleRes int themeResId, ILayerControlView view, MyListener myListener) {
        super(context, themeResId);
        this.mContext = context;
        this.controlView = view;
        this.myListener = myListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addlayer);

        initView();
    }

    private void initView() {
        ImageView iv_close = (ImageView) findViewById(R.id.iv_close);
        RecyclerView rv_layer = (RecyclerView) findViewById(R.id.rv_layer);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final List<File> groups = MyApplication.resourcesManager.getOtmsFolder(controlView.getSysLayerData());
        final List<Map<String, List<File>>> child = MyApplication.resourcesManager.getChildeData(mContext, groups);
        List<File> fileList = MyApplication.resourcesManager.getImgTitlePath();
        for (int i = 0; i < groups.size(); i++) {
            layerList.add(groups.get(i).getName());
        }

        //创建默认的线性LayoutManager, 竖直排布
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_layer.setLayoutManager(layoutManager);
        //创建并设置Adapter
        AddLayerAdapter adapter = new AddLayerAdapter(mContext, layerList);
        rv_layer.setAdapter(adapter);

        adapter.setItemClickListener(new AddLayerAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = layerList.get(position);
                layerMap.put(name, child.get(0).get(name));
                myListener.addFile(groups, layerMap);
                dismiss();
            }
        });
    }
}
