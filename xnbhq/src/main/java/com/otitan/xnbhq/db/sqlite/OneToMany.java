package com.otitan.xnbhq.db.sqlite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表格数据注解类
 */
@Target({ java.lang.annotation.ElementType.METHOD,
		java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	public abstract Class targetEntity();

	public abstract String mappedBy();
}