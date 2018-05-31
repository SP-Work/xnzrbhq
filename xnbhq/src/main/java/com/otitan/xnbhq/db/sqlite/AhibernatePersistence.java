package com.otitan.xnbhq.db.sqlite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 数据库名称
 */
public class AhibernatePersistence
{
  public static String getEntityName(Class<?> clazz)
  {
    Annotation entityAnnotation = null;
    for (Annotation annotation : clazz.getAnnotations()) {
      Class annotationClass = annotation.annotationType();
      if (annotationClass.getName().equals("javax.persistence.Entity")) {
        entityAnnotation = annotation;
      }
    }

    if (entityAnnotation == null)
      return null;
    try
    {
      Method method = entityAnnotation.getClass().getMethod("name", new Class[0]);
      String name = (String)method.invoke(entityAnnotation, new Object[0]);
      if ((name != null) && (name.length() > 0)) {
        return name;
      }
      return null;
    }
    catch (Exception e) {
      throw new IllegalStateException("Could not get entity name from class " + clazz, e);
    }
  }
}