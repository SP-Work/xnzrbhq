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

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.YHSWActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ToastUtil;

/**
 * 添加踏查路线信息
 */
public class YhswLxtcDialog extends Dialog {
	Context context;
	YHSWActivity activity;

	public YhswLxtcDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		this.activity = (YHSWActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.yhsw_lxtc_edit);
		setCanceledOnTouchOutside(false);
		Button upload = (Button) findViewById(R.id.yhsw_tclx_upload);
		Button bdsave = (Button) findViewById(R.id.yhsw_tclx_bdsave);

		final EditText yhsw_tclx_tcr = (EditText) findViewById(R.id.yhsw_tclx_tcr);
		final Button yhsw_tclx_tcrq = (Button) findViewById(R.id.yhsw_tclx_tcrq);
		final EditText yhsw_tclx_tcdw = (EditText) findViewById(R.id.yhsw_tclx_tcdw);
		final EditText yhsw_tclx_tcdbmj = (EditText) findViewById(R.id.yhsw_tclx_tcdbmj);
		final EditText yhsw_tclx_yhswmc = (EditText) findViewById(R.id.yhsw_tclx_yhswmc);
		final EditText yhsw_tclx_jzmc = (EditText) findViewById(R.id.yhsw_tclx_jzmc);
		final Spinner yhsw_tclx_lfzc = (Spinner) findViewById(R.id.yhsw_tclx_lfzc);
		ArrayAdapter lfzcadapter = new ArrayAdapter(context,
				R.layout.myspinner, context.getResources().getStringArray(
				R.array.lfzc));
		yhsw_tclx_lfzc.setAdapter(lfzcadapter);
		final EditText yhsw_tclx_city = (EditText) findViewById(R.id.yhsw_tclx_city);
		final EditText yhsw_tclx_county = (EditText) findViewById(R.id.yhsw_tclx_county);
		final EditText yhsw_tclx_town = (EditText) findViewById(R.id.yhsw_tclx_town);
		final EditText yhsw_tclx_village = (EditText) findViewById(R.id.yhsw_tclx_village);
		final EditText yhsw_tclx_sbr = (EditText) findViewById(R.id.yhsw_tclx_sbr);
		final Button yhsw_tclx_sbsj = (Button) findViewById(R.id.yhsw_tclx_sbsj);
		final EditText yhsw_tclx_bz = (EditText) findViewById(R.id.yhsw_tclx_bz);
		yhsw_tclx_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tclx_sbsj, false);
			}

		});
		yhsw_tclx_tcrq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tclx_tcrq, false);
			}

		});

		// 在线上传
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("".equals(activity.lxtcid)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.addtclxfailed));
				} else {
					String tcr = yhsw_tclx_tcr.getText().toString();
					if (TextUtils.isEmpty(tcr)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcrnotnull));
						return;
					}
					String tcrq = yhsw_tclx_tcrq.getText().toString().trim();
					if (TextUtils.isEmpty(tcrq)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcrqnotnull));
						return;
					}
					String tcdw = yhsw_tclx_tcdw.getText().toString().trim();
					if (TextUtils.isEmpty(tcrq)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcdwnotnull));
						return;
					}
					String tcdbmj = yhsw_tclx_tcdbmj.getText().toString()
							.trim();
					if (TextUtils.isEmpty(tcdbmj)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcdbmjnotnull));
						return;
					}

					String yhswmc = yhsw_tclx_yhswmc.getText().toString()
							.trim();
					if (TextUtils.isEmpty(yhswmc)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.yhswmcnotnull));
						return;
					}

					String jzmc = yhsw_tclx_jzmc.getText().toString().trim();
					String lfzc = yhsw_tclx_lfzc.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(lfzc)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.lfzcnotnull));
						return;
					}

					String city = yhsw_tclx_city.getText().toString().trim();
					if (TextUtils.isEmpty(city)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.citynotnull));
						return;
					}
					String county = yhsw_tclx_county.getText().toString()
							.trim();
					if (TextUtils.isEmpty(county)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.countynotnull));
						return;
					}
					String town = yhsw_tclx_town.getText().toString().trim();
					if (TextUtils.isEmpty(town)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.townnotnull));
						return;
					}
					String village = yhsw_tclx_village.getText().toString()
							.trim();
					if (TextUtils.isEmpty(village)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.villagenotnull));
						return;
					}
					String sbr = yhsw_tclx_sbr.getText().toString()
							.trim();
					if (TextUtils.isEmpty(sbr)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.sbrnotnull));
						return;
					}
					String sbsj = yhsw_tclx_sbsj.getText().toString()
							.trim();
					if (TextUtils.isEmpty(sbsj)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.sbaosjnotnull));
						return;
					}
					String bz = yhsw_tclx_bz.getText().toString().trim();
					String zdjd = activity.currentPoint.getX() + "";
					String zdwd = activity.currentPoint.getY() + "";
					Webservice web = new Webservice(context);
					String result = web.updateYhswlxtcData(activity.lxtcid,
							tcr, tcrq, tcdw, tcdbmj, zdjd, zdwd, yhswmc, jzmc,
							lfzc, city, county, town, village, bz, "1",sbr,sbsj);
					String[] splits = result.split(",");
					if (splits.length > 0) {
						if ("true".equals(splits[0])) {
							activity.tcqd.setVisibility(View.VISIBLE);
							activity.qxtc.setVisibility(View.VISIBLE);
							activity.zjtcd.setVisibility(View.GONE);
							activity.jstc.setVisibility(View.GONE);
							DataBaseHelper.deleteYhswTclx2Data(context, "db.sqlite", activity.lxtcid);
							ToastUtil.setToast(context, context.getResources()
									.getString(R.string.addsuccess));
							dismiss();
						} else {
							ToastUtil.setToast(context, context.getResources()
									.getString(R.string.addtclxfailed));
						}
					} else {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.addtclxfailed));
					}

				}
			}
		});
		// 本地保存
		bdsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("".equals(activity.lxtcid)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.addtclxfailed));
				} else {
					String tcr = yhsw_tclx_tcr.getText().toString();
					if (TextUtils.isEmpty(tcr)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcrnotnull));
						return;
					}
					String tcrq = yhsw_tclx_tcrq.getText().toString().trim();
					if (TextUtils.isEmpty(tcrq)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcrqnotnull));
						return;
					}
					String tcdw = yhsw_tclx_tcdw.getText().toString().trim();
					if (TextUtils.isEmpty(tcrq)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcdwnotnull));
						return;
					}
					String tcdbmj = yhsw_tclx_tcdbmj.getText().toString()
							.trim();
					if (TextUtils.isEmpty(tcdbmj)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.tcdbmjnotnull));
						return;
					}

					String yhswmc = yhsw_tclx_yhswmc.getText().toString()
							.trim();
					if (TextUtils.isEmpty(yhswmc)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.yhswmcnotnull));
						return;
					}

					String jzmc = yhsw_tclx_jzmc.getText().toString().trim();
					String lfzc = yhsw_tclx_lfzc.getSelectedItemPosition()+"";
					if (TextUtils.isEmpty(lfzc)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.lfzcnotnull));
						return;
					}

					String city = yhsw_tclx_city.getText().toString().trim();
					if (TextUtils.isEmpty(city)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.citynotnull));
						return;
					}
					String county = yhsw_tclx_county.getText().toString()
							.trim();
					if (TextUtils.isEmpty(county)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.countynotnull));
						return;
					}
					String town = yhsw_tclx_town.getText().toString().trim();
					if (TextUtils.isEmpty(town)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.townnotnull));
						return;
					}
					String village = yhsw_tclx_village.getText().toString()
							.trim();
					if (TextUtils.isEmpty(village)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.villagenotnull));
						return;
					}
					String sbr = yhsw_tclx_sbr.getText().toString()
							.trim();
					if (TextUtils.isEmpty(sbr)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.sbrnotnull));
						return;
					}
					String sbsj = yhsw_tclx_sbsj.getText().toString()
							.trim();
					if (TextUtils.isEmpty(sbsj)) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.sbaosjnotnull));
						return;
					}
					String bz = yhsw_tclx_bz.getText().toString().trim();
					String zdjd = activity.currentPoint.getX() + "";
					String zdwd = activity.currentPoint.getY() + "";
					DataBaseHelper.updateYhswTclxData(context, "db.sqlite", activity.lxtcid, tcr, tcrq, tcdw, tcdbmj, zdjd, zdwd, yhswmc, jzmc, lfzc, city, county, town, village, bz,sbr,sbsj);
					Webservice web=new Webservice(context);
					web.deleteYhswTclxData(activity.lxtcid);
					activity.tcqd.setVisibility(View.VISIBLE);
					activity.qxtc.setVisibility(View.VISIBLE);
					activity.zjtcd.setVisibility(View.GONE);
					activity.jstc.setVisibility(View.GONE);
					dismiss();
				}
			}
		});
		Button cancle = (Button) findViewById(R.id.yhsw_tclx_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
}
