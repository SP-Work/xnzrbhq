package com.otitan.xnbhq.supertreeview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片显示（舍去）
 */
public class Image2Activity extends Activity {
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

	private TextView returnview;//imgdelete
	/*当前图片所在顺序*/
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_show);
		TextView back=(TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		final ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewpage);

		String xbh = getIntent().getStringExtra("xbh");

		lst = MyApplication.resourcesManager.getImages(xbh);

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
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			}
			group.addView(imageView);
		}

		pageViews = new ArrayList<View>();
		Bitmap bitmap=null;

		for (int i = 0; i < lst.size(); i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(params);
			try {
				FileInputStream is = new FileInputStream(lst.get(i).getAbsolutePath());
				BitmapFactory.Options options = new BitmapFactory.Options();

				options.inJustDecodeBounds = false;

				options.inSampleSize = 10;   // width，hight设为原来的十分一

				bitmap = BitmapFactory.decodeStream(is, null, options);
				is.close();
//				bitmap = BitmapFactory.decodeFile(filepath);
			} catch (Exception e) {
			}

//			Uri uri = Uri.fromFile(lst.get(i));
			if(bitmap!=null){
				imageView.setImageBitmap(bitmap);
			}
			pageViews.add(imageView);
		}
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());

//		returnview = (TextView) findViewById(R.id.image_return);
//		returnview.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});

//		imgdelete = (TextView) findViewById(R.id.image_delete);
//		imgdelete.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						viewPager.removeView(pageViews.remove(index));
//						File file = lst.get(index);
//						file.delete();
//						lst.remove(lst.get(index));
//						group.removeView(tips[index]);
//						viewPager.setAdapter(new GuidePageAdapter());
//					}
//				});
//			}
//		});
	}

	// 指引页面数据适配器
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
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			index = arg1;
			return pageViews.get(arg1);
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

	// 指引页面更改事件监听器
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int selectItems) {
			for (int i = 0; i < tips.length; i++) {
				if (i == selectItems) {
					tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
				} else {
					tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
	}

}
