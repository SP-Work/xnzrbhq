package com.otitan.xnbhq.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.Spannable;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.FeatureLayer.SelectionMode;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.EdFeatureResultAdapter;
import com.otitan.xnbhq.dao.XuJiModel;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.edite.activity.ImageActivity;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.EdLineAdapter;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.EDUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.XuJiUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;
/**
 * Created by li on 2016/5/26.
 * 二调小班编辑页面
 */
public class ED_XBEditActivity extends Activity {

	private static final String EXTRA_LINES = "EXTRA_LINES";

	private static final int TAKE_PICTURE = 0x000001;

	private ListView listView;
	private List<Line> mLines;
	TextView title, ydd, xbkzd, xbqh, chldc, ymdc, jgdc,calculte;
	/**显示小班号*/
	TextView tv_xbh;
	private EdLineAdapter mAdapter;
	/** 数据是否保存 */
	public boolean saveflag = false;
	private Context mContext;
	// private ViewGroup rootView;
	private GeodatabaseFeature selGeoFeature;
	/** 保存查询图层 */
	GeodatabaseFeatureTable gdbft = null;
	// private FeatureLayer featureLayer;
	public static FeatureLayer featureLayer;
	//public static Map<String, Object> attribute = null;
	//private List<Field> fieldList = null;
	/** 当前小班唯一识别字段集合 */
	public List<Row> gcmc;
	/** 当前小班小班号 */
	private String currentxbh = "", kzdh = "";
	private TextView btnreturn, photograph, seepicture;
	/** 数据类型 */
	private String type;
	private Geometry selectGeometry = null;
	public long featureId;
	/** 小班空间查询结果 */
	List<GeodatabaseFeature> list = new ArrayList<GeodatabaseFeature>();
	/** FeatureLayer图层 */
	public static FeatureTemplate layerTemplate;
	/** 工程名称 */
	private String pname = "工程名称";
	/** 图片字段 */
	private Line pcLine;
	private String picname;
	private EditText zpeditText;
	/** 图片保存地址 */
	private String picPath = "";
	/** 工程所在地址 */
	private String path;
	
	/** 是否需要计算 */
	boolean iscalculate=false;
	/**面信息*/
	MyFeture myFeture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ed_attributedite_fragment);
		
		mContext = ED_XBEditActivity.this;
		initData(savedInstanceState);
		
		intialView();// 初始化控件
		
		getXbhData(selGeoFeature);// 获取小班号

	}

	/** 初始化数据 */
	public void initData(Bundle savedInstanceState) {
		myFeture = (MyFeture) getIntent().getSerializableExtra("myfeture");
		
		path = myFeture.getPath();
		pname = myFeture.getPname();
		
		type = getIntent().getStringExtra("type");// 数据类型

		selGeoFeature = (GeodatabaseFeature) myFeture.getFeature();
		selectGeometry = selGeoFeature.getGeometry();
		featureLayer = myFeture.getMyLayer().getLayer();
		//fieldList = selGeoFeature.getTable().getFields();
		
		if (savedInstanceState == null) {
			mLines = createLines(selGeoFeature);
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
	}

	/** 初始化控件 */
	private void intialView() {
		listView = (ListView) findViewById(R.id.listView_xbedit);

		mAdapter = new EdLineAdapter(ED_XBEditActivity.this, mLines, type, selGeoFeature,featureLayer);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);
		btnreturn.setOnClickListener(new MyListener());
		photograph = (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new MyListener());
		seepicture = (TextView) findViewById(R.id.ld_see_pic);// 图片浏览
		seepicture.setOnClickListener(new MyListener());
		// 小班号
		tv_xbh = (TextView) findViewById(R.id.tv_xbh);
		// 标题
		title = (TextView) findViewById(R.id.tv_title);
		//计算
		calculte = (TextView) findViewById(R.id.calculate);
		calculte.setOnClickListener(new MyListener());
		// 样地点
		ydd = (TextView) findViewById(R.id.tv_ydd);
		ydd.setOnClickListener(new MyListener());
		// 小班控制点
		xbkzd = (TextView) findViewById(R.id.tv_xbkzd);
		xbkzd.setOnClickListener(new MyListener());
		// 小班区划
		xbqh = (TextView) findViewById(R.id.tv_xbqh);
		xbqh.setOnClickListener(new MyListener());
		// 小班成活率调查表
		chldc = (TextView) findViewById(R.id.tv_chldc);
		chldc.setOnClickListener(new MyListener());
		// 角规调查表
		jgdc = (TextView) findViewById(R.id.tv_jgdc);
		jgdc.setOnClickListener(new MyListener());
		// 样木调查表
		ymdc = (TextView) findViewById(R.id.tv_ymdc);
		ymdc.setOnClickListener(new MyListener());

		if (type.equals("XIAOBAN_PY")) {
			title.setText("区划小班面");
			xbqh.setVisibility(View.INVISIBLE);
			ymdc.setVisibility(View.INVISIBLE);
		} else if (type.equals("YDD_PT")) {
			title.setText("样地点");
			ydd.setVisibility(View.INVISIBLE);
			xbkzd.setVisibility(View.INVISIBLE);
			ymdc.setVisibility(View.VISIBLE);
		} else if (type.equals("XBKZD_PT")) {
			title.setText("小班控制点");
			ydd.setVisibility(View.INVISIBLE);
			xbkzd.setVisibility(View.INVISIBLE);
			ymdc.setVisibility(View.INVISIBLE);
		}
	}

	/** 填充数据 */
	private ArrayList<Line> createLines(GeodatabaseFeature feature) {
		ArrayList<Line> lines = new ArrayList<Line>();
		int size = feature.getAttributes().size();
		List<Field> fieldList = feature.getTable().getFields();
		for (int i = 0; i < size; i++) {
			Field field = fieldList.get(i);
			String name = field.getName();
			Line line = new Line();
			line.setNum(i);
			line.setTview(field.getAlias());
			line.setKey(name);
			line.setfLength(field.getLength());
			line.setFieldType(field.getFieldType());
			CodedValueDomain domain = (CodedValueDomain) field.getDomain();
			line.setDomain(domain);
	        boolean floag = field.isNullable();
	        line.setNullable(floag);

			Object obj = feature.getAttributes().get(name);
			if (obj != null) {
				String value = obj.toString();
				// 1:选择类型字段 2:数字类型 3：文字类型 4：日期选择
				String fieldtype = EDUtil.getEdAttributetype(mContext, name,type);
				if (fieldtype.equals("1")) {
					List<Row> rowlist = null;
					if (name.equals("YSSZ") || name.equals("ZYZCSZ") || name.equals("SZDM")) {
						rowlist = EDUtil.getEdAttributeList(mContext, "YSSZ",type);
					} else {
						rowlist = EDUtil.getEdAttributeList(mContext, name,type);
					}

					if (name.equals("XIANG")) {
						// 获取县名
						String xian = "";
						for (Line line1 : lines) {
							if (line1.getKey().equals("XIAN")) {
								xian = line1.getValue();
								break;
							}
						}
						List<Row> xianglist = DataBaseHelper.getXiangList(mContext, xian);
						rowlist = xianglist;
					}
					if (name.equals("CUN")) {
						// 获取县名
						String xiang = "";
						for (Line line1 : lines) {
							if (line1.getKey().equals("XIANG")) {
								xiang = line1.getValue();
								break;
							}
						}
						List<Row> cunlist = DataBaseHelper.getCunList(mContext,xiang);
						rowlist = cunlist;
					}

					if (rowlist.size() > 0) {
						for (Row row : rowlist) {
							if (row.getId().equals(value)) {
								value = row.getName();
								line.setText(value);
								line.setValue(row.getId());
							}
						}
					}
				} else {
					line.setText(value);
					line.setValue(value);
				}
			}
			lines.add(line);
		}
		return lines;
	}

	class MyListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btnreturn:
				/* 返回按钮 */
				ED_XBEditActivity.this.finish();
				break;
			case R.id.ld_see_pic:
				/* 图片浏览 */
				lookpictures();
				break;
			case R.id.calculate:
				calculate();
				break;
				/* 样地点 */
			case R.id.tv_ydd:
				showYYD();
				break;
				/* 小班控制点 */
			case R.id.tv_xbkzd:
				showXBKZD();
				break;
				/* 小班区划 */
			case R.id.tv_xbqh:
				featureLayer = BaseActivity.myLayer.getLayer();
				selGeoFeature = (GeodatabaseFeature)featureLayer.getFeature(BaseActivity.selGeoFeature.getId()) ;
				mLines = createLines(selGeoFeature);
				getXbhData(selGeoFeature);
				xbqh.setVisibility(View.INVISIBLE);
				ydd.setVisibility(View.VISIBLE);
				xbkzd.setVisibility(View.VISIBLE);
				ymdc.setVisibility(View.INVISIBLE);
				jgdc.setVisibility(View.INVISIBLE);
				title.setText("小班区划");
				type = "XIAOBAN_PY";
				
				mAdapter = new EdLineAdapter(ED_XBEditActivity.this, mLines, type,selGeoFeature, featureLayer);
				listView.setAdapter(mAdapter);

				break;
			/* 角规调查 */
			case R.id.tv_jgdc:
				Intent jgdcintent = new Intent(ED_XBEditActivity.this,Ed_JgdcActivity.class);
				jgdcintent.putExtra("XBH", currentxbh);
				jgdcintent.putExtra("KZDH", kzdh);
				jgdcintent.putExtra("path", path);
				startActivity(jgdcintent);
				break;
			/* 样木调查 */
			case R.id.tv_ymdc:
				Intent ymdcintent = new Intent(ED_XBEditActivity.this,ED_YmdcActivity.class);
				ymdcintent.putExtra("XBH", currentxbh);
				ymdcintent.putExtra("YDH",selGeoFeature.getAttributeValue("YDH").toString());
				ymdcintent.putExtra("path", path);
				startActivity(ymdcintent);
				break;
			default:
				break;
			}
		}
	}
	
	/** 计算计算项 */
	public void calculate() {
		ProgressDialogUtil.startProgressDialog(mContext);

    	/**小班株数计算 */
		if((BaseActivity.selectFeatureAts.get("GQZS")+"").equals("null")||(BaseActivity.selectFeatureAts.get("MIANJI")+"").equals("null")){
			ToastUtil.setToast((Activity) mContext, "请填写公顷株数或面积");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		double gqzs=Double.parseDouble(BaseActivity.selectFeatureAts.get("GQZS")+"");
		double area=Double.parseDouble(BaseActivity.selectFeatureAts.get("MIANJI")+"");
	
		if(gqzs==0||area==0){
			ToastUtil.setToast( mContext, "请填写公顷株数或面积");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		int xbzs=(int) (gqzs*area);
		int rexbzs=Integer.parseInt(BaseActivity.selectFeatureAts.get("XBZS").toString()) ;
		if(xbzs!=rexbzs){
			BaseActivity.selectFeatureAts.put("XBZS", xbzs);
			iscalculate=true;
		}
		
		/**林木蓄积计算 */
		lmxjcal(area);
	
		
		/**散生木蓄积计算 */
		ssmxjcal();
		/**小班蓄积计算 */
		xbxjcal();
		if(iscalculate){
			upDate();
			iscalculate=false;
		}
		ProgressDialogUtil.stopProgressDialog(mContext);
	}
	
	/** 更新数据 */
	private void upDate() {
		long featureid = BaseActivity.selGeoFeature.getId();
		Graphic updateGraphic = new Graphic(
				BaseActivity.selGeoFeature.getGeometry(),
				BaseActivity.selGeoFeature.getSymbol(),
				BaseActivity.selectFeatureAts);
		try
		{
			BaseActivity.myLayer.getTable().updateFeature(
					featureid, updateGraphic);
		} catch (Exception e)
		{
			ToastUtil.setToast( mContext, "计算失败");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;

		}
		ToastUtil.setToast( mContext, "计算完成");
		mLines = createLines(BaseActivity.selGeoFeature);
		mAdapter.setData(ED_XBEditActivity.this, mLines, type);
		mAdapter.notifyDataSetChanged();
	}
	
	/**小班蓄积计算 */
	private void xbxjcal() {
		if((BaseActivity.selectFeatureAts.get("SSMXJ")+"").equals("null")||(BaseActivity.selectFeatureAts.get("LMXJ")+"").equals("null")){
			ToastUtil.setToast( mContext, "请先计算林木蓄积和散生木蓄积");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		double ssmxj=Double.parseDouble(BaseActivity.selectFeatureAts.get("SSMPJXJ")+"");
		double lmxj=Double.parseDouble(BaseActivity.selectFeatureAts.get("LMXJ")+"");
		double xbxj=ssmxj+lmxj;
		double rexbxj=Double.parseDouble(BaseActivity.selectFeatureAts.get("XBXJ").toString());
		if(xbxj!=rexbxj){
			BaseActivity.selectFeatureAts.put("XBXJ", xbxj);
			iscalculate=true;
		}
	}

	
	/**散生木蓄积计算 */
	private void ssmxjcal() {
		if((BaseActivity.selectFeatureAts.get("SSMSZ")+"").equals("null")){
			ToastUtil.setToast( mContext, "请填写散生木树种");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		if((BaseActivity.selectFeatureAts.get("SSMPJXJ")+"").equals("null")){
			ToastUtil.setToast( mContext, "请填写散生木平均胸径");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		if((BaseActivity.selectFeatureAts.get("SSMPJG")+"").equals("null")){
			ToastUtil.setToast( mContext, "请填写散生木平均树高");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		if((BaseActivity.selectFeatureAts.get("SSMZS")+"").equals("null")){
			ToastUtil.setToast( mContext, "请填写散生木株数");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		String szdm=BaseActivity.selectFeatureAts.get("SSMSZ")+"";
		double xj=Double.parseDouble(BaseActivity.selectFeatureAts.get("SSMPJXJ")+"");
		double sg=Double.parseDouble(BaseActivity.selectFeatureAts.get("SSMPJG")+"");
		int zs=Integer.parseInt(BaseActivity.selectFeatureAts.get("SSMZS")+"");
		XuJiModel ym=new XuJiModel();
		DecimalFormat df=new DecimalFormat("0.00");
		ym.setSZMC(szdm);
		ym.setXJ(xj);
		ym.setSG(sg);
		double ymxj=XuJiUtil.EYXuJi(ym);
		String zxj=df.format(ymxj*zs);
		String rexbzs=df.format(Double.parseDouble(BaseActivity.selectFeatureAts.get("SSMXJ")+""));
		if(zxj!=rexbzs){
			BaseActivity.selectFeatureAts.put("SSMXJ", zxj);
			iscalculate=true;
		}
		
	}
	
	/**林木蓄积计算 */
	private void lmxjcal(double area) {
		if((BaseActivity.selectFeatureAts.get("GQXJ")+"").equals("null")){
			ToastUtil.setToast( mContext, "请填写公顷蓄积");
			ProgressDialogUtil.stopProgressDialog(mContext);
			return;
		}
		
		double gqxj=Double.parseDouble(BaseActivity.selectFeatureAts.get("GQXJ")+"");
		double lmxj= gqxj*area;
		double relmxj=Double.parseDouble(BaseActivity.selectFeatureAts.get("LMXJ")+"");
		if(lmxj!=relmxj){
			BaseActivity.selectFeatureAts.put("LMXJ", lmxj);
			iscalculate=true;
		}
	}


	/** 展示样地点数据 */
	private void showYYD() {
		queryByGeometry(selGeoFeature.getGeometry(), "YDH");
	}

	/** 展示小班控制点数据 */
	private void showXBKZD() {
		queryByGeometry(selGeoFeature.getGeometry(), "KZDH");
	}

	/** 离线的空间查询 */
	public void queryByGeometry(Geometry geometry, final String fieldname) {
		list.clear();
		QueryParameters queryParams = new QueryParameters();
		queryParams.setOutFields(new String[] { "*" });
		queryParams.setSpatialRelationship(SpatialRelationship.CONTAINS);
		queryParams.setGeometry(geometry);
		queryParams.setReturnGeometry(true);
		queryParams.setOutSpatialReference(ErDiaoActivity.xblayer.getSpatialReference());
		if (fieldname.equals("YDH")) {
			featureLayer = ErDiaoActivity.yddlayer;
		} else {
			featureLayer = ErDiaoActivity.xbkzdlayer;
		}
		featureLayer.selectFeatures(queryParams, SelectionMode.NEW,
				new CallbackListener<FeatureResult>() {

					@Override
					public void onError(Throwable arg0) {
						ToastUtil.setToast(mContext, "查询出错");
					}

					@Override
					public void onCallback(final FeatureResult result) {
						if (result.featureCount() > 0) {
							Iterator<Object> iterator = result.iterator();
							while (iterator.hasNext()) {
								GeodatabaseFeature geoFeature = (GeodatabaseFeature) iterator.next();
								list.add(geoFeature);
							}
						}

						runOnUiThread(new Runnable() {
							// 弹出结果展示窗口
							@Override
							public void run() {
								showListFeatureResult(list, fieldname);
							}
						});

					}
				});
	}

	/**
	 * 弹出结果展示窗口 fieldname listview展示的字段
	 */
	public void showListFeatureResult(final List<GeodatabaseFeature> list,final String fieldname) {

		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.ed_queryresult);
		dialog.setCanceledOnTouchOutside(true);
		final TextView dialogtitle = (TextView) dialog.findViewById(R.id.tv_querytitle);
		final TextView dialogcontent = (TextView) dialog.findViewById(R.id.tv_content);
		// 添加
		Button addyd = (Button) dialog.findViewById(R.id.btn_addyd);
		if (fieldname.equals("YDH")) {
			dialogtitle.setText("样地号");
			type = "YDD_PT";
			gdbft = (GeodatabaseFeatureTable) ErDiaoActivity.ydd_featuretable;

			featureLayer = ErDiaoActivity.yddlayer;

			addyd.setVisibility(View.INVISIBLE);
		} else {
			dialogtitle.setText("控制点号");
			type = "XBKZD_PT";
			addyd.setText("添加小班控制点");
			gdbft = (GeodatabaseFeatureTable) ErDiaoActivity.xbkzd_featuretable;
			featureLayer = ErDiaoActivity.xbkzdlayer;
		}
		// 判断小班是否存在样地点或小班控制点
		if (list.size() > 0) {
			if (fieldname.equals("YDH")) {
				dialogcontent.setText("样地数：" + list.size());
			} else {
				dialogcontent.setText("小班控制点数：" + list.size());
			}
			ListView itemlistview = (ListView) dialog.findViewById(R.id.ed_featureresult_listview);
			EdFeatureResultAdapter adapter = new EdFeatureResultAdapter(mContext, list, fieldname);
			itemlistview.setAdapter(adapter);

			itemlistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					dialog.dismiss();
					xbqh.setVisibility(View.VISIBLE);
					if (fieldname.equals("YDH")) {
						title.setText("样地点");
						ymdc.setVisibility(View.VISIBLE);
						xbqh.setVisibility(View.VISIBLE);
						ydd.setVisibility(View.INVISIBLE);
						xbkzd.setVisibility(View.INVISIBLE);
						jgdc.setVisibility(View.INVISIBLE);
					} else {
						title.setText("小班控制点");
						ydd.setVisibility(View.INVISIBLE);
						xbqh.setVisibility(View.VISIBLE);
						ymdc.setVisibility(View.INVISIBLE);
						xbkzd.setVisibility(View.INVISIBLE);
						jgdc.setVisibility(View.VISIBLE);
						Object obj = list.get(position).getAttributes().get(fieldname);
						kzdh = obj == null ? "" :obj.toString();
					}
					
					selGeoFeature = (GeodatabaseFeature) list.get(position);
					//fieldList = selGeoFeature.getTable().getFields();
					mLines = createLines(selGeoFeature);
					mAdapter = new EdLineAdapter(ED_XBEditActivity.this, mLines, type,selGeoFeature, featureLayer);
					listView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					getXbhData(selGeoFeature);
				}
			});

		} else {

			if (fieldname.equals("YDH")) {
				dialogcontent.setText("样地数：0");
			} else {
				dialogcontent.setText("小班控制点数：0");
			}
		}

		addyd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();
				
				// 如没有获取当前定位点
				if (BaseActivity.currentPoint == null) {
					ToastUtil.setToast(mContext, "当前定位失败，请稍后重试");
					return;
				}
				Point newydd = BaseActivity.currentPoint;
				List<Field> fieldList = gdbft.getFields();
				Map<String, Object> att = new HashMap<String, Object>();
				for (Field f : fieldList) {
					att.put(f.getName(), "");
				}
				long id = addNewYddtoGeodatabase(newydd, att);
				if (id != -1) {
					xbqh.setVisibility(View.VISIBLE);
					if (fieldname.equals("YDH")) {
						title.setText("样地点");
						ymdc.setVisibility(View.VISIBLE);
						jgdc.setVisibility(View.INVISIBLE);
						type = "YDD_PT";
					} else {
						title.setText("小班控制点");
						xbkzd.setVisibility(View.INVISIBLE);
						ydd.setVisibility(View.INVISIBLE);
						jgdc.setVisibility(View.VISIBLE);
						type = "XBKZD_PT";
					}
					selGeoFeature = (GeodatabaseFeature) featureLayer.getFeature(id);
					mLines = createLines(selGeoFeature);
					mAdapter.setData(ED_XBEditActivity.this, mLines, type);
					mAdapter.notifyDataSetChanged();
					ToastUtil.setToast(mContext, "添加成功");
				}
			}

		});

		BussUtil.setDialogParams(mContext, dialog, 0.5, 0.5);
	}

	/** 添加样地点到Geodatabase */
	private long addNewYddtoGeodatabase(Point point,Map<String, Object> editAttributes) {
		try {
			Boolean isWithIn = GeometryEngine.within(point, selectGeometry,BaseActivity.mapView.getSpatialReference());
			if (isWithIn) {

				if (list.size() > 0) {
					GeodatabaseFeature lastydd = list.get(list.size() - 1);
					DecimalFormat df = new DecimalFormat("0000000");
					String t = df.format(Long.parseLong((String) lastydd
							.getAttributes().get("KZDH")) + 1);
					kzdh = t;
					editAttributes.put("DWHZB", point.getX());// 定位横坐标
					editAttributes.put("DWZZB", point.getY());
					editAttributes.put("KZDH", kzdh);

				} else {
					kzdh = currentxbh + "01";
					editAttributes.put("DWHZB", point.getX());// 定位横坐标
					editAttributes.put("DWZZB", point.getY());
					editAttributes.put("KZDH", kzdh);
				}

				Graphic addedGraphic = new Graphic(point, null,editAttributes);
				long id = featureLayer.getFeatureTable().addFeature(addedGraphic);
				if (id > 0) {
					ToastUtil.setToast(mContext, "添加成功");
				}
				return id;
			} else {
				ToastUtil.setToast(mContext, "添加失败,添加的样地点或控制点不在该小班范围内");
				return -1;
			}

		} catch (TableException e) {
			ToastUtil.setToast(mContext, "添加失败");
			e.printStackTrace();
			return -1;
		}

	}

	/** 获取FeatureLayer图层样式 */
	public static void getEditSymbol(FeatureLayer flayer) {
		String typeIdField = ((GeodatabaseFeatureTable) flayer
				.getFeatureTable()).getTypeIdField();
		if (typeIdField.equals("")) {
			List<FeatureTemplate> featureTemp = ((GeodatabaseFeatureTable) flayer
					.getFeatureTable()).getFeatureTemplates();

			for (FeatureTemplate featureTemplate : featureTemp) {
				GeodatabaseFeature g;
				try {
					g = ((GeodatabaseFeatureTable) flayer.getFeatureTable())
							.createFeatureWithTemplate(featureTemplate, null);
					BaseActivity.layerSymbol = flayer.getRenderer()
							.getSymbol(g);
					BaseActivity.layerTemplate = featureTemplate;
				} catch (TableException e) {
					e.printStackTrace();
				}
			}
		} else {
			List<FeatureType> featureTypes = ((GeodatabaseFeatureTable) flayer
					.getFeatureTable()).getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				FeatureTemplate[] templates = featureType.getTemplates();
				for (FeatureTemplate featureTemplate : templates) {
					GeodatabaseFeature g;
					try {
						g = ((GeodatabaseFeatureTable) flayer.getFeatureTable())
								.createFeatureWithTemplate(featureTemplate,
										null);
						BaseActivity.layerSymbol = flayer.getRenderer()
								.getSymbol(g);
						BaseActivity.layerTemplate = featureTemplate;
					} catch (TableException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/** 图片浏览*/
	public void lookpictures(){
		
		List<File> lst = MyApplication.resourcesManager.getImages(picPath);
		if(lst.size() == 0){
			ToastUtil.setToast(mContext, "没有图片");
			return;
		}
		
		Intent intent = new Intent(mContext, ImageActivity.class);
		intent.putExtra("xbh", currentxbh);
		intent.putExtra("picPath", picPath);
		intent.putExtra("type", "0");
		startActivity(intent);
	}
	
	/** 图片浏览 */
	public void lookpictures(Line line) {
		try {
			List<File> lst = MyApplication.resourcesManager.getImages(picPath);
			if (lst.size() == 0) {
				ToastUtil.setToast(this, "没有图片");
				return;
			}
			Intent intent = new Intent(mContext, ImageActivity.class);
			intent.putExtra("xbh", currentxbh);
			intent.putExtra("picPath", picPath);
			intent.putExtra("type", "0");// 判断是否有多个图片
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 拍照 */
	private String mCurrentPhotoPath;
	
	public void takephoto(Line line,EditText editText){
		this.pcLine = line;
		this.picname = line.getKey();
		this.zpeditText = editText;
		takephoto(new View(mContext));
	}

	public void takephoto(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String time = (String) DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA));
		String picname = time + ".jpg";
		File file = new File(picPath + "/" + picname);
		mCurrentPhotoPath = picPath + "/" + picname;
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		// 更新照片编号字段
		startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

			dealPhotoFile(mCurrentPhotoPath);// 为图片添加标记并保存

		}
	}

	/** 获取图片地址 */
	public String getImagePath() {
		String path = null;
		try {
			String root = ResourcesManager.image;
			path = MyApplication.resourcesManager.getFolderPath(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	/** 图片添加水印 */
	private void dealPhotoFile(final String file) {
		PhotoTask task = new PhotoTask(file);
		task.start();
	}

	/** 图片添加水印 */
	private class PhotoTask extends Thread {
		private String file;

		public PhotoTask(String file) {
			this.file = file;
		}

		@Override
		public void run() {
			BufferedOutputStream bos = null;
			Bitmap icon = null;
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(file, options); // ��ʱ����bmΪ��
				int height1 = options.outHeight;
				int width1 = options.outWidth;
				float percent = height1 > width1 ? height1 / 960f
						: width1 / 960f;

				if (percent < 1) {
					percent = 1;
				}
				// int width = (int) (width1 / percent);
				// int height = (int) (height1 / percent);
				icon = Bitmap.createBitmap(width1, height1,
						Bitmap.Config.ARGB_8888);

				Canvas canvas = new Canvas(icon);
				Paint photoPaint = new Paint();
				photoPaint.setDither(true);
				// photoPaint.setFilterBitmap(true);
				options.inJustDecodeBounds = false;

				Bitmap prePhoto = BitmapFactory.decodeFile(file);
				if (percent > 1) {
					prePhoto = Bitmap.createScaledBitmap(prePhoto, width1,
							height1, true);
				}

				canvas.drawBitmap(prePhoto, 0, 0, photoPaint);

				if (prePhoto != null && !prePhoto.isRecycled()) {
					prePhoto.recycle();
					prePhoto = null;
					System.gc();
				}

				Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
						| Paint.DEV_KERN_TEXT_FLAG);
				textPaint.setTextSize(50.0f);
				textPaint.setTypeface(Typeface.DEFAULT);
				textPaint.setColor(Color.RED);
				// textPaint.setShadowLayer(3f, 1, 1, Color.DKGRAY);

				String mark = "小班号:" + currentxbh + "  拍照时间:"
						+ getCurrTime("yyyy-MM-dd HH:mm:ss");
				float textWidth = textPaint.measureText(mark);
				canvas.drawText(mark, width1 - textWidth - 10, height1 - 26,
						textPaint);

				bos = new BufferedOutputStream(new FileOutputStream(file));

				// int quaility = (int) (100 / percent > 80 ? 80 : 100 /
				// percent);
				icon.compress(CompressFormat.JPEG, 95, bos);
				bos.flush();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (icon != null && !icon.isRecycled()) {
					icon.recycle();
					icon = null;
					System.gc();
				}
			}

			runOnUiThread(new Runnable() {
				public void run() {
					String bftxt = "";
					if(pcLine != null){
						bftxt = pcLine.getText();
					}
					
					if(bftxt == null || bftxt.equals("")){
						File file = new File(mCurrentPhotoPath);
						if(file.exists()){
							zpeditText.setText(file.getName());
						}
					}else{
						File file = new File(mCurrentPhotoPath);
						if(file.exists()){
							zpeditText.setText(bftxt+","+ file.getName());
						}
					}
					
					setEditTextCursorLocation(zpeditText);
				}
			});
		}
	}

	/** 更新照片编号 */
	public void updateZp(String pctext, Line line, String bfText) {
		Map<String, Object> attribute = featureLayer.getFeature(selGeoFeature.getId()).getAttributes(); 
		if (bfText == null) {
			if (line.getText().equals("")) {
				return;
			}
		} else {
			String txt = line.getText();
			if (txt != null && txt.equals(bfText)) {
				return;
			}
		}
		boolean flag = true;
		int length = pctext.length();
		int size = 0;
		for (Field ff : selGeoFeature.getTable().getFields()) {
			if (ff.getName().equals(line.getKey())) {
				size = ff.getLength();
				break;
			}
		}

		if (length > size) {
			ToastUtil.setToast(mContext, "数据长度超过数据库规定长度");
			return;
		}

		attribute.put(line.getKey(), pctext);

		Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),
				selGeoFeature.getSymbol(), attribute);
		FeatureTable featureTable = featureLayer.getFeatureTable();
		try {
			long featureid = selGeoFeature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
		} catch (TableException e) {
			flag = false;
			e.printStackTrace();
		}
		if (flag) {
			line.setText(pctext);
			mAdapter.notifyDataSetChanged();
			ToastUtil.setToast((Activity) mContext, "照片编号更新成功");
		} else {
			ToastUtil.setToast((Activity) mContext, "照片编号更新失败");
		}
	}

	/** 获取当前时间 */
	@SuppressLint("SimpleDateFormat")
	private static String getCurrTime(String pattern) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		return (new SimpleDateFormat(pattern)).format(new Date());
	}

	/** 获取小班号数据 */
	public void getXbhData(GeodatabaseFeature feature) {
		/**获取图斑的唯一编号*/
		Object obj = selGeoFeature.getAttributes().get("WYBH");
		if(obj != null){
			currentxbh = obj.toString();
		}
		DecimalFormat df = new DecimalFormat("0.00");
		double area = selGeoFeature.getGeometry().calculateArea2D();
		tv_xbh.setText(df.format(Math.abs(area))+"/"+df.format(Math.abs(area*0.0015))+
				"/"+df.format(Math.abs(area*0.0001))+"  平方米/亩/公顷");
		picPath = getImagePath(path);
	}

	/** 获取图片保存地址 */
	public String getImagePath(String path) {
		File file = new File(path);
		String path1 = file.getParent()+ "/images";
		File file2 = new File(path1);
		boolean flag = file2.exists();
		if(!flag){
			file2.mkdirs();
		}
		if(currentxbh==null || currentxbh.equals("")){
			picPath = path1;
		}else{
			String path2 = file2.getPath()+"/"+currentxbh;
			File file3 = new File(path2);
			if(!file3.exists()){
				file3.mkdirs();
			}
			picPath = file3.getPath();
		}
		return picPath;
	}

	/** 把edittext的光标放置在最后 */
	public void setEditTextCursorLocation(EditText et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

}
