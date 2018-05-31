package com.titan.gzzhjc.impl;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.titan.gzzhjc.customview.PopupWindowCustom;
import com.titan.gzzhjc.dao.PopupWindowDao;

/**
 * Created by li on 2017/3/6.
 *
 * 时间选择弹窗
 */

public class PopupWindowImpl implements PopupWindowDao {

    String selValue = "2015";
    @Override
    public PopupWindowCustom showTimeSel(Context context,View childview,View.OnClickListener onClickListener) {
        PopupWindowCustom pop = new PopupWindowCustom(context, onClickListener);
        pop.showAtLocation(childview, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        return pop;
    }

}
