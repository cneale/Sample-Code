package edu.cmu.cs15437.hw4.formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class LoginForm extends FormBean {
	private String userId;
	private String password;
	
	public String getUserId()  { return userId; }
	public String getPassword()  { return password; }
	
	public void setUserId(String s) { userId = s;  }
	public void setPassword(String s) {	password = s.trim();                  }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (userId == null || userId.length() == 0) {
	    errors.add("ERROR: userid is empty");
		}
   		
		if (password == null || password.length() == 0) {
			errors.add("ERROR: password is empty");
		}
		
		return errors;
	}
}