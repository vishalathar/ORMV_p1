package com.ormv.customexceptions;

public class ClassNotAnnotatedWithEntity extends RuntimeException {

	public ClassNotAnnotatedWithEntity(String message) {
		super(message);
	}
}
