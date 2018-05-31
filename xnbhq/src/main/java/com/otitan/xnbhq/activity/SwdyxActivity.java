package com.otitan.xnbhq.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.core.geometry.Point;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.SwdyxBbgAdapter;
import com.otitan.xnbhq.adapter.SwdyxCgAdapter;
import com.otitan.xnbhq.adapter.SwdyxDwrmAdapter;
import com.otitan.xnbhq.adapter.SwdyxJczAdapter;
import com.otitan.xnbhq.adapter.SwdyxJhzAdapter;
import com.otitan.xnbhq.adapter.SwdyxJydwdwAdapter;
import com.otitan.xnbhq.adapter.SwdyxJyscAdapter;
import com.otitan.xnbhq.adapter.SwdyxRcxjAdapter;
import com.otitan.xnbhq.adapter.SwdyxXyfzjdAdapter;
import com.otitan.xnbhq.dialog.YyybAddDialog;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.customview.MorePopWindow;
import com.titan.baselibrary.util.DialogParamsUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2016/5/26.
 * 生物多样性页面
 */
public class SwdyxActivity extends BaseActivity {

    SwdyxActivity mContext;
    View parentview, bottomview;
    TextView xcddc;
    /* 巡查点调查popupwindow */
    MorePopWindow xcddcPopWindow;

    /* 巡查管理数据list */
    List<HashMap<String, String>> rcxjLlist = new ArrayList<>();
    /* 动物扰民事件管理list */
    List<HashMap<String, String>> dwrmLlist = new ArrayList<>();
    /* 救护站管理list */
    List<HashMap<String, String>> jhzLlist = new ArrayList<>();
    /* 驯养繁殖基地list */
    List<HashMap<String, String>> xyfzjdLlist = new ArrayList<>();
    /* 经营动物单位list */
    List<HashMap<String, String>> jydwdwLlist = new ArrayList<>();
    /* 标本馆list */
    List<HashMap<String, String>> bbgLlist = new ArrayList<>();
    /* 餐馆list */
    List<HashMap<String, String>> cgLlist = new ArrayList<>();
    /* 疫源疫病监测站list */
    List<HashMap<String, String>> jczLlist = new ArrayList<>();
    /* 交易市场list */
    List<HashMap<String, String>> jyscLlist = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parentview = getLayoutInflater().inflate(R.layout.activity_swdyx, null);
        super.onCreate(savedInstanceState);
        setContentView(parentview);

        mContext = SwdyxActivity.this;
        ImageView topview = (ImageView) parentview.findViewById(R.id.topview);
        topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_swdyx));

        activitytype = getIntent().getStringExtra("name");
        proData = BussUtil.getConfigXml(mContext, activitytype);

        initView();

    }

    /**
     * 生物多样性 控件初始化
     */
    private void initView() {
        bottomview = parentview.findViewById(R.id.swdyx_bottomview);
        TextView rcxcgl = (TextView) bottomview.findViewById(R.id.swdyx_rcxcgl);
        rcxcgl.setOnClickListener(new MyListense());

        TextView dwrmgl = (TextView) bottomview.findViewById(R.id.swdyx_dwrmgl);
        dwrmgl.setOnClickListener(new MyListense());

        xcddc = (TextView) bottomview.findViewById(R.id.swdyx_xcddc);
        xcddc.setOnClickListener(new MyListense());

        TextView yyyb = (TextView) bottomview.findViewById(R.id.swdyx_yyyb);
        yyyb.setOnClickListener(new MyListense());

        xbbjInclude.setVisibility(View.GONE);

    }

    @Override
    public View getParentView() {
        return parentview;
    }

    class MyAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // 日常巡查管理
            if (params[0].equals("rcxcgl")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        initRcxjData();
                        initRcxjView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 动物扰民事件
            } else if (params[0].equals("dwrmgl")) {
                initDwrmglData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initdwrmglView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 救护站
            } else if (params[0].equals("jhzgl")) {
                initJhzData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initJhzView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 驯养繁殖基地
            } else if (params[0].equals("xyfzjd")) {
                initXyfzjdData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initXyfzjdView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 经营动物单位
            } else if (params[0].equals("jydwdw")) {
                initJydwdwData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initJydwdwView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 标本馆
            } else if (params[0].equals("bbg")) {
                initBbgData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initBbgView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 餐馆
            } else if (params[0].equals("cg")) {
                initCgData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initCgView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 疫源疫病监测站
            } else if (params[0].equals("yyybjcz")) {
                initYyybjczData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initYyybjczView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
                // 交易市场
            } else if (params[0].equals("jysc")) {
                initJyscData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        initJyscView();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }
                });
            }
            return null;
        }

    }

    class MyListense implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.swdyx_rcxcgl:
                    // 日常巡检管理页面
                    initRcxjData();
                    initRcxjView();
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    break;
                case R.id.swdyx_dwrmgl:
                    // 动物扰民管理界面
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("dwrmgl");
                    break;
                case R.id.swdyx_yyyb:
                    /*疫源疫病*/
                    YyybAddDialog addDialog = new YyybAddDialog(mContext,R.style.Dialog);
                    DialogParamsUtil.setDialogParamsCenter(mContext,addDialog,0.5,0.6);
                    break;
                case R.id.swdyx_xcddc:
                    /*巡查点调查*/
                    xcddcPopWindow = new MorePopWindow(SwdyxActivity.this, R.layout.popup_swdyx_xcddc);
                    xcddcPopWindow.showAtLocation(bottomview, Gravity.BOTTOM | Gravity.CENTER,
                            xcddc.getWidth() / 2, xcddc.getHeight() + 10);
                /* 救护站 */
                    View jhzAdd = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_jhz_add);
                /* 驯养繁殖基地 */
                    View xyfzjdAdd = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_xyfzjd_add);
                /* 经营动物单位 */
                    View jydwdwAdd = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_jydwdw_add);
                /* 标本馆 */
                    View bbgAdd = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_bbg_add);
                /* 餐馆 */
                    View cgAdd = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_cg_add);
				/* 疫源疫病监测站*/
                    View yyybjczAdd = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_yyybjcz_add);
				/* 交易市场*/
                    View jysc = xcddcPopWindow.getContentView().findViewById(
                            R.id.swdyx_jysc_add);
                    jhzAdd.setOnClickListener(new MyListense());
                    xyfzjdAdd.setOnClickListener(new MyListense());
                    jydwdwAdd.setOnClickListener(new MyListense());
                    bbgAdd.setOnClickListener(new MyListense());
                    cgAdd.setOnClickListener(new MyListense());
                    yyybjczAdd.setOnClickListener(new MyListense());
                    jysc.setOnClickListener(new MyListense());
                    break;
			/* 救护站信息管理界面 */
                case R.id.swdyx_jhz_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("jhzgl");
                    break;
			/* 驯养繁殖基地管理 */
                case R.id.swdyx_xyfzjd_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("xyfzjd");
                    break;
			/* 经营动物单位管理 */
                case R.id.swdyx_jydwdw_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("jydwdw");
                    break;
			/* 标本馆管理 */
                case R.id.swdyx_bbg_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("bbg");
                    break;
			/* 餐馆管理 */
                case R.id.swdyx_cg_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("cg");
                    break;
				/*疫源疫病监测站*/
                case R.id.swdyx_yyybjcz_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("yyybjcz");
                    break;
				/*交易市场*/
                case R.id.swdyx_jysc_add:
                    xcddcPopWindow.dismiss();
                    ProgressDialogUtil.startProgressDialog(mContext);
                    new MyAsyncTask().execute("jysc");
                    break;
                default:
                    break;

            }
        }
    }

    /* 巡查点添加 */
    public void addXcdPoint(Point point) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_xcd_add);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button sure = (Button) dialog.findViewById(R.id.swdyx_xcd_add_btn_sure);
        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });

        ImageView close = (ImageView) dialog
                .findViewById(R.id.swdyx_xcd_add_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.5, 0.6);
    }

    /**
     * 弹出日常巡检管理的页面
     */
    public void initRcxjView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_rcxcgl_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog.findViewById(R.id.swdyx_rcxj_zwsj);

        final Spinner swdyx_rcgl_sjss = (Spinner) dialog.findViewById(R.id.swdyx_rcgl_sjss);
        final Spinner swdyx_rcgl_sfywfxw = (Spinner) dialog.findViewById(R.id.swdyx_rcgl_sfywfxw);
        final List<String> sjsslist = new ArrayList<String>();
        sjsslist.add("--全部--");
        for (int i = 0; i < sjssLlist.size(); i++) {
            sjsslist.add(sjssLlist.get(i).get("DUNAME"));
        }
        ArrayAdapter<String> sjssadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, sjsslist);
        sjssadapter.setDropDownViewResource(R.layout.myspinner);
        swdyx_rcgl_sjss.setAdapter(sjssadapter);
        ArrayAdapter<CharSequence> sfwfadapter = ArrayAdapter.createFromResource(mContext, R.array.sfwfarray, R.layout.myspinner);
        swdyx_rcgl_sfywfxw.setAdapter(sfwfadapter);

        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_rcxj_listview);
        final SwdyxRcxjAdapter adapter = new SwdyxRcxjAdapter(mContext,
                mapView, graphicsLayer, rcxjLlist);
        if (rcxjLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_rcxj_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_rcgl_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showAddDialog(adapter, listView, zwsjTxt);
            }
        });
        Button delete = (Button) dialog.findViewById(R.id.swdyx_rcgl_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                rcgldelete(adapter, listView, zwsjTxt);
            }
        });
        CheckBox box = (CheckBox) dialog.findViewById(R.id.cb);
        box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < rcxjLlist.size(); i++) {
                        rcxjLlist.get(i)
                                .put(rcxjLlist.get(i).get("ID"), "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < rcxjLlist.size(); i++) {
                        rcxjLlist.get(i).put(rcxjLlist.get(i).get("ID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        Button search = (Button) dialog.findViewById(R.id.swdyx_rcgl_search);
        final EditText swdyx_rcgl_xcbm = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_xcbm);
        final EditText swdyx_rcgl_xcdd = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_xcdd);
        final EditText swdyx_rcgl_bxcdw = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_bxcdw);
        final Button swdyx_rcgl_kssj = (Button) dialog
                .findViewById(R.id.swdyx_rcgl_kssj);
        final Button swdyx_rcgl_jssj = (Button) dialog
                .findViewById(R.id.swdyx_rcgl_jssj);
        swdyx_rcgl_kssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_rcgl_kssj, false);
            }
        });
        swdyx_rcgl_jssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_rcgl_jssj, false);
            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ProgressDialogUtil.startProgressDialog(mContext);
                String xcbm = swdyx_rcgl_xcbm.getText().toString().trim();
                String xcdd = swdyx_rcgl_xcdd.getText().toString().trim();
                String bxcdw = swdyx_rcgl_bxcdw.getText().toString().trim();
                int num = -1;
                String sjss = "";
                if (sjssLlist != null && sjssLlist.size() > 0) {
                    int sjsspo = swdyx_rcgl_sjss.getSelectedItemPosition();
                    if (sjsspo <= 0) {
                        sjss = "";
                    } else {
                        sjss = swdyx_rcgl_sjss.getSelectedItem().toString();
                    }
                }
                String kssj = swdyx_rcgl_kssj.getText().toString().trim();
                String jssj = swdyx_rcgl_jssj.getText().toString().trim();
                String rcgl_sfywfxw = (String) swdyx_rcgl_sfywfxw
                        .getSelectedItem();
                String sfywfxw = "";
                if (swdyx_rcgl_sfywfxw.getSelectedItemId() == 0) {
                    sfywfxw = "";
                } else {
                    sfywfxw = rcgl_sfywfxw;
                }

                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxRcglData(xcbm, xcdd, bxcdw,
                            sjss, kssj, jssj, sfywfxw);
                    Log.i("add", "+result+" + result);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initRcxjSearchData(result, adapter, listView, zwsjTxt);

                } else {
                    ToastUtil.setToast(mContext, "网络未连接");
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /**
     * 日常巡检管理 删除
     */
    protected void rcgldelete(SwdyxRcxjAdapter adapter, ListView listView, TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<>();
        for (int i = 0; i < rcxjLlist.size(); i++) {
            if (BussUtil.isEmperty(rcxjLlist.get(i).get("ID").trim())) {
                if ("true".equals(rcxjLlist.get(i).get(
                        rcxjLlist.get(i).get("ID")))) {
                    buffer.append(rcxjLlist.get(i).get("ID") + ",");
                    flags.add(rcxjLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxRcglData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    rcxjLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (rcxjLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }

    }

    /**
     * 日常巡检管理 添加
     */
    public void showAddDialog(final SwdyxRcxjAdapter adapter,final ListView listView, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_rcxcgl_add);
        dialog.setCanceledOnTouchOutside(false);

        final EditText swdyx_rcgl_xcry = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_xcry);
        final EditText swdyx_rcgl_xcbm = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_xcbm);
        final EditText swdyx_rcgl_xcdd = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_xcdd);
        final Button swdyx_rcgl_xctime = (Button) dialog
                .findViewById(R.id.swdyx_rcgl_xctime);
        final EditText swdyx_rcgl_cdrc = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_cdrc);
        final EditText swdyx_rcgl_cdcls = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_cdcls);
        final EditText swdyx_rcgl_bxcdw = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_bxcdw);
        final EditText swdyx_rcgl_bxcr = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_bxcr);
        final EditText swdyx_rcgl_lon = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_lon);
        final EditText swdyx_rcgl_lat = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_lat);
        final EditText swdyx_rcgl_xcjg = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_xcjg);
        final CheckBox swdyx_rcgl_sfywfxw = (CheckBox) dialog
                .findViewById(R.id.swdyx_rcgl_sfywfxw);
        final EditText swdyx_rcgl_bz = (EditText) dialog
                .findViewById(R.id.swdyx_rcgl_bz);

        String xx = currentPoint.getX() + "";
        swdyx_rcgl_lon.setText(xx);
        String yy = currentPoint.getY() + "";
        swdyx_rcgl_lat.setText(yy);

        swdyx_rcgl_xctime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_rcgl_xctime, false);
            }
        });
        Button save = (Button) dialog.findViewById(R.id.swdyx_rcgl_zxsave);
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_rcgl_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (TextUtils.isEmpty(swdyx_rcgl_xcry.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.xcrynotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_rcgl_xcdd.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.xcddnotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_rcgl_cdrc.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.cdrcnotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_rcgl_xctime.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.xctimenotnull));
                    return;
                }
                String xcry = swdyx_rcgl_xcry.getText().toString().trim();
                String xcbm = swdyx_rcgl_xcbm.getText().toString().trim();
                String xcdd = swdyx_rcgl_xcdd.getText().toString().trim();
                String xctime = swdyx_rcgl_xctime.getText().toString().trim();
                String cdrc = swdyx_rcgl_cdrc.getText().toString().trim();
                String cdcls = swdyx_rcgl_cdcls.getText().toString().trim();
                String bxcdw = swdyx_rcgl_bxcdw.getText().toString().trim();
                String bxcr = swdyx_rcgl_bxcr.getText().toString().trim();
                String lon = swdyx_rcgl_lon.getText().toString().trim();
                String lat = swdyx_rcgl_lat.getText().toString().trim();
                String xcjg = swdyx_rcgl_xcjg.getText().toString().trim();
                String bz = swdyx_rcgl_bz.getText().toString().trim();
                String sfywfxw = "";
                if (swdyx_rcgl_sfywfxw.isChecked()) {
                    sfywfxw = "是";
                } else {
                    sfywfxw = "否";
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String datacode = "guiyangshi";
                if(StartActivity.bsuserbase != null){
                    datacode = StartActivity.bsuserbase.getDATASHARE().trim();
                }
                String result = web.addSwdyxRcglData(xcry, xcbm, xcdd, xctime,
                        cdrc, cdcls, bxcdw, bxcr, lon, lat, xcjg, sfywfxw, bz,
                        datacode);
                        //StartActivity.bsuserbase.getDATASHARE().toString());
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (rcxjLlist.size() == 0) {
                            listView.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<>();
                        map.put("ID", splits[1]);
                        map.put("INSPECTOR", xcry);
                        map.put("INSPECTDEPARTMENT", xcbm);
                        map.put("INSPECTSITE", xcdd);
                        map.put("INSPECTTIME", xctime);
                        map.put("DISPATCHNUM", cdrc);
                        map.put("VEHICLENUM", cdcls);
                        map.put("INSPECTEDUNIT", bxcdw);
                        map.put("PERINSPECTED", bxcr);
                        map.put("LONGITUDE", lon);
                        map.put("LATITUDE", lat);
                        map.put("INSPECTRESULT", xcjg);
                        map.put("REMARK", bz);
                        map.put("ISILLEGAL", sfywfxw);
                        rcxjLlist.add(map);
                        adapter.notifyDataSetChanged();

                        ToastUtil.setToast(mContext, getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 获取日常巡检管理的数据并绑定到listview上 */
    public void initRcxjData() {
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxRcglData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                rcxjLlist = BussUtil.getSwdyxRcxjJsonData(result);
            }
        } else {
            //ToastUtil.setToast(mContext, "网络未连接");
            getLooper(2);
        }
    }

    /* 获取日常巡检管理 搜索 的数据并绑定到listview上 */
    public void initRcxjSearchData(String result, SwdyxRcxjAdapter adapter,
                                   ListView listView, TextView zwsjTxt) {
        rcxjLlist.clear();
        if (result.equals(Webservice.netException)) {
            //ToastUtil.setToast(mContext, Webservice.netException);
            getLooper(0);
        } else if (result.equals("无数据")) {
            //ToastUtil.setToast(mContext, "无数据");
            getLooper(1);
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxRcxjJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                rcxjLlist.add(list.get(i));
            }
            if (rcxjLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 获取动物扰民事件管理数据 */
    public void initDwrmglData() {
//		if (app == null) {
//			ToastUtil.setToast(mContext, "网络未连接");
//			return;
//		}
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxDwrmglData();
            if (result.equals(Webservice.netException)) {
                ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                ToastUtil.setToast(mContext, "无数据");
            } else {
                dwrmLlist = BussUtil.getSwdyxDwrmJsonData(result);
            }
        } else {
            ToastUtil.setToast(mContext, "网络未连接");
        }
    }

    /* 弹出动物扰民管理的页面 */
    public void initdwrmglView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_dwrmgl_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_dwrmgl_zwsj);
        final Spinner swdyx_dwrm_sjss = (Spinner) dialog.findViewById(R.id.swdyx_dwrm_sjss);
        Spinner sfclwb = (Spinner) dialog.findViewById(R.id.swdyx_dwrm_sfclwb);
        final List<String> sjsslist = new ArrayList<String>();
        sjsslist.add("--全部--");
        for (int i = 0; i < sjssLlist.size(); i++) {
            sjsslist.add(sjssLlist.get(i).get("DUNAME"));
        }
        ArrayAdapter sjssadapter = new ArrayAdapter(mContext, R.layout.myspinner, sjsslist);
        swdyx_dwrm_sjss.setAdapter(sjssadapter);
        ArrayAdapter<CharSequence> sfcladapter = ArrayAdapter
                .createFromResource(mContext, R.array.sfwfarray,
                        R.layout.myspinner);
        sfclwb.setAdapter(sfcladapter);

        final ListView listView = (ListView) dialog.findViewById(R.id.swdyx_dwrmgl_listview);
        final SwdyxDwrmAdapter adapter = new SwdyxDwrmAdapter(mContext,
                dwrmLlist);
        if (dwrmLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_dwrm_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        Button add = (Button) dialog.findViewById(R.id.swdyx_dwrm_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                showdwrmAddDialog(adapter, listView, zwsjTxt);
            }
        });
        Button delete = (Button) dialog.findViewById(R.id.swdyx_dwrm_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dwrmdelete(adapter, listView, zwsjTxt);
            }
        });
        CheckBox box = (CheckBox) dialog.findViewById(R.id.cb);
        box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < dwrmLlist.size(); i++) {
                        dwrmLlist.get(i)
                                .put(dwrmLlist.get(i).get("ID"), "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < dwrmLlist.size(); i++) {
                        dwrmLlist.get(i).put(dwrmLlist.get(i).get("ID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        Button search = (Button) dialog.findViewById(R.id.swdyx_dwrm_search);
        final EditText swdyx_dwrm_sjmc = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_sjmc);
        final EditText swdyx_dwrm_rmdw = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_rmdw);
        final Spinner swdyx_dwrm_sfclwb = (Spinner) dialog.findViewById(R.id.swdyx_dwrm_sfclwb);
        final Button swdyx_dwrm_fssj = (Button) dialog
                .findViewById(R.id.swdyx_dwrm_fssj);
        final Button swdyx_dwrm_jssj = (Button) dialog
                .findViewById(R.id.swdyx_dwrm_jssj);
        swdyx_dwrm_fssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_dwrm_fssj, false);
            }
        });
        swdyx_dwrm_jssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_dwrm_jssj, false);
            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                String sjmc = swdyx_dwrm_sjmc.getText().toString().trim();
                String rmdw = swdyx_dwrm_rmdw.getText().toString().trim();
                String kssj = swdyx_dwrm_fssj.getText().toString().trim();
                String jssj = swdyx_dwrm_jssj.getText().toString().trim();
                String sfclwb = "";
                if (swdyx_dwrm_sfclwb.getSelectedItemId() == 0) {
                    sfclwb = "";
                } else {
                    sfclwb = swdyx_dwrm_sfclwb.getSelectedItem().toString();
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                int num = -1;
                String sjss = "";
                if (sjssLlist != null && sjssLlist.size() > 0) {
                    int sjsspo = swdyx_dwrm_sjss.getSelectedItemPosition();
                    if (sjsspo <= 0) {
                        sjss = "";
                    } else {
                        sjss = swdyx_dwrm_sjss.getSelectedItem().toString();
                    }
                }
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxDwrmData(sjmc, rmdw, sfclwb,
                            kssj, jssj, sjss);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initDwrmSearchData(result, adapter, listView, zwsjTxt);
                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }

            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /**
     * 动物扰民 删除
     */
    protected void dwrmdelete(SwdyxDwrmAdapter adapter, ListView listView,
                              TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<>();
        for (int i = 0; i < dwrmLlist.size(); i++) {
            if (BussUtil.isEmperty(dwrmLlist.get(i).get("ID").trim())) {
                if ("true".equals(dwrmLlist.get(i).get(
                        dwrmLlist.get(i).get("ID")))) {
                    buffer.append(dwrmLlist.get(i).get("ID") + ",");
                    flags.add(dwrmLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxDwrmgl(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    dwrmLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (dwrmLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 动物扰民 添加 */
    protected void showdwrmAddDialog(final SwdyxDwrmAdapter adapter, final ListView listView, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_dwrmgl_add);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final Button fssj = (Button) dialog.findViewById(R.id.swdyx_dwrm_fssj);
        fssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(fssj, false);
            }
        });

        final EditText swdyx_dwrm_rmsjmc = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_rmsjmc);

        final EditText swdyx_dwrm_fsdd = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_fsdd);

        final EditText swdyx_dwrm_rmdw = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_rmdw);

        final EditText swdyx_dwrm_brr = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_brr);

        final CheckBox swdyx_dwrm_sfclw = (CheckBox) dialog
                .findViewById(R.id.swdyx_dwrm_sfclw);

        final EditText swdyx_dwrm_zbr = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_zbr);

        final EditText swdyx_dwrm_sjms = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_sjms);

        final EditText swdyx_dwrm_sjcljg = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_sjcljg);

        final EditText swdyx_dwrm_bz = (EditText) dialog
                .findViewById(R.id.swdyx_dwrm_bz);

        Button save = (Button) dialog.findViewById(R.id.swdyx_dwrm_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String name = swdyx_dwrm_rmsjmc.getText().toString().trim();
                if (!BussUtil.isEmperty(name)) {
                    ToastUtil.setToast(mContext, getResources().getString(R.string.troublenamenotnull));
                    return;
                }
                String address = swdyx_dwrm_fsdd.getText().toString().trim();
                if (!BussUtil.isEmperty(address)) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.adressnotnull));
                    return;
                }
                String fstime = fssj.getText().toString().trim();
                if (!BussUtil.isEmperty(fstime)) {
                    ToastUtil.setToast(mContext,
                            getResources()
                                    .getString(R.string.actiontimenotnull));
                    return;
                }
                String rmdw = swdyx_dwrm_rmdw.getText().toString().trim();
                String brr = swdyx_dwrm_brr.getText().toString().trim();
                String sfclw;
                if (swdyx_dwrm_sfclw.isChecked()) {
                    sfclw = "是";
                } else {
                    sfclw = "否";
                }
                String zbr = swdyx_dwrm_zbr.getText().toString().trim();
                String sjms = swdyx_dwrm_sjms.getText().toString().trim();
                String sjcljg = swdyx_dwrm_sjcljg.getText().toString().trim();
                String bz = swdyx_dwrm_bz.getText().toString().trim();
                Webservice web = new Webservice(mContext);
                String datacode = "guiyangshi";
                if(StartActivity.bsuserbase != null){
                    datacode = StartActivity.bsuserbase.getDATASHARE().trim();
                }
                String result = web.addSwdyxDwrmgl(name, address, rmdw, brr,
                        fstime, sfclw, zbr, sjms, sjcljg, bz,
                        datacode);
                        //StartActivity.bsuserbase.getDATASHARE());
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (dwrmLlist.size() == 0) {
                            listView.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("ID", splits[1]);
                        map.put("EVENTNAME", name);
                        map.put("ADDRESS", address);
                        map.put("BIONAME", rmdw);
                        map.put("DISPERSON", brr);
                        map.put("ISFINISHED", sfclw);
                        map.put("TRANSACTOR", zbr);
                        map.put("DESCRIPTION", sjms);
                        map.put("RESULTDESC", sjcljg);
                        map.put("REMARK", bz);
                        map.put("HAPPENTIME", fstime);
                        dwrmLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_dwrm_cancle);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 获取动物扰民 搜索 的数据并绑定到listview上 */
    public void initDwrmSearchData(String result, SwdyxDwrmAdapter adapter,
                                   ListView listView, TextView zwsjTxt) {
        dwrmLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxDwrmJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                dwrmLlist.add(list.get(i));
            }
            if (dwrmLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }

    }

    /* 救护站信息管理界面 */
    public void initJhzView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_jhzgl_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final TextView zwsjTxt = (TextView) dialog.findViewById(R.id.swdyx_jhzgl_zwsj);

        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_jhz_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        final ListView listview = (ListView) dialog
                .findViewById(R.id.swdyx_jhzgl_listview);
        final SwdyxJhzAdapter adapter = new SwdyxJhzAdapter(mContext, mapView,
                graphicsLayer, jhzLlist);
        if (jhzLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.swdyx_jhzgl_cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < jhzLlist.size(); i++) {
                        jhzLlist.get(i).put(jhzLlist.get(i).get("ID"), "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < jhzLlist.size(); i++) {
                        jhzLlist.get(i).put(jhzLlist.get(i).get("ID"), "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        Button delete = (Button) dialog.findViewById(R.id.swdyx_jhzgl_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jhzdelete(adapter, listview, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_jhzgl_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                showJhzAddDialog(adapter, listview, zwsjTxt);
            }
        });
        Button search = (Button) dialog.findViewById(R.id.swdyx_jhzgl_search);
        final EditText swdyx_jhzgl_jzzmc = (EditText) dialog
                .findViewById(R.id.swdyx_jhzgl_jzzmc);
        final EditText swdyx_jhzgl_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_jhzgl_fzr);
        final Spinner swdyx_jhzgl_xzqy = (Spinner) dialog
                .findViewById(R.id.swdyx_jhzgl_xzqy);

        final List<String> xzqy = new ArrayList<String>();
        xzqy.add("--全部--");
        for (int i = 0; i < xzqyLlist.size(); i++) {
            xzqy.add(xzqyLlist.get(i).get("DUNAME"));
        }
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                String jzzmc = swdyx_jhzgl_jzzmc.getText().toString().trim();
                String fzr = swdyx_jhzgl_fzr.getText().toString().trim();
                int num1 = swdyx_jhzgl_xzqy.getSelectedItemPosition();
                String xzqy = "";
                if (num1 > 0) {
                    xzqy = num1 + "";
                } else {
                    xzqy = "";
                }
                if (app == null) {
                    ToastUtil.setToast(mContext, "网络未连接");
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    return;
                }
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxJhzData(jzzmc, fzr, xzqy);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initJhzSearchData(result, adapter, listview, zwsjTxt);
                } else {
                    ToastUtil.setToast(mContext, "网络未连接");
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 1, 1);
    }

    /* 救护站 添加 */
    protected void showJhzAddDialog(final SwdyxJhzAdapter adapter,
                                    final ListView listview, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_jhzgl_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_jhz_head);
        head.setText(mContext.getResources().getString(
                R.string.addsavestationinfo));
        Button save = (Button) dialog.findViewById(R.id.swdyx_jhz_save);
        save.setText(mContext.getResources().getString(R.string.sure));

        final EditText swdyx_jhz_jhzm = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_jhzm);

        final EditText swdyx_jhz_glry = (EditText) dialog
                .findViewById(R.id.swdyx_jhz_glry);

        final Spinner swdyx_jhz_qywz = (Spinner) dialog
                .findViewById(R.id.swdyx_jhz_qywz);

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

        swdyx_jhz_jd.setText(currentPoint.getX() + "");
        swdyx_jhz_wd.setText(currentPoint.getY() + "");
        final List<String> xzqy = new ArrayList<String>();
        xzqy.add("--请选择区域位置--");
        for (int i = 0; i < xzqyLlist.size(); i++) {
            xzqy.add(xzqyLlist.get(i).get("DUNAME"));
        }
        ArrayAdapter xzqyadapter = new ArrayAdapter(mContext,R.layout.myspinner, xzqy);
        swdyx_jhz_qywz.setAdapter(xzqyadapter);
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_jhz_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (TextUtils.isEmpty(swdyx_jhz_jhzm.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.jhzmnotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_jhz_glry.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.glrynotnull));
                    return;
                }
                if (swdyx_jhz_qywz.getSelectedItemPosition() <= 0) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.qywznotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_jhz_wd.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.wdnotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_jhz_jd.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.jdnotnull));
                    return;
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
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxJhzData(jhzm, glry, qywz, xxdz, wd,
                        jd, lxdh, jztj, bz);
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (jhzLlist.size() == 0) {
                            listview.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("ID", splits[1]);
                        map.put("AIDNAME", jhzm);
                        map.put("MANAGER", glry);
                        map.put("AREACODE", qywz);
                        map.put("ADDRESS", xxdz);
                        map.put("LNG", wd);
                        map.put("LAT", jd);
                        map.put("PHONE", lxdh);
                        map.put("CONDITION", jztj);
                        map.put("REMARK", bz);
                        jhzLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);

    }

    /* 获取救护站管理的数据并绑定到listview上 */
    public void initJhzData() {
//		if (app == null) {
//			ToastUtil.setToast(mContext, "网络未连接");
//			return;
//		}
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxJhzData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                jhzLlist = BussUtil.getSwdyxJhzJsonData(result);
            }
        } else {
            //ToastUtil.setToast(mContext, "网络未连接");
            getLooper(2);
        }
    }

    /* 救护站 删除 */
    protected void jhzdelete(SwdyxJhzAdapter adapter, ListView listView,
                             TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < jhzLlist.size(); i++) {
            if (BussUtil.isEmperty(jhzLlist.get(i).get("ID").trim())) {
                if ("true".equals(jhzLlist.get(i)
                        .get(jhzLlist.get(i).get("ID")))) {
                    buffer.append(jhzLlist.get(i).get("ID") + ",");
                    flags.add(jhzLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxJhzData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    jhzLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (jhzLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 获取救护站 搜索 的数据并绑定到listview上 */
    public void initJhzSearchData(String result, SwdyxJhzAdapter adapter,
                                  ListView listView, TextView zwsjTxt) {
        jhzLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxJhzJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                jhzLlist.add(list.get(i));
            }
            if (jhzLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }

    }

    /* 获取驯养繁殖基地的数据并绑定到listview上 */
    public void initXyfzjdData() {
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxXyfzjdData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                xyfzjdLlist = BussUtil.getSwdyxXyfzjdJsonData(result);
            }
        }
    }

    /* 弹出驯养繁殖基地的页面 */
    protected void initXyfzjdView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_xyfzjdgl_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_xyfzjd_zwsj);

        final Spinner swdyx_xyfzjd_jdxz = (Spinner) dialog
                .findViewById(R.id.swdyx_xyfzjd_jdxz);
        final Spinner swdyx_xyfzjd_sjss = (Spinner) dialog
                .findViewById(R.id.swdyx_xyfzjd_sjss);
        final List<String> jdxz = new ArrayList<String>();
        jdxz.add("--全部--");
        for (int i = 0; i < jdxzLlist.size(); i++) {
            jdxz.add(jdxzLlist.get(i).get("DICVAL"));
        }
        ArrayAdapter jdxzadapter = new ArrayAdapter(mContext,R.layout.myspinner, jdxz);
        swdyx_xyfzjd_jdxz.setAdapter(jdxzadapter);

        final List<String> sjss = new ArrayList<String>();
        sjss.add("--全部--");
        for (int i = 0; i < sjssLlist.size(); i++) {
            sjss.add(sjssLlist.get(i).get("DUNAME"));
        }
        ArrayAdapter sjssadapter = new ArrayAdapter(mContext,R.layout.myspinner, sjss);
        swdyx_xyfzjd_sjss.setAdapter(sjssadapter);

        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_xyfzjdgl_listview);
        final SwdyxXyfzjdAdapter adapter = new SwdyxXyfzjdAdapter(mContext,
                mapView, graphicsLayer, xyfzjdLlist);
        if (xyfzjdLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_xyfzjd_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < xyfzjdLlist.size(); i++) {
                        xyfzjdLlist.get(i).put(xyfzjdLlist.get(i).get("ID"),
                                "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < xyfzjdLlist.size(); i++) {
                        xyfzjdLlist.get(i).put(xyfzjdLlist.get(i).get("ID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        Button delete = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xyfzjddelete(adapter, listView, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                showxyfzjdAddDialog(adapter, listView, zwsjTxt);
            }
        });
        final EditText swdyx_xyfzjd_jdmc = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jdmc);
        final EditText swdyx_xyfzjd_jdfzr = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jdfzr);
        final Button swdyx_xyfzjd_zcsj = (Button) dialog
                .findViewById(R.id.swdyx_xyfzjd_zcsj);
        final Button swdyx_xyfzjd_jssj = (Button) dialog
                .findViewById(R.id.swdyx_xyfzjd_jssj);
        swdyx_xyfzjd_zcsj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_xyfzjd_zcsj, false);
            }
        });
        swdyx_xyfzjd_jssj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trajectoryPresenter.initSelectTimePopuwindow(swdyx_xyfzjd_jssj, false);
            }
        });
        Button search = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                ProgressDialogUtil.startProgressDialog(mContext);
                String jdmc = swdyx_xyfzjd_jdmc.getText().toString().trim();
                String jdfzr = swdyx_xyfzjd_jdfzr.getText().toString().trim();
                int num1 = swdyx_xyfzjd_jdxz.getSelectedItemPosition();
                String jdxz = "";
                if (num1 > 0) {
                    jdxz = num1 + "";
                } else {
                    jdxz = "";
                }
                String sjss = "";
                if (sjssLlist != null && sjssLlist.size() > 0) {
                    int sjsspo = swdyx_xyfzjd_sjss.getSelectedItemPosition();
                    if (sjsspo <= 0) {
                        sjss = "";
                    } else {
                        sjss = swdyx_xyfzjd_sjss.getSelectedItem().toString();
                    }
                }
                String zckssj = swdyx_xyfzjd_zcsj.getText().toString().trim();

                String zcjssj = swdyx_xyfzjd_jssj.getText().toString().trim();
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxXyfzjdData(jdmc, jdfzr,jdxz, sjss, zckssj, zcjssj);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initXyfzjdSearchData(result, adapter, listView, zwsjTxt);

                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 1, 1);
    }

    /* 驯养繁殖基地 删除 */
    protected void xyfzjddelete(SwdyxXyfzjdAdapter adapter, ListView listView,TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < xyfzjdLlist.size(); i++) {
            if (BussUtil.isEmperty(xyfzjdLlist.get(i).get("ID").trim())) {
                if ("true".equals(xyfzjdLlist.get(i).get(
                        xyfzjdLlist.get(i).get("ID")))) {
                    buffer.append(xyfzjdLlist.get(i).get("ID") + ",");
                    flags.add(xyfzjdLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).toString();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxXyfzjdData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    xyfzjdLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (xyfzjdLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 驯养繁殖基地 添加 */
    public void showxyfzjdAddDialog(final SwdyxXyfzjdAdapter adapter,
                                    final ListView listView, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_xyfzjdgl_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_xyfzjd_head);
        head.setText(getResources().getString(R.string.addxyfzjdinfo));
        final EditText swdyx_xyfzjd_jdmc = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jdmc);
        final Spinner swdyx_xyfzjd_jdxz = (Spinner) dialog
                .findViewById(R.id.swdyx_xyfzjd_jdxz);
        final List<String> jdxz = new ArrayList<String>();
        jdxz.add("--请选择基地性质--");
        for (int i = 0; i < jdxzLlist.size(); i++) {
            jdxz.add(jdxzLlist.get(i).get("DICVAL"));
        }
        ArrayAdapter jdxzadapter = new ArrayAdapter(mContext,R.layout.myspinner, jdxz);
        swdyx_xyfzjd_jdxz.setAdapter(jdxzadapter);
        final EditText swdyx_xyfzjd_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_fzr);

        final EditText swdyx_xyfzjd_lxfs = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_lxfs);

        final EditText swdyx_xyfzjd_jddz = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jddz);

        final EditText swdyx_xyfzjd_jyfw = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jyfw);

        final EditText swdyx_xyfzjd_jyyt = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jyyt);

        final EditText swdyx_xyfzjd_xwdd = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_xwdd);

        final EditText swdyx_xyfzjd_jbr = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jbr);

        final EditText swdyx_xyfzjd_jd = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_jd);

        final EditText swdyx_xyfzjd_wd = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_wd);

        final EditText swdyx_xyfzjd_bz = (EditText) dialog
                .findViewById(R.id.swdyx_xyfzjd_bz);

        swdyx_xyfzjd_jd.setText(currentPoint.getX() + "");
        swdyx_xyfzjd_wd.setText(currentPoint.getY() + "");

        Button save = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_save);
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_xyfzjd_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (TextUtils.isEmpty(swdyx_xyfzjd_jdmc.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.jdmcnotnull));
                    return;
                }
                if (swdyx_xyfzjd_jdxz.getSelectedItemPosition() <= 0) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.jdxznotnull));
                    return;
                }
                if (TextUtils.isEmpty(swdyx_xyfzjd_jyfw.getText())) {
                    ToastUtil.setToast(mContext,
                            getResources().getString(R.string.jdfwnotnull));
                    return;
                }
                String jdmc = swdyx_xyfzjd_jdmc.getText().toString().trim();
                String jdxz = swdyx_xyfzjd_jdxz.getSelectedItemPosition() + "";
                String fzr = swdyx_xyfzjd_fzr.getText().toString().trim();
                String lxdh = swdyx_xyfzjd_lxfs.getText().toString().trim();
                String dz = swdyx_xyfzjd_jddz.getText().toString().trim();
                String jyfw = swdyx_xyfzjd_jyfw.getText().toString().trim();
                String jyyt = swdyx_xyfzjd_jyyt.getText().toString().trim();
                String xwdd = swdyx_xyfzjd_xwdd.getText().toString().trim();
                String jbr = swdyx_xyfzjd_jbr.getText().toString().trim();
                String jd = swdyx_xyfzjd_jd.getText().toString().trim();
                String wd = swdyx_xyfzjd_wd.getText().toString().trim();
                String bz = swdyx_xyfzjd_bz.getText().toString().trim();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

                String djsj = formatter.format(curDate);

                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxXyfzjdData(jdmc, jdxz, fzr, lxdh,
                        dz, jyfw, jyyt, xwdd, jbr, jd, wd, bz, djsj,
                        StartActivity.bsuserbase.getDATASHARE());
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (xyfzjdLlist.size() == 0) {
                            listView.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("ID", splits[1]);
                        map.put("BASENAME", jdmc);
                        map.put("PROPERTY", jdxz);
                        map.put("MANAGER", fzr);
                        map.put("PHONE", lxdh);
                        map.put("ADDRESS", dz);
                        map.put("BUSISCOPE", jyfw);
                        map.put("BUSIUSE", jyyt);
                        map.put("SELLPLACE", xwdd);
                        map.put("TRANSACTOR", jbr);
                        map.put("LONGITUDE", jd);
                        map.put("LATITUDE", wd);
                        map.put("REMARK", bz);
                        map.put("REGDATE", djsj);
                        xyfzjdLlist.add(map);
                        adapter.notifyDataSetChanged();

                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 获取驯养繁殖基地 搜索 的数据并绑定到listview上 */
    public void initXyfzjdSearchData(String result, SwdyxXyfzjdAdapter adapter,ListView listView, TextView zwsjTxt) {
        xyfzjdLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        }else if (result.equals(Webservice.fwqException)) {
            ToastUtil.setToast(mContext, Webservice.fwqException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil.getSwdyxXyfzjdJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                xyfzjdLlist.add(list.get(i));
            }
            if (xyfzjdLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 获取经营动物单位数据 */
    public void initJydwdwData() {
//		if (app == null) {
//			ToastUtil.setToast(mContext, "网络未连接");
//			return;
//		}
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxJydwdwData();
            if (result.equals(Webservice.netException)) {
                getLooper(0); // 网络异常
            } else if (result.equals("无数据")) {
                getLooper(1); // 无数据
            } else {
                jydwdwLlist = BussUtil.getSwdyxJydwdwJsonData(result);
            }
        } else {
            getLooper(2); // 网络未连接
        }
    }

    /* 弹出经营动物单位页面 */
    public void initJydwdwView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_jydwdwgl_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_jydwdw_zwsj);
        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_jydwdw_listview);
        final SwdyxJydwdwAdapter adapter = new SwdyxJydwdwAdapter(mContext,
                mapView, graphicsLayer, jydwdwLlist);
        if (jydwdwLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < jydwdwLlist.size(); i++) {
                        jydwdwLlist.get(i).put(
                                jydwdwLlist.get(i).get("OBJECTID"), "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < jydwdwLlist.size(); i++) {
                        jydwdwLlist.get(i).put(
                                jydwdwLlist.get(i).get("OBJECTID"), "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ImageView close = (ImageView) dialog
                .findViewById(R.id.swdyx_xyfzjd_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        Button delete = (Button) dialog.findViewById(R.id.swdyx_jydwdw_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jydwdwdelete(adapter, listView, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_jydwdw_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                showJydwdwAddDialog(adapter, listView, zwsjTxt);
            }
        });
        final EditText swdyx_jydwdw_dwfzr = (EditText) dialog
                .findViewById(R.id.swdyx_jydwdw_dwfzr);
        Button search = (Button) dialog.findViewById(R.id.swdyx_jydwdw_search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                String dwfzr = swdyx_jydwdw_dwfzr.getText().toString();

                ProgressDialogUtil.startProgressDialog(mContext);
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxJydwdwData(dwfzr);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initJydwdwSearchData(result, adapter, listView, zwsjTxt);

                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }

            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 经营动物单位 添加 */
    protected void showJydwdwAddDialog(final SwdyxJydwdwAdapter adapter,
                                       final ListView listview, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_jydwdwgl_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_jydwdw_head);
        head.setText(mContext.getResources().getString(R.string.jydwdwadd));
        Button save = (Button) dialog.findViewById(R.id.swdyx_jydwdw_save);
        save.setText(mContext.getResources().getString(R.string.sure));

        final EditText swdyx_jydwdw_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_jydwdw_fzr);

        final EditText swdyx_jydwdw_lxfs = (EditText) dialog
                .findViewById(R.id.swdyx_jydwdw_lxfs);

        final EditText swdyx_jydwdw_jd = (EditText) dialog
                .findViewById(R.id.swdyx_jydwdw_jd);

        final EditText swdyx_jydwdw_wd = (EditText) dialog
                .findViewById(R.id.swdyx_jydwdw_wd);

        final EditText swdyx_jydwdw_dz = (EditText) dialog
                .findViewById(R.id.swdyx_jydwdw_dz);

        swdyx_jydwdw_jd.setText(currentPoint.getX() + "");
        swdyx_jydwdw_wd.setText(currentPoint.getY() + "");
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_jydwdw_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String fzr = swdyx_jydwdw_fzr.getText().toString().trim();
                if (!BussUtil.isEmperty(fzr)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.dwfzrnotnull));
                    return;
                }
                String lxfs = swdyx_jydwdw_lxfs.getText().toString().trim();
                if (!BussUtil.isEmperty(lxfs)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.lxfsnotnull));
                    return;
                }
                String jd = swdyx_jydwdw_jd.getText().toString().trim();
                if (!BussUtil.isEmperty(jd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String wd = swdyx_jydwdw_wd.getText().toString().trim();
                if (!BussUtil.isEmperty(wd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                String dz = swdyx_jydwdw_dz.getText().toString().trim();
                if (!BussUtil.isEmperty(dz)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.dwdznotnull));
                    return;
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxJydwdwglData(fzr, lxfs, jd, wd, dz);
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (jydwdwLlist.size() == 0) {
                            listview.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OBJECTID", splits[1]);
                        map.put("FZR", fzr);
                        map.put("PHONE", lxfs);
                        map.put("X", jd);
                        map.put("Y", wd);
                        map.put("ADDRESS", dz);
                        jydwdwLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);

    }

    /* 获取经营动物单位 搜索 的数据并绑定到listview上 */
    public void initJydwdwSearchData(String result, SwdyxJydwdwAdapter adapter,
                                     ListView listView, TextView zwsjTxt) {
        jydwdwLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxJydwdwJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                jydwdwLlist.add(list.get(i));
            }
            if (jydwdwLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 经营动物单位 删除 */
    protected void jydwdwdelete(SwdyxJydwdwAdapter adapter, ListView listView,
                                TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < jydwdwLlist.size(); i++) {
            if (BussUtil.isEmperty(jydwdwLlist.get(i).get("OBJECTID").trim())) {
                if ("true".equals(jydwdwLlist.get(i).get(
                        jydwdwLlist.get(i).get("OBJECTID")))) {
                    buffer.append(jydwdwLlist.get(i).get("OBJECTID") + ",");
                    flags.add(jydwdwLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxJydwdwData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    jydwdwLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (jydwdwLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 标本馆 删除 */
    protected void bbgdelete(SwdyxBbgAdapter adapter, ListView listView,
                             TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < bbgLlist.size(); i++) {
            if (BussUtil.isEmperty(bbgLlist.get(i).get("OBJECTID").toString())) {
                if ("true".equals(bbgLlist.get(i).get(
                        bbgLlist.get(i).get("OBJECTID")))) {
                    buffer.append(bbgLlist.get(i).get("OBJECTID") + ",");
                    flags.add(bbgLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxBbgData(id);
            Log.i("123", "result" + result);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    bbgLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (bbgLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 获取标本馆数据 */
    public void initBbgData() {
//		if (app == null) {
//			ToastUtil.setToast(mContext, "网络未连接");
//			return;
//		}
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxBbgData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                bbgLlist = BussUtil.getSwdyxBbgJsonData(result);
            }
        } else {
            //ToastUtil.setToast(mContext, "网络未连接");
            getLooper(2);
        }
    }

    /* 弹出 标本馆 界面 并绑定listview */
    protected void initBbgView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_bbg_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_bbg_zwsj);
        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_bbg_listview);
        final SwdyxBbgAdapter adapter = new SwdyxBbgAdapter(mContext, mapView,
                graphicsLayer, bbgLlist);
        if (bbgLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < bbgLlist.size(); i++) {
                        bbgLlist.get(i).put(bbgLlist.get(i).get("OBJECTID"),
                                "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < bbgLlist.size(); i++) {
                        bbgLlist.get(i).put(bbgLlist.get(i).get("OBJECTID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_bbg_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        Button delete = (Button) dialog.findViewById(R.id.swdyx_bbg_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                bbgdelete(adapter, listView, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_bbg_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MyApplication.getInstance().netWorkTip()) {
                    showBbgAddDialog(adapter, listView, zwsjTxt);
                }
            }
        });
        final EditText swdyx_bbg_bbgmc = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_bbgmc);
        final EditText swdyx_bbg_bbgfzr = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_bbgfzr);
        Button search = (Button) dialog.findViewById(R.id.swdyx_bbg_search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                String mc = swdyx_bbg_bbgmc.getText().toString();
                String fzr = swdyx_bbg_bbgfzr.getText().toString();

                ProgressDialogUtil.startProgressDialog(mContext);
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxBbgData(mc, fzr);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initBbgSearchData(result, adapter, listView, zwsjTxt);

                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }

            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 标本馆 添加 */
    protected void showBbgAddDialog(final SwdyxBbgAdapter adapter,final ListView listview, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_bbg_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_bbg_head);
        head.setText(mContext.getResources().getString(R.string.bbgadd));
        Button save = (Button) dialog.findViewById(R.id.swdyx_bbg_save);
        save.setText(mContext.getResources().getString(R.string.sure));

        final EditText swdyx_bbg_bbgmc = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_bbgmc);

        final EditText swdyx_bbg_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_fzr);

        final EditText swdyx_bbg_lxfs = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_lxfs);

        final EditText swdyx_bbg_jd = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_jd);

        final EditText swdyx_bbg_wd = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_wd);

        final EditText swdyx_bbg_dz = (EditText) dialog
                .findViewById(R.id.swdyx_bbg_dz);

        swdyx_bbg_jd.setText(currentPoint.getX() + "");
        swdyx_bbg_wd.setText(currentPoint.getY() + "");
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_bbg_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String mc = swdyx_bbg_bbgmc.getText().toString().trim();
                if (!BussUtil.isEmperty(mc)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.bbgmcnotnull));
                    return;
                }
                String fzr = swdyx_bbg_fzr.getText().toString().trim();
                if (!BussUtil.isEmperty(fzr)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.bbgfzrnotnull));
                    return;
                }
                String lxfs = swdyx_bbg_lxfs.getText().toString().trim();

                String jd = swdyx_bbg_jd.getText().toString().trim();
                if (!BussUtil.isEmperty(jd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String wd = swdyx_bbg_wd.getText().toString().trim();
                if (!BussUtil.isEmperty(wd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                String dz = swdyx_bbg_dz.getText().toString().trim();
                if (!BussUtil.isEmperty(dz)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.bbgdznotnull));
                    return;
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxBbgData(mc, fzr, jd, wd, lxfs, dz);
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (bbgLlist.size() == 0) {
                            listview.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OBJECTID", splits[1]);
                        map.put("BBG", mc);
                        map.put("FZR", fzr);
                        map.put("X", jd);
                        map.put("Y", wd);
                        map.put("PHONE", lxfs);
                        map.put("ADDRESS", dz);
                        bbgLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 获取标本馆 搜索 的数据并绑定到listview上 */
    public void initBbgSearchData(String result, SwdyxBbgAdapter adapter,
                                  ListView listView, TextView zwsjTxt) {
        bbgLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxBbgJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                bbgLlist.add(list.get(i));
            }
            if (bbgLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 获取餐馆数据 */
    public void initCgData() {
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxCgData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                cgLlist = BussUtil.getSwdyxCgJsonData(result);
            }
        }
    }

    /* 弹出  餐馆  界面并绑定listview */
    protected void initCgView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_cg_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_cg_zwsj);
        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_cg_listview);
        final SwdyxCgAdapter adapter = new SwdyxCgAdapter(mContext, mapView,
                graphicsLayer, cgLlist);
        if (cgLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < cgLlist.size(); i++) {
                        cgLlist.get(i).put(cgLlist.get(i).get("OBJECTID"),
                                "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < cgLlist.size(); i++) {
                        cgLlist.get(i).put(cgLlist.get(i).get("OBJECTID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_cg_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        Button delete = (Button) dialog.findViewById(R.id.swdyx_cg_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cgdelete(adapter, listView, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_cg_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MyApplication.getInstance().netWorkTip()) {
                    showCgAddDialog(adapter, listView, zwsjTxt);
                }
            }
        });
        final EditText swdyx_cg_cgmc = (EditText) dialog
                .findViewById(R.id.swdyx_cg_cgmc);
        final EditText swdyx_cg_rjxf = (EditText) dialog
                .findViewById(R.id.swdyx_cg_rjxf);
        Button search = (Button) dialog.findViewById(R.id.swdyx_cg_search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                String mc = swdyx_cg_cgmc.getText().toString();
                String pay = swdyx_cg_rjxf.getText().toString();

                ProgressDialogUtil.startProgressDialog(mContext);
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxCgData(mc, pay);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initCgSearchData(result, adapter, listView, zwsjTxt);
                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 餐馆 删除 */
    protected void cgdelete(SwdyxCgAdapter adapter, ListView listView,
                            TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < cgLlist.size(); i++) {
            if (BussUtil.isEmperty(cgLlist.get(i).get("OBJECTID").trim())) {
                if ("true".equals(cgLlist.get(i).get(
                        cgLlist.get(i).get("OBJECTID")))) {
                    buffer.append(cgLlist.get(i).get("OBJECTID") + ",");
                    flags.add(cgLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxCgData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    cgLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (cgLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 餐馆 添加 */
    protected void showCgAddDialog(final SwdyxCgAdapter adapter,
                                   final ListView listview, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_cg_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_cg_head);
        head.setText(mContext.getResources().getString(R.string.cgadd));
        Button save = (Button) dialog.findViewById(R.id.swdyx_cg_save);
        save.setText(mContext.getResources().getString(R.string.sure));

        final EditText swdyx_cg_cgmc = (EditText) dialog
                .findViewById(R.id.swdyx_cg_cgmc);

        final EditText swdyx_cg_pay = (EditText) dialog
                .findViewById(R.id.swdyx_cg_pay);

        final EditText swdyx_cg_jd = (EditText) dialog
                .findViewById(R.id.swdyx_cg_jd);

        final EditText swdyx_cg_wd = (EditText) dialog
                .findViewById(R.id.swdyx_cg_wd);

        final EditText swdyx_cg_dz = (EditText) dialog
                .findViewById(R.id.swdyx_cg_dz);

        swdyx_cg_jd.setText(currentPoint.getX() + "");
        swdyx_cg_wd.setText(currentPoint.getY() + "");
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_cg_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String mc = swdyx_cg_cgmc.getText().toString().trim();
                if (!BussUtil.isEmperty(mc)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.cgmcnotnull));
                    return;
                }
                String jd = swdyx_cg_jd.getText().toString().trim();
                if (!BussUtil.isEmperty(jd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String wd = swdyx_cg_wd.getText().toString().trim();
                if (!BussUtil.isEmperty(wd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                String dz = swdyx_cg_dz.getText().toString().trim();
                if (!BussUtil.isEmperty(dz)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.cgdznotnull));
                    return;
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                String pay = swdyx_cg_pay.getText().toString().trim();
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxCgData(mc, jd, wd, dz, pay);
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (cgLlist.size() == 0) {
                            listview.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OBJECTID", splits[1]);
                        map.put("NAME", mc);
                        map.put("X", jd);
                        map.put("Y", wd);
                        map.put("ADDRESS", dz);
                        map.put("PAY", pay);
                        cgLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);

    }

    /* 获取餐馆 搜索 的数据并绑定到listview上 */
    public void initCgSearchData(String result, SwdyxCgAdapter adapter,
                                 ListView listView, TextView zwsjTxt) {
        cgLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxCgJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                cgLlist.add(list.get(i));
            }
            if (cgLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 获取疫源疫病监测站数据 */
    public void initYyybjczData() {
//		if (app == null) {
//			ToastUtil.setToast(mContext, "网络未连接");
//			return;
//		}
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxYyybjczData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                jczLlist = BussUtil.getSwdyxYyybjczJsonData(result);
            }
        }
    }

    /* 弹出 疫源疫病监测站 界面 并绑定listview */
    protected void initYyybjczView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_yyybjcz_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_jcz_zwsj);
        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_jcz_listview);
        final SwdyxJczAdapter adapter = new SwdyxJczAdapter(mContext, mapView,
                graphicsLayer, jczLlist);
        if (jczLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < jczLlist.size(); i++) {
                        jczLlist.get(i).put(jczLlist.get(i).get("OBJECTID"),
                                "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < jczLlist.size(); i++) {
                        jczLlist.get(i).put(jczLlist.get(i).get("OBJECTID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_jcz_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        Button delete = (Button) dialog.findViewById(R.id.swdyx_jcz_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jczdelete(adapter, listView, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_jcz_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MyApplication.getInstance().netWorkTip()) {
                    showJczAddDialog(adapter, listView, zwsjTxt);
                }
            }
        });
        final EditText swdyx_jcz_mc = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_mc);
        final EditText swdyx_jcz_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_fzr);
        Button search = (Button) dialog.findViewById(R.id.swdyx_jcz_search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mc = swdyx_jcz_mc.getText().toString();
                String fzr = swdyx_jcz_fzr.getText().toString();

                ProgressDialogUtil.startProgressDialog(mContext);
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxYyybjczData(mc, fzr);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initJczSearchData(result, adapter, listView, zwsjTxt);

                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }

            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /*疫源疫病监测站  删除*/
    protected void jczdelete(SwdyxJczAdapter adapter, ListView listView,
                             TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < jczLlist.size(); i++) {
            if (BussUtil.isEmperty(jczLlist.get(i).get("OBJECTID").trim())) {
                if ("true".equals(jczLlist.get(i).get(
                        jczLlist.get(i).get("OBJECTID")))) {
                    buffer.append(jczLlist.get(i).get("OBJECTID") + ",");
                    flags.add(jczLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxYyybjczData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    jczLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (jczLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 疫源疫病监测站 添加 */
    protected void showJczAddDialog(final SwdyxJczAdapter adapter,
                                    final ListView listview, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_yyybjcz_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_jcz_head);
        head.setText(mContext.getResources().getString(R.string.jczadd));
        Button save = (Button) dialog.findViewById(R.id.swdyx_jcz_save);
        save.setText(mContext.getResources().getString(R.string.sure));

        final EditText swdyx_jcz_mc = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_mc);

        final EditText swdyx_jcz_fzr = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_fzr);

        final EditText swdyx_jcz_jd = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_jd);

        final EditText swdyx_jcz_wd = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_wd);

        final EditText swdyx_jcz_dz = (EditText) dialog
                .findViewById(R.id.swdyx_jcz_dz);

        swdyx_jcz_jd.setText(currentPoint.getX() + "");
        swdyx_jcz_wd.setText(currentPoint.getY() + "");
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_jcz_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String mc = swdyx_jcz_mc.getText().toString().trim();
                if (!BussUtil.isEmperty(mc)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jcznamenotnull));
                    return;
                }
                String fzr = swdyx_jcz_fzr.getText().toString().trim();
                if (!BussUtil.isEmperty(fzr)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jczfzrnotnull));
                    return;
                }

                String jd = swdyx_jcz_jd.getText().toString().trim();
                if (!BussUtil.isEmperty(jd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String wd = swdyx_jcz_wd.getText().toString().trim();
                if (!BussUtil.isEmperty(wd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                String dz = swdyx_jcz_dz.getText().toString().trim();
                if (!BussUtil.isEmperty(dz)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jczdznotnull));
                    return;
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxYyybjczData(jd, wd, dz, mc, fzr);
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (jczLlist.size() == 0) {
                            listview.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OBJECTID", splits[1]);
                        map.put("X", jd);
                        map.put("Y", wd);
                        map.put("ADDRESS", dz);
                        map.put("NAME", mc);
                        map.put("FZR", fzr);
                        jczLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);

    }

    /* 获取标本馆 疫源疫病监测站 的数据并绑定到listview上 */
    public void initJczSearchData(String result, SwdyxJczAdapter adapter,
                                  ListView listView, TextView zwsjTxt) {
        jczLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxYyybjczJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                jczLlist.add(list.get(i));
            }
            if (jczLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    /* 弹出 交易市场 界面 并绑定listview */
    protected void initJyscView() {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.swdyx_jyscgl_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final TextView zwsjTxt = (TextView) dialog
                .findViewById(R.id.swdyx_jysc_zwsj);
        final ListView listView = (ListView) dialog
                .findViewById(R.id.swdyx_jysc_listview);
        final SwdyxJyscAdapter adapter = new SwdyxJyscAdapter(mContext, mapView,
                graphicsLayer, jyscLlist);
        if (jyscLlist.size() > 0) {
            zwsjTxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        } else {
            zwsjTxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        CheckBox cb = (CheckBox) dialog.findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    for (int i = 0; i < jyscLlist.size(); i++) {
                        jyscLlist.get(i).put(jyscLlist.get(i).get("OBJECTID"),
                                "true");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < jyscLlist.size(); i++) {
                        jyscLlist.get(i).put(jyscLlist.get(i).get("OBJECTID"),
                                "false");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.swdyx_jysc_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        Button delete = (Button) dialog.findViewById(R.id.swdyx_jysc_delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jyscdelete(adapter, listView, zwsjTxt);
            }
        });
        Button add = (Button) dialog.findViewById(R.id.swdyx_jysc_add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                showJyscAddDialog(adapter, listView, zwsjTxt);
            }
        });
        final EditText swdyx_jysc_mc = (EditText) dialog
                .findViewById(R.id.swdyx_jysc_mc);
        Button search = (Button) dialog.findViewById(R.id.swdyx_jysc_search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mc = swdyx_jysc_mc.getText().toString();

                ProgressDialogUtil.startProgressDialog(mContext);
                if (MyApplication.getInstance().netWorkTip()) {
                    Webservice web = new Webservice(mContext);
                    String result = web.searchSwdyxJyscData(mc);
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    initJyscSearchData(result, adapter, listView, zwsjTxt);

                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }

            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);
    }

    /* 获取交易市场数据 */
    public void initJyscData() {
//		if (app == null) {
//			ToastUtil.setToast(mContext, "网络未连接");
//			return;
//		}
        if (MyApplication.getInstance().netWorkTip()) {
            Webservice webservice = new Webservice(mContext);
            String result = webservice.getSwdyxJyscData();
            if (result.equals(Webservice.netException)) {
                //ToastUtil.setToast(mContext, Webservice.netException);
                getLooper(0);
            } else if (result.equals("无数据")) {
                //ToastUtil.setToast(mContext, "无数据");
                getLooper(1);
            } else {
                jyscLlist = BussUtil.getSwdyxJyscJsonData(result);
            }
        }
    }

    /* 交易市场 删除 */
    protected void jyscdelete(SwdyxJyscAdapter adapter, ListView listView,
                              TextView zwsjTxt) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<HashMap<String, String>> flags = new ArrayList<>();
        for (int i = 0; i < jyscLlist.size(); i++) {
            if (BussUtil.isEmperty(jyscLlist.get(i).get("OBJECTID").trim())) {
                if ("true".equals(jyscLlist.get(i).get(
                        jyscLlist.get(i).get("OBJECTID")))) {
                    buffer.append(jyscLlist.get(i).get("OBJECTID") + ",");
                    flags.add(jyscLlist.get(i));
                }
            }
        }
        if (buffer.length() > 0) {
            String id = buffer.substring(0, buffer.length() - 1).trim();
            Webservice webservice = new Webservice(mContext);
            String result = webservice.deleteSwdyxJyscData(id);
            if ("true".equals(result)) {
                for (int i = 0; i < flags.size(); i++) {
                    jyscLlist.remove(flags.get(i));
                }
                adapter.notifyDataSetChanged();
                if (jyscLlist.size() == 0) {
                    listView.setVisibility(View.GONE);
                    zwsjTxt.setVisibility(View.VISIBLE);
                }
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletesuccess));
            } else {
                ToastUtil.setToast(mContext,
                        getResources().getString(R.string.deletefailed));
            }
        }
    }

    /* 交易市场 添加 */
    protected void showJyscAddDialog(final SwdyxJyscAdapter adapter,
                                     final ListView listview, final TextView zwsjTxt) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setContentView(R.layout.swdyx_jysc_edit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView head = (TextView) dialog.findViewById(R.id.swdyx_jysc_head);
        head.setText(mContext.getResources().getString(R.string.addjyscxx));
        Button save = (Button) dialog.findViewById(R.id.swdyx_jysc_save);
        save.setText(mContext.getResources().getString(R.string.sure));

        final EditText swdyx_jysc_mc = (EditText) dialog
                .findViewById(R.id.swdyx_jysc_mc);

        final EditText swdyx_jysc_jd = (EditText) dialog
                .findViewById(R.id.swdyx_jysc_jd);

        final EditText swdyx_jysc_wd = (EditText) dialog
                .findViewById(R.id.swdyx_jysc_wd);

        final EditText swdyx_jysc_dz = (EditText) dialog
                .findViewById(R.id.swdyx_jysc_dz);

        swdyx_jysc_jd.setText(currentPoint.getX() + "");
        swdyx_jysc_wd.setText(currentPoint.getY() + "");
        Button cancle = (Button) dialog.findViewById(R.id.swdyx_jysc_cancle);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String mc = swdyx_jysc_mc.getText().toString().trim();
                if (!BussUtil.isEmperty(mc)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jyscnamenotnull));
                    return;
                }
                String jd = swdyx_jysc_jd.getText().toString().trim();
                if (!BussUtil.isEmperty(jd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jdnotnull));
                    return;
                }
                String wd = swdyx_jysc_wd.getText().toString().trim();
                if (!BussUtil.isEmperty(wd)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.wdnotnull));
                    return;
                }
                String dz = swdyx_jysc_dz.getText().toString().trim();
                if (!BussUtil.isEmperty(dz)) {
                    ToastUtil.setToast(mContext, mContext.getResources()
                            .getString(R.string.jyscdznotnull));
                    return;
                }
                if (!MyApplication.getInstance().netWorkTip()) {
                    return;
                }
                Webservice web = new Webservice(mContext);
                String result = web.addSwdyxJyscData(jd, wd, dz, mc);
                String[] splits = result.split(",");
                if (splits.length > 0) {
                    if ("True".equals(splits[0])) {
                        if (jyscLlist.size() == 0) {
                            listview.setVisibility(View.VISIBLE);
                            zwsjTxt.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OBJECTID", splits[1]);
                        map.put("NAME", mc);
                        map.put("X", jd);
                        map.put("Y", wd);
                        map.put("ADDRESS", dz);
                        jyscLlist.add(map);
                        adapter.notifyDataSetChanged();
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addsuccess));
                        dialog.dismiss();
                    } else {
                        ToastUtil.setToast(mContext,
                                getResources().getString(R.string.addfailed));
                    }
                }

            }
        });
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        BussUtil.setDialogParams(mContext, dialog, 0.85, 0.9);

    }

    /* 获取交易市场 搜索 的数据并绑定到listview上 */
    public void initJyscSearchData(String result, SwdyxJyscAdapter adapter,ListView listView, TextView zwsjTxt) {
        jyscLlist.clear();
        if (result.equals(Webservice.netException)) {
            ToastUtil.setToast(mContext, Webservice.netException);
        } else if (result.equals("无数据")) {
            ToastUtil.setToast(mContext, "无数据");
        } else {
            List<HashMap<String, String>> list = BussUtil
                    .getSwdyxJyscJsonData(result);
            for (int i = 0; i < list.size(); i++) {
                jyscLlist.add(list.get(i));
            }
            if (jyscLlist.size() == 0) {
                listView.setVisibility(View.GONE);
                zwsjTxt.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                zwsjTxt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        }
    }

    private void getLooper(int i) {
        // 获取主线程looper
        Looper looper = Looper.getMainLooper();
        // 构造handler使之可与looper通信
        MyHandler mHandler =new MyHandler(looper);

        Message m = new Message();
        m.what = i;
        mHandler.sendMessage(m);
    }

    private class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: // 网络异常
                    ToastUtil.setToast(mContext, Webservice.netException);
                    break;
                case 1: // 无数据
                    ToastUtil.setToast(mContext, "无数据");
                    break;
                case 2: // 网络未连接
                    ToastUtil.setToast(mContext, "网络未连接");
                    break;
            }
        }
    }
}