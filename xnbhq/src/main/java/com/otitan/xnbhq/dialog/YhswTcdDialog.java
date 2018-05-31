package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.YHSWActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.UtilTime;

/**
 * 添加踏查点信息
 */
public class YhswTcdDialog extends Dialog {
	Context mContext;
	YHSWActivity activity;
	private String flag;

	public YhswTcdDialog(Context context, int theme, String flag) {
		super(context, theme);
		this.mContext = context;
		this.activity = (YHSWActivity) context;
		this.flag = flag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.yhsw_yhswtc_add);
		setCanceledOnTouchOutside(false);

		Button upload = (Button) findViewById(R.id.yhsw_tcd_upload);
		String dcdbh = UtilTime.getSystemtime1();
		final TextView yhsw_tcd_dcdbh = (TextView) findViewById(R.id.yhsw_tcd_dcdbh);
		yhsw_tcd_dcdbh.setText(dcdbh);

		final TextView yhsw_tcd_dclxbh = (TextView) findViewById(R.id.yhsw_tcd_dclxbh);
		yhsw_tcd_dclxbh.setText(activity.lxtcid);

		final EditText yhsw_tcd_dcr = (EditText) findViewById(R.id.yhsw_tcd_dcr);
		final Button yhsw_tcd_dcrq = (Button) findViewById(R.id.yhsw_tcd_dcrq);
		final EditText yhsw_tcd_dcdw = (EditText) findViewById(R.id.yhsw_tcd_dcdw);
		final EditText yhsw_tcd_xdm = (EditText) findViewById(R.id.yhsw_tcd_xdm);
		final TextView yhsw_tcd_lon = (TextView) findViewById(R.id.yhsw_tcd_lon);
		final TextView yhsw_tcd_lat = (TextView) findViewById(R.id.yhsw_tcd_lat);
		final EditText yhsw_tcd_hb = (EditText) findViewById(R.id.yhsw_tcd_hb);
		final EditText yhsw_tcd_xbh = (EditText) findViewById(R.id.yhsw_tcd_xbh);
		final EditText yhsw_tcd_xbmj = (EditText) findViewById(R.id.yhsw_tcd_xbmj);
		final EditText yhsw_tcd_jzmc = (EditText) findViewById(R.id.yhsw_tcd_jzmc);
		final Spinner yhsw_tcd_cbtj = (Spinner) findViewById(R.id.yhsw_tcd_cbtj);
		final Spinner yhsw_tcd_lfzc = (Spinner) findViewById(R.id.yhsw_tcd_lfzc);
		final EditText yhsw_tcd_yhswmc = (EditText) findViewById(R.id.yhsw_tcd_yhswmc);
		final Spinner yhsw_tcd_whbw = (Spinner) findViewById(R.id.yhsw_tcd_whbw);
		final Spinner yhsw_tcd_mcwhcd = (Spinner) findViewById(R.id.yhsw_tcd_mcwhcd);
		final Spinner yhsw_tcd_ct = (Spinner) findViewById(R.id.yhsw_tcd_ct);
		final EditText yhsw_tcd_mcwhmj = (EditText) findViewById(R.id.yhsw_tcd_mcwhmj);
		final Spinner yhsw_tcd_source = (Spinner) findViewById(R.id.yhsw_tcd_source);
		final EditText yhsw_tcd_bz = (EditText) findViewById(R.id.yhsw_tcd_bz);
		final EditText yhsw_tcd_city = (EditText) findViewById(R.id.yhsw_tcd_city);
		final EditText yhsw_tcd_county = (EditText) findViewById(R.id.yhsw_tcd_county);
		final EditText yhsw_tcd_town = (EditText) findViewById(R.id.yhsw_tcd_town);
		final EditText yhsw_tcd_village = (EditText) findViewById(R.id.yhsw_tcd_village);
		final TextView yhsw_tcd_uploadstatus = (TextView) findViewById(R.id.yhsw_tcd_uploadstatus);
		final EditText yhsw_tcd_sbr = (EditText) findViewById(R.id.yhsw_tcd_sbr);
		final Button yhsw_tcd_sbsj = (Button) findViewById(R.id.yhsw_tcd_sbsj);

		yhsw_tcd_dcrq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tcd_dcrq, false);
			}

		});
		yhsw_tcd_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tcd_sbsj, false);
			}
		});
		ArrayAdapter cbtjAdapter = new ArrayAdapter(mContext,
				R.layout.myspinner, mContext.getResources().getStringArray(
				R.array.cbtj));
		yhsw_tcd_cbtj.setAdapter(cbtjAdapter);
		ArrayAdapter whbwAdapter = new ArrayAdapter(mContext,
				R.layout.myspinner, mContext.getResources().getStringArray(
				R.array.whbw));
		yhsw_tcd_whbw.setAdapter(whbwAdapter);
		ArrayAdapter mcwhcdAdapter = new ArrayAdapter(mContext,
				R.layout.myspinner, mContext.getResources().getStringArray(
				R.array.mcwhcd));
		yhsw_tcd_mcwhcd.setAdapter(mcwhcdAdapter);
		ArrayAdapter ctAdapter = new ArrayAdapter(mContext, R.layout.myspinner,
				mContext.getResources().getStringArray(R.array.ct));
		yhsw_tcd_ct.setAdapter(ctAdapter);
		ArrayAdapter lyAdapter = new ArrayAdapter(mContext, R.layout.myspinner,
				mContext.getResources().getStringArray(R.array.ly));
		yhsw_tcd_source.setAdapter(lyAdapter);
		ArrayAdapter lfzcadapter = new ArrayAdapter(mContext,
				R.layout.myspinner, mContext.getResources().getStringArray(
				R.array.lfzc));
		yhsw_tcd_lfzc.setAdapter(lfzcadapter);
		if (activity.currentPoint != null) {
			yhsw_tcd_lon.setText(activity.currentPoint.getX() + "");
			yhsw_tcd_lat.setText(activity.currentPoint.getY() + "");
		}
		// 在线上传
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String lxtcbh = yhsw_tcd_dclxbh.getText().toString().trim();
				if ("".equals(lxtcbh)) {
					ToastUtil.setToast(mContext, mContext.getResources()
							.getString(R.string.addfailed));
				} else {
					String dcr = yhsw_tcd_dcr.getText().toString().trim();
					if (TextUtils.isEmpty(dcr)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.dcrnotnull));
						return;
					}
					String dcdbh = yhsw_tcd_dcdbh.getText().toString().trim();
					String dcdw = yhsw_tcd_dcdw.getText().toString().trim();
					if (TextUtils.isEmpty(dcdw)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.dcdwnotnull));
						return;
					}
					String xdm = yhsw_tcd_xdm.getText().toString().trim();
					if (TextUtils.isEmpty(xdm)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.xdmnotnull));
						return;
					}
					String lon = yhsw_tcd_lon.getText().toString().trim();
					if (TextUtils.isEmpty(lon)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.jdnotnull));
						return;
					}
					String lat = yhsw_tcd_lat.getText().toString().trim();
					if (TextUtils.isEmpty(lat)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.jdnotnull));
						return;
					}
					String xbh = yhsw_tcd_xbh.getText().toString().trim();
					if (TextUtils.isEmpty(xbh)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.xbhnotnull));
						return;
					}
					String xbmj = yhsw_tcd_xbmj.getText().toString().trim();
					if (TextUtils.isEmpty(xbmj)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.xbmjnotnull));
						return;
					}
					String jzmc = yhsw_tcd_jzmc.getText().toString().trim();
					if (TextUtils.isEmpty(jzmc)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.jzmcnotnull));
						return;
					}
					String cbtj = yhsw_tcd_cbtj.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(cbtj)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.cbtjnotnull));
						return;
					}
					String lfzc = yhsw_tcd_lfzc.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(lfzc)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.lfzcnotnull));
						return;
					}
					String yhswmc = yhsw_tcd_yhswmc.getText().toString().trim();
					if (TextUtils.isEmpty(yhswmc)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.yhswmcnotnull));
						return;
					}
					String whbw = yhsw_tcd_whbw.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(whbw)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.whbwnotnull));
						return;
					}
					String mcwhcd = yhsw_tcd_mcwhcd.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(mcwhcd)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.mcwhcdnotnull));
						return;
					}
					String ct = yhsw_tcd_ct.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(ct)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.ctnotnull));
						return;
					}
					String mcwhmj = yhsw_tcd_mcwhmj.getText().toString().trim();
					if (TextUtils.isEmpty(mcwhmj)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.mcwhmjnotnull));
						return;
					}
					String city = yhsw_tcd_city.getText().toString().trim();
					if (TextUtils.isEmpty(city)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.citynotnull));
						return;
					}
					String county = yhsw_tcd_county.getText().toString().trim();
					if (TextUtils.isEmpty(county)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.countynotnull));
						return;
					}
					String town = yhsw_tcd_town.getText().toString().trim();
					if (TextUtils.isEmpty(town)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.townnotnull));
						return;
					}
					String village = yhsw_tcd_village.getText().toString()
							.trim();
					if (TextUtils.isEmpty(village)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.villagenotnull));
						return;
					}
					String bz = yhsw_tcd_bz.getText().toString().trim();
					String source = yhsw_tcd_source.getSelectedItemPosition()+"";
					String hb = yhsw_tcd_hb.getText().toString().trim();
					String dcrq = yhsw_tcd_dcrq.getText().toString().trim();
					String sbr=yhsw_tcd_sbr.getText().toString();
					String sbsj=yhsw_tcd_sbsj.getText().toString();
					Webservice web = new Webservice(mContext);
					String result = web.addYhswTcdData(dcdbh, lxtcbh, dcr,
							dcrq, dcdw, lon, lat, hb, xdm, xbh, xbmj, jzmc,
							cbtj, lfzc, whbw, yhswmc, mcwhcd, ct, mcwhmj,
							source, bz, city, county, town, village, "1",sbr,sbsj);
					String[] splits = result.split(",");
					if (splits.length > 0) {
						if ("True".equals(splits[0])) {
							activity.tcqd.setVisibility(View.GONE);
							activity.qxtc.setVisibility(View.VISIBLE);
							activity.zjtcd.setVisibility(View.VISIBLE);
							activity.jstc.setVisibility(View.VISIBLE);
							ToastUtil.setToast(
									mContext,
									mContext.getResources().getString(
											R.string.zxsbsuccess));
							dismiss();
						} else {
							ToastUtil.setToast(
									mContext,
									mContext.getResources().getString(
											R.string.zxsbfailed));
						}
					} else {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.zxsbfailed));
					}
				}

				// }

			}
		});

		Button bdsave = (Button) findViewById(R.id.yhsw_tcd_bdsave);
		// 本地保存
		bdsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String lxtcbh = yhsw_tcd_dclxbh.getText().toString().trim();
				if ("".equals(lxtcbh)) {
					ToastUtil.setToast(mContext, mContext.getResources()
							.getString(R.string.addfailed));
				} else {
					String dcr = yhsw_tcd_dcr.getText().toString().trim();
					if (TextUtils.isEmpty(dcr)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.dcrnotnull));
						return;
					}
					String dcdbh = yhsw_tcd_dcdbh.getText().toString().trim();
					String dcdw = yhsw_tcd_dcdw.getText().toString().trim();
					if (TextUtils.isEmpty(dcdw)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.dcdwnotnull));
						return;
					}
					String xdm = yhsw_tcd_xdm.getText().toString().trim();
					if (TextUtils.isEmpty(xdm)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.xdmnotnull));
						return;
					}
					String lon = yhsw_tcd_lon.getText().toString().trim();
					if (TextUtils.isEmpty(lon)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.jdnotnull));
						return;
					}
					String lat = yhsw_tcd_lat.getText().toString().trim();
					if (TextUtils.isEmpty(lat)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.jdnotnull));
						return;
					}
					String xbh = yhsw_tcd_xbh.getText().toString().trim();
					if (TextUtils.isEmpty(xbh)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.xbhnotnull));
						return;
					}
					String xbmj = yhsw_tcd_xbmj.getText().toString().trim();
					if (TextUtils.isEmpty(xbmj)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.xbmjnotnull));
						return;
					}
					String jzmc = yhsw_tcd_jzmc.getText().toString().trim();
					if (TextUtils.isEmpty(jzmc)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.jzmcnotnull));
						return;
					}
					String cbtj = yhsw_tcd_cbtj.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(cbtj)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.cbtjnotnull));
						return;
					}
					String lfzc = yhsw_tcd_lfzc.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(lfzc)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.lfzcnotnull));
						return;
					}
					String yhswmc = yhsw_tcd_yhswmc.getText().toString().trim();
					if (TextUtils.isEmpty(yhswmc)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.yhswmcnotnull));
						return;
					}
					String whbw = yhsw_tcd_whbw.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(whbw)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.whbwnotnull));
						return;
					}
					String mcwhcd = yhsw_tcd_mcwhcd.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(mcwhcd)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.mcwhcdnotnull));
						return;
					}
					String ct = yhsw_tcd_ct.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(ct)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.ctnotnull));
						return;
					}
					String mcwhmj = yhsw_tcd_mcwhmj.getText().toString().trim();
					if (TextUtils.isEmpty(mcwhmj)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.mcwhmjnotnull));
						return;
					}
					String city = yhsw_tcd_city.getText().toString().trim();
					if (TextUtils.isEmpty(city)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.citynotnull));
						return;
					}
					String county = yhsw_tcd_county.getText().toString().trim();
					if (TextUtils.isEmpty(county)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.countynotnull));
						return;
					}
					String town = yhsw_tcd_town.getText().toString().trim();
					if (TextUtils.isEmpty(town)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.townnotnull));
						return;
					}
					String village = yhsw_tcd_village.getText().toString()
							.trim();
					if (TextUtils.isEmpty(village)) {
						ToastUtil.setToast(mContext, mContext.getResources()
								.getString(R.string.villagenotnull));
						return;
					}
					String uploadstatus = yhsw_tcd_uploadstatus.getText()
							.toString().trim();
					String bz = yhsw_tcd_bz.getText().toString().trim();
					String source = yhsw_tcd_source.getSelectedItemPosition()+"";
					String hb = yhsw_tcd_hb.getText().toString().trim();
					String dcrq = yhsw_tcd_dcrq.getText().toString().trim();
					String sbr=yhsw_tcd_sbr.getText().toString();
					String sbsj=yhsw_tcd_sbsj.getText().toString();
					DataBaseHelper.addYhswTcdData(mContext, "db.sqlite", dcdbh,
							lxtcbh, dcr, dcrq, dcdw, lon, lat, hb, xdm, xbh,
							xbmj, jzmc, cbtj, lfzc, whbw, yhswmc, mcwhcd, ct,
							mcwhmj, source, bz, city, county, town, village,sbr,sbsj);
					ToastUtil.setToast(mContext, mContext.getResources()
							.getString(R.string.bdsavesuccess));
					activity.tcqd.setVisibility(View.GONE);
					activity.qxtc.setVisibility(View.VISIBLE);
					activity.zjtcd.setVisibility(View.VISIBLE);
					activity.jstc.setVisibility(View.VISIBLE);
					dismiss();
				}
			}
		});
		Button cancle = (Button) findViewById(R.id.yhsw_tcd_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

	}
}
