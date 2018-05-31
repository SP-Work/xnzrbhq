package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.otitan.xnbhq.R;

import java.util.List;

/**
 * Created by li on 2017/6/1.
 *
 * 图斑面积dialog
 */

public class ResultAreaDialog extends Dialog {

    private Context mContext;
    private double mianji = 0;
    private List<GeodatabaseFeature> list;

    public ResultAreaDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ResultAreaDialog(@NonNull Context context, @StyleRes int themeResId,List<GeodatabaseFeature> list) {
        super(context, themeResId);
        this.mContext = context;
        this.list = list;
    }

    protected ResultAreaDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_duoban_mj);
        for (GeodatabaseFeature geo : list) {
            mianji = mianji + geo.getGeometry().calculateArea2D();
        }
        TextView tvpfj = (TextView) findViewById(R.id.pfmmj);
        tvpfj.setText(mianji + "    平方米");

        TextView tvmu = (TextView) findViewById(R.id.mumj);
        tvmu.setText((mianji * 0.0015) + "    亩");
    }
}
