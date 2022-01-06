package com.ormv.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class ConnectionUtil {

	//private static Connection conn = null;
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);

	// connection pool
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static GenericObjectPool gPool = null;

	private ConnectionUtil() {
		super();
	}

	public static DataSource setUpPool() throws Exception {

		Class.forName(JDBC_DRIVER);

		// Create an instance of GenericObjectPoo that holds our Pool of Connection
		// Objects
		gPool = new GenericObjectPool();
		gPool.setMaxActive(5);

		Properties prop = new Properties();
		String url = "";
		String username = "";
		String password = "";

		prop = getPropsFromFile();

		url = prop.getProperty("url");
		username = prop.getProperty("username");
		password = prop.getProperty("password");

		// Create a ConnectionFactory Object which will be used by the pool to create
		// the connection object
		ConnectionFactory cf = new DriverManagerConnectionFactory(url, username, password);

		// Create a PoolableConnectionFactory that will wrap around the Connection
		// Object created by the above connectionFactory
		// in order to add pooling functionality.
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		return new PoolingDataSource(gPool);
	}

//	public static Connection getConnection() {
//	
//		try {
//			if (conn != null && !conn.isClosed()) {
//				logger.info("returned re-used connection object");
//				
//				return conn;
//			}
//		} catch (SQLException e) {
//			logger.error("we failed to re-use a connection");
//			e.toString();
//			return null;
//		}
//		
//		Properties prop = new Properties(); 
//		String url = "";
//		String username = "";
//		String password = "";
//		
//			prop = getPropsFromFile();
//			
//			url = prop.getProperty("url"); 
//			username =  prop.getProperty("username");
//			password = prop.getProperty("password");
//			try {
//				conn = DriverManager.getConnection(url, username, password);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				//e.printStackTrace();
//				logger.warn(e);
//			}
//			logger.info("Database Connection Established");
//			
//		
//		
//		return conn; 
//	}

	private static Properties getPropsFromFile() {
		Properties props = new Properties();
		String appDir = new File("").getAbsolutePath();
		String fileName = "ormv.cfg.properties";
		java.nio.file.Path path = java.nio.file.Paths.get(appDir, "src", "main", "resources", fileName);
		boolean fileExists = java.nio.file.Files.exists(path);
		if (!fileExists) {
			return props;
		}
		String pathString = path.toString();
		try (FileReader file = new FileReader(pathString)) {
			props.load(file);
			return props;
		} catch (IOException e) {
			logger.error(e);
		}
		return props;
	}

	public static GenericObjectPool getConnectionPool() {
		return gPool;
	}

	// This will be a method used to print the connection pool status
	public static void printDbStatus() {
		System.out.println("Max: " + getConnectionPool().getMaxActive() + "; Active: "
				+ getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
	}

}
