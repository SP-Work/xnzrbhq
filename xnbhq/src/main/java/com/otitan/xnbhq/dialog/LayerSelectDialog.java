package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LayerAdapter;
import com.otitan.xnbhq.entity.ActionMode;
import com.otitan.xnbhq.entity.MyLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/6/2.
 * 图层选择dialog
 */

public class LayerSelectDialog extends Dialog implements ListView.OnItemClickListener{

    private Context mContext;
    private List<MyLayer> layerList = new ArrayList<>();
    private SetOnItemClickListener setOnItemClickListener;
    private ActionMode actionMode;


    public LayerSelectDialog(@NonNull Context context, @StyleRes int themeResId,List<MyLayer> list,ActionMode mode) {
        super(context, themeResId);
        this.mContext = context;
        this.layerList = list;
        this.actionMode = mode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_featureselect);
        setCanceledOnTouchOutside(true);

        ListView listview = (ListView) findViewById(R.id.listview_layers);
        LayerAdapter adapter = new LayerAdapter(layerList, mContext);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    public void setLayerOnItemClickListener(SetOnItemClickListener layerOnItemClickListener){
        if(layerOnItemClickListener != null){
            this.setOnItemClickListener = layerOnItemClickListener;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setOnItemClickListener.setLayerOnItemClickListener(parent, view, position, id,actionMode,layerList,this);
    }

    /*点击接口*/
    public interface SetOnItemClickListener{
       void setLayerOnItemClickListener(AdapterView<?> parent, View view, int position, long id, ActionMode mode, List<MyLayer> layerList,LayerSelectDialog layerSelectDialog);
    }
}
