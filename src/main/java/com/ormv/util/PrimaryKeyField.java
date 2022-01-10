package com.ormv.util;

import java.lang.reflect.Field;

import com.ormv.annotations.Column;
import com.ormv.annotations.Id;
import com.ormv.customexceptions.ColumnFieldNotAnnotatedWithId;
import com.ormv.customexceptions.CustomException;

public class PrimaryKeyField {

private Field field; // from java.lang.reflect
	
	public PrimaryKeyField(Field field) {
		
		if(field.getAnnotation(Id.class) == null) {
			throw new ColumnFieldNotAnnotatedWithId("Cannot create ColumnField object! Provided field, "
					+ getName() + "is not annotated with @Id");
		}
		this.field = field;
	}
	
	public String getName() {
		return field.getName();
	}
	
	// return the TYPE of the field that's annotated
	public Class<?> getType() {
		return field.getType();
	}
	
	// get columnName() -=- extract the column name attribute from the column annotation
	public String getColumnName() {
		return field.getAnnotation(Id.class).columnName();
	}
	
	public String getCheck() {
		return field.getAnnotation(Id.class).check();
	}
	
	public boolean isUnique() {
		return field.getAnnotation(Id.class).unique();
	}
	
	public boolean isNullable() {
		return field.getAnnotation(Id.class).nullable();
	}
	
	public String getStrategy() {
		return field.getAnnotation(Id.class).strategy();
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
