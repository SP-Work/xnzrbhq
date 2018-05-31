package com.otitan.xnbhq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.RenderSetDialog;
import com.otitan.xnbhq.entity.MyLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 图层渲染Adapter
 */
public class TcRenderAdapter extends BaseAdapter {
	private List<MyLayer> list = new ArrayList<>();
	private LayoutInflater inflater = null;
	private RenderSetDialog renderSetDialog;
	
	public TcRenderAdapter(Context context,List<MyLayer> list,RenderSetDialog dialog) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.renderSetDialog = dialog;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_expandable_group, null);
			holder.tv = (TextView) convertView.findViewById(R.id.id_group_txt);
			holder.tv1 = (TextView) convertView.findViewById(R.id.layer_render);
			holder.tv1.setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).getLname());
		holder.tv1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				renderSetDialog.showLayerRender(list.get(position));
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		TextView tv1;
	}

}
