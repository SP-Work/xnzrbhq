package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.HzbjDialog;
import com.otitan.xnbhq.dialog.ShuziYxclDialog;
import com.otitan.xnbhq.util.BussUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 连续清查 周界测量 adapter
 */

public class LxqcZjclAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	private TextView ycjl;
	private TextView sywc,zjzcwc;

	public LxqcZjclAdapter(Context context ,List<HashMap<String, String>> list,TextView ycjl,TextView sywc,TextView zjzcwc) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.ycjl=ycjl;
		this.sywc=sywc;
		this.zjzcwc=zjzcwc;
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_yxcldc, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String>map=list.get(position);
		if (BussUtil.isEmperty(map.get("CZ").trim())) {
			holder.tv1.setText(map.get("CZ").trim());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(map.get("FWJ").trim())) {
			holder.tv2.setText(map.get("FWJ").trim());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(map.get("QXJ").trim())) {
			holder.tv3.setText(map.get("QXJ").trim());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(map.get("XJ").trim())) {
			holder.tv4.setText(map.get("XJ").trim());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(map.get("SPJ").trim())) {
			holder.tv5.setText(map.get("SPJ").trim());
		} else {
			holder.tv5.setText("");
		}
		//累计值
		float a=0;
		for(int i=0;i<=position;i++){
			if(!"".equals(list.get(i).get("SPJ").trim())){
				a=a+Float.parseFloat(list.get(i).get("SPJ").trim());
			}
		}
		holder.tv6.setText(a+"");
		final TextView cztv=holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog hzdialog=new HzbjDialog(context,"测站", cztv,map, "CZ");
				BussUtil.setDialogParams(context, hzdialog, 0.5, 0.5);
			}
		});
		final TextView tv2=holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog=new ShuziYxclDialog(context, "方位角(°)", tv2, map,"FWJ", "QXJ","XJ","SPJ",  list, "0", "1", null, null, null, "1",ycjl,sywc,"2");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView tv3=holder.tv3;
		final TextView tv4=holder.tv4;
		final TextView tv5=holder.tv5;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog=new ShuziYxclDialog(context, "倾斜角(°)", tv3, map,"QXJ", "QXJ","XJ","SPJ", list, "0", "1", tv3, tv4, tv5, "2",ycjl,sywc,"2");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog=new ShuziYxclDialog(context,"斜距(米)", tv4, map,"XJ", "QXJ","XJ","SPJ", list, "0", "2", tv3, tv4, tv5, "3",ycjl,sywc,"2");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		holder.tv5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog=new ShuziYxclDialog(context, "水平距(米)", tv5, map,"SPJ", "QXJ","XJ","SPJ", list, "0", "2", tv3, tv4, tv5, "4",ycjl,sywc,"2");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		//删除
		holder.tv7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.remove(list.get(position));


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

				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public TextView tv6;
		public TextView tv7;
	}

}
