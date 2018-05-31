package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.drawTool.DrawTool;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;

/**
 * Created by li on 2017/6/1.
 * 空间统计
 */

public class SpaceStatisticsDialog extends Dialog {

    private Context mContext;
    private DrawTool drawTool;

    public SpaceStatisticsDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public SpaceStatisticsDialog(@NonNull Context context, @StyleRes int themeResId,DrawTool drawTool) {
        super(context, themeResId);
        this.mContext = context;
        this.drawTool = drawTool;
    }

    protected SpaceStatisticsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_shara_kjtj);

        ImageView closeview = (ImageView) findViewById(R.id.kongjianyangshi_close);

        View yuanview = (View) findViewById(R.id.kongjian_yuan);
        yuanview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawTool.activate(DrawTool.CIRCLE);
                ToastUtil.setToast(mContext, "你选择了圆斑勾绘,可滑动离开屏幕结束勾绘");
                dismiss();
            }
        });

        View juxing = (View) findViewById(R.id.kongjian_juxing);
        juxing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawTool.activate(DrawTool.ENVELOPE);
                ToastUtil.setToast(mContext, "你选择了矩形勾绘,可滑动离开屏幕结束勾绘");
                dismiss();
            }
        });

        View duobianxing = (View) findViewById(R.id.kongjian_duobianxing);
        duobianxing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawTool.activate(DrawTool.POLYGON);
                ToastUtil.setToast(mContext, "你选择了多边形勾绘,可双击屏幕结束勾绘");
                dismiss();
            }
        });

        closeview.setOnClickListener(new CancleListener(this));
    }
}
