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
 * 添加样地病害调查信息
 */
public class YhswYdbhdcAddDialog extends Dialog {
	Context context;
	YHSWActivity activity;
	private Point currentpoint;

	public YhswYdbhdcAddDialog(Context context, int theme,Point point) {
		super(context, theme);
		this.context = context;
		this.activity = (YHSWActivity) context;
		this.currentpoint=point;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.yhsw_ydbhdc_add);
		setCanceledOnTouchOutside(false);
		View status=findViewById(R.id.status);
		status.setVisibility(View.INVISIBLE);
		Button cancle = (Button) findViewById(R.id.yhsw_ydbhdc_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		String ydbh = UtilTime.getSystemtime1();
		final TextView yhsw_ydbhdc_ydbh = (TextView) findViewById(R.id.yhsw_ydbhdc_ydbh);
		yhsw_ydbhdc_ydbh.setText(ydbh);
		final EditText yhsw_ydbhdc_dcr = (EditText) findViewById(R.id.yhsw_ydbhdc_dcr);
		final TextView yhsw_ydbhdc_dcrq = (TextView) findViewById(R.id.yhsw_ydbhdc_dcrq);
		final EditText yhsw_ydbhdc_dcdw = (EditText) findViewById(R.id.yhsw_ydbhdc_dcdw);
		final EditText yhsw_ydbhdc_ydc = (EditText) findViewById(R.id.yhsw_ydbhdc_ydc);
		final EditText yhsw_ydbhdc_ydk = (EditText) findViewById(R.id.yhsw_ydbhdc_ydk);
		final Spinner yhsw_ydbhdc_ydxz = (Spinner) findViewById(R.id.yhsw_ydbhdc_ydxz);
		final EditText yhsw_ydbhdc_ydmj = (EditText) findViewById(R.id.yhsw_ydbhdc_ydmj);
		final Spinner yhsw_ydbhdc_dclx = (Spinner) findViewById(R.id.yhsw_ydbhdc_dclx);
		final EditText yhsw_ydbhdc_yddbmj = (EditText) findViewById(R.id.yhsw_ydbhdc_yddbmj);
		final EditText yhsw_ydbhdc_zhmj = (EditText) findViewById(R.id.yhsw_ydbhdc_zhmj);
		final EditText yhsw_ydbhdc_hb = (EditText) findViewById(R.id.yhsw_ydbhdc_hb);
		final EditText yhsw_ydbhdc_ycjjd = (EditText) findViewById(R.id.yhsw_ydbhdc_ycjjd);
		final EditText yhsw_ydbhdc_ycjwd = (EditText) findViewById(R.id.yhsw_ydbhdc_ycjwd);
		final EditText yhsw_ydbhdc_xbh = (EditText) findViewById(R.id.yhsw_ydbhdc_xbh);
		final EditText yhsw_ydbhdc_xbmj = (EditText) findViewById(R.id.yhsw_ydbhdc_xbmj);
		final EditText yhsw_ydbhdc_city = (EditText) findViewById(R.id.yhsw_ydbhdc_city);
		final EditText yhsw_ydbhdc_county = (EditText) findViewById(R.id.yhsw_ydbhdc_county);
		final EditText yhsw_ydbhdc_town = (EditText) findViewById(R.id.yhsw_ydbhdc_town);
		final EditText yhsw_ydbhdc_village = (EditText) findViewById(R.id.yhsw_ydbhdc_village);
		final EditText yhsw_ydbhdc_xdm = (EditText) findViewById(R.id.yhsw_ydbhdc_xdm);
		final EditText yhsw_ydbhdc_jzmc = (EditText) findViewById(R.id.yhsw_ydbhdc_jzmc);
		final EditText yhsw_ydbhdc_bhmc = (EditText) findViewById(R.id.yhsw_ydbhdc_bhmc);
		final Spinner yhsw_ydbhdc_fblx = (Spinner) findViewById(R.id.yhsw_ydbhdc_fblx);
		final EditText yhsw_ydbhdc_pjxj = (EditText) findViewById(R.id.yhsw_ydbhdc_pjxj);
		final Spinner yhsw_ydbhdc_whcd = (Spinner) findViewById(R.id.yhsw_ydbhdc_whcd);
		final Spinner yhsw_ydbhdc_ly = (Spinner) findViewById(R.id.yhsw_ydbhdc_ly);
		final Spinner yhsw_ydbhdc_cbtj = (Spinner) findViewById(R.id.yhsw_ydbhdc_cbtj);
		final EditText yhsw_ydbhdc_bgl = (EditText) findViewById(R.id.yhsw_ydbhdc_bgl);
		final EditText yhsw_ydbhdc_bgzs = (EditText) findViewById(R.id.yhsw_ydbhdc_bgzs);
		final EditText yhsw_ydbhdc_pd = (EditText) findViewById(R.id.yhsw_ydbhdc_pd);
		final Spinner yhsw_ydbhdc_dixing = (Spinner) findViewById(R.id.yhsw_ydbhdc_dixing);
		final EditText yhsw_ydbhdc_yubidu = (EditText) findViewById(R.id.yhsw_ydbhdc_yubidu);
		final EditText yhsw_ydbhdc_pjgao = (EditText) findViewById(R.id.yhsw_ydbhdc_pjgao);
		final EditText yhsw_ydbhdc_yhzwgd = (EditText) findViewById(R.id.yhsw_ydbhdc_yhzwgd);
		final EditText yhsw_ydbhdc_ykh = (EditText) findViewById(R.id.yhsw_ydbhdc_ykh);
		final EditText yhsw_ydbhdc_yksd = (EditText) findViewById(R.id.yhsw_ydbhdc_yksd);
		final EditText yhsw_ydbhdc_shuage = (EditText) findViewById(R.id.yhsw_ydbhdc_shuage);
		final EditText yhsw_ydbhdc_mpmc = (EditText) findViewById(R.id.yhsw_ydbhdc_mpmc);
		final EditText yhsw_ydbhdc_mpzmj = (EditText) findViewById(R.id.yhsw_ydbhdc_mpzmj);
		final EditText yhsw_ydbhdc_chanml = (EditText) findViewById(R.id.yhsw_ydbhdc_chanml);
		final EditText yhsw_ydbhdc_zymmpz = (EditText) findViewById(R.id.yhsw_ydbhdc_zymmpz);
		final EditText yhsw_ydbhdc_sbr=(EditText) findViewById(R.id.yhsw_ydbhdc_sbr);
		final Button yhsw_ydbhdc_sbsj=(Button) findViewById(R.id.yhsw_ydbhdc_sbsj);
		final Spinner yhsw_ydbhdc_gslx = (Spinner) findViewById(R.id.yhsw_ydbhdc_gslx);
		final EditText yhsw_ydbhdc_bz = (EditText) findViewById(R.id.yhsw_ydbhdc_bz);
		if(currentpoint!=null){
			yhsw_ydbhdc_ycjjd.setText(currentpoint.getX()+"");
			yhsw_ydbhdc_ycjwd.setText(currentpoint.getY()+"");
		}
//		ArrayAdapter dxadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.dx));
//		yhsw_ydbhdc_dixing.setAdapter(dxadapter);
//		ArrayAdapter fblxadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.fblx));
//		yhsw_ydbhdc_fblx.setAdapter(fblxadapter);
//		ArrayAdapter gslxadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.gslx));
//		yhsw_ydbhdc_gslx.setAdapter(gslxadapter);
//		ArrayAdapter cbtjadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.cbtj));
//		yhsw_ydbhdc_cbtj.setAdapter(cbtjadapter);
//		ArrayAdapter lyadapter = new ArrayAdapter(context, R.unreportdata.myspinner,
//				context.getResources().getStringArray(R.array.ly));
//		yhsw_ydbhdc_ly.setAdapter(lyadapter);
//		ArrayAdapter whcdadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.mcwhcd));
//		yhsw_ydbhdc_whcd.setAdapter(whcdadapter);
//		ArrayAdapter dclxadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.dclx));
//		yhsw_ydbhdc_dclx.setAdapter(dclxadapter);
//		ArrayAdapter ydxzadapter = new ArrayAdapter(context,
//				R.unreportdata.myspinner, context.getResources().getStringArray(
//				R.array.ydxz));
//		yhsw_ydbhdc_ydxz.setAdapter(ydxzadapter);
		yhsw_ydbhdc_dcrq.setText(UtilTime.getSystemtime2());
		yhsw_ydbhdc_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_ydbhdc_sbsj, false);
			}
		});
		//本地保存
		Button bdsave = (Button) findViewById(R.id.yhsw_ydbhdc_bdsave);
		bdsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (TextUtils.isEmpty(yhsw_ydbhdc_ydbh.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String ydbh = yhsw_ydbhdc_ydbh.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_dcr.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcr = yhsw_ydbhdc_dcr.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_dcrq.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcsj = yhsw_ydbhdc_dcrq.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_dcdw.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String dcdw = yhsw_ydbhdc_dcdw.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_ydmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String ydmj = yhsw_ydbhdc_ydmj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_yddbmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String yddbmj = yhsw_ydbhdc_yddbmj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_zhmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String zhmj = yhsw_ydbhdc_zhmj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_ycjjd.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjjd = yhsw_ydbhdc_ycjjd.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_ycjwd.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String ycjwd = yhsw_ydbhdc_ycjwd.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_xbh.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbh = yhsw_ydbhdc_xbh.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_xbmj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String xbmj = yhsw_ydbhdc_xbmj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_city.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String city = yhsw_ydbhdc_city.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_county.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String county = yhsw_ydbhdc_county.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_town.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String town = yhsw_ydbhdc_town.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_village.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String village = yhsw_ydbhdc_village.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_xdm.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String xdm = yhsw_ydbhdc_xdm.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_jzmc.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String jzmc = yhsw_ydbhdc_jzmc.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_bhmc.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bhmcnotnull));
					return;
				}
				String bhmc = yhsw_ydbhdc_bhmc.getText().toString().trim();

				String fblx = yhsw_ydbhdc_fblx.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(fblx)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.fblxnotnull));
					return;
				}

				String gslx = yhsw_ydbhdc_gslx.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(gslx)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.gslxnotnull));
					return;
				}
				String bgl = yhsw_ydbhdc_bgl.getText().toString().trim();
				if (TextUtils.isEmpty(bgl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bglnotnull));
					return;
				}

				if (TextUtils.isEmpty(yhsw_ydbhdc_bgzs.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bgzsnotnull));
					return;
				}
				String bgzs = yhsw_ydbhdc_bgzs.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_pjxj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.pjxjnotnull));
					return;
				}
				String pjxj = yhsw_ydbhdc_pjxj.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_yubidu.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ybdnotnull));
					return;
				}
				String ybd = yhsw_ydbhdc_yubidu.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_sbr.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbr = yhsw_ydbhdc_sbr.getText().toString().trim();

				if (TextUtils.isEmpty(yhsw_ydbhdc_sbsj.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String sbsj = yhsw_ydbhdc_sbsj.getText().toString().trim();

				String whcd = yhsw_ydbhdc_whcd.getSelectedItemPosition()+"";
				String ydc = yhsw_ydbhdc_ydc.getText().toString().trim();
				String ydxz = yhsw_ydbhdc_ydxz.getSelectedItemPosition()+"";
				String ydk = yhsw_ydbhdc_ydk.getText().toString().trim();
				String dclx = yhsw_ydbhdc_dclx.getSelectedItemPosition()+"";
				String hb = yhsw_ydbhdc_hb.getText().toString().trim();
				String ly = yhsw_ydbhdc_ly.getSelectedItemPosition()+"";
				String cbtj = yhsw_ydbhdc_cbtj.getSelectedItemPosition()+"";
				String sl = yhsw_ydbhdc_shuage.getText().toString().trim();
				String pd = yhsw_ydbhdc_pd.getText().toString().trim();
				String dx = yhsw_ydbhdc_dixing.getSelectedItemPosition()+"";
				String ykh = yhsw_ydbhdc_ykh.getText().toString().trim();
				String yksd = yhsw_ydbhdc_yksd.getText().toString().trim();
				String pjg = yhsw_ydbhdc_pjgao.getText().toString().trim();
				String yhzwgd = yhsw_ydbhdc_yhzwgd.getText().toString().trim();
				String mpmc = yhsw_ydbhdc_mpmc.getText().toString().trim();
				String mpzmj = yhsw_ydbhdc_mpzmj.getText().toString().trim();
				String cml = yhsw_ydbhdc_chanml.getText().toString().trim();
				String zymmpz = yhsw_ydbhdc_zymmpz.getText().toString().trim();
				String bz = yhsw_ydbhdc_bz.getText().toString().trim();
				DataBaseHelper.addYhswYdbhdcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, xdm, jzmc, bhmc, fblx, ly, whcd, cbtj, gslx, bgl, bgzs, pjxj, sl, ybd, pd, dx, ykh, yksd, pjg, yhzwgd, mpmc, mpzmj, cml, zymmpz, bz, sbr, sbsj);
				ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
				YhswYdbhxcDialog dialog=new YhswYdbhxcDialog(context, R.style.Dialog,ydbh,"0");
				BussUtil.setDialogParams(context,dialog, 0.85, 0.9);
				dismiss();
			}
		});
		//在线上报
		Button upload = (Button) findViewById(R.id.yhsw_ydbhdc_upload);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_ydbhdc_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcr = yhsw_ydbhdc_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_ydbhdc_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_ydbhdc_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String ydmj = yhsw_ydbhdc_ydmj.getText().toString().trim();
				if (TextUtils.isEmpty(ydmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String yddbmj = yhsw_ydbhdc_yddbmj.getText().toString().trim();
				if (TextUtils.isEmpty(yddbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String zhmj = yhsw_ydbhdc_zhmj.getText().toString().trim();
				if (TextUtils.isEmpty(zhmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String ycjjd = yhsw_ydbhdc_ycjjd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjwd = yhsw_ydbhdc_ycjwd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String xbh = yhsw_ydbhdc_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_ydbhdc_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String city = yhsw_ydbhdc_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_ydbhdc_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_ydbhdc_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_ydbhdc_village.getText().toString().trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String xdm = yhsw_ydbhdc_xdm.getText().toString().trim();
				if (TextUtils.isEmpty(xdm)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xdmnotnull));
					return;
				}
				String jzmc = yhsw_ydbhdc_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String bhmc = yhsw_ydbhdc_bhmc.getText().toString().trim();
				if (TextUtils.isEmpty(bhmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bhmcnotnull));
					return;
				}
				String fblx = yhsw_ydbhdc_fblx.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(fblx)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.fblxnotnull));
					return;
				}
				String gslx = yhsw_ydbhdc_gslx.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(gslx)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.gslxnotnull));
					return;
				}
				String bgl = yhsw_ydbhdc_bgl.getText().toString().trim();
				if (TextUtils.isEmpty(bgl)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bglnotnull));
					return;
				}
				String bgzs = yhsw_ydbhdc_bgzs.getText().toString().trim();
				if (TextUtils.isEmpty(bgzs)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.bgzsnotnull));
					return;
				}
				String pjxj = yhsw_ydbhdc_pjxj.getText().toString().trim();
				if (TextUtils.isEmpty(pjxj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.pjxjnotnull));
					return;
				}
				String ybd = yhsw_ydbhdc_yubidu.getText().toString().trim();
				if (TextUtils.isEmpty(ybd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ybdnotnull));
					return;
				}
				String sbr = yhsw_ydbhdc_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_ydbhdc_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String whcd = yhsw_ydbhdc_whcd.getSelectedItemPosition()+"";
				String ydc = yhsw_ydbhdc_ydc.getText().toString().trim();
				String ydxz = yhsw_ydbhdc_ydxz.getSelectedItemPosition()+"";
				String ydk = yhsw_ydbhdc_ydk.getText().toString().trim();
				String dclx = yhsw_ydbhdc_dclx.getSelectedItemPosition()+"";
				String hb = yhsw_ydbhdc_hb.getText().toString().trim();
				String ly = yhsw_ydbhdc_ly.getSelectedItemPosition()+"";
				String cbtj = yhsw_ydbhdc_cbtj.getSelectedItemPosition()+"";
				String sl = yhsw_ydbhdc_shuage.getText().toString().trim();
				String pd = yhsw_ydbhdc_pd.getText().toString().trim();
				String dx = yhsw_ydbhdc_dixing.getSelectedItemPosition()+"";
				String ykh = yhsw_ydbhdc_ykh.getText().toString().trim();
				String yksd = yhsw_ydbhdc_yksd.getText().toString().trim();
				String pjg = yhsw_ydbhdc_pjgao.getText().toString().trim();
				String yhzwgd = yhsw_ydbhdc_yhzwgd.getText().toString().trim();
				String mpmc = yhsw_ydbhdc_mpmc.getText().toString().trim();
				String mpzmj = yhsw_ydbhdc_mpzmj.getText().toString().trim();
				String cml = yhsw_ydbhdc_chanml.getText().toString().trim();
				String zymmpz = yhsw_ydbhdc_zymmpz.getText().toString().trim();
				String bz = yhsw_ydbhdc_bz.getText().toString().trim();
				Webservice web = new Webservice(context);
				String result = web.addYhswYdbhdcData(ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, xdm, jzmc, bhmc, fblx, ly, whcd, cbtj, gslx, bgl, bgzs, pjxj, sl, ybd, pd, dx, ykh, yksd, pjg, yhzwgd, mpmc, mpzmj, cml, zymmpz, bz, "1", sbr, sbsj);
				String[] splits = result.split(",");
				if (splits.length > 0) {
					if ("True".equals(splits[0])) {
						ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
						YhswYdbhxcDialog dialog=new YhswYdbhxcDialog(context, R.style.Dialog,ydbh,"1");
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
