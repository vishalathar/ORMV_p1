package com.ormv.customexceptions;

public class NoColumnsInClass extends RuntimeException {
	
	public NoColumnsInClass(String message) {
		super(message);
	}
}
