package edu.cmu.cs15437.hw4.model;

import java.util.Arrays;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanFactory;
import org.mybeans.factory.BeanFactoryException;
import org.mybeans.factory.BeanTable;
import org.mybeans.factory.DuplicateKeyException;
import org.mybeans.factory.MatchArg;
import org.mybeans.factory.RollbackException;
import org.mybeans.factory.Transaction;

import edu.cmu.cs15437.hw4.databeans.FriendshipBean;

public class FriendshipDAO{
    private BeanFactory<FriendshipBean> factory;
    
    public FriendshipDAO() throws DAOException {
        try {
			// Get a BeanTable so we can create the "users" table
	        BeanTable<FriendshipBean> friendshipTable = BeanTable.getInstance(FriendshipBean.class,"onCampusFriendships");
	        
	        if (!friendshipTable.exists()) friendshipTable.create("id");
	        
	        // Long running web apps need to clean up idle database connections.
	        // So we can tell each BeanTable to clean them up.  (You would only notice
	        // a problem after leaving your web app running for several hours.)
	        friendshipTable.setIdleConnectionCleanup(true);
            
	        // Get a BeanFactory which the actions will use to read and write rows of the "user" table
	        factory = friendshipTable.getFactory();
		} catch (BeanFactoryException e) {
			throw new DAOException(e);
		}
    }
    public void create(FriendshipBean friendship) throws DAOException {
        try{
			Transaction.begin();
            FriendshipBean friend = factory.create();
            factory.copyInto(friendship, friend);
            Transaction.commit();
        }catch(RollbackException e){
            throw new DAOException(e);
        } finally{
            if(Transaction.isActive()) Transaction.rollback();
        }
    }
	
	public void delete(int id, String user) throws DAOException {
		try {
			Transaction.begin();
    		FriendshipBean p = factory.lookup(id);

    		if (p == null) {
				throw new DAOException("Friend does not exist: id="+id);
    		}

    		if (!user.equals(p.getUserA()) && !user.equals(p.getUserB())) {
				throw new DAOException("Individual is not a friend to "+user);
    		}
			factory.delete(id);
			Transaction.commit();
		} catch (RollbackException e) {
			throw new DAOException(e);
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
    
	protected BeanFactory<FriendshipBean> getFactory() { return factory; }
	
    public String[][] getFriendships(String userID) throws DAOException{
        try{
            FriendshipBean[] friendshipsA = factory.match(MatchArg.equals("userA",userID));
            FriendshipBean[] friendshipsB = factory.match(MatchArg.equals("userB",userID));
            int size = friendshipsA.length + friendshipsB.length;
            int i = 0;
            String[][] friends = new String[2][size];
            
            for(int j = 0; j < friendshipsA.length; j++){
                friends[0][i] = friendshipsA[j].getUserB();
				friends[1][i] = Integer.toString(friendshipsA[j].getId());
                i++;
            }
            
            for(int k = 0; k < friendshipsB.length; k++){
                friends[0][i] = friendshipsB[k].getUserA();
				friends[1][i] = Integer.toString(friendshipsB[k].getId());
                i++;
            }
			return friends;
		} catch (RollbackException e) {
			throw new DAOException(e);
     
		}
	}
}	