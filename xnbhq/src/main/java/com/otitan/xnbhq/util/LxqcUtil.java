package com.otitan.xnbhq.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

import com.otitan.xnbhq.adapter.LxqcYdyzAdapter;
import com.otitan.xnbhq.dialog.HzbjDialog;
import com.otitan.xnbhq.dialog.ShuziDialog;
import com.otitan.xnbhq.dialog.XzzyDialog;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.service.PullParseXml;

/**
 * 连续清查工具类
 */
public class LxqcUtil {
    //static String LXQC="/连续清查";
    static String FILENAME = "/config.xml";

    /**
     * 获取资源type值
     * attributeName资源tablename
     */
    public static String getAttributetype(Context ctx, String attributeName, String path) {
        //String str = ResourcesManager.otms + LXQC+FILENAME;
        //String path =ResourcesManager.getFilePath(str);
        path = new File(path).getParent() + FILENAME;
        String type = "";
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            PullParseXml parseXml = new PullParseXml();
            type = parseXml.PullParseXMLforFeildType(inputStream, attributeName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 获取资源qzlx值 1为汉字输入 2为数字输入 3为选项输入
     */
    public static String getAttributeQztype(Context ctx, String attributeName, String path) {
        //String str = ResourcesManager.otms + LXQC+FILENAME;
        //String path =ResourcesManager.getFilePath(str);
        path = new File(path).getParent() + FILENAME;
        String type = "";
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            PullParseXml parseXml = new PullParseXml();
            type = parseXml.PullParseXMLforQzlxType(inputStream, attributeName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 获取资源xiaoshu值 控制数字输入小数点后几位
     */
    public static String getAttributeXstype(Context ctx, String attributeName, String path) {
        //String str = ResourcesManager.otms + LXQC+FILENAME;
        //String path =ResourcesManager.getFilePath(str);
        path = new File(path).getParent() + FILENAME;
        String type = "";
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            PullParseXml parseXml = new PullParseXml();
            type = parseXml.PullParseXMLforXslxType(inputStream, attributeName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 获取资源列表
     * attributeName资源tablename
     */
    public static List<Row> getAttributeList(Context ctx, String attributeName, String path) {
        //String str = ResourcesManager.otms + LXQC+FILENAME;
        //String path =ResourcesManager.getFilePath(str);
        path = new File(path).getParent() + FILENAME;
        List<Row> list = null;
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            PullParseXml parseXml = new PullParseXml();
            list = parseXml.PullParseXML(inputStream, attributeName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取资源小班号字段
     * attributeName资源tablename
     */
    public static String getAttributeXbh(Context ctx, String tableName, String idname, String dbpath) {
        //String str = ResourcesManager.otms + LXQC+FILENAME;
        //String path =ResourcesManager.getFilePath(str);
        dbpath = new File(dbpath).getParent() + FILENAME;
        String xbh = "";
        try {
            File file = new File(dbpath);
            FileInputStream inputStream = new FileInputStream(file);
            PullParseXml parseXml = new PullParseXml();
            xbh = parseXml.PullParseXbhXML(inputStream, tableName, idname);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return xbh;
    }

    /**
     * 设置强制填写的提示框
     * context 上下文
     * theme 主题
     * tv 要赋值的textview
     * map
     * zd 字段名
     * sp 偏好
     * type 为1为文字输入 2时为数字输入
     * inputtype数字输入类型 为0为小数1为整数
     * xiaoshu 1为小数点后1为2为小数点后两位
     */
    public static void showAlertDialog(final Context context, final String theme, final TextView tv, final HashMap<String, String> map, final String zd, final String inputtype, final String xiaoshu, final String type) {
        final Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("强制填写").setMessage("当前字段已设置为不可填写，是否强制填写？")
                .setPositiveButton("是", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if ("1".equals(type)) {
                            HzbjDialog hzdialog = new HzbjDialog(context, theme, tv, map, zd);
                            BussUtil.setDialogParams(context, hzdialog, 0.5, 0.5);
                        } else if ("2".equals(type)) {
                            ShuziDialog shuzidialog = new ShuziDialog(context, theme, tv, map, zd, null, null, inputtype, xiaoshu, "");
                            BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
                        }
                    }
                }).setNegativeButton("否", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).create();
        dialog.show();
    }

    /**
     * 连续清查样地因子调查不可修改项
     * context
     * theme
     * tv
     * position
     * lines
     * adapter
     * input_history
     * type 1为汉字输入 2为数字输入 3为选项输入
     */
    public static void showTcAlertDialog(final Context context, final TextView tv, final Line line, final LxqcYdyzAdapter adapter, final String name, final String type, final String path) {
        final Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("强制填写").setMessage("当前字段已设置为不可填写，是否强制填写？")
                .setPositiveButton("是", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if ("1".equals(type)) {
                            HzbjDialog wbdialog = new HzbjDialog(context, tv, line, adapter);
                            BussUtil.setDialogParams(context, wbdialog, 0.5, 0.5);
                        } else if ("2".equals(type)) {
//					String numtemp = LxqcUtil.getAttributeXstype(context, "slzylxqc.xml", name);
                            String numtemp = LxqcUtil.getAttributeXstype(context, name, path);
                            if (numtemp.equals("0")) {
                                ShuziDialog szdialog = new ShuziDialog(context, tv, line, "1", "", adapter);
                                BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
                            } else if (numtemp.equals("1")) {
                                ShuziDialog szdialog = new ShuziDialog(context, tv, line, "0", "1", adapter);
                                BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
                            } else if (numtemp.equals("2")) {
                                ShuziDialog szdialog = new ShuziDialog(context, tv, line, "0", "2", adapter);
                                BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
                            } else if (numtemp.equals("-1")) {
                                ShuziDialog szdialog = new ShuziDialog(context, tv, line, "", "", adapter);
                                BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
                            }
                        } else if ("3".equals(type)) {
                            List<Row> list = LxqcUtil.getAttributeList(context, name, path);
                            XzzyDialog xzdialog = new XzzyDialog(context, list, tv, line, adapter);
                            BussUtil.setDialogParams(context, xzdialog, 0.5, 0.5);
                        }
                    }
                }).setNegativeButton("否", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).create();
        dialog.show();
    }
}
