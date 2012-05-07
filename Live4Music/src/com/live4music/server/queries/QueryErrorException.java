package com.live4music.server.queries;

/**
 * 
 * Exception class for queries that are not defined properly
 */
public class QueryErrorException extends Exception {
	
	private static final long serialVersionUID = -8974147697593531573L;
	
	private String queryType;
	
	public QueryErrorException(String message, String queryType){
		super(message);
		this.queryType = queryType;
	}
	
	/**
	 * returns the query type for the query generated this exception
	 * @return
	 */
	public String getQueryType(){
		return this.queryType;
	}
	
	@Override
	/**
	 * returns the string representation of the query error message
	 */
	public String toString(){
		return this.getMessage()+": "+this.getQueryType();
	}
}
