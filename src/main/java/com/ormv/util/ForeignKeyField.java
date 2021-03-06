package com.ormv.util;

import java.lang.reflect.Field;

import com.ormv.annotations.Column;
import com.ormv.annotations.JoinColumn;
import com.ormv.customexceptions.ColumnFieldNotAnnotatedWithJoinColumn;
import com.ormv.customexceptions.CustomException;

public class ForeignKeyField {

private Field field; // from java.lang.reflect
	
	public ForeignKeyField(Field field) {
		
		if(field.getAnnotation(JoinColumn.class) == null) {
			throw new ColumnFieldNotAnnotatedWithJoinColumn("Cannot create ColumnField object! Provided field, "
					+ getName() + "is not annotated with @JoinColumn");
		}
		this.field = field;
	}
	
	public String getName() {
		return field.getName();
	}
	
	public Class<?> getType() {
		return field.getType();
	}
	
	
	public String getColumnName() {
		return field.getAnnotation(JoinColumn.class).columnName();
	}
	
	public String getCheck() {
		return field.getAnnotation(JoinColumn.class).check();
	}
	
	public boolean isUnique() {
		return field.getAnnotation(JoinColumn.class).unique();
	}
	
	public boolean isNullable() {
		return field.getAnnotation(JoinColumn.class).nullable();
	}


	public Object getValue(Object obj) {
		try {
			setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new CustomException(e.toString());
		}
	}
	
	public void setValue(Object obj, Object fieldVal) {
		try {
			setAccessible(true);
			field.set(obj, fieldVal);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new CustomException(e.toString());
		}
	}
	
	public void setAccessible(boolean b) {
		field.setAccessible(b);
	}
}
