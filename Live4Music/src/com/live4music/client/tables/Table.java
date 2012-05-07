package com.live4music.client.tables;

/**
 * 
 * abstract class for all tables in the program (not the DB tables!)
 */
public abstract class Table {
	
	private String tableType;
	
	/**
	 * constructor for the table
	 * @param type
	 */
	public Table(String type){
		this.tableType = type;
	}
	
	public String getTableType(){
		return this.tableType;
	}
}
