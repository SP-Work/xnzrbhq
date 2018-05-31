package com.otitan.xnbhq.listviewinedittxt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.FeatureLayer.SelectionMode;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.edite.activity.XbEditActivity;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.titan.baselibrary.swipemenulistview.SwipeMenu;
import com.titan.baselibrary.swipemenulistview.SwipeMenuCreator;
import com.titan.baselibrary.swipemenulistview.SwipeMenuItem;
import com.titan.baselibrary.swipemenulistview.SwipeMenuListView;
import com.titan.baselibrary.timepaker.TimePopupWindow;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * listview item 数据加载适配器
 */
@SuppressLint("SimpleDateFormat")
public class LineAdapter extends BaseAdapter {

	private List<Line> lines;
	private XbEditActivity mContext;
	private DecimalFormat format = new DecimalFormat("0.000000");
	DecimalFormat df = new DecimalFormat("0.00");
	private double zlchl = 0;//平均造林成活率

	private List<GeodatabaseFeature> yddPointList = new ArrayList<>();

	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String pname;
	/*   */
	private FeatureLayer featureLayer;
	private GeodatabaseFeature selGeoFeature;
	private Map<String, Object> attribute;

	private MyFeture myFeture = null;

	public LineAdapter(XbEditActivity context, List<Line> lines, MyFeture myFeture) {
		this.mContext = context;
		this.lines = lines;
		this.myFeture = myFeture;
		this.pname = myFeture.getPname();
		this.featureLayer = myFeture.getMyLayer().getLayer();
		this.selGeoFeature = myFeture.getFeature();
		this.attribute = selGeoFeature.getAttributes();
		//SharedPreferences preferences = context.getSharedPreferences(pname+"mustfield", Context.MODE_PRIVATE);
		//getGeometryInfo(myFeture.getFeature().getGeometry());
	}

	@Override
	public int getCount() {
		if (lines != null) {
			return lines.size();
		}
		return 0;
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

		if (holder.etLine.getTag() instanceof TextWatcher) {
			holder.etLine.removeTextChangedListener((TextWatcher) (holder.etLine.getTag()));
		}

		final Line line = lines.get(position);
		String alias = line.getTview();
		holder.tvLine.setText(alias);

		String key = line.getKey();
		boolean flag = false;
		String layername = featureLayer.getFeatureTable().getTableName();
		if (layername.contains("设计") || pname.contains("设计")) {
			flag = true;
		} else {
			flag = isNoEditField(key);
		}

		holder.etLine.setText(line.getText());
		holder.etLine.setEnabled(!flag);

		if (flag) {
			//holder.tvLine.setBackgroundColor(Color.GRAY);
			holder.etLine.setBackgroundColor(Color.GRAY);
			holder.etLine.setClickable(false);
		} else {
			holder.tvLine.setBackgroundColor(Color.WHITE);
			holder.etLine.setBackgroundColor(Color.WHITE);
		}

		if(!line.isNullable() && !line.getKey().contains("OBJECTID")){
			holder.tvLine.setBackgroundColor(Color.RED);
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
					if(BaseActivity.currentLon == 0.0){
						ToastUtil.setToast(mContext, "未取得坐标位置");
						return;
					}
					showLonLatDialog(line, holder.etLine);
				} else {
					if (domain != null) {
						Map<String, String> values = domain.getCodedValues();
						if(tview.contains("乡") || tview.contains("xiang") || tview.contains("XIANG")){
							String str = "";
							for(Line ln : lines){
								if(ln.getTview().contains("县")){
									Object obj = attribute.get(ln.getKey());
									str = obj == null ? "" : obj.toString() ;
									break;
								}
							}

							Map<String, String> value = new HashMap<String, String>();
							for(String vv : values.keySet()){
								if(vv.contains(str)){
									value.put(vv,values.get(vv));
								}else if(!key.contains(str) && vv.contains(str+key)){
									value.put(vv,values.get(vv));
								}
							}
							values = value;
						}else if(tview.contains("村") || tview.contains("cun")||tview.contains("CUN")){
							String xiang = "";
							String xian = "";
							for(Line ln : lines){
								if(ln.getTview().contains("乡")){
									Object obj = attribute.get(ln.getKey());
									xiang = obj == null ? "" : obj.toString() ;
								}else if(ln.getTview().contains("县")){
									Object obj = attribute.get(ln.getKey());
									xian = obj == null ? "" : obj.toString() ;
								}
							}
							Map<String, String> value = new HashMap<String, String>();
							for(String vv : values.keySet()){
								if(xiang.contains(xian)){
									if(vv.contains(xiang)){
										value.put(vv,values.get(vv));
									}
								}else{
									if(vv.contains(xian+xiang)){
										value.put(vv,values.get(vv));
									}
								}
							}
							values = value;
						}
						if (values != null && values.size() > 0) {
							showTowDialog(line, values, holder.etLine, tview,position);
							return;
						}
					}else{
						if(line.getTview().contains("县")){
							Map<String, String> values = Util.getXXCDomain(mContext, key,"", "xian");
							if (values != null && values.size() > 0) {
								showTowDialog(line, values, holder.etLine, tview,position);
							}
						}else if(line.getTview().contains("乡")){
							String xianD = "";
							for(Line ln : lines){
								if(ln.getTview().contains("县")){
									Object obj = attribute.get(ln.getKey());
									xianD = obj == null ? "" : obj.toString() ;
									break;
								}
							}

							Map<String, String> values = Util.getXXCDomain(mContext, key, xianD, "xiang");
							if (values != null && values.size() > 0) {
								showTowDialog(line, values, holder.etLine, tview,position);
							}

						}else if(line.getTview().contains("村")){
							String xianD = "",xiangD = "";
							for(Line ln : lines){
								if(ln.getTview().contains("县")){
									Object obj = attribute.get(ln.getKey());
									xianD = obj == null ? "" : obj.toString() ;
								}else if(ln.getTview().contains("乡")){
									Object obj = attribute.get(ln.getKey());
									xiangD = obj == null ? "" : obj.toString() ;
								}
							}
							Map<String, String> values = new HashMap<String, String>();
							if(xiangD.contains(xianD)){
								values = Util.getXXCDomain(mContext, key, xiangD, "cun");
							}else{
								values = Util.getXXCDomain(mContext, key, xianD+xiangD, "cun");
							}

							if (values != null && values.size() > 0) {
								showTowDialog(line, values, holder.etLine, tview,position);
							}
						}else{
							List<Row> lst = isDMField(key);
							if (lst == null) {
								getZlchl(line);
								showKeyDialog(line, holder.etLine);
							} else {
								int size = lst.size();
								if (size > 0) {
									showTowDialog(line, lst, holder.etLine, tview,position);
								} else {
									getZlchl(line);
									showKeyDialog(line, holder.etLine);
								}
							}
						}
					}
				}
			}
		});

		holder.etLine.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					final boolean focus = line.isFocus();
					check(position);
					boolean flag = holder.etLine.isFocused();
					if (!focus && !flag) {
						holder.etLine.requestFocus();
						holder.etLine.onWindowFocusChanged(true);
					}
				}
				return false;
			}
		});

		final TextWatcher watcher = new SimpeTextWather() {

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					line.setText(null);
				} else {
					line.setText(String.valueOf(s));
				}
			}
		};
		holder.etLine.addTextChangedListener(watcher);
		holder.etLine.setTag(watcher);

		return convertView;
	}

	private void check(int position) {
		for (Line l : lines) {
			l.setFocus(false);
		}
		lines.get(position).setFocus(true);
	}

	static class ViewHolder {
		TextView tvLine;
		TextView etLine;
	}

	/** 检测字段是那种类型的字段 */
	private List<Row> isDMField(String fieldname) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, fieldname);
		return list;
	}

	/** 检测字段是否为不能修改的字段 */
	public boolean isNoEditField(String key) {
//		SharedPreferences preferences = mContext.getSharedPreferences(pname + "notedite", Context.MODE_PRIVATE);
		Set<String> notes = new HashSet<>();//preferences.getStringSet(pname,new HashSet<String>());
//		if (notes.size() == 0) {
//
//		}
		List<Row> rows = BussUtil.getConfigXml(mContext, pname, "notedite");
		if (rows == null) {
			return false;
		} else {
			for (Row row : rows) {
				notes.add(row.getName());
			}
			//Editor editor = preferences.edit();
			//editor.clear();
			//editor.putStringSet(pname, notes).apply();
		}

		for (String row : notes) {
			if (row.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/** 隐藏软件盘 */
	private void hide(TextView text) {
		InputMethodManager imm = (InputMethodManager) text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
	}

	/** 时间选择popupwindow */
	public void initSelectTimePopuwindow(final Line line,final TextView editText) {
		TimePopupWindow timePopupWindow = new TimePopupWindow(mContext, TimePopupWindow.Type.ALL);
		timePopupWindow.setCyclic(true);
		// 时间选择后回调
		timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				String bfText = line.getText();
				String seltime = getTime(date);
				editText.setText(seltime);
				line.setText(seltime);
				updataData(line, bfText, seltime,seltime);
				CursorUtil.setTextViewLocation(editText);
			}
		});
		timePopupWindow.showAtLocation(editText, Gravity.BOTTOM, 0, 0,new Date(), false);
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
		String lon = format.format(BaseActivity.currentLon) + "";
		txtlon.setText(lon);
		TextView txtlat = (TextView) dialog.findViewById(R.id.edittxt_input_lat);
		String lat = format.format(BaseActivity.currentLat) + "";
		txtlat.setText(lat);
		final TextView txtx = (TextView) dialog.findViewById(R.id.edittxt_input_x);
		String xx = format.format(BaseActivity.currentPoint.getX()) + "";
		txtx.setText(xx);
		final TextView txty = (TextView) dialog.findViewById(R.id.edittxt_input_y);
		String yy = format.format(BaseActivity.currentPoint.getY()) + "";
		txty.setText(yy);

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
				updataData(line, bfText, value,value);
				CursorUtil.setTextViewLocation(editText);
			}
		});

		BussUtil.setDialogGravity_Center(mContext, dialog, 0.55, 0.55);
	}

	/** 代码dialog弹出 */
	int position = -1;

	private void showTowDialog(final Line line, final List<Row> row,final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog.findViewById(R.id.textView_two_alias);

		textView.setText(txt);

		final EditText seltext = (EditText) dialog.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();

		final ListView listView = (ListView) dialog.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
		if (row.size() > 0) {
			for (Row r : row) {
				String id = r.getId();
				String value = r.getName();
				lst.add(id + "-" + value);
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				position = psition;
				String txt = lst.get(psition);
				String str = txt.split("-")[1];
				seltext.setText(str);
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
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.myspinner, list);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int psition, long arg3) {
							String txt = list.get(psition);
							String str = txt.split("-")[1];
							seltext.setText(str);
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
				} else {
					value = text;
					txt = text;
				}
				position = -1;
				String bfText = line.getText();
				if(bfText.equals(txt)){
					dialog.dismiss();
					return;
				}
				editText.setText(text);
				CursorUtil.setTextViewLocation(editText);
				line.setText(txt);
				updataData(line, bfText,txt, value);
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

	/** 代码dialog弹出 */
	private void showTowDialog(final Line line, final Map<String, String> map,final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog.findViewById(R.id.edittxt_input_two);
		seltext.clearFocus();
		seltext.setText(line.getText());

		final ListView listView = (ListView) dialog.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
		for (String key : map.keySet()) {
			String value = map.get(key);
			lst.add(key + "-" + value);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int psition, long arg3) {
				position = psition;
				String txt = lst.get(psition);
				String str = txt.split("-")[1];
				seltext.setText(str);
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
				String af = et.toString().trim();
				if (!af.equals(bf)) {
					final List<String> list = new ArrayList<>();
					for (String str : lst) {
						if (str.contains(af)) {
							list.add(str);
						}
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.myspinner, list);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
												int psition, long arg3) {
							String txt = list.get(psition);
							String str = txt.split("-")[1];
							seltext.setText(str);
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
					value = lst.get(position).split("-")[0];//代码
					txt = lst.get(position).split("-")[1];//代码值
				} else {
					value = text;
					txt = text;
				}
				position = -1;
				String bfText = line.getText();
				if(bfText != null && bfText.equals(txt)){
					dialog.dismiss();
					return;
				}
				editText.setText(text);
				CursorUtil.setTextViewLocation(editText);
				line.setText(txt);
				updataData(line, bfText,txt, value);
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
				String txt = edit.getText() == null ? "" : edit.getText().toString().trim();
				//line.setText(txt);
				mContext.updateZp(txt, line, bfText);
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
				mContext.lookpictures(mContext,line);
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/** 弹出输入框dialog */
	private void showKeyDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_input);
		dialog.setCanceledOnTouchOutside(false);
		final SharedPreferences preferences = mContext.getSharedPreferences(line.getKey(), Context.MODE_PRIVATE);
		final Set<String> items = preferences.getStringSet(line.getKey(),new HashSet<String>());
		final ArrayList<String> list = new ArrayList<String>();
		if (items.size() > 0) {
			for (String str : items) {
				list.add(str);
			}
		}
		TextView aliasTxt = (TextView) dialog.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		SwipeMenuListView listView = (SwipeMenuListView) dialog.findViewById(R.id.listView_two_selectvalue);

		final EditText edit = (EditText) dialog.findViewById(R.id.edittxt_input);
		listView.setVisibility(View.VISIBLE);
		if(line.getTview().contains("成活率") || line.getTview().contains("保存率")){
			if(zlchl != 0){
				edit.setText(zlchl+"");
			}
//			else{
//				ToastUtil.setToast(mContext, "只能在样地中修改对应值");
//			}
			//edit.setFocusable(false);
			//listView.setVisibility(View.GONE);
		}else if (line.getTview().contains("面积")) {
			double area = Math.abs(selGeoFeature.getGeometry().calculateArea2D());
			String strt = line.getText().trim();
			double yarea = 0;
			if(strt != null && !strt.equals("")){
				boolean f = Util.CheckStrIsDouble(strt.trim());
				if(f){
					yarea = Double.parseDouble(strt.trim());
				}
			}

			if(line.getTview().contains("公顷")){

				if(yarea == 0){
					edit.setText(df.format(area*0.0001));
				}else if(area*0.0015 > yarea*15){
					edit.setText(df.format(yarea));
				}else{
					double d = (yarea*15 - area*0.0015)/yarea*15 * 100;
					ToastUtil.setToast(mContext, "误差率"+df.format(d)+"%");
					if(d > 5){
						edit.setText(df.format(area*0.0001));
					}else{
						edit.setText(df.format(yarea));
					}
				}
			}else{
				if(yarea == 0){
					edit.setText(df.format(area*0.0015));
				}else if(area*0.0015 > yarea){
					edit.setText(df.format(yarea));
				}else{
					double d = (yarea - area*0.0015)/yarea * 100;
					ToastUtil.setToast(mContext, "误差率"+df.format(d)+"%");
					if(d > 5){
						edit.setText(df.format(area*0.0015));
					}else{
						edit.setText(df.format(yarea));
					}
				}
			}
		}else if(line.getTview().contains("东经") || line.getTview().contains("北纬")){
			View view = dialog.findViewById(R.id.textview_gszh);
			view.setVisibility(View.VISIBLE);
			edit.setText(editText.getText().toString());
			TextView zhwxsh = (TextView) dialog.findViewById(R.id.zhuanhxshu);
			zhwxsh.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String txts = edit.getText().toString().trim();//106°39'50.151"
					if((txts != null || txts.equals("")) && txts.contains("°")){
						String[] jingdu = txts.split("°");
						String[] weidu;
						if(jingdu[1].trim().contains("'")){
							weidu = jingdu[1].trim().split("\'");
						}else{
							ToastUtil.setToast(mContext, "数据格式不正确");
							double str = Double.parseDouble(jingdu[0]);
							DecimalFormat df = new DecimalFormat("0.000000");
							edit.setText(df.format(str)+"");
							CursorUtil.setEditTextLocation(edit);
							return;
						}
						double miao = 0;
						if(weidu[1].trim().contains("\"")){
							miao = Double.parseDouble(weidu[1].trim().split("\"")[0]);
						}else{
							ToastUtil.setToast(mContext, "数据格式不正确");
							double str = Double.parseDouble(jingdu[0])+Double.parseDouble(weidu[0])/60;
							DecimalFormat df = new DecimalFormat("0.000000");
							edit.setText(df.format(str)+"");
							CursorUtil.setEditTextLocation(edit);
							return;
						}

						double str = Double.parseDouble(jingdu[0])+Double.parseDouble(weidu[0])/60+miao/3600;
						DecimalFormat df = new DecimalFormat("0.000000");
						edit.setText(df.format(str)+"");
						CursorUtil.setEditTextLocation(edit);
					}
				}
			});

			TextView dfmview = (TextView) dialog.findViewById(R.id.fhwdfmgs);
			dfmview.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String txt = editText.getText().toString().trim();
					if(txt.contains("°")){
						edit.setText(txt);
						CursorUtil.setEditTextLocation(edit);
					}else{
						String txts = edit.getText().toString().trim();
						boolean flag = Util.CheckStrIsDouble(txts);
						if(flag){
							int du = (int) Math.floor(Double.parseDouble(txts));
							double fff = (Double.parseDouble(txts)-du)*60;
							int fen =(int) Math.floor(fff);
							DecimalFormat df = new DecimalFormat("0.000");
							String miao = df.format((fff - fen)*60);
							String str = du+"°"+fen+"'"+miao+"\"";
							edit.setText(str);
							CursorUtil.setEditTextLocation(edit);
						}else{
							ToastUtil.setToast(mContext, "数据格式不正确，转换不了");
							return;
						}
					}
				}
			});
		} else {
			edit.setText(editText.getText().toString());
		}
		CursorUtil.setTextViewLocation(edit);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.myspinner, list);
		listView.setAdapter(adapter);

		TextView sure = (TextView) dialog.findViewById(R.id.textView_two_sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bfText = line.getText();
				hide(edit);
				final String strTxt = edit.getText() == null ? null : edit.getText().toString();

				if (line.getTview().contains("面积")) {
					boolean result = Util.CheckStrIsDouble(strTxt);
					if(!result){
						ToastUtil.setToast(mContext, "输入数据不符合数据格式");
						return;
					}
				}

				editText.setText(strTxt);
				dialog.dismiss();
				line.setText(strTxt);
				updataData(line, bfText, strTxt,strTxt);
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

	public void addMenuItem(SwipeMenu menu){
		SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
		// set item background
		deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
		// set item width
		deleteItem.setWidth(dp2px(90));
		// set a icon
		deleteItem.setIcon(R.drawable.ic_delete);
		// add to menu
		menu.addMenuItem(deleteItem);
	}

	/** 保存数据 */
	public void updataData(Line line, String bfText,String txt, String value) {
		Map<String, Object> attributes = featureLayer.getFeature(selGeoFeature.getId()).getAttributes();
		if(bfText.equals(value)){
			return;
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
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			try {
				Date date = df.parse(value);
				attributes.put(key, date);
			} catch (ParseException e) {
				ToastUtil.setToast(mContext, "输入数据错误");
				e.printStackTrace();
				return;
			}
		} else if (line.getFieldType() == Field.esriFieldTypeDouble) {
			try {
				double dd = Double.parseDouble(value);
				attributes.put(key, dd);
			} catch (NumberFormatException e) {
				ToastUtil.setToast(mContext, "输入数据错误");
				e.printStackTrace();
				return;
			}
		} else if(myFeture.getCname().contains("古树名木")){
			attributes.put(key, txt);
		}else{
			attributes.put(key, value);
		}

		//Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),selGeoFeature.getSymbol(),attributes);
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
		try {
			long featureid = selGeoFeature.getId();
			GeodatabaseFeature feature = new GeodatabaseFeature(attributes,featureTable);
			//Feature feature = new GeodatabaseFeature(attributes,selGeoFeature.getGeometry(),myFeture.getMyLayer().getTable());
			featureTable.updateFeature(featureid, feature);
		} catch (TableException e) {
			attribute.put(line.getKey(), bfText);
			line.setText(bfText);
			line.setValue(bfText);
			ToastUtil.setToast((Activity) mContext, "更新失败");
			e.printStackTrace();
		}

		line.setValue(value);
		line.setText(txt);
		ToastUtil.setToast((Activity) mContext, "更新成功");
		lines.set(line.getNum(), line);
		notifyDataSetChanged();
		attribute = attributes;
		/** 更新小班号 */
//		for (Row row : mContext.gcmc) {
//			if (row.getName().equals(line.getKey())) {
//				mContext.currentxbh = "";
//				mContext.getXbhData(pname);
//			}
//		}

	}

	/** 更新历史录入数据 */
	public void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line, String strTxt) {
		if (!items.contains(strTxt.trim()) && !strTxt.equals("")) {
			items.add(strTxt.trim());
			Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(line.getKey(), items).apply();
		}
	}

	/** 更新历史录入数据 */
	public void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).apply();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

	public void getGeometryInfo(Geometry geometry) {
		yddPointList.clear();
		for(final MyLayer layer : BaseActivity.layerNameList){
			if(layer.getLayer().getGeometryType().equals(Geometry.Type.POINT)){
				getGeometryInfo(geometry, layer);
			}
		}
	}

	/**查询小班数据*/
	public void getGeometryInfo(final Geometry geometry,final MyLayer layer) {

		QueryParameters queryParams = new QueryParameters();
		queryParams.setOutFields(new String[] { "*" });
		queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
		queryParams.setGeometry(geometry);
		queryParams.setReturnGeometry(true);
		queryParams.setOutSpatialReference(BaseActivity.spatialReference);
		layer.getLayer().selectFeatures(queryParams, SelectionMode.NEW,
				new CallbackListener<FeatureResult>() {

					@Override
					public void onError(Throwable arg0) {
						ToastUtil.setToast(mContext, "查询出错");
					}

					@Override
					public void onCallback(FeatureResult featureResult) {
						long size = featureResult.featureCount();
						if (size > 0) {
							Iterator<Object> iterator = featureResult.iterator();
							GeodatabaseFeature geodatabaseFeature = null;
							while (iterator.hasNext()) {
								geodatabaseFeature = (GeodatabaseFeature) iterator.next();
								yddPointList.add(geodatabaseFeature);
							}
						}
					}
				});
	}

	/**获取造林成活率*/
	public void getZlchl(Line line) {

		zlchl = 0;
		if (line.getTview().contains("成活率") || line.getTview().contains("保存率")) {
			getGeometryInfo(myFeture.getFeature().getGeometry());
			SystemClock.sleep(1000);
			int m = 0;
			int size = yddPointList.size();
			System.out.println(size+"=== 个数");
			for (GeodatabaseFeature feature : yddPointList) {
				List<Field> fls = feature.getTable().getFields();
				for (Field fl : fls) {
					if (fl.getAlias().contains("成活率") || fl.getAlias().contains("保存率")) {
						Object obj = feature.getAttributeValue(fl);
						if (obj != null) {
							boolean flag = Util.CheckStrIsDouble(obj.toString().trim());
							if (!flag)
								break;
							zlchl = zlchl+Double.parseDouble(obj.toString().trim());
							m++;
						}
					}
				}
			}
			if(m > 0){
				zlchl = zlchl/m;
			}
			DecimalFormat format = new DecimalFormat("0.00");
			zlchl = Double.parseDouble(format.format(zlchl));
		}
	}

}
