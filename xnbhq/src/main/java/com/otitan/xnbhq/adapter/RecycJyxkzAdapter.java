package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Jyxkz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 32 on 2017/6/27.
 *
 * 木材经营许可证查询 Adapter
 */

public class RecycJyxkzAdapter extends RecyclerView.Adapter<RecycJyxkzAdapter.JyxkzViewHolder> {

    private Context mContext;
    private List<Jyxkz> mDatas = new ArrayList<>();

    public RecycJyxkzAdapter(Context context, List<Jyxkz> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public JyxkzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JyxkzViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_jyxkz, parent,false));
    }

    @Override
    public void onBindViewHolder(JyxkzViewHolder holder, int position) {
        if (mDatas.get(position).getISTG().equals("1")) {
            holder.mTv_ns.setText("已年审");
        } else {
            holder.mTv_ns.setText("未年审");
        }
        holder.mTv_dq.setText(mDatas.get(position).getDQNAME());
        holder.mTv_zh.setText(mDatas.get(position).getPERMIT_NUM());
        holder.mTv_mc.setText(mDatas.get(position).getCOMPANYNAME());
        holder.mTv_dz.setText(mDatas.get(position).getCOMPANYADDR());
        holder.mTv_zx.setText(mDatas.get(position).getSFZX());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class JyxkzViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv_ns; // 是否年审
        private final TextView mTv_dq; // 所属地区
        private final TextView mTv_zh; // 许可证号
        private final TextView mTv_mc; // 企业名称
        private final TextView mTv_dz; // 企业地址
        private final TextView mTv_zx; // 是否注销

        public JyxkzViewHolder(View itemView) {
            super(itemView);
            mTv_ns = (TextView) itemView.findViewById(R.id.tv_ns);
            mTv_dq = (TextView) itemView.findViewById(R.id.tv_dq);
            mTv_zh = (TextView) itemView.findViewById(R.id.tv_zh);
            mTv_mc = (TextView) itemView.findViewById(R.id.tv_mc);
            mTv_dz = (TextView) itemView.findViewById(R.id.tv_dz);
            mTv_zx = (TextView) itemView.findViewById(R.id.tv_zx);
        }
    }
}
