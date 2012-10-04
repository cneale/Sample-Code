package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.UserBean;
import edu.cmu.cs15437.hw4.formbeans.FindForm;

/*
Add a value to the list of bookmarks
 */
public class FindFriendAction extends Action {
	private FormBeanFactory<FindForm> formBeanFactory = FormBeanFactory.getInstance(FindForm.class);

	private UserDAO  userDAO;

    public FindFriendAction(Model model) {
    	userDAO = model.getUserDAO();
	}

    public String getName() { return "find.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {
		
			FindForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);
			
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			
			UserBean[] list = userDAO.lookupStartsWith(form.getLast(),form.getFirst());
			
			if (!form.isPresent()) {
				request.setAttribute("searchlist",list);
	            return "peoplelist.jsp";
	        }
		/*
	        // Any validation errors?
			
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
				request.setAttribute("searchlist",list);
	            return "peoplelist.jsp";
	        }
			*/
			
			if (list.length == 0 ) {
				errors.add("No matches for last name starts with \""+form.getLast()+"\" and first starts with \""+form.getFirst()+"\"");
				request.setAttribute("searchlist",list);
				return "peoplelist.jsp";
			}
			
			if (list.length > 0) {
				request.setAttribute("searchlist",list);
				return "peoplelist.jsp";
			}
			request.setAttribute("searchlist",list);
			return "peoplelist.jsp";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
