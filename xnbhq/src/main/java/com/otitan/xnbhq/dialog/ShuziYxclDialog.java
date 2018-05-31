package com.otitan.xnbhq.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.ToastUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 数字输入
 */
public class ShuziYxclDialog extends Dialog {
    Context context;
    String name;
    private TextView tv;
    HashMap<String, String> map;
    String zd;//当前编辑的项字段
    private String qxjzd;
    private String xjzd;
    private String spjzd;
    private String chushizhi;
    private String shuzhi = "";
    private String inputtype = "";
    private List<HashMap<String, String>> list = new ArrayList<>();
    private String xiaoshu;
    private TextView qxj;
    TextView xj;
    private TextView spj;
    private String category;
    private TextView ycjl;
    private TextView sywc;
    private String zjyxtype;

    // inputtype为"0"时输入为小数 ，为"1"时为整数
    // xiaoshu为小数精切到小数点后第几位 “1”为1位 "2"为2位
    // category为"1"时为方位角进入，为"2"时为倾斜角进入，为"3"时为斜距进入，为"4"时为水平距进入
    //zjyxtype引线进入为1 周界进入为2
    public ShuziYxclDialog(Context context, String name,
                           TextView tv, HashMap<String, String> map, String zd, String qxjzd, String xjzd, String spjzd, List<HashMap<String, String>> list,
                           String inputtype, String xiaoshu, TextView qxj, TextView xj,
                           TextView spj, String category, TextView ycjl, TextView sywc, String zjyxtype) {
        super(context, R.style.Dialog);
        this.context = context;
        this.name = name;
        this.tv = tv;
        this.map = map;
        this.zd = zd;
        this.qxjzd = qxjzd;
        this.xjzd = xjzd;
        this.spjzd = spjzd;
        this.inputtype = inputtype;
        this.list = list;
        this.xiaoshu = xiaoshu;
        this.qxj = qxj;
        this.xj = xj;
        this.spj = spj;
        this.category = category;
        this.ycjl = ycjl;
        this.sywc = sywc;
        this.zjyxtype = zjyxtype;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shuzi);
        setCanceledOnTouchOutside(false);
        if (tv != null) {
            chushizhi = tv.getText().toString().trim();
        }
        TextView headtext = (TextView) findViewById(R.id.headtext);
        Button back = (Button) findViewById(R.id.back);
        Button wancheng = (Button) findViewById(R.id.wancheng);
        final EditText content = (EditText) findViewById(R.id.content);
        final TextView sz1 = (TextView) findViewById(R.id.sz1);
        final TextView sz2 = (TextView) findViewById(R.id.sz2);
        final TextView sz3 = (TextView) findViewById(R.id.sz3);
        final TextView sz4 = (TextView) findViewById(R.id.sz4);
        final TextView sz5 = (TextView) findViewById(R.id.sz5);
        final TextView sz6 = (TextView) findViewById(R.id.sz6);
        final TextView sz7 = (TextView) findViewById(R.id.sz7);
        final TextView sz8 = (TextView) findViewById(R.id.sz8);
        final TextView sz9 = (TextView) findViewById(R.id.sz9);
        final TextView sz0 = (TextView) findViewById(R.id.sz0);

        final TextView szdian = (TextView) findViewById(R.id.szdian);
        final TextView szjia = (TextView) findViewById(R.id.szjia);
        final TextView szx = (TextView) findViewById(R.id.szx);
        final TextView szqk = (TextView) findViewById(R.id.szqk);
        headtext.setText(name);
        content.setText(chushizhi);
        shuzhi = "";
        content.setSelection(chushizhi.length());

        sz1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "1";
                } else {
                    shuzhi = shuzhi + "1";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "2";
                } else {
                    shuzhi = shuzhi + "2";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "3";
                } else {
                    shuzhi = shuzhi + "3";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "4";
                } else {
                    shuzhi = shuzhi + "4";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "5";
                } else {
                    shuzhi = shuzhi + "5";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "6";
                } else {
                    shuzhi = shuzhi + "6";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "7";
                } else {
                    shuzhi = shuzhi + "7";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "8";
                } else {
                    shuzhi = shuzhi + "8";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    shuzhi = "9";
                } else {
                    shuzhi = shuzhi + "9";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        sz0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!shuzhi.equals("") && shuzhi.contains(".")) {
                    String[] split = shuzhi.split("\\.", -1);
                    if (split.length == 2) {
                        if ((xiaoshu.equals("1") && split[1].length() >= 1)
                                || (xiaoshu.equals("2") && split[1].length() >= 2)) {
                            return;
                        }
                    }
                }
                if (shuzhi.equals("0")) {
                    return;
                } else if (shuzhi.equals("")) {
                    shuzhi = shuzhi + "0";
                } else {
                    if (shuzhi.startsWith("0") && !shuzhi.contains(".")) {

                    } else {
                        shuzhi = shuzhi + "0";

                    }
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        szdian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (shuzhi.equals("")) {
                    return;
                }
                if (!shuzhi.contains(".")) {
                    shuzhi = shuzhi + ".";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        szjia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (shuzhi.contains("-")) {
                    shuzhi = shuzhi.replaceAll("-", "");
                } else {
                    shuzhi = "-" + shuzhi;
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        szx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (shuzhi.length() > 1) {
                    shuzhi = shuzhi.substring(0, shuzhi.length() - 1);
                } else {
                    shuzhi = "";
                }
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });
        szqk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                shuzhi = "";
                content.setText(shuzhi);
                content.setSelection(shuzhi.length());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        //完成
        wancheng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String contenttx = content.getText().toString().trim();
                //输入的是倾斜角
                if ("2".equals(category) && !"".equals(contenttx)) {
                    Double a = Double.parseDouble(contenttx);
                    if (a > 90 || a < 0) {
                        ToastUtil.setToast(context, context.getResources().getString(R.string.qxjtxfw));
                        return;
                    }
                    //输入倾斜角，斜距不为空时，计算水平距
                    if (!"".equals(xj.getText().toString())) {
                        if (a == 90) {
                            spj.setText("0");
                            map.put(spjzd, "0");
                        } else {
                            double b = (Math.cos(a * Math.PI / 180) * Double.parseDouble(xj.getText().toString()));
                            if ((b + "").contains(".")) {
                                String[] split = (b + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        b = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
                                    }
                                }
                            }
                            spj.setText(b + "");
                            map.put(spjzd, b + "");
                        }
                        //输入倾斜角，斜距为空，水平距不为空时，计算斜距
                    } else if (!"".equals(spj.getText().toString())) {
                        if (a == 90) {
                            spj.setText("0");
                            map.put(spjzd, "0");
                        } else {
                            double b = (Double.parseDouble(spj.getText().toString()) / Math.cos(a * Math.PI / 180));
                            if ((b + "").contains(".")) {
                                String[] split = (b + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        b = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
                                    }
                                }
                            }
                            xj.setText(b + "");
                            map.put(xjzd, b + "");
                        }
                    }
                    //输入的是斜距
                } else if ("3".equals(category) && !"".equals(contenttx)) {
                    if (!"".equals(qxj.getText().toString())) {
                        double a = Double.parseDouble(qxj.getText().toString());
                        double b = Math.cos(a * Math.PI / 180) * (Double.parseDouble(contenttx));
                        if ((b + "").contains(".")) {
                            String[] split = (b + "").split("\\.", -1);
                            if (split.length == 2) {
                                if (split[1].length() > 2) {
                                    b = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
                                }
                            }
                        }
                        spj.setText(b + "");
                        map.put(spjzd, b + "");
                    }
                    //输入的为水平距
                } else if ("4".equals(category) && !"".equals(contenttx)) {
                    if (!"".equals(qxj.getText().toString())) {
                        double a = Double.parseDouble(qxj.getText().toString());
                        if (a != 90) {
                            double b = (Double.parseDouble(contenttx) / Math.cos(a * Math.PI / 180));
                            if ((b + "").contains(".")) {
                                String[] split = (b + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        b = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
                                    }
                                }
                            }
                            xj.setText(b + "");
                            map.put(xjzd, b + "");
                        } else {
                            contenttx = "0";
                            ToastUtil.setToast(context, context.getResources().getString(R.string.qxjtweijiushi));
                        }
                    }
                }
                if ("".equals(contenttx)) {
                    contenttx = "0";
                }
                if (contenttx.length() > 1 && contenttx.endsWith(".")) {
                    contenttx = contenttx.substring(0, shuzhi.length() - 1);
                }
                if (contenttx.contains(".")) {
                    float result = Float.parseFloat(contenttx);
                    if (tv != null) {
                        tv.setText(result + "");
                    }
                    if (map != null) {
                        map.put(zd, result + "");
                    }
                } else {
                    int result = Integer.parseInt(contenttx);
                    if (tv != null) {
                        tv.setText(result + "");
                    }
                    if (map != null) {
                        map.put(zd, result + "");
                    }
                }
                //刷新listview数据
                if ("1".equals(zjyxtype)) {
                    SlzylxqcYxcljlDialog.refreshdata();
                } else if ("2".equals(zjyxtype)) {
                    SlzylxqcZjcljlDialog.refreshdata();
                }
                //引线测量数据处理
                if ("1".equals(zjyxtype)) {
                    // 已测距离和剩余距离处理
                    List<Double> sywclist = new ArrayList<Double>();
                    String ycjltx = "";
                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, String> map = list.get(i);
                        if (!"".equals(map.get("FWJ"))
                                && !"".equals(map.get("SPJ"))) {
                            double a = Double.parseDouble(map.get("FWJ"));
                            double b = Double.parseDouble(map.get("SPJ"));
                            double c = Math.cos(a * Math.PI / 180) * b;
                            if ((c + "").contains(".")) {
                                String[] split = (c + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        c = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            sywclist.add(c);
                            ycjltx = ycjltx + "(" + c + ")";
                        } else if ("".equals(map.get("FWJ"))
                                && !"".equals(map.get("SPJ"))) {
                            double b = Double.parseDouble(map.get("SPJ"));
                            if ((b + "").contains(".")) {
                                String[] split = (b + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        b = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            sywclist.add(b);
                            ycjltx = ycjltx + "(" + b + ")";
                        } else {
                            ycjltx = ycjltx + "(" + 0 + ")";
                        }
                    }
                    if (ycjl != null) {
                        ycjl.setText(ycjltx);
                    }
                    double a = 0;
                    for (int i = 0; i < sywclist.size(); i++) {
                        a = a + sywclist.get(i);
                    }
                    if ((a + "").contains(".")) {
                        String[] split = (a + "").split("\\.", -1);
                        if (split.length == 2) {
                            if (split[1].length() > 2) {
                                a = Double.parseDouble(split[0] + "."
                                        + split[1].substring(0, 2));
                            }
                        }
                    }
                    if (sywc != null) {
                        sywc.setText("(" + a + ")");
                    }
                    //周界测量数据处理
                } else if ("2".equals(zjyxtype)) {
                    // 已测距离和剩余距离处理
                    List<Double> sywclist = new ArrayList<Double>();
                    List<Double> syyclist = new ArrayList<Double>();
                    sywclist.add(25.82);
                    sywclist.add(25.82);
                    sywclist.add(25.82);
                    sywclist.add(25.82);
                    String wcjltx = "";
                    String ycjltx = "";
                    double wc = 25.82;
                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, String> map = list.get(i);
                        double a = 0;
                        double b = 0;
                        if (!"".equals(map.get("FWJ")) && !"".equals(map.get("SPJ"))) {
                            a = Double.parseDouble(map.get("FWJ"));
                            b = Double.parseDouble(map.get("SPJ"));
                        } else if ("".equals(map.get("FWJ")) && !"".equals(map.get("SPJ"))) {
                            a = 0;
                            b = Double.parseDouble(map.get("SPJ"));
                        } else if (!"".equals(map.get("FWJ")) && "".equals(map.get("SPJ"))) {
                            a = Double.parseDouble(map.get("FWJ"));
                            b = 0;
                        } else if ("".equals(map.get("FWJ")) && "".equals(map.get("SPJ"))) {
                            a = 0;
                            b = 0;
                        }
                        double c = 0;
                        if (sywclist.size() == 4) {
                            c = Math.cos(a * Math.PI / 180) * b;
                            if ((c + "").contains(".")) {
                                String[] split = (c + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        c = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            syyclist.add(c);
                            if (c > 0) {
                                wc = wc - c;
                            }
                            if ((wc + "").contains(".")) {
                                String[] split = (wc + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        wc = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            if (wc > 0) {
                                wcjltx = "(" + wc + ")(25.82)(25.82)(25.82)";
                            } else {
                                wc = 25.82;
                                wcjltx = "(25.82)(25.82)(25.82)";
                                sywclist.remove(sywclist.get(3));
                            }
                        } else if (sywclist.size() == 3) {
                            c = Math.sin(a * Math.PI / 180) * b;
                            if ((c + "").contains(".")) {
                                String[] split = (c + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        c = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            syyclist.add(c);
                            if (c > 0) {
                                wc = wc - c;
                            }
                            if ((wc + "").contains(".")) {
                                String[] split = (wc + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        wc = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            if (wc > 0) {
                                wcjltx = "(" + wc + ")(25.82)(25.82)";
                            } else {
                                wc = 25.82;
                                wcjltx = "(25.82)(25.82)";
                                sywclist.remove(sywclist.get(2));
                            }
                        } else if (sywclist.size() == 2) {
                            c = -Math.cos(a * Math.PI / 180) * b;
                            if ((c + "").contains(".")) {
                                String[] split = (c + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        c = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            syyclist.add(c);
                            if (c > 0) {
                                wc = wc - c;
                            }
                            if ((wc + "").contains(".")) {
                                String[] split = (wc + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        wc = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            if (wc > 0) {
                                wcjltx = "(" + wc + ")(25.82)";
                            } else {
                                wc = 25.82;
                                wcjltx = "(25.82)";
                                sywclist.remove(sywclist.get(1));
                            }
                        } else if (sywclist.size() == 1) {
                            c = -Math.sin(a * Math.PI / 180) * b;
                            if ((c + "").contains(".")) {
                                String[] split = (c + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        c = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            syyclist.add(c);
                            if (c > 0) {
                                wc = wc - c;
                            }
                            if ((wc + "").contains(".")) {
                                String[] split = (wc + "").split("\\.", -1);
                                if (split.length == 2) {
                                    if (split[1].length() > 2) {
                                        wc = Double.parseDouble(split[0] + "."
                                                + split[1].substring(0, 2));
                                    }
                                }
                            }
                            if (wc > 0) {
                                wcjltx = "(" + wc + ")";
                            } else {
                                wcjltx = "测量完毕";
                                sywclist.remove(sywclist.get(0));
                            }
                        }

                    }
                    for (int i = 0; i < syyclist.size(); i++) {
                        ycjltx = ycjltx + "(" + syyclist.get(i) + ")";
                    }
                    if (ycjl != null) {
                        ycjl.setText(ycjltx);
                    }
                    if (sywc != null) {
                        sywc.setText(wcjltx);
                    }
                }
                dismiss();
            }
        });
        if ("1".equals(inputtype)) {
            szdian.setClickable(false);
            szdian.setBackgroundColor(Color.parseColor("#00688B"));
        }
    }

}
