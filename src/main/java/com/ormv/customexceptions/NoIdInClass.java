package com.ormv.customexceptions;

public class NoIdInClass extends RuntimeException{
	
	public NoIdInClass(String message) {
		super(message);
	}
}
