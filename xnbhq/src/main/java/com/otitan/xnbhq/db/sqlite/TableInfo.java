package com.otitan.xnbhq.db.sqlite;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 表格数据信息实体类
 */
public class TableInfo
{
  private Object target;
  private String tableName;
  private Field idField;
  private Map<String, Field> fieldNameMap;

  public String getTableName()
  {
    return this.tableName;
  }

  public Field getIdField() {
    return this.idField;
  }

  public Object getTarget() {
    return this.target;
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  public void setIdField(Field idField) {
    this.idField = idField;
  }

  public Map<String, Field> getFieldNameMap() {
    return this.fieldNameMap;
  }

  public void setFieldNameMap(Map<String, Field> fieldNameMap) {
    this.fieldNameMap = fieldNameMap;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}