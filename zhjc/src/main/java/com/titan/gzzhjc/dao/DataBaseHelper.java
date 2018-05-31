package com.titan.gzzhjc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 将数据文件拷贝到移动设备中
 */
public class DataBaseHelper {

    private static String dbname = "echart/db.sqlite";
    static Map<String, String> map = null;

    /**
     * 检查文件是否存在
     */
    public static boolean checkDataBase(String fileDir, String filename) {
        SQLiteDatabase checkDB = null;
        String myPath = fileDir + "/" + filename;
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
            return false;
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * 从安装文件中拷贝到设备中
     */

    public static void CopyDatabase(Context context, String fileDir, String filename) {
        try {
            InputStream db = context.getResources().getAssets().open(filename);
            FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
            byte[] buffer = new byte[8129];
            int count = 0;

            while ((count = db.read(buffer)) >= 0) {
                fos.write(buffer, 0, count);
            }

            fos.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取新闻
     */

    public static String getNews(String fileDir, String filename) {
        SQLiteDatabase checkDB = null;
        String myPath = fileDir + "/" + filename;
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.execSQL("select * from KMLQ_info");
        } catch (SQLiteException e) {
            // database does't exist yet.
            return null;
        }

        return "";
    }



    /**
     *inputstream转byte[]
     */
    public static byte[] InputStreamToByte(InputStream in) throws IOException{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while((count = in.read(data,0,4096)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return outStream.toByteArray();
    }
}
