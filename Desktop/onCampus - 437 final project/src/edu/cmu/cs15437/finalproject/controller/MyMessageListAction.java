package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.MessageDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.MessageBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;

/*
Add a value to the list of bookmarks
 */
public class MyMessageListAction extends Action {

	private MessageDAO messageDAO;
	private UserDAO  userDAO;

    public MyMessageListAction(Model model) {
    	messageDAO = model.getMessageDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "messages.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {
	    	
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			request.setAttribute("mymessagelist", messageDAO.getMyMessages(user.getUserID()));
					
			return "mymessagelist.jsp";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } 
    }
}
