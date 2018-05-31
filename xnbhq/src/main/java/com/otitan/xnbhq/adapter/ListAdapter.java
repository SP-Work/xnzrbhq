package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.R;

/**
 * Created by li on 2017/6/2.
 * 汉字编辑窗口dialog的adapter
 */

public class ListAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private Context context;
    private List<String> list = new ArrayList<>();

    public ListAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_xzzy, null);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String item = list.get(position);
        holder.tv1.setText(item);
        return convertView;
    }

    public final class ViewHolder {
        public TextView tv1;
    }

}
