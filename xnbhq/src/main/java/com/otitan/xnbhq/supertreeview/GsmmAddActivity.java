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
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureTemplate;
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
 * 古树名木 添加
 * （舍去）
 */
public class GsmmAddActivity extends Activity {
	private FeatureLayer featureLayer;
	private Context mContext;
	private FeatureTemplate layerTemplate;
	GeodatabaseFeature g=null;
	Point point;
	private static final int TAKE_PICTURE = 0x000001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_notepad);
		mContext=this;
		point=BaseActivity.touchpoint;
		featureLayer = BaseActivity.myLayer.getLayer();
		layerTemplate=BaseActivity.layerTemplate;
		try {
			g = ((GeodatabaseFeatureTable) featureLayer
					.getFeatureTable()).createFeatureWithTemplate(layerTemplate, point);
		} catch (TableException e1) {
			e1.printStackTrace();
		}
		final Symbol symbol = featureLayer.getRenderer().getSymbol(g);
		final Map<String, Object> editAttributes = g.getAttributes();
		final int size = editAttributes.size() - 1;
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
				for(int i=0 ;i<size;i++){
					editAttributes.put(g.getTable().getFields().get(i + 1).getName(), lines.get(i).getText());
				}
				Graphic addedGraphic = new Graphic(point, symbol,editAttributes);
				FeatureTable featureTable = featureLayer.getFeatureTable();
				long id=0;
				try {
					id = featureTable.addFeature(addedGraphic);
				} catch (TableException e) {
					e.printStackTrace();
				}
				ToastUtil.setToast(mContext, "添加成功==" + id);
				BaseActivity.mapView.invalidate();
				featureLayer.setSelectionColor(Color.TRANSPARENT);
				ToastUtil.setToast(mContext,getResources().getString(R.string.addsuccess));
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
		TextView showphoto= (TextView) findViewById(R.id.ld_see_pic);
		showphoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String objectid="gsmmpys";
				List<File> lst = MyApplication.resourcesManager.getImages(objectid);
				if(lst.size() == 0){
					ToastUtil.setToast(mContext, "没有图片");
					return;
				}
				Intent intent = new Intent(GsmmAddActivity.this, Image2Activity.class);
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
			line.setTview(g.getTable().getFields().get(i + 1).getAlias());
			if("经度".equals(g.getTable().getFields().get(i + 1).getAlias())){
				line.setText(point.getX()+"");
			}else if("纬度".equals(g.getTable().getFields().get(i + 1).getAlias())){
				line.setText(point.getY()+"");
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
			image=new File(fileName+"/"+picname);
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
