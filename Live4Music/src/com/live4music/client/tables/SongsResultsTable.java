package com.live4music.client.tables;

import java.util.*;

/**
 * 
 * Songs table data structure
 * 
 * holds all songs (by SongItem class) for the specified album id
 * for each song it holds:
 * - track (key)
 * - song name
 * - song artist
 * - length (in seconds)
 */
public class SongsResultsTable extends Table {
	
	private long albumID;
	private Map<Integer,SongsResultsTableItem> songs = new HashMap<Integer,SongsResultsTableItem>();
	
	/**
	 * constructor for a songs results table
	 * @param albumID
	 */
	public SongsResultsTable(long albumID){
		super("SongsResultsTable");
		this.albumID = albumID;
	}
	
	/**
	 * constructor for a songs results table with songs list
	 * @param albumID
	 * @param variousArtists
	 * @param songs
	 */
	public SongsResultsTable(long albumID, HashMap<Integer,SongsResultsTableItem> songs){
		super("SongsResultsTable");
		this.albumID = albumID;
		this.songs = songs;
	}
	
	/**
	 * adder for song item
	 * @param track
	 * @param s
	 */
	public void addSong(SongsResultsTableItem s){
		songs.put(s.getTrack(), s);
	}
	
	/**
	 * detailed adder for song item
	 * @param track
	 * @param name
	 * @param artist
	 * @param length
	 */
	public void addSong(int track, String name, String artist, int length){
		SongsResultsTableItem s = new SongsResultsTableItem(track,name,artist,length);
		songs.put(track, s);
	}
		
	/**
	 * song getter by track number
	 * @param track
	 * @return
	 */
	public SongsResultsTableItem getSong(int track){
		return songs.get(track);
	}
	
	/**
	 * getter for the songs list
	 * @return
	 */
	public Map<Integer,SongsResultsTableItem> getSongs(){
		return songs;
	}
	
	/**
	 * getter for the album ID to which this songs table belongs
	 * @return
	 */
	public long getAlbumID(){
		return this.albumID;
	}
}
