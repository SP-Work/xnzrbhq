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
 * 有害生物 木材病虫害调查信息Adapter
 */
public class YhswMcbchdcAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private LayoutInflater inflater = null;
	private YHSWActivity activity;

	public YhswMcbchdcAdapter(Context context,List<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.item_yhsw_mcbchdc, null);
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

		if (BussUtil.isEmperty(list.get(position).get("MCBCHBH"))) {
			holder.tv1.setText(list.get(position).get("MCBCHBH"));
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
		if (BussUtil.isEmperty(list.get(position).get("WHCD"))) {
			int a = Integer.parseInt(list.get(position).get("WHCD"));
			String[] whcds = context.getResources().getStringArray(
					R.array.mcwhcd);
			holder.tv4.setText(whcds[a]);
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

	/**木材病虫害 上报 */
	protected void ShangBao(HashMap<String, String> map) {
		Webservice web = new Webservice(context);
		String mcbchbh = map.get("MCBCHBH");
		String dcr = map.get("DCR");
		String dcsj = map.get("DCSJ");
		String dcdw = map.get("DCDW");
		String bdcdw = map.get("BDCDW");
		String dcdjd = map.get("DCDJD");
		String dcdwd = map.get("DCDWD");
		String zyjgcp = map.get("ZYJGCP");
		String xdm = map.get("XDM");
		String xykcmcpz = map.get("XYKCMCPZ");
		String zyjgjysz = map.get("ZYJGJYSZ");
		String ccmcsl = map.get("CCMCSL");
		String kcl = map.get("CCL");
		String ccmcpz = map.get("CCMCPZ");
		String bcmc = map.get("BCMC");
		String whcd = map.get("WHCD");
		String whbw = map.get("WHBW");
		String ct = map.get("CT");
		String city = map.get("CITY");
		String county = map.get("COUNTY");
		String town = map.get("TOWN");
		String village = map.get("VILLAGE");
		String bz = map.get("BZ");
		String sbr = map.get("SBR");
		String sbsj = map.get("SBSJ");

		String result = web.addYhswMcbchdcData(mcbchbh, dcr, dcsj, dcdw, bdcdw, dcdjd, dcdwd, zyjgcp, xdm, xykcmcpz, zyjgjysz, ccmcsl, kcl, ccmcpz, bcmc, whcd, whbw, ct, city, county, town, village, bz, "1", sbr, sbsj);
		String[] splits = result.split(",");
		if (splits.length > 0) {
			if ("True".equals(splits[0])) {
				DataBaseHelper.deleteYhswMcbchdcData(context, "db.sqlite",
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
		dialog.setContentView(R.layout.yhsw_mcbchdc_add);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView yhsw_ydzwdc_head = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_head);
		yhsw_ydzwdc_head.setText(context.getString(R.string.mcbchdcedit));

		View status = dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		final TextView yhsw_mcbchdc_mcbchbh = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_mcbchbh);
		yhsw_mcbchdc_mcbchbh.setText(map.get("MCBCHBH").trim());

		final EditText yhsw_mcbchdc_dcr = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcr);
		yhsw_mcbchdc_dcr.setText(map.get("DCR").trim());

		final TextView yhsw_mcbchdc_dcrq = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcrq);
		yhsw_mcbchdc_dcrq.setText(map.get("DCSJ").trim());

		final EditText yhsw_mcbchdc_dcdw = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcdw);
		yhsw_mcbchdc_dcdw.setText(map.get("DCDW").trim());

		final EditText yhsw_mcbchdc_bdcdw = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_bdcdw);
		yhsw_mcbchdc_bdcdw.setText(map.get("BDCDW").trim());

		final EditText yhsw_mcbchdc_zyjgcp = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_zyjgcp);
		yhsw_mcbchdc_zyjgcp.setText(map.get("ZYJGCP").trim());

		final EditText yhsw_mcbchdc_dcdjd = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcdjd);
		yhsw_mcbchdc_dcdjd.setText(map.get("DCDJD").trim());

		final EditText yhsw_mcbchdc_dcdwd = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcdwd);
		yhsw_mcbchdc_dcdwd.setText(map.get("DCDWD").trim());

		final EditText yhsw_mcbchdc_xdm = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_xdm);
		yhsw_mcbchdc_xdm.setText(map.get("XDM").trim());

		final EditText yhsw_mcbchdc_xykcmcpz = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_xykcmcpz);
		yhsw_mcbchdc_xykcmcpz.setText(map.get("XYKCMCPZ").trim());

		final EditText yhsw_mcbchdc_zyjgjysz = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_zyjgjysz);
		yhsw_mcbchdc_zyjgjysz.setText(map.get("ZYJGJYSZ").trim());

		final EditText yhsw_mcbchdc_ccmcsl = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_ccmcsl);
		yhsw_mcbchdc_ccmcsl.setText(map.get("CCMCSL").trim());

		final EditText yhsw_mcbchdc_kcl = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_kcl);
		yhsw_mcbchdc_kcl.setText(map.get("CCL").trim());

		final EditText yhsw_mcbchdc_ccmcpz = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_ccmcpz);
		yhsw_mcbchdc_ccmcpz.setText(map.get("CCMCPZ").trim());

		final EditText yhsw_mcbchdc_bcmc = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_bcmc);
		yhsw_mcbchdc_bcmc.setText(map.get("BCMC").trim());

		final Spinner yhsw_mcbchdc_whcd = (Spinner) dialog
				.findViewById(R.id.yhsw_mcbchdc_whcd);
		if (BussUtil.isEmperty(map.get("WHCD"))) {
			int a = Integer.parseInt(map.get("WHCD"));
			yhsw_mcbchdc_whcd.setSelection(a);
		}

		final Spinner yhsw_mcbchdc_whbw = (Spinner) dialog.findViewById(R.id.yhsw_mcbchdc_whbw);
		if (BussUtil.isEmperty(map.get("WHBW"))) {
			int a = Integer.parseInt(map.get("WHBW"));
			yhsw_mcbchdc_whbw.setSelection(a);
		}

		final Spinner yhsw_mcbchdc_ct = (Spinner) dialog
				.findViewById(R.id.yhsw_mcbchdc_ct);

		if (BussUtil.isEmperty(map.get("CT"))) {
			int a = Integer.parseInt(map.get("CT"));
			yhsw_mcbchdc_ct.setSelection(a);
		}

		final EditText yhsw_mcbchdc_city = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_city);
		yhsw_mcbchdc_city.setText(map.get("CITY").trim());

		final EditText yhsw_mcbchdc_county = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_county);
		yhsw_mcbchdc_county.setText(map.get("COUNTY").trim());

		final EditText yhsw_mcbchdc_town = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_town);
		yhsw_mcbchdc_town.setText(map.get("TOWN").trim());

		final EditText yhsw_mcbchdc_village = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_village);
		yhsw_mcbchdc_village.setText(map.get("VILLAGE").trim());

		final EditText yhsw_mcbchdc_sbr = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_sbr);
		yhsw_mcbchdc_sbr.setText(map.get("SBR").trim());

		final Button yhsw_mcbchdc_sbsj = (Button) dialog
				.findViewById(R.id.yhsw_mcbchdc_sbsj);
		yhsw_mcbchdc_sbsj.setText(map.get("SBSJ").trim());
		yhsw_mcbchdc_sbsj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_mcbchdc_sbsj, false);
			}
		});

		final TextView yhsw_mcbchdc_sbzt = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_sbzt);
		if ("1".equals(map.get("SCZT").trim())) {
			yhsw_mcbchdc_sbzt.setText(context.getResources().getString(
					R.string.haveupload));
		} else if ("0".equals(map.get("SCZT").trim())) {
			yhsw_mcbchdc_sbzt.setText(context.getResources().getString(
					R.string.havenotupload));
		} else {
			yhsw_mcbchdc_sbzt.setText("");
		}

		final EditText yhsw_mcbchdc_bz = (EditText) dialog
				.findViewById(R.id.yhsw_mcbchdc_bz);
		yhsw_mcbchdc_bz.setText(map.get("BZ").trim());

		Button upload = (Button) dialog.findViewById(R.id.yhsw_mcbchdc_upload);
		upload.setText(context.getResources().getString(R.string.bc));
		Button bdsave = (Button) dialog.findViewById(R.id.yhsw_mcbchdc_bdsave);
		bdsave.setVisibility(View.GONE);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String mcbchbh = yhsw_mcbchdc_mcbchbh.getText().toString().trim();
				if (TextUtils.isEmpty(mcbchbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.mcbchbhnotnull));
					return;
				}
				String dcr = yhsw_mcbchdc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_mcbchdc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_mcbchdc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String bdcdw = yhsw_mcbchdc_bdcdw.getText().toString().trim();
				if (TextUtils.isEmpty(bdcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bdcdw_nonull));
					return;
				}
				String dcdjd = yhsw_mcbchdc_dcdjd.getText().toString().trim();
				if (TextUtils.isEmpty(dcdjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdjdnotnull));
					return;
				}
				String dcdwd = yhsw_mcbchdc_dcdwd.getText().toString().trim();
				if (TextUtils.isEmpty(dcdwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwdnotnull));
					return;
				}
				String zyjgcp = yhsw_mcbchdc_zyjgcp.getText().toString().trim();
				if (TextUtils.isEmpty(zyjgcp)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zyjgcpnotnull));
					return;
				}
				String xdm = yhsw_mcbchdc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String xykcmcpz = yhsw_mcbchdc_xykcmcpz.getText().toString().trim();
				if (TextUtils.isEmpty(xykcmcpz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xykcmcpznotnull));
					return;
				}
				String zyjgjysz = yhsw_mcbchdc_zyjgjysz.getText().toString().trim();
				if (TextUtils.isEmpty(zyjgjysz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zyjgjysznotnull));
					return;
				}
				String ccmcsl = yhsw_mcbchdc_ccmcsl.getText().toString().trim();
				if (TextUtils.isEmpty(ccmcsl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ccmcslnotnull));
					return;
				}
				String kcl = yhsw_mcbchdc_kcl.getText().toString().trim();
				if (TextUtils.isEmpty(kcl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.kclnotnull));
					return;
				}
				String ccmcpz = yhsw_mcbchdc_ccmcpz.getText().toString().trim();
				if (TextUtils.isEmpty(ccmcpz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ccmcpznotnull));
					return;
				}
				String bcmc = yhsw_mcbchdc_bcmc.getText().toString().trim();
				if (TextUtils.isEmpty(bcmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bcmcnotnull));
					return;
				}
				String whcd = yhsw_mcbchdc_whcd.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whcd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whcdnotnull));
					return;
				}
				String whbw = yhsw_mcbchdc_whbw.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whbw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whbwnotnull));
					return;
				}
				String ct = yhsw_mcbchdc_ct.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(ct)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ctnotnull));
					return;
				}
				String city = yhsw_mcbchdc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_mcbchdc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_mcbchdc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_mcbchdc_village.getText().toString().trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String sbr = yhsw_mcbchdc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_mcbchdc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String bz = yhsw_mcbchdc_bz.getText().toString().trim();
				DataBaseHelper.updateYhswMcbchdcData(context, "db.sqlite", mcbchbh, dcr, dcsj, dcdw, bdcdw, dcdjd, dcdwd, zyjgcp, xdm, xykcmcpz, zyjgjysz, ccmcsl, kcl, ccmcpz, bcmc, whcd, whbw, ct, city, county, town, village, bz, "0", sbr, sbsj);
				map.put("DCR", dcr);
				map.put("DCSJ", dcsj);
				map.put("DCDW", dcdw);
				map.put("BDCDW", bdcdw);
				map.put("DCDJD", dcdjd);
				map.put("DCDWD", dcdwd);
				map.put("ZYJGCP", zyjgcp);
				map.put("XDM", xdm);
				map.put("XYKCMCPZ", xykcmcpz);
				map.put("ZYJGJYSZ", zyjgjysz);
				map.put("CCMCSL", ccmcsl);
				map.put("CCL", kcl);
				map.put("CCMCPZ", ccmcpz);
				map.put("BCMC", bcmc);
				map.put("WHCD", whcd);
				map.put("WHBW", whbw);
				map.put("CT", ct);
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
		Button cancle = (Button) dialog.findViewById(R.id.yhsw_mcbchdc_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 1, 1);
	}

	/* 查看 */
	protected void showCkeckDialog(HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_mcbchdc_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView mcbchbh = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_mcbchbh);
		mcbchbh.setText(map.get("MCBCHBH").trim());

		TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_dcr);
		dcr.setText(map.get("DCR").trim());

		TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_dcrq);
		dcsj.setText(map.get("DCSJ").replace("/", "-"));

		TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_dcdw);
		dcdw.setText(map.get("DCDW").trim());

		TextView bdcdw = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_bdcdw);
		bdcdw.setText(map.get("BDCDW").trim());

		TextView zyjgcp = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_zyjgcp);
		zyjgcp.setText(map.get("ZYJGCP").trim());

		TextView dcdjd = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcdjd);
		dcdjd.setText(map.get("DCDJD").trim());

		TextView dcdwd = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_dcdwd);
		dcdwd.setText(map.get("DCDWD").trim());

		TextView xdm = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_xdm);
		xdm.setText(map.get("XDM").trim());

		TextView xykcmcpz = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_xykcmcpz);
		xykcmcpz.setText(map.get("XYKCMCPZ").trim());

		TextView zyjgjysz = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_zyjgjysz);
		zyjgjysz.setText(map.get("ZYJGJYSZ").trim());

		TextView ccmcsl = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_ccmcsl);
		ccmcsl.setText(map.get("CCMCSL").trim());

		TextView kcl = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_kcl);
		kcl.setText(map.get("CCL").trim());

		TextView ccmcpz = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_ccmcpz);
		ccmcpz.setText(map.get("CCMCPZ").trim());

		TextView bcmc = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_bcmc);
		bcmc.setText(map.get("BCMC").trim());

		TextView whcd = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_whcd);
		if (BussUtil.isEmperty(map.get("WHCD").trim())) {
			int a = Integer.parseInt(map.get("WHCD").trim());
			String[] array = context.getResources().getStringArray(
					R.array.mcwhcd);
			whcd.setText(array[a]);
		}

		TextView whbw = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_whbw);
		if (BussUtil.isEmperty(map.get("WHBW").toString())) {
			int a = Integer.parseInt(map.get("WHBW").trim());
			String[] array = context.getResources().getStringArray(R.array.whbw);
			whbw.setText(array[a]);
		}

		TextView ct = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_ct);
		if (BussUtil.isEmperty(map.get("CT").trim())) {
			int a = Integer.parseInt(map.get("CT").trim());
			String[] array = context.getResources().getStringArray(R.array.ct);
			ct.setText(array[a]);
		}

		TextView city = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_city);
		city.setText(map.get("CITY").trim());

		TextView county = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_county);
		county.setText(map.get("COUNTY").trim());

		TextView town = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_town);
		town.setText(map.get("TOWN").trim());

		TextView village = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_village);
		village.setText(map.get("VILLAGE").trim());

		TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_sbr);
		sbr.setText(map.get("SBR").trim());

		TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_sbsj);
		sbsj.setText(map.get("SBSJ").replace("/", "-"));


		View status = dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		TextView sbzt = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_sbzt);
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
		TextView bz = (TextView) dialog.findViewById(R.id.yhsw_mcbchdc_bz);
		bz.setText(map.get("BZ").trim());

		Button back = (Button) dialog.findViewById(R.id.yhsw_mcbchdc_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		BussUtil.setDialogParams(context,dialog, 1, 1);
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
