package com.otitan.xnbhq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.R;

/**
 * Created by li on 2016/6/2.
 * 图层控制展示图层数据
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private String[] groups;
    private String[][] childs;

    public MyExpandableListAdapter(Context context, String[] groups,String[][] childs) {
        this.mContext = context;
        this.groups = groups;
        this.childs = childs;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childs[groupPosition][childPosition].trim();
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return childs[groupPosition].length;
    }

    public TextView getGenericView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 40);

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        // textView.setPadding(10, 0, 0, 0);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.background_img));
        return textView;
    }

    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        textView.setTextColor(Color.BLUE);
        textView.setBackgroundDrawable(mContext.getResources().getDrawable(
                R.drawable.background_img));
        textView.setBackgroundColor(Color.WHITE);
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
}
