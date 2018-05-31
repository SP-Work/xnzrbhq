package com.otitan.xnbhq.adapter;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.YHSWActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 有害生物 踏查点Adapter
 */
public class YhswTcdAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private LayoutInflater inflater = null;
    YHSWActivity activity;

    public YhswTcdAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        activity = (YHSWActivity) context;
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
            convertView = inflater.inflate(R.layout.item_yhsw_tcdtc, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
            holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
            holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
            holder.tv7 = (TextView) convertView.findViewById(R.id.tv7);
            holder.tv8 = (TextView) convertView.findViewById(R.id.tv8);
            holder.tv9 = (TextView) convertView.findViewById(R.id.tv9);
            holder.tv10 = (TextView) convertView.findViewById(R.id.tv10);
            holder.tv11 = (TextView) convertView.findViewById(R.id.tv11);
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

        if (BussUtil.isEmperty(list.get(position).get("DCDID"))) {
            holder.tv1.setText(list.get(position).get("DCDID"));
        } else {
            holder.tv1.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("DCLXID"))) {
            holder.tv2.setText(list.get(position).get("DCLXID"));
        } else {
            holder.tv2.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("DCDEPARTMENT"))) {
            holder.tv3.setText(list.get(position).get("DCDEPARTMENT"));
        } else {
            holder.tv3.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("DCPERSON"))) {
            holder.tv4.setText(list.get(position).get("DCPERSON"));
        } else {
            holder.tv4.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("JZNAME"))) {
            holder.tv5.setText(list.get(position).get("JZNAME"));
        } else {
            holder.tv5.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("SBR"))) {
            holder.tv6.setText(list.get(position).get("SBR"));
        } else {
            holder.tv6.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("SBSJ"))) {
            holder.tv7.setText(list.get(position).get("SBSJ"));
        } else {
            holder.tv7.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("UPLOADSTATUS"))) {
            if ("0".equals(list.get(position).get("UPLOADSTATUS"))) {
                holder.tv8.setText(context.getResources().getString(
                        R.string.havenotupload));
                holder.tv10.setVisibility(View.VISIBLE);
                holder.tv11.setVisibility(View.VISIBLE);
            } else if ("1".equals(list.get(position).get("UPLOADSTATUS").trim())) {
                holder.tv8.setText(context.getResources().getString(
                        R.string.haveupload));
                holder.tv10.setVisibility(View.GONE);
                holder.tv11.setVisibility(View.GONE);
            } else {
                holder.tv8.setText("");
                holder.tv10.setVisibility(View.VISIBLE);
                holder.tv11.setVisibility(View.VISIBLE);
            }

        } else {
            holder.tv8.setText("");
        }
        holder.tv9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCkeckDialog(position);
            }
        });
        holder.tv10.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showEditDialog(list.get(position));
            }
        });
        holder.tv11.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ShangBao(list.get(position));
            }
        });
        return convertView;
    }


    /*上报*/
    protected void ShangBao(HashMap<String, String> map) {
        String dcdbh = map.get("DCDID");
        String dclxbh = map.get("DCLXID");
        String dcr = map.get("DCPERSON");
        String dctime = map.get("DCTIME");
        String dcdw = map.get("DCDEPARTMENT");
        String lon = map.get("LON");
        String lat = map.get("LAT");
        String hb = map.get("ALTITUDE");
        String xdm = map.get("TOPONYMY");
        String xbh = map.get("XBH");
        String xbmj = map.get("XBAREA");
        String jzmc = map.get("JZNAME");
        String cbtj = map.get("CBTJ");
        String lfzc = map.get("LFZC");
        String whbw = map.get("WHBW");
        String yhswmc = map.get("YHSWNAME");
        String mcwhcd = map.get("MCWHCD");
        String ct = map.get("CT");
        String mcwhmj = map.get("MCWHMJ");
        String ly = map.get("SOURCE");
        String bz = map.get("REMARK");
        String city = map.get("CITY");
        String county = map.get("COUNTY");
        String town = map.get("TOWN");
        String village = map.get("VILLAGE");
        String sbr = map.get("SBR");
        String sbsj = map.get("SBSJ");
        Webservice web = new Webservice(context);
        String result = web.addYhswTcdData(dcdbh, dclxbh, dcr, dctime, dcdw, lon, lat, hb, xdm, xbh, xbmj, jzmc, cbtj, lfzc, whbw, yhswmc, mcwhcd, ct, mcwhmj, ly, bz, city, county, town, village, "1", sbr, sbsj);
        String[] splits = result.split(",");
        if (splits.length > 0) {
            if ("True".equals(splits[0])) {
                DataBaseHelper.deleteYhswTcdData(context, "db.sqlite", map.get("ID"));
                map.put("UPLOADSTATUS", "1");
                notifyDataSetChanged();
                ToastUtil.setToast(context, context.getResources().getString(R.string.uploadsuccess));
            } else {
                ToastUtil.setToast(context, context.getResources().getString(R.string.uploadfailed));
            }
        } else {
            ToastUtil.setToast(context, context.getResources().getString(R.string.uploadfailed));
        }

    }

    /* 编辑 */
    protected void showEditDialog(final HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.yhsw_yhswtc_add);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        View scztxs = dialog.findViewById(R.id.yhsw_tcd_scztxs);
        scztxs.setVisibility(View.VISIBLE);
        TextView head = (TextView) dialog.findViewById(R.id.yhsw_tcd_head);
        head.setText(context.getResources().getString(R.string.edityhswtcdinfo));
        final TextView yhsw_tcd_dcdbh = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_dcdbh);
        if (BussUtil.isEmperty(map.get("DCDID").trim())) {
            yhsw_tcd_dcdbh.setText(map.get("DCDID").trim());
        } else {
            yhsw_tcd_dcdbh.setText("");
        }
        final TextView yhsw_tcd_dclxbh = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_dclxbh);
        if (BussUtil.isEmperty(map.get("DCLXID").trim())) {
            yhsw_tcd_dclxbh.setText(map.get("DCLXID").trim());
        } else {
            yhsw_tcd_dclxbh.setText("");
        }
        final EditText yhsw_tcd_dcr = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_dcr);
        if (BussUtil.isEmperty(map.get("DCPERSON").trim())) {
            yhsw_tcd_dcr.setText(map.get("DCPERSON").trim());
        } else {
            yhsw_tcd_dcr.setText("");
        }
        final Button yhsw_tcd_dcrq = (Button) dialog
                .findViewById(R.id.yhsw_tcd_dcrq);
        yhsw_tcd_dcrq.setBackgroundColor(context.getResources().getColor(
                R.color.button_grey));
        if (BussUtil.isEmperty(map.get("DCTIME").trim())) {
            yhsw_tcd_dcrq.setText(map.get("DCTIME").trim());
        } else {
            yhsw_tcd_dcrq.setText("");
        }
        final EditText yhsw_tcd_dcdw = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_dcdw);
        if (BussUtil.isEmperty(map.get("DCDEPARTMENT").trim())) {
            yhsw_tcd_dcdw.setText(map.get("DCDEPARTMENT").trim());
        } else {
            yhsw_tcd_dcdw.setText("");
        }
        final EditText yhsw_tcd_xdm = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_xdm);
        if (BussUtil.isEmperty(map.get("TOPONYMY").trim())) {
            yhsw_tcd_xdm.setText(map.get("TOPONYMY").trim());
        } else {
            yhsw_tcd_xdm.setText("");
        }
        final TextView yhsw_tcd_lon = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_lon);
        if (BussUtil.isEmperty(map.get("LON").trim())) {
            yhsw_tcd_lon.setText(map.get("LON").trim());
        } else {
            yhsw_tcd_lon.setText("");
        }
        final TextView yhsw_tcd_lat = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_lat);
        if (BussUtil.isEmperty(map.get("LAT").trim())) {
            yhsw_tcd_lat.setText(map.get("LAT").trim());
        } else {
            yhsw_tcd_lat.setText("");
        }
        final EditText yhsw_tcd_hb = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_hb);
        if (BussUtil.isEmperty(map.get("ALTITUDE").trim())) {
            yhsw_tcd_hb.setText(map.get("ALTITUDE").trim());
        } else {
            yhsw_tcd_hb.setText("");
        }
        final EditText yhsw_tcd_xbh = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_xbh);
        if (BussUtil.isEmperty(map.get("XBH").trim())) {
            yhsw_tcd_xbh.setText(map.get("XBH").trim());
        } else {
            yhsw_tcd_xbh.setText("");
        }
        final EditText yhsw_tcd_xbmj = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_xbmj);
        if (BussUtil.isEmperty(map.get("XBAREA").trim())) {
            yhsw_tcd_xbmj.setText(map.get("XBAREA").trim());
        } else {
            yhsw_tcd_xbmj.setText("");
        }
        final EditText yhsw_tcd_jzmc = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_jzmc);
        if (BussUtil.isEmperty(map.get("JZNAME").trim())) {
            yhsw_tcd_jzmc.setText(map.get("JZNAME").trim());
        } else {
            yhsw_tcd_jzmc.setText("");
        }
        final Spinner yhsw_tcd_cbtj = (Spinner) dialog
                .findViewById(R.id.yhsw_tcd_cbtj);
        if (BussUtil.isEmperty(map.get("CBTJ").trim())) {
            int a = Integer.parseInt(map.get("CBTJ").trim());
            yhsw_tcd_cbtj.setSelection(a);
        }
        final Spinner yhsw_tcd_lfzc = (Spinner) dialog
                .findViewById(R.id.yhsw_tcd_lfzc);
        if (BussUtil.isEmperty(map.get("LFZC").trim())) {
            int a = Integer.parseInt(map.get("LFZC").trim());
            yhsw_tcd_lfzc.setSelection(a);
        }
        final EditText yhsw_tcd_yhswmc = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_yhswmc);
        if (BussUtil.isEmperty(map.get("YHSWNAME").trim())) {
            yhsw_tcd_yhswmc.setText(map.get("YHSWNAME").trim());
        } else {
            yhsw_tcd_yhswmc.setText("");
        }
        final Spinner yhsw_tcd_whbw = (Spinner) dialog
                .findViewById(R.id.yhsw_tcd_whbw);
        if (BussUtil.isEmperty(map.get("WHBW").trim())) {
            int a = Integer.parseInt(map.get("WHBW").trim());
            yhsw_tcd_whbw.setSelection(a);
        }
        final Spinner yhsw_tcd_mcwhcd = (Spinner) dialog
                .findViewById(R.id.yhsw_tcd_mcwhcd);
        if (BussUtil.isEmperty(map.get("MCWHCD").trim())) {
            int a = Integer.parseInt(map.get("MCWHCD").trim());
            yhsw_tcd_mcwhcd.setSelection(a);
        }
        final Spinner yhsw_tcd_ct = (Spinner) dialog
                .findViewById(R.id.yhsw_tcd_ct);
        if (BussUtil.isEmperty(map.get("CT").trim())) {
            int a = Integer.parseInt(map.get("CT").trim());
            yhsw_tcd_ct.setSelection(a);
        }
        final EditText yhsw_tcd_mcwhmj = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_mcwhmj);
        if (BussUtil.isEmperty(map.get("MCWHMJ").trim())) {
            yhsw_tcd_mcwhmj.setText(map.get("MCWHMJ").trim());
        } else {
            yhsw_tcd_mcwhmj.setText("");
        }
        final Spinner yhsw_tcd_source = (Spinner) dialog
                .findViewById(R.id.yhsw_tcd_source);
        if (BussUtil.isEmperty(map.get("SOURCE").trim())) {
            int a = Integer.parseInt(map.get("SOURCE").trim());
            yhsw_tcd_source.setSelection(a);
        }
        final EditText yhsw_tcd_city = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_city);
        if (BussUtil.isEmperty(map.get("CITY").trim())) {
            yhsw_tcd_city.setText(map.get("CITY").trim());
        } else {
            yhsw_tcd_city.setText("");
        }
        final EditText yhsw_tcd_county = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_county);
        if (BussUtil.isEmperty(map.get("COUNTY").trim())) {
            yhsw_tcd_county.setText(map.get("COUNTY").trim());
        } else {
            yhsw_tcd_county.setText("");
        }
        final EditText yhsw_tcd_town = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_town);
        if (BussUtil.isEmperty(map.get("TOWN").trim())) {
            yhsw_tcd_town.setText(map.get("TOWN").trim());
        } else {
            yhsw_tcd_town.setText("");
        }
        final EditText yhsw_tcd_village = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_village);
        if (BussUtil.isEmperty(map.get("VILLAGE").trim())) {
            yhsw_tcd_village.setText(map.get("VILLAGE").trim());
        } else {
            yhsw_tcd_village.setText("");
        }
        final TextView yhsw_tcd_uploadstatus = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_uploadstatus);
        if (BussUtil.isEmperty(map.get("UPLOADSTATUS").trim())) {
            if ("1".equals(map.get("UPLOADSTATUS").trim())) {
                yhsw_tcd_uploadstatus.setText(context.getResources().getString(
                        R.string.haveupload));
            } else if ("0".equals(map.get("UPLOADSTATUS").trim())) {
                yhsw_tcd_uploadstatus.setText(context.getResources().getString(
                        R.string.havenotupload));
            }
        } else {
            yhsw_tcd_uploadstatus.setText("");
        }
        final EditText yhsw_tcd_bz = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_bz);
        if (BussUtil.isEmperty(map.get("REMARK").trim())) {
            yhsw_tcd_bz.setText(map.get("REMARK").trim());
        } else {
            yhsw_tcd_bz.setText("");
        }
        final EditText yhsw_tcd_sbr = (EditText) dialog
                .findViewById(R.id.yhsw_tcd_sbr);
        if (BussUtil.isEmperty(map.get("SBR").trim())) {
            yhsw_tcd_sbr.setText(map.get("SBR").trim());
        } else {
            yhsw_tcd_sbr.setText("");
        }
        final Button yhsw_tcd_sbsj = (Button) dialog
                .findViewById(R.id.yhsw_tcd_sbsj);
        if (BussUtil.isEmperty(map.get("SBSJ").trim())) {
            yhsw_tcd_sbsj.setText(map.get("SBSJ").trim());
        } else {
            yhsw_tcd_sbsj.setText("");
        }
        yhsw_tcd_sbsj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tcd_sbsj, false);
            }
        });
        Button upload = (Button) dialog.findViewById(R.id.yhsw_tcd_upload);
        upload.setText(context.getResources().getString(R.string.bc));
        Button bdsave = (Button) dialog.findViewById(R.id.yhsw_tcd_bdsave);
        bdsave.setVisibility(View.GONE);
        upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String dcr = yhsw_tcd_dcr.getText().toString().trim();
                if ("".equals(dcr)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.dcrnotnull));
                    return;
                }
                String dctime = yhsw_tcd_dcrq.getText().toString().trim();
                String dcdw = yhsw_tcd_dcdw.getText().toString().trim();
                if ("".equals(dcdw)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.dcdwnotnull));
                    return;
                }
                String lon = yhsw_tcd_lon.getText().toString().trim();
                String lat = yhsw_tcd_lat.getText().toString().trim();
                String hb = yhsw_tcd_hb.getText().toString().trim();
                String xdm = yhsw_tcd_xdm.getText().toString().trim();
                if ("".equals(xdm)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.xdmnotnull));
                    return;
                }
                String xbh = yhsw_tcd_xbh.getText().toString().trim();
                if ("".equals(xbh)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.xbhnotnull));
                    return;
                }
                String xbmj = yhsw_tcd_xbmj.getText().toString().trim();
                if ("".equals(xbmj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.xbmjnotnull));
                    return;
                }
                String jzmc = yhsw_tcd_jzmc.getText().toString().trim();
                if ("".equals(jzmc)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jzmcnotnull));
                    return;
                }
                String cbtj = yhsw_tcd_cbtj.getSelectedItemPosition() + "";
                if ("".equals(cbtj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.cbtjnotnull));
                    return;
                }
                String lfzc = yhsw_tcd_lfzc.getSelectedItemPosition() + "";
                if ("".equals(lfzc)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.lfzcnotnull));
                    return;
                }
                String whbw = yhsw_tcd_whbw.getSelectedItemPosition() + "";
                if ("".equals(whbw)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.whbwnotnull));
                    return;
                }
                String yhswmc = yhsw_tcd_yhswmc.getText().toString().trim();
                if ("".equals(yhswmc)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.yhswmcnotnull));
                    return;
                }
                String mcwhcd = yhsw_tcd_mcwhcd.getSelectedItemPosition() + "";
                if ("".equals(mcwhcd)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.mcwhcdnotnull));
                    return;
                }
                String ly = yhsw_tcd_source.getSelectedItemPosition() + "";
                String ct = yhsw_tcd_ct.getSelectedItemPosition() + "";
                if ("".equals(ct)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.ctnotnull));
                    return;
                }
                String mcwhmj = yhsw_tcd_mcwhmj.getText().toString().trim();
                if ("".equals(mcwhmj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.mcwhmjnotnull));
                    return;
                }
                String bz = yhsw_tcd_bz.getText().toString().trim();
                String city = yhsw_tcd_city.getText().toString().trim();
                if ("".equals(city)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.citynotnull));
                    return;
                }
                String county = yhsw_tcd_county.getText().toString().trim();
                if ("".equals(county)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.countynotnull));
                    return;
                }
                String town = yhsw_tcd_town.getText().toString().trim();
                /*if ("".equals(town)) {
                    ToastUtil.setToast(context, context.getResources()
							.getString(R.string.townnotnull));
					return;
				}*/
                String village = yhsw_tcd_village.getText().toString().trim();
                if ("".equals(village)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.villagenotnull));
                    return;
                }
                String sbr = yhsw_tcd_sbr.getText().toString().trim();
                if ("".equals(sbr)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.sbrnotnull));
                    return;
                }
                String sbsj = yhsw_tcd_sbsj.getText().toString().trim();
                if ("".equals(sbsj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.sbaosjnotnull));
                    return;
                }
                if ("0".equals(map.get("UPLOADSTATUS").trim())) {
                    DataBaseHelper.updateYhswTcdData(context, "db.sqlite", map
                                    .get("DCDID").trim(), dcr, dctime, dcdw, lon,
                            lat, hb, xdm, xbh, xbmj, jzmc, cbtj, lfzc, whbw,
                            yhswmc, mcwhcd, ct, mcwhmj, ly, bz, city, county,
                            town, village, "0", sbr, sbsj);
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editsuccess));
                    map.put("DCDID", map.get("DCDID").trim());
                    map.put("DCLXID", map.get("DCLXID").trim());
                    map.put("DCPERSON", dcr);
                    map.put("DCTIME", dctime);
                    map.put("DCDEPARTMENT", dcdw);
                    map.put("LON", lon);
                    map.put("LAT", lat);
                    map.put("ALTITUDE", hb);
                    map.put("TOPONYMY", xdm);
                    map.put("XBH", xbh);
                    map.put("XBAREA", xbmj);
                    map.put("JZNAME", jzmc);
                    map.put("CBTJ", cbtj);
                    map.put("LFZC", lfzc);
                    map.put("WHBW", whbw);
                    map.put("YHSWNAME", yhswmc);
                    map.put("MCWHCD", mcwhcd);
                    map.put("SOURCE", ly);
                    map.put("CT", ct);
                    map.put("MCWHMJ", mcwhmj);
                    map.put("REMARK", bz);
                    map.put("CITY", city);
                    map.put("COUNTY", county);
                    map.put("TOWN", town);
                    map.put("VILLAGE", village);
                    map.put("UPLOADSTATUS", "0");
                    map.put("SBR", sbr);
                    map.put("SBSJ", sbsj);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
        Button cancle = (Button) dialog.findViewById(R.id.yhsw_tcd_cancle);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogParams(context, dialog, 0.85, 0.9);
    }

    /* 查看 */
    protected void showCkeckDialog(int position) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.yhsw_yhswtc_check);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView dcdid = (TextView) dialog.findViewById(R.id.yhsw_tcd_dcdbh);
        if (BussUtil.isEmperty(list.get(position).get("DCDID").trim())) {
            dcdid.setText(list.get(position).get("DCDID").trim());
        } else {
            dcdid.setText("");
        }
        TextView dclxid = (TextView) dialog.findViewById(R.id.yhsw_tcd_dclxbh);
        if (BussUtil.isEmperty(list.get(position).get("DCLXID").trim())) {
            dclxid.setText(list.get(position).get("DCLXID").trim());
        } else {
            dclxid.setText("");
        }
        TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_tcd_dcr);
        if (BussUtil.isEmperty(list.get(position).get("DCPERSON").trim())) {
            dcr.setText(list.get(position).get("DCPERSON").trim());
        } else {
            dcr.setText("");
        }
        TextView dcrq = (TextView) dialog.findViewById(R.id.yhsw_tcd_dcrq);
        if (BussUtil.isEmperty(list.get(position).get("DCTIME").trim())) {
            dcrq.setText(list.get(position).get("DCTIME").trim());
        } else {
            dcrq.setText("");
        }
        TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_tcd_dcdw);
        if (BussUtil.isEmperty(list.get(position).get("DCDEPARTMENT")
                .trim())) {
            dcdw.setText(list.get(position).get("DCDEPARTMENT").trim());
        } else {
            dcdw.setText("");
        }
        TextView xdm = (TextView) dialog.findViewById(R.id.yhsw_tcd_xdm);
        if (BussUtil.isEmperty(list.get(position).get("TOPONYMY").trim())) {
            xdm.setText(list.get(position).get("TOPONYMY").trim());
        } else {
            xdm.setText("");
        }
        TextView lon = (TextView) dialog.findViewById(R.id.yhsw_tcd_lon);
        if (BussUtil.isEmperty(list.get(position).get("LON").trim())) {
            lon.setText(list.get(position).get("LON").trim());
        } else {
            lon.setText("");
        }
        TextView lat = (TextView) dialog.findViewById(R.id.yhsw_tcd_lat);
        if (BussUtil.isEmperty(list.get(position).get("LAT").trim())) {
            lat.setText(list.get(position).get("LAT").trim());
        } else {
            lat.setText("");
        }
        TextView hb = (TextView) dialog.findViewById(R.id.yhsw_tcd_hb);
        if (BussUtil.isEmperty(list.get(position).get("ALTITUDE").trim())) {
            hb.setText(list.get(position).get("ALTITUDE").trim());
        } else {
            hb.setText("");
        }
        TextView xbh = (TextView) dialog.findViewById(R.id.yhsw_tcd_xbh);
        if (BussUtil.isEmperty(list.get(position).get("XBH").trim())) {
            xbh.setText(list.get(position).get("XBH").trim());
        } else {
            xbh.setText("");
        }
        TextView xbmj = (TextView) dialog.findViewById(R.id.yhsw_tcd_xbmj);
        if (BussUtil.isEmperty(list.get(position).get("XBAREA").trim())) {
            xbmj.setText(list.get(position).get("XBAREA").trim());
        } else {
            xbmj.setText("");
        }
        TextView jzmc = (TextView) dialog.findViewById(R.id.yhsw_tcd_jzmc);
        if (BussUtil.isEmperty(list.get(position).get("JZNAME").trim())) {
            jzmc.setText(list.get(position).get("JZNAME").trim());
        } else {
            jzmc.setText("");
        }
        TextView cbtj = (TextView) dialog.findViewById(R.id.yhsw_tcd_cbtj);
        if (BussUtil.isEmperty(list.get(position).get("CBTJ"))) {
            int a = Integer.parseInt(list.get(position).get("CBTJ"));
            String[] array = context.getResources().getStringArray(R.array.cbtj);
            cbtj.setText(array[a]);
        }
        TextView lfzc = (TextView) dialog.findViewById(R.id.yhsw_tcd_lfzc);
        if (BussUtil.isEmperty(list.get(position).get("LFZC"))) {
            int a = Integer.parseInt(list.get(position).get("LFZC"));
            String[] array = context.getResources().getStringArray(R.array.lfzc);
            lfzc.setText(array[a]);
        }
        TextView yhswmc = (TextView) dialog.findViewById(R.id.yhsw_tcd_yhswmc);
        if (BussUtil.isEmperty(list.get(position).get("YHSWNAME").trim())) {
            yhswmc.setText(list.get(position).get("YHSWNAME").trim());
        } else {
            yhswmc.setText("");
        }
        TextView whbw = (TextView) dialog.findViewById(R.id.yhsw_tcd_whbw);
        if (BussUtil.isEmperty(list.get(position).get("WHBW"))) {
            int a = Integer.parseInt(list.get(position).get("WHBW"));
            String[] array = context.getResources().getStringArray(R.array.whbw);
            whbw.setText(array[a]);
        }
        TextView mcwhcd = (TextView) dialog.findViewById(R.id.yhsw_tcd_mcwhcd);
        if (BussUtil.isEmperty(list.get(position).get("MCWHCD"))) {
            int a = Integer.parseInt(list.get(position).get("MCWHCD"));
            String[] array = context.getResources().getStringArray(R.array.mcwhcd);
            mcwhcd.setText(array[a]);
        }
        TextView ct = (TextView) dialog.findViewById(R.id.yhsw_tcd_ct);
        if (BussUtil.isEmperty(list.get(position).get("CT"))) {
            int a = Integer.parseInt(list.get(position).get("CT"));
            String[] array = context.getResources().getStringArray(R.array.ct);
            ct.setText(array[a]);
        }
        TextView mcwhmj = (TextView) dialog.findViewById(R.id.yhsw_tcd_mcwhmj);
        if (BussUtil.isEmperty(list.get(position).get("MCWHMJ").trim())) {
            mcwhmj.setText(list.get(position).get("MCWHMJ").trim());
        } else {
            mcwhmj.setText("");
        }
        TextView source = (TextView) dialog.findViewById(R.id.yhsw_tcd_source);
        if (BussUtil.isEmperty(list.get(position).get("SOURCE"))) {
            int a = Integer.parseInt(list.get(position).get("SOURCE"));
            String[] array = context.getResources().getStringArray(R.array.ly);
            source.setText(array[a]);
        }
        TextView bz = (TextView) dialog.findViewById(R.id.yhsw_tcd_bz);
        if (BussUtil.isEmperty(list.get(position).get("REMARK").trim())) {
            bz.setText(list.get(position).get("REMARK").trim());
        } else {
            bz.setText("");
        }
        TextView city = (TextView) dialog.findViewById(R.id.yhsw_tcd_city);
        if (BussUtil.isEmperty(list.get(position).get("CITY").trim())) {
            city.setText(list.get(position).get("CITY").trim());
        } else {
            city.setText("");
        }
        TextView county = (TextView) dialog.findViewById(R.id.yhsw_tcd_county);
        if (BussUtil.isEmperty(list.get(position).get("COUNTY").trim())) {
            county.setText(list.get(position).get("COUNTY").trim());
        } else {
            county.setText("");
        }
        TextView town = (TextView) dialog.findViewById(R.id.yhsw_tcd_town);
        if (BussUtil.isEmperty(list.get(position).get("TOWN").trim())) {
            town.setText(list.get(position).get("TOWN").trim());
        } else {
            town.setText("");
        }
        TextView village = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_village);
        if (BussUtil.isEmperty(list.get(position).get("VILLAGE").trim())) {
            village.setText(list.get(position).get("VILLAGE").trim());
        } else {
            village.setText("");
        }
        TextView uploadstatus = (TextView) dialog
                .findViewById(R.id.yhsw_tcd_uploadstatusw);//yhsw_tcd_uploadstatus
        if (BussUtil.isEmperty(list.get(position).get("UPLOADSTATUS")
                .trim())) {
            if ("1".equals(list.get(position).get("UPLOADSTATUS").trim())) {
                uploadstatus.setText(context.getResources().getString(
                        R.string.haveupload));
            } else if ("0".equals(list.get(position).get("UPLOADSTATUS")
                    .trim())) {
                uploadstatus.setText(context.getResources().getString(
                        R.string.havenotupload));
            }
        } else {
            uploadstatus.setText("");
        }
        Button back = (Button) dialog.findViewById(R.id.yhsw_tcd_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogParams(context, dialog, 0.85, 0.9);
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
        public TextView tv9;
        public TextView tv10;
        public TextView tv11;
    }

}
