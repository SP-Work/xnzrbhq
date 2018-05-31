package com.otitan.xnbhq.listviewinedittxt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
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
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.edite.activity.PointEditActivity;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.entity.YzlTree;
import com.otitan.xnbhq.supertreeview.NodeResource;
import com.otitan.xnbhq.util.BussUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 小班样地点数据Adapter
 */
public class PontAdapter extends BaseAdapter {

	private List<Line> lines;
	private PointEditActivity mContext;
	private DecimalFormat format = new DecimalFormat("0.000000");
	private DecimalFormat df = new DecimalFormat("0.000");
	/* 标识数据是否修改了 */
	public boolean saveflag = false;
	public HashMap<String, String> hashMap = new HashMap<String, String>();

	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private String pname;

	/*   */
	private FeatureLayer featureLayer;
	private GeodatabaseFeature selGeoFeature;
	private Map<String, Object> attribute = null;

	MyFeture myFeture = null;
	private Set<String> mustColumn = new HashSet<String>();

	public PontAdapter(PointEditActivity context, List<Line> lines,
					   MyFeture feture) {
		this.mContext = context;
		this.lines = lines;
		this.myFeture = feture;
		this.pname = myFeture.getPname();
		this.featureLayer = myFeture.getMyLayer().getLayer();
		this.selGeoFeature = myFeture.getFeature();
		this.attribute = selGeoFeature.getAttributes();
		SharedPreferences preferences = context.getSharedPreferences(pname
				+ "mustfield", Context.MODE_PRIVATE);
		mustColumn = preferences.getStringSet(pname, new HashSet<String>());
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
		boolean flag = isNoEditField(key);
		holder.etLine.setText(line.getText());
		holder.etLine.setEnabled(!flag);

		if (flag) {
			holder.tvLine.setBackgroundColor(Color.GRAY);
			holder.etLine.setBackgroundColor(Color.GRAY);
		} else {
			holder.tvLine.setBackgroundColor(Color.WHITE);
			holder.etLine.setBackgroundColor(Color.WHITE);
		}
		boolean ff = line.isNullable();
		if (!ff && !line.getKey().contains("OBJECTID")) {
			holder.tvLine.setBackgroundColor(Color.RED);
		}
		// if(mustColumn.contains(line.getKey())){
		// holder.tvLine.setBackgroundColor(Color.RED);
		// //holder.etLine.setBackgroundColor(Color.RED);
		// }

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
				if (tview.contains("照片")) {
					showTakePhoto(line);
				} else if (tview.contains("时间") || tview.equals("dcsj")
						|| tview.contains("日期")) {
					initSelectTimePopuwindow(line, holder.etLine);
				} else if (tview.contains("坐标") || tview.contains("经度")
						|| tview.contains("纬度")) {
					showLonLatDialog(line, holder.etLine);
				} else {
					if (domain != null) {
						Map<String, String> values = domain.getCodedValues();

						if(line.getTview().contains("乡")){
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
								}
							}
							values = value;
						}else if(line.getTview().contains("村")){
							String str = "";
							for(Line ln : lines){
								if(ln.getTview().contains("乡")){
									Object obj = attribute.get(ln.getKey());
									str = obj == null ? "" : obj.toString() ;
									break;
								}
							}
							Map<String, String> value = new HashMap<String, String>();
							for(String vv : values.keySet()){
								if(vv.contains(str)){
									value.put(vv,values.get(vv));
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
		;
		return list;
	}

	/** 检测字段是否为不能修改的字段 */
	public boolean isNoEditField(String fieldname) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, "notedite");
		if (list == null) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getName();
			if (name.equals(fieldname)) {
				return true;
			}
		}
		return false;
	}

	/** 生成多级菜单 */
	public List<NodeResource> initNodeTree(List<YzlTree> dilei) {

		List<NodeResource> list = new ArrayList<NodeResource>();
		/* 循环添加节点id,代码及名称 */
		/* 第一级节点添加 */
		List<YzlTree> tree = new ArrayList<YzlTree>();
		HashMap<String, Integer> index = new HashMap<String, Integer>();
		int startIndex = 0;
		for (int i = 0; i < dilei.size(); i++) {
			YzlTree dl = dilei.get(i);
			if (dl.getDLLEVEL() != 1)
				continue;
			NodeResource n1 = new NodeResource("" + -1, "" + startIndex,
					dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
			list.add(n1);
			tree.add(dl);
			index.put(dl.getDLDM(), startIndex);
			startIndex++;
		}
		/* 添加第二级 */
		List<YzlTree> tree1 = new ArrayList<YzlTree>();
		HashMap<String, Integer> index1 = new HashMap<String, Integer>();
		for (int ii = 0; ii < tree.size(); ii++) {
			for (int i = 0; i < dilei.size(); i++) {
				YzlTree dl = dilei.get(i);
				if (dl.getDLLEVEL() != 2)
					continue;
				if (dl.getDLPARENTDM().equals(tree.get(ii).getDLDM())) {
					NodeResource n2 = new NodeResource(""
							+ index.get(dl.getDLPARENTDM()), "" + startIndex,
							dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
					list.add(n2);
					tree1.add(dl);
					index1.put(dl.getDLDM(), startIndex);
					startIndex++;
				}
			}
		}
		/* 添加第三级 */
		List<YzlTree> tree2 = new ArrayList<YzlTree>();
		HashMap<String, Integer> index2 = new HashMap<String, Integer>();
		for (int ii = 0; ii < tree1.size(); ii++) {
			for (int i = 0; i < dilei.size(); i++) {
				YzlTree dl = dilei.get(i);
				if (dl.getDLLEVEL() != 3)
					continue;
				if (dl.getDLLEVEL() == 3
						&& dl.getDLPARENTDM().equals(tree1.get(ii).getDLDM())) {
					NodeResource n3 = new NodeResource(""
							+ index1.get(dl.getDLPARENTDM()), "" + startIndex,
							dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
					list.add(n3);
					tree2.add(dl);
					index2.put(dl.getDLDM(), startIndex);
					startIndex++;
				}
			}
		}
		/* 添加第三级 */
		List<YzlTree> tree3 = new ArrayList<YzlTree>();
		HashMap<String, Integer> index3 = new HashMap<String, Integer>();
		for (int ii = 0; ii < tree2.size(); ii++) {
			for (int i = 0; i < dilei.size(); i++) {
				YzlTree dl = dilei.get(i);
				if (dl.getDLLEVEL() != 4)
					continue;
				if (dl.getDLLEVEL() == 4
						&& dl.getDLPARENTDM().equals(tree2.get(ii).getDLDM())) {
					NodeResource n4 = new NodeResource(""
							+ index2.get(dl.getDLPARENTDM()), "" + startIndex,
							dl.getDLDM() + "-" + dl.getNAME(), dl.getDLDM());
					list.add(n4);
					tree3.add(dl);
					index3.put(dl.getDLDM(), startIndex);
					startIndex++;
				}
			}
		}

		return list;
	}

	/** 隐藏软件盘 */
	private void hide(TextView text) {
		InputMethodManager imm = (InputMethodManager) text.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
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
				updataData(line, bfText, seltime, seltime);
				setTextViewCursorLocation(editText);
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

		TextView txtlon = (TextView) dialog
				.findViewById(R.id.edittxt_input_lon);
		txtlon.setText(format.format(BaseActivity.currentLon) + "");
		TextView txtlat = (TextView) dialog
				.findViewById(R.id.edittxt_input_lat);
		txtlat.setText(format.format(BaseActivity.currentLat) + "");
		final TextView txtx = (TextView) dialog
				.findViewById(R.id.edittxt_input_x);
		txtx.setText(format.format(BaseActivity.currentPoint.getX()) + "");
		final TextView txty = (TextView) dialog
				.findViewById(R.id.edittxt_input_y);
		txty.setText(format.format(BaseActivity.currentPoint.getY()) + "");

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
				updataData(line, bfText, value, value);
				setTextViewCursorLocation(editText);
			}
		});

		BussUtil.setDialogGravity_Center(mContext, dialog, 0.55, 0.55);
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

		ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
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
				String txt = lst.get(psition);
				String str = txt.split("-")[1];
				seltext.setText(str);
				setTextViewCursorLocation(seltext);
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
				if (bfText != null && bfText.equals(txt)) {
					dialog.dismiss();
					return;
				}
				editText.setText(text);
				setTextViewCursorLocation(editText);
				line.setText(txt);
				updataData(line, bfText, txt, value);
				setTextViewCursorLocation(editText);
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
	private void showTowDialog(final Line line, final Map<String, String> map,
							   final TextView editText, String txt, final int index) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_two_selectvalue);
		TextView textView = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		textView.setText(txt);

		final EditText seltext = (EditText) dialog
				.findViewById(R.id.edittxt_input_two);
		seltext.setText(line.getText());
		seltext.clearFocus();

		final ListView listView = (ListView) dialog
				.findViewById(R.id.listView_two_selectvalue);
		final List<String> lst = new ArrayList<String>();
		for (String key : map.keySet()) {
			String value = map.get(key);
			lst.add(key + "-" + value);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.myspinner, lst);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int psition, long arg3) {
				position = psition;
				String txt = lst.get(psition);
				String str = txt.split("-")[1];
				seltext.setText(str);
				setTextViewCursorLocation(seltext);
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
							setTextViewCursorLocation(seltext);
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
				String text = seltext.getText().toString().trim();
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
				if (bfText.equals(txt)) {
					dialog.dismiss();
					return;
				}
				editText.setText(text);
				setTextViewCursorLocation(editText);
				line.setText(txt);
				updataData(line, bfText, txt, value);
				setTextViewCursorLocation(editText);
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

	/** 弹出输入框dialog */
	private void showKeyDialog(final Line line, final TextView editText) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_input);
		dialog.setCanceledOnTouchOutside(false);
		final SharedPreferences preferences = mContext.getSharedPreferences(
				line.getKey(), Context.MODE_PRIVATE);
		final Set<String> items = preferences.getStringSet(line.getKey(),
				new HashSet<String>());
		final ArrayList<String> list = new ArrayList<String>();
		if (items.size() > 0) {
			for (String str : items) {
				list.add(str);
			}
		}
		TextView aliasTxt = (TextView) dialog.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog.findViewById(R.id.edittxt_input);
		edit.setText(line.getText());
		setEditTextCursorLocation(edit);

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
				final String strTxt = edit.getText() == null ? null : edit
						.getText().toString();

				if (line.getKey().equals("chzs")
						|| line.getTview().contains("成活株数")) {// //成活株数
					double bzdzzs = 0;
					for (Line ll : lines) {
						if (ll.getKey().equals("bzdzzs")
								|| ll.getTview().contains("总株数")) {
							String str = ll.getText();
							if (str == null || str.equals("")) {
								ToastUtil.setToast(mContext, "请先输入标准地总株数");
								dialog.dismiss();
								return;
							}
							boolean result = Util.CheckStrIsDouble(str);
							if (result) {
								bzdzzs = Double.parseDouble(str);
							}
							break;
						}
					}
					double chzs = 0;
					boolean rs = Util.CheckStrIsDouble(strTxt);
					if (rs) {
						chzs = Double.parseDouble(strTxt);
					}
					if (chzs > bzdzzs) {
						ToastUtil.setToast(mContext, "成活株数不能大于总株数");
						return;
					}
				}

				if (line.getKey().equals("chl")
						|| line.getTview().contains("成活率")) {// 成活率
					if (strTxt == null || strTxt.equals("")) {
						return;
					}
					boolean result = Util.CheckStrIsDouble(strTxt);
					if (result) {
						double chl = Double.parseDouble(strTxt);
						if (chl > 100) {
							ToastUtil.setToast(mContext, "成活率不能大于100%");
							return;
						}
					}
				}
				editText.setText(strTxt);
				dialog.dismiss();
				line.setText(strTxt);
				updataData(line, bfText, strTxt, strTxt);
				setTextViewCursorLocation(editText);
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
				setTextViewCursorLocation(edit);
			}
		});

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
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

	/** 保存数据 */
	@SuppressLint("SimpleDateFormat")
	public void updataData(Line line, String bfText, String txt, String value) {
		Map<String, Object> attributes = featureLayer.getFeature(selGeoFeature.getId()).getAttributes();
		if (bfText.equals(value)) {
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
		if (myFeture.getCname().contains("古树名木")) {
			attributes.put(key, txt);
		} else {
			attributes.put(key, value);
		}

		if (line.getFieldType() == Field.esriFieldTypeDate) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(value);
				attributes.put(key, date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		attributes = setGlValue(line,attributes);

		Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),selGeoFeature.getSymbol(), attributes);
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
		try {
			long featureid = selGeoFeature.getId();
			GeodatabaseFeature feature = new GeodatabaseFeature(attributes,featureTable);
			featureTable.updateFeature(featureid, feature);
		} catch (TableException e) {
			attribute.put(line.getKey(), bfText);
			line.setText(bfText);
			line.setValue(bfText);
			ToastUtil.setToast((Activity) mContext, "更新失败");
			e.printStackTrace();
			return;
		}
		line.setValue(value);
		line.setText(txt);
		ToastUtil.setToast((Activity) mContext, "更新成功");
		lines.set(line.getNum(), line);
		notifyDataSetChanged();
		attribute = attributes;
	}

	public Map<String, Object> setGlValue(Line line,Map<String, Object> attributes) {
		if (line.getKey().equals("chzs") || line.getTview().contains("成活株数")) {
			double chzs = 0;
			boolean rs = Util.CheckStrIsDouble(line.getText().trim());
			if (rs) {
				chzs = Double.parseDouble(line.getText().trim());
			} else {
				ToastUtil.setToast(mContext, "数据输入格式不对");
				return new HashMap<String, Object>();
			}
			double bzdzzs = 0;
			for (Line ll : lines) {
				if (ll.getKey().equals("bzdzzs")
						|| ll.getTview().contains("总株数")) {
					String str = ll.getText().trim();
					if (str == null || str.equals("")) {
						return new HashMap<String, Object>();
					}
					boolean result = Util.CheckStrIsDouble(str);
					if (result) {
						bzdzzs = Double.parseDouble(str);
					}
					break;
				}
			}
			DecimalFormat format = new DecimalFormat("0.00");
			double chl_d = (chzs / bzdzzs) * 100;
			String chl = format.format(chl_d);
			for (Line ll : lines) {
				if (ll.getTview().contains("成活率") || ll.getKey().equals("chl")) {
					ll.setText(chl);
					lines.set(ll.getNum(), ll);
					attributes.put(ll.getKey(), chl + "");
					break;
				}
			}
		} else if (line.getKey().equals("bzdlx")
				|| line.getTview().contains("标准地类型")) {
			String txt = line.getText();
			if (txt.equals("样方")) {
				for (Line ll : lines) {
					if (ll.getKey().equals("bzdck")
							|| ll.getTview().contains("标准地长宽")) {
						String bftxt = ll.getText();
						ll.setText("10*10");
						attributes.put(ll.getKey(), "10*10");
					}
				}
			} else if (txt.equals("样圆")) {
				for (Line ll : lines) {
					if (ll.getKey().equals("bzdck")
							|| ll.getTview().contains("标准地长宽")) {
						String bftxt = ll.getText();
						ll.setText("7.98");
						// updataData(ll, bftxt, "7.98", "7.98");
						attributes.put(ll.getKey(), "7.98");
						lines.set(ll.getNum(), line);
					}
				}
			}
		}
		return attributes;
	}

	/** 把TextView的光标放置在最后 */
	public void setTextViewCursorLocation(TextView et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** 把edittext的光标放置在最后 */
	public void setEditTextCursorLocation(EditText et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** 拍照 */
	public void showTakePhoto(final Line line) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_edittxt_takephoto);
		dialog.setCanceledOnTouchOutside(false);

		TextView aliasTxt = (TextView) dialog
				.findViewById(R.id.textView_two_alias);
		aliasTxt.setText(line.getTview());

		final EditText edit = (EditText) dialog
				.findViewById(R.id.edittxt_input);
		edit.setText(line.getText());
		setEditTextCursorLocation(edit);

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
				mContext.lookpictures(mContext,line);
			}
		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/** 更新历史录入数据 */
	public void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line, String strTxt) {
		if (!items.contains(strTxt.trim()) && !strTxt.equals("")) {
			items.add(strTxt.trim());
			Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(line.getKey(), items).commit();
		}
	}

	/** 更新历史录入数据 */
	public void updateSet(SharedPreferences preferences, Set<String> items,
						  Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).commit();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

}
