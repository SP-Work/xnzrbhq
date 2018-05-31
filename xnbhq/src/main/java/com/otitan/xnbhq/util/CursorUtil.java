package com.otitan.xnbhq.util;

import android.text.Selection;
import android.text.Spannable;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by li on 2017/6/1.
 * 光标位置工具类
 */

public class CursorUtil {

    /**
     * 把edittext的光标放置在最后
     */
    public static void setEditTextLocation(EditText et) {
        CharSequence txt = et.getText();
        if (txt instanceof Spannable) {
            Spannable spanText = (Spannable) txt;
            Selection.setSelection(spanText, txt.length());
        }
    }

    /**
     * 把TextView的光标放置在最后
     */
    public static void setTextViewLocation(TextView et) {
        CharSequence txt = et.getText();
        if (txt instanceof Spannable) {
            Spannable spanText = (Spannable) txt;
            Selection.setSelection(spanText, txt.length());
        }
    }

}
