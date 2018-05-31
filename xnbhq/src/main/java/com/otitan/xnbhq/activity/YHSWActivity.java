package com.otitan.xnbhq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.YhswLxtcAdapter;
import com.otitan.xnbhq.adapter.YhswMcbchdcAdapter;
import com.otitan.xnbhq.adapter.YhswScxcbpcAdapter;
import com.otitan.xnbhq.adapter.YhswTcdAdapter;
import com.otitan.xnbhq.adapter.YhswYcddcAdapter;
import com.otitan.xnbhq.adapter.YhswYdbhdcAdapter;
import com.otitan.xnbhq.adapter.YhswYdchdcAdapter;
import com.otitan.xnbhq.adapter.YhswYhzwdcAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.YhswLxtcDialog;
import com.otitan.xnbhq.dialog.YhswMcbchdcAddDialog;
import com.otitan.xnbhq.dialog.YhswScxcbpcAddDialog;
import com.otitan.xnbhq.dialog.YhswTcdDialog;
import com.otitan.xnbhq.dialog.YhswYcddcAddDialog;
import com.otitan.xnbhq.dialog.YhswYdbhdcAddDialog;
import com.otitan.xnbhq.dialog.YhswYdchdcAddDialog;
import com.otitan.xnbhq.dialog.YhswYhzwdcAddDialog;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.UtilTime;
import com.titan.baselibrary.customview.MorePopWindow;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by li on 2016/5/26.
 * 有害生物页面
 */
public class YHSWActivity extends BaseActivity {

	/* 执行progressbar */
	private int REQUEST_CODE = 0;
	private int REQUEST_IMAGE = 1;
	private static final int PROGRESSDIALOG = 1;
	private static final int STOPPROGRESS = 2;
	View parentview, bottomview, caozuoview;
	public TextView tcqd, qxtc, zjtcd, jstc, yhswtc, ydchdc, ydbhdc, yhzwdc,
			mcbchdc, ycddc,scxcbpc;
	// 判断是不是踏查开始
	boolean tcks = false;
	// 路线踏查ID
	public String lxtcid;
	MorePopWindow PopWindow;
	// 判断是什么位置的popWindow
	String poptype;
	/* 踏查点 数据list */
	List<HashMap<String, String>> tcdLlist = new ArrayList<>();
	/* 踏查路线 数据list */
	List<HashMap<String, String>> tclxLlist = new ArrayList<>();
	/* 样地虫害调查 数据list */
	List<HashMap<String, String>> ydchLlist = new ArrayList<>();
	/* 样地虫害调查详查 数据list */
	List<HashMap<String, String>> ydchxcLlist = new ArrayList<>();
	/* 样地虫害调查 数据list */
	List<HashMap<String, String>> ydbhLlist = new ArrayList<>();
	/* 样地病害调查详查 数据list */
	List<HashMap<String, String>> ydbhxcLlist = new ArrayList<>();
	/* 有害植物调查 数据list */
	List<HashMap<String, String>> yhzwLlist = new ArrayList<>();
	/* 木材病虫害调查 数据list */
	List<HashMap<String, String>> mcbchLlist = new ArrayList<>();
	/* 诱虫灯调查 数据list */
	List<HashMap<String, String>> ycdLlist = new ArrayList<>();
	/* 诱虫灯调查 数据list */
	List<HashMap<String, String>> scxcbLlist = new ArrayList<>();

	Context mContext;

	@SuppressLint({ "InflateParams", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentview = getLayoutInflater().inflate(R.layout.activity_yhsw, null);
		super.onCreate(savedInstanceState);
		setContentView(parentview);

		mContext = YHSWActivity.this;
		ImageView topview = (ImageView) parentview.findViewById(R.id.topview);
		topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_yhsw));

		ProgressDialogUtil.startProgressDialog(mContext);
		stopProgress();
		activitytype = getIntent().getStringExtra("name");

		proData = BussUtil.getConfigXml(mContext,activitytype);

		initview();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			/* 执行progressdialog */
				case PROGRESSDIALOG:
					ProgressDialogUtil.startProgressDialog(mContext);
					break;
			/* 停止 */
				case STOPPROGRESS:
				/* 设置进度条位置 */
					ProgressDialogUtil.stopProgressDialog(mContext);
					break;
			}
		};
	};

	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// 有害生物踏查
			if (params[0].equals("yhsw_tcd")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initTcdData();
						initTcdView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				// 有害生物踏查路线
			} else if (params[0].equals("yhsw_tclx")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initTclxData();
						initTclxView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				// 样地虫害调查
			}else if (params[0].equals("yhsw_ydchdc")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initYdchdcData();
						initYdchdcView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				// 样地病害调查
			} else if (params[0].equals("yhsw_ydbhdc")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initYdbhdcData();
						initYdbhdcView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				// 有害植物调查
			} else if (params[0].equals("yhsw_yhzwdc")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initYhzwdcData();
						initYhzwdcView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				// 木材病虫害调查
			} else if (params[0].equals("yhsw_mcbchdc")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initMcbchdcData();
						initMcbchdcView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				// 诱虫灯调查
			} else if (params[0].equals("yhsw_ycddc")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initYcddcData();
						initYcddcView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
				//松材线虫病普查
			}else if (params[0].equals("yhsw_scxcbpc")) {
				runOnUiThread(new Runnable() {
					public void run() {
						initScxcbpcData();
						initScxcbpcView();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}
			return null;
		}

	}
	/**弹出有害生物 踏查路线管理界面*/
	protected void initTclxView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_lxtc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog
				.findViewById(R.id.yhsw_lxtc_zwsj);
		final ListView listView = (ListView) dialog
				.findViewById(R.id.yhsw_lxtc_listview);
		final YhswLxtcAdapter adapter = new YhswLxtcAdapter(mContext,
				tclxLlist);
		if (tclxLlist.size() > 0) {
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
					for (int i = 0; i < tclxLlist.size(); i++) {
						tclxLlist.get(i).put(tclxLlist.get(i).get("ID"),
								"true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < tclxLlist.size(); i++) {
						tclxLlist.get(i).put(tclxLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog
				.findViewById(R.id.yhsw_lxtc_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_lxtc_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				lxtcdelete(adapter, listView, zwsjTxt);
			}
		});

		final EditText yhsw_tclx_jzmc=(EditText) dialog.findViewById(R.id.yhsw_lxtc_jzmc);
		final Spinner yhsw_tclx_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_tclx_sfsb);
		ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		yhsw_tclx_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_lxtc_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tclxsearch(dialog,adapter,listView,zwsjTxt,yhsw_tclx_jzmc,yhsw_tclx_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 1, 1);
	}
	/**踏查路线 搜索*/
	protected void tclxsearch(Dialog dialog, YhswLxtcAdapter adapter,
							  ListView listView, TextView zwsjTxt, EditText yhsw_lxtc_jzmc, Spinner yhsw_tclx_sfsb) {
		final String jzmc=yhsw_lxtc_jzmc.getText().toString();
		final String sbzt1=yhsw_tclx_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		tclxLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswTclxData(jzmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswTclxJsonData(result);
				for(int i=0;i<list.size();i++){
					tclxLlist.add(list.get(i));
				}
			}
		}
		List<HashMap<String, String>> result = DataBaseHelper.searchYhswTclxData(mContext, "db.sqlite",jzmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			tclxLlist.add(result.get(i));
		}
		if (tclxLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}

	}
	/**有害生物路线踏查 删除*/
	protected void lxtcdelete(YhswLxtcAdapter adapter, ListView listView,
							  TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < tclxLlist.size(); i++) {
			if (BussUtil.isEmperty(tclxLlist.get(i).get("ID").trim())) {
				// 本地数据
				if ("0".equals(tclxLlist.get(i).get("UPLOADSTATUS"))) {
					if ("true".equals(tclxLlist.get(i).get(
							tclxLlist.get(i).get("ID")))) {
						bufferbd.append(tclxLlist.get(i).get("ID") + ",");
						flags.add(tclxLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(tclxLlist.get(i).get("UPLOADSTATUS"))) {
					if ("true".equals(tclxLlist.get(i).get(
							tclxLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswTclxData(mContext, "db.sqlite",
						split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			tclxLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (tclxLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}
	}
	/**弹出有害生物 松材线虫病管理界面*/
	protected void initScxcbpcView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_scxcbpc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog
				.findViewById(R.id.yhsw_scxcb_zwsj);
		final ListView listView = (ListView) dialog
				.findViewById(R.id.yhsw_scxcb_listview);
		final YhswScxcbpcAdapter adapter = new YhswScxcbpcAdapter(mContext,
				scxcbLlist);
		if (scxcbLlist.size() > 0) {
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
					for (int i = 0; i < scxcbLlist.size(); i++) {
						scxcbLlist.get(i).put(scxcbLlist.get(i).get("ID"),
								"true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < scxcbLlist.size(); i++) {
						scxcbLlist.get(i).put(scxcbLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog
				.findViewById(R.id.yhsw_scxcb_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_scxcb_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				scxcbpcdelete(adapter, listView, zwsjTxt);
			}
		});

		final EditText yhsw_scxcb_jzmc=(EditText) dialog.findViewById(R.id.yhsw_scxcb_jzmc);
		final Spinner yhsw_scxcb_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_scxcb_sfsb);
		ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		yhsw_scxcb_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_scxcb_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				scxcbsearch(dialog,adapter,listView,zwsjTxt,yhsw_scxcb_jzmc,yhsw_scxcb_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}
	/**松材线虫病 搜索*/
	protected void scxcbsearch(Dialog dialog, YhswScxcbpcAdapter adapter,
							   ListView listView, TextView zwsjTxt, EditText yhsw_scxcb_jzmc,
							   Spinner yhsw_scxcb_sfsb) {
		final String jzmc=yhsw_scxcb_jzmc.getText().toString();
		final String sbzt1=yhsw_scxcb_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		scxcbLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswScxcbData(jzmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswScxcbpcJsonData(result);
				for(int i=0;i<list.size();i++){
					scxcbLlist.add(list.get(i));
				}
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper.searchYhswScxcbData(
				mContext, "db.sqlite",jzmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			scxcbLlist.add(result.get(i));
		}
		if (scxcbLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}
	/**松材线虫病普查信息 删除*/
	protected void scxcbpcdelete(YhswScxcbpcAdapter adapter, ListView listView,
								 TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < scxcbLlist.size(); i++) {
			if (BussUtil.isEmperty(scxcbLlist.get(i).get("ID").trim())) {
				// 本地数据
				if ("0".equals(scxcbLlist.get(i).get("SCZT"))) {
					if ("true".equals(scxcbLlist.get(i).get(
							scxcbLlist.get(i).get("ID")))) {
						bufferbd.append(scxcbLlist.get(i).get("ID") + ",");
						flags.add(scxcbLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(scxcbLlist.get(i).get("SCZT"))) {
					if ("true".equals(scxcbLlist.get(i).get(
							scxcbLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswScxcbpcData(mContext, "db.sqlite",
						split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			scxcbLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (scxcbLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}

	}
	/** 弹出有害生物 诱虫灯管理界面 */
	protected void initYcddcView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_ycddc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog
				.findViewById(R.id.yhsw_ycddc_zwsj);
		final ListView listView = (ListView) dialog
				.findViewById(R.id.yhsw_ycddc_listview);
		final YhswYcddcAdapter adapter = new YhswYcddcAdapter(mContext,
				ycdLlist);
		if (ycdLlist.size() > 0) {
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
					for (int i = 0; i < ycdLlist.size(); i++) {
						ycdLlist.get(i).put(ycdLlist.get(i).get("ID"),
								"true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < ycdLlist.size(); i++) {
						ycdLlist.get(i).put(ycdLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog
				.findViewById(R.id.yhsw_ycddc_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_ycddc_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ycddcdelete(adapter, listView, zwsjTxt);
			}
		});

		final EditText yhsw_ycddc_ycdmc=(EditText) dialog.findViewById(R.id.yhsw_ycddc_ycdmc);
		final Spinner yhsw_ycddc_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_ycddc_sfsb);
		ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		yhsw_ycddc_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_ycddc_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ycddcsearch(dialog,adapter,listView,zwsjTxt,yhsw_ycddc_ycdmc,yhsw_ycddc_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	/**诱虫灯 搜索*/
	protected void ycddcsearch(Dialog dialog, YhswYcddcAdapter adapter,
							   ListView listView, TextView zwsjTxt, EditText yhsw_ycddc_ycdmc,
							   Spinner yhsw_ycddc_sfsb) {
		final String ycdmc=yhsw_ycddc_ycdmc.getText().toString();
		final String sbzt1=yhsw_ycddc_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		ycdLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswYcdData(ycdmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswYcddcJsonData(result);
				for(int i=0;i<list.size();i++){
					ycdLlist.add(list.get(i));
				}
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper.searchYhswYcdData(
				mContext, "db.sqlite",ycdmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			ycdLlist.add(result.get(i));
		}
		if (ycdLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}
	/**获取松材线虫病普查数据*/
	public void initScxcbpcData() {
		scxcbLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswSxcbpcData();// 获取在线数据
			Log.i("yyy", "result++"+result);
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				scxcbLlist = BussUtil.getYhswScxcbpcJsonData(result);
			}

		} else {
			ToastUtil.setToast(mContext, "网络未连接");
		}
		List<HashMap<String, String>> resultbd = DataBaseHelper
				.getYhswScxcbpcData(mContext, "db.sqlite");// 获取本地数据
		for (int i = 0; i < resultbd.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = resultbd.get(i);
			scxcbLlist.add(map);
		}
	}

	/**诱虫灯调查 删除*/
	protected void ycddcdelete(YhswYcddcAdapter adapter, ListView listView, TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < ycdLlist.size(); i++) {
			if (BussUtil.isEmperty(ycdLlist.get(i).get("ID").trim())) {
				// 本地数据
				if ("0".equals(ycdLlist.get(i).get("SCZT"))) {
					if ("true".equals(ycdLlist.get(i).get(ycdLlist.get(i).get("ID")))) {
						bufferbd.append(ycdLlist.get(i).get("ID") + ",");
						flags.add(ycdLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(ycdLlist.get(i).get("SCZT"))) {
					if ("true".equals(ycdLlist.get(i).get(ycdLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswYcddcData(mContext, "db.sqlite",split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			ycdLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (ycdLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}
	}

	/* 弹出有害生物 木材病虫害管理界面 */
	protected void initMcbchdcView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_mcbchdc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog
				.findViewById(R.id.yhsw_mcbchdc_zwsj);
		final ListView listView = (ListView) dialog
				.findViewById(R.id.yhsw_mcbchdc_listview);
		final YhswMcbchdcAdapter adapter = new YhswMcbchdcAdapter(mContext,
				mcbchLlist);
		if (mcbchLlist.size() > 0) {
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
					for (int i = 0; i < mcbchLlist.size(); i++) {
						mcbchLlist.get(i).put(mcbchLlist.get(i).get("ID"),
								"true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < mcbchLlist.size(); i++) {
						mcbchLlist.get(i).put(mcbchLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog
				.findViewById(R.id.yhsw_mcbchdc_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_mcbchdc_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mcbchdcdelete(adapter, listView, zwsjTxt);
			}
		});

		final Spinner yhsw_mcbchdc_whcd=(Spinner) dialog.findViewById(R.id.yhsw_mcbchdc_whcd);
		final Spinner yhsw_mcbchdc_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_mcbchdc_sfsb);
		ArrayAdapter whcdadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.whcd));
		ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		yhsw_mcbchdc_whcd.setAdapter(whcdadapter);
		yhsw_mcbchdc_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_mcbchdc_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mcbxhdcsearch(dialog,adapter,listView,zwsjTxt,yhsw_mcbchdc_whcd,yhsw_mcbchdc_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	/**木材病虫害调查 搜索*/
	protected void mcbxhdcsearch(Dialog dialog, YhswMcbchdcAdapter adapter,
								 ListView listView, TextView zwsjTxt, Spinner yhsw_mcbchdc_whcd,
								 Spinner yhsw_mcbchdc_sfsb) {
		final String whcd1=yhsw_mcbchdc_whcd.getSelectedItemPosition()+"";
		final String sbzt1=yhsw_mcbchdc_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}
		String whcd="";
		if("1".equals(whcd1)){
			whcd="0";
		}else if("2".equals(whcd1)){
			whcd="1";
		}else if("3".equals(whcd1)){
			whcd="2";
		}else if("4".equals(whcd1)){
			whcd="3";
		}
		mcbchLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswMcbchData(whcd, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswMcbchdcJsonData(result);
				for(int i=0;i<list.size();i++){
					mcbchLlist.add(list.get(i));
				}
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper.searchYhswMcbchData(
				mContext, "db.sqlite",whcd,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			mcbchLlist.add(result.get(i));
		}
		if (mcbchLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}
	/** 获取诱虫灯调查数据 */
	public void initYcddcData() {
		ycdLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswYcddcData();// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				ycdLlist = BussUtil.getYhswYcddcJsonData(result);
			}

		}
		List<HashMap<String, String>> resultbd = DataBaseHelper
				.getYhswYcddcData(mContext, "db.sqlite");// 获取本地数据
		for (int i = 0; i < resultbd.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = resultbd.get(i);
			ycdLlist.add(map);
		}
	}

	/** 木材病虫害 删除 */
	protected void mcbchdcdelete(YhswMcbchdcAdapter adapter, ListView listView,
								 TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < mcbchLlist.size(); i++) {
			if (BussUtil.isEmperty(mcbchLlist.get(i).get("ID").toString())) {
				// 本地数据
				if ("0".equals(mcbchLlist.get(i).get("SCZT"))) {
					if ("true".equals(mcbchLlist.get(i).get(
							mcbchLlist.get(i).get("ID")))) {
						bufferbd.append(mcbchLlist.get(i).get("ID") + ",");
						flags.add(mcbchLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(mcbchLlist.get(i).get("SCZT"))) {
					if ("true".equals(mcbchLlist.get(i).get(
							mcbchLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1)
					.toString();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswMcbchdcData(mContext, "db.sqlite",
						split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			mcbchLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (mcbchLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}
	}

	/** 弹出有害生物 有害植物管理界面 */
	protected void initYhzwdcView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_yhzwdc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog
				.findViewById(R.id.yhsw_yhzwdc_zwsj);
		final ListView listView = (ListView) dialog
				.findViewById(R.id.yhsw_yhzwdc_listview);
		final YhswYhzwdcAdapter adapter = new YhswYhzwdcAdapter(mContext,
				yhzwLlist);
		if (yhzwLlist.size() > 0) {
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
					for (int i = 0; i < yhzwLlist.size(); i++) {
						yhzwLlist.get(i)
								.put(yhzwLlist.get(i).get("ID"), "true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < yhzwLlist.size(); i++) {
						yhzwLlist.get(i).put(yhzwLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog
				.findViewById(R.id.yhsw_yhzwdc_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_yhzwdc_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				yhzwdcdelete(adapter, listView, zwsjTxt);
			}
		});

		final EditText yhsw_yhzwdc_yhzwmc=(EditText) dialog.findViewById(R.id.yhsw_yhzwdc_yhzwmc);
		final Spinner yhsw_yhzwdc_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_yhzwdc_sfsb);
		ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		yhsw_yhzwdc_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_yhzwdc_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				yhzwdcsearch(dialog,adapter,listView,zwsjTxt,yhsw_yhzwdc_yhzwmc,yhsw_yhzwdc_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	/**有害植物调查 搜索*/
	protected void yhzwdcsearch(Dialog dialog, YhswYhzwdcAdapter adapter,
								ListView listView, TextView zwsjTxt, EditText yhsw_yhzwdc_yhzwmc,
								Spinner yhsw_yhzwdc_sfsb) {
		final String zwmc=yhsw_yhzwdc_yhzwmc.getText().toString();
		final String sbzt1=yhsw_yhzwdc_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		yhzwLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswYhzwData(zwmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswYhzwdcJsonData(result);
				for(int i=0;i<list.size();i++){
					yhzwLlist.add(list.get(i));
				}
			}
		}
		List<HashMap<String, String>> result = DataBaseHelper.searchYhswYhzwData(
				mContext, "db.sqlite",zwmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			yhzwLlist.add(result.get(i));
		}
		if (yhzwLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}
	/** 获取木材病虫害调查数据 */
	public void initMcbchdcData() {
		mcbchLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswMcbchdcData();// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				mcbchLlist = BussUtil.getYhswMcbchdcJsonData(result);
			}

		}

		List<HashMap<String, String>> resultbd = DataBaseHelper
				.getYhswMcbchData(mContext, "db.sqlite");// 获取本地数据
		for (int i = 0; i < resultbd.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = resultbd.get(i);
			mcbchLlist.add(map);
		}
	}

	/** 删除有害植物信息 */
	protected void yhzwdcdelete(YhswYhzwdcAdapter adapter, ListView listView,
								TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < yhzwLlist.size(); i++) {
			if (BussUtil.isEmperty(yhzwLlist.get(i).get("ID").trim())) {
				// 本地数据
				if ("0".equals(yhzwLlist.get(i).get("SCZT"))) {
					if ("true".equals(yhzwLlist.get(i).get(
							yhzwLlist.get(i).get("ID")))) {
						bufferbd.append(yhzwLlist.get(i).get("ID") + ",");
						flags.add(yhzwLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(yhzwLlist.get(i).get("SCZT"))) {
					if ("true".equals(yhzwLlist.get(i).get(
							yhzwLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswYhzwdcData(mContext, "db.sqlite",
						split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			yhzwLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (yhzwLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}
	}

	/** 弹出有害生物 样地病害管理界面 */
	protected void initYdbhdcView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_ydbhdc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog.findViewById(R.id.yhsw_ydbhdc_zwsj);
		final ListView listView = (ListView) dialog.findViewById(R.id.yhsw_ydbhdc_listview);
		final YhswYdbhdcAdapter adapter = new YhswYdbhdcAdapter(mContext,ydbhLlist, ydbhxcLlist);
		if (ydbhLlist.size() > 0) {
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
					for (int i = 0; i < ydbhLlist.size(); i++) {
						ydbhLlist.get(i)
								.put(ydbhLlist.get(i).get("ID"), "true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < ydbhLlist.size(); i++) {
						ydbhLlist.get(i).put(ydbhLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog
				.findViewById(R.id.yhsw_ydbhdc_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_ydbhdc_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ydbhdcdelete(adapter, listView, zwsjTxt);
			}
		});

		final EditText yhsw_ydbhdc_bhmc=(EditText) dialog.findViewById(R.id.yhsw_ydbhdc_bhmc);
		final Spinner yhsw_ydbhdc_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_ydbhdc_sfsb);
		ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.layout.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		yhsw_ydbhdc_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_ydbhdc_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ydbhdcsearch(dialog,adapter,listView,zwsjTxt,yhsw_ydbhdc_bhmc,yhsw_ydbhdc_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	protected void ydbhdcsearch(Dialog dialog, YhswYdbhdcAdapter adapter,
								ListView listView, TextView zwsjTxt, EditText yhsw_ydbhdc_bhmc,
								Spinner yhsw_ydbhdc_sfsb) {
		final String bhmc=yhsw_ydbhdc_bhmc.getText().toString();
		final String sbzt1=yhsw_ydbhdc_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		ydbhLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswYdbhData(bhmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswYdbhdcJsonData(result);
				for(int i=0;i<list.size();i++){
					ydbhLlist.add(list.get(i));
				}
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper.searchYhswYdbhData(
				mContext, "db.sqlite",bhmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			ydbhLlist.add(result.get(i));
		}
		if (ydbhLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}
	/* 样地病害调查 删除 */
	protected void ydbhdcdelete(YhswYdbhdcAdapter adapter, ListView listView,
								TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < ydbhLlist.size(); i++) {
			if (BussUtil.isEmperty(ydbhLlist.get(i).get("ID").trim())) {
				// 本地数据
				if ("0".equals(ydbhLlist.get(i).get("SCZT"))) {
					if ("true".equals(ydbhLlist.get(i).get(
							ydbhLlist.get(i).get("ID")))) {
						bufferbd.append(ydbhLlist.get(i).get("ID") + ",");
						flags.add(ydbhLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(ydbhLlist.get(i).get("SCZT"))) {
					if ("true".equals(ydbhLlist.get(i).get(
							ydbhLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswYdbhdcData(mContext, "db.sqlite",
						split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			ydbhLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (ydbhLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}
	}

	// 弹出有害生物样地虫害界面
	private void initYdchdcView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_ydchdc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog.findViewById(R.id.yhsw_ydchdc_zwsj);
		final ListView listView = (ListView) dialog.findViewById(R.id.yhsw_ydchdc_listview);
		final YhswYdchdcAdapter adapter = new YhswYdchdcAdapter(mContext,ydchLlist, ydchxcLlist);
		if (ydchLlist.size() > 0) {
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
					for (int i = 0; i < ydchLlist.size(); i++) {
						ydchLlist.get(i)
								.put(ydchLlist.get(i).get("ID"), "true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < ydchLlist.size(); i++) {
						ydchLlist.get(i).put(ydchLlist.get(i).get("ID"),
								"false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.yhsw_ydchdc_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_ydchdc_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ydchdcdelete(adapter, listView, zwsjTxt);
			}
		});

		final EditText yhsw_ydchdc_hcmc=(EditText) dialog.findViewById(R.id.yhsw_ydchdc_hcmc);
		final Spinner yhsw_ydchdc_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_ydchdc_sfsb);
		//ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.unreportdata.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		//yhsw_ydchdc_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_ydchdc_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ydchdcsearch(dialog,adapter,listView,zwsjTxt,yhsw_ydchdc_hcmc,yhsw_ydchdc_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 0.8, 0.8);
	}

	/**样地虫害调查 搜索*/
	protected void ydchdcsearch(Dialog dialog, YhswYdchdcAdapter adapter,
								ListView listView, TextView zwsjTxt, EditText yhsw_ydchdc_hcmc,
								Spinner yhsw_ydchdc_sfsb) {
		final String hcmc=yhsw_ydchdc_hcmc.getText().toString();
		final String sbzt1=yhsw_ydchdc_sfsb.getSelectedItemPosition()+"";
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		ydchLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswYdchData(hcmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswYdchdcJsonData(result);
				for(int i=0;i<list.size();i++){
					ydchLlist.add(list.get(i));
				}
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper.searchYhswYdchData(
				mContext, "db.sqlite",hcmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			ydchLlist.add(result.get(i));
		}
		if (ydchLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}
	/** 获取样地病害调查数据 */
	public void initYdbhdcData() {
		ydbhLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswYdbhdcData();// 获取在线数据
			String bhxcresult = webservice.getYhswYdbhxcData();
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				ydbhLlist = BussUtil.getYhswYdbhdcJsonData(result);
			}

			if (bhxcresult.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (bhxcresult.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				ydbhxcLlist = BussUtil.getYhswYdbhxcJsonData(bhxcresult);
			}
		}


		List<HashMap<String, String>> resultbd = DataBaseHelper
				.getYhswYdbhdcData(mContext, "db.sqlite");// 获取本地数据
		for (int i = 0; i < resultbd.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = resultbd.get(i);
			ydbhLlist.add(map);
		}

		List<HashMap<String, String>> xcresultbd = DataBaseHelper
				.getYhswydbhxxdcData(mContext, "db.sqlite");
		for (int i = 0; i < xcresultbd.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = xcresultbd.get(i);
			ydbhxcLlist.add(map);
		}
	}

	/* 获取有害植物调查数据 */
	public void initYhzwdcData() {
		yhzwLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswYhzwdcData();// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				yhzwLlist = BussUtil.getYhswYhzwdcJsonData(result);
			}
		}

		List<HashMap<String, String>> resultbd = DataBaseHelper
				.getYhswYhzwData(mContext, "db.sqlite");// 获取本地数据
		for (int i = 0; i < resultbd.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = resultbd.get(i);
			yhzwLlist.add(map);
		}

	}

	/* 样地虫害调查 删除 */
	protected void ydchdcdelete(YhswYdchdcAdapter adapter, ListView listView,TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<HashMap<String, String>>();
		boolean isbd = false;
		boolean iszx = false;
		for (int i = 0; i < ydchLlist.size(); i++) {
			String id = ydchLlist.get(i).get("ID").trim();
			if (BussUtil.isEmperty(id)) {
				// 本地数据
				String sczt = ydchLlist.get(i).get("SCZT");
				if ("0".equals(sczt)) {
					if ("true".equals(ydchLlist.get(i).get(id))) {
						bufferbd.append(id);
						flags.add(ydchLlist.get(i));
						isbd = true;
					}
					// 在线数据
				} else if ("1".equals(sczt)) {
					if ("true".equals(ydchLlist.get(i).get(id))) {
						iszx = true;
						ToastUtil.setToatBootem(mContext,getResources().getString(R.string.yysbsu));
						return;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswYdchdcData(mContext, "db.sqlite",split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			ydchLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (ydchLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			//ToastUtil.setToatBootem(mContext,getResources().getString(R.string.yysbsu));
			Toast.makeText(mContext,getResources().getString(R.string.yysbsu),Toast.LENGTH_SHORT).show();
		}

	}

	/* 弹出有害生物踏查点界面 */
	private void initTcdView() {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.yhsw_tcdtc_view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final TextView zwsjTxt = (TextView) dialog.findViewById(R.id.yhsw_tcd_zwsj);
		final ListView listView = (ListView) dialog.findViewById(R.id.yhsw_tcd_listview);
		final YhswTcdAdapter adapter = new YhswTcdAdapter(mContext, tcdLlist);
		if (tcdLlist.size() > 0) {
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
					for (int i = 0; i < tcdLlist.size(); i++) {
						tcdLlist.get(i).put(tcdLlist.get(i).get("ID"), "true");
					}
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < tcdLlist.size(); i++) {
						tcdLlist.get(i).put(tcdLlist.get(i).get("ID"), "false");
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		ImageView close = (ImageView) dialog.findViewById(R.id.yhsw_tcd_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		Button delete = (Button) dialog.findViewById(R.id.yhsw_tcd_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tcddelete(adapter, listView, zwsjTxt);
			}
		});


		final EditText yhsw_tcd_jzmc=(EditText) dialog.findViewById(R.id.yhsw_tcd_jzmc);
		final Spinner yhsw_tcd_sfsb=(Spinner) dialog.findViewById(R.id.yhsw_tcd_sfsb);
		//ArrayAdapter sfsbadapter=new ArrayAdapter(mContext, R.unreportdata.myspinner, mContext.getResources().getStringArray(R.array.sfsb));
		//yhsw_tcd_sfsb.setAdapter(sfsbadapter);
		Button search = (Button) dialog.findViewById(R.id.yhsw_tcd_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tcdsearch(dialog,adapter,listView,zwsjTxt,yhsw_tcd_jzmc,yhsw_tcd_sfsb);
			}
		});
		BussUtil.setDialogParams(mContext,dialog, 1, 1);
	}

	/**踏查点 搜索
	 * @param zwsjTxt
	 * @param listView
	 * @param adapter */
	protected void tcdsearch(Dialog dialog, YhswTcdAdapter adapter, ListView listView, TextView zwsjTxt, EditText yhsw_tcd_jzmc, Spinner yhsw_tcd_sfsb) {
		final String sbzt1=yhsw_tcd_sfsb.getSelectedItemPosition()+"";
		final String jzmc=yhsw_tcd_jzmc.getText().toString();
		String sbzt="";
		if("1".equals(sbzt1)){
			sbzt="1";
		}else if("2".equals(sbzt1)){
			sbzt="0";
		}

		tcdLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice web=new Webservice(mContext);
			String result = web.searchYhswTcdlData(jzmc, sbzt);// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				List<HashMap<String, String>> list = BussUtil.getYhswTcdJsonData(result);
				for(int i=0;i<list.size();i++){
					tcdLlist.add(list.get(i));
				}
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper.searchYhswTcdData(
				mContext, "db.sqlite",jzmc,sbzt);// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			tcdLlist.add(result.get(i));
		}
		if (tcdLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.VISIBLE);
			zwsjTxt.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}

	}
	/** 获取样地虫害数据*/
	public void initYdchdcData() {
		ydchLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswYdchdcData();// 获取样地虫害在线数据
			String chxcresult = webservice.getYhswYdchxcData();
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				ydchLlist = BussUtil.getYhswYdchdcJsonData(result);
			}

			if (chxcresult.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (chxcresult.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				ydchxcLlist = BussUtil.getYhswYdchxcJsonData(chxcresult);
			}
		}

		List<HashMap<String, String>> result = DataBaseHelper
				.getYhswYdchdcData(mContext, "db.sqlite");// 获取样地虫害本地数据
		for (int i = 0; i < result.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = result.get(i);
			ydchLlist.add(map);
		}

		List<HashMap<String, String>> xcresult = DataBaseHelper
				.getYhswydxxdcData(mContext, "db.sqlite");
		for (int i = 0; i < xcresult.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = xcresult.get(i);
			ydchxcLlist.add(map);
		}

	}

	// 踏查点 删除
	protected void tcddelete(YhswTcdAdapter adapter, ListView listView,TextView zwsjTxt) {
		StringBuffer bufferbd = new StringBuffer();
		ArrayList<HashMap<String, String>> flags = new ArrayList<>();
		boolean iszx = false;
		boolean isbd = false;
		for (int i = 0; i < tcdLlist.size(); i++) {
			if (BussUtil.isEmperty(tcdLlist.get(i).get("ID").trim())) {
				// 本地数据
				if ("0".equals(tcdLlist.get(i).get("UPLOADSTATUS"))) {
					if ("true".equals(tcdLlist.get(i).get(
							tcdLlist.get(i).get("ID")))) {
						bufferbd.append(tcdLlist.get(i).get("ID") + ",");
						flags.add(tcdLlist.get(i));
						isbd = true;
					}
					// 有已提交数据
				} else if ("1".equals(tcdLlist.get(i).get("UPLOADSTATUS"))) {
					if ("true".equals(tcdLlist.get(i).get(
							tcdLlist.get(i).get("ID")))) {
						iszx = true;
					}
				}
			}
		}
		if (bufferbd.length() > 0) {
			String idbd = bufferbd.substring(0, bufferbd.length() - 1).trim();
			String[] split = idbd.split(",");
			for (int i = 0; i < split.length; i++) {
				DataBaseHelper.deleteYhswTcdData(mContext, "db.sqlite",
						split[i]);
			}
		}
		for (int i = 0; i < flags.size(); i++) {
			tcdLlist.remove(flags.get(i));
		}
		adapter.notifyDataSetChanged();
		if (tcdLlist.size() == 0) {
			listView.setVisibility(View.GONE);
			zwsjTxt.setVisibility(View.VISIBLE);
		}
		if (isbd) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.deletesuccess));
		}
		if (iszx) {
			ToastUtil.setToast(mContext,
					getResources().getString(R.string.yysbsu));
		}
	}

	/* 有害生物 控件初始化 */
	private void initview() {
		bottomview = parentview.findViewById(R.id.yhsw_bottomview);
		yhswtc = (TextView) bottomview.findViewById(R.id.yhsw_yhswtc);
		yhswtc.setOnClickListener(new MyListense());
		ydchdc = (TextView) bottomview.findViewById(R.id.yhsw_ydchdc);
		ydchdc.setOnClickListener(new MyListense());
		ydbhdc = (TextView) bottomview.findViewById(R.id.yhsw_ydbhdc);
		ydbhdc.setOnClickListener(new MyListense());
		yhzwdc = (TextView) bottomview.findViewById(R.id.yhsw_yhzwdc);
		yhzwdc.setOnClickListener(new MyListense());
		mcbchdc = (TextView) bottomview.findViewById(R.id.yhsw_mcbchdc);
		mcbchdc.setOnClickListener(new MyListense());
		ycddc = (TextView) bottomview.findViewById(R.id.yhsw_ycddc);
		ycddc.setOnClickListener(new MyListense());
		scxcbpc = (TextView) bottomview.findViewById(R.id.yhsw_scxcbpc);
		scxcbpc.setOnClickListener(new MyListense());

		TextView erweima =(TextView) bottomview.findViewById(R.id.yhsw_erweima);
		erweima.setOnClickListener(new MyListense());

		caozuoview = parentview.findViewById(R.id.yhsw_cz);
		tcqd = (TextView) caozuoview.findViewById(R.id.yhsw_tcqd);
		qxtc = (TextView) caozuoview.findViewById(R.id.yhsw_qxtc);
		zjtcd = (TextView) caozuoview.findViewById(R.id.yhsw_zjtcd);
		jstc = (TextView) caozuoview.findViewById(R.id.yhsw_jstc);
		tcqd.setOnClickListener(new MyListense());
		qxtc.setOnClickListener(new MyListense());
		zjtcd.setOnClickListener(new MyListense());
		jstc.setOnClickListener(new MyListense());

		xbbjInclude.setVisibility(View.GONE);
	}

	// 获取踏查点在线数据
	public void initTcdData() {
		tcdLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswtcdData();// 获取在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				tcdLlist = BussUtil.getYhswTcdJsonData(result);
			}
		}
		List<HashMap<String, String>> result = DataBaseHelper.getYhswTcdData(
				mContext, "db.sqlite");// 获取本地数据
		for (int i = 0; i < result.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = result.get(i);
			tcdLlist.add(map);
		}
	}
	/**获取 有害生物踏查路线数据*/
	public void initTclxData() {
		tclxLlist.clear();
		if (app == null) {
			ToastUtil.setToast(mContext, "网络未连接");
			return;
		}
		if (MyApplication.getInstance().netWorkTip()) {
			Webservice webservice = new Webservice(mContext);
			String result = webservice.getYhswtclxData();// 获取踏查路线在线数据
			if (result.equals(Webservice.netException)) {
				ToastUtil.setToast(mContext, Webservice.netException);
			} else if (result.equals("无数据")) {
				// ToastUtil.setToast(mContext, "无数据");
			} else {
				tclxLlist = BussUtil.getYhswTclxJsonData(result);
				//如果存在没有添加的路线踏查就删除数据
				for(int i = 0; i < tclxLlist.size(); i++){
					HashMap<String, String>delmap=tclxLlist.get(i);
					if("".equals(delmap.get("UPLOADSTATUS"))){
						webservice.deleteYhswTclxData(delmap.get("TCLXID"));
						tclxLlist.remove(delmap);
					}
				}
			}
		} else {
			ToastUtil.setToast(mContext, "网络未连接");
		}
		List<HashMap<String, String>> result1 = DataBaseHelper
				.getYhswTclxData(mContext, "db.sqlite");// 获取踏查路线本地数据
		for (int i = 0; i < result1.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map = result1.get(i);
			tclxLlist.add(map);
		}
	}
	class MyListense implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
				case R.id.yhsw_erweima:
					//二维码扫描
					Intent intent = new Intent(YHSWActivity.this, CaptureActivity.class);
					startActivityForResult(intent, REQUEST_CODE);
					break;
				// 调查点踏查
				case R.id.yhsw_yhswtc:
					poptype = "yhswtc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str1 = getResources().getString(
							R.string.addnewyhswtc);
					String pop2str1 = getResources().getString(R.string.czyhswlsxx);
					String pop3str1 = getResources().getString(R.string.czyhswtclxlsxx);
					initItem(pop1str1, pop2str1,pop3str1, yhswtc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				// 样地虫害调查
				case R.id.yhsw_ydchdc:
					poptype = "ydchdc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str2 = getResources().getString(R.string.addydchdc);
					String pop2str2 = getResources().getString(R.string.czydchdcxx);
					initItem(pop1str2, pop2str2,"", ydchdc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				// 样地病害调查
				case R.id.yhsw_ydbhdc:
					poptype = "ydbhdc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str3 = getResources().getString(R.string.addydbhdc);
					String pop2str3 = getResources().getString(R.string.czydbhdcxx);
					initItem(pop1str3, pop2str3,"", ydbhdc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				// 有害植物调查
				case R.id.yhsw_yhzwdc:
					poptype = "yhzwdc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str4 = getResources()
							.getString(R.string.addyhzwdcxx);
					String pop2str4 = getResources().getString(
							R.string.czyhzwdclsxx);
					initItem(pop1str4, pop2str4,"", yhzwdc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				// 木材病虫害调查
				case R.id.yhsw_mcbchdc:
					poptype = "mcbchdc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str5 = getResources().getString(
							R.string.addmcbchdcxx);
					String pop2str5 = getResources()
							.getString(R.string.czmcbchdcxx);
					initItem(pop1str5, pop2str5,"", mcbchdc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				// 诱虫灯调查
				case R.id.yhsw_ycddc:
					poptype = "ycddc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str6 = getResources().getString(R.string.addycddcxx);
					String pop2str6 = getResources().getString(R.string.czycddcxx);
					initItem(pop1str6, pop2str6,"", ycddc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				//松材线虫病普查
				case R.id.yhsw_scxcbpc:
					poptype = "scxcbpc";
					if (PopWindow != null) {
						PopWindow.dismiss();
					}
					String pop1str7 = getResources().getString(R.string.addscxcbpcxx);
					String pop2str7 = getResources().getString(R.string.czscxcbpcxx);
					initItem(pop1str7, pop2str7,"", scxcbpc, poptype);
					tcqd.setVisibility(View.GONE);
					qxtc.setVisibility(View.GONE);
					zjtcd.setVisibility(View.GONE);
					jstc.setVisibility(View.GONE);
					break;
				// 添加有害生物信息
				case R.id.yhsw_pop1:
					// 有害生物踏查
					if ("yhswtc".equals(poptype)) {
						pop1action("yhswtc");
						// 样地虫害踏查
					} else if ("ydchdc".equals(poptype)) {
						pop1action("ydchdc");
						// 样地病害踏查
					} else if ("ydbhdc".equals(poptype)) {
						pop1action("ydbhdc");
						// 有害植物调查
					} else if ("yhzwdc".equals(poptype)) {
						pop1action("yhzwdc");
						// 木材病虫害调查
					} else if ("mcbchdc".equals(poptype)) {
						pop1action("mcbchdc");
						// 诱虫灯调查
					} else if ("ycddc".equals(poptype)) {
						pop1action("ycddc");
						//松材线虫病普查
					}else if ("scxcbpc".equals(poptype)) {
						pop1action("scxcbpc");
					}
					break;
				// 操作有害生物信息
				case R.id.yhsw_pop2:
					if ("yhswtc".equals(poptype)) {
						pop2action("yhswtc");
					} else if ("ydchdc".equals(poptype)) {
						pop2action("ydchdc");
					} else if ("ydbhdc".equals(poptype)) {
						pop2action("ydbhdc");
					} else if ("yhzwdc".equals(poptype)) {
						pop2action("yhzwdc");
					} else if ("mcbchdc".equals(poptype)) {
						pop2action("mcbchdc");
					} else if ("ycddc".equals(poptype)) {
						pop2action("ycddc");
					}else if ("scxcbpc".equals(poptype)) {
						pop2action("scxcbpc");
					}
					break;
				// 操作有害生物信息
				case R.id.yhsw_pop3:
					if ("yhswtc".equals(poptype)) {
						pop3action("yhswtc");
					}
					break;
				// 踏查起点
				case R.id.yhsw_tcqd:
					tcstart();
					break;
				// 取消踏查
				case R.id.yhsw_qxtc:
					cancletc();
					break;
				// 增加踏查点
				case R.id.yhsw_zjtcd:
					showAddtcdDialog("yhsw_zjtcd");
					break;
				// 结束踏查
				case R.id.yhsw_jstc:
					showLxtcdialog();
					break;
			}
		}
	}

	// 点击pop1执行的操作
	private void pop1action(String type) {
		PopWindow.dismiss();
		if ("yhswtc".equals(type)) {
			if (!tcks) {
				tcqd.setVisibility(View.VISIBLE);
				qxtc.setVisibility(View.VISIBLE);
				zjtcd.setVisibility(View.GONE);
				jstc.setVisibility(View.GONE);
			}
		} else if ("ydchdc".equals(type)) {
			showAddydchdcDialog();
		} else if ("ydbhdc".equals(type)) {
			showAddydbhdcDialog();
		} else if ("yhzwdc".equals(type)) {
			showAddyhzwdcDialog();
		} else if ("mcbchdc".equals(type)) {
			showAddmcbchdcDialog();
		} else if ("ycddc".equals(type)) {
			showAddycddcDialog();
		}else if ("scxcbpc".equals(type)) {
			showAddscxcbpcDialog();
		}
	}

	// 点击pop2执行的操作
	private void pop2action(String type) {
		// 踏查点踏查
		if ("yhswtc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_tcd");
			// 样地虫害调查
		} else if ("ydchdc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_ydchdc");
			// 样地病害调查
		} else if ("ydbhdc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_ydbhdc");
			// 有害植物调查
		} else if ("yhzwdc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_yhzwdc");
			// 木材病虫害调查
		} else if ("mcbchdc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_mcbchdc");
			// 诱虫灯调查
		} else if ("ycddc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_ycddc");
			//松材线虫病普查
		}else if ("scxcbpc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_scxcbpc");
		}
	}
	// 点击pop3执行的操作
	public void pop3action(String type) {
		// 踏查点踏查
		if ("yhswtc".equals(type)) {
			PopWindow.dismiss();
			handler.sendEmptyMessage(PROGRESSDIALOG);
			new MyAsyncTask().execute("yhsw_tclx");
			// 样地虫害调查
		}
	}
	// 每一项的点击事件
	@SuppressLint("NewApi")
	private void initItem(String pop1str, String pop2str,String pop3str, TextView item,String type) {
		PopWindow = new MorePopWindow(YHSWActivity.this, R.layout.popup_yhsw);
		PopWindow.showAsDropDown(item,yhswtc.getWidth() / 2 - PopWindow.getWidth() / 2,yhswtc.getHeight() - 35);
		TextView pop1 = (TextView) PopWindow.getContentView().findViewById(R.id.yhsw_pop1);
		pop1.setText(pop1str);
		TextView pop2 = (TextView) PopWindow.getContentView().findViewById(R.id.yhsw_pop2);
		pop2.setText(pop2str);
		if("yhswtc".equals(type)){
			TextView line = (TextView) PopWindow.getContentView().findViewById(R.id.linepop3);
			View linear = (View) PopWindow.getContentView().findViewById(R.id.linearpop3);
			line.setVisibility(View.VISIBLE);
			linear.setVisibility(View.VISIBLE);
		}
		TextView pop3 = (TextView) PopWindow.getContentView().findViewById(R.id.yhsw_pop3);
		pop3.setText(pop3str);

		pop1.setOnClickListener(new MyListense());
		pop2.setOnClickListener(new MyListense());
		pop3.setOnClickListener(new MyListense());
		if ("yhswtc".equals(type)) {
			yhswtc.setBackgroundColor(getResources().getColor(R.color.grey));
			ydchdc.setBackground(getResources().getDrawable(R.drawable.background_view_rounded_blue));
			ydbhdc.setBackground(getResources().getDrawable(R.drawable.background_view_rounded_blue));
			yhzwdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			mcbchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ycddc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			scxcbpc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		} else if ("ydchdc".equals(type)) {
			ydchdc.setBackgroundColor(getResources().getColor(R.color.grey));
			yhswtc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydbhdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			yhzwdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			mcbchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ycddc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			scxcbpc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		} else if ("ydbhdc".equals(type)) {
			ydbhdc.setBackgroundColor(getResources().getColor(R.color.grey));
			yhswtc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			yhzwdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			mcbchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ycddc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			scxcbpc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		} else if ("yhzwdc".equals(type)) {
			yhzwdc.setBackgroundColor(getResources().getColor(R.color.grey));
			yhswtc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydbhdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			mcbchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ycddc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			scxcbpc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		} else if ("mcbchdc".equals(type)) {
			mcbchdc.setBackgroundColor(getResources().getColor(R.color.grey));
			yhswtc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydbhdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			yhzwdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ycddc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			scxcbpc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		} else if ("ycddc".equals(type)) {
			ycddc.setBackgroundColor(getResources().getColor(R.color.grey));
			yhswtc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydbhdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			yhzwdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			mcbchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			scxcbpc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		} else if ("scxcbpc".equals(type)) {
			scxcbpc.setBackgroundColor(getResources().getColor(R.color.grey));
			yhswtc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ydbhdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			yhzwdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			mcbchdc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
			ycddc.setBackground(getResources().getDrawable(
					R.drawable.background_view_rounded_blue));
		}

	}

	@Override
	public View getParentView() {
		return parentview;
	}
	/**松材线虫病普查 添加*/
	private void showAddscxcbpcDialog() {
		YhswScxcbpcAddDialog dialog = new YhswScxcbpcAddDialog(mContext,R.style.Dialog, currentPoint);
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}
	/** 诱虫灯调查 添加 */
	private void showAddycddcDialog() {
		YhswYcddcAddDialog dialog = new YhswYcddcAddDialog(mContext,
				R.style.Dialog, currentPoint);
		dialog.show();
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	/** 木材病虫害 添加 */
	public void showAddmcbchdcDialog() {
		YhswMcbchdcAddDialog dialog = new YhswMcbchdcAddDialog(mContext,R.style.Dialog, currentPoint);
		BussUtil.setDialogParams(mContext,dialog, 1, 1);
	}

	/** 有害植物调查 添加 */
	public void showAddyhzwdcDialog() {
		YhswYhzwdcAddDialog dialog = new YhswYhzwdcAddDialog(mContext,
				R.style.Dialog, currentPoint);
		dialog.show();
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	// 样地病害添加
	public void showAddydbhdcDialog() {
		YhswYdbhdcAddDialog dialog = new YhswYdbhdcAddDialog(mContext,R.style.Dialog, currentPoint);
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	// 显示样地虫害调查 添加
	public void showAddydchdcDialog() {
		YhswYdchdcAddDialog dialog = new YhswYdchdcAddDialog(mContext,R.style.Dialog, currentPoint);
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	// 取消踏查
	public void cancletc() {
		tcqd.setVisibility(View.VISIBLE);
		qxtc.setVisibility(View.VISIBLE);
		zjtcd.setVisibility(View.GONE);
		jstc.setVisibility(View.GONE);
		ToastUtil.setToast(mContext,
				getResources().getString(R.string.qxtcsuccess));
	}

	// 有害生物路线踏查 添加
	public void showLxtcdialog() {
		YhswLxtcDialog dialog = new YhswLxtcDialog(mContext, R.style.Dialog);
		dialog.show();
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	// 踏查开始
	public void tcstart() {
		Webservice web = new Webservice(mContext);
		String lxbh = UtilTime.getSystemtime1();
		if (currentPoint == null) {
			ToastUtil.setToast(mContext,
					mContext.getResources().getString(R.string.qdtcfailed));
			return;
		}
		String lon = currentPoint.getX() + "";
		String lat = currentPoint.getY() + "";
		DataBaseHelper.addYhswTclxData(mContext, "db.sqlite", lxbh, lon, lat);
		String result = web.addYhswlxtcData(lxbh, lon, lat);
		String[] splits = result.split(",");
		if (splits.length > 0) {
			if ("True".equals(splits[0])) {
				lxtcid = lxbh;
				tcqd.setVisibility(View.GONE);
				qxtc.setVisibility(View.VISIBLE);
				zjtcd.setVisibility(View.VISIBLE);
				jstc.setVisibility(View.GONE);
				ToastUtil.setToast(mContext,mContext.getResources().getString(R.string.qdtcsuccess));
			} else {
				ToastUtil.setToast(mContext,
						mContext.getResources().getString(R.string.qdtcfailed));
			}
		}

	}

	/* 显示添加踏查点界面 */
	public void showAddtcdDialog(String flag) {
		YhswTcdDialog dialog = new YhswTcdDialog(mContext, R.style.Dialog, flag);
		dialog.show();
		BussUtil.setDialogParams(mContext,dialog, 0.85, 0.9);
	}

	public void stopProgress() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ProgressDialogUtil.stopProgressDialog(mContext);
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/**
		 * 处理二维码扫描结果
		 */
		if (requestCode == REQUEST_CODE) {
			//处理扫描结果（在界面上显示）
			if (null != data) {
				Bundle bundle = data.getExtras();
				if (bundle == null) {
					return;
				}
				if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
					String result = bundle.getString(CodeUtils.RESULT_STRING);
					if(result.contains("http://") || result.contains("https://")){
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
						intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
						startActivity(intent);
					}else{
						ToastUtil.setToast(mContext,result);
					}

				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
					ToastUtil.setToast(mContext, "解析二维码失败");
				}
			}
		}
	}
}
