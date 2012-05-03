package com.live4music.client.tables;

/**
 * created by Ariel
 * 
 * enum for the employees positions in the employees table
 */
public enum EmployeePositionsEnum {

	NETWORK_MANAGER(0,"Network manager"),
	MANAGER(1,"Store manager"),
	ASSIST_MANAGER(2,"Assist manager"),
	SALESMAN(3,"Salesman");
	
	private int intRepresentation;
	private String strRepresentation;
	
	/**
	 * private constructor
	 * @param intRep
	 * @param strRep
	 */
	private EmployeePositionsEnum(int intRep, String strRep){
		this.intRepresentation = intRep;
		this.strRepresentation = strRep;
	}
	
	/**
	 * getter for the integer representation
	 * @return
	 */
	public int getIntRep(){
		return this.intRepresentation;
	}
	
	/**
	 * getter for the string representation
	 * @return
	 */
	public String getStrRep(){
		return this.strRepresentation;
	}
	
	/**
	 * A static method that converts the integer i to its EmployeePositionsEnum enum value
	 * @param i
	 * @return
	 */
	public static EmployeePositionsEnum convertFromInt(int i){
		return values()[i];
	}
}
