package com.otitan.xnbhq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.dialog.XzzyDialog;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.titan.baselibrary.customview.MorePopWindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Created by li on 2017/6/2.
 * 森林资源连续清查 跨角林调查dialog adapter
 */
public class LxqcKjldcAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<HashMap<String, String>> list = new ArrayList<>();
	private Context context;
	private String[] mc;
	private HashMap<String, String> map = new HashMap<>();
	private MorePopWindow popWindow;
	private BaseActivity activity;
	private String name="";
	private String dbpath;

	public LxqcKjldcAdapter(Context context, List<HashMap<String, String>> list,String[] mc,String path) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mc = mc;
		this.activity=(BaseActivity) context;
		this.name=context.getResources().getString(R.string.ysszname);
		this.dbpath = path;
	}

	@Override
	public int getCount() {
		return 13;
	}

	@Override
	public Object getItem(int arg0) {
		return "";
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
			convertView = inflater.inflate(R.layout.item_slzylxqc_kjldc, null);
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
		if(list.size()!=0){
			map = list.get(0);
		}
		holder.tv1.setText(mc[position]);
		final TextView tv2=holder.tv2;
		//前期1
		final TextView tv3=holder.tv3;
		final TextView tv4=holder.tv4;
		//前期2
		final TextView tv5=holder.tv5;
		final TextView tv6=holder.tv6;
		//前期3
		final TextView tv7=holder.tv7;
		if(0==position){
			holder.tv2.setText(map.get("MJBL1"));
			holder.tv3.setText(map.get("QQMJBL1"));
			holder.tv4.setText(map.get("MJBL2"));
			holder.tv5.setText(map.get("QQMJBL2"));
			holder.tv6.setText(map.get("MJBL3"));
			holder.tv7.setText(map.get("QQMJBL3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context, "面积比例", tv2, map, "MJBL1",  list, null, "0", "2", "1");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"MJBL1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context, "面积比例", tv4, map, "MJBL2",  list, null, "0", "2", "1");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"MJBL2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context,  "面积比例", tv6, map, "MJBL3",  list, null, "0", "2", "1");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"MJBL3");
				}
			});
		}else if(1==position){
			holder.tv2.setText(map.get("DL1"));
			holder.tv3.setText(map.get("QQDL1"));
			holder.tv4.setText(map.get("DL2"));
			holder.tv5.setText(map.get("QQDL2"));
			holder.tv6.setText(map.get("DL3"));
			holder.tv7.setText(map.get("QQDL3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "EDDL");
					XzzyDialog dialog = new XzzyDialog(context,"地类", listtemp, tv2,map,"DL1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"DL1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "EDDL");
					XzzyDialog dialog = new XzzyDialog(context,"地类", listtemp, tv4,map,"DL2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"DL2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "EDDL");
					XzzyDialog dialog = new XzzyDialog(context,"地类", listtemp, tv6,map,"DL3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"DL3");
				}
			});
		}else if(2==position){
			holder.tv2.setText(map.get("TDQS1"));
			holder.tv3.setText(map.get("QQTDQS1"));
			holder.tv4.setText(map.get("TDQS2"));
			holder.tv5.setText(map.get("QQTDQS2"));
			holder.tv6.setText(map.get("TDQS3"));
			holder.tv7.setText(map.get("QQTDQS3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LDSYQ");
					XzzyDialog dialog = new XzzyDialog(context,"土地权属", listtemp, tv2,map,"TDQS1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"TDQS1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LDSYQ");
					XzzyDialog dialog = new XzzyDialog(context,"土地权属", listtemp, tv4,map,"TDQS2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"TDQS2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LDSYQ");
					XzzyDialog dialog = new XzzyDialog(context,"土地权属", listtemp, tv6,map,"TDQS3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"TDQS3");
				}
			});
		}else if(3==position){
			holder.tv2.setText(map.get("LMQS1"));
			holder.tv3.setText(map.get("QQLMQS1"));
			holder.tv4.setText(map.get("LMQS2"));
			holder.tv5.setText(map.get("QQLMQS2"));
			holder.tv6.setText(map.get("LMQS3"));
			holder.tv7.setText(map.get("QQLMQS3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LMSYQ");
					XzzyDialog dialog = new XzzyDialog(context,"林木权属", listtemp, tv2,map,"LMQS1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"LMQS1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LMSYQ");
					XzzyDialog dialog = new XzzyDialog(context,"林木权属", listtemp, tv4,map,"LMQS2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"LMQS2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LMSYQ");
					XzzyDialog dialog = new XzzyDialog(context,"林木权属", listtemp, tv6,map,"LMQS3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"LMQS3");
				}
			});
		}else if(4==position){
			holder.tv2.setText(map.get("LZ1"));
			holder.tv3.setText(map.get("QQLZ1"));
			holder.tv4.setText(map.get("LZ2"));
			holder.tv5.setText(map.get("QQLZ2"));
			holder.tv6.setText(map.get("LZ3"));
			holder.tv7.setText(map.get("QQLZ3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LINZHONG");
					XzzyDialog dialog = new XzzyDialog(context,"林种", listtemp, tv2,map,"LZ1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"LZ1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LINZHONG");
					XzzyDialog dialog = new XzzyDialog(context,"林种", listtemp, tv4,map,"LZ2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"LZ2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LINZHONG");
					XzzyDialog dialog = new XzzyDialog(context,"林种", listtemp, tv6,map,"LZ3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"LZ3");
				}
			});
		}else if(5==position){
			holder.tv2.setText(map.get("QY1"));
			holder.tv3.setText(map.get("QQQY1"));
			holder.tv4.setText(map.get("QY2"));
			holder.tv5.setText(map.get("QQQY2"));
			holder.tv6.setText(map.get("QY3"));
			holder.tv7.setText(map.get("QQQY3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "QIYUAN");
					XzzyDialog dialog = new XzzyDialog(context,"起源", listtemp, tv2,map,"QY1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"QY1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "QIYUAN");
					XzzyDialog dialog = new XzzyDialog(context,"起源", listtemp, tv4,map,"QY2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"QY2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "QIYUAN");
					XzzyDialog dialog = new XzzyDialog(context,"起源", listtemp, tv6,map,"QY3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"QY3");
				}
			});
		}else if(6==position){
			holder.tv2.setText(map.get("YSSZ1"));
			holder.tv3.setText(map.get("QQYSSZ1"));
			holder.tv4.setText(map.get("YSSZ2"));
			holder.tv5.setText(map.get("QQYSSZ2"));
			holder.tv6.setText(map.get("YSSZ3"));
			holder.tv7.setText(map.get("QQYSSZ3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> bqysszlist=LxqcUtil.getAttributeList(context,name,dbpath);
					XzzyDialog bqysszdialog = new XzzyDialog(context,"优势树种", bqysszlist, tv2,map, "YSSZ1");
					BussUtil.setDialogParams(context, bqysszdialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"YSSZ1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> bqysszlist =LxqcUtil.getAttributeList(context,name,dbpath);
					XzzyDialog bqysszdialog = new XzzyDialog(context,"优势树种", bqysszlist, tv4,map, "YSSZ2");
					BussUtil.setDialogParams(context, bqysszdialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"YSSZ2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> bqysszlist =LxqcUtil.getAttributeList(context,name,dbpath);
					XzzyDialog bqysszdialog = new XzzyDialog(context,"优势树种", bqysszlist, tv6,map, "YSSZ3");
					BussUtil.setDialogParams(context, bqysszdialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"YSSZ3");
				}
			});
		}else if(7==position){
			holder.tv2.setText(map.get("LZU1"));
			holder.tv3.setText(map.get("QQLZU1"));
			holder.tv4.setText(map.get("LZU2"));
			holder.tv5.setText(map.get("QQLZU2"));
			holder.tv6.setText(map.get("LZU3"));
			holder.tv7.setText(map.get("QQLZU3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LINGZU");
					XzzyDialog dialog = new XzzyDialog(context, "龄组", listtemp, tv2,map,"LZU1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"LZU1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LINGZU");
					XzzyDialog dialog = new XzzyDialog(context,"龄组", listtemp, tv4,map,"LZU2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"LZU2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "LINGZU");
					XzzyDialog dialog = new XzzyDialog(context,"龄组", listtemp, tv6,map,"LZU3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"LZU3");
				}
			});
		}else if(8==position){
			holder.tv2.setText(map.get("YBD1"));
			holder.tv3.setText(map.get("QQYBD1"));
			holder.tv4.setText(map.get("YBD2"));
			holder.tv5.setText(map.get("QQYBD2"));
			holder.tv6.setText(map.get("YBD3"));
			holder.tv7.setText(map.get("QQYBD3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context,"郁闭度", tv2, map, "YBD1",  list, null, "0", "2", "");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"YBD1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context,  "郁闭度", tv4, map, "YBD2",  list, null, "0", "2", "");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"YBD2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context,  "郁闭度", tv6, map, "YBD3", list, null, "0", "2", "");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"YBD3");
				}
			});
		}else if(9==position){
			holder.tv2.setText(map.get("PJSG1"));
			holder.tv3.setText(map.get("QQPJSG1"));
			holder.tv4.setText(map.get("PJSG2"));
			holder.tv5.setText(map.get("QQPJSG2"));
			holder.tv6.setText(map.get("PJSG3"));
			holder.tv7.setText(map.get("QQPJSG3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context, "平均树高", tv2, map, "PJSG1", list, null, "0", "1", "");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"PJSG1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context,  "平均树高", tv4, map, "PJSG2",  list, null, "0", "1", "");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"PJSG2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShuziDialog shuzidialog=new ShuziDialog(context,  "平均树高", tv6, map, "PJSG3",list, null, "0", "1", "");
					BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"PJSG3");
				}
			});
		}else if(10==position){
			holder.tv2.setText(map.get("SLQLJG1"));
			holder.tv3.setText(map.get("QQSLQLJG1"));
			holder.tv4.setText(map.get("SLQLJG2"));
			holder.tv5.setText(map.get("QQSLQLJG2"));
			holder.tv6.setText(map.get("SLQLJG3"));
			holder.tv7.setText(map.get("QQSLQLJG3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SLQLJG");
					XzzyDialog dialog = new XzzyDialog(context,"森林群落结构", listtemp, tv2,map,"SLQLJG1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"SLQLJG1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SLQLJG");
					XzzyDialog dialog = new XzzyDialog(context,"森林群落结构", listtemp, tv4,map,"SLQLJG2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"SLQLJG2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SLQLJG");
					XzzyDialog dialog = new XzzyDialog(context,"森林群落结构", listtemp, tv6,map,"SLQLJG3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"SLQLJG3");
				}
			});
			//树种结构
		}else if(11==position){
			holder.tv2.setText(map.get("SZJG1"));
			holder.tv3.setText(map.get("QQSZJG1"));
			holder.tv4.setText(map.get("SZJG2"));
			holder.tv5.setText(map.get("QQSZJG2"));
			holder.tv6.setText(map.get("SZJG3"));
			holder.tv7.setText(map.get("QQSZJG3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SZJG");
					XzzyDialog dialog = new XzzyDialog(context,"树种结构", listtemp, tv2,map,"SZJG1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv3,tv2,map,"SZJG1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SZJG");
					XzzyDialog dialog = new XzzyDialog(context, "树种结构", listtemp, tv4,map,"SZJG2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"SZJG2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SZJG");
					XzzyDialog dialog = new XzzyDialog(context, "树种结构", listtemp, tv6,map,"SZJG3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"SZJG3");
				}
			});
			//商品林经营等级
		}else if(12==position){
			holder.tv2.setText(map.get("SPLJYDJ1"));
			holder.tv3.setText(map.get("QQSPLJYDJ1"));
			holder.tv4.setText(map.get("SPLJYDJ2"));
			holder.tv5.setText(map.get("QQSPLJYDJ2"));
			holder.tv6.setText(map.get("SPLJYDJ3"));
			holder.tv7.setText(map.get("QQSPLJYDJ3"));
			holder.tv2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SPLJYDJ");
					XzzyDialog dialog = new XzzyDialog(context,"商品林经营等级", listtemp, tv2,map,"SPLJYDJ1");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"SPLJYDJ1");
				}
			});
			holder.tv4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SPLJYDJ");
					XzzyDialog dialog = new XzzyDialog(context,"商品林经营等级", listtemp, tv4,map,"SPLJYDJ2");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv5,tv4,map,"SPLJYDJ2");
				}
			});
			holder.tv6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					List<Row> listtemp = ResourcesManager.getAssetsAttributeList(
							context, "slzylxqc.xml", "SPLJYDJ");
					XzzyDialog dialog = new XzzyDialog(context,"商品林经营等级", listtemp, tv6,map,"SPLJYDJ3");
					BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				}
			});
			holder.tv7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showPop(tv7,tv6,map,"SPLJYDJ3");
				}
			});
		}

		return convertView;
	}
	// 显示长按后的转抄当前POP
	private void showPop(final TextView item,final TextView bqtv, final HashMap<String, String> map2, final String zd) {
		popWindow = new MorePopWindow(activity, R.layout.popup_lxqc);
		popWindow.showAsDropDown(item,
				item.getWidth() / 2 - popWindow.getWidth() / 2,
				item.getHeight() - 35);
		TextView pop = (TextView) popWindow.getConentView().findViewById(R.id.pop);
		pop.setText("转抄当前");
		pop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String itemstr=item.getText().toString();
				bqtv.setText(itemstr);
				map2.put(zd, itemstr);
				popWindow.dismiss();
			}
		});

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
