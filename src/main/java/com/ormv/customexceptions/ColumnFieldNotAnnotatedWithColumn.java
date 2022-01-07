package com.ormv.customexceptions;

public class ColumnFieldNotAnnotatedWithColumn extends RuntimeException{
	
	public ColumnFieldNotAnnotatedWithColumn(String message) {
		super(message);
	}

}