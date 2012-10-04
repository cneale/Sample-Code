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
import edu.cmu.cs15437.hw4.formbeans.AddFriendForm;

/*
Add a value to the list of bookmarks
 */
public class AddFriendAction extends Action {
	private FormBeanFactory<AddFriendForm> formBeanFactory = FormBeanFactory.getInstance(AddFriendForm.class);

	private MessageDAO messageDAO;
	private UserDAO  userDAO;

    public AddFriendAction(Model model) {
    	messageDAO = model.getMessageDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "addfriend.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
		String reciever = request.getParameter("reciever");

        
		try {

			AddFriendForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);
	    	
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			
		/*	if (!form.isPresent()) {
	            return "peoplelist.jsp";
	        }
	
	        // Any validation errors?
			
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "peoplelist.jsp";
	        }
			*/

			MessageBean message = new MessageBean(user.getUserID());
			message.setUserId(reciever);
			message.setSubject("New Friend Request From:" + user.getFirstName() + " " + user.getLastName());
			message.setMessage("Do you wish to add " + user.getUserID() + " as a friend?");
			message.setRequest(true);
			
			messageDAO.create(message);
			
			String webapp = request.getContextPath();
				return webapp + "/find.do";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
