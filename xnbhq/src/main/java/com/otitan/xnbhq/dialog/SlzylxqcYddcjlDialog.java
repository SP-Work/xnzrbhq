package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 样地调查记录
 */
public class SlzylxqcYddcjlDialog extends Dialog implements View.OnClickListener {
	List<HashMap<String, String>> list;
	Context context;
	HashMap<String, String> map;
	private TextView ydh, ztmc, ydxz, ydmj, ydhzb, ydzzb, ydjj, dxttfh1, dxttfh2, wph,
			lbxbh, lgzdh, dfxzbm, linyxzbh, dishizhou, xiansq, xiangz, cun,
			xdm, zrbhq, slgy, guoylc, jtlc, lyqyj, dcy1, gzdw1, dcy2, gzdw2,
			dcy3, gzdw3, xd1, gzdwdz1, xd2, gzdwdz2, xd3, gzdwdz3, jiancy1,
			jcygzdw1, jiancy2, jiancygzdw2, jiancy3, jiancygzdw3, diaocrq,
			jcrq, location;
	String ydhselect;
	String[] zd;
	/**样地调查记录表*/
	public SlzylxqcYddcjlDialog(Context context,
								List<HashMap<String, String>> list, String ydhselect,
								TextView location) {
		super(context, R.style.Dialog);
		this.list = list;
		this.context = context;
		this.ydhselect = ydhselect;
		this.location = location;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_yddcjlb_view);
		zd = context.getResources().getStringArray(R.array.yddcjlbzd);
		if (list.size() > 0) {
			map = list.get(0);
		} else {
			list = new ArrayList<HashMap<String, String>>();
			map = new HashMap<String, String>();
			for (int i = 0; i < zd.length; i++) {
				if (zd[i].equals("YDH")) {
					map.put(zd[i], ydhselect);
				} else {
					map.put(zd[i], "");
				}
			}
			list.add(map);
		}
		if (map != null && "".equals(map.get("ZTMC"))) {
			map.put("ZTMC", context.getResources().getString(R.string.lxqcztmc));
		}
		if (map != null && "".equals(map.get("YDXZ"))) {
			map.put("YDXZ", context.getResources().getString(R.string.lxqcydxz));
		}
		if (map != null && "".equals(map.get("YDMJ"))) {
			map.put("YDMJ", context.getResources().getString(R.string.lxqcydmj));
		}
		if (map != null && "".equals(map.get("YDMJ"))) {
			map.put("YDJJ", context.getResources().getString(R.string.lxqcydjj));
		}

		ydh = (TextView) findViewById(R.id.ydh);
		ztmc = (TextView) findViewById(R.id.ztmc);
		ztmc.setOnClickListener(this);
		ydxz = (TextView) findViewById(R.id.ydxz);
		ydxz.setOnClickListener(this);
		ydmj = (TextView) findViewById(R.id.ydmj);
		ydmj.setOnClickListener(this);
		ydhzb = (TextView) findViewById(R.id.ydhzb);
		ydzzb = (TextView) findViewById(R.id.ydzzb);
		ydjj = (TextView) findViewById(R.id.ydjj);
		ydjj.setOnClickListener(this);
		dxttfh1 = (TextView) findViewById(R.id.dxttfh1);
		dxttfh1.setOnClickListener(this);
		dxttfh2 = (TextView) findViewById(R.id.dxttfh2);
		dxttfh2.setOnClickListener(this);
		wph = (TextView) findViewById(R.id.wph);
		wph.setOnClickListener(this);
		lbxbh = (TextView) findViewById(R.id.lbxbh);
		lbxbh.setOnClickListener(this);
		lgzdh = (TextView) findViewById(R.id.lgzdh);
		lgzdh.setOnClickListener(this);
		dfxzbm = (TextView) findViewById(R.id.dfxzbm);
		dfxzbm.setOnClickListener(this);
		linyxzbh = (TextView) findViewById(R.id.linyxzbh);
		linyxzbh.setOnClickListener(this);
		dishizhou = (TextView) findViewById(R.id.dishizhou);
		dishizhou.setOnClickListener(this);
		xiansq = (TextView) findViewById(R.id.xiansq);
		xiansq.setOnClickListener(this);
		xiangz = (TextView) findViewById(R.id.xiangz);
		xiangz.setOnClickListener(this);
		cun = (TextView) findViewById(R.id.cun);
		cun.setOnClickListener(this);
		xdm = (TextView) findViewById(R.id.xdm);
		xdm.setOnClickListener(this);
		zrbhq = (TextView) findViewById(R.id.zrbhq);
		zrbhq.setOnClickListener(this);
		slgy = (TextView) findViewById(R.id.slgy);
		slgy.setOnClickListener(this);
		guoylc = (TextView) findViewById(R.id.guoylc);
		guoylc.setOnClickListener(this);
		jtlc = (TextView) findViewById(R.id.jtlc);
		jtlc.setOnClickListener(this);
		lyqyj = (TextView) findViewById(R.id.lyqyj);
		lyqyj.setOnClickListener(this);
		dcy1 = (TextView) findViewById(R.id.dcy1);
		dcy1.setOnClickListener(this);
		gzdw1 = (TextView) findViewById(R.id.gzdw1);
		gzdw1.setOnClickListener(this);
		dcy2 = (TextView) findViewById(R.id.dcy2);
		dcy2.setOnClickListener(this);
		gzdw2 = (TextView) findViewById(R.id.gzdw2);
		gzdw2.setOnClickListener(this);
		dcy3 = (TextView) findViewById(R.id.dcy3);
		dcy3.setOnClickListener(this);
		gzdw3 = (TextView) findViewById(R.id.gzdw3);
		gzdw3.setOnClickListener(this);
		xd1 = (TextView) findViewById(R.id.xd1);
		xd1.setOnClickListener(this);
		gzdwdz1 = (TextView) findViewById(R.id.gzdwdz1);
		gzdwdz1.setOnClickListener(this);
		xd2 = (TextView) findViewById(R.id.xd2);
		xd2.setOnClickListener(this);
		gzdwdz2 = (TextView) findViewById(R.id.gzdwdz2);
		gzdwdz2.setOnClickListener(this);
		xd3 = (TextView) findViewById(R.id.xd3);
		xd3.setOnClickListener(this);
		gzdwdz3 = (TextView) findViewById(R.id.gzdwdz3);
		gzdwdz3.setOnClickListener(this);
		jiancy1 = (TextView) findViewById(R.id.jiancy1);
		jiancy1.setOnClickListener(this);
		jcygzdw1 = (TextView) findViewById(R.id.jcygzdw1);
		jcygzdw1.setOnClickListener(this);
		jiancy2 = (TextView) findViewById(R.id.jiancy2);
		jiancy2.setOnClickListener(this);
		jiancygzdw2 = (TextView) findViewById(R.id.jiancygzdw2);
		jiancygzdw2.setOnClickListener(this);
		jiancy3 = (TextView) findViewById(R.id.jiancy3);
		jiancy3.setOnClickListener(this);
		jiancygzdw3 = (TextView) findViewById(R.id.jiancygzdw3);
		jiancygzdw3.setOnClickListener(this);
		diaocrq = (TextView) findViewById(R.id.diaocrq);
		diaocrq.setOnClickListener(this);
		jcrq = (TextView) findViewById(R.id.jcrq);
		jcrq.setOnClickListener(this);
		if (map != null) {
			ydh.setText(map.get("YDH"));
			ztmc.setText(map.get("ZTMC"));
			ydxz.setText(map.get("YDXZ"));
			ydmj.setText(map.get("YDMJ"));
			ydhzb.setText(map.get("YDHZB"));
			ydzzb.setText(map.get("YDZZB"));
			ydjj.setText(map.get("YDJJ"));
			dxttfh1.setText(map.get("DXTTFH1"));
			dxttfh2.setText(map.get("DXTTFH2"));
			wph.setText(map.get("WPH"));
			lbxbh.setText(map.get("LBXBH"));
			lgzdh.setText(map.get("LGZDH"));
			dfxzbm.setText(map.get("DFXZBM"));
			linyxzbh.setText(map.get("LYXZBM"));
			dishizhou.setText(map.get("DI"));
			xiansq.setText(map.get("XIAN"));
			xiangz.setText(map.get("XIANG"));
			cun.setText(map.get("CUN"));
			xdm.setText(map.get("XDM"));
			zrbhq.setText(map.get("ZRBHQ"));
			slgy.setText(map.get("SLGY"));
			guoylc.setText(map.get("GYLC"));
			jtlc.setText(map.get("JTLC"));
			lyqyj.setText(map.get("LYQYJ"));
			dcy1.setText(map.get("DCY1"));
			gzdw1.setText(map.get("DCYGZDW1"));
			dcy2.setText(map.get("DCY2"));
			gzdw2.setText(map.get("DCYGZDW2"));
			dcy3.setText(map.get("DCY3"));
			gzdw3.setText(map.get("DCYGZDW3"));
			xd1.setText(map.get("XD1"));
			gzdwdz1.setText(map.get("DWJDZ1"));
			xd2.setText(map.get("XD2"));
			gzdwdz2.setText(map.get("DWJDZ2"));
			xd3.setText(map.get("XD3"));
			gzdwdz3.setText(map.get("DWJDZ3"));
			gzdwdz3.setText(map.get("DWJDZ3"));
			jiancy1.setText(map.get("JCY1"));
			jcygzdw1.setText(map.get("JCYGZDW1"));
			jiancy2.setText(map.get("JCY2"));
			jiancygzdw2.setText(map.get("JCYGZDZ2"));
			jiancy3.setText(map.get("JCY3"));
			jiancygzdw3.setText(map.get("JCYGZDZ3"));
			diaocrq.setText(map.get("DCRQ"));
			jcrq.setText(map.get("JCRQ"));
		}
		Button save = (Button) findViewById(R.id.save);
		Button cancle = (Button) findViewById(R.id.cancle);
		save.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			/** 总体名称 */
			case R.id.ztmc:
				LxqcUtil.showAlertDialog(context, "总体名称", ztmc, map, "ZTMC", "",
						"", "1");
				break;
			/** 样地形状 */
			case R.id.ydxz:
				LxqcUtil.showAlertDialog(context, "样地形状", ydxz, map, "YDXZ", "",
						"", "1");
				break;
			/** 样地面积 */
			case R.id.ydmj:
				LxqcUtil.showAlertDialog(context, "样地面积", ydmj, map, "YDMJ", "",
						"", "1");
				break;
			/** 样地间距 */
			case R.id.ydjj:
				LxqcUtil.showAlertDialog(context, "样地间距", ydjj, map, "YDJJ", "",
						"", "1");
				break;
			/** 地形图图幅号(1:5万) */
			case R.id.dxttfh1:
				HzbjDialog dxttfh1dialog = new HzbjDialog(context, "地形图图幅号(1:5万)",
						dxttfh1, map, "DXTTFH1");
				BussUtil.setDialogParams(context, dxttfh1dialog, 0.5, 0.5);
				break;
			/** 卫片号 */
			case R.id.wph:
				HzbjDialog wphdialog = new HzbjDialog(context, "卫片号", wph, map,
						"WPH");
				BussUtil.setDialogParams(context, wphdialog, 0.5, 0.5);
				break;
			/** 地形图图幅号(1:1万) */
			case R.id.dxttfh2:
				HzbjDialog dxttfh2dialog = new HzbjDialog(context, "地形图图幅号(1:1万)",
						dxttfh2, map, "DXTTFH2");
				BussUtil.setDialogParams(context, dxttfh2dialog, 0.5, 0.5);
				break;
			/** 林保小班号 */
			case R.id.lbxbh:
				HzbjDialog lbxbhdialog = new HzbjDialog(context, "林保小班号", lbxbh,
						map, "LBXBH");
				BussUtil.setDialogParams(context, lbxbhdialog, 0.5, 0.5);
				break;
			/** 林改宗地号 */
			case R.id.lgzdh:
				HzbjDialog lgzdhdialog = new HzbjDialog(context, "林改宗地号", lgzdh,
						map, "LGZDH");
				BussUtil.setDialogParams(context, lgzdhdialog, 0.5, 0.5);
				break;
			/** 地方行政编码 */
			case R.id.dfxzbm:
				HzbjDialog dfxzbmdialog = new HzbjDialog(context, "地方行政编码", dfxzbm,
						map, "DFXZBM");
				BussUtil.setDialogParams(context, dfxzbmdialog, 0.5, 0.5);
				break;
			/** 林业行政编码 */
			case R.id.linyxzbh:
				HzbjDialog linyxzbhdialog = new HzbjDialog(context, "林业行政编码",
						linyxzbh, map, "LYXZBM");
				BussUtil.setDialogParams(context, linyxzbhdialog, 0.5, 0.5);
				break;
			/** 地(市、州) */
			case R.id.dishizhou:
				List<Row> dishizhoulist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "SHI");
				XzzyDialog dishizhoudialog = new XzzyDialog(context, "地(市、州)",
						dishizhoulist, dishizhou, map, "DI");
				BussUtil.setDialogParams(context, dishizhoudialog, 0.5, 0.5);
				break;
			/** 县(市、区) */
			case R.id.xiansq:
				List<Row> xiansqlist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "XIAN");
				XzzyDialog xiansqdialog = new XzzyDialog(context, "县(市、区)",
						xiansqlist, xiansq, map, "XIAN");
				BussUtil.setDialogParams(context, xiansqdialog, 0.5, 0.5);
				break;
			/** 乡(镇) */
			case R.id.xiangz:
				HzbjDialog xiangzdialog = new HzbjDialog(context, "乡(镇)", xiangz,
						map, "XIANG");
				BussUtil.setDialogParams(context, xiangzdialog, 0.5, 0.5);
				break;
			/** 村 */
			case R.id.cun:
				HzbjDialog cundialog = new HzbjDialog(context, "村", cun, map, "CUN");
				BussUtil.setDialogParams(context, cundialog, 0.5, 0.5);
				break;
			/** 小地名 */
			case R.id.xdm:
				HzbjDialog xdmdialog = new HzbjDialog(context, "小地名", xdm, map,
						"XDM");
				BussUtil.setDialogParams(context, xdmdialog, 0.5, 0.5);
				break;
			/** 自然保护区 */
			case R.id.zrbhq:
				HzbjDialog zrbhqdialog = new HzbjDialog(context, "自然保护区", zrbhq,
						map, "ZRBHQ");
				BussUtil.setDialogParams(context, zrbhqdialog, 0.5, 0.5);
				break;
			/** 森林公园 */
			case R.id.slgy:
				HzbjDialog slgydialog = new HzbjDialog(context, "森林公园", slgy, map,
						"SLGY");
				BussUtil.setDialogParams(context, slgydialog, 0.5, 0.5);
				break;
			/** 国有林场 */
			case R.id.guoylc:
				HzbjDialog guoylcdialog = new HzbjDialog(context, "国有林场", guoylc,
						map, "GYLC");
				BussUtil.setDialogParams(context, guoylcdialog, 0.5, 0.5);
				break;
			/** 集体林场 */
			case R.id.jtlc:
				HzbjDialog jtlcdialog = new HzbjDialog(context, "集体林场", jtlc, map,
						"JTLC");
				BussUtil.setDialogParams(context, jtlcdialog, 0.5, 0.5);
				break;
			/** 林业企业局 */
			case R.id.lyqyj:
				HzbjDialog lyqyjdialog = new HzbjDialog(context, "林业企业局", lyqyj,
						map, "LYQYJ");
				BussUtil.setDialogParams(context, lyqyjdialog, 0.5, 0.5);
				break;
			/** 调查员1 */
			case R.id.dcy1:
				HzbjDialog dcy1dialog = new HzbjDialog(context, "调查员", dcy1, map,
						"DCY1");
				BussUtil.setDialogParams(context, dcy1dialog, 0.5, 0.5);
				break;
			/** 工作单位1 */
			case R.id.gzdw1:
				HzbjDialog gzdw1dialog = new HzbjDialog(context, "工作单位", gzdw1,
						map, "DCYGZDW1");
				BussUtil.setDialogParams(context, gzdw1dialog, 0.5, 0.5);
				break;
			/** 调查员2 */
			case R.id.dcy2:
				HzbjDialog dcy2dialog = new HzbjDialog(context, "调查员", dcy2, map,
						"DCY2");
				BussUtil.setDialogParams(context, dcy2dialog, 0.5, 0.5);
				break;
			/** 工作单位2 */
			case R.id.gzdw2:
				HzbjDialog gzdw2dialog = new HzbjDialog(context, "工作单位", gzdw2,
						map, "DCYGZDW2");
				BussUtil.setDialogParams(context, gzdw2dialog, 0.5, 0.5);
				break;
			/** 调查员3 */
			case R.id.dcy3:
				HzbjDialog dcy3dialog = new HzbjDialog(context, "调查员", dcy3, map,
						"DCY3");
				BussUtil.setDialogParams(context, dcy3dialog, 0.5, 0.5);
				break;
			/** 工作单位3 */
			case R.id.gzdw3:
				HzbjDialog gzdw3dialog = new HzbjDialog(context, "工作单位", gzdw3,
						map, "DCYGZDW3");
				BussUtil.setDialogParams(context, gzdw3dialog, 0.5, 0.5);
				break;
			/** 向导1 */
			case R.id.xd1:
				HzbjDialog xd1dialog = new HzbjDialog(context, "向导", xd1, map,
						"XD1");
				BussUtil.setDialogParams(context, xd1dialog, 0.5, 0.5);
				break;
			/** 工作单位地址1 */
			case R.id.gzdwdz1:
				HzbjDialog gzdwdz1dialog = new HzbjDialog(context, "工作单位地址",
						gzdwdz1, map, "DWJDZ1");
				BussUtil.setDialogParams(context, gzdwdz1dialog, 0.5, 0.5);
				break;
			/** 向导2 */
			case R.id.xd2:
				HzbjDialog xd2dialog = new HzbjDialog(context, "向导", xd2, map,
						"XD2");
				BussUtil.setDialogParams(context, xd2dialog, 0.5, 0.5);
				break;
			/** 工作单位地址2 */
			case R.id.gzdwdz2:
				HzbjDialog gzdwdz2dialog = new HzbjDialog(context, "工作单位地址",
						gzdwdz2, map, "DWJDZ2");
				BussUtil.setDialogParams(context, gzdwdz2dialog, 0.5, 0.5);
				break;
			/** 向导3 */
			case R.id.xd3:
				HzbjDialog xd3dialog = new HzbjDialog(context, "向导", xd3, map,
						"XD3");
				BussUtil.setDialogParams(context, xd3dialog, 0.5, 0.5);
				break;
			/** 工作单位地址3 */
			case R.id.gzdwdz3:
				HzbjDialog gzdwdz3dialog = new HzbjDialog(context, "工作单位地址",
						gzdwdz3, map, "DWJDZ3");
				BussUtil.setDialogParams(context, gzdwdz3dialog, 0.5, 0.5);
				break;
			/** 检查员1 */
			case R.id.jiancy1:
				HzbjDialog jiancy1dialog = new HzbjDialog(context, "检查员", jiancy1,
						map, "JCY1");
				BussUtil.setDialogParams(context, jiancy1dialog, 0.5, 0.5);
				break;
			/** 检查员工作单位1 */
			case R.id.jcygzdw1:
				HzbjDialog jcygzdw1dialog = new HzbjDialog(context, "检查员工作单位",
						jcygzdw1, map, "JCYGZDW1");
				BussUtil.setDialogParams(context, jcygzdw1dialog, 0.5, 0.5);
				break;
			/** 检查员2 */
			case R.id.jiancy2:
				HzbjDialog jiancy2dialog = new HzbjDialog(context, "检查员", jiancy2,
						map, "JCY2");
				BussUtil.setDialogParams(context, jiancy2dialog, 0.5, 0.5);
				break;
			/** 检查员工作单位2 */
			case R.id.jiancygzdw2:
				HzbjDialog jiancygzdw2dialog = new HzbjDialog(context, "检查员工作单位",
						jiancygzdw2, map, "JCYGZDZ2");
				BussUtil.setDialogParams(context, jiancygzdw2dialog, 0.5, 0.5);
				break;
			/** 检查员3 */
			case R.id.jiancy3:
				HzbjDialog jiancy3dialog = new HzbjDialog(context, "检查员", jiancy3,
						map, "JCY3");
				BussUtil.setDialogParams(context, jiancy3dialog, 0.5, 0.5);
				break;
			/** 检查员工作单位3 */
			case R.id.jiancygzdw3:
				HzbjDialog jiancygzdw3dialog = new HzbjDialog(context, "检查员工作单位",
						jiancygzdw3, map, "JCYGZDZ3");
				BussUtil.setDialogParams(context, jiancygzdw3dialog, 0.5, 0.5);
				break;
			/** 调查日期 */
			case R.id.diaocrq:
				HzbjDialog diaocrqdialog = new HzbjDialog(context, "调查日期", diaocrq,
						map, "DCRQ");
				BussUtil.setDialogParams(context, diaocrqdialog, 0.5, 0.5);
				break;
			/** 检查日期 */
			case R.id.jcrq:
				HzbjDialog jcrqdialog = new HzbjDialog(context, "检查日期", jcrq, map,
						"JCRQ");
				BussUtil.setDialogParams(context, jcrqdialog, 0.5, 0.5);
				break;
			/** 保存 */
			case R.id.save:
				DataBaseHelper.deleteLxqcYddcbData(context, ydhselect);
				if (list.size() > 0) {
					DataBaseHelper.addLxqcYddcbData(context, zd, list);
				}
				dealdata();
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.bcsuccess));
				break;
			/** 取消 */
			case R.id.cancle:
				dismiss();
				break;

		}
	}

	/** 处理位置数据 */
	private void dealdata() {
		String result = "";
		String di = "";
		String xian = "";
		String a = dishizhou.getText().toString();
		if (a != null && a != "") {
			String[] arr1 = a.split("-");
			if (arr1.length >= 2) {
				di = arr1[1];
			}
		}
		String b = xiansq.getText().toString();
		if (b != null && b != "") {
			String[] arr2 = b.split("-");
			if (arr2.length >= 2) {
				xian = arr2[1];
			}
		}
		String xiang = xiangz.getText().toString();
		String cunname = cun.getText().toString();
		if (di != "") {
			result = result + di + ",";
		}
		if (xian != "") {
			result = result + xian + ",";
		}
		if (xiang != null && xiang != "") {
			result = result + xiang + ",";
		}
		if (cunname != null && cunname != "") {
			result = result + cunname;
		}
		if (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}

		location.setText(result);

	}
}
