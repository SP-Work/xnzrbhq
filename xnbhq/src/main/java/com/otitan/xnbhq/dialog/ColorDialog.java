package com.otitan.xnbhq.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.MyLayer;
import com.titan.baselibrary.color.ColorPickerView;
import com.titan.baselibrary.color.SansumColorSelecter;
import com.titan.baselibrary.listener.CancleListener;

/**
 * Created by li on 2017/6/1.
 * 颜色选择器
 */

public class ColorDialog extends Dialog {

    private Context mContext;
    private int type;
    private TextView view;
    private SeekBar seekBar;
    private MyLayer myLayer;

    public ColorDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ColorDialog(@NonNull Context context, @StyleRes int themeResId, final int type, final TextView view,
                       final SeekBar seekBar, final MyLayer myLayer) {
        super(context, themeResId);
        this.mContext = context;
        this.type = type;
        this.view = view;
        this.seekBar = seekBar;
        this.myLayer = myLayer;
    }

    protected ColorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //展示图层透明度和图层颜色设置的dialog
        setContentView(R.layout.base_color);
        setCanceledOnTouchOutside(true);

        TextView nocolor = (TextView) findViewById(R.id.nocolor);
        nocolor.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                int color = mContext.getResources().getColor(R.color.transparent);
                if (type == 0) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "tianchongse", color).apply();
                } else if (type == 1) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "bianjiese", color).apply();
                }
                view.setBackground(mContext.getResources().getDrawable(R.drawable.touming));
                seekBar.setProgress(100);
                MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "tmd", 100).apply();
                dismiss();
            }
        });

        ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.color_picker);
        colorPickerView.setOnColorChangeListenrer(new ColorPickerView.OnColorChangedListener() {

            @Override
            public void colorChanged(int color) {
                if (type == 0) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "tianchongse", color).apply();
                } else if (type == 1) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "bianjiese", color).apply();
                }
                view.setBackgroundColor(color);
                dismiss();
            }
        });

        SansumColorSelecter select = (SansumColorSelecter) findViewById(R.id.color_seleter);
        select.setColorSelecterLinstener(new SansumColorSelecter.ColorSelecterLinstener() {

            @Override
            public void onColorSeleter(int color) {
                if (type == 0) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "tianchongse", color).apply();
                } else if (type == 1) {
                    MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "bianjiese", color).apply();
                }
                view.setBackgroundColor(color);
                dismiss();
            }
        });

        Button button = (Button) findViewById(R.id.render_btn_sure);
        button.setOnClickListener(new CancleListener(this));

    }
}
