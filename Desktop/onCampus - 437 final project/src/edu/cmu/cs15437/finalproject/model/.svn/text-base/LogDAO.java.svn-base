package edu.cmu.cs15437.addrbook2.model;

import javax.servlet.http.HttpServletRequest;

import org.mybeans.dao.DAOException;

public abstract class LogDAO {
	public static LogDAO getInstance() {
		return new LogFactoryDAO();
	}
	
	public static LogDAO getFileInstance(String fileName) {
		return new LogFileDAO(fileName);
	}
	
    public abstract void write(HttpServletRequest request, String text) throws DAOException;
}