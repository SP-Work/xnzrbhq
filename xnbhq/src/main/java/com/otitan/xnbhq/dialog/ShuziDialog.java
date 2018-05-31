package com.otitan.xnbhq.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Graphic;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ErDiaoActivity;
import com.otitan.xnbhq.adapter.EdYmdcadapter;
import com.otitan.xnbhq.adapter.JgdcLineAdapter;
import com.otitan.xnbhq.adapter.LxqcYdyzAdapter;
import com.otitan.xnbhq.dao.Jgdc;
import com.otitan.xnbhq.dao.Ym;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.listviewinedittxt.EdLineAdapter;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ToastUtil;

/**
 * Created by li on 2017/6/5.
 * 数字输入dialog
 */

public class ShuziDialog extends Dialog {
	Context context;
	String name = "";
	private TextView tv;
	HashMap<String, String> map;
	String zd = "";
	private String chushizhi = "";
	private String shuzhi = "";
	/** inputtype为"0"时输入为小数 ，为"1"时为整数 */
	private String inputtype = "";
	List<HashMap<String, String>> list;
	private TextView zongshu;
	/** xiaoshu为小数精切到小数点后第几位 “1”为1位 "2"为2位 */
	private String xiaoshu = "";
	/** kjltype为“1”是跨角林面积比例输入应[0,1.00]且必须精确到0.05 */
	private String kjltype = "";
	/** 输入内容框 */
	EditText content;
	/** 从EdLineAdapter获取的数据 */
	private int position;
	private List<Line> lines;
	private Line line;
	List<Ym> ymlist;
	private Ym ym;
	private EdLineAdapter edlineadapter = null;
	private EdYmdcadapter ymdcadapter = null;
	private JgdcLineAdapter jgdcatAdapter = null;
	private Jgdc jgdc;
	/** 每木检尺中样木过滤 为1为样木过滤进入 */
	String type = "";
	/** 计算周界绝对闭合差用到"1"为其进入 */
	private String zjtype = "";
	/** 赋值周界相对闭合差TextView */
	private TextView xdbhctv;
	/** 判断每木检尺中样木过滤 输入是否为空 */
	private boolean flag = false;
	/** 连续清查样地因子调查 */
	private LxqcYdyzAdapter ydyzAdapter;

	/** 当前选择图层 */
	private  FeatureLayer curfeaturelayer;
	/** 当前选择feature */
	private  GeodatabaseFeature curfeture;
	public Map<String, Object> attribute;

	/** 森林资源连续清查 跨角林 */
	public ShuziDialog(Context context, String name, TextView tv,
					   HashMap<String, String> map, String zd,
					   List<HashMap<String, String>> list, TextView zongshu,
					   String inputtype, String xiaoshu, String kjltype) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.map = map;
		this.zd = zd;
		this.inputtype = inputtype;
		this.list = list;
		this.zongshu = zongshu;
		this.xiaoshu = xiaoshu;
		this.kjltype = kjltype;

	}

	public ShuziDialog(Context context, String name, TextView tv,
					   String inputtype, String xiaoshu, String type) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.inputtype = inputtype;
		this.type = type;
	}

	public ShuziDialog(Context context, String name, TextView tv) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;

	}

	/** 周界绝对闭合差计算用到xdbhctv赋值周界相对闭合差tv */
	public ShuziDialog(Context context, String name, TextView tv,
					   TextView xdbhctv, String inputtype, String xiaoshu, String zjtype) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.zjtype = zjtype;
		this.xdbhctv = xdbhctv;
		this.inputtype = inputtype;
		this.xiaoshu = xiaoshu;
	}

	public ShuziDialog(Context context, String name, TextView tv, int position,
					   List<Line> lines, EdLineAdapter edLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = name;
		this.tv = tv;
		this.position = position;
		this.lines = lines;
		this.edlineadapter = edLineAdapter;

	}

	/** 连续清查样地因子 */
	public ShuziDialog(Context context, TextView tv, Line line,
					   String inputtype, String xiaoshu, LxqcYdyzAdapter ydyzAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = line.getTview();
		this.tv = tv;
		this.inputtype = inputtype;
		this.line = line;
		this.xiaoshu = xiaoshu;
		this.ydyzAdapter = ydyzAdapter;

	}

	/** 二调小班区划和样地调查 */
	public ShuziDialog(Context context, TextView tv, Line line,
					   EdLineAdapter edLineAdapter, GeodatabaseFeature cf, FeatureLayer gf) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = line.getTview();
		this.tv = tv;
		this.line = line;
		this.edlineadapter = edLineAdapter;
		this.curfeture = cf;
		this.curfeaturelayer = gf;
		this.attribute = cf.getAttributes();

	}

	/** 样木调查 */
	public ShuziDialog(Context context, String title, TextView tv, Ym ym,
					   EdYmdcadapter edYmdcadapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.name = title;
		this.tv = tv;
		this.ym = ym;
		this.xiaoshu = "2";// 精确到小数点两位
		this.ymdcadapter = edYmdcadapter;

	}

	/** 角规调查 */
	public ShuziDialog(Context mContext, TextView tv, Line line, Jgdc jgdc,
					   JgdcLineAdapter jgdcLineAdapter) {
		super(mContext, R.style.Dialog);
		this.context = mContext;
		this.name = line.getTview();
		this.tv = tv;
		this.line = line;
		this.jgdc = jgdc;
		this.jgdcatAdapter = jgdcLineAdapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_shuzi);
		setCanceledOnTouchOutside(false);
		if (tv != null) {
			chushizhi = tv.getText().toString().trim();
		}
		TextView headtext = (TextView) findViewById(R.id.headtext);
		Button back = (Button) findViewById(R.id.back);
		Button wancheng = (Button) findViewById(R.id.wancheng);
		content = (EditText) findViewById(R.id.content);
		final TextView sz1 = (TextView) findViewById(R.id.sz1);
		final TextView sz2 = (TextView) findViewById(R.id.sz2);
		final TextView sz3 = (TextView) findViewById(R.id.sz3);
		final TextView sz4 = (TextView) findViewById(R.id.sz4);
		final TextView sz5 = (TextView) findViewById(R.id.sz5);
		final TextView sz6 = (TextView) findViewById(R.id.sz6);
		final TextView sz7 = (TextView) findViewById(R.id.sz7);
		final TextView sz8 = (TextView) findViewById(R.id.sz8);
		final TextView sz9 = (TextView) findViewById(R.id.sz9);
		final TextView sz0 = (TextView) findViewById(R.id.sz0);

		final TextView szdian = (TextView) findViewById(R.id.szdian);
		final TextView szjia = (TextView) findViewById(R.id.szjia);
		final TextView szx = (TextView) findViewById(R.id.szx);
		final TextView szqk = (TextView) findViewById(R.id.szqk);
		headtext.setText(name);
		content.setText(chushizhi);
		shuzhi = "";
		CursorUtil.setEditTextLocation(content);

		sz1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "1";
				} else {
					shuzhi = shuzhi + "1";
				}
				content.setText(shuzhi);
				// content.setSelection(shuzhi.length());
				CursorUtil.setEditTextLocation(content);
			}
		});
		sz2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "2";
				} else {
					shuzhi = shuzhi + "2";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "3";
				} else {
					shuzhi = shuzhi + "3";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "4";
				} else {
					shuzhi = shuzhi + "4";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "5";
				} else {
					shuzhi = shuzhi + "5";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "6";
				} else {
					shuzhi = shuzhi + "6";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "7";
				} else {
					shuzhi = shuzhi + "7";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "8";
				} else {
					shuzhi = shuzhi + "8";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					shuzhi = "9";
				} else {
					shuzhi = shuzhi + "9";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		sz0.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!shuzhi.equals("") && shuzhi.contains(".")) {
					String[] split = shuzhi.split("\\.", -1);
					if (split.length == 2) {
						if ((xiaoshu.equals("1") && split[1].length() >= 1)
								|| (xiaoshu.equals("2") && split[1].length() >= 2)) {
							return;
						}
					}
				}
				if (shuzhi.equals("0")) {
					return;
				} else if (shuzhi.equals("")) {
					shuzhi = shuzhi + "0";
				} else {
					if (shuzhi.startsWith("0") && !shuzhi.contains(".")) {

					} else {
						shuzhi = shuzhi + "0";

					}
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		szdian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (shuzhi.equals("")) {
					return;
				}
				if (!shuzhi.contains(".")) {
					shuzhi = shuzhi + ".";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		szjia.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (shuzhi.contains("-")) {
					shuzhi = shuzhi.replaceAll("-", "");
				} else {
					shuzhi = "-" + shuzhi;
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		szx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (shuzhi.length() > 1) {
					shuzhi = shuzhi.substring(0, shuzhi.length() - 1);
				} else {
					shuzhi = "";
				}
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});
		szqk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shuzhi = "";
				content.setText(shuzhi);
				content.setSelection(shuzhi.length());
			}
		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		wancheng.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String value = "";
				if (TextUtils.isEmpty(content.getText())) {
					value = "0";
					flag = true;
				} else {
					value = content.getText().toString().trim();
				}

				if ("1".equals(kjltype)) {
					double a = Double.parseDouble(value);
					int b = (int) (a * 100);
					if (a < 0 || a > 1 || (b % 5) != 0) {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.mjblsrfw));
						return;
					}
				}
				if (value.length() > 1 && value.endsWith(".")) {
					value = value.substring(0, shuzhi.length() - 1);
				}
				if (value.contains(".")) {
					float result = Float.parseFloat(value);
					if (tv != null) {
						tv.setText(result + "");
					}
					if (map != null) {
						map.put(zd, result + "");
					}
				} else {
					if (tv != null) {
						tv.setText(value);
					}
					if (map != null) {
						map.put(zd, value);
					}
				}
				int a = 0;
				float b = 0;
				if (list != null && zongshu != null) {
					for (int i = 0; i < list.size(); i++) {
						if (!"".equals(list.get(i).get(zd))) {
							if ("1".equals(inputtype)) {
								a = a + Integer.parseInt(list.get(i).get(zd));
							} else {
								b = b + Float.parseFloat(list.get(i).get(zd));
							}
						}
					}
					if ("1".equals(inputtype)) {
						zongshu.setText(a + "");
					} else {
						float c = b / (list.size());
						if ((c + "").contains(".")) {
							String[] split = (c + "").split("\\.", -1);
							if (split.length == 2) {
								if (xiaoshu.equals("2")
										&& split[1].length() > 2) {
									c = Float.parseFloat(split[0] + "."
											+ split[1].substring(0, 2));
								} else if (xiaoshu.equals("1")
										&& split[1].length() > 1) {
									c = Float.parseFloat(split[0] + "."
											+ split[1].substring(0, 1));
								}

							}
						}
						zongshu.setText(c + "");
					}
				}

				if (edlineadapter != null) {
					btn_complete(context, line);
				} else if (jgdcatAdapter != null) {
					// 样木调查
					ed_jgdc_complete(context, jgdcatAdapter);
				} else if (ymdcadapter != null) {
					// 样木调查
					ed_ymdc_complete(context, ymdcadapter);
				}
				if ("1".equals(type)) {
					if (flag) {
						tv.setText("");
					}
					SlzylxqcMmjcDialog.ymgl();
				}
				// 周界相对闭合差计算
				if ("1".equals(zjtype)) {
					if (!"".equals(value)) {
						double jdbhc = Double.parseDouble(value);
						double temp = (jdbhc / (25.82 * 4)) * 100;
						if ((temp + "").contains(".")) {
							String[] split = (temp + "").split("\\.", -1);
							if (split.length == 2) {
								if (split[1].length() > 2) {
									temp = Double.parseDouble(split[0] + "."
											+ split[1].substring(0, 2));
								}
							}
						}
						xdbhctv.setText(temp + "");
					}
				}
				dismiss();
			}
		});
		if ("1".equals(inputtype)) {
			szdian.setClickable(false);
			szdian.setBackgroundColor(Color.parseColor("#00688B"));
		}
	}

	/** 二调角规调查数据保存 */
	public void ed_jgdc_complete(Context context, JgdcLineAdapter jgdcatAdapter) {
		btn_complete1(context, line);
		jgdcatAdapter.notifyDataSetChanged();

	}

	/** 二调角规调查数据保存 */
	private void btn_complete1(Context context, Line curline) {

		String bfvalue = String.valueOf(curline.getValue());
		// 要保存的数据
		String name = curline.getKey();
		String curvalue = content.getText().toString();

		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} else if (name.equals("XIONGJING")) {
			jgdc.setXIONGJING(Double.parseDouble(curvalue));
		} else if (name.equals("PJMSG")) {
			jgdc.setPJMSG(Double.parseDouble(curvalue));
		} else if (name.equals("JGDM")) {
			jgdc.setJGDM(Double.parseDouble(curvalue));
		} else if (name.equals("GQZS")) {
			jgdc.setGQZS(Double.parseDouble(curvalue));
		} else if (name.equals("GQXJ")) {
			jgdc.setGQXJ(Double.parseDouble(curvalue));
		}
		/*
		 * else if (curvalue.length() > bfvalue.length()) {
		 * ToastUtil.setToast((Activity) context, "所填字段长度超出数据库约束长度"); return; }
		 */

		boolean isupdate = DataBaseHelper.updateJgdc(context, jgdc,
				ErDiaoActivity.datapath);
		if (isupdate) {
			line.setText(curvalue);
			line.setValue(curvalue);
			ToastUtil.setToast((Activity) context, "更新成功");

		} else {
			ToastUtil.setToast((Activity) context, "更新失败");
			return;
		}

	}

	/** 二调样木调查数据保存 */
	public void ed_ymdc_complete(Context context, EdYmdcadapter ymdcadapter) {
		String curvalue = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (chushizhi.equals(curvalue)) {
			return;

		}
		switch (tv.getId()) {
			// 胸径
			case R.id.xj:
				ym.setXIONGJING(Double.valueOf(curvalue));
				break;

			// 备注
			case R.id.bz:
				ym.setBEIZHU(curvalue);
				break;

		}
		boolean idupdate = DataBaseHelper.updateYm(context, ym,
				ErDiaoActivity.datapath);
		if (idupdate) {
			ToastUtil.setToast(context, "样木更新成功");
			ymdcadapter.notifyDataSetChanged();
		} else {
			ToastUtil.setToast(context, "样木更新失败");
		}
	}

	/** 保存数据 */
	public void btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String curvalue = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} else {
			attribute.put(name, curvalue);
			long featureid = curfeture.getId();
			Graphic updateGraphic = new Graphic(curfeture.getGeometry(),
					curfeture.getSymbol(), attribute);
			try {
				curfeaturelayer.getFeatureTable().updateFeature(featureid,
						updateGraphic);

				curline.setText(curvalue);
			} catch (Exception e) {
				bfvalue = bfvalue == "null" ? "" : bfvalue;
				curline.setText(bfvalue);
				attribute.put(name, bfvalue);
				ToastUtil.setToast((Activity) mContext, "更新失败");
				return;

			}
			ToastUtil.setToast((Activity) mContext, "更新成功");
		}
		edlineadapter.notifyDataSetChanged();

	}

	/** 连续清查保存数据 */
	public void lxqc_btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String curvalue = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} else {
			attribute.put(name, curvalue);
			long featureid = curfeture.getId();
			Graphic updateGraphic = new Graphic(curfeture.getGeometry(),
					curfeture.getSymbol(), attribute);
			try {
				curfeaturelayer.getFeatureTable().updateFeature(featureid,updateGraphic);

				curline.setText(curvalue);
			} catch (Exception e) {
				bfvalue = bfvalue == "null" ? "" : bfvalue;
				curline.setText(bfvalue);
				attribute.put(name, bfvalue);
				ToastUtil.setToast((Activity) mContext, "更新失败");
				return;

			}
			ToastUtil.setToast((Activity) mContext, "更新成功");
		}

	}
}
