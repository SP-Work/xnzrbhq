package com.otitan.xnbhq.db.sqlite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库操作类
 */
public class Operate {
	private String tableName;

	public Operate(Class<?> clazz) {
		this.tableName = extractTableName(clazz);
	}

	public String extractTableName(Class<?> clazz) {
		Table table = (Table) clazz.getAnnotation(Table.class);
		String name = null;
		if ((table != null) && (table.name() != null)
				&& (table.name().length() > 0)) {
			name = table.name();
		} else {
			name = AhibernatePersistence.getEntityName(clazz);
			if (name == null) {
				name = clazz.getSimpleName().toLowerCase();
			}
		}
		return name;
	}

	public String getTableName() {
		return this.tableName;
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);

		return isNum.matches();
	}

	public String buildSelectSql(String tableName, Map<String, String> where) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT * FROM ");
		sb.append(tableName);
		Iterator<?> iter = null;
		if (where != null) {
			sb.append(" WHERE ");
			iter = where.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry e = (Map.Entry) iter.next();
				sb.append(e.getKey()).append(" = ")
						.append("'" + e.getValue() + "'");
				if (iter.hasNext()) {
					sb.append(" AND ");
				}
			}
		}
		String str = sb.toString();////SELECT * FROM station WHERE name like '%金钱%'
		if(str.contains("like") && str.contains("%")){//SELECT * FROM station WHERE name like = '%金钱%'
			String sql = sb.toString().replace(" = ", " ");
			return sql;
		}
		return sb.toString();
	}

	public String buildInsertSql(String tableName,
								 Map<String, String> insertColumns) {
		StringBuilder columns = new StringBuilder(256);
		StringBuilder values = new StringBuilder(256);
		columns.append("INSERT INTO ");

		columns.append(tableName).append(" (");
		values.append("(");

		Iterator<?> iter = insertColumns.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			Object obj = e.getKey();
			columns.append(e.getKey());

			if(obj.equals("id")){
				values.append("null");
			}else{
				values.append("'" + e.getValue() + "'");
			}
			if (iter.hasNext()) {
				columns.append(", ");
				values.append(", ");
			}
		}
		columns.append(") values ");
		values.append(")");
		columns.append(values);
		return columns.toString();
	}

	public String buildUpdateSql(String tableName,
								 Map<String, String> needUpdate, Map<String, String> where) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("UPDATE ");
		sb.append(tableName).append(" SET ");

		Iterator<?> iter = needUpdate.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			sb.append(e.getKey()).append(" = ")
					.append("'" + e.getValue() + "'");
			if (iter.hasNext()) {
				sb.append(", ");
			}
		}
		if (where != null) {
			sb.append(" where ");
			iter = where.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry e = (Map.Entry) iter.next();
				sb.append(e.getKey()).append(" = ")
						.append("'" + e.getValue() + "'");
				if (iter.hasNext()) {
					sb.append(" and ");
				}
			}
		}
		return sb.toString();
	}

	public String buildDeleteSql(String tableName, Map<String, String> where) {
		StringBuffer buf = new StringBuffer(tableName.length() + 10);
		buf.append("DELETE FROM ").append(tableName);
		if (where != null) {
			buf.append(" WHERE ");
			Iterator<?> iter = where.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry e = (Map.Entry) iter.next();
				buf.append(e.getKey()).append(" = ")
						.append("'" + e.getValue() + "'");
				if (iter.hasNext()) {
					buf.append(" AND ");
				}
			}
		}
		return buf.toString();
	}

	public Map<String, String> buildWhere(Object entity) throws Exception {
		Map<String, String> where = new HashMap<String, String>();
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Annotation[] fieldAnnotations = (Annotation[]) null;
		for (Field field : fields) {
			field.setAccessible(true);
			fieldAnnotations = field.getAnnotations();
			if (fieldAnnotations.length != 0) {
				for (Annotation annotation : fieldAnnotations) {
					String columnName = null;
					if (annotation instanceof Id)
						columnName = ((Id) annotation).name();
					else if (annotation instanceof Column)
						columnName = ((Column) annotation).name();
					else if (annotation instanceof OneToMany) {
						continue;
					}
					try {
						if ((field.get(entity) != null)
								&& (field.get(entity).toString().length() > 0))
							where.put(
									((columnName != null) && (!columnName
											.equals(""))) ? columnName : field
											.getName(), field.get(entity)
											.toString());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (where.isEmpty()) {
			throw new Exception("can't delete,entity is illegal");
		}
		return where;
	}
}