package com.ormv.customexceptions;

public class ColumnFieldNotAnnotatedWithJoinColumn extends RuntimeException {

	public ColumnFieldNotAnnotatedWithJoinColumn(String message) {
		super(message);
	}
}
