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
		DemoUser du2 = new DemoUser("Fozia", "Chughtai");
		
		edao = new EntityDAO<>(cfg);
		
		// functions need to get to work
		
		du1.setId((int)edao.save(du1));
		edao.save(du2);
		
		DemoUser du1Copy = (DemoUser)edao.get(DemoUser.class, du1.getId());
		System.out.println(du1Copy);
		
		du1.setFirstName("Arfa");
		du1.setLastname("Athar");
		edao.update(du1);
		
		//System.out.println(du1);
		// functions working
		//edao.delete(du1);
		//edao.truncate(DemoUser.class);
		
		
	}

}
