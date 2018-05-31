package com.titan.gzzhjc.dao;

import android.content.Context;
import android.view.View;

import com.titan.gzzhjc.customview.PopupWindowCustom;

/**
 * Created by li on 2017/3/6.
 *
 * 时间选择接口
 */

public interface PopupWindowDao {

    PopupWindowCustom showTimeSel(Context context, View childview, View.OnClickListener onClickListener);
}
