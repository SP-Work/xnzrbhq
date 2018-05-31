package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcYxclAdapter;
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
 *  连续清查 样地 引线测量 dialog
 */

public class SlzylxqcYxcljlDialog extends Dialog {

	Context context;
	List<HashMap<String, String>>list = new ArrayList<>();
	String ydhselect;
	static LxqcYxclAdapter adapteryx;
	public SlzylxqcYxcljlDialog(Context context,List<HashMap<String, String>>list, String ydhselect) {
		super(context, R.style.Dialog);
		this.context=context;
		this.list=list;
		this.ydhselect=ydhselect;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_yxcljl_view);
		TextView ydh=(TextView) findViewById(R.id.ydh);
		final TextView ycjl=(TextView) findViewById(R.id.ycjl);
		final TextView sywc=(TextView) findViewById(R.id.sywc);
		ydh.setText(ydhselect);

		/**已测距离和剩余距离处理*/
		List<Double>sywclist=new ArrayList<Double>();
		String ycjltx="";
		for(int i=0;i<list.size();i++){
			HashMap<String, String>map=list.get(i);
			if(!"".equals(map.get("FWJ"))&&!"".equals(map.get("SPJ"))){
				double a=Double.parseDouble(map.get("FWJ"));
				double b=Double.parseDouble(map.get("SPJ"));
				double c=Math.cos(a*Math.PI/180)*b;
				if((c+"").contains(".")){
					String[] split = (c+"").split("\\.", -1);
					if(split.length==2){
						if(split[1].length()>2){
							c=Double.parseDouble(split[0]+"."+split[1].substring(0, 2));
						}
					}
				}
				sywclist.add(c);
				ycjltx=ycjltx+"("+c+")";
			}else if("".equals(map.get("FWJ"))&&!"".equals(map.get("SPJ"))){
				double b=Double.parseDouble(map.get("SPJ"));
				if((b+"").contains(".")){
					String[] split = (b+"").split("\\.", -1);
					if(split.length==2){
						if(split[1].length()>2){
							b=Double.parseDouble(split[0]+"."+split[1].substring(0, 2));
						}
					}
				}
				sywclist.add(b);
				ycjltx=ycjltx+"("+b+")";
			}else{
				ycjltx=ycjltx+"("+0+")";
			}
		}
		ycjl.setText(ycjltx);
		double a=0;
		for(int i=0;i<sywclist.size();i++){
			a=a+sywclist.get(i);
		}
		if((a+"").contains(".")){
			String[] split = (a+"").split("\\.", -1);
			if(split.length==2){
				if(split[1].length()>2){
					a=Double.parseDouble(split[0]+"."+split[1].substring(0, 2));
				}
			}
		}
		sywc.setText("("+a+")");


		ListView listview=(ListView) findViewById(R.id.listview);
		adapteryx=new LxqcYxclAdapter(context, list, ycjl, sywc);
		listview.setAdapter(adapteryx);
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
				map.put("SPJ", "");
				list.add(map);
				adapteryx.notifyDataSetChanged();
			}
		});
		/**保存*/
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				DataBaseHelper.deleteLxqcYxcljlData(context, ydhselect);
				for(int i=0;i<list.size();i++){
					HashMap<String, String>map=list.get(i);
					DataBaseHelper.addLxqcYxcljlData(context, ydhselect, map.get("CZ"), map.get("FWJ"), map.get("QXJ"), map.get("XJ"), map.get("SPJ"));
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
	public static void refreshdata(){
		adapteryx.notifyDataSetChanged();
	}
}
