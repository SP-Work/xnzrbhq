package com.otitan.xnbhq.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.fragment.Ed_YmdcFragment;
/**
 * Created by li on 2016/5/26.
 * 二调样木调查页面
 */
public class ED_YmdcActivity extends FragmentActivity implements
		OnClickListener {
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	String XBH, YDH;
	public SharedPreferences input_history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ed_ymdc);
		input_history = getSharedPreferences("input_history", MODE_PRIVATE);
		Intent intent = getIntent();
		XBH = intent.getStringExtra("XBH");
		YDH = intent.getStringExtra("YDH");
		if (findViewById(R.id.ym_container) != null) {
			if (savedInstanceState == null) {
				Ed_YmdcFragment ed_ymdcfragment = new Ed_YmdcFragment();
				fragmentManager = getSupportFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				Bundle bundle = new Bundle();
				bundle.putStringArray("data", new String[] { XBH, YDH });
				ed_ymdcfragment.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.replace(R.id.ym_container, ed_ymdcfragment).commit();
			}
		}

		intiView();
	}

	/** 初始化控件 */
	private void intiView() {
		TextView btnreturn = (TextView) findViewById(R.id.ymdc_btnreturn);
		btnreturn.setOnClickListener(this);
		TextView ydh = (TextView) findViewById(R.id.ymdc_tv_ydh);
		ydh.setText(YDH);
		TextView xbh = (TextView) findViewById(R.id.ymdc_tv_xbh);
		xbh.setText(XBH);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ymdc_btnreturn:
				ED_YmdcActivity.this.finish();
				break;

			default:
				break;
		}

	}

}
