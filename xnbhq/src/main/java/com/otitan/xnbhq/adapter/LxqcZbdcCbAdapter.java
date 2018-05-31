package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.HzbjDialog;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.util.BussUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 植被调查 adapter
 */

public class LxqcZbdcCbAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	private TextView zongcbpjg;
	private String type;

	// type"1"为草本 "2"为地被物
	public LxqcZbdcCbAdapter(Context context,
							 List<HashMap<String, String>> list, TextView cbpjg, String type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.zongcbpjg = cbpjg;
		this.type = type;
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_zwdcjlcb,null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> map = list.get(position);
		if ("1".equals(type)) {
			if (BussUtil.isEmperty(map.get("CBMC").trim())) {
				holder.tv1.setText(map.get("CBMC").trim());
			} else {
				holder.tv1.setText("");
			}
			if (BussUtil.isEmperty(map.get("CBPJG").trim())) {
				holder.tv2.setText(map.get("CBPJG").trim());
			} else {
				holder.tv2.setText("");
			}
			if (BussUtil.isEmperty(map.get("CBGD").trim())) {
				holder.tv3.setText(map.get("CBGD").trim());
			} else {
				holder.tv3.setText("");
			}
		} else if ("2".equals(type)) {
			if (BussUtil.isEmperty(map.get("DBWMC").trim())) {
				holder.tv1.setText(map.get("DBWMC").trim());
			} else {
				holder.tv1.setText("");
			}
			if (BussUtil.isEmperty(map.get("DBWPJG").trim())) {
				holder.tv2.setText(map.get("DBWPJG").trim());
			} else {
				holder.tv2.setText("");
			}
			if (BussUtil.isEmperty(map.get("DBWGD").trim())) {
				holder.tv3.setText(map.get("DBWGD").trim());
			} else {
				holder.tv3.setText("");
			}
		}

		final TextView cbmc = holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(type)) {
					HzbjDialog hzbjdialog = new HzbjDialog(context, "草本名称", cbmc,map, "CBMC");
					BussUtil.setDialogParams(context, hzbjdialog, 0.5, 0.5);
				} else if ("2".equals(type)) {
					HzbjDialog hzbjdialog = new HzbjDialog(context, "地被物名称", cbmc,map, "DBWMC");
					BussUtil.setDialogParams(context, hzbjdialog, 0.5, 0.5);
				}
			}
		});
		final TextView cbpjgtxt = holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(type)) {
					ShuziDialog shuzidialog = new ShuziDialog(context, "草本平均高(米)", cbpjgtxt, map, "CBPJG",
							list, zongcbpjg, "0",
							"2","");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				} else if ("2".equals(type)) {
					ShuziDialog shuzidialog = new ShuziDialog(context,"地被物平均高(米)", cbpjgtxt, map,
							"DBWPJG", list,
							zongcbpjg, "0", "2","");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			}
		});
		final TextView cbgd = holder.tv3;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(type)) {
					ShuziDialog shuzidialog = new ShuziDialog(context, "草本盖度(%)", cbgd, map, "CBGD",list, null, "1",
							"","");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				} else if ("2".equals(type)) {
					ShuziDialog shuzidialog = new ShuziDialog(context,"地被物盖度(%)", cbgd, map, "DBWGD", list, null, "1",
							"","");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			}
		});
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(type)) {
					list.remove(list.get(position));

					// 计算灌木平均高
					float zongcbpjgf = 0;
					for (int i = 0; i < list.size(); i++) {
						if (!"".equals(list.get(i).get("CBPJG"))) {
							zongcbpjgf = zongcbpjgf
									+ Float.parseFloat(list.get(i).get("CBPJG"));
						}
					}
					String[] split = ((zongcbpjgf / (list.size())) + "").split(
							"\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							zongcbpjgf = Float.parseFloat(split[0] + "."+ split[1].substring(0, 2));
						}else{
							zongcbpjgf = Float.parseFloat(split[0] + "."+ split[1]);
						}
					}
					zongcbpjg.setText(zongcbpjgf + "");
				} else if ("2".equals(type)) {
					list.remove(list.get(position));

					// 计算灌木平均高
					float zongcbpjgf = 0;
					for (int i = 0; i < list.size(); i++) {
						if (!"".equals(list.get(i).get("DBWPJG"))) {
							zongcbpjgf = zongcbpjgf
									+ Float.parseFloat(list.get(i)
									.get("DBWPJG"));
						}
					}
					String[] split = ((zongcbpjgf / (list.size())) + "").split(
							"\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							zongcbpjgf = Float.parseFloat(split[0] + "."+ split[1].substring(0, 2));
						}else{
							zongcbpjgf = Float.parseFloat(split[0] + "."+ split[1]);
						}
					}
					zongcbpjg.setText(zongcbpjgf + "");
				}
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
	}

}
