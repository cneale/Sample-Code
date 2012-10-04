package edu.cmu.cs15437.hw4.model;

import java.util.Arrays;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanFactory;
import org.mybeans.factory.BeanFactoryException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.MatchArg;
import org.mybeans.factory.DuplicateKeyException;
import org.mybeans.factory.RollbackException;
import org.mybeans.factory.Transaction;

import edu.cmu.cs15437.hw4.databeans.LocationBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;


public class LocationDAO {
	private BeanFactory<LocationBean> factory;
	
	public LocationDAO() throws DAOException {
		try {
			// Get a BeanTable so we can create the "users" table
	        BeanTable<LocationBean> locationTable = BeanTable.getInstance(LocationBean.class,"locations");
	        
	        if (!locationTable.exists()) locationTable.create("locationid");
	        
	        // Long running web apps need to clean up idle database connections.
	        // So we can tell each BeanTable to clean them up.  (You would only notice
	        // a problem after leaving your web app running for several hours.)
	        locationTable.setIdleConnectionCleanup(true);
	
	        // Get a BeanFactory which the actions will use to read and write rows of the "user" table
	        factory = locationTable.getFactory();
		} catch (BeanFactoryException e) {
			throw new DAOException(e);
		}
	}
	
	public void create(LocationBean location) throws DAOException {
        try {
        	Transaction.begin();
			LocationBean dbLocation = factory.create();
			factory.copyInto(location,dbLocation);
			Transaction.commit();
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public LocationBean lookup(int id) throws DAOException {
		try {
			return factory.lookup(id);
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
	public LocationBean lookupplace(String name) throws DAOException {
		try {
			LocationBean[] location = factory.match(MatchArg.equals("name",name));
			if (location == null)
				return null;				
			else return location[0];
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
	protected BeanFactory<LocationBean> getFactory() { return factory; }
	
	public LocationBean[] getLocations() throws DAOException {
		try {
			LocationBean[] locations = factory.match();
			return locations;
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
}
