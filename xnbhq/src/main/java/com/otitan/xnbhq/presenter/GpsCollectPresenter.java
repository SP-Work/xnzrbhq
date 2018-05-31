package com.otitan.xnbhq.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.esri.core.geometry.Point;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.SettingDialog;
import com.otitan.xnbhq.mview.IBaseView;
import com.otitan.xnbhq.util.ResourcesManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 路径轨迹 Presenter
 */
public class GpsCollectPresenter {

    private Context mContext;
    private IBaseView iBaseView;
    public int collection_type = 0;
    private String copydbpath = "";
    private String copydbname = "";

    public boolean isTravel() {
        return travel;
    }

    public void setTravel(boolean travel) {
        this.travel = travel;
    }

    public boolean travel = false;

    public int getCollection_type() {
        return collection_type;
    }

    public void setCollection_type(int collection_type) {
        this.collection_type = collection_type;
    }


    public GpsCollectPresenter(Context ctx, IBaseView iBaseView) {
        this.mContext = ctx;
        this.iBaseView = iBaseView;
    }

    /**弹出选择类型*/
    private AlertDialog leaveTypeDialog;
    public void showCollectionType(final View gpsCaijiInclude,final SettingDialog settingDialog){
        String[] list = mContext.getResources().getStringArray(R.array.collection_type);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("请选择");
        alertBuilder.setSingleChoiceItems(list, collection_type, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int index) {
                setCollection_type(index);
                leaveTypeDialog.dismiss();
                gpsCaijiInclude.setVisibility(View.VISIBLE);
                settingDialog.dismiss();
            }
        });
        leaveTypeDialog = alertBuilder.create();
        leaveTypeDialog.setInverseBackgroundForced(true);
        leaveTypeDialog.setCancelable(true);
        leaveTypeDialog.show();
    }

    /**复制轨迹数据库到选择对应文件夹*/
    public String copyGjDb(){
        String dbpath = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            copydbname = dateFormat.format(new Date())+"_guiji.sqlite";
            String fileDir = ResourcesManager.getInstance(mContext).getFolderPath(ResourcesManager.sqlite);
            dbpath = fileDir + "/"+ copydbname;
            InputStream db = mContext.getResources().getAssets().open("guiji.sqlite");
            FileOutputStream fos = new FileOutputStream(dbpath);
            byte[] buffer = new byte[8129];
            int count = 0;
            while ((count = db.read(buffer)) >= 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            db.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbpath;
    }
    /**添加数据到新建数据库内*/
    public void addGjdateTodb(Point curPoint){
        if(curPoint == null){
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String recodeTime = dateFormat.format(new Date());
        DataBaseHelper baseHelper = new DataBaseHelper();
        baseHelper.addGuijiData(mContext,MyApplication.macAddress, curPoint.getX(),curPoint.getY(), recodeTime, 0+"",copydbname);
    }



}
