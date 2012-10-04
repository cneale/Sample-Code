package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.EventDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.EventBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;

/*
Add a value to the list of bookmarks
 */
public class MyEventListAction extends Action {

	private EventDAO eventDAO;
	private UserDAO  userDAO;

    public MyEventListAction(Model model) {
    	eventDAO = model.getEventDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "eventlist.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {
	    	
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			
            // Set up my bookmark list
			request.setAttribute("myeventlist",eventDAO.getMyEvents(user.getUserID()));
	        return "myeventlist.jsp";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
		}	
    }
}
