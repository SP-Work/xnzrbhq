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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
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
 * 生物多样性 疫源疫病监测站Adapter
 */
public class SwdyxJczAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SwdyxActivity activity;
    private MapView mapView;
    private GraphicsLayer graphicsLayer;

    public SwdyxJczAdapter(Context context, MapView mapView,
                           GraphicsLayer graphicsLayer, List<HashMap<String, String>> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.mapView = mapView;
        this.graphicsLayer = graphicsLayer;
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
            convertView = inflater.inflate(R.layout.item_swdyx_yyybjcz, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
            holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
            holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
            holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (BussUtil.isEmperty(list.get(position).get("OBJECTID").trim())) {
                    HashMap<String, String> map = list.get(position);
                    map.put(list.get(position).get("OBJECTID"), arg1 + "");
                    notifyDataSetChanged();
                }
            }
        });
        holder.cb.setChecked(Boolean.parseBoolean(list.get(position).get(
                list.get(position).get("OBJECTID"))));

        holder.tv1.setText((position + 1) + "");
        if (BussUtil.isEmperty(list.get(position).get("NAME").trim())) {
            holder.tv2.setText(list.get(position).get("NAME").trim());
        } else {
            holder.tv2.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("ADDRESS").trim())) {
            holder.tv3.setText(list.get(position).get("ADDRESS").trim());
        } else {
            holder.tv3.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("FZR").trim())) {
            holder.tv4.setText(list.get(position).get("FZR").trim());
        } else {
            holder.tv4.setText("");
        }
        holder.tv5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCheckDailog(list.get(position));
            }
        });
        holder.tv6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showEditDailog(list.get(position));

            }
        });
        holder.tv7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!BussUtil.isEmperty(list.get(position).get("X"))
                        || !BussUtil.isEmperty(list.get(position).get(
                        "Y"))) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.longorlannull));
                    return;
                }
                double lon = Double.parseDouble(list.get(position).get(
                        "X"));
                double lat = Double.parseDouble(list.get(position).get(
                        "Y"));
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

    /* 查看 */
    protected void showCheckDailog(HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_yyybjcz_check);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView swdyx_jcz_mc = (TextView) dialog.findViewById(R.id.swdyx_jcz_mc);
        TextView swdyx_jcz_fzr = (TextView) dialog.findViewById(R.id.swdyx_jcz_fzr);
        TextView swdyx_jcz_jd = (TextView) dialog.findViewById(R.id.swdyx_jcz_jd);

        TextView swdyx_jcz_wd = (TextView) dialog.findViewById(R.id.swdyx_jcz_wd);

        TextView swdyx_jcz_dz = (TextView) dialog.findViewById(R.id.swdyx_jcz_dz);

        Button back = (Button) dialog.findViewById(R.id.swdyx_jcz_back);
        //监测站名称
        if (BussUtil.isEmperty(map.get("NAME").trim())) {
            swdyx_jcz_mc.setText(map.get("NAME").trim());
        } else {
            swdyx_jcz_mc.setText("");
        }
        // 监测站负责人
        if (BussUtil.isEmperty(map.get("FZR").trim())) {
            swdyx_jcz_fzr.setText(map.get("FZR").trim());
        } else {
            swdyx_jcz_fzr.setText("");
        }
        // 经度
        if (BussUtil.isEmperty(map.get("X").trim())) {
            swdyx_jcz_jd.setText(map.get("X").trim());
        } else {
            swdyx_jcz_jd.setText("");
        }
        // 纬度
        if (BussUtil.isEmperty(map.get("Y").trim())) {
            swdyx_jcz_wd.setText(map.get("Y").trim());
        } else {
            swdyx_jcz_wd.setText("");
        }
        // 地址
        if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
            swdyx_jcz_dz.setText(map.get("ADDRESS").trim());
        } else {
            swdyx_jcz_dz.setText("");
        }

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });
        BussUtil.setDialogGravity_Center(context, dialog, 0.8, 0.90);
    }

    /* 编辑 */
    protected void showEditDailog(final HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_yyybjcz_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView head = (TextView) dialog.findViewById(R.id.swdyx_jcz_head);
        head.setText(context.getResources().getString(R.string.jczedit));

        final EditText swdyx_jcz_mc = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_mc);
        if (BussUtil.isEmperty(map.get("NAME").trim())) {
            swdyx_jcz_mc.setText(map.get("NAME").trim());
        } else {
            swdyx_jcz_mc.setText("");
        }

        final EditText swdyx_jcz_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_fzr);
        if (BussUtil.isEmperty(map.get("FZR").trim())) {
            swdyx_jcz_fzr.setText(map.get("FZR").trim());
        } else {
            swdyx_jcz_fzr.setText("");
        }

        final EditText swdyx_jcz_jd = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_jd);
        if (BussUtil.isEmperty(map.get("X").trim())) {
            swdyx_jcz_jd.setText(map.get("X").trim());
        } else {
            swdyx_jcz_jd.setText("");
        }

        final EditText swdyx_jcz_wd = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_wd);
        if (BussUtil.isEmperty(map.get("Y").trim())) {
            swdyx_jcz_wd.setText(map.get("Y").trim());
        } else {
            swdyx_jcz_wd.setText("");
        }
        final EditText swdyx_jcz_dz = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_dz);
        if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
            swdyx_jcz_dz.setText(map.get("ADDRESS").trim());
        } else {
            swdyx_jcz_dz.setText("");
        }
        Button save = (Button) dialog.findViewById(R.id.swdyx_jcz_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Webservice webservice = new Webservice(context);
                String id = "";
                if (BussUtil.isEmperty(map.get("OBJECTID").trim())) {
                    id = map.get("OBJECTID").trim();
                }
                String mc = swdyx_jcz_mc.getText().toString().trim();
                if (!BussUtil.isEmperty(mc)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jcznamenotnull));
                    return;
                }
                String fzr = swdyx_jcz_fzr.getText().toString().trim();
                if (!BussUtil.isEmperty(fzr)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jczfzrnotnull));
                    return;
                }
                String jd = swdyx_jcz_jd.getText().toString().trim();
                if (!BussUtil.isEmperty(jd)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String wd = swdyx_jcz_wd.getText().toString().trim();
                if (!BussUtil.isEmperty(wd)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                String dz = swdyx_jcz_dz.getText().toString().trim();
                if (!BussUtil.isEmperty(dz)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jczdznotnull));
                    return;
                }
                String result = webservice.updateSwdyxYyybjczData(id, jd, wd, dz, mc, fzr);
                if ("true".equals(result)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editsuccess));
                    map.put("X", jd);
                    map.put("Y", wd);
                    map.put("ADDRESS", dz);
                    map.put("NAME", mc);
                    map.put("FZR", fzr);
                    notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editfailed));
                }
            }
        });

        Button cancle = (Button) dialog
                .findViewById(R.id.swdyx_jcz_cancle);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogGravity_Center(context, dialog, 0.8, 0.90);
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
    }

}
