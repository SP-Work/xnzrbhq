package com.otitan.xnbhq.db.sqlite;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 表格工具类
 */
@SuppressLint("DefaultLocale")
public class TableUtils {
	public static boolean DEBUG = false;

	private static String DEFAULT_FOREIGN_KEY_SUFFIX = "_id";

	public static TableInfo extractTableInfo(Class<?> clazz) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableName(extractTableName(clazz));
		tableInfo.setIdField(extractIdField(clazz));
		Field[] fields = clazz.getDeclaredFields();
		Map<String, Field> fieldNameMap = new HashMap<String, Field>();
		Annotation[] fieldAnnotations = (Annotation[]) null;
		for (Field field : fields) {
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

					fieldNameMap
							.put(((columnName != null) && (!columnName
									.equals(""))) ? columnName : field
									.getName(), field);
				}
			}
		}
		tableInfo.setFieldNameMap(fieldNameMap);
		return tableInfo;
	}

	public static String buildDropTableStatement(TableInfo tableInfo) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("DROP TABLE ");
		sb.append("IF EXISTS ");
		sb.append(tableInfo.getTableName());
		return sb.toString();
	}

	public static Object getFieldValue(String filedName, Object obj)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		Field field = obj.getClass().getDeclaredField(filedName);
		field.setAccessible(true);
		return field.get(obj);
	}

	public static String buildQueryStatements(String tableName,
			String fieldName, String fieldValue) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("SELECT * FROM ");
		sb.append(tableName);
		sb.append(" WHERE ");
		sb.append(fieldName);
		sb.append("=");
		sb.append(fieldValue);
		return sb.toString();
	}

	public static String buildInsertTableStatements(TableInfo tableInfo) {
		StringBuilder sb = new StringBuilder(256);
		StringBuilder sbValues = new StringBuilder(256);
		sb.append("INSERT INTO ");
		sb.append(tableInfo.getTableName());
		sb.append(" (");
		sbValues.append(" (");
		Boolean isFirst = Boolean.valueOf(true);

		for (Map.Entry entry : tableInfo.getFieldNameMap().entrySet()) {
			if (isFirst.booleanValue()) {
				isFirst = Boolean.valueOf(false);
			} else {
				sb.append(", ");
				sbValues.append(", ");
			}
			sb.append(entry.getKey());
			sbValues.append("?");
		}
		sb.append(")");
		sbValues.append(")");
		sb.append(" values");
		sb.append(sbValues);
		return sb.toString();
	}

	public static String buildCreateTableStatement(TableInfo tableInfo,
			boolean ifNotExists) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("CREATE TABLE ");

		if (ifNotExists) {
			sb.append("IF NOT EXISTS ");
		}

		sb.append(tableInfo.getTableName());
		sb.append(" (");

		Iterator<?> iter = null;
		iter = tableInfo.getFieldNameMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry e = (Map.Entry) iter.next();
			Field f = (Field) e.getValue();
			if (f.getType().getSimpleName().equals("Long")) {
				sb.append(e.getKey() + " INTEGER");
			}

			if (f.getType().getSimpleName().equals("String")) {
				sb.append(e.getKey() + " TEXT");
			}
			Field idFiled = tableInfo.getIdField();

			if ((idFiled != null) && (idFiled.getName().equals(f.getName()))) {
				sb.append(" PRIMARY KEY");
			}

			if (iter.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	@SuppressLint("DefaultLocale")
	public static String extractTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
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

	public static Field extractIdField(Class<?> clazz) {
		Field idField = null;
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getAnnotations().length != 0) {
				field.setAccessible(true);
				for (Annotation annotation : field.getAnnotations()) {
					Class<?> annotationClass = annotation.annotationType();
					String name = annotationClass.getName();
					String id = Id.class.getName();
					if (!name.equals(id))
						continue;
					idField = field;
				}
			}
		}

		return idField;
	}

	public static int createTable(SQLiteDatabase db, boolean ifNotExists,
			Class<?> cls) {
		int i = -1;
		TableInfo tableInfo = extractTableInfo(cls);
		String sql = buildCreateTableStatement(tableInfo, ifNotExists);
		if (!DEBUG) {
			db.execSQL(sql);
		}
		i = 1;
		return i;
	}

	public static int dropTable(SQLiteDatabase db, Class<?> entityClasses) {
		int i = -1;
		TableInfo tableInfo = extractTableInfo(entityClasses);
		String sql = buildDropTableStatement(tableInfo);
		System.out.println(sql);
		if (!DEBUG) {
			db.execSQL(sql);
		}
		i = 1;
		return i;
	}
}