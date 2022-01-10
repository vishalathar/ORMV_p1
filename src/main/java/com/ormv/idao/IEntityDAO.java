package com.ormv.idao;

import java.util.List;

import com.ormv.util.MetaModel;

public interface IEntityDAO {

	public void createTableJDBC(String query);
	
	public <T> void createTableQuery(List<MetaModel<T>> models);
	
	public void DELETE_FROM_JDBC_BY_ID(String query, Object id);
	
	public void DELETE_ALL_JDBC_BY_CLASS(String query);
	
	public Object GET_AN_OBJECT_BY_ID(String query, Object Obj);
	
	public Object INSERT_INTO_AN_OBJECT_JDBC(String query, Object Obj);
	
	public void UPDATE_AN_OBJECT_BY_OBJECT_JDBC(String query, Object Obj);
	
	
	// *** PERSISTENCE Methods ***
	// saves the object in db 
	// return pk
	public Object save(Object Obj);
	
	// saves the object in db if not present, if present update it
	// returns pk
	// TODO: public int saveOrUpdate(Class<?> Object);
	
	// updates the object in db by pk
	// 
	public void update(Object Obj);
	
	// *** DELETION Method ***
	
	// delete by id
	public void delete(Object Obj);
	
	// delete all records
	public void truncate(Class<?> clazz);
	
	
	
	
	// *** READING Methods ***
	
	// return the object by pk
	public Object get(Class<?> clazz, Object id);
	
	// return list
	public List<Class<?>> getAll(Class<?> clazz);

}