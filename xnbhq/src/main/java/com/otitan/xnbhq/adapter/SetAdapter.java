package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.util.Util;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;
import jsqlite.Exception;

/**
 * 小班查询Adapter
 */
public class SetAdapter extends BaseAdapter {
	private List<GeodatabaseFeature> list = new ArrayList<>();
	private LayoutInflater inflater = null;
	private Context context;
	private String pname;
	private MyLayer myLayer;
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	private Map<String, String> xiangMap = new HashMap<>();
	private Map<String, String> xianMap = new HashMap<>();
	private Map<String, String> cunMap = new HashMap<>();

	public SetAdapter(List<GeodatabaseFeature> list,Context context,MyLayer myLayer) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		this.myLayer = myLayer;
		xianMap = Util.getXianValue(context);
		xiangMap = Util.getXiangValue(context);
		cunMap = Util.getCunValue(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_xiaoban, null);
			//holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.position = position;
		//异步获取县乡村小班号并填充数据
		//new MyAsyncTask().execute(holder);

		String xian = "", xiang = "", cun = "", xbh = "";// 对应 县 乡 村
		String xianD = "", xiangD = "";

		GeodatabaseFeature feature = list.get(position);
		List<Field> lst = feature.getTable().getFields();
		for (Field f : lst) {

			Object obj = feature.getAttributeValue(f);
			CodedValueDomain domain = (CodedValueDomain) f.getDomain();

			if (f.getAlias().contains("县") || f.getAlias().equals("XIAN") || f.getAlias().equals("xian")) {
				if (obj != null) {
					xianD = obj.toString();
					if (domain != null) {
						xian = domain.getCodedValues().get(obj);
					} else {
						xian = Util.getXXCValue(context, xianD, xianD,xianMap);
					}
				}
			}
			if (f.getAlias().contains("乡") || f.getAlias().equals("XIANG") || f.getAlias().equals("xiang")) {
				if (obj != null) {
					xiangD = obj.toString();
					if (domain != null) {
						xiang = domain.getCodedValues().get(obj);
					} else {
						xiang = Util.getXXCValue(context, xiangD,xianD, xiangMap);
					}
				}
			}
			if (f.getAlias().contains("村") || f.getAlias().equals("CUN") || f.getAlias().equals("cun")) {
				if (obj != null) {
					if (domain != null) {
						cun = domain.getCodedValues().get(obj);
					} else {
						if (xiangD.contains(xianD)) {
							cun = Util.getXXCValue(context,obj.toString(), xiangD, cunMap);
						} else {
							cun = Util.getXXCValue(context,obj.toString(), xianD + xiangD,cunMap);
						}
					}
				}
			}
			if (f.getAlias().contains("小班号") || f.getAlias().equals("XBH") || f.getAlias().equals("xbh")) {
				if (obj != null) {
					xbh = obj.toString();
				}
			}
		}

		holder.tv2.setText(xian);
		holder.tv3.setText(xiang);
		holder.tv4.setText(cun);
		holder.tv5.setText(xbh);
		double area = list.get(position).getGeometry().calculateArea2D()*0.0015;
		String mj = decimalFormat.format(area);
		holder.tv6.setText(mj);
		return convertView;
	}

	final class ViewHolder {
		//TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
		TextView tv6;
		int position;
	}

	class MyAsyncTask extends AsyncTask<ViewHolder, Void, Void> {

		String xian = "", xiang = "", cun = "", xbh = "";// 对应 县 乡 村
		String xianD = "", xiangD = "";
		ViewHolder[] view;

		protected Void doInBackground(ViewHolder... holder) {
			view =holder.clone();
			int position = view[0].position;
			GeodatabaseFeature feature = list.get(position);
			List<Field> lst = feature.getTable().getFields();
			for (Field f : lst) {

				Object obj = feature.getAttributeValue(f);
				CodedValueDomain domain = (CodedValueDomain) f.getDomain();

				if (f.getAlias().contains("县")) {
					if (obj != null) {
						xianD = obj.toString();
						if (domain != null) {
							xian = domain.getCodedValues().get(obj);
						} else {
							xian = Util.getXXCValue(context, xianD, xianD,xianMap);
						}
					}
				}
				if (f.getAlias().contains("乡")) {
					if (obj != null) {
						xiangD = obj.toString();
						if (domain != null) {
							xiang = domain.getCodedValues().get(obj);
						} else {
							xiang = Util.getXXCValue(context, xiangD,xianD, xiangMap);
						}
					}
				}
				if (f.getAlias().contains("村")) {
					if (obj != null) {
						if (domain != null) {
							cun = domain.getCodedValues().get(obj);
						} else {
							if (xiangD.contains(xianD)) {
								cun = Util.getXXCValue(context,obj.toString(), xiangD, cunMap);
							} else {
								cun = Util.getXXCValue(context,obj.toString(), xianD + xiangD,cunMap);
							}
						}
					}
				}
				if (f.getAlias().contains("小班号")) {
					if (obj != null) {
						xbh = obj.toString();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			view[0].tv2.setText(xian);
			view[0].tv3.setText(xiang);
			view[0].tv4.setText(cun);
			view[0].tv5.setText(xbh);
		}
	}

	public void getXXCValue(){

	}

	public List<Map<String, Object>> getList(String sql) {

		final List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");
			try {
				Class.forName("jsqlite.JDBCDriver").newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			//String sql = "select * from " + TABLE_NAME;
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("daima", data[0]);
					map.put("name", data[1]);
					searchList.add(map);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return searchList;
	}
}