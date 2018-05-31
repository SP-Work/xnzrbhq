package com.otitan.xnbhq.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.otitan.xnbhq.MyApplication;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片处理工具类
 */
public class PhotoHandledUtils {

	/**
	 * 给图片加水印
	 * 此方法为异步方法
	 * @param imagepath 图片的绝对地址
	 * @param time 添加的时间水印字符串
	 * @param zuobiao 添加的坐标水印字符串
	 * @param bz 添加的备注水印字符串
	 */
	public static void dealPhotoFile(String imagepath,String time,String zuobiao,String bz) {
		BufferedOutputStream bos = null;
		Bitmap icon = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagepath, options); // 此时返回bm为空
			float percent = options.outHeight > options.outWidth ? options.outHeight / 960f
					: options.outWidth / 960f;

			if (percent < 1) {
				percent = 1;
			}
			int width = (int) (options.outWidth / percent);
			int height = (int) (options.outHeight / percent);
			icon = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

			// 初始化画布 绘制的图像到icon上
			Canvas canvas = new Canvas(icon);
			// 建立画笔
			Paint photoPaint = new Paint();
			// 获取跟清晰的图像采样
			photoPaint.setDither(true);
			// 过滤一些
			// photoPaint.setFilterBitmap(true);
			options.inJustDecodeBounds = false;

			Bitmap prePhoto = BitmapFactory.decodeFile(imagepath);
			if (percent > 1) {
				prePhoto = Bitmap.createScaledBitmap(prePhoto, width,
						height, true);
			}

			canvas.drawBitmap(prePhoto, 0, 0, photoPaint);

			if (prePhoto != null && !prePhoto.isRecycled()) {
				prePhoto.recycle();
				prePhoto = null;
				System.gc();
			}

			// 设置画笔
			Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
			// 字体大小
			textPaint.setTextSize(20.0f);
			// 采用默认的宽度
			textPaint.setTypeface(Typeface.DEFAULT);
			// 采用的颜色
			textPaint.setColor(Color.YELLOW);
			// 阴影设置
			// textPaint.setShadowLayer(3f, 1, 1, Color.DKGRAY);

			// 时间水印
			if(!"".equals(time)){
				float textWidth = textPaint.measureText(time);
				canvas.drawText(time, (width+textWidth)/2 -textWidth, height - 70,textPaint);
			}
			if(!"".equals(zuobiao)){
				float textWidth = textPaint.measureText(zuobiao);
				canvas.drawText(zuobiao, (width+textWidth)/2 -textWidth , height - 40,textPaint);
			}
			if(!"".equals(bz)){
				float textWidth = textPaint.measureText(bz);
				canvas.drawText(bz, width - textWidth - 10 , height - 10,textPaint);
			}
			bos = new BufferedOutputStream(new FileOutputStream(imagepath));

			int quaility = (int) (100 / percent > 80 ? 80 : 100 / percent);
			icon.compress(CompressFormat.JPEG, quaility, bos);
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
	}
	/** 照片添加时间水印*/
	public static void dealPhotoFile(final String file) {
		PhotoTask task = new PhotoTask(file);
		task.start();
	}
	/** 照片添加时间水印线程*/
	public static class PhotoTask extends Thread {
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
				BitmapFactory.decodeFile(file, options); // 此时返回bm为空
				int height1 = options.outHeight;
				int width1 = options.outWidth;
				float percent = height1 > width1 ? height1 / 960f : width1 / 960f;

				if (percent < 1) {
					percent = 1;
				}
				//int width = (int) (width1 / percent);
				//int height = (int) (height1 / percent);
				icon = Bitmap.createBitmap(width1, height1, Bitmap.Config.ARGB_8888);

				// 初始化画布 绘制的图像到icon上
				Canvas canvas = new Canvas(icon);
				// 建立画笔
				Paint photoPaint = new Paint();
				// 获取跟清晰的图像采样
				photoPaint.setDither(true);
				// 过滤一些
				// photoPaint.setFilterBitmap(true);
				options.inJustDecodeBounds = false;

				Bitmap prePhoto = BitmapFactory.decodeFile(file);
				if (percent > 1) {
					prePhoto = Bitmap.createScaledBitmap(prePhoto, width1, height1, true);
				}

				canvas.drawBitmap(prePhoto, 0, 0, photoPaint);

				if (prePhoto != null && !prePhoto.isRecycled()) {
					prePhoto.recycle();
					prePhoto = null;
					System.gc();
				}

				// 设置画笔
				Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
				// 字体大小
				textPaint.setTextSize(50.0f);
				// 采用默认的宽度
				textPaint.setTypeface(Typeface.DEFAULT);
				// 采用的颜色
				textPaint.setColor(Color.RED);
				// 阴影设置
				//textPaint.setShadowLayer(3f, 1, 1, Color.DKGRAY);

				// 时间水印
				String mark = getCurrTime("yyyy-MM-dd HH:mm:ss");
				float textWidth = textPaint.measureText(mark);
				canvas.drawText(mark, width1 - textWidth - 10, height1 - 26, textPaint);

				bos = new BufferedOutputStream(new FileOutputStream(file));

				int quaility = (int) (100 / percent > 80 ? 80 : 100 / percent);
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
	/** 获取当前时间*/
	private static String getCurrTime(String pattern) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		return (new SimpleDateFormat(pattern)).format(new Date());
	}
}
