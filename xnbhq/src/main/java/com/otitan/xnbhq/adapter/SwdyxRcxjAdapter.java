package com.otitan.xnbhq.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.SwdyxActivity;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 生物多样性 日常巡检管理Adapter
 */
public class SwdyxRcxjAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private MapView mapView;
	private GraphicsLayer graphicsLayer;

	public SwdyxRcxjAdapter(Context context, MapView mapView,GraphicsLayer graphicsLayer, List<HashMap<String, String>> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mapView = mapView;
		this.graphicsLayer = graphicsLayer;
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
			convertView = inflater.inflate(R.layout.item_swdyx_rcxj, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.rcxj_cb);
			holder.tv1 = (TextView) convertView.findViewById(R.id.rcxj_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.rcxj_tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.rcxj_tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.rcxj_tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.rcxj_tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.rcxj_tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.rcxj_tv7);
			holder.tv8 = (TextView) convertView.findViewById(R.id.rcxj_tv8);
			holder.tv9 = (TextView) convertView.findViewById(R.id.rcxj_tv9);
			holder.tv10 = (TextView) convertView.findViewById(R.id.rcxj_tv10);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (BussUtil.isEmperty(list.get(position).get("ID").trim())) {
					HashMap<String, String> map = list.get(position);
					map.put(list.get(position).get("ID"), arg1 + "");
					notifyDataSetChanged();
				}
			}
		});
		holder.cb.setChecked(Boolean.parseBoolean(list.get(position).get(
				list.get(position).get("ID"))));

		holder.tv1.setText((position + 1) + "");
		if (BussUtil.isEmperty(list.get(position).get("INSPECTDEPARTMENT").trim())) {
			holder.tv2.setText(list.get(position).get("INSPECTDEPARTMENT").trim());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("INSPECTSITE").trim())) {
			holder.tv3.setText(list.get(position).get("INSPECTSITE").trim());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("DISPATCHNUM").trim())) {
			holder.tv4.setText(list.get(position).get("DISPATCHNUM").trim());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("INSPECTEDUNIT").trim())) {
			holder.tv5.setText(list.get(position).get("INSPECTEDUNIT").trim());
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("PERINSPECTED").trim())) {
			holder.tv6.setText(list.get(position).get("PERINSPECTED").trim());
		} else {
			holder.tv6.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("INSPECTTIME").trim())) {
			try {
				String str = list.get(position).get("INSPECTTIME").trim().substring(0, 10);
				Date date = format.parse(str.replace("/", "-"));
				holder.tv7.setText(format.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			holder.tv7.setText("");
		}
		holder.tv8.setText("查看");
		holder.tv8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCheckDailog(list.get(position));
			}
		});
		holder.tv9.setText("编辑");
		holder.tv9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDailog(list.get(position));

			}
		});
		holder.tv10.setText("定位");
		holder.tv10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!BussUtil.isEmperty(list.get(position).get("LONGITUDE"))
						|| !BussUtil.isEmperty(list.get(position).get(
						"LATITUDE"))) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.longorlannull));
					return;
				}
				double lon = Double.parseDouble(list.get(position).get(
						"LONGITUDE"));
				double lat = Double.parseDouble(list.get(position).get(
						"LATITUDE"));
				Point point = new Point(lon, lat);
				Graphic graphic = new Graphic(point, new SimpleMarkerSymbol(
						Color.RED, 10,com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE));
				graphicsLayer.addGraphic(graphic);
				mapView.setExtent(point);
				ToastUtil.setToast(
						context,
						context.getResources().getString(
								R.string.locationsuccess));
			}
		});

		return convertView;
	}

	/* 查看 */
	protected void showCheckDailog(HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.swdyx_rcxcgl_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView swdyx_rcgl_xcry_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_xcry_check);
		TextView swdyx_rcgl_xcdd_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_xcdd_check);
		TextView swdyx_rcgl_cdrc_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_cdrc_check);
		TextView swdyx_rcgl_bxcdw_edit = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_bxcdw_check);
		TextView swdyx_rcgl_lon_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_lon_check);
		TextView swdyx_rcgl_xcjg_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_xcjg_check);
		TextView swdyx_rcgl_bz_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_bz_check);
		TextView swdyx_rcgl_xcbm_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_xcbm_check);
		TextView swdyx_rcgl_xctime_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_xctime_check);
		TextView swdyx_rcgl_cdcls_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_cdcls_check);
		TextView swdyx_rcgl_bxcr_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_bxcr_check);
		TextView swdyx_rcgl_lat_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_lat_check);
		TextView swdyx_rcgl_sfywfxw_check = (TextView) dialog
				.findViewById(R.id.swdyx_rcgl_sfywfxw_check);
		Button swdyx_rcgl_close_check = (Button) dialog
				.findViewById(R.id.swdyx_rcgl_close_check);

		if (BussUtil.isEmperty(map.get("INSPECTOR").trim())) {
			swdyx_rcgl_xcry_check.setText(map.get("INSPECTOR").trim());
		} else {
			swdyx_rcgl_xcry_check.setText("");
		}
		// 巡查地点
		if (BussUtil.isEmperty(map.get("INSPECTSITE").trim())) {
			swdyx_rcgl_xcdd_check.setText(map.get("INSPECTSITE").trim());
		} else {
			swdyx_rcgl_xcdd_check.setText("");
		}
		if (BussUtil.isEmperty(map.get("DISPATCHNUM").trim())) {
			swdyx_rcgl_cdrc_check.setText(map.get("DISPATCHNUM").trim());
		} else {
			swdyx_rcgl_cdrc_check.setText("");
		}

		if (BussUtil.isEmperty(map.get("INSPECTEDUNIT").trim())) {
			swdyx_rcgl_bxcdw_edit.setText(map.get("INSPECTEDUNIT").trim());
		} else {
			swdyx_rcgl_bxcdw_edit.setText("");
		}
		if (BussUtil.isEmperty(map.get("LONGITUDE").trim())) {
			swdyx_rcgl_lon_check.setText(map.get("LONGITUDE").trim());
		} else {
			swdyx_rcgl_lon_check.setText("");
		}
		if (BussUtil.isEmperty(map.get("INSPECTRESULT").trim())) {
			swdyx_rcgl_xcjg_check.setText(map.get("INSPECTRESULT").trim());
		} else {
			swdyx_rcgl_xcjg_check.setText("");
		}
		if (BussUtil.isEmperty(map.get("REMARK").trim())) {
			swdyx_rcgl_bz_check.setText(map.get("REMARK").trim());
		} else {
			swdyx_rcgl_bz_check.setText("");
		}
		if (BussUtil.isEmperty(map.get("INSPECTDEPARTMENT").trim())) {
			swdyx_rcgl_xcbm_check.setText(map.get("INSPECTDEPARTMENT").trim());
		} else {
			swdyx_rcgl_xcbm_check.setText("");
		}
		try {
			if (BussUtil.isEmperty(map.get("INSPECTTIME").trim())) {
				String str = map.get("INSPECTTIME").trim().substring(0, 10);
				Date date = format.parse(str.replace("/", "-"));
				swdyx_rcgl_xctime_check.setText(format.format(date));
			} else {
				swdyx_rcgl_xctime_check.setText("");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (BussUtil.isEmperty(map.get("VEHICLENUM").trim())) {
			swdyx_rcgl_cdcls_check.setText(map.get("VEHICLENUM").trim());
		} else {
			swdyx_rcgl_cdcls_check.setText("");
		}
		// 被巡查人
		if (BussUtil.isEmperty(map.get("PERINSPECTED").trim())) {
			swdyx_rcgl_bxcr_check.setText(map.get("PERINSPECTED").trim());
		} else {
			swdyx_rcgl_bxcr_check.setText("");
		}
		// 纬度
		if (BussUtil.isEmperty(map.get("LATITUDE").trim())) {
			swdyx_rcgl_lat_check.setText(map.get("LATITUDE").trim());
		} else {
			swdyx_rcgl_lat_check.setText("");
		}
		if (BussUtil.isEmperty(map.get("ISILLEGAL").trim())) {
			swdyx_rcgl_sfywfxw_check.setText(map.get("ISILLEGAL").trim());
		} else {
			swdyx_rcgl_sfywfxw_check.setText("");
		}
		swdyx_rcgl_close_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();

			}
		});
        BussUtil.setDialogGravity_Center(context, dialog, 0.8, 0.90);
	}

	/* 编辑 */
	protected void showEditDailog(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.swdyx_rcxcgl_edit);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		final EditText swdyx_rcgl_xcry = (EditText) dialog.findViewById(R.id.swdyx_rcgl_xcry_edit);
		if (BussUtil.isEmperty(map.get("INSPECTOR").trim())) {
			swdyx_rcgl_xcry.setText(map.get("INSPECTOR").trim());
		} else {
			swdyx_rcgl_xcry.setText("");
		}

		final EditText swdyx_rcgl_xcbm = (EditText) dialog.findViewById(R.id.swdyx_rcgl_xcbm_edit);
		if (BussUtil.isEmperty(map.get("INSPECTDEPARTMENT").trim())) {
			swdyx_rcgl_xcbm.setText(map.get("INSPECTDEPARTMENT").trim());
		} else {
			swdyx_rcgl_xcbm.setText("");
		}

		final EditText swdyx_rcgl_xcdd = (EditText) dialog.findViewById(R.id.swdyx_rcgl_xcdd_edit);
		if (BussUtil.isEmperty(map.get("INSPECTSITE").trim())) {
			swdyx_rcgl_xcdd.setText(map.get("INSPECTSITE").trim());
		} else {
			swdyx_rcgl_xcdd.setText("");
		}

		final EditText swdyx_rcgl_cdrc = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_cdrc_edit);
		if (BussUtil.isEmperty(map.get("DISPATCHNUM").trim())) {
			swdyx_rcgl_cdrc.setText(map.get("DISPATCHNUM").trim());
		} else {
			swdyx_rcgl_cdrc.setText("");
		}

		final EditText swdyx_rcgl_cdcls = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_cdcls_edit);
		if (BussUtil.isEmperty(map.get("VEHICLENUM").trim())) {
			swdyx_rcgl_cdcls.setText(map.get("VEHICLENUM").trim());
		} else {
			swdyx_rcgl_cdcls.setText("");
		}

		final Button swdyx_rcgl_xctime = (Button) dialog
				.findViewById(R.id.swdyx_rcgl_xctime_edit);
		try {
			if (BussUtil.isEmperty(map.get("INSPECTTIME").trim())) {
				String str = map.get("INSPECTTIME").trim().substring(0, 10);
				Date date = format.parse(str.replace("/", "-"));
				swdyx_rcgl_xctime.setText(format.format(date));
			} else {
				swdyx_rcgl_xctime.setText("");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		final SwdyxActivity activity=(SwdyxActivity) context;
		swdyx_rcgl_xctime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(swdyx_rcgl_xctime, false);
			}
		});
		final EditText swdyx_rcgl_bxcdw = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_bxcdw_edit);
		if (BussUtil.isEmperty(map.get("INSPECTEDUNIT").trim())) {
			swdyx_rcgl_bxcdw.setText(map.get("INSPECTEDUNIT").trim());
		} else {
			swdyx_rcgl_bxcdw.setText("");
		}

		final EditText swdyx_rcgl_bxcr = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_bxcr_edit);
		if (BussUtil.isEmperty(map.get("PERINSPECTED").trim())) {
			swdyx_rcgl_bxcr.setText(map.get("PERINSPECTED").trim());
		} else {
			swdyx_rcgl_bxcr.setText("");
		}

		final EditText swdyx_rcgl_lon = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_lon_edit);
		if (BussUtil.isEmperty(map.get("LONGITUDE").trim())) {
			swdyx_rcgl_lon.setText(map.get("LONGITUDE").trim());
		} else {
			swdyx_rcgl_lon.setText("");
		}

		final EditText swdyx_rcgl_lat = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_lat_edit);
		if (BussUtil.isEmperty(map.get("LATITUDE").trim())) {
			swdyx_rcgl_lat.setText(map.get("LATITUDE").trim());
		} else {
			swdyx_rcgl_lat.setText("");
		}

		final EditText swdyx_rcgl_xcjg = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_xcjg_edit);
		if (BussUtil.isEmperty(map.get("INSPECTRESULT").trim())) {
			swdyx_rcgl_xcjg.setText(map.get("INSPECTRESULT").trim());
		} else {
			swdyx_rcgl_xcjg.setText("");
		}

		final CheckBox swdyx_rcgl_sfywfxw = (CheckBox) dialog
				.findViewById(R.id.swdyx_rcgl_sfywfxw_edit);
		if (BussUtil.isEmperty(map.get("ISILLEGAL").trim())) {
			if ("是".equals(map.get("ISILLEGAL").trim())) {
				swdyx_rcgl_sfywfxw.setChecked(true);
			} else {
				swdyx_rcgl_sfywfxw.setChecked(false);
			}
		} else {
			swdyx_rcgl_sfywfxw.setChecked(false);
		}

		final EditText swdyx_rcgl_bz = (EditText) dialog
				.findViewById(R.id.swdyx_rcgl_bz_edit);
		if (BussUtil.isEmperty(map.get("REMARK").trim())) {
			swdyx_rcgl_bz.setText(map.get("REMARK").trim());
		} else {
			swdyx_rcgl_bz.setText("");
		}

		Button save = (Button) dialog.findViewById(R.id.swdyx_rcgl_save_edit);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Webservice webservice = new Webservice(context);
				String id = "";
				if (BussUtil.isEmperty(map.get("ID").trim())) {
					id = map.get("ID").trim();
				}
				String xcry = swdyx_rcgl_xcry.getText().toString().trim();
				if (!BussUtil.isEmperty(xcry)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xcrynotnull));
					return;
				}
				String xcdd = swdyx_rcgl_xcdd.getText().toString().trim();
				if (!BussUtil.isEmperty(xcdd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xcddnotnull));
					return;
				}
				String cdrc = swdyx_rcgl_cdrc.getText().toString().trim();
				if (!BussUtil.isEmperty(cdrc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.cdrcnotnull));
					return;
				}
				String xctime = swdyx_rcgl_xctime.getText().toString().trim();
				if (!BussUtil.isEmperty(xctime)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xctimenotnull));
					return;
				}
				String xcbm = swdyx_rcgl_xcbm.getText().toString().trim();
				String cdcls = swdyx_rcgl_cdcls.getText().toString().trim();
				String bxcdw = swdyx_rcgl_bxcdw.getText().toString().trim();
				String bxcr = swdyx_rcgl_bxcr.getText().toString().trim();
				String lon = swdyx_rcgl_lon.getText().toString().trim();
				String lat = swdyx_rcgl_lat.getText().toString().trim();
				String xcjg = swdyx_rcgl_xcjg.getText().toString().trim();
				String sfywfxw;
				if (swdyx_rcgl_sfywfxw.isChecked()) {
					sfywfxw = "是";
				} else {
					sfywfxw = "否";
				}
				String bz = swdyx_rcgl_bz.getText().toString().trim();

				String result = webservice.updateSwdyxRcglData(id, xcry, xcbm,
						xcdd, xctime, cdrc, cdcls, bxcdw, bxcr, lon, lat, xcjg,
						sfywfxw, bz);
				if ("true".equals(result)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editsuccess));
					map.put("INSPECTOR", xcry);
					map.put("INSPECTDEPARTMENT", xcbm);
					map.put("INSPECTSITE", xcdd);
					map.put("INSPECTTIME", xctime);
					map.put("DISPATCHNUM", cdrc);
					map.put("VEHICLENUM", cdcls);
					map.put("INSPECTEDUNIT", bxcdw);
					map.put("PERINSPECTED", bxcr);
					map.put("LONGITUDE", lon);
					map.put("LATITUDE", lat);
					map.put("INSPECTRESULT", xcjg);
					map.put("ISILLEGAL", sfywfxw);
					map.put("REMARK", bz);
					notifyDataSetChanged();
					dialog.dismiss();
				} else {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editfailed));
				}
			}
		});

		Button cancle = (Button) dialog
				.findViewById(R.id.swdyx_rcgl_cancle_edit);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		BussUtil.setDialogGravity_Center(context, dialog, 0.8, 0.90);
	}

	public final class ViewHolder {
		public CheckBox cb;
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
