package com.otitan.xnbhq.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.esri.core.geometry.Point;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.PhotoHandledUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片编辑dialog
 */
public class EditPhotoDialog extends Dialog {
	Context context;
	private String imagepath;
	Point point;
	private String zuobiao = "";
	BaseActivity activity;
	private Bitmap bitmap = null;
	private ImageView showimage;
	String miaoshu;

	public EditPhotoDialog(Context context, int theme, String imagepath,
						   Point point) {
		super(context, theme);
		this.context = context;
		this.imagepath = imagepath;
		this.point = point;
		this.activity = (BaseActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_edit);
		setCanceledOnTouchOutside(false);
		if (point != null) {
			zuobiao = "X:" + point.getX() + "  Y:" + point.getY();
		}
		if (!"".equals(imagepath)) {
			bitmap = getSmallBitmap(imagepath);
		}
		showimage = (ImageView) findViewById(R.id.showimage);
		if (bitmap != null) {
			showimage.setImageBitmap(bitmap);
		}
		Button zuobiaobz = (Button) findViewById(R.id.zuobiaobz);
		zuobiaobz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PhotoTask task = new PhotoTask(imagepath, "", zuobiao, "");
				task.start();
			}
		});
		Button timebz = (Button) findViewById(R.id.timebz);
		timebz.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String time = getCurrTime("yyyy-MM-dd HH:mm:ss");
				PhotoTask task = new PhotoTask(imagepath, time, "", "");
				task.start();
			}
		});
		Button tianjiams = (Button) findViewById(R.id.tianjiams);
		tianjiams.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context, R.style.Dialog);
				dialog.setContentView(R.layout.photo_addms);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				final EditText content = (EditText) dialog
						.findViewById(R.id.photo_content);
				Button sure = (Button) dialog.findViewById(R.id.photo_sure);
				Button cancle = (Button) dialog.findViewById(R.id.photo_cancle);
				cancle.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
				sure.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String bz=content.getText().toString();
						PhotoTask task = new PhotoTask(imagepath, "", "", bz);
						task.start();
						dialog.dismiss();
					}
				});
			}
		});
		Button sure = (Button) findViewById(R.id.sure);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	private static String getCurrTime(String pattern) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		return (new SimpleDateFormat(pattern)).format(new Date());
	}

	private class PhotoTask extends Thread {
		private String filepath;
		private String time;
		private String zuobiao;
		private String bz;

		public PhotoTask(String filepath, String time, String zuobiao, String bz) {
			this.filepath = filepath;
			this.time = time;
			this.zuobiao = zuobiao;
			this.bz = bz;
		}

		@Override
		public void run() {
			PhotoHandledUtils.dealPhotoFile(filepath, time, zuobiao, bz);
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!"".equals(imagepath)) {
						Bitmap bitmap = getSmallBitmap(imagepath);
						showimage.setImageBitmap(bitmap);
					}
				}
			});
		}

	}

	/** 根据路径获得并压缩返回bitmap用于显示 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算图片的缩放
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
