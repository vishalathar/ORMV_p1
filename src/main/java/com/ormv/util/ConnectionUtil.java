package com.ormv.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil {
	
	private static Connection conn = null;
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	
	private ConnectionUtil() {
		super();
	}
	
public static Connection getConnection() {
	
		try {
			if (conn != null && !conn.isClosed()) {
				logger.info("returned re-used connection object");
				
				return conn;
			}
		} catch (SQLException e) {
			logger.error("we failed to re-use a connection");
			e.toString();
			return null;
		}
		
		Properties prop = new Properties(); 
		String url = "";
		String username = "";
		String password = "";
		
		try {
			
			prop.load(new FileReader("C:\\Users\\athar\\OneDrive\\Documents\\Revature\\Project 0\\project-0-vishalathar\\src\\main\\resources\\application.properties"));
			
			url = prop.getProperty("url"); 
			username =  prop.getProperty("username");
			password = prop.getProperty("password");
			
			
			conn = DriverManager.getConnection(url, username, password);
			logger.info("Database Connection Established");
			
		} catch (SQLException e) {
			logger.error("SQL Exception thrown - Cannot establish DB connection");
			e.toString();
		} catch (FileNotFoundException e) {
			logger.error("Cannot locate application.properties file");
			e.toString();
		} catch (IOException e) {
			logger.error("Something wrong with app.props file");
			e.toString();
		}
		
		return conn; 
	}
}
