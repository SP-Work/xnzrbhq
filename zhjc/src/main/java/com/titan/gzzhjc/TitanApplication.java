package com.titan.gzzhjc;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.titan.gzzhjc.dao.DataBaseHelper;

import java.io.File;

/**
 * Created by whs on 2017/2/28
 */

public class TitanApplication extends Application {
    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        /** Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         * 发布新版本时需要修改以及bugly isbug需要改成false等部分
         */
        CrashReport.initCrashReport(getApplicationContext(), "0153a45832", true);
        initDatabase();

    }

    /**
     * 初始化数据
     */
    public void initDatabase() {
        try {
            boolean exitflag = false;
            String daname = "db.sqlite";
            String path = mContext.getFilesDir().getPath();
            File file = new File(path);
            if (file.exists()) {
                File ff = new File(path + "/" + "test2.db");//上次为1 或者 0
                boolean flag = ff.exists();
                if (!flag) {
                    exitflag = DataBaseHelper.checkDataBase(path, daname);
                    if (exitflag) {
                        //删除数据库
                        new File(path + "/" + daname).delete();
                        DataBaseHelper.CopyDatabase(mContext, path, daname);
                    }
                    ff.createNewFile();
                }
            }
            if(!exitflag){
                DataBaseHelper.CopyDatabase(mContext, path, daname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
