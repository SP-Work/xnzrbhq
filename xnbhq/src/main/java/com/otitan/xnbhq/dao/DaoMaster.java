package com.otitan.xnbhq.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.otitan.xnbhq.db.sqlite.Table;
import com.otitan.xnbhq.db.sqlite.TableUtils;

public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1002;
    private static Class<?> clazz;
    private static String path;
    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        YmDao.createTable(db, ifNotExists);
        JgdcDao.createTable(db, ifNotExists);
        CustomerDao.createTable(db, ifNotExists);
        OrderDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        YmDao.dropTable(db, ifExists);
        JgdcDao.dropTable(db, ifExists);
        CustomerDao.dropTable(db, ifExists);
        OrderDao.dropTable(db, ifExists);
    }

    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String dbpath, CursorFactory factory) {
            super(context, dbpath, factory, SCHEMA_VERSION);
            //createTableIfNotExist(path,this);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, true);
            //createTableIfNotExist(path,this);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper<T> extends OpenHelper {

        public DevOpenHelper(Context context,Class<T> classOfT,String dbpath) {
            super(context, dbpath, null);
            clazz = classOfT;
            path = dbpath;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(YmDao.class);
        registerDaoClass(JgdcDao.class);
        registerDaoClass(CustomerDao.class);
        registerDaoClass(OrderDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /** 如果表不存在就创建表 */
    public static void createTableIfNotExist(String path,SQLiteOpenHelper helper) {
        String tablename = getTableName();
        boolean result = false;
        if (tablename == null) {
            return;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = "select count(*) as c from " + path
                    + " where type ='table' and name ='" + tablename.trim()
                    + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
        }
        if (!result) {
            TableUtils.createTable(db, true, clazz);
        }

    }

    /** 根据注解获取表名 */
    public static String getTableName() {
        String tableName = null;
        Table myAnnotation = clazz.getAnnotation(Table.class);
        for (Method method : myAnnotation.annotationType().getDeclaredMethods()) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Object invoke = null;
            try {
                invoke = method.invoke(myAnnotation);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            tableName = invoke.toString();
        }
        return tableName;
    }


}
