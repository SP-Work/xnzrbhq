package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otitan.xnbhq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32 on 2017/12/14.
 * 内容搜索列表
 */
public class AddLayerAdapter extends RecyclerView.Adapter<AddLayerAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mDates = new ArrayList<>();

    private MyItemClickListener mItemClickListener;

    public AddLayerAdapter(Context context, List<String> dates) {
        mContext = context;
        mDates = dates;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_addlayer, null), mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTv_layer.setText(mDates.get(position));
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTv_layer;

        private MyItemClickListener mListener;

        public MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);

            mTv_layer = (TextView) itemView.findViewById(R.id.tv_layer);

            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         */
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
