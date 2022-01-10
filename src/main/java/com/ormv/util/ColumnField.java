package com.ormv.util;

import java.lang.reflect.Field;

import com.ormv.annotations.Column;
import com.ormv.customexceptions.ColumnFieldNotAnnotatedWithColumn;
import com.ormv.customexceptions.CustomException;

public class ColumnField {

	private Field field; // from java.lang.reflect

	public ColumnField(Field field) {
		
		
		if (field.getAnnotation(Column.class) == null) {
			throw new ColumnFieldNotAnnotatedWithColumn("Cannot create ColumnField object! Provided field, "
					+ getName() + "is not annotated with @Column");
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

	// get columnName() -=- extract the column name attribute from the column
	// annotation
	public String getColumnName() {
		return field.getAnnotation(Column.class).columnName();
	}
	
	public String getCheck() {
		return field.getAnnotation(Column.class).check();
	}
	
	public boolean isUnique() {
		return field.getAnnotation(Column.class).unique();
	}
	
	public boolean isNullable() {
		return field.getAnnotation(Column.class).nullable();
	}
	

	public Object getValue(Object obj) {
		try {
			setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
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
