package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Station;
import com.otitan.xnbhq.entity.XdmSearchHistory;
import com.otitan.xnbhq.util.BussUtil;

import java.util.List;

/**
 * 小地名查询Adapter
 * @param <T>
 */
public class SearchXdmAdapter<T> extends BaseAdapter{
	List<T> list = null;
	
	private LayoutInflater inflater = null;
	
	public SearchXdmAdapter(List<T> list, Context context) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(list != null){
			return list.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHoler holder = null;
		if (null == convertView) {
			holder = new ViewHoler();
			convertView = inflater.inflate(R.layout.item_textview, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHoler) convertView.getTag();
		}

		T t = list.get(position);
		String clsname = t.getClass().getName();
		if(clsname.equals(Station.class.getName())){
			Station station = (Station) t;
			if(BussUtil.isEmperty(station.getName())){
				holder.tv1.setText((position+1)+"   "+station.getName());
			}else{
				holder.tv1.setText((position+1)+"   ");
			}
		}else if(clsname.equals(XdmSearchHistory.class.getName())){
			XdmSearchHistory station = (XdmSearchHistory) t;
			if(BussUtil.isEmperty(station.getName())){
				holder.tv1.setText((position+1)+"   "+station.getName());
			}else{
				holder.tv1.setText((position+1)+"    ");
			}
		}
		
		return convertView;
	}
	
	private class ViewHoler{
		TextView tv1;
	}

}
