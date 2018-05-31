package com.otitan.xnbhq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.YHSWActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 有害生物 诱虫灯管理Adapter
 */
public class YhswYcddcAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private LayoutInflater inflater = null;
	private YHSWActivity activity;

	public YhswYcddcAdapter(Context context,List<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		activity = (YHSWActivity) context;
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
			convertView = inflater.inflate(R.layout.item_yhsw_ycddc, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
			holder.tv8 = (TextView) convertView.findViewById(R.id.tv8);
			holder.tv9 = (TextView) convertView.findViewById(R.id.tv9);
			holder.tv10 = (TextView) convertView.findViewById(R.id.tv10);
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

		if (BussUtil.isEmperty(list.get(position).get("YCDBH"))) {
			holder.tv1.setText(list.get(position).get("YCDBH"));
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("DCR"))) {
			holder.tv2.setText(list.get(position).get("DCR"));
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("DCDW"))) {
			holder.tv3.setText(list.get(position).get("DCDW"));
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("YCDMC"))) {
			holder.tv4.setText(list.get(position).get("YCDMC"));
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBR"))) {
			holder.tv5.setText(list.get(position).get("SBR"));
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBSJ"))) {
			String sbsj = list.get(position).get("SBSJ").replace("/", "-");
			holder.tv6.setText(sbsj);
		} else {
			holder.tv6.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SCZT"))) {
			if ("0".equals(list.get(position).get("SCZT"))) {
				holder.tv7.setText(context.getResources().getString(
						R.string.havenotupload));
				holder.tv10.setVisibility(View.VISIBLE);
				holder.tv9.setVisibility(View.VISIBLE);
			} else if ("1".equals(list.get(position).get("SCZT"))) {
				holder.tv7.setText(context.getResources().getString(
						R.string.haveupload));
				holder.tv10.setVisibility(View.GONE);
				holder.tv9.setVisibility(View.GONE);
			} else {
				holder.tv7.setText("");
				holder.tv10.setVisibility(View.VISIBLE);
				holder.tv9.setVisibility(View.VISIBLE);
			}
		} else {
			holder.tv7.setText("");
			holder.tv10.setVisibility(View.VISIBLE);
			holder.tv9.setVisibility(View.VISIBLE);
		}
		holder.tv8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCkeckDialog(list.get(position));
			}
		});
		holder.tv9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showEditDialog(list.get(position));
			}
		});
		holder.tv10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShangBao(list.get(position));
			}
		});
		return convertView;
	}

	/**诱虫灯 上报 */
	protected void ShangBao(HashMap<String, String> map) {
		Webservice web = new Webservice(context);
		String ycdbh = map.get("YCDBH");
		String ycdmc = map.get("YCDMC");
		String dcr = map.get("DCR");
		String dcsj = map.get("DCSJ");
		String dcdw = map.get("DCDW");
		String dcdjd = map.get("DCDJD");
		String dcdwd = map.get("DCDWD");
		String hb = map.get("HB");
		String xdm = map.get("XDM");
		String xbh = map.get("XBH");
		String xbmj = map.get("XBMJ");
		String zyhcmc = map.get("ZYHCMC");
		String lfzc = map.get("LFZC");
		String hcsl = map.get("HCSL");
		String ybd = map.get("YBD");
		String pjg = map.get("PJG");
		String pjxj = map.get("PJXJ");
		String city = map.get("CITY");
		String county = map.get("COUNTY");
		String town = map.get("TOWN");
		String village = map.get("VILLAGE");
		String bz = map.get("BZ");
		String sbr = map.get("SBR");
		String sbsj = map.get("SBSJ");

		String result = web.addYhswYcddcData(ycdbh, ycdmc, dcr, dcdw, dcsj, dcdjd, dcdwd, hb, xdm, xbh, xbmj, zyhcmc, lfzc, hcsl, ybd, pjg, pjxj, city, county, town, village, bz, "1", sbr, sbsj);
		String[] splits = result.split(",");
		if (splits.length > 0) {
			if ("True".equals(splits[0])) {
				DataBaseHelper.deleteYhswYcddcData(context, "db.sqlite",
						map.get("ID"));
				map.put("SCZT", "1");
				notifyDataSetChanged();
				ToastUtil.setToast(context,
						context.getResources()
								.getString(R.string.uploadsuccess));
			} else {
				ToastUtil
						.setToast(
								context,
								context.getResources().getString(
										R.string.uploadfailed));
			}
		} else {
			ToastUtil.setToast(context,
					context.getResources().getString(R.string.uploadfailed));
		}
	}

	/* 编辑 */
	protected void showEditDialog(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.yhsw_ycddc_add);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView yhsw_ycddc_head = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_head);
		yhsw_ycddc_head.setText(context.getResources().getString(R.string.ycddcedit));

		View status = dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		final TextView yhsw_ycddc_ycdbh = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_ycdbh);
		yhsw_ycddc_ycdbh.setText(map.get("YCDBH").trim());

		final EditText yhsw_ycddc_dcr = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_dcr);
		yhsw_ycddc_dcr.setText(map.get("DCR").trim());

		final TextView yhsw_ycddc_dcrq = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_dcrq);
		yhsw_ycddc_dcrq.setText(map.get("DCSJ").trim());

		final EditText yhsw_ycddc_dcdw = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_dcdw);
		yhsw_ycddc_dcdw.setText(map.get("DCDW").trim());

		final EditText yhsw_ycddc_ycdmc = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_ycdmc);
		yhsw_ycddc_ycdmc.setText(map.get("YCDMC").trim());

		final EditText yhsw_ycddc_hb = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_hb);
		yhsw_ycddc_hb.setText(map.get("HB").trim());

		final EditText yhsw_ycddc_xdm = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_xdm);
		yhsw_ycddc_xdm.setText(map.get("XDM").trim());

		final EditText yhsw_ycddc_xbh = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_xbh);
		yhsw_ycddc_xbh.setText(map.get("XBH").trim());

		final EditText yhsw_ycddc_dcdjd = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_dcdjd);
		yhsw_ycddc_dcdjd.setText(map.get("DCDJD").trim());

		final EditText yhsw_ycddc_dcdwd = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_dcdwd);
		yhsw_ycddc_dcdwd.setText(map.get("DCDWD").trim());

		final EditText yhsw_ycddc_xbmj = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_xbmj);
		yhsw_ycddc_xbmj.setText(map.get("XBMJ").trim());

		final EditText yhsw_ycddc_zyhcmc = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_zyhcmc);
		yhsw_ycddc_zyhcmc.setText(map.get("ZYHCMC").trim());

		final Spinner yhsw_ycddc_lfzc = (Spinner) dialog
				.findViewById(R.id.yhsw_ycddc_lfzc);
		if (BussUtil.isEmperty(map.get("LFZC"))) {
			int a = Integer.parseInt(map.get("LFZC"));
			yhsw_ycddc_lfzc.setSelection(a);
		}

		final EditText yhsw_ycddc_hcsl = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_hcsl);
		yhsw_ycddc_hcsl.setText(map.get("HCSL").trim());

		final EditText yhsw_ycddc_ybd = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_ybd);
		yhsw_ycddc_ybd.setText(map.get("YBD").trim());

		final EditText yhsw_ycddc_pjg = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_pjg);
		yhsw_ycddc_pjg.setText(map.get("PJG").trim());

		final EditText yhsw_ycddc_pjxj = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_pjxj);
		yhsw_ycddc_pjxj.setText(map.get("PJXJ").trim());

		final EditText yhsw_ycddc_city = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_city);
		yhsw_ycddc_city.setText(map.get("CITY").trim());

		final EditText yhsw_ycddc_county = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_county);
		yhsw_ycddc_county.setText(map.get("COUNTY").trim());

		final EditText yhsw_ycddc_town = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_town);
		yhsw_ycddc_town.setText(map.get("TOWN").trim());

		final EditText yhsw_ycddc_village = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_village);
		yhsw_ycddc_village.setText(map.get("VILLAGE").trim());

		final EditText yhsw_ycddc_sbr = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_sbr);
		yhsw_ycddc_sbr.setText(map.get("SBR").trim());

		final Button yhsw_ycddc_sbsj = (Button) dialog
				.findViewById(R.id.yhsw_ycddc_sbsj);
		yhsw_ycddc_sbsj.setText(map.get("SBSJ").trim());
		yhsw_ycddc_sbsj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_ycddc_sbsj, false);
			}
		});

		final TextView yhsw_ycddc_sbzt = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_sbzt);
		if ("1".equals(map.get("SCZT").trim())) {
			yhsw_ycddc_sbzt.setText(context.getResources().getString(
					R.string.haveupload));
		} else if ("0".equals(map.get("SCZT").trim())) {
			yhsw_ycddc_sbzt.setText(context.getResources().getString(
					R.string.havenotupload));
		} else {
			yhsw_ycddc_sbzt.setText("");
		}

		final EditText yhsw_ycddc_bz = (EditText) dialog
				.findViewById(R.id.yhsw_ycddc_bz);
		yhsw_ycddc_bz.setText(map.get("BZ").trim());

		Button upload = (Button) dialog.findViewById(R.id.yhsw_ycddc_upload);
		upload.setText(context.getResources().getString(R.string.bc));
		Button bdsave = (Button) dialog.findViewById(R.id.yhsw_ycddc_bdsave);
		bdsave.setVisibility(View.GONE);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ycdbh = yhsw_ycddc_ycdbh.getText().toString().trim();
				if (TextUtils.isEmpty(ycdbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycdbhnotnull));
					return;
				}
				String ycdmc = yhsw_ycddc_ycdmc.getText().toString().trim();
				if (TextUtils.isEmpty(ycdmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycdmcnotnull));
					return;
				}
				String dcr = yhsw_ycddc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_ycddc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_ycddc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String dcdjd = yhsw_ycddc_dcdjd.getText().toString().trim();
				if (TextUtils.isEmpty(dcdjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdjdnotnull));
					return;
				}
				String dcdwd = yhsw_ycddc_dcdwd.getText().toString().trim();
				if (TextUtils.isEmpty(dcdwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwdnotnull));
					return;
				}

				String xdm = yhsw_ycddc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String xbh = yhsw_ycddc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_ycddc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String zyhcmc = yhsw_ycddc_zyhcmc.getText().toString().trim();
				if (TextUtils.isEmpty(zyhcmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zyhcmcnotnull));
					return;
				}
				String hcsl = yhsw_ycddc_hcsl.getText().toString().trim();
				if (TextUtils.isEmpty(hcsl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.hcslnotnull));
					return;
				}
				String ybd = yhsw_ycddc_ybd.getText().toString().trim();
				if (TextUtils.isEmpty(ybd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ybdnotnull));
					return;
				}
				String pjg = yhsw_ycddc_pjg.getText().toString().trim();
				if (TextUtils.isEmpty(pjg)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.pjgnotnull));
					return;
				}
				String pjxj = yhsw_ycddc_pjxj.getText().toString().trim();
				if (TextUtils.isEmpty(pjxj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.pjxjnotnull));
					return;
				}
				String city = yhsw_ycddc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_ycddc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_ycddc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_ycddc_village.getText().toString().trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}

				String sbr = yhsw_ycddc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_ycddc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}

				String hb = yhsw_ycddc_hb.getText().toString().trim();
				String lfzc = yhsw_ycddc_lfzc.getSelectedItemPosition()+"";
				String bz = yhsw_ycddc_bz.getText().toString().trim();
				DataBaseHelper.updateYhswYcddcData(context, "db.sqlite", ycdbh, ycdmc, dcr, dcdw, dcsj, dcdjd, dcdwd, hb, xdm, xbh, xbmj, zyhcmc, lfzc, hcsl, ybd, pjg, pjxj, city, county, town, village, bz, "0", sbr, sbsj);
				map.put("YCDMC", ycdmc);
				map.put("DCR", dcr);
				map.put("DCSJ", dcsj);
				map.put("DCDW", dcdw);
				map.put("DCDJD", dcdjd);
				map.put("DCDWD", dcdwd);
				map.put("HB", hb);
				map.put("XDM", xdm);
				map.put("XBH", xbh);
				map.put("XBMJ", xbmj);
				map.put("ZYHCMC", zyhcmc);
				map.put("LFZC", lfzc);
				map.put("HCSL", hcsl);
				map.put("YBD", ybd);
				map.put("PJG", pjg);
				map.put("PJXJ", pjxj);
				map.put("CITY", city);
				map.put("COUNTY", county);
				map.put("TOWN", town);
				map.put("VILLAGE", village);
				map.put("BZ", bz);
				map.put("SCZT", "0");
				map.put("SBR", sbr);
				map.put("SBSJ", sbsj);
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.editsuccess));
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		Button cancle = (Button) dialog.findViewById(R.id.yhsw_ycddc_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
	}

	/* 查看 */
	protected void showCkeckDialog(HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_ycddc_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView ycdbh = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_ycdbh);
		ycdbh .setText(map.get("YCDBH").trim());

		TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_ycddc_dcr);
		dcr.setText(map.get("DCR").trim());

		TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_ycddc_dcrq);
		dcsj.setText(map.get("DCSJ").replace("/", "-"));

		TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_ycddc_dcdw);
		dcdw.setText(map.get("DCDW").trim());

		TextView ycdmc = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_ycdmc);
		ycdmc.setText(map.get("YCDMC").trim());

		TextView hb = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_hb);
		hb.setText(map.get("HB").trim());

		TextView xdm = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_xdm);
		xdm.setText(map.get("XDM").trim());

		TextView xbh = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_xbh);
		xbh.setText(map.get("XBH").trim());

		TextView dcdjd = (TextView) dialog.findViewById(R.id.yhsw_ycddc_dcdjd);
		dcdjd.setText(map.get("DCDJD").trim());

		TextView dcdwd = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_dcdwd);
		dcdwd.setText(map.get("DCDWD").trim());

		TextView xbmj = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_xbmj);
		xbmj.setText(map.get("XBMJ").trim());

		TextView zyhcmc = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_zyhcmc);
		zyhcmc.setText(map.get("ZYHCMC").trim());


		TextView lfzc = (TextView) dialog.findViewById(R.id.yhsw_ycddc_lfzc);
		if (BussUtil.isEmperty(map.get("LFZC").trim())) {
			int a = Integer.parseInt(map.get("LFZC").trim());
			String[] array = context.getResources().getStringArray(
					R.array.lfzc);
			lfzc.setText(array[a]);
		}
		TextView hcsl = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_hcsl);
		hcsl.setText(map.get("HCSL").trim());

		TextView ybd = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_ybd);
		ybd.setText(map.get("YBD").trim());

		TextView pjg = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_pjg);
		pjg.setText(map.get("PJG").trim());

		TextView pjxj = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_pjxj);
		pjxj.setText(map.get("PJXJ").trim());

		TextView city = (TextView) dialog.findViewById(R.id.yhsw_ycddc_city);
		city.setText(map.get("CITY").trim());

		TextView county = (TextView) dialog.findViewById(R.id.yhsw_ycddc_county);
		county.setText(map.get("COUNTY").trim());

		TextView town = (TextView) dialog.findViewById(R.id.yhsw_ycddc_town);
		town.setText(map.get("TOWN").trim());

		TextView village = (TextView) dialog.findViewById(R.id.yhsw_ycddc_village);
		village.setText(map.get("VILLAGE").trim());

		TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_ycddc_sbr);
		sbr.setText(map.get("SBR").trim());

		TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_ycddc_sbsj);
		sbsj.setText(map.get("SBSJ").replace("/", "-"));


		View status = dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		TextView sbzt = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_sbzt);
		if (BussUtil.isEmperty(map.get("SCZT").trim())) {
			if ("1".equals(map.get("SCZT").trim())) {
				sbzt.setText(context.getResources().getString(
						R.string.haveupload));
			} else if ("0".equals(map.get("SCZT").trim())) {
				sbzt.setText(context.getResources().getString(
						R.string.havenotupload));
			}
		} else {
			sbzt.setText("");
		}
		TextView bz = (TextView) dialog.findViewById(R.id.yhsw_ycddc_bz);
		bz.setText(map.get("BZ").trim());

		Button back = (Button) dialog.findViewById(R.id.yhsw_ycddc_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
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
