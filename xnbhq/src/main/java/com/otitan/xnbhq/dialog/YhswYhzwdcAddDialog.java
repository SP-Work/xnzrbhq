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
 * 添加样地虫害详查信息
 */
public class YhswYhzwdcAddDialog extends Dialog {
	Context context;
	YHSWActivity activity;
	private Point currentpoint;

	public YhswYhzwdcAddDialog(Context context, int theme,Point point) {
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
		setContentView(R.layout.yhsw_yhzwdc_add);
		setCanceledOnTouchOutside(false);
		View status=findViewById(R.id.status);
		status.setVisibility(View.INVISIBLE);
		Button cancle = (Button) findViewById(R.id.yhsw_yhzw_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		String ydbh = UtilTime.getSystemtime1();
		final TextView yhsw_yhzw_ydbh = (TextView) findViewById(R.id.yhsw_yhzw_ydbh);
		yhsw_yhzw_ydbh.setText(ydbh);
		final TextView yhsw_yhzw_dcrq = (TextView) findViewById(R.id.yhsw_yhzw_dcrq);
		yhsw_yhzw_dcrq.setText(UtilTime.getSystemtime2());
		final EditText yhsw_yhzw_dcr = (EditText) findViewById(R.id.yhsw_yhzw_dcr);
		final EditText yhsw_yhzw_dcdw = (EditText) findViewById(R.id.yhsw_yhzw_dcdw);
		final EditText yhsw_yhzw_ydc = (EditText) findViewById(R.id.yhsw_yhzw_ydc);
		final EditText yhsw_yhzw_ydk = (EditText) findViewById(R.id.yhsw_yhzw_ydk);

		final Spinner yhsw_yhzw_ydxz = (Spinner) findViewById(R.id.yhsw_yhzw_ydxz);
//		ArrayAdapter ydxzadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.ydxz));
//		yhsw_yhzw_ydxz.setAdapter(ydxzadapter);

		final EditText yhsw_yhzw_ydmj = (EditText) findViewById(R.id.yhsw_yhzw_ydmj);
		final EditText yhsw_yhzw_zhmj = (EditText) findViewById(R.id.yhsw_yhzw_zhmj);
		final EditText yhsw_yhzw_yddbmj = (EditText) findViewById(R.id.yhsw_yhzw_yddbmj);

		final Spinner yhsw_yhzw_ly = (Spinner) findViewById(R.id.yhsw_yhzw_ly);
//		ArrayAdapter lyadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.ly));
//		yhsw_yhzw_ly.setAdapter(lyadapter);

		final EditText yhsw_yhzw_hb = (EditText) findViewById(R.id.yhsw_yhzw_hb);

		final EditText yhsw_yhzw_ycjjd = (EditText) findViewById(R.id.yhsw_yhzw_ycjjd);
		final EditText yhsw_yhzw_ycjwd = (EditText) findViewById(R.id.yhsw_yhzw_ycjwd);

		if(currentpoint!=null){
			yhsw_yhzw_ycjjd.setText(currentpoint.getX()+"");
			yhsw_yhzw_ycjwd.setText(currentpoint.getY()+"");
		}

		final EditText yhsw_yhzw_xbh = (EditText) findViewById(R.id.yhsw_yhzw_xbh);
		final EditText yhsw_yhzw_xbmj = (EditText) findViewById(R.id.yhsw_yhzw_xbmj);
		final EditText yhsw_yhzw_city = (EditText) findViewById(R.id.yhsw_yhzw_city);
		final EditText yhsw_yhzw_county = (EditText) findViewById(R.id.yhsw_yhzw_county);
		final EditText yhsw_yhzw_town = (EditText) findViewById(R.id.yhsw_yhzw_town);
		final EditText yhsw_yhzw_village = (EditText) findViewById(R.id.yhsw_yhzw_village);
		final EditText yhsw_yhzw_yhzwmc = (EditText) findViewById(R.id.yhsw_yhzw_yhzwmc);
		final EditText yhsw_yhzw_jzmc = (EditText) findViewById(R.id.yhsw_yhzw_jzmc);
		final EditText yhsw_yhzw_yhzwhigh = (EditText) findViewById(R.id.yhsw_yhzw_yhzwhigh);
		final EditText yhsw_yhzw_yhzwdj = (EditText) findViewById(R.id.yhsw_yhzw_yhzwdj);
		final EditText yhsw_yhzw_whzwmc = (EditText) findViewById(R.id.yhsw_yhzw_whzwmc);

		final Spinner yhsw_yhzw_whcd = (Spinner) findViewById(R.id.yhsw_yhzw_whcd);
//		ArrayAdapter whcdadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.mcwhcd));
//		yhsw_yhzw_whcd.setAdapter(whcdadapter);

		final Spinner yhsw_yhzw_whbw = (Spinner) findViewById(R.id.yhsw_yhzw_whbw);
//		ArrayAdapter whbwadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.whbw));
//		yhsw_yhzw_whbw.setAdapter(whbwadapter);

		final Spinner yhsw_yhzw_cbtj = (Spinner) findViewById(R.id.yhsw_yhzw_cbtj);
//		ArrayAdapter cbtjadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.cbtj));
//		yhsw_yhzw_cbtj.setAdapter(cbtjadapter);

		final EditText yhsw_yhzw_bhzgs = (EditText) findViewById(R.id.yhsw_yhzw_bhzgs);
		final EditText yhsw_yhzw_zgzs = (EditText) findViewById(R.id.yhsw_yhzw_zgzs);
		final EditText yhsw_yhzw_whl = (EditText) findViewById(R.id.yhsw_yhzw_whl);
		final EditText yhsw_yhzw_whzwszzk = (EditText) findViewById(R.id.yhsw_yhzw_whzwszzk);
		final EditText yhsw_yhzw_pjxj = (EditText) findViewById(R.id.yhsw_yhzw_pjxj);
		final EditText yhsw_yhzw_pjgao = (EditText) findViewById(R.id.yhsw_yhzw_pjgao);
		final EditText yhsw_yhzw_shuage = (EditText) findViewById(R.id.yhsw_yhzw_shuage);
		final EditText yhsw_yhzw_yubidu = (EditText) findViewById(R.id.yhsw_yhzw_yubidu);
		final EditText yhsw_yhzw_dixing = (EditText) findViewById(R.id.yhsw_yhzw_dixing);
		final EditText yhsw_yhzw_djing = (EditText) findViewById(R.id.yhsw_yhzw_djing);

		final Spinner yhsw_yhzw_fblx = (Spinner) findViewById(R.id.yhsw_yhzw_fblx);
//		ArrayAdapter fblxadapter=new ArrayAdapter(context, R.unreportdata.myspinner, context.getResources().getStringArray(R.array.fblxyhzw));
//		yhsw_yhzw_fblx.setAdapter(fblxadapter);

		final EditText yhsw_yhzw_podu = (EditText) findViewById(R.id.yhsw_yhzw_podu);
		final EditText yhsw_yhzw_sbr = (EditText) findViewById(R.id.yhsw_yhzw_sbr);

		final Button yhsw_yhzw_sbsj = (Button) findViewById(R.id.yhsw_yhzw_sbsj);
		yhsw_yhzw_sbsj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_yhzw_sbsj, false);
			}
		});
		final EditText yhsw_yhzw_yksd = (EditText) findViewById(R.id.yhsw_yhzw_yksd);
		final EditText yhsw_yhzw_bz = (EditText) findViewById(R.id.yhsw_yhzw_bz);
		//在线上报
		Button upload = (Button) findViewById(R.id.yhsw_yhzw_upload);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (TextUtils.isEmpty(yhsw_yhzw_ydbh.getText())) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String ydbh = yhsw_yhzw_ydbh.getText().toString().trim();

				String dcr = yhsw_yhzw_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_yhzw_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_yhzw_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String ydmj = yhsw_yhzw_ydmj.getText().toString().trim();
				if (TextUtils.isEmpty(ydmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String yddbmj = yhsw_yhzw_yddbmj.getText().toString().trim();
				if (TextUtils.isEmpty(yddbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String zhmj = yhsw_yhzw_zhmj.getText().toString().trim();
				if (TextUtils.isEmpty(zhmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String ycjjd = yhsw_yhzw_ycjjd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjwd = yhsw_yhzw_ycjwd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String xbh = yhsw_yhzw_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_yhzw_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String city = yhsw_yhzw_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_yhzw_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_yhzw_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_yhzw_village.getText().toString().trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String jzmc = yhsw_yhzw_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String yhzwmc = yhsw_yhzw_yhzwmc.getText().toString().trim();
				if (TextUtils.isEmpty(yhzwmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yhzwmcnotnull));
					return;
				}
				String yhzwgd = yhsw_yhzw_yhzwhigh.getText().toString().trim();
				if (TextUtils.isEmpty(yhzwgd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yhzwgdnotnull));
					return;
				}
				String yhzwdj= yhsw_yhzw_yhzwdj.getText().toString().trim();
				if (TextUtils.isEmpty(yhzwdj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yhzwdjnotnull));
					return;
				}
				String whbw = yhsw_yhzw_whbw.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whbw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whbwnotnull));
					return;
				}
				String sbr = yhsw_yhzw_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_yhzw_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String ydxz = yhsw_yhzw_ydxz.getSelectedItemPosition()+"";
				String ydc = yhsw_yhzw_ydc.getText().toString();
				String ydk = yhsw_yhzw_ydk.getText().toString();
				String hb = yhsw_yhzw_hb.getText().toString();
				String whzwmc = yhsw_yhzw_whzwmc.getText().toString();
				String whcd = yhsw_yhzw_whcd.getSelectedItemPosition()+"";
				String cbtj = yhsw_yhzw_cbtj.getSelectedItemPosition()+"";
				String ly = yhsw_yhzw_ly.getSelectedItemPosition()+"";
				String bhzgs = yhsw_yhzw_bhzgs.getText().toString();
				String zgzs = yhsw_yhzw_zgzs.getText().toString();
				String whl = yhsw_yhzw_whl.getText().toString();
				String whzwszzk = yhsw_yhzw_whzwszzk.getText().toString();
				String pjxj = yhsw_yhzw_pjxj.getText().toString();
				String pjg = yhsw_yhzw_pjgao.getText().toString();
				String sl = yhsw_yhzw_shuage.getText().toString();
				String ybd = yhsw_yhzw_yubidu.getText().toString();
				String dx = yhsw_yhzw_dixing.getText().toString();
				String dj = yhsw_yhzw_djing.getText().toString();
				String fblx = yhsw_yhzw_fblx.getSelectedItemPosition()+"";
				String pd = yhsw_yhzw_podu.getText().toString();
				String yksd = yhsw_yhzw_yksd.getText().toString();
				String bz = yhsw_yhzw_bz.getText().toString();
				Webservice web = new Webservice(context);
				String result = web.addYhswYhzwdcData(ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, jzmc, yhzwmc, yhzwgd, yhzwdj, whzwmc, whbw, whcd, ly, cbtj, bhzgs, zgzs, whl, whzwszzk, pjxj, pjg, sl, ybd, dx, dj, fblx, pd, yksd, bz, "1", sbr, sbsj);
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
		Button bdsave = (Button) findViewById(R.id.yhsw_yhzw_bdsave);
		bdsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_yhzw_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String dcr = yhsw_yhzw_dcr.getText().toString().trim();
				if (TextUtils.isEmpty(dcr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcrnotnull));
					return;
				}
				String dcsj = yhsw_yhzw_dcrq.getText().toString().trim();
				if (TextUtils.isEmpty(dcsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcsjnotnull));
					return;
				}
				String dcdw = yhsw_yhzw_dcdw.getText().toString().trim();
				if (TextUtils.isEmpty(dcdw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.dcdwnotnull));
					return;
				}
				String ydmj = yhsw_yhzw_ydmj.getText().toString().trim();
				if (TextUtils.isEmpty(ydmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydmjnotnull));
					return;
				}
				String yddbmj = yhsw_yhzw_yddbmj.getText().toString().trim();
				if (TextUtils.isEmpty(yddbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yddbmjnotnull));
					return;
				}
				String zhmj = yhsw_yhzw_zhmj.getText().toString().trim();
				if (TextUtils.isEmpty(zhmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.zhmjnotnull));
					return;
				}
				String ycjjd = yhsw_yhzw_ycjjd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjjd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjjdnotnull));
					return;
				}
				String ycjwd = yhsw_yhzw_ycjwd.getText().toString().trim();
				if (TextUtils.isEmpty(ycjwd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ycjwdnotnull));
					return;
				}
				String xbh = yhsw_yhzw_xbh.getText().toString().trim();
				if (TextUtils.isEmpty(xbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbhnotnull));
					return;
				}
				String xbmj = yhsw_yhzw_xbmj.getText().toString().trim();
				if (TextUtils.isEmpty(xbmj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.xbmjnotnull));
					return;
				}
				String city = yhsw_yhzw_city.getText().toString().trim();
				if (TextUtils.isEmpty(city)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.citynotnull));
					return;
				}
				String county = yhsw_yhzw_county.getText().toString().trim();
				if (TextUtils.isEmpty(county)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.countynotnull));
					return;
				}
				String town = yhsw_yhzw_town.getText().toString().trim();
				if (TextUtils.isEmpty(town)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}
				String village = yhsw_yhzw_village.getText().toString().trim();
				if (TextUtils.isEmpty(village)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.villagenotnull));
					return;
				}
				String jzmc = yhsw_yhzw_jzmc.getText().toString().trim();
				if (TextUtils.isEmpty(jzmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.jzmcnotnull));
					return;
				}
				String yhzwmc = yhsw_yhzw_yhzwmc.getText().toString().trim();
				if (TextUtils.isEmpty(yhzwmc)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yhzwmcnotnull));
					return;
				}
				String yhzwgd = yhsw_yhzw_yhzwhigh.getText().toString().trim();
				if (TextUtils.isEmpty(yhzwgd)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yhzwgdnotnull));
					return;
				}
				String yhzwdj= yhsw_yhzw_yhzwdj.getText().toString().trim();
				if (TextUtils.isEmpty(yhzwdj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.yhzwdjnotnull));
					return;
				}
				String whbw = yhsw_yhzw_whbw.getSelectedItemPosition()+"";
				if (TextUtils.isEmpty(whbw)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.whbwnotnull));
					return;
				}
				String sbr = yhsw_yhzw_sbr.getText().toString().trim();
				if (TextUtils.isEmpty(sbr)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbrnotnull));
					return;
				}
				String sbsj = yhsw_yhzw_sbsj.getText().toString().trim();
				if (TextUtils.isEmpty(sbsj)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.sbaosjnotnull));
					return;
				}
				String ydxz = yhsw_yhzw_ydxz.getSelectedItemPosition()+"";
				String ydc = yhsw_yhzw_ydc.getText().toString();
				String ydk = yhsw_yhzw_ydk.getText().toString();
				String hb = yhsw_yhzw_hb.getText().toString();
				String whzwmc = yhsw_yhzw_whzwmc.getText().toString();
				String whcd = yhsw_yhzw_whcd.getSelectedItemPosition()+"";
				String cbtj = yhsw_yhzw_cbtj.getSelectedItemPosition()+"";
				String ly = yhsw_yhzw_ly.getSelectedItemPosition()+"";
				String bhzgs = yhsw_yhzw_bhzgs.getText().toString();
				String zgzs = yhsw_yhzw_zgzs.getText().toString();
				String whl = yhsw_yhzw_whl.getText().toString();
				String whzwszzk = yhsw_yhzw_whzwszzk.getText().toString();
				String pjxj = yhsw_yhzw_pjxj.getText().toString();
				String pjg = yhsw_yhzw_pjgao.getText().toString();
				String sl = yhsw_yhzw_shuage.getText().toString();
				String ybd = yhsw_yhzw_yubidu.getText().toString();
				String dx = yhsw_yhzw_dixing.getText().toString();
				String dj = yhsw_yhzw_djing.getText().toString();
				String fblx = yhsw_yhzw_fblx.getSelectedItemPosition()+"";
				String pd = yhsw_yhzw_podu.getText().toString();
				String yksd = yhsw_yhzw_yksd.getText().toString();
				String bz = yhsw_yhzw_bz.getText().toString();
				DataBaseHelper.addYhswYhzwdcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, jzmc, yhzwmc, yhzwgd, yhzwdj, whzwmc, whbw, whcd, ly, cbtj, bhzgs, zgzs, whl, whzwszzk, pjxj, pjg, sl, ybd, dx, dj, fblx, pd, yksd, bz, sbr, sbsj);
				ToastUtil.setToast(context, context.getResources().getString(R.string.addsuccess));
				dismiss();
			}
		});

	}
}
