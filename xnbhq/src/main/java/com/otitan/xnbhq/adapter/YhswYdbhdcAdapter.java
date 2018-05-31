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
 * 有害生物 样地病害管理Adapter
 */
public class YhswYdbhdcAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private List<HashMap<String, String>> xclist = new ArrayList<>();
    private HashMap<String, String> xcmap = new HashMap<>();
    private LayoutInflater inflater = null;
    private YHSWActivity activity;

    public YhswYdbhdcAdapter(Context context, List<HashMap<String, String>> list, List<HashMap<String, String>> xclist) {
        this.context = context;
        this.list = list;
        this.xclist = xclist;
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
            convertView = inflater.inflate(R.layout.item_yhsw_ydchdc, null);
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

        if (BussUtil.isEmperty(list.get(position).get("YDBH"))) {
            holder.tv1.setText(list.get(position).get("YDBH"));
        } else {
            holder.tv1.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("DCR"))) {
            holder.tv2.setText(list.get(position).get("DCR"));
        } else {
            holder.tv2.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("DCDW"))) {
            holder.tv3.setText(list.get(position).get("DCDW"));
        } else {
            holder.tv3.setText("");
        }
        if (BussUtil.isEmperty(list.get(position).get("BHMC"))) {
            holder.tv4.setText(list.get(position).get("BHMC"));
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
        if (BussUtil.isEmperty(list.get(position).get("SCZT"))) {
            if ("0".equals(list.get(position).get("SCZT"))) {
                holder.tv7.setText(context.getResources().getString(
                        R.string.havenotupload));
                holder.tv10.setVisibility(View.VISIBLE);
                holder.tv9.setVisibility(View.VISIBLE);
            } else if ("1".equals(list.get(position).get("SCZT"))) {
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
                ShangBao(xcmap, list.get(position));
            }
        });
        holder.tv11.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showYdchxcDialog(xcmap, list.get(position));
            }
        });
        return convertView;
    }

    /* 样地虫害详查 查看 */
    protected void showYdchxcDialog(HashMap<String, String> xcmap,
                                    HashMap<String, String> map) {
        // 判断是否存在对应的详情表
        for (int i = 0; i < xclist.size(); i++) {
            if (BussUtil.isEmperty(map.get("YDBH"))
                    && BussUtil.isEmperty(xclist.get(i).get("YDBH"))) {
                if (map.get("YDBH").equals(xclist.get(i).get("YDBH"))) {
                    xcmap = xclist.get(i);
                    break;
                }
            }
        }
        if (xcmap != null) {
            final Dialog dialog = new Dialog(context, R.style.Dialog);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            dialog.setContentView(R.layout.yhsw_ydbhxcb_check);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            TextView yhsw_ydchxcb_ydbh = (TextView) dialog
                    .findViewById(R.id.yhsw_ydbhxcb_ydbh);
            TextView yhsw_ydchxcb_ysbh = (TextView) dialog
                    .findViewById(R.id.yhsw_ydbhxcb_ysbh);
            TextView yhsw_ydbhxcb_bhfj = (TextView) dialog
                    .findViewById(R.id.yhsw_ydbhxcb_bhfj);
            TextView yhsw_ydbhxcb_sg = (TextView) dialog
                    .findViewById(R.id.yhsw_ydbhxcb_sg);
            TextView yhsw_ydbhxcb_xj = (TextView) dialog
                    .findViewById(R.id.yhsw_ydbhxcb_xj);
            TextView yhsw_ydbhxcb_gf = (TextView) dialog
                    .findViewById(R.id.yhsw_ydbhxcb_gf);
            yhsw_ydchxcb_ydbh.setText(xcmap.get("YDBH"));
            yhsw_ydchxcb_ysbh.setText(xcmap.get("YSBH"));
            String[] bhfj = context.getResources().getStringArray(R.array.bhfj);
            if (BussUtil.isEmperty(xcmap.get("BHFJ"))) {
                int a = Integer.parseInt(xcmap.get("BHFJ"));
                yhsw_ydbhxcb_bhfj.setText(bhfj[a]);
            } else {
                yhsw_ydbhxcb_bhfj.setText("");
            }
            yhsw_ydbhxcb_sg.setText(xcmap.get("SG"));
            yhsw_ydbhxcb_xj.setText(xcmap.get("XJ"));
            yhsw_ydbhxcb_gf.setText(xcmap.get("GF"));
            Button back = (Button) dialog.findViewById(R.id.yhsw_ydbhxcb_back);
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();

                }
            });
            BussUtil.setDialogParams(context, dialog, 0.85, 0.9);
        } else {
            ToastUtil.setToast(context,
                    context.getResources().getString(R.string.xcxxbcz));
        }

    }

    /* 上报 */
    protected void ShangBao(HashMap<String, String> xcmap, HashMap<String, String> map) {
        Webservice web = new Webservice(context);
        String ydbh = map.get("YDBH");
        String dcr = map.get("DCR");
        String dcsj = map.get("DCSJ");
        String dcdw = map.get("DCDW");
        String ydxz = map.get("YDXZ");
        String ydc = map.get("YDC");
        String ydk = map.get("YDK");
        String ydmj = map.get("YDMJ");
        String dclx = map.get("DCLX");
        String yddbmj = map.get("YDDBMJ");
        String zhmj = map.get("ZHMJ");
        String hb = map.get("HB");
        String ycjjd = map.get("YCJLON");
        String ycjwd = map.get("YCJLAT");
        String xbh = map.get("XBH");
        String xbmj = map.get("XBMJ");
        String city = map.get("CITY");
        String county = map.get("COUNTY");
        String town = map.get("TOWN");
        String village = map.get("VILLAGE");
        String xdm = map.get("XDM");
        String jzmc = map.get("JZMC");
        String bhmc = map.get("BHMC");
        String fblx = map.get("FBLX");
        String ly = map.get("LY");
        String whcd = map.get("WHCD");
        String cbtj = map.get("CBTJ");
        String gslx = map.get("GSLX");
        String bgl = map.get("BGL");
        String bgzs = map.get("BGZS");
        String pjxj = map.get("PJXJ");
        String sl = map.get("SL");
        String ybd = map.get("YBD");
        String pd = map.get("PD");
        String dx = map.get("DX");
        String ykh = map.get("YKH");
        String yksd = map.get("YKSD");
        String pjg = map.get("PJG");
        String yhzwgd = map.get("YHZWGD");
        String mpmc = map.get("MPMC");
        String mpzmj = map.get("MPZMJ");
        String cml = map.get("CML");
        String zymmpz = map.get("ZYMMPZ");
        String bz = map.get("BZ");
        String sbr = map.get("SBR");
        String sbsj = map.get("SBSJ");
        String result = web.addYhswYdbhdcData(ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, xdm, jzmc, bhmc, fblx, ly, whcd, cbtj, gslx, bgl, bgzs, pjxj, sl, ybd, pd, dx, ykh, yksd, pjg, yhzwgd, mpmc, mpzmj, cml, zymmpz, bz, "1", sbr, sbsj);
        String[] splits = result.split(",");
        if (splits.length > 0) {
            if ("True".equals(splits[0])) {
                DataBaseHelper.deleteYhswYdbhdcData(context, "db.sqlite",
                        map.get("ID"));
                map.put("SCZT", "1");
                notifyDataSetChanged();
                ToastUtil.setToast(context, context.getResources().getString(R.string.uploadsuccess));
                // 判断是否存在对应的详情表
                for (int i = 0; i < xclist.size(); i++) {
                    if (BussUtil.isEmperty(map.get("YDBH")) && BussUtil.isEmperty(xclist.get(i).get("YDBH"))) {
                        if (map.get("YDBH").equals(xclist.get(i).get("YDBH"))) {
                            xcmap = xclist.get(i);
                            break;
                        }
                    }
                }
                if (xcmap != null) {
                    String xcresult = web.addYhswYdbhxcData(xcmap.get("YDBH"), xcmap.get("YSBH"), xcmap.get("BHFJ"), xcmap.get("SG"), xcmap.get("XJ"), xcmap.get("GF"));
                    String[] xcsplits = xcresult.split(",");
                    if (xcsplits.length > 0) {
                        if ("True".equals(xcsplits[0])) {
                            ToastUtil.setToast(context, context.getResources()
                                    .getString(R.string.xcsbsuccess));
                            DataBaseHelper.deleteYhswYdbhxcData(context,
                                    "db.sqlite", xcmap.get("ID"));
                        } else {
                            ToastUtil.setToast(context, context.getResources()
                                    .getString(R.string.xcsbfailed));
                        }
                    } else {
                        ToastUtil.setToast(context, context.getResources()
                                .getString(R.string.xcsbfailed));
                    }
                }
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

    /* 编辑 */
    protected void showEditDialog(final HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.yhsw_ydbhdc_add);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView yhsw_ydbhdc_head = (TextView) dialog
                .findViewById(R.id.yhsw_ydbhdc_head);
        yhsw_ydbhdc_head.setText(context.getString(R.string.editydbhdcxx));
        final TextView yhsw_ydbhdc_ydbh = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ydbh);
        yhsw_ydbhdc_ydbh.setText(map.get("YDBH").trim());

        final EditText yhsw_ydbhdc_dcr = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_dcr);
        yhsw_ydbhdc_dcr.setText(map.get("DCR").trim());

        final TextView yhsw_ydbhdc_dcrq = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_dcrq);
        yhsw_ydbhdc_dcrq.setText(map.get("DCSJ").trim());

        final EditText yhsw_ydbhdc_dcdw = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_dcdw);
        yhsw_ydbhdc_dcdw.setText(map.get("DCDW").trim());

        final EditText yhsw_ydbhdc_ydc = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_ydc);
        yhsw_ydbhdc_ydc.setText(map.get("YDC").trim());

        final EditText yhsw_ydbhdc_ydk = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_ydk);
        yhsw_ydbhdc_ydk.setText(map.get("YDK").trim());

        final Spinner yhsw_ydbhdc_ydxz = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_ydxz);

        if (BussUtil.isEmperty(map.get("YDXZ"))) {
            int a = Integer.parseInt(map.get("YDXZ"));
            yhsw_ydbhdc_ydxz.setSelection(a);
        }


        final EditText yhsw_ydbhdc_ydmj = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_ydmj);
        yhsw_ydbhdc_ydmj.setText(map.get("YDMJ").trim());

        final Spinner yhsw_ydbhdc_dclx = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_dclx);
        if (BussUtil.isEmperty(map.get("DCLX"))) {
            int a = Integer.parseInt(map.get("DCLX"));
            yhsw_ydbhdc_dclx.setSelection(a);
        }


        final EditText yhsw_ydbhdc_yddbmj = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_yddbmj);
        yhsw_ydbhdc_yddbmj.setText(map.get("YDDBMJ").trim());

        final EditText yhsw_ydbhdc_zhmj = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_zhmj);
        yhsw_ydbhdc_zhmj.setText(map.get("ZHMJ").trim());

        final EditText yhsw_ydbhdc_hb = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_hb);
        yhsw_ydbhdc_hb.setText(map.get("HB").trim());

        final EditText yhsw_ydbhdc_ycjjd = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_ycjjd);
        yhsw_ydbhdc_ycjjd.setText(map.get("YCJLON").trim());

        final EditText yhsw_ydbhdc_ycjwd = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_ycjwd);
        yhsw_ydbhdc_ycjwd.setText(map.get("YCJLAT").trim());

        final EditText yhsw_ydbhdc_xbh = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_xbh);
        yhsw_ydbhdc_xbh.setText(map.get("XBH").trim());

        final EditText yhsw_ydbhdc_xbmj = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_xbmj);
        yhsw_ydbhdc_xbmj.setText(map.get("XBMJ").trim());

        final EditText yhsw_ydbhdc_city = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_city);
        yhsw_ydbhdc_city.setText(map.get("CITY").trim());

        final EditText yhsw_ydbhdc_county = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_county);
        yhsw_ydbhdc_county.setText(map.get("COUNTY").trim());

        final EditText yhsw_ydbhdc_town = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_town);
        yhsw_ydbhdc_town.setText(map.get("TOWN").trim());

        final EditText yhsw_ydbhdc_village = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_village);
        yhsw_ydbhdc_village.setText(map.get("VILLAGE").trim());

        final EditText yhsw_ydbhdc_xdm = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_xdm);
        yhsw_ydbhdc_xdm.setText(map.get("XDM").trim());

        final EditText yhsw_ydbhdc_jzmc = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_jzmc);
        yhsw_ydbhdc_jzmc.setText(map.get("JZMC").trim());

        final EditText yhsw_ydbhdc_bhmc = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_bhmc);
        yhsw_ydbhdc_bhmc.setText(map.get("BHMC").trim());

        final Spinner yhsw_ydbhdc_fblx = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_fblx);
        if (BussUtil.isEmperty(map.get("FBLX"))) {
            int a = Integer.parseInt(map.get("FBLX"));
            yhsw_ydbhdc_fblx.setSelection(a);
        }

        final EditText yhsw_ydbhdc_pjxj = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_pjxj);
        yhsw_ydbhdc_pjxj.setText(map.get("PJXJ").trim());

        final Spinner yhsw_ydbhdc_whcd = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_whcd);
        if (BussUtil.isEmperty(map.get("WHCD"))) {
            int a = Integer.parseInt(map.get("WHCD"));
            yhsw_ydbhdc_whcd.setSelection(a);
        }

        final Spinner yhsw_ydbhdc_ly = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_ly);
        if (BussUtil.isEmperty(map.get("LY"))) {
            int a = Integer.parseInt(map.get("LY"));
            yhsw_ydbhdc_ly.setSelection(a);
        }

        final Spinner yhsw_ydbhdc_cbtj = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_cbtj);
        if (BussUtil.isEmperty(map.get("CBTJ"))) {
            int a = Integer.parseInt(map.get("CBTJ"));
            yhsw_ydbhdc_cbtj.setSelection(a);
        }

        final EditText yhsw_ydbhdc_bgl = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_bgl);
        yhsw_ydbhdc_bgl.setText(map.get("BGL").trim());

        final EditText yhsw_ydbhdc_bgzs = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_bgzs);
        yhsw_ydbhdc_bgzs.setText(map.get("BGZS").trim());

        final EditText yhsw_ydbhdc_pd = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_pd);
        yhsw_ydbhdc_pd.setText(map.get("PD").trim());


        final Spinner yhsw_ydbhdc_dixing = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_dixing);
        if (BussUtil.isEmperty(map.get("DX"))) {
            int a = Integer.parseInt(map.get("DX"));
            yhsw_ydbhdc_dixing.setSelection(a);
        }

        final EditText yhsw_ydbhdc_yubidu = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_yubidu);
        yhsw_ydbhdc_yubidu.setText(map.get("YBD").trim());

        final EditText yhsw_ydbhdc_pjgao = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_pjgao);
        yhsw_ydbhdc_pjgao.setText(map.get("PJG").trim());

        final EditText yhsw_ydbhdc_yhzwgd = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_yhzwgd);
        yhsw_ydbhdc_yhzwgd.setText(map.get("YHZWGD").trim());

        final EditText yhsw_ydbhdc_ykh = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_ykh);
        yhsw_ydbhdc_ykh.setText(map.get("YKH").trim());

        final EditText yhsw_ydbhdc_yksd = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_yksd);
        yhsw_ydbhdc_yksd.setText(map.get("YKSD").trim());

        final EditText yhsw_ydbhdc_shuage = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_shuage);
        yhsw_ydbhdc_shuage.setText(map.get("SL").trim());

        final EditText yhsw_ydbhdc_mpmc = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_mpmc);
        yhsw_ydbhdc_mpmc.setText(map.get("MPMC").trim());

        final EditText yhsw_ydbhdc_mpzmj = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_mpzmj);
        yhsw_ydbhdc_mpzmj.setText(map.get("MPZMJ").trim());

        final EditText yhsw_ydbhdc_chanml = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_chanml);
        yhsw_ydbhdc_chanml.setText(map.get("CML").trim());

        final EditText yhsw_ydbhdc_zymmpz = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_zymmpz);
        yhsw_ydbhdc_zymmpz.setText(map.get("ZYMMPZ").trim());

        final EditText yhsw_ydbhdc_sbr = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_sbr);
        yhsw_ydbhdc_sbr.setText(map.get("SBR").trim());

        final Button yhsw_ydbhdc_sbsj = (Button) dialog.findViewById(R.id.yhsw_ydbhdc_sbsj);
        yhsw_ydbhdc_sbsj.setText(map.get("SBSJ").trim());
        yhsw_ydbhdc_sbsj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activity.trajectoryPresenter.initSelectTimePopuwindow(yhsw_ydbhdc_sbsj, false);
            }
        });

        final Spinner yhsw_ydbhdc_gslx = (Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_gslx);
        if (BussUtil.isEmperty(map.get("GSLX"))) {
            int a = Integer.parseInt(map.get("GSLX"));
            yhsw_ydbhdc_gslx.setSelection(a);
        }

        final TextView yhsw_ydbhdc_sczt = (TextView) dialog
                .findViewById(R.id.yhsw_ydbhdc_uploadstatus);
        if ("1".equals(map.get("SCZT").trim())) {
            yhsw_ydbhdc_sczt.setText(context.getResources().getString(
                    R.string.haveupload));
        } else if ("0".equals(map.get("SCZT").trim())) {
            yhsw_ydbhdc_sczt.setText(context.getResources().getString(
                    R.string.havenotupload));
        } else {
            yhsw_ydbhdc_sczt.setText("");
        }

        final EditText yhsw_ydbhdc_bz = (EditText) dialog.findViewById(R.id.yhsw_ydbhdc_bz);
        yhsw_ydbhdc_bz.setText(map.get("BZ").trim());

        Button upload = (Button) dialog
                .findViewById(R.id.yhsw_ydbhdc_upload);
        upload.setText(context.getResources().getString(R.string.bc));
        Button bdsave = (Button) dialog.findViewById(R.id.yhsw_ydbhdc_bdsave);
        bdsave.setVisibility(View.GONE);
        upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String ydbh = yhsw_ydbhdc_ydbh.getText().toString().trim();
                if (TextUtils.isEmpty(ydbh)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.ydbhnotnull));
                    return;
                }
                String dcr = yhsw_ydbhdc_dcr.getText().toString().trim();
                if (TextUtils.isEmpty(dcr)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.dcrnotnull));
                    return;
                }
                String dcsj = yhsw_ydbhdc_dcrq.getText().toString().trim();
                if (TextUtils.isEmpty(dcsj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.dcsjnotnull));
                    return;
                }
                String dcdw = yhsw_ydbhdc_dcdw.getText().toString().trim();
                if (TextUtils.isEmpty(dcdw)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.dcdwnotnull));
                    return;
                }
                String ydmj = yhsw_ydbhdc_ydmj.getText().toString().trim();
                if (TextUtils.isEmpty(ydmj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.ydmjnotnull));
                    return;
                }
                String yddbmj = yhsw_ydbhdc_yddbmj.getText().toString().trim();
                if (TextUtils.isEmpty(yddbmj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.yddbmjnotnull));
                    return;
                }
                String zhmj = yhsw_ydbhdc_zhmj.getText().toString().trim();
                if (TextUtils.isEmpty(zhmj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.zhmjnotnull));
                    return;
                }
                String ycjjd = yhsw_ydbhdc_ycjjd.getText().toString().trim();
                if (TextUtils.isEmpty(ycjjd)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.ycjjdnotnull));
                    return;
                }
                String ycjwd = yhsw_ydbhdc_ycjwd.getText().toString().trim();
                if (TextUtils.isEmpty(ycjwd)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.ycjwdnotnull));
                    return;
                }
                String xbh = yhsw_ydbhdc_xbh.getText().toString().trim();
                if (TextUtils.isEmpty(xbh)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.xbhnotnull));
                    return;
                }
                String xbmj = yhsw_ydbhdc_xbmj.getText().toString().trim();
                if (TextUtils.isEmpty(xbmj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.xbmjnotnull));
                    return;
                }
                String city = yhsw_ydbhdc_city.getText().toString().trim();
                if (TextUtils.isEmpty(city)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.citynotnull));
                    return;
                }
                String county = yhsw_ydbhdc_county.getText().toString().trim();
                if (TextUtils.isEmpty(county)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.countynotnull));
                    return;
                }
                String town = yhsw_ydbhdc_town.getText().toString().trim();
                if (TextUtils.isEmpty(town)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.townnotnull));
                    return;
                }
                String village = yhsw_ydbhdc_village.getText().toString().trim();
                if (TextUtils.isEmpty(village)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.villagenotnull));
                    return;
                }
                String xdm = yhsw_ydbhdc_xdm.getText().toString().trim();
                if (TextUtils.isEmpty(xdm)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.xdmnotnull));
                    return;
                }
                String jzmc = yhsw_ydbhdc_jzmc.getText().toString().trim();
                if (TextUtils.isEmpty(jzmc)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.jzmcnotnull));
                    return;
                }
                String bhmc = yhsw_ydbhdc_bhmc.getText().toString().trim();
                if (TextUtils.isEmpty(bhmc)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.bhmcnotnull));
                    return;
                }
                String fblx = yhsw_ydbhdc_fblx.getSelectedItemPosition() + "";
                if (TextUtils.isEmpty(fblx)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.fblxnotnull));
                    return;
                }
                String gslx = yhsw_ydbhdc_gslx.getSelectedItemPosition() + "";
                if (TextUtils.isEmpty(gslx)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.gslxnotnull));
                    return;
                }
                String bgl = yhsw_ydbhdc_bgl.getText().toString().trim();
                if (TextUtils.isEmpty(bgl)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.bglnotnull));
                    return;
                }
                String bgzs = yhsw_ydbhdc_bgzs.getText().toString().trim();
                if (TextUtils.isEmpty(bgzs)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.bgzsnotnull));
                    return;
                }
                String pjxj = yhsw_ydbhdc_pjxj.getText().toString().trim();
                if (TextUtils.isEmpty(pjxj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.pjxjnotnull));
                    return;
                }
                String ybd = yhsw_ydbhdc_yubidu.getText().toString().trim();
                if (TextUtils.isEmpty(ybd)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.ybdnotnull));
                    return;
                }
                String sbr = yhsw_ydbhdc_sbr.getText().toString().trim();
                if (TextUtils.isEmpty(sbr)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.sbrnotnull));
                    return;
                }
                String sbsj = yhsw_ydbhdc_sbsj.getText().toString().trim();
                if (TextUtils.isEmpty(sbsj)) {
                    ToastUtil.setToast(context, context.getResources()
                            .getString(R.string.sbaosjnotnull));
                    return;
                }
                String whcd = yhsw_ydbhdc_whcd.getSelectedItemPosition() + "";
                String ydc = yhsw_ydbhdc_ydc.getText().toString().trim();
                String ydxz = yhsw_ydbhdc_ydxz.getSelectedItemPosition() + "";
                String ydk = yhsw_ydbhdc_ydk.getText().toString().trim();
                String dclx = yhsw_ydbhdc_dclx.getSelectedItemPosition() + "";
                String hb = yhsw_ydbhdc_hb.getText().toString().trim();
                String ly = yhsw_ydbhdc_ly.getSelectedItemPosition() + "";
                String cbtj = yhsw_ydbhdc_cbtj.getSelectedItemPosition() + "";
                String sl = yhsw_ydbhdc_shuage.getText().toString().trim();
                String pd = yhsw_ydbhdc_pd.getText().toString().trim();
                String dx = yhsw_ydbhdc_dixing.getSelectedItemPosition() + "";
                String ykh = yhsw_ydbhdc_ykh.getText().toString().trim();
                String yksd = yhsw_ydbhdc_yksd.getText().toString().trim();
                String pjg = yhsw_ydbhdc_pjgao.getText().toString().trim();
                String yhzwgd = yhsw_ydbhdc_yhzwgd.getText().toString().trim();
                String mpmc = yhsw_ydbhdc_mpmc.getText().toString().trim();
                String mpzmj = yhsw_ydbhdc_mpzmj.getText().toString().trim();
                String cml = yhsw_ydbhdc_chanml.getText().toString().trim();
                String zymmpz = yhsw_ydbhdc_zymmpz.getText().toString().trim();
                String bz = yhsw_ydbhdc_bz.getText().toString().trim();
                DataBaseHelper.updateYhswYdbhdcData(context, "db.sqlite", ydbh, dcr, dcsj, dcdw, ydxz, ydc, ydk, ydmj, dclx, yddbmj, zhmj, hb, ycjjd, ycjwd, xbh, xbmj, city, county, town, village, xdm, jzmc, bhmc, fblx, ly, whcd, cbtj, gslx, bgl, bgzs, pjxj, sl, ybd, pd, dx, ykh, yksd, pjg, yhzwgd, mpmc, mpzmj, cml, zymmpz, bz, sbr, sbsj);
                map.put("DCR", dcr);
                map.put("DCSJ", dcsj);
                map.put("DCDW", dcdw);
                map.put("YDXZ", ydxz);
                map.put("YDC", ydc);
                map.put("YDK", ydk);
                map.put("YDMJ", ydmj);
                map.put("DCLX", dclx);
                map.put("YDDBMJ", yddbmj);
                map.put("ZHMJ", zhmj);
                map.put("HB", hb);
                map.put("YCJLON", ycjjd);
                map.put("YCJLAT", ycjwd);
                map.put("XBH", xbh);
                map.put("XBMJ", xbmj);
                map.put("CITY", city);
                map.put("COUNTY", county);
                map.put("TOWN", town);
                map.put("VILLAGE", village);
                map.put("XDM", xdm);
                map.put("JZMC", jzmc);
                map.put("BHMC", bhmc);
                map.put("FBLX", fblx);
                map.put("LY", ly);
                map.put("WHCD", whcd);
                map.put("CBTJ", cbtj);
                map.put("GSLX", gslx);
                map.put("BGL", bgl);
                map.put("BGZS", bgzs);
                map.put("PJXJ", pjxj);
                map.put("SL", sl);
                map.put("YBD", ybd);
                map.put("PD", pd);
                map.put("DX", dx);
                map.put("YKH", ykh);
                map.put("YKSD", yksd);
                map.put("PJG", pjg);
                map.put("YHZWGD", yhzwgd);
                map.put("MPMC", mpmc);
                map.put("MPZMJ", mpzmj);
                map.put("CML", cml);
                map.put("ZYMMPZ", zymmpz);
                map.put("BZ", bz);
                map.put("SBR", sbr);
                map.put("SBSJ", sbsj);
                ToastUtil.setToast(context,
                        context.getResources().getString(R.string.editsuccess));
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        Button cancle = (Button) dialog.findViewById(R.id.yhsw_ydbhdc_cancle);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogParams(context, dialog, 0.85, 0.9);
    }

    /* 查看 */
    protected void showCkeckDialog(HashMap<String, String> map) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.yhsw_ydbhdc_check);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView dcdid = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ydbh);
        dcdid.setText(map.get("YDBH").trim());

        TextView dcr = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_dcr);
        dcr.setText(map.get("DCR").trim());

        TextView dcsj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_dcrq);
        dcsj.setText(map.get("DCSJ").replace("/", "-"));

        TextView dcdw = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_dcdw);
        dcdw.setText(map.get("DCDW").trim());

        TextView ydc = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ydc);
        ydc.setText(map.get("YDC").trim());

        TextView ydk = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ydk);
        ydk.setText(map.get("YDK").trim());

        TextView ydxz = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ydxz);
        if (BussUtil.isEmperty(map.get("YDXZ").trim())) {
            int a = Integer.parseInt(map.get("YDXZ").trim());
            String[] array = context.getResources().getStringArray(R.array.ydxz);
            ydxz.setText(array[a]);
        }

        TextView ydmj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ydmj);
        ydmj.setText(map.get("YDMJ").trim());

        TextView dclx = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_dclx);
        if (BussUtil.isEmperty(map.get("DCLX").trim())) {
            int a = Integer.parseInt(map.get("DCLX").trim());
            String[] array = context.getResources().getStringArray(R.array.dclx);
            dclx.setText(array[a]);
        }

        TextView yddbmj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_yddbmj);
        yddbmj.setText(map.get("YDDBMJ").trim());

        TextView zhmj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_zhmj);
        zhmj.setText(map.get("ZHMJ").trim());

        TextView hb = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_hb);
        hb.setText(map.get("HB").trim());

        TextView ycjjd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ycjjd);
        ycjjd.setText(map.get("YCJLON").trim());

        TextView ycjwd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ycjwd);
        ycjwd.setText(map.get("YCJLAT").trim());

        TextView xbh = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_xbh);
        xbh.setText(map.get("XBH").trim());

        TextView xbmj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_xbmj);
        xbmj.setText(map.get("XBMJ").trim());

        TextView city = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_city);
        city.setText(map.get("CITY").trim());

        TextView county = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_county);
        county.setText(map.get("COUNTY").trim());

        TextView town = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_town);
        town.setText(map.get("TOWN").trim());

        TextView village = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_village);
        village.setText(map.get("VILLAGE").trim());

        TextView xdm = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_xdm);
        xdm.setText(map.get("XDM").trim());

        TextView jzmc = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_jzmc);
        jzmc.setText(map.get("JZMC").trim());

        TextView bhmc = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_bhmc);
        bhmc.setText(map.get("BHMC").trim());

        TextView fblx = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_fblx);
        if (BussUtil.isEmperty(map.get("FBLX").trim())) {
            int a = Integer.parseInt(map.get("FBLX").trim());
            String[] array = context.getResources().getStringArray(R.array.fblx);
            fblx.setText(array[a]);
        }

        TextView pjxj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_pjxj);
        pjxj.setText(map.get("PJXJ").trim());

        TextView whcd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_whcd);
        if (BussUtil.isEmperty(map.get("WHCD").trim())) {
            int a = Integer.parseInt(map.get("WHCD").trim());
            String[] array = context.getResources().getStringArray(R.array.mcwhcd);
            whcd.setText(array[a]);
        }

        TextView ly = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ly);
        if (BussUtil.isEmperty(map.get("LY").trim())) {
            int a = Integer.parseInt(map.get("LY").trim());
            String[] array = context.getResources().getStringArray(R.array.ly);
            ly.setText(array[a]);
        }

        TextView cbtj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_cbtj);
        if (BussUtil.isEmperty(map.get("CBTJ").trim())) {
            int a = Integer.parseInt(map.get("CBTJ").trim());
            String[] array = context.getResources().getStringArray(R.array.cbtj);
            cbtj.setText(array[a]);
        }

        TextView bgl = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_bgl);
        bgl.setText(map.get("BGL").trim());

        TextView bgzs = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_bgzs);
        bgzs.setText(map.get("BGZS").trim());

        TextView pd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_pd);
        pd.setText(map.get("PD").trim());

        TextView dx = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_dixing);
        if (BussUtil.isEmperty(map.get("DX").trim())) {
            int a = Integer.parseInt(map.get("DX").trim());
            String[] array = context.getResources().getStringArray(R.array.dx);
            dx.setText(array[a]);
        }

        TextView ybd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_yubidu);
        ybd.setText(map.get("YBD").trim());

        TextView pjg = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_pjgao);
        pjg.setText(map.get("PJG").trim());

        TextView yhzwgd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_yhzwgd);
        yhzwgd.setText(map.get("YHZWGD").trim());

        TextView ykh = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_ykh);
        ykh.setText(map.get("YKH").trim());

        TextView yksd = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_yksd);
        yksd.setText(map.get("YKSD").trim());

        TextView sl = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_shuage);
        sl.setText(map.get("SL").trim());

        TextView mpmc = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_mpmc);
        if (BussUtil.isEmperty(map.get("MPMC").trim())) {
            mpmc.setText(map.get("MPMC").trim());
        } else {
            mpmc.setText("");
        }
        TextView mpzmj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_mpzmj);
        mpzmj.setText(map.get("MPZMJ").trim());

        TextView cml = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_chanml);
        cml.setText(map.get("CML").trim());

        TextView zymmpz = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_zymmpz);
        zymmpz.setText(map.get("ZYMMPZ").trim());

        TextView sbr = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_sbr);
        sbr.setText(map.get("SBR").trim());

        TextView sbsj = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_sbsj);
        sbsj.setText(map.get("SBSJ").replace("/", "-"));

        TextView gslx = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_gslx);
        if (BussUtil.isEmperty(map.get("GSLX").trim())) {
            int a = Integer.parseInt(map.get("GSLX").trim());
            String[] array = context.getResources().getStringArray(R.array.gslx);
            gslx.setText(array[a]);
        }

        TextView bz = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_bz);
        bz.setText(map.get("BZ").trim());

        TextView uploadstatus = (TextView) dialog
                .findViewById(R.id.yhsw_ydbhdc_uploadstatus);
        if (BussUtil.isEmperty(map.get("SCZT").trim())) {
            if ("1".equals(map.get("SCZT").trim())) {
                uploadstatus.setText(context.getResources().getString(
                        R.string.haveupload));
            } else if ("0".equals(map.get("SCZT").trim())) {
                uploadstatus.setText(context.getResources().getString(
                        R.string.havenotupload));
            }
        } else {
            uploadstatus.setText("");
        }
        Button back = (Button) dialog.findViewById(R.id.yhsw_ydbhdc_back);
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
        public TextView tv12;
    }

}
