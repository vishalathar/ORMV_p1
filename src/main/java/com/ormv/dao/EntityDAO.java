package com.ormv.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.ormv.annotations.Entity;
import com.ormv.annotations.Id;
import com.ormv.idao.IEntityDAO;
import com.ormv.inspection.ClassInspector;
import com.ormv.util.ColumnField;
import com.ormv.util.Configuration;
import com.ormv.util.ConnectionUtil;
import com.ormv.util.ForeignKeyField;
import com.ormv.util.MetaModel;
import com.ormv.util.PrimaryKeyField;

public class EntityDAO<T> implements IEntityDAO {
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	private List<MetaModel<Class<?>>> metaModels;

	public EntityDAO(Configuration cfg) {
		super();
		this.metaModels = cfg.getMetaModels();
	}

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
	public void DELETE_FROM_JDBC_BY_ID(String query, Object Obj) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = Configuration.getConnection();

			preparedStatement = connection.prepareStatement(query);

			for (MetaModel<?> model : metaModels) {

				if (model.getClazz().equals(Obj.getClass())) {
					List<ColumnField> cFields = model.getColumnFields();

					PrimaryKeyField pkf = model.getPrimaryKeyField();
					pkf.setAccessible(true);
					logger.info("pkf value: " + pkf.getValue(Obj));

					if (pkf.getType().equals(int.class)) {
						preparedStatement.setInt(1, (int) pkf.getValue(Obj));

					} else if (pkf.getType().equals(String.class)) {
						preparedStatement.setString(1, (String) pkf.getValue(Obj));

					}

				}
			}

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
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
	public void DELETE_ALL_JDBC_BY_CLASS(String query) {
		// TODO Auto-generated method stub

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = Configuration.getConnection();

			preparedStatement = connection.prepareStatement(query);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
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
	public <T> void createTableQuery(List<MetaModel<T>> models) {
		for (MetaModel<?> model : models) {
			StringBuilder sb = new StringBuilder();

			// getting table name
			Class clazz = model.getClazz();
			logger.info("model: " + clazz.getName());
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
				if (model.getPrimaryKeyField().getStrategy().equals("GenerationType.IDENTITY")) {
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

			logger.info(sb.substring(0, sb.toString().length() - 1) + ");");
			String query = sb.substring(0, sb.toString().length() - 1) + ");";
			createTableJDBC(query);
		}

	}

	@Override
	public Object GET_AN_OBJECT_BY_ID(String query, Object Obj) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = Configuration.getConnection();

			preparedStatement = connection.prepareStatement(query);
			PrimaryKeyField pkf = null;
			// get primary key value and its type
			for (MetaModel<?> model : metaModels) {

				if (model.getClazz().equals(Obj.getClass())) {
					pkf = model.getPrimaryKeyField();
					pkf.setAccessible(true);

					if (pkf.getType().equals(int.class)) {
						preparedStatement.setInt(1, (int) pkf.getValue(Obj));

					} else if (pkf.getType().equals(String.class)) {
						preparedStatement.setString(1, (String) pkf.getValue(Obj));

					}

				}
			}
			ResultSet rs;

			if ((rs = preparedStatement.executeQuery()) != null) {

				while (rs.next()) {
					for (MetaModel<?> model : metaModels) {
						if (model.getClazz().equals(Obj.getClass())) {
							if (pkf.getType().equals(int.class)) {
								pkf.setValue(Obj, rs.getInt(pkf.getColumnName()));

							} else if (pkf.getType().equals(String.class)) {
								pkf.setValue(Obj, rs.getString(pkf.getColumnName()));

							}

							List<ColumnField> cFields = model.getColumnFields();
							for (ColumnField cf : cFields) {
								if (cf.getType().equals(int.class)) {
									cf.setValue(Obj, rs.getInt(cf.getColumnName()));
								} else if (cf.getType().equals(String.class)) {
									cf.setValue(Obj, rs.getString(cf.getColumnName()));
								}
							}

							List<ForeignKeyField> fFields = model.getForeignKeyFields();
							for (ForeignKeyField fkf : fFields) {
								if (fkf.getType().equals(int.class)) {
									fkf.setValue(Obj, rs.getInt(fkf.getColumnName()));
								} else if (fkf.getType().equals(String.class)) {
									fkf.setValue(Obj, rs.getString(fkf.getColumnName()));
								}
							}

						}
					}

				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
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
		return Obj;
	}

	@Override
	public void delete(Object Obj) {
		// TODO Auto-generated method stub

		// getting name of table
		Class<?> clazz = Obj.getClass();
		logger.info("Class Name : " + clazz.getSimpleName());
		PrimaryKeyField pkf = null;
		Entity entity = clazz.getAnnotation(Entity.class);

		// Class<?> c = (Class<?>) safeCastTo(Obj, clazz);

		// getting name of primary key
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			Id id = f.getAnnotation(Id.class);

			if (id != null) {
				pkf = new PrimaryKeyField(f);

			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM " + entity.tableName().toLowerCase() + " WHERE " + pkf.getColumnName() + " = ?;");
		logger.info(sb);
		pkf.setAccessible(true);
		logger.info("pkf value: " + pkf.getValue(Obj));
		DELETE_FROM_JDBC_BY_ID(sb.toString(), Obj);
	}

	@Override
	public void truncate(Class<?> clazz) {
		// TODO Auto-generated method stub

		// getting name of table
		// Class<?> clazz = Obj.getClass();
		logger.info("Class Name : " + clazz.getSimpleName());
		PrimaryKeyField pkf = null;
		Entity entity = clazz.getAnnotation(Entity.class);

		StringBuilder sb = new StringBuilder();
		sb.append("TRUNCATE " + entity.tableName().toLowerCase() + " ;");
		logger.info(sb);

		DELETE_ALL_JDBC_BY_CLASS(sb.toString());

	}

	@Override
	public Object get(Class<?> clazz, Object id) {

		StringBuilder sb = new StringBuilder();
		sb.append("Select * FROM ");
		// get table name:
		Entity entity = clazz.getAnnotation(Entity.class);
		sb.append(entity.tableName().toLowerCase());
		sb.append(" WHERE ");

		PrimaryKeyField pkf = null;
		Object o = null;
		for (MetaModel<?> model : metaModels) {
			if (model.getClazz().equals(clazz)) {

				try {
					Class<?> clazz2 = Class.forName(clazz.getName());
					logger.info("clazz 2: " + clazz2.getName());
					o = clazz2.getConstructor(Object.class).newInstance(id);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// get primary key fieldname:
				pkf = model.getPrimaryKeyField();
				pkf.setAccessible(true);
				sb.append(pkf.getColumnName());
				sb.append(" = ? ");

			}
		}
		sb.append(";");
		logger.info(sb);
		Object getObj = GET_AN_OBJECT_BY_ID(sb.toString(), o);
		return getObj;

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

	@Override
	public Object INSERT_INTO_AN_OBJECT_JDBC(String query, Object Obj) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = Configuration.getConnection();
			preparedStatement = connection.prepareStatement(query);

			int i = 1;
			// getting values of fields:
			for (MetaModel<?> model : metaModels) {

				if (model.getClazz().equals(Obj.getClass())) {
					List<ColumnField> cFields = model.getColumnFields();
					Field[] oFields = Obj.getClass().getDeclaredFields();

					for (ColumnField cf : cFields) {
						cf.setAccessible(true);
						logger.info("type: " + cf.getType());

						if (cf.getType().equals(int.class)) {
							preparedStatement.setInt(i, (int) cf.getValue(Obj));
							i++;
						} else if (cf.getType().equals(String.class)) {
							preparedStatement.setString(i, (String) cf.getValue(Obj));
							i++;
						}
					}

				}
			}

			ResultSet rs;

			if ((rs = preparedStatement.executeQuery()) != null) {

				rs.next();
				logger.info("returned pk id: " + rs.getInt(1));
				Object id = rs.getInt(1);
				return id;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
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
		return null;

	}

	
	@Override
	public Object save(Object Obj) {

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");

		// get table name:
		Entity entity = Obj.getClass().getAnnotation(Entity.class);
		sb.append(entity.tableName().toLowerCase());
		sb.append(" (");

		// getting names of fields
		for (MetaModel model : metaModels) {

			if (model.getClazz().equals(Obj.getClass())) {
				List<ColumnField> cFields = model.getColumnFields();

				for (ColumnField cf : cFields) {

					sb.append(cf.getColumnName());

					sb.append(",");
				}
//				
//				List<ForeignKeyField> fkFields = model.getForeignKeyFields();
//
//				for (ForeignKeyField fkf : fkFields) {
//
//					sb.append(fkf.getColumnName());
//
//					sb.append(",");
//				}

			}
		}

		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(") VALUES (");

		PrimaryKeyField pkf = null;
		for (MetaModel<?> model : metaModels) {

			if (model.getClazz().equals(Obj.getClass())) {
				List<ColumnField> cFields = model.getColumnFields();
				Field[] oFields = Obj.getClass().getDeclaredFields();
				pkf = model.getPrimaryKeyField();

				for (ColumnField cf : cFields) {
					sb.append("?");
					sb.append(",");
				}

			}
		}

		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(") RETURNING ");
		sb.append(pkf.getColumnName());
		sb.append(";");

		logger.info(sb.toString());
		Object id = INSERT_INTO_AN_OBJECT_JDBC(sb.toString(), Obj);
		return id;
	}

	@Override
	public void UPDATE_AN_OBJECT_BY_OBJECT_JDBC(String query, Object Obj) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = Configuration.getConnection();
			preparedStatement = connection.prepareStatement(query);

			int i = 1;
			// getting values of fields:
			for (MetaModel<?> model : metaModels) {

				if (model.getClazz().equals(Obj.getClass())) {
					List<ColumnField> cFields = model.getColumnFields();
					Field[] oFields = Obj.getClass().getDeclaredFields();

					for (ColumnField cf : cFields) {
						logger.info("type: " + cf.getType());

						if (cf.getType().equals(int.class)) {
							preparedStatement.setInt(i, (int) cf.getValue(Obj));
							i++;
						} else if (cf.getType().equals(String.class)) {
							preparedStatement.setString(i, (String) cf.getValue(Obj));
							i++;
						}
					}
//					
//					List<ForeignKeyField> fkFields = model.getForeignKeyFields();
//					for (ForeignKeyField fkf : fkFields) {
//						logger.info(fkf.getType());
//						logger.info(model.getClazz());
//						logger.info(model.getClassName());
//						logger.info(model.);
//						
//						if (fkf.getType().equals(int.class)) {
//							preparedStatement.setInt(i, (int) fkf.getValue(Obj));
//							i++;
//						} else if (fkf.getType().equals(String.class)) {
//							preparedStatement.setString(i, (String) fkf.getValue(Obj));
//							i++;
//						} else if (fkf.getType().equals(model.getClazz())) {
//							preparedStatement.setObject(i, fkf.getValue(Obj));
//							i++;
//						}
//					}
					
					PrimaryKeyField pkf = model.getPrimaryKeyField();
					if (pkf.getType().equals(int.class)) {
						preparedStatement.setInt(i, (int) pkf.getValue(Obj));
						i++;
					} else if (pkf.getType().equals(String.class)) {
						preparedStatement.setString(i, (String) pkf.getValue(Obj));
						i++;
					}

				}
			}

			ResultSet rs;

			if ((rs = preparedStatement.executeQuery()) != null) {

				rs.next();
				logger.info("returned pk id: " + rs.getInt(1));
				Object id = rs.getInt(1);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.warn(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
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
	public void update(Object Obj) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");

		// get table name:
		Entity entity = Obj.getClass().getAnnotation(Entity.class);
		sb.append(entity.tableName().toLowerCase());
		sb.append(" SET ");

		// getting names of fields
		PrimaryKeyField pkf = null;
		for (MetaModel model : metaModels) {

			if (model.getClazz().equals(Obj.getClass())) {
				List<ColumnField> cFields = model.getColumnFields();
				pkf = model.getPrimaryKeyField();

				for (ColumnField cf : cFields) {

					sb.append(cf.getColumnName());

					sb.append(" = ?,");
				}
				
//				List<ForeignKeyField> fkFields = model.getForeignKeyFields();
//				if(fkFields!=null) {
//					for (ForeignKeyField fkf : fkFields) {
//
//						sb.append(fkf.getColumnName());
//
//						sb.append(" = ?,");
//					}
//				}

			}
		}

		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(" WHERE ");

		sb.append(pkf.getColumnName());
		sb.append("=?;");

		logger.info(sb.toString());
		UPDATE_AN_OBJECT_BY_OBJECT_JDBC(sb.toString(), Obj);
		
	}

}