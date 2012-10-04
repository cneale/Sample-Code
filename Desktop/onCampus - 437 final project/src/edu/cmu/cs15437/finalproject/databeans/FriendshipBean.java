package edu.cmu.cs15437.hw4.databeans;

public class FriendshipBean{
    private String userA = null;
    private String userB = null;
    private int id = -1;
    
	public FriendshipBean() { 
    }

    public int getId() { return id; }
    public void setId(int id){ this.id = id; }
    public String getUserA(){return userA;}
    public void setUserA(String user){userA = user;}
    public String getUserB(){return userB;}
    public void setUserB(String user){userB = user;}
}