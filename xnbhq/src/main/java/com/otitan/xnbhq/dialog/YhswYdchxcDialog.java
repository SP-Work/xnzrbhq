package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.UtilTime;

/**
 * 添加样地虫害调查信息
 */
public class YhswYdchxcDialog extends Dialog {
	Context context;
	private String ydbhstr;
	private String upstatus;

	public YhswYdchxcDialog(Context context, int theme,String ydbh,String upstatus) {
		super(context, theme);
		this.context = context;
		this.ydbhstr=ydbh;
		this.upstatus=upstatus;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.yhsw_ydchxcb_add);
		setCanceledOnTouchOutside(false);
		Button cancle = (Button) findViewById(R.id.yhsw_ydchxcb_cancle);
		Button save = (Button) findViewById(R.id.yhsw_ydchxcb_save);

		final TextView yhsw_ydchxcb_ydbh = (TextView) findViewById(R.id.yhsw_ydchxcb_ydbh);
		final TextView yhsw_ydchxcb_ysbh = (TextView) findViewById(R.id.yhsw_ydchxcb_ysbh);
		final EditText yhsw_ydchxcb_luan = (EditText) findViewById(R.id.yhsw_ydchxcb_luan);
		final EditText yhsw_ydchxcb_youchong = (EditText) findViewById(R.id.yhsw_ydchxcb_youchong);
		final EditText yhsw_ydchxcb_yong = (EditText) findViewById(R.id.yhsw_ydchxcb_yong);
		final EditText yhsw_ydchxcb_cchong = (EditText) findViewById(R.id.yhsw_ydchxcb_cchong);
		final EditText yhsw_ydchxcb_cdao = (EditText) findViewById(R.id.yhsw_ydchxcb_cdao);
		final EditText yhsw_ydchxcb_shugao = (EditText) findViewById(R.id.yhsw_ydchxcb_shugao);
		final EditText yhsw_ydchxcb_xjing = (EditText) findViewById(R.id.yhsw_ydchxcb_xjing);
		final EditText yhsw_ydchxcb_gfu = (EditText) findViewById(R.id.yhsw_ydchxcb_gfu);
		yhsw_ydchxcb_ydbh.setText(ydbhstr);
		String ysbhstr = UtilTime.getSystemtime1();
		yhsw_ydchxcb_ysbh.setText(ysbhstr);
		
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_ydchxcb_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String ysbh = yhsw_ydchxcb_ysbh.getText().toString().trim();
				if (TextUtils.isEmpty(ysbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ysbhnotnull));
					return;
				}
				String luan = yhsw_ydchxcb_luan.getText().toString().trim();
				String yc = yhsw_ydchxcb_youchong.getText().toString().trim();
				String yong = yhsw_ydchxcb_yong.getText().toString().trim();
				String cc = yhsw_ydchxcb_cchong.getText().toString().trim();
				String cd = yhsw_ydchxcb_cdao.getText().toString().trim();
				String sg = yhsw_ydchxcb_shugao.getText().toString().trim();
				String xj = yhsw_ydchxcb_xjing.getText().toString().trim();
				String gf = yhsw_ydchxcb_gfu.getText().toString().trim();
				if("1".equals(upstatus)){
					Webservice web = new Webservice(context);
					String result = web.addYhswYdchxcData(ydbh, ysbh, luan, yc,
							yong, cc, cd, sg, xj, gf);
					String[] splits = result.split(",");
					if (splits.length > 0) {
						if ("True".equals(splits[0])) {
							ToastUtil.setToast(context, context.getResources()
									.getString(R.string.addsuccess));
							dismiss();
						} else {
							ToastUtil.setToast(context, context.getResources()
									.getString(R.string.addfailed));
						}
					} else {
						ToastUtil.setToast(context, context.getResources()
								.getString(R.string.addfailed));
					}
				}else if("0".equals(upstatus)){
					DataBaseHelper.addYhswYdchxcData(context, "db.sqlite", ydbh, ysbh, luan, yc, yong, cc, cd, sg, xj, gf);
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.addsuccess));
					dismiss();
				}
				
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
}
