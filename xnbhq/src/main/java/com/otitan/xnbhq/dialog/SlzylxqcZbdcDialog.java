package com.otitan.xnbhq.dialog;

import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcZbdcCbAdapter;
import com.otitan.xnbhq.adapter.LxqcZbdcGmAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 植被调查 dialog
 */

public class SlzylxqcZbdcDialog extends Dialog {
	Context context;
	String ydhselect;

	public SlzylxqcZbdcDialog(Context context,String ydhselect) {
		super(context, R.style.Dialog);
		this.context=context;
		this.ydhselect=ydhselect;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_zbdcjl_view);
		setCanceledOnTouchOutside(false);
		// 先显示灌木布局
		final View lineargm = findViewById(R.id.lineargm);
		final View linearcb = findViewById(R.id.linearcb);
		final View lineardbw = findViewById(R.id.lineardbw);
		lineargm.setVisibility(View.VISIBLE);
		linearcb.setVisibility(View.GONE);
		lineardbw.setVisibility(View.GONE);

		TextView ydh = (TextView) findViewById(R.id.ydh);
		final TextView yfqksm = (TextView) findViewById(R.id.yfqksm);
		final TextView yfszwz = (TextView) findViewById(R.id.yfszwz);
		final TextView yfzgd = (TextView) findViewById(R.id.yfzgd);
		final TextView gaidu = (TextView) findViewById(R.id.gaidu);
		final TextView gmzongzs = (TextView) findViewById(R.id.gmzongzs);
		final TextView gmpjg = (TextView)findViewById(R.id.gmpjg);
		final TextView gmpjdj = (TextView)findViewById(R.id.gmpjdj);
		ydh.setText(ydhselect);
		String ydhtx = ydh.getText().toString();
		/**共有的信息*/
		final List<HashMap<String, String>> zbztlist = DataBaseHelper
				.searchLxqcZbdcData(context, ydhtx, "0");
		/**灌木信息*/
		final List<HashMap<String, String>> zbgmlist = DataBaseHelper
				.searchLxqcZbdcData(context, ydhtx, "1");
		/**草本信息*/
		final List<HashMap<String, String>> zbcblist = DataBaseHelper
				.searchLxqcZbdcData(context, ydhtx, "2");
		/**地被物信息*/
		final List<HashMap<String, String>> dbwlist = DataBaseHelper
				.searchLxqcZbdcData(context, ydhtx, "3");
		if (zbztlist.size() != 0) {
			yfqksm.setText(zbztlist.get(0).get("YFQKSM"));
			yfszwz.setText(zbztlist.get(0).get("YFSZWZ"));
			yfzgd.setText(zbztlist.get(0).get("YFZGD"));
		}else{
			HashMap<String, String>map=new HashMap<String, String>();
			map.put("YDH", ydhselect);
			map.put("YFQKSM", "");
			map.put("YFSZWZ", "");
			map.put("YFZGD", "");
			zbztlist.add(map);
		}
		ListView listview = (ListView)findViewById(R.id.listview);
		final LxqcZbdcGmAdapter adaptergm = new LxqcZbdcGmAdapter(context,
				zbgmlist, gmzongzs, gmpjg, gmpjdj);
		listview.setAdapter(adaptergm);

		// 计算灌木总株数
		int gmzs = 0;
		for (int i = 0; i < zbgmlist.size(); i++) {
			if (!"".equals(zbgmlist.get(i).get("GMZS"))) {
				gmzs = gmzs + Integer.parseInt(zbgmlist.get(i).get("GMZS"));
			}
		}
		gmzongzs.setText(gmzs + "");

		// 计算灌木平均高
		float zonggmpjg = 0;
		for (int i = 0; i < zbgmlist.size(); i++) {
			if (!"".equals(zbgmlist.get(i).get("GMPJG"))) {
				zonggmpjg = zonggmpjg
						+ Float.parseFloat(zbgmlist.get(i).get("GMPJG"));
			}
		}
		String[] split = ((zonggmpjg / (zbgmlist.size())) + "")
				.split("\\.", -1);
		if (split.length == 2) {
			if (split[1].length() > 2) {
				zonggmpjg = Float.parseFloat(split[0] + "."
						+ split[1].substring(0, 2));
			} else {
				zonggmpjg = Float.parseFloat(split[0] + "." + split[1]);
			}
		}
		gmpjg.setText(zonggmpjg + "");

		// 计算灌木平均地径
		float zonggmpjdj = 0;
		for (int i = 0; i < zbgmlist.size(); i++) {
			if (!"".equals(zbgmlist.get(i).get("GMPJDJ"))) {
				zonggmpjdj = zonggmpjdj
						+ Float.parseFloat(zbgmlist.get(i).get("GMPJDJ"));
			}
		}
		String[] split1 = ((zonggmpjdj / (zbgmlist.size())) + "").split("\\.",
				-1);
		if (split1.length == 2) {
			if (split1[1].length() > 1) {
				zonggmpjdj = Float.parseFloat(split1[0] + "."
						+ split1[1].substring(0, 1));
			} else {
				zonggmpjdj = Float.parseFloat(split1[0] + "." + split1[1]);
			}
		}
		gmpjdj.setText(zonggmpjdj + "");

		TextView addmore = (TextView)findViewById(R.id.addmore);
		// 添加更多
		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < zbgmlist.size(); i++) {
					HashMap<String, String> lsmap = zbgmlist.get(i);
					if ("".equals(lsmap.get("GMMC"))
							|| "".equals(lsmap.get("GMZS"))
							|| "".equals(lsmap.get("GMPJG"))
							|| "".equals(lsmap.get("GMPJDJ"))
							|| "".equals(lsmap.get("GMGD"))) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.ywtxwzjl));
						return;
					}
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("GMMC", "");
				map.put("GMZS", "");
				map.put("GMPJG", "");
				map.put("GMPJDJ", "");
				map.put("GMGD", "");
				zbgmlist.add(map);

				// 计算灌木平均高
				float zonggmpjg = 0;
				for (int i = 0; i < zbgmlist.size(); i++) {
					if (!"".equals(zbgmlist.get(i).get("GMPJG"))) {
						zonggmpjg = zonggmpjg
								+ Float.parseFloat(zbgmlist.get(i).get("GMPJG"));
					}
				}
				String[] split = ((zonggmpjg / (zbgmlist.size())) + "").split(
						"\\.", -1);
				if (split.length == 2) {
					if (split[1].length() > 2) {
						zonggmpjg = Float.parseFloat(split[0] + "."
								+ split[1].substring(0, 2));
					} else {
						zonggmpjg = Float.parseFloat(split[0] + "." + split[1]);
					}
				}
				gmpjg.setText(zonggmpjg + "");

				// 计算灌木平均地径
				float zonggmpjdj = 0;
				for (int i = 0; i < zbgmlist.size(); i++) {
					if (!"".equals(zbgmlist.get(i).get("GMPJDJ"))) {
						zonggmpjdj = zonggmpjdj
								+ Float.parseFloat(zbgmlist.get(i)
								.get("GMPJDJ"));
					}
				}
				String[] split1 = ((zonggmpjdj / (zbgmlist.size())) + "")
						.split("\\.", -1);
				if (split1.length == 2) {
					if (split1[1].length() > 1) {
						zonggmpjdj = Float.parseFloat(split1[0] + "."
								+ split1[1].substring(0, 1));
					} else {
						zonggmpjdj = Float.parseFloat(split1[0] + "."
								+ split1[1]);
					}
				}
				gmpjdj.setText(zonggmpjdj + "");
				adaptergm.notifyDataSetChanged();
			}
		});
		// 取消
		Button cancle = (Button)findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		// 保存
		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 总体信息删除
				DataBaseHelper.deleteLxqcZbdcData(context, ydhselect, "0");
				// 灌木信息删除
				DataBaseHelper.deleteLxqcZbdcData(context, ydhselect, "1");
				// 草本信息删除
				DataBaseHelper.deleteLxqcZbdcData(context, ydhselect, "2");
				// 地被物信息删除
				DataBaseHelper.deleteLxqcZbdcData(context, ydhselect, "3");
				// 总体信息添加
				for (int i = 0; i < zbztlist.size(); i++) {
					DataBaseHelper.addLxqcZbdcData(context,ydhselect, zbztlist
									.get(i).get("YFQKSM"), zbztlist.get(i)
									.get("YFSZWZ"), zbztlist.get(i).get("YFZGD"), "0",
							"", "", "", "", "", "", "", "", "", "", "");
				}
				// 灌木信息添加
				for (int i = 0; i < zbgmlist.size(); i++) {
					DataBaseHelper
							.addLxqcZbdcData(context, ydhselect, "", "", "", "1",
									zbgmlist.get(i).get("GMMC"), zbgmlist
											.get(i).get("GMZS"), zbgmlist
											.get(i).get("GMPJG"),
									zbgmlist.get(i).get("GMPJDJ"), zbgmlist
											.get(i).get("GMGD"), "", "", "",
									"", "", "");
				}
				// 草本信息添加
				for (int i = 0; i < zbcblist.size(); i++) {
					DataBaseHelper.addLxqcZbdcData(context, ydhselect, "", "", "",
							"2", "", "", "", "", "", zbcblist.get(i)
									.get("CBMC"), zbcblist.get(i).get("CBPJG"),
							zbcblist.get(i).get("CBGD"), "", "", "");
				}
				// 地被物信息添加
				for (int i = 0; i < dbwlist.size(); i++) {
					DataBaseHelper.addLxqcZbdcData(context, ydhselect, "", "", "",
							"3", "", "", "", "", "", "", "", "", dbwlist.get(i)
									.get("DBWMC"),
							dbwlist.get(i).get("DBWPJG"),
							dbwlist.get(i).get("DBWGD"));
				}
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.bcsuccess));
			}
		});
		// 样方情况说明
		yfqksm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = context.getResources().getString(R.string.yfqksm);
				HashMap<String, String> map = null;
				String zd = "";
				if (zbztlist.size() != 0) {
					map = zbztlist.get(0);
					zd = "YFQKSM";
				}
				HzbjDialog hzbjdialog = new HzbjDialog(context,
						name, yfqksm, map, zd);
				BussUtil.setDialogParams(context, hzbjdialog, 0.5, 0.5);
			}
		});
		// 样方设置位置
		yfszwz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String> map = null;
				String zd = "";
				if (zbztlist.size() != 0) {
					map = zbztlist.get(0);
					zd = "YFSZWZ";
				}
				HzbjDialog hzbjdialog = new HzbjDialog(context,
						"样方设置位置", yfszwz,map, zd);
				BussUtil.setDialogParams(context, hzbjdialog, 0.5, 0.5);
			}
		});
		// 样方总盖度
		yfzgd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String> map = null;
				String zd = "";
				if (zbztlist.size() != 0) {
					map = zbztlist.get(0);
					zd = "YFZGD";
				}
				ShuziDialog shuzidialog=new ShuziDialog(context,"样方总盖度(%)", yfzgd, map, "YFZGD", null,null,"1","","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		// 盖度
		gaidu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context, "盖度(%)", gaidu, null, "", null, null, "1", "", "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});

		final TextView gm = (TextView) findViewById(R.id.gm);
		final TextView cb = (TextView) findViewById(R.id.cb);
		final TextView dbw = (TextView) findViewById(R.id.dbw);
		// 灌木点击事件
		gm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gm.setBackgroundColor(Color.parseColor("#368bc1"));
				cb.setBackgroundColor(Color.parseColor("#90EE90"));
				dbw.setBackgroundColor(Color.parseColor("#90EE90"));

				lineargm.setVisibility(View.VISIBLE);
				linearcb.setVisibility(View.GONE);
				lineardbw.setVisibility(View.GONE);

			}
		});
		// 草本点击事件
		cb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cb.setBackgroundColor(Color.parseColor("#368bc1"));
				gm.setBackgroundColor(Color.parseColor("#90EE90"));
				dbw.setBackgroundColor(Color.parseColor("#90EE90"));

				lineargm.setVisibility(View.GONE);
				linearcb.setVisibility(View.VISIBLE);
				lineardbw.setVisibility(View.GONE);
				ListView listviewcb = (ListView) findViewById(R.id.cblistview);
				final TextView cbpjg = (TextView)findViewById(R.id.cbpjg);
				TextView cbaddmore = (TextView)findViewById(R.id.cbaddmore);
				final LxqcZbdcCbAdapter adaptercb = new LxqcZbdcCbAdapter(
						context, zbcblist, cbpjg, "1");

				// 计算草本平均高
				float zongcbpjg = 0;
				for (int i = 0; i < zbcblist.size(); i++) {
					if (!"".equals(zbcblist.get(i).get("CBPJG"))) {
						zongcbpjg = zongcbpjg
								+ Float.parseFloat(zbcblist.get(i).get("CBPJG"));
					}
				}
				String[] split = ((zongcbpjg / (zbcblist.size())) + "").split(
						"\\.", -1);
				if (split.length == 2) {
					if (split[1].length() > 2) {
						zongcbpjg = Float.parseFloat(split[0] + "."
								+ split[1].substring(0, 2));
					} else {
						zongcbpjg = Float.parseFloat(split[0] + "." + split[1]);
					}
				}
				cbpjg.setText(zongcbpjg + "");

				listviewcb.setAdapter(adaptercb);
				cbaddmore.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						for (int i = 0; i < zbcblist.size(); i++) {
							HashMap<String, String> amap = zbcblist.get(i);
							if ("".equals(amap.get("CBMC"))
									|| "".equals(amap.get("CBPJG"))
									|| "".equals(amap.get("CBGD"))) {
								ToastUtil.setToast(
										context,
										context.getResources().getString(
												R.string.ywtxwzjl));
								return;
							}
						}
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("CBMC", "");
						map.put("CBPJG", "");
						map.put("CBGD", "");
						zbcblist.add(map);
						// 计算草本平均高
						float zongcbpjg = 0;
						for (int i = 0; i < zbcblist.size(); i++) {
							if (!"".equals(zbcblist.get(i).get("CBPJG"))) {
								zongcbpjg = zongcbpjg
										+ Float.parseFloat(zbcblist.get(i).get(
										"CBPJG"));
							}
						}
						String[] split = ((zongcbpjg / (zbcblist.size())) + "")
								.split("\\.", -1);
						if (split.length == 2) {
							if (split[1].length() > 2) {
								zongcbpjg = Float.parseFloat(split[0] + "."
										+ split[1].substring(0, 2));
							} else {
								zongcbpjg = Float.parseFloat(split[0] + "."
										+ split[1]);
							}
						}
						cbpjg.setText(zongcbpjg + "");
						adaptercb.notifyDataSetChanged();
					}
				});

			}
		});
		// 地被物点击事件
		dbw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dbw.setBackgroundColor(Color.parseColor("#368bc1"));
				gm.setBackgroundColor(Color.parseColor("#90EE90"));
				cb.setBackgroundColor(Color.parseColor("#90EE90"));

				lineargm.setVisibility(View.GONE);
				linearcb.setVisibility(View.GONE);
				lineardbw.setVisibility(View.VISIBLE);

				ListView dbwlistview = (ListView)findViewById(R.id.dbwlistview);
				final TextView dbwpjg = (TextView)findViewById(R.id.dbwpjg);
				TextView dbwaddmore = (TextView)findViewById(R.id.dbwaddmore);
				final LxqcZbdcCbAdapter adapterdbw = new LxqcZbdcCbAdapter(
						context, dbwlist, dbwpjg, "2");

				// 计算地被物平均高
				float zongdbwpjg = 0;
				for (int i = 0; i < dbwlist.size(); i++) {
					if (!"".equals(dbwlist.get(i).get("DBWPJG"))) {
						zongdbwpjg = zongdbwpjg
								+ Float.parseFloat(dbwlist.get(i).get("DBWPJG"));
					}
				}
				String[] split = ((zongdbwpjg / (dbwlist.size())) + "").split(
						"\\.", -1);
				if (split.length == 2) {
					if (split[1].length() > 2) {
						zongdbwpjg = Float.parseFloat(split[0] + "."
								+ split[1].substring(0, 2));
					} else {
						zongdbwpjg = Float
								.parseFloat(split[0] + "." + split[1]);
					}
				}
				dbwpjg.setText(zongdbwpjg + "");

				dbwlistview.setAdapter(adapterdbw);
				dbwaddmore.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						for (int i = 0; i < dbwlist.size(); i++) {
							HashMap<String, String> amap = dbwlist.get(i);
							if ("".equals(amap.get("DBWMC"))
									|| "".equals(amap.get("DBWPJG"))
									|| "".equals(amap.get("DBWGD"))) {
								ToastUtil.setToast(
										context,
										context.getResources().getString(
												R.string.ywtxwzjl));
								return;
							}
						}
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("DBWMC", "");
						map.put("DBWPJG", "");
						map.put("DBWGD", "");
						dbwlist.add(map);

						// 计算地被物平均高
						float zongdbwpjg = 0;
						for (int i = 0; i < dbwlist.size(); i++) {
							if (!"".equals(dbwlist.get(i).get("DBWPJG"))) {
								zongdbwpjg = zongdbwpjg
										+ Float.parseFloat(dbwlist.get(i).get(
										"DBWPJG"));
							}
						}
						String[] split = ((zongdbwpjg / (dbwlist.size())) + "")
								.split("\\.", -1);
						if (split.length == 2) {
							if (split[1].length() > 2) {
								zongdbwpjg = Float.parseFloat(split[0] + "."
										+ split[1].substring(0, 2));
							} else {
								zongdbwpjg = Float.parseFloat(split[0] + "."
										+ split[1]);
							}
						}
						dbwpjg.setText(zongdbwpjg + "");

						adapterdbw.notifyDataSetChanged();
					}
				});
			}
		});

	}

}
