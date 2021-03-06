package com.ormv.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.ormv.annotations.Column;
import com.ormv.annotations.Entity;
import com.ormv.annotations.Id;
import com.ormv.annotations.JoinColumn;
import com.ormv.customexceptions.ClassNotAnnotatedWithEntity;
import com.ormv.customexceptions.NoColumnsInClass;
import com.ormv.customexceptions.NoForeignKeys;
import com.ormv.customexceptions.NoIdInClass;

/**
 * This class' job is to gather as much information as possible about the class we want 
 * to transpose into a DB Entity (table).
 * 
 * This class's job is to model data about a nother class.
 */
public class MetaModel<T> { // we're inferring that the MetaModel class can only be a metamodel of another class
	
	private Class<?> clazz;
	private PrimaryKeyField primaryKeyField; // we created this "type" in our com.revature.util package
	private List<ColumnField> columnFields = new LinkedList<>();
	private List<ForeignKeyField> foreignKeyFields = new LinkedList<>();
	
	// a method to check and then transpose a normal java class to a MetaModel class (we need 
	// to check for the @Entity annotation
	public static MetaModel<Class<?>> of(Class<?> clazz) {
	
		// we check that the class we're passing through has the @Entity annotation
		
		if (clazz.getAnnotation(Entity.class) == null) {
			throw new ClassNotAnnotatedWithEntity("Cannot create MetaModel object! Provided class "
					+ clazz.getName() + " is not annotated with @Entity");
			
		}
		// if so....reutrn a new MetaModel object of the class passed through 
		return new MetaModel<>(clazz);
	}

	// we only call the constructor when we invoke the MetaModel.of(MyClass.class);
	public MetaModel(Class<?> clazz) {
		this.clazz = clazz;
		// i added this
		this.primaryKeyField = this.setPrimaryKey();
		this.columnFields  = this.setColumns();
		this.foreignKeyFields = this.setForeignKeys();
		
	}
	
	// this method will return all the column fields of a metamodel class
	public List<ColumnField> setColumns() {
		// this method reutrns all the properties of the class that are marked with @Column annotation
		
		Field[] fields = clazz.getDeclaredFields();
		
		// for each field within the above Field[], check if it has the Column annotation
		// if it DOES have the @Column annotation add it to the metamodels's columnFields LinkedList
		for (Field field : fields) {
			
			// The column reference variable will NOT be null, if the field is annotated with @Column
			Column column = field.getAnnotation(Column.class);
			
			if (column != null) {
				// if it is indeed marked with @Column, instantiate a new ColumnField object with its data
				columnFields.add(new ColumnField(field));
			}
		}
		
		// add some extra logic in the case that the class doens't have any column fields
		try {
			if (columnFields.isEmpty()) {
				throw new NoColumnsInClass("No columns found in: " + clazz.getName());
			}
		} catch(NoColumnsInClass e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		
		return columnFields;
	}
	/*
	Field[] fields = Test.class.getDeclaredFields();       
	for (Field field : fields) {       
		if(field.isAnnotationPresent(FieldDescr.class))
		{       
			MyAnnotation myAnnotation = field.getAnnotation(MyAnnotation.class);       
			System.out.println(myAnnoatation.name());       
		}       
	}
	
	*/
	
	
	// As of right now I have a way to extract the primary key of a MetaModel object
	public PrimaryKeyField setPrimaryKey() {
		// capture an array of its fields
		Field[] fields = clazz.getDeclaredFields();
		// check if the Id comes back as NOT null
		try {
			for (Field field : fields) {
				Id primaryKey = field.getAnnotation(Id.class);
				
				// IF primaryKey is NOT null, then generate a PrimaryKeyField object from that field
				if (primaryKey != null) {
					// this will capture the first and (hopefully) only primary key that exists
					return new PrimaryKeyField(field);
				}
			}
			throw new NoIdInClass("Did not find a field annotated with @Id in " + clazz.getName());
		} catch(NoIdInClass e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
		}
	}
	
	// this is almost exactly like the getColumns() method, but we're checking for the @JoinColumn
	public List<ForeignKeyField> setForeignKeys() {
		
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			
			JoinColumn foreignKey = field.getAnnotation(JoinColumn.class);
			
			if (foreignKey != null) {
				foreignKeyFields.add(new ForeignKeyField(field));
			}
		}
		try {
			if (foreignKeyFields == null) {
				throw new NoForeignKeys("No foreign keys found in: " + clazz.getName());
			}
		} catch(NoForeignKeys e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
		}
		
		
		return foreignKeyFields;

	}
	
	
	public String getSimpleClassName() {
		return clazz.getSimpleName();
	}
	
	public String getClassName() {
		return clazz.getName();
	}
	
	public Class<?> getClazz(){
		return this.clazz;
	}

	public PrimaryKeyField getPrimaryKeyField() {
		return primaryKeyField;
	}

	public List<ColumnField> getColumnFields() {
		return columnFields;
	}

	
	public List<ForeignKeyField> getForeignKeyFields() {
		return foreignKeyFields;
	}
	
	

}
