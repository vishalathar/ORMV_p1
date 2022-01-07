package com.ormv.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
	String columnName();
	String check() default "none";
	boolean unique() default true;
	boolean nullable() default false;
	String strategy() default "";
}