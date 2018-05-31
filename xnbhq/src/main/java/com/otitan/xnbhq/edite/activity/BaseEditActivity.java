package com.otitan.xnbhq.edite.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by li on 2017/6/16.
 * 属性编辑的基activity
 */
public abstract class BaseEditActivity extends Activity {

    protected static final String EXTRA_LINES = "EXTRA_LINES";

    protected View childView;
    protected Context mContext;
    protected static int LINE_NUM;
    protected MyFeture myFeture = null;
    protected ListView listView;
    /** 工程名称 */
    protected String pname;
    protected String cname;
    /** 工程所在地址 */
    protected String path;
    /***/
    protected String parentStr = "";
    /**上个页面数据的属性的id*/
    protected int parentId = -1;
    /**编辑的feature*/
    protected GeodatabaseFeature selGeoFeature;
    /**县乡村的代码结构*/
    protected Map<String, String> xianMap = new HashMap<>();
    protected Map<String, String> xiangMap = new HashMap<>();
    protected Map<String, String> cunMap = new HashMap<>();
    /**当前页面数据属性的id*/
    protected long fid = -1;
    protected FeatureLayer featureLayer;
    protected List<Field> fieldList = new ArrayList<>();
    protected DecimalFormat df = new DecimalFormat("0.00");
    /**图片字段*/
    protected Line pcLine;
    protected String picname;
    protected EditText zpeditText;
    /**图片保存地址*/
    protected String picPath = "";
    protected static final int TAKE_PICTURE = 0x000001;
    /** 记录小班的唯一编号 当前小班的小班号*/
    protected String currentxbh="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_edit);
        mContext = BaseEditActivity.this;

        initData();

        myFeture = (MyFeture) getIntent().getSerializableExtra("myfeture");
        parentStr = (String) getIntent().getSerializableExtra("parent");
        parentId =  Integer.parseInt((String) getIntent().getSerializableExtra("id"));

        currentxbh = getXbhData();

        pname = myFeture.getPname();
        path = myFeture.getPath();
        cname = myFeture.getCname();

        selGeoFeature = myFeture.getFeature();
        fid = selGeoFeature.getId();
        featureLayer = myFeture.getMyLayer().getLayer();
        fieldList = selGeoFeature.getTable().getFields();
        LINE_NUM = selGeoFeature.getAttributes().size();

    }

    public abstract View getParentView();

    /**初始化县乡村结构代码*/
    private void initData(){
        xianMap = Util.getXianValue(mContext);
        xiangMap = Util.getXiangValue(mContext);
        cunMap = Util.getCunValue(mContext);
    }

    /** 绑定数据*/
    @SuppressLint("SimpleDateFormat")
    public ArrayList<Line> createLines() {
        ArrayList<Line> lines = new ArrayList<Line>();
        String xianD = "",xiangD= "";
        for (int i = 0; i < LINE_NUM; i++) {
            Field field = fieldList.get(i);
            Line line = new Line();
            line.setNum(i);
            line.setTview(field.getAlias());
            line.setfLength(field.getLength());
            line.setKey(field.getName());
            CodedValueDomain domain = (CodedValueDomain) field.getDomain();
            line.setDomain(domain);
            line.setFieldType(field.getFieldType());
            boolean floag = field.isNullable();
            line.setNullable(floag);

            Object obj = selGeoFeature.getAttributes().get(field.getName());
            if(obj != null){
                String value = obj.toString();

                if(field.getAlias().contains("县") || field.getAlias().contains("XIAN") ||field.getAlias().contains("xian")){
                    xianD = value;
                }else if(field.getAlias().contains("乡") || field.getAlias().contains("XIANG") ||field.getAlias().contains("xiang")){
                    xiangD = value;
                }

                if(line.getFieldType() == Field.esriFieldTypeDate){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if(!obj.equals("0:00:00")){
                        Date date = new Date(Long.parseLong(obj.toString()));
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        value = sdf.format(date);
                    }
                }

                if(domain != null){
                    Map<String, String> values = domain.getCodedValues();
                    for(String key : values.keySet()){

                        if(key.equals(value)){
                            line.setText(values.get(key));
                            break;
                        }else if(field.getAlias().contains("乡") || field.getAlias().contains("XIANG") ||field.getAlias().contains("xianG")){
                            if(!value.contains(xianD) && key.equals(xianD+value)){
                                line.setText(values.get(key));
                                break;
                            }else{
                                line.setText(value);
                            }
                        }else if(field.getAlias().contains("村")|| field.getAlias().contains("CUN") ||field.getAlias().contains("cun")){
                            if(value.contains(xiangD) && key.equals(xianD+value)){
                                line.setText(values.get(key));
                                break;
                            }else if(key.equals(xianD+xiangD+value)){
                                line.setText(values.get(key));
                                break;
                            }else{
                                line.setText(value);
                            }
                        }else{
                            line.setText(value);
                        }
                    }
                }else{

                    if(field.getAlias().contains("县") || field.getName().equals("xian") || field.getName().equals("XIAN")){
                        xianD = value;
                        String str = Util.getXXCValue(mContext, value, "", xianMap);
                        line.setText(str);
                    }else if(field.getAlias().contains("乡") || field.getName().equals("xiang") || field.getName().equals("XIANG")){
                        xiangD = value;
                        String str = Util.getXXCValue(mContext, value, xianD, xiangMap);
                        line.setText(str);
                    }else if(field.getAlias().contains("村") || field.getName().equals("cun") || field.getName().equals("CUN")){
                        String str = value;
                        if(xiangD.contains(xianD)){
                            str = Util.getXXCValue(mContext, value, xiangD, cunMap);
                        }else{
                            str = Util.getXXCValue(mContext, value,xianD+xiangD, cunMap);
                        }
                        line.setText(str);
                    }else{
                        List<Row> list = isDMField(field.getName());
                        if(list != null && list.size() > 0){
                            for(Row row : list){
                                if(row.getId().equals(value)){
                                    line.setText(row.getName());
                                    break;
                                }else{
                                    line.setText(value);
                                }
                            }
                        }else{
                            line.setText(value);
                        }
                    }
                }
            }else{
                line.setText("");
            }
            lines.add(line);
        }
        return lines;
    }

    /** 检测字段是那种类型的字段 */
    protected List<Row> isDMField(String key) {
        List<Row> list = BussUtil.getConfigXml(mContext, pname, key);
        return list;
    }

    /** 拍照*/
    public String mCurrentPhotoPath;// 图片路径
    //tname 为字段
    public void takephoto(Line line,EditText editText){
        this.pcLine = line;
        this.picname = line.getKey();
        this.zpeditText = editText;
        takephotop(new View(mContext));
    }

    public void takephotop(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        @SuppressWarnings("static-access")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String time = formatter.format(new Date());
        //String picname = currentxbh + "-" + time + ".jpg";
        String txt = zpeditText.getText() == null ? "" : zpeditText.getText().toString();
        String[] names = txt.split(",");
        int num = 0;
        if(names.length == 1 && txt.equals("")){
            num = 1;
        }else if(names.length == 1 && !txt.equals("")){
            num = 2;
        }else{
            num = names.length+1;
        }

        String picname = "";
        if(currentxbh != null && !currentxbh.equals("")){
            picname = currentxbh+"_"+(num)+".jpg";
        }else{
            picname = time+".jpg";
        }

        File file = new File(picPath +"/"+picname);
        file = picIsExst(file);
        mCurrentPhotoPath = file.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                Uri photoURI = FileProvider.getUriForFile(mContext, this.getApplicationContext().getPackageName() + ".provider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (photoURI != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }

        startActivityForResult(intent, TAKE_PICTURE);
    }

    /**检查照片是否存在*/
    public File picIsExst(File file){
        if(file.exists()){
            String name = file.getName();
            String[] names = name.split("_");
            String[] sss = names[1].split("\\.");
            name = names[0]+"_"+(Integer.parseInt(sss[0])+1)+".jpg";
            file = new File(picPath +"/"+name);
            return picIsExst(file);
        }else{
            return file;
        }
    }

    /** 图片浏览*/
    public void lookpictures(Activity activity){

        List<File> lst = MyApplication.resourcesManager.getImages(picPath);
        if(lst.size() == 0){
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("xbh", currentxbh);
        intent.putExtra("picPath", picPath);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    /** 图片浏览*/
    public void lookpictures(Activity activity,Line line){
        String pcname = line.getText();
        String[] pcarray = pcname.split(",");
        List<File> lst = ResourcesManager.getImages(picPath,pcarray);
        if(lst.size() == 0){
            ToastUtil.setToast(mContext, "没有图片");
            return;
        }

        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("xbh", currentxbh);
        intent.putExtra("picPath", picPath);
        intent.putExtra("type", pcname);
        startActivity(intent);
    }

    /**返回时检测必填字段*/
    public void finishThis(){
        GeodatabaseFeature geoFeature = (GeodatabaseFeature) featureLayer.getFeature(selGeoFeature.getId());
        boolean flag = false;
        String obString = null;
        Map<String,Object> map = geoFeature.getAttributes();
        for(Field field : fieldList){
            boolean ff = field.isNullable();
            if(!ff && map != null){
                Object value = map.get(field.getName());
                if(value == null || value.equals(" ") || value.equals("")){
                    flag = true;
                    obString = field.getAlias();
                    break;
                }
            }
        }

        if(flag){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("必填字段( "+obString +" )未填写完整,继续返回吗？");
            builder.setTitle("信息提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else{
            finish();
        }

    }

    /** 获取当前时间 */
    protected static String getCurrTime(String pattern) {
        if (pattern == null) {
            pattern = "yyyyMMddHHmmss";
        }
        return (new SimpleDateFormat(pattern)).format(new Date());
    }

    /** 照片添加时间水印*/
    protected void dealPhotoFile(final String file) {
        BaseEditActivity.PhotoTask task = new BaseEditActivity.PhotoTask(file);
        task.start();
    }

    /** 照片添加时间水印线程*/
    private class PhotoTask extends Thread {
        private String file;

        public PhotoTask(String file) {
            this.file = file;
        }

        @Override
        public void run() {
            BufferedOutputStream bos = null;
            Bitmap icon = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file, options); // 此时返回bm为空
                int height1 = options.outHeight;
                int width1 = options.outWidth;
                float percent = height1 > width1 ? height1 / 960f : width1 / 960f;

                if (percent < 1) {
                    percent = 1;
                }
                //int width = (int) (width1 / percent);
                //int height = (int) (height1 / percent);
                icon = Bitmap.createBitmap(width1, height1, Bitmap.Config.ARGB_8888);

                // 初始化画布 绘制的图像到icon上
                Canvas canvas = new Canvas(icon);
                // 建立画笔
                Paint photoPaint = new Paint();
                // 获取跟清晰的图像采样
                photoPaint.setDither(true);
                // 过滤一些
                // photoPaint.setFilterBitmap(true);
                options.inJustDecodeBounds = false;

                Bitmap prePhoto = BitmapFactory.decodeFile(file);
                if (percent > 1) {
                    prePhoto = Bitmap.createScaledBitmap(prePhoto, width1, height1, true);
                }

                canvas.drawBitmap(prePhoto, 0, 0, photoPaint);

                if (prePhoto != null && !prePhoto.isRecycled()) {
                    prePhoto.recycle();
                    prePhoto = null;
                    System.gc();
                }

                // 设置画笔
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
                // 字体大小
                textPaint.setTextSize(50.0f);
                // 采用默认的宽度
                textPaint.setTypeface(Typeface.DEFAULT);
                // 采用的颜色
                textPaint.setColor(Color.RED);
                // 阴影设置
                //textPaint.setShadowLayer(3f, 1, 1, Color.DKGRAY);

                // 时间水印
                String mark = "";
                String don = "  拍摄时间:"+getCurrTime("yyyy-MM-dd HH:mm:ss")+"经度:"+BaseActivity.currentPoint.getX() +"纬度:"+ BaseActivity.currentPoint.getY();
                if(pcLine == null){
                    mark = "唯一号:"+currentxbh +don;
                }else{
                    mark = pcLine.getTview()+"    唯一号:"+currentxbh +don;
                }

                float textWidth = textPaint.measureText(mark);
                canvas.drawText(mark, width1 - textWidth - 10, height1 - 26, textPaint);

                bos = new BufferedOutputStream(new FileOutputStream(file));

                //int quaility = (int) (100 / percent > 80 ? 80 : 100 / percent);
                icon.compress(Bitmap.CompressFormat.JPEG, 95, bos);

                bos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (icon != null && !icon.isRecycled()) {
                    icon.recycle();
                    icon = null;
                    System.gc();
                }
            }

        }
    }


    /**显示照片编号*/
    protected void updateZPBH(){
        runOnUiThread(new Runnable() {
            public void run() {
                String bftxt = "";
                if(pcLine != null){
                    bftxt = pcLine.getText();
                }

                if(bftxt == null || bftxt.equals("")){
                    File file = new File(mCurrentPhotoPath);
                    if(file.exists()){
                        zpeditText.setText(file.getName());
                    }
                }else{
                    File file = new File(mCurrentPhotoPath);
                    if(file.exists()){
                        zpeditText.setText(bftxt+","+ file.getName());
                    }
                }
                CursorUtil.setEditTextLocation(zpeditText);
            }
        });
    }

    //	/** 获取小班号数据 */
	public String getXbhData()
	{
        String xbh = "";
        /**获取图斑的唯一编号*/
        Object obj = myFeture.getFeature().getAttributes().get("WYBH");
        if(obj != null){
            xbh = obj.toString();
        }else {
            List<Field> list = myFeture.getFeature().getTable().getFields();
            for(Field field :list){
                String name = field.getName();
                String alias = field.getAlias();
                Object value = myFeture.getFeature().getAttributeValue(field);
                if(value != null && (name.equals("DJH")||name.equals("djh") ||alias.equals("地籍号"))){
                    xbh = value.toString();
                }
            }
        }

        if(xbh.equals("")){
            String strkey = "";
            List<Field> list = myFeture.getFeature().getTable().getFields();
            for(Field field :list){
                String name = field.getName();
                String alias = field.getAlias();
                Object value = myFeture.getFeature().getAttributeValue(field);
                if(value != null && (name.equals("XIAN")||name.equals("xian") ||alias.equals("县"))){
                    strkey = strkey + value.toString();
                }else if(value != null && (name.equals("XIANG")||name.equals("xiang") ||alias.equals("乡"))){
                    if(value.toString().contains(strkey)){
                        strkey = value.toString();
                    }else{
                        strkey = strkey + value.toString();
                    }
                }else if(value != null && (name.equals("CUN")||name.equals("cun") ||alias.equals("村"))){
                    if(value.toString().contains(strkey)){
                        strkey = value.toString();
                    }else{
                        strkey = strkey + value.toString();
                    }
                }else if(value != null && (name.equals("XBH")||name.equals("xbh") ||alias.equals("小班号"))){
                    strkey = strkey + value.toString();
                }
            }

            xbh = strkey;
        }
        return xbh;
	}


    /** 获取图片保存地址*/
    public String getImagePath(String path){
        File file = new File(path);
        String path1 = file.getParent()+ "/images";
        File file2 = new File(path1);
        boolean flag = file2.exists();
        if(!flag){
            file2.mkdirs();
        }
        if(currentxbh==null || currentxbh.equals("")){
            picPath = path1;
        }else{
            String path2 = file2.getPath()+"/"+currentxbh;
            File file3 = new File(path2);
            if(!file3.exists()){
                file3.mkdirs();
            }
            picPath = file3.getPath();
        }
        return picPath;
    }
}
