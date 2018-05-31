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

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.table.FeatureTable;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.FeatureResultAdapter;
import com.otitan.xnbhq.edite.activity.XbEditActivity;
import com.otitan.xnbhq.entity.ActionMode;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.List;
import java.util.Map;
/**
 * Created by li on 2016/5/26.
 * 二调页面
 */
public class ErDiaoActivity extends BaseActivity {

	View parentView;
	public static FeatureLayer yddlayer, xblayer;
	static FeatureLayer xbkzdlayer;
	static FeatureTable ydd_featuretable, xbkzd_featuretable;
	public static String datapath;

	Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_ed, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);

		mContext = ErDiaoActivity.this;
		ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_erdiao));

		activitytype = getIntent().getStringExtra("name");// erdiao
		proData = BussUtil.getConfigXml(mContext,activitytype);
	}

	@Override
	public View getParentView() {
		return parentView;
	}

	@Override
	public void attributeFeture(View view) {
		initTouch();
		int size = selGeoFeaturesList.size();
		if(size > 0){
			showListFeatureResult(selGeoFeaturesList,ActionMode.MODE_attribute_edit);
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

				getSelParams(selGeoFeaturesList, position,selMap);

				MyFeture feture = new MyFeture(myLayer.getPname(), myLayer.getPath(),
						myLayer.getCname(), selGeoFeature,myLayer);
				Bundle bundle = new Bundle();
				bundle.putSerializable("myfeture", feture);

				datapath = myLayer.getPath();

				Intent intent = null;
				if (selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
					intent = new Intent(mContext,ED_XBEditActivity.class);
					intent.putExtra("type", "XIAOBAN_PY");
					intent.putExtra("pname", myLayer.getPname());
				}else if (selectGeometry.getType().equals(Geometry.Type.POINT)) {
					Map<String, Object> att = BaseActivity.selGeoFeature.getAttributes();
					if (att.containsKey("YDH")) {
						//样地点
						intent = new Intent(mContext,ED_XBEditActivity.class);
						intent.putExtra("type", "YDD_PT");
						intent.putExtra("pname", myLayer.getPname());
					} else {
						//小班控制点
						intent = new Intent(mContext,ED_XBEditActivity.class);
						intent.putExtra("type", "XBKZD_PT");
						intent.putExtra("pname", myLayer.getPname());
					}
				}else{
					intent = new Intent(mContext, XbEditActivity.class);
					String pname = myLayer.getPname();// 工程名称
					String cname = myLayer.getCname();
					intent.putExtra("pname", pname);
					intent.putExtra("cname", cname);
				}
				intent.putExtra("path", datapath);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		BussUtil.setDialogParam(mContext, dialog, 0.55, 0.55,0.55, 0.55);
	}

}
