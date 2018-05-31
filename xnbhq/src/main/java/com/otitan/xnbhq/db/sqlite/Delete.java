package com.otitan.xnbhq.db.sqlite;

import java.util.Map;

/**
 * 数据删除
 */
public class Delete extends Operate {
	private Object entity;
	private Map<String, String> where;

	public Delete(Object entity) {
		super(entity.getClass());
		this.entity = entity;
		try {
			this.where = buildWhere(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Delete(Class clazz, Map<String, String> where) {
		super(clazz);
		this.where = where;
	}

	public String toStatementString() {
		return buildDeleteSql(getTableName(), this.where);
	}
}
