package com.ormv.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinColumn {
	
	String columnName();
	String check() default "none";
	boolean unique() default false;
	boolean nullable() default true;
	// TODO: CASCADE TYPE in future versions

}