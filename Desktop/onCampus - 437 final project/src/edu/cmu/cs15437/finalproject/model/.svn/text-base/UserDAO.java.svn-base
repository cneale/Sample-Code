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

import edu.cmu.cs15437.hw4.databeans.UserBean;

public class UserDAO {
    private BeanFactory<UserBean> factory;
    
    public UserDAO() throws DAOException {
        try{
            BeanTable<UserBean> userTable = BeanTable.getInstance(UserBean.class,"onCampusUsers");
            
            if(!userTable.exists()) userTable.create("userID");
            
	        // Long running web apps need to clean up idle database connections.
	        // So we can tell each BeanTable to clean them up.  (You would only notice
	        // a problem after leaving your web app running for several hours.)
	        userTable.setIdleConnectionCleanup(true);
            
	        // Get a BeanFactory which the actions will use to read and write rows of the "user" table
	        factory = userTable.getFactory();
		} catch (BeanFactoryException e) {
			throw new DAOException(e);
		}
    }
    
    public void create(UserBean user) throws DAOException {
        String origID = user.getUserID();
        try{
            Transaction.begin();
            user.setUserID(user.getUserID().toLowerCase());
            UserBean dBUser = factory.create(user.getUserID());
            factory.copyInto(user, dBUser);
            Transaction.commit();
        }catch(DuplicateKeyException e){
            throw new DAOException("UserID: "+user.getUserID()+"already exists.");
        }catch(RollbackException e){
            throw new DAOException(e);
        } finally{
            if(Transaction.isActive()) Transaction.rollback();
        }
    }
    
	public UserBean[] lookupStartsWith(String startOfLast, String startOfFirst) throws DAOException {
    	try {
	    UserBean[] list = getFactory().match(
					      MatchArg.startsWithIgnoreCase("lastName",startOfLast),
					      MatchArg.startsWithIgnoreCase("firstName",startOfFirst));
	    Arrays.sort(list);
	    return list;
    	} catch (RollbackException e) {
	    throw new DAOException(e);
    	}
    }
	
    public UserBean lookup(String userID) throws DAOException{
        try{
            return factory.lookup(userID);
        }catch (RollbackException e){
            throw new DAOException(e);
        }
    }
	
	protected BeanFactory<UserBean> getFactory() { return factory; }
	
	public void setEvent(int eventid, String userid) throws DAOException{
        try {
	    Transaction.begin();
	    UserBean dbUser = factory.lookup(userid);
			
	    if (dbUser == null) {
			throw new DAOException("User "+userid+" no longer exists");
	    }
		
		dbUser.seteventAttendingID(eventid);
	    Transaction.commit();
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
}
