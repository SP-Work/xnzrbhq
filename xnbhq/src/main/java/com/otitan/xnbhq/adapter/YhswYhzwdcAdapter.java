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
 * 有害生物 有害植物管理Adapter
 */
public class YhswYhzwdcAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private LayoutInflater inflater = null;
	private YHSWActivity activity;

	public YhswYhzwdcAdapter(Context context,List<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.item_yhsw_yhzwdc, null);
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

		if (BussUtil.isEmperty(list.get(position).get("YDBH"))) {
			holder.tv1.setText(list.get(position).get("YDBH"));
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
		if (BussUtil.isEmperty(list.get(position).get("YHZWMC"))) {
			holder.tv4.setText(list.get(position).get("YHZWMC"));
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBR"))) {
			holder.tv5.setText(list.get(position).get("SBR"));
		} else {
			holder.tv5.setText("");
		}
		if (BussUtil.isEmperty(list.get(position).get("SBSJ"))) {
			String sbsj=list.get(position).get("SBSJ").replace("/", "-");
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

	/* 上报 */
	protected void ShangBao(HashMap<String, String> map) {
		Webservice web = new Webservice(context);
		String ydbh = map.get("YDBH");
		String dcr = map.get("DCR");
		String dcsj = map.get("DCSJ");
		String dcdw = map.get("DCDW");
		String ydxz = map.get("YDXZ");
		String ydc = map.get("YDC");
		String ydk = map.get("YDK");
		String ydmj = map.get("YDMJ");
		String yddbmj = map.get("YDDBMJ");
		String zhmj = map.get("ZHMJ");
		String hb = map.get("HB");
		String ycjjd = map.get("YCJLON");
		String ycjwd = map.get("YCJLAT");
		String xbh = map.get("XBH");
		String xbmj = map.get("XBMJ");
		String city = map.get("CITY");
		String county = map.get("COUNTY");
		String town = map.get("TOWN");
		String village = map.get("VILLAGE");
		String xdm = map.get("XDM");
		String jzmc = map.get("JZMC");
		String yhzwmc = map.get("YHZWMC");
		String yhzwgd = map.get("YHZWGD");
		String yhzwdj = map.get("YHZWDJ");
		String whzwmc = map.get("WHZWMC");
		String whbw = map.get("WHBW");
		String whcd = map.get("WHCD");
		String ly = map.get("LY");
		String cbtj = map.get("CBTJ");
		String bhzgs = map.get("BHZGS");
		String zgzs = map.get("ZGZS");
		String whl = map.get("WHL");
		String whzwszzk = map.get("WHZWSZZK");
		String pjxj = map.get("PJXJ");
		String pjg = map.get("PJG");
		String sl = map.get("SL");
		String ybd = map.get("YBD");
		String dx = map.get("DX");
		String dj = map.get("DJ");
		String fblx = map.get("FBLX");
		String pd = map.get("PD");
		String yksd = map.get("YKSD");
		String bz = map.get("BZ");
		String sbr = map.get("SBR");
		String sbsj = map.get("SBSJ");
		String result = web.addYhswYhzwdcData(ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, jzmc, yhzwmc, yhzwgd, yhzwdj, whzwmc, whbw, whcd, ly, cbtj, bhzgs, zgzs, whl, whzwszzk, pjxj, pjg, sl, ybd, dx, dj, fblx, pd, yksd, bz, "1", sbr, sbsj);
		String[] splits = result.split(",");
		if (splits.length > 0) {
			if ("True".equals(splits[0])) {
				DataBaseHelper.deleteYhswYhzwdcData(context, "db.sqlite",
						map.get("ID"));
				map.put("SCZT", "1");
				notifyDataSetChanged();
				ToastUtil.setToast(context,context.getResources().getString(R.string.uploadsuccess));
			}else{
				ToastUtil.setToast(context,context.getResources().getString(R.string.uploadfailed));
			}
		}else{
			ToastUtil.setToast(context,context.getResources().getString(R.string.uploadfailed));
		}
	}

	/* 编辑 */
	protected void showEditDialog(final HashMap<String, String> map) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dialog.setContentView(R.layout.yhsw_yhzwdc_add);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView yhsw_ydzwdc_head = (TextView) dialog
				.findViewById(R.id.yhsw_yhzwdc_head);
		yhsw_ydzwdc_head.setText(context.getString(R.string.yhzwdcedit));

		View status=dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		final TextView yhsw_yhzw_ydbh = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ydbh);
		yhsw_yhzw_ydbh.setText(map.get("YDBH").trim());

		final EditText yhsw_yhzw_dcr = (EditText) dialog.findViewById(R.id.yhsw_yhzw_dcr);
		yhsw_yhzw_dcr.setText(map.get("DCR").trim());

		final TextView yhsw_yhzw_dcrq = (TextView) dialog.findViewById(R.id.yhsw_yhzw_dcrq);
		yhsw_yhzw_dcrq.setText(map.get("DCSJ").trim());

		final EditText yhsw_yhzw_dcdw = (EditText) dialog.findViewById(R.id.yhsw_yhzw_dcdw);
		yhsw_yhzw_dcdw.setText(map.get("DCDW").trim());

		final EditText yhsw_yhzw_ydc = (EditText) dialog.findViewById(R.id.yhsw_yhzw_ydc);
		yhsw_yhzw_ydc.setText(map.get("YDC").trim());

		final EditText yhsw_yhzw_ydk = (EditText) dialog.findViewById(R.id.yhsw_yhzw_ydk);
		yhsw_yhzw_ydk.setText(map.get("YDK").trim());

		final Spinner yhsw_yhzw_ydxz = (Spinner) dialog.findViewById(R.id.yhsw_yhzw_ydxz);
		if(BussUtil.isEmperty(map.get("YDXZ"))){
			int a=Integer.parseInt(map.get("YDXZ"));
			yhsw_yhzw_ydxz.setSelection(a);
		}


		final EditText yhsw_yhzw_ydmj = (EditText) dialog.findViewById(R.id.yhsw_yhzw_ydmj);
		yhsw_yhzw_ydmj.setText(map.get("YDMJ").trim());

		final EditText yhsw_yhzw_zhmj = (EditText) dialog.findViewById(R.id.yhsw_yhzw_zhmj);
		yhsw_yhzw_zhmj.setText(map.get("ZHMJ").trim());

		final EditText yhsw_yhzw_yddbmj = (EditText) dialog.findViewById(R.id.yhsw_yhzw_yddbmj);
		yhsw_yhzw_yddbmj.setText(map.get("YDDBMJ").trim());

		final Spinner yhsw_yhzw_ly = (Spinner) dialog.findViewById(R.id.yhsw_yhzw_ly);
		if(BussUtil.isEmperty(map.get("LY"))){
			int a=Integer.parseInt(map.get("LY"));
			yhsw_yhzw_ly.setSelection(a);
		}

		final EditText yhsw_yhzw_hb = (EditText) dialog.findViewById(R.id.yhsw_yhzw_hb);
		yhsw_yhzw_hb.setText(map.get("HB").trim());

		final EditText yhsw_yhzw_ycjjd = (EditText) dialog.findViewById(R.id.yhsw_yhzw_ycjjd);
		yhsw_yhzw_ycjjd.setText(map.get("YCJLON").trim());

		final EditText yhsw_yhzw_ycjwd = (EditText) dialog.findViewById(R.id.yhsw_yhzw_ycjwd);
		yhsw_yhzw_ycjwd.setText(map.get("YCJLAT").trim());

		final EditText yhsw_yhzw_xbh = (EditText) dialog.findViewById(R.id.yhsw_yhzw_xbh);
		yhsw_yhzw_xbh.setText(map.get("XBH").trim());

		final EditText yhsw_yhzw_xbmj = (EditText) dialog.findViewById(R.id.yhsw_yhzw_xbmj);
		yhsw_yhzw_xbmj.setText(map.get("XBMJ").trim());

		final EditText yhsw_yhzw_city = (EditText) dialog.findViewById(R.id.yhsw_yhzw_city);
		yhsw_yhzw_city.setText(map.get("CITY").trim());

		final EditText yhsw_yhzw_county = (EditText) dialog.findViewById(R.id.yhsw_yhzw_county);
		yhsw_yhzw_county.setText(map.get("COUNTY").trim());

		final EditText yhsw_yhzw_town = (EditText) dialog.findViewById(R.id.yhsw_yhzw_town);
		yhsw_yhzw_town.setText(map.get("TOWN").trim());

		final EditText yhsw_yhzw_village = (EditText) dialog.findViewById(R.id.yhsw_yhzw_village);
		yhsw_yhzw_village.setText(map.get("VILLAGE").trim());

		final EditText yhsw_yhzw_yhzwmc = (EditText) dialog.findViewById(R.id.yhsw_yhzw_yhzwmc);
		yhsw_yhzw_yhzwmc.setText(map.get("YHZWMC").trim());

		final EditText yhsw_yhzw_jzmc = (EditText) dialog.findViewById(R.id.yhsw_yhzw_jzmc);
		yhsw_yhzw_jzmc.setText(map.get("JZMC").trim());

		final EditText yhsw_yhzw_yhzwhigh = (EditText) dialog.findViewById(R.id.yhsw_yhzw_yhzwhigh);
		yhsw_yhzw_yhzwhigh.setText(map.get("YHZWGD").trim());

		final EditText yhsw_yhzw_yhzwdj = (EditText) dialog.findViewById(R.id.yhsw_yhzw_yhzwdj);
		yhsw_yhzw_yhzwdj.setText(map.get("YHZWDJ").trim());

		final EditText yhsw_yhzw_whzwmc = (EditText) dialog.findViewById(R.id.yhsw_yhzw_whzwmc);
		yhsw_yhzw_whzwmc.setText(map.get("WHZWMC").trim());

		final Spinner yhsw_yhzw_whcd = (Spinner) dialog.findViewById(R.id.yhsw_yhzw_whcd);
		if(BussUtil.isEmperty(map.get("WHCD"))){
			int a=Integer.parseInt(map.get("WHCD"));
			yhsw_yhzw_whcd.setSelection(a);
		}

		final Spinner yhsw_yhzw_whbw = (Spinner) dialog.findViewById(R.id.yhsw_yhzw_whbw);
		if(BussUtil.isEmperty(map.get("WHBW"))){
			int a=Integer.parseInt(map.get("WHBW"));
			yhsw_yhzw_whbw.setSelection(a);
		}

		final Spinner yhsw_yhzw_cbtj = (Spinner) dialog.findViewById(R.id.yhsw_yhzw_cbtj);
		if(BussUtil.isEmperty(map.get("CBTJ"))){
			int a=Integer.parseInt(map.get("CBTJ"));
			yhsw_yhzw_cbtj.setSelection(a);
		}

		final EditText yhsw_yhzw_bhzgs = (EditText) dialog.findViewById(R.id.yhsw_yhzw_bhzgs);
		yhsw_yhzw_bhzgs.setText(map.get("BHZGS").trim());

		final EditText yhsw_yhzw_zgzs = (EditText) dialog.findViewById(R.id.yhsw_yhzw_zgzs);
		yhsw_yhzw_zgzs.setText(map.get("ZGZS").trim());

		final EditText yhsw_yhzw_whl = (EditText) dialog.findViewById(R.id.yhsw_yhzw_whl);
		yhsw_yhzw_whl.setText(map.get("WHL").trim());

		final EditText yhsw_yhzw_whzwszzk = (EditText) dialog.findViewById(R.id.yhsw_yhzw_whzwszzk);
		yhsw_yhzw_whzwszzk.setText(map.get("WHZWSZZK").trim());

		final EditText yhsw_yhzw_pjxj = (EditText) dialog.findViewById(R.id.yhsw_yhzw_pjxj);
		yhsw_yhzw_pjxj.setText(map.get("PJXJ").trim());

		final EditText yhsw_yhzw_pjgao = (EditText) dialog.findViewById(R.id.yhsw_yhzw_pjgao);
		yhsw_yhzw_pjgao.setText(map.get("PJG").trim());

		final EditText yhsw_yhzw_shuage = (EditText) dialog.findViewById(R.id.yhsw_yhzw_shuage);
		yhsw_yhzw_shuage.setText(map.get("SL").trim());

		final EditText yhsw_yhzw_yubidu = (EditText) dialog.findViewById(R.id.yhsw_yhzw_yubidu);
		yhsw_yhzw_yubidu.setText(map.get("YBD").trim());

		final EditText yhsw_yhzw_dixing = (EditText) dialog.findViewById(R.id.yhsw_yhzw_dixing);
		yhsw_yhzw_dixing.setText(map.get("DX").trim());

		final EditText yhsw_yhzw_djing = (EditText) dialog.findViewById(R.id.yhsw_yhzw_djing);
		yhsw_yhzw_djing.setText(map.get("DJ").trim());

		final Spinner yhsw_yhzw_fblx = (Spinner) dialog.findViewById(R.id.yhsw_yhzw_fblx);
		if(BussUtil.isEmperty(map.get("FBLX"))){
			int a=Integer.parseInt(map.get("FBLX"));
			yhsw_yhzw_fblx.setSelection(a);
		}

		final EditText yhsw_yhzw_podu = (EditText) dialog.findViewById(R.id.yhsw_yhzw_podu);
		yhsw_yhzw_podu.setText(map.get("PD").trim());

		final EditText yhsw_yhzw_sbr = (EditText) dialog.findViewById(R.id.yhsw_yhzw_sbr);
		yhsw_yhzw_sbr.setText(map.get("SBR").trim());

		final Button yhsw_yhzw_sbsj = (Button) dialog.findViewById(R.id.yhsw_yhzw_sbsj);
		yhsw_yhzw_sbsj.setText(map.get("SBSJ").trim());
		yhsw_yhzw_sbsj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_yhzw_sbsj, false);
			}
		});

		final EditText yhsw_yhzw_yksd = (EditText) dialog.findViewById(R.id.yhsw_yhzw_yksd);
		yhsw_yhzw_yksd.setText(map.get("YKSD").trim());

		final TextView yhsw_yhzw_sczt = (TextView) dialog
				.findViewById(R.id.yhsw_yhzw_sczt);
		if ("1".equals(map.get("SCZT").trim())) {
			yhsw_yhzw_sczt.setText(context.getResources().getString(
					R.string.haveupload));
		} else if ("0".equals(map.get("SCZT").trim())) {
			yhsw_yhzw_sczt.setText(context.getResources().getString(
					R.string.havenotupload));
		} else {
			yhsw_yhzw_sczt.setText("");
		}

		final EditText yhsw_yhzw_bz = (EditText) dialog.findViewById(R.id.yhsw_yhzw_bz);
		yhsw_yhzw_bz.setText(map.get("BZ").trim());

		Button upload = (Button) dialog
				.findViewById(R.id.yhsw_yhzw_upload);
		upload.setText(context.getResources().getString(R.string.bc));
		Button bdsave = (Button) dialog.findViewById(R.id.yhsw_yhzw_bdsave);
		bdsave.setVisibility(View.GONE);
		upload.setOnClickListener(new OnClickListener() {

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
				DataBaseHelper.updateYhswYhzwdcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, jzmc, yhzwmc, yhzwgd, yhzwdj, whzwmc, whbw, whcd, ly, cbtj, bhzgs, zgzs, whl, whzwszzk, pjxj, pjg, sl, ybd, dx, dj, fblx, pd, yksd, bz, sbr, sbsj);
				map.put("DCR", dcr);
				map.put("DCSJ", dcsj);
				map.put("DCDW", dcdw);
				map.put("YDXZ", ydxz);
				map.put("YDC", ydc);
				map.put("YDK", ydk);
				map.put("YDMJ", ydmj);
				map.put("YDDBMJ", yddbmj);
				map.put("ZHMJ", zhmj);
				map.put("HB", hb);
				map.put("YCJLON", ycjjd);
				map.put("YCJLAT", ycjwd);
				map.put("XBH", xbh);
				map.put("XBMJ", xbmj);
				map.put("CITY", city);
				map.put("COUNTY", county);
				map.put("TOWN", town);
				map.put("VILLAGE", village);
				map.put("JZMC", jzmc);
				map.put("YHZWMC", yhzwmc);
				map.put("YHZWGD", yhzwgd);
				map.put("YHZWDJ", yhzwdj);
				map.put("WHZWMC", whzwmc);
				map.put("WHBW", whbw);
				map.put("WHCD", whcd);
				map.put("LY", ly);
				map.put("CBTJ", cbtj);
				map.put("BHZGS", bhzgs);
				map.put("ZGZS", zgzs);
				map.put("WHL", whl);
				map.put("WHZWSZZK", whzwszzk);
				map.put("PJXJ", pjxj);
				map.put("PJG", pjg);
				map.put("SL", sl);
				map.put("YBD", ybd);
				map.put("DX", dx);
				map.put("DJ", dj);
				map.put("FBLX", fblx);
				map.put("PD", pd);
				map.put("YKSD", yksd);
				map.put("BZ", bz);
				map.put("SCZT", "0");
				map.put("SBR", sbr);
				map.put("SBSJ", sbsj);
				ToastUtil.setToast(context,context.getResources().getString(R.string.editsuccess));
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		Button cancle = (Button) dialog.findViewById(R.id.yhsw_yhzw_cancle);
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
		dialog.setContentView(R.layout.yhsw_yhzwdc_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		TextView ydbh = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ydbh);
		ydbh.setText(map.get("YDBH").trim());

		TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_yhzw_dcr);
		dcr.setText(map.get("DCR").trim());

		TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_dcrq);
		dcsj.setText(map.get("DCSJ").replace("/", "-"));

		TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_yhzw_dcdw);
		dcdw.setText(map.get("DCDW").trim());

		TextView ydc = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ydc);
		ydc.setText(map.get("YDC").trim());

		TextView ydk = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ydk);
		ydk.setText(map.get("YDK").trim());

		TextView ydxz = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ydxz);
		if(BussUtil.isEmperty(map.get("YDXZ").trim())){
			int a=Integer.parseInt(map.get("YDXZ").trim());
			String[]array=context.getResources().getStringArray(R.array.ydxz);
			ydxz.setText(array[a]);
		}

		TextView ydmj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ydmj);
		ydmj.setText(map.get("YDMJ").trim());

		TextView yddbmj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_yddbmj);
		yddbmj.setText(map.get("YDDBMJ").trim());

		TextView zhmj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_zhmj);
		zhmj.setText(map.get("ZHMJ").trim());

		TextView ly = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ly);
		if(BussUtil.isEmperty(map.get("LY").trim())){
			int a=Integer.parseInt(map.get("LY").trim());
			String[]array=context.getResources().getStringArray(R.array.ly);
			ly.setText(array[a]);
		}

		TextView hb = (TextView) dialog.findViewById(R.id.yhsw_yhzw_hb);
		hb.setText(map.get("HB").trim());

		TextView ycjjd = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ycjjd);
		ycjjd.setText(map.get("YCJLON").trim());

		TextView ycjwd = (TextView) dialog.findViewById(R.id.yhsw_yhzw_ycjwd);
		ycjwd.setText(map.get("YCJLAT").trim());

		TextView xbh = (TextView) dialog.findViewById(R.id.yhsw_yhzw_xbh);
		xbh.setText(map.get("XBH").trim());

		TextView xbmj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_xbmj);
		xbmj.setText(map.get("XBMJ").trim());

		TextView city = (TextView) dialog.findViewById(R.id.yhsw_yhzw_city);
		city.setText(map.get("CITY").trim());

		TextView county = (TextView) dialog.findViewById(R.id.yhsw_yhzw_county);
		county.setText(map.get("COUNTY").trim());

		TextView town = (TextView) dialog.findViewById(R.id.yhsw_yhzw_town);
		town.setText(map.get("TOWN").trim());

		TextView village = (TextView) dialog.findViewById(R.id.yhsw_yhzw_village);
		village.setText(map.get("VILLAGE").trim());

		TextView yhzwmc = (TextView) dialog.findViewById(R.id.yhsw_yhzw_yhzwmc);
		yhzwmc.setText(map.get("YHZWMC").trim());

		TextView jzmc = (TextView) dialog.findViewById(R.id.yhsw_yhzw_jzmc);
		jzmc.setText(map.get("JZMC").trim());

		TextView yhzwgd = (TextView) dialog.findViewById(R.id.yhsw_yhzw_yhzwhigh);
		yhzwgd.setText(map.get("YHZWGD").trim());

		TextView yhzwdj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_yhzwdj);
		yhzwdj.setText(map.get("YHZWDJ").trim());

		TextView whzwmc = (TextView) dialog.findViewById(R.id.yhsw_yhzw_whzwmc);
		whzwmc.setText(map.get("WHZWMC").trim());

		TextView whcd = (TextView) dialog.findViewById(R.id.yhsw_yhzw_whcd);
		if(BussUtil.isEmperty(map.get("WHCD").trim())){
			int a=Integer.parseInt(map.get("WHCD").trim());
			String[]array=context.getResources().getStringArray(R.array.mcwhcd);
			whcd.setText(array[a]);
		}

		TextView whbw = (TextView) dialog.findViewById(R.id.yhsw_yhzw_whbw);
		if(BussUtil.isEmperty(map.get("WHBW").trim())){
			int a=Integer.parseInt(map.get("WHBW").trim());
			String[]array=context.getResources().getStringArray(R.array.whbw);
			whbw.setText(array[a]);
		}

		TextView cbtj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_cbtj);
		if(BussUtil.isEmperty(map.get("CBTJ").trim())){
			int a=Integer.parseInt(map.get("CBTJ").trim());
			String[]array=context.getResources().getStringArray(R.array.cbtj);
			cbtj.setText(array[a]);
		}

		TextView bhzgs = (TextView) dialog.findViewById(R.id.yhsw_yhzw_bhzgs);
		bhzgs.setText(map.get("BHZGS").trim());

		TextView zgzs = (TextView) dialog.findViewById(R.id.yhsw_yhzw_zgzs);
		zgzs.setText(map.get("ZGZS").trim());

		TextView whl = (TextView) dialog.findViewById(R.id.yhsw_yhzw_whl);
		whl.setText(map.get("WHL").trim());

		TextView whzwszzk = (TextView) dialog.findViewById(R.id.yhsw_yhzw_whzwszzk);
		whzwszzk.setText(map.get("WHZWSZZK").trim());

		TextView pjxj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_pjxj);
		pjxj.setText(map.get("PJXJ").trim());

		TextView pjg = (TextView) dialog.findViewById(R.id.yhsw_yhzw_pjgao);
		pjg.setText(map.get("PJG").trim());

		TextView sl = (TextView) dialog.findViewById(R.id.yhsw_yhzw_shuage);
		sl.setText(map.get("SL").trim());

		TextView ybd = (TextView) dialog.findViewById(R.id.yhsw_yhzw_yubidu);
		ybd.setText(map.get("YBD").trim());

		TextView dx = (TextView) dialog.findViewById(R.id.yhsw_yhzw_dixing);
		dx.setText(map.get("DX").trim());

		TextView dj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_djing);
		dj.setText(map.get("DJ").trim());

		TextView fblx = (TextView) dialog.findViewById(R.id.yhsw_yhzw_fblx);
		if(BussUtil.isEmperty(map.get("FBLX").trim())){
			int a=Integer.parseInt(map.get("FBLX").trim());
			String[]array=context.getResources().getStringArray(R.array.fblxyhzw);
			fblx.setText(array[a]);
		}

		TextView podu = (TextView) dialog.findViewById(R.id.yhsw_yhzw_podu);
		podu.setText(map.get("PD").trim());

		TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_yhzw_sbr);
		sbr.setText(map.get("SBR").trim());

		TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_yhzw_sbsj);
		sbsj.setText(map.get("SBSJ").replace("/", "-"));

		TextView yksd = (TextView) dialog.findViewById(R.id.yhsw_yhzw_yksd);
		yksd.setText(map.get("YKSD").trim());

		TextView bz = (TextView) dialog.findViewById(R.id.yhsw_yhzw_bz);
		bz.setText(map.get("BZ").trim());

		View status=dialog.findViewById(R.id.status);
		status.setVisibility(View.VISIBLE);

		TextView uploadstatus = (TextView) dialog
				.findViewById(R.id.yhsw_yhzw_sczt);
		if (BussUtil.isEmperty(map.get("SCZT").trim())) {
			if ("1".equals(map.get("SCZT").trim())) {
				uploadstatus.setText(context.getResources().getString(
						R.string.haveupload));
			} else if ("0".equals(map.get("SCZT").trim())) {
				uploadstatus.setText(context.getResources().getString(
						R.string.havenotupload));
			}
		} else {
			uploadstatus.setText("");
		}
		Button back = (Button) dialog.findViewById(R.id.yhsw_yhzw_back);
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
