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
 * 添加诱虫灯信息
 */
public class YhswYcddcAddDialog extends Dialog {
	Context context;
	YHSWActivity activity;
	private Point currentpoint;

	public YhswYcddcAddDialog(Context context, int theme,Point point) {
		super(context, theme);
		this.context = context;
		this.activity = (YHSWActivity) context;
		this.currentpoint=point;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.yhsw_ycddc_add);
		setCanceledOnTouchOutside(false);
		View status=findViewById(R.id.status);
		status.setVisibility(View.INVISIBLE);
		Button cancle = (Button) findViewById(R.id.yhsw_ycddc_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		String ydbh = UtilTime.getSystemtime1();
		final TextView yhsw_ycddc_ycdbh = (TextView) findViewById(R.id.yhsw_ycddc_ycdbh);
		yhsw_ycddc_ycdbh.setText(ydbh);
		final TextView yhsw_ycddc_dcrq = (TextView) findViewById(R.id.yhsw_ycddc_dcrq);
		yhsw_ycddc_dcrq.setText(UtilTime.getSystemtime2());
		final EditText yhsw_ycddc_dcr = (EditText) findViewById(R.id.yhsw_ycddc_dcr);
		final EditText yhsw_ycddc_dcdw = (EditText) findViewById(R.id.yhsw_ycddc_dcdw);
		final EditText yhsw_ycddc_ycdmc = (EditText) findViewById(R.id.yhsw_ycddc_ycdmc);
		final EditText yhsw_ycddc_hb = (EditText) findViewById(R.id.yhsw_ycddc_hb);
		final EditText yhsw_ycddc_xdm = (EditText) findViewById(R.id.yhsw_ycddc_xdm);
		final EditText yhsw_ycddc_xbh = (EditText) findViewById(R.id.yhsw_ycddc_xbh);

		final EditText yhsw_ycddc_dcdjd = (EditText) findViewById(R.id.yhsw_ycddc_dcdjd);
		final EditText yhsw_ycddc_dcdwd = (EditText) findViewById(R.id.yhsw_ycddc_dcdwd);
		if(currentpoint!=null){
			yhsw_ycddc_dcdjd.setText(currentpoint.getX()+"");
			yhsw_ycddc_dcdwd.setText(currentpoint.getY()+"");
		}

		final EditText yhsw_ycddc_xbmj = (EditText) findViewById(R.id.yhsw_ycddc_xbmj);
		final EditText yhsw_ycddc_zyhcmc = (EditText) findViewById(R.id.yhsw_ycddc_zyhcmc);

		final Spinner yhsw_ycddc_lfzc = (Spinner) findViewById(R.id.yhsw_ycddc_lfzc);
		ArrayAdapter lfzcadapter=new ArrayAdapter(context, R.layout.myspinner, context.getResources().getStringArray(R.array.lfzc));
		yhsw_ycddc_lfzc.setAdapter(lfzcadapter);

		final EditText yhsw_ycddc_hcsl = (EditText) findViewById(R.id.yhsw_ycddc_hcsl);
		final EditText yhsw_ycddc_ybd = (EditText) findViewById(R.id.yhsw_ycddc_ybd);
		final EditText yhsw_ycddc_pjg = (EditText) findViewById(R.id.yhsw_ycddc_pjg);
		final EditText yhsw_ycddc_pjxj = (EditText) findViewById(R.id.yhsw_ycddc_pjxj);
		final EditText yhsw_ycddc_city = (EditText) findViewById(R.id.yhsw_ycddc_city);
		final EditText yhsw_ycddc_county = (EditText) findViewById(R.id.yhsw_ycddc_county);
		final EditText yhsw_ycddc_town = (EditText) findViewById(R.id.yhsw_ycddc_town);
		final EditText yhsw_ycddc_village = (EditText) findViewById(R.id.yhsw_ycddc_village);
		final EditText yhsw_ycddc_sbr = (EditText) findViewById(R.id.yhsw_ycddc_sbr);

		final Button yhsw_ycddc_sbsj = (Button) findViewById(R.id.yhsw_ycddc_sbsj);
		yhsw_ycddc_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_ycddc_sbsj, false);
			}
		});

		final EditText yhsw_ycddc_bz = (EditText) findViewById(R.id.yhsw_ycddc_bz);
		//在线上报
		Button upload = (Button) findViewById(R.id.yhsw_ycddc_upload);
		upload.setOnClickListener(new View.OnClickListener() {

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

				Webservice web = new Webservice(context);
				String result = web.addYhswYcddcData(ycdbh, ycdmc, dcr, dcdw, dcsj, dcdjd, dcdwd, hb, xdm, xbh, xbmj, zyhcmc, lfzc, hcsl, ybd, pjg, pjxj, city, county, town, village, bz, "1", sbr, sbsj);
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
//		//本地保存
		Button bdsave = (Button) findViewById(R.id.yhsw_ycddc_bdsave);
		bdsave.setOnClickListener(new View.OnClickListener() {

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
				DataBaseHelper.addYhswYcddcData(context, "db.sqlite", ycdbh, ycdmc, dcr, dcdw, dcsj, dcdjd, dcdwd, hb, xdm, xbh, xbmj, zyhcmc, lfzc, hcsl, ybd, pjg, pjxj, city, county, town, village, bz, "0", sbr, sbsj);
				ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
				dismiss();
			}
		});

	}
}
