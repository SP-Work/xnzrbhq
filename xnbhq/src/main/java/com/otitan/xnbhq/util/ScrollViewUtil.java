package com.otitan.xnbhq.util;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.otitan.xnbhq.MyApplication;
import com.titan.baselibrary.util.ScreenTool;

/**
 * Created by li on 2017/10/30.
 * 动态设置高度
 */

public class ScrollViewUtil {
    public static void setListViewHeightBasedOnChildren(ListView listView,Context context) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        ScreenTool.Screen screen = MyApplication.screen;
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        int screenheight = 0;
        boolean flag = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (flag) { //竖屏
            screenheight = screen.getHeightPixels();
            if(screenheight*2/3<=height){
                height = screenheight*2/3;
            }
        } else {//横屏
            screenheight = screen.getWidthPixels();
            if(screenheight*4/15<=height){
                height = screenheight*4/15;
            }
        }
        params.height =height;
        listView.setLayoutParams(params);
    }

}
