package com.live4music.server.db;

/**
 * Testing for DBAccessLayer
 *
 */
public class RunMe
{

	public static void main(String[] args)
	{
		try
		{
			// Init connection pool
			DBAccessLayer.SetConnectionParams("localhost", 1521, "xe", "store", "store");
			
		//	int id = DBAccessLayer.insertAndGetID("INSERT INTO ALBUMS\n (ALBUM_NAME, GENERE, LENGTH_SEC) VALUES('Eclipse', 'Java', 2329)", "ALBUM_ID");
		//	System.out.println("returned: " + id);

	//		int updated = DBAccessLayer.executeUpdate("UPDATE ALBUMS SET p'rice = 50, genere = 'trans' WHERE album_name = 'Eclipse'");
		//	System.out.println("updated: " + updated);
			
	/*		DBQueryResults res = DBAccessLayer.executeQuery("SELECT * FROM ALBUMS");
			while (res.getResultSet().next())
			{
				System.out.println(res.getResultSet().getInt(1) + "," + res.getResultSet().getString(2) + "," + res.getResultSet().getString(5));
			}
			res.close();*/
			
			// Batch of similar commands
		/*	String pattern = "INSERT INTO ALBUMS (album_name, genere, length_sec) VALUES(?, 'rock n roll', ?)";
			LinkedList<String> firstArg = new LinkedList<String>();
			LinkedList<String> secondArg = new LinkedList<String>();
			firstArg.add("Rotem1");
			firstArg.add("Rotem2");
			firstArg.add("Rotem3");
			firstArg.add("Rotem4");
			firstArg.add("Rotem5");
			firstArg.add("Rotem6");
			secondArg.add(Integer.toString(345));
			secondArg.add(Integer.toString(346));
			secondArg.add(Integer.toString(347));
			secondArg.add(Integer.toString(348));
			secondArg.add(Integer.toString(349));
			secondArg.add(Integer.toString(350));		
			int updated = DBAccessLayer.executePatternBatch(pattern, firstArg, secondArg);
			System.out.println("updated: " + updated); */
	
			// Batch of different commands
/*			LinkedList<String> lst1 = new LinkedList<String>();
			lst1.add("INSERT INTO ALBUMS (ALBUM_NAME, GENERE, LENGTH_SEC) VALUES ('last try', 'who cares', 123)");
			lst1.add("INSERT INTO SONGS (ALBUM_ID, TRACK_NUM, SONG_NAME, LENGTH_SEC) VALUES (ALBUMS_SEQ.CURRVAL, 5, 'name bla', 55)");
			int updated = DBAccessLayer.executeBatch(lst1);
			System.out.println("updated: " + updated);*/
			
			// Transactions
		/*	LinkedList<String> lst1 = new LinkedList<String>();
			lst1.add("INSERT INTO ALBUMS (ALBUM_NAME, GENERE, LENGTH_SEC) VALUES ('trans try', 'who cares - trans', 133)");
			lst1.add("UPDATE ALBUMS SET LENGTH_SEC = LENGTH_SEC+10 WHERE ALBUM_NAME = 'trans try'");
			int updated = DBAccessLayer.executeCommandsAtomic(lst1);
			System.out.println("updated: " + updated);*/		
			
		//	lst.add(e)
			

			
	//		Compresser.CompressFile("freedb-complete-20091201.tar.bz2");
	//		Compresser.CompressFile("freedb-update-20091101-20091201.tar.tar");			
		//	DiscsDB.DiscDBParser.parseFile("archive.tar");
//			ConfigurationManager cm = new ConfigurationManager("store.props");
	//		System.out.println(cm.getHost() + "\n" + cm.getSID() + "\n" +
		//			cm.getPort() + "\n" + cm.getUsername() + "\n" + cm.getPassword());
		} catch (Exception e) {
				
		} finally {
			DBConnectionPool.closeAllConnections();
		}
	}

	
	
}









