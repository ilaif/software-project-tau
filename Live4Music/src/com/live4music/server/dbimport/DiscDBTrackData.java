package com.live4music.server.dbimport;

public class DiscDBTrackData {

	private String 	title;
	private String 	artist;
	private int		lengthSdc;
	private	String	name;
	private	int		trackNum;
	
	
	// C'tor	
	public DiscDBTrackData(int trackNum, String trackTitle, int trackLengthSec)
	{	
		this.trackNum = trackNum;
		this.title = trackTitle;	
		this.lengthSdc = trackLengthSec;
								
		parseTrackTitle();
		
		this.artist = this.artist.replace("'", "''");
		this.title = this.title.replace("'", "''");
		this.name = this.name.replace("'", "''");		
	}
	
	
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public int getLengthSdc() {
		return lengthSdc;
	}
	public void setLengthSdc(int lengthSdc) {
		this.lengthSdc = lengthSdc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTrackNum() {
		return trackNum;
	}
	public void setTrackNum(int trackNum) {
		this.trackNum = trackNum;
	}
	
	public	boolean	hasArtist()
	{
		return ((this.artist.length() > 0) && (this.artist != Constants.NOT_AVAILABLE));
	}		
	
	
	/** Try to get track's name and artist, if the title is in the format of:
	 *		artist / name
	 *		possible other titles:
	 *		artist1 / artist2 / ... / artistN / track name
	 */	
	public	void	parseTrackTitle()	
	{		
		this.name = "";
		this.artist = Constants.NOT_AVAILABLE;
		if (this.title.indexOf(Constants.TITLE_DELIMITER) != -1)
		{		
			String[] artistAndName = 	this.title.split(Constants.TITLE_DELIMITER);
			if (artistAndName.length == 2)
			{
				this.artist = artistAndName[0].trim();
				this.name = artistAndName[1].trim();	
			}						
		}
		// No slash, no artist defined for track	
		else
		{
			this.name = this.title;	
//			Debug.log("DiscDBParser::parseTarFile: INFO - no slash title: " + this.title);								
		}		
	}
	
	
	public	boolean isValid()
	{
		// valid track is of from: artist / name (or it doesn't contain a " / ")
		if (this.title.indexOf(Constants.TITLE_DELIMITER) != -1)
		{		
			String[] artistAndName = 	this.title.split(Constants.TITLE_DELIMITER);
			if (artistAndName.length != 2)
			{
				return false;
			}
		}		
		return ((this.name.length() > 0) && 
				(this.name.length() <= Constants.MAX_TRACK_NAME_LENGTH) &&
				(this.artist.length() <= Constants.MAX_ARTIST_NAME_LENGTH) &&
				(this.artist.length() > 0));
	}
	
	
	public	String	toString()
	{
		String ret = this.trackNum + ". Title: " + this.title;

		if (this.name != null)
		{
			ret += "   Name: " + this.name;
		}
		else
		{
			ret += "   Name: " + "N/A";
		}

		if (this.artist != null)
		{
			ret += "   Artist: " + this.artist;
		}
		else
		{
			ret += "   Artist: " + "N/A";
		}

		ret += "   length: " + this.lengthSdc + " seconds\n";
		return ret;
	}
}
