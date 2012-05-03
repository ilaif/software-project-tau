package com.live4music.client.tables;

/**
 * created by Ariel
 * 
 * enum for the order status used in the stock orders and requests tables
 */
public enum OrderStatusEnum {
	
	WAITING(0,"Waiting"),
	COMPLETED(1,"Completed"),
	DENIED(2,"Denied"),
	CANCELED(3,"Canceled");
	
	private int intRepresentation;
	private String strRepresentation;
	
	/**
	 * private constructor
	 * @param intRep
	 * @param strRep
	 */
	private OrderStatusEnum(int intRep, String strRep){
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
	 * A static method that converts the integer i to its OrderStatusEnum enum value
	 * @param i
	 * @return
	 */
	public static OrderStatusEnum convertFromInt(int i){
		return values()[i];
	}
}
