package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.ShouCangAdapter;
import com.otitan.xnbhq.db.DbHelperService;
import com.otitan.xnbhq.entity.ShouCang;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.otitan.xnbhq.util.UtilTime;
import com.titan.baselibrary.swipemenulistview.SwipeMenu;
import com.titan.baselibrary.swipemenulistview.SwipeMenuCreator;
import com.titan.baselibrary.swipemenulistview.SwipeMenuItem;
import com.titan.baselibrary.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏窗口
 */
public class ShouCangDialog extends Dialog implements View.OnClickListener {

	Point point;
	private EditText miaoshuev;
	Context context;
	private SqlType sqlType;
	HashMap<String, String> map;
	ShouCangAdapter adapter;
	private GraphicsLayer graphicsLayer;
	private PictureMarkerSymbol markerSymbol;
	private MapView mapView;
	DbHelperService<ShouCang> service;
	private ShouCang cang = null;
	private List<ShouCang> list = new ArrayList<>();

	public static enum SqlType {
		/** 添加 */
		ADD,
		/** 更新 */
		UPDATE,
		/** 查询 */
		SELECT
	}

	/** type为“1”为收藏，type为“2”为编辑收藏 */
	public ShouCangDialog(Context context, Point point, SqlType type) {
		super(context, R.style.Dialog);
		this.point = point;
		this.context = context;
		this.sqlType = type;
		service = new DbHelperService<ShouCang>(context, ShouCang.class);
	}

	public ShouCangDialog(Context context, GraphicsLayer layer,PictureMarkerSymbol symbol, MapView mapView,SqlType type) {
		super(context, R.style.Dialog);
		this.context = context;
		this.graphicsLayer = layer;
		this.markerSymbol = symbol;
		this.mapView = mapView;
		this.sqlType = type;
		service = new DbHelperService<ShouCang>(context, ShouCang.class);
	}

	public ShouCangDialog(Context context,ShouCang cang,SqlType type,
						  DbHelperService<ShouCang> service,ShouCangAdapter adapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.sqlType = type;
		this.cang = cang;
		this.service = service;
		this.adapter = adapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getPView();
		setContentView(view);

		initView(view);

	}

	/** 页面控件初始化 */
	public View getPView() {
		View pView = null;
		if (sqlType.equals(SqlType.ADD) || sqlType.equals(SqlType.UPDATE)) {
			pView = getLayoutInflater().inflate(R.layout.dialog_shoucang_view,null);
			return pView;
		}

		if (sqlType.equals(SqlType.SELECT)) {
			pView = getLayoutInflater().inflate(R.layout.dialog_shoucangshow_view, null);
			return pView;
		}
		return pView;
	}

	/** 初始化页面控件 */
	public void initView(View view) {
		if (sqlType.equals(SqlType.ADD)) {
			miaoshuev = (EditText) view.findViewById(R.id.miaoshuev);
			Button save = (Button) view.findViewById(R.id.save);
			save.setOnClickListener(this);
			Button cancle = (Button) view.findViewById(R.id.cancle);
			cancle.setOnClickListener(this);
		} else if (sqlType.equals(SqlType.UPDATE)) {
			miaoshuev = (EditText) view.findViewById(R.id.miaoshuev);
			TextView tv = (TextView) view.findViewById(R.id.head);
			tv.setText(context.getResources().getString(R.string.scdedit));
			if (cang != null) {
				miaoshuev.setText(cang.getMIAOSHU());
				CursorUtil.setEditTextLocation(miaoshuev);
			}
			Button save = (Button) view.findViewById(R.id.save);
			save.setOnClickListener(this);
			Button cancle = (Button) view.findViewById(R.id.cancle);
			cancle.setOnClickListener(this);
		} else if (sqlType.equals(SqlType.SELECT)) {
			ImageView back = (ImageView) view.findViewById(R.id.back);
			back.setOnClickListener(this);
			list.clear();
			list = service.getObjectsByWhere(null);
			SwipeMenuListView lv = (SwipeMenuListView) view.findViewById(R.id.listview_sc);
			TextView zwsj = (TextView) view.findViewById(R.id.zwsj);
			SwipeMenuListViewSetAdapte(lv, list, zwsj);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			// 保存
			case R.id.save:
				baoCun();
				break;
			// 取消
			case R.id.cancle:
				this.dismiss();
				break;
			case R.id.back:
				this.dismiss();
				break;
			default:
				break;
		}
	}

	// 保存
	private void baoCun() {
		Object obj = miaoshuev.getText();
		String miaoshu = obj == null ? "" : obj.toString();
		if (sqlType.equals(SqlType.ADD)) {
			ShouCang shouCang = new ShouCang();
			shouCang.setMIAOSHU(miaoshu);
			String time = UtilTime.getSystemtime2();
			shouCang.setTIME(time);
			if (point != null && point.isValid()) {
				shouCang.setLON(point.getX() + "");
				shouCang.setLAT(point.getY() + "");
			}
			//
			boolean flag = service.add(shouCang);
			if (flag) {
				ToastUtil.setToast(context,context.getResources().getString(R.string.bcsuccess));
			} else {
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.faild));
			}

		} else if (sqlType.equals(SqlType.UPDATE)) {
			HashMap<String, String> where = new HashMap<String, String>();
			where.put("id", cang.getId()+"");
			cang.setMIAOSHU(miaoshu);
			boolean flag = service.update(cang, where);
			if(flag){
				adapter.notifyDataSetChanged();
				ToastUtil.setToast(context,context.getResources().getString(R.string.editsuccess));
			}else{
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.updatefaild));
			}
		}
		dismiss();
	}

	/*滑动删除*/
	public void SwipeMenuListViewSetAdapte(final SwipeMenuListView lv, final List<ShouCang> list, final TextView zwsj) {
		adapter = new ShouCangAdapter(context, list,lv, zwsj);
		lv.setAdapter(adapter);
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				addMenuItem(menu);
			}
		};
		// set creator
		lv.setMenuCreator(creator);

		// step 2. listener item click event
		lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						update(lv, list, position);
						break;
					case 1:
						delete(lv,list,position,zwsj);
						break;
				}
			}
		});

		if (list.size() == 0) {
			zwsj.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
		} else {
			zwsj.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
		}
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				ShouCang shouCang = list.get(arg2);
				String lon = shouCang.getLON();
				String lat = shouCang.getLAT();
				if (!"".equals(lon) && !"".equals(lat)) {
					double x = Double.parseDouble(lon);
					double y = Double.parseDouble(lat);
					Point point = new Point(x, y);
					graphicsLayer.removeAll();
					Graphic graphic = new Graphic(point, markerSymbol);
					graphicsLayer.addGraphic(graphic);
					mapView.setExtent(point, 0, true);
					dismiss();
				}
			}
		});
	}

	public void addMenuItem(SwipeMenu menu) {

		SwipeMenuItem editItem = new SwipeMenuItem(context);
		// set item background
		editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
		// set item width
		editItem.setWidth(Util.dp2px(context, 90));
		// set a icon
		editItem.setTitle("编辑");
		editItem.setTitleSize(18);
		editItem.setTitleColor(Color.BLUE);
		// add to menu
		menu.addMenuItem(editItem);

		SwipeMenuItem deleteItem = new SwipeMenuItem(context);
		// set item background
		deleteItem
				.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
		// set item width
		deleteItem.setWidth(Util.dp2px(context, 90));
		// set a icon
		deleteItem.setIcon(R.drawable.ic_delete);
		// add to menu
		menu.addMenuItem(deleteItem);
	}
	/**删除数据*/
	public void delete(SwipeMenuListView lv,List<ShouCang> list,int position,TextView zwsj){
		ShouCang shouCang = list.get(position);
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", shouCang.getId()+"");
		boolean flag= service.delete(map);
		//boolean flag = DataBaseHelper.deleteScdData(context,shouCang.getId() + "");
		if(flag){
			ToastUtil.setToast(context, "删除成功");
			list.remove(list.get(position));
			adapter.notifyDataSetChanged();
		}
		if (list.size() == 0) {
			zwsj.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
		} else {
			zwsj.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
		}
	}
	/*更新数据*/
	public void update(SwipeMenuListView lv,List<ShouCang> list,int position){
		ShouCang cang = list.get(position);
		ShouCangDialog dialog = new ShouCangDialog(context,cang, SqlType.UPDATE,service,adapter);
		BussUtil.setDialogParams(context, dialog, 0.7, 0.8);
	}
}
