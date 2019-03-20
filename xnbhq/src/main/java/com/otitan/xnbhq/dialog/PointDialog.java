package com.otitan.xnbhq.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ImageBrowseActivity;
import com.otitan.xnbhq.adapter.GroupAdapter;
import com.otitan.xnbhq.adapter.PointAdapter;
import com.otitan.xnbhq.adapter.Recyc_imageAdapter;
import com.otitan.xnbhq.entity.CheckPoint;
import com.otitan.xnbhq.mview.IPicView;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.permission.PermissionsActivity;
import com.titan.baselibrary.permission.PermissionsChecker;
import com.titan.gzzhjc.entity.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

import static com.otitan.xnbhq.BaseActivity.PICK_PHOTO_POINT;

/**
 * Created by sp on 2019/3/12.
 * 野生动物调查
 */
public class PointDialog extends Dialog implements Recyc_imageAdapter.PicOnclick {

    @BindView(R.id.rv_point)
    RecyclerView mRv_point;
    @BindView(R.id.ll_new)
    LinearLayout mLl_new;
    @BindView(R.id.et_name)
    EditText mEt_name;
    @BindView(R.id.et_lon)
    EditText mEt_lon;
    @BindView(R.id.et_lat)
    EditText mEt_lat;
    @BindView(R.id.et_altitude)
    EditText mEt_altitude;
    @BindView(R.id.et_time)
    EditText mEt_time;
    @BindView(R.id.et_forest)
    EditText mEt_forest;
    @BindView(R.id.et_origin)
    EditText mEt_origin;
    @BindView(R.id.et_number)
    EditText mEt_number;
    @BindView(R.id.et_area)
    EditText mEt_area;
    @BindView(R.id.rv_pic)
    RecyclerView mRv_pic;

    private BaseActivity mContext;
    private IPicView picView;

    Box<CheckPoint> checkPointBox = MyApplication.boxStore.boxFor(CheckPoint.class);

    private static final String PREFS_NAME = "MyPrefsFile";
    private static SharedPreferences titanSp;

    private static WindowManager.LayoutParams dialogParams;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public PointDialog(@NonNull BaseActivity context, int themeResId, IPicView baseView) {
        super(context, themeResId);
        mContext = context;
        picView = baseView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_point);

        ButterKnife.bind(this);

        titanSp = mContext.getSharedPreferences(PREFS_NAME, 0);

        if (new PermissionsChecker(mContext).lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(mContext, PermissionsActivity.PERMISSIONS_REQUEST_CODE, PERMISSIONS);
        }

        initView();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);

        String lon = String.valueOf(BaseActivity.gpspoint.getX()); // 经度
        String lat = String.valueOf(BaseActivity.gpspoint.getY()); // 纬度
        mEt_lon.setText(lon);
        mEt_lat.setText(lat);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String today = format.format(new Date(System.currentTimeMillis()));
        mEt_time.setText(today);

        mRv_point.setLayoutManager(new LinearLayoutManager(mContext));
        mRv_point.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        initList();
    }

    private void initList() {
        final List<CheckPoint> checkPointList = checkPointBox.query().build().find();
        if (checkPointList.size() == 0) {
            mLl_new.setVisibility(View.VISIBLE);
            return;
        }
        PointAdapter pointAdapter = new PointAdapter(mContext, checkPointList);
        mRv_point.setAdapter(pointAdapter);
        pointAdapter.setItemClickListener(new PointAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long pointId = checkPointList.get(position).getID();
                titanSp.edit().putLong("PointID", pointId).apply();
                ToastUtil.setToast(mContext, "已选择\n" + checkPointList.get(position).getName());
                dismiss();
            }
        });
    }

    @OnClick({R.id.tv_add, R.id.et_pic, R.id.tv_determine, R.id.tv_cancel})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                int visibility = mLl_new.getVisibility();
                if (visibility == 8) {
                    mLl_new.setVisibility(View.VISIBLE);
                    mRv_point.setVisibility(View.GONE);
                } else {
                    mLl_new.setVisibility(View.GONE);
                    mRv_point.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_pic:
                toSelectPic();
                break;
            case R.id.tv_determine:
                getPointInfo();
                break;
            case R.id.tv_cancel:
                picView.getPicList(PICK_PHOTO_POINT).clear();
                dismiss();
                break;
        }
    }

    /**
     * 跳转到选择图片界面
     */
    private void toSelectPic() {
        if (picView.getPicList(PICK_PHOTO_POINT).size() != 0 && picView.getPicList(PICK_PHOTO_POINT).size() != 9) {
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
                    mContext.startActivityForResult(intent, PICK_PHOTO_POINT);
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
        if (picView.getPicList(PICK_PHOTO_POINT).size() == 9) {
            com.titan.baselibrary.util.ToastUtil.setToast(mContext, "照片最多只能选择9张");
            return;
        }
        if (picView.getPicList(PICK_PHOTO_POINT).size() == 0) {
            Intent intent = new Intent(mContext, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
            mContext.startActivityForResult(intent, mContext.PICK_PHOTO_POINT);
        }
    }

    /**选择图片后加载图片*/
    public void loadPhoto() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv_pic.setLayoutManager(layoutManager);
        Recyc_imageAdapter adapter = new Recyc_imageAdapter(mContext, picView.getPicList(PICK_PHOTO_POINT), dialogParams.width/4);
        mRv_pic.setAdapter(adapter);
        adapter.setPicOnclick(this);
    }

    private void getPointInfo() {
        String name = mEt_name.getText().toString().trim();
        String lon = mEt_lon.getText().toString().trim();
        String lat = mEt_lat.getText().toString().trim();
        String altitude = mEt_altitude.getText().toString().trim();
        String time = mEt_time.getText().toString().trim();
        String forest = mEt_forest.getText().toString().trim();
        String origin = mEt_origin.getText().toString().trim();
        String number = mEt_number.getText().toString().trim();
        String area = mEt_area.getText().toString().trim();
        String picPath = new Gson().toJson(picView.getPicList(PICK_PHOTO_POINT));

        if (name.isEmpty()) {
            ToastUtil.setToast(mContext, "请输入踏查点名称");
            return;
        }

        CheckPoint checkPoint = new CheckPoint();
        checkPoint.setName(name);
        checkPoint.setLon(lon);
        checkPoint.setLat(lat);
        checkPoint.setAltitude(altitude);
        checkPoint.setTime(time);
        checkPoint.setForest(forest);
        checkPoint.setOrigin(origin);
        checkPoint.setClassNumber(number);
        checkPoint.setArea(area);
        checkPoint.setPicPath(picPath);
        checkPointBox.put(checkPoint);

        initList();
        mRv_point.setVisibility(View.VISIBLE);
        mLl_new.setVisibility(View.GONE);
    }

    @Override
    public void setPicOnclick(View item, int position) {
        Intent intent = new Intent(mContext, ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images", picView.getPicList(PICK_PHOTO_POINT));
        intent.putExtra("position", position);
        mContext.startActivity(intent);
    }

    public void setDialogParams(WindowManager.LayoutParams dialogParams) {
        PointDialog.dialogParams = dialogParams;
    }
}
