package com.otitan.xnbhq.listviewinedittxt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ED_XBEditActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.HzbjDialog;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.dialog.XzzyDialog;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.EDUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.timepaker.TimePopupWindow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 二调数据编辑适配器
 */
public class EdLineAdapter extends BaseAdapter {
	private List<Line> lines;

	private String type;
	private ED_XBEditActivity mContext;
	public boolean saveflag = false;
	@SuppressLint("UseSparseArrays")
	public HashMap<String, String> hashMap = new HashMap<String, String>();
	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private String xian, xiang, cun;
	// 当前选择图层
	public FeatureLayer curfeaturelayer;
	// 当前选择feature
	public GeodatabaseFeature curfeture;
	public Map<String, Object> attribute;

	public EdLineAdapter(ED_XBEditActivity context, List<Line> lines) {
		this.mContext = context;
		this.lines = lines;
	}

	/**
	 * sp :用于存储历史输入记录
	 */
	public EdLineAdapter(ED_XBEditActivity context, List<Line> mLines, String type) {
		this.mContext = context;
		this.lines = mLines;
		this.type = type;

	}

	/**
	 * sp :用于存储历史输入记录
	 */
	public EdLineAdapter(ED_XBEditActivity context, List<Line> mLines, String type,
						 GeodatabaseFeature gf, FeatureLayer fl) {
		this.mContext = context;
		this.lines = mLines;
		this.type = type;
		this.curfeture = gf;
		this.curfeaturelayer = fl;
		if(gf != null){
			this.attribute = gf.getAttributes();
		}
	}

	public void setData(ED_XBEditActivity context, List<Line> mLines, String type) {
		this.mContext = context;
		this.lines = mLines;
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

		final Line line = lines.get(position);
		holder.tvLine.setText(line.getTview());
		boolean flag = isNoEditField(line.getKey());
		holder.etLine.setEnabled(!flag);
		// 判断是否是计算项
		// boolean iscalculate = isCalculate(lines.get(position).getName());
		/*
		 * if (iscalculate) { holder.etLine.setBackgroundColor(Color.YELLOW); }
		 */
		if (TextUtils.isEmpty(line.getText())) {
			holder.etLine.setText("");
		} else {
			holder.etLine.setText(line.getText());
		}

		if (line.isFocus()) {
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
				getLineDes(lines.get(position), tv);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView tvLine;
		TextView etLine;
	}

	public void getLineDes(Line line, TextView tv) {
		// 获取字段别名
		String title = line.getTview();
		String name = line.getKey();
		if(title.contains("照片") || title.contains("相片")){
			showTakePhoto(line);
			return;
		}
		// 1:选择类型字段 2:数字类型 3：文字类型 4：日期选择
		String fieldtype = EDUtil.getEdAttributetype(mContext, name, type);
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
				for (Line line1 : lines) {
					if (line1.getKey().equals("XIAN")) {
						xian = line1.getValue();
						break;
					}
				}
				List<Row> xianglist = DataBaseHelper.getXiangList(mContext,
						xian);
				list = xianglist;
			}
			if (name.equals("CUN")) {
				// 获取县名
				for (Line line1 : lines) {
					if (line1.getKey().equals("XIANG")) {
						xiang = line1.getValue();
						break;
					}
				}
				List<Row> cunlist = DataBaseHelper.getCunList(mContext, xiang);
				list = cunlist;
			}

			XzzyDialog xzdialog = new XzzyDialog(mContext, list, tv, line,
					this, curfeture, curfeaturelayer);
			xzdialog.show();
			BussUtil.setDialogParams(mContext, xzdialog, 0.5, 0.5);

		} else if (fieldtype.equals("2")) {
			ShuziDialog szdialog = new ShuziDialog(mContext, tv, line, this,
					curfeture, curfeaturelayer);
			szdialog.show();
			BussUtil.setDialogParams(mContext, szdialog, 0.5, 0.5);

		} else if (fieldtype.equals("3")) {
			HzbjDialog wbdialog = new HzbjDialog(mContext, title, tv, line,
					this);
			wbdialog.show();
			BussUtil.setDialogParams(mContext, wbdialog, 0.5, 0.5);

		} else if (fieldtype.equals("4")) {
			// hide(holder.etLine);
			initSelectTimePopuwindow(line, tv);
			// initSelectTimePopuwindow(holder.etLine);

		} else {
			HzbjDialog wbdialog = new HzbjDialog(mContext, title, tv, line,
					this);
			wbdialog.show();
			BussUtil.setDialogParams(mContext, wbdialog, 0.5, 0.5);
		}
	}

	/** 获取不可编辑字段 */
	public boolean isNoEditField(String fieldname) {
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

	public void initSelectTimePopuwindow(final View view) {
		TimePopupWindow timePopupWindow = new TimePopupWindow(mContext,
				TimePopupWindow.Type.ALL);
		timePopupWindow.setCyclic(true);
		timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				String seltime = getTime(date);
				((TextView) view).setText(seltime);
			}
		});
		timePopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0, new Date());
	}

	/** 时间选择popupwindow */
	public void initSelectTimePopuwindow(final Line line,
										 final TextView editText) {
		TimePopupWindow timePopupWindow = new TimePopupWindow(mContext,
				TimePopupWindow.Type.ALL);
		timePopupWindow.setCyclic(true);
		// 时间选择后回调
		timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				String bfText = line.getText();
				String seltime = getTime(date);
				editText.setText(seltime);
				line.setText(seltime);
				updataData(line, bfText, seltime);
				// setEditTextCursorLocation(editText);
			}
		});
		timePopupWindow.showAtLocation(editText, Gravity.BOTTOM, 0, 0,
				new Date(), false);
	}

	public String getTime(Date date) {
		return dateFormat.format(date);
	}

	/** 保存数据 */
	public void updataData(Line line, String bfText, String value) {
		if (bfText == null) {
			if (line.getText().equals("")) {
				return;
			}
		} else {
			if (line.getText().equals(bfText)) {
				return;
			}
		}
		int length = line.getfLength();
		int size = 0;
		if (value != null) {
			size = value.length();
		}
		if (size > length) {
			ToastUtil.setToast(mContext, "输入值长度大于数据库长度");
			return;
		}

		String key = line.getKey();
		attribute.put(key, value);

		Graphic updateGraphic = new Graphic(curfeture.getGeometry(),
				curfeture.getSymbol(), attribute);
		FeatureTable featureTable = curfeaturelayer.getFeatureTable();
		try {
			long featureid = curfeture.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e) {
			e.printStackTrace();
			attribute.put(line.getKey(), bfText);
			line.setText(bfText);
			line.setValue(bfText);
			ToastUtil.setToast((Activity) mContext, "更新失败");
			return;
		}
		line.setValue(value);
		line.setText(value);
		ToastUtil.setToast((Activity) mContext, "更新成功");
		lines.set(line.getNum(), line);
		notifyDataSetChanged();
	}

	/** 拍照 */
	public void showTakePhoto(final Line line) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_takephoto);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog.findViewById(R.id.edittxt_input);
		edit.setText(line.getText());
		CursorUtil.setEditTextLocation(edit);

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				line.setText(edit.getText().toString());
				mContext.updateZp(edit.getText().toString(), line, bfText);
				dialog.dismiss();
			}
		});

		TextView takephoto = (TextView) dialog.findViewById(R.id.takephoto);
		takephoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 拍照
				mContext.takephoto(line, edit);
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		TextView lookpic = (TextView) dialog.findViewById(R.id.lookphoto);
		lookpic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 图片浏览
				mContext.lookpictures(line);
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}


}
