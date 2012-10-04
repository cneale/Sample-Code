package edu.cmu.cs15437.hw4.databeans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class LocationBean {

    private String name;
	private int locationid;
    private int xcoordinate;
	private int ycoordinate;

    public LocationBean(String name) { 
		this.name = name;
		}

	public LocationBean(int id) { 
		this.locationid = id;
	}	
    public String getName()        { return name;          }
	public int getLocationId()         { return locationid;          }
    public int getX()       { return xcoordinate;          }
	public int getY()        { return ycoordinate;          }
	
    public void setLocationId(int s)  { locationid = s;             }
	public void setX(int s) { xcoordinate = s;             }
	public void setY(int s)  { ycoordinate = s;             }

}
