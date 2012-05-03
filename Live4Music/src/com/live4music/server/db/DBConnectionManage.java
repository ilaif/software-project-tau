package com.live4music.server.db;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.live4music.client.tables.*;
import com.live4music.client.ui.*;
import com.live4music.server.dbimport.DiscDBAlbumData;
import com.live4music.server.dbimport.DiscDBParser;
import com.live4music.server.dbimport.DiscDBTrackData;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;


public class DBConnectionManage {
	
	public class GetEmployeesTable implements Runnable{
		
		public void run() {
			Debug.log("DBConnectionManage.GetEmployeesTable thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			String employeesQuery =
				"SELECT *\n"+
				"FROM Employees\n"+
				"WHERE store_id = " + StaticProgramTables.thisStore.getStoreID();
			
			DBQueryResults queryResults = DBAccessLayer.executeQuery(employeesQuery);
			if (queryResults == null){
				Debug.log("DBConnectionInterface.GetEmployeesTable [ERROR]: Got NULL from query");
				
				GuiUpdatesInterface.initEmployeesTable(new EmployeesTable(StaticProgramTables.thisStore.getStoreID()));
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_EMP_FAILURE);

				return;
			}
			ResultSet rs = queryResults.getResultSet();
			
			EmployeesTable employees = new EmployeesTable(StaticProgramTables.thisStore.getStoreID());
			
			try {
				while (rs.next()){
					employees.addEmployee(rs.getInt("Employee_id"), 
							rs.getString("First_name"),
							rs.getString("Last_name"),
							toGUIDate(rs.getDate("Hire_date")),
							toGUIDate(rs.getDate("Birth_date")),
							rs.getString("Address"),
							rs.getString("Phone_Number"), 
							rs.getString("Cell_phone_number"), 
							EmployeePositionsEnum.convertFromInt(rs.getInt("Position")));
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionInterface.GetEmployeesTable [ERROR]: Failed extracting employees from ResultSet");
				Debug.log(e.getStackTrace().toString());
				
				GuiUpdatesInterface.initEmployeesTable(new EmployeesTable(StaticProgramTables.thisStore.getStoreID()));
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_EMP_FAILURE);
				
				queryResults.close();
				return;
			}
			
			queryResults.close();
			GuiUpdatesInterface.initEmployeesTable(employees);
		}
		
		private String toGUIDate(Date d){
			String[] dateArr = d.toString().split("-");
			return dateArr[2]+"/"+dateArr[1]+"/"+dateArr[0];
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public class CheckIfEmployeeExists implements Runnable{
		private int employeeID;

		public CheckIfEmployeeExists(int employeeID) {
			super();
			this.employeeID = employeeID;
		}
		
		
		public void run() {
			Debug.log("DBConnectionManage.CheckIfEmployeeExists thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			String employeeExistsQuery =
				"SELECT *\n"+
				"FROM Employees\n"+
				"WHERE employee_id = " + employeeID;
			
			DBQueryResults queryResults = DBAccessLayer.executeQuery(employeeExistsQuery);
			if (queryResults == null){
				Debug.log("DBConnectionInterface.CheckIfEmployeeExists [ERROR]: Got NULL from query");
				
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INSERT_SAVE_EMP_FAILURE);
				return;
			}
			ResultSet rs = queryResults.getResultSet();
			
			boolean empExists = false;
			
			try {
				empExists = rs.next();
			} catch (SQLException e) {
				Debug.log("DBConnectionInterface.InitDBConnection [ERROR]: Failed extracting employee from ResultSet");
				Debug.log(e.getStackTrace().toString());
				
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INSERT_SAVE_EMP_FAILURE);
				queryResults.close();
				
				return;	
			}
			queryResults.close();
			
			GuiUpdatesInterface.tryInsertNewEmployee(employeeID,empExists);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public class InsertUpdateEmployee implements Runnable{
		private EmployeesTableItem employee;

		public InsertUpdateEmployee(EmployeesTableItem employee) {
			super();
			this.employee = employee;
		}
		
		
		public void run() {
			Debug.log("DBConnectionManage.InsertUpdateEmployee thread is started",DebugOutput.FILE,DebugOutput.STDOUT);

			String employeeRemove = 
				"DELETE FROM Employees\n"+
				"WHERE employee_id = " + employee.getEmployeeID();

			String employeeInsert = 
				"INSERT INTO Employees(" +
				"employee_id, " +
				"first_name, " +
				"last_name, " +
				"Hire_Date, " +
				"Birth_Date, " +
				"address, " +
				"Phone_Number, " +
				"cell_phone_Number, " +
				"store_id, " +
				"position)\n" +
				
				"VALUES(" +
				employee.getEmployeeID()+"," +
				"'"+employee.getFirstName()+"'," +
				"'"+employee.getLastName()+"'," +
				"TO_DATE('"+employee.getEmploymentDate()+"','DD/MM/YYYY')"+","+
				"TO_DATE('"+employee.getBirthDate()+"','DD/MM/YYYY')"+","+
				"'"+employee.getAddress()+"',"+
				"'"+employee.getPhone()+"',"+
				"'"+employee.getCellPhone()+"',"+
				employee.getStoreID()+","+
				employee.getPosition().getIntRep()+")";
			
			List<String> sqlCommands = new ArrayList<String>();
			sqlCommands.add(employeeRemove);
			sqlCommands.add(employeeInsert);
			
			if (employee.getPosition() == EmployeePositionsEnum.MANAGER){
				String storeManagerUpdate = "UPDATE Stores\n" +
					"SET manager_id = " + employee.getEmployeeID() + "\n" +
					"WHERE store_id = " + StaticProgramTables.thisStore.getStoreID();
				sqlCommands.add(storeManagerUpdate);
			}

			
			if (DBAccessLayer.executeCommandsAtomic(sqlCommands) != sqlCommands.size()){
				Debug.log("DBConnectionInterface.InsertUpdateEmployee [ERROR]: Failed Removing and Updating Employees");

				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INSERT_SAVE_EMP_FAILURE);
				return;
			}

			GuiUpdatesInterface.insertUpdateEmployee(employee);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public class RemoveEmployee implements Runnable{
		private int employeeID;

		public RemoveEmployee(int employeeID) {
			super();
			this.employeeID = employeeID;
		}
		
		
		public void run() {
			Debug.log("DBConnectionManage.RemoveEmployee thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			String employeeRemove = "DELETE FROM Employees\n" +
					"WHERE employee_id = "+employeeID;

			if (DBAccessLayer.executeUpdate(employeeRemove) == -1){
				Debug.log("DBConnectionInterface.InitDBConnection [ERROR]: Failed Removing Employee");
				
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INSERT_SAVE_EMP_FAILURE);
				return;
			}
			
			GuiUpdatesInterface.removeEmployee(employeeID);
		}		
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public class UpdateDatabase implements Runnable{
		
		private class ParseFile implements Runnable{
			private String filename;
			private boolean finishedSuccessfully = false;
			
			public ParseFile(String filename) {
				this.filename = filename;
			}

			
			public void run() {
				try {
					DiscDBParser.parseFile(filename);
				} catch (IOException e) {
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					return;	
				}
				finishedSuccessfully = true;
			}

			public boolean isFinishedSuccessfully() {
				return finishedSuccessfully;
			}
			
			
			
		}
		
		////////////////////////////////////////////////
		
		private class BatchAddToDB implements Runnable{
			private Thread parseThread;
			private boolean finishedSuccessfully = false;
			private Map<String,Integer> genres = new HashMap<String,Integer>();
			private Map<String,Integer> artistNames= new HashMap<String,Integer>();
			private int albumID;
			private int nextGenreID;
			private int nextArtistID;
			
			public BatchAddToDB(Thread parseThread) {
				this.parseThread = parseThread;
				Debug.log("DBConnectionManage.BatchAddToDB: access DB to get all current genres");
				DBQueryResults genresQueryResults = DBAccessLayer.executeQuery("SELECT genre_id, genre_name FROM genres");
				if (genresQueryResults == null) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: failed while getting genres from DB");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					return;
				}
				
				ResultSet genresRS = genresQueryResults.getResultSet();
				try {
					while (genresRS.next()){
						genres.put(genresRS.getString("genre_name"), genresRS.getInt("genre_id"));
					}
				} catch (SQLException e) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: RS failure while getting genres");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					genresQueryResults.close();
					return;
				}
				genresQueryResults.close();
				
				
				Debug.log("DBConnectionManage.BatchAddToDB: access DB to get all current artist names");
				DBQueryResults artistNamesQueryResults = DBAccessLayer.executeQuery("SELECT artist_id, artist_name FROM artists");
				if (artistNamesQueryResults == null) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: failed while getting artists from DB");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					return;
				}
				
				ResultSet artistsRS = artistNamesQueryResults.getResultSet();
				try {
					while (artistsRS.next()){
						artistNames.put(artistsRS.getString("artist_name"), artistsRS.getInt("artist_id"));
					}
				} catch (SQLException e) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: RS failure while getting artists");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					artistNamesQueryResults.close();
					return;
				}
				artistNamesQueryResults.close();
				
				
				// Get last album id from DB.
				Debug.log("DBConnectionManage.BatchAddToDB: access DB to get last album_id");
				DBQueryResults maxAlbumID = DBAccessLayer.executeQuery("SELECT max(album_id) AS max_id FROM Albums");
				if (maxAlbumID == null) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: failed while attempting to get max album_id");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					return;
				}
				ResultSet albumIDRS = maxAlbumID.getResultSet();
				try {
					if (albumIDRS.next()) albumID = albumIDRS.getInt("max_id")+1;
				} catch (SQLException e) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: RS failure while getting max album_id");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					maxAlbumID.close();
					return;
				}
				maxAlbumID.close();
				
				// Get last genre id from DB.
				Debug.log("DBConnectionManage.BatchAddToDB: access DB to get last genre_id");
				DBQueryResults maxGenreID = DBAccessLayer.executeQuery("SELECT max(genre_id) AS max_id FROM genres");
				if (maxGenreID == null) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: failed while attempting to get max genre_id");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					return;
				}
				ResultSet genresIDRS = maxGenreID.getResultSet();
				try {
					if (genresIDRS.next()) nextGenreID = genresIDRS.getInt("max_id") + 1;
				} catch (SQLException e) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: RS failure while getting max genre_id");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					maxGenreID.close();
					return;
				}
				maxGenreID.close();

				// Get last artist id from DB.
				Debug.log("DBConnectionManage.BatchAddToDB: access DB to get last artist_id");
				DBQueryResults maxArtistID = DBAccessLayer.executeQuery("SELECT max(artist_id) AS max_id FROM artists");
				if (maxArtistID == null) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: failed while attempting to get max artist_id");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					return;
				}
				ResultSet artistIDRS = maxArtistID.getResultSet();
				try {
					if (artistIDRS.next()) nextArtistID = artistIDRS.getInt("max_id") + 1;
				} catch (SQLException e) {
					Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: RS failure while getting max artist_id");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
					finishedSuccessfully = false;
					maxArtistID.close();
					return;
				}
				maxArtistID.close();

			}

			
			public void run() {
				List<DiscDBAlbumData> parsedAlbums;
				while (parseThread.isAlive() || DiscDBParser.getCurrentAlbumListSize() > 0){
					int artistID;
					int genreID;
					
				// the code below was commented in by Rotem - 18/1/2010
				/*	int numToRemove = Math.min(DiscsDB.Constants.ALBUMS_BATCH_SIZE,DiscDBParser.getCurrentAlbumListSize());
					Debug.log("DBConnectionManage.BatchAddToDB read " + numToRemove + " albums");
					if (numToRemove == 0) {
						try {
							Thread.sleep(1000);
							continue;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					parsedAlbums = DiscDBParser.removeAllbumsDataFromList(numToRemove);*/
					
					/**
					 * this line handles it all:
					 * Tries to read ALBUMS_BATCH_SIZE data
					 * Waits till there is enough data to read, or parsing ends
					 * Reads data
					 * Signal the thread who inserts the data that the list size was decreased
					 */ 
					parsedAlbums = DiscDBParser.removeAllbumsDataFromList(com.live4music.server.dbimport.Constants.ALBUMS_BATCH_SIZE);
					Debug.log("DBConnectionManage.BatchAddToDB read " + parsedAlbums.size() + " albums");
					
					String genreInsertStatement = "INSERT INTO genres(genre_id, genre_name) VALUES(?,?)";
					List<FieldTypes> genreFieldTypes = new ArrayList<FieldTypes>();
					genreFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					genreFieldTypes.add(FieldTypes.FIELD_TYPE_STRING);
					
					List<Object> genreIDList = new ArrayList<Object>();
					List<Object> genreNameList = new ArrayList<Object>();
					
					List<List<Object>> genreArgumentsList = new ArrayList<List<Object>>();
					genreArgumentsList.add(genreIDList);
					genreArgumentsList.add(genreNameList);
					
					String artistInsertStatement = "INSERT INTO artists(artist_id, artist_name) VALUES(?,?)";
					List<FieldTypes> artistFieldTypes = new ArrayList<FieldTypes>();
					artistFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					artistFieldTypes.add(FieldTypes.FIELD_TYPE_STRING);
					
					List<Object> artistIDList = new ArrayList<Object>();
					List<Object> artistNameList = new ArrayList<Object>();
					
					List<List<Object>> artistArgumentsList = new ArrayList<List<Object>>();
					artistArgumentsList.add(artistIDList);
					artistArgumentsList.add(artistNameList);
					
					String albumInsertStatement = "INSERT INTO albums(album_id,album_name, artist_id, year, genre_id, length_sec,price) VALUES(?,?,?,?,?,?,?)";
					List<FieldTypes> albumFieldTypes = new ArrayList<FieldTypes>();
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_STRING);
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					albumFieldTypes.add(FieldTypes.FIELD_TYPE_INT);

					List<Object> albumIDList = new ArrayList<Object>();
					List<Object> albumNameList = new ArrayList<Object>();
					List<Object> albumArtistIDList = new ArrayList<Object>();
					List<Object> albumYearList = new ArrayList<Object>();
					List<Object> albumGenreIDList = new ArrayList<Object>();
					List<Object> albumLengthList = new ArrayList<Object>();
					List<Object> albumPriceList = new ArrayList<Object>();
					
					List<List<Object>> albumArgumentsList = new ArrayList<List<Object>>();
					albumArgumentsList.add(albumIDList);
					albumArgumentsList.add(albumNameList);
					albumArgumentsList.add(albumArtistIDList);
					albumArgumentsList.add(albumYearList);
					albumArgumentsList.add(albumGenreIDList);
					albumArgumentsList.add(albumLengthList);
					albumArgumentsList.add(albumPriceList);
					
					String songInsertStatement = "INSERT INTO songs(album_id, track_num, song_name, artist_id, length_sec) VALUES(?,?,?,?,?)";
					List<FieldTypes> songFieldTypes = new ArrayList<FieldTypes>();
					songFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					songFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					songFieldTypes.add(FieldTypes.FIELD_TYPE_STRING);
					songFieldTypes.add(FieldTypes.FIELD_TYPE_INT);
					songFieldTypes.add(FieldTypes.FIELD_TYPE_INT);

					List<Object> songAlbumIDList = new ArrayList<Object>();
					List<Object> songTrackNumList = new ArrayList<Object>();
					List<Object> songNameList = new ArrayList<Object>();
					List<Object> songArtistIDList = new ArrayList<Object>();
					List<Object> songLengthList = new ArrayList<Object>();

					List<List<Object>> songArgumentsList = new ArrayList<List<Object>>();
					songArgumentsList.add(songAlbumIDList);
					songArgumentsList.add(songTrackNumList);
					songArgumentsList.add(songNameList);
					songArgumentsList.add(songArtistIDList);
					songArgumentsList.add(songLengthList);

					
					for (DiscDBAlbumData albumData : parsedAlbums) {
						
						String genreName = albumData.getGenere().toLowerCase();
						if (!genres.containsKey(genreName)){
							genreID = nextGenreID++;
							genres.put(genreName, genreID);
							genreIDList.add(genreID);
							genreNameList.add(genreName);
						} else {
							genreID = genres.get(genreName);
						}
						
						if (!artistNames.containsKey(albumData.getArtist())){
							artistID = nextArtistID++;
							artistNames.put(albumData.getArtist(), artistID);
							artistIDList.add(artistID);
							artistNameList.add(albumData.getArtist());
						} else {
							artistID = artistNames.get(albumData.getArtist());
						}
						
						albumIDList.add(albumID);
						albumNameList.add(albumData.getName());
						albumArtistIDList.add(artistID);
						albumYearList.add(new Integer(albumData.getYear()));
						albumGenreIDList.add(genreID);
						albumLengthList.add(albumData.getLengthSec());
						albumPriceList.add(albumData.getPrice());	
						
						for (DiscDBTrackData trackData : albumData.getTrackList()) {
							if (!artistNames.containsKey(trackData.getArtist())){
								artistID = nextArtistID++;
								artistNames.put(trackData.getArtist(), artistID);
								artistIDList.add(artistID);
								artistNameList.add(trackData.getArtist());
							} else {
								artistID = artistNames.get(trackData.getArtist());
							}
							
							songAlbumIDList.add(albumID);
							songTrackNumList.add(trackData.getTrackNum());
							songNameList.add(trackData.getName());
							songArtistIDList.add(artistID);
							songLengthList.add(trackData.getLengthSdc());
						}
						albumID++;
					}
					
					if ((genreIDList.size() != 0) &&
							(DBAccessLayer.executePatternBatch(genreInsertStatement, genreArgumentsList, genreFieldTypes) != genreIDList.size())){
						Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: bad number of genre inserts executed");
						GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
						finishedSuccessfully = false;
						return;
					}
										
					if ((artistIDList.size() != 0) &&
							(DBAccessLayer.executePatternBatch(artistInsertStatement, artistArgumentsList, artistFieldTypes) != artistIDList.size())){
						Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: bad number of artist inserts executed");
						GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
						finishedSuccessfully = false;
						return;
					}
					
					if (DBAccessLayer.executePatternBatch(albumInsertStatement, albumArgumentsList, albumFieldTypes) != albumIDList.size()){
						Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: bad number of album inserts executed");
						GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
						finishedSuccessfully = false;
						return;
					}
					
					if (DBAccessLayer.executePatternBatch(songInsertStatement, songArgumentsList, songFieldTypes) != songAlbumIDList.size()){
						Debug.log("DBConnectionManage.BatchAddToDB [ERROR]: bad number of song inserts executed");
						GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
						finishedSuccessfully = false;
						return;
					}
				}
				finishedSuccessfully = true;
			}

			public boolean isFinishedSuccessfully() {
				return finishedSuccessfully;
			}
			
		}
		
		////////////////////////////////////////////////
		
		private String filename;
		
		public UpdateDatabase(String filename) {
			super();
			this.filename = filename;
		}
		
		
		
		
		public void run() {
			Debug.log("DBConnectionManage.UpdateDatabase thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			int lastAlbumID = 0;

			DBQueryResults maxIDQuery = DBAccessLayer.executeQuery("SELECT MAX(album_id) AS last_album_id\n" +
					"FROM Albums");
			if (maxIDQuery == null){
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
				return;
			}
			ResultSet rs = maxIDQuery.getResultSet();
			try {
				rs.next();
				lastAlbumID = rs.getInt("last_album_id");
			} catch (SQLException e) {
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
				return;
			}
			maxIDQuery.close();
			
			ParseFile fileParser = new ParseFile(filename);
			Thread parseThread = new Thread(fileParser);
			parseThread.start();
			
			BatchAddToDB dbAdder = new BatchAddToDB(parseThread);
			Thread updateThread = new Thread(dbAdder);
			updateThread.start();

			while (parseThread.isAlive() || updateThread.isAlive()){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.err.println("Caught an insomniac exception.\n" +
							"Don't really know what to do.\n" +
							"Will continue");	
				}
			}
			
			if (!fileParser.isFinishedSuccessfully() || !dbAdder.isFinishedSuccessfully()){ // Revert to previous DB state
				DBAccessLayer.executeUpdate("DELETE FROM Albums\n" +
						"WHERE album_id > " + lastAlbumID);
				DBAccessLayer.executeUpdate("DELETE FROM Songs\n" +
						"WHERE album_id > " + lastAlbumID);
			}
			if (fileParser.isFinishedSuccessfully() && dbAdder.isFinishedSuccessfully()) // only return finished successfully message when relevant.
				GuiUpdatesInterface.notifyDataBaseUpdated(filename); 
		}
	}
}
