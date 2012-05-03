package com.live4music.server.queries;

/**
 * created by Ariel
 * 
 * enum for the stock search option in the album search query
 */
public enum AlbumSearchStockOptionEnum {

	ALL(0, "All"),
	NETWORK(1,"Network"),
	STORE(2,"Store");
	
	private int intRepresentation;
	private String strRepresentation;
	
	/**
	 * private constructor
	 * @param intRep
	 * @param strRep
	 */
	private AlbumSearchStockOptionEnum(int intRep, String strRep){
		intRepresentation = intRep;
		strRepresentation = strRep;
	}
	
	/**
	 * return the int representation for the enum
	 * to be used in the database query
	 * @return
	 */
	public int getIntRep(){
		return this.intRepresentation;
	}
	
	/**
	 * return the string representation of the enum
	 * @return
	 */
	public String getStrRep(){
		return this.strRepresentation;
	}
	
	@Override
	public String toString(){
		return this.strRepresentation;
	}
}
