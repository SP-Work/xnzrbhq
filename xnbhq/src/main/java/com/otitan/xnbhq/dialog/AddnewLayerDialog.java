package com.otitan.xnbhq.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.FileSelectAdapter;
import com.otitan.xnbhq.util.AssetHelper;
import com.otitan.xnbhq.util.BaseUtil;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ResourcesManager;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by li on 2017/6/7.
 * 新建图层dialog
 */

public class AddnewLayerDialog extends Dialog {

    private Context mContext;
    private int which = 0;

    public AddnewLayerDialog(@NonNull Context context, @StyleRes int themeResId, int which) {
        super(context, themeResId);
        this.mContext = context;
        this.which = which;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_folder);
        setCanceledOnTouchOutside(false);

        final EditText editText = (EditText) findViewById(R.id.filename_txt);

        ListView listView = (ListView) findViewById(R.id.folde_select);

        final List<File> folders  = ResourcesManager.getInstance(mContext).getOtmsFolder();
        FileSelectAdapter adapter = new FileSelectAdapter(mContext,folders);
        listView.setAdapter(adapter);
        BaseUtil.setHeight(adapter,listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(TextUtils.isEmpty(editText.getText())){
                    ToastUtil.setToast(mContext,"请输入图层名称");
                    return;
                }
                String newname = editText.getText().toString().trim();
                if(newname.equals("")){
                    ToastUtil.setToast(mContext,"图层名称不能为空格");
                    return;
                }
                String path = folders.get(position).getPath();
                String filename = "";
                if(which == 0){
                    filename  = "erdiao.geodatabase";
                }else if(which ==1){
                    filename  = "ldbg.geodatabase";
                }else{
                    filename  = "tongyong.geodatabase";
                }
                try {
                    ProgressDialogUtil.startProgressDialog(mContext);
                    AssetHelper.CopyAsset(mContext,path,filename,newname+".geodatabase");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isSave = true;
                dismiss();
            }
        });
        ImageView close = (ImageView) findViewById(R.id.sel_addnewlayer_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSave){
                    saveDialogTip(AddnewLayerDialog.this);
                }else{
                    dismiss();
                }
            }
        });

    }


    /**选择模板后弹出输入图层名称及文件存放地址选择*/
    private boolean isSave = false;
    private void showInputNameDialog(){
        final Dialog dialog = new Dialog(mContext,R.style.Dialog);
        dialog.setContentView(R.layout.dialog_select_folder);
        dialog.setCanceledOnTouchOutside(false);
        EditText editText = (EditText) dialog.findViewById(R.id.filename_txt);

        ListView listView = (ListView) dialog.findViewById(R.id.folde_select);

        try {
            List<File> folders  = ResourcesManager.getInstance(mContext).getOtmsFolder();
            FileSelectAdapter adapter = new FileSelectAdapter(mContext,folders);
            listView.setAdapter(adapter);
            BaseUtil.setHeight(adapter,listView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isSave = true;
            }
        });
        ImageView close = (ImageView) dialog.findViewById(R.id.sel_addnewlayer_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSave){
                    saveDialogTip(AddnewLayerDialog.this);
                }else{
                    dialog.dismiss();
                }
            }
        });
        BussUtil.setDialogParams(mContext, dialog, 0.5, 0.6);

        //        TextView nojiami = (TextView) findViewById(R.id.no_mi_model);
//        nojiami.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showInputNameDialog();
//            }
//        });
//
//        TextView jiami = (TextView) findViewById(R.id.mi_model);
//        jiami.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showInputNameDialog();
//            }
//        });
//
//        ImageView close = (ImageView) findViewById(R.id.type_addnewlayer_close);
//        close.setOnClickListener(new CancleListener(this));
    }

    /**文件是否保存成功提示*/
    private void saveDialogTip(final AddnewLayerDialog dialog){
        new AlertDialog.Builder(mContext)
                .setTitle("消息提示")
                .setMessage("图层文件未保存，确定关闭吗？")
                .setPositiveButton("是", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

}
