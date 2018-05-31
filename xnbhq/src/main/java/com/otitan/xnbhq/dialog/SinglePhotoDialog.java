package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.PhotoShowAdapter;
import com.otitan.xnbhq.util.ResourcesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 图片显示dialog
 */
public class SinglePhotoDialog extends Dialog implements View.OnClickListener{
	int position;
	Context context;
	private ViewPager viewPager;
	private List<View> pageViews;
	private List<HashMap<String, Object>> seephoyolist = new ArrayList<>();
	private int index;
	private PhotoShowAdapter psadapter;
	private GuidePageAdapter gpadapter;

	public SinglePhotoDialog(Context context,int position, List<HashMap<String, Object>> seephoyolist, PhotoShowAdapter psadapter) {
		super(context, R.style.Dialog);
		this.position=position;
		this.context=context;
		this.seephoyolist=seephoyolist;
		this.psadapter=psadapter;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_singleimage);
		index=position;
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		pageViews = new ArrayList<View>();
		for (int i = 0; i < seephoyolist.size(); i++) {
			ImageView imageView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(params);
			if(seephoyolist!=null){
			imageView.setImageBitmap((Bitmap)seephoyolist.get(i).get(MyApplication.resourcesManager.BITMAP));
			}
			pageViews.add(imageView);
		}
		gpadapter=new GuidePageAdapter();
		viewPager.setAdapter(gpadapter);
		viewPager.setCurrentItem(position);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		Button back=(Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		Button delete=(Button) findViewById(R.id.delete);
		delete.setOnClickListener(this);
	}
	// ָ��ҳ������������
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
			Log.i("index", arg1+"+destroyItem+");
			if(arg1<pageViews.size()){
				((ViewPager) arg0).removeView(pageViews.get(arg1));
			}
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			Log.i("index", arg1+"+instantiateItem+");
			if(arg1<pageViews.size()){
			((ViewPager) arg0).addView(pageViews.get(arg1));
			}
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
	// ָ��ҳ������¼�������
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int selectItems) {
			index = selectItems;
			if(index==-1){
				index=position;
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// ����
		case R.id.back:
			dismiss();
			break;
		// ɾ��
		case R.id.delete:
			Log.i("index", index+"++");
			if(seephoyolist!=null&&index!=-1&&index<seephoyolist.size()){
				HashMap<String, Object> map=seephoyolist.get(index);
				String path1= MyApplication.resourcesManager.getLxqcImagePath("4");
				String path2=MyApplication.resourcesManager.getLxqcImagePath("5");
				String imagename=(String) map.get(ResourcesManager.NAME);
				MyApplication.resourcesManager.deleteImageForName(path1, imagename);
				MyApplication.resourcesManager.deleteImageForName(path2, imagename);
				seephoyolist.remove(seephoyolist.get(index));
				pageViews.remove(pageViews.get(index));
//				gpadapter.notifyDataSetChanged();
				psadapter.notifyDataSetChanged();
				viewPager.setAdapter(new GuidePageAdapter());
			}
			break;
			
		}
	}

}
