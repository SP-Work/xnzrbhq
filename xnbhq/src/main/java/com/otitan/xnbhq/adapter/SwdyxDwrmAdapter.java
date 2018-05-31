package com.otitan.xnbhq.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.SwdyxActivity;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 生物多样性 动物扰民管理Adapter
 */

public class SwdyxDwrmAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public SwdyxDwrmAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_swdyx_dwrm, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.dwrm_cb);
            holder.tv1 = (TextView) convertView.findViewById(R.id.dwrm_tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.dwrm_tv2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.dwrm_tv3);
            holder.tv4 = (TextView) convertView.findViewById(R.id.dwrm_tv4);
            holder.tv5 = (TextView) convertView.findViewById(R.id.dwrm_tv5);
            holder.tv6 = (TextView) convertView.findViewById(R.id.dwrm_tv6);
            holder.tv7 = (TextView) convertView.findViewById(R.id.dwrm_tv7);
            holder.tv8 = (TextView) convertView.findViewById(R.id.dwrm_tv8);
            holder.tv9 = (TextView) convertView.findViewById(R.id.dwrm_tv9);
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
        Log.i("abc", list.get(position).toString() + "+++");
        if (BussUtil.isEmperty(list.get(position).get("EVENTNAME").trim())) {
            holder.tv2.setText(list.get(position).get("EVENTNAME").trim());
        } else {
            holder.tv2.setText("");
        }
        if (BussUtil
                .isEmperty(list.get(position).get("BIONAME").trim())) {
            holder.tv3
                    .setText(list.get(position).get("BIONAME").trim());
        } else {
            holder.tv3.setText("");
        }
        if (BussUtil
                .isEmperty(list.get(position).get("DISPERSON").trim())) {
            holder.tv4
                    .setText(list.get(position).get("DISPERSON").trim());
        } else {
            holder.tv4.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("TRANSACTOR")
                .trim())) {
            holder.tv5.setText(list.get(position).get("TRANSACTOR")
                    .trim());
        } else {
            holder.tv5.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("ADDRESS")
                .trim())) {
            holder.tv6.setText(list.get(position).get("ADDRESS")
                    .trim());
        } else {
            holder.tv6.setText("");
        }
        if (BussUtil
                .isEmperty(list.get(position).get("HAPPENTIME").trim())) {
            try {
                String str = list.get(position).get("HAPPENTIME").trim()
                        .substring(0, 10);
                Date date = format.parse(str.replace("/", "-"));
                holder.tv7.setText(format.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.tv7.setText("");
        }
        holder.tv8.setText("查看");
        holder.tv8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCheckDailog(list.get(position));
            }
        });
        holder.tv9.setText("编辑");
        holder.tv9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showEditDailog(list.get(position));

            }
        });


        return convertView;
    }

    /* 查看 */
    protected void showCheckDailog(HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_dwrmgl_check);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView swdyx_dwrm_sjmc = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_sjmc);
        TextView swdyx_dwrm_fsdd = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_fsdd);
        TextView swdyx_dwrm_rmdw = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_rmdw);
        TextView swdyx_dwrm_brr = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_brr);
        TextView swdyx_dwrm_fssj = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_fssj);
        TextView swdyx_dwrm_sfclw = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_sfclw);
        TextView swdyx_dwrm_zbr = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_zbr);
        TextView swdyx_dwrm_sjms = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_sjms);
        TextView swdyx_dwrm_sjcljg = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_sjcljg);
        TextView swdyx_dwrm_bz = (TextView) dialog
                .findViewById(R.id.swdyx_dwrm_bz);

        Button back = (Button) dialog.findViewById(R.id.swdyx_dwrm_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });

        if (BussUtil.isEmperty(map.get("EVENTNAME").trim())) {
            swdyx_dwrm_sjmc.setText(map.get("EVENTNAME").trim());
        } else {
            swdyx_dwrm_sjmc.setText("");
        }

        if (BussUtil.isEmperty(map.get("TRANSACTOR").trim())) {
            swdyx_dwrm_zbr.setText(map.get("TRANSACTOR").trim());
        } else {
            swdyx_dwrm_zbr.setText("");
        }

        if (BussUtil.isEmperty(map.get("DISPERSON").trim())) {
            swdyx_dwrm_brr.setText(map.get("DISPERSON").trim());
        } else {
            swdyx_dwrm_brr.setText("");
        }
        if (BussUtil.isEmperty(map.get("DESCRIPTION").trim())) {
            swdyx_dwrm_sjms.setText(map.get("DESCRIPTION").trim());
        } else {
            swdyx_dwrm_sjms.setText("");
        }
        if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
            swdyx_dwrm_fsdd.setText(map.get("ADDRESS").trim());
        } else {
            swdyx_dwrm_fsdd.setText("");
        }
        if (BussUtil.isEmperty(map.get("BIONAME").trim())) {
            swdyx_dwrm_rmdw.setText(map.get("BIONAME").trim());
        } else {
            swdyx_dwrm_rmdw.setText("");
        }
        if (BussUtil.isEmperty(map.get("RESULTDESC").trim())) {
            swdyx_dwrm_sjcljg.setText(map.get("RESULTDESC")
                    .trim());
        } else {
            swdyx_dwrm_sjcljg.setText("");
        }

        try {
            if (BussUtil.isEmperty(map.get("HAPPENTIME").trim())) {
                String str = map.get("HAPPENTIME").trim().substring(0, 10);
                Date date = format.parse(str.replace("/", "-"));
                swdyx_dwrm_fssj.setText(format.format(date));
            } else {
                swdyx_dwrm_fssj.setText("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (BussUtil.isEmperty(map.get("ISFINISHED").trim())) {
            swdyx_dwrm_sfclw.setText(map.get("ISFINISHED").trim());
        } else {
            swdyx_dwrm_sfclw.setText("");
        }
        if (BussUtil.isEmperty(map.get("REMARK").trim())) {
            swdyx_dwrm_bz.setText(map.get("REMARK").trim());
        } else {
            swdyx_dwrm_bz.setText("");
        }

        BussUtil.setDialogGravity_Center(context, dialog, 0.8, 0.90);
    }

    /* 编辑 */
    protected void showEditDailog(final HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_dwrmgl_add);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView swdyx_dwrm_head = (TextView) dialog.findViewById(R.id.swdyx_dwrm_head);
        swdyx_dwrm_head.setText(context.getResources().getString(R.string.editanimaltrouble));

        final EditText swdyx_dwrm_rmsjmc = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_rmsjmc);
        if (BussUtil.isEmperty(map.get("EVENTNAME").trim())) {
            swdyx_dwrm_rmsjmc.setText(map.get("EVENTNAME").trim());
        } else {
            swdyx_dwrm_rmsjmc.setText("");
        }
        final EditText swdyx_dwrm_fsdd = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_fsdd);
        if (BussUtil.isEmperty(map.get("ADDRESS").trim())) {
            swdyx_dwrm_fsdd.setText(map.get("ADDRESS").trim());
        } else {
            swdyx_dwrm_fsdd.setText("");
        }
        final EditText swdyx_dwrm_rmdw = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_rmdw);
        if (BussUtil.isEmperty(map.get("BIONAME").trim())) {
            swdyx_dwrm_rmdw.setText(map.get("BIONAME").trim());
        } else {
            swdyx_dwrm_rmdw.setText("");
        }
        final EditText swdyx_dwrm_brr = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_brr);
        if (BussUtil.isEmperty(map.get("DISPERSON").trim())) {
            swdyx_dwrm_brr.setText(map.get("DISPERSON").trim());
        } else {
            swdyx_dwrm_brr.setText("");
        }
        final Button swdyx_dwrm_fssj = (Button) dialog
                .findViewById(R.id.swdyx_dwrm_fssj);
        if (BussUtil.isEmperty(map.get("HAPPENTIME").trim())) {
            swdyx_dwrm_fssj.setText(map.get("HAPPENTIME").trim());
        } else {
            swdyx_dwrm_fssj.setText("");
        }
        final SwdyxActivity activity = (SwdyxActivity) context;
        swdyx_dwrm_fssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activity.trajectoryPresenter.initSelectTimePopuwindow(swdyx_dwrm_fssj, false);
            }
        });
        final EditText swdyx_dwrm_zbr = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_zbr);
        if (BussUtil.isEmperty(map.get("TRANSACTOR").trim())) {
            swdyx_dwrm_zbr.setText(map.get("TRANSACTOR").trim());
        } else {
            swdyx_dwrm_zbr.setText("");
        }
        final EditText swdyx_dwrm_sjms = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_sjms);
        if (BussUtil.isEmperty(map.get("DESCRIPTION").trim())) {
            swdyx_dwrm_sjms.setText(map.get("DESCRIPTION").trim());
        } else {
            swdyx_dwrm_sjms.setText("");
        }

        final EditText swdyx_dwrm_sjcljg = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_sjcljg);
        if (BussUtil.isEmperty(map.get("RESULTDESC").trim())) {
            swdyx_dwrm_sjcljg.setText(map.get("RESULTDESC").trim());
        } else {
            swdyx_dwrm_sjcljg.setText("");
        }
        final EditText swdyx_dwrm_bz = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_bz);
        if (BussUtil.isEmperty(map.get("REMARK").trim())) {
            swdyx_dwrm_bz.setText(map.get("REMARK").trim());
        } else {
            swdyx_dwrm_bz.setText("");
        }
        final CheckBox swdyx_dwrm_sfclw = (CheckBox) dialog.findViewById(R.id.swdyx_dwrm_sfclw);
        if (BussUtil.isEmperty(map.get("ISFINISHED").trim())) {
            if ("是".equals(map.get("ISFINISHED").trim())) {
                swdyx_dwrm_sfclw.setChecked(true);
            } else {
                swdyx_dwrm_sfclw.setChecked(false);
            }
        } else {
            swdyx_dwrm_sfclw.setChecked(false);
        }
        Button save = (Button) dialog.findViewById(R.id.swdyx_dwrm_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Webservice webservice = new Webservice(context);
                String id = "";
                if (BussUtil.isEmperty(map.get("ID").trim())) {
                    id = map.get("ID").trim();
                }
                String name = swdyx_dwrm_rmsjmc.getText().toString().trim();
                if (!BussUtil.isEmperty(name)) {
                    ToastUtil.setToast(context, context.getResources().getString(R.string.troublenamenotnull));
                    return;
                }
                String address = swdyx_dwrm_fsdd.getText().toString().trim();
                if (!BussUtil.isEmperty(address)) {
                    ToastUtil.setToast(context, context.getResources().getString(R.string.adressnotnull));
                    return;
                }
                String fstime = swdyx_dwrm_fssj.getText().toString().trim();
                if (!BussUtil.isEmperty(fstime)) {
                    ToastUtil.setToast(context, context.getResources().getString(R.string.actiontimenotnull));
                    return;
                }
                String rmdw = swdyx_dwrm_rmdw.getText().toString().trim();
                String brr = swdyx_dwrm_brr.getText().toString().trim();

                String zbr = swdyx_dwrm_zbr.getText().toString().trim();
                String sjms = swdyx_dwrm_sjms.getText().toString().trim();
                String sjcljg = swdyx_dwrm_sjcljg.getText().toString().trim();
                String bz = swdyx_dwrm_bz.getText().toString().trim();
                String sfclw;
                if (swdyx_dwrm_sfclw.isChecked()) {
                    sfclw = "是";
                } else {
                    sfclw = "否";
                }
                String result = webservice.updateSwdyxDwrmgl(id, name, address, rmdw, brr, fstime, sfclw, zbr, sjms, sjcljg, bz);
                if ("true".equals(result)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editsuccess));
                    map.put("EVENTNAME", name);
                    map.put("ADDRESS", address);
                    map.put("BIONAME", rmdw);
                    map.put("DISPERSON", brr);
                    map.put("HAPPENTIME", fstime);
                    map.put("ISFINISHED", sfclw);
                    map.put("TRANSACTOR", zbr);
                    map.put("DESCRIPTION", sjms);
                    map.put("RESULTDESC", sjcljg);
                    map.put("REMARK", bz);
                    notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.editfailed));
                }
            }
        });

        Button cancle = (Button) dialog
                .findViewById(R.id.swdyx_dwrm_cancle);
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
        public TextView tv8;
        public TextView tv9;
    }


}
