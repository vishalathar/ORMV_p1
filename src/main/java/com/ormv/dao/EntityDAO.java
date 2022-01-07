package com.ormv.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ormv.util.ColumnField;
import com.ormv.util.Configuration;
import com.ormv.util.ConnectionUtil;
import com.ormv.util.MetaModel;

public class EntityDAO<T> {
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	public void createTableJDBC(String query) {
		
		Connection connection = null;
		Statement statement = null;
		
		
		try {
			connection = Configuration.getConnection();
			statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.warn(e);
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					logger.warn(e);
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					logger.warn(e);
				}
			}
		}
		
		
	}
	
	/*
	 * 
		DROP TABLE IF EXISTS vishal.users CASCADE;
		CREATE TABLE vishal.users (
			
		 	user_id SERIAL PRIMARY KEY,
		 	username VARCHAR(50) NOT NULL UNIQUE,
		 	first_name VARCHAR(50) NOT NULL,
		 	last_name VARCHAR(50) NOT NULL,
		 	user_pass VARCHAR(50) NOT NULL,
		 	user_role vishal.role NOT NULL,
		 	ssn INTEGER NOT NULL,
		 	address VARCHAR(50) NOT NULL,
		 	city VARCHAR(50) NOT NULL,
		 	country VARCHAR(50) NOT NULL,
		 	postal_code VARCHAR(50) NOT NULL
		 	
		);

	 * */
	
	public String createTableQuery(MetaModel<T> model) {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS ");
		sb.append(model.getSimpleClassName().toLowerCase());
		sb.append(" CASCADE;");
		sb.append("CREATE TABLE ");
		sb.append(model.getSimpleClassName().toLowerCase());
		sb.append(" (");
		// for primary key
		if(model.getPrimaryKeyField() != null) {
			sb.append(model.getPrimaryKeyField().getName());
			sb.append(" ");
			if(model.getPrimaryKeyField().getStrategy() == "GenerationType.IDENTITY") {
				sb.append("SERIAL ");
			}
			else {
				if(model.getPrimaryKeyField().getType().getSimpleName().equals("int")) {
					sb.append("INTEGER ");
				}
				else if(model.getPrimaryKeyField().getType().getSimpleName().equals("String")) {
					sb.append("VARCHAR(255) ");
				}
			}
			if(!model.getPrimaryKeyField().getCheck().equals("none")) {
				sb.append("CHECK (" + model.getPrimaryKeyField().getCheck() + ") ");
			}
			sb.append("PRIMARY KEY,");
		}
		if(model.getColumnFields() != null) {
			for(ColumnField cf : model.getColumnFields()) {
				sb.append(cf.getColumnName());
				sb.append(" ");
				
				// type
				if(cf.getType().getSimpleName().equals("int")) {
					sb.append("INTEGER ");
				}
				else if(cf.getType().getSimpleName().equals("String")) {
					sb.append("VARCHAR(255) ");
				}
				// constraints
				String s = cf.isNullable() ? "" : "NOT NULL ";
				sb.append(s);
				
				s = cf.isUnique() ? "UNIQUE " : "";
				sb.append(s);
				
				if(!cf.getCheck().equals("none")) {
					sb.append("CHECK (" + cf.getCheck() + ") ");
				}
				
				sb.append(",");
				
			}
		}
		
		System.out.println(sb.substring(0,sb.toString().length()-1) + ");");
		return sb.substring(0,sb.toString().length()-1) + ");";
	}

}
