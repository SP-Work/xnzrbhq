package com.otitan.xnbhq.db.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作类
 * @param <T>
 */
public class AhibernateDao<T> {
	private static String EMPTY_SQL = "DELETE FROM ";
	private SQLiteDatabase db;
	private String TAG = "AhibernateDao";

	public AhibernateDao(SQLiteDatabase db) {
		this.db = db;
	}

	public boolean insert(T entity) {
		String sql = new Insert(entity).toStatementString();
		Log.d(this.TAG, "insert sql:" + sql);
		try {
			this.db.execSQL(sql);
			return true;
		} catch (SQLException e) {
			Log.e(this.TAG, "inserting to database failed: " + sql, e);
			return false;
		} finally {
//			if (db.isOpen())
//				db.close();
		}
	}

	public List<T> queryList(Class<?> clazz, Map<String, String> where) {
		String sql = new Select(clazz, where).toStatementString();
		Log.d(this.TAG, "query sql:" + sql);
		Cursor cursor = this.db.rawQuery(sql, null);
		EntityBuilder<T> builder = new EntityBuilder<T>(clazz, cursor);
		List<T> queryList = (List<T>) builder.buildQueryList();
		cursor.close();
		return queryList;
	}

	public List<T> queryList(T entity) {
		String sql = new Select(entity).toStatementString();
		Log.d(this.TAG, "query sql:" + sql);
		Cursor cursor = this.db.rawQuery(sql, null);
		EntityBuilder<T> builder = new EntityBuilder(entity.getClass(), cursor);
		List<T> queryList = (List<T>) builder.buildQueryList();
		cursor.close();
		return queryList;
	}

	public boolean update(T entity, Map<String, String> where) {
		String sql = new Update(entity, where).toStatementString();
		Log.d(this.TAG, "update sql:" + sql);
		SQLiteStatement stmt = null;
		try {
			stmt = this.db.compileStatement(sql);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Log.e(this.TAG, e.getMessage() + " sql:" + sql);
			return false;
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public boolean delete(Class<?> clazz, Map<String, String> where) {
		String sql = null;
		if (where == null)
			sql = new Delete(clazz).toStatementString();
		else {
			sql = new Delete(clazz, where).toStatementString();
		}
		Log.d(this.TAG, "delete sql:" + sql);
		SQLiteStatement stmt = null;
		try {
			stmt = this.db.compileStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			Log.e(this.TAG, e.getMessage() + " sql:" + sql);
			return false;
		} finally {
			if (stmt != null)
				stmt.close();
			return true;
		}
	}

	public void truncate(Class<?> clazz) {
		String sql = EMPTY_SQL + TableUtils.extractTableName(clazz);
		Log.d(this.TAG, "truncate sql:" + sql);
		SQLiteStatement stmt = null;
		try {
			stmt = this.db.compileStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			Log.e(this.TAG, e.getMessage() + " sql:" + sql);
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	public void delete(T entity) {
		String sql = null;
		sql = new Delete(entity).toStatementString();
		Log.d(this.TAG, "delete sql:" + sql);
		SQLiteStatement stmt = null;
		try {
			stmt = this.db.compileStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			Log.e(this.TAG, e.getMessage() + " sql:" + sql);
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
}