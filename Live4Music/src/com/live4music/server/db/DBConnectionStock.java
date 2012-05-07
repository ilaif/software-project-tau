package com.live4music.server.db;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.live4music.client.tables.*;
import com.live4music.client.ui.AlbumStockInfoCallerEnum;
import com.live4music.client.ui.DBActionFailureEnum;
import com.live4music.client.ui.GuiUpdatesInterface;
import com.live4music.client.ui.OrdersRequestsActionsEnum;
import com.live4music.client.ui.StaticProgramTables;
import com.live4music.server.queries.*;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;


/** 
 *	This class contains the Runnable classes for handling the GUI tab "SALE".
 * 	Each class corresponds to a single method in DBConnectionInterface class.
 */
public class DBConnectionStock {
	/**
	 * Date converter
	 */
	private String toGUIDate(Date d) {
		String[] dateArr = d.toString().split("-");
		return dateArr[2]+"/"+dateArr[1]+"/"+dateArr[0];
	}
	
	private long getRandLocation() {
		long precision = 1000000000 * 100; 
		long ret = ((new Random()).nextLong() % precision);
		while (ret <= 0)
			ret = ((new Random()).nextLong() % precision);
		return ret;
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void getOrderAvailableStores(OrderAvailableStoresQuery query);"
	 */
	public class GetOrderAvailableStores implements Runnable{
		private OrderAvailableStoresQuery query;
		
 		public GetOrderAvailableStores(OrderAvailableStoresQuery query) {
			this.query = query;
		}

		
		public void run() {
			Debug.log("DBConnectionStock.GetOrderAvailableStores thread is started");
			
			OrderAvailableStoresTable availableStores = new OrderAvailableStoresTable();
			
			String queryStr = "SELECT DISTINCT stores.store_id, city, quantity FROM stores, stock " +
							  "WHERE (stores.store_id=stock.store_id) AND (stock.album_id="+query.getAlbumID()+")";
			DBQueryResults dBQRes = DBAccessLayer.executeQuery(queryStr);

			if (dBQRes == null) {
				Debug.log("DBConnectionStock.GetOrderAvailableStores [ERROR]: DB access failed, NULL pointer returned.", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.CHECK_AVAIL_FAILURE);
				return;
			}
			ResultSet rs = dBQRes.getResultSet(); 
			try {
				while (rs.next()){
					availableStores.addStore(rs.getInt(1), rs.getString(2), rs.getInt(3));
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionStock.GetOrderAvailableStores [ERROR] encountered an database access error", DebugOutput.STDERR, DebugOutput.FILE);
				Debug.log(e.getStackTrace().toString());
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.CHECK_AVAIL_FAILURE);
				dBQRes.close();
				return;
			}
							
				
			Debug.log("DBConnectionStock.GetOrderAvailableStores done with DB, calling GUI's updateOrderAvailableStores");
			GuiUpdatesInterface.updateOrderAvailableStores(availableStores);
			dBQRes.close();
		}
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void getOrdersTable();"
	 */
	public class GetOrdersTable implements Runnable{
		
		public void run() {
			Debug.log("DBConnectionStock.GetOrdersTable thread is started");
			
			OrdersOrRequestsTable ordersOrRequestsTable = new OrdersOrRequestsTable(true);
			int thisStoreID = StaticProgramTables.getThisStore().getStoreID();
			String queryStr = "SELECT order_id, supplying_store_id, album_id, quantity, order_date, status FROM orders " +
								"WHERE ordering_store_id="+thisStoreID;
			DBQueryResults dBQRes = DBAccessLayer.executeQuery(queryStr); 
			if (dBQRes == null) {
				Debug.log("DBConnectionStock.GetOrdersTable [ERROR]: DB access failed, NULL pointer returned.", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.initOrdersTable(new OrdersOrRequestsTable(true));
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_ORDERS_FAILURE);
				return;
			}
			ResultSet rs = dBQRes.getResultSet();
			try {
				while (rs.next()){
					int ordStat = rs.getInt(6);
					ordersOrRequestsTable.addOrder(rs.getInt(1), thisStoreID, rs.getInt(2), rs.getLong(3), rs.getInt(4), toGUIDate(rs.getDate(5)), 
							OrderStatusEnum.convertFromInt(ordStat)); 
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionStock.GetOrdersTable [ERROR] encountered an database access error:");
				Debug.log(e.getStackTrace().toString());
				GuiUpdatesInterface.initOrdersTable(new OrdersOrRequestsTable(true));
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_ORDERS_FAILURE);
				dBQRes.close();
				return;
			}
			
		Debug.log("DBConnectionStock.GetOrdersTable done with DB, calling GUI's initOrdersTable");
		GuiUpdatesInterface.initOrdersTable(ordersOrRequestsTable);
		dBQRes.close();			
		}
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void refreshOrdersTable();"
	 */
	public class RefreshOrdersTable implements Runnable{

		
		public void run() {
			Debug.log("DBConnectionStock.RefreshOrdersTable thread is started");
			
			OrdersOrRequestsTable ordersOrRequestsTable = new OrdersOrRequestsTable(true);
			int thisStoreID = StaticProgramTables.getThisStore().getStoreID();
			String queryStr = "SELECT order_id, supplying_store_id, album_id, quantity, order_date, status FROM orders " +
								"WHERE ordering_store_id="+thisStoreID;
			DBQueryResults dBQRes = DBAccessLayer.executeQuery(queryStr); 
			if (dBQRes == null) {
				Debug.log("DBConnectionStock.RefreshOrdersTable [ERROR]: DB access failed, NULL pointer returned.", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
				return;
			}
			ResultSet rs = dBQRes.getResultSet();
			try {
				while (rs.next()){
					int ordStat = rs.getInt(6);
					ordersOrRequestsTable.addOrder(rs.getInt(1), thisStoreID, rs.getInt(2), rs.getLong(3), rs.getInt(4), toGUIDate(rs.getDate(5)), 
							OrderStatusEnum.convertFromInt(ordStat)); 
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionStock.RefreshOrdersTable [ERROR] encountered an database access error:");
				Debug.log(e.getStackTrace().toString());
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
				dBQRes.close();
				return;
			}
			
		Debug.log("DBConnectionStock.RefreshOrdersTable done with DB, calling GUI's initOrdersTable");
		GuiUpdatesInterface.refreshOrdersTable(ordersOrRequestsTable);
		dBQRes.close();
		}
		
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void removeOrder(int orderID);"
	 */
	public class RemoveOrder implements Runnable{
		private int orderID;
			
		public RemoveOrder(int orderID) {
			super();
			this.orderID = orderID;
		}
		
		public void run() {
			Debug.log("DBConnectionStock.RemoveOrder thread is started");
			
			String queryStr = "DELETE FROM orders WHERE order_id="+orderID;
 
			if (DBAccessLayer.executeUpdate(queryStr) < 1){
				Debug.log("DBConnectionStock.RemoveOrder [ERROR]: Couldn't remove order with ID: '"+orderID+"'", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
				return;
			}
			
			Debug.log("DBConnectionStock.RemoveOrder done with DB, calling GUI's removeOrder");
			GuiUpdatesInterface.removeOrder(orderID);
		}
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void placeOrder(OrdersOrRequestsTableItem order);"
	 */
	public class PlaceOrder implements Runnable{
		private OrdersOrRequestsTableItem order;

		public PlaceOrder(OrdersOrRequestsTableItem order) {
			super();
			this.order = order;
		}

		
		public void run() {
			Debug.log("DBConnectionStock.PlaceOrder thread is started");
			
			OrdersOrRequestsTableItem retOrder = new OrdersOrRequestsTableItem(-1, // Default
																			   StaticProgramTables.getThisStore().getStoreID(),
																			   order.getSupplyingStoreID(),
																			   order.getAlbumID(),
																			   order.getQuantity(),
																			   order.getDate(),
																			   OrderStatusEnum.WAITING);
			
			String [] dateArr = order.getDate().split("/");
			String toDateString = "'"+dateArr[0]+"/"+dateArr[1]+"/"+dateArr[2]+"','%d/%m/%Y'";
			String query = "INSERT INTO orders(ordering_store_id, supplying_store_id, album_id, quantity, order_date, status) " +
							"VALUES("+StaticProgramTables.getThisStore().getStoreID()+","+order.getSupplyingStoreID()+","+order.getAlbumID()+"," +
									""+order.getQuantity()+",STR_TO_DATE("+toDateString+"),"+OrderStatusEnum.WAITING.getIntRep()+")";
			
			int retOrderID = DBAccessLayer.insertAndGetID(query, "order_id");
			if (retOrderID < 0){
				Debug.log("DBConnectionStock.PlaceOrder thread [ERROR]: Unable to insert album with ID "+order.getAlbumID()+" to DB");
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.PLACE_ORDER_FAILURE);
				return;
			}
			retOrder.setOrderID(retOrderID);
			Debug.log("DBConnectionStock.PlaceOrder [INFO]: OrderID="+retOrder.getOrderID()+", AlbumID="+retOrder.getAlbumID());
			Debug.log("DBConnectionStock.PlaceOrder done with DB, calling GUI's addOrder");
			GuiUpdatesInterface.addOrder(retOrder);			
		}
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void updateOrderStatus(int orderID, OrderStatusEnum status);"
	 */
	public class UpdateOrderStatus implements Runnable{
		private int orderID;
		private OrderStatusEnum status;
		public UpdateOrderStatus(int orderID, OrderStatusEnum status) {
			super();
			this.orderID = orderID;
			this.status = status;
		}
		
		
		public void run() {
			Debug.log("DBConnectionStock.UpdateOrderStatus thread is started");
			
			DBQueryResults dBQres = DBAccessLayer.executeQuery("SELECT * FROM orders WHERE order_id="+orderID);
			if (dBQres == null) {
				Debug.log("DBConnectionStock.UpdateOrderStatus [ERROR]: DB access failed, NULL pointer returned.");
				GuiUpdatesInterface.notifyDBFailure(status.compareTo(OrderStatusEnum.CANCELED) == 0 ? 
													DBActionFailureEnum.ORDERS_ACTION_FAILURE : DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
				return;
			}
			ResultSet rs = dBQres.getResultSet();
			try {
				switch (status){
				case CANCELED:
					if (!rs.next()){
						Debug.log("DBConnectionStock.UpdateOrderStatus [ERROR]: Tried to cancel non existing order");
						GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
						dBQres.close();
						return;
					}
					if (rs.getInt("status") != OrderStatusEnum.WAITING.getIntRep()){
						Debug.log("DBConnectionStock.UpdateOrderStatus couldn't change order status to "+status+" . Unexpected current status.");
						GuiUpdatesInterface.denyOrdersOrRequestsTableAction(OrdersRequestsActionsEnum.CANCEL_ORDER,
								(rs.getInt("status")== OrderStatusEnum.COMPLETED.getIntRep() ? 
										OrdersRequestsActionsEnum.APPROVE_REQUEST : OrdersRequestsActionsEnum.DENY_REQUEST), orderID);
						dBQres.close();
						return;
					}
					if (DBAccessLayer.executeUpdate("UPDATE orders SET status="+OrderStatusEnum.CANCELED.getIntRep()+" WHERE order_id="+orderID) != 1){ 
						//Filed to execute UPDATE
						Debug.log("DBConnectionStock.UpdateOrderStatus [ERROR]: couldn't change order status to "+status);
						GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
						dBQres.close();
						return;
					}
					
					GuiUpdatesInterface.updateOrderStatus(orderID, status);
					break;
				case COMPLETED:
					if (!rs.next()){						
						Debug.log("DBConnectionStock.UpdateOrderStatus [NOTICE]: Tried to approve non existing order.");
						GuiUpdatesInterface.denyOrdersOrRequestsTableAction(OrdersRequestsActionsEnum.APPROVE_REQUEST, OrdersRequestsActionsEnum.CANCEL_ORDER, orderID);
						dBQres.close();
						return;
					} else if (rs.getInt("status") != OrderStatusEnum.WAITING.getIntRep()){	
						Debug.log("DBConnectionStock.UpdateOrderStatus couldn't change order status to "+status+" . Unexpected current status.");
						GuiUpdatesInterface.denyOrdersOrRequestsTableAction(OrdersRequestsActionsEnum.APPROVE_REQUEST, OrdersRequestsActionsEnum.CANCEL_ORDER, orderID);
						dBQres.close();
						return;
					} else { 
						// Approving request, updating stocks.
						
						List<String> queryList = new ArrayList<String>();
						
						// Update the supplier
						queryList.add("UPDATE stock SET quantity=quantity-"+rs.getInt("quantity")+" " +
									"WHERE (store_id="+rs.getInt("supplying_store_id")+") AND (album_id="+rs.getLong("album_id")+")");
						
						// Update the receiver
						queryList.add("UPDATE stock SET quantity=quantity+"+rs.getInt("quantity")+" " +
						"WHERE (store_id="+rs.getInt("ordering_store_id")+") AND (album_id="+rs.getLong("album_id")+")");
						
						// Change order status to COMPLETED
						queryList.add("UPDATE orders SET status="+OrderStatusEnum.COMPLETED.getIntRep()+" WHERE order_id="+orderID);
						
						// Update the Stock table
						queryList.add("DELETE FROM stock WHERE quantity=0");
						
						
						int [] minUpdatesPerCommand = {1, 1, 1, 0};
						int retCode = DBAccessLayer.executeCommandsAtomic(queryList, minUpdatesPerCommand);
						
						if (retCode == 1){// The receiver doesn't have the CD in stock table.
							queryList.remove(1);
							queryList.add(1, "INSERT INTO stock(album_id, store_id, quantity, storage_location) " +
											"VALUES("+rs.getInt("album_id")+","+rs.getInt("ordering_store_id")+","+
												rs.getInt("quantity")+","+getRandLocation()+")");
							retCode = DBAccessLayer.executeCommandsAtomic(queryList, minUpdatesPerCommand);
						}
						
						if (retCode < queryList.size()){
							Debug.log("DBConnectionStock.UpdateOrderStatus [ERROR]: couldn't change order status to "+status);
							GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
							dBQres.close();
							return;
						}
						
						Debug.log("DBConnectionStock.UpdateOrderStatus updated Orders and Stock tables");
						GuiUpdatesInterface.removeRequest(orderID, true);
					}
					break;
				case DENIED: 
					if (!rs.next()){							
						Debug.log("DBConnectionStock.UpdateOrderStatus [NOTICE]: Tried to deny non existing order");
						GuiUpdatesInterface.denyOrdersOrRequestsTableAction(OrdersRequestsActionsEnum.APPROVE_REQUEST, OrdersRequestsActionsEnum.CANCEL_ORDER, orderID);
						dBQres.close();
						return;
					} else if (rs.getInt("status") != OrderStatusEnum.WAITING.getIntRep()){		
						Debug.log("DBConnectionStock.UpdateOrderStatus couldn't change order status to "+status+" . Unexpected current status.");
						GuiUpdatesInterface.denyOrdersOrRequestsTableAction(OrdersRequestsActionsEnum.APPROVE_REQUEST, OrdersRequestsActionsEnum.CANCEL_ORDER, orderID);
						dBQres.close();
						return;
					} else { 
						// Denying request
						if (DBAccessLayer.executeUpdate("UPDATE orders SET status="+OrderStatusEnum.DENIED.getIntRep()+" WHERE order_id="+orderID) < 1){
							Debug.log("DBConnectionStock.UpdateOrderStatus [ERROR]: Failed to update status of the order to 'Denied'");
							GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
							dBQres.close();
							return;
						}else{//SUCCESS
							Debug.log("DBConnectionStock.UpdateOrderStatus updated order status to Denied.");
							GuiUpdatesInterface.removeRequest(orderID, false);
						}
					}
					break;
				}// closes Switch		
				} catch (SQLException e) {
					Debug.log("DBConnectionStock.UpdateOrderStatus [ERROR]: DB access failed. Switch statement interrupted.");
					Debug.log(e.getStackTrace().toString());
					GuiUpdatesInterface.notifyDBFailure(status.compareTo(OrderStatusEnum.CANCELED) == 0 ? 
							DBActionFailureEnum.ORDERS_ACTION_FAILURE : DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
					dBQres.close();
					return;
				}
			dBQres.close();
		}
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void getRequestsTable();"
	 */
	public class GetRequestTable implements Runnable{

		public void run() {
			Debug.log("DBConnectionStock.GetRequestTable thread is started");
			
			OrdersOrRequestsTable ordersOrRequestsTable = new OrdersOrRequestsTable(false);
			int thisStoreID = StaticProgramTables.getThisStore().getStoreID();
			String queryStr = "SELECT order_id, ordering_store_id, album_id, quantity, order_date, status FROM orders " +
								"WHERE (supplying_store_id="+thisStoreID+") AND (status="+OrderStatusEnum.WAITING.getIntRep()+")";
			DBQueryResults dBQRes = DBAccessLayer.executeQuery(queryStr); 
			if (dBQRes == null) {
				Debug.log("DBConnectionStock.GetRequestTable [ERROR]: DB access failed, NULL pointer returned.", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.initRequestsTable(new OrdersOrRequestsTable(false));
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_REQUESTS_FAILURE);
				return;
			}
			ResultSet rs = dBQRes.getResultSet();
			try {
				while (rs.next()){
					int ordStat = rs.getInt(6);
					ordersOrRequestsTable.addOrder(rs.getInt(1), rs.getInt(2), thisStoreID, rs.getLong(3), rs.getInt(4), toGUIDate(rs.getDate(5)), 
							OrderStatusEnum.convertFromInt(ordStat)); 
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionStock.GetRequestTable [ERROR] encountered an database access error:");
				Debug.log(e.getStackTrace().toString());
				GuiUpdatesInterface.initRequestsTable(new OrdersOrRequestsTable(false));
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_REQUESTS_FAILURE);
				dBQRes.close();
				return;
			}
			
		Debug.log("DBConnectionStock.GetRequestTable done with DB, calling GUI's initRequestsTable");
		GuiUpdatesInterface.initRequestsTable(ordersOrRequestsTable);
		dBQRes.close();
		}
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void refreshRequestsTable();"
	 */
	public class RefreshRequestsTable implements Runnable{

		
		public void run() {
			Debug.log("DBConnectionStock.RefreshRequestsTable thread is started");
			
			OrdersOrRequestsTable ordersOrRequestsTable = new OrdersOrRequestsTable(false);
			int thisStoreID = StaticProgramTables.getThisStore().getStoreID();
			String queryStr = "SELECT order_id, ordering_store_id, album_id, quantity, order_date, status FROM orders " +
								"WHERE (supplying_store_id="+thisStoreID+") AND (status="+OrderStatusEnum.WAITING.getIntRep()+")";
			DBQueryResults dBQRes = DBAccessLayer.executeQuery(queryStr); 
			if (dBQRes == null) {
				Debug.log("DBConnectionStock.RefreshRequestsTable [ERROR]: DB access failed, NULL pointer returned.", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
				return;
			}
			ResultSet rs = dBQRes.getResultSet();
			try {
				while (rs.next()){
					int ordStat = rs.getInt(6);
					ordersOrRequestsTable.addOrder(rs.getInt(1), rs.getInt(2), thisStoreID, rs.getLong(3), rs.getInt(4), toGUIDate(rs.getDate(5)), 
							OrderStatusEnum.convertFromInt(ordStat)); 
				}
			} catch (SQLException e) {
				Debug.log("DBConnectionStock.RefreshRequestsTable [ERROR] encountered an database access error:");
				Debug.log(e.getStackTrace().toString());
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
				dBQRes.close();
				return;
			}
			
		Debug.log("DBConnectionStock.RefreshRequestsTable done with DB, calling GUI's initRequestsTable");
		GuiUpdatesInterface.refreshRequestsTable(ordersOrRequestsTable);
		dBQRes.close();
		}
		
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void getAlbumStockInfo();"
	 */
	public class GetAlbumStockInfo implements Runnable{
		private long albumID;
		private AlbumStockInfoCallerEnum caller;
		
		public GetAlbumStockInfo(long albumID, AlbumStockInfoCallerEnum caller){
			this.albumID = albumID;
			this.caller = caller;
		}
		
		
		public void run() {
			Debug.log("DBConnectionInterface.GetAlbumStockInfo thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			int storeID = StaticProgramTables.getThisStore().getStoreID();
			DBQueryResults dBQRes = DBAccessLayer.executeQuery("SELECT quantity, storage_location FROM stock " +
															   "WHERE (album_id="+albumID+" ) AND (store_id="+storeID+")");
			if (dBQRes == null){
				Debug.log("DBConnectionSale.GetAlbumStockInfo [ERROR]: DB access failed, NULL pointer returned.", DebugOutput.FILE, DebugOutput.STDERR);
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.GET_STOCK_INFO_FAILURE);
				return;
			}
			ResultSet rs = dBQRes.getResultSet();
			long retStorageLocation = 0;
			int retQuantity = 0;
			try {
				if (!rs.next()){//The album doesn't appear in the STOCK table.
					Debug.log("DBConnectionSale.GetAlbumStockInfo Done working with DB  \n(The album doesn't appear in the STOCK table) calling GUI's updateAlbumStockInformation.");
					GuiUpdatesInterface.updateAlbumStockInformation(albumID, retStorageLocation, retQuantity, caller);
					dBQRes.close();
					return;
				}
				retStorageLocation = rs.getLong("storage_location");
				retQuantity = rs.getInt("quantity");
			} catch (SQLException e) {
				Debug.log("DBConnectionSale.GetAlbumStockInfo [ERROR]: Failed to iterate over the ResultSet.");
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.GET_STOCK_INFO_FAILURE);
				dBQRes.close();
				return;
			}
			
			Debug.log("DBConnectionSale.GetAlbumStockInfo Done working with DB calling GUI's updateAlbumStockInformation.");
			GuiUpdatesInterface.updateAlbumStockInformation(albumID, retStorageLocation, retQuantity, caller);
			dBQRes.close();
		}
		
	}
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void placeOrderFromSupplier();"
	 */
	public class PlaceOrderFromSupplier implements Runnable {
		
		private long albumID;
		private int quantity;
		
		public PlaceOrderFromSupplier(long albumID, int quantity){
			this.albumID = albumID;
			this.quantity = quantity;
		}

		
		public void run() {
			Debug.log("DBConnectionStock.GetFromSupplier thread is started");
			
			int storeID = StaticProgramTables.getThisStore().getStoreID();
			int nRowsUpdated = DBAccessLayer.executeUpdate("UPDATE stock SET quantity=quantity+"+quantity+"" +
										" WHERE (album_id="+albumID+") AND (store_id="+storeID+")");
			if (nRowsUpdated < 0){
				Debug.log("DBConnectionStock.GetFromSupplier [ERROR]: DB access failure. Failed to update stock table.");
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.PLACE_ORDER_FAILURE);
				return;
			}
			if (nRowsUpdated == 0){ // This is a new album in the store (previous quantity == 0)
				nRowsUpdated = DBAccessLayer.executeUpdate("INSERT INTO stock(album_id, store_id, quantity, storage_location) " +
						"VALUES("+albumID+","+storeID+","+quantity+","+getRandLocation()+")");
				if (nRowsUpdated < 1){
					Debug.log("DBConnectionStock.GetFromSupplier [ERROR]: DB access failure. Failed to insert values to stock.");
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.PLACE_ORDER_FAILURE);
					return;
				}
			}
			
			Debug.log("DBConnectionStock.GetFromSupplier done working with DB.");
			GuiUpdatesInterface.approveOrderFromSupplierDone();			
		}
	}
}
