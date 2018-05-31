package com.otitan.xnbhq.adapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.SlzylxqcYdyzActivity;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.titan.baselibrary.swipemenulistview.SwipeMenu;
import com.titan.baselibrary.swipemenulistview.SwipeMenuCreator;
import com.titan.baselibrary.swipemenulistview.SwipeMenuItem;
import com.titan.baselibrary.swipemenulistview.SwipeMenuListView;
import com.titan.baselibrary.timepaker.TimePopupWindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 连续清查 样地因子 adapter
 */
public class LxqcYdyzAdapter extends BaseAdapter {

	private List<Line> lines = new ArrayList<>();
	private SlzylxqcYdyzActivity mContext;
	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DecimalFormat format = new DecimalFormat(".000000");
	private String xian, xiang, cun;

	private DecimalFormat df = new DecimalFormat(".00");

	private String pname;
	private FeatureLayer featureLayer;
	private GeodatabaseFeature selGeoFeature;
	private Map<String, Object> attribute = new HashMap<>();

	public LxqcYdyzAdapter(List<Line> lines) {
		this.lines = lines;
	}

	public LxqcYdyzAdapter(SlzylxqcYdyzActivity context, List<Line> lines,String name,
						   FeatureLayer featureLayer, GeodatabaseFeature gfFeature) {
		this.mContext = context;
		this.lines = lines;
		this.pname = name;
		this.featureLayer = featureLayer;
		this.selGeoFeature = gfFeature;
		this.attribute = gfFeature.getAttributes();
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
		holder.etLine.setText(line.getText());
		holder.etLine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String tview = line.getTview();
				String key = line.getKey();
				CodedValueDomain domain = line.getDomain();
				hide(holder.etLine);
				if (tview.contains("照片") || tview.contains("相片")) {
					showTakePhoto(line);
				} else if (tview.contains("时间") || tview.equals("dcsj")
						|| tview.contains("日期")) {
					initSelectTimePopuwindow(line, holder.etLine);
				} else if (tview.contains("坐标") || tview.contains("经度")
						|| tview.contains("纬度")) {
					showLonLatDialog(line, holder.etLine);
				} else {
					if (domain != null) {
						ToastUtil.setToast(mContext, "domain");
						Map<String, String> values = domain.getCodedValues();
						if (values != null && values.size() > 0) {
							showTowDialog(line, values, holder.etLine, tview,
									position);
							return;
						}
					}
					List<Row> lst = isDMField(key);
					if (lst == null) {
						showKeyDialog(line, holder.etLine);
					} else {
						int size = lst.size();
						if (size > 0) {
							showTowDialog(line, lst, holder.etLine, tview,
									position);
						} else {
							showKeyDialog(line, holder.etLine);
						}
					}

				}
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView tvLine;
		TextView etLine;
	}

	/** 隐藏软件盘 */
	private void hide(TextView text) {
		InputMethodManager imm = (InputMethodManager) text.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
	}

	/** 时间选择popupwindow */
	public void initSelectTimePopuwindow(final Line line,final TextView editText) {
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
				CursorUtil.setTextViewLocation(editText);
			}
		});
		timePopupWindow.showAtLocation(editText, Gravity.BOTTOM, 0, 0,
				new Date(), false);
	}

	/** 获取当前时间 */
	public String getTime(Date date) {
		return dateFormat.format(date);
	}

	/** 获取当前经纬度 */
	private void showLonLatDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_lonlat_input);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_lonlat);
		aliasTxt.setText(line.getTview());

		TextView txtlon = (TextView) dialog.findViewById(R.id.edittxt_input_lon);
		String lonTxt = format.format(BaseActivity.currentLon) + "";
		txtlon.setText(lonTxt);
		TextView txtlat = (TextView) dialog.findViewById(R.id.edittxt_input_lat);
		String latTxt = format.format(BaseActivity.currentLat) + "";
		txtlat.setText(latTxt);
		final TextView txtx = (TextView) dialog.findViewById(R.id.edittxt_input_x);
		String xTxt = format.format(BaseActivity.currentPoint.getX()) + "";
		txtx.setText(xTxt);
		final TextView txty = (TextView) dialog.findViewById(R.id.edittxt_input_y);
		String yTxt = format.format(BaseActivity.currentPoint.getY()) + "";
		txty.setText(yTxt);

		TextView back = (TextView) dialog.findViewById(R.id.editlonlat_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		TextView sure = (TextView) dialog.findViewById(R.id.editlonlat_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				dialog.dismiss();
				String key = line.getTview();
				String value = "";
				if (key.contains("横坐标") || key.contains("经度")) {
					String x = txtx.getText().toString();
					editText.setText(x);
					line.setText(x);
					value = x;
				} else if (key.contains("纵坐标") || key.contains("纬度")) {
					String y = format.format(Double.parseDouble(txty.getText()
							.toString())) + "";
					editText.setText(y);
					line.setText(y);
					value = y;
				}
				updataData(line, bfText, value);
				CursorUtil.setTextViewLocation(editText);
			}
		});

		BussUtil.setDialogGravity_Center(mContext, dialog, 0.55, 0.55);
	}

	/** 拍照 */
	private void showTakePhoto(final Line line) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_takephoto);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
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
				hide(edit);
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

	/** 代码dialog弹出 */
	private void showTowDialog(final Line line, final Map<String, String> map,
							   final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog
				.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();
		seltext.setText(line.getText());

		final ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<>();
		// lst.add("无");
		for (String key : map.keySet()) {
			String value = map.get(key);
			lst.add(key + "-" + value);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
				R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				position = psition;
				String selTxt = lst.get(psition);
				String text = selTxt.split("-")[1];
				seltext.setText(text);
				CursorUtil.setTextViewLocation(seltext);
			}
		});

		seltext.addTextChangedListener(new TextWatcher() {
			String bf = "";

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				bf = arg0.toString();
			}

			@Override
			public void afterTextChanged(Editable et) {
				String af = et.toString();
				if (!af.equals(bf)) {
					final List<String> list = new ArrayList<>();
					for (String str : lst) {
						if (str.contains(af)) {
							list.add(str);
						}
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							mContext, R.layout.myspinner, list);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int psition, long arg3) {
							String selTxt = list.get(psition);
							String text = selTxt.split("-")[1];
							seltext.setText(text);
							CursorUtil.setTextViewLocation(seltext);
						}
					});
					notifyDataSetChanged();
				}
			}
		});

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String text = seltext.getText().toString();
				String value = "";
				String txt = "";
				if (position != -1) {
					value = lst.get(position).split("-")[0];
					txt = lst.get(position).split("-")[1];
					if (text.contains(txt)) {
						value = text.replace(txt, value);
					} else {
						value = text;
					}
				} else {
					value = text;
				}
				String bfText = line.getText();
				editText.setText(text);
				CursorUtil.setTextViewLocation(editText);
				line.setText(txt);
				updataData(line, bfText, value);
				CursorUtil.setTextViewLocation(editText);
				dialog.dismiss();
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		BussUtil.setDialogGravity_Center(mContext, dialog, 0.5, 0.5);
	}

	/** 检测字段是那种类型的字段 */
	private List<Row> isDMField(String fieldname) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, fieldname);
		return list;
	}

	/** 弹出输入框dialog */
	private void showKeyDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_input);
		dialog.setCanceledOnTouchOutside(false);
		final SharedPreferences preferences = mContext.getSharedPreferences(
				line.getKey(), Context.MODE_PRIVATE);
		final Set<String> items = preferences.getStringSet(line.getKey(),
				new HashSet<String>());
		final ArrayList<String> list = new ArrayList<>();
		if (items.size() > 0) {
			for (String str : items) {
				list.add(str);
			}
		}
		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
		if (line.getTview().contains("面积")) {
			double area = selGeoFeature.getGeometry().calculateArea2D();
			edit.setText(df.format(Math.abs(area) / 667));
		} else {
			edit.setText(editText.getText().toString());
		}
		CursorUtil.setEditTextLocation(edit);

		SwipeMenuListView listView = (SwipeMenuListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.myspinner, list);
		listView.setAdapter(adapter);

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				hide(edit);
				final String strTxt = edit.getText() == null ? null : edit.getText().toString();
				editText.setText(strTxt);
				dialog.dismiss();
				line.setText(strTxt);
				updataData(line, bfText, strTxt);
				CursorUtil.setTextViewLocation(editText);
				updateSet(preferences, items, line, strTxt);
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hide(edit);
				dialog.dismiss();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				String selTxt = list.get(psition);
				edit.setText(selTxt);
				CursorUtil.setTextViewLocation(edit);
			}
		});

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				addMenuItem(menu);

			}
		};
		// set creator
		listView.setMenuCreator(creator);

		// step 2. listener item click event
		listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						String str = list.get(position);
						list.remove(str);
						adapter.notifyDataSetChanged();
						items.remove(str);
						updateSet(preferences, items, line);
						break;
				}
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	private void addMenuItem(SwipeMenu menu){
		SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
		// set item background
		deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
		// set item width
		deleteItem.setWidth(Util.dp2px(mContext,90));
		// set a icon
		deleteItem.setIcon(R.drawable.ic_delete);
		// add to menu
		menu.addMenuItem(deleteItem);
	}

	/** 保存数据 */
	private void updataData(Line line, String bfText, String value) {
		if (bfText == null) {
			if (line.getText().equals("")) {
				return;
			}
		} else {
			String txt = line.getText();
			if (txt != null && txt.equals(bfText)) {
				return;
			}
		}

		if(value.contains("-")){
			value = value.split("-")[0];
		}

		int length = line.getfLength();
		int size = 0;
		if (value != null) {
			size = value.length();
		}
		if (line.getFieldType() == Field.esriFieldTypeString) {
			if (size > length) {
				ToastUtil.setToast(mContext, "输入值长度大于数据库长度");
				return;
			}
		}
		String key = line.getKey();
		if (line.getFieldType() == Field.esriFieldTypeDate) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(value);
				attribute.put(key, date);
			} catch (ParseException e) {
				ToastUtil.setToast(mContext, "输入数据错误");
				e.printStackTrace();
				return;
			}
		} else if (line.getFieldType() == Field.esriFieldTypeDouble) {
			try {
				double dd = Double.parseDouble(value);
				attribute.put(key, dd);
			} catch (NumberFormatException e) {
				ToastUtil.setToast(mContext, "输入数据错误");
				e.printStackTrace();
				return;
			}
		} else {
			attribute.put(key, value);
		}

		Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),
				selGeoFeature.getSymbol(), attribute);
		FeatureTable featureTable = featureLayer.getFeatureTable();
		try {
			long featureid = selGeoFeature.getId();
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
		ToastUtil.setToast((Activity) mContext, "更新成功");
		lines.set(line.getNum(), line);
		notifyDataSetChanged();

		/* 更新小班号 */
		for (Row row : mContext.gcmc) {
			if (row.getName().equals(line.getKey())) {
				mContext.ydhselect = "";
				mContext.getXbhData(pname);
			}
		}

	}

	/** 更新历史录入数据 */
	private void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line, String strTxt) {
		if (!items.contains(strTxt.trim())) {
			items.add(strTxt.trim());
			Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(line.getKey(), items).apply();
		}
	}

	/** 更新历史录入数据 */
	private void updateSet(SharedPreferences preferences, Set<String> items,Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).apply();
	}

	/** 代码dialog弹出 */
	int position = -1;
	private void showTowDialog(final Line line, final List<Row> row,
							   final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog
				.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();

		final ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
		// lst.add("无");
		if (row.size() > 0) {
			for (Row r : row) {
				String id = r.getId();
				String value = r.getName();
				lst.add(id + "-" + value);
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				position = psition;
				String selTxt = lst.get(psition);
				String text = selTxt.split("-")[1];
				seltext.setText(text);
				CursorUtil.setTextViewLocation(seltext);
			}
		});

		seltext.addTextChangedListener(new TextWatcher() {
			String bf = "";

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				bf = arg0.toString();
			}

			@Override
			public void afterTextChanged(Editable et) {
				String af = et.toString();
				if (!af.equals(bf)) {
					final List<String> list = new ArrayList<String>();
					for (String str : lst) {
						if (str.contains(af)) {
							list.add(str);
						}
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							mContext, R.layout.myspinner, list);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int psition, long arg3) {
							String selTxt = list.get(psition);
							String text = selTxt.split("-")[1];
							seltext.setText(text);
							CursorUtil.setTextViewLocation(seltext);
						}
					});
					notifyDataSetChanged();
				}
			}
		});

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String text = seltext.getText().toString();
				String value = "";
				String txt = "";
				if (position != -1) {
					value = lst.get(position).split("-")[0];
					txt = lst.get(position).split("-")[1];
					if (text.contains(txt)) {
						value = text.replace(txt, value);
					} else {
						value = text;
					}
				} else {
					value = text;
				}
				String bfText = line.getText();
				editText.setText(text);
				CursorUtil.setTextViewLocation(editText);
				line.setText(txt);
				updataData(line, bfText, value);
				CursorUtil.setTextViewLocation(editText);
				dialog.dismiss();
			}
		});

		TextView back = (TextView) dialog.findViewById(R.id.textView_two_back);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		BussUtil.setDialogGravity_Center(mContext, dialog, 0.5, 0.5);
	}




}
