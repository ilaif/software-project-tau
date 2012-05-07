package com.live4music.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.live4music.client.tables.*;
import com.live4music.client.ui.AlbumStockInfoCallerEnum;
import com.live4music.client.ui.DBActionFailureEnum;
import com.live4music.client.ui.GuiUpdatesInterface;
import com.live4music.server.queries.*;
import com.live4music.shared.general.*;
import com.live4music.shared.general.Debug.DebugOutput;


/**
 * 
 * DB Connection Interface
 * 
 * all functions used by gui to pass queries / transactions to the DB layer
 * for each method, the corresponding method in GuiUpdatesInterface, the one that
 * should be called when finishing, is mentioned in the javadoc
 * 
 * *** System out prints are for debugging ***
 *  
 **/


public class DBConnectionInterface{
	
	// static fields for all thread holders:
	private static DBConnectionSearch dbcSearch = new DBConnectionSearch();
	private static DBConnectionSale dbcSale = new DBConnectionSale();
	private static DBConnectionStock dbcStock = new DBConnectionStock();
	private static DBConnectionManage dbcManage= new DBConnectionManage();
	
	//////////
	// Main //
	//////////
	
	/**
	 * invoked automatically when program starts
	 * initializes connection with given parameters
	 * after connection is done, DB invokes initialization of stores table,
	 * for selection of current store in initial dialog
	 * corresponding method in GUI.GuiUpdatesInterface: initStoresTable
	 */
	public static void initDBConnection(ConfigurationManager confMan){
		Debug.log("DBConnectionInterface.initDBConnection is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(new InitDBConnection(confMan))).start();
	}
	
	/**
	 * This class corresponds to the above method "public static void initDBConnection(ConfigurationManager confMan)"
	 * This thread role is to initialize the Connection Pool and to get the "Stores" table from the DataBase to the GUI.
	 */
	public static class InitDBConnection implements Runnable{
		private ConfigurationManager confMan;
		
		public InitDBConnection(ConfigurationManager confMan){
			this.confMan = confMan;
		}
		
		public void run() {
			Debug.log("DBConnectionInterface.InitDBConnection thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			DBConnectionPool.SetConnectionParams(confMan.getHost(),
												 confMan.getPort(), 
												 confMan.getSID(),
												 confMan.getUsername(),
												 confMan.getPassword());
			Debug.log("DBConnectionInterface.InitDBConnection initialized DBConnectionPool");
			
			// Get Stores Table From The DataBase:
			StoresTable stores = new StoresTable();
			
			DBQueryResults dBQRes = DBAccessLayer.executeQuery("SELECT store_id, city, address, phone_number, manager_id FROM stores");	
			if (dBQRes == null){
				Debug.log("DBConnectionInterface.InitDBConnection [ERROR]: Failed to get stores table from DB. NULL pointer returned.");
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.DB_CONN_FAILURE);
				return;
			} else {
				ResultSet rs = dBQRes.getResultSet();
				try {
					while (rs.next()){
						stores.addStore(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
					}
				} catch (SQLException e) {
					Debug.log("DBConnectionInterface.InitDBConnection [ERROR]: Failed extracting stores from ResultSet");
					Debug.log(e.getStackTrace().toString());
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.DB_CONN_FAILURE);
					dBQRes.close();
					return;
				}	
				Debug.log("DBConnectionInterface.InitDBConnection done with the DB.");	
				GuiUpdatesInterface.initStoresTable(stores);
				dBQRes.close();
			}
		}
	}
	////////////////
	// Search tab //
	////////////////
	
	/**
	 * invoked in Main \ search-tab \ "Search" button
	 * invokes albums search query
	 * corresponding method in GUI.GuiUpdatesInterface: updateAlbumResultsTable
	 */
	public static void getAlbumsSearchResults(AlbumSearchQuery albumSearchQuery){
		Debug.log("DBConnectionInterface.getAlbumsSearchResults is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcSearch.new GetAlbumsSearchResults(albumSearchQuery))).start();		
	}
	
	/**
	 * invoked in Main \ search-tab \ album search result selection
	 * invokes query to get all songs (in a SongsResultsTable structure) for the given album
	 * corresponding method in GUI.GuiUpdatesInterface: updateSongsResultsTable
	 * @param albumID
	 */
	public static void getSongsResults(long albumID){
		Debug.log("DBConnectionInterface.getSongsResults is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcSearch.new GetSongsResults(albumID))).start();
	}
	
	//////////////
	// Sale tab //
	//////////////
	
	/**
	 * invoked in Main \ sale-tab \ "Make Sale" button
	 * invokes sale creation and adds to sales table
	 * corresponding method in GUI.GuiUpdatesInterface: initSaleTable
	 */
	public static void makeSale(SaleTable sale){
		Debug.log("DBConnectionInterface.makeSale is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcSale.new MakeSale(sale))).start();		
	}
	
	///////////////
	// Stock tab //
	///////////////
	
	// order's available stores
	
	/**
	 * invoked in Main \ stock-tab \ "Check Availability" button
	 * invokes order's available stores query
	 * corresponding method in GUI.GuiUpdatesInterface: updateOrderAvailableStores
	 * @param albumID
	 */
	public static void getOrderAvailableStores(OrderAvailableStoresQuery query){
		Debug.log("DBConnectionInterface.getOrderAvailableStores is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new GetOrderAvailableStores(query))).start();
	}
	
	// orders table
	
	/**
	 * invoked in InitialDialog \ "Start" - when program starts, after choosing the
	 * current store-id, "Start" is selected to initialize data view
	 * invokes query to get initial orders table for selected store
	 * corresponding method in GUI.GuiUpdatesInterface: initOrdersTable
	 */
	public static void getOrdersTable(){
		Debug.log("DBConnectionInterface.getOrdersTable is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new GetOrdersTable())).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \ "Refresh" (orders) button
	 * updates orders table according to DB
	 * corresponding method in GUI.GuiUpdatesInterface: refreshOrdersTable
	 */
	public static void refreshOrdersTable(){
		Debug.log("DBConnectionInterface.refreshOrdersTable is called",DebugOutput.FILE,DebugOutput.STDOUT);	
		
		(new Thread(dbcStock.new RefreshOrdersTable())).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \ "Remove Order" button
	 * removes order with given order-id from orders table
	 * corresponding method in GUI.GuiUpdatesInterface: removeOrder
	 * @param orderID
	 */
	public static void removeOrder(int orderID){
		Debug.log("DBConnectionInterface.removeOrder is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new RemoveOrder(orderID))).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \ "Place Order" button
	 * invokes add order to orders table
	 * corresponding method in GUI.GuiUpdatesInterface: addOrder
	 */
	public static void placeOrder(OrdersOrRequestsTableItem order){
		Debug.log("DBConnectionInterface.placeOrder is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new PlaceOrder(order))).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \ "Order from Supplier" button
	 * invokes order (immediate) from supplier
	 * corresponding method in GUI.GuiUpdatesInterface: approveOrderFromSupplierDone
	 * @param albumID
	 * @param quantity
	 */
	public static void placeOrderFromSupplier(long albumID, int quantity){
		Debug.log("DBConnectionInterface.placeOrderFromSupplier is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new PlaceOrderFromSupplier(albumID, quantity))).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \
	 *  	- "Cancel Order" button by current store -> canceled
	 * 		- "Deny Request" button by requested store -> denied
	 * 		- "Approve Request" button by requested store -> completed
	 * update order in orders table to given status. order in orders table will always appear:
	 * - waiting
	 * - denied
	 * - completed
	 * - canceled (by requester)
	 * corresponding method in GUI.GuiUpdatesInterface:
	 * - if was called by requester (orders table button invoked): updateOrderStatus
	 * - if was called by supplier (requests table button invoked): removeRequest
	 * @param orderID
	 * @param status
	 */
	public static void updateOrderStatus(int orderID, OrderStatusEnum status){
		Debug.log("DBConnectionInterface.updateOrderStatus is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new UpdateOrderStatus(orderID, status))).start();
	}
	
	// requests table
	
	/**
	 * invoked in InitialDialog \ "Start" - when program starts, after choosing the
	 * current store-id, "Start" is selected to initialize data view
	 * invokes query to get initial requests table for selected store
	 * corresponding method in GUI.GuiUpdatesInterface: initRequestsTable
	 */
	public static void getRequestsTable(){
		Debug.log("DBConnectionInterface.getRequestsTable is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new GetRequestTable())).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \ "Refresh" (requests) button
	 * updates requests table according to DB
	 * corresponding method in GUI.GuiUpdatesInterface: refreshRequestsTable
	 */
	public static void refreshRequestsTable(){
		Debug.log("DBConnectionInterface.refreshRequestsTable is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcStock.new RefreshRequestsTable())).start();
	}
	
	/**
	 * invoked in Main \ stock-tab \ "Approve Request" button
	 * send album id to receive the stock information for the selected album: price,
	 * location and quantity in store
	 *  corresponding method in GUI.GuiUpdatesInterface: updateAlbumStockInformation
	 *  *** caller can be either get stock info for an album search result or for check before approving request
	 * @param albumID
	 */
	public static void getAlbumStockInfo(long albumID, AlbumStockInfoCallerEnum caller){
		Debug.log("DBConnectionInterface.getAlbumStockInfo is called",DebugOutput.FILE,DebugOutput.STDOUT);
	
		(new Thread(dbcStock.new GetAlbumStockInfo(albumID, caller))).start();
		
	}

	////////////////////
	// Management tab //
	////////////////////
	
	// employees table
	
	/**
	 * invoked in InitialDialog \ "Start" - when program starts, after choosing the
	 * current store-id, "Start" is selected to initialize data view
	 * invokes query to get initial employees table for selected store
	 * corresponding method in GUI.GuiUpdatesInterface: initEmployeesTable
	 */
	public static void getEmployeesTable(){
		Debug.log("DBConnectionInterface.getEmployeesTable is called",DebugOutput.FILE,DebugOutput.STDOUT);
	
		(new Thread(dbcManage.new GetEmployeesTable())).start();
	}
	
	/**
	 * invoked in Main \ manage-tab \ "Insert" button
	 * check if employee exists in network employees table
	 * corresponding method in GUI.GuiUpdatesInterface: tryInsertNewEmployee
	 * @param employeeID
	 */
	public static void checkIfEmployeeExists(int employeeID){
		Debug.log("DBConnectionInterface.checkIfEmployeeExists is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcManage.new CheckIfEmployeeExists(employeeID))).start();
	}
	
	/**
	 * invoked in Main \ management-tab \
	 * 		- "Insert" button
	 * 		- "Save" button
	 * adds employee to employees table if not a member, or updates employee details if already exists
	 * corresponding method in GUI.GuiUpdatesInterface: insertUpdateEmployee
	 * @param employee
	 */
	public static void insertUpdateEmployee(EmployeesTableItem employee){
		Debug.log("DBConnectionInterface.insertUpdateEmployee is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcManage.new InsertUpdateEmployee(employee))).start();
	}
	
	/**
	 * invoked in Main \ management-tab \ "Remove Employee" button
	 * removes employee with given employee-id from employees table
	 * corresponding method in GUI.GuiUpdatesInterface: removeEmployee 
	 * @param employeeID
	 */
	public static void removeEmployee(int employeeID){
		Debug.log("DBConnectionInterface.removeEmployee is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcManage.new RemoveEmployee(employeeID))).start();
	}
	
	// database update
	
	/**
	 * invoked in Main \ management-tab \ "Update Database" button
	 * invokes database update from given filename
	 * corresponding method in GUI.GuiUpdatesInterface: notifyDataBaseUpdated
	 * @param filename
	 */
	public static void updateDataBase(String filename){
		Debug.log("DBConnectionInterface.updateDataBase is called",DebugOutput.FILE,DebugOutput.STDOUT);
		
		(new Thread(dbcManage.new UpdateDatabase(filename))).start();
	}
}