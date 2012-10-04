package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.LocationDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.EventBean;

public class MapPopupAction extends Action{
    private EventDAO eventDAO;
    private LocationDAO locationDAO;
    private UserDAO userDAO;
    
    public MapPopupAction(Model model){
        locationDAO = model.getLocationDAO();
        eventDAO = model.getEventDAO();
        userDAO = model.getUserDAO();
    }
    
    public String getName(){
        return "mappopup.do";
    }
    
    public String perform(HttpServlet request){
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        int id = (int) request.getAttribute("location_id");
        try{
            EventBean[] beanList = eventDAO.getEventsByLocation(id);
            request.setAttribute("eventlist",beanList);
            String webapp = request.getContextPath();
            return "tabbedView.jsp";
        } catch(DAOException e){
            error.add(e.getMessage());
            return "error.jsp"
        }
            
    }
}