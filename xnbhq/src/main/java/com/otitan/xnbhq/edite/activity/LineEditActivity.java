package com.otitan.xnbhq.edite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.listviewinedittxt.PolylineAdapter;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 小班查询 线数据
 */
public class LineEditActivity extends BaseEditActivity {

	private ArrayList<Line> mLines;
	private PolylineAdapter mAdapter;
	private Context mContext;
	/**图片保存地址*/
	private TextView btnreturn,photograph,seepicture,txtviewxbh,xbarea;

	SharedPreferences preferences;

	private View parentview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentview = getLayoutInflater().inflate(R.layout.activity_line_edit,null);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(parentview);
		mContext = LineEditActivity.this;
		listView = (ListView) findViewById(R.id.listView_xbedit);
		if (savedInstanceState == null) {
			mLines = createLines();
		} else {
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		mAdapter = new PolylineAdapter(this, mLines,myFeture);
		listView.setAdapter(mAdapter);

		btnreturn = (TextView) findViewById(R.id.btnreturn);
		btnreturn.setOnClickListener(new MyListener());
		photograph = (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new MyListener());
		seepicture = (TextView) findViewById(R.id.ld_see_pic);
		seepicture.setOnClickListener(new MyListener());

		xbarea = (TextView) findViewById(R.id.tv_xbarea);
		double length = selGeoFeature.getGeometry().calculateLength2D();
		String txt = df.format(Math.abs(length))+"   米";
		xbarea.setText(txt);

		txtviewxbh = (TextView) findViewById(R.id.tv_xbh);
		txtviewxbh.setText(currentxbh);
		picPath = getImagePath(path);
		getMustField();

	}

	@Override
	public View getParentView() {
		return parentview;
	}


	class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btnreturn:
					finishThis();
					break;
				case R.id.ld_see_pic:
					/*图片浏览*/
					lookpictures(LineEditActivity.this);
					break;
				default:
					break;
			}
		}
	}

	/** 获取图片保存地址*/
	public String getImagePath(String path){
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

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			updateZPBH();
			dealPhotoFile(mCurrentPhotoPath);
		}
	}


	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){
		updateJjzp(pctext,line,bfText);
	}

	public void updateJjzp(String pctext,Line line,String bfText){
		GeodatabaseFeature feature = (GeodatabaseFeature) featureLayer.getFeature(selGeoFeature.getId());
		Map<String, Object> attribute = feature.getAttributes();

		if(pctext == null || (bfText != null && pctext.equals(bfText))){
			return;
		}

		boolean flag = true;
		int length = pctext.length();
		int size = 0;
		for(Field ff : fieldList){
			if(ff.getName().equals(line.getKey())){
				size = ff.getLength();
				break;
			}
		}

		if(length > size){
			ToastUtil.setToast(mContext, "数据长度超过数据库规定长度");
			return;
		}

		attribute.put(line.getKey(), pctext);

		Graphic updateGraphic = new Graphic(feature.getGeometry(),feature.getSymbol(),attribute);
		GeodatabaseFeatureTable featureTable = (GeodatabaseFeatureTable) featureLayer.getFeatureTable();
		try
		{
			long featureid = feature.getId();
			GeodatabaseFeature geodatabaseFeature = new GeodatabaseFeature(attribute,featureTable);
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

	/**获取必填必填字段*/
	public void getMustField(){
		preferences = getSharedPreferences(pname+"mustfield", MODE_PRIVATE);
		List<Row> list = BussUtil.getConfigXml(mContext, pname, "mustfield");
		if(list == null){
			return;
		}
		if(list != null || list.size() >= 0){
			Set<String> rows = new HashSet<String>();
			for (Row row : list) {
				rows.add(row.getName());
			}
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.putStringSet(pname, rows).apply();
		}
	}

	/**显示照片编号*/
	public void updateZPBH(){
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
				CursorUtil.setEditTextLocation(zpeditText);
			}
		});
	}


}
