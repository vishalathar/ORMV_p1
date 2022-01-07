package com.ormv.util;

import java.lang.reflect.Field;

import com.ormv.annotations.Column;
import com.ormv.customexceptions.ColumnFieldNotAnnotatedWithColumn;

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

}
