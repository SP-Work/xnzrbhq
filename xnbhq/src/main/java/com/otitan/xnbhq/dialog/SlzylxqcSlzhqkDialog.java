package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcSlzhdcAdapter;
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
 * 森林灾害情况 dialog
 */
public class SlzylxqcSlzhqkDialog extends Dialog implements View.OnClickListener{
	Context context;
	List<HashMap<String, String>> list = new ArrayList<>();
	String ydhselect;
	LxqcSlzhdcAdapter adapter;
	public SlzylxqcSlzhqkDialog(Context context,List<HashMap<String, String>> list,String ydhselect) {
		super(context, R.style.Dialog);
		this.context=context;
		this.list=list;
		this.ydhselect=ydhselect;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_slzhdc_view);

		TextView ydh=(TextView) findViewById(R.id.ydh);
		if(ydhselect!=null){
			ydh.setText(ydhselect);
		}

		ListView listview=(ListView) findViewById(R.id.listview);
		adapter=new LxqcSlzhdcAdapter(context, list);
		listview.setAdapter(adapter);

		TextView addmore=(TextView) findViewById(R.id.addmore);
		addmore.setOnClickListener(this);
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		Button cancle=(Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			/**添加更多*/
			case R.id.addmore:
				HashMap<String, String>map=new HashMap<String, String>();
				map.put("YDH", ydhselect);
				map.put("XH", "");
				map.put("ZHLX", "");
				map.put("WHBW", "");
				map.put("SHYMZS", "");
				map.put("SHDJ", "");
				list.add(map);
				adapter.notifyDataSetChanged();
				break;
			/**保存*/
			case R.id.save:
				DataBaseHelper.deleteLxqcSlzhAllData(context, ydhselect);
				final String[] zd = context.getResources().getStringArray(R.array.lxqcslzhzd);
				for(int i=0;i<list.size();i++){
					DataBaseHelper.addLxqcSlzhqkData(context, zd, list.get(i));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
				break;
			/**取消*/
			case R.id.cancle:
				dismiss();
				break;

		}
	}
}
