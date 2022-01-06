package com.ormv.ormdemo;

import java.util.List;

import com.ormv.demomodels.DemoOtherClass;
import com.ormv.demomodels.DemoUser;
import com.ormv.util.Configuration;
import com.ormv.util.ColumnField;
import com.ormv.util.ForeignKeyField;
import com.ormv.util.MetaModel;
import com.ormv.util.PrimaryKeyField;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Configuration cfg = new Configuration();
		
		cfg.addAnnotatedClass(DemoUser.class);
		//cfg.addAnnotatedClass(DemoOtherClass.class);
		
		cfg.getConnection("url", "username", "password");
		
		for (MetaModel<?> metamodel : cfg.getMetaModels()) {
			
			System.out.printf("Printing MetaModel for class: %s\n", metamodel.getClassName());
			PrimaryKeyField pk = metamodel.getPrimaryKey();
			List<ColumnField> columns = metamodel.getColumns();
			List<ForeignKeyField> foreignKeyFields = metamodel.getForeignKeys();
			
			System.out.printf("\t Found a primary key field named %s, of type %s, which maps to the column with name: %s\n", 
					pk.getName(), pk.getType(), pk.getColumnName());
			
			for (ColumnField column : columns) {
				System.out.printf("\t Found a column field named %s, of type %s, which maps to the column with name: %s\n", 
						column.getName(), column.getType(), column.getColumnName());
			}
			
			for (ForeignKeyField foreignKey : foreignKeyFields) {
				System.out.printf("\t Found a foreign key field named %s, of type %s, which maps to the column with name: %s\n", 
						foreignKey.getName(), foreignKey.getType(), foreignKey.getColumnName());
			}		
		}
	}

}
