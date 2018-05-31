package com.titan.gzzhjc.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.titan.gzzhjc.R;

/**
 * 时间选择弹窗（十二五期间，2016年）
 */
public class PopupWindowCustom extends PopupWindow {

    private Context mContext;

    private View view;

    public PopupWindowCustom(Context mContext, View.OnClickListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.item_timeselect_popu, null);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popup_bottom_anim);

        Button btn_sew = (Button) view.findViewById(R.id.item_time_sewq);
        Button btn_2016 = (Button) view.findViewById(R.id.item_time_elyl);
        btn_sew.setOnClickListener(itemsOnClick);
        btn_2016.setOnClickListener(itemsOnClick);

    }

    class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> {

        Context context;
        String[] mDatas;
        View.OnClickListener itemsOnClick;
        public HomeAdapter(Context context, String[] array,View.OnClickListener itemsClick) {
            this.context = context;
            this.mDatas = array;
            this.itemsOnClick = itemsClick;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(context).inflate(R.layout.item_txtview,parent,false);
            MyViewHolder holder = new MyViewHolder(itemview);
            holder.itemView.setOnClickListener(itemsOnClick);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas[position]);
            holder.itemView.setId(position);
        }

        @Override
        public int getItemCount() {
            return mDatas.length;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Button tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (Button) view.findViewById(R.id.item_value);
        }
    }
}
