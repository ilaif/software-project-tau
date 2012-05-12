package com.live4music.server.queries;


import org.eclipse.swt.widgets.*;

import com.live4music.client.ui.Main;
import com.live4music.client.ui.MainFuncs;

/**
 * 
 * Album search query data structure
 * 
 * holds album search query information:
 * - option #1: By album id:
 * 	* album ID
 * - option #2: By other parameters (all optional)
 * 	* album name
 *	* artist
 *	* year (from / to)
 *	* song names
 *	* genres (10 defaults and another one for other genre)
 * construction throws QueryErrorException in case the query was not defined well by the user
 */
public class AlbumSearchQuery extends Query {
	// class fields: correspond to the album search query fields
	private boolean isByAlbumID; // is this search done by album ID
	private long albumID = 0;
	
	private boolean hasAlbumName = false;
	private String albumName = null;
	
	private boolean hasArtist = false;
	private String artist = null;
	
	private boolean hasYear = false;
	private int yearFrom = -1;
	private int yearTo = -1;
	
	private boolean hasSongNames = false;
	private String songNames = null;
	
	private boolean hasGenres = false;
	private boolean[] genresArr = new boolean[6];
	private boolean hasOtherGenre = false;
	private String otherGenre = null;
	
	private AlbumSearchStockOptionEnum stockOption = AlbumSearchStockOptionEnum.ALL;

	private static String[] genreNames = {"rock", "jazz", "pop", "blues",
		"world", "hip hop", "electronic", "Classical"};
	private static int thisYear = 2010; // holds the current year
	
	/**
	 * constructor for new album search query
	 * @throws QueryErrorException: in case the query fields are not initialized legally
	 */
	public AlbumSearchQuery() throws QueryErrorException{
		super("AlbumSearchQuery");
		this.isByAlbumID = Main.getSearchBulletByAlbum().getSelection();
		
		// in case the search is by album id
		if (this.isByAlbumID){
			// set the album id field
			QueryErrorException qee = new QueryErrorException("Album ID must be an integer greater than 0",this.getQueryType());
			String albumIDText = Main.getSearchTextBoxAlbumID().getText();
			
			if (albumIDText == null || albumIDText.length() == 0) throw qee; // the album id text is null or empty
			try{
				long albumID = Long.parseLong(albumIDText);
				if (albumID <= 0) throw qee; // the album id text is an integer <= 0
				else this.albumID = albumID; // SET ALBUM ID
			}catch(NumberFormatException nfe){throw qee;} // the album id text is not an integer
			// set all other parameters fields - all are set off by default
		}
		
		// in case the search is by other parameters
		else {
			// check that every selected parameter has a non empty text box
			// album name
			this.hasAlbumName = Main.getSearchCheckBoxAlbumName().getSelection();
			if (hasAlbumName){
				String albumNameText = Main.getSearchTextBoxAlbumName().getText();
				albumNameText = MainFuncs.replaceSubString(albumNameText, "'", "''");
				if (albumNameText == null || albumNameText.length() == 0) // album name text is empty
					throw new QueryErrorException("Album name option selected, field must have value", this.getQueryType());
				else this.albumName = albumNameText.toLowerCase();
			}
			
			// artist
			this.hasArtist = Main.getSearchCheckBoxArtist().getSelection();
			if (hasArtist){
				String artistText = Main.getSearchTextBoxArtist().getText();
				artistText = MainFuncs.replaceSubString(artistText, "'", "''");
				if (artistText == null || artistText.length() == 0) // artist name text is empty
					throw new QueryErrorException("Artist option selected, field must have value", this.getQueryType());
				else this.artist = artistText.toLowerCase();
			}
			
			// year from / to
			this.hasYear = Main.getSearchCheckBoxYear().getSelection();
			if (hasYear){
				String yearFromText = Main.getSearchTextBoxYearFrom().getText();
				String yearToText = Main.getSearchTextBoxYearTo().getText();
				QueryErrorException qee = new QueryErrorException("Year option selected, fields must have "
						+"integer values between 1900 and "+thisYear+" with 'from' year less than or equals "
						+"'to' year", this.getQueryType());
				
				if (yearFromText == null || yearFromText.length() == 0 ||
						yearToText == null || yearToText.length() == 0) throw qee; // year from / to text is empty
				else {
					try{
						int fromYear = Integer.parseInt(yearFromText);
						int toYear =  Integer.parseInt(yearToText);
						if ((toYear < 1900) || (thisYear < toYear) ||
							(fromYear < 1900) || (thisYear < fromYear) || (toYear < fromYear)) // years limits are illegal
							throw qee;
						else { // SET ALBUM YEAR FROM AND YEAR TO
							this.yearFrom = fromYear;
							this.yearTo = toYear;
						}
					} catch (NumberFormatException nfe){throw qee;} // year from / to text is not an integer
				}
			}
			
			// song names
			this.hasSongNames = Main.getSearchCheckBoxSongNames().getSelection();
			if (hasSongNames){
				String songNamesText = Main.getSearchTextBoxSongNames().getText();
				songNamesText = MainFuncs.replaceSubString(songNamesText, "'", "''");
				if (songNamesText == null || songNamesText.length() == 0) // song names text is empty
					throw new QueryErrorException("Song names option selected, field must have value", this.getQueryType());
				else this.songNames = songNamesText.toLowerCase();
			}
			
			// genres
			this.hasGenres = Main.getSearchCheckBoxGenres().getSelection();
			if (hasGenres){
				boolean anySelected = false;
				Button[] genres = Main.getSearchCheckBoxGenresArr();
				for (Button g: genres) anySelected = anySelected || g.getSelection();
				anySelected = anySelected || Main.getSearchCheckBoxGenreOther().getSelection();
				
				if (!anySelected) throw new QueryErrorException("Genres option selected, at least one genre must be selected",
						this.getQueryType()); // no genres are selected
				
				// SET GENRES ARRAY
				for (int i = 0; i < this.genresArr.length; i++){
					genresArr[i] = genres[i].getSelection();
				}
				
				String otherGenreText = Main.getSearchTextBoxGenreOther().getText();
				otherGenreText = MainFuncs.replaceSubString(otherGenreText, "'", "''");
				if (Main.getSearchCheckBoxGenreOther().getSelection() &&
						(otherGenreText.length() == 0 || otherGenreText == null))
					throw new QueryErrorException("Other genre option selected, field must have value", this.getQueryType()); // in case other genre selected but has no value
				else { // SET OTHER GENRE
					this.otherGenre = otherGenreText.toLowerCase();
					this.hasOtherGenre = Main.getSearchCheckBoxGenreOther().getSelection();
				}
			}
			
			// finally check if none option is selected, then throw error
			if (!(hasAlbumName || hasArtist || hasYear || hasSongNames || hasGenres))
				throw new QueryErrorException("Must specify at least one album parameter", this.getQueryType());
		}
		
		// set stock option
		if (Main.getSearchBulletInStockInNetwork().getSelection()){
			this.stockOption = AlbumSearchStockOptionEnum.NETWORK;
		} else if (Main.getSearchBulletInStockInStore().getSelection()){
			this.stockOption = AlbumSearchStockOptionEnum.STORE;
		} // else it is the "All" option that is selected by default
	}
	
	///////////////////////////
	//	Getters and setters	 //
	///////////////////////////

	public boolean isByAlbumID() {
		return isByAlbumID;
	}

	public long getAlbumID() {
		return albumID;
	}

	public boolean hasAlbumName() {
		return hasAlbumName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public boolean hasArtist() {
		return hasArtist;
	}

	public String getArtist() {
		return artist;
	}

	public boolean hasYear() {
		return hasYear;
	}

	public int getYearFrom() {
		return yearFrom;
	}

	public int getYearTo() {
		return yearTo;
	}

	public boolean hasSongNames() {
		return hasSongNames;
	}

	public String getSongNames() {
		return songNames;
	}

	public boolean hasGenres() {
		return hasGenres;
	}

	public boolean[] getGenresArr() {
		return genresArr;
	}

	public boolean hasOtherGenre() {
		return hasOtherGenre;
	}

	public String getOtherGenre() {
		return otherGenre;
	}

	public static String[] getGenreNames() {
		return genreNames;
	}
	
	public AlbumSearchStockOptionEnum getStockOption() {
		return stockOption;
	}
}