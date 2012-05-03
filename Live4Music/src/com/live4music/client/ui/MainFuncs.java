package com.live4music.client.ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

import com.live4music.client.tables.*;
import com.live4music.server.db.DBConnectionInterface;
import com.live4music.server.db.DBConnectionPool;
import com.live4music.shared.general.*;
import com.live4music.shared.general.Debug.*;


/**
 * created by Ariel
 * 
 * Main window handlers
 */
public class MainFuncs {
	
	// this flag is false if a DB action runs in background and false otherwise
	// DB actions are allowed one at a time
	public static boolean allowDBAction = false;
	public static MessageBox msgDBActionNotAllowed;
	
	// flags for notifying a table initialization error
	public static boolean errorInitOrders = false;
	public static boolean errorInitRequests = false;
	public static boolean errorInitEmployees = false;
	
	// flags for tables initialization
	public static boolean isOrdersInitialized = false;
	public static boolean isRequestsInitialized = false;
	public static boolean isEmployeesInitialized = false;
	
	/////////////////////////
	//	initialize program //
	/////////////////////////
	
	/**
	 * invoked automatically on program startup
	 * - creates initial connection with DB
	 * - initializes stores list
	 */
	public static void initDBConnection(){
		// create connection
		StaticProgramTables.confMan = new ConfigurationManager(Main.getRelPath()+"db.properties");
		
		DBConnectionInterface.initDBConnection(StaticProgramTables.confMan);
	}
	
	/**
	 * invoked when main window opens
	 * waits until all tables are updated from DB:
	 * - orders
	 * - requests
	 * - employees
	 * then updates gui fields
	 */
	public static void initializeTablesAndFields(){
		// wait until all tables are initialized
		while (!isOrdersInitialized || !isRequestsInitialized || !isEmployeesInitialized){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("*** BUG: sleep failed");
			}
		}
		
		// initialize all fields:
		/////////////////////////
		
		// initialize store details view
		MainFuncs.initStoreDetails();
		// initialize welcome group
		MainFuncs.initWelcomeGroup();
		
		// initialize search tab view
		SearchFuncs.initSearchTabView();
		// initialize search listeners
		SearchFuncs.initSearchListeners();
		
		// initialize sale tab view
		SaleFuncs.initSaleTabView();
		// initialize sale listeners
		SaleFuncs.initSaleListeners();
		
		// initialize orders table values
		StockFuncs.updateOrdersTableView();
		// initialize requests table values
		StockFuncs.updateRequestsTableView();
		// initialize stock tab view
		StockFuncs.initStockTabView();
		// initialize stock tab listeners
		StockFuncs.initStockTabListeners();
		
		// initialize employees table
		ManageFuncs.initManageTabView();
		// initialize sale salesman list
		SaleFuncs.updateSalesmenList();
		Main.getSaleComboSalesmanIDNameInput().select(0);
		// initialize employee tab listeners
		ManageFuncs.initManageListeners();
		// initialize current sale
		// initialized only here, after employees are initialized
		SaleFuncs.initCurrentSale();
	}
	
	public static void initMsgDBActionNotAllowed(){
		msgDBActionNotAllowed = new MessageBox(Main.getMainShell(),SWT.ICON_WARNING);
		msgDBActionNotAllowed.setText("Cannot invoke action");
		msgDBActionNotAllowed.setMessage("Cannot invoke action, DB is busy.\nPlease try again later.");
	}
	
	/**
	 * initialize initDialog listeners 
	 */
	public static void initiDialogBoxListeners(){
		InitialDialog.getInitDialogButtonStart().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Initial Dialog: Start button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						
						// get selected store id
						String storeIDStr = InitialDialog.getInitDialogCombo().getText().split(":")[0];
						try{
							int storeID = Integer.parseInt(storeIDStr);
							// set current store to selected store
							StoresTableItem thisStore = StaticProgramTables.stores.getStore(storeID);
							StaticProgramTables.setThisStore(thisStore);
							
							// update the stores details representation in main window is done in Main
							
							// update orders, requests and employees table
							DBConnectionInterface.getOrdersTable();
							DBConnectionInterface.getRequestsTable();
							DBConnectionInterface.getEmployeesTable(); // will initialize also employees combo box
							
							// close init dialog
							InitialDialog.closeInitDialog();
							
						} catch (NumberFormatException nfe){
							Debug.log("*** DEBUG: MainFuncs initial dialog \\ start listener", DebugOutput.FILE, DebugOutput.STDERR);
						}
					}
				}
		);
		
//		InitialDialog.getInitDialogButtonExit().addSelectionListener(
//				new SelectionAdapter() {
//					public void widgetSelected(SelectionEvent e){
//						Debug.log("Initial Dialog: Exit button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
//						// close all DB connections and log files and exit
//						DBConnectionPool.closeAllConnections();
//						Debug.closeLog();
//						System.exit(-1);
//					}
//				}
//		);
	}
	
	/**
	 * initialize initial dialog combo box
	 */
	public static void initDialogComboBoxItems(){
		// enable combo box
		InitialDialog.getInitDialogCombo().setEnabled(true);
		for(StoresTableItem store : StaticProgramTables.stores.getStores().values()){
			InitialDialog.getInitDialogCombo().add(store.getStoreID()+": "+store.getCity());
		}
		// choose first by default
		InitialDialog.getInitDialogCombo().select(0);
	}
	
	/**
	 * initialize stores details view in main window
	 */
	public static void initStoreDetails(){
		Main.getMainLabelStoreDetailsStoreID().setText("Store: "+StaticProgramTables.getThisStore().getStoreID());
		Main.getMainLabelStoreDetailsStoreAddress().setText("Address: "+
				StaticProgramTables.thisStore.getAddress()+", "+StaticProgramTables.getThisStore().getCity());
		Main.getMainLabelStoreDetailsStorePhone().setText("Phone: "+StaticProgramTables.getThisStore().getPhone());
		Main.getMainLabelStoreDetailsStoreManager().setText("Manager: "+StaticProgramTables.getThisStore().getManagerID());	
	}
	
	/**
	 * tab switching
	 * @param tab
	 */
	public static void switchTab(int tab){
		Main.getMainTabFolder().setSelection(tab);
	}
	
	// string manipulation helper
	public static String replaceSubString(String str, String pattern, String replace) {
	    int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	        result.append(str.substring(s, e));
	        result.append(replace);
	        s = e+pattern.length();
	    }
	    result.append(str.substring(s));
	    return result.toString();
	}
	
	//////////////////////////
	//	DB error handling	//
	//////////////////////////
	
	/**
	 * notifies the user a DB error has occurred and:
	 * - if the action was an initial action, allows him to retry or quit program
	 * - if the action wasn't crucial, notifies and restores gui
	 */
	public static void notifyDBFailure(DBActionFailureEnum failure){		
		switch (failure){
		case DB_CONN_FAILURE:
			// could not initiate connection with DB
			MainFuncs.notifyDBConnectionFailure();
			break;
		case INIT_ORDERS_FAILURE:
			// could not initialize orders table
			StockFuncs.notifyInitOrdersFailure();
			break;
		case INIT_REQUESTS_FAILURE:
			// could not initialize requests table
			StockFuncs.notifyInitRequestsFailure();
			break;
		case INIT_EMP_FAILURE:
			// could not initialize employees table
			ManageFuncs.notifyInitEmployeesFailure();
			break;
		case SEARCH_FAILURE:
			// could not fetch album search results
			SearchFuncs.notifySearchFailure();
			break;
		case GET_STOCK_INFO_FAILURE:
			// could not get album stock information
			SearchFuncs.notifyGetStockInfoFailure();
			break;
		case GET_SONGS_FAILURE:
			// could not get song list
			SearchFuncs.notifyFetchSongsFailure();
			break;
		case MAKE_SALE_FAILURE:
			// could not make sale
			SaleFuncs.notifyMakeSaleFailure();
			break;
		case CHECK_AVAIL_FAILURE:
			// could not get order's available stores
			StockFuncs.notifyOrderAvailableStoresFailure();
			break;
		case PLACE_ORDER_FAILURE:
			// could not place new order
			StockFuncs.notifyPlaceOrderFailure();
			break;
		case ORDERS_ACTION_FAILURE:
			// could not refresh / remove / cancel orders
			StockFuncs.notifyOrdersActionFailure();
			break;
		case REQUESTS_ACTION_FAILURE:
			// could not deny / approve requests
			StockFuncs.notifyRequestsActionFailure();
			break;
		case UPDATE_DB_FAILURE:
			// could not update database
			ManageFuncs.notifyDBUpdateFailure();
			break;
		case INSERT_SAVE_EMP_FAILURE:
			// could not save / insert employee
			ManageFuncs.notifySaveInsertEmployeeFailure();
			break;
		case REM_EMP_FAILURE:
			// could not remove employee
			ManageFuncs.notifyRemoveEmployeeFailure();
			break;
		default:
			Debug.log("*** BUG: MainFuncs.notifyDBFailure bug", DebugOutput.FILE, DebugOutput.STDERR);
		}
	}
	
	/**
	 * notifies the user the could not initiate a connection with the DB
	 * and allows retry or exit program
	 */
	public static void notifyDBConnectionFailure(){
		MessageBox errMsg = new MessageBox(InitialDialog.getDialogShell(),SWT.ICON_ERROR | SWT.YES | SWT.NO);
		errMsg.setText("DB Connection Error");
		errMsg.setMessage("Could not initiate connection with the data-base.\n"+
				"Click yes to retry connect or no to quit.");
		// retry connection
		if (errMsg.open() == SWT.YES){
			DBConnectionInterface.initDBConnection(StaticProgramTables.confMan);
		}
		// otherwise quit program
		else {
			// close all DB connections and log files and exit
			DBConnectionPool.closeAllConnections();
			Debug.closeLog();
			System.exit(-1);
		}
	}

	
	//////////////////////////////
	//	Date and Time getters	//
	//////////////////////////////
	
	 /**
	 * date getter
	 * @return string: current date in format dd/MM/yyyy
	 */
	public static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
	/**
	 * date getter
	 * @return string: current day
	 */
	public static String getDay(){
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
	/**
	 * date getter
	 * @return string: current time in format HH:mm
	 */
	public static String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("kk:mm");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        if(dateStr.startsWith("24"))
        	dateStr = dateStr.replaceFirst("24", "00");
        return dateStr;
    }
	
	//////////////
	//	Welcome	//
	//////////////
	
	public static void initWelcomeGroup(){
		Main.getMainLabelWelcomeText().setText(
				"Live 4 Music\n"+
				"The best music store 4 you!"
				);
	}

	public static boolean isAllowDBAction() {
		return allowDBAction;
	}

	public static void setAllowDBAction(boolean allowDBAction) {
		MainFuncs.allowDBAction = allowDBAction;
	}

	public static MessageBox getMsgDBActionNotAllowed() {
		return msgDBActionNotAllowed;
	}

	public static void setMsgDBActionNotAllowed(MessageBox msgDBActionNotAllowed) {
		MainFuncs.msgDBActionNotAllowed = msgDBActionNotAllowed;
	}

	public static boolean isOrdersInitialized() {
		return isOrdersInitialized;
	}

	public static void setOrdersInitialized(boolean isOrdersInitialized) {
		MainFuncs.isOrdersInitialized = isOrdersInitialized;
	}

	public static boolean isRequestsInitialized() {
		return isRequestsInitialized;
	}

	public static void setRequestsInitialized(boolean isRequestsInitialized) {
		MainFuncs.isRequestsInitialized = isRequestsInitialized;
	}

	public static boolean isEmployeesInitialized() {
		return isEmployeesInitialized;
	}

	public static void setEmployeesInitialized(boolean isEmployeesInitialized) {
		MainFuncs.isEmployeesInitialized = isEmployeesInitialized;
	}
}
