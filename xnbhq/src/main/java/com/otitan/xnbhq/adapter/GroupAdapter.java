package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 2019/3/18.
 * 调查组
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private Context mContext;
    private List<Group> mData;

    private MyItemClickListener mItemClickListener;

    public GroupAdapter(Context context, List<Group> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false), mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTv_name.setText(mData.get(position).getName());
        holder.mTv_time.setText(mData.get(position).getTime());
        holder.mTv_member.setText(mData.get(position).getMember());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTv_name;
        private final TextView mTv_time;
        private final TextView mTv_member;

        private MyItemClickListener mListener;

        MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);

            mTv_name = itemView.findViewById(R.id.tv_name);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_member = itemView.findViewById(R.id.tv_member);

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
