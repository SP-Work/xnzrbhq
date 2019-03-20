package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Group;
import com.otitan.xnbhq.entity.LineInfo;

import java.util.List;

/**
 * Created by sp on 2019/3/18.
 * 样线
 */
public class HarmfulAdapter extends RecyclerView.Adapter<HarmfulAdapter.MyViewHolder> {

    private Context mContext;
    private List<LineInfo> mData;

    private MyItemClickListener mItemClickListener;

    public HarmfulAdapter(Context context, List<LineInfo> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_harmful, parent, false), mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTv_num.setText(mData.get(position).getRouteNum());
        holder.mTv_time.setText(mData.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTv_num;
        private final TextView mTv_time;

        private MyItemClickListener mListener;

        MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);

            mTv_num = itemView.findViewById(R.id.tv_num);
            mTv_time = itemView.findViewById(R.id.tv_time);

            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
