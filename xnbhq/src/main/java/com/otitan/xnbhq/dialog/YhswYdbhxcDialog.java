package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.UtilTime;

/**
 * 添加样地病害详查信息
 */
public class YhswYdbhxcDialog extends Dialog {
	Context context;
	private String ydbhstr;
	private String upstatus;

	public YhswYdbhxcDialog(Context context, int theme,String ydbh,String upstatus) {
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
		setContentView(R.layout.yhsw_ydbhxcb_add);
		setCanceledOnTouchOutside(false);
		Button cancle = (Button) findViewById(R.id.yhsw_ydbhxcb_cancle);
		Button save = (Button) findViewById(R.id.yhsw_ydbhxcb_save);

		final TextView yhsw_ydbhxcb_ydbh = (TextView) findViewById(R.id.yhsw_ydbhxcb_ydbh);
		final TextView yhsw_ydbhxcb_ysbh = (TextView) findViewById(R.id.yhsw_ydbhxcb_ysbh);
		final Spinner yhsw_ydbhxcb_bhfj = (Spinner) findViewById(R.id.yhsw_ydbhxcb_bhfj);
		ArrayAdapter bhfjadapter=new ArrayAdapter(context, R.layout.myspinner, context.getResources().getStringArray(R.array.bhfj));
		yhsw_ydbhxcb_bhfj.setAdapter(bhfjadapter);
		final EditText yhsw_ydbhxcb_sg=(EditText) findViewById(R.id.yhsw_ydbhxcb_sg);
		final EditText yhsw_ydbhxcb_xj=(EditText) findViewById(R.id.yhsw_ydbhxcb_xj);
		final EditText yhsw_ydbhxcb_gf=(EditText) findViewById(R.id.yhsw_ydbhxcb_gf);
		
		yhsw_ydbhxcb_ydbh.setText(ydbhstr);
		String ysbhstr = UtilTime.getSystemtime1();
		yhsw_ydbhxcb_ysbh.setText(ysbhstr);
		
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String ydbh = yhsw_ydbhxcb_ydbh.getText().toString().trim();
				if (TextUtils.isEmpty(ydbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ydbhnotnull));
					return;
				}
				String ysbh = yhsw_ydbhxcb_ysbh.getText().toString().trim();
				if (TextUtils.isEmpty(ysbh)) {
					ToastUtil.setToast(context, context.getResources()
							.getString(R.string.ysbhnotnull));
					return;
				}
				String bhfj = yhsw_ydbhxcb_bhfj.getSelectedItemPosition()+"";
				String sg = yhsw_ydbhxcb_sg.getText().toString().trim();
				String xj = yhsw_ydbhxcb_xj.getText().toString().trim();
				String gf = yhsw_ydbhxcb_gf.getText().toString().trim();
				if("1".equals(upstatus)){
					Webservice web = new Webservice(context);
					String result =web.addYhswYdbhxcData(ydbh, ysbh, bhfj, sg, xj, gf);
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
					DataBaseHelper.addYhswYdbhxcData(context, "db.sqlite", ydbh, ysbh, bhfj, sg, xj, gf);
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
