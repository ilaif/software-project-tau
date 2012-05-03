package com.live4music.client.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;


import com.cloudgarden.resource.SWTResourceManager;
import com.live4music.client.tables.TablesExamples;
import com.live4music.server.db.DBConnectionPool;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;

/**
 * Main
 */
public class Main extends org.eclipse.swt.widgets.Composite {

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	//////////////////////////////////
	//	Class Fields definitions	//
	//////////////////////////////////
	
	// Main
	private static Shell shell;
	private static Display display;
	public static final Font defaultFont = SWTResourceManager.getFont("Arial", 9, 0, false, false);
	public static final Font largeFont = SWTResourceManager.getFont("Arial", 15, 0, false, false);
	
	// Main Menu

	// Main Window Details 
	//////////////////////
	// Store details
	private static Composite mainGroupStoreDetails;
	private static Label mainLabelStoreDetailsStoreID;
	private static Label mainLabelStoreDetailsDateTime;
	private static Label mainLabelStoreDetailsStoreAddress;
	private static Label mainLabelStoreDetailsStorePhone;
	private static Label mainLabelStoreDetailsStoreManager;
	
	private static int central_x;
	private static int central_y;
	
	public final static String sep = File.separator;
	public final static String relPath = "."+sep+"res"+sep;
	
	// Quick tips
	private static Composite mainGroupWelcome;
	private static Label mainLabelWelcomeText;
	
	// Tab folder
	private static TabFolder mainTabFolder;
	
	// Search tab
	/////////////
	private static TabItem searchTabItem;
	private static Composite searchTabComposite;
	
	// Search options group
	private static Group searchGroupOptions;
	private static Button searchBulletByAlbum;
	private static Text searchTextBoxAlbumID;
	private static Button searchBulletOtherParameters;
	private static Button searchCheckBoxAlbumName;
	private static Text searchTextBoxAlbumName;
	private static Button searchCheckBoxArtist;
	private static Text searchTextBoxArtist;
	private static Button searchCheckBoxYear;
	private static Text searchTextBoxYearFrom;
	private static Label searchLabelYearTo;
	private static Text searchTextBoxYearTo;
	private static Button searchCheckBoxSongNames;
	private static Text searchTextBoxSongNames;
	private static Composite searchCompositeStockField;
	private static Label searchLabelStock;
	private static Button searchBulletInStockAll;
	private static Button searchBulletInStockInStore;
	private static Button searchBulletInStockInNetwork;
	private static Button searchCheckBoxGenres;
	private static Button[] searchCheckBoxGenresArr = new Button[8];
	private static Button searchCheckBoxGenreOther;
	private static Text searchTextBoxGenreOther;
	private static Button searchButtonClear;
	private static Button searchButtonSearch;
	
	// Search results group
	private static Group searchGroupResults;
	private static Table searchTableAlbumResults;
	private static TableColumn searchTableColumnAlbumID;
	private static TableColumn searchTableColumnAlbumName;
	private static TableColumn searchTableColumnAlbumArtist;
	private static TableColumn searchTableColumnAlbumYear;
	private static TableColumn searchTableColumnAlbumGenre;
	private static TableColumn searchTableColumnAlbumLength;
	private static Label searchProgressBarLabel;
	private static Table searchTableSongResults;
	private static TableColumn searchTableColumnSongTrack;
	private static TableColumn searchTableColumnSongName;
	private static TableColumn searchTableColumnSongArtist;
	private static TableColumn searchTableColumnSongLength;
	
	// search results progress bar
	private static Panel searchPanelDBProgress;
	private static Frame searchFrameDBProgress;
	private static Composite searchCompositeDBProgressContainer;
	private static JLabel searchJLabelDBProgressBar;
	
	// Stock information group
	private static Group searchGroupStockInfo;
	private static Label searchLabelStockInfoStoreStock;
	private static Label searchLabelStockInfoLocation;
	private static Label searchLabelStockInfoPrice;
	private static Button searchButtonStockInfoOrder;
	
	// Sale group
	private static Group searchGroupSaleInfo;	
	private static Label searchLabelSaleInfoQuantity;
	private static Text searchTextBoxSaleInfoQuantity;
	private static Button searchButtonSaleInfoSale;
	
	// Sale tab
	///////////
	private static TabItem saleTabItem;
	private static Composite saleCompositeMain;
	
	// Sale details group
	private static Group saleGroupSaleDetails;
	private static Label saleLabelSaleDate;
	private static Label saleLabelDateInput;
	private static Label saleLabelSalesmanIDName;
	private static Combo saleComboSalesmanIDNameInput;
	private static Label saleLabelSaleTime;
	private static Label saleLabelTimeInput;
	
	// Sale table
	private static Table saleTableSaleItems;
	private static TableColumn saleTableColumnAlbumID;
	private static TableColumn saleTableColumnAlbumName;
	private static TableColumn saleTableColumnQuantity;
	private static TableColumn saleTableColumnPricePerItem;
	private static TableColumn saleTableColumnPriceTotal;
	private static Button saleButtonRemoveItem;
	private static Button saleButtonClearSale;
	private static Label saleLabelTotalPrice;
	private static Label saleLabelTotalPriceValue;
	private static Button saleButtonMakeSale;
	
	// Stock tab
	////////////
	private static TabItem stockTabItem;
	private static Composite stockTabComposite;
	
	// Order group
	private static Group stockGroupOrderForm;
	private static Label stockLabelAlbumID;
	private static Label stockLabelAlbumIDInput;
	private static Label stockLabelDate;
	private static Label stockLabelOrderDateInput;
	private static Button stockButtonCheckAvailability;
	private static Label stockLabelOrderFromStore;
	
	private static Table stockTableOrderAvailableStores;
	private static TableColumn stockTableColumnStoreID;
	private static TableColumn stockTableColumnStoreCity;
	private static TableColumn stockTableColumnQuantity;
	
	private static Label stockLabelPrice;
	private static Label stockLabelStorePriceInput;
	private static Label stockLabelQuantityInStock;
	private static Label stockLabelQuantityInStockInput;
	private static Label stockLabelStorageLocation;
	private static Label stockLabelStorageLocationInput;
	private static Label stockLabelQuantityToOrder;
	private static Text stockTextBoxQuantityToOrder;
	private static Button stockButtonClearOrder;
	private static Button stockButtonPlaceOrder;
	
	// Orders table
	private static Label stockLabelOrders;
	private static Table stockTableOrders;
	private static TableColumn stockTableColumnOrdersOrderID;
	private static TableColumn stockTableColumnOrdersSupplierID;
	private static TableColumn stockTableColumnOrdersAlbumID;
	private static TableColumn stockTableColumnOrdersQuantity;
	private static TableColumn stockTableColumnOrdersDate;
	private static TableColumn stockTableColumnOrdersStatus;
	private static TableColumn stockTableColumnOrdersCompletionDate;
	private static Button stockButtonRefreshOrders;
	private static Button stockButtonRemoveOrder;
	private static Button stockButtonCancelOrder;
	
	// Requests table
	private static Label stockLabelRequests;	
	private static Table stockTableRequests;
	private static TableColumn stockTableColumnRequestsOrderID;
	private static TableColumn stockTableColumnRequestsOrderingStoreID;
	private static TableColumn stockTableColumnRequestsAlbumID;
	private static TableColumn stockTableColumnRequestsQuantity;
	private static TableColumn stockTableColumnRequestsDate;
	private static TableColumn stockTableColumnRequestsStatus;
	private static TableColumn stockTableColumnRequestsCompletionDate;
	private static Button stockButtonRefreshRequests;
	private static Button stockButtonDenyRequest;
	private static Button stockButtonApproveRequest;
	
	// Management tab
	/////////////////
	private static TabItem managementTabItem;
	private static Composite manageMainComposite;
	
	// Employees table
	private static Label manageLabelEmployees;
	private static Table manageTableEmployees;
	private static TableColumn manageTableColumnEmployeeID;
	private static TableColumn manageTableColumnEmployeePName;
	private static Button stockButtonPlaceOrderSupplier;
	private static Button searchButtonGetStockInfo;
	private static Button searchButtonShowSongs;
	private static TableColumn manageTableColumnEmployeeLName;
	private static TableColumn manageTableColumnEmployeePosition;
	
	// Edit employee details group
	private static Group manageGroupEditEmployee;
	private static Label manageLabelEmployeeEmploymentDateInput;
	private static Label manageLabelEmployeeEmploymentDate;
	private static Label manageLabelEmployeeStoreID;
	private static Label manageLabelEmployeeEmployeeStoreIDInput;
	private static Label manageLabelEmployeeID;
	private static Text manageTextBoxEmployeeIDInput;
	private static Label manageLabelEmployeeBirth;
	private static Text manageTextBoxEmployeeBirthInput;
	private static Label manageLabelEmployeeFName;
	private static Text manageTextBoxEmployeeFNameInput;	
	private static Label manageLabelEmployeeLName;
	private static Text manageTextBoxEmployeeLNameInput;
	private static Label manageLabelEmployeeAddress;
	private static Text manageTextBoxEmployeeAddressInput;
	private static Label manageLabelEmployeePhone;
	private static Text manageTextBoxEmployeePhoneInput;
	private static Label manageLabelEmployeeCellPhone;
	private static Text manageTextBoxEmployeeCellPhoneInput;
	private static Label manageLabelEmployeePosition;
	private static Combo manageComboEmployeePositionInput;
	private static Button manageButtonEmployeeNew;
	private static Button manageButtonEmployeeInsert;
	private static Button manageButtonEmployeeNoSave;
	private static Button manageButtonEmployeeEdit;
	private static Button manageButtonEmployeeSave;
	private static Button manageButtonEmployeeRemove;
	
	// Update database group
	private static Group manageGroupDBSManage;
	private static Label manageLabelDBSUpdate;
	private static Text manageTextBoxDBSUpdateFileInput;
	private static Button manageButtonDBSBrowse;
	private static Button manageButtonDBSUpdate;
	
	// update DB progress bar
	private static Panel managePanelDBProgress;
	private static Frame manageFrameDBProgress;
	private static Composite manageCompositeDBProgressContainer;
	private static JLabel manageJLabelDBProgressBar;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		//////////////////////////////////////////
		//		INIT EXAMPLES - TO BE REMOVED	//
		//////////////////////////////////////////
		TablesExamples.initTablesExamples();
		
		// Start GUI
		showGUI();
		
		// close all DB connections and log files and exit
		DBConnectionPool.closeAllConnections();
		Debug.closeLog();
		System.exit(-1);
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.TITLE | SWT.CLOSE | SWT.MIN);
		shell.setText("Live 4 Music");
		shell.setFont(defaultFont);
		
		// set shell position
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		central_x = (int)dim.getWidth()/2;
		central_y = (int)dim.getHeight()/2;
		shell.setLocation(central_x-500,central_y-375);
		
		// set program icon
		Image progIcon = new Image(display,relPath+"icon.png");
		shell.setImage(progIcon);
		
		Main inst = new Main(shell, SWT.NULL);
		inst.setFont(defaultFont);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public Main(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	/**
	 * Main Program window
	 * holds tabs for functionality:
	 * - Search
	 * - Sales
	 * - Stock
	 * - Management
	 */
	private void initGUI() {
		try {			
			this.setLayout(null);
			this.layout();
			pack();
			this.setSize(1000, 750);

			// initialize allow DB message box
			MainFuncs.initMsgDBActionNotAllowed();
			
			{
				/*
				 * open initialize dialog
				 */
				// initialize DB Connection (will initialize stores table)
				MainFuncs.initDBConnection();
				// open initial dialog
				InitialDialog.openInitDialog();
			}
			
			/*
			 * **************************************************
			 * 
			 * After initial dialog is opened and selected store,
			 * opens main program window
			 * 
			 * **************************************************
			 */
			{
				mainTabFolder = new TabFolder(this, SWT.NONE);
				mainTabFolder.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(0, 0, 0));
				mainTabFolder.setSelection(0);
				mainTabFolder.setBounds(10, 178, 983, 562);
				mainTabFolder.setFont(defaultFont);

				{
					/*
					 * Search Tab
					 * ==========
					 * Contains search fields, results and stock and sale actions
					 */
					searchTabItem = new TabItem(mainTabFolder, SWT.NONE);
					searchTabItem.setText("Search");
					searchTabItem.setToolTipText("Search for Albums");
					{
						searchTabComposite = new Composite(mainTabFolder, SWT.NONE);
						searchTabComposite.setLayout(null);
						searchTabItem.setControl(searchTabComposite);
						{
							searchGroupOptions = new Group(searchTabComposite, SWT.NONE);
							searchGroupOptions.setLayout(null);
							searchGroupOptions.setText("Search by");
							searchGroupOptions.setBounds(5, 0, 355, 313);
							searchGroupOptions.setFont(defaultFont);
							{
								searchBulletByAlbum = new Button(searchGroupOptions, SWT.RADIO | SWT.LEFT);
								searchBulletByAlbum.setText("Search by album ID:");
								searchBulletByAlbum.setBounds(12, 16, 129, 22);
								searchBulletByAlbum.setSelection(true);
								searchBulletByAlbum.setFont(defaultFont);
							}
							{
								searchBulletOtherParameters = new Button(searchGroupOptions, SWT.RADIO | SWT.LEFT);
								searchBulletOtherParameters.setText("Search by other parameters:");
								searchBulletOtherParameters.setBounds(12, 42, 173, 22);
								searchBulletOtherParameters.setFont(defaultFont);
							}
							{
								searchTextBoxAlbumID = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxAlbumID.setBounds(151, 16, 192, 22);
								searchTextBoxAlbumID.setToolTipText("Enter album ID");
								searchTextBoxAlbumID.setFont(defaultFont);
							}
							{
								searchCheckBoxAlbumName = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxAlbumName.setText("Album name:");
								searchCheckBoxAlbumName.setBounds(12, 69, 102, 22);
								searchCheckBoxAlbumName.setFont(defaultFont);
							}
							{
								searchCheckBoxArtist = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxArtist.setText("Artist:");
								searchCheckBoxArtist.setBounds(12, 93, 102, 21);
								searchCheckBoxArtist.setFont(defaultFont);
							}
							{
								searchCheckBoxYear = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxYear.setText("Year from:");
								searchCheckBoxYear.setBounds(12, 117, 102, 23);
								searchCheckBoxYear.setFont(defaultFont);
							}
							{
								searchCheckBoxSongNames = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxSongNames.setText("Song name(s):");
								searchCheckBoxSongNames.setBounds(12, 141, 102, 22);
								searchCheckBoxSongNames.setToolTipText("Enter song names / partial names separated by semicolons\n"+
										"e.g. 'first song name;second song name;third song name'");
								searchCheckBoxSongNames.setFont(defaultFont);
							}
							{
								searchCheckBoxGenres = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenres.setText("Genre(s):");
								searchCheckBoxGenres.setBounds(12, 165, 82, 22);
								searchCheckBoxGenres.setFont(defaultFont);
							}
							{
								searchTextBoxAlbumName = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxAlbumName.setBounds(118, 69, 225, 22);
								searchTextBoxAlbumName.setFont(defaultFont);
							}
							{
								searchTextBoxArtist = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxArtist.setBounds(118, 93, 225, 22);
								searchTextBoxArtist.setFont(defaultFont);
							}
							{
								searchTextBoxYearFrom = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxYearFrom.setBounds(118, 117, 54, 22);
								searchTextBoxYearFrom.setFont(defaultFont);
							}
							{
								searchLabelYearTo = new Label(searchGroupOptions, SWT.NONE);
								searchLabelYearTo.setText("To:");
								searchLabelYearTo.setBounds(190, 120, 30, 16);
								searchLabelYearTo.setFont(defaultFont);
							}
							{
								searchTextBoxYearTo = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxYearTo.setBounds(224, 117, 60, 22);
								searchTextBoxYearTo.setFont(defaultFont);
							}
							{
								searchTextBoxSongNames = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxSongNames.setBounds(118, 141, 225, 22);
								searchTextBoxSongNames.setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[0] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[0].setText("Rock");
								searchCheckBoxGenresArr[0].setBounds(12, 186, 78, 16);
								searchCheckBoxGenresArr[0].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[1] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[1].setText("Jazz");
								searchCheckBoxGenresArr[1].setBounds(12, 206, 78, 16);
								searchCheckBoxGenresArr[1].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[2] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[2].setText("Pop");
								searchCheckBoxGenresArr[2].setBounds(96, 186, 78, 16);
								searchCheckBoxGenresArr[2].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[3] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[3].setText("Blues");
								searchCheckBoxGenresArr[3].setBounds(96, 206, 78, 16);
								searchCheckBoxGenresArr[3].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[4] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[4].setText("World");
								searchCheckBoxGenresArr[4].setBounds(180, 186, 73, 16);
								searchCheckBoxGenresArr[4].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[5] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[5].setText("Hip-Hop");
								searchCheckBoxGenresArr[5].setBounds(180, 206, 79, 16);
								searchCheckBoxGenresArr[5].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[6] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[6].setText("Electronic");
								searchCheckBoxGenresArr[6].setBounds(265, 186, 78, 16);
								searchCheckBoxGenresArr[6].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[7] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[7].setText("Classical");
								searchCheckBoxGenresArr[7].setBounds(265, 206, 78, 16);
								searchCheckBoxGenresArr[7].setFont(defaultFont);
							}
							/*{
								searchCheckBoxGenresArr[8] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[8].setText("Metal");
								searchCheckBoxGenresArr[8].setBounds(270, 186, 60, 16);
								searchCheckBoxGenresArr[8].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[9] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[9].setText("Electronic");
								searchCheckBoxGenresArr[9].setBounds(270, 206, 60, 16);
								searchCheckBoxGenresArr[9].setFont(defaultFont);
							}*/
							{
								searchCheckBoxGenreOther = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenreOther.setText("Other:");
								searchCheckBoxGenreOther.setBounds(12, 223, 70, 22);
								searchCheckBoxGenreOther.setFont(defaultFont);
							}
							{
								searchTextBoxGenreOther = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxGenreOther.setBounds(86, 224, 255, 22);
								searchTextBoxGenreOther.setFont(defaultFont);
							}
							{
								searchButtonClear = new Button(searchGroupOptions, SWT.PUSH | SWT.CENTER);
								searchButtonClear.setText("Clear Fields");
								searchButtonClear.setBounds(12, 273, 156, 33);
								searchButtonClear.setFont(defaultFont);
							}
							{
								searchButtonSearch = new Button(searchGroupOptions, SWT.PUSH | SWT.CENTER);
								searchButtonSearch.setText("Search");
								searchButtonSearch.setBounds(174, 273, 169, 33);
								searchButtonSearch.setFont(defaultFont);
							}
							{
								searchCompositeStockField = new Composite(searchGroupOptions, SWT.NONE);
								searchCompositeStockField.setLayout(null);
								searchCompositeStockField.setBounds(12, 247, 331, 20);
								{
									searchLabelStock = new Label(searchCompositeStockField, SWT.NONE);
									searchLabelStock.setText("Stock:");
									searchLabelStock.setBounds(0, 3, 58, 16);
									searchLabelStock.setFont(defaultFont);
								}
								{
									searchBulletInStockInStore = new Button(searchCompositeStockField, SWT.RADIO | SWT.LEFT);
									searchBulletInStockInStore.setText("In store");
									searchBulletInStockInStore.setBounds(244, 0, 75, 22);
									searchBulletInStockInStore.setFont(defaultFont);
								}
								{
									searchBulletInStockInNetwork = new Button(searchCompositeStockField, SWT.RADIO | SWT.LEFT);
									searchBulletInStockInNetwork.setText("In network");
									searchBulletInStockInNetwork.setBounds(146, 0, 86, 22);
									searchBulletInStockInNetwork.setFont(defaultFont);
								}
								{
									searchBulletInStockAll = new Button(searchCompositeStockField, SWT.RADIO | SWT.LEFT);
									searchBulletInStockAll.setText("All");
									searchBulletInStockAll.setBounds(64, 0, 70, 22);
									searchBulletInStockAll.setSelection(true);
									searchBulletInStockAll.setFont(defaultFont);
								}
							}
						}
						{
							searchGroupResults = new Group(searchTabComposite, SWT.NONE);
							searchGroupResults.setLayout(null);
							searchGroupResults.setText("Search Results");
							searchGroupResults.setBounds(366, 0, 599, 524);
							searchGroupResults.setFont(defaultFont);
							{
								searchTableAlbumResults = new Table(searchGroupResults, SWT.BORDER | SWT.FULL_SELECTION
										| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE); // single row selection
								searchTableAlbumResults.setBounds(13, 23, 576, 200);
								searchTableAlbumResults.setHeaderVisible(true);
								searchTableAlbumResults.setLinesVisible(true);
								searchTableAlbumResults.setFont(defaultFont);
								int numOfColumns = 6;
								int tableWidth = searchTableAlbumResults.getClientArea().width - getBorderWidth()*2;
								
								searchTableColumnAlbumID = new TableColumn(searchTableAlbumResults, SWT.NONE);
								searchTableColumnAlbumID.setText("Album ID");
								searchTableColumnAlbumID.setResizable(true);
								searchTableColumnAlbumID.setMoveable(true);
								searchTableColumnAlbumID.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnAlbumName = new TableColumn(searchTableAlbumResults, SWT.NONE);
								searchTableColumnAlbumName.setText("Album Name");
								searchTableColumnAlbumName.setResizable(true);
								searchTableColumnAlbumName.setMoveable(true);
								searchTableColumnAlbumName.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnAlbumArtist = new TableColumn(searchTableAlbumResults, SWT.NONE);
								searchTableColumnAlbumArtist.setText("Artist");
								searchTableColumnAlbumArtist.setResizable(true);
								searchTableColumnAlbumArtist.setMoveable(true);
								searchTableColumnAlbumArtist.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnAlbumYear = new TableColumn(searchTableAlbumResults, SWT.NONE);
								searchTableColumnAlbumYear.setText("Year");
								searchTableColumnAlbumYear.setResizable(true);
								searchTableColumnAlbumYear.setMoveable(true);
								searchTableColumnAlbumYear.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnAlbumGenre = new TableColumn(searchTableAlbumResults, SWT.NONE);
								searchTableColumnAlbumGenre.setText("Genre");
								searchTableColumnAlbumGenre.setResizable(true);
								searchTableColumnAlbumGenre.setMoveable(true);
								searchTableColumnAlbumGenre.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnAlbumLength = new TableColumn(searchTableAlbumResults, SWT.NONE);
								searchTableColumnAlbumLength.setText("Length");
								searchTableColumnAlbumLength.setResizable(true);
								searchTableColumnAlbumLength.setMoveable(true);
								searchTableColumnAlbumLength.setWidth(tableWidth / numOfColumns);
							}
							{
								searchTableSongResults = new Table(searchGroupResults, SWT.BORDER | SWT.FULL_SELECTION
										| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
								searchTableSongResults.setBounds(13, 330, 576, 184);
								searchTableSongResults.setHeaderVisible(true);
								searchTableSongResults.setLinesVisible(true);
								searchTableSongResults.setFont(defaultFont);

								int tableWidth = searchTableSongResults.getClientArea().width - getBorderWidth()*2;
								int numOfColumns = 4;
								
								searchTableColumnSongTrack = new TableColumn(searchTableSongResults, SWT.NONE);
								searchTableColumnSongTrack.setText("Track");
								searchTableColumnSongTrack.setResizable(true);
								searchTableColumnSongTrack.setMoveable(true);
								searchTableColumnSongTrack.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnSongName = new TableColumn(searchTableSongResults, SWT.NONE);
								searchTableColumnSongName.setText("Song name");
								searchTableColumnSongName.setResizable(true);
								searchTableColumnSongName.setMoveable(true);
								searchTableColumnSongName.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnSongArtist = new TableColumn(searchTableSongResults, SWT.NONE);
								searchTableColumnSongArtist.setText("Artist");
								searchTableColumnSongArtist.setResizable(true);
								searchTableColumnSongArtist.setMoveable(true);
								searchTableColumnSongArtist.setWidth(tableWidth / numOfColumns);
								
								searchTableColumnSongLength = new TableColumn(searchTableSongResults, SWT.NONE);
								searchTableColumnSongLength.setText("Length");
								searchTableColumnSongLength.setResizable(true);
								searchTableColumnSongLength.setMoveable(true);
								searchTableColumnSongLength.setWidth(tableWidth / numOfColumns);
							}
							{
								searchCompositeDBProgressContainer = new Composite(searchGroupResults, SWT.EMBEDDED);
								searchCompositeDBProgressContainer.setBounds(15, 230, 390, 60);
								{
									searchJLabelDBProgressBar = new JLabel("Searching", JLabel.CENTER);
									searchJLabelDBProgressBar.setBounds(15, 230, 390, 50);									
									searchFrameDBProgress = SWT_AWT.new_Frame(searchCompositeDBProgressContainer);
									{
										searchPanelDBProgress = new Panel();
										searchFrameDBProgress.add(searchPanelDBProgress);
										searchPanelDBProgress.add(searchJLabelDBProgressBar);
									}
								}
							}
							{
								searchButtonShowSongs = new Button(searchGroupResults, SWT.PUSH | SWT.CENTER);
								searchButtonShowSongs.setText("Show Song List");
								searchButtonShowSongs.setBounds(15, 296, 574, 30);
								searchButtonShowSongs.setFont(defaultFont);
							}
						}
						{
							searchGroupStockInfo = new Group(searchTabComposite, SWT.NONE);
							searchGroupStockInfo.setLayout(null);
							searchGroupStockInfo.setText("Stock Information");
							searchGroupStockInfo.setBounds(5, 319, 171, 146);
							searchGroupStockInfo.setFont(defaultFont);
							{
								searchLabelStockInfoStoreStock = new Label(searchGroupStockInfo, SWT.NONE);
								searchLabelStockInfoStoreStock.setText("Store stock:");
								searchLabelStockInfoStoreStock.setBounds(12, 22, 152, 19);
								searchLabelStockInfoStoreStock.setFont(defaultFont);
							}
							{
								searchLabelStockInfoLocation = new Label(searchGroupStockInfo, SWT.NONE);
								searchLabelStockInfoLocation.setText("Storage location: ");
								searchLabelStockInfoLocation.setBounds(12, 44, 152, 19);
								searchLabelStockInfoLocation.setFont(defaultFont);
							}
							{
								searchLabelStockInfoPrice = new Label(searchGroupStockInfo, SWT.NONE);
								searchLabelStockInfoPrice.setText("Price:");
								searchLabelStockInfoPrice.setBounds(12, 66, 152, 19);
								searchLabelStockInfoPrice.setFont(defaultFont);
							}
							{
								searchButtonStockInfoOrder = new Button(searchGroupStockInfo, SWT.PUSH | SWT.CENTER);
								searchButtonStockInfoOrder.setText("Place an Order...");
								searchButtonStockInfoOrder.setBounds(12, 113, 147, 23);
								searchButtonStockInfoOrder.setFont(defaultFont);
							}
							{
								searchButtonGetStockInfo = new Button(searchGroupStockInfo, SWT.PUSH | SWT.CENTER);
								searchButtonGetStockInfo.setText("Get Stock Information");
								searchButtonGetStockInfo.setBounds(12, 87, 147, 26);
								searchButtonGetStockInfo.setFont(defaultFont);
							}
						}
						{
							searchGroupSaleInfo = new Group(searchTabComposite, SWT.NONE);
							searchGroupSaleInfo.setLayout(null);
							searchGroupSaleInfo.setText("Sale");
							searchGroupSaleInfo.setBounds(182, 319, 177, 146);
							searchGroupSaleInfo.setFont(defaultFont);
							{
								searchLabelSaleInfoQuantity = new Label(searchGroupSaleInfo, SWT.NONE);
								searchLabelSaleInfoQuantity.setText("Add to sale with quantity:");
								searchLabelSaleInfoQuantity.setBounds(8, 22, 157, 22);
								searchLabelSaleInfoQuantity.setFont(defaultFont);
							}
							{
								searchTextBoxSaleInfoQuantity = new Text(searchGroupSaleInfo, SWT.BORDER);
								searchTextBoxSaleInfoQuantity.setText("1");
								searchTextBoxSaleInfoQuantity.setBounds(42, 52, 89, 22);
								searchTextBoxSaleInfoQuantity.setFont(defaultFont);
							}
							{
								searchButtonSaleInfoSale = new Button(searchGroupSaleInfo, SWT.PUSH | SWT.CENTER);
								searchButtonSaleInfoSale.setText("Add to Sale...");
								searchButtonSaleInfoSale.setBounds(8, 88, 157, 48);
								searchButtonSaleInfoSale.setFont(defaultFont);
							}
						}
					}
				}
				{
					/*
					 * Sale tab
					 * ========
					 * Manage current sale
					 * 
					 */
					saleTabItem = new TabItem(mainTabFolder, SWT.NONE);
					saleTabItem.setText("Sale");
					saleTabItem.setToolTipText("Manage current sale");
					{
						saleCompositeMain = new Composite(mainTabFolder, SWT.NONE);
						saleCompositeMain.setLayout(null);
						saleTabItem.setControl(saleCompositeMain);
						{
							saleGroupSaleDetails = new Group(saleCompositeMain, SWT.NONE);
							saleGroupSaleDetails.setLayout(null);
							saleGroupSaleDetails.setText("Manage Current Sale");
							saleGroupSaleDetails.setBounds(12, 0, 953, 56);
							saleGroupSaleDetails.setFont(defaultFont);
							{
								saleLabelSalesmanIDName = new Label(saleGroupSaleDetails, SWT.NONE);
								saleLabelSalesmanIDName.setText("Salesman:");
								saleLabelSalesmanIDName.setBounds(25, 24, 75, 18);
								saleLabelSalesmanIDName.setFont(defaultFont);
							}
							{
								saleComboSalesmanIDNameInput = new Combo(saleGroupSaleDetails, SWT.READ_ONLY);
								saleComboSalesmanIDNameInput.setBounds(106, 24, 309, 21);
								saleComboSalesmanIDNameInput.setFont(defaultFont);
							}							
							{
								saleLabelSaleDate = new Label(saleGroupSaleDetails, SWT.NONE);
								saleLabelSaleDate.setText("Date of sale:");
								saleLabelSaleDate.setBounds(427, 25, 92, 18);
								saleLabelSaleDate.setFont(defaultFont);
							}
							{
								saleLabelSaleTime = new Label(saleGroupSaleDetails, SWT.NONE);
								saleLabelSaleTime.setText("Time of sale:");
								saleLabelSaleTime.setBounds(605, 25, 86, 18);
								saleLabelSaleTime.setFont(defaultFont);
							}
							{
								saleLabelDateInput = new Label(saleGroupSaleDetails, SWT.BORDER);
								saleLabelDateInput.setBounds(524, 25, 75, 18);
								saleLabelDateInput.setText(MainFuncs.getDate());
								saleLabelDateInput.setFont(defaultFont);
							}
							{
								saleLabelTimeInput = new Label(saleGroupSaleDetails, SWT.BORDER);
								saleLabelTimeInput.setBounds(696, 25, 69, 18);
								saleLabelTimeInput.setText(MainFuncs.getTime());
								saleLabelTimeInput.setFont(defaultFont);
							}
						}
						{
							saleTableSaleItems = new Table(saleCompositeMain, SWT.BORDER | SWT.FULL_SELECTION
									| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
							saleTableSaleItems.setBounds(12, 63, 953, 385);
							saleTableSaleItems.setHeaderVisible(true);
							saleTableSaleItems.setLinesVisible(true);
							saleTableSaleItems.setFont(defaultFont);
							int tableWidth = saleTableSaleItems.getClientArea().width - getBorderWidth()*2;
							int numOfColumns = 5;
							
							// adding columns
							saleTableColumnAlbumID = new TableColumn(saleTableSaleItems, SWT.NONE);
							saleTableColumnAlbumID.setText("Album ID");
							saleTableColumnAlbumID.setResizable(true);
							saleTableColumnAlbumID.setMoveable(true);
							saleTableColumnAlbumID.setWidth(tableWidth / numOfColumns);
							
							saleTableColumnAlbumName = new TableColumn(saleTableSaleItems, SWT.NONE);
							saleTableColumnAlbumName.setText("Album name");
							saleTableColumnAlbumName.setResizable(true);
							saleTableColumnAlbumName.setMoveable(true);
							saleTableColumnAlbumName.setWidth(111);
							saleTableColumnAlbumName.setWidth(tableWidth / numOfColumns);
							
							saleTableColumnQuantity = new TableColumn(saleTableSaleItems, SWT.NONE);
							saleTableColumnQuantity.setText("Quantity");
							saleTableColumnQuantity.setResizable(true);
							saleTableColumnQuantity.setMoveable(true);
							saleTableColumnQuantity.setWidth(tableWidth / numOfColumns);
							
							saleTableColumnPricePerItem = new TableColumn(saleTableSaleItems, SWT.NONE);
							saleTableColumnPricePerItem.setText("Price per item");
							saleTableColumnPricePerItem.setResizable(true);
							saleTableColumnPricePerItem.setMoveable(true);
							saleTableColumnPricePerItem.setWidth(tableWidth / numOfColumns);
							
							saleTableColumnPriceTotal = new TableColumn(saleTableSaleItems, SWT.NONE);
							saleTableColumnPriceTotal.setText("Total price");
							saleTableColumnPriceTotal.setResizable(true);
							saleTableColumnPriceTotal.setMoveable(true);
							saleTableColumnPriceTotal.setWidth(tableWidth / numOfColumns);
						}
						{
							saleLabelTotalPriceValue = new Label(saleCompositeMain, SWT.BORDER);
							saleLabelTotalPriceValue.setBounds(871, 454, 94, 22);
							saleLabelTotalPriceValue.setFont(defaultFont);
						}
						{
							saleLabelTotalPrice = new Label(saleCompositeMain, SWT.NONE);
							saleLabelTotalPrice.setText("Total price:");
							saleLabelTotalPrice.setBounds(779, 455, 87, 22);
							saleLabelTotalPrice.setAlignment(SWT.RIGHT);
							saleLabelTotalPrice.setFont(defaultFont);
						}
						{
							saleButtonRemoveItem = new Button(saleCompositeMain, SWT.PUSH | SWT.CENTER);
							saleButtonRemoveItem.setText("Remove Item");
							saleButtonRemoveItem.setBounds(12, 454, 110, 30);
							saleButtonRemoveItem.setFont(defaultFont);
						}
						{
							saleButtonClearSale = new Button(saleCompositeMain, SWT.PUSH | SWT.CENTER);
							saleButtonClearSale.setText("Clear Sale");
							saleButtonClearSale.setBounds(128, 454, 110, 30);
							saleButtonClearSale.setFont(defaultFont);
						}
						{
							saleButtonMakeSale = new Button(saleCompositeMain, SWT.PUSH | SWT.CENTER);
							saleButtonMakeSale.setText("Make Sale");
							saleButtonMakeSale.setBounds(872, 483, 93, 41);
							saleButtonMakeSale.setFont(defaultFont);
						}
					}
				}
				{
					/*
					 * Stock tab
					 * =========
					 * Manage Stock, orders and requests
					 * 
					 */
					stockTabItem = new TabItem(mainTabFolder, SWT.NONE);
					stockTabItem.setText("Stock");
					stockTabItem.setToolTipText("View and manage orders and requests");
					{
						stockTabComposite = new Composite(mainTabFolder, SWT.NONE);
						stockTabComposite.setLayout(null);
						stockTabItem.setControl(stockTabComposite);
						{
							stockGroupOrderForm = new Group(stockTabComposite, SWT.NONE);
							stockGroupOrderForm.setLayout(null);
							stockGroupOrderForm.setText("Order Albums");
							stockGroupOrderForm.setBounds(2, 0, 963, 147);
							stockGroupOrderForm.setFont(defaultFont);
							{
								stockLabelAlbumID = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelAlbumID.setText("Album ID:");
								stockLabelAlbumID.setBounds(8, 38, 74, 22);
								stockLabelAlbumID.setFont(defaultFont);
							}
							{
								stockLabelAlbumIDInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelAlbumIDInput.setBounds(90, 36, 154, 22);
								stockLabelAlbumIDInput.setFont(defaultFont);
							}
							{
								stockLabelDate = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelDate.setText("Order date:");
								stockLabelDate.setBounds(12, 71, 74, 22);
								stockLabelDate.setFont(defaultFont);
							}
							{
								stockLabelOrderDateInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelOrderDateInput.setBounds(92, 71, 70, 22);
								stockLabelOrderDateInput.setText(MainFuncs.getDate());
								stockLabelOrderDateInput.setFont(defaultFont);
							}
							{
								stockButtonCheckAvailability = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonCheckAvailability.setText("Check Availability");
								stockButtonCheckAvailability.setBounds(8, 105, 235, 33);
								stockButtonCheckAvailability.setFont(defaultFont);
							}
							{
								stockLabelOrderFromStore = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelOrderFromStore.setText("Order from:");
								stockLabelOrderFromStore.setBounds(256, 18, 79, 24);
								stockLabelOrderFromStore.setFont(defaultFont);
							}
							{
								stockTableOrderAvailableStores = new Table(stockGroupOrderForm, SWT.BORDER | SWT.FULL_SELECTION
										| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
								stockTableOrderAvailableStores.setBounds(254, 42, 469, 96);
								stockTableOrderAvailableStores.setHeaderVisible(true);
								stockTableOrderAvailableStores.setLinesVisible(true);
								stockTableOrderAvailableStores.setFont(defaultFont);
								int numOfColumns = 3;
								int tableWidth = stockTableOrderAvailableStores.getClientArea().width - getBorderWidth()*2;
								
								stockTableColumnStoreID = new TableColumn(stockTableOrderAvailableStores, SWT.NONE);
								stockTableColumnStoreID.setText("Store ID");
								stockTableColumnStoreID.setResizable(true);
								stockTableColumnStoreID.setMoveable(true);
								stockTableColumnStoreID.setWidth(tableWidth / numOfColumns);
								
								stockTableColumnStoreCity = new TableColumn(stockTableOrderAvailableStores, SWT.NONE);
								stockTableColumnStoreCity.setText("City");
								stockTableColumnStoreCity.setResizable(true);
								stockTableColumnStoreCity.setMoveable(true);
								stockTableColumnStoreCity.setWidth(tableWidth / numOfColumns);
								
								stockTableColumnQuantity = new TableColumn(stockTableOrderAvailableStores, SWT.NONE);
								stockTableColumnQuantity.setText("Quantity");
								stockTableColumnQuantity.setResizable(true);
								stockTableColumnQuantity.setMoveable(true);
								stockTableColumnQuantity.setWidth(tableWidth / numOfColumns);
							}
							{
								stockLabelQuantityInStock = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelQuantityInStock.setText("Quantity:");
								stockLabelQuantityInStock.setBounds(842, 19, 52, 22);
								stockLabelQuantityInStock.setFont(defaultFont);
							}
							{
								stockLabelQuantityInStockInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelQuantityInStockInput.setBounds(895, 18, 57, 22);
								stockLabelQuantityInStockInput.setFont(defaultFont);
							}
							{
								stockLabelPrice = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelPrice.setText("Price:");
								stockLabelPrice.setBounds(738, 20, 52, 22);
								stockLabelPrice.setFont(defaultFont);
							}
							{
								stockLabelStorePriceInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelStorePriceInput.setBounds(790, 18, 46, 22);
								stockLabelStorePriceInput.setFont(defaultFont);
							}
							{
								stockLabelStorageLocation = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelStorageLocation.setText("Location:");
								stockLabelStorageLocation.setBounds(738, 44, 52, 22);
								stockLabelStorageLocation.setFont(defaultFont);
							}
							{
								stockLabelStorageLocationInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelStorageLocationInput.setBounds(791, 43, 161, 22);
								stockLabelStorageLocationInput.setFont(defaultFont);
							}
							{
								stockButtonPlaceOrder = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonPlaceOrder.setText("Place Order");
								stockButtonPlaceOrder.setBounds(832, 116, 121, 23);
								stockButtonPlaceOrder.setFont(defaultFont);
							}
							{
								stockButtonClearOrder = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonClearOrder.setText("Clear Fields");
								stockButtonClearOrder.setBounds(737, 94, 89, 45);
								stockButtonClearOrder.setFont(defaultFont);
							}
							{
								stockLabelQuantityToOrder = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelQuantityToOrder.setText("Quantity to order:");
								stockLabelQuantityToOrder.setBounds(737, 72, 108, 22);
								stockLabelQuantityToOrder.setFont(defaultFont);
							}
							{
								stockTextBoxQuantityToOrder = new Text(stockGroupOrderForm, SWT.BORDER);
								stockTextBoxQuantityToOrder.setText("1");
								stockTextBoxQuantityToOrder.setBounds(852, 68, 100, 22);
								stockTextBoxQuantityToOrder.setFont(defaultFont);
							}
							{
								stockButtonPlaceOrderSupplier = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonPlaceOrderSupplier.setText("Order from Supplier");
								stockButtonPlaceOrderSupplier.setBounds(832, 94, 121, 22);
								stockButtonPlaceOrderSupplier.setFont(defaultFont);
							}
						}
						{
							stockTableOrders = new Table(stockTabComposite, SWT.BORDER | SWT.FULL_SELECTION
									| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
							stockTableOrders.setBounds(5, 174, 960, 110);
							stockTableOrders.setHeaderVisible(true);
							stockTableOrders.setLinesVisible(true);
							stockTableOrders.setFont(defaultFont);
							int numOfColumns = 6; //7
							int tableWidth = stockTableOrders.getClientArea().width - getBorderWidth()*2;
							
							stockTableColumnOrdersOrderID = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersOrderID.setText("Order ID");
							stockTableColumnOrdersOrderID.setResizable(true);
							stockTableColumnOrdersOrderID.setMoveable(true);
							stockTableColumnOrdersOrderID.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnOrdersSupplierID = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersSupplierID.setText("Supplier ID");
							stockTableColumnOrdersSupplierID.setResizable(true);
							stockTableColumnOrdersSupplierID.setMoveable(true);
							stockTableColumnOrdersSupplierID.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnOrdersAlbumID = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersAlbumID.setText("Album ID");
							stockTableColumnOrdersAlbumID.setResizable(true);
							stockTableColumnOrdersAlbumID.setMoveable(true);
							stockTableColumnOrdersAlbumID.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnOrdersQuantity = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersQuantity.setText("Quantity");
							stockTableColumnOrdersQuantity.setResizable(true);
							stockTableColumnOrdersQuantity.setMoveable(true);
							stockTableColumnOrdersQuantity.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnOrdersDate = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersDate.setText("Order date");
							stockTableColumnOrdersDate.setResizable(true);
							stockTableColumnOrdersDate.setMoveable(true);
							stockTableColumnOrdersDate.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnOrdersStatus = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersStatus.setText("Status");
							stockTableColumnOrdersStatus.setResizable(true);
							stockTableColumnOrdersStatus.setMoveable(true);
							stockTableColumnOrdersStatus.setWidth(tableWidth / numOfColumns);
							
							/*
							stockTableColumnOrdersCompletionDate = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersCompletionDate.setText("Completion Date");
							stockTableColumnOrdersCompletionDate.setResizable(true);
							stockTableColumnOrdersCompletionDate.setMoveable(true);
							stockTableColumnOrdersCompletionDate.setWidth(tableWidth / numOfColumns);
							*/
						}
						{
							stockTableRequests = new Table(stockTabComposite, SWT.BORDER | SWT.FULL_SELECTION
									| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
							stockTableRequests.setBounds(4, 317, 961, 104);
							stockTableRequests.setHeaderVisible(true);
							stockTableRequests.setLinesVisible(true);
							stockTableRequests.setFont(defaultFont);
							int numOfColumns = 6; // 7
							int tableWidth = stockTableRequests.getClientArea().width - getBorderWidth()*2;
							
							stockTableColumnRequestsOrderID = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsOrderID.setText("Order ID");
							stockTableColumnRequestsOrderID.setResizable(true);
							stockTableColumnRequestsOrderID.setMoveable(true);
							stockTableColumnRequestsOrderID.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnRequestsOrderingStoreID = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsOrderingStoreID.setText("Requesting Store ID");
							stockTableColumnRequestsOrderingStoreID.setResizable(true);
							stockTableColumnRequestsOrderingStoreID.setMoveable(true);
							stockTableColumnRequestsOrderingStoreID.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnRequestsAlbumID = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsAlbumID.setText("Album ID");
							stockTableColumnRequestsAlbumID.setResizable(true);
							stockTableColumnRequestsAlbumID.setMoveable(true);
							stockTableColumnRequestsAlbumID.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnRequestsQuantity = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsQuantity.setText("Quantity");
							stockTableColumnRequestsQuantity.setResizable(true);
							stockTableColumnRequestsQuantity.setMoveable(true);
							stockTableColumnRequestsQuantity.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnRequestsDate = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsDate.setText("Order date");
							stockTableColumnRequestsDate.setResizable(true);
							stockTableColumnRequestsDate.setMoveable(true);
							stockTableColumnRequestsDate.setWidth(tableWidth / numOfColumns);
							
							stockTableColumnRequestsStatus = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsStatus.setText("Status");
							stockTableColumnRequestsStatus.setResizable(true);
							stockTableColumnRequestsStatus.setMoveable(true);
							stockTableColumnRequestsStatus.setWidth(tableWidth / numOfColumns);
							
							/*
							stockTableColumnRequestsCompletionDate = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsCompletionDate.setText("Completion Date");
							stockTableColumnRequestsCompletionDate.setResizable(true);
							stockTableColumnRequestsCompletionDate.setMoveable(true);
							stockTableColumnRequestsCompletionDate.setWidth(tableWidth / numOfColumns);
							*/
						}						
						{
							stockLabelRequests = new Label(stockTabComposite, SWT.NONE);
							stockLabelRequests.setText("Requests:");
							stockLabelRequests.setBounds(6, 295, 64, 20);
							stockLabelRequests.setFont(defaultFont);
						}
						{
							stockButtonApproveRequest = new Button(stockTabComposite, SWT.PUSH | SWT.CENTER);
							stockButtonApproveRequest.setText("Approve Request");
							stockButtonApproveRequest.setBounds(864, 427, 101, 24);
							stockButtonApproveRequest.setFont(defaultFont);
						}
						{
							stockButtonDenyRequest = new Button(stockTabComposite, SWT.PUSH | SWT.CENTER);
							stockButtonDenyRequest.setText("Deny Request");
							stockButtonDenyRequest.setBounds(757, 427, 101, 24);
							stockButtonDenyRequest.setFont(defaultFont);
						}
						{
							stockButtonRefreshRequests = new Button(stockTabComposite, SWT.PUSH | SWT.CENTER);
							stockButtonRefreshRequests.setText("Refresh");
							stockButtonRefreshRequests.setBounds(650, 427, 101, 24);
							stockButtonRefreshRequests.setFont(defaultFont);
						}
						{
							stockLabelOrders = new Label(stockTabComposite, SWT.NONE);
							stockLabelOrders.setText("Orders:");
							stockLabelOrders.setBounds(7, 153, 60, 20);
							stockLabelOrders.setFont(defaultFont);
						}
						{
							stockButtonCancelOrder = new Button(stockTabComposite, SWT.PUSH | SWT.CENTER);
							stockButtonCancelOrder.setText("Cancel Order");
							stockButtonCancelOrder.setBounds(864, 290, 101, 24);
							stockButtonCancelOrder.setFont(defaultFont);
						}
						{
							stockButtonRemoveOrder = new Button(stockTabComposite, SWT.PUSH | SWT.CENTER);
							stockButtonRemoveOrder.setText("Remove Order");
							stockButtonRemoveOrder.setBounds(757, 290, 101, 24);
							stockButtonRemoveOrder.setFont(defaultFont);
						}
						{
							stockButtonRefreshOrders = new Button(stockTabComposite, SWT.PUSH | SWT.CENTER);
							stockButtonRefreshOrders.setText("Refresh");
							stockButtonRefreshOrders.setBounds(650, 290, 101, 24);
							stockButtonRefreshOrders.setFont(defaultFont);
						}
					}
				}
				{
					/*
					 * Management tab
					 * ==============
					 * Manage Employees and database
					 * 
					 */
					managementTabItem = new TabItem(mainTabFolder, SWT.NONE);
					managementTabItem.setText("Management");
					managementTabItem.setToolTipText("View and manage employees, update database");
					{
						manageMainComposite = new Composite(mainTabFolder, SWT.NONE);
						manageMainComposite.setLayout(null);
						managementTabItem.setControl(manageMainComposite);
						{
							manageLabelEmployees = new Label(manageMainComposite, SWT.NONE);
							manageLabelEmployees.setText("Employees:");
							manageLabelEmployees.setBounds(12, 12, 86, 22);
							manageLabelEmployees.setFont(defaultFont);
						}
						{
							manageTableEmployees = new Table(manageMainComposite, SWT.BORDER | SWT.FULL_SELECTION
									| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
							manageTableEmployees.setBounds(14, 34, 621, 216);
							manageTableEmployees.setHeaderVisible(true);
							manageTableEmployees.setLinesVisible(true);
							manageTableEmployees.setFont(defaultFont);
							int numOfColumns = 4;
							int tableWidth = manageTableEmployees.getClientArea().width - getBorderWidth()*2;
							
							manageTableColumnEmployeeID = new TableColumn(manageTableEmployees, SWT.NONE);
							manageTableColumnEmployeeID.setText("Employee ID");
							manageTableColumnEmployeeID.setResizable(true);
							manageTableColumnEmployeeID.setMoveable(true);
							manageTableColumnEmployeeID.setWidth(tableWidth / numOfColumns);
							
							manageTableColumnEmployeePName = new TableColumn(manageTableEmployees, SWT.NONE);
							manageTableColumnEmployeePName.setText("First name");
							manageTableColumnEmployeePName.setResizable(true);
							manageTableColumnEmployeePName.setMoveable(true);
							manageTableColumnEmployeePName.setWidth(tableWidth / numOfColumns);
							
							manageTableColumnEmployeeLName = new TableColumn(manageTableEmployees, SWT.NONE);
							manageTableColumnEmployeeLName.setText("Last name");
							manageTableColumnEmployeeLName.setResizable(true);
							manageTableColumnEmployeeLName.setMoveable(true);
							manageTableColumnEmployeeLName.setWidth(tableWidth / numOfColumns);
							
							manageTableColumnEmployeePosition = new TableColumn(manageTableEmployees, SWT.NONE);
							manageTableColumnEmployeePosition.setText("Position");
							manageTableColumnEmployeePosition.setResizable(true);
							manageTableColumnEmployeePosition.setMoveable(true);
							manageTableColumnEmployeePosition.setWidth(tableWidth / numOfColumns);
						}
						{
							manageGroupEditEmployee = new Group(manageMainComposite, SWT.NONE);
							manageGroupEditEmployee.setLayout(null);
							manageGroupEditEmployee.setText("Edit Employee Details");
							manageGroupEditEmployee.setBounds(641, 12, 324, 441);
							manageGroupEditEmployee.setFont(defaultFont);
							{
								manageLabelEmployeeID = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeID.setText("ID:");
								manageLabelEmployeeID.setBounds(12, 79, 25, 22);
								manageLabelEmployeeID.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeIDInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeIDInput.setBounds(12, 101, 129, 22);
								manageTextBoxEmployeeIDInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeBirth = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeBirth.setText("Date of birth:");
								manageLabelEmployeeBirth.setBounds(177, 79, 78, 22);
								manageLabelEmployeeBirth.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeBirthInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeBirthInput.setBounds(177, 101, 129, 22);
								manageTextBoxEmployeeBirthInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeFName = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeFName.setText("First name:");
								manageLabelEmployeeFName.setBounds(12, 131, 62, 22);
								manageLabelEmployeeFName.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeFNameInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeFNameInput.setBounds(12, 153, 129, 22);
								manageTextBoxEmployeeFNameInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeLName = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeLName.setText("Last name:");
								manageLabelEmployeeLName.setBounds(177, 131, 60, 22);
								manageLabelEmployeeLName.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeLNameInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeLNameInput.setBounds(178, 153, 129, 22);
								manageTextBoxEmployeeLNameInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeEmploymentDate = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeEmploymentDate.setText("Date of employment:");
								manageLabelEmployeeEmploymentDate.setBounds(12, 24, 112, 22);
								manageLabelEmployeeEmploymentDate.setFont(defaultFont);
							}
							{
								manageLabelEmployeeAddress = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeAddress.setText("Address:");
								manageLabelEmployeeAddress.setBounds(12, 183, 51, 22);
								manageLabelEmployeeAddress.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeAddressInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeAddressInput.setBounds(12, 205, 296, 22);
								manageTextBoxEmployeeAddressInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeStoreID = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeStoreID.setText("Employing store ID:");
								manageLabelEmployeeStoreID.setBounds(176, 24, 106, 22);
								manageLabelEmployeeStoreID.setFont(defaultFont);
							}
							{
								manageLabelEmployeeEmployeeStoreIDInput = new Label(manageGroupEditEmployee, SWT.BORDER);
								manageLabelEmployeeEmployeeStoreIDInput.setBounds(176, 49, 130, 22);
								manageLabelEmployeeEmployeeStoreIDInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeEmploymentDateInput = new Label(manageGroupEditEmployee, SWT.BORDER);
								manageLabelEmployeeEmploymentDateInput.setBounds(12, 50, 130, 22);
								manageLabelEmployeeEmploymentDateInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeePhone = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeePhone.setText("Phone:");
								manageLabelEmployeePhone.setBounds(12, 235, 60, 22);
								manageLabelEmployeePhone.setFont(defaultFont);
							}
							{
								manageLabelEmployeeCellPhone = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeCellPhone.setText("Cellular Phone:");
								manageLabelEmployeeCellPhone.setBounds(180, 235, 102, 22);
								manageLabelEmployeeCellPhone.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeePhoneInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeePhoneInput.setBounds(12, 257, 131, 22);
								manageTextBoxEmployeePhoneInput.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeCellPhoneInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeCellPhoneInput.setBounds(177, 257, 131, 22);
								manageTextBoxEmployeeCellPhoneInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeePosition = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeePosition.setText("Position:");
								manageLabelEmployeePosition.setBounds(12, 291, 60, 22);
								manageLabelEmployeePosition.setFont(defaultFont);
							}
							{
								manageComboEmployeePositionInput = new Combo(manageGroupEditEmployee, SWT.READ_ONLY);
								manageComboEmployeePositionInput.setBounds(12, 319, 294, 23);
								manageComboEmployeePositionInput.setFont(defaultFont);
							}
							{
								manageButtonEmployeeNew = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeNew.setText("New");
								manageButtonEmployeeNew.setBounds(12, 363, 60, 30);
								manageButtonEmployeeNew.setFont(defaultFont);
							}
							{
								manageButtonEmployeeInsert = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeInsert.setText("Insert");
								manageButtonEmployeeInsert.setBounds(81, 363, 60, 30);
								manageButtonEmployeeInsert.setFont(defaultFont);
							}
							{
								manageButtonEmployeeEdit = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeEdit.setText("Edit");
								manageButtonEmployeeEdit.setBounds(12, 399, 60, 30);
								manageButtonEmployeeEdit.setFont(defaultFont);
							}
							{
								manageButtonEmployeeSave = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeSave.setText("Save");
								manageButtonEmployeeSave.setBounds(81, 399, 60, 30);
								manageButtonEmployeeSave.setFont(defaultFont);
							}
							{
								manageButtonEmployeeRemove = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeRemove.setText("Remove Employee");
								manageButtonEmployeeRemove.setBounds(150, 399, 157, 30);
								manageButtonEmployeeRemove.setFont(defaultFont);
							}
							{
								manageButtonEmployeeNoSave = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeNoSave.setText("Exit Without Saving");
								manageButtonEmployeeNoSave.setBounds(150, 363, 157, 30);
								manageButtonEmployeeNoSave.setFont(defaultFont);
							}
						}
						{
							manageGroupDBSManage = new Group(manageMainComposite, SWT.NONE);
							manageGroupDBSManage.setLayout(null);
							manageGroupDBSManage.setText("Manage Database");
							manageGroupDBSManage.setBounds(12, 296, 623, 157);
							manageGroupDBSManage.setFont(defaultFont);
							{
								manageLabelDBSUpdate = new Label(manageGroupDBSManage, SWT.NONE);
								manageLabelDBSUpdate.setText("Select update file:");
								manageLabelDBSUpdate.setBounds(12, 21, 96, 18);
								manageLabelDBSUpdate.setFont(defaultFont);
							}
							{
								manageTextBoxDBSUpdateFileInput = new Text(manageGroupDBSManage, SWT.BORDER);
								manageTextBoxDBSUpdateFileInput.setBounds(14, 42, 599, 22);
								manageTextBoxDBSUpdateFileInput.setEnabled(false);
								manageTextBoxDBSUpdateFileInput.setFont(defaultFont);
							}
							{
								manageButtonDBSBrowse = new Button(manageGroupDBSManage, SWT.PUSH | SWT.CENTER);
								manageButtonDBSBrowse.setText("Browse...");
								manageButtonDBSBrowse.setBounds(421, 69, 74, 23);
								manageButtonDBSBrowse.setFont(defaultFont);
							}
							{
								manageButtonDBSUpdate = new Button(manageGroupDBSManage, SWT.PUSH | SWT.CENTER);
								manageButtonDBSUpdate.setText("Update Database");
								manageButtonDBSUpdate.setBounds(501, 69, 112, 23);
								manageButtonDBSUpdate.setFont(defaultFont);
							}
							{
								manageCompositeDBProgressContainer = new Composite(manageGroupDBSManage, SWT.EMBEDDED);
								manageCompositeDBProgressContainer.setBounds(12, 92, 401, 59);
								{
									manageJLabelDBProgressBar = new JLabel("Processing", JLabel.CENTER);
									manageJLabelDBProgressBar.setBounds(16, 97, 397, 48);
									
									manageFrameDBProgress = SWT_AWT.new_Frame(manageCompositeDBProgressContainer);
									{
										managePanelDBProgress = new Panel();
										manageFrameDBProgress.add(managePanelDBProgress);
										managePanelDBProgress.add(manageJLabelDBProgressBar);
									}
								}
							}
							
						}
					}
				}
			}
			
			Image progIcon = new Image(display,relPath+"icon.png");			
			
			Composite mainGroupTop = new Composite(this, SWT.NONE);
			mainGroupTop.setBackgroundMode(SWT.INHERIT_DEFAULT);
			mainGroupTop.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(1, 1, 1));
			mainGroupTop.setBounds(0, 0, 1000, 163);
			
			Label mainIconLabel = new Label(mainGroupTop, SWT.NONE);
			mainIconLabel.setLocation(10, 10);
			mainIconLabel.setSize(135, 135);
			mainIconLabel.setBackground(SWTResourceManager.getColor(1, 1, 1));
			mainIconLabel.setImage(progIcon);
			{
				mainGroupWelcome = new Composite(mainGroupTop, SWT.NONE);
				mainGroupWelcome.setLocation(151, 10);
				mainGroupWelcome.setSize(500, 145);
				mainGroupWelcome.setBackgroundMode(SWT.INHERIT_DEFAULT);
				mainGroupWelcome.setLayout(null);
				mainGroupWelcome.setFont(defaultFont);
				{
					mainLabelWelcomeText = new Label(mainGroupWelcome, SWT.NONE);
					mainLabelWelcomeText.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					mainLabelWelcomeText.setBounds(25, 0, 457, 58);
					mainLabelWelcomeText.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Arial", 16, SWT.BOLD));
				}
				{
					mainLabelStoreDetailsStoreID = new Label(mainGroupWelcome, SWT.NONE);
					mainLabelStoreDetailsStoreID.setBounds(25, 65, 101, 20);
					mainLabelStoreDetailsStoreID.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					mainLabelStoreDetailsStoreID.setFont(defaultFont);
				}
				{
					mainLabelStoreDetailsStoreAddress = new Label(mainGroupWelcome, SWT.NONE);
					mainLabelStoreDetailsStoreAddress.setBounds(25, 91, 245, 20);
					mainLabelStoreDetailsStoreAddress.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					mainLabelStoreDetailsStoreAddress.setFont(defaultFont);
				}
				{
					mainLabelStoreDetailsStoreManager = new Label(mainGroupWelcome, SWT.NONE);
					mainLabelStoreDetailsStoreManager.setBounds(25, 117, 162, 22);
					mainLabelStoreDetailsStoreManager.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					mainLabelStoreDetailsStoreManager.setFont(defaultFont);
				}
			}
			
			
			{
				/*
				 * Main window details: store details and quick tips
				 */
				mainGroupStoreDetails = new Composite(mainGroupTop, SWT.NONE);
				mainGroupStoreDetails.setLocation(666, 10);
				mainGroupStoreDetails.setSize(324, 145);
				mainGroupStoreDetails.setBackgroundMode(SWT.INHERIT_DEFAULT);
				mainGroupStoreDetails.setLayout(null);
				mainGroupStoreDetails.setFont(defaultFont);
				{
					mainLabelStoreDetailsStorePhone = new Label(mainGroupStoreDetails, SWT.NONE);
					mainLabelStoreDetailsStorePhone.setAlignment(SWT.RIGHT);
					mainLabelStoreDetailsStorePhone.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					mainLabelStoreDetailsStorePhone.setBounds(138, 26, 186, 20);
					mainLabelStoreDetailsStorePhone.setFont(defaultFont);
				}
				{
					mainLabelStoreDetailsDateTime = new Label(mainGroupStoreDetails, SWT.NONE);
					mainLabelStoreDetailsDateTime.setAlignment(SWT.RIGHT);
					mainLabelStoreDetailsDateTime.setLocation(157, 0);
					mainLabelStoreDetailsDateTime.setSize(167, 20);
					mainLabelStoreDetailsDateTime.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					mainLabelStoreDetailsDateTime.setText(MainFuncs.getDay() + ", " + MainFuncs.getDate()+ ", "+ MainFuncs.getTime());
					mainLabelStoreDetailsDateTime.setFont(defaultFont);
				}
			}
			
			// initialize gui view
			//////////////////////
			
			MainFuncs.initializeTablesAndFields();
			
			//////////////////////
			
			//StockFiller.writeFillingTrans();//TODO this row is for inserting initial stock
			
		} catch (Exception e) {
			e.printStackTrace();
			Debug.log(e.getMessage(),DebugOutput.FILE,DebugOutput.STDERR);
		}
	}

	//////////////////////////
	//	Getters and Setters	//
	//////////////////////////
	
	public static Shell getMainShell(){
		return shell;
	}

	public static Composite getMainGroupStoreDetails() {
		return mainGroupStoreDetails;
	}

	public static Label getMainLabelStoreDetailsStoreID() {
		return mainLabelStoreDetailsStoreID;
	}

	public static Label getMainLabelStoreDetailsDateTime() {
		return mainLabelStoreDetailsDateTime;
	}

	public static Label getMainLabelStoreDetailsStoreAddress() {
		return mainLabelStoreDetailsStoreAddress;
	}

	public static Label getMainLabelStoreDetailsStorePhone() {
		return mainLabelStoreDetailsStorePhone;
	}

	public static Label getMainLabelStoreDetailsStoreManager() {
		return mainLabelStoreDetailsStoreManager;
	}

	public static Composite getMainGroupWelcome() {
		return mainGroupWelcome;
	}

	public static Label getMainLabelWelcomeText() {
		return mainLabelWelcomeText;
	}

	public static TabFolder getMainTabFolder() {
		return mainTabFolder;
	}

	public static TabItem getSearchTabItem() {
		return searchTabItem;
	}

	public static Composite getSearchTabComposite() {
		return searchTabComposite;
	}

	public static Group getSearchGroupOptions() {
		return searchGroupOptions;
	}

	public static Button getSearchBulletByAlbum() {
		return searchBulletByAlbum;
	}

	public static Text getSearchTextBoxAlbumID() {
		return searchTextBoxAlbumID;
	}

	public static Button getSearchBulletOtherParameters() {
		return searchBulletOtherParameters;
	}

	public static Button getSearchCheckBoxAlbumName() {
		return searchCheckBoxAlbumName;
	}

	public static Text getSearchTextBoxAlbumName() {
		return searchTextBoxAlbumName;
	}

	public static Button getSearchCheckBoxArtist() {
		return searchCheckBoxArtist;
	}

	public static Text getSearchTextBoxArtist() {
		return searchTextBoxArtist;
	}

	public static Button getSearchCheckBoxYear() {
		return searchCheckBoxYear;
	}

	public static Text getSearchTextBoxYearFrom() {
		return searchTextBoxYearFrom;
	}

	public static Label getSearchLabelYearTo() {
		return searchLabelYearTo;
	}

	public static Text getSearchTextBoxYearTo() {
		return searchTextBoxYearTo;
	}

	public static Button getSearchCheckBoxSongNames() {
		return searchCheckBoxSongNames;
	}

	public static Text getSearchTextBoxSongNames() {
		return searchTextBoxSongNames;
	}

	public static Composite getSearchCompositeStockField() {
		return searchCompositeStockField;
	}

	public static Label getSearchLabelStock() {
		return searchLabelStock;
	}

	public static Button getSearchBulletInStockAll() {
		return searchBulletInStockAll;
	}

	public static Button getSearchBulletInStockInStore() {
		return searchBulletInStockInStore;
	}

	public static Button getSearchBulletInStockInNetwork() {
		return searchBulletInStockInNetwork;
	}

	public static Button getSearchCheckBoxGenres() {
		return searchCheckBoxGenres;
	}

	public static Button[] getSearchCheckBoxGenresArr(){
		return searchCheckBoxGenresArr;
	}
	
	public static Button getSearchCheckBoxGenreRock() {
		return searchCheckBoxGenresArr[0];
	}
	
	public static Button getSearchCheckBoxGenreJazz() {
		return searchCheckBoxGenresArr[1];
	}

	public static Button getSearchCheckBoxGenre03() {
		return searchCheckBoxGenresArr[2];
	}

	public static Button getSearchCheckBoxGenre04() {
		return searchCheckBoxGenresArr[3];
	}

	public static Button getSearchCheckBoxGenre05() {
		return searchCheckBoxGenresArr[4];
	}

	public static Button getSearchCheckBoxGenre06() {
		return searchCheckBoxGenresArr[5];
	}

	public static Button getSearchCheckBoxGenre07() {
		return searchCheckBoxGenresArr[6];
	}

	public static Button getSearchCheckBoxGenre08() {
		return searchCheckBoxGenresArr[7];
	}

	public static Button getSearchCheckBoxGenre09() {
		return searchCheckBoxGenresArr[8];
	}

	public static Button getSearchCheckBoxGenre10() {
		return searchCheckBoxGenresArr[9];
	}

	public static Button getSearchCheckBoxGenreOther() {
		return searchCheckBoxGenreOther;
	}

	public static Text getSearchTextBoxGenreOther() {
		return searchTextBoxGenreOther;
	}

	public static Button getSearchButtonClear() {
		return searchButtonClear;
	}

	public static Button getSearchButtonSearch() {
		return searchButtonSearch;
	}

	public static Group getSearchGroupResults() {
		return searchGroupResults;
	}

	public static Table getSearchTableAlbumResults() {
		return searchTableAlbumResults;
	}

	public static TableColumn getSearchTableColumnAlbumID() {
		return searchTableColumnAlbumID;
	}

	public static TableColumn getSearchTableColumnAlbumName() {
		return searchTableColumnAlbumName;
	}

	public static TableColumn getSearchTableColumnAlbumArtist() {
		return searchTableColumnAlbumArtist;
	}

	public static TableColumn getSearchTableColumnAlbumYear() {
		return searchTableColumnAlbumYear;
	}

	public static TableColumn getSearchTableColumnAlbumGenre() {
		return searchTableColumnAlbumGenre;
	}

	public static TableColumn getSearchTableColumnAlbumLength() {
		return searchTableColumnAlbumLength;
	}

	public static Label getSearchProgressBarLabel() {
		return searchProgressBarLabel;
	}

	public static Table getSearchTableSongResults() {
		return searchTableSongResults;
	}

	public static TableColumn getSearchTableColumnSongName() {
		return searchTableColumnSongName;
	}

	public static TableColumn getSearchTableColumnSongArtist() {
		return searchTableColumnSongArtist;
	}

	public static TableColumn getSearchTableColumnSongLength() {
		return searchTableColumnSongLength;
	}

	public static Group getSearchGroupStockInfo() {
		return searchGroupStockInfo;
	}

	public static Label getSearchLabelStockInfoStoreStock() {
		return searchLabelStockInfoStoreStock;
	}

	public static Label getSearchLabelStockInfoLocation() {
		return searchLabelStockInfoLocation;
	}

	public static Label getSearchLabelStockInfoPrice() {
		return searchLabelStockInfoPrice;
	}

	public static Button getSearchButtonStockInfoOrder() {
		return searchButtonStockInfoOrder;
	}

	public static Group getSearchGroupSaleInfo() {
		return searchGroupSaleInfo;
	}

	public static Label getSearchLabelSaleInfoQuantity() {
		return searchLabelSaleInfoQuantity;
	}

	public static Text getSearchTextBoxSaleInfoQuantity() {
		return searchTextBoxSaleInfoQuantity;
	}

	public static Button getSearchButtonSaleInfoSale() {
		return searchButtonSaleInfoSale;
	}

	public static TabItem getSaleTabItem() {
		return saleTabItem;
	}

	public static Composite getSaleCompositeMain() {
		return saleCompositeMain;
	}

	public static Group getSaleGroupSaleDetails() {
		return saleGroupSaleDetails;
	}

	public static Label getSaleLabelSaleDate() {
		return saleLabelSaleDate;
	}

	public static Label getSaleLabelDateInput() {
		return saleLabelDateInput;
	}

	public static Label getSaleLabelSalesmanIDName() {
		return saleLabelSalesmanIDName;
	}

	public static Combo getSaleComboSalesmanIDNameInput() {
		return saleComboSalesmanIDNameInput;
	}

	public static Label getSaleLabelSaleTime() {
		return saleLabelSaleTime;
	}

	public static Label getSaleLabelTimeInput() {
		return saleLabelTimeInput;
	}

	public static Table getSaleTableSaleItems() {
		return saleTableSaleItems;
	}

	public static TableColumn getSaleTableColumnAlbumID() {
		return saleTableColumnAlbumID;
	}

	public static TableColumn getSaleTableColumnAlbumName() {
		return saleTableColumnAlbumName;
	}

	public static TableColumn getSaleTableColumnQuantity() {
		return saleTableColumnQuantity;
	}

	public static TableColumn getSaleTableColumnPricePerItem() {
		return saleTableColumnPricePerItem;
	}

	public static TableColumn getSaleTableColumnPriceTotal() {
		return saleTableColumnPriceTotal;
	}

	public static Button getSaleButtonRemoveItem() {
		return saleButtonRemoveItem;
	}

	public static Button getSaleButtonClearSale() {
		return saleButtonClearSale;
	}

	public static Label getSaleLabelTotalPrice() {
		return saleLabelTotalPrice;
	}

	public static Label getSaleLabelTotalPriceValue() {
		return saleLabelTotalPriceValue;
	}

	public static Button getSaleButtonMakeSale() {
		return saleButtonMakeSale;
	}

	public static TabItem getStockTabItem() {
		return stockTabItem;
	}

	public static Composite getStockTabComposite() {
		return stockTabComposite;
	}

	public static Group getStockGroupOrderForm() {
		return stockGroupOrderForm;
	}

	public static Label getStockLabelAlbumID() {
		return stockLabelAlbumID;
	}

	public static Label getStockLabelAlbumIDInput() {
		return stockLabelAlbumIDInput;
	}

	public static Label getStockLabelDate() {
		return stockLabelDate;
	}

	public static Label getStockLabelOrderDateInput() {
		return stockLabelOrderDateInput;
	}

	public static Button getStockButtonCheckAvailability() {
		return stockButtonCheckAvailability;
	}

	public static Label getStockLabelOrderFromStore() {
		return stockLabelOrderFromStore;
	}

	public static Table getStockTableOrderAvailableStores() {
		return stockTableOrderAvailableStores;
	}

	public static TableColumn getStockTableColumnStoreID() {
		return stockTableColumnStoreID;
	}

	public static TableColumn getStockTableColumnStoreCity() {
		return stockTableColumnStoreCity;
	}

	public static TableColumn getStockTableColumnQuantity() {
		return stockTableColumnQuantity;
	}

	public static Label getStockLabelPrice() {
		return stockLabelPrice;
	}

	public static Label getStockLabelStorePriceInput() {
		return stockLabelStorePriceInput;
	}

	public static Label getStockLabelQuantityInStock() {
		return stockLabelQuantityInStock;
	}

	public static Label getStockLabelQuantityInStockInput() {
		return stockLabelQuantityInStockInput;
	}

	public static Label getStockLabelStorageLocation() {
		return stockLabelStorageLocation;
	}

	public static Label getStockLabelStorageLocationInput() {
		return stockLabelStorageLocationInput;
	}

	public static Label getStockLabelQuantityToOrder() {
		return stockLabelQuantityToOrder;
	}

	public static Text getStockTextBoxQuantityToOrder() {
		return stockTextBoxQuantityToOrder;
	}

	public static Button getStockButtonClearOrder() {
		return stockButtonClearOrder;
	}

	public static Button getStockButtonPlaceOrder() {
		return stockButtonPlaceOrder;
	}

	public static Label getStockLabelOrders() {
		return stockLabelOrders;
	}

	public static Table getStockTableOrders() {
		return stockTableOrders;
	}

	public static TableColumn getStockTableColumnOrdersOrderID() {
		return stockTableColumnOrdersOrderID;
	}

	public static TableColumn getStockTableColumnOrdersSupplierID() {
		return stockTableColumnOrdersSupplierID;
	}

	public static TableColumn getStockTableColumnOrdersAlbumID() {
		return stockTableColumnOrdersAlbumID;
	}

	public static TableColumn getStockTableColumnOrdersQuantity() {
		return stockTableColumnOrdersQuantity;
	}

	public static TableColumn getStockTableColumnOrdersDate() {
		return stockTableColumnOrdersDate;
	}

	public static TableColumn getStockTableColumnOrdersStatus() {
		return stockTableColumnOrdersStatus;
	}

	public static TableColumn getStockTableColumnOrdersCompletionDate() {
		return stockTableColumnOrdersCompletionDate;
	}

	public static Button getStockButtonRemoveOrder() {
		return stockButtonRemoveOrder;
	}

	public static Button getStockButtonCancelOrder() {
		return stockButtonCancelOrder;
	}

	public static Label getStockLabelRequests() {
		return stockLabelRequests;
	}

	public static Table getStockTableRequests() {
		return stockTableRequests;
	}

	public static TableColumn getStockTableColumnRequestsOrderID() {
		return stockTableColumnRequestsOrderID;
	}

	public static TableColumn getStockTableColumnRequestsOrderingStoreID() {
		return stockTableColumnRequestsOrderingStoreID;
	}

	public static TableColumn getStockTableColumnRequestsAlbumID() {
		return stockTableColumnRequestsAlbumID;
	}

	public static TableColumn getStockTableColumnRequestsQuantity() {
		return stockTableColumnRequestsQuantity;
	}

	public static TableColumn getStockTableColumnRequestsDate() {
		return stockTableColumnRequestsDate;
	}

	public static TableColumn getStockTableColumnRequestsStatus() {
		return stockTableColumnRequestsStatus;
	}

	public static TableColumn getStockTableColumnRequestsCompletionDate() {
		return stockTableColumnRequestsCompletionDate;
	}

	public static Button getStockButtonDenyRequest() {
		return stockButtonDenyRequest;
	}

	public static Button getStockButtonApproveRequest() {
		return stockButtonApproveRequest;
	}

	public static TabItem getManagementTabItem() {
		return managementTabItem;
	}

	public static Composite getManageMainComposite() {
		return manageMainComposite;
	}

	public static Label getManageLabelEmployees() {
		return manageLabelEmployees;
	}

	public static Table getManageTableEmployees() {
		return manageTableEmployees;
	}

	public static TableColumn getManageTableColumnEmployeeID() {
		return manageTableColumnEmployeeID;
	}

	public static TableColumn getManageTableColumnEmployeePName() {
		return manageTableColumnEmployeePName;
	}

	public static TableColumn getManageTableColumnEmployeeLName() {
		return manageTableColumnEmployeeLName;
	}

	public static TableColumn getManageTableColumnEmployeePosition() {
		return manageTableColumnEmployeePosition;
	}

	public static Group getManageGroupEditEmployee() {
		return manageGroupEditEmployee;
	}

	public static Label getManageLabelEmployeeEmploymentDateInput() {
		return manageLabelEmployeeEmploymentDateInput;
	}

	public static Label getManageLabelEmployeeEmploymentDate() {
		return manageLabelEmployeeEmploymentDate;
	}

	public static Label getManageLabelEmployeeStoreID() {
		return manageLabelEmployeeStoreID;
	}

	public static Label getManageLabelEmployeeEmployeeStoreIDInput() {
		return manageLabelEmployeeEmployeeStoreIDInput;
	}

	public static Label getManageLabelEmployeeID() {
		return manageLabelEmployeeID;
	}

	public static Text getManageTextBoxEmployeeIDInput() {
		return manageTextBoxEmployeeIDInput;
	}

	public static Label getManageLabelEmployeeBirth() {
		return manageLabelEmployeeBirth;
	}

	public static Text getManageTextBoxEmployeeBirthInput() {
		return manageTextBoxEmployeeBirthInput;
	}

	public static Label getManageLabelEmployeeFName() {
		return manageLabelEmployeeFName;
	}

	public static Text getManageTextBoxEmployeeFNameInput() {
		return manageTextBoxEmployeeFNameInput;
	}

	public static Label getManageLabelEmployeeLName() {
		return manageLabelEmployeeLName;
	}

	public static Text getManageTextBoxEmployeeLNameInput() {
		return manageTextBoxEmployeeLNameInput;
	}

	public static Label getManageLabelEmployeeAddress() {
		return manageLabelEmployeeAddress;
	}

	public static Text getManageTextBoxEmployeeAddressInput() {
		return manageTextBoxEmployeeAddressInput;
	}

	public static Label getManageLabelEmployeePhone() {
		return manageLabelEmployeePhone;
	}

	public static Text getManageTextBoxEmployeePhoneInput() {
		return manageTextBoxEmployeePhoneInput;
	}

	public static Label getManageLabelEmployeeCellPhone() {
		return manageLabelEmployeeCellPhone;
	}

	public static Text getManageTextBoxEmployeeCellPhoneInput() {
		return manageTextBoxEmployeeCellPhoneInput;
	}

	public static Label getManageLabelEmployeePosition() {
		return manageLabelEmployeePosition;
	}

	public static Combo getManageComboEmployeePositionInput() {
		return manageComboEmployeePositionInput;
	}

	public static Button getManageButtonEmployeeNew() {
		return manageButtonEmployeeNew;
	}

	public static Button getManageButtonEmployeeInsert() {
		return manageButtonEmployeeInsert;
	}

	public static Button getManageButtonEmployeeNoSave() {
		return manageButtonEmployeeNoSave;
	}

	public static Button getManageButtonEmployeeEdit() {
		return manageButtonEmployeeEdit;
	}

	public static Button getManageButtonEmployeeSave() {
		return manageButtonEmployeeSave;
	}

	public static Button getManageButtonEmployeeRemove() {
		return manageButtonEmployeeRemove;
	}

	public static Group getManageGroupDBSManage() {
		return manageGroupDBSManage;
	}

	public static Label getManageLabelDBSUpdate() {
		return manageLabelDBSUpdate;
	}

	public static Text getManageTextBoxDBSUpdateFileInput() {
		return manageTextBoxDBSUpdateFileInput;
	}

	public static Button getManageButtonDBSBrowse() {
		return manageButtonDBSBrowse;
	}

	public static Button getManageButtonDBSUpdate() {
		return manageButtonDBSUpdate;
	}

	public static void setShell(Shell shell) {
		Main.shell = shell;
	}

	public static Panel getManagePanelDBProgress() {
		return managePanelDBProgress;
	}

	public static Frame getManageFrameDBProgress() {
		return manageFrameDBProgress;
	}

	public static Composite getManageCompositeDBProgressContainer() {
		return manageCompositeDBProgressContainer;
	}

	public static JLabel getManageJLabelDBProgressBar() {
		return manageJLabelDBProgressBar;
	}

	public static Composite getSearchCompositeDBProgressContainer() {
		return searchCompositeDBProgressContainer;
	}

	public static void setSearchCompositeDBProgressContainer(
			Composite searchCompositeDBProgressContainer) {
		Main.searchCompositeDBProgressContainer = searchCompositeDBProgressContainer;
	}

	public static Display getMainDisplay() {
		return display;
	}

	public static void setDisplay(Display display) {
		Main.display = display;
	}

	public static Button getStockButtonRefreshOrders() {
		return stockButtonRefreshOrders;
	}

	public static void setStockButtonRefreshOrders(Button stockButtonRefreshOrders) {
		Main.stockButtonRefreshOrders = stockButtonRefreshOrders;
	}

	public static Button getStockButtonRefreshRequests() {
		return stockButtonRefreshRequests;
	}

	public static void setStockButtonRefreshRequests(
			Button stockButtonRefreshRequests) {
		Main.stockButtonRefreshRequests = stockButtonRefreshRequests;
	}

	public static Button getSearchButtonShowSongs() {
		return searchButtonShowSongs;
	}

	public static void setSearchButtonShowSongs(Button searchButtonShowSongs) {
		Main.searchButtonShowSongs = searchButtonShowSongs;
	}

	public static Button getSearchButtonGetStockInfo() {
		return searchButtonGetStockInfo;
	}

	public static void setSearchButtonGetStockInfo(Button searchButtonGetStockInfo) {
		Main.searchButtonGetStockInfo = searchButtonGetStockInfo;
	}

	public static Button getStockButtonPlaceOrderSupplier() {
		return stockButtonPlaceOrderSupplier;
	}

	public static void setStockButtonPlaceOrderSupplier(
			Button stockButtonPlaceOrderSupplier) {
		Main.stockButtonPlaceOrderSupplier = stockButtonPlaceOrderSupplier;
	}

	public static int getCentral_x() {
		return central_x;
	}

	public static void setCentral_x(int centralX) {
		central_x = centralX;
	}

	public static int getCentral_y() {
		return central_y;
	}

	public static void setCentral_y(int centralY) {
		central_y = centralY;
	}

	public static String getSep() {
		return sep;
	}

	public static String getRelPath() {
		return relPath;
	}
}
