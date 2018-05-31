package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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
 * 添加松材线虫病信息
 */
public class YhswScxcbpcAddDialog extends Dialog {
	Context context;
	YHSWActivity activity;
	private Point currentpoint;

	public YhswScxcbpcAddDialog(Context context, int theme,Point point) {
		super(context, theme);
		this.context = context;
		this.activity = (YHSWActivity) context;
		this.currentpoint=point;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.yhsw_scxcbpc_add);
		setCanceledOnTouchOutside(false);
		View status=findViewById(R.id.status);
		status.setVisibility(View.GONE);
		Button cancle = (Button) findViewById(R.id.yhsw_scxcbpc_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		String ydbh = UtilTime.getSystemtime1();
		final TextView yhsw_scxcbpc_ydbh = (TextView) findViewById(R.id.yhsw_scxcbpc_ydbh);
		yhsw_scxcbpc_ydbh.setText(ydbh);
		final TextView yhsw_scxcbpc_dcrq = (TextView) findViewById(R.id.yhsw_scxcbpc_dcrq);
		yhsw_scxcbpc_dcrq.setText(UtilTime.getSystemtime2());
		final EditText yhsw_scxcbpc_dcr = (EditText) findViewById(R.id.yhsw_scxcbpc_dcr);
		final EditText yhsw_scxcbpc_dcdw = (EditText) findViewById(R.id.yhsw_scxcbpc_dcdw);

		final EditText yhsw_scxcbpc_jd = (EditText) findViewById(R.id.yhsw_scxcbpc_jd);
		final EditText yhsw_scxcbpc_wd = (EditText) findViewById(R.id.yhsw_scxcbpc_wd);
		if(currentpoint!=null){
			yhsw_scxcbpc_jd.setText(currentpoint.getX()+"");
			yhsw_scxcbpc_wd.setText(currentpoint.getY()+"");
		}

		final EditText yhsw_scxcbpc_xbh = (EditText) findViewById(R.id.yhsw_scxcbpc_xbh);
		final EditText yhsw_scxcbpc_xbmj = (EditText) findViewById(R.id.yhsw_scxcbpc_xbmj);

		final Spinner yhsw_scxcbpc_whcd = (Spinner) findViewById(R.id.yhsw_scxcbpc_whcd);
		//ArrayAdapter whcdadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.mcwhcd));
		//yhsw_scxcbpc_whcd.setAdapter(whcdadapter);

		final EditText yhsw_scxcbpc_ksssmj = (EditText) findViewById(R.id.yhsw_scxcbpc_ksssmj);
		final EditText yhsw_scxcbpc_sz = (EditText) findViewById(R.id.yhsw_scxcbpc_sz);
		final EditText yhsw_scxcbpc_kssssl = (EditText) findViewById(R.id.yhsw_scxcbpc_kssssl);
		final EditText yhsw_scxcbpc_jzmc = (EditText) findViewById(R.id.yhsw_scxcbpc_jzmc);
		final EditText yhsw_scxcbpc_pjg = (EditText) findViewById(R.id.yhsw_scxcbpc_pjg);
		final EditText yhsw_scxcbpc_pjxj = (EditText) findViewById(R.id.yhsw_scxcbpc_pjxj);
		final EditText yhsw_scxcbpc_qyr = (EditText) findViewById(R.id.yhsw_scxcbpc_qyr);
		final EditText yhsw_scxcbpc_qybw = (EditText) findViewById(R.id.yhsw_scxcbpc_qybw);
		final EditText yhsw_scxcbpc_qysl = (EditText) findViewById(R.id.yhsw_scxcbpc_qysl);
		final EditText yhsw_scxcbpc_jjsl = (EditText) findViewById(R.id.yhsw_scxcbpc_jjsl);
		final EditText yhsw_scxcbpc_xdm = (EditText) findViewById(R.id.yhsw_scxcbpc_xdm);
		final EditText yhsw_scxcbpc_jdr = (EditText) findViewById(R.id.yhsw_scxcbpc_jdr);

		final Button yhsw_scxcbpc_jdrq = (Button) findViewById(R.id.yhsw_scxcbpc_jdrq);
		yhsw_scxcbpc_jdrq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_scxcbpc_jdrq, false);
			}
		});

		final EditText yhsw_scxcbpc_jdjg = (EditText) findViewById(R.id.yhsw_scxcbpc_jdjg);
		final EditText yhsw_scxcbpc_sflb = (EditText) findViewById(R.id.yhsw_scxcbpc_sflb);
		final EditText yhsw_scxcbpc_city = (EditText) findViewById(R.id.yhsw_scxcbpc_city);
		final EditText yhsw_scxcbpc_county = (EditText) findViewById(R.id.yhsw_scxcbpc_county);
		final EditText yhsw_scxcbpc_town = (EditText) findViewById(R.id.yhsw_scxcbpc_town);
		final EditText yhsw_scxcbpc_village = (EditText) findViewById(R.id.yhsw_scxcbpc_village);
		final EditText yhsw_scxcbpc_sbr = (EditText) findViewById(R.id.yhsw_scxcbpc_sbr);

		final Button yhsw_scxcbpc_sbsj=(Button) findViewById(R.id.yhsw_scxcbpc_sbsj);
		yhsw_scxcbpc_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_scxcbpc_sbsj, false);
			}
		});

		final EditText yhsw_scxcbpc_bz = (EditText) findViewById(R.id.yhsw_scxcbpc_bz);
		//在线上报
		Button upload = (Button) findViewById(R.id.yhsw_scxcbpc_upload);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_scxcbpc_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcsj = yhsw_scxcbpc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcr = yhsw_scxcbpc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}

				String dcdw = yhsw_scxcbpc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String jd = yhsw_scxcbpc_jd.getText().toString().trim();
				if (TextUtils.isEmpty(jd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksjdnotnull));
					return;
				}
				String wd = yhsw_scxcbpc_wd.getText().toString().trim();
				if (TextUtils.isEmpty(wd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.kswdnotnull));
					return;
				}
				String xbh = yhsw_scxcbpc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_scxcbpc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String whcd = yhsw_scxcbpc_whcd.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whcd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whcdnotnull));
					return;
				}
				String ksssmj = yhsw_scxcbpc_ksssmj.getText().toString().trim();
				if (TextUtils.isEmpty(ksssmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksssmjnotnull));
					return;
				}
				String sz = yhsw_scxcbpc_sz.getText().toString().trim();
				if (TextUtils.isEmpty(sz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sznotnull));
					return;
				}
				String kssssl = yhsw_scxcbpc_kssssl.getText().toString().trim();
				if (TextUtils.isEmpty(kssssl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksssslnotnull));
					return;
				}
				String jzmc = yhsw_scxcbpc_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}

				String qyr = yhsw_scxcbpc_qyr.getText().toString().trim();
				if (TextUtils.isEmpty(qyr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.qyrnotnull));
					return;
				}
				String qysl = yhsw_scxcbpc_qysl.getText().toString().trim();
				if (TextUtils.isEmpty(qysl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.qyslnotnull));
					return;
				}
				String jjsl = yhsw_scxcbpc_jjsl.getText().toString().trim();
				if (TextUtils.isEmpty(jjsl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jjslnotnull));
					return;
				}
				String xdm = yhsw_scxcbpc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String jdr = yhsw_scxcbpc_jdr.getText().toString().trim();
				if (TextUtils.isEmpty(jdr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdr));
					return;
				}
				String jdrq = yhsw_scxcbpc_jdrq.getText().toString().trim();
				if (TextUtils.isEmpty(jdrq)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdrqnotnull));
					return;
				}

				String jdjg = yhsw_scxcbpc_jdjg.getText().toString().trim();
				if (TextUtils.isEmpty(jdjg)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdjgnotnull));
					return;
				}
				String sflb = yhsw_scxcbpc_sflb.getText().toString().trim();
				if (TextUtils.isEmpty(sflb)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.mcywlbnotnull));
					return;
				}
				String city = yhsw_scxcbpc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_scxcbpc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_scxcbpc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_scxcbpc_village.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String sbr = yhsw_scxcbpc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_scxcbpc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}

				String pjg = yhsw_scxcbpc_pjg.getText().toString().trim();
				String pjxj = yhsw_scxcbpc_pjxj.getText().toString().trim();
				String qybw = yhsw_scxcbpc_qybw.getText().toString().trim();
				String bz = yhsw_scxcbpc_bz.getText().toString().trim();
				Webservice web = new Webservice(context);
				String result = web.addYhswSxcbpcData(ydbh, dcr, dcsj, dcdw, jd, wd, xbh, xbmj, whcd, ksssmj, sz, kssssl, jzmc, pjg, pjxj, qyr, qybw, qysl, jjsl, xdm, jdr, jdrq, jdjg, sflb, city, county, town, village, bz, "1", sbr, sbsj);
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
		Button bdsave = (Button) findViewById(R.id.yhsw_scxcbpc_bdsave);
		bdsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_scxcbpc_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcsj = yhsw_scxcbpc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcr = yhsw_scxcbpc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}

				String dcdw = yhsw_scxcbpc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String jd = yhsw_scxcbpc_jd.getText().toString().trim();
				if (TextUtils.isEmpty(jd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksjdnotnull));
					return;
				}
				String wd = yhsw_scxcbpc_wd.getText().toString().trim();
				if (TextUtils.isEmpty(wd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.kswdnotnull));
					return;
				}
				String xbh = yhsw_scxcbpc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_scxcbpc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String whcd = yhsw_scxcbpc_whcd.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whcd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whcdnotnull));
					return;
				}
				String ksssmj = yhsw_scxcbpc_ksssmj.getText().toString().trim();
				if (TextUtils.isEmpty(ksssmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksssmjnotnull));
					return;
				}
				String sz = yhsw_scxcbpc_sz.getText().toString().trim();
				if (TextUtils.isEmpty(sz)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sznotnull));
					return;
				}
				String kssssl = yhsw_scxcbpc_kssssl.getText().toString().trim();
				if (TextUtils.isEmpty(kssssl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ksssslnotnull));
					return;
				}
				String jzmc = yhsw_scxcbpc_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}

				String qyr = yhsw_scxcbpc_qyr.getText().toString().trim();
				if (TextUtils.isEmpty(qyr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.qyrnotnull));
					return;
				}
				String qysl = yhsw_scxcbpc_qysl.getText().toString().trim();
				if (TextUtils.isEmpty(qysl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.qyslnotnull));
					return;
				}
				String jjsl = yhsw_scxcbpc_jjsl.getText().toString().trim();
				if (TextUtils.isEmpty(jjsl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jjslnotnull));
					return;
				}
				String xdm = yhsw_scxcbpc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String jdr = yhsw_scxcbpc_jdr.getText().toString().trim();
				if (TextUtils.isEmpty(jdr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdrnotnull));
					return;
				}
				String jdrq = yhsw_scxcbpc_jdrq.getText().toString().trim();
				if (TextUtils.isEmpty(jdrq)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdrqnotnull));
					return;
				}

				String jdjg = yhsw_scxcbpc_jdjg.getText().toString().trim();
				if (TextUtils.isEmpty(jdjg)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jdjgnotnull));
					return;
				}
				String sflb = yhsw_scxcbpc_sflb.getText().toString().trim();
				if (TextUtils.isEmpty(sflb)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.mcywlbnotnull));
					return;
				}
				String city = yhsw_scxcbpc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_scxcbpc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_scxcbpc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_scxcbpc_village.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String sbr = yhsw_scxcbpc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_scxcbpc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}

				String pjg = yhsw_scxcbpc_pjg.getText().toString().trim();
				String pjxj = yhsw_scxcbpc_pjxj.getText().toString().trim();
				String qybw = yhsw_scxcbpc_qybw.getText().toString().trim();
				String bz = yhsw_scxcbpc_bz.getText().toString().trim();
				DataBaseHelper.addYhswSxcbpcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, jd, wd, xbh, xbmj, whcd, ksssmj, sz, kssssl, jzmc, pjg, pjxj, qyr, qybw, qysl, jjsl, xdm, jdr, jdrq, jdjg, sflb, city, county, town, village, bz, "0", sbr, sbsj);
				ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
				dismiss();
			}
		});

	}
}
