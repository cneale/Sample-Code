package edu.cmu.cs15437.hw4.formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class AttendEventForm extends FormBean {
	private String sender;
	private String reciever;
	private String message;
	private String subject;
	
	public String getSender()  { return sender; }
	public String getReciever()  { return reciever; }
	public String getMessage()  { return message; }
	public String getSubject()  { return subject; }
	
	public void setSender(String s) { sender = s;  }
	public void setLast(String s) {	reciever = s.trim();}
	public void setMessage(String s) {	message = s; }
	public void setSubject(String s) {	subject = s; }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		
		if (reciever == null || reciever.length() == 0) {
	    errors.add("ERROR: no destination userid specified");
		}
   		
		if (message == null || message.length() == 0) {
			errors.add("ERROR: message is empty");
		}
		
		if (subject == null || subject.length() == 0) {
			errors.add("ERROR: subject is empty");
		}

		// no errors tracked here
   		
		return errors;
	}
}