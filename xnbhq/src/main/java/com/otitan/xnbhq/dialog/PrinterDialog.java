package com.otitan.xnbhq.dialog;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dothantech.lpapi.LPAPI;
import com.dothantech.printer.IDzPrinter;
import com.lling.photopicker.PhotoPickerActivity;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.ImageBrowseActivity;
import com.otitan.xnbhq.adapter.Recyc_imageAdapter;
import com.otitan.xnbhq.mview.IPicView;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.permission.PermissionsActivity;
import com.titan.baselibrary.permission.PermissionsChecker;
import com.titan.gzzhjc.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.otitan.xnbhq.BaseActivity.PICK_PHOTO_POINT;
import static com.otitan.xnbhq.BaseActivity.PICK_PHOTO_PRINT;

/**
 * Created by sp on 2019/3/12.
 * 打印
 */
public class PrinterDialog extends Dialog implements Recyc_imageAdapter.PicOnclick {

    @BindView(R.id.tv_printer)
    TextView mTv_printer;
    @BindView(R.id.et_text)
    EditText mEt_text;
    @BindView(R.id.rv_pic)
    RecyclerView mRv_pic;

    private BaseActivity mContext;
    private IPicView picView;

    private static WindowManager.LayoutParams dialogParams;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private final Handler mHandler = new Handler();

    private LPAPI api;
    private List<IDzPrinter.PrinterAddress> pairedPrinters = new ArrayList<>();
    // 上次连接成功的设备对象
    private IDzPrinter.PrinterAddress mPrinterAddress = null;

    public PrinterDialog(@NonNull BaseActivity context, int themeResId, IPicView baseView) {
        super(context, themeResId);
        mContext = context;
        picView = baseView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_printer);

        ButterKnife.bind(this);

        if (new PermissionsChecker(mContext).lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(mContext, PermissionsActivity.PERMISSIONS_REQUEST_CODE, PERMISSIONS);
        }

        initView();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);

        // 调用LPAPI对象的init方法初始化对象
        this.api = LPAPI.Factory.createInstance(mCallback);


        // 尝试连接上次成功连接的打印机
        if (mPrinterAddress != null) {
            if (api.openPrinterByAddress(mPrinterAddress)) {
                // 连接打印机的请求提交成功，刷新界面提示
                mTv_printer.setText("打印机连接成功");
                return;
            }
        }
    }

    @OnClick({R.id.iv_close, R.id.tv_printer, R.id.tv_text, R.id.tv_pic, R.id.tv_determine, R.id.tv_cancel})
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_printer:
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if (btAdapter == null) {
                    ToastUtil.setToast(mContext, "当前设备不支持蓝牙");
                    return;
                }

                if (!btAdapter.isEnabled()) {
                    ToastUtil.setToast(mContext, "蓝牙适配器未开启");
                    return;
                }

                pairedPrinters = api.getAllPrinterAddresses(null);
                new AlertDialog.Builder(mContext)
                        .setTitle("选择已绑定的设备")
                        .setAdapter(new DeviceListAdapter(), new DeviceListItemClicker()).show();
                break;
            /*case R.id.tv_text:
                int visibility = mEt_text.getVisibility();
                if (visibility == 8) {
                    mEt_text.setVisibility(View.VISIBLE);
                } else {
                    mEt_text.setVisibility(View.GONE);
                }
                break;*/
            case R.id.tv_pic:
                toSelectPic();
                break;
            case R.id.tv_determine:
                mEt_text.setVisibility(View.VISIBLE);
                String text = mEt_text.getText().toString().trim();
                if (text.isEmpty()) {
                    ToastUtil.setToast(mContext, "请输入打印内容");
                } else if (isPrinterConnected()) {
                    textPrint(text);
                }
                printPic();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private void printPic() {
        ArrayList<String> picList = picView.getPicList(PICK_PHOTO_PRINT);
        String path = picList.get(0);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            Bundle param = new Bundle();
            param.putInt(IDzPrinter.PrintParamName.PRINT_COPIES, 1);
            param.putInt(IDzPrinter.PrintParamName.PRINT_DIRECTION, 0);
            api.printBitmap(bitmap, param);
        }
    }

    private void textPrint(String text) {
        if (isPrinterConnected()) {
            if (printText(text)) {
//                onPrintStart();
            } else {
//                onPrintFailed();
            }
        }
    }

    // 打印文本
    private boolean printText(String text) {
        // 开始绘图任务，传入参数(页面宽度, 页面高度)
        api.startJob(48, 50, 0);

        // 开始一个页面的绘制，绘制文本字符串
        // 传入参数(需要绘制的文本字符串, 绘制的文本框左上角水平位置, 绘制的文本框左上角垂直位置, 绘制的文本框水平宽度, 绘制的文本框垂直高度, 文字大小, 字体风格)
        api.drawText(text, 10, 2, 40, 30, 4);

        // 结束绘图任务提交打印
        return api.commitJob();
    }

    /*// 开始打印标签时操作
    private void onPrintStart() {
        // 开始打印标签时，刷新界面提示
        showStateAlertDialog(R.string.nowisprinting);
    }*/

    private final LPAPI.Callback mCallback = new LPAPI.Callback() {

        /****************************************************************************************************************************************/
        // 所有回调函数都是在打印线程中被调用，因此如果需要刷新界面，需要发送消息给界面主线程，以避免互斥等繁琐操作。
        /****************************************************************************************************************************************/

        // 打印机连接状态发生变化时被调用
        @Override
        public void onStateChange(IDzPrinter.PrinterAddress arg0, IDzPrinter.PrinterState arg1) {
            final IDzPrinter.PrinterAddress printer = arg0;
            switch (arg1) {
                case Connected:
                case Connected2:
                    // 打印机连接成功，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 连接打印机成功时，刷新界面提示，保存相关信息
                            ToastUtil.setToast(mContext, "连接打印机成功");
                            mTv_printer.setText("连接打印机成功");
                            mPrinterAddress = printer;
                        }
                    });
                    break;

                case Disconnected:
                    // 打印机连接失败、断开连接，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            onPrinterDisconnected();
                        }
                    });
                    break;

                default:
                    break;
            }
        }

        // 蓝牙适配器状态发生变化时被调用
        @Override
        public void onProgressInfo(IDzPrinter.ProgressInfo arg0, Object arg1) {
        }

        @Override
        public void onPrinterDiscovery(IDzPrinter.PrinterAddress arg0, IDzPrinter.PrinterInfo arg1) {
        }

        // 打印标签的进度发生变化是被调用
        @Override
        public void onPrintProgress(IDzPrinter.PrinterAddress address, Object bitmapData, IDzPrinter.PrintProgress progress, Object addiInfo) {
            switch (progress) {
                case Success:
                    // 打印标签成功，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            onPrintSuccess();
                        }
                    });
                    break;

                case Failed:
                    // 打印标签失败，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            onPrintFailed();
                        }
                    });
                    break;

                default:
                    break;
            }
        }
    };

    // 判断当前打印机是否连接
    private boolean isPrinterConnected() {
        // 调用LPAPI对象的getPrinterState方法获取当前打印机的连接状态
        IDzPrinter.PrinterState state = api.getPrinterState();

        // 打印机未连接
        if (state == null || state.equals(IDzPrinter.PrinterState.Disconnected)) {
            ToastUtil.setToast(mContext, "打印机未连接");
            return false;
        }

        // 打印机正在连接
        if (state.equals(IDzPrinter.PrinterState.Connecting)) {
            ToastUtil.setToast(mContext, "打印机正在连接");
            return false;
        }

        // 打印机已连接
        return true;
    }

    // 用于填充打印机列表的Adapter
    private class DeviceListAdapter extends BaseAdapter {
        private TextView tv_name = null;
        private TextView tv_mac = null;

        @Override
        public int getCount() {
            return pairedPrinters.size();
        }

        @Override
        public Object getItem(int position) {
            return pairedPrinters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_printer, null);
            }
            tv_name = convertView.findViewById(R.id.tv_device_name);
            tv_mac = convertView.findViewById(R.id.tv_macaddress);

            if (pairedPrinters != null && pairedPrinters.size() > position) {
                IDzPrinter.PrinterAddress printer = pairedPrinters.get(position);
                tv_name.setText(printer.shownName);
                tv_mac.setText(printer.macAddress);
            }

            return convertView;
        }
    }

    // 打印机列表的每项点击事件
    private class DeviceListItemClicker implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            IDzPrinter.PrinterAddress printer = pairedPrinters.get(which);
            if (printer != null) {
                // 连接选择的打印机
                if (api.openPrinterByAddress(printer)) {
                    // 连接打印机的请求提交成功，刷新界面提示
                    return;
                }
            }

            // 连接打印机失败，刷新界面提示
            mTv_printer.setText("连接打印机失败");
        }
    }

    private void toSelectPic() {
        if (picView.getPicList(PICK_PHOTO_PRINT).size() != 0 && picView.getPicList(PICK_PHOTO_PRINT).size() != 9) {
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
                    mContext.startActivityForResult(intent, PICK_PHOTO_PRINT);
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
        if (picView.getPicList(PICK_PHOTO_PRINT).size() == 9) {
            com.titan.baselibrary.util.ToastUtil.setToast(mContext, "照片最多只能选择9张");
            return;
        }
        if (picView.getPicList(PICK_PHOTO_PRINT).size() == 0) {
            Intent intent = new Intent(mContext, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
            mContext.startActivityForResult(intent, mContext.PICK_PHOTO_PRINT);
        }
    }

    /**选择图片后加载图片*/
    public void loadPhoto() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv_pic.setLayoutManager(layoutManager);
        Recyc_imageAdapter adapter = new Recyc_imageAdapter(mContext, picView.getPicList(PICK_PHOTO_PRINT), dialogParams.width/4);
        mRv_pic.setAdapter(adapter);
        adapter.setPicOnclick(this);
    }

    @Override
    public void setPicOnclick(View item, int position) {
        Intent intent = new Intent(mContext, ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images", picView.getPicList(PICK_PHOTO_PRINT));
        intent.putExtra("position", position);
        mContext.startActivity(intent);
    }

    public void setDialogParams(WindowManager.LayoutParams dialogParams) {
        PrinterDialog.dialogParams = dialogParams;
    }
}
