package com.live4music.client.ui;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.*;

import com.live4music.client.tables.*;
import com.live4music.server.db.*;
import com.live4music.server.queries.*;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;


/**
 * created by Ariel
 * 
 * Search tab handlers
 */
public class SearchFuncs {

	/**
	 * initializes the search tab view: enabled and disabled fields, default values etc.
	 */
	protected static void initSearchTabView(){
		// set search parameters group to be set on "by album id"
		switchAlbumSearchBullet(true);
		Main.getSearchButtonStockInfoOrder().setEnabled(false);
		Main.getSearchButtonSaleInfoSale().setEnabled(false);
		Main.getSearchButtonShowSongs().setEnabled(false);
		Main.getSearchButtonGetStockInfo().setEnabled(false);
		// set progress visibility off
		showDBProgress(false);
	}

	/**
	 * initialize search tab listeners
	 */
	protected static void initSearchListeners(){
		
		/////////////////////////////
		// Search parameters group //
		/////////////////////////////
		
		// search bullets listeners
		///////////////////////////
		
		Main.getSearchBulletByAlbum().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: by Album Selected",DebugOutput.FILE,DebugOutput.STDOUT);
						// enable album ID and disable other parameters text boxes
						switchAlbumSearchBullet(true);
					}
				}
		);
		
		Main.getSearchBulletOtherParameters().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: by Other Parameters Selected",DebugOutput.FILE,DebugOutput.STDOUT);
						// disable album ID and enable other parameters text boxes
						switchAlbumSearchBullet(false);
					}
				}
		);
		
		// search check boxes listeners
		///////////////////////////////
		
		Main.getSearchCheckBoxAlbumName().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: album name checkbox changed",DebugOutput.FILE,DebugOutput.STDOUT);
						// change album name text box accordingly
						setSearchAlbumNameState();
					}
				}
		);
		
		Main.getSearchCheckBoxArtist().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: artist checkbox changed",DebugOutput.FILE,DebugOutput.STDOUT);
						// change artist text box accordingly
						setSearchArtistState();
					}
				}
		);
		
		Main.getSearchCheckBoxYear().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: year checkbox changed",DebugOutput.FILE,DebugOutput.STDOUT);
						// change artist text box accordingly
						setSearchYearState();
					}
				}
		);
		
		Main.getSearchCheckBoxSongNames().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: song names checkbox changed",DebugOutput.FILE,DebugOutput.STDOUT);
						// change artist text box accordingly
						setSearchSongNamesState();
					}
				}
		);
		
		Main.getSearchCheckBoxGenres().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: genres checkbox changed",DebugOutput.FILE,DebugOutput.STDOUT);
						// change artist text box accordingly
						setSearchGenresState();
					}
				}
		);
		
		Main.getSearchCheckBoxGenreOther().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: other genre checkbox changed",DebugOutput.FILE,DebugOutput.STDOUT);
						// change artist text box accordingly
						setSearchGenreOtherState();
					}
				}
		);
		
		// clear / search buttons
		/////////////////////////
		
		// clear fields
		Main.getSearchButtonClear().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: clear fields button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						// clear all search fields
						clearSearchFields();
					}
				}
		);
		
		// search
		Main.getSearchButtonSearch().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: search button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						
						// create AlbumSearchQuery
						AlbumSearchQuery q = createAlbumSearchQuery();
						if (q != null)
						{
							// check if DB is not busy, else pop a message
							if (MainFuncs.isAllowDBAction()){
								// flag DB as busy
								MainFuncs.setAllowDBAction(false);
								
								// set gui environment
								setEnvSearchInvoked();
								// set progress visibility
								showDBProgress(true);
								// send query to DB
								DBConnectionInterface.getAlbumsSearchResults(q);
								
							} else {
								MainFuncs.getMsgDBActionNotAllowed().open();
							}
							
						}
					}
				}
		);
		
		//////////////////////////
		// Search results group //
		//////////////////////////
		
		// albums table listener
		Main.getSearchTableAlbumResults().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: album result selected",DebugOutput.FILE,DebugOutput.STDOUT);
						
						// get selected album table item
						Long albumID = Long.valueOf(Main.getSearchTableAlbumResults().getSelection()[0].getText(0));
						AlbumsResultsTableItem album = StaticProgramTables.results.getAlbum(albumID);
						// enable update songs table button
						Main.getSearchButtonShowSongs().setEnabled(true);
						// enable get stock information
						Main.getSearchButtonGetStockInfo().setEnabled(true);
						// update stock information
						setLabelPrice(Integer.toString(album.getPrice()));
						
						// update song list view (will be empty if haven't been brought yet)
						if (album.getSongs() == null){
							Main.getSearchTableSongResults().removeAll();
						} else {
							updateSongsResultsTableView(album.getSongs());
						}
						
						// check if stock info is brought from the DB
						if (album.getStorageLocation() > 0){
							setLabelStockLocation(Long.toString(album.getStorageLocation()));
							setLabelStoreStock(Integer.toString(album.getQuantity()));
							Main.getSearchButtonStockInfoOrder().setEnabled(true);
							// enable add to sale
							Main.getSearchButtonSaleInfoSale().setEnabled(true);
						}
						// else enable only get stock info button
						else {
							setLabelStockLocation("");
							setLabelStoreStock("");
							Main.getSearchButtonStockInfoOrder().setEnabled(false);
							// disable add to sale
							Main.getSearchButtonSaleInfoSale().setEnabled(false);
						}
					}
				}
		);
		
		// albums table listener
		Main.getSearchButtonShowSongs().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: show song list button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						
						// get selected album table item
						Long albumID = Long.valueOf(Main.getSearchTableAlbumResults().getSelection()[0].getText());
						AlbumsResultsTableItem album = StaticProgramTables.results.getAlbum(albumID);
						// update songs table
						getSongsResultsTable(album);
					}
				}
		);
		
		//////////////////////
		// Stock info group //
		//////////////////////
		
		// get stock information button
		Main.getSearchButtonGetStockInfo().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: get stock information button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						
						getAlbumStockInfoInvokation();
						
					}
				}
		);
		
		// add to order button
		Main.getSearchButtonStockInfoOrder().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: add to order button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						// clear order fields
						StockFuncs.clearFieldsButtonInvokation();
						
						// set order parameters to selected album
						Long albumID = Long.valueOf(Main.getSearchTableAlbumResults().getSelection()[0].getText());
						AlbumsResultsTableItem selectedAlbum = StaticProgramTables.results.getAlbum(albumID);
						StockFuncs.setOrderFields(albumID, selectedAlbum.getStorageLocation(),
								selectedAlbum.getQuantity(), selectedAlbum.getPrice());
						
						// enable check availability button in stock tab and place order from supplier
						Main.getStockButtonCheckAvailability().setEnabled(true);
						Main.getStockButtonPlaceOrderSupplier().setEnabled(true);
						
						// move to stock tab
						MainFuncs.switchTab(3);
					}
				}
		);
		
		///////////////////////
		// Add to sale group //
		///////////////////////
		
		// add to sale button listener
		Main.getSearchButtonSaleInfoSale().addSelectionListener(
				new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e){
						Debug.log("Search tab: add to sale button clicked",DebugOutput.FILE,DebugOutput.STDOUT);
						// check if quantity ok
						
						checkAndAddSaleItem();
					}
				}
		);
	}
	
	/*****************************************************
	 * 						HANDLERS
	 * 
	 * all methods used to handle events in the search tab
	 * 
	 * ***************************************************
	 */
	
	//////////////////////////////
	// handle search parameters //
	//////////////////////////////
	
	/**
	 * sets search tab \ search by fields according to enableByAlbumID:
	 * - if true: by album id enabled, by other parameters disabled
	 * - if false: by other parameters disabled, by album id enabled
	 * @param enableByAlbumID
	 */
	protected static void switchAlbumSearchBullet(boolean enableByAlbumID){
		// set other parameters search fields by enableByAlbumID
		Main.getSearchCheckBoxAlbumName().setEnabled(!enableByAlbumID);
		Main.getSearchTextBoxAlbumName().setEnabled(!enableByAlbumID);
		Main.getSearchCheckBoxArtist().setEnabled(!enableByAlbumID);
		Main.getSearchTextBoxArtist().setEnabled(!enableByAlbumID);
		Main.getSearchCheckBoxYear().setEnabled(!enableByAlbumID);
		Main.getSearchTextBoxYearFrom().setEnabled(!enableByAlbumID);
		Main.getSearchTextBoxYearTo().setEnabled(!enableByAlbumID);
		Main.getSearchCheckBoxSongNames().setEnabled(!enableByAlbumID);
		Main.getSearchTextBoxSongNames().setEnabled(!enableByAlbumID);
		Main.getSearchCheckBoxGenres().setEnabled(!enableByAlbumID);
		Button[] genres = Main.getSearchCheckBoxGenresArr();
		for (Button g: genres) g.setEnabled(!enableByAlbumID);
		Main.getSearchCheckBoxGenreOther().setEnabled(!enableByAlbumID);
		Main.getSearchTextBoxGenreOther().setEnabled(!enableByAlbumID);
		// set album id search fields by enableByAlbumID
		Main.getSearchTextBoxAlbumID().setEnabled(enableByAlbumID);
		
		// in case other parameters are enabled, set text boxes according to their checkboxes
		// otherwise it should be disabled anyway
		if (!enableByAlbumID){
			setSearchAlbumNameState();
			setSearchArtistState();
			setSearchYearState();
			setSearchSongNamesState();
			setSearchGenresState();
			setSearchGenreOtherState();
		}
	}
	
	/**
	 * set album name text box by its checkbox state
	 */
	protected static void setSearchAlbumNameState(){
		boolean isEnabled = Main.getSearchCheckBoxAlbumName().getSelection();
		Main.getSearchTextBoxAlbumName().setEnabled(isEnabled);
	}
	
	/**
	 * set artist text box by its checkbox state
	 */
	protected static void setSearchArtistState(){
		boolean isEnabled = Main.getSearchCheckBoxArtist().getSelection();
		Main.getSearchTextBoxArtist().setEnabled(isEnabled);
	}
	
	/**
	 * set year text boxes (from, to) by its checkbox state
	 */
	protected static void setSearchYearState(){
		boolean isEnabled = Main.getSearchCheckBoxYear().getSelection();
		Main.getSearchTextBoxYearFrom().setEnabled(isEnabled);
		Main.getSearchTextBoxYearTo().setEnabled(isEnabled);
	}
	
	/**
	 * set song names text box by its checkbox state
	 */
	protected static void setSearchSongNamesState(){
		boolean isEnabled = Main.getSearchCheckBoxSongNames().getSelection();
		Main.getSearchTextBoxSongNames().setEnabled(isEnabled);
	}
	
	/**
	 * set genres checkboxes by its the genres checkbox state
	 */
	protected static void setSearchGenresState(){
		boolean isEnabled = Main.getSearchCheckBoxGenres().getSelection();
		Button[] genres = Main.getSearchCheckBoxGenresArr();
		for (Button g: genres) g.setEnabled(isEnabled);
		Main.getSearchCheckBoxGenreOther().setEnabled(isEnabled);
		setSearchGenreOtherState();
	}
	
	/**
	 * set other genre text box by its checkbox state
	 */
	protected static void setSearchGenreOtherState(){
		boolean isEnabled = Main.getSearchCheckBoxGenreOther().getSelection() &&	// other genre check box is on
							Main.getSearchCheckBoxGenres().getSelection();			// and genres check box is on
		Main.getSearchTextBoxGenreOther().setEnabled(isEnabled);
	}
	
	////////////////////////////
	//	handle search buttons //
	////////////////////////////
	
	/**
	 * clear all search fields
	 */
	protected static void clearSearchFields(){
		// clear all text boxes
		Main.getSearchTextBoxAlbumID().setText("");
		Main.getSearchTextBoxAlbumName().setText("");
		Main.getSearchTextBoxArtist().setText("");
		Main.getSearchTextBoxGenreOther().setText("");
		Main.getSearchTextBoxSaleInfoQuantity().setText("1");
		Main.getSearchTextBoxSongNames().setText("");
		Main.getSearchTextBoxYearFrom().setText("");
		Main.getSearchTextBoxYearTo().setText("");
		Main.getSearchTextBoxGenreOther().setText("");
		// clear all check boxes in genres
		for(Button g: Main.getSearchCheckBoxGenresArr()){
			g.setSelection(false);
		}
		Main.getSearchCheckBoxGenreOther().setSelection(false);
		Main.getSearchTextBoxGenreOther().setEnabled(false);
	}
	
	/**
	 * returns current search query or pops a warning if search query is illegal
	 * @return
	 */
	protected static AlbumSearchQuery createAlbumSearchQuery(){
		try{
			AlbumSearchQuery q = new AlbumSearchQuery();
			return q;
		}catch(QueryErrorException qee){
			MessageBox queryErrorMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
			queryErrorMsg.setMessage(qee.getMessage());
			queryErrorMsg.setText("Query Error");
			queryErrorMsg.open();
			return null;
		}
	}
	
	/**
	 * sets the gui environment when search is invoked: disables search button, clears tables
	 * and disables stock and sale buttons from search tab
	 */
	public static void setEnvSearchInvoked(){
		// buttons:
		Main.getSearchButtonSearch().setEnabled(false);
		Main.getSearchButtonStockInfoOrder().setEnabled(false);
		Main.getSearchButtonSaleInfoSale().setEnabled(false);
		Main.getSearchButtonGetStockInfo().setEnabled(false);
		Main.getSearchButtonShowSongs().setEnabled(false);

		// clear all results if search is invoked
		Main.getSearchTableAlbumResults().removeAll();
		Main.getSearchTableSongResults().removeAll();
		clearStockInfo();
	}
	
	//////////////////////////////
	//	handle album results	//
	//////////////////////////////
	
	/**
	 * sets the progress bar visibility on and off
	 * @param show
	 */
	public static void showDBProgress(boolean show){
		Main.getSearchCompositeDBProgressContainer().setVisible(show);
	}
	
	/**
	 * getter for the current search results data structure
	 */
	public static AlbumsResultsTable getCurrentSearchResults(){
		return StaticProgramTables.results;
	}
	
	/**
	 * setter for the current search results data structure
	 */
	public static void setCurrentSearchResults(AlbumsResultsTable results){
		StaticProgramTables.results = results;
	}
	
	/**
	 * update current results and the Album results table accordingly
	 * @param results
	 */
	public static void updateAlbumsResultsTable(AlbumsResultsTable results){
		// set current results to the new ones
		setCurrentSearchResults(results);
		
		// first clear all results tables
		Main.getSearchTableAlbumResults().removeAll();
		Main.getSearchTableSongResults().removeAll();
		// now enter new results
		for(AlbumsResultsTableItem album: results.getAlbums().values()){
			TableItem item = new TableItem(Main.getSearchTableAlbumResults(), SWT.NONE);
			String[] entry = new String[]{
					Long.toString(album.getAlbumID()),
					album.getAlbumName(),
					album.getArtist(),
					Integer.toString(album.getYear()),
					album.getGenre(),
					getMinutesSeconds(album.getLength())
			};
			item.setText(entry);
		}
		
		// set progress visibility off
		showDBProgress(false);
	}
	
	/**
	 * pops a message to the user with number of results 
	 * @param numOfResults
	 */
	public static void popSearchResultsDoneMessage(int numOfResults){
		MessageBox msg = new MessageBox(Main.getMainShell(),SWT.ICON_INFORMATION);
		msg.setText("Search done");
		if (numOfResults == 0){
			msg.setMessage("No albums found.");
		} else if (numOfResults <= StaticProgramTables.getMaxNumOfResults()){
			msg.setMessage("Search done, found "+numOfResults+" albums.");
		} else {
			msg.setMessage("Found "+numOfResults+" results.\n"+
					"Only first "+StaticProgramTables.getMaxNumOfResults()+" results will be shown, to get other results please refine your search.");
		}
		msg.open();
	}
	
	/**
	 * restores search tab environment after albums results came back:
	 * enables search button
	 */
	public static void setEnvSearchDone(){
		Main.getSearchButtonSearch().setEnabled(true);
		// flag DB as free
		MainFuncs.setAllowDBAction(true);
	}
	
	/**
	 * updates the songs results table according to selected album
	 * if album is selected for the first time, gets the songs list from DB
	 * else gets it from the current album results table
	 * @param album
	 */
	public static void getSongsResultsTable(AlbumsResultsTableItem album){
		// try to fetch songs list from album results
		SongsResultsTable songs = album.getSongs();
		
		// check if songs list is empty / null, if so call results from DB
		if (songs == null || songs.getSongs().isEmpty()){
			// check if DB is not busy, else pop a message
			if (MainFuncs.isAllowDBAction()){
				// flag DB as busy
				MainFuncs.setAllowDBAction(false);
				
				DBConnectionInterface.getSongsResults(album.getAlbumID());
				
			} else {
				MainFuncs.getMsgDBActionNotAllowed().open();
			}
		} else { // album result already has songs table
			updateSongsResultsTableView(songs);
		}
	}
	
	/**
	 * updates the album search results - sets the given album its songs table
	 * updates songs table view
	 * @param albumID
	 */
	public static void updateSongsResultsTable(long albumID, SongsResultsTable songs){
		// update album results
		StaticProgramTables.results.getAlbum(albumID).setSongs(songs);
		// update songs table view
		updateSongsResultsTableView(songs);
		// flag DB as free
		MainFuncs.setAllowDBAction(true);
	}
	
	/**
	 * updates the songs results table view with given songs table
	 * @param songs
	 */
	public static void updateSongsResultsTableView(SongsResultsTable songs){
		// first clear songs table
		Main.getSearchTableSongResults().removeAll();
		// now enter songs
		HashMap<Integer,SongsResultsTableItem> songsResults = (HashMap<Integer,SongsResultsTableItem>)songs.getSongs();
		for(int i = 1; i <= songsResults.size(); i++){
			SongsResultsTableItem song = songsResults.get(i);
			TableItem item = new TableItem(Main.getSearchTableSongResults(), SWT.NONE);
			String[] entry = new String[]{
					Integer.toString(song.getTrack()),
					song.getSongName(),
					song.getSongArtist(),
					getMinutesSeconds(song.getSongLength())
			};
			item.setText(entry);
		}
	}
	
	/**
	 * invoked by "Get Stock Information" button
	 * calls DB to get storage location and quantity for selected album result
	 */
	public static void getAlbumStockInfoInvokation(){
		// get selected album id
		Long albumID = Long.valueOf(Main.getSearchTableAlbumResults().getSelection()[0].getText(0));
		
		// check if DB is not busy, else pop a message		
		if (MainFuncs.isAllowDBAction()){
			// flag DB as busy
			MainFuncs.setAllowDBAction(false);
				
			DBConnectionInterface.getAlbumStockInfo(albumID, AlbumStockInfoCallerEnum.CALLED_BY_SEARCH_RESULT);
			
		} else {
			MainFuncs.getMsgDBActionNotAllowed().open();
		}
	}
	
	/**
	 * invoked from DB, updates given album's storage location and quantity in store
	 * updates gui buttons and stock information labels
	 * @param albumID
	 * @param storageLocation
	 * @param quantity
	 */
	public static void updateAlbumStockInfo(long albumID, long storageLocation, int quantity){
		// update results stock information
		StaticProgramTables.results.getAlbums().get(albumID).setStorageLocation(storageLocation);
		StaticProgramTables.results.getAlbums().get(albumID).setQuantity(quantity);
		
		// update gui view
		setLabelStockLocation((storageLocation > 0)?(Long.toString(storageLocation)):("0"));
		setLabelStoreStock(Integer.toString(quantity));
		Main.getSearchButtonStockInfoOrder().setEnabled(true);
		// enable add to sale and get stock info
		Main.getSearchButtonSaleInfoSale().setEnabled(true);
		Main.getSearchButtonGetStockInfo().setEnabled(true);
		
		// flag DB as free
		MainFuncs.setAllowDBAction(true);
	}
	
	/**
	 * returns the minutes-seconds representation for the given seconds integer
	 * @param secs
	 * @return
	 */
	public static String getMinutesSeconds(int seconds){
		String mins = Integer.toString(seconds/60);
		seconds = seconds%60;
		String secs = Integer.toString(seconds);
		if (seconds < 10)
			secs = "0"+secs;
		
		return mins+":"+secs;
	}
	
	///////////////////////
	// handle stock info //
	///////////////////////
	
	/**
	 * clears stock information
	 */
	public static void clearStockInfo(){
		Main.getSearchLabelStockInfoStoreStock().setText("Store stock: ");
		Main.getSearchLabelStockInfoLocation().setText("Storage location: ");
		Main.getSearchLabelStockInfoPrice().setText("Price: ");
	}
	
	/**
	 * set store stock label
	 * @param str
	 */
	protected static void setLabelStoreStock(String str){
		Main.getSearchLabelStockInfoStoreStock().setText("Store stock: "+str);
	}
	
	/**
	 * set stock location label
	 * @param str
	 */
	protected static void setLabelStockLocation(String str){
		Main.getSearchLabelStockInfoLocation().setText("Storage location: "+str);
	}
	
	/**
	 * set price label
	 * @param str
	 */
	protected static void setLabelPrice(String str){
		Main.getSearchLabelStockInfoPrice().setText("Price: "+str);
	}
	
	///////////////////////
	// handle sale group //
	///////////////////////
	
	public static void checkAndAddSaleItem(){
		MessageBox addToSaleErrorMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		addToSaleErrorMsg.setMessage("Quantity must be an integer greater than 0");
		addToSaleErrorMsg.setText("Add to sale Error");
		
		try{
			int addToSaleQuantity = Integer.parseInt(Main.getSearchTextBoxSaleInfoQuantity().getText());
			// if quantity <= 0, pop error message
			if (addToSaleQuantity <= 0) addToSaleErrorMsg.open();
			
			else try{ // quantity ok, check that stock has wanted quantity
				// get selected album
				AlbumsResultsTableItem selectedAlbum = StaticProgramTables.results.getAlbum(
						Integer.parseInt(Main.getSearchTableAlbumResults().getSelection()[0].getText()));
				// get current stock quantity
				int stockQuantity = selectedAlbum.getQuantity();
				
				// check if quantity is allowed:
				// check if album quantity that is already in sale + wanted quantity
				// given now is <= total album quantity in stock
				
				SaleTableItem saleItem = StaticProgramTables.sale.getSaleItem(selectedAlbum.getAlbumID());
				int quantityAlreadyInSale = (saleItem == null) ? 0 : saleItem.getQuantity(); 
				if (stockQuantity < addToSaleQuantity + quantityAlreadyInSale){
					addToSaleErrorMsg.setMessage("You don't have enough in stock!");
					addToSaleErrorMsg.open();
				} else {
					///////////////////////////
					// quantity ok, add to sale
					///////////////////////////
					
					SaleFuncs.addItemToSale(selectedAlbum.getAlbumID(),
							selectedAlbum.getAlbumName(), addToSaleQuantity,
							selectedAlbum.getPrice());
					
					// jump to sale tab
					MainFuncs.switchTab(2);
				}
				
			} catch (NumberFormatException nfe){
				System.out.println("*** DEBUG: SearchFuncs 'add to sale' button bug");
			}
		} catch (NumberFormatException nfe){
			// given quantity is not a number
			addToSaleErrorMsg.open();
		}
	}
	
	//////////////////////////
	//	DB failure handling	//
	//////////////////////////
	
	/**
	 * notifies the search could not be done
	 * and restores gui
	 */
	public static void notifySearchFailure(){
		// hide progress
		Main.getSearchCompositeDBProgressContainer().setVisible(false);
		
		// pop message
		MessageBox errMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		errMsg.setText("DB Connection Error");
		errMsg.setMessage("Could not search due to a connection error.\n"+
				"Please try again later.");
		// retry connection
		if (errMsg.open() == SWT.OK) {
			// restore gui
			setEnvSearchDone();
		}
	}
	
	/**
	 * notifies the songs could not be fetched
	 * and restores gui
	 */
	public static void notifyFetchSongsFailure(){
		MessageBox errMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		errMsg.setText("DB Connection Error");
		errMsg.setMessage("Could not get song list due to a connection error.\n"+
				"Please try again later.");
		// retry connection
		if (errMsg.open() == SWT.OK) {
			// restore gui
			MainFuncs.setAllowDBAction(true);
		}
	}
	
	/**
	 * notifies the stock info could not be fetched
	 * and restores gui
	 */
	public static void notifyGetStockInfoFailure(){
		MessageBox errMsg = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		errMsg.setText("DB Connection Error");
		errMsg.setMessage("Could not get stock information due to a connection error.\n"+
				"Please try again later.");
		// retry connection
		if (errMsg.open() == SWT.OK) {
			// restore gui
			MainFuncs.setAllowDBAction(true);
		}
	}
}