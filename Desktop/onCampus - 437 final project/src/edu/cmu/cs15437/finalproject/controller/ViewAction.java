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
import edu.cmu.cs15437.hw4.formbeans.ComposeForm;

/*
Add a value to the list of bookmarks
 */
public class ViewAction extends Action {
	private FormBeanFactory<ComposeForm> formBeanFactory = FormBeanFactory.getInstance(ComposeForm.class);

	private MessageDAO messageDAO;
	private UserDAO  userDAO;

    public ViewAction(Model model) {
    	messageDAO = model.getMessageDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "view.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
		
        
		try {

			ComposeForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);
	        return "viewmessage.jsp";
	        
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
