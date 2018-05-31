package com.titan.gzzhjc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by li on 2017/3/6.
 */

public class DBManager extends SQLiteOpenHelper{

    private SQLiteDatabase db;

    // 数据库版本号
    public static final int DATABASE_VERSION = 1;
    // 数据库名
    private static String DATABASE_NAME = "/db.sqlite";

    public DBManager(Context context,String databaseptah)
    {
        super(context, databaseptah, null, DATABASE_VERSION);
        File name = new File(databaseptah);
        db = SQLiteDatabase.openOrCreateDatabase(name, null);//读SD卡数据库必须如此--用静态方法打开数据库。
        db.getVersion();

    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public int getDbversion(){
        return  db.getVersion();
    }


    /**
     * query all persons, return list
     * @return List<Person>
     */
    public Map<String,String> queryGzLmcf(String name)
    {
        Map<String,String> map = new HashMap<>();
        String sql = "select GZLYJCB.DQNAME,GZLYJCB.NSLCFXE from TAB_DQ INNER JOIN GZLYJCB ON TAB_DQ.DQCODE = GZLYJCB.DQCODE where TAB_DQ.PID = " +
                "(select TAB_DQ.ID from TAB_DQ where TAB_DQ.DQNAME='"+name+"');";

        Cursor c = db.rawQuery(sql,null);
        boolean flag = c.moveToNext();
        while (flag)
        {
            String dqname = c.getString(c.getColumnIndex("DQNAME"));
            String cfxe = c.getString(c.getColumnIndex("NSLCFXE"));
            map.put(name,cfxe);
        }
        db.close();
        return map;
    }

    /**
     * query all persons, return cursor
     * @return Cursor
     */
    public Cursor queryTheCursor(String sql,SQLiteDatabase db)
    {
        Cursor c = null;
        if(db != null){
            c = db.rawQuery(sql,null);
        }
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        // 释放数据库资源
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
