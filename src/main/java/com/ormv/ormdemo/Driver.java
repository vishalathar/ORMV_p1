package com.ormv.ormdemo;

import java.util.List;

import com.ormv.dao.EntityDAO;
import com.ormv.demomodels.DemoOtherClass;
import com.ormv.demomodels.DemoUser;
import com.ormv.util.Configuration;
public class Driver {
	
	private static EntityDAO edao = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Configuration cfg = new Configuration();
		
		cfg.addAnnotatedClass(DemoOtherClass.class);
		
		cfg.addAnnotatedClass(DemoUser.class);
		
		cfg.showReflectionMagic();
		
		DemoUser du1 = new DemoUser("Vishal", "Athar");
		DemoUser du2 = new DemoUser(1, "Fozia", "Chughtai");
		
		edao = new EntityDAO<>(cfg);
		
		//edao.save(du1);
		
		//edao.delete(du2);
		edao.truncate(DemoUser.class);
		
		
	}

}
