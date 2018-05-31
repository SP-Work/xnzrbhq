package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.dialog.XzzyDialog;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Created by li on 2017/6/2.
 * 树（毛竹）高测量 adapter
 */
public class LxqcSgclAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	private TextView pjxj;
	private TextView pjsg;
	private TextView pjzzxg;

	public LxqcSgclAdapter(Context context ,List<HashMap<String, String>> list,TextView pjxj,TextView pjsg,TextView pjzzxg) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.pjxj=pjxj;
		this.pjsg=pjsg;
		this.pjzzxg=pjzzxg;
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
		if (BussUtil.isEmperty(map.get("YMH").trim())) {
			holder.tv1.setText(map.get("YMH").trim());
		} else {
			holder.tv1.setText("");
		}
		if (BussUtil.isEmperty(map.get("SZ").trim())) {
			holder.tv2.setText(map.get("SZ").trim());
		} else {
			holder.tv2.setText("");
		}
		if (BussUtil.isEmperty(map.get("XJ").trim())) {
			holder.tv3.setText(map.get("XJ").trim());
		} else {
			holder.tv3.setText("");
		}
		if (BussUtil.isEmperty(map.get("SG").trim())) {
			holder.tv4.setText(map.get("SG").trim());
		} else {
			holder.tv4.setText("");
		}
		if (BussUtil.isEmperty(map.get("ZZXG").trim())) {
			holder.tv5.setText(map.get("ZZXG").trim());
		} else {
			holder.tv5.setText("");
		}
		final TextView ymhtv=holder.tv1;
		holder.tv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context, "样木号", ymhtv, map, "YMH", list,null,"1","","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView sztv=holder.tv2;
		holder.tv2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<Row> listtemp =DataBaseHelper.searchShuZhongData(context);
				XzzyDialog dialog = new XzzyDialog(context,"树种", listtemp, sztv,map,"SZ");
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
			}
		});
		final TextView xjtv=holder.tv3;
		holder.tv3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context,  "胸径(厘米)", xjtv, map, "XJ",list,pjxj,"0","2","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView sgtv=holder.tv4;
		holder.tv4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context,  "树高(米)", sgtv, map, "SG", list,pjsg,"0","1","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		final TextView zzxgtv=holder.tv5;
		holder.tv5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziDialog shuzidialog=new ShuziDialog(context,  "竹枝下高(米)", zzxgtv, map, "ZZXG", list,pjzzxg,"0","1","");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		holder.tv6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.remove(list.get(position));

				//计算平均树高
				float pjsgft=0;
				for(int i=0;i<list.size();i++){
					if(!"".equals(list.get(i).get("SG"))){
						pjsgft=pjsgft+Float.parseFloat(list.get(i).get("SG"));
					}
				}
				String[]split=((pjsgft/(list.size()))+"").split("\\.", -1);
				if(split.length==2){
					if(split[1].length()>1){
						pjsgft=Float.parseFloat(split[0]+"."+split[1].substring(0, 1));
					}else{
						pjsgft=Float.parseFloat(split[0]+"."+split[1]);
					}
				}
				pjsg.setText(pjsgft+"");

				//计算平均胸径
				float pjxjft=0;
				for(int i=0;i<list.size();i++){
					if(!"".equals(list.get(i).get("XJ"))){
						pjxjft=pjxjft+Float.parseFloat(list.get(i).get("XJ"));
					}
				}
				String[]split1=((pjxjft/(list.size()))+"").split("\\.", -1);
				if(split1.length==2){
					if(split1[1].length()>2){
						pjxjft=Float.parseFloat(split1[0]+"."+split1[1].substring(0, 2));
					}else{
						pjxjft=Float.parseFloat(split1[0]+"."+split1[1]);
					}
				}
				pjxj.setText(pjxjft+"");

				//计算平均竹枝下高
				float pjzzxgft=0;
				for(int i=0;i<list.size();i++){
					if(!"".equals(list.get(i).get("ZZXG"))){
						pjzzxgft=pjzzxgft+Float.parseFloat(list.get(i).get("ZZXG"));
					}
				}
				String[]split2=((pjzzxgft/(list.size()))+"").split("\\.", -1);
				if(split2.length==2){
					if(split2[1].length()>1){
						pjzzxgft=Float.parseFloat(split2[0]+"."+split2[1].substring(0, 1));
					}else{
						pjzzxgft=Float.parseFloat(split2[0]+"."+split2[1]);
					}
				}
				pjzzxg.setText(pjzzxgft+"");

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
