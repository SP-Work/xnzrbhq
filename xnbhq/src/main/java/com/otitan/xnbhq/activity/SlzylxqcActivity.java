package com.otitan.xnbhq.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.FeatureResultAdapter;
import com.otitan.xnbhq.dialog.SlzylxqcDialog;
import com.otitan.xnbhq.dialog.SlzylxqcYdwztDialog;
import com.otitan.xnbhq.dialog.SlzylxqcYindwztDialog;
import com.otitan.xnbhq.entity.ActionMode;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.List;
/**
 * Created by li on 2016/5/26.
 * 连续清查页面
 */
public class SlzylxqcActivity extends BaseActivity {

	View parentView;
	private static final int TAKE_IMAGE = 0x000004;
	private static final int TAKE_IMAGE_YDWZT = 0x000005;

	Context mContext;

	public static String dbpath = "";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView=getLayoutInflater().inflate(R.layout.activity_slzylxqc, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);

		mContext = SlzylxqcActivity.this;
		ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_lxqc));

		activitytype = getIntent().getStringExtra("name");
		proData = BussUtil.getConfigXml(mContext,activitytype);

		//kjtj_center.setVisibility(View.GONE);

	}
	@Override
	public View getParentView() {
		return parentView;
	}
	@Override
	public void attributeFeture(View view) {
		initTouch();
		int size = selGeoFeaturesList.size();
		if(size >0){
			showListFeatureResult(selGeoFeaturesList, ActionMode.MODE_attribute_edit);
		}else{
			attributButton.setChecked(false);
			ToastUtil.setToast(mContext, "未任何选中图斑");
		}
	}

	/** 弹出结果展示窗口 */
	public void showListFeatureResult(final List<GeodatabaseFeature> list,final ActionMode mode) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog);
		dialog.setContentView(R.layout.featureresult_view);
		dialog.setCanceledOnTouchOutside(true);
		final ListView listView = (ListView) dialog.findViewById(R.id.featureresult_listview);
		FeatureResultAdapter adapter = new FeatureResultAdapter(mContext, list,selMap);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				if(graphicsLayer != null){
					graphicsLayer.removeAll();
				}

				getSelParams(list, position,selMap);

				MyFeture feture = new MyFeture(myLayer.getPname(), myLayer.getPath(),
						myLayer.getCname(), selGeoFeature,myLayer);
				Bundle bundle = new Bundle();
				bundle.putSerializable("myfeture", feture);

				SlzylxqcDialog dialog = new SlzylxqcDialog(SlzylxqcActivity.this,feture,position,mode);
				BussUtil.setDialogParamsFull(mContext, dialog);

			}
		});

		BussUtil.setDialogParam(mContext, dialog, 0.55, 0.55,0.55, 0.55);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case TAKE_IMAGE:// 森林资源连续清查 样地位置图拍照
				if (resultCode == RESULT_OK) {
					SlzylxqcYdwztDialog.getImageBitmap("1");
				}
				break;
			case TAKE_IMAGE_YDWZT:// 森林资源连续清查 引点位置图拍照
				if (resultCode == RESULT_OK) {
					SlzylxqcYindwztDialog.getImageBitmap("1");
				}
				break;
			default:
				break;
		}
	}
}
