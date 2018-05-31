package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcZjclAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ToastUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 连续清查 周界测量记录 dialog
 */

public class SlzylxqcZjcljlDialog extends Dialog {

	Context context;
	static List<HashMap<String, String>>list = new ArrayList<>();
	private List<HashMap<String, String>>gylist = new ArrayList<>();
	String ydhselect;
	static LxqcZjclAdapter adapterzj;
	static TextView zjzcwc;
	private HashMap<String, String> gymap = new HashMap<>();
	private TextView ycjl;
	private TextView sywc;
	private TextView zjjdbhc;
	private TextView zjxdbhc;

	public SlzylxqcZjcljlDialog(Context context,List<HashMap<String, String>>list,List<HashMap<String, String>>gylist, String ydhselect) {
		super(context, R.style.Dialog);
		this.context=context;
		this.list=list;
		this.ydhselect=ydhselect;
		this.gylist=gylist;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_zjcljl_view);
		TextView ydh=(TextView) findViewById(R.id.ydh);
		ycjl=(TextView) findViewById(R.id.ycjl);
		sywc=(TextView) findViewById(R.id.sywc);
		zjjdbhc=(TextView) findViewById(R.id.zjjdbhc);
		zjxdbhc=(TextView) findViewById(R.id.zjxdbhc);
		zjzcwc=(TextView) findViewById(R.id.zjzcwc);
		if(gylist!=null&& gylist.size()!=0){
			gymap=gylist.get(0);
			zjjdbhc.setText(gymap.get("ZJJDBHC"));
			zjxdbhc.setText(gymap.get("ZJXDBHC"));
		}else{
			gymap=new HashMap<String, String>();
			gymap.put("YDH", ydhselect);
			gymap.put("ZJJDBHC", "");
			gymap.put("ZJXDBHC", "");
			gymap.put("ZJZCWC", "");
		}
		zjxdbhc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LxqcUtil.showAlertDialog(context, "周界相对闭合差(%)", zjxdbhc, null, "","0","1", "2");
			}
		});
		zjjdbhc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context, "周界绝对闭合差(米)", zjjdbhc, zjxdbhc, "0", "2", "1");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		zjzcwc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context, "周界周长误差(米)", zjzcwc, null, "0", "2", "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		ydh.setText(ydhselect);

		// 已测距离和剩余距离处理
		List<Double> sywclist = new ArrayList<Double>();
		List<Double> syyclist = new ArrayList<Double>();
		sywclist.add(25.82);
		sywclist.add(25.82);
		sywclist.add(25.82);
		sywclist.add(25.82);
		String wcjltx = "";
		String ycjltx = "";
		double wc=25.82;
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> map = list.get(i);
			double a =0;
			double b =0;
			if (!"".equals(map.get("FWJ"))&& !"".equals(map.get("SPJ"))) {
				a = Double.parseDouble(map.get("FWJ"));
				b = Double.parseDouble(map.get("SPJ"));
			}else if("".equals(map.get("FWJ"))&& !"".equals(map.get("SPJ"))){
				a=0;
				b = Double.parseDouble(map.get("SPJ"));
			}else if(!"".equals(map.get("FWJ"))&& "".equals(map.get("SPJ"))){
				a = Double.parseDouble(map.get("FWJ"));
				b=0;
			}else if("".equals(map.get("FWJ"))&& "".equals(map.get("SPJ"))){
				a =0;
				b= 0;
			}
			double c=0;
			if(sywclist.size()==4){
				c = Math.cos(a * Math.PI / 180) * b;
				if ((c + "").contains(".")) {
					String[] split = (c + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							c = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				syyclist.add(c);
				if(c>0){
					wc=wc-c;
				}
				if ((wc + "").contains(".")) {
					String[] split = (wc + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							wc = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				if(wc>0){
					wcjltx="("+wc+")(25.82)(25.82)(25.82)";
				}else{
					wc=25.82;
					wcjltx="(25.82)(25.82)(25.82)";
					sywclist.remove(sywclist.get(3));
				}
			}else if(sywclist.size()==3){
				c = Math.sin(a * Math.PI / 180) * b;
				if ((c + "").contains(".")) {
					String[] split = (c + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							c = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				syyclist.add(c);
				if(c>0){
					wc=wc-c;
				}
				if ((wc + "").contains(".")) {
					String[] split = (wc + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							wc = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				if(wc>0){
					wcjltx="("+wc+")(25.82)(25.82)";
				}else{
					wc=25.82;
					wcjltx="(25.82)(25.82)";
					sywclist.remove(sywclist.get(2));
				}
			}else if(sywclist.size()==2){
				c = -Math.cos(a * Math.PI / 180) * b;
				if ((c + "").contains(".")) {
					String[] split = (c + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							c = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				syyclist.add(c);
				if(c>0){
					wc=wc-c;
				}
				if ((wc + "").contains(".")) {
					String[] split = (wc + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							wc = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				if(wc>0){
					wcjltx="("+wc+")(25.82)";
				}else{
					wc=25.82;
					wcjltx="(25.82)";
					sywclist.remove(sywclist.get(1));
				}
			}else if(sywclist.size()==1){
				c = -Math.sin(a * Math.PI / 180) * b;
				if ((c + "").contains(".")) {
					String[] split = (c + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							c = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				syyclist.add(c);
				if(c>0){
					wc=wc-c;
				}
				if ((wc + "").contains(".")) {
					String[] split = (wc + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 2) {
							wc = Double.parseDouble(split[0] + "."
									+ split[1].substring(0, 2));
						}
					}
				}
				if(wc>0){
					wcjltx="("+wc+")";
				}else{
					wcjltx="测量完毕";
					sywclist.remove(sywclist.get(0));
				}
			}

		}
		for(int i=0;i<syyclist.size();i++){
			ycjltx=ycjltx+"("+syyclist.get(i)+")";
		}
		if (ycjl != null) {
			ycjl.setText(ycjltx);
		}
		if (sywc != null) {
			sywc.setText(wcjltx);
		}

		//周界测量误差
		double a=0;
		for(int i=0;i<list.size();i++){
			if(!"".equals(list.get(i).get("SPJ").toString())){
				a=a+Double.parseDouble(list.get(i).get("SPJ").toString());
			}
		}
		double b=a-(4*25.82);
		if ((b + "").contains(".")) {
			String[] split = (b + "").split("\\.", -1);
			if (split.length == 2) {
				if (split[1].length() > 2) {
					b = Double.parseDouble(split[0] + "."
							+ split[1].substring(0, 2));
				}
			}
		}
		zjzcwc.setText(b+"");

		ListView listview=(ListView) findViewById(R.id.listview);
		adapterzj=new LxqcZjclAdapter(context, list, ycjl, sywc,zjzcwc);
		listview.setAdapter(adapterzj);
		TextView addmore=(TextView) findViewById(R.id.addmore);
		//添加更多
		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String>map=new HashMap<String, String>();
				map.put("YDH", "");
				map.put("CZ", "");
				map.put("FWJ", "");
				map.put("QXJ", "");
				map.put("XJ", "");
				map.put("SPJ", "25.82");
				list.add(map);
				adapterzj.notifyDataSetChanged();
			}
		});
		//保存
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DataBaseHelper.deleteLxqcZjcljlData(context, ydhselect);
				String xdbhctx=zjxdbhc.getText().toString();
				String jdbhctx=zjjdbhc.getText().toString();
				String zcwctx=zjzcwc.getText().toString();
				DataBaseHelper.addLxqcZjcljlGyData(context, ydhselect,jdbhctx, xdbhctx, zcwctx);
				for(int i=0;i<list.size();i++){
					HashMap<String, String>map=list.get(i);
					DataBaseHelper.addLxqcZjcljlData(context, ydhselect, map.get("CZ"), map.get("FWJ"), map.get("QXJ"), map.get("XJ"), map.get("SPJ"));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
			}
		});
		//取消
		Button cancle=(Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
	public static void refreshdata(){
		adapterzj.notifyDataSetChanged();
		//累计值
		double a=0;
		for(int i=0;i<list.size();i++){
			if(!"".equals(list.get(i).get("SPJ").trim())){
				a=a+Double.parseDouble(list.get(i).get("SPJ").trim());
			}
		}
		double b=a-(4*25.82);
		if ((b + "").contains(".")) {
			String[] split = (b + "").split("\\.", -1);
			if (split.length == 2) {
				if (split[1].length() > 2) {
					b = Double.parseDouble(split[0] + "."
							+ split[1].substring(0, 2));
				}
			}
		}
		zjzcwc.setText(b+"");
	}
}
