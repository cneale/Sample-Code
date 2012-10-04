package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.MessageDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;
import edu.cmu.cs15437.hw4.model.EventDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.EventBean;
import edu.cmu.cs15437.hw4.formbeans.IdForm;
import edu.cmu.cs15437.hw4.databeans.UserBean;

/*
Add a value to the list of bookmarks
 */
public class RemoveEventAction extends Action {
	private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);

	private EventDAO eventDAO;
	private UserDAO  userDAO;

    public RemoveEventAction(Model model) {
    	eventDAO = model.getEventDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "addevent.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {

			IdForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);
	    	
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			
			if (!form.isPresent()) {
	            return "eventlist.jsp";
	        }
	
	        // Any validation errors?
			
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "eventlist.jsp";
	        }
			
			eventDAO.delete(form.getIdAsInt(),user.getUserID());

			
			String webapp = request.getContextPath();
				return webapp + "/eventlist.do";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
