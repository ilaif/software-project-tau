package com.live4music.client.tables;

import java.util.*;

/**
 * 
 * Albums search results table data structure
 * 
 * holds all AlbumItem tuples search results held by their album ID
 */
public class AlbumsResultsTable extends Table {
	private Map<Long,AlbumsResultsTableItem> albums = new HashMap<Long,AlbumsResultsTableItem>();
	
	/**
	 * constructor for the albums search results table
	 */
	public AlbumsResultsTable(){
		super("AlbumsResultsTable");
	}
	
	/**
	 * constructor for the albums search results table
	 * with the album results map as input
	 * @param albums
	 */
	public AlbumsResultsTable(HashMap<Long,AlbumsResultsTableItem> albums){
		super("AlbumsResultsTable");
		this.albums = albums;
	}
	
	//////////////////////////
	//	adders and getters	//
	//////////////////////////
	
	/**
	 * album adder to the albums search results map
	 * @param albumID
	 * @param album
	 */
	public void addAlbum(AlbumsResultsTableItem album){
		albums.put(album.getAlbumID(), album);
	}
	
	/**
	 * album adder to the albums search results map
	 * with explicit album details
	 * @param albumID
	 * @param albumName
	 * @param artist
	 * @param year
	 * @param genre
	 * @param length
	 * @param songs
	 * @param price
	 * @param storageLocation
	 * @param quantity
	 */
	public void addAlbum(long albumID, String albumName, String artist, int year, String genre,
			int length, SongsResultsTable songs, int price, long storageLocation, int quantity){
		AlbumsResultsTableItem album = new AlbumsResultsTableItem(albumID,albumName,artist,year,genre,
				length,songs,price,storageLocation,quantity);
		albums.put(albumID, album);
	}
	
	/**
	 * album item getter
	 * @param albumID
	 * @return
	 */
	public AlbumsResultsTableItem getAlbum(long albumID){
		return albums.get(albumID);
	}
	
	/**
	 * get albums results iterator
	 * @return
	 */
	public Map<Long,AlbumsResultsTableItem> getAlbums(){
		return albums;
	}
}
