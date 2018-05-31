package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 未成林造林地调查信息
 */
public class SlzylxqcWclzldcDialog extends Dialog implements
		View.OnClickListener {
	Context context;
	HashMap<String, String> map;
	List<HashMap<String, String>> list = new ArrayList<>();
	private TextView wclzlqk;
	private TextView zlnd;
	private TextView ml;
	private TextView czmd;
	private TextView mmchl;
	private TextView szhong1;
	private TextView bli1;
	private TextView szhong2;
	private TextView bli2;
	private TextView szhong3;
	private TextView bli3;
	private TextView szhong4;
	private TextView bli4;
	String ydhselect;
	public SlzylxqcWclzldcDialog(Context context, String ydhselect) {
		super(context, R.style.Dialog);
		this.context = context;
		this.ydhselect=ydhselect;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_wclzlddc_view);
		setCanceledOnTouchOutside(false);
		map = new HashMap<String, String>();
		list = new ArrayList<HashMap<String, String>>();
		final TextView ydh = (TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);
		wclzlqk = (TextView) findViewById(R.id.wclzlqk);
		wclzlqk.setOnClickListener(this);
		zlnd = (TextView) findViewById(R.id.zlnd);
		zlnd.setOnClickListener(this);
		ml = (TextView) findViewById(R.id.ml);
		ml.setOnClickListener(this);
		czmd = (TextView) findViewById(R.id.czmd);
		czmd.setOnClickListener(this);
		mmchl = (TextView) findViewById(R.id.mmchl);
		mmchl.setOnClickListener(this);

		szhong1 = (TextView) findViewById(R.id.szhong1);
		szhong1.setOnClickListener(this);
		bli1 = (TextView) findViewById(R.id.bli1);
		bli1.setOnClickListener(this);

		szhong2 = (TextView) findViewById(R.id.szhong2);
		szhong2.setOnClickListener(this);
		bli2 = (TextView) findViewById(R.id.bli2);
		bli2.setOnClickListener(this);

		szhong3 = (TextView) findViewById(R.id.szhong3);
		szhong3.setOnClickListener(this);
		bli3 = (TextView) findViewById(R.id.bli3);
		bli3.setOnClickListener(this);

		szhong4 = (TextView) findViewById(R.id.szhong4);
		szhong4.setOnClickListener(this);
		bli4 = (TextView) findViewById(R.id.bli4);
		bli4.setOnClickListener(this);

		final CheckBox ggai = (CheckBox) findViewById(R.id.ggai);
		final CheckBox bzhi = (CheckBox) findViewById(R.id.bzhi);
		final CheckBox sfei = (CheckBox) findViewById(R.id.sfei);
		final CheckBox fyv = (CheckBox) findViewById(R.id.fyv);
		final CheckBox ghu = (CheckBox) findViewById(R.id.ghu);
		final List<HashMap<String, String>> list = DataBaseHelper
				.searchLxqcWclzlddcData(context, ydhselect);

		if (list.size() != 0) {
			HashMap<String, String> hashmap = list.get(0);
			wclzlqk.setText(hashmap.get("ZLDQK"));
			zlnd.setText(hashmap.get("ZLND"));
			ml.setText(hashmap.get("ML"));
			czmd.setText(hashmap.get("CZMD"));
			mmchl.setText(hashmap.get("MMCH"));
			/**抚育管护措施*/
			String[] fyghcs = hashmap.get("FYGHCS").split("-");
			for (int i = 0; i < fyghcs.length; i++) {
				if (ggai.getText().toString().equals(fyghcs[i])) {
					ggai.setChecked(true);
					continue;
				}
				if (bzhi.getText().toString().equals(fyghcs[i])) {
					bzhi.setChecked(true);
					continue;
				}
				if (sfei.getText().toString().equals(fyghcs[i])) {
					sfei.setChecked(true);
					continue;
				}
				if (fyv.getText().toString().equals(fyghcs[i])) {
					fyv.setChecked(true);
					continue;
				}
				if (ghu.getText().toString().equals(fyghcs[i])) {
					ghu.setChecked(true);
					continue;
				}
			}
			/**树种组成*/
			String[] szzcbf = hashmap.get("SZZC").split(",", -1);
			if (szzcbf.length != 0) {
				szhong1.setText(szzcbf[0]);
				bli1.setText(szzcbf[1]);

				szhong2.setText(szzcbf[2]);
				bli2.setText(szzcbf[3]);

				szhong3.setText(szzcbf[4]);
				bli3.setText(szzcbf[5]);

				szhong4.setText(szzcbf[6]);
				bli4.setText(szzcbf[7]);
			}
		}
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int bl1 = 0;
				int bl2 = 0;
				int bl3 = 0;
				int bl4 = 0;
				if (!"".equals(bli1.getText().toString())) {
					bl1 = Integer.parseInt(bli1.getText().toString());
				}
				if (!"".equals(bli2.getText().toString())) {
					bl2 = Integer.parseInt(bli2.getText().toString());
				}
				if (!"".equals(bli3.getText().toString())) {
					bl3 = Integer.parseInt(bli3.getText().toString());
				}
				if (!"".equals(bli4.getText().toString())) {
					bl4 = Integer.parseInt(bli4.getText().toString());
				}
				if ((bl1 + bl2 + bl3 + bl4) != 10
						&& (bl1 + bl2 + bl3 + bl4) != 0) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.szzcblnottrue));
					return;
				}
				String fyghcs = "";
				if (ggai.isChecked()) {
					fyghcs = fyghcs + ggai.getText().toString();
				}
				if (bzhi.isChecked()) {
					fyghcs = fyghcs + "-" + bzhi.getText().toString();
				}
				if (sfei.isChecked()) {
					fyghcs = fyghcs + "-" + sfei.getText().toString();
				}
				if (fyv.isChecked()) {
					fyghcs = fyghcs + "-" + fyv.getText().toString();
				}
				if (ghu.isChecked()) {
					fyghcs = fyghcs + "-" + ghu.getText().toString();
				}

				String wclzlqktx = wclzlqk.getText().toString();
				String zlndtx = zlnd.getText().toString();
				String mltx = ml.getText().toString();
				String czmdtx = czmd.getText().toString();
				String mmchltx = mmchl.getText().toString();

				String sz1 = szhong1.getText().toString();
				String sz2 = szhong2.getText().toString();
				String sz3 = szhong3.getText().toString();
				String sz4 = szhong4.getText().toString();
				String b1, b2, b3, b4;
				if (bl1 == 0) {
					b1 = "";
				} else {
					b1 = bl1 + "";
				}
				if (bl2 == 0) {
					b2 = "";
				} else {
					b2 = bl2 + "";
				}
				if (bl3 == 0) {
					b3 = "";
				} else {
					b3 = bl3 + "";
				}
				if (bl4 == 0) {
					b4 = "";
				} else {
					b4 = bl4 + "";
				}
				String szzc = sz1 + "," + b1 + "," + sz2 + "," + b2 + "," + sz3
						+ "," + b3 + "," + sz4 + "," + b4;
				DataBaseHelper.deleteLxqcWclzlddcAllData(context, ydhselect);
				DataBaseHelper.addLxqcWclzlddcData(context, ydhselect,
						wclzlqktx, zlndtx, mltx, czmdtx, mmchltx, fyghcs,
						szzc);
				ToastUtil.setToast(context,context.getResources().getString(R.string.editsuccess));
				dismiss();
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			/**未成林造林情况*/
			case R.id.wclzlqk:
				List<Row> wclzlqklist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "wclzlqk");
				XzzyDialog dialog = new XzzyDialog(context,"未成林造林情况", wclzlqklist, wclzlqk);
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);

				break;
			/**造林年度*/
			case R.id.zlnd:
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"造林年度", zlnd, map, "", "", "", "",list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				break;
			/**苗龄*/
			case R.id.ml:
				ShuziYxclDialog shuzimldialog = new ShuziYxclDialog(context,"苗龄", ml, map, "", "", "", "", list, "1", "", null, null, null, "",
						null, null, "");
				BussUtil.setDialogParams(context, shuzimldialog, 0.5, 0.5);
				break;
			/**初植密度*/
			case R.id.czmd:
				ShuziYxclDialog shuziczmddialog = new ShuziYxclDialog(context,"初植密度(株/公顷)", czmd, map, "", "", "", "", list, "1", "", null, null, null,
						"", null, null, "");
				BussUtil.setDialogParams(context, shuziczmddialog, 0.5, 0.5);
				break;
			/**苗木成活率*/
			case R.id.mmchl:
				ShuziYxclDialog shuzimmchldialog = new ShuziYxclDialog(context,"苗木成活(保存)率(%)", mmchl, map, "", "", "", "", list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzimmchldialog, 0.5, 0.5);
				break;
			/**树种1*/
			case R.id.szhong1:
				List<Row> szlist1 =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog szzcdialog1 = new XzzyDialog(context,"树种", szlist1, szhong1);
				BussUtil.setDialogParams(context, szzcdialog1, 0.5, 0.5);
				break;
			/**树种2*/
			case R.id.szhong2:
				List<Row> szlist2 =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog szzcdialog2 = new XzzyDialog(context,"树种", szlist2, szhong2);
				BussUtil.setDialogParams(context, szzcdialog2, 0.5, 0.5);
				break;
			/**树种3*/
			case R.id.szhong3:
				List<Row> szlist3 =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog szzcdialog3 = new XzzyDialog(context,"树种", szlist3, szhong3);
				BussUtil.setDialogParams(context, szzcdialog3, 0.5, 0.5);
				break;
			/**树种4*/
			case R.id.szhong4:
				List<Row> szlist4 =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog szzcdialog4 = new XzzyDialog(context,"树种", szlist4, szhong4);
				BussUtil.setDialogParams(context, szzcdialog4, 0.5, 0.5);
				break;
			/**比例1*/
			case R.id.bli1:
				ShuziYxclDialog bl1dialog = new ShuziYxclDialog(context,"比例", bli1, map, "", "", "", "", list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, bl1dialog, 0.5, 0.5);
				break;
			/**比例2*/
			case R.id.bli2:
				ShuziYxclDialog bl2dialog = new ShuziYxclDialog(context, "比例", bli2, map, "", "", "", "",list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, bl2dialog, 0.5, 0.5);
				break;
			/**比例3*/
			case R.id.bli3:
				ShuziYxclDialog bl3dialog = new ShuziYxclDialog(context,"比例", bli3, map, "", "", "", "", list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, bl3dialog, 0.5, 0.5);
				break;
			/**比例4*/
			case R.id.bli4:
				ShuziYxclDialog bl4dialog = new ShuziYxclDialog(context,"比例", bli4, map, "", "", "", "",list, "1", "", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, bl4dialog, 0.5, 0.5);
				break;
		}
	}

}
