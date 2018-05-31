package com.otitan.xnbhq.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dao.Jgdc;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.HzbjDialog;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.dialog.XzzyDialog;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.EDUtil;
/**
 * Created by li on 2016/5/26.
 * 角规调查adapter
 */
public class JgdcLineAdapter extends BaseAdapter {
	private MyApplication app;
	private List<Line> lines;
	private String type;
	private Jgdc jgdc;
	private Context mContext;
	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SharedPreferences input_history;
	private String xian, xiang, cun;

	/**
	 * sp :用于存储历史输入记录
	 */
	public JgdcLineAdapter(Context context, List<Line> mLines,
						   SharedPreferences sp, String type, Jgdc jgdc) {
		this.mContext = context;
		this.lines = mLines;
		this.input_history = sp;
		this.type = type;
		this.jgdc = jgdc;

	}

	public void setData(Context context, List<Line> mLines,
						SharedPreferences sp, String type) {
		this.mContext = context;
		this.lines = mLines;
		this.input_history = sp;
		this.type = type;
	}

	@Override
	public int getCount() {
		return lines.size();
	}

	@Override
	public Line getItem(int position) {
		return lines.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
						final ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_listviewinedittxt_line, parent, false);
			holder.etLine = (TextView) convertView.findViewById(R.id.etLine);
			holder.tvLine = (TextView) convertView.findViewById(R.id.tvline);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvLine.setText(lines.get(position).getTview());
		boolean flag = isNoEditField(lines.get(position).getKey());
		holder.etLine.setEnabled(!flag);
		if (TextUtils.isEmpty(lines.get(position).getText())) {
			holder.etLine.setText("");
		} else {
			holder.etLine.setText(lines.get(position).getText());
		}

		if (lines.get(position).isFocus()) {
			if (!holder.etLine.isFocused()) {
				holder.etLine.requestFocus();
			}
		} else {
			if (holder.etLine.isFocused()) {
				holder.etLine.clearFocus();
			}
		}

		holder.etLine.setOnClickListener(new View.OnClickListener() {
			TextView tv = holder.etLine;

			@Override
			public void onClick(View arg0) {
				// 获取字段别名
				String name = lines.get(position).getKey();
				// 1:选择类型字段 2:数字类型 3：文字类型 4：日期选择
				String fieldtype = EDUtil.getEdAttributetype(mContext, name,type);
				if (fieldtype.equals("1")) {
					List<Row> list = null;
					if (name.equals("YSSZ") || name.equals("ZYZCSZ")
							|| name.equals("SZDM")) {
						list = EDUtil.getEdAttributeList(mContext, "YSSZ", type);
					} else {
						list = EDUtil.getEdAttributeList(mContext, name, type);
					}

					if (name.equals("XIANG")) {
						// 获取县名
						for (Line line : lines) {
							if (line.getKey().equals("XIAN")) {
								xian = line.getValue();
							}
						}
						List<Row> xianglist = DataBaseHelper.getXiangList(mContext, xian);
						list = xianglist;
					}
					if (name.equals("CUN")) {
						// 获取县名
						for (Line line : lines) {
							if (line.getKey().equals("XIANG")) {
								xiang = line.getValue();
							}
						}
						List<Row> cunlist = DataBaseHelper.getCunList(mContext,xiang);
						list = cunlist;
					}

					XzzyDialog xzdialog = new XzzyDialog(mContext, list, tv,
							lines.get(position), jgdc, JgdcLineAdapter.this);
					xzdialog.show();
					BussUtil.setDialogParams(mContext, xzdialog, 0.5, 0.5);

				} else if (fieldtype.equals("2"))

				{
					ShuziDialog szdialog = new ShuziDialog(mContext, tv, lines
							.get(position), jgdc, JgdcLineAdapter.this);
					BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);

				} else if (fieldtype.equals("3")) {
					HzbjDialog szdialog = new HzbjDialog(mContext, tv, lines
							.get(position), jgdc, JgdcLineAdapter.this);
					BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);
				}

			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView tvLine;
		TextView etLine;
	}

	/** 获取不可编辑字段 */
	private boolean isNoEditField(String fieldname) {
		List<Row> list = null;

		list = EDUtil.getEdAttributeList(mContext, "notedite", type);
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getName();
			if (name.equals(fieldname)) {
				return true;
			}
		}
		return false;
	}
	/** 获取计算项 */
	public boolean isCalculate(String fieldname) {
		List<Row> list = null;

		list = EDUtil.getEdAttributeList(mContext, "calculate", type);
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getName();
			if (name.equals(fieldname)) {
				return true;
			}
		}
		return false;
	}


	public String getTime(Date date) {
		return dateFormat.format(date);
	}

}
