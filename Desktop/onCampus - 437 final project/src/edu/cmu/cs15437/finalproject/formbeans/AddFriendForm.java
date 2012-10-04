package edu.cmu.cs15437.hw4.formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class AddFriendForm extends FormBean {
	private String sender;
	private String reciever;
	
	public String getSender()  { return sender; }
	public String getReciever()  { return reciever; }
	
	public void setSender(String s) { sender = s;  }
	public void setLast(String s) {	reciever = s.trim();                  }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		// no errors tracked here
   		
		return errors;
	}
}