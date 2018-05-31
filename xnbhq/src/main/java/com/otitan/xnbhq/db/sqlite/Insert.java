package com.otitan.xnbhq.db.sqlite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据添加
 */
public class Insert extends Operate {
	private Object entity;

	public Insert(Object entity) {
		super(entity.getClass());
		this.entity = entity;
	}

	public Map<String, String> getInsertColumns() {
		Map<String, String> insertColumns = new HashMap<String, String>();
		Class<?> clazz = this.entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Annotation[] fieldAnnotations = (Annotation[]) null;
		for (Field field : fields) {
			fieldAnnotations = field.getAnnotations();
			if (fieldAnnotations.length != 0) {
				for (Annotation annotation : fieldAnnotations) {
					String columnName = null;
					if ((annotation instanceof Id)
							&& (!((Id) annotation).autoGenerate())) {
						columnName = ((Id) annotation).name();
					} else if (annotation instanceof Column)
						columnName = ((Column) annotation).name();
					else if (annotation instanceof OneToMany) {
						continue;
					}

					field.setAccessible(true);
					try {
						insertColumns.put(((columnName != null) && (!columnName
								.equals(""))) ? columnName : field.getName(),
								(field.get(this.entity) == null) ? null : field
										.get(this.entity).toString());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return insertColumns;
	}

	public String toStatementString() {
		return buildInsertSql(getTableName(), getInsertColumns());
	}
}
