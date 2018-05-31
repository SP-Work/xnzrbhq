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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 生物多样性 经营动物单位Adapter
 */
public class SwdyxJydwdwAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private SwdyxActivity activity;
	private MapView mapView;
	private GraphicsLayer graphicsLayer;

	public SwdyxJydwdwAdapter(Context context, MapView mapView,
							  GraphicsLayer graphicsLayer, List<HashMap<String, String>> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mapView = mapView;
		this.graphicsLayer = graphicsLayer;
		this.activity=(SwdyxActivity) context;
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
			convertView = inflater.inflate(R.layout.item_swdyx_jydwdwgl, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.tv1 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.swdyx_jydwdw_tv7);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (BussUtil.isEmperty(list.get(position).get("OBJECTID").trim())) {
					HashMap<String, String> map = list.get(position);
					map.put(list.get(position).get("OBJECTID"), arg1 + "");
					notifyDataSetChanged();
				}
			}
		});
		holder.cb.setChecked(Boolean.parseBoolean(list.get(position).get(
				list.get(position).get("OBJECTID"))));

		holder.tv1.setText((position + 1) + "");
		if (BussUtil.isEmperty(list.get(position).get("FZR").trim())) {
			holder.tv2.setText(list.get(position).get("FZR").trim());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("ADDRESS").trim())) {
			holder.tv3.setText(list.get(position).get("ADDRESS").trim());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("PHONE").trim())) {
			holder.tv4.setText(list.get(position).get("PHONE").trim());
		} else {
			holder.tv4.setText("");
		}


		holder.tv5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCheckDailog(list.get(position));
			}
		});
		holder.tv6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDailog(list.get(position));

			}
		});
		holder.tv7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!BussUtil.isEmperty(list.get(position).get("X"))
						|| !BussUtil.isEmperty(list.get(position).get(
						"Y"))) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.longorlannull));
					return;
				}
				double lon = Double.parseDouble(list.get(position).get(
						"X"));
				double lat = Double.parseDouble(list.get(position).get(
						"Y"));
				Point point = new Point(lon, lat);
				Graphic graphic = new Graphic(point, new SimpleMarkerSymbol(
						Color.RED, 10,
						com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE));
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
		dialog.setContentView(R.layout.swdyx_jydwdwgl_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView swdyx_jydwdw_fzr = (TextView) dialog
				.findViewById(R.id.swdyx_jydwdw_fzr);
		TextView swdyx_jydwdw_lxfs = (TextView) dialog
				.findViewById(R.id.swdyx_jydwdw_lxfs);
		TextView swdyx_jydwdw_jd = (TextView) dialog
				.findViewById(R.id.swdyx_jydwdw_jd);

		TextView swdyx_jydwdw_wd = (TextView) dialog
				.findViewById(R.id.swdyx_jydwdw_wd);

		TextView swdyx_jydwdw_dz = (TextView) dialog
				.findViewById(R.id.swdyx_jydwdw_dz);

		Button back = (Button) dialog.findViewById(R.id.swdyx_jydwdw_back);
		// 负责人
		if (BussUtil.isEmperty(map.get("FZR").trim())) {
			swdyx_jydwdw_fzr.setText(map.get("FZR").trim());
		} else {
			swdyx_jydwdw_fzr.setText("");
		}
		// 联系方式
		if (BussUtil.isEmperty(map.get("PHONE").trim())) {
			swdyx_jydwdw_lxfs.setText(map.get("PHONE").trim());
		} else {
			swdyx_jydwdw_lxfs.setText("");
		}
		// 经度
		if (BussUtil.isEmperty(map.get("X").trim())) {
			swdyx_jydwdw_jd.setText(map.get("X").trim());
		} else {
			swdyx_jydwdw_jd.setText("");
		}
		// 纬度
		if (BussUtil.isEmperty(map.get("Y").trim())) {
			swdyx_jydwdw_wd.setText(map.get("Y").trim());
		} else {
			swdyx_jydwdw_wd.setText("");
		}
		// 基地地址
		if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
			swdyx_jydwdw_dz.setText(map.get("ADDRESS").trim());
		} else {
			swdyx_jydwdw_dz.setText("");
		}

		back.setOnClickListener(new OnClickListener() {

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
		dialog.setContentView(R.layout.swdyx_jydwdwgl_edit);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		TextView head=(TextView) dialog.findViewById(R.id.swdyx_jydwdw_head);
		head.setText(context.getResources().getString(R.string.jydwdwedit));

		final EditText swdyx_jydwdw_fzr = (EditText) dialog
				.findViewById(R.id.swdyx_jydwdw_fzr);
		if (BussUtil.isEmperty(map.get("FZR").trim())) {
			swdyx_jydwdw_fzr.setText(map.get("FZR").trim());
		} else {
			swdyx_jydwdw_fzr.setText("");
		}

		final EditText swdyx_jydwdw_lxfs = (EditText) dialog
				.findViewById(R.id.swdyx_jydwdw_lxfs);
		if (BussUtil.isEmperty(map.get("PHONE").trim())) {
			swdyx_jydwdw_lxfs.setText(map.get("PHONE").trim());
		} else {
			swdyx_jydwdw_lxfs.setText("");
		}

		final EditText swdyx_jydwdw_jd = (EditText) dialog
				.findViewById(R.id.swdyx_jydwdw_jd);
		if (BussUtil.isEmperty(map.get("X").trim())) {
			swdyx_jydwdw_jd.setText(map.get("X").trim());
		} else {
			swdyx_jydwdw_jd.setText("");
		}

		final EditText swdyx_jydwdw_wd = (EditText) dialog
				.findViewById(R.id.swdyx_jydwdw_wd);
		if (BussUtil.isEmperty(map.get("Y").trim())) {
			swdyx_jydwdw_wd.setText(map.get("Y").trim());
		} else {
			swdyx_jydwdw_wd.setText("");
		}

		final EditText swdyx_jydwdw_dz = (EditText) dialog
				.findViewById(R.id.swdyx_jydwdw_dz);
		if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
			swdyx_jydwdw_dz.setText(map.get("ADDRESS").trim());
		} else {
			swdyx_jydwdw_dz.setText("");
		}
		Button save = (Button) dialog.findViewById(R.id.swdyx_jydwdw_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Webservice webservice = new Webservice(context);
				String id = "";
				if (BussUtil.isEmperty(map.get("OBJECTID").trim())) {
					id = map.get("OBJECTID").trim();
				}
				String fzr = swdyx_jydwdw_fzr.getText().toString().trim();
				if (!BussUtil.isEmperty(fzr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dwfzrnotnull));
					return;
				}
				String lxfs = swdyx_jydwdw_lxfs.getText().toString().trim();
				if (!BussUtil.isEmperty(lxfs)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.lxfsnotnull));
					return;
				}
				String jd = swdyx_jydwdw_jd.getText().toString().trim();
				if (!BussUtil.isEmperty(jd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdnotnull));
					return;
				}
				String wd = swdyx_jydwdw_wd.getText().toString().trim();
				if (!BussUtil.isEmperty(wd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.wdnotnull));
					return;
				}
				String dz = swdyx_jydwdw_dz.getText().toString().trim();
				if (!BussUtil.isEmperty(dz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dwdznotnull));
					return;
				}
				String result = webservice.updateSwdyxJydwdwData(id, fzr, lxfs, jd, wd, dz);
				if ("true".equals(result)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editsuccess));
					map.put("FZR", fzr);
					map.put("PHONE", lxfs);
					map.put("X", jd);
					map.put("Y", wd);
					map.put("ADDRESS", dz);
					notifyDataSetChanged();
					dialog.dismiss();
				} else {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.editfailed));
				}
			}
		});

		Button cancle = (Button) dialog
				.findViewById(R.id.swdyx_jydwdw_cancle);
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
	}



}
