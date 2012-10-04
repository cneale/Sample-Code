package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.FriendshipDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.FriendshipBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;
import edu.cmu.cs15437.hw4.formbeans.IdForm;

/*
Add a value to the list of bookmarks
 */
public class RemoveFriendAction extends Action {
	private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);

	private FriendshipDAO friendshipDAO;
	private UserDAO  userDAO;

    public RemoveFriendAction(Model model) {
    	friendshipDAO = model.getFriendshipDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "removefriend.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {

			IdForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);
	    	
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			
			if (!form.isPresent()) {
	            return "myfriendlist.jsp";
	        }
	
	        // Any validation errors?
			
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "myfriendlist.jsp";
	        }
			
			friendshipDAO.delete(form.getIdAsInt(), user.getUserID());
			
			String webapp = request.getContextPath();
				return webapp + "/myfriendlist.do";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
