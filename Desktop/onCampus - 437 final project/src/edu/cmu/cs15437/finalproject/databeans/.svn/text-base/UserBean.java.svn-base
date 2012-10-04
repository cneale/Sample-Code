package edu.cmu.cs15437.hw4.databeans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UserBean implements Comparable<UserBean>{
    private String userID = null;
    private String firstName = null;
    private String lastName = null;
    private String password = null;
    private int eventAttendingID = -1;
    
    public UserBean(){
        
    }
    
    //may need to hash password
    
    public String getUserID(){ return userID; }
    public void setUserID(String uID){ userID = uID;}
    public String getFirstName(){ return firstName; }
    public void setFirstName(String fName){firstName = fName;}
    public String getLastName(){ return lastName;}
    public void setLastName(String lName){ lastName = lName;}
    public void seteventAttendingID(int eventid){eventAttendingID = eventid;}
    public int geteventAttendingID(){ return eventAttendingID;}
	public void setPassword(String passw){ password = passw;}
	public String getPassword(){ return password;}
	
	public int compareTo(UserBean other) {
	// Order first by owner, then by position
		
	    int lastcomp = lastName.compareTo(other.getLastName());
		int firstcomp = lastName.compareTo(other.getLastName());
		if(lastcomp != 0)
			return lastcomp;
		else 
			return firstcomp;
    }
	
    public boolean equals(Object obj) {
	if (obj instanceof UserBean) {
	    UserBean other = (UserBean) obj;
	    return compareTo(other) == 0;
	}
	return false;
    }

    
}