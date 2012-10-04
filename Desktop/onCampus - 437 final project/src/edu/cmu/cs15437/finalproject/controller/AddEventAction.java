package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.EventDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.EventBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;
import edu.cmu.cs15437.hw4.formbeans.NewEventForm;
import edu.cmu.cs15437.hw4.formbeans.UserForm;

/*
Add a value to the list of bookmarks
 */
public class AddEventAction extends Action {
	private FormBeanFactory<NewEventForm> formBeanFactory = FormBeanFactory.getInstance(NewEventForm.class);

	private EventDAO eventDAO;
	private UserDAO  userDAO;

    public AddEventAction(Model model) {
    	eventDAO = model.getEventDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "addevent.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {

			NewEventForm form = formBeanFactory.create(request);
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
			
	
			DateFormat dformat = new SimpleDateFormat();
			EventBean event = new EventBean(form.getEvent());
			event.setUserId(form.getUserId());
			event.setComment(form.getCaption());
			event.setLocation(form.getLocation());
			event.setSDate(dformat.format(new Date(form.getSYear(),form.getSMonth(),form.getSDay(),form.getSHour(),form.getSMinute())));
			event.setEDate(dformat.format(new Date(form.getEYear(),form.getEMonth(),form.getEDay(),form.getEHour(),form.getEMinute())));
			event.setSTime(form.getSEvening());
			event.setETime(form.getEEvening());
			
			eventDAO.create(event);
			
			String webapp = request.getContextPath();
				return webapp + "/peoplelist.do";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
