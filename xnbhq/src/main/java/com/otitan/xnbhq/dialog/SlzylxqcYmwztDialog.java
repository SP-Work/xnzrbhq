package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LxqcMmjcdcAdapter;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.LxqcUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.customview.MorePopWindow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 样木位置图 dialog
 */
public class SlzylxqcYmwztDialog extends Dialog implements View.OnClickListener {
	Context context;
	List<HashMap<String, String>> list = new ArrayList<>();
	HashMap<String, String> map;
	private TextView selectym, dwd, lmlx, jclx, szdm, qqxj, bqxj, cfgllx, lincun, kjxh,
			fwj, spj, zhlx, bz, gdbz,lmlxqq,qqjclx,qqszdm,zcqqxj,zcbqxj,qqcfgllx,qqlincun
			,qqkjxh,qqfwj,qqspj,qqzhlx,qqbz;
	/**用来标记样木号*/
	private int flags = -1;
	String ydhselect;
	LxqcMmjcdcAdapter adapter;
	private MorePopWindow PopWindow;
	BaseActivity activity;
	PaintView view;
	private static final int TAKE_PICTURE = 0x000003;

	public SlzylxqcYmwztDialog(Context context,
							   List<HashMap<String, String>> list,String ydhselect,LxqcMmjcdcAdapter adapter) {
		super(context, R.style.Dialog);
		this.list = list;
		this.context = context;
		this.ydhselect=ydhselect;
		this.adapter=adapter;
		this.activity=(BaseActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slzylxqc_ymwzt_view);

		List<HashMap<String, Float>> ymwzlist = GetList();
		/**放样木位置图的控件*/
		LinearLayout linear=(LinearLayout) findViewById(R.id.linear);
		String startymh="";
		if(list != null && list.size() != 0){
			startymh=list.get(0).get("YMH");
		}
		view =new PaintView(context, ymwzlist,startymh);
		LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		linear.addView(view,Params);

		Button cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(this);
		Button takephoto = (Button) findViewById(R.id.takephoto);
		takephoto.setOnClickListener(this);
		Button sure = (Button) findViewById(R.id.sure);
		sure.setOnClickListener(this);

		Button backone = (Button) findViewById(R.id.backone);
		backone.setOnClickListener(this);
		Button nextone = (Button) findViewById(R.id.nextone);
		nextone.setOnClickListener(this);
		selectym = (TextView) findViewById(R.id.selectym);
		selectym.setOnClickListener(this);

		dwd = (TextView) findViewById(R.id.dwd);
		dwd.setOnClickListener(this);
		lmlx = (TextView) findViewById(R.id.lmlx);
		lmlx.setOnClickListener(this);
		jclx = (TextView) findViewById(R.id.jclx);
		jclx.setOnClickListener(this);
		szdm = (TextView) findViewById(R.id.szdm);
		szdm.setOnClickListener(this);
		qqxj = (TextView) findViewById(R.id.qqxj);
		qqxj.setOnClickListener(this);
		bqxj = (TextView) findViewById(R.id.bqxj);
		bqxj.setOnClickListener(this);
		cfgllx = (TextView) findViewById(R.id.cfgllx);
		cfgllx.setOnClickListener(this);
		lincun = (TextView) findViewById(R.id.lincun);
		lincun.setOnClickListener(this);
		kjxh = (TextView) findViewById(R.id.kjxh);
		kjxh.setOnClickListener(this);
		fwj = (TextView) findViewById(R.id.fwj);
		fwj.setOnClickListener(this);
		spj = (TextView) findViewById(R.id.spj);
		spj.setOnClickListener(this);
		zhlx = (TextView) findViewById(R.id.zhlx);
		zhlx.setOnClickListener(this);
		bz = (TextView) findViewById(R.id.bz);
		bz.setOnClickListener(this);
		gdbz = (TextView) findViewById(R.id.gdbz);
		gdbz.setOnClickListener(this);
		// 长按
		lmlxqq = (TextView) findViewById(R.id.lmlxqq);
		lmlxqq.setOnClickListener(this);
		qqjclx = (TextView) findViewById(R.id.qqjclx);
		qqjclx.setOnClickListener(this);
		qqszdm = (TextView) findViewById(R.id.qqszdm);
		qqszdm.setOnClickListener(this);
		zcqqxj = (TextView) findViewById(R.id.zcqqxj);
		zcqqxj.setOnClickListener(this);
		zcbqxj = (TextView) findViewById(R.id.zcbqxj);
		zcbqxj.setOnClickListener(this);
		qqcfgllx = (TextView) findViewById(R.id.qqcfgllx);
		qqcfgllx.setOnClickListener(this);
		qqlincun = (TextView) findViewById(R.id.qqlincun);
		qqlincun.setOnClickListener(this);
		qqkjxh = (TextView) findViewById(R.id.qqkjxh);
		qqkjxh.setOnClickListener(this);
		qqfwj = (TextView) findViewById(R.id.qqfwj);
		qqfwj.setOnClickListener(this);
		qqspj = (TextView) findViewById(R.id.qqspj);
		qqspj.setOnClickListener(this);
		qqzhlx = (TextView) findViewById(R.id.qqzhlx);
		qqzhlx.setOnClickListener(this);
		qqbz = (TextView) findViewById(R.id.qqbz);
		qqbz.setOnClickListener(this);
		// 赋初始值
		if (list != null && list.size() != 0) {
			flags = 0;
			map = list.get(0);
			dwd.setText(map.get("DWD"));
			lmlx.setText(map.get("LMLX"));
			jclx.setText(map.get("JCLX"));
			szdm.setText(map.get("SHUZ"));
			qqxj.setText(map.get("QQXJ"));
			bqxj.setText(map.get("BQXJ"));
			cfgllx.setText(map.get("CFGLLX"));
			lincun.setText(map.get("LINCEN"));
			kjxh.setText(map.get("KJDLXH"));
			fwj.setText(map.get("FWJ"));
			spj.setText(map.get("SPJ"));
			zhlx.setText(map.get("ZHLX"));
			bz.setText(map.get("BZ"));
			gdbz.setText(map.get("GDBZSM"));
			selectym.setText(map.get("YMH"));

			lmlxqq.setText(map.get("QQLMLX"));
			qqjclx.setText(map.get("QQJCLX"));
			qqszdm.setText(map.get("QQSHUZ"));
			zcbqxj.setText(map.get("QQXJ"));
			qqcfgllx.setText(map.get("QQCFGLLX"));
			qqlincun.setText(map.get("QQLINCEN"));
			qqkjxh.setText(map.get("QQKJDLXH"));
			qqfwj.setText(map.get("QQFWJ"));
			qqspj.setText(map.get("QQSPJ"));
			qqzhlx.setText(map.get("QQZHLX"));
			qqbz.setText(map.get("QQBZ"));

		}
	}

	private List<HashMap<String, Float>> GetList() {
		List<HashMap<String, Float>>ymwzlist=new ArrayList<HashMap<String,Float>>();
		for(int i=0;i<list.size();i++){
			HashMap<String, Float>tempmap=new HashMap<String, Float>();
			if(list.get(i).get("FWJ")!=""&&list.get(i).get("SPJ")!=""&&list.get(i).get("YMH")!=""){
				/**方位角*/
				double angle=Double.parseDouble(list.get(i).get("FWJ"));
				/**水平距*/
				float tempspj=Float.parseFloat(list.get(i).get("SPJ"));
				/**样木号*/
				float ymhtext=Float.parseFloat(list.get(i).get("YMH"));
				double tempx=Math.sin(angle* Math.PI / 180)*tempspj;
				double tempy=Math.cos(angle* Math.PI / 180)*tempspj;
				/**水平距离*/
				float x=0;
				/**竖直距离*/
				float y=0;
				/**对计算来的水平距进行处理保留小数点后一位*/
				if ((tempx + "").contains(".")) {
					String[] split = (tempx + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 1) {
							x = Float.parseFloat(split[0] + "."
									+ split[1].substring(0, 1));
						}
					}
				}
				/**对计算来的竖直距进行处理保留小数点后一位*/
				if ((tempy + "").contains(".")) {
					String[] split = (tempy + "").split("\\.", -1);
					if (split.length == 2) {
						if (split[1].length() > 1) {
							y = Float.parseFloat(split[0] + "."
									+ split[1].substring(0, 1));
						}
					}
				}
				tempmap.put("WIDTH", x);
				tempmap.put("HEIGHT", y);
				tempmap.put("NUMBER", ymhtext);
				ymwzlist.add(tempmap);
			}
		}
		return ymwzlist;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			// 返回
			case R.id.cancle:
				String[]zd1=context.getResources().getStringArray(R.array.mmjczd);
				DataBaseHelper.deleteLxqcMmjcData(context, ydhselect);
				for(int i=0;i<list.size();i++){
					DataBaseHelper.addLxqcMmjcData(context, zd1, list.get(i));
				}
				adapter.notifyDataSetChanged();
				dismiss();
				break;
			// 保存
			case R.id.sure:
				String[] zd2 = context.getResources()
						.getStringArray(R.array.mmjczd);
				DataBaseHelper.deleteLxqcMmjcData(context, ydhselect);
				for (int i = 0; i < list.size(); i++) {
					DataBaseHelper.addLxqcMmjcData(context, zd2, list.get(i));
				}
				ToastUtil.setToast(context,
						context.getResources().getString(R.string.bcsuccess));
				adapter.notifyDataSetChanged();
				break;
			// 照相
			case R.id.takephoto:
				takePhoto();
				break;
			// 前一个
			case R.id.backone:
				if (flags == -1) {
					ToastUtil.setToast(context, "暂无记录");
					selectym.setText("");
				} else if (flags == 0) {
					ToastUtil.setToast(context, "已是第一条数据");
				} else {
					flags = flags - 1;
					refreshData(flags);
				}
				break;
			// 下一个
			case R.id.nextone:
				if (flags + 1 < list.size()) {
					flags = flags + 1;
					refreshData(flags);
				} else if (flags + 1 == list.size()) {
					ToastUtil.setToast(context, "已经是最后一条数据");
				}
				break;
			// 选中的样木号
			case R.id.selectym:
				break;
			// 定位点
			case R.id.dwd:
				List<Row> dwdtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "DWD");
				XzzyDialog dwddialog = new XzzyDialog(context,"定位点", dwdtemp, dwd, map, "DWD");
				dwddialog.show();
				BussUtil.setDialogParams(context, dwddialog, 0.5, 0.5);
				break;
			// 立木类型
			case R.id.lmlx:
				List<Row> limutemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "LMLX");
				XzzyDialog limudialog = new XzzyDialog(context,"立木类型", limutemp, lmlx, map, "LMLX");
				limudialog.show();
				BussUtil.setDialogParams(context, limudialog, 0.5, 0.5);
				break;
			// 检尺类型
			case R.id.jclx:
				List<Row> jclxtemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "JCLX");
				XzzyDialog jclxdialog = new XzzyDialog(context,"检尺类型", jclxtemp, jclx, map, "JCLX");
				jclxdialog.show();
				BussUtil.setDialogParams(context, jclxdialog, 0.5, 0.5);
				break;
			// 树种代码
			case R.id.szdm:
				List<Row> sztemp = DataBaseHelper.searchShuZhongData(context);
				XzzyDialog szdialog = new XzzyDialog(context,"树种",sztemp, szdm, map, "SHUZ");
				szdialog.show();
				BussUtil.setDialogParams(context, szdialog, 0.5, 0.5);
				break;
			// 前期胸径
			case R.id.qqxj:
				LxqcUtil.showAlertDialog(context, "前期胸径", qqxj, map, "QQXJ", "0", "1", "2");
				break;
			// 本期胸径
			case R.id.bqxj:
				ShuziDialog xjshuzidialog = new ShuziDialog(context,"本期胸径", bqxj, map, "BQXJ", list, null, "0",
						"1", "");
				xjshuzidialog.show();
				BussUtil.setDialogParams(context, xjshuzidialog, 0.5, 0.5);
				break;
			// 采伐管理类型
			case R.id.cfgllx:
				List<Row> cfgltemp = ResourcesManager.getAssetsAttributeList(
						context, "slzylxqc.xml", "CFGLLX");
				XzzyDialog cfgldialog = new XzzyDialog(context,"采伐管理类型", cfgltemp, cfgllx, map, "CFGLLX");
				cfgldialog.show();
				BussUtil.setDialogParams(context, cfgldialog, 0.5, 0.5);
				break;
			// 林层
			case R.id.lincun:
				List<Row> lctemp = ResourcesManager.getAssetsAttributeList(context,
						"slzylxqc.xml", "LINCENG");
				XzzyDialog lcdialog = new XzzyDialog(context, "林层",
						lctemp, lincun, map, "LINCEN");
				lcdialog.show();
				BussUtil.setDialogParams(context, lcdialog, 0.5, 0.5);
				break;
			// 跨角序号
			case R.id.kjxh:
				ShuziDialog kjxhdialog = new ShuziDialog(context,"跨角序号", kjxh, map, "KJDLXH", null, null, "1", "0", "");
				kjxhdialog.show();
				BussUtil.setDialogParams(context, kjxhdialog, 0.5, 0.5);
				break;
			// 方位角
			case R.id.fwj:
				ShuziDialog fwjdialog = new ShuziDialog(context,"方位角(°)", fwj, map, "FWJ", null, null, "0", "1", "");
				fwjdialog.show();
				BussUtil.setDialogParams(context, fwjdialog, 0.5, 0.5);
				break;
			// 水平距
			case R.id.spj:
				ShuziDialog spjdialog = new ShuziDialog(context,"水平距(米)", spj, map, "SPJ", null, null, "0", "1", "");
				spjdialog.show();
				BussUtil.setDialogParams(context, spjdialog, 0.5, 0.5);
				break;
			// 灾害类型
			case R.id.zhlx:
				List<Row> zhlxtemp = ResourcesManager.getAssetsAttributeList(context,
						"slzylxqc.xml", "ZHLX");
				XzzyDialog zhlxdialog = new XzzyDialog(context,"灾害类型",zhlxtemp, zhlx, map, "ZHLX");
				zhlxdialog.show();
				BussUtil.setDialogParams(context, zhlxdialog, 0.5, 0.5);
				break;
			// 备注
			case R.id.bz:
				HzbjDialog bzdialog=new HzbjDialog(context, "备注", bz,map, "BZ");
				bzdialog.show();
				BussUtil.setDialogParams(context, bzdialog, 0.5, 0.5);
				break;
			// 固定标志
			case R.id.gdbz:
				HzbjDialog gdbzdialog=new HzbjDialog(context, "固定标志", gdbz,map, "GDBZSM");
				gdbzdialog.show();
				BussUtil.setDialogParams(context, gdbzdialog, 0.5, 0.5);
				break;
			// 前期立木类型
			case R.id.lmlxqq:
				showPop(lmlxqq, lmlx, map, "LMLX","");
				break;
			// 前期检尺类型
			case R.id.qqjclx:
				showPop(qqjclx, jclx, map, "JCLX","");
				break;
			// 前期树种代码
			case R.id.qqszdm:
				showPop(qqszdm, szdm, map, "SHUZ","");
				break;
			// 前期胸径
			case R.id.zcqqxj:
				showPop(zcqqxj, qqxj, map, "QQXJ","1");
				break;
			// 本期胸径
			case R.id.zcbqxj:
				showPop(zcbqxj, bqxj, map, "BQXJ","1");
				break;
			// 采伐管理类型
			case R.id.qqcfgllx:
				showPop(qqcfgllx, cfgllx, map, "CFGLLX","1");
				break;
			// 林层
			case R.id.qqlincun:
				showPop(qqlincun, lincun, map, "LINCEN","");
				break;
			// 跨角序号
			case R.id.qqkjxh:
				showPop(qqkjxh, kjxh, map, "KJDLXH","");
				break;
			// 方位角
			case R.id.qqfwj:
				showPop(qqfwj, fwj, map, "FWJ","");
				break;
			// 水平距
			case R.id.qqspj:
				showPop(qqspj, spj, map, "SPJ","");
				break;
			// 灾害类型
			case R.id.qqzhlx:
				showPop(qqzhlx, zhlx, map, "ZHLX","1");
				break;
			// 备注
			case R.id.qqbz:
				showPop(qqbz, bz, map, "BZ","");
				break;
		}
	}

	/**照相*/
	private void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File file = createImageFile();
			if (file != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				activity.startActivityForResult(intent, TAKE_PICTURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 存放文件位置 */
	private File createImageFile() throws IOException {
		File image = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String fileName= MyApplication.resourcesManager.getLxqcImagePath("5");
			String picname =MyApplication.resourcesManager.getLxqcImagename(ydhselect, "1", map.get("YMH"));
			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			image = new File(fileName + "/" + picname);
		} else {
			ToastUtil.setToast(context, "没有SD卡");
		}
		return image;
	}
	/**显示长按后的转抄当前POP*/
	private void showPop(final TextView item,final TextView bqtv, final HashMap<String, String> map2, final String zd,String type) {
		PopWindow = new MorePopWindow(activity, R.layout.popup_lxqc);
		PopWindow.showAsDropDown(item,
				item.getWidth() / 2 - PopWindow.getWidth() / 2,
				item.getHeight() - 35);
		TextView pop = (TextView) PopWindow.getConentView().findViewById(
				R.id.pop);
		pop.setText("转抄当前");
		if("1".equals(type)){
			ToastUtil.setToast(context, "当前项不准转抄！");
			return;
		}
		pop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String itemstr=item.getText().toString();
				bqtv.setText(itemstr);
				map2.put(zd, itemstr);
				PopWindow.dismiss();
			}
		});

	}
	/** 刷新数据 */
	private void refreshData(int temp) {
		if (list != null) {
			map = list.get(temp);
		}
		if (map != null) {
			dwd.setText(map.get("DWD"));
			lmlx.setText(map.get("LMLX"));
			jclx.setText(map.get("JCLX"));
			szdm.setText(map.get("SHUZ"));
			qqxj.setText(map.get("QQXJ"));
			bqxj.setText(map.get("BQXJ"));
			cfgllx.setText(map.get("CFGLLX"));
			lincun.setText(map.get("LINCEN"));
			kjxh.setText(map.get("KJDLXH"));
			fwj.setText(map.get("FWJ"));
			spj.setText(map.get("SPJ"));
			zhlx.setText(map.get("ZHLX"));
			bz.setText(map.get("BZ"));
			gdbz.setText(map.get("GDBZSM"));
			selectym.setText(map.get("YMH"));

			lmlxqq.setText(map.get("QQLMLX"));
			qqjclx.setText(map.get("QQJCLX"));
			qqszdm.setText(map.get("QQSHUZ"));
			zcbqxj.setText(map.get("QQXJ"));
			qqcfgllx.setText(map.get("QQCFGLLX"));
			qqlincun.setText(map.get("QQLINCEN"));
			qqkjxh.setText(map.get("QQKJDLXH"));
			qqfwj.setText(map.get("QQFWJ"));
			qqspj.setText(map.get("QQSPJ"));
			qqzhlx.setText(map.get("QQZHLX"));
			qqbz.setText(map.get("QQBZ"));
			view.setYmh(map.get("YMH"));
		}
	}
	class PaintView extends View{
		/**绘制的bitmap*/
		private Bitmap bitmap;
		private Bitmap bitmap1;
		private Bitmap bitmap2;
		List<HashMap<String, Float>>listtemp;
		String selectymh;
		public PaintView(Context context,List<HashMap<String, Float>>list,String startymh) {
			super(context);
			this.listtemp=list;
			this.selectymh=startymh;
			initBitmap();
		}
		/**设置选中的样木号*/
		public void setYmh(String ymh){
			this.selectymh=ymh;
			invalidate();
		}
		/**图片初始化*/
		private void initBitmap() {
			/**底图bitmap*/
			if(bitmap == null){
				bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lxqc_ymwzt));
			}
			/**未选中样木的图片bitmap*/
			if(bitmap1 == null){
				bitmap1 = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lxqc_ymwzt_point));
			}
			/**选中样木的图片bitmap*/
			if(bitmap2 == null){
				bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lxqc_selectpoint));
			}
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			float width=bitmap.getWidth();
			float height=bitmap.getHeight();
			canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2,
					getHeight() / 2 - bitmap.getHeight() / 2, null);
			for (int i = 0; i < listtemp.size(); i++) {
				HashMap<String, Float>map=listtemp.get(i);
				float x=map.get("WIDTH");
				float y=map.get("HEIGHT");
				float ym=map.get("NUMBER");
				/**样木号进行处理，如果样木号有小数点则去掉*/
				String num="";
				if ((ym + "").contains(".")) {
					String[] split = (ym + "").split("\\.", -1);
					if(split.length>0){
						num=split[0];
					}
				}else{
					num=ym+"";
				}
				x=width/26*x;
				y=height/26*y;
				if(selectymh!=""&&selectymh.equals(num)){
					canvas.drawBitmap(bitmap2, getWidth() / 2+x-bitmap2.getWidth()/2, getHeight() / 2-y-bitmap2.getHeight()/2, null);
					Paint paint=new Paint();
					paint.setColor(Color.RED);
					canvas.drawText(num,  getWidth() / 2+x+1, getHeight() / 2-y+bitmap2.getHeight()+1, paint);
				}else{
					canvas.drawBitmap(bitmap1, getWidth() / 2+x-bitmap1.getWidth()/2, getHeight() / 2-y-bitmap1.getHeight()/2, null);
					Paint paint=new Paint();
					paint.setColor(Color.BLUE);
					canvas.drawText(num,  getWidth() / 2+x+1, getHeight() / 2-y+bitmap1.getHeight()+1, paint);
				}
			}
		}
	}
}
