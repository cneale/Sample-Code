package edu.cmu.cs15437.hw4.model;

import java.util.Arrays;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanFactory;
import org.mybeans.factory.BeanFactoryException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.MatchArg;
import org.mybeans.factory.RollbackException;
import org.mybeans.factory.Transaction;

import edu.cmu.cs15437.hw4.databeans.MessageBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;

public class MessageDAO {
	private BeanFactory<MessageBean> factory;
	
	public MessageDAO() throws DAOException {
		try {
			// Get a BeanTable so we can create the "message" table
	        BeanTable<MessageBean> messageTable = BeanTable.getInstance(MessageBean.class,"message");
	        
	        if (!messageTable.exists()) messageTable.create("id");
	        
	        // Long running web apps need to clean up idle database connections.
	        // So we can tell each BeanTable to clean them up.  (You would only notice
	        // a problem after leaving your web app running for several hours.)
	        messageTable.setIdleConnectionCleanup(true);
	
	        // Get a BeanFactory which the actions will use to read and write rows of the "messages" table
	        factory = messageTable.getFactory();
		} catch (BeanFactoryException e) {
			throw new DAOException(e);
		}
	}
	
	public void create(MessageBean message) throws DAOException {
        try {
        	Transaction.begin();
			MessageBean dbMessage = factory.create();
			factory.copyInto(message,dbMessage);
			Transaction.commit();
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public void delete(int id, String user) throws DAOException {
		try {
			Transaction.begin();
    		MessageBean p = factory.lookup(id);

    		if (p == null) {
				throw new DAOException("Message does not exist: id="+ id);
    		}

    		if (!user.equals(p.getUserId())) {
				throw new DAOException("Message not owned by "+ user);
    		}
			factory.delete(id);
			Transaction.commit();
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
	
	public MessageBean lookup(int id) throws DAOException {
		try {
			return factory.lookup(id);
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
	
	protected BeanFactory<MessageBean> getFactory() { return factory; }
	
	public MessageBean[] getMyMessages(String user) throws DAOException {
		try {	
			MessageBean[] list = factory.match(MatchArg.equals("userid",user));
			Arrays.sort(list);
			return list;
		} catch (RollbackException e) {
			throw new DAOException(e);
		}
	}
}
