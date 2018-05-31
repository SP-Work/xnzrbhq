package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by li on 2016/5/26.
 * 属性编辑adapter
 */
public class AttributeAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private List<Field> fieldList = new ArrayList<>();
	private Map<String, Object> selectAttribute;
	private String currentLayerName = "当前图层";
	private Context context;

	private Map<String, String> xiangMap = new HashMap<>();
	private Map<String, String> xianMap = new HashMap<>();
	private Map<String, String> cunMap = new HashMap<>();

	public AttributeAdapter(List<Field> fieldList,Map<String, Object> selectAttribute, Context context, String layer) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.fieldList = fieldList;
		this.selectAttribute = selectAttribute;
		this.currentLayerName = layer;

		xianMap = Util.getXianValue(context);
		xiangMap = Util.getXiangValue(context);
		cunMap = Util.getCunValue(context);
	}

	@Override
	public int getCount() {
		return fieldList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_attributeinfo, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv_key);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String strValue = "";// 对应 县 乡 村
		String xianD = "", xiangD = "";

		Field f = fieldList.get(position);
		Object obj = selectAttribute.get(f.getName());
		CodedValueDomain domain = (CodedValueDomain) f.getDomain();
		if (f.getAlias().contains("县")) {
			if (obj != null) {
				xianD = obj.toString();
				if (domain != null) {
					strValue = domain.getCodedValues().get(obj);
				} else {
					strValue = Util.getXXCValue(context, xianD, xianD, xianMap);
				}
			}
		} else if (f.getAlias().contains("乡")) {
			if (obj != null) {
				xiangD = obj.toString();
				if (domain != null) {
					strValue = domain.getCodedValues().get(obj);
				} else {
					strValue = Util.getXXCValue(context, xiangD, xianD,xiangMap);
				}
			}
		} else if (f.getAlias().contains("村")) {
			if (obj != null) {
				if (domain != null) {
					strValue = domain.getCodedValues().get(obj);
				} else {
					if (xiangD.contains(xianD)) {
						strValue = Util.getXXCValue(context, obj.toString(),
								xiangD, cunMap);
					} else {
						strValue = Util.getXXCValue(context, obj.toString(),
								xianD + xiangD, cunMap);
					}
				}
			}
		}else{
			strValue = obj != null ? obj.toString() : "";
		}
		String txt1 = fieldList.get(position).getAlias()+":";
		holder.tv1.setText(txt1);
		holder.tv2.setText(strValue);

		return convertView;
	}

	final class ViewHolder {
		TextView tv1;
		TextView tv2;
	}

}