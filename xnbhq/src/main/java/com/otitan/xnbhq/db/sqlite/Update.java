package com.otitan.xnbhq.db.sqlite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据更新
 */
public class Update extends Operate {
	private Object entity;
	private Map<String, String> where;

	public Update(Object entity) {
		super(entity.getClass());
		this.entity = entity;
		try {
			this.where = getDefaultWhereField();
		} catch (Exception e) {
			this.where = null;
			e.printStackTrace();
		}
	}

	public Update(Object entity, Map<String, String> where) {
		super(entity.getClass());
		this.entity = entity;
		this.where = where;
	}

	public Map<String, String> getDefaultWhereField()
			throws IllegalArgumentException, IllegalAccessException {
		Map defaultWhereField = new HashMap();
		Class clazz = this.entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Annotation[] fieldAnnotations = (Annotation[]) null;
		for (Field field : fields) {
			fieldAnnotations = field.getAnnotations();
			if (fieldAnnotations.length != 0) {
				for (Annotation annotation : fieldAnnotations) {
					String columnName = null;
					if (!(annotation instanceof Id))
						continue;
					columnName = ((Id) annotation).name();
					field.setAccessible(true);
					defaultWhereField.put(((columnName != null) && (!columnName
							.equals(""))) ? columnName : field.getName(),
							(field.get(this.entity) == null) ? null : field
									.get(this.entity).toString());
				}
			}

		}

		return defaultWhereField;
	}

	public Map<String, String> getUpdateFields() {
		Map updateFields = new HashMap();
		Class clazz = this.entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Annotation[] fieldAnnotations = (Annotation[]) null;
		for (Field field : fields) {
			fieldAnnotations = field.getAnnotations();
			if (fieldAnnotations.length != 0)
				for (Annotation annotation : fieldAnnotations) {
					String columnName = null;
					if ((annotation instanceof Id)
							&& (!((Id) annotation).autoGenerate())) {
						columnName = ((Id) annotation).name();
					} else {
						if (annotation instanceof Column)
							columnName = ((Column) annotation).name();
						else if (annotation instanceof OneToMany) {
							continue;
						}

						field.setAccessible(true);
						try {
							updateFields
									.put(((columnName != null) && (!columnName
											.equals(""))) ? columnName : field
											.getName(),
											(field.get(this.entity) == null) ? null
													: field.get(this.entity)
															.toString());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
		}
		return updateFields;
	}

	public String toStatementString() {
		return buildUpdateSql(getTableName(), getUpdateFields(), this.where);
	}

	public Map<String, String> getWhereFiled() {
		return this.where;
	}
}