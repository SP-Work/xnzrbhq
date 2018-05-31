package com.otitan.xnbhq.edite.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.listviewinedittxt.YddcLineAdapter;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 小班图片
 */
public class YzlYddActivity extends BaseEditActivity {

	View parentView;
	FeatureLayer featureLayer;
	/** FeatureLayer图层 */
	private FeatureTemplate layerTemplate;
	private List<Field> ydd_fields;
	private Symbol layerSymbol;
	private Renderer layerRenderer;
	private Map<String, Object> layerFeatureAts;

	private Context mContext;
	/**与样地点关联的小班唯一号*/
	public String xbwybh;
	public String currentxbh;

	public long numSize = 0;
	private long feutureID;

	private YddcLineAdapter mAdapter;
	private ArrayList<Line> mLines;

	GeodatabaseFeature geoFeature;
	/** 选择图斑的属性信息 */
	//public static Map<String, Object> geoFeatureAts = null;
	//private List<Field> fieldList = null;

	private TextView yddcxbh,mmdc;
	/**返回、图片浏览、拍照*/
	private TextView back,piclook;

	MyFeture xbmyFeture = null;
	private String yddLayername = "";
	DecimalFormat format = new DecimalFormat("0.000000");
	/**小班面*/
	private TextView yddc_xbm;
	/**小班设计密度*/
	public double sjmdSize = 0;
	private MyLayer myLayer = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_yzlyddc, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		mContext = YzlYddActivity.this;

		xbmyFeture = (MyFeture) getIntent().getSerializableExtra("myfeture");
		yddLayername = getIntent().getStringExtra("yddname");
		xbwybh = getIntent().getStringExtra("xbh");
		numSize = getIntent().getLongExtra("numSize", 0);

		picPath = getImagePath(path);

		double lon = BaseActivity.currentPoint.getX();
		double lat = BaseActivity.currentPoint.getY();

		for(MyLayer layer : BaseActivity.layerNameList){
			String name = layer.getCname();
			Type type = layer.getTable().getGeometryType();
			String lname = layer.getTable().getTableName();
			if(type.equals(Geometry.Type.POINT) && name.equals(xbmyFeture.getCname()) && lname.equals(yddLayername)){
				myLayer = layer;
				featureLayer = layer.getLayer();
				getEditSymbo(featureLayer);
				//getGeometryInfo(xbmyFeture.getFeature().getGeometry());
				break;
			}
		}

		Point point = new Point(Double.parseDouble(format.format(lon)), Double.parseDouble(format.format(lat)));
		int count1 = xbmyFeture.getFeature().getAttributes().keySet().size();
		int count2 = ydd_fields.size();

		List<Field> lst1 = xbmyFeture.getFeature().getTable().getFields();
		for(int k=0;k<count2;k++){
			Field field2 = ydd_fields.get(k);
			String alias2 = field2.getAlias();
			String key2 = field2.getName();

			for(int i=0;i<count1;i++){
				Field field1 = lst1.get(i);
				String alias1 = field1.getAlias();
				String key1 = field1.getName();

				if(alias1.contains("设计造林密度") || key2.contains("SJZLMD")){
					Object obj1 = xbmyFeture.getFeature().getAttributeValue(key1);
					if(obj1 != null){
						boolean flag = Util.CheckStrIsDouble(obj1.toString());
						if(flag){
							sjmdSize = Double.parseDouble(obj1.toString());
						}
					}
				}

				if(alias1.contains(alias2) || alias2.contains(alias1) || key2.equals(key1)){
					layerFeatureAts.put(key2, xbmyFeture.getFeature().getAttributes().get(key1));
					break;
				}
			}

			if(field2.getAlias().contains("横坐标") || field2.getAlias().contains("经度")){
				layerFeatureAts.put(key2, format.format(lon));
			}else if(field2.getAlias().contains("纵坐标") || field2.getAlias().contains("纬度")){
				layerFeatureAts.put(key2, format.format(lat));
			}else if(field2.getAlias().contains("标准地号") || key2.equals("bzdh")){//标准地号
				layerFeatureAts.put(key2, xbwybh+"_"+(numSize+1));
			}else if(field2.getAlias().contains("样地类型") || key2.equals("YDLX")){
				layerFeatureAts.put(key2, 2);//自动添加的样地
			}
		}

		addFeatureTolayer(point);

		geoFeature = (GeodatabaseFeature) featureLayer.getFeature(feutureID);
		//fieldList = geoFeature.getTable().getFields();

//		for(Field field:ydd_fields){
//			updataData();
//		}

		Object obj = geoFeature.getAttributes().get("WYBH");
		if(obj != null){
			currentxbh = obj.toString();
		}

		picPath = getImagePath(path);
		listView = (ListView) findViewById(R.id.listView_xbedit);
		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		MyFeture feture = new MyFeture(pname, path, cname, geoFeature, myLayer);
		mAdapter = new YddcLineAdapter(YzlYddActivity.this, mLines,feture);
		if(mAdapter != null){
			listView.setAdapter(mAdapter);
		}

		mmdc = (TextView) findViewById(R.id.yddc_mmdcb);
		mmdc.setOnClickListener(new MyListener());

		yddcxbh = (TextView) findViewById(R.id.yddc_xbh);
		yddcxbh.setText(currentxbh);

		back = (TextView) findViewById(R.id.yddc_btnreturn);
		back.setOnClickListener(new MyListener());

		piclook = (TextView) findViewById(R.id.yddc_seepic);
		piclook.setOnClickListener(new MyListener());

		yddc_xbm = (TextView) findViewById(R.id.yddc_xbm);
		yddc_xbm.setOnClickListener(new MyListener());

		//gcmc = BussUtil.getConfigXml(mContext, pname,"wysbzd");// 项目名称
	}

	@Override
	public View getParentView() {
		return parentView;
	}

	class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.yddc_btnreturn:
				/*返回按钮*/
					finishThis();
					break;
				case R.id.yddc_seepic:
					//图片浏览
					lookpictures(YzlYddActivity.this);
					break;
				case R.id.yddc_mmdcb:
					/**添加样木调查数据*/
					//addYmdcData();
					break;
				case R.id.yddc_xbm://跳转到小班属性页
					toXbFeature();
					break;
				default:
					break;
			}
		}
	}

	/** 绑定数据*/
	public ArrayList<Line> createLines() {
		ArrayList<Line> lines = new ArrayList<Line>();
		LINE_NUM = geoFeature.getAttributes().size();
		String xianD = "",xiangD= "";
		for (int i = 0; i < LINE_NUM; i++) {
			Field field = ydd_fields.get(i);
			Line line = new Line();
			line.setNum(i);
			line.setTview(field.getAlias());
			line.setfLength(field.getLength());
			line.setKey(field.getName());
			CodedValueDomain domain = (CodedValueDomain) field.getDomain();
			line.setDomain(domain);
			line.setFieldType(field.getFieldType());
			boolean ff = field.isNullable();
			line.setNullable(ff);

			Object obj = geoFeature.getAttributes().get(field.getName());
			if(obj != null){
				String value = obj.toString().trim();
				if(field.getAlias().contains("县") || field.getAlias().contains("XIAN") ||field.getAlias().contains("xian")){
					xianD = value;
				}else if(field.getAlias().contains("乡") || field.getAlias().contains("XIANG") ||field.getAlias().contains("xiang")){
					xiangD = value;
				}
				if(line.getFieldType() == Field.esriFieldTypeDate){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(Long.parseLong(obj.toString()));
					value = sdf.format(date);
				}

				if(domain != null){
					Map<String, String> values = domain.getCodedValues();
					for(String key : values.keySet()){
						if(key.equals(value)){
							line.setText(values.get(key));
							break;
						}else if(field.getAlias().contains("乡") || field.getAlias().contains("XIANG") ||field.getAlias().contains("xianG")){
							if(key.equals(xianD+value)){
								line.setText(values.get(key));
								break;
							}
						}else if(field.getAlias().contains("村")|| field.getAlias().contains("CUN") ||field.getAlias().contains("cun")){
							if(key.equals(xiangD+value)){
								line.setText(values.get(key));
								break;
							}
						}else{
							line.setText(value);
						}
					}
				}else{
					Map<String, String> xianMap = Util.getXianValue(mContext);
					Map<String, String> xiangMap = Util.getXiangValue(mContext);
					Map<String, String> cunMap = Util.getCunValue(mContext);
					if(field.getAlias().contains("县") || field.getName().equals("xian") || field.getName().equals("XIAN")){
						xianD = value;
						String str = Util.getXXCValue(mContext, value, "", xianMap);
						line.setText(str);
					}else if(field.getAlias().contains("乡") || field.getName().equals("xiang") || field.getName().equals("XIANG")){
						xiangD = value;
						String str = Util.getXXCValue(mContext, value, xianD, xiangMap);
						line.setText(str);
					}else if(field.getAlias().contains("村") || field.getName().equals("cun") || field.getName().equals("CUN")){
						String str = value;
						if(xiangD.contains(xianD)){
							str = Util.getXXCValue(mContext, value, xiangD, cunMap);
						}else{
							str = Util.getXXCValue(mContext, value,xianD+xiangD, cunMap);
						}
						line.setText(str);
					}else{
						List<Row> list = isDMField(field.getName());
						if(list != null && list.size() > 0){
							for(Row row : list){
								if(row.getId().equals(value)){
									line.setText(row.getName());
									break;
								}else{
									line.setText(value);
								}
							}
						}else{
							line.setText(value);
						}
					}
				}
			}else{
				line.setText("");
			}
			lines.add(line);
		}
		return lines;
	}

	/**添加样地点 到数据图层*/
	public void addFeatureTolayer(Point point){
		Geometry geom = GeometryEngine.simplify(point,SpatialReference.create(2343));
		addFeatureOnLayer(geom, layerFeatureAts);
	}

	/** 添加feature在图层上 */
	public void addFeatureOnLayer(Geometry geom,Map<String, Object> selectFeatureAts) {
		try {
			GeodatabaseFeature g = myLayer.getTable().createFeatureWithTemplate(layerTemplate, geom);
			Symbol symbol = myLayer.getRenderer().getSymbol(g);
			// symbol为null也可以 why？
			Map<String, Object> editAttributes = null;
			if (selectFeatureAts == null) {
				editAttributes = g.getAttributes();
			} else {
				editAttributes = selectFeatureAts;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String str = format.format(new Date());
			editAttributes.put("WYBH", str);


			for(Field field : myLayer.getTable().getFields()){
				String key = field.getName();
				int type = field.getFieldType();
				Object value = editAttributes.get(key);
				if(value == null){
					if(type == Field.esriFieldTypeDouble){
						editAttributes.put(key,0.0);
					}else{
						editAttributes.put(key,"");
					}
				}
			}

			for(String key : editAttributes.keySet()){
				Log.e(key,editAttributes.get(key)+"");
			}

			Graphic addedGraphic = new Graphic(geom, symbol, editAttributes);
			GeodatabaseFeatureTable featureTable = myLayer.getTable();
			GeodatabaseFeature feature = new GeodatabaseFeature(editAttributes,featureTable);
			long id = featureTable.addFeature(feature);
			//long id = featureTable.addFeature(addedGraphic);
			feutureID = id;
			geoFeature = (GeodatabaseFeature) featureLayer.getFeature(feutureID);
			//geoFeatureAts = geoFeature.getAttributes();
			ydd_fields = geoFeature.getTable().getFields();
			/* 添加小班后 记录添加小班的id 备撤销时删除 */
			//recordXb(feutureID, "add", editAttributes, geom, featureLayer);
		} catch (TableException e) {
			e.printStackTrace();
		}

	}

	/**更新样地点数据*/
	public void updataData(){
		Graphic updateGraphic = new Graphic(geoFeature.getGeometry(),geoFeature.getSymbol(),geoFeature.getAttributes());
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
		try
		{
			GeodatabaseFeature feature = new GeodatabaseFeature(geoFeature.getAttributes(),featureTable);
			featureTable.updateFeature(feutureID, feature);
		} catch (TableException e)
		{
			e.printStackTrace();
			ToastUtil.setToast((Activity) mContext, "更新失败");
			return;
		}
		geoFeature = (GeodatabaseFeature) featureLayer.getFeature(feutureID);
		//fieldList = geoFeature.getTable().getFields();
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
			if(xbwybh != null || xbwybh.equals("")){
				String path2 = file2.getPath()+"/"+xbwybh;
				File file3 = new File(path2);
				if(!file3.exists()){
					file3.mkdirs();
				}
				picPath = file3.getPath();
			}else{
				picPath = path1;
			}

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

	/** 获取FeatureLayer图层样式 */
	public void getEditSymbo(FeatureLayer flayer) {
		String typeIdField = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getTypeIdField();
		if (typeIdField.equals("")) {
			List<FeatureTemplate> featureTemp = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getFeatureTemplates();

			for (FeatureTemplate featureTemplate : featureTemp) {
				GeodatabaseFeature g;
				try {
					g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate, null);
					layerRenderer = flayer.getRenderer();
					layerSymbol = layerRenderer.getSymbol(g);
					layerTemplate = featureTemplate;
					layerFeatureAts = g.getAttributes();
					ydd_fields = g.getTable().getFields();
				} catch (TableException e) {
					e.printStackTrace();
				}
			}
		} else {
			List<FeatureType> featureTypes = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				FeatureTemplate[] templates = featureType.getTemplates();
				for (FeatureTemplate featureTemplate : templates) {
					GeodatabaseFeature g;
					try {
						g = ((GeodatabaseFeatureTable) flayer.getFeatureTable()).createFeatureWithTemplate(featureTemplate,null);
						layerRenderer = flayer.getRenderer();
						layerSymbol = layerRenderer.getSymbol(g);
						layerTemplate = featureTemplate;
						layerFeatureAts = g.getAttributes();
						ydd_fields = g.getTable().getFields();
					} catch (TableException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			updateZPBH();
			dealPhotoFile(mCurrentPhotoPath);

		}
	}

	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){
		this.pcLine = line;
		updateJjzp(pctext,line,bfText);
	}
	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(geoFeature.getId());
		Map<String, Object> geoFeatureAts = feature.getAttributes();

		if(pctext == null || (bfText != null && pctext.equals(bfText))){
			return;
		}

		boolean flag = true;
		int length = pctext.length();
		int size = 0;
		for(Field ff : ydd_fields){
			if(ff.getName().equals(line.getKey())){
				size = ff.getLength();
				break;
			}
		}

		if(length > size){
			ToastUtil.setToast(mContext, "数据长度超过数据库规定长度");
			return;
		}
		geoFeatureAts.put(line.getKey(), pctext);

		//Graphic updateGraphic = new Graphic(feature.getGeometry(),feature.getSymbol(),geoFeatureAts);
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
		try
		{
			long featureid = feature.getId();
			GeodatabaseFeature geodatabaseFeature = new GeodatabaseFeature(geoFeatureAts, featureTable);
			featureTable.updateFeature(featureid, geodatabaseFeature);
		} catch (TableException e)
		{
			flag = false;
			e.printStackTrace();
		}
		if(flag){
			line.setText(pctext);
			mAdapter.notifyDataSetChanged();
			ToastUtil.setToast((Activity) mContext, "照片编号更新成功");
		}else{
			ToastUtil.setToast((Activity) mContext, "照片编号更新失败");
		}
	}



	@Override
	public void onBackPressed() {
		finishThis();
		return;
	}

	/**跳转至小班属性页*/
	public void toXbFeature(){
		this.finish();
	}



}
