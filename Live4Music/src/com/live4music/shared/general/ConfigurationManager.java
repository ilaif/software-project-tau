package com.live4music.shared.general;

import java.util.*;
import java.io.*;

public class ConfigurationManager {

	String 	username;
	String	password;
	String	SID;
	String	host;
	int		port;
	int		maxDBConnections;
	String	OS;
	
	
	public ConfigurationManager(String fileName) {
		loadConfiguration(fileName);
	}
	
	
	public boolean loadConfiguration(String fileName) {
		try	{
			Properties p = new Properties();
			p.load(new FileInputStream(fileName));
			
			setUsername(p.getProperty("DBUser"));
			setPassword(p.getProperty("DBPassword"));
			setSID(p.getProperty("SID"));
			setHost(p.getProperty("DBHost"));
			setPort(Integer.parseInt(p.getProperty("DBPort")));
			setMaxDBConnections(Integer.parseInt(p.getProperty("MaxDBConnections")));
			
		}
	    catch (Exception e) {
	        System.out.println(e);
	        return false;
	    }		
	    return true;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getSID() {
		return SID;
	}


	public void setSID(String sid) {
		SID = sid;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public int getMaxDBConnections() {
		return maxDBConnections;
	}


	public void setMaxDBConnections(int maxDBConnections) {
		this.maxDBConnections = maxDBConnections;
	}
}
