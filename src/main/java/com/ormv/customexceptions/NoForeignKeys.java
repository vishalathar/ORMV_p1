package com.ormv.customexceptions;

public class NoForeignKeys extends RuntimeException {

	public NoForeignKeys(String message) {
		super(message);
	}
}
