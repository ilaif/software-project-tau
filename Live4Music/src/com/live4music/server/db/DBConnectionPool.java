package com.live4music.server.db;

import java.sql.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.live4music.shared.general.Debug;

public class DBConnectionPool {
	
	private static List<Connection> connectionPool = new LinkedList<Connection>();
	protected static final ReentrantLock lock = new ReentrantLock();
	
	private static boolean	FirstConnection = true;
	private	static boolean	SetParams = false;
	
	/* Connection parameters */
	private	static	String host;
	private static	int port;
	private	static	String SID;
	private static	String username;
	private static	String password;
	
	private static int nPassedConnections = 0;
		
	/**
	 * This method MUST BE CALLED before any connections can be created
	 * @param hostname
	 * @param portNum
	 * @param sid
	 * @param user
	 * @param pass
	 */
	public	static void	SetConnectionParams(String hostname, int portNum, 
										String sid, String user, String pass) {
		host = hostname;
		port = portNum;
		SID = sid;
		username = user;
		password = pass;
		
		SetParams = true;
	}
	
	
	/**
	 * Get a connection from the pool
	 * If no connections available, create a new one and add to pool
	 * @return
	 */
	public static Connection getConnection() {	
		/**
		 * Connection Params must be set before using the pool
		 */
		if (!SetParams) {
			Debug.log("DBConnectionPool::getConnection: ERROR - connection parameter haven't been set yet");			
			return null;			
		}
		try {
			lock.lock();
			// loading the driver in first connection 
			if (FirstConnection) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					Debug.log("DBConnectionPool::getConnection: ERROR - Unable to load the Oracle JDBC driver");
					return null;
				}
				FirstConnection = false;
			}
			
			// No connections ? create a new one and add to pool
			if (connectionPool.isEmpty()) {	
				try {
					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://" + host + ":" + port + "/" +
							SID, username, password);
					if (conn == null) {
						throw  new SQLException();
					}
					connectionPool.add(conn);
				} catch (SQLException e) {
					Debug.log("DBConnectionPool::getConnection: ERROR - Unable to connect - " + e.toString());
					return null;
				}								
			}
			
			// Now list is not empty, return one of the connections
			nPassedConnections++;
			return (connectionPool.remove(0));
		} finally {
			lock.unlock();
		}	
	}

	
	/**
	 * returns a connection back to the pool
	 * @pre:	The connection was retrieved from the pool
	 * @param conn
	 */
	public static void	endConnection(Connection conn) {	
		try	{	
			lock.lock();
			nPassedConnections--;
			connectionPool.add(conn);			
		} finally {
			lock.unlock();
		}		
	}

	
	/**
	 * Close all connections
	 * @Pre: All distributed connections have returned to the pool
	 * 			(or else, they won't be closed)
	 */
	public static void closeAllConnections() {
		try {
			lock.lock();
			for (Connection conn : connectionPool) {
				try {
					conn.close();
				} catch (SQLException e) {
					Debug.log("DBConnectionPool::closeAllConnections: ERROR - exception when closing connection: " + e.toString());
				}					
			}
			Debug.log("DBConnectionPool::closeAllConnections [INFO]: The number of requested and not returned connections is: "+nPassedConnections);
		} finally {
			lock.unlock();
			Debug.log("DBConnectionPool::closeAllConnections: INFO - closed all connections");		
		}
	}
}
