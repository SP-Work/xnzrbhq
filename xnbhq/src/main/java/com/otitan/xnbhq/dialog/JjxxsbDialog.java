package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ImageBrowseActivity;
import com.otitan.xnbhq.adapter.Recyc_imageAdapter;
import com.otitan.xnbhq.entity.Emergency;
import com.otitan.xnbhq.entity.Image;
import com.otitan.xnbhq.mview.IPicView;
import com.otitan.xnbhq.service.LiveNetworkMonitor;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.baselibrary.util.ToastUtil;
import com.titan.baselibrary.util.Util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.otitan.xnbhq.BaseActivity.PICK_PHOTO;

/**
 * Created by li on 2017/7/6.
 * 紧急信息上报
 */

public class JjxxsbDialog extends Dialog implements Recyc_imageAdapter.PicOnclick {

    private BaseActivity mContext;
    private JjxxsbDialog dialog;
    private RecyclerView recyc;
    private static int width = 0;
    private ArrayList<Image> images = new ArrayList<>();
    private IPicView picView;
    private DecimalFormat format = new DecimalFormat("0.000000");

    public static WindowManager.LayoutParams getDialogParams() {
        return dialogParams;
    }

    public void setDialogParams(WindowManager.LayoutParams dialogParams) {
        JjxxsbDialog.dialogParams = dialogParams;
    }

    public static WindowManager.LayoutParams dialogParams;

    public JjxxsbDialog(@NonNull BaseActivity context, @StyleRes int themeResId,IPicView baseView) {
        super(context, themeResId);
        this.mContext = context;
        width = this.getWindow().getAttributes().width;
        this.picView = baseView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jjxxsb);
        setCanceledOnTouchOutside(false);
        dialog = this;
        initview();

    }

    /*初始化控件*/
    private void initview() {
        ImageView close = (ImageView) findViewById(R.id.xxsb_close);
        close.setOnClickListener(new CancleListener(this));

//        Spinner sjlx = (Spinner) findViewById(R.id.sjlx_spinner);
//        sjlx.setSelection(0, false);
//        sjlx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        final EditText sjms = (EditText) findViewById(R.id.txt_sjms);
        final EditText sjmc = (EditText) findViewById(R.id.txt_fsdd);
        final EditText tel = (EditText) findViewById(R.id.txt_tel);
        final EditText beizhu = (EditText) findViewById(R.id.txt_beizhu);

        TextView txt_jd = findViewById(R.id.txt_jd);
        txt_jd.setText(format.format(picView.getGpsPoint().getX()));

        TextView txt_wd = findViewById(R.id.txt_wd);
        txt_wd.setText(format.format(picView.getGpsPoint().getY()));

        final TextView pic =(TextView) findViewById(R.id.xczp_pic);
        recyc = (RecyclerView) findViewById(R.id.txt_xczp);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectPic();
            }
        });

        //loadPhoto();

        TextView sure = (TextView) findViewById(R.id.xxsb_save);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //网络连接后上报成功
                Emergency emergency = new Emergency();
                emergency.setXJ_JD(picView.getGpsPoint().getX()+"");
                emergency.setXJ_WD(picView.getGpsPoint().getY()+"");
                emergency.setXJ_MSXX(sjms.getText().toString());
                emergency.setXJ_SJMC(sjmc.getText().toString());
                emergency.setREMARK(beizhu.getText().toString());
                emergency.setXJ_SBBH(MyApplication.macAddress);

                if(!RetrofitHelper.getInstance(mContext).networkMonitor.isConnected()){
                    ToastUtil.setToast(mContext,"网络未连接数据保存到本地");
                    //保存数据


                    return;
                }
                Gson gson = new Gson();
                for(String pic : picView.getPicList(PICK_PHOTO)){
                    String base = Util.picToString(pic);
                    Image image = new Image();
                    image.setBase(base);
                    image.setName(new File(pic).getName());
                    images.add(image);
                }
                String jsonimage = gson.toJson(images);

                emergency.setXJ_ZPDZ(jsonimage);
                String json = new Gson().toJson(emergency);
                if (MyApplication.getInstance().netWorkTip()) {
                    senInofToServer(json);
                }
            }
        });

    }

    /*发送数据*/
    private void senInofToServer(String json) {
        ProgressDialogUtil.startProgressDialog(mContext);
        Observable<String> oberver = RetrofitHelper.getInstance(mContext).getServer().upRequisitionInfo(json);
        oberver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.setToast(mContext,"网络连接错误");
                    }

                    @Override
                    public void onNext(String s) {
                        dialog.dismiss();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        if(s.equals("true")){
                            ToastUtil.setToast(mContext, "上报成功");
                        }else{
                            ToastUtil.setToast(mContext, "上报失败,检查网络");
                        }
                    }
                });
    }

    @Override
    public void setPicOnclick(View item, int position) {
        Intent intent = new Intent(mContext,ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images",picView.getPicList(PICK_PHOTO));
        intent.putExtra("position",position);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到选择图片界面
     */
    public void toSelectPic() {
        if (picView.getPicList(PICK_PHOTO).size() != 0 && picView.getPicList(PICK_PHOTO).size() != 9) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("重新选择会覆盖之前的图片");
            builder.setMessage("是否重新选择");
            builder.setCancelable(true);
            builder.setPositiveButton("重新选择", new OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(mContext, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
                    mContext.startActivityForResult(intent, PICK_PHOTO);
                }
            });
            builder.setNegativeButton("取消", new OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(16);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(16);
        }
        if (picView.getPicList(PICK_PHOTO).size() == 9) {
            ToastUtil.setToast(mContext, "照片最多只能选择9张");
            return;
        }
        if (picView.getPicList(PICK_PHOTO).size() == 0) {
            Intent intent = new Intent(mContext, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
            mContext.startActivityForResult(intent, PICK_PHOTO);
        }
    }

    /**选择图片后加载图片*/
    public void loadPhoto(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyc.setLayoutManager(layoutManager);
        Recyc_imageAdapter adapter = new Recyc_imageAdapter(mContext, picView.getPicList(PICK_PHOTO), dialogParams.width/4);
        recyc.setAdapter(adapter);
        adapter.setPicOnclick(dialog);
    }
}

