package com.otitan.xnbhq.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.esri.core.geometry.Geometry;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by li on 2016/5/26.
 * 三级列表ExpandableAdapter
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {

	private List<File> groups = new ArrayList<>();
	private BaseActivity activity;
	private List<Map<String, List<File>>> childs = new ArrayList<>();
	private HashMap<String, Boolean> childCheckBox = new HashMap<>();
	

	public ExpandableAdapter(BaseActivity activity, List<File> groups,
			List<Map<String, List<File>>> childs,HashMap<String, Boolean> childCheckBox) {
		this.activity = activity;
		this.groups = groups;
		this.childs = childs;
		this.childCheckBox = childCheckBox;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(groups.get(groupPosition).getName()).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_expandable_child, null);
			holder = new ViewHolder();
			holder.cText = (TextView) convertView.findViewById(R.id.id_child_txt);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_child);
			holder.checkBox.setTag(groupPosition+"="+childPosition);
			holder.imageview_2 = (ImageView) convertView.findViewById(R.id.featurelayer_extent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (childs.get(groupPosition).get(groups.get(groupPosition).getName())
				.get(childPosition).getName().endsWith(".otms")) {
			holder.cText.setText(childs.get(groupPosition)
					.get(groups.get(groupPosition).getName())
					.get(childPosition).getName().replace(".otms", ""));
		}
		if (childs.get(groupPosition).get(groups.get(groupPosition).getName())
				.get(childPosition).getName().endsWith(".geodatabase")) {
			holder.cText.setText(childs.get(groupPosition)
					.get(groups.get(groupPosition).getName())
					.get(childPosition).getName().replace(".geodatabase", ""));
		}
		
		String path = childs.get(groupPosition).get(groups.get(groupPosition).getName()).get(childPosition).getPath();
		boolean flag = childCheckBox.get(path);
		holder.checkBox.setChecked(flag);
		//holder.checkBox.setOnCheckedChangeListener(new MyCbCheckedChangeListener());
		
		holder.imageview_2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String gname = groups.get(groupPosition).getName();
				String cname = childs.get(groupPosition).get(gname).get(childPosition).getName();
				for (int i = 0; i < BaseActivity.layerNameList.size(); i++) {
					String name = BaseActivity.layerNameList.get(i).getPname();
					String name1 = BaseActivity.layerNameList.get(i).getCname();
					final Geometry.Type  type = BaseActivity.layerNameList.get(i).getLayer().getGeometryType();
					if (name.equals(gname) && cname.contains(name1) && type == Geometry.Type.POLYGON) {
						final Geometry geometry = BaseActivity.layerNameList.get(i).getLayer().getFullExtent();
//						Graphic graphic = new Graphic(geometry,new SimpleFillSymbol(Color.RED, SimpleFillSymbol.STYLE.CROSS));
//						activity.graphicsLayer.addGraphic(graphic);
						activity.ZoomToGeom(geometry);
						break;
					}
				}
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		List<File> list = childs.get(groupPosition).get(groups.get(groupPosition).getName());
		if(list != null){
			return list.size();
		}else{
			return 0;
		}

	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupHolder;
		if (null == convertView) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_expandable_group, null);
			groupHolder = new GroupViewHolder();

			groupHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
			groupHolder.cText = (TextView) convertView.findViewById(R.id.id_group_txt);
			groupHolder.delete = (TextView) convertView.findViewById(R.id.group_delete);
			groupHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
			groupHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, groupHolder.swipeLayout.findViewWithTag("Bottom2"));
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupViewHolder) convertView.getTag();
		}

		groupHolder.cText.setText(groups.get(groupPosition).getName());
		//groupHolder.imageview_2.setVisibility(View.GONE);
		groupHolder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delete(groupPosition);
			}
		});
		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class ViewHolder {
		TextView cText;
		CheckBox checkBox;
		ImageView imageview_2;
	}

	private static class GroupViewHolder {
		//ImageView imageview_1;
		SwipeLayout swipeLayout;
		TextView cText;
		TextView delete;
	}

	void delete(int groupPosition){
		File file = groups.get(groupPosition);
		groups.remove(groupPosition);
		int h = childs.size();
		for(int m=0;m<childs.size();m++){
			for(String key : childs.get(m).keySet()){
				if(key.equals(file.getName())){
					childs.remove(m);
					break;
				}
			}
		}
		this.notifyDataSetInvalidated();
		int l = childs.size();

		Log.e("+++++++++",h+" =====   "+l);
	}
	
	class MyCbCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton v, boolean arg1) {
			switch (v.getId()) {
			case R.id.cb_child:
				Object obj = v.getTag();
				String[] str = obj.toString().split("=");
				int gpositon = Integer.parseInt(str[0]);
				int cpositon = Integer.parseInt(str[1]);
				activity.initOtmsChild(groups, childs, gpositon, cpositon);
				v.toggle();
				notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}

}
