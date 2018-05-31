package com.otitan.xnbhq.supertreeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.PhotoHandledUtils;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 古树名木 编辑
 * （舍去）
 */
public class GsmmEditActivity extends Activity {
	private GsmmEditActivity activity;
	private Map<String, Object> selectFeatureAts=null;
	private GeodatabaseFeature selGeoFeature;
	private FeatureLayer featureLayer;
	private List<Field> selectfiledList = null;
	private Context mContext;
	private Geometry selectGeometry = null;
	private Symbol layerSymbol;
	public long featureId ;
	private static final int TAKE_PICTURE = 0x000001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_notepad);
		activity=this;
		mContext=this;
		selectGeometry=BaseActivity.selectGeometry;
		layerSymbol= BaseActivity.layerSymbol;
		if(BaseActivity.selGeoFeature != null){
			selGeoFeature = (GeodatabaseFeature) BaseActivity.selGeoFeature;
			featureLayer = BaseActivity.myLayer.getLayer();
			selectfiledList = selGeoFeature.getTable().getFields();
			selectFeatureAts = selGeoFeature.getAttributes();
			featureId = Long.parseLong(selGeoFeature.getAttributeValue(selectfiledList.get(0)).toString());
		}
		final int size = selectFeatureAts.size() - 1;
		final ArrayList<Line> lines = createLines(size);
		TextView back= (TextView)findViewById(R.id.btnreturn);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TextView btnsure= (TextView) findViewById(R.id.btnsure);
		btnsure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < size; i++) {
					selectFeatureAts.put(selectfiledList.get(i + 1).getName(), lines.get(i).getText());
				}
				Graphic updateGraphic = new Graphic(selectGeometry, layerSymbol,selectFeatureAts);
				FeatureTable featureTable = featureLayer.getFeatureTable();
				try {
					featureTable.updateFeature(featureId, updateGraphic);
				} catch (TableException e) {
					e.printStackTrace();
				}
				ToastUtil.setToast(mContext, "更新成功==" + featureId);
				BaseActivity.mapView.invalidate();
				featureLayer.setSelectionColor(Color.TRANSPARENT);
				ToastUtil.setToast(mContext,mContext.getResources().getString(R.string.updatesuccess));
				finish();
			}
		});
		TextView photograph= (TextView) findViewById(R.id.photograph);
		photograph.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				takephoto();
			}
		});
		TextView photoshow= (TextView) findViewById(R.id.ld_see_pic);
		photoshow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String objectid="gsmmpys";
				List<File> lst = MyApplication.resourcesManager.getImages(objectid);
				if(lst.size() == 0){
					ToastUtil.setToast(mContext, "没有图片");
					return;
				}
				Intent intent = new Intent(GsmmEditActivity.this, Image2Activity.class);
				intent.putExtra("xbh", objectid);
				startActivity(intent);
			}
		});
		ListView listview= (ListView) findViewById(R.id.listView_xbedit);
		LineAdapter adapter=new LineAdapter(lines);
		listview.setAdapter(adapter);
	}
	private ArrayList<Line> createLines(int size) {
		ArrayList<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < size; i++) {
			Line line = new Line();
			line.setNum(i);
			line.setTview(selectfiledList.get(i + 1).getAlias());
			if(selectFeatureAts.get(selectfiledList.get(i+1).getName())!=null){
				line.setText(selectFeatureAts.get(selectfiledList.get(i+1).getName()).toString());
			}
			lines.add(line);
		}
		return lines;
	}
	/** 拍照*/
	private String mCurrentPhotoPath;// 图片路径
	public void takephoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File file = createImageFile();
			if(file!=null){
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, TAKE_PICTURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**存放文件位置*/
	private File createImageFile() throws IOException {
		File image=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String fileName =getImagePath();
			String objectid="gsmmpys";
			String picname ="yhh-"+objectid+"-"+String.valueOf(System.currentTimeMillis()) + ".jpg";
			File file = new File(fileName);
			if(!file.exists()){
				file.mkdirs();
			}
			image=new File(fileName+"/"+picname );
			mCurrentPhotoPath = image.getAbsolutePath();
		}else{
			Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
		}
		return image;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKE_PICTURE ) {
			PhotoHandledUtils.dealPhotoFile(mCurrentPhotoPath);
			ToastUtil.setToast(mContext, "添加图片成功");
		}

	}
	/** 获取图片保存地址*/
	public String getImagePath(){
		String path = null ;
		try {
			String root = MyApplication.resourcesManager.getMemoryPath()[0];
			path = root+ResourcesManager.ROOT_MAPS+ResourcesManager.image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
}
