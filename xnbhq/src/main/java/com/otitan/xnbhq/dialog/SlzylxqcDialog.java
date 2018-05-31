package com.otitan.xnbhq.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.SlzylxqcActivity;
import com.otitan.xnbhq.activity.SlzylxqcYdyzActivity;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.db.DbHelperService;
import com.otitan.xnbhq.entity.ActionMode;
import com.otitan.xnbhq.entity.LxqcXmdc;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.timepaker.TimePopupWindow;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 清查记录查询dialog
 */
public class SlzylxqcDialog extends Dialog{
	Context context;
	SlzylxqcActivity activity;
	private static final int TAKE_PICTURE = 0x000003;
	/** 执行progressbar */
	private static final int PROGRESSDIALOG = 1;
	private static final int STOPPROGRESS = 2;
	/** 样地调查记录list */
	private List<HashMap<String, String>> yddcjlLlist = new ArrayList<>();
	/** 下木调查表list */
	private List<HashMap<String, String>> xmdcLlist = new ArrayList<>();
	/** 森林灾害情况调查list */
	private List<HashMap<String, String>> slzhqkLlist = new ArrayList<>();
	/** 复查期内样地变化调查list */
	private List<HashMap<String, String>> fcqnydbhLlist = new ArrayList<>();
	/** 天然更新情况调查list */
	private List<HashMap<String, String>> trgxqkdcLlist = new ArrayList<>();
	/** 引线测量记录调查list */
	private List<HashMap<String, String>> yxcljldcLlist = new ArrayList<>();
	/** 周界测量记录调查list */
	private List<HashMap<String, String>> zjcljldcLlist = new ArrayList<>();
	/**周界共有信息*/
	private List<HashMap<String, String>> zjgylist= new ArrayList<>();
	/** 跨角林记录调查list */
	private List<HashMap<String, String>> kjljldcLlist = new ArrayList<>();
	/** 样地位置图list */
	private List<HashMap<String, String>> ydwztLlist = new ArrayList<>();
	/** 引点位置图list */
	private List<HashMap<String, String>> yindwztLlist = new ArrayList<>();
	/** 引点位置图list */
	private List<HashMap<String, String>> gpshjdcLlist = new ArrayList<>();
	/** 样地目录list */
	private List<HashMap<String, String>> ydmmLlist = new ArrayList<>();
	/** 每木检尺记录 */
	private List<HashMap<String, String>> mmjcjlLlist = new ArrayList<>();
	/** 每木检尺记录 */
	private List<HashMap<String, String>> sylist = new ArrayList<>();
	/** 样地号*/
	private String ydhselect="";
	/**签名开始的Bitmap*/
	private static Bitmap qmbitmap = null;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/** 估计查询 */
	private String selectTime = "选择时间";
	private Button zdcf;
	private Button zdyz;
	private Button dcjs;
	private Button fhzd;
	/**点的属性信息*/
	private Map<String, Object> selectFeatureAts = new HashMap<>();
	private TextView location,ydh,ld_see_pic;
	private MyFeture myFeture = null;
	private ActionMode actionMode;
	int position;
	/**森林资源联系清查属性页面*/
	public SlzylxqcDialog(SlzylxqcActivity context,MyFeture layer,int posm,ActionMode mode) {
		super(context, R.style.Dialog);
		this.context = context;
		this.activity = context;
		this.myFeture = layer;
		this.actionMode = mode;
		this.position = posm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_view);

		location=(TextView) findViewById(R.id.location);
		ydh=(TextView) findViewById(R.id.ydh);
		selectFeatureAts=BaseActivity.selectFeatureAts;
		String Xbhzd = LxqcUtil.getAttributeXbh(context, "XBH", "LXQCXBH",myFeture.getPath());
		ydhselect=(String) selectFeatureAts.get("WYBH");
		if(ydhselect!=null){
			ydh.setText(ydhselect);
		}else{
			ydhselect="";
		}
		/**获取需要的样地位置*/
		handler.sendEmptyMessage(PROGRESSDIALOG);
		new MyAsyncTask().execute("ydwz");

		/**查看图片*/
		ld_see_pic=(TextView) findViewById(R.id.ld_see_pic);
		ld_see_pic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				lookPic();
			}
		});
		/**驻地出发时间*/
		zdcf=(Button) findViewById(R.id.zdcf);
		zdcf.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initSelectTimePopuwindow(zdcf, false);
			}
		});
		/**找到样桩时间*/
		zdyz=(Button) findViewById(R.id.zdyz);
		zdyz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initSelectTimePopuwindow(zdyz, false);
			}
		});
		/**调查结束时间*/
		dcjs=(Button) findViewById(R.id.dcjs);
		dcjs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initSelectTimePopuwindow(dcjs, false);
			}
		});
		/**返回驻地时间*/
		fhzd=(Button) findViewById(R.id.fhzd);
		fhzd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initSelectTimePopuwindow(fhzd, false);
			}
		});

		handler.sendEmptyMessage(PROGRESSDIALOG);
		new MyAsyncTask().execute("ydbmm");


		final TextView yddcjlb = (TextView) findViewById(R.id.yddcjlb);
		/**样地调查记录表*/
		yddcjlb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("ydcjlb");

			}
		});
		/**样地因子调查*/
		TextView ydyzdc = (TextView) findViewById(R.id.slzylxqc_ydyzdc);
		ydyzdc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.getSelParams(BaseActivity.selGeoFeaturesList, position,activity.selMap);
				Intent intent = new Intent(context, SlzylxqcYdyzActivity.class);
				intent.putExtra("ydh", ydhselect);
				context.startActivity(intent);
			}
		});
		/**样地位置图*/
		TextView ydwzt = (TextView) findViewById(R.id.ydwzt);
		ydwzt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("ydwzt");
			}
		});
		/**引点位置图*/
		TextView yingdwzt = (TextView) findViewById(R.id.yingdwzt);
		yingdwzt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("yingdwzt");
			}
		});
		/**下木调查*/
		TextView xmdc = (TextView) findViewById(R.id.xmdc);
		xmdc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("xmdc");
			}
		});
		/**森林灾害情况调查*/
		TextView slzhqkdc = (TextView) findViewById(R.id.slzhqkdc);
		slzhqkdc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("slzhqkdc");
			}
		});
		/**复查期内样地变化情况调查*/
		TextView fcqlydbh = (TextView) findViewById(R.id.fcqlydbh);
		fcqlydbh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowFcqlydbhDialog();
			}
		});
		/**天然更新情况调查*/
		TextView trgxdc = (TextView) findViewById(R.id.trgxdc);
		trgxdc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("trgxdc");
			}
		});
		/**未成林造林地调查*/
		TextView wclzlddc = (TextView) findViewById(R.id.wclzlddc);
		wclzlddc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowWclzldcDialog();
			}
		});
		/**植被调查*/
		TextView zbdc = (TextView) findViewById(R.id.zbdc);
		zbdc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowZbdcdcDialog();
			}
		});
		/**树（毛竹）高测量记录*/
		TextView sgcljl = (TextView) findViewById(R.id.sgcljl);
		sgcljl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShowSgcljlDialog();
			}
		});
		/**引线测量调查*/
		TextView yxcldc = (TextView) findViewById(R.id.yxcldc);
		yxcldc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("yxcldc");
			}
		});
		/**周界测量调查*/
		TextView zjcldc = (TextView) findViewById(R.id.zjcldc);
		zjcldc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("zjcldc");
			}
		});
		/**跨角林调查记录*/
		TextView kjldcjl = (TextView) findViewById(R.id.kjldcjl);
		kjldcjl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("kjldcjl");
			}
		});
		/**GPS航迹调查记录*/
		TextView gpshjdcjl = (TextView) findViewById(R.id.gpshjdcjl);
		gpshjdcjl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("gpshjdcjl");
			}
		});
		/**每木检尺记录*/
		TextView mmjcdc=(TextView) findViewById(R.id.mmjcdc);
		mmjcdc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("mmjcdc");
			}
		});
		/**签名*/
		View qianming=findViewById(R.id.qianming);
		/**读取本地签名图片地址*/
		String path =MyApplication.resourcesManager.getLxqcImagePath("1");
		qmbitmap =MyApplication.resourcesManager.getImageBitmap(ydhselect, path);
		final ImageView qmimage=(ImageView) findViewById(R.id.qmimage);
		if(qmbitmap!=null){
			qmimage.setImageBitmap(qmbitmap);
		}
		qianming.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SlzylxqcQianMingDialog dialog=new SlzylxqcQianMingDialog(context,ydhselect,qmimage,qmbitmap,"1");
				BussUtil.setDialogParamsFull(context, dialog);
			}
		});

		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		/**拍照*/
		TextView photograph = (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				takephoto();
			}
		});
	}
	/**样地调查记录表Dialog*/
	protected void ShowYddcjlbDialog() {
		SlzylxqcYddcjlDialog dialog=new SlzylxqcYddcjlDialog(context,yddcjlLlist,ydhselect,location);
		BussUtil.setDialogParamsFull(context, dialog);
	}

	/** 时间选择popupwindow */
	public void initSelectTimePopuwindow(final Button button, boolean isBefore) {
		TimePopupWindow timePopupWindow = new TimePopupWindow(context, TimePopupWindow.Type.ALL);
		timePopupWindow.setCyclic(true);
		/** 时间选择后回调*/
		timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				selectTime = getTime(date);
				button.setText(selectTime);
				DataBaseHelper.deleteLxqcYdbmmData(context, ydhselect);
				DataBaseHelper.addLxqcLxqcYdbmmData(context, ydhselect, zdcf
						.getText().toString(), zdyz.getText().toString(), dcjs
						.getText().toString(), fhzd.getText().toString());
			}
		});
		timePopupWindow.showAtLocation(button, Gravity.BOTTOM, 0, 0,
				new Date(), isBefore);
	}

	public String getTime(Date date) {
		return format.format(date);
	}

	/** 设置签名的初始bitmap */
	public static void setBitmap(Bitmap bitmap) {
		qmbitmap = bitmap;
	}

	/** 引线测量调查 */
	protected void ShowYxcldcDialog() {
		yxcljldcLlist = DataBaseHelper.searchLxqcYxcljlData(context, ydhselect);
		SlzylxqcYxcljlDialog yxcldialog = new SlzylxqcYxcljlDialog(context, yxcljldcLlist, ydhselect);
		BussUtil.setDialogParamsFull(context, yxcldialog);
	}

	/** 树（毛竹）高测量记录 */
	protected void ShowSgcljlDialog() {
		List<HashMap<String, String>> list = DataBaseHelper
				.searchLxqcSgcljlData(context, ydhselect);
		SlzylxqcSgclDialog sgcldialog = new SlzylxqcSgclDialog(context, list,ydhselect);
		BussUtil.setDialogParamsFull(context, sgcldialog);
	}

	/** 植被调查 */
	protected void ShowZbdcdcDialog() {
		SlzylxqcZbdcDialog dialog=new SlzylxqcZbdcDialog(context, ydhselect);
		BussUtil.setDialogParamsFull(context, dialog);
	}

	/** 未成林造林地调查 */
	protected void ShowWclzldcDialog() {
		SlzylxqcWclzldcDialog dialog=new SlzylxqcWclzldcDialog(context,ydhselect);
		BussUtil.setDialogParamsFull(context, dialog);
	}

	/** 天然更新情况调查Dialog */
	protected void ShowTrgxqkdcDialog() {
		SlzylxqcTrgxqkDialog dialog = new SlzylxqcTrgxqkDialog(context, trgxqkdcLlist,ydhselect);
		BussUtil.setDialogParamsFull(context, dialog);
	}

	/** 复查期内样地变化情况Dialog */
	protected void ShowFcqlydbhDialog() {
		SlzylxqcFcqnydbhdcDialog dialog=new SlzylxqcFcqnydbhdcDialog(context,fcqnydbhLlist,ydhselect,myFeture.getPath());
		BussUtil.setDialogParamsFull(context, dialog);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				/** 执行progressdialog */
				case PROGRESSDIALOG:
					ProgressDialogUtil.startProgressDialog(context);
					break;
				/** 停止 */
				case STOPPROGRESS:
					/** 设置进度条位置 */
					ProgressDialogUtil.stopProgressDialog(context);
					break;
			}
		};
	};

	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			/**样地目录*/
			if (params[0].equals("ydbmm")) {
				ydmmLlist = DataBaseHelper.searchLxqcYdbmmData(context,
						ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						if (ydmmLlist.size() != 0) {
							HashMap<String, String> map = ydmmLlist.get(0);
							if (map != null) {
								zdcf.setText(map.get("ZDCFSJ"));
								zdyz.setText(map.get("ZDYZSJ"));
								dcjs.setText(map.get("DCJSSJ"));
								fhzd.setText(map.get("FHZDSJ"));
							}
						}
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("xmdc")) {
				/**下木调查*/
				new DbHelperService<LxqcXmdc>(context, LxqcXmdc.class);
				xmdcLlist = DataBaseHelper.getlxqcxmdcData(context);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowXmdcDialog(xmdcLlist);
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("slzhqkdc")) {
				/**森林灾害情况调查*/
				slzhqkLlist = DataBaseHelper.getslzhqkdcData(context,ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowSlzhqcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("trgxdc")) {
				/**天然更新情况调查*/
				trgxqkdcLlist = DataBaseHelper.getTrgxqkdcData(context,ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowTrgxqkdcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("yxcldc")) {
				/**引线测量记录*/
				yxcljldcLlist = DataBaseHelper.searchLxqcYxcljlData(context,
						ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowYxcldcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("zjcldc")) {
				/**周界测量记录*/
				zjcljldcLlist = DataBaseHelper.searchLxqcZjcljlData(context,
						ydhselect,"2");
				zjgylist = DataBaseHelper.searchLxqcZjcljlData(context,
						ydhselect,"1");
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowZjcldcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("kjldcjl")) {
				/**跨角林调查记录*/
				kjljldcLlist = DataBaseHelper.searchLxqcKjldcjlData(context,
						ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowKjldcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("ydwzt")) {
				/**样地位置图*/
				ydwztLlist = DataBaseHelper.searchLxqcYdwztData(context,
						ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowYdwztDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			} else if (params[0].equals("yingdwzt")) {
				/**引点位置图*/
				yindwztLlist = DataBaseHelper.searchLxqcYindwztData(context,
						ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowYindwztDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}else if (params[0].equals("gpshjdcjl")) {
				/**GPS航迹调查记录*/
				gpshjdcLlist = DataBaseHelper.searchLxqcGpshjdcbData(context,
						ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowGpshjdcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}else if (params[0].equals("ydcjlb")) {
				/**样地调查记录表*/
				yddcjlLlist = DataBaseHelper.getLxqcYddcData(context,ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowYddcjlbDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}else if (params[0].equals("mmjcdc")) {
				/**每木检尺记录*/
				mmjcjlLlist = DataBaseHelper.getLxqcMmjcData(context,ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						ShowMmjcDialog();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}else if (params[0].equals("ydwz")) {
				/**获取样地调查记录表信息更新首页样地位置*/
				sylist = DataBaseHelper.getLxqcYddcData(context,ydhselect);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						dealWithData();
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}
			return null;
		}
	}
	/**图片浏览*/
	public void lookPic(){

		List<HashMap<String, Object>> seephoyolist = new ArrayList<HashMap<String, Object>>();
		String ydpath = MyApplication.resourcesManager.getLxqcImagePath("4");
		String ympath = MyApplication.resourcesManager.getLxqcImagePath("5");
		List<HashMap<String, Object>> templist1 = MyApplication.resourcesManager.getImageBitmapAll(ydhselect,ympath);
		List<HashMap<String, Object>> templist2 = MyApplication.resourcesManager.getImageBitmapAll(ydhselect,ydpath);
		if(templist1!=null){
			for(int i=0;i<templist1.size();i++){
				seephoyolist.add(templist1.get(i));
			}
		}
		if(templist2!=null){
			for(int i=0;i<templist2.size();i++){
				seephoyolist.add(templist2.get(i));
			}
		}
		if(seephoyolist.size()!=0){
			PhotoShowDialog photodialog=new PhotoShowDialog(context,seephoyolist);
			BussUtil.setDialogParamsFull(context, photodialog);
		}else{
			ToastUtil.setToast(context, "当前系统没有图片");
		}
	}
	/**得到首页要填的位置数据*/
	protected void dealWithData() {
		String result="";
		if(sylist!=null&&sylist.size()!=0){
			HashMap<String, String>symap=sylist.get(0);
			String di="";
			String xian="";
			if(symap!=null){
				String a=symap.get("DI");
				if(a!=null&&a!=""){
					String []arr1=a.split("-");
					if(arr1.length>=2){
						di=arr1[1];
					}
				}
				String b=symap.get("XIAN");
				if(b!=null&&b!=""){
					String []arr2=b.split("-");
					if(arr2.length>=2){
						xian=arr2[1];
					}
				}
				String xiang=symap.get("XIANG");
				String cun=symap.get("CUN");
				if(di!=""){
					result=result+di+",";
				}
				if(xian!=""){
					result=result+xian+",";
				}
				if(xiang!=null&&xiang!=""){
					result=result+xiang+",";
				}
				if(cun!=null&&cun!=""){
					result=result+cun;
				}
				if(result.endsWith(",")){
					result=result.substring(0,result.length()-1);
				}

			}
			location.setText(result);
		}
	}
	/**每木检尺调查*/
	protected void ShowMmjcDialog() {
		SlzylxqcMmjcDialog dialog=new SlzylxqcMmjcDialog(context, ydhselect, mmjcjlLlist);
		BussUtil.setDialogParamsFull(context, dialog);
	}
	/** GPS航迹调查记录 */
	private void ShowGpshjdcDialog() {
		HashMap<String, String> map = null;
		if (gpshjdcLlist.size() > 0) {
			map = gpshjdcLlist.get(0);
		}else{
			map=new HashMap<String, String>();
			map.put("YDH", ydhselect);
			map.put("CJKSSJ", "");
			map.put("CJJSSJ", "");
			map.put("CJJL", "");
			map.put("XZYDTJ", "");
		}
		SlzylxqcGpshjdcDialog gpshjdialog = new SlzylxqcGpshjdcDialog(context, map, ydhselect);
		BussUtil.setDialogParamsFull(context, gpshjdialog);
	}

	/** 引点位置图 */
	protected void ShowYindwztDialog() {
		HashMap<String, String> map = DataBaseHelper.searchLxqcYindwzgyData(
				context, ydhselect);
		SlzylxqcYindwztDialog yindwztdialog = new SlzylxqcYindwztDialog(
				context, R.style.Dialog, yindwztLlist, map, ydhselect);
		BussUtil.setDialogParamsFull(context, yindwztdialog);
	}

	/** 样地位置图界面 */
	private void ShowYdwztDialog() {
		HashMap<String, String> map = DataBaseHelper.searchLxqcYdwztYdtzData(context, ydhselect);
		SlzylxqcYdwztDialog ydwztdialog = new SlzylxqcYdwztDialog(context,ydwztLlist, map, ydhselect);
		BussUtil.setDialogParamsFull(context, ydwztdialog);
	}

	/** 跨角林调查记录界面 */
	protected void ShowKjldcDialog() {
		SlzylxqcKjldcjlDialog kjldialog = new SlzylxqcKjldcjlDialog(context, kjljldcLlist,
				ydhselect,myFeture.getPath());
		kjldialog.show();
		BussUtil.setDialogParamsFull(context, kjldialog);
	}

	/** 周界测量记录界面
	 * @param ' */
	private void ShowZjcldcDialog() {
		SlzylxqcZjcljlDialog yxcldialog = new SlzylxqcZjcljlDialog(context,zjcljldcLlist,zjgylist, ydhselect);
		BussUtil.setDialogParamsFull(context, yxcldialog);
	}

	/** 森林灾害情况界面 */
	private void ShowSlzhqcDialog() {
		SlzylxqcSlzhqkDialog dialog=new SlzylxqcSlzhqkDialog(context, slzhqkLlist, ydhselect);
		BussUtil.setDialogParamsFull(context, dialog);
	}



	/** 下木调查记录 */
	protected void ShowXmdcDialog(List<HashMap<String, String>> list) {
		SlzylxqcXmdcDialog xmdcdialog=new SlzylxqcXmdcDialog(context, ydhselect, list);
		BussUtil.setDialogParamsFull(context, xmdcdialog);
	}

	/**拍照*/
	public void takephoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			/** 指定存放拍摄照片的位置*/
			File file = createImageFile();
			if (file != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				activity.startActivityForResult(intent, TAKE_PICTURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 存放文件位置 */
	private File createImageFile() throws IOException {
		File image = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String fileName=MyApplication.resourcesManager.getLxqcImagePath("4");
			String picname = MyApplication.resourcesManager.getLxqcImagename(ydhselect, "2", "");
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			image = new File(fileName + "/" + picname);
		} else {
			ToastUtil.setToast(context, "没有SD卡");
		}
		return image;
	}
}
