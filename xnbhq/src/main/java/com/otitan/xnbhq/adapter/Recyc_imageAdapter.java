package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.otitan.xnbhq.R;

import java.util.List;

/**
 * Created by otitan_li on 2018/1/6.
 * Recyc_imageAdapter
 */

public class Recyc_imageAdapter extends RecyclerView.Adapter<Recyc_imageAdapter.MyViewHolder> implements View.OnClickListener{

    private List<String> pathList;
    private Context mContext;
    private int mColumnWidth=100;
    private PicOnclick picOnclick;

    public Recyc_imageAdapter(Context ctx,List<String> list,int width){
        this.mContext = ctx;
        this.pathList = list;
        this.mColumnWidth = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        String path = pathList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.img_pic.setImageBitmap(bitmap);
        holder.img_pic.setOnClickListener(this);
        holder.img_pic.setId(position);
        holder.itemView.setId(position);
        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    @Override
    public void onClick(View v) {
        if(picOnclick != null){
            int id = v.getId();
            picOnclick.setPicOnclick(v, id);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_pic;
        ImageView img_close;

        public MyViewHolder(View v) {
            super(v);
            img_pic = (ImageView) v.findViewById(R.id.imageView_pic);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mColumnWidth,mColumnWidth);
            //params.setMargins(10,10,10,10);
            img_pic.setLayoutParams(params);
            img_close = (ImageView) v.findViewById(R.id.imageView_close);
        }
    }

    /**
     * 图片点击回调接口
     */
    public interface PicOnclick{
        void setPicOnclick(View item, int position);
    }

    /**
     * 调用此方法，实现点击
     * @param onclick
     */
    public void setPicOnclick(PicOnclick onclick){
        this.picOnclick = onclick;
    }

}
