package com.live4music.client.ui;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;

import com.live4music.client.tables.AlbumsResultsTableItem;
import com.live4music.client.tables.EmployeesTableItem;
import com.live4music.client.tables.SaleTable;
import com.live4music.client.tables.SaleTableItem;
import com.live4music.server.db.DBConnectionInterface;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;

/**
 * 
 * Sale tab handlers
 */
public class SaleFuncs {
	
	/**
	 * initializes the sale tab view: enabled and disabled fields, default values etc.
	 * /// not needed since it's done by invoking initCurrentSale() from Main after employees
	 * /// table is initialized
	 */
	protected static void initSaleTabView(){
		// disable buttons
		Main.getSaleButtonRemoveItem().setEnabled(false);
		Main.getSaleButtonMakeSale().setEnabled(false);
	}

	/**
	 * initialize search tab listeners
	 */
	public static void initSaleListeners(){
				
		// sale table listener
		Main.getSaleTableSaleItems().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Sale tab: sale item selected",DebugOutput.FILE,DebugOutput.STDOUT);
						// enable remove item
						Main.getSaleButtonRemoveItem().setEnabled(true);
					}
				}
		);
		
		// remove item button
		Main.getSaleButtonRemoveItem().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Sale tab: remove item button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						// remove selected item
						removeItemFromSale();
					}
				}
		);
		
		// clear sale button
		Main.getSaleButtonClearSale().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Sale tab: clear sale button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						// clear sale table
						clearSaleTable();
					}
				}
		);
		
		// make sale button
		Main.getSaleButtonMakeSale().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Sale tab: make sale button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						
						MessageBox makeSaleErrorMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
						makeSaleErrorMsg.setMessage("No employee selected");
						makeSaleErrorMsg.setText("Make a sale Error");
						if(getSelectedSalesman()==null){
							makeSaleErrorMsg.open();
						}
						// check if DB is not busy, else pop a message
						else if (MainFuncs.isAllowDBAction()){
							// flag DB as busy
							MainFuncs.setAllowDBAction(false);
							
							// update sale's time and date
							StaticProgramTables.sale.setTime(MainFuncs.getTime());
							StaticProgramTables.sale.setDate(MainFuncs.getDate());
							// update current sale salesman
							StaticProgramTables.sale.setSalesman(getSelectedSalesman());
							// make sale 
							DBConnectionInterface.makeSale(StaticProgramTables.sale);
							
						} else {
							MainFuncs.getMsgDBActionNotAllowed().open();
						}
					}
				}
		);
	}
	
	////////////////////
	//	Sale handlers //
	////////////////////
	
	// sale table initialization
	////////////////////////////

	/**
	 * clear sale table when sale is done to enable new sale
	 */
	public static void clearSaleTable(){
		// clear current sale data structure
		StaticProgramTables.sale.getSaleItems().clear();
		// clear fields
		Main.getSaleTableSaleItems().removeAll();
		Main.getSaleLabelTotalPriceValue().setText("0");
		Main.getSaleLabelDateInput().setText(MainFuncs.getDate());
		Main.getSaleLabelTimeInput().setText(MainFuncs.getTime());
		// set buttons
		Main.getSaleButtonRemoveItem().setEnabled(false);
		Main.getSaleButtonMakeSale().setEnabled(false);
	}
	
	/**
	 * initialize current sale with current salesman, update date and time
	 */
	public static void initCurrentSale(){
		// save old sale for later
		SaleTable oldSale = StaticProgramTables.sale;
		
		// initialize new sale table
		StaticProgramTables.sale = new SaleTable();
		// set salesman to current salesman
		StaticProgramTables.sale.setSalesman(getSelectedSalesman());

		// update view
		clearSaleTable();
		
		// if albums results aren't null
		if (StaticProgramTables.results != null){
			// update search tab items' quantities for those that were in sale
			Set<Long> albumsSold = oldSale.getSaleItems().keySet();
			boolean foundAtLeastOne = false;
			for (long albumID: albumsSold){
				// check if album appears in search results and its stock info is already fetched
				// (since here album is approved for request, it must have storage location in this store
				// thus if storage location is 0, stock info for this album hasn't been fetched yet in search tab)
				AlbumsResultsTableItem album = StaticProgramTables.results.getAlbum(albumID);
				if ((album != null) && (album.getStorageLocation() != 0)){
					int newQuantity = album.getQuantity()-oldSale.getSaleItem(albumID).getQuantity();
					// update its stock info
					album.setQuantity(newQuantity);
					// flag that found one
					foundAtLeastOne = true;
				}
			}
			
			// if found at least one, unselect any result selected in search tab, and update its view
			if (foundAtLeastOne){
				Main.getSearchTableAlbumResults().deselectAll();
				SearchFuncs.clearStockInfo();
				Main.getSearchButtonGetStockInfo().setEnabled(false);
				Main.getSearchButtonStockInfoOrder().setEnabled(false);
				Main.getSearchButtonSaleInfoSale().setEnabled(false);
			}
		}
		
		// flag DB as free
		MainFuncs.setAllowDBAction(true);
	}
	
	/**
	 * pops up a message that sale is complete
	 */
	public static void popSaleDoneMessage(){
		MessageBox msgSaleComplete = new MessageBox(Main.getMainShell(),SWT.ICON_INFORMATION);
		msgSaleComplete.setText("Sale completed");
		msgSaleComplete.setMessage("Sale has been made successfully");
		msgSaleComplete.open();
	}
	
	/**
	 * updates salesmen list according to current employees table
	 */
	public static void updateSalesmenList(){
		// first clear all salesmen in the list
		Main.getSaleComboSalesmanIDNameInput().removeAll();
		
		// then insert all salesmen (all employees available in the store)
		for(EmployeesTableItem employee: StaticProgramTables.employees.getEmployees().values()){
			Main.getSaleComboSalesmanIDNameInput().add(Integer.toString(employee.getEmployeeID())+
					": "+employee.getFirstName()+" "+employee.getLastName());
		}
		
		// select first in list
		Main.getSaleComboSalesmanIDNameInput().select(0);
	}
	
	/**
	 * returns the integer value of selected salesman ID
	 * (or -1 if encountered a parsing error)
	 * @return
	 */
	protected static int getSalesmanIDFromSelected(){
		String employeeDetails = Main.getSaleComboSalesmanIDNameInput().getText();
		if(employeeDetails==null){
			return -1;
		}
		else if (employeeDetails.trim()==""){
			return -1;
		}
		else{
			StringTokenizer tokenizer = new StringTokenizer(employeeDetails,":");
			try{
				int id = Integer.parseInt(tokenizer.nextToken());
				return id;
			}catch(NumberFormatException nfe){
				Debug.log("*** BUG: SaleFuncs.getSalesmanIDFromSelected - salesman id is not an integer", DebugOutput.FILE, DebugOutput.STDERR);
				return -1;
			}catch(NoSuchElementException nsee){return -1;}
		}
	}
	
	/**
	 * returns selected salesman
	 * @return
	 */
	public static EmployeesTableItem getSelectedSalesman(){
		return StaticProgramTables.employees.getEmployee(getSalesmanIDFromSelected());
	}
	
	// adding and removing sale items
	/////////////////////////////////
	
	public static void updateSaleTableView(){
		int totalSalePrice = 0;
		// first remove all items
		Main.getSaleTableSaleItems().removeAll();
		
		// then insert all items again (updated)
		for(SaleTableItem saleItem: StaticProgramTables.sale.getSaleItems().values()){
			totalSalePrice += saleItem.getTotalPerItem();
			TableItem item = new TableItem(Main.getSaleTableSaleItems(),SWT.NONE);
			String[] entry = new String[]{
					Long.toString(saleItem.getAlbumID()),
					saleItem.getAlbumName(),
					Integer.toString(saleItem.getQuantity()),
					Integer.toString(saleItem.getPricePerItem()),
					Integer.toString(saleItem.getTotalPerItem())
			};
			item.setText(entry);
		}
		
		// update total sale price
		Main.getSaleLabelTotalPriceValue().setText(Integer.toString(totalSalePrice));
		
		// set buttons
		Main.getSaleButtonRemoveItem().setEnabled(false);
		if (StaticProgramTables.sale.getSaleItems().isEmpty()){ // if sale table is empty, disable make sale
			Main.getSaleButtonMakeSale().setEnabled(false);
		} else {
			Main.getSaleButtonMakeSale().setEnabled(true);
		}
	}
	
	/**
	 * adds new sale item to sale table
	 * or updates quantity if sale item already exists
	 * INVOKED ONLY IN  - SEARCH TAB \ ADD TO SALE -  BUTTON
	 * (that's where it gets all the details)
	 */
	public static void addItemToSale(long albumID, String albumName, int quantity, int price){
		// check if sale item already exists
		SaleTableItem saleItem = StaticProgramTables.sale.getSaleItem(albumID);
		
		if (saleItem == null){ // if sale item doesn't exist
			// create new sale item and add to current sale
			saleItem = new SaleTableItem(albumID,albumName,quantity,price);
			StaticProgramTables.sale.addSaleItem(saleItem);
		} else {
			// set new quantity, price and total price
			saleItem.setQuantity(saleItem.getQuantity()+quantity);
		}
		
		// update gui view
		updateSaleTableView();
	}
	
	/**
	 * removes selected sale item from sale table
	 */
	public static void removeItemFromSale(){
		try{
			// remove sale item from sale
			long saleItemAlbumID = Long.parseLong(Main.getSaleTableSaleItems().getSelection()[0].getText());
			StaticProgramTables.sale.getSaleItems().remove(saleItemAlbumID);
		} catch (NumberFormatException nfe){
			System.out.println("*** BUG: SaleFuncs.removeItemFromSale bug");
		}
		
		// update view
		updateSaleTableView();
	}
	
	//////////////////////////
	//	DB failure handling	//
	//////////////////////////
	
	/**
	 * notifies the sale could not be made
	 * and restores gui
	 */
	public static void notifyMakeSaleFailure(){
		MessageBox errMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		errMsg.setText("DB Connection Error");
		errMsg.setMessage("Could not make sale due to a connection error.\n"+
				"Please try again later.");
		// retry connection
		if (errMsg.open() == SWT.OK) {
			// restore gui
			MainFuncs.setAllowDBAction(true);
		}
	}
}
