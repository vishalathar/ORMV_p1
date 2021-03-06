package com.ormv.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ormv.dao.EntityDAO;

/**
 * The purpose of this class is to have the User only provide a few
 * things in order for the ORM to establish a connection and build the tables
 * based on a list of User-Defined classes that the user passes to the ORM to
 * iuntrospect and construct in the DB 
 * @param <T>
 *
 */
public class Configuration<T> {
	
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	private EntityDAO<T> edao = new EntityDAO<>(this);

	// this is the list of classes that the user wants our ORM to "scan" aka introspect and build 
	// as DB objects
	private List<MetaModel<Class<?>>> metaModelList;
	
	// This method doesn't technically follow SRP Single Responsibility Principle
	public Configuration<T> addAnnotatedClass(Class<?> annotatedClass) {

		
		// first check if a linked List has been instantiated...
		// if not, instantiate it!
		if (metaModelList == null) {
			metaModelList = new LinkedList<>();
		}
		
		// we need to call the method that transforms a class into an appropriate
		// data model that our ORM can introspect (a MetaModel)
		metaModelList.add(MetaModel.of(annotatedClass));
		
		return this; // returns the configuration object on which the addAnnotatedClass() method is being called
	}
	
	public List<MetaModel<Class<?>>> getMetaModels() {
		
		// in the case that the metaModelList of the Configuration object is empty, return an empty list.
		// otherwise, reutrn the metaModelList.
		return (metaModelList == null) ? Collections.emptyList() : metaModelList;
	}
	
	public void printMetaModels() {
		for (MetaModel<?> metamodel : metaModelList){
			
			System.out.printf("Printing MetaModel for class: %s\n", metamodel.getClassName());
			if(metamodel.getPrimaryKeyField() != null) {
				System.out.printf("\t Found a primary key field named %s, of type %s, which maps to the column with name: %s\n", 
						metamodel.getPrimaryKeyField().getName(), metamodel.getPrimaryKeyField().getType(), metamodel.getPrimaryKeyField().getColumnName());
			}
			
			if(metamodel.getColumnFields() != null) {
				for (ColumnField column : metamodel.getColumnFields()) {
					System.out.printf("\t Found a column field named %s, of type %s, which maps to the column with name: %s\n", 
							column.getName(), column.getType(), column.getColumnName());
				}
			}
			
			if(metamodel.getForeignKeyFields() != null)
			{
				for (ForeignKeyField foreignKey : metamodel.getForeignKeyFields()) {
					System.out.printf("\t Found a foreign key field named %s, of type %s, which maps to the column with name: %s\n", 
							foreignKey.getName(), foreignKey.getType(), foreignKey.getColumnName());
				}	
			}
		}
		
	}
	
	// return a Connection object OR call on a separate class like Connection Util
	public static Connection getConnection() {
		
		Connection conn = null;
		DataSource dataSource = null;
		try {
			dataSource = ConnectionUtil.setUpPool();
		
			ConnectionUtil.printDbStatus();
		
		// Performing a Database Operation!
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//se.printStackTrace();
			logger.warn(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.warn(e);
		}
		ConnectionUtil.printDbStatus();
		
		return conn; 
	}
	
	public void showReflectionMagic() {
		
			edao.createTableQuery(metaModelList);

		
	}

}