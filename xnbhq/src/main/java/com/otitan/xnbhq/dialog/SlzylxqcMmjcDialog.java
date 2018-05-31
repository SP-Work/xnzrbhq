package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcMmjcdcAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.BussUtil;
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
 * 每木检尺dialog
 */
public class SlzylxqcMmjcDialog extends Dialog implements
		View.OnClickListener {
	Context context;
	String ydhselect;
	static List<HashMap<String, String>> listtemp;
	static List<HashMap<String, String>> list;
	static LxqcMmjcdcAdapter adapter;
	static TextView ymhgl,qingkong;

	public SlzylxqcMmjcDialog(Context context, String ydhselect,
							  List<HashMap<String, String>> mmjcjlLlist) {
		super(context, R.style.Dialog);
		this.context = context;
		this.ydhselect = ydhselect;
		this.list = mmjcjlLlist;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_mmjclj_view);
		TextView ydh = (TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);
		Button tongji = (Button) findViewById(R.id.tongji);
		tongji.setOnClickListener(this);
		Button ydwzt = (Button) findViewById(R.id.ydwzt);
		ydwzt.setOnClickListener(this);
		ymhgl = (TextView) findViewById(R.id.ymhgl);
		ymhgl.setOnClickListener(this);
		TextView addmore = (TextView) findViewById(R.id.addmore);
		addmore.setOnClickListener(this);
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(this);

		listtemp=new ArrayList<HashMap<String,String>>();
		for(int i=0;i<list.size();i++){
			listtemp.add(list.get(i));
		}
		ListView listview = (ListView) findViewById(R.id.listview);
		adapter = new LxqcMmjcdcAdapter(context, listtemp);
		listview.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			/**统计*/
			case R.id.tongji:

				break;
			/**样木位置图*/
			case R.id.ydwzt:
				SlzylxqcYmwztDialog ymwztdialog=new SlzylxqcYmwztDialog(context,list,ydhselect,adapter);
				BussUtil.setDialogParamsFull(context, ymwztdialog);
				break;
			/**样木号过滤*/
			case R.id.ymhgl:
				ShuziDialog spjdialog=new ShuziDialog(context, "样木号过滤", ymhgl, "1", "", "1");
				BussUtil.setDialogParams(context, spjdialog, 0.5, 0.5);
				break;
			/**添加更多*/
			case R.id.addmore:
				String[] zd = context.getResources().getStringArray(R.array.mmjczd);
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < zd.length; i++) {
					map.put(zd[i], "");
				}
				map.put("YDH", ydhselect);
				if(list.size()==0){
					map.put("YMH", "1");
				}else{
					int a=Integer.parseInt(list.get(list.size()-1).get("YMH"))+1;
					map.put("YMH", a+"");
				}
				map.put("FWJ", "0.0");
				map.put("SPJ", "0.0");
				list.add(map);
				listtemp.add(map);
				adapter.notifyDataSetChanged();
				break;
			/**保存*/
			case R.id.save:
				String[]zd2=context.getResources().getStringArray(R.array.mmjczd);
				DataBaseHelper.deleteLxqcMmjcData(context, ydhselect);
				for(int i=0;i<listtemp.size();i++){
					DataBaseHelper.addLxqcMmjcData(context, zd2, listtemp.get(i));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
				break;
			/**取消*/
			case R.id.cancle:
				dismiss();
				break;
		}

	}
	/**样木过滤*/
	public static void ymgl() {
		listtemp.clear();
		String ymhgltv=ymhgl.getText().toString().trim();
		for(int i=0;i<list.size();i++){
			HashMap<String, String>hashmap=list.get(i);
			if("".equals(ymhgltv)){
				listtemp.add(hashmap);
			}else if(hashmap.get("YMH").contains(ymhgltv)){
				listtemp.add(hashmap);
			}
		}
		adapter.notifyDataSetChanged();
	}

}
