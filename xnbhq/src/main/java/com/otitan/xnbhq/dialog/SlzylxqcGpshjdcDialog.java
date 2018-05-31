package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GPS航迹调查记录
 */
public class SlzylxqcGpshjdcDialog extends Dialog {

	Context context;
	static String ydhselect;
	HashMap<String, String> map;
	BaseActivity activity;

	public SlzylxqcGpshjdcDialog(Context context,
								 HashMap<String, String> map, String ydhselect) {
		super(context, R.style.Dialog);
		this.context = context;
		this.ydhselect = ydhselect;
		this.map = map;
		this.activity = (BaseActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_gpshjdc_view);

		TextView ydh = (TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);

		final Button kssj = (Button) findViewById(R.id.kssj);
		final Button jssj = (Button) findViewById(R.id.jssj);
		final TextView cjjl = (TextView) findViewById(R.id.cjjl);
		final EditText xzydtj = (EditText) findViewById(R.id.xzydtj);
		TextView zpsl = (TextView) findViewById(R.id.zpsl);
		TextView ydzpsl = (TextView) findViewById(R.id.ydzpsl);
		TextView ymzpsl = (TextView) findViewById(R.id.ymzpsl);
		if (map != null) {
			kssj.setText(map.get("CJKSSJ"));
			jssj.setText(map.get("CJJSSJ"));
			cjjl.setText(map.get("CJJL"));
			xzydtj.setText(map.get("XZYDTJ"));
		}
		/**样地图片路径*/
		String ydtppath = MyApplication.resourcesManager.getLxqcImagePath("4");
		int ydtpnum=MyApplication.resourcesManager.getImageNumber(ydtppath, ydhselect);
		/**样木图片路径*/
		String ymtppath = MyApplication.resourcesManager.getLxqcImagePath("5");
		int ymtpnum=MyApplication.resourcesManager.getImageNumber(ymtppath, ydhselect);
		int zgs=ydtpnum+ymtpnum;
		zpsl.setText(zgs+"");
		ydzpsl.setText(ydtpnum+"");
		ymzpsl.setText(ymtpnum+"");
		/**航迹采集开始时间*/
		kssj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(kssj, false);
			}
		});
		/**航迹采集结束时间*/
		jssj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.trajectoryPresenter.initSelectTimePopuwindow(jssj, false);
			}
		});
		/**航迹采集距离*/
		cjjl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<HashMap<String, String>>list=new ArrayList<HashMap<String,String>>();
				ShuziYxclDialog shuzidialog = new ShuziYxclDialog(context,"GPS航迹采集距离(米)", cjjl, map, "CJJL", "",
						"", "",  list, "0", "1",null, null, null, "", null, null, "");
				BussUtil.setDialogParams(context, shuzidialog, 0.5, 0.5);
			}
		});
		/**寻找样地途径*/
		xzydtj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HzbjDialog hzdialog = new HzbjDialog(context,
						"寻找样地途径", xzydtj,map, "XZYDTJ");
				BussUtil.setDialogParams(context, hzdialog, 0.5, 0.5);
			}
		});
		/**取消按钮*/
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		/**保存按钮*/
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DataBaseHelper.deleteLxqcGpshjdcbData(context, ydhselect);
				if (map != null) {
					DataBaseHelper.addLxqcGpshjdcbData(context, ydhselect,
							kssj.getText().toString(), jssj.getText().toString(),
							map.get("CJJL"), map.get("XZYDTJ"));
				}
				ToastUtil.setToast(context, context.getResources().getString(R.string.bcsuccess));
				dismiss();
			}
		});
	}
}
