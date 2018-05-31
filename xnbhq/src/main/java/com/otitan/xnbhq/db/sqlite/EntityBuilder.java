package com.otitan.xnbhq.db.sqlite;

import android.database.Cursor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建实体
 * @param <T>
 */
public class EntityBuilder<T>
{
  private Class<?> clazz;
  private Cursor cursor;

  public EntityBuilder(Class<?> clazz, Cursor cursor)
  {
    this.clazz = clazz;
    this.cursor = cursor;
  }

  public List<Object> buildQueryList() {
    List<Object> queryList = new ArrayList<Object>();
    Field[] fields = this.clazz.getDeclaredFields();
    if (this.cursor.moveToFirst()) {
      for (int i = 0; i < this.cursor.getCount(); ++i) {
        this.cursor.moveToPosition(i);
        try {
          Object t = this.clazz.newInstance();
          Annotation[] fieldAnnotations = (Annotation[])null;
          for (Field field : fields) {
            field.setAccessible(true);
            fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations.length != 0) {
              for (Annotation annotation : fieldAnnotations) {
                String columnName = null;
                if (annotation instanceof Id)
                  columnName = ((Id)annotation).name();
                else if (annotation instanceof Column)
                  columnName = ((Column)annotation).name();
                else if (annotation instanceof OneToMany) {
                    continue;
                  }

                if (field.getType().getSimpleName().equals("Long"))
                  field.set(
                    t, 
                    Long.valueOf(this.cursor.getLong(this.cursor
                    .getColumnIndexOrThrow(((columnName != null) && 
                    (!columnName .equals(""))) ? 
                    columnName : field
                    .getName()))));
                else if (field.getType().getSimpleName().equals("String")) {
                  field.set(
                    t, 
                    this.cursor.getString(this.cursor
                    .getColumnIndexOrThrow(((columnName != null) && 
                    (!columnName
                    .equals(""))) ? 
                    columnName : field
                    .getName())));
                }
              }
            }
          }

          queryList.add(t);
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }

    return queryList;
  }
}