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
		cfg.addAnnotatedClass(DemoOtherClass.class);
		
		cfg.getConnection();
		
		
		cfg.printMetaModels();
		cfg.showReflectionMagic();
		
		
	}

}
