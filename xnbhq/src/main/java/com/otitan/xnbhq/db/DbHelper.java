package com.otitan.xnbhq.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.otitan.xnbhq.db.sqlite.Table;
import com.otitan.xnbhq.db.sqlite.TableUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 数据创建帮助类
 * @param <T>
 */
public class DbHelper<T> extends SQLiteOpenHelper{

	public static final int SCHEMA_VERSION = 2000;
	private Class<?> clszz;
	private String dbpath;

	public DbHelper(Context context, String name,Class<T> clazz){
		super(context, name, null,SCHEMA_VERSION);
		this.clszz = clazz;
		this.dbpath = name;
		createTableIfNotExist(dbpath, this);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableUtils.createTable(db, true, clszz);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		TableUtils.dropTable(db, clszz);
		onCreate(db);
	}

	/** 如果表不存在就创建表 */
	public void createTableIfNotExist(String path,SQLiteOpenHelper helper) {
		String tablename = getTableName();
		boolean result = false;
		if (tablename == null) {
			return;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tablename.trim()
					+ "' ";
			cursor = db.rawQuery(sql, null);
			boolean flag = cursor.moveToNext();
			if (flag) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (!result) {
			TableUtils.createTable(db, true, clszz);
		}
	}

	/** 根据注解获取表名 */
	public String getTableName() {
		String tableName = null;
		Table myAnnotation = clszz.getAnnotation(Table.class);
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
