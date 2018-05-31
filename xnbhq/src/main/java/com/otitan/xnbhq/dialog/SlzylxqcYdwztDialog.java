package com.otitan.xnbhq.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcYdwztAdapter;
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
 * 样地位置图拍照 dialog
 */
public class SlzylxqcYdwztDialog extends Dialog {

	Context context;
	List<HashMap<String, String>> list = new ArrayList<>();
	static String ydhselect;
	HashMap<String, String> map = new HashMap<>();
	static LxqcYdwztAdapter ydwztadapter;
	BaseActivity activity;
	private int TAKE_PICTURE = 0x000004;
	private static Bitmap bitmap;
	static ImageView image;
	private static String takephotoname;
	private static ScreenTool.Screen screen;
	private Button photo;
	private Button huitu;

	public SlzylxqcYdwztDialog(Context context,
							   List<HashMap<String, String>> list, HashMap<String, String> map,
							   String ydhselect) {
		super(context, R.style.Dialog);
		this.context = context;
		this.list = list;
		this.map = map;
		this.ydhselect=ydhselect;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_ydwzt_view);
		activity = (BaseActivity) context;
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

		TextView ydh = (TextView) findViewById(R.id.ydh);
		final TextView ydwztsm = (TextView) findViewById(R.id.ydwztsm);
		ydh.setText(ydhselect);
		if(map==null){
			map=new HashMap<String, String>();
			map.put("YDTZSM", "");
			map.put("TYPE", "0");
			map.put("YDH", ydhselect);
		}else{
			ydwztsm.setText(map.get("YDTZSM"));
		}
		ydwztsm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog hzdialog = new HzbjDialog(context,"样地特征说明", ydwztsm,map, "YDTZSM");
				BussUtil.setDialogParams(context, hzdialog, 0.5, 0.5);
			}
		});

		ListView listview = (ListView) findViewById(R.id.listview);
		ydwztadapter = new LxqcYdwztAdapter(context, list);
		listview.setAdapter(ydwztadapter);

		TextView addmore = (TextView) findViewById(R.id.addmore);
		/**添加更多*/
		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("MC", "");
				map.put("BH", "");
				map.put("FWJ", "");
				map.put("SPJ", "");
				list.add(map);
				ydwztadapter.notifyDataSetChanged();
			}
		});
		/**保存*/
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DataBaseHelper.deleteLxqcYdwztData(context, ydhselect);
				String ydtz = ydwztsm.getText().toString().trim();
				DataBaseHelper.addLxqcYdwztYdtzData(context, ydhselect, ydtz);
				for (int i = 0; i < list.size(); i++) {
					HashMap<String, String> map = list.get(i);
					DataBaseHelper.addLxqcYdwztData(context, ydhselect,
							map.get("MC"), map.get("BH"), map.get("FWJ"),
							map.get("SPJ"));
				}
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.bcsuccess));
			}
		});
		/**取消*/
		Button cancle = (Button) findViewById(R.id.cancle);
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
				SlzylxqcQianMingDialog qmdialog = new SlzylxqcQianMingDialog(context, ydhselect, image, bitmap, "2");
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
				activity.startActivityForResult(intent, TAKE_PICTURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 获取图片Bitmap,如果type则为“1”调用相机的，删除以前图片 */
	public static void getImageBitmap(String type) {
		String path =MyApplication.resourcesManager.getLxqcImagePath("2");
		/**删除以前图片*/
		if (type.equals("1")) {
			File f = new File(path);
			if (f.exists()) {
				File[] fl = f.listFiles();
				for (int i = 0; i < fl.length; i++) {
					if (fl[i].toString().endsWith(".jpg")
							&& fl[i].toString().contains(ydhselect)
							&& !fl[i].toString().contains(takephotoname)) {
						fl[i].delete();
					}
				}
			}
		}

		bitmap = MyApplication.resourcesManager.getImageBitmap(ydhselect, path);
		if (bitmap != null) {
			Bitmap wztbitmap = Bitmap.createScaledBitmap(bitmap,
					(int) (screen.getWidthPixels() * 0.49),
					(int) (screen.getHeightPixels() * 0.49), true);
			image.setImageBitmap(wztbitmap);
		}
	}

	/** 存放文件位置,如果是相机记住图片名字 */
	private File createImageFile() throws IOException {
		File image = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String fileName =MyApplication.resourcesManager.getLxqcImagePath("2");
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			takephotoname = String.valueOf(System.currentTimeMillis());
			image = new File(fileName + "/" + ydhselect + "_" + takephotoname
					+ ".jpg");
		} else {
			ToastUtil.setToast(context, "没有SD卡");
		}
		return image;
	}

	public static void setBitmap(Bitmap bitmap1) {
		bitmap=bitmap1;
	}

	public static void refreshdata() {
		ydwztadapter.notifyDataSetChanged();
	}
}
