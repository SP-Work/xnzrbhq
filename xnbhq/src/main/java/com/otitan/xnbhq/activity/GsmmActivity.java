package com.otitan.xnbhq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.BussUtil;
/**
 * Created by li on 2016/5/26.
 * 古树名木页面
 */
public class GsmmActivity extends BaseActivity {

	View parentView;
	GsmmActivity activity;
	Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_gsmm, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);
		
		mContext = GsmmActivity.this;
		ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
		topview.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.share_top_gsmm));
		
		activitytype = getIntent().getStringExtra("name");
		proData = BussUtil.getConfigXml(mContext,activitytype);
	}

	@Override
	public View getParentView() {
		return parentView;
	}
	
}
