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
			
			String selectPart = "SELECT Albums.album_id, " +
					"Albums.album_name, " +
					"artists.artist_name, " +
					"Albums.year, " +
					"genres.genre_name, " +
					"Albums.length_sec, " +
					"Albums.price\n";
			String fromPart = "FROM Albums, artists, genres";
			String wherePart = "WHERE Albums.artist_id = artists.artist_id AND\n" +
					"Albums.genre_id = genres.genre_id"; 
				
			if (albumSearchQuery.isByAlbumID()){
				wherePart += " AND\n" +
						"Albums.album_id = " + albumSearchQuery.getAlbumID();
			} else {
				if (albumSearchQuery.hasAlbumName()){
					wherePart += " AND\n" +
							"LOWER(Albums.album_name) LIKE '%" + albumSearchQuery.getAlbumName()+"%'";
					
				}
				if (albumSearchQuery.hasArtist()) {
					wherePart += " AND\n" +
							"LOWER(artists.artist_name) LIKE '%" + albumSearchQuery.getArtist()+"%'";
				}
				if (albumSearchQuery.hasGenres()) {
					wherePart += " AND\n(";
					boolean firstGenre = true;
					for (int i = 0;i < albumSearchQuery.getGenresArr().length;i++){
						if (albumSearchQuery.getGenresArr()[i]){
							if (firstGenre) {
								firstGenre = false;
							}else {						
								wherePart += " OR\n";
							}
							
							wherePart += "genres.genre_name LIKE '%" + AlbumSearchQuery.getGenreNames()[i] + "%'";
						}
					}
					if (albumSearchQuery.hasOtherGenre()){
						wherePart += " OR\n" +
								"genres.genre_name LIKE '%" + albumSearchQuery.getOtherGenre()+"%'";
					}
					
					wherePart += ")\n";
				} else {
					if (albumSearchQuery.hasOtherGenre()){
						wherePart += " AND\n" +
								"genres.genre_name LIKE '%" + albumSearchQuery.getOtherGenre()+"%'";
					}
				}
				
				if (albumSearchQuery.hasYear()){
					wherePart += " AND\n" +
							"Albums.year <= " + albumSearchQuery.getYearTo() + " AND\n" +
							"Albums.year >= " + albumSearchQuery.getYearFrom();
				}
				if (albumSearchQuery.hasSongNames()){
					int i = 0;
					for (String songName : albumSearchQuery.getSongNames().split(";")) {
						fromPart += ", Songs s"+i;
						wherePart += " AND\n" +
								"Albums.Album_id = s"+i+".album_id";						
						wherePart += " AND\n" +
								"LOWER(s"+i+".song_name) LIKE '%" + songName + "%'";
						i++;
					}
				}
				
				if (albumSearchQuery.getStockOption() == AlbumSearchStockOptionEnum.STORE){
					fromPart += ", Stock";
					wherePart += " AND\n" +
							"Albums.album_id = Stock.album_id AND\n" +
							"Stock.store_id = " + StaticProgramTables.thisStore.getStoreID();
				} else if (albumSearchQuery.getStockOption() == AlbumSearchStockOptionEnum.NETWORK){
					fromPart += ", Stock";
					wherePart += " AND\n" +
							"Albums.album_id = Stock.album_id";
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
			
			String songsQuery = "SELECT Songs.track_num, Songs.song_name, artists.artist_name, Songs.length_sec\n";
			songsQuery += "FROM Songs, Artists\n";
			songsQuery += "WHERE Songs.album_id = " + albumID + " AND\n" +
					"Songs.artist_id = artists.artist_id\n";
			songsQuery += "ORDER BY Songs.track_num";
			
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
