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
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.UtilTime;

/**
 * 添加样地虫害调查信息
 */
public class YhswYdchdcAddDialog extends Dialog {
	Context context;
	YHSWActivity activity;
	private Point currentpoint;

	public YhswYdchdcAddDialog(Context context, int theme,Point point) {
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
		setContentView(R.layout.yhsw_ydchdc_add);
		setCanceledOnTouchOutside(false);
		View status=findViewById(R.id.status);
		status.setVisibility(View.GONE);
		Button cancle = (Button) findViewById(R.id.yhsw_tcd_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		String ydbh = UtilTime.getSystemtime1();
		final TextView yhsw_ydchdc_ydbh = (TextView) findViewById(R.id.yhsw_ydchdc_ydbh);
		yhsw_ydchdc_ydbh.setText(ydbh);
		final EditText yhsw_ydchdc_dcr = (EditText) findViewById(R.id.yhsw_ydchdc_dcr);
		final TextView yhsw_ydchdc_dcrq = (TextView) findViewById(R.id.yhsw_ydchdc_dcrq);
		final EditText yhsw_ydchdc_dcdw = (EditText) findViewById(R.id.yhsw_ydchdc_dcdw);
		final EditText yhsw_ydchdc_ydc = (EditText) findViewById(R.id.yhsw_ydchdc_ydc);
		final EditText yhsw_ydchdc_ydk = (EditText) findViewById(R.id.yhsw_ydchdc_ydk);
		final Spinner yhsw_ydchdc_ydxz = (Spinner) findViewById(R.id.yhsw_ydchdc_ydxz);
		final EditText yhsw_ydchdc_ydmj = (EditText) findViewById(R.id.yhsw_ydchdc_ydmj);
		final Spinner yhsw_ydchdc_dclx = (Spinner) findViewById(R.id.yhsw_ydchdc_dclx);
		final EditText yhsw_ydchdc_yddbmj = (EditText) findViewById(R.id.yhsw_ydchdc_yddbmj);
		final EditText yhsw_ydchdc_zhmj = (EditText) findViewById(R.id.yhsw_ydchdc_zhmj);
		final EditText yhsw_ydchdc_hb = (EditText) findViewById(R.id.yhsw_ydchdc_hb);
		final EditText yhsw_ydchdc_ycjjd = (EditText) findViewById(R.id.yhsw_ydchdc_ycjjd);
		final EditText yhsw_ydchdc_ycjwd = (EditText) findViewById(R.id.yhsw_ydchdc_ycjwd);
		final EditText yhsw_ydchdc_xbh = (EditText) findViewById(R.id.yhsw_ydchdc_xbh);
		final EditText yhsw_ydchdc_xbmj = (EditText) findViewById(R.id.yhsw_ydchdc_xbmj);
		final EditText yhsw_ydchdc_city = (EditText) findViewById(R.id.yhsw_ydchdc_city);
		final EditText yhsw_ydchdc_county = (EditText) findViewById(R.id.yhsw_ydchdc_county);
		final EditText yhsw_ydchdc_town = (EditText) findViewById(R.id.yhsw_ydchdc_town);
		final EditText yhsw_ydchdc_village = (EditText) findViewById(R.id.yhsw_ydchdc_village);
		final EditText yhsw_ydchdc_xdm = (EditText) findViewById(R.id.yhsw_ydchdc_xdm);
		final EditText yhsw_ydchdc_jzmc = (EditText) findViewById(R.id.yhsw_ydchdc_jzmc);
		final EditText yhsw_ydchdc_chmc = (EditText) findViewById(R.id.yhsw_ydchdc_chmc);
		final Spinner yhsw_ydchdc_ct = (Spinner) findViewById(R.id.yhsw_ydchdc_ct);
		final Spinner yhsw_ydchdc_whbw = (Spinner) findViewById(R.id.yhsw_ydchdc_whbw);
		final Spinner yhsw_ydchdc_whcd = (Spinner) findViewById(R.id.yhsw_ydchdc_whcd);
		final Spinner yhsw_ydchdc_ly = (Spinner) findViewById(R.id.yhsw_ydchdc_ly);
		final Spinner yhsw_ydchdc_cbtj = (Spinner) findViewById(R.id.yhsw_ydchdc_cbtj);
		final EditText yhsw_ydchdc_cage = (EditText) findViewById(R.id.yhsw_ydchdc_cage);
		final EditText yhsw_ydchdc_czlv = (EditText) findViewById(R.id.yhsw_ydchdc_czlv);
		final EditText yhsw_ydchdc_ckmd = (EditText) findViewById(R.id.yhsw_ydchdc_ckmd);
		final EditText yhsw_ydchdc_shuage = (EditText) findViewById(R.id.yhsw_ydchdc_shuage);
		final EditText yhsw_ydchdc_yubidu = (EditText) findViewById(R.id.yhsw_ydchdc_yubidu);
		final EditText yhsw_ydchdc_jkzs = (EditText) findViewById(R.id.yhsw_ydchdc_jkzs);
		final EditText yhsw_ydchdc_swzs = (EditText) findViewById(R.id.yhsw_ydchdc_swzs);
		final EditText yhsw_ydchdc_ykh = (EditText) findViewById(R.id.yhsw_ydchdc_ykh);
		final EditText yhsw_ydchdc_yksd = (EditText) findViewById(R.id.yhsw_ydchdc_yksd);
		final Spinner yhsw_ydchdc_powei = (Spinner) findViewById(R.id.yhsw_ydchdc_powei);
		final EditText yhsw_ydchdc_poxiang = (EditText) findViewById(R.id.yhsw_ydchdc_poxiang);
		final Spinner yhsw_ydchdc_gslx = (Spinner) findViewById(R.id.yhsw_ydchdc_gslx);
		final EditText yhsw_ydchdc_mpmc = (EditText) findViewById(R.id.yhsw_ydchdc_mpmc);
		final EditText yhsw_ydchdc_mpzmj = (EditText) findViewById(R.id.yhsw_ydchdc_mpzmj);
		final EditText yhsw_ydchdc_chanml = (EditText) findViewById(R.id.yhsw_ydchdc_chanml);
		final EditText yhsw_ydchdc_zymmpz = (EditText) findViewById(R.id.yhsw_ydchdc_zymmpz);
		final EditText yhsw_ydchdc_bz = (EditText) findViewById(R.id.yhsw_ydchdc_bz);
		final EditText yhsw_ydchdc_sbr=(EditText) findViewById(R.id.yhsw_ydchdc_sbr);
		final Button yhsw_ydchdc_sbsj=(Button) findViewById(R.id.yhsw_ydchdc_sbsj);

		if(currentpoint!=null){
			yhsw_ydchdc_ycjjd.setText(currentpoint.getX()+"");
			yhsw_ydchdc_ycjwd.setText(currentpoint.getY()+"");
		}
//		ArrayAdapter gslxadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.gslx));
		//yhsw_ydchdc_gslx.setAdapter(gslxadapter);
//		ArrayAdapter poweiadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.pw));
//		yhsw_ydchdc_powei.setAdapter(poweiadapter);
//		ArrayAdapter cbtjadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.cbtj));
//		yhsw_ydchdc_cbtj.setAdapter(cbtjadapter);
//		ArrayAdapter lyadapter = new ArrayAdapter(context, R.unreportdata.myspinner,
//				context.getResources().getStringArray(R.array.ly));
//		yhsw_ydchdc_ly.setAdapter(lyadapter);
//		ArrayAdapter whcdadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.mcwhcd));
//		yhsw_ydchdc_whcd.setAdapter(whcdadapter);
//		ArrayAdapter whbwadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.whbw));
//		yhsw_ydchdc_whbw.setAdapter(whbwadapter);
//		ArrayAdapter ctadapter = new ArrayAdapter(context, R.unreportdata.myspinner,
//				context.getResources().getStringArray(R.array.ct));
//		yhsw_ydchdc_ct.setAdapter(ctadapter);
//		ArrayAdapter dclxadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.dclx));
//		yhsw_ydchdc_dclx.setAdapter(dclxadapter);
//		ArrayAdapter ydxzadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.ydxz));
//		yhsw_ydchdc_ydxz.setAdapter(ydxzadapter);

		yhsw_ydchdc_dcrq.setText(UtilTime.getSystemtime2());
		yhsw_ydchdc_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_ydchdc_sbsj, false);
			}
		});
		//本地保存
		Button bdsave = (Button) findViewById(R.id.yhsw_tcd_bdsave);
		bdsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(yhsw_ydchdc_ydbh.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String ydbh = yhsw_ydchdc_ydbh.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_dcr.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcr = yhsw_ydchdc_dcr.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_dcrq.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcsj = yhsw_ydchdc_dcrq.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_dcdw.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String dcdw = yhsw_ydchdc_dcdw.getText().toString().trim();

				String ydxz = yhsw_ydchdc_ydxz.getSelectedItemPosition()+"";
				String ydc = yhsw_ydchdc_ydc.getText().toString().trim();
				String ydk = yhsw_ydchdc_ydk.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_ydmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String ydmj = yhsw_ydchdc_ydmj.getText().toString().trim();

				String dclx = yhsw_ydchdc_dclx.getSelectedItemPosition()+"";

				if (TextUtils.isEmpty(yhsw_ydchdc_yddbmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String yddbmj = yhsw_ydchdc_yddbmj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_zhmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String zhmj = yhsw_ydchdc_zhmj.getText().toString().trim();

				String hb = yhsw_ydchdc_hb.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_ycjjd.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjjd = yhsw_ydchdc_ycjjd.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_ycjwd.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String ycjwd = yhsw_ydchdc_ycjwd.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_xbh.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbh = yhsw_ydchdc_xbh.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_xbmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String xbmj = yhsw_ydchdc_xbmj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_city.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String city = yhsw_ydchdc_city.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_county.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String county = yhsw_ydchdc_county.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_town.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String town = yhsw_ydchdc_town.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_village.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String village = yhsw_ydchdc_village.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_xdm.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String xdm = yhsw_ydchdc_xdm.getText().toString().trim();


				if (TextUtils.isEmpty(yhsw_ydchdc_jzmc.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String jzmc = yhsw_ydchdc_jzmc.getText().toString().trim();


				if (TextUtils.isEmpty(yhsw_ydchdc_chmc.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.chmcnotnull));
					return;
				}
				String chmc = yhsw_ydchdc_chmc.getText().toString().trim();

				String ct = yhsw_ydchdc_ct.getSelectedItemPosition()+"";

				String whbw = yhsw_ydchdc_whbw.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whbw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whbwnotnull));
					return;
				}

				String whcd = yhsw_ydchdc_whcd.getSelectedItemPosition()+"";
				String ly = yhsw_ydchdc_ly.getSelectedItemPosition()+"";
				String cbtj = yhsw_ydchdc_cbtj.getSelectedItemPosition()+"";
				String cl = yhsw_ydchdc_cage.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydchdc_czlv.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.czlnotnull));
					return;
				}
				String czl = yhsw_ydchdc_czlv.getText().toString().trim();


				if (TextUtils.isEmpty(yhsw_ydchdc_ckmd.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ckmdnotnull));
					return;
				}
				String ckmd = yhsw_ydchdc_ckmd.getText().toString().trim();


				if (TextUtils.isEmpty(yhsw_ydchdc_sbr.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbr=yhsw_ydchdc_sbr.getText().toString().trim();


				if (TextUtils.isEmpty(yhsw_ydchdc_sbsj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String sbsj=yhsw_ydchdc_sbsj.getText().toString().trim();

				String sl = yhsw_ydchdc_shuage.getText().toString().trim();
				String ybd = yhsw_ydchdc_yubidu.getText().toString().trim();
				String jkzs = yhsw_ydchdc_jkzs.getText().toString().trim();
				String swzs = yhsw_ydchdc_swzs.getText().toString().trim();
				String ykh = yhsw_ydchdc_ykh.getText().toString().trim();
				String yksd = yhsw_ydchdc_yksd.getText().toString().trim();
				String pw = yhsw_ydchdc_powei.getSelectedItemPosition()+"";
				String px = yhsw_ydchdc_poxiang.getText().toString().trim();
				String gslx = yhsw_ydchdc_gslx.getSelectedItemPosition()+"";
				String mpmc = yhsw_ydchdc_mpmc.getText().toString().trim();
				String mpzmj = yhsw_ydchdc_mpzmj.getText().toString().trim();
				String cml = yhsw_ydchdc_chanml.getText().toString().trim();
				String zymmpz = yhsw_ydchdc_zymmpz.getText().toString().trim();
				String bz = yhsw_ydchdc_bz.getText().toString().trim();
				DataBaseHelper.addYhswYdchdcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, xdm, jzmc, chmc, ct, whbw, whcd, ly, cbtj, cl, czl, ckmd, sl, ybd, jkzs, swzs, ykh, yksd, pw, px, gslx, mpmc, mpzmj, cml, zymmpz, bz,sbr,sbsj);
				ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
				YhswYdchxcDialog dialog=new YhswYdchxcDialog(context, R.style.Dialog,ydbh,"0");
				BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
				dismiss();
			}
		});
		//在线上报
		Button upload = (Button) findViewById(R.id.yhsw_tcd_uploadstatus);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_ydchdc_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcr = yhsw_ydchdc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_ydchdc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_ydchdc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String ydxz = yhsw_ydchdc_ydxz.getSelectedItemPosition()+"";
				String ydc = yhsw_ydchdc_ydc.getText().toString().trim();
				String ydk = yhsw_ydchdc_ydk.getText().toString().trim();
				String ydmj = yhsw_ydchdc_ydmj.getText().toString().trim();
				if (TextUtils.isEmpty(ydmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String dclx = yhsw_ydchdc_dclx.getSelectedItemPosition()+"";
				String yddbmj = yhsw_ydchdc_yddbmj.getText().toString().trim();
				if (TextUtils.isEmpty(yddbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String zhmj = yhsw_ydchdc_zhmj.getText().toString().trim();
				if (TextUtils.isEmpty(zhmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String hb = yhsw_ydchdc_hb.getText().toString().trim();
				String ycjjd = yhsw_ydchdc_ycjjd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjwd = yhsw_ydchdc_ycjwd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String xbh = yhsw_ydchdc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_ydchdc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String city = yhsw_ydchdc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_ydchdc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_ydchdc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_ydchdc_village.getText().toString()
						.trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String xdm = yhsw_ydchdc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String jzmc = yhsw_ydchdc_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String chmc = yhsw_ydchdc_chmc.getText().toString().trim();
				if (TextUtils.isEmpty(chmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.chmcnotnull));
					return;
				}
				String ct = yhsw_ydchdc_ct.getSelectedItemPosition()+"";
				String whbw = yhsw_ydchdc_whbw.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whbw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whbwnotnull));
					return;
				}
				String whcd = yhsw_ydchdc_whcd.getSelectedItemPosition()+"";
				String ly = yhsw_ydchdc_ly.getSelectedItemPosition()+"";
				String cbtj = yhsw_ydchdc_cbtj.getSelectedItemPosition()+"";
				String cl = yhsw_ydchdc_cage.getText().toString().trim();
				String czl = yhsw_ydchdc_czlv.getText().toString().trim();
				if (TextUtils.isEmpty(czl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.czlnotnull));
					return;
				}
				String ckmd = yhsw_ydchdc_ckmd.getText().toString().trim();
				if (TextUtils.isEmpty(ckmd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ckmdnotnull));
					return;
				}
				String sbr=yhsw_ydchdc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj=yhsw_ydchdc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String sl = yhsw_ydchdc_shuage.getText().toString().trim();
				String ybd = yhsw_ydchdc_yubidu.getText().toString().trim();
				String jkzs = yhsw_ydchdc_jkzs.getText().toString().trim();
				String swzs = yhsw_ydchdc_swzs.getText().toString().trim();
				String ykh = yhsw_ydchdc_ykh.getText().toString().trim();
				String yksd = yhsw_ydchdc_yksd.getText().toString().trim();
				String pw = yhsw_ydchdc_powei.getSelectedItemPosition()+"";
				String px = yhsw_ydchdc_poxiang.getText().toString().trim();
				String gslx = yhsw_ydchdc_gslx.getSelectedItemPosition()+"";
				String mpmc = yhsw_ydchdc_mpmc.getText().toString().trim();
				String mpzmj = yhsw_ydchdc_mpzmj.getText().toString().trim();
				String cml = yhsw_ydchdc_chanml.getText().toString().trim();
				String zymmpz = yhsw_ydchdc_zymmpz.getText().toString().trim();
				String bz = yhsw_ydchdc_bz.getText().toString().trim();
				Webservice web = new Webservice(context);
				String result = web.addYhswYdchdcData(ydbh, dcr, dcsj, dcdw,
						ydxz, ydc, ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd,
						ycjwd, xbh, xbmj, city, county, town, village, xdm,
						jzmc, chmc, ct, whbw, whcd, ly, cbtj, cl, czl, ckmd,
						sl, ybd, jkzs, swzs, ykh, yksd, pw, px, gslx, mpmc,
						mpzmj, cml, zymmpz, bz, "1",sbr,sbsj);
				String[] splits = result.split(",");
				if (splits.length > 0) {
					if ("True".equals(splits[0])) {
						ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
						YhswYdchxcDialog dialog=new YhswYdchxcDialog(context, R.style.Dialog,ydbh,"1");
						dialog.show();
						BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
						dismiss();
					}else{
						ToastUtil.setToast(context, context.getResources().getString(R.string.addfailed));
					}
				}else{
					ToastUtil.setToast(context, context.getResources().getString(R.string.addfailed));
				}
			}
		});
	}
}
