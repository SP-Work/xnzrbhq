package com.otitan.xnbhq.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.SwdyxActivity;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 生物多样性 救护站信息管理Adapter
 */
public class SwdyxJhzAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private GraphicsLayer graphicsLayer;
    private MapView mapView;
    private SwdyxActivity activity;

    public SwdyxJhzAdapter(Context context, MapView mapView, GraphicsLayer graphicsLayer, List<HashMap<String, String>> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.graphicsLayer = graphicsLayer;
        this.mapView = mapView;
        this.activity = (SwdyxActivity) context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_swdyx_jhz, null);
            holder.cb = (CheckBox) convertView
                    .findViewById(R.id.swdyx_jhzgl_cb);
            holder.tv1 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv1);
            holder.tv2 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv2);
            holder.tv3 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv3);
            holder.tv4 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv4);
            holder.tv5 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv5);
            holder.tv6 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv6);
            holder.tv7 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv7);
            holder.tv8 = (TextView) convertView
                    .findViewById(R.id.swdyx_jhzgl_tv8);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (BussUtil.isEmperty(list.get(position).get("ID").trim())) {
                    HashMap<String, String> map = list.get(position);
                    map.put(list.get(position).get("ID"), arg1 + "");
                    notifyDataSetChanged();
                }
            }
        });
        holder.cb.setChecked(Boolean.parseBoolean(list.get(position).get(
                list.get(position).get("ID"))));

        holder.tv1.setText((position + 1) + "");
        if (BussUtil.isEmperty(list.get(position).get("AIDNAME").trim())) {
            holder.tv2.setText(list.get(position).get("AIDNAME").trim());
        } else {
            holder.tv2.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("MANAGER").trim())) {
            holder.tv3.setText(list.get(position).get("MANAGER").trim());
        } else {
            holder.tv3.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("PHONE").trim())) {
            holder.tv4.setText(list.get(position).get("PHONE").trim());
        } else {
            holder.tv4.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("ADDRESS").trim())) {
            holder.tv5.setText(list.get(position).get("ADDRESS").trim());
        } else {
            holder.tv5.setText("");
        }
        holder.tv6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCheckdialog(list.get(position));
            }
        });
        holder.tv7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showEditdialog(list.get(position));
            }
        });
        holder.tv8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!BussUtil.isEmperty(list.get(position).get("LNG"))
                        || !BussUtil.isEmperty(list.get(position).get(
                        "LAT"))) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.longorlannull));

                    return;
                }
                double lon = Double.parseDouble(list.get(position).get(
                        "LNG"));
                double lat = Double.parseDouble(list.get(position).get(
                        "LAT"));
                Point point = new Point(lon, lat);
                Graphic graphic = new Graphic(point, new SimpleMarkerSymbol(
                        Color.RED, 10,
                        com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE));
                graphicsLayer.addGraphic(graphic);
                mapView.setExtent(point);
                ToastUtil.setToast(
                        context,
                        context.getResources().getString(
                                R.string.locationsuccess));
            }
        });
        return convertView;
    }

    /* 编辑 */
    protected void showEditdialog(final HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_jhzgl_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText swdyx_jhz_jhzm = (EditText) dialog.findViewById(R.id.swdyx_jhz_jhzm);
        final EditText swdyx_jhz_glry = (EditText) dialog.findViewById(R.id.swdyx_jhz_glry);
        final Spinner swdyx_jhz_qywz = (Spinner) dialog.findViewById(R.id.swdyx_jhz_qywz);
        List<String> list = new ArrayList<String>();
        list.add("--请选择区域位置--");
        for (int i = 0; i < activity.xzqyLlist.size(); i++) {
            list.add(activity.xzqyLlist.get(i).get("DUNAME"));
        }
        ArrayAdapter<String> xzqyadapter = new ArrayAdapter<String>(context, R.layout.myspinner, list);
        swdyx_jhz_qywz.setAdapter(xzqyadapter);
        final EditText swdyx_jhz_xxdz = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_xxdz);
        final EditText swdyx_jhz_wd = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_wd);
        final EditText swdyx_jhz_jd = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_jd);
        final EditText swdyx_jhz_lxdh = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_lxdh);
        final EditText swdyx_jhz_jztj = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_jztj);
        final EditText swdyx_jhz_bz = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_bz);
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_jhz_cancle);
        Button save = (Button) dialog.findViewById(R.id.swdyx_jhz_save);
        if (BussUtil.isEmperty(map.get("AIDNAME").trim())) {
            swdyx_jhz_jhzm.setText(map.get("AIDNAME").trim());
        } else {
            swdyx_jhz_jhzm.setText("");
        }
        if (BussUtil.isEmperty(map.get("MANAGER").trim())) {
            swdyx_jhz_glry.setText(map.get("MANAGER").trim());
        } else {
            swdyx_jhz_glry.setText("");
        }
        if (BussUtil.isEmperty(map.get("AREACODE").trim())) {
            int a = Integer.parseInt(map.get("AREACODE").trim());
            if (a >= 0 && list.size() > a) {
                swdyx_jhz_qywz.setSelection(a);
            }
        }
        if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
            swdyx_jhz_xxdz.setText(map.get("ADDRESS").trim());
        } else {
            swdyx_jhz_xxdz.setText("");
        }
        if (BussUtil.isEmperty(map.get("LNG").trim())) {
            swdyx_jhz_wd.setText(map.get("LNG").trim());
        } else {
            swdyx_jhz_wd.setText("");
        }
        if (BussUtil.isEmperty(map.get("LAT").trim())) {
            swdyx_jhz_jd.setText(map.get("LAT").trim());
        } else {
            swdyx_jhz_jd.setText("");
        }
        if (BussUtil.isEmperty(map.get("PHONE").trim())) {
            swdyx_jhz_lxdh.setText(map.get("PHONE").trim());
        } else {
            swdyx_jhz_lxdh.setText("");
        }
        if (BussUtil.isEmperty(map.get("CONDITION").trim())) {
            swdyx_jhz_jztj.setText(map.get("CONDITION").trim());
        } else {
            swdyx_jhz_jztj.setText("");
        }
        if (BussUtil.isEmperty(map.get("REMARK").trim())) {
            swdyx_jhz_bz.setText(map.get("REMARK").trim());
        } else {
            swdyx_jhz_bz.setText("");
        }
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!BussUtil.isEmperty(swdyx_jhz_jhzm.getText().toString())) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jhzmnotnull));
                    return;
                }
                if (!BussUtil.isEmperty(swdyx_jhz_glry.getText().toString())) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.glrynotnull));
                    return;
                }
                if (!BussUtil.isEmperty(swdyx_jhz_wd.getText().toString())) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                if (!BussUtil.isEmperty(swdyx_jhz_jd.getText().toString())) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String id = "";
                if (BussUtil.isEmperty(map.get("ID").trim())) {
                    id = map.get("ID").trim();
                }
                String jhzm = swdyx_jhz_jhzm.getText().toString().trim();
                String glry = swdyx_jhz_glry.getText().toString().trim();
                String qywz = swdyx_jhz_qywz.getSelectedItemPosition() + "";
                String xxdz = swdyx_jhz_xxdz.getText().toString().trim();
                String wd = swdyx_jhz_wd.getText().toString().trim();
                String jd = swdyx_jhz_jd.getText().toString().trim();
                String lxdh = swdyx_jhz_lxdh.getText().toString().trim();
                String jztj = swdyx_jhz_jztj.getText().toString().trim();
                String bz = swdyx_jhz_bz.getText().toString().trim();
                Webservice web = new Webservice(context);
                String result = web.updateSwdyxJhzData(id, jhzm, glry, qywz,
                        xxdz, wd, jd, lxdh, jztj, bz);
                if ("true".equals(result)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editsuccess));
                    map.put("AIDNAME", jhzm);
                    map.put("MANAGER", glry);
                    map.put("AREACODE", qywz);
                    map.put("ADDRESS", xxdz);
                    map.put("LNG", wd);
                    map.put("LAT", jd);
                    map.put("PHONE", lxdh);
                    map.put("CONDITION", jztj);
                    map.put("REMARK", bz);
                    notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editfailed));
                }
            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogGravity_Center(context, dialog, 0.85, 0.9);
    }

    /* 查看 */
    protected void showCheckdialog(HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_jhzgl_check);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView jhzm = (TextView) dialog.findViewById(R.id.swdyx_jhz_jhzm);
        TextView glry = (TextView) dialog.findViewById(R.id.swdyx_jhz_glry);
        TextView qywz = (TextView) dialog.findViewById(R.id.swdyx_jhz_qywz);
        TextView xxdz = (TextView) dialog.findViewById(R.id.swdyx_jhz_xxdz);
        TextView wd = (TextView) dialog.findViewById(R.id.swdyx_jhz_wd);
        TextView jd = (TextView) dialog.findViewById(R.id.swdyx_jhz_jd);
        TextView lxdh = (TextView) dialog.findViewById(R.id.swdyx_jhz_lxdh);
        TextView jztj = (TextView) dialog.findViewById(R.id.swdyx_jhz_jztj);
        TextView bz = (TextView) dialog.findViewById(R.id.swdyx_jhz_bz);
        Button back = (Button) dialog.findViewById(R.id.swdyx_jhz_back);
        if (BussUtil.isEmperty(map.get("AIDNAME").trim())) {
            jhzm.setText(map.get("AIDNAME").trim());
        } else {
            jhzm.setText("");
        }
        if (BussUtil.isEmperty(map.get("MANAGER").trim())) {
            glry.setText(map.get("MANAGER").trim());
        } else {
            glry.setText("");
        }
        if (BussUtil.isEmperty(map.get("AREACODE").trim())) {
            int a = Integer.parseInt(map.get("AREACODE").trim());
            String qywztx = "";
            if (a > 0 && activity.xzqyLlist.size() > a) {
                qywztx = activity.xzqyLlist.get(a - 1).get("DUNAME");
            }
            qywz.setText(qywztx);
        } else {
            qywz.setText("");
        }
        if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
            xxdz.setText(map.get("ADDRESS").trim());
        } else {
            xxdz.setText("");
        }
        if (BussUtil.isEmperty(map.get("LNG").trim())) {
            wd.setText(map.get("LNG").trim());
        } else {
            wd.setText("");
        }
        if (BussUtil.isEmperty(map.get("LAT").trim())) {
            jd.setText(map.get("LAT").trim());
        } else {
            jd.setText("");
        }
        if (BussUtil.isEmperty(map.get("PHONE").trim())) {
            lxdh.setText(map.get("PHONE").trim());
        } else {
            lxdh.setText("");
        }
        if (BussUtil.isEmperty(map.get("CONDITION").trim())) {
            jztj.setText(map.get("CONDITION").trim());
        } else {
            jztj.setText("");
        }
        if (BussUtil.isEmperty(map.get("REMARK").trim())) {
            bz.setText(map.get("REMARK").trim());
        } else {
            bz.setText("");
        }
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogGravity_Center(context, dialog, 0.85, 0.9);
    }

    public final class ViewHolder {
        public CheckBox cb;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
        public TextView tv5;
        public TextView tv6;
        public TextView tv7;
        public TextView tv8;
    }


}
