package com.otitan.xnbhq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.otitan.xnbhq.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区县查询Adapter(舍去)
 */
public class QuxianSelectAdapter extends BaseAdapter{

	private List<Map<String, Object>> list = new ArrayList<>();
	private LayoutInflater inflater = null;
    private HashMap<String,Boolean> isSelected = new HashMap<>();
    
	@SuppressLint("UseSparseArrays")
	public QuxianSelectAdapter(Context context,List<Map<String, Object>> list,HashMap<String,Boolean> isSelected) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.isSelected = isSelected;
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_quxianselect, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.item_xb = (CheckBox)convertView.findViewById(R.id.item_xb);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv1.setText(list.get(position).get("name").toString());
		holder.item_xb.setChecked(isSelected.get(list.get(position).get("name").toString()));
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView tv1;
		CheckBox item_xb;
	}
}
