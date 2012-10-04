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

/*
Add a value to the list of bookmarks
 */
public class MyFriendListAction extends Action {


	private FriendshipDAO friendshipDAO;
	private UserDAO  userDAO;

    public MyFriendListAction(Model model) {
    	friendshipDAO = model.getFriendshipDAO();
    	userDAO  = model.getUserDAO();
	}

    public String getName() { return "myfriendlist.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        
		try {

	    	UserBean user = (UserBean) request.getSession().getAttribute("user");
			
			String[][] friends = friendshipDAO.getFriendships(user.getUserID());
			
			UserBean[] friend = new UserBean[friends[0].length];
			String[] friend1 = new String[friends[1].length];
			int i = 0;
			for(i = 0; i < friends[0].length; i++){
				friend[i] = userDAO.lookup(friends[0][i]);
				friend1[i] = friends[1][i];
			}
			
			request.setAttribute("myfriendlist", friend);
			request.setAttribute("friendid", friend1);
			
			return "myfriendlist.jsp";
			
        } catch (DAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } 
    }
}
