package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcKjldcAdapter;
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
 * 森林资源连续清查 跨角林调查dialog
 */
public class SlzylxqcKjldcjlDialog extends Dialog {

	Context context;
	List<HashMap<String, String>>list;
	HashMap<String, String>map;
	static String ydhselect;
	String[] zd;
	String dbpath = null;

	public SlzylxqcKjldcjlDialog(Context context,List<HashMap<String, String>>list,
								 String ydhselect,String path) {
		super(context, R.style.Dialog);
		this.context=context;
		this.list=list;
		this.ydhselect=ydhselect;
		this.dbpath = path;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_kjldcjl_view);

		zd = context.getResources().getStringArray(R.array.kjldcjlzd);

		TextView ydh=(TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);
		if(list==null){
			list=new ArrayList<HashMap<String,String>>();
		}
		if(list.size()==0){
			map=new HashMap<String, String>();
			for(int i=0;i<zd.length;i++){
				map.put(zd[i], "");
			}
			map.put("YDH", ydhselect);
			list.add(map);
		}else{
			map=list.get(0);
		}
		String[] mc=context.getResources().getStringArray(R.array.kjldcjlmc);
		ListView listview=(ListView) findViewById(R.id.listview);
		final LxqcKjldcAdapter kjladapter=new LxqcKjldcAdapter(context, list,mc,dbpath);
		listview.setAdapter(kjladapter);
		/**1清除按钮*/
		Button qc1=(Button) findViewById(R.id.qc1);
		qc1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(list.size()!=0){
					map.put("MJBL1", "");
					map.put("DL1", "");
					map.put("TDQS1", "");
					map.put("LMQS1", "");
					map.put("LZ1", "");
					map.put("QY1", "");
					map.put("YSSZ1", "");
					map.put("LZU1", "");
					map.put("YBD1", "");
					map.put("PJSG1", "");
					map.put("SLQLJG1", "");
					map.put("SZJG1", "");
					map.put("SPLJYDJ1", "");
					kjladapter.notifyDataSetChanged();
				}
			}
		});
		/**2清除按钮*/
		Button qc2=(Button) findViewById(R.id.qc2);
		qc2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(list.size()!=0){
					map.put("MJBL2", "");
					map.put("DL2", "");
					map.put("TDQS2", "");
					map.put("LMQS2", "");
					map.put("LZ2", "");
					map.put("QY2", "");
					map.put("YSSZ2", "");
					map.put("LZU2", "");
					map.put("YBD2", "");
					map.put("PJSG2", "");
					map.put("SLQLJG2", "");
					map.put("SZJG2", "");
					map.put("SPLJYDJ2", "");
					kjladapter.notifyDataSetChanged();
				}
			}
		});
		/**3清除按钮*/
		Button qc3=(Button) findViewById(R.id.qc3);
		qc3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(list.size()!=0){
					map.put("MJBL3", "");
					map.put("DL3", "");
					map.put("TDQS3", "");
					map.put("LMQS3", "");
					map.put("LZ3", "");
					map.put("QY3", "");
					map.put("YSSZ3", "");
					map.put("LZU3", "");
					map.put("YBD3", "");
					map.put("PJSG3", "");
					map.put("SLQLJG3", "");
					map.put("SZJG3", "");
					map.put("SPLJYDJ3", "");
					kjladapter.notifyDataSetChanged();
				}
			}
		});
		/**转抄所有1*/
		Button zcsy1=(Button) findViewById(R.id.zcsy1);
		zcsy1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				map.put("MJBL1", map.get("QQMJBL1"));
				map.put("DL1", map.get("QQDL1"));
				map.put("TDQS1", map.get("QQTDQS1"));
				map.put("LMQS1", map.get("QQLMQS1"));
				map.put("LZ1", map.get("QQLZ1"));
				map.put("QY1", map.get("QQQY1"));
				map.put("YSSZ1", map.get("QQYSSZ1"));
				map.put("LZU1", map.get("QQLZU1"));
				map.put("YBD1", map.get("QQYBD1"));
				map.put("PJSG1", map.get("QQPJSG1"));
				map.put("SLQLJG1", map.get("QQSLQLJG1"));
				map.put("SZJG1", map.get("QQSZJG1"));
				map.put("SPLJYDJ1", map.get("QQSPLJYDJ1"));
				kjladapter.notifyDataSetChanged();
			}
		});
		/**转抄所有2*/
		Button zcsy2=(Button) findViewById(R.id.zcsy2);
		zcsy2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				map.put("MJBL2", map.get("QQMJBL2"));
				map.put("DL2", map.get("QQDL2"));
				map.put("TDQS2", map.get("QQTDQS2"));
				map.put("LMQS2", map.get("QQLMQS2"));
				map.put("LZ2", map.get("QQLZ2"));
				map.put("QY2", map.get("QQQY2"));
				map.put("YSSZ2", map.get("QQYSSZ2"));
				map.put("LZU2", map.get("QQLZU2"));
				map.put("YBD2", map.get("QQYBD2"));
				map.put("PJSG2", map.get("QQPJSG2"));
				map.put("SLQLJG2", map.get("QQSLQLJG2"));
				map.put("SZJG2", map.get("QQSZJG2"));
				map.put("SPLJYDJ2", map.get("QQSPLJYDJ2"));
				kjladapter.notifyDataSetChanged();
			}
		});
		/**转抄所有3*/
		Button zcsy3=(Button) findViewById(R.id.zcsy3);
		zcsy3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				map.put("MJBL3", map.get("QQMJBL3"));
				map.put("DL3", map.get("QQDL3"));
				map.put("TDQS3", map.get("QQTDQS3"));
				map.put("LMQS3", map.get("QQLMQS3"));
				map.put("LZ3", map.get("QQLZ3"));
				map.put("QY3", map.get("QQQY3"));
				map.put("YSSZ3", map.get("QQYSSZ3"));
				map.put("LZU3", map.get("QQLZU3"));
				map.put("YBD3", map.get("QQYBD3"));
				map.put("PJSG3", map.get("QQPJSG3"));
				map.put("SLQLJG3", map.get("QQSLQLJG3"));
				map.put("SZJG3", map.get("QQSZJG3"));
				map.put("SPLJYDJ3", map.get("QQSPLJYDJ3"));
				kjladapter.notifyDataSetChanged();
			}
		});
		/**取消按钮*/
		Button cancle=(Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		/**保存按钮*/
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DataBaseHelper.deleteLxqcKjldcjlData(context, ydhselect);
				HashMap<String, String>map=list.get(0);
				DataBaseHelper.addLxqcKjldcjlData(context, zd, map);
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
			}
		});
	}
}
