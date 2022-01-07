package com.ormv.customexceptions;

public class ColumnFieldNotAnnotatedWithId extends RuntimeException {
	
	public ColumnFieldNotAnnotatedWithId(String message) {
		super(message);
	}
}
