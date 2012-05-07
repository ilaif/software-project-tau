package com.live4music.client.tables;

/**
 * 
 * Album item data structure
 * 
 * holds all fields for a single album search result, to be inserted into an album search results table
 * also holds information relevant to sale and stock
 * contains the fields:
 * - album id
 * - album name
 * - artist
 * - year
 * - genre
 * - length (in seconds)
 * - songs list:
 * 	* song name
 * 	* artist
 * 	* length (in seconds)
 * additional information:
 * - price
 * - storage location
 * - quantity in stock
 */
public class AlbumsResultsTableItem {
	private long albumID = -1;
	private String albumName = null;
	private String artist = null;
	private int year = -1;
	private String genre = null;
	private int length = -1;
	private SongsResultsTable songs = null;
	
	private int price = -1;
	private long storageLocation = -1;	//TODO format yet to be determined
	private int quantity = -1;
	
	//////////////////////
	//	constructors	//
	//////////////////////
	
	/**
	 * constructor for an album result, to be entered to the album results table
	 */
	public AlbumsResultsTableItem(long albumID){
		this.albumID = albumID;
	}
	
	/**
	 * constructor with all album fields except songs list
	 * @param albumID
	 * @param albumName
	 * @param artist
	 * @param year
	 * @param genre
	 * @param length
	 */
	public AlbumsResultsTableItem(long albumID, String albumName, String artist,
			int year, String genre, int length, int price, long storageLocation, int quantity){
		this.albumID = albumID;
		this.albumName = albumName;
		this.artist = artist;
		this.year = year;
		this.genre = genre;
		this.length = length;
		this.price = price;
		this.storageLocation = storageLocation;
		this.quantity = quantity;
	}
	
	/**
	 * constructor with all album fields
	 * @param albumID
	 * @param albumName
	 * @param artist
	 * @param year
	 * @param genre
	 * @param length
	 * @param songs
	 */
	public AlbumsResultsTableItem(long albumID, String albumName, String artist, int year,
			String genre, int length, SongsResultsTable songs, int price, long storageLocation, int quantity){
		this.albumID = albumID;
		this.albumName = albumName;
		this.artist = artist;
		this.year = year;
		this.genre = genre;
		this.length = length;
		this.songs = songs;
		this.price = price;
		this.storageLocation = storageLocation;
		this.quantity = quantity;
	}

	/////////////////////////
	// getters and setters //
	/////////////////////////
	
	public long getAlbumID() {
		return albumID;
	}

	public void setAlbumID(long albumID) {
		this.albumID = albumID;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public SongsResultsTable getSongs() {
		return songs;
	}

	public void setSongs(SongsResultsTable songs) {
		this.songs = songs;
	}

	public int getPrice() {
		return price;
	}

	public long getStorageLocation() {
		return storageLocation;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setStorageLocation(long storageLocation) {
		this.storageLocation = storageLocation;
	}
}
