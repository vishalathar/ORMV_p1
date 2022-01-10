package com.ormv.session;

import com.ormv.dao.EntityDAO;
import com.ormv.util.Configuration;

public class ORMV_Session extends EntityDAO {
	
	private static EntityDAO edao = null;


	public ORMV_Session(Configuration cfg) {
		super(cfg);
		new EntityDAO<>(cfg);
		// TODO Auto-generated constructor stub
	}

}