package edu.cmu.cs15437.hw4.databeans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MessageBean implements Comparable<MessageBean>{

    private String user;
	private String sender;
	private String subject;
    private String message;
	private boolean request;
	private int id = -1;

    public MessageBean(String sender) { 
		this.sender = sender;
		}
		
	public MessageBean(int id) { 
		this.id = id;
    }	
	
    public String getUserId()         { return user;          }
    public String getSender()        { return sender;          }
	public String getSubject()        { return subject;          }
    public String getMessage()       { return message;          }
	public boolean getRequest()        { return request;          }
	public int getId()        { return id;          }
	
    public void setUserId(String s)  { user = s;             }
	public void setSender(String s)   { sender = s;          }
	public void setSubject(String s)  { subject = s;             }
	public void setMessage(String s) { message = s;             }
	public void setRequest(boolean s)  { request = s;             }
	public void setId(int s)  { id = s;             }
	
	public int compareTo(MessageBean other) {
	// Order by message id
		if (other.id > id)
			return -1;
		else return 1;
    }
	
    public boolean equals(Object obj) {
	if (obj instanceof MessageBean) {
	    MessageBean other = (MessageBean) obj;
	    return compareTo(other) == 0;
	}
	return false;
	}
}
