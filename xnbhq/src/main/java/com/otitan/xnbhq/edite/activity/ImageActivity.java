package com.otitan.xnbhq.edite.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.BitmapTool;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Created by li on 2016/5/26.
 * 图片展示页面
 */
public class ImageActivity extends Activity {
	/**
	 * ViewPager
	 */
	private ViewPager viewPager;
	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	private List<File> lst;

	private ArrayList<View> pageViews;

	private TextView returnview,imgdelete;
	private TextView picnameView;
	/* 当前图片所在顺序 */
	private int index = 0;

	GuidePageAdapter adapter;
	ViewGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image);
		group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);

		picnameView = (TextView) findViewById(R.id.picname);

		String xbh = getIntent().getStringExtra("xbh");
		String picPath = getIntent().getStringExtra("picPath");
		String type = getIntent().getStringExtra("type");

		if(type.equals("0")){
			lst = MyApplication.resourcesManager.getImages(picPath);
		}else{
			String[] array = type.split(",");
			lst = MyApplication.resourcesManager.getImages(picPath,array);
		}

		// 将点点加入到ViewGroup中
		tips = new ImageView[lst.size()];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			// 控制点间距离
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ll.setMargins(5, 0, 5, 0);
			imageView.setLayoutParams(ll);
			tips[i] = imageView;
			if (i == 0) {
				picnameView.setText(lst.get(0).getName());
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageView);
		}

		pageViews = new ArrayList<View>();

		for (int i = 0; i < lst.size(); i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(params);
			Bitmap bm = null;
			try {
				bm = BitmapTool.getBitmap(lst.get(i).getPath(),MyApplication.screen.getWidthPixels(), MyApplication.screen.getHeightPixels());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			imageView.setImageBitmap(bm);
			pageViews.add(imageView);
		}

		adapter = new GuidePageAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());

		returnview = (TextView) findViewById(R.id.image_return);
		returnview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		imgdelete = (TextView) findViewById(R.id.image_delete);
		imgdelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDeleteDialog();
			}
		});
	}

	/**
	 * 指引页面数据适配器
	 */
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			//return super.getItemPosition(object);
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager vp = ((ViewPager) arg0);
			View view = pageViews.get(arg1);
			vp.addView(view);
			view.destroyDrawingCache();
			//picnameView.setText(lst.get(arg1).getName());
			return view;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 指引页面更改事件监听器
	 */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int selectItems) {
			picnameView.setText(lst.get(selectItems).getName());
			tips[index].setBackgroundResource(R.drawable.page_indicator_unfocused);
			index = selectItems;
			tips[selectItems].setBackgroundResource(R.drawable.page_indicator_focused);
//			for (int i = 0; i < tips.length; i++) {
//				if (i == selectItems) {
//
//				} else {
//
//				}
//			}
		}
	}

	public int computeSampleSize(BitmapFactory.Options options,
								 int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * compute Initial Sample Size
	 *
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private int computeInitialSampleSize(BitmapFactory.Options options,
										 int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/** 提示是否刪除小班 */
	public void showDeleteDialog() {
		Builder builder = new Builder(ImageActivity.this);
		builder.setMessage("确认删除吗!");
		builder.setTitle("信息提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteFile();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void deleteFile(){
		if(pageViews.size() == 0){
			return;
		}
		File file = lst.get(index);
		if(file.exists()){
			file.delete();
			lst.remove(file);
		}

		View view = pageViews.get(index);
		viewPager.removeView(view);
		pageViews.remove(view);
		group.removeView(tips[index]);
		//adapter = new GuidePageAdapter();
		adapter.notifyDataSetChanged();
		//viewPager.setAdapter(adapter);
		if(pageViews.size() == 0){
			finish();
		}
	}

}
