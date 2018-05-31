package com.otitan.xnbhq.dialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ErDiaoActivity;
import com.otitan.xnbhq.adapter.EdYmdcadapter;
import com.otitan.xnbhq.adapter.JgdcLineAdapter;
import com.otitan.xnbhq.adapter.ListAdapter;
import com.otitan.xnbhq.adapter.LxqcYdyzAdapter;
import com.otitan.xnbhq.dao.Jgdc;
import com.otitan.xnbhq.dao.Ym;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.listviewinedittxt.EdLineAdapter;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.otitan.xnbhq.util.UtilTime;
import com.titan.baselibrary.swipemenulistview.SwipeMenu;
import com.titan.baselibrary.swipemenulistview.SwipeMenuCreator;
import com.titan.baselibrary.swipemenulistview.SwipeMenuItem;
import com.titan.baselibrary.swipemenulistview.SwipeMenuListView;

/**
 * Created by li on 2017/6/2.
 * 汉字输入dialog
 */
public class HzbjDialog extends Dialog implements android.view.View.OnClickListener {
	Context context;
	String name;
	private TextView tv;
	private SwipeMenuListView lv;
	HashMap<String, String> map;
	String zd;
	private String chushizhi;
	/** 用于存储输入历史记录 */
	private SharedPreferences input_histroy;
	List<String> list;
	/** 样木 */
	private Ym ym;
	List<Ym> ymlist;
	private Set<String> items;
	EditText content;
	private int position;//
	private List<Line> lines;
	private Line line;
	private EdLineAdapter edlineadapter = null;
	private EdYmdcadapter ymdcadapter;
	private JgdcLineAdapter jgAdapter;
	private Jgdc jgdc;
	private LxqcYdyzAdapter ydyzAdapter;

	public interface IComplete {

		void btn_complete(Context mcontext, int position, List<Line> lines,
						  EdLineAdapter edlineadaper);

	}

	public HzbjDialog(Context context, String name, TextView tv,int position, List<Line> lines,
					  EdLineAdapter edLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.position = position;
		this.lines = lines;
		this.edlineadapter = edLineAdapter;
	}

	/** 连续清查样地因子调查 */
	public HzbjDialog(Context context,TextView tv, Line line, LxqcYdyzAdapter ydyzAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = line.getTview();
		this.line = line;
		this.tv = tv;
		this.ydyzAdapter = ydyzAdapter;
	}

	/** 二调小班区划和样地调查 */
	public HzbjDialog(Context context, String name, TextView tv,Line line, EdLineAdapter edLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.line = line;
		this.edlineadapter = edLineAdapter;
	}

	public HzbjDialog(Context context, String name, TextView tv) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
	}

	public HzbjDialog(Context context, String name, TextView tv,HashMap<String, String> map, String zd) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.map = map;
		this.zd = zd;
	}

	/** 样木调查 */
	public HzbjDialog(Context mContext, String title, TextView tv,Ym ym, EdYmdcadapter edYmdcadapter) {
		super(mContext, R.style.Dialog);
		this.context = mContext;
		this.name = title;
		this.tv = tv;
		this.ym = ym;
		this.ymdcadapter = edYmdcadapter;
	}

	/** 角规调查 */
	public HzbjDialog(Context mContext, TextView tv, Line line, Jgdc jgdc,
					  JgdcLineAdapter jgdcLineAdapter) {
		super(mContext, R.style.Dialog);
		this.context = mContext;
		this.name = line.getTview();
		this.tv = tv;
		this.line = line;
		this.input_histroy = jgdcLineAdapter.input_history;
		this.jgdc = jgdc;
		this.jgAdapter = jgdcLineAdapter;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hzbj);
		setCanceledOnTouchOutside(false);
		// 初始值
		if (tv != null) {
			chushizhi = tv.getText().toString();
		}
		TextView headtext = (TextView) findViewById(R.id.headtext);
		Button back = (Button) findViewById(R.id.back);
		Button wancheng = (Button) findViewById(R.id.wancheng);
		content = (EditText) findViewById(R.id.content);
		final TextView qingchu = (TextView) findViewById(R.id.qingchu);
		final TextView riqi = (TextView) findViewById(R.id.riqi);
		final TextView shijian = (TextView) findViewById(R.id.shijian);
		final TextView haiba = (TextView) findViewById(R.id.haiba);
		haiba.setOnClickListener(this);
		final TextView jingdu = (TextView) findViewById(R.id.jingdu);
		jingdu.setOnClickListener(this);
		final TextView weidu = (TextView) findViewById(R.id.weidu);
		weidu.setOnClickListener(this);
		lv = (SwipeMenuListView) findViewById(R.id.listView1);

		input_histroy = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		items = input_histroy.getStringSet(name, new HashSet<String>());
		list = new ArrayList<String>();
		if (items.size() > 0) {

			String[] data = (String[]) items.toArray(new String[items
					.size()]);
			for (int i = 0; i < data.length; i++) {
				list.add(data[i].trim().toString());
			}

		}
		final ListAdapter adapter = new ListAdapter(context, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				content.setText(list.get(arg2));
			}
		});

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(context);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(Util.dp2px(context,90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		lv.setMenuCreator(creator);

		// step 2. listener item click event
		lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu,
										int index) {
				switch (index) {
					case 0:
						String str = list.get(position);
						list.remove(str);
						adapter.notifyDataSetChanged();
						items.remove(str);
						updateSet(input_histroy, items, line);
						break;
				}
			}
		});

		headtext.setText(name);
		content.setText(chushizhi);
		content.setSelection(chushizhi.length());
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		qingchu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content.setText("");
			}
		});
		riqi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content.setText(UtilTime.getSystemtime3());
			}
		});
		shijian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content.setText(UtilTime.getSystemtime2());
			}
		});
		wancheng.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!content.getText().toString().trim().equals("")){
					items.add(content.getText().toString());
					SharedPreferences.Editor editor = input_histroy.edit();
					editor.clear();
					editor.putStringSet(name, items).commit();
				}

				tv.setText(content.getText().toString());
				if (map != null) {
					map.put(zd, content.getText().toString());
				}
				// 隐藏小键盘
				InputMethodManager inputMethodManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(
						content.getWindowToken(), 0);
				if (edlineadapter != null) {
					btn_complete(context, line);
				} else if (ymdcadapter != null) {
					// 样木调查
					ed_ymdc_complete(context, ymdcadapter);
				} else if (jgAdapter != null) {
					ed_jgdc_complete(context, jgAdapter);
				}
				dismiss();
			}
		});
	}

	/** 二调角规调查数据保存 */
	private void ed_jgdc_complete(Context context2, JgdcLineAdapter jgAdapter) {
		Line line = lines.get(position);

		String value = content.getText().toString();
		jgdc.setBEIZHU(value);
		line.setText(value);

		boolean isupdate = DataBaseHelper.updateJgdc(context, jgdc,ErDiaoActivity.datapath);
		if (isupdate) {
			ToastUtil.setToast((Activity) context, "更新成功");
			return;
		} else {
			ToastUtil.setToast((Activity) context, "更新失败");
		}

		// lines.set(line.getNum(), line);
		jgAdapter.notifyDataSetChanged();
	}

	/** 二调样木调查数据保存 */
	private void ed_ymdc_complete(Context context, EdYmdcadapter edlineadapter) {
		String curvalue = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (chushizhi.equals(curvalue)) {
			return;

		}
		switch (tv.getId()) {
			// 胸径
			case R.id.xj:
				ym.setXIONGJING(Double.valueOf(curvalue));
				break;

			// 备注
			case R.id.bz:
				ym.setBEIZHU(curvalue);
				break;

		}
		boolean idupdate = DataBaseHelper.updateYm(context, ym,ErDiaoActivity.datapath);
		if (idupdate) {
			ToastUtil.setToast(context, "样木更新成功");
			ymdcadapter.notifyDataSetChanged();
		} else {
			ToastUtil.setToast(context, "样木更新失败");
		}
	}

	/** 二调保存数据 */
	private void btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String curvalue = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} else {
			BaseActivity.selectFeatureAts = edlineadapter.curfeture
					.getAttributes();
			BaseActivity.selectFeatureAts.put(name, curvalue);
			long featureid = edlineadapter.curfeture.getId();
			Graphic updateGraphic = new Graphic(
					edlineadapter.curfeture.getGeometry(),
					edlineadapter.curfeture.getSymbol(),
					BaseActivity.selectFeatureAts);
			try {
				edlineadapter.curfeaturelayer.getFeatureTable().updateFeature(
						featureid, updateGraphic);

				curline.setText(curvalue);
				// curline.setValue(curvalue);
			} catch (Exception e) {
				bfvalue = bfvalue == "null" ? "" : bfvalue;
				curline.setText(bfvalue);
				// curline.setValue(bfvalue);
				BaseActivity.selectFeatureAts.put(name, bfvalue);
				ToastUtil.setToast((Activity) mContext, "更新失败");
				return;

			}
			ToastUtil.setToast((Activity) mContext, "更新成功");
		}
		// lines.set(line.getNum(), line);
		edlineadapter.notifyDataSetChanged();

	}

	/** 保存数据 */
	public void btn_complete(Context mContext, int position, List<Line> lines,
							 EdLineAdapter edlineadaper) {
		GeodatabaseFeature editgeodatafeature = null;
		FeatureLayer editfeature = null;
		if ("" != null) {
			// editgeodatafeature = edlineadapter.selGeoFeature;
			editfeature = ErDiaoActivity.yddlayer;
		} else {
			editgeodatafeature = BaseActivity.selGeoFeature;
			editfeature = BaseActivity.myLayer.getLayer();
		}
		String name = lines.get(position).getKey();
		String value = content.getText().toString();
		Line line = lines.get(position);
		line.setText(value);
		editgeodatafeature.getAttributes().put(name, value);
		Map<String, Object> att = editgeodatafeature.getAttributes();
		for (Field f : editgeodatafeature.getTable().getFields()) {
			if (f.getName().equals(name)) {
				att.put(f.getName(), value);
			}
		}
		Graphic updateGraphic = new Graphic(editgeodatafeature.getGeometry(),
				editgeodatafeature.getSymbol(), att);
		FeatureTable featureTable = editfeature.getFeatureTable();
		try {
			long featureid = editgeodatafeature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e) {
			ToastUtil.setToast((Activity) mContext, "更新失败");
			e.printStackTrace();
			return;
		}
		ToastUtil.setToast((Activity) mContext, "更新成功");

		// lines.set(line.getNum(), line);
		edlineadaper.notifyDataSetChanged();

	}

	/** 森林资源连续清查保存数据 */
	public void lxqc_btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String curvalue = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} else {
			BaseActivity.selectFeatureAts.put(name, curvalue);
			long featureid = BaseActivity.selGeoFeature.getId();
			Graphic updateGraphic = new Graphic(
					BaseActivity.selGeoFeature.getGeometry(),
					BaseActivity.selGeoFeature.getSymbol(),
					BaseActivity.selectFeatureAts);
			try {
				BaseActivity.myLayer.getTable().updateFeature(featureid, updateGraphic);
				curline.setText(curvalue);
			} catch (Exception e) {
				bfvalue = bfvalue == "null" ? "" : bfvalue;
				curline.setText(bfvalue);
				BaseActivity.selectFeatureAts.put(name, bfvalue);
				ToastUtil.setToast((Activity) mContext, "更新失败");
				return;

			}
			ToastUtil.setToast((Activity) mContext, "更新成功");
		}

	}

	@Override
	public void onClick(View v) {
		DecimalFormat df = new DecimalFormat("#0.00000");
		switch (v.getId()) {
			// 海拔
			case R.id.haiba:
				String altitude = df.format(BaseActivity.currentPoint.getZ());
				content.setText(altitude);
				break;
			// 经度
			case R.id.jingdu:

				String longitude = df.format(BaseActivity.currentPoint.getX());
				content.setText(longitude);
				break;
			// 纬度
			case R.id.weidu:
				String latitude = df.format(BaseActivity.currentPoint.getY());
				content.setText(latitude);
				break;
			default:
				break;
		}
	}

	/** 更新历史录入数据 */
	private void updateSet(SharedPreferences preferences, Set<String> items,Line line) {
		Editor editor = preferences.edit();
		editor.clear();
		editor.putStringSet(line.getKey(), items).apply();
	}

}
