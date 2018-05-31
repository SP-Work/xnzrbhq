package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 复查期样地变化情况dialog
 */
public class SlzylxqcFcqnydbhdcDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private String ydhselect;
	private List<HashMap<String, String>> fcqnydbhLlist = new ArrayList<>();
	private TextView dleibhyy;
	private TextView ywtsddjsm;
	private TextView lzhongbhyy;
	private TextView qyuanbhyy;
	private TextView ysszhongbhyy;
	private TextView lzubhyy;
	private TextView zblxingbhyy;
	private TextView qqdlei;
	private TextView bqdlei;
	private TextView qqlzhong;
	private TextView bqlzhong;
	private TextView qqqyuan;
	private TextView bqqyuan;
	private TextView qqysszhong;
	private TextView bqysszhong;
	private TextView qqlzu;
	private TextView bqlzu;
	private TextView qqzblxing;
	private TextView bqzblxing;
	private String name="";
	private String dbpath = null;
	public SlzylxqcFcqnydbhdcDialog(Context context,List<HashMap<String, String>> fcqnydbhLlist,
									String ydhselect,String path) {
		super(context, R.style.Dialog);
		this.context = context;
		this.fcqnydbhLlist = fcqnydbhLlist;
		this.ydhselect=ydhselect;
		this.name=context.getResources().getString(R.string.ysszname);
		this.dbpath = path;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_fcqlydbhqk_view);
		setCanceledOnTouchOutside(false);
		TextView ydh=(TextView) findViewById(R.id.ydh);
		ydh.setText(ydhselect);
		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		qqdlei = (TextView) findViewById(R.id.qqdlei);
		qqdlei.setOnClickListener(this);
		qqlzhong = (TextView) findViewById(R.id.qqlzhong);
		qqlzhong.setOnClickListener(this);
		qqqyuan = (TextView) findViewById(R.id.qqqyuan);
		qqqyuan.setOnClickListener(this);
		qqysszhong = (TextView) findViewById(R.id.qqysszhong);
		qqysszhong.setOnClickListener(this);
		qqlzu = (TextView) findViewById(R.id.qqlzu);
		qqlzu.setOnClickListener(this);
		qqzblxing = (TextView) findViewById(R.id.qqzblxing);
		qqzblxing.setOnClickListener(this);

		bqdlei = (TextView) findViewById(R.id.bqdlei);
		bqdlei.setOnClickListener(this);
		bqlzhong = (TextView) findViewById(R.id.bqlzhong);
		bqlzhong.setOnClickListener(this);
		bqqyuan = (TextView) findViewById(R.id.bqqyuan);
		bqqyuan.setOnClickListener(this);
		bqysszhong = (TextView) findViewById(R.id.bqysszhong);
		bqysszhong.setOnClickListener(this);
		bqlzu = (TextView) findViewById(R.id.bqlzu);
		bqlzu.setOnClickListener(this);
		bqzblxing = (TextView) findViewById(R.id.bqzblxing);
		bqzblxing.setOnClickListener(this);

		dleibhyy = (TextView) findViewById(R.id.dleibhyy);
		dleibhyy.setOnClickListener(this);
		lzhongbhyy = (TextView) findViewById(R.id.lzhongbhyy);
		lzhongbhyy.setOnClickListener(this);
		qyuanbhyy = (TextView) findViewById(R.id.qyuanbhyy);
		qyuanbhyy.setOnClickListener(this);
		ysszhongbhyy = (TextView) findViewById(R.id.ysszhongbhyy);
		ysszhongbhyy.setOnClickListener(this);
		lzubhyy = (TextView) findViewById(R.id.lzubhyy);
		lzubhyy.setOnClickListener(this);
		zblxingbhyy = (TextView) findViewById(R.id.zblxingbhyy);
		zblxingbhyy.setOnClickListener(this);

		ywtsddjsm = (TextView) findViewById(R.id.ywtsddjsm);
		ywtsddjsm.setOnClickListener(this);


		fcqnydbhLlist = DataBaseHelper.getFcqlydbhqkData(context);
		HashMap<String, String> map = null;
		if (fcqnydbhLlist.size() > 0) {
			map = fcqnydbhLlist.get(0);
		}
		if (map != null) {
			qqdlei.setText(map.get("QQDL").trim());
			qqlzhong.setText(map.get("QQLZ").trim());
			qqqyuan.setText(map.get("QQQY").trim());
			qqysszhong.setText(map.get("QQYSSZ").trim());
			qqlzu.setText(map.get("QQLZU").trim());
			qqzblxing.setText(map.get("QQZBLX").trim());

			bqdlei.setText(map.get("BQDL").trim());
			bqlzhong.setText(map.get("BQLZ").trim());
			bqqyuan.setText(map.get("BQQY").trim());
			bqysszhong.setText(map.get("BQYSSZ").trim());
			bqlzu.setText(map.get("BQLZU").trim());
			bqzblxing.setText(map.get("BQZBLX").trim());

			dleibhyy.setText(map.get("DLBHYY").trim());
			lzhongbhyy.setText(map.get("LZBHYY").trim());
			qyuanbhyy.setText(map.get("QYBHYY").trim());
			ysszhongbhyy.setText(map.get("YSSZBHYY").trim());
			lzubhyy.setText(map.get("LZUBHYY").trim());
			zblxingbhyy.setText(map.get("ZBLXBHYY").trim());

			ywtsddjsm.setText(map.get("YDYWTSDDJSM").trim());
			ydh.setText(ydhselect);
		}
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				String[] zd = context.getResources().getStringArray(
						R.array.lxqcfcqydbhzd);
				hashmap.put("YDH", ydhselect);
				String qqdl = qqdlei.getText().toString().trim();
				hashmap.put("QQDL", qqdl);
				String qqlzhongtx = qqlzhong.getText().toString().trim();
				hashmap.put("QQLZ", qqlzhongtx);
				String qqqyuantx = qqqyuan.getText().toString().trim();
				hashmap.put("QQQY", qqqyuantx);
				String qqysszhongtx = qqysszhong.getText().toString().trim();
				hashmap.put("QQYSSZ", qqysszhongtx);
				String qqlzutx = qqlzu.getText().toString().trim();
				hashmap.put("QQLZU", qqlzutx);
				String qqzblxingtx = qqzblxing.getText().toString().trim();
				hashmap.put("QQZBLX", qqzblxingtx);

				String bqdleitx = bqdlei.getText().toString().trim();
				hashmap.put("BQDL", bqdleitx);
				String bqlzhongtx = bqlzhong.getText().toString().trim();
				hashmap.put("BQLZ", bqlzhongtx);
				String bqqyuantx = bqqyuan.getText().toString().trim();
				hashmap.put("BQQY", bqqyuantx);
				String bqysszhongtx = bqysszhong.getText().toString().trim();
				hashmap.put("BQYSSZ", bqysszhongtx);
				String bqlzutx = bqlzu.getText().toString().trim();
				hashmap.put("BQLZU", bqlzutx);
				String bqzblxingtx = bqzblxing.getText().toString().trim();
				hashmap.put("BQZBLX", bqzblxingtx);

				String dleibhyytx = dleibhyy.getText().toString().trim();
				hashmap.put("DLBHYY", dleibhyytx);
				String lzhongbhyytx = lzhongbhyy.getText().toString().trim();
				hashmap.put("LZBHYY", lzhongbhyytx);
				String qyuanbhyytx = qyuanbhyy.getText().toString().trim();
				hashmap.put("QYBHYY", qyuanbhyytx);
				String ysszhongbhyytx = ysszhongbhyy.getText().toString()
						.trim();
				hashmap.put("YSSZBHYY", ysszhongbhyytx);
				String lzubhyytx = lzubhyy.getText().toString().trim();
				hashmap.put("LZUBHYY", lzubhyytx);
				String zblxingbhyytx = zblxingbhyy.getText().toString().trim();
				hashmap.put("ZBLXBHYY", zblxingbhyytx);
				String ywtsddjsmtx = ywtsddjsm.getText().toString().trim();
				hashmap.put("YDYWTSDDJSM", ywtsddjsmtx);
				DataBaseHelper.deleteFcqnydbhqkAllData(context, ydhselect);
				DataBaseHelper.addFcqnydbhqkData(context, zd, hashmap);
				ToastUtil.setToast(context,context.getResources().getString(R.string.editsuccess));
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			/**前期地类*/
			case R.id.qqdlei:
				List<Row> qqdllist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "EDDL");
				XzzyDialog qqdldialog = new XzzyDialog(context,"前期地类", qqdllist, qqdlei);
				BussUtil.setDialogParams(context, qqdldialog, 0.5, 0.5);
				break;
			/**前期林种*/
			case R.id.qqlzhong:
				List<Row> linzhonglist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "LINZHONG");
				XzzyDialog linzhongdialog = new XzzyDialog(context, "前期林种", linzhonglist, qqlzhong);
				BussUtil.setDialogParams(context, linzhongdialog, 0.5, 0.5);
				break;

			/**前期起源*/
			case R.id.qqqyuan:
				List<Row> qiyuanlist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "QIYUAN");
				XzzyDialog qiyuandialog = new XzzyDialog(context,"前期起源", qiyuanlist, qqqyuan);
				BussUtil.setDialogParams(context, qiyuandialog, 0.5, 0.5);
				break;
			/**前期优势树种*/
			case R.id.qqysszhong:
				List<Row> qqysszlist=LxqcUtil.getAttributeList(context,name,dbpath);
				XzzyDialog qqysszdialog = new XzzyDialog(context,"前期优势树种", qqysszlist, qqysszhong);
				BussUtil.setDialogParams(context, qqysszdialog, 0.5, 0.5);
				break;
			/**前期龄组*/
			case R.id.qqlzu:
				List<Row> qqlingzulist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "LINGZU");
				XzzyDialog qqlingzudialog = new XzzyDialog(context,"前期龄组", qqlingzulist, qqlzu);
				BussUtil.setDialogParams(context, qqlingzudialog, 0.5, 0.5);
				break;
			/**前期植被类型*/
			case R.id.qqzblxing:
				List<Row> qqzblxlist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "ZBLX");
				XzzyDialog qqzblxdialog = new XzzyDialog(context,"前期植被类型", qqzblxlist, qqzblxing);
				BussUtil.setDialogParams(context, qqzblxdialog, 0.5, 0.5);
				break;
			/**本期地类*/
			case R.id.bqdlei:
				List<Row> bqdllist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "EDDL");
				XzzyDialog bqdldialog = new XzzyDialog(context,"本期地类", bqdllist, bqdlei);
				BussUtil.setDialogParams(context, bqdldialog, 0.5, 0.5);
				break;
			/**本期林种*/
			case R.id.bqlzhong:
				List<Row> bqlingzhonglist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "LINZHONG");
				XzzyDialog bqlinzhongdialog = new XzzyDialog(context, "本期林种", bqlingzhonglist, bqlzhong);
				BussUtil.setDialogParams(context, bqlinzhongdialog, 0.5, 0.5);
				break;
			/**本期起源*/
			case R.id.bqqyuan:
				List<Row> bqqiyuanlist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "QIYUAN");
				XzzyDialog bqqiyuandialog = new XzzyDialog(context,"本期起源", bqqiyuanlist, bqqyuan);
				BussUtil.setDialogParams(context, bqqiyuandialog, 0.5, 0.5);
				break;

			/**本期优势树种*/
			case R.id.bqysszhong:
				List<Row> bqysszlist =LxqcUtil.getAttributeList(context,name,dbpath);
				XzzyDialog bqysszdialog = new XzzyDialog(context,"本期优势树种", bqysszlist, bqysszhong);
				BussUtil.setDialogParams(context, bqysszdialog, 0.5, 0.5);
				break;
			/**本期龄组*/
			case R.id.bqlzu:
				List<Row> bqlingzulist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "LINGZU");
				XzzyDialog bqlingzudialog = new XzzyDialog(context,"本期龄组", bqlingzulist, bqlzu);
				BussUtil.setDialogParams(context, bqlingzudialog, 0.5, 0.5);
				break;
			/**本期植被类型*/
			case R.id.bqzblxing:
				List<Row> bqzblxlist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "ZBLX");
				XzzyDialog bqzblxdialog = new XzzyDialog(context,"本期植被类型", bqzblxlist, bqzblxing);
				BussUtil.setDialogParams(context, bqzblxdialog, 0.5, 0.5);
				break;
			/**地类变化原因*/
			case R.id.dleibhyy:
				List<Row> wclzlqklist = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "DLBHYY");
				XzzyDialog dialog = new XzzyDialog(context, "地类变化原因", wclzlqklist, dleibhyy);
				BussUtil.setDialogParams(context, dialog, 0.5, 0.5);
				break;
			/**林种变化原因*/
			case R.id.lzhongbhyy:
				HzbjDialog lzbhyydialog=new HzbjDialog(context,  "样种变化原因", lzhongbhyy, null, "");
				BussUtil.setDialogParams(context, lzbhyydialog, 0.5, 0.5);
				break;
			/**起源变化原因*/
			case R.id.qyuanbhyy:
				HzbjDialog qybhyydialog=new HzbjDialog(context, "起源变化原因", qyuanbhyy, null, "");
				BussUtil.setDialogParams(context, qybhyydialog, 0.5, 0.5);
				break;
			/**优势树种变化原因*/
			case R.id.ysszhongbhyy:
				HzbjDialog ysszbhyydialog=new HzbjDialog(context, "优势树种变化原因", ysszhongbhyy,null, "");
				BussUtil.setDialogParams(context, ysszbhyydialog, 0.5, 0.5);
				break;
			/**龄组变化原因*/
			case R.id.lzubhyy:
				HzbjDialog linzubhyydialog=new HzbjDialog(context,"龄组变化原因", lzubhyy,null, "");
				BussUtil.setDialogParams(context, linzubhyydialog, 0.5, 0.5);
				break;
			/**植被类型变化原因*/
			case R.id.zblxingbhyy:
				HzbjDialog zblxbhyydialog=new HzbjDialog(context,"植被类型变化原因", zblxingbhyy, null, "");
				BussUtil.setDialogParams(context, zblxbhyydialog, 0.5, 0.5);
				break;
			/**样地有无特殊对待及说明*/
			case R.id.ywtsddjsm:
				HzbjDialog ywtsdddialog=new HzbjDialog(context,"样地有无特殊对待及说明", ywtsddjsm, null, "");
				BussUtil.setDialogParams(context, ywtsdddialog, 0.5, 0.5);
				break;

		}
	}

}