package edu.cmu.cs15437.hw4.formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class FindForm extends FormBean {
	private String first;
	private String last;
	
	public String getFirst()  { return first; }
	public String getLast()  { return last; }
	
	public void setFirst(String s) { first = s;  }
	public void setLast(String s) {	last = s.trim();                  }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if ((first == null || first.length() == 0) && (last == null || last.length() == 0)) {
	    errors.add("ERROR: at least one of first or last name must be filled for search");
		}
   		
		return errors;
	}
}