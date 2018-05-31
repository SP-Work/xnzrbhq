package com.otitan.xnbhq.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.db.sqlite.AhibernateDao;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * 数据编辑
 * @param <T>
 */
public class DbHelperService<T> {
    private Context mContext;

    private DbHelper<T> mDatabaseHelper;

    private SQLiteDatabase mSQLiteDatabase;

    private AhibernateDao<T> mDao;

    private Class<T> clazz;

    private String dbpath = "";

    public Class<T> getClazz() {
        return clazz;
    }
    public void GetBaseClass(Class<T> classOfT){
        try {
            @SuppressWarnings("unchecked")
            Class<T> c = (Class<T>) Class.forName(classOfT.getName());
            clazz=c;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }//这里使用泛型通配符
    }

    public DbHelperService (Context context, Class<T> classOfT) {
        GetBaseClass(classOfT);
        this.mContext = context;
        try {
            dbpath = MyApplication.resourcesManager.getDataBase("db.sqlite");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mDatabaseHelper =new DbHelper<T>(mContext,dbpath,clazz);
        this.mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        this.mDao = new AhibernateDao<T>(this.mSQLiteDatabase);

    }

    // ===================object begin===========================
    public List<T> getObjectsByWhere(Map<String, String> where) {
        List<T> objectList = mDao.queryList(clazz, where);
        return objectList;
    }

    public List<T> getObjects(T t) {
        List<T> objectList = mDao.queryList(t);
        return objectList;
    }

    public boolean add(T t) {
        boolean flag = mDao.insert(t);
        return flag;
    }

    public boolean update(T t, Map<String, String> where) {
        boolean flag = mDao.update(t, where);
        return flag;
    }

    public boolean delete(Map<String, String> where) {
        boolean flag = mDao.delete(clazz, where);
        return flag;
    }
    // ===================object end===============================


}
