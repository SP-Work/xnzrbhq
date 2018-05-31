package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.ShouCang;
import com.otitan.xnbhq.util.Util;
import com.titan.baselibrary.swipemenulistview.SwipeMenuListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏Adapter
 */
public class ShouCangAdapter extends BaseAdapter {

	private Context context;
	private List<ShouCang> scdlist = new ArrayList<>();
	private SwipeMenuListView lv;
	private TextView zwsj;
	private DecimalFormat decimalFormat = new DecimalFormat("0.000000");

	public ShouCangAdapter(Context context, List<ShouCang> scdlist,SwipeMenuListView lv, TextView zwsj) {
		this.context = context;
		this.scdlist = scdlist;
		this.lv = lv;
		this.zwsj = zwsj;
	}

	@Override
	public int getCount() {
		return scdlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return scdlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_txt, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv);
			holder.tv1.setGravity(Gravity.LEFT);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}

		holder.tv1.setText(Util.round(scdlist.get(position).getLON()));
		holder.tv2.setText(Util.round(scdlist.get(position).getLAT()));
		holder.tv3.setText(scdlist.get(position).getTIME());
		holder.tv4.setText(scdlist.get(position).getMIAOSHU());
		return convertView;
	}

	private class HolderView {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
	}


}
