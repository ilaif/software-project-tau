package com.live4music.server.dbimport;

public class Constants {

	// field length constraints
	public static	final int		MAX_GENRE_LENGTH = 50;
	public static	final int		MAX_ARTIST_NAME_LENGTH = 150;
	public static	final int		MAX_ALBUM_NAME_LENGTH = 150;
	public static	final int		MAX_TRACK_NAME_LENGTH = 150;		
	
	public static final String DB_NULL_VALUE = "NULL";
	
	public	static	final	String	TITLE_DELIMITER = " " + "/" + " ";
	
	public	static	final	int		ALBUMS_BATCH_SIZE = 5000;
	public	static	final	int		MAX_ALBUMS_LIST_SIZE = 15000;
	
	public	static	final	String	UNKNOWN_GENRE = "unknown";
	
	public	static	final	String	NON_ASCII_REGEX	= "[^\\p{ASCII}]";
	
	// Min and max values for randomize
	public	static	final	int		MIN_ALBUM_PRICE = 30;
	public	static	final	int		MAX_ALBUM_PRICE = 120;	
	
	public	static	final	String	VARIOUS_ARTITSTS = "Various Artists";
	public	static	final	String	VARIOUS_ARTITSTS_INDICATOR1 = "various";
	public	static	final	String	VARIOUS_ARTITSTS_INDICATOR2 = "va.";
	
	public static final 	String	 NOT_AVAILABLE = "N/A";
	
}
