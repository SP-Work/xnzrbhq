package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcSgclAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.ToastUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Created by li on 2017/6/2.
 * 树（毛竹）高测量 dialog
 */
public class SlzylxqcSgclDialog extends Dialog {

	Context context;
	List<HashMap<String, String>>list = new ArrayList<>();
	String ydhselect;
	public SlzylxqcSgclDialog(Context context, List<HashMap<String, String>>list,String ydhselect) {
		super(context, R.style.Dialog);
		this.context=context;
		this.list=list;
		this.ydhselect=ydhselect;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_sgcljl_view);
		TextView ydh=(TextView) findViewById(R.id.ydh);
		final TextView pjsgao=(TextView) findViewById(R.id.pjsgao);
		final TextView pjxjin=(TextView) findViewById(R.id.pjxjin);
		final TextView pjzzxg=(TextView) findViewById(R.id.pjzzxg);

		ydh.setText(ydhselect);

		/**计算平均树高*/
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
		pjsgao.setText(pjsgft+"");

		/**计算平均胸径*/
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
		pjxjin.setText(pjxjft+"");

		/**计算平均竹枝下高*/
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


		ListView listview=(ListView) findViewById(R.id.listview);
		final LxqcSgclAdapter adaptersg=new LxqcSgclAdapter(context, list, pjxjin, pjsgao, pjzzxg);
		listview.setAdapter(adaptersg);
		TextView addmore=(TextView) findViewById(R.id.addmore);
		/**添加更多*/
		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for(int i=0;i<list.size();i++){
					HashMap<String, String>lsmap=list.get(i);
					if("".equals(lsmap.get("YMH"))||"".equals(lsmap.get("SZ"))||"".equals(lsmap.get("XJ"))||"".equals(lsmap.get("SG"))||"".equals(lsmap.get("ZZXG"))){
						ToastUtil.setToast(context, context.getResources().getString(R.string.ywtxwzjl));
						return;
					}
				}
				HashMap<String, String>map=new HashMap<String, String>();
				map.put("YMH", "");
				map.put("SZ", "");
				map.put("XJ", "");
				map.put("SG", "");
				map.put("ZZXG", "");
				list.add(map);
				/**计算平均树高*/
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
				pjsgao.setText(pjsgft+"");

				/**计算平均胸径*/
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
				pjxjin.setText(pjxjft+"");

				/**计算平均竹枝下高*/
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
				adaptersg.notifyDataSetChanged();
			}
		});
		/**保存*/
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				DataBaseHelper.deleteLxqcSgcljlData(context, ydhselect);
				for(int i=0;i<list.size();i++){
					HashMap<String, String>map=list.get(i);
					DataBaseHelper.addLxqcSgcljlData(context, ydhselect, map.get("YMH"), map.get("SZ"), map.get("XJ"), map.get("SG"), map.get("ZZXG"));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
			}
		});
		/**删除*/
		Button cancle=(Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});


	}

}
