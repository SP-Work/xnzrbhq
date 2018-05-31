package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.HzbjDialog;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.util.BussUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 植被调查 灌木 adapter
 */

public class LxqcZbdcGmAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	private TextView zongzs;
	private TextView zonggmpjg;
	private TextView zonggmpjdj;

	public LxqcZbdcGmAdapter(Context context ,List<HashMap<String, String>> list,TextView zongzs,TextView gmpjg,TextView gmpjdj) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.zongzs=zongzs;
		this.zonggmpjg=gmpjg;
		this.zonggmpjdj=gmpjdj;
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_zwdcjlgm, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String>map=list.get(position);
		if (BussUtil.isEmperty(map.get("GMMC").trim())) {
			holder.tv1.setText(map.get("GMMC").trim());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(map.get("GMZS").trim())) {
			holder.tv2.setText(map.get("GMZS").trim());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(map.get("GMPJG").trim())) {
			holder.tv3.setText(map.get("GMPJG").trim());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(map.get("GMPJDJ").trim())) {
			holder.tv4.setText(map.get("GMPJDJ").trim());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(map.get("GMGD").trim())) {
			holder.tv5.setText(map.get("GMGD").trim());
		} else {
			holder.tv5.setText("");
		}
		final TextView gmmc=holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog hzbjdialog=new HzbjDialog(context, "灌木名称", gmmc,map, "GMMC");
				BussUtil.setDialogParams(context, hzbjdialog, 0.5, 0.5);
			}
		});
		final TextView gmzs=holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context, "灌木珠数", gmzs, map, "GMZS", list,zongzs,"1","","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView gmpjg=holder.tv3;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context,"灌木平均高(米)", gmpjg, map, "GMPJG", list,zonggmpjg,"0","2","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView gmpjdj=holder.tv4;
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context,  "灌木平均地径(厘米)", gmpjdj, map, "GMPJDJ", list,zonggmpjdj,"0","1","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView gmgd=holder.tv5;
		holder.tv5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context,  "灌木盖度(%)", gmgd, map, "GMGD", null,null,"1","","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		holder.tv6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.remove(list.get(position));
				//计算灌木总株数
				int gmzs=0;
				for(int i=0;i<list.size();i++){
					if(!"".equals(list.get(i).get("GMZS"))){
						gmzs=gmzs+Integer.parseInt(list.get(i).get("GMZS"));
					}
				}
				zongzs.setText(gmzs+"");

				//计算灌木平均高
				float zonggmpjgf=0;
				for(int i=0;i<list.size();i++){
					if(!"".equals(list.get(i).get("GMPJG"))){
						zonggmpjgf=zonggmpjgf+Float.parseFloat(list.get(i).get("GMPJG"));
					}
				}
				String[]split=((zonggmpjgf/(list.size()))+"").split("\\.", -1);
				if(split.length==2){
					if(split[1].length()>2){
						zonggmpjgf=Float.parseFloat(split[0]+"."+split[1].substring(0, 2));
					}else{
						zonggmpjgf=Float.parseFloat(split[0]+"."+split[1]);
					}
				}
				zonggmpjg.setText(zonggmpjgf+"");

				//计算灌木平均地径
				float zonggmpjdjf=0;
				for(int i=0;i<list.size();i++){
					if(!"".equals(list.get(i).get("GMPJDJ"))){
						zonggmpjdjf=zonggmpjdjf+Float.parseFloat(list.get(i).get("GMPJDJ"));
					}
				}
				String[]split1=((zonggmpjdjf/(list.size()))+"").split("\\.", -1);
				if(split1.length==2){
					if(split1[1].length()>1){
						zonggmpjdjf=Float.parseFloat(split1[0]+"."+split1[1].substring(0, 1));
					}else{
						zonggmpjdjf=Float.parseFloat(split1[0]+"."+split1[1]);
					}
				}
				zonggmpjdj.setText(zonggmpjdjf+"");
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
	}

}
