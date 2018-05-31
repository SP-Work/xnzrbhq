package com.otitan.xnbhq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
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
 * 有害生物路线Adapter
 */
public class YhswLxtcAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private LayoutInflater inflater = null;
    private YHSWActivity activity;

    public YhswLxtcAdapter(Context context, List<HashMap<String, String>> list) {
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
            convertView = inflater.inflate(R.layout.item_yhsw_lxtc, null);
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

        if (BussUtil.isEmperty(list.get(position).get("TCLXID"))) {
            holder.tv1.setText(list.get(position).get("TCLXID"));
        } else {
            holder.tv1.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("TCPERSON"))) {
            holder.tv2.setText(list.get(position).get("TCPERSON"));
        } else {
            holder.tv2.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("TCDEPARTMENT"))) {
            holder.tv3.setText(list.get(position).get("TCDEPARTMENT"));
        } else {
            holder.tv3.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("JZMC"))) {
            holder.tv4.setText(list.get(position).get("JZMC"));
        } else {
            holder.tv4.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("SBR"))) {
            holder.tv5.setText(list.get(position).get("SBR"));
        } else {
            holder.tv5.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("SBSJ"))) {
            String sbsj = list.get(position).get("SBSJ").replace("/", "-");
            holder.tv6.setText(sbsj);
        } else {
            holder.tv6.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("UPLOADSTATUS"))) {
            if ("0".equals(list.get(position).get("UPLOADSTATUS"))) {
                holder.tv7.setText(context.getResources().getString(
                        R.string.havenotupload));
                holder.tv10.setVisibility(View.VISIBLE);
                holder.tv9.setVisibility(View.VISIBLE);
            } else if ("1".equals(list.get(position).get("UPLOADSTATUS"))) {
                holder.tv7.setText(context.getResources().getString(
                        R.string.haveupload));
                holder.tv10.setVisibility(View.GONE);
                holder.tv9.setVisibility(View.GONE);
            } else {
                holder.tv7.setText("");
                holder.tv10.setVisibility(View.VISIBLE);
                holder.tv9.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tv7.setText("");
            holder.tv10.setVisibility(View.VISIBLE);
            holder.tv9.setVisibility(View.VISIBLE);
        }
        holder.tv8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCkeckDialog(list.get(position));
            }
        });
        holder.tv9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showEditDialog(list.get(position));
            }
        });
        holder.tv10.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ShangBao(list.get(position));
            }
        });
        return convertView;
    }

    /**
     * 路线踏查 上报
     */
    protected void ShangBao(HashMap<String, String> map) {
        Webservice web = new Webservice(context);
        String tclxid = map.get("TCLXID");
        String tcr = map.get("TCPERSON");
        String tcsj = map.get("TCTIME");
        String tcdw = map.get("TCDEPARTMENT");
        String tcdbmj = map.get("TCDBMJ");
        String qdjd = map.get("TCQDLON");
        String qdwd = map.get("TCQDLAT");
        String zdjd = map.get("TCZDLON");
        String zdwd = map.get("TCZDLAT");
        String yhswmc = map.get("YHSWNAME");
        String jzmc = map.get("JZMC");
        String lfzc = map.get("LFZC");
        String city = map.get("CITY");
        String county = map.get("COUNTY");
        String town = map.get("TOWN");
        String village = map.get("VILLAGE");
        String bz = map.get("REMARK");
        String sbr = map.get("SBR");
        String sbsj = map.get("SBSJ");

        String result = web.addYhswlxtcallData(tclxid, tcr, tcsj, tcdw, tcdbmj, qdjd, qdwd, zdjd, zdwd, yhswmc, jzmc, lfzc, city, county, town, village, bz, "1", sbr, sbsj);
        String[] splits = result.split(",");
        if (splits.length > 0) {
            if ("True".equals(splits[0])) {
                DataBaseHelper.deleteYhswTclxData(context, "db.sqlite",
                        map.get("ID"));
                map.put("UPLOADSTATUS", "1");
                notifyDataSetChanged();
                ToastUtil.setToast(context,
                        context.getResources()
                                .getString(R.string.uploadsuccess));
            } else {
                ToastUtil
                        .setToast(
                                context,
                                context.getResources().getString(
                                        R.string.uploadfailed));
            }
        } else {
            ToastUtil.setToast(context,
                    context.getResources().getString(R.string.uploadfailed));
        }
    }

    /**
     * 路线踏查 编辑
     */
    protected void showEditDialog(final HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.yhsw_lxtc_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView yhsw_tclx_head = (TextView) dialog.findViewById(R.id.yhsw_tclx_head);
        yhsw_tclx_head.setText(context.getResources().getString(R.string.edittclxxx));

        final EditText yhsw_tclx_tcr = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_tcr);
        yhsw_tclx_tcr.setText(map.get("TCPERSON"));

        final Button yhsw_tclx_tcrq = (Button) dialog
                .findViewById(R.id.yhsw_tclx_tcrq);
        yhsw_tclx_tcrq.setText(map.get("TCTIME"));

        final EditText yhsw_tclx_tcdw = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_tcdw);
        yhsw_tclx_tcdw.setText(map.get("TCDEPARTMENT"));


        final EditText yhsw_tclx_tcdbmj = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_tcdbmj);
        yhsw_tclx_tcdbmj.setText(map.get("TCDBMJ"));

        final EditText yhsw_tclx_yhswmc = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_yhswmc);
        yhsw_tclx_yhswmc.setText(map.get("YHSWNAME"));

        final EditText yhsw_tclx_jzmc = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_jzmc);
        yhsw_tclx_jzmc.setText(map.get("JZMC"));


        final Spinner yhsw_tclx_lfzc = (Spinner) dialog.findViewById(R.id.yhsw_tclx_lfzc);
        if (BussUtil.isEmperty(map.get("LFZC").trim())) {
            int a = Integer.parseInt(map.get("LFZC").trim());
            yhsw_tclx_lfzc.setSelection(a);
        }

        final EditText yhsw_tclx_city = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_city);
        yhsw_tclx_city.setText(map.get("CITY"));

        final EditText yhsw_tclx_county = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_county);
        yhsw_tclx_county.setText(map.get("COUNTY"));

        final EditText yhsw_tclx_town = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_town);
        yhsw_tclx_town.setText(map.get("TOWN"));

        final EditText yhsw_tclx_village = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_village);
        yhsw_tclx_village.setText(map.get("VILLAGE"));

        final EditText yhsw_tclx_sbr = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_sbr);
        yhsw_tclx_sbr.setText(map.get("SBR"));

        final Button yhsw_tclx_sbsj = (Button) dialog
                .findViewById(R.id.yhsw_tclx_sbsj);
        yhsw_tclx_sbsj.setText(map.get("SBSJ"));

        final EditText yhsw_tclx_bz = (EditText) dialog
                .findViewById(R.id.yhsw_tclx_bz);
        yhsw_tclx_bz.setText(map.get("REMARK"));

        yhsw_tclx_sbsj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tclx_sbsj, false);
            }

        });
        yhsw_tclx_tcrq.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_tclx_tcrq, false);
            }

        });

        Button upload = (Button) dialog.findViewById(R.id.yhsw_tclx_upload);
        upload.setText(context.getResources().getString(R.string.bc));
        Button bdsave = (Button) dialog.findViewById(R.id.yhsw_tclx_bdsave);
        bdsave.setVisibility(View.GONE);
        upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String tclxid = map.get("TCLXID");
                if ("".equals(tclxid)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.addtclxfailed));
                } else {
                    String tcr = yhsw_tclx_tcr.getText().toString();
                    if (TextUtils.isEmpty(tcr)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.tcrnotnull));
                        return;
                    }
                    String tcrq = yhsw_tclx_tcrq.getText().toString().trim();
                    if (TextUtils.isEmpty(tcrq)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.tcrqnotnull));
                        return;
                    }
                    String tcdw = yhsw_tclx_tcdw.getText().toString().trim();
                    if (TextUtils.isEmpty(tcrq)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.tcdwnotnull));
                        return;
                    }
                    String tcdbmj = yhsw_tclx_tcdbmj.getText().toString()
                            .trim();
                    if (TextUtils.isEmpty(tcdbmj)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.tcdbmjnotnull));
                        return;
                    }

                    String yhswmc = yhsw_tclx_yhswmc.getText().toString()
                            .trim();
                    if (TextUtils.isEmpty(yhswmc)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.yhswmcnotnull));
                        return;
                    }

                    String jzmc = yhsw_tclx_jzmc.getText().toString().trim();
                    String lfzc = yhsw_tclx_lfzc.getSelectedItemPosition() + "";
                    if (TextUtils.isEmpty(lfzc)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.lfzcnotnull));
                        return;
                    }

                    String city = yhsw_tclx_city.getText().toString().trim();
                    if (TextUtils.isEmpty(city)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.citynotnull));
                        return;
                    }
                    String county = yhsw_tclx_county.getText().toString()
                            .trim();
                    if (TextUtils.isEmpty(county)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.countynotnull));
                        return;
                    }
                    String town = yhsw_tclx_town.getText().toString().trim();
                    if (TextUtils.isEmpty(town)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.townnotnull));
                        return;
                    }
                    String village = yhsw_tclx_village.getText().toString()
                            .trim();
                    if (TextUtils.isEmpty(village)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.villagenotnull));
                        return;
                    }
                    String sbr = yhsw_tclx_sbr.getText().toString().trim();
                    if (TextUtils.isEmpty(sbr)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.sbrnotnull));
                        return;
                    }
                    String sbsj = yhsw_tclx_sbsj.getText().toString().trim();
                    if (TextUtils.isEmpty(sbsj)) {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.sbaosjnotnull));
                        return;
                    }
                    String bz = yhsw_tclx_bz.getText().toString().trim();
                    DataBaseHelper.updateYhswTclxData2(context, "db.sqlite",
                            tclxid, tcr, tcrq, tcdw, tcdbmj, yhswmc, jzmc, lfzc, city, county, town,
                            village, bz, sbr, sbsj);
                    dialog.dismiss();
                    map.put("TCLXID", tclxid);
                    map.put("TCPERSON", tcr);
                    map.put("TCTIME", tcrq);
                    map.put("TCDEPARTMENT", tcdw);
                    map.put("TCDBMJ", tcdbmj);
                    map.put("YHSWNAME", yhswmc);
                    map.put("JZMC", jzmc);
                    map.put("LFZC", lfzc);
                    map.put("CITY", city);
                    map.put("COUNTY", county);
                    map.put("TOWN", town);
                    map.put("VILLAGE", village);
                    map.put("REMARK", bz);
                    map.put("SBR", sbr);
                    map.put("SBSJ", sbsj);
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editsuccess));
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        Button cancle = (Button) dialog.findViewById(R.id.yhsw_tclx_cancle);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogParams(context, dialog, 0.85, 0.9);
    }

    /**
     * 踏查路线 查看
     */
    protected void showCkeckDialog(HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.yhsw_lxtc_check);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView tclxbh = (TextView) dialog.findViewById(R.id.yhsw_tclx_tclxbh);
        tclxbh.setText(map.get("TCLXID").trim());

        TextView sbzt = (TextView) dialog.findViewById(R.id.yhsw_tclx_sbzt);
        if (BussUtil.isEmperty(map.get("UPLOADSTATUS").trim())) {
            if ("1".equals(map.get("UPLOADSTATUS").trim())) {
                sbzt.setText(context.getResources().getString(
                        R.string.haveupload));
            } else if ("0".equals(map.get("UPLOADSTATUS").trim())) {
                sbzt.setText(context.getResources().getString(
                        R.string.havenotupload));
            }
        } else {
            sbzt.setText("");
        }

        TextView tcqdjd = (TextView) dialog.findViewById(R.id.yhsw_tclx_tcqdjd);
        tcqdjd.setText(map.get("TCQDLON").trim());

        TextView tcqdwd = (TextView) dialog.findViewById(R.id.yhsw_tclx_tcqdwd);
        tcqdwd.setText(map.get("TCQDLAT").trim());

        TextView tczdjd = (TextView) dialog.findViewById(R.id.yhsw_tclx_tczdjd);
        tczdjd.setText(map.get("TCZDLON").trim());

        TextView tczdwd = (TextView) dialog.findViewById(R.id.yhsw_tclx_tczdwd);
        tczdwd.setText(map.get("TCZDLAT").trim());

        TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_tclx_tcr);
        dcr.setText(map.get("TCPERSON").trim());

        TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_tclx_tcrq);
        dcsj.setText(map.get("TCTIME").replace("/", "-"));

        TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_tclx_tcdw);
        dcdw.setText(map.get("TCDEPARTMENT").trim());

        TextView tcdbmj = (TextView) dialog.findViewById(R.id.yhsw_tclx_tcdbmj);
        tcdbmj.setText(map.get("TCDBMJ").trim());

        TextView yhswmc = (TextView) dialog.findViewById(R.id.yhsw_tclx_yhswmc);
        yhswmc.setText(map.get("YHSWNAME").trim());

        TextView jzmc = (TextView) dialog.findViewById(R.id.yhsw_tclx_jzmc);
        jzmc.setText(map.get("JZMC").trim());

        TextView lfzc = (TextView) dialog.findViewById(R.id.yhsw_tclx_lfzc);
        if (BussUtil.isEmperty(map.get("LFZC").trim())) {
            int a = Integer.parseInt(map.get("LFZC").trim());
            String[] array = context.getResources()
                    .getStringArray(R.array.lfzc);
            lfzc.setText(array[a]);
        }

        TextView city = (TextView) dialog.findViewById(R.id.yhsw_tclx_city);
        city.setText(map.get("CITY").trim());

        TextView county = (TextView) dialog.findViewById(R.id.yhsw_tclx_county);
        county.setText(map.get("COUNTY").trim());

        TextView town = (TextView) dialog.findViewById(R.id.yhsw_tclx_town);
        town.setText(map.get("TOWN").trim());

        TextView village = (TextView) dialog
                .findViewById(R.id.yhsw_tclx_village);
        village.setText(map.get("VILLAGE").trim());

        TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_tclx_sbr);
        sbr.setText(map.get("SBR").trim());

        TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_tclx_sbsj);
        sbsj.setText(map.get("SBSJ").trim());

        TextView bz = (TextView) dialog.findViewById(R.id.yhsw_tclx_bz);
        bz.setText(map.get("REMARK").trim());

        Button back = (Button) dialog.findViewById(R.id.yhsw_tclx_back);
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
    }

}
