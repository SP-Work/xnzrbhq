package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.PhotoShowAdapter;
import com.otitan.xnbhq.util.BussUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 图片显示列表dialog
 */
public class PhotoShowDialog extends Dialog {

	private static List<HashMap<String, Object>> seephoyolist;
	private Context context;

	public PhotoShowDialog(Context context, List<HashMap<String, Object>> seephoyolist) {
		super(context, R.style.Dialog);
		this.seephoyolist=seephoyolist;
		this.context=context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_photoshow);
		GridView gridview=(GridView) findViewById(R.id.gridview);
		final PhotoShowAdapter adapter=new PhotoShowAdapter(context, seephoyolist);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object>map=null;
				SinglePhotoDialog dialog=new SinglePhotoDialog(context, arg2,seephoyolist,adapter);
				dialog.show();
				BussUtil.setDialogParamsFull(context, dialog);
			}
		});
		Button back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

}
