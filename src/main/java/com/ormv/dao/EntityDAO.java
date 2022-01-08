package com.ormv.dao;

import java.lang.ProcessBuilder.Redirect.Type;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import org.apache.log4j.Logger;

import com.ormv.annotations.Entity;
import com.ormv.annotations.Id;
import com.ormv.idao.IEntityDAO;
import com.ormv.util.ColumnField;
import com.ormv.util.Configuration;
import com.ormv.util.ConnectionUtil;
import com.ormv.util.ForeignKeyField;
import com.ormv.util.MetaModel;
import com.ormv.util.PrimaryKeyField;

public class EntityDAO<T> implements IEntityDAO {
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);

	@Override
	public void createTableJDBC(String query) {

		Connection connection = null;
		Statement statement = null;

		try {
			connection = Configuration.getConnection();
			statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					logger.warn(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					logger.warn(e);
				}
			}
		}

	}

	@Override
	public void INSERT_INTO_JDBC(String query) {

		Connection connection = null;
		Statement statement = null;

		try {
			connection = Configuration.getConnection();
			statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					logger.warn(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					logger.warn(e);
				}
			}
		}

	}

	/*
	 * 
	 * DROP TABLE IF EXISTS vishal.users CASCADE; CREATE TABLE vishal.users (
	 * 
	 * user_id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, first_name
	 * VARCHAR(50) NOT NULL, last_name VARCHAR(50) NOT NULL, user_pass VARCHAR(50)
	 * NOT NULL, user_role vishal.role NOT NULL, ssn INTEGER NOT NULL, address
	 * VARCHAR(50) NOT NULL, city VARCHAR(50) NOT NULL, country VARCHAR(50) NOT
	 * NULL, postal_code VARCHAR(50) NOT NULL
	 * 
	 * );
	 * 
	 */

	@Override
	public <T> void createTableQuery(List<MetaModel<T>> models) {
		for (MetaModel<?> model : models) {
			StringBuilder sb = new StringBuilder();

			// getting table name
			Class clazz = model.getClazz();
			System.out.println("model: " + clazz.getName());
			Entity entity = (Entity) clazz.getAnnotation(Entity.class);

			// preparing sql statement
			sb.append("DROP TABLE IF EXISTS ");
			sb.append(entity.tableName().toLowerCase());
			sb.append(" CASCADE;");
			sb.append("CREATE TABLE ");
			sb.append(entity.tableName().toLowerCase());
			sb.append(" (");
			// for primary key
			if (model.getPrimaryKeyField() != null) {
				sb.append(model.getPrimaryKeyField().getColumnName());
				sb.append(" ");
				if (model.getPrimaryKeyField().getStrategy() == "GenerationType.IDENTITY") {
					sb.append("SERIAL ");
				} else {
					if (model.getPrimaryKeyField().getType().getSimpleName().equals("int")) {
						sb.append("INTEGER ");
					} else if (model.getPrimaryKeyField().getType().getSimpleName().equals("String")) {
						sb.append("VARCHAR(255) ");
					}
				}
				if (!model.getPrimaryKeyField().getCheck().equals("none")) {
					sb.append("CHECK (" + model.getPrimaryKeyField().getCheck() + ") ");
				}
				sb.append("PRIMARY KEY,");
			}
			if (model.getColumnFields() != null) {
				for (ColumnField cf : model.getColumnFields()) {
					sb.append(cf.getColumnName());
					sb.append(" ");

					// type
					if (cf.getType().getSimpleName().equals("int")) {
						sb.append("INTEGER ");
					} else if (cf.getType().getSimpleName().equals("String")) {
						sb.append("VARCHAR(255) ");
					}
					// constraints
					String s = cf.isNullable() ? "" : "NOT NULL ";
					sb.append(s);

					s = cf.isUnique() ? "UNIQUE " : "";
					sb.append(s);

					if (!cf.getCheck().equals("none")) {
						sb.append("CHECK (" + cf.getCheck() + ") ");
					}

					sb.append(",");

				}
			}
			if (model.getForeignKeyFields() != null) {
				for (ForeignKeyField ff : model.getForeignKeyFields()) {
					sb.append(ff.getColumnName());
					sb.append(" ");
					// give foreign key INTEGER
					sb.append("INTEGER ");

					// constraints
					String s = ff.isNullable() ? "" : "NOT NULL ";
					sb.append(s);

					s = ff.isUnique() ? "UNIQUE " : "";
					sb.append(s);

					if (!ff.getCheck().equals("none")) {
						sb.append("CHECK (" + ff.getCheck() + ") ");
					}
					// getting name of the class its making join with
					String s1 = ff.getType().getSimpleName().toString();
					String primaryKeyName = "";
					for (MetaModel<?> m : models) {
						if (m.getSimpleClassName().equals(s1)) {
							PrimaryKeyField pkf = m.getPrimaryKeyField();
							primaryKeyName = pkf.getColumnName();
						}

					}
					sb.append("REFERENCES " + ff.getType().getSimpleName() + "(" + primaryKeyName
							+ ") ON DELETE CASCADE ON UPDATE CASCADE");

					sb.append(",");

				}
			}

			System.out.println(sb.substring(0, sb.toString().length() - 1) + ");");
			String query = sb.substring(0, sb.toString().length() - 1) + ");";
			createTableJDBC(query);
		}

	}

	@Override
	public void delete(Object Obj) {
		// TODO Auto-generated method stub
		
		// getting name of table
		Class<?> clazz = Obj.getClass();
		System.out.println("Class Name : " + clazz.getSimpleName());
		PrimaryKeyField pkf = null;
		Entity entity = clazz.getAnnotation(Entity.class);
		
		Class<?> c = (Class<?>) safeCastTo(Obj, clazz);
		
		// getting name of primary key
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields) {
			Id id = f.getAnnotation(Id.class);
			
			if(id!=null) {
				pkf = new PrimaryKeyField(f);	
			}
		}
		
		
//		for (Annotation annotation : c.class.getAnnotations()) {
//            Class<? extends Annotation> type = annotation.annotationType();
//            System.out.println("Values of " + type.getName());
//
//            for (Method method : type.getDeclaredMethods()) {
//                Object value = method.invoke(annotation, (Object[])null);
//                System.out.println(" " + method.getName() + ": " + value);
//            }
//        }
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM " + entity.tableName().toLowerCase() + " WHERE " + pkf.getColumnName() + " = ?;");
		System.out.println(sb);
		
		//INSERT_INTO_JDBC(sb, int)	;
		
	}

	@Override
	public int save(Object Obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Object Obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Class<?> clazz) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(Class<?> clazz, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Class<?>> getAll(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T safeCastTo(Object obj, Class<T> to) {
		if (obj != null) {
			Class<?> c = obj.getClass();
			if (to.isAssignableFrom(c)) {
				return to.cast(obj);
			}
		}
		return null;
	}

}