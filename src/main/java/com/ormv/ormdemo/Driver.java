package com.ormv.ormdemo;

import java.util.List;

import com.ormv.dao.EntityDAO;
import com.ormv.demomodels.DemoOtherClass;
import com.ormv.demomodels.DemoUser;
import com.ormv.util.Configuration;
public class Driver {
	
	private static EntityDAO edao = new EntityDAO<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Configuration cfg = new Configuration();
		
		cfg.addAnnotatedClass(DemoOtherClass.class);
		
		cfg.addAnnotatedClass(DemoUser.class);
		
		cfg.showReflectionMagic();
		
		DemoUser du1 = new DemoUser("Vishal", "Athar");
		DemoUser du2 = new DemoUser("Fozia", "Chughtai");
		
		//edao.save(du1);
		
		//edao.delete(du1);
		
		
	}

}
