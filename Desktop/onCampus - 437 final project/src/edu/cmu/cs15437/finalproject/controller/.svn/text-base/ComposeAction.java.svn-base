package edu.cmu.cs15437.hw4.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.cmu.cs15437.hw4.model.Model;
import edu.cmu.cs15437.hw4.model.MessageDAO;
import edu.cmu.cs15437.hw4.model.UserDAO;
import edu.cmu.cs15437.hw4.model.FriendshipDAO;

import org.mybeans.dao.DAOException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import edu.cmu.cs15437.hw4.databeans.MessageBean;
import edu.cmu.cs15437.hw4.databeans.FriendshipBean;
import edu.cmu.cs15437.hw4.databeans.UserBean;
import edu.cmu.cs15437.hw4.formbeans.ComposeForm;

/*
Add a value to the list of bookmarks
 */
public class ComposeAction extends Action {
	private FormBeanFactory<ComposeForm> formBeanFactory = FormBeanFactory.getInstance(ComposeForm.class);

	private MessageDAO messageDAO;
	private UserDAO  userDAO;
	private FriendshipDAO  friendshipDAO;
	private MessageBean reply;
    public ComposeAction(Model model) {
    	messageDAO = model.getMessageDAO();
    	userDAO  = model.getUserDAO();
		friendshipDAO  = model.getFriendshipDAO();
	}

    public String getName() { return "send.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
		String mes = request.getParameter("textmessage");
		String button = request.getParameter("button");
		String webapp = request.getContextPath();
        
		try {

			ComposeForm form = formBeanFactory.create(request);
			request.setAttribute("form",form);
	    	
	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			if (!form.isPresent()) {
	            return "compose.jsp";
	        }
	
	        // Any validation errors?
			
			if(request.getSession().getAttribute("message") != null){
				reply = (MessageBean)request.getSession().getAttribute("message");
				if(reply.getRequest() == true){
					if(Arrays.asList((friendshipDAO.getFriendships(user.getUserID())[0])).contains(reply.getSender())){
						errors.add("Error: You are already friends with" + reply.getSender());
						request.setAttribute("mymessagelist", messageDAO.getMyMessages(user.getUserID()));
						return "mymessagelist.jsp";
					}
					else if(user.getUserID().equals(reply.getSender())){
						errors.add("Error: Sorry can't add yourself as a friend, LOL");
						request.setAttribute("mymessagelist", messageDAO.getMyMessages(user.getUserID()));
						return "mymessagelist.jsp";
					}
					else{
						FriendshipBean friend = new FriendshipBean();
						friend.setUserA(reply.getSender());
						friend.setUserB(user.getUserID());
						friendshipDAO.create(friend);
					}
					return webapp + "/messages.do";
				}
				if(mes == null || (mes.length() == 0))
					errors.add("ERROR: message is empty");
			}
			else{
				errors.addAll(form.getValidationErrors());
				if(userDAO.lookup(form.getReciever()) == null)
					errors.add("ERROR: user does not exist");
			}
			
			if(mes == null || (mes.length() == 0))
				errors.add("ERROR: message is empty");
			
	        if (errors.size() != 0) {
	            return "compose.jsp";
	        }
			
				

			MessageBean message = new MessageBean(user.getUserID());
			if(request.getSession().getAttribute("message") == null) {
			message.setUserId(form.getReciever());
			message.setSender(user.getUserID());
			message.setSubject(form.getSubject());
			message.setMessage(mes);
			message.setRequest(false);
			}
			else{
				message.setUserId(reply.getSender());
				message.setSender(user.getUserID());
				message.setSubject(">" + reply.getSubject());
				message.setMessage(mes);
				message.setRequest(false);
				request.getSession().setAttribute("message",null);
			}
			messageDAO.create(message);
			
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
