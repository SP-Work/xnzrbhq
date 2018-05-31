package com.otitan.xnbhq.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.Graphic;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ErDiaoActivity;
import com.otitan.xnbhq.adapter.EdYmdcadapter;
import com.otitan.xnbhq.adapter.JgdcLineAdapter;
import com.otitan.xnbhq.adapter.LxqcYdyzAdapter;
import com.otitan.xnbhq.adapter.XzzyAdapter;
import com.otitan.xnbhq.dao.Jgdc;
import com.otitan.xnbhq.dao.Ym;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.EdLineAdapter;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.ChineseToEnglish;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调查数据编辑保存
 */
public class XzzyDialog extends Dialog implements
		View.OnClickListener {
	Context context;
	private String headtv;
	private String resultstr = "";
	/** 内容 */
	TextView content;
	/** 输入的内容 */
	private TextView inputtxt;
	List<Row> list;
	/** 需要被赋值的textview */
	private TextView tv1;
	private List<Row> temlist = new ArrayList<>();
	List<Ym> ymlist;
	/** 样木 */
	private Ym ym;
	private View shuzilinear, ywlinear;
	XzzyAdapter adapter;
	/** 需要绑定的map */
	HashMap<String, String> map = null;
	/** 需要绑定的字段名 */
	String zd;
	int position;//
	List<Line> lines;
	private Line line;
	private EdLineAdapter edlineadapter;
	private LxqcYdyzAdapter lxqcLineAdapter;
	private EdYmdcadapter ymdcadaper;
	private JgdcLineAdapter jgdcadapter;
	private Jgdc jgdc;

	// 当前选择图层
	private  FeatureLayer curfeaturelayer;
	// 当前选择feature
	private  GeodatabaseFeature curfeture;
	public Map<String, Object> attribute;

	/** 森林资源连续清查 */
	public XzzyDialog(Context context, List<Row> list, TextView tv1, Line line,
					  LxqcYdyzAdapter lxqcLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = line.getTview();
		this.tv1 = tv1;// 绑定的控件
		this.temlist = list;
		this.line = line;
		this.lxqcLineAdapter = lxqcLineAdapter;
	}

	public XzzyDialog(Context context,String headtv,
					  List<Row> list, TextView tv1, Line line,
					  EdLineAdapter edLineAdapter) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = headtv;
		this.tv1 = tv1;// 绑定的控件
		this.temlist = list;
		this.line = line;
		this.edlineadapter = edLineAdapter;
	}

	/** 二调小班区划和样地调查 */
	public XzzyDialog(Context context, List<Row> list, TextView tv1, Line line,
					  EdLineAdapter edLineAdapter, GeodatabaseFeature gf, FeatureLayer fl) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = line.getTview();
		this.tv1 = tv1;// 绑定的控件
		this.temlist = list;
		this.line = line;
		this.edlineadapter = edLineAdapter;
		this.curfeture = gf;
		this.curfeaturelayer = fl;
		this.attribute = gf.getAttributes();
	}

	/** 样木调查 */
	public XzzyDialog(Context context, String title, List<Row> feildlist,
					  TextView tv, Ym ym, EdYmdcadapter ymdcadaper) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = title;
		this.temlist = feildlist;
		this.tv1 = tv;// 绑定的控件
		this.ym = ym;
		this.ymdcadaper = ymdcadaper;
	}

	/** 角规调查 */
	public XzzyDialog(Context context, List<Row> list, TextView tv1, Line line,
					  Jgdc jg, JgdcLineAdapter jgdcadaper) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = line.getTview();
		this.tv1 = tv1;// 绑定的控件
		this.temlist = list;
		this.line = line;
		this.jgdcadapter = jgdcadaper;
		this.jgdc = jg;
	}

	/** 连续清查 */
	public XzzyDialog(Context context, String headtv, List<Row> list,
					  TextView tv1) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = headtv;
		this.tv1 = tv1;// 绑定的控件
		this.temlist = list;
	}

	/** 连续清查 */
	public XzzyDialog(Context context, String headtv, List<Row> list,
					  TextView tv1, HashMap<String, String> map, String zd) {
		super(context, R.style.Dialog);
		this.context = context;
		this.headtv = headtv;
		this.tv1 = tv1;// 绑定的控件
		this.temlist = list;
		this.map = map;
		this.zd = zd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_xzzy);
		setCanceledOnTouchOutside(false);
		list = new ArrayList<Row>();
		for (int i = 0; i < temlist.size(); i++) {
			list.add(temlist.get(i));
		}
		// 标题名字
		TextView headtext = (TextView) findViewById(R.id.headtext);
		headtext.setText(headtv);

		content = (TextView) findViewById(R.id.content);
		content.setText(tv1.getText().toString());
		inputtxt = (TextView) findViewById(R.id.inputtxt);
		// 中英文键盘显示
		shuzilinear = findViewById(R.id.shuzilinear);
		ywlinear = findViewById(R.id.ywlinear);
		shuzilinear.setVisibility(View.VISIBLE);
		ywlinear.setVisibility(View.GONE);

		TextView tv0 = (TextView) findViewById(R.id.tv0);
		TextView tv1 = (TextView) findViewById(R.id.tv1);
		TextView tv2 = (TextView) findViewById(R.id.tv2);
		TextView tv3 = (TextView) findViewById(R.id.tv3);
		TextView tv4 = (TextView) findViewById(R.id.tv4);
		TextView tv5 = (TextView) findViewById(R.id.tv5);
		TextView tv6 = (TextView) findViewById(R.id.tv6);
		TextView tv7 = (TextView) findViewById(R.id.tv7);
		TextView tv8 = (TextView) findViewById(R.id.tv8);
		TextView tv9 = (TextView) findViewById(R.id.tv9);
		TextView tvszc = (TextView) findViewById(R.id.tvszc);
		TextView tvszx = (TextView) findViewById(R.id.tvszx);
		TextView tvabc = (TextView) findViewById(R.id.tvabc);
		tv0.setOnClickListener(this);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);
		tv6.setOnClickListener(this);
		tv7.setOnClickListener(this);
		tv8.setOnClickListener(this);
		tv9.setOnClickListener(this);
		tvszc.setOnClickListener(this);
		tvszx.setOnClickListener(this);
		tvabc.setOnClickListener(this);

		TextView tva = (TextView) findViewById(R.id.tva);
		tva.setOnClickListener(this);
		TextView tvb = (TextView) findViewById(R.id.tvb);
		tvb.setOnClickListener(this);
		TextView tvc = (TextView) findViewById(R.id.tvc);
		tvc.setOnClickListener(this);
		TextView tvd = (TextView) findViewById(R.id.tvd);
		tvd.setOnClickListener(this);
		TextView tve = (TextView) findViewById(R.id.tve);
		tve.setOnClickListener(this);
		TextView tvf = (TextView) findViewById(R.id.tvf);
		tvf.setOnClickListener(this);
		TextView tvg = (TextView) findViewById(R.id.tvg);
		tvg.setOnClickListener(this);
		TextView tvh = (TextView) findViewById(R.id.tvh);
		tvh.setOnClickListener(this);
		TextView tvi = (TextView) findViewById(R.id.tvi);
		tvi.setOnClickListener(this);
		TextView tvj = (TextView) findViewById(R.id.tvj);
		tvj.setOnClickListener(this);
		TextView tvk = (TextView) findViewById(R.id.tvk);
		tvk.setOnClickListener(this);
		TextView tvl = (TextView) findViewById(R.id.tvl);
		tvl.setOnClickListener(this);
		TextView tvm = (TextView) findViewById(R.id.tvm);
		tvm.setOnClickListener(this);
		TextView tvn = (TextView) findViewById(R.id.tvn);
		tvn.setOnClickListener(this);
		TextView tvo = (TextView) findViewById(R.id.tvo);
		tvo.setOnClickListener(this);
		TextView tvp = (TextView) findViewById(R.id.tvp);
		tvp.setOnClickListener(this);
		TextView tvq = (TextView) findViewById(R.id.tvq);
		tvq.setOnClickListener(this);
		TextView tvr = (TextView) findViewById(R.id.tvr);
		tvr.setOnClickListener(this);
		TextView tvs = (TextView) findViewById(R.id.tvs);
		tvs.setOnClickListener(this);
		TextView tvt = (TextView) findViewById(R.id.tvt);
		tvt.setOnClickListener(this);
		TextView tvu = (TextView) findViewById(R.id.tvu);
		tvu.setOnClickListener(this);
		TextView tvv = (TextView) findViewById(R.id.tvv);
		tvv.setOnClickListener(this);
		TextView tvw = (TextView) findViewById(R.id.tvw);
		tvw.setOnClickListener(this);
		TextView tvx = (TextView) findViewById(R.id.tvx);
		tvx.setOnClickListener(this);
		TextView tvy = (TextView) findViewById(R.id.tvy);
		tvy.setOnClickListener(this);
		TextView tvz = (TextView) findViewById(R.id.tvz);
		tvz.setOnClickListener(this);
		TextView tv123 = (TextView) findViewById(R.id.tv123);
		tv123.setOnClickListener(this);
		// 清空
		TextView tvqc = (TextView) findViewById(R.id.tvqc);
		tvqc.setOnClickListener(this);
		// 删除
		TextView tvsc = (TextView) findViewById(R.id.tvsc);
		tvsc.setOnClickListener(this);

		Button back = (Button) findViewById(R.id.back);
		Button wancheng = (Button) findViewById(R.id.wancheng);
		back.setOnClickListener(this);
		wancheng.setOnClickListener(this);

		ListView listView = (ListView) findViewById(R.id.listView);
		adapter = new XzzyAdapter(context, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				content.setText(list.get(arg2).getId() + "-"
						+ list.get(arg2).getName());
			}
		});
	}

	// 筛选与输入框中内容关联的项
	public void chooseItem(String str) {
		list.clear();
		ChineseToEnglish ce = new ChineseToEnglish();
		for (int i = 0; i < temlist.size(); i++) {
			Row row = temlist.get(i);
			if (shuzilinear.getVisibility() == View.VISIBLE
					&& row.getId().contains(str)) {
				list.add(row);
			} else if (ywlinear.getVisibility() == View.VISIBLE
					&& ce.getAllFirstLetter(row.getName()).contains(str)) {
				list.add(row);
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.tv0:
				resultstr = resultstr + 0;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv1:
				resultstr = resultstr + 1;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv2:
				resultstr = resultstr + 2;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv3:
				resultstr = resultstr + 3;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv4:
				resultstr = resultstr + 4;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv5:
				resultstr = resultstr + 5;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv6:
				resultstr = resultstr + 6;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv7:
				resultstr = resultstr + 7;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv8:
				resultstr = resultstr + 8;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tv9:
				resultstr = resultstr + 9;
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 清空
			case R.id.tvszc:
				resultstr = "";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 删除
			case R.id.tvszx:
				if (resultstr.length() > 1) {
					resultstr = resultstr.substring(0, resultstr.length() - 1);
				} else if (resultstr.length() == 1) {
					resultstr = "";
				}
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tva:
				resultstr = resultstr + "a";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvb:
				resultstr = resultstr + "b";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvc:
				resultstr = resultstr + "c";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvd:
				resultstr = resultstr + "d";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tve:
				resultstr = resultstr + "e";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvf:
				resultstr = resultstr + "f";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvg:
				resultstr = resultstr + "g";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvh:
				resultstr = resultstr + "h";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvi:
				resultstr = resultstr + "i";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvj:
				resultstr = resultstr + "j";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvk:
				resultstr = resultstr + "k";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvl:
				resultstr = resultstr + "l";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvm:
				resultstr = resultstr + "m";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvn:
				resultstr = resultstr + "n";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvo:
				resultstr = resultstr + "o";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvp:
				resultstr = resultstr + "p";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvq:
				resultstr = resultstr + "q";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvr:
				resultstr = resultstr + "r";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvs:
				resultstr = resultstr + "s";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvt:
				resultstr = resultstr + "t";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvu:
				resultstr = resultstr + "u";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvv:
				resultstr = resultstr + "v";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvw:
				resultstr = resultstr + "w";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvx:
				resultstr = resultstr + "x";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvy:
				resultstr = resultstr + "y";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			case R.id.tvz:
				resultstr = resultstr + "z";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 英文字母清空
			case R.id.tvqc:
				resultstr = "";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 英文字母删除
			case R.id.tvsc:
				if (resultstr.length() > 1) {
					resultstr = resultstr.substring(0, resultstr.length() - 1);
				} else if (resultstr.length() == 1) {
					resultstr = "";
				}
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 切换到英文字母键盘
			case R.id.tvabc:
				shuzilinear.setVisibility(View.GONE);
				ywlinear.setVisibility(View.VISIBLE);
				resultstr = "";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 切换到数字键盘
			case R.id.tv123:
				shuzilinear.setVisibility(View.VISIBLE);
				ywlinear.setVisibility(View.GONE);
				resultstr = "";
				inputtxt.setText(resultstr);
				chooseItem(resultstr);
				break;
			// 返回
			case R.id.back:
				dismiss();
				break;
			// 完成
			case R.id.wancheng:
				tv1.setText(content.getText().toString());
				if (map != null) {
					map.put(zd, content.getText().toString());
				}
				if (edlineadapter != null) {
					ed_btn_complete(context, edlineadapter);
				} else if (lxqcLineAdapter != null) {
					lxqc_btn_complete(context);
				} else if (ymdcadaper != null) {
					// 样木调查
					ed_ymdc_complete(context, ymdcadaper);
				} else if (jgdcadapter != null) {
					// 样木调查
					ed_jgdc_complete(context, jgdcadapter);
				}
				dismiss();
				break;
			default:
				break;
		}
	}

	/** 二调角规调查数据保存 */
	private void ed_jgdc_complete(Context mContext, JgdcLineAdapter jgdcadapter) {
		btn_complete1(mContext, line);
		jgdcadapter.notifyDataSetChanged();

	}

	/** 角规调查数据保存 */
	private void btn_complete1(Context mContext, Line curline) {
		// 原始数据
		String bfvalue = String.valueOf(curline.getValue());
		String[] values = content.getText().toString().split("-");
		// 要保存的数据
		String curvalue = values[0];
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} /*
		 * else if (curvalue.length() > bfvalue.length()) {
		 * ToastUtil.setToast((Activity) mContext, "所填字段长度超出数据库约束长度"); return; }
		 */

		jgdc.setSZDM(curvalue);
		boolean isupdate = DataBaseHelper.updateJgdc(mContext, jgdc,
				ErDiaoActivity.datapath);
		if (isupdate) {
			curline.setText(values[1]);
			curline.setValue(curvalue);
			ToastUtil.setToast((Activity) mContext, "更新成功");

		} else {
			ToastUtil.setToast((Activity) mContext, "更新失败");
			return;
		}

		// lines.set(line.getNum(), line);
	}

	/** 二调样木调查数据保存 */
	private void ed_ymdc_complete(Context mContext, EdYmdcadapter ymdcadapter) {

		String bfvalue = String.valueOf(tv1.getText());
		String txt = content.getText() == null ? "" : content.getText().toString();
		if(txt.equals("")){
			return;
		}
		String[] values = txt.split("-");
		String curvalue = values[1];
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		}
		String value = values[0];
		switch (tv1.getId()) {
			// 树种代码
			case R.id.sz:
				ym.setSZDM(value);
				break;
			case R.id.xj:
				ym.setXIONGJING(Double.valueOf(value));
				break;
			// 林木质量
			case R.id.lmzl:
				ym.setLMZL(value);
				break;
			// 立木类型
			case R.id.lmlx:
				ym.setLMLX(value);
				break;
			// 所属林层
			case R.id.sslc:
				ym.setSSLC(value);
				break;
			// 备注
			case R.id.bz:
				ym.setBEIZHU(value);
				break;

		}
		boolean idupdate = DataBaseHelper.updateYm(mContext, ym,
				ErDiaoActivity.datapath);
		if (idupdate) {
			ToastUtil.setToast(mContext, "样木更新成功");
			ymdcadapter.notifyDataSetChanged();
		} else {
			ToastUtil.setToast(mContext, "样木更新失败");
		}

	}

	// 二调保存数据
	public void ed_btn_complete(Context mContext, EdLineAdapter edlineadaper) {
		btn_complete(mContext, line);
		edlineadaper.notifyDataSetChanged();
	}

	// 连续清查保存数据
	public void lxqc_btn_complete(Context mContext) {
		lxqc_btn_complete(mContext, line);
		lxqcLineAdapter.notifyDataSetChanged();
	}

	/** 小班区划和样地调查保存数据 */
	public void btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String[] values = content.getText().toString().split("-");
		if(values.length < 2){
			ToastUtil.setToast(mContext, "数据输入有误");
			return;
		}
		String curvalue = values[1];
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(curvalue)) {
			return;

		} else if (values[1].length() > curline.getfLength()) {
			ToastUtil.setToast((Activity) mContext, "所填字段长度超出数据库约束长度");
			return;
		} else {
			attribute.put(name, values[0]);
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

	}

	/** 保存数据 */
	public void lxqc_btn_complete(Context mContext, Line curline) {
		String bfvalue = String.valueOf(curline.getText());
		String name = curline.getKey();
		String value = content.getText().toString();
		// 判断修改值与之前值是否相等
		if (bfvalue.equals(value)) {
			return;

		} else {
			attribute.put(name, value);
			long featureid = curfeture.getId();
			Graphic updateGraphic = new Graphic(curfeture.getGeometry(),
					curfeture.getSymbol(), attribute);
			try {
				curfeaturelayer.getFeatureTable().updateFeature(featureid, updateGraphic);
				curline.setText(value);
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
