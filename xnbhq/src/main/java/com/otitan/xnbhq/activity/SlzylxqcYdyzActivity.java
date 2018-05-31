package com.otitan.xnbhq.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcYdyzAdapter;
import com.otitan.xnbhq.dialog.PhotoShowDialog;
import com.otitan.xnbhq.edite.activity.ImageActivity;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Created by li on 2016/5/26.
 * 连续清查样地因子页面
 */
public class SlzylxqcYdyzActivity extends Activity {
	private static final String EXTRA_LINES = "EXTRA_LINES";
	FeatureLayer featureLayer;
	List<Field> fieldList = null;
	String dbpath = null;
	Context mContext;
	List<Line> mLines;
	static int size;
	private static final int TAKE_PICTURE = 0x000003;
	/* 执行progressbar */
	private static final int PROGRESSDIALOG = 1;
	private static final int STOPPROGRESS = 2;

	LxqcYdyzAdapter mAdapter;

	/** 工程名称 */
	private String pname;
	private String cname;

	/** 记录小班的唯一编号 当前小班的小班号*/
	public String ydhselect="";
	/**图片保存地址*/
	private String picPath = "";
	/**图片字段*/
	private Line pcLine;
	private String picname;
	private EditText zpeditText;

	/**小班识别字段*/
	public List<Row> gcmc = null;

	/** 选择小班的GeodatabaseFeature */
	public static GeodatabaseFeature selGeoFeature = null;

	TextView txtviewxbh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_notepad);
		Intent intent=this.getIntent();
		ydhselect=intent.getStringExtra("ydh");

		txtviewxbh = (TextView) findViewById(R.id.tv_xbh);

		TextView tvyddcb=(TextView) findViewById(R.id.tvyddcb);
		tvyddcb.setVisibility(View.VISIBLE);
		tvyddcb.setText(getResources().getString(R.string.ydyzdc));

		mContext=SlzylxqcYdyzActivity.this;
		if(selGeoFeature == null){
			selGeoFeature = BaseActivity.selGeoFeature;
		}else{
			selGeoFeature = (GeodatabaseFeature) BaseActivity.myLayer.getLayer().getFeature(selGeoFeature.getId());
		}

		featureLayer = BaseActivity.myLayer.getLayer();
		fieldList = selGeoFeature.getTable().getFields();
		size=selGeoFeature.getAttributes().size();
		dbpath = BaseActivity.myLayer.getPath();
		pname = BaseActivity.myLayer.getPname();
		cname = BaseActivity.myLayer.getCname();
		if (savedInstanceState == null){
			mLines = createLines();
		} else{
			mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
		}
		ListView listView=(ListView) findViewById(R.id.listView_xbedit);
		mAdapter=new LxqcYdyzAdapter(SlzylxqcYdyzActivity.this,mLines,pname,
				featureLayer,selGeoFeature);
		listView.setAdapter(mAdapter);

		getXbhData(pname);

		/**图片浏览*/
		TextView ld_see_pic=(TextView) findViewById(R.id.ld_see_pic);
		ld_see_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(PROGRESSDIALOG);
				new MyAsyncTask().execute("seepic");
			}
		});

		TextView back=(TextView) findViewById(R.id.btnreturn);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	/**拍照*/
	public void takephoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File file = createImageFile();
			if (file != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, TAKE_PICTURE);
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
			String fileName= MyApplication.resourcesManager.getLxqcImagePath("4");
			String picname = MyApplication.resourcesManager.getLxqcImagename(ydhselect, "2", "");
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			image = new File(fileName + "/" + picname);
		} else {
			ToastUtil.setToast(mContext, "没有SD卡");
		}
		return image;
	}
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			if (params[0].equals("seepic")) {
				runOnUiThread(new Runnable() {
					public void run() {
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
							PhotoShowDialog photodialog=new PhotoShowDialog(mContext,seephoyolist);
							photodialog.show();
							BussUtil.setDialogParamsFull(mContext, photodialog);
						}else{
							ToastUtil.setToast(mContext, "当前系统没有图片");
						}
					}
				});
				handler.sendEmptyMessage(STOPPROGRESS);
			}
			return null;
		}
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				/** 执行progressdialog */
				case PROGRESSDIALOG:
					ProgressDialogUtil.startProgressDialog(mContext);
					break;
				/** 停止 */
				case STOPPROGRESS:
					/** 设置进度条位置 */
					ProgressDialogUtil.stopProgressDialog(mContext);
					break;
			}
		};
	};
	/** 填充数据 */
	private ArrayList<Line> createLines()
	{
		ArrayList<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < size; i++)
		{
			Field field = fieldList.get(i);
			Line line = new Line();
			line.setNum(i);
			line.setTview(field.getAlias());
			line.setfLength(field.getLength());
			line.setKey(field.getName());
			CodedValueDomain domain = (CodedValueDomain) field.getDomain();
			line.setDomain(domain);
			line.setFieldType(field.getFieldType());

			Object obj = BaseActivity.selGeoFeature.getAttributes().get(field.getName());
			if(obj != null){
				String value = obj.toString();
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
						}else{
							line.setText(value);
						}
					}
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
			}else{
				line.setText("");
			}
			lines.add(line);
		}
		return lines;
	}

	/** 检测字段是那种类型的字段 */
	private List<Row> isDMField(String key) {
		List<Row> list = BussUtil.getConfigXml(mContext, pname, key);
		return list;
	}


	/**更新照片编号*/
	public void updateZp(String pctext,Line line,String bfText){

		updateJjzp(pctext,line,bfText);
	}

	public void updateJjzp(String pctext,Line line,String bfText){
		Map<String, Object> attribute = selGeoFeature.getAttributes();
		if(bfText == null){
			if(line.getText().equals("")){
				return;
			}
		}else{
			String txt = line.getText();
			if(txt != null && txt.equals(bfText)){
				return;
			}
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

		Graphic updateGraphic = new Graphic(selGeoFeature.getGeometry(),
				selGeoFeature.getSymbol(),attribute);
		FeatureTable featureTable = featureLayer.getFeatureTable();
		try
		{
			long featureid = selGeoFeature.getId();
			featureTable.updateFeature(featureid, updateGraphic);
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

	/** 拍照*/
	private String mCurrentPhotoPath;// 图片路径
	//tname 为字段
	public void takephoto(Line line,EditText editText){
		this.pcLine = line;
		this.picname = line.getKey();
		this.zpeditText = editText;
		takephoto(new View(mContext));
	}
	public void takephoto(View view){

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		@SuppressWarnings("static-access")
		String time = (String) new DateFormat().format("yyyyMMddhhmmss",Calendar.getInstance(Locale.CHINA));
		//String picname = currentxbh+"-"+time+".jpg";
		String picname = time+".jpg";
		File file = new File(picPath +"/"+picname);
		mCurrentPhotoPath = picPath +"/"+picname;
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, TAKE_PICTURE);
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
		if(ydhselect==null || ydhselect.equals("")){
			picPath = path1;
		}else{
			String path2 = file2.getPath()+"/"+ydhselect;
			File file3 = new File(path2);
			if(!file3.exists()){
				file3.mkdirs();
			}
			picPath = file3.getPath();
		}
		return picPath;
	}

	/** 图片浏览*/
	public void lookpictures(Line line){
		String pcname = line.getText();
		String[] pcarray = pcname.split(",");
		List<File> lst = ResourcesManager.getImages(picPath,pcarray);
		if(lst.size() == 0){
			ToastUtil.setToast(mContext, "没有图片");
			return;
		}

		Intent intent = new Intent(SlzylxqcYdyzActivity.this, ImageActivity.class);
		intent.putExtra("xbh", ydhselect);
		intent.putExtra("picPath", picPath);
		intent.putExtra("type", pcname);
		startActivity(intent);
	}

	/** 获取小班号数据 */
	public void getXbhData(String pnagme)
	{
		gcmc = BussUtil.getConfigXml(mContext, pname,"wysbzd");// 项目名称
		if(gcmc == null){
			return;
		}
		for(Row row : gcmc){
			String key = row.getName();
			Object obj = BaseActivity.selectFeatureAts.get(key);
			if(obj != null){
				String str = BaseActivity.selectFeatureAts.get(key).toString();
				ydhselect = ydhselect + str;
			}else{
				continue;
			}
		}
		txtviewxbh.setText(ydhselect);

		picPath = getImagePath(dbpath);
	}

}
