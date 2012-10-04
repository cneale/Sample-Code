package edu.cmu.cs15437.hw4.model;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.mybeans.dao.DAOException;
import org.mybeans.factory.BeanTable;

public class Model {
    private EventDAO eventDAO;
    private LocationDAO  locationDAO;
	private MessageDAO  messageDAO;
    private UserDAO userDAO;
    private FriendshipDAO friendshipDAO;
    
    public Model(ServletConfig config) throws ServletException {
	try {
	    String csvDirStr = config.getInitParameter("csvDir");
	    if (csvDirStr != null && csvDirStr.length() > 0) {
		File csvDir = new File(csvDirStr);
		BeanTable.useCSVFiles(csvDir);
	    } else {
		String jdbcDriver   = config.getInitParameter("jdbcDriverName");
		String jdbcURL      = config.getInitParameter("jdbcURL");
		String jdbcUser     = config.getInitParameter("jdbcUser");
		String jdbcPassword = config.getInitParameter("jdbcPassword");
		BeanTable.useJDBC(jdbcDriver,jdbcURL,jdbcUser,jdbcPassword);
	    }
			
	    locationDAO  = new LocationDAO();
	    eventDAO = new EventDAO();
		messageDAO = new MessageDAO();
        userDAO = new UserDAO();
        friendshipDAO = new FriendshipDAO();
	} catch (DAOException e) {
	    throw new ServletException(e);
	}
    }
	
    public LocationDAO getLocationDAO() { return locationDAO; }
    public EventDAO  getEventDAO()  { return eventDAO;  }
	public MessageDAO  getMessageDAO()  { return messageDAO;  }
    public FriendshipDAO getFriendshipDAO(){ return friendshipDAO;}
    public UserDAO getUserDAO(){ return userDAO;}
}
