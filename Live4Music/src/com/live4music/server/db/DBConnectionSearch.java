package com.live4music.server.db;

import java.sql.*;

import com.live4music.client.tables.*;
import com.live4music.client.ui.*;
import com.live4music.server.queries.*;
import com.live4music.shared.general.*;
import com.live4music.shared.general.Debug.*;


public class DBConnectionSearch {
	
	public class GetAlbumsSearchResults implements Runnable {
		
		private AlbumSearchQuery albumSearchQuery;

		public GetAlbumsSearchResults(AlbumSearchQuery albumSearchQuery) {
			this.albumSearchQuery = albumSearchQuery;
		}		
		
		public void run() {
			Debug.log("DBConnectionSearch.GetAlbumsSearchResults thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			String selectPart = "SELECT distinct albums.album_id, " +
					"albums.album_name, " +
					"artists.artist_name, " +
					"albums.year, " +
					"genres.genre_name, " +
					"albums.length_sec, " +
					"albums.price\n";
			String fromPart = "FROM albums, artists, genres";
			String wherePart = "WHERE albums.artist_id = artists.artist_id AND\n" +
					"albums.genre_id = genres.genre_id"; 
				
			if (albumSearchQuery.isByAlbumID()){
				wherePart += " AND\n" +
						"albums.album_id = " + albumSearchQuery.getAlbumID();
			} else {
				if (albumSearchQuery.hasAlbumName()){
					wherePart += " AND\n" +
							"LOWER(albums.album_name) LIKE '%" + albumSearchQuery.getAlbumName()+"%'";
					
				}
				if (albumSearchQuery.hasArtist()) {
					wherePart += " AND\n" +
							"LOWER(artists.artist_name) LIKE '%" + albumSearchQuery.getArtist()+"%'";
				}
				
				if (albumSearchQuery.hasYear()){
					wherePart += " AND\n" +
							"albums.year <= " + albumSearchQuery.getYearTo() + " AND\n" +
							"albums.year >= " + albumSearchQuery.getYearFrom();
				}
				if (albumSearchQuery.hasSongNames()){
					int i = 0;
					for (String songName : albumSearchQuery.getSongNames().split(";")) {
						fromPart += ", songs s"+i;
						wherePart += " AND\n" +
								"albums.Album_id = s"+i+".album_id";						
						wherePart += " AND\n" +
								"LOWER(s"+i+".song_name) LIKE '%" + songName + "%'";
						i++;
					}
				}
				
				if (albumSearchQuery.getStockOption() == AlbumSearchStockOptionEnum.STORE){
					fromPart += ", stock";
					wherePart += " AND\n" +
							"albums.album_id = stock.album_id AND\n" +
							"stock.store_id = " + StaticProgramTables.thisStore.getStoreID();
				} else if (albumSearchQuery.getStockOption() == AlbumSearchStockOptionEnum.NETWORK){
					fromPart += ", stock";
					wherePart += " AND\n" +
							"albums.album_id = stock.album_id";
				}
			}
			
			fromPart += "\n";
			wherePart += "\n";
			
			DBQueryResults searchQueryResults = DBAccessLayer.executeQuery(selectPart+fromPart+wherePart);
			if (searchQueryResults == null){
				Debug.log("DBConnectionSearch.GetAlbumsSearchResults: [ERROR] got null pointer from search query");
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.SEARCH_FAILURE);
				return;
			}
			ResultSet rs = searchQueryResults.getResultSet();
			
			AlbumsResultsTable resultsTable = new AlbumsResultsTable();
			int numOfResults = 0;
			
			try {
				while (rs.next()){
					if (numOfResults < StaticProgramTables.maxNumOfResults)
						resultsTable.addAlbum(rs.getInt("album_id"), 
								rs.getString("album_name"), 
								rs.getString("artist_name"),
								rs.getInt("year"),
								rs.getString("genre_name"), 
								rs.getInt("length_sec"),
								new SongsResultsTable(rs.getInt("album_id")),
								rs.getInt("price"),
								-1,
								-1);
					numOfResults++;
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionSearch.GetAlbumsSearchResults: [ERROR] SQLException in RS traversal");
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.SEARCH_FAILURE);
				searchQueryResults.close();
				return;
			}
			
			searchQueryResults.close();
			
			GuiUpdatesInterface.updateAlbumResultsTable(resultsTable,numOfResults);
		}		
	}
	
	public class GetSongsResults implements Runnable{
		long albumID;
		public GetSongsResults(long albumID) {
			this.albumID = albumID;
		}
		
		public void run() {
			Debug.log("DBConnectionSearch.GetSongsResults thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			String songsQuery = "SELECT songs.track_num, songs.song_name, artists.artist_name, songs.length_sec\n";
			songsQuery += "FROM songs, artists\n";
			songsQuery += "WHERE songs.album_id = " + albumID + " AND\n" +
					"songs.artist_id = artists.artist_id\n";
			songsQuery += "ORDER BY songs.track_num";
			
			DBQueryResults songsQueryResults = DBAccessLayer.executeQuery(songsQuery);
			if (songsQueryResults == null){
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.SEARCH_FAILURE);
				return;
			}
			ResultSet rs = songsQueryResults.getResultSet();

			SongsResultsTable resultsTable = new SongsResultsTable(albumID);
			
			try {
				while (rs.next()){
					resultsTable.addSong(rs.getInt("track_num"),
							rs.getString("song_name"),
							rs.getString("artist_name"),
							rs.getInt("length_sec"));
				}
			} catch (SQLException e) {
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.SEARCH_FAILURE);
				songsQueryResults.close();
				return;
			}
			
			songsQueryResults.close();
			
			GuiUpdatesInterface.updateSongsResultsTable(albumID, resultsTable);
		}
	}
}
