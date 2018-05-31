package com.otitan.xnbhq.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.otitan.xnbhq.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Created by li on 2016/5/26.
 * FeatureResultAdapter 二调属性编辑adapter
 */
public class FeatureResultAdapter extends BaseAdapter {

	private Context mContext;
	private List<GeodatabaseFeature> list = new ArrayList<>();
	private Map<GeodatabaseFeature,String> selMap = new HashMap<>();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");
	
	
	public FeatureResultAdapter(Context context, List<GeodatabaseFeature> list,Map<GeodatabaseFeature,String> selMap) {
		this.mContext = context;
		this.list = list;
		this.selMap = selMap;
		notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layer_txt, null);
			int color = mContext.getResources().getColor(R.color.blue);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_layername);
			holder.tv.setTextColor(color);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv_layerid);
			holder.tv1.setTextColor(color);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv_featuremj);
			holder.tv3.setTextColor(color);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		long field = list.get(position).getId();
		String cname = selMap.get(list.get(position));
		String lname = list.get(position).getTable().getTableName();
		holder.tv.setText(cname+"--"+lname);
		holder.tv1.setText(field+"");
		double area = list.get(position).getGeometry().calculateArea2D()*0.0015;
		String mj = decimalFormat.format(area);
		holder.tv3.setText(mj);
		
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		TextView tv1;
		TextView tv3;
	}

}
