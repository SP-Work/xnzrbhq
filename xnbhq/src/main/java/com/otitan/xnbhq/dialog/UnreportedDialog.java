package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Emergency;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.baselibrary.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by otitan_li on 2018/5/31.
 * UnreportedDialog 未上传事件数据dialog
 */

public class UnreportedDialog extends Dialog {

    private Context mContext;
    private ArrayList<Emergency> dataList = new ArrayList<>();

    public UnreportedDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public UnreportedDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected UnreportedDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.unreportedata);

    }

    @Override
    protected void onStart() {
        super.onStart();

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
                        UnreportedDialog.this.dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.setToast(mContext,"网络连接错误");
                    }

                    @Override
                    public void onNext(String s) {
                        UnreportedDialog.this.dismiss();
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        if(s.equals("true")){
                            ToastUtil.setToast(mContext, "上报成功");
                        }else{
                            ToastUtil.setToast(mContext, "上报失败,检查网络");
                        }
                    }
                });
    }
}
