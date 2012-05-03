package com.live4music.server.queries;

/**
 * created by Ariel
 * 
 * an abstract query class
 */
public abstract class Query {

	private String queryType;
	
	/**
	 * constructor
	 * @param type
	 */
	public Query(String type){
		this.queryType = type;
	}
	
	/**
	 * getter for the query type
	 * @return
	 */
	public String getQueryType(){
		return this.queryType;
	}
}
