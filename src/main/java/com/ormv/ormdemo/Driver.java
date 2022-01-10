package com.ormv.ormdemo;

import java.util.List;

import com.ormv.dao.EntityDAO;
import com.ormv.demomodels.DemoOtherClass;
import com.ormv.demomodels.DemoUser;
import com.ormv.session.ORMV_Session;
import com.ormv.util.Configuration;
public class Driver {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Configuration cfg = new Configuration();
		
		cfg.addAnnotatedClass(DemoOtherClass.class);
		
		cfg.addAnnotatedClass(DemoUser.class);
		
		cfg.showReflectionMagic();
		
		DemoUser du1 = new DemoUser("Vishal", "Athar");
		DemoUser du2 = new DemoUser("Fozia", "Chughtai");
		
		ORMV_Session ses = new ORMV_Session(cfg);
		
		// functions need to get to work
		
		du1.setId((int)ses.save(du1));
		ses.save(du2);
		
		DemoUser du1Copy = (DemoUser)ses.get(DemoUser.class, du1.getId());
		System.out.println(du1Copy);
		
		du1.setFirstName("Arfa");
		du1.setLastname("Athar");
		ses.update(du1);
		
		System.out.println(du1);
		// functions working
		ses.delete(du1);
		ses.truncate(DemoUser.class);
		
		
	}

}
