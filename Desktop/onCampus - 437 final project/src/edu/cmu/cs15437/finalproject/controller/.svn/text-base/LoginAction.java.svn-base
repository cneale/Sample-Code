package edu.cmu.cs15437.hw4.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.UserBean;
import edu.cmu.cs15437.hw4.formbeans.LoginForm;
import edu.cmu.cs15437.hw4.formbeans.RegisterForm;

/*
 * Processes the parameters from the form in login.jsp.
 * If successful, set the "user" session attribute to the
 * user's User bean and then redirects to view the originally
 * requested photo.  If there was no photo originally requested
 * to be viewed (as specified by the "redirect" hidden form
 * value), just redirect to manage.do to allow the user to manage
 * his photos.
 */
public class LoginAction extends Action {
	private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory.getInstance(LoginForm.class);
	
	private UserDAO userDAO;

	public LoginAction(Model model) {
		userDAO = model.getUserDAO();
	}

	public String getName() { return "login.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
		String button = request.getParameter("button");
        
        try {
	    	LoginForm form = formBeanFactory.create(request);
	        request.setAttribute("form",form);

	        // If the servlet path isn't "/login.do", the controller sent a request here
	        // because the user needed to login.  We want to support redirect back to the
	        // original request only if the user is just trying to view an image.
	        // So, only set up redirect back to original request path="/view.do".
	        if (request.getServletPath().equals("/messages.do") || 
			request.getServletPath().equals("/compose.do") || 
			request.getServletPath().equals("/friend.do") ||
			request.getServletPath().equals("/friendlist.do") ||
			request.getServletPath().equals("/eventlist.do")||
			request.getServletPath().equals("/events.do")){
			
	        	String redirectTo = request.getRequestURI()+"?"+request.getQueryString();
	        	HttpSession session = request.getSession();
	        	session.setAttribute("redirectTo",redirectTo);
	        }

	        // If no params were passed, return with no errors so that the form will be
	        // presented (we assume for the first time).
	        if (!form.isPresent()) {
	            return "login.jsp";
	        }

	        // Any validation errors?
	        errors.addAll(form.getValidationErrors());
			
	        if (errors.size() != 0) {
	            return "login.jsp";
	        }
						// Look up the user
	        UserBean user = userDAO.lookup(form.getUserId());
			
			if(button.equals("register")){
					if (user != null) {
						errors.add("User ID already exists");
						return "login.jsp";
					}

				HttpSession session = request.getSession();	
				session.setAttribute("userid",form.getUserId());
				session.setAttribute("password",form.getPassword());
				String webapp = request.getContextPath();
				return webapp + "/register.jsp";
			}
	        
	        
	        if (user == null) {
	            errors.add("User ID not found");
	            return "login.jsp";
	        }

	        // Check the password
	        if (!((user.getPassword()).equals(form.getPassword()))) {
	            errors.add("Incorrect password");
	            return "login.jsp";
	        }
			
	        // Attach (this copy of) the user bean to the session
	        HttpSession session = request.getSession();
	        session.setAttribute("user",user);
	
	        // After successful login send to...
	        String redirectTo = (String) session.getAttribute("redirectTo");
	        if (redirectTo != null) return redirectTo;
	        
	        
	        // If redirectTo is null, redirect to the "manage" action
			String webapp = request.getContextPath();
			return webapp + "/messages.do";
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
		}
    
}
