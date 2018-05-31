package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcXmdcAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 下木调查记录 dialog
 */
public class SlzylxqcXmdcDialog extends Dialog implements View.OnClickListener{

	Context context;
	String ydh;
	List<HashMap<String, String>>list = new ArrayList<>();
	LxqcXmdcAdapter adapter;
	public SlzylxqcXmdcDialog(Context context,String ydh,List<HashMap<String, String>>list) {
		super(context, R.style.Dialog);
		this.ydh=ydh;
		this.context=context;
		this.list=list;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_xmdc_view);

		TextView ydhtv=(TextView) findViewById(R.id.ydh);
		if(ydh!=null){
			ydhtv.setText(ydh);
		}

//		Button save=(Button) findViewById(R.id.save);
//		save.setOnClickListener(this);
//		Button cancle=(Button) findViewById(R.id.cancle);
//		cancle.setOnClickListener(this);
		TextView addmore=(TextView) findViewById(R.id.addmore);
		addmore.setOnClickListener(this);
		ListView listview=(ListView) findViewById(R.id.listview);
		adapter=new LxqcXmdcAdapter(context, list);
		listview.setAdapter(adapter);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			/**保存*/
			case R.id.save:
//			DataBaseHelper.deleteLxqcXmdcbAllData(context, ydh);
//			final String[] zd = context.getResources().getStringArray(R.array.lxqcxmdczd);
//			for(int i=0;i<list.size();i++){
//				DataBaseHelper.addLxqcXmdcbData(context, zd, list.get(i));
//			}
//			ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
				break;
			/**取消*/
			case R.id.cancle:
				dismiss();
				break;
			/**添加更多*/
			case R.id.addmore:
				HashMap<String, String>map=new HashMap<String, String>();
				map.put("YDH",ydh);
				map.put("SZMC", "");
				map.put("GD", "");
				map.put("XJ", "");
				list.add(map);
				String[] zd = context.getResources().getStringArray(R.array.lxqcxmdczd);
				DataBaseHelper.addLxqcXmdcbData(context, zd, map);

				adapter.notifyDataSetChanged();
				break;
		}
	}
}
