package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.dialog.XzzyDialog;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ResourcesManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Created by li on 2017/6/2.
 * 每木检尺 adapter
 */
public class LxqcMmjcdcAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;

	public LxqcMmjcdcAdapter(Context context, List<HashMap<String, String>> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_mmjcjl, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
			holder.tv8 = (TextView) convertView.findViewById(R.id.tv8);
			holder.tv9 = (TextView) convertView.findViewById(R.id.tv9);
			holder.tv10 = (TextView) convertView.findViewById(R.id.tv10);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> map = list.get(position);
		holder.tv1.setText(map.get("YMH"));
		holder.tv2.setText(map.get("LMLX"));
		final TextView lmlx=holder.tv2;
		holder.tv3.setText(map.get("JCLX"));
		final TextView jclx=holder.tv3;
		holder.tv4.setText(map.get("SHUZ"));
		final TextView shuzhong=holder.tv4;
		holder.tv5.setText(map.get("QQXJ"));
		final TextView qqxj=holder.tv5;
		holder.tv6.setText(map.get("BQXJ"));
		final TextView bqxj=holder.tv6;
		holder.tv7.setText(map.get("DWD"));
		final TextView dwd=holder.tv7;
		holder.tv8.setText(map.get("FWJ"));
		final TextView fwj=holder.tv8;
		holder.tv9.setText(map.get("SPJ"));
		final TextView spj=holder.tv9;
		// 立木类型
		holder.tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> limutemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "LMLX");
				XzzyDialog limudialog = new XzzyDialog(context,"立木类型", limutemp, lmlx,map,"LMLX");
				BussUtil.setDialogParams(context, limudialog, 0.5, 0.5);
			}
		});
		// 检尺类型
		holder.tv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> jclxtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "JCLX");
				XzzyDialog jclxdialog = new XzzyDialog(context,"检尺类型", jclxtemp, jclx,map,"JCLX");
				BussUtil.setDialogParams(context, jclxdialog, 0.5, 0.5);
			}
		});
		// 树种
		holder.tv4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> sztemp =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog szdialog = new XzzyDialog(context,"树种", sztemp, shuzhong,map,"SHUZ");
				BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
			}
		});
		// 前期胸径
		holder.tv5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LxqcUtil.showAlertDialog(context, "前期胸径", qqxj, map, "QQXJ", "0", "1", "2");
			}
		});
		// 本期胸径
		holder.tv6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog xjshuzidialog = new ShuziDialog(context, "本期胸径", bqxj,
						map, "BQXJ", list, null, "0", "1", "");
				BussUtil.setDialogParams(context, xjshuzidialog, 0.5, 0.5);
			}
		});
		// 定位点
		holder.tv7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> dwdtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "DWD");
				XzzyDialog dwddialog = new XzzyDialog(context,"定位点", dwdtemp, dwd,map,"DWD");
				BussUtil.setDialogParams(context, dwddialog, 0.5, 0.5);
			}
		});
		// 方位角
		holder.tv8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog fwjshuzidialog=new ShuziDialog(context, "方位角", fwj, map, "FWJ", list,null,"0","1","");
				BussUtil.setDialogParams(context, fwjshuzidialog, 0.5, 0.5);
			}
		});
		// 水平距
		holder.tv9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog spjdialog=new ShuziDialog(context,  "水平距(米)", spj, map, "SPJ", list,null,"0","1","");
				BussUtil.setDialogParams(context, spjdialog, 0.5, 0.5);
			}
		});
		// 删除
		holder.tv10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.remove(list.get(position));
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public TextView tv6;
		public TextView tv7;
		public TextView tv8;
		public TextView tv9;
		public TextView tv10;
	}
}
