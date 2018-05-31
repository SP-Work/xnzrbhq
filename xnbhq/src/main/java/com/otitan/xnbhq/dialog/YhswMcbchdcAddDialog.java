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

import com.esri.core.geometry.Point;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.YHSWActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.UtilTime;

/**
 * 添加木材病虫害调查信息
 */
public class YhswMcbchdcAddDialog extends Dialog {
	Context context;
	YHSWActivity activity;
	private Point currentpoint;

	public YhswMcbchdcAddDialog(Context context, int theme,Point point) {
		super(context, theme);
		this.context = context;
		this.activity = (YHSWActivity) context;
		this.currentpoint=point;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		setContentView(R.layout.yhsw_mcbchdc_add);
		setCanceledOnTouchOutside(false);
		View status=findViewById(R.id.status);
		status.setVisibility(View.GONE);
		Button cancle = (Button) findViewById(R.id.yhsw_mcbchdc_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		String mcbchbh = UtilTime.getSystemtime1();
		final TextView yhsw_mcbchdc_mcbchbh = (TextView) findViewById(R.id.yhsw_mcbchdc_mcbchbh);
		yhsw_mcbchdc_mcbchbh.setText(mcbchbh);
		final TextView yhsw_mcbchdc_dcrq = (TextView) findViewById(R.id.yhsw_mcbchdc_dcrq);
		yhsw_mcbchdc_dcrq.setText(UtilTime.getSystemtime2());
		final EditText yhsw_mcbchdc_dcr = (EditText) findViewById(R.id.yhsw_mcbchdc_dcr);
		final EditText yhsw_mcbchdc_dcdw = (EditText) findViewById(R.id.yhsw_mcbchdc_dcdw);
		final EditText yhsw_mcbchdc_bdcdw = (EditText) findViewById(R.id.yhsw_mcbchdc_bdcdw);
		final EditText yhsw_mcbchdc_zyjgcp = (EditText) findViewById(R.id.yhsw_mcbchdc_zyjgcp);
		final EditText yhsw_mcbchdc_dcdjd = (EditText) findViewById(R.id.yhsw_mcbchdc_dcdjd);
		final EditText yhsw_mcbchdc_dcdwd = (EditText) findViewById(R.id.yhsw_mcbchdc_dcdwd);
		final EditText yhsw_mcbchdc_xdm = (EditText) findViewById(R.id.yhsw_mcbchdc_xdm);
		final EditText yhsw_mcbchdc_xykcmcpz = (EditText) findViewById(R.id.yhsw_mcbchdc_xykcmcpz);
		final EditText yhsw_mcbchdc_zyjgjysz = (EditText) findViewById(R.id.yhsw_mcbchdc_zyjgjysz);
		final EditText yhsw_mcbchdc_ccmcsl = (EditText) findViewById(R.id.yhsw_mcbchdc_ccmcsl);
		final EditText yhsw_mcbchdc_kcl = (EditText) findViewById(R.id.yhsw_mcbchdc_kcl);
		final EditText yhsw_mcbchdc_ccmcpz = (EditText) findViewById(R.id.yhsw_mcbchdc_ccmcpz);
		final EditText yhsw_mcbchdc_bcmc = (EditText) findViewById(R.id.yhsw_mcbchdc_bcmc);

		final Spinner yhsw_mcbchdc_whcd = (Spinner) findViewById(R.id.yhsw_mcbchdc_whcd);

		final Spinner yhsw_mcbchdc_whbw = (Spinner) findViewById(R.id.yhsw_mcbchdc_whbw);
		ArrayAdapter whbwadapter=new ArrayAdapter(context, R.layout.myspinner, context.getResources().getStringArray(R.array.whbw));
		yhsw_mcbchdc_whbw.setAdapter(whbwadapter);

		final Spinner yhsw_mcbchdc_ct = (Spinner) findViewById(R.id.yhsw_mcbchdc_ct);
		ArrayAdapter ctadapter=new ArrayAdapter(context, R.layout.myspinner, context.getResources().getStringArray(R.array.ct));
		yhsw_mcbchdc_ct.setAdapter(ctadapter);

		final EditText yhsw_mcbchdc_city = (EditText) findViewById(R.id.yhsw_mcbchdc_city);
		final EditText yhsw_mcbchdc_county = (EditText) findViewById(R.id.yhsw_mcbchdc_county);
		final EditText yhsw_mcbchdc_town = (EditText) findViewById(R.id.yhsw_mcbchdc_town);
		final EditText yhsw_mcbchdc_village = (EditText) findViewById(R.id.yhsw_mcbchdc_village);
		final EditText yhsw_mcbchdc_sbr = (EditText) findViewById(R.id.yhsw_mcbchdc_sbr);
		final Button yhsw_mcbchdc_sbsj=(Button) findViewById(R.id.yhsw_mcbchdc_sbsj);
		final EditText yhsw_mcbchdc_bz = (EditText) findViewById(R.id.yhsw_mcbchdc_bz);
		if(currentpoint!=null){
			yhsw_mcbchdc_dcdjd.setText(currentpoint.getX()+"");
			yhsw_mcbchdc_dcdwd.setText(currentpoint.getY()+"");
		}
		yhsw_mcbchdc_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_mcbchdc_sbsj, false);
			}
		});

		//在线上报
		Button upload = (Button) findViewById(R.id.yhsw_mcbchdc_upload);
		upload.setOnClickListener(new View.OnClickListener() {

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
							.getString(R.string.bdcdw_nonull));
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
				Webservice web = new Webservice(context);
				String result = web.addYhswMcbchdcData(mcbchbh, dcr, dcsj, dcdw, bdcdw, dcdjd, dcdwd, zyjgcp, xdm, xykcmcpz, zyjgjysz, ccmcsl, kcl, ccmcpz, bcmc, whcd, whbw, ct, city, county, town, village, bz, "1", sbr, sbsj);
				String[] splits = result.split(",");
				if (splits.length > 0) {
					if ("True".equals(splits[0])) {
						ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
						dismiss();
					}else{
						ToastUtil.setToast(context, context.getResources().getString(R.string.addfailed));
					}
				}else{
					ToastUtil.setToast(context, context.getResources().getString(R.string.addfailed));
				}
			}
		});

		//本地保存
		Button bdsave = (Button) findViewById(R.id.yhsw_mcbchdc_bdsave);
		bdsave.setOnClickListener(new View.OnClickListener() {

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
				DataBaseHelper.addYhswMcbchdcData(context, "db.sqlite", mcbchbh, dcr, dcsj, dcdw, bdcdw, dcdjd, dcdwd, zyjgcp, xdm, xykcmcpz, zyjgjysz, ccmcsl, kcl, ccmcpz, bcmc, whcd, whbw, ct, city, county, town, village, bz, "0", sbr, sbsj);
				ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
				dismiss();
			}
		});

	}
}
