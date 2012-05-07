package com.live4music.client.tables;

import java.util.*;

import com.live4music.client.ui.*;
import com.live4music.server.queries.*;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;


/**
 * 
 * holds examples of tables classes, for debugging
 */
public class TablesExamples {

	// for error checking
	public static boolean debugInitDBConn = false;
	public static int initConnCounter = 0;
	public static boolean debugInitOrders = false;
	public static boolean debugInitRequests = false;
	public static boolean debugInitEmployees = false;
	public static boolean debugSearch = false;
	public static int searchCounter = 0;
	public static boolean debugSongList = false;
	public static int songListCounter = 0;
	public static boolean debugSale = false;
	public static int saleCounter = 0;
	public static boolean debugAvailableStores = false;
	public static int availableStoresCounter = 0;
	public static boolean debugPlaceOrder = false;
	public static int placeOrerCounter = 0;
	public static boolean debugOrdersAction = false;
	public static int ordersActionCounter = 0;
	public static boolean debugRequestsAction = false;
	public static int requestsActionCounter = 0;
	public static boolean debugUpdateDB = false;
	public static int updateDBCounter = 0;
	public static boolean debugInsertSaveEmployee = false;
	public static int insertSaveEmployeeCounter = 0;
	public static boolean debugRemEmployee = false;
	public static int remEmployeeCounter = 0;
	public static boolean debugGetStockInfo = false;
	public static int getStockInfoCounter = 0;
	
	public static AlbumsResultsTable albumsResultsTableExample = new AlbumsResultsTable();
	public static SongsResultsTable songsResultsTableExample1;
	public static SongsResultsTable songsResultsTableExample2;
	public static StoresTable storesTableExample = new StoresTable();
	public static OrdersOrRequestsTable ordersTableExample = new OrdersOrRequestsTable(true);
	public static OrdersOrRequestsTable requestsTableExample = new OrdersOrRequestsTable(false);
	public static List<SaleTable> sales = new ArrayList<SaleTable>();
	public static OrderAvailableStoresTable orderAvailableStoresExample = new OrderAvailableStoresTable();
	public static EmployeesTable employees = new EmployeesTable(0);
	public static int ordersRequestsCounter = 7;
	
	/**
	 * initialize tables examples
	 */
	public static void initTablesExamples() {
		
		// albums and songs results table example
		
		songsResultsTableExample1 = new SongsResultsTable(1);
		songsResultsTableExample1.addSong(1, "You are my Sima", "Shimon", 180);
		songsResultsTableExample1.addSong(2,"Here in Ashdod", "Shimon", 200);
		
		AlbumsResultsTableItem albumsResultsItem1 = new AlbumsResultsTableItem(1, "Shimon Comes Live",
				"Shimon", 1998, "Rock", 1800, null, 60, -1, -1);
		
		songsResultsTableExample2 = new SongsResultsTable(2);
		songsResultsTableExample2.addSong(1, "You are my Sima", "Shimon", 180);
		songsResultsTableExample2.addSong(2,"Who is your Sami", "Shimon feat. Sami", 200);
		songsResultsTableExample2.addSong(3,"Who is your Susu", "Shimon feat. Susu", 150);
		
		AlbumsResultsTableItem albumsResultsItem2 = new AlbumsResultsTableItem(2, "Shimon and Friends",
				"Various", 1995, "Rock", 2000, null, 49, -1, -1);
		
		albumsResultsTableExample.addAlbum(albumsResultsItem1);
		albumsResultsTableExample.addAlbum(albumsResultsItem2);
		
		// stores table example
		
		StoresTableItem store1 = new StoresTableItem(0,"Tel-Aviv","Ben Yehuda 2","03-67890123",12345678);
		StoresTableItem store2 = new StoresTableItem(1,"Jerusalem","King George 4","02-56789012",87654321);
		StoresTableItem store3 = new StoresTableItem(2,"Haifa","Carmel 8","04-45678901",19283746);
		
		storesTableExample.addStore(store1);
		storesTableExample.addStore(store2);
		storesTableExample.addStore(store3);
		
		// order's available stores example
		
		OrderAvailableStoresTableItem availableStore1 = new OrderAvailableStoresTableItem(store2.getStoreID(),
				store2.getCity(),3);
		OrderAvailableStoresTableItem availableStore2 = new OrderAvailableStoresTableItem(store3.getStoreID(),
				store3.getCity(),2);
		
		orderAvailableStoresExample.addStore(availableStore1);
		orderAvailableStoresExample.addStore(availableStore2);
		
		// orders table example
		
		OrdersOrRequestsTableItem order1 = new OrdersOrRequestsTableItem(1,store1.getStoreID(),
				store2.getStoreID(),albumsResultsItem1.getAlbumID(),2,"12/04/08",OrderStatusEnum.WAITING);
		OrdersOrRequestsTableItem order2 = new OrdersOrRequestsTableItem(2,store1.getStoreID(),
				store3.getStoreID(),albumsResultsItem2.getAlbumID(),19,"15/08/09",OrderStatusEnum.DENIED);
		OrdersOrRequestsTableItem order3 = new OrdersOrRequestsTableItem(3,store1.getStoreID(),
				store2.getStoreID(),albumsResultsItem1.getAlbumID(),5,"30/01/09",OrderStatusEnum.COMPLETED);
		
		ordersTableExample.addOrder(order1);
		ordersTableExample.addOrder(order2);
		ordersTableExample.addOrder(order3);
		
		// requests table example
		
		OrdersOrRequestsTableItem request1 = new OrdersOrRequestsTableItem(4,store2.getStoreID(),
				store1.getStoreID(),albumsResultsItem1.getAlbumID(),4,"01/03/09",OrderStatusEnum.WAITING);
		OrdersOrRequestsTableItem request2 = new OrdersOrRequestsTableItem(5,store3.getStoreID(),
				store1.getStoreID(),albumsResultsItem2.getAlbumID(),9,"23/10/09",OrderStatusEnum.WAITING);
		OrdersOrRequestsTableItem request3 = new OrdersOrRequestsTableItem(6,store2.getStoreID(),
				store1.getStoreID(),albumsResultsItem2.getAlbumID(),2,"11/11/09",OrderStatusEnum.WAITING);
		
		requestsTableExample.addOrder(request1);
		requestsTableExample.addOrder(request2);
		requestsTableExample.addOrder(request3);
		
		// employees table example
		
		EmployeesTableItem employee1 = new EmployeesTableItem(123456789, "Test1", "Test1",
				"13/03/2012", "14/08/1989", "41 108th St., New York", "03-3232323", "054-2332323", 0,
				EmployeePositionsEnum.NETWORK_MANAGER);
		EmployeesTableItem employee2 = new EmployeesTableItem(987654253, "Test2", "Test2",
				"21/02/2001", "07/08/1991", "86 44th St., New York", "08-323123", "054-4234234", 0,
				EmployeePositionsEnum.MANAGER);
		EmployeesTableItem employee3 = new EmployeesTableItem(928347462, "Test3", "Test3",
				"02/04/2012", "02/04/1982", "123 Trafalgar Square, London", "03-32132435", "054-432423", 0,
				EmployeePositionsEnum.ASSIST_MANAGER);
		EmployeesTableItem employee4 = new EmployeesTableItem(199292919, "Test4", "Test4",
				"01/04/2011", "13/07/1992", "23 1st St., New York", "03-432423", "054-4324", 0,
				EmployeePositionsEnum.SALESMAN);
		
		employees.addEmployee(employee1);
		employees.addEmployee(employee2);
		employees.addEmployee(employee3);
		employees.addEmployee(employee4);
	}
	
	/**
	 * invoke GUI albums search results update with example
	 */
	public static void getAlbumsSearchResults(){
		waitSome(2000);
		if (debugSearch){
			if (searchCounter < 1){
				searchCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.SEARCH_FAILURE);
			} else GuiUpdatesInterface.updateAlbumResultsTable(albumsResultsTableExample,55);
		} else GuiUpdatesInterface.updateAlbumResultsTable(albumsResultsTableExample,55);
	}
	
	/**
	 * invoke GUI songs results update with example
	 * @param albumID
	 */
	public static void getSongsResults(long albumID){
		waitSome(1000);
		if (debugSongList){
			if (songListCounter < 2){
				songListCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.GET_SONGS_FAILURE);
			}else {
				if (albumID == 1)
					GuiUpdatesInterface.updateSongsResultsTable(albumID, songsResultsTableExample1);
				else // albumID == 2
					GuiUpdatesInterface.updateSongsResultsTable(albumID, songsResultsTableExample2);
			}
		} else {
			if (albumID == 1)
				GuiUpdatesInterface.updateSongsResultsTable(albumID, songsResultsTableExample1);
			else // albumID == 2
				GuiUpdatesInterface.updateSongsResultsTable(albumID, songsResultsTableExample2);
		}		
	}
	
	/**
	 * invoke GUI stores table initialize with example
	 */
	public static void initStoresTable(){
		waitSome(500);
		if (debugInitDBConn){
			if (initConnCounter < 1){
				initConnCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.DB_CONN_FAILURE);
			} else GuiUpdatesInterface.initStoresTable(storesTableExample);
		} else GuiUpdatesInterface.initStoresTable(storesTableExample);
	}
	
	/**
	 * invoke make sale as example
	 * real method should insert given sale to data base
	 * @param sale
	 */
	public static void makeSale(SaleTable sale){
		waitSome(1000);
		if (debugSale){
			if (saleCounter < 1){
				saleCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.MAKE_SALE_FAILURE);
			}
			else {
				sales.add(sale);
				GuiUpdatesInterface.initSaleTable();
			}
		} else {
			sales.add(sale);
			GuiUpdatesInterface.initSaleTable();
		}
	}
	
	/**
	 * invoke GUI order's available stores update with example
	 * @param albumID
	 */
	public static void getOrderAvailableStores(OrderAvailableStoresQuery query){
		waitSome(1000);
		if (debugAvailableStores){
			if (availableStoresCounter < 2){
				availableStoresCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.CHECK_AVAIL_FAILURE);
			} else GuiUpdatesInterface.updateOrderAvailableStores(orderAvailableStoresExample);
		} else GuiUpdatesInterface.updateOrderAvailableStores(orderAvailableStoresExample);
	}
	
	/**
	 * invoke orders table initialize with example
	 */
	public static void getOrdersTable(){
		waitSome(1000);
		if (debugInitOrders){
			GuiUpdatesInterface.initOrdersTable(new OrdersOrRequestsTable(true));
			GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_ORDERS_FAILURE);
		}
		else GuiUpdatesInterface.initOrdersTable(ordersTableExample);
	}
	
	/**
	 * invoke refresh orders in gui
	 */
	public static void refreshOrdersTable(){
		waitSome(1000);
		if (debugOrdersAction){
			if (ordersActionCounter < 4){
				ordersActionCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
			} else GuiUpdatesInterface.refreshOrdersTable(ordersTableExample);
		} else GuiUpdatesInterface.refreshOrdersTable(ordersTableExample);
	}
	
	/**
	 * invoke remove order from orders table in gui
	 * @param orderID
	 */
	public static void removeOrder(int orderID){
		waitSome(1000);
		if (debugOrdersAction){
			if (ordersActionCounter < 4){
				ordersActionCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
			} else GuiUpdatesInterface.removeOrder(orderID);
		} else GuiUpdatesInterface.removeOrder(orderID);
	}
	
	/**
	 * invoke add order to orders table
	 * @param order
	 */
	public static void placeOrder(OrdersOrRequestsTableItem order){
		waitSome(1000);
		if (debugPlaceOrder){
			if (placeOrerCounter < 2){
				placeOrerCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.PLACE_ORDER_FAILURE);
			} else {
				// give order its id and update orders-requests counter
				order.setOrderID(ordersRequestsCounter++);
				GuiUpdatesInterface.addOrder(order);
			}
		} else {
			// give order its id and update orders-requests counter
			order.setOrderID(ordersRequestsCounter++);
			GuiUpdatesInterface.addOrder(order);
		}
	}
	
	/**
	 * invoke approve order from supplier is done
	 * @param albumID
	 * @param quantity
	 */
	public static void placeOrderFromSupplier(long albumID, int quantity){
		waitSome(1000);
		GuiUpdatesInterface.approveOrderFromSupplierDone();
	}
	
	public static void updateOrderStatus(int orderID, OrderStatusEnum status){
		waitSome(1000);
		boolean isApproved = (status == OrderStatusEnum.COMPLETED); 
		
		if (status == OrderStatusEnum.CANCELED) // called by orders
			if (debugOrdersAction){
				if (ordersActionCounter < 4){
					ordersActionCounter++;
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.ORDERS_ACTION_FAILURE);
				} else GuiUpdatesInterface.updateOrderStatus(orderID, status);
			} else GuiUpdatesInterface.updateOrderStatus(orderID, status);
		else { // called by requests
			if (debugRequestsAction){
				if (requestsActionCounter < 3){
					requestsActionCounter++;
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
				} else GuiUpdatesInterface.removeRequest(orderID,isApproved);
			} else GuiUpdatesInterface.removeRequest(orderID,isApproved);
		}
	}
	
	/**
	 * invoke requests table initialize with example
	 */
	public static void getRequestsTable(){
		waitSome(1000);
		if (debugInitRequests){
			GuiUpdatesInterface.initRequestsTable(new OrdersOrRequestsTable(false));
			GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_REQUESTS_FAILURE);
		}
		else GuiUpdatesInterface.initRequestsTable(requestsTableExample);		
	}
	
	/**
	 * invoke refresh requests in gui
	 */
	public static void refreshRequestsTable(){
		waitSome(1000);
		if (debugRequestsAction){
			if (requestsActionCounter < 3){
				requestsActionCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
			} else GuiUpdatesInterface.refreshRequestsTable(requestsTableExample);
		} else GuiUpdatesInterface.refreshRequestsTable(requestsTableExample);
	}
	
	/**
	 * invoke get album stock information
	 * @param albumID
	 * @param caller
	 */
	public static void getAlbumStockInfo(long albumID, AlbumStockInfoCallerEnum caller){
		waitSome(1000);
		// called from stock
		if (caller == AlbumStockInfoCallerEnum.CALLED_BY_APPROVE_REQUEST){
			if (debugGetStockInfo){
				if (getStockInfoCounter < 2){
					getStockInfoCounter ++;
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REQUESTS_ACTION_FAILURE);
				} else {
					if (albumID == 1) GuiUpdatesInterface.updateAlbumStockInformation(albumID, 17, 1,
							AlbumStockInfoCallerEnum.CALLED_BY_APPROVE_REQUEST);
					else GuiUpdatesInterface.updateAlbumStockInformation(albumID, 43, 56, // album id = 2
							AlbumStockInfoCallerEnum.CALLED_BY_APPROVE_REQUEST);			
				}
			} else {
				if (albumID == 1) GuiUpdatesInterface.updateAlbumStockInformation(albumID, 17, 1,
						AlbumStockInfoCallerEnum.CALLED_BY_APPROVE_REQUEST);
				else GuiUpdatesInterface.updateAlbumStockInformation(albumID, 43, 56, // album id = 2
						AlbumStockInfoCallerEnum.CALLED_BY_APPROVE_REQUEST);
			}
		}
		
		// called from search
		if (caller == AlbumStockInfoCallerEnum.CALLED_BY_SEARCH_RESULT){
			if (debugGetStockInfo){
				if (getStockInfoCounter < 2){
					getStockInfoCounter ++;
					GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.GET_STOCK_INFO_FAILURE);
				} else {
					if (albumID == 1) GuiUpdatesInterface.updateAlbumStockInformation(albumID, 17, 1,
							AlbumStockInfoCallerEnum.CALLED_BY_SEARCH_RESULT);
					else GuiUpdatesInterface.updateAlbumStockInformation(albumID, 43, 56, // album id = 2
							AlbumStockInfoCallerEnum.CALLED_BY_SEARCH_RESULT);			
				}
			} else {
				if (albumID == 1) GuiUpdatesInterface.updateAlbumStockInformation(albumID, 17, 1,
						AlbumStockInfoCallerEnum.CALLED_BY_SEARCH_RESULT);
				else GuiUpdatesInterface.updateAlbumStockInformation(albumID, 43, 56, // album id = 2
						AlbumStockInfoCallerEnum.CALLED_BY_SEARCH_RESULT);
			}
		}		
	}
	
	// Management tab
	
	/**
	 * invoke employees table initialize with example
	 */
	public static void getEmployeesTable(){
		waitSome(1000);
		if (debugInitEmployees){
			GuiUpdatesInterface.initEmployeesTable(new EmployeesTable(StaticProgramTables.thisStore.getStoreID()));
			GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INIT_EMP_FAILURE);
		}
		else GuiUpdatesInterface.initEmployeesTable(employees);
	}

	/**
	 * check if employee exists in network
	 * this example returns "false" always, as if the employee doesn't exist in DB
	 * @param employeeID
	 */
	public static void checkIfEmployeeExists(int employeeID){
		waitSome(1000);
		GuiUpdatesInterface.tryInsertNewEmployee(employeeID, false);
	}
	
	/**
	 * inserts employee to "DB" if new, or updates details if employee exists
	 * @param employee
	 */
	public static void insertUpdateEmployee(EmployeesTableItem employee){
		waitSome(1000);
		if (debugInsertSaveEmployee){
			if (insertSaveEmployeeCounter < 2){
				insertSaveEmployeeCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.INSERT_SAVE_EMP_FAILURE);
			} else {
				if (employees.getEmployee(employee.getEmployeeID()) != null) // employee exists, remove it
					employees.getEmployees().remove(employee.getEmployeeID());
				// now insert new one
				employees.addEmployee(employee);
				
				// update store's employees list
				GuiUpdatesInterface.insertUpdateEmployee(employee);
			}
		} else {
			if (employees.getEmployee(employee.getEmployeeID()) != null) // employee exists, remove it
				employees.getEmployees().remove(employee.getEmployeeID());
			// now insert new one
			employees.addEmployee(employee);
			
			// update store's employees list
			GuiUpdatesInterface.insertUpdateEmployee(employee);
		}
	}
	
	/**
	 * remove employee from gui
	 * @param employeeID
	 */
	public static void removeEmployee(int employeeID){
		waitSome(1000);
		if (debugRemEmployee){
			if (remEmployeeCounter < 2){
				remEmployeeCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.REM_EMP_FAILURE);
			} else GuiUpdatesInterface.removeEmployee(employeeID);
		} else GuiUpdatesInterface.removeEmployee(employeeID);
	}
	
	/**
	 * notify gui that update is complete
	 * @param filename
	 */
	public static void updateDataBase(String filename){
		waitSome(3000);
		if (debugUpdateDB){
			if (updateDBCounter < 2){
				updateDBCounter++;
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.UPDATE_DB_FAILURE);
			} else GuiUpdatesInterface.notifyDataBaseUpdated(filename); 
		} else GuiUpdatesInterface.notifyDataBaseUpdated(filename);
	}
	
	/**
	 * sleep some for testing purposes
	 * @param milisecs
	 */
	public static void waitSome(int milisecs){
		try {
			Debug.log("<< TablesExamples: waiting some... >>",DebugOutput.FILE,DebugOutput.STDOUT);
			// simulate DB action
			Thread.sleep(milisecs);
		} catch (InterruptedException e) {
			// sleep didn't work
			System.err.println("*** BUG: Sleep didn't work...");
		}
	}
}
