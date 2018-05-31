package com.titan.gzzhjc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.titan.gzzhjc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/3/2.
 *
 * 首页（MainActivity）各个模块列表适配器
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    Context context;
    List<String> txts = new ArrayList<>();
    List<Integer> lst = new ArrayList<>();
    OnItemRecycleListener itemRecycleListener = null;

    public RecycleAdapter(Context context,List<String> txtsList,List<Integer> ls){
        this.context = context;
        this.txts = txtsList;
        this.lst = ls;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_recyclerview,null);
        ItemHolder itemHolder = new ItemHolder(itemView);
        itemHolder.itemView.setOnClickListener(this);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder iholder = (ItemHolder) holder;
        Drawable drawable = context.getResources().getDrawable(lst.get(position));
        iholder.imageView.setImageDrawable(drawable);
        iholder.textView.setText(txts.get(position));
        holder.itemView.setTag(position);
        holder.itemView.setId(lst.get(position));
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    @Override
    public void onClick(View view) {
        if(itemRecycleListener != null){
            itemRecycleListener.onItemClick(view,view.getTag().toString());
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recyclerView_img);
            textView = (TextView)itemView.findViewById(R.id.recyclerView_txt);
        }
    }

    /**
     * 回调接口
     */
    public interface  OnItemRecycleListener{
        void onItemClick(View view, String str);
    }

    /**
     * 点击事件监听
     */
    public void setOnItemClickListener(OnItemRecycleListener listener){
        itemRecycleListener = listener;
    }

}
