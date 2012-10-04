package edu.cmu.cs15437.hw4.model;

import java.util.Arrays;
import java.util.List;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanFactory;
import org.mybeans.factory.BeanFactoryException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.MatchArg;
import org.mybeans.factory.RollbackException;
import org.mybeans.factory.Transaction;

import edu.cmu.cs15437.hw4.databeans.EventBean;

public class EventDAO {
	private BeanFactory<EventBean> factory;
	
	public EventDAO() throws DAOException {
		try {
	        // Get a BeanTable to create the "photo" table
	        BeanTable<EventBean> eventTable = BeanTable.getInstance(EventBean.class,"events");
	        
	        if (!eventTable.exists()) eventTable.create("eventid");
	        
	        // Long running web apps need to clean up idle database connections.
	        // So we can tell each BeanTable to clean them up.  (You would only notice
	        // a problem after leaving your web app running for several hours.)
	        eventTable.setIdleConnectionCleanup(true);
	
	        // Get a BeanFactory which the actions will use to read and write
	        // rows of the "photo" table
	        factory = eventTable.getFactory();
		} catch (BeanFactoryException e) {
			throw new DAOException(e);
		}
	}

	public void create(EventBean newEvent) throws DAOException {
		try {
			Transaction.begin();
			EventBean dbEvent = factory.create();
			factory.copyInto(newEvent,dbEvent);
			Transaction.commit();
			
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}

	public void delete(int id, String owner) throws DAOException {
		try {
			Transaction.begin();
    		EventBean p = factory.lookup(id);

    		if (p == null) {
				throw new DAOException("Event does not exist: id="+id);
    		}

    		if (!owner.equals(p.getUserId())) {
				throw new DAOException("Event not owned by "+owner);
    		}
			factory.delete(id);
			Transaction.commit();
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public EventBean[] getMyEvents(String userid) throws DAOException {
		try {
			
			EventBean[] list = factory.match(MatchArg.equals("userid",userid));
			Arrays.sort(list);
			return list;
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
	public EventBean[] getAllEvents() throws DAOException {
		try {
			EventBean[] list = factory.match();
			Arrays.sort(list);
			return list;
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
    public EventBean[] getEventsByLocation(int id){
        try{
            EventBean[] list = factory.match(MatchArg.equals("locationid",id));
            return list;
        }catch(RollbackException  e){
            throw new DAOException(e);
        }
    }
    
	public EventBean lookup(int id) throws DAOException {
		try {
			return factory.lookup(id);
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
}