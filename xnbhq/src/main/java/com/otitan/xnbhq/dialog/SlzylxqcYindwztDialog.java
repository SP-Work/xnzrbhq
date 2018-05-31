package com.otitan.xnbhq.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcYindwztAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.util.ScreenTool;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 连续清查 引点位置图拍照 dialog
 */
public class SlzylxqcYindwztDialog extends Dialog {

	Context context;
	List<HashMap<String, String>>list = new ArrayList<>();
	static String ydhselect;
	HashMap<String, String>map = new HashMap<>();
	static LxqcYindwztAdapter yindwztadapter;
	BaseActivity activity;
	private int TAKE_IMAGE_YDWZT = 0x000005;
	static Bitmap bitmap;
	static ImageView image;
	private static String takephotoname;
	private static ScreenTool.Screen screen;
	private Button photo;
	private Button huitu;
	public SlzylxqcYindwztDialog(Context context, int theme,List<HashMap<String, String>>list,HashMap<String, String>map, String ydhselect) {
		super(context, theme);
		this.context=context;
		this.list=list;
		this.ydhselect=ydhselect;
		this.map=map;
	}
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_yindwzt_view);
		activity=(BaseActivity) context;
		screen = MyApplication.screen;

		FrameLayout framlayot = (FrameLayout) findViewById(R.id.framlayot);
		image = new ImageView(context);
		LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(
				(int) (screen.getWidthPixels() * 0.49),
				(int) (screen.getHeightPixels() * 0.49));
		framlayot.addView(image, Params);

		photo = new Button(context);
		FrameLayout.LayoutParams photoarams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		photoarams.gravity = Gravity.LEFT |Gravity.BOTTOM;
		photo.setBackground(context.getResources().getDrawable(
				R.drawable.background_view_textbutton_selector));
		photo.setPadding(10, 10, 10, 10);
		photo.setText("相机");
		photo.setTextColor(Color.WHITE);
		photo.setTextSize(context.getResources().getDimension(
				R.dimen.larger_txtsize18));
		framlayot.addView(photo, photoarams);

		huitu = new Button(context);
		FrameLayout.LayoutParams huituarams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		huituarams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		huitu.setBackground(context.getResources().getDrawable(
				R.drawable.background_view_textbutton_selector));
		huitu.setPadding(10, 10, 10, 10);
		huitu.setText("绘图");
		huitu.setTextColor(Color.WHITE);
		huitu.setTextSize(context.getResources().getDimension(
				R.dimen.larger_txtsize18));
		framlayot.addView(huitu, huituarams);
		getImageBitmap("");

		if(map==null){
			map=new HashMap<String, String>();
			map.put("YDH", ydhselect);
			map.put("YDTZSM", "");
			map.put("ZBFWJ", "");
			map.put("CFWJ", "");
			map.put("YXJL", "");
			map.put("LC", "");
			map.put("GPSX", "");
			map.put("GPSY", "");
			map.put("TYPE", "0");
		}
		TextView ydh=(TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);

		/**引点特征说明*/
		final TextView ydtzsm=(TextView) findViewById(R.id.ydtzsm);
		ydtzsm.setText(map.get("YDTZSM"));
		ydtzsm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog hzdialog = new HzbjDialog(context,
						"引点特征说明", ydtzsm,map, "YDTZSM");
				hzdialog.show();
				BussUtil.setDialogParams(context, hzdialog, 0.5, 0.5);
			}
		});
		/**坐标方位角*/
		final TextView zbfwj=(TextView) findViewById(R.id.zbfwj);
		zbfwj.setText(map.get("ZBFWJ"));
		zbfwj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"坐标方位角", zbfwj, map, "ZBFWJ", "", "", "", list, "0", "1", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		/**磁方位角*/
		final TextView cfwj=(TextView) findViewById(R.id.cfwj);
		cfwj.setText(map.get("CFWJ"));
		cfwj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context, "磁方位角", cfwj, map, "CFWJ", "", "", "", list, "0", "1", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		/**引线距离*/
		final TextView yxjl=(TextView) findViewById(R.id.yxjl);
		yxjl.setText(map.get("YXJL"));
		yxjl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"引线距离", yxjl, map, "YXJL", "", "", "", list, "0", "1", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		/**罗差*/
		final TextView luocha=(TextView) findViewById(R.id.luocha);
		luocha.setText(map.get("LC"));
		luocha.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"罗差", luocha, map, "LC", "", "", "", list, "0", "1", null, null,
						null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		/**GPS横坐标*/
		final TextView hzb=(TextView) findViewById(R.id.hzb);
		hzb.setText(map.get("GPSX"));
		/**GPS纵坐标*/
		final TextView zzb=(TextView) findViewById(R.id.zzb);
		zzb.setText(map.get("GPSY"));


		ListView listview=(ListView) findViewById(R.id.listview);
		yindwztadapter=new LxqcYindwztAdapter(context, list);
		listview.setAdapter(yindwztadapter);

		TextView addmore=(TextView) findViewById(R.id.addmore);
		/**添加更多*/
		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String>map=new HashMap<String, String>();
				map.put("MC", "");
				map.put("BH", "");
				map.put("FWJ", "");
				map.put("SPJ", "");
				list.add(map);
				yindwztadapter.notifyDataSetChanged();
			}
		});
		/**保存*/
		Button save=(Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DataBaseHelper.deleteLxqcYindwzgyData(context, ydhselect);
				String ydtz=ydtzsm.getText().toString().trim();
				String zbfw=zbfwj.getText().toString().trim();
				String cfw=cfwj.getText().toString().trim();
				String yxj=yxjl.getText().toString().trim();
				String lc=luocha.getText().toString().trim();
				String x=hzb.getText().toString().trim();
				String y=zzb.getText().toString().trim();
				DataBaseHelper.addLxqcYindwzgyData(context, ydhselect, ydtz, zbfw, cfw, yxj, lc, x, y);
				for(int i=0;i<list.size();i++){
					HashMap<String, String>map=list.get(i);
					DataBaseHelper.addLxqcYindwzgyData(context, ydhselect, map.get("MC"), map.get("BH"), map.get("FWJ"), map.get("SPJ"));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
			}
		});
		/**取消*/
		Button cancle=(Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		/**相机*/
		photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				takephoto();
			}
		});
		/**绘图*/
		huitu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SlzylxqcQianMingDialog qmdialog=new SlzylxqcQianMingDialog(context, ydhselect, image, bitmap, "3");
				BussUtil.setDialogParamsFull(context, qmdialog);
			}
		});
	}
	/** 拍照 */
	public void takephoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			/**指定存放拍摄照片的位置*/
			File file = createImageFile();
			if (file != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				activity.startActivityForResult(intent, TAKE_IMAGE_YDWZT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**获取图片Bitmap,如果type则为“1”调用相机的，删除以前图片*/
	public static void getImageBitmap(String type){
		String path = MyApplication.resourcesManager.getLxqcImagePath("3");
		/**删除以前图片*/
		if(type.equals("1")){
			File f = new File(path);
			if (f.exists()) {
				File[] fl = f.listFiles();
				for (int i = 0; i < fl.length; i++) {
					if (fl[i].toString().endsWith(".jpg")&& fl[i].toString().contains(ydhselect)&&!fl[i].toString().contains(takephotoname)) {
						fl[i].delete();
					}
				}
			}
		}

		bitmap = MyApplication.resourcesManager.getImageBitmap(ydhselect, path);
		if(bitmap!=null){
			image.setImageBitmap(bitmap);
		}
	}
	/** 存放文件位置,如果是相机记住图片名字*/
	private File createImageFile() throws IOException {
		File image = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String fileName = MyApplication.resourcesManager.getLxqcImagePath("3");
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			takephotoname=String.valueOf(System.currentTimeMillis());
			image = new File(fileName + "/" + ydhselect + "_"
					+ takephotoname + ".jpg");
		} else {
			ToastUtil.setToast(context, "没有SD卡");
		}
		return image;
	}

	public static void refreshdata(){
		yindwztadapter.notifyDataSetChanged();
	}
}
