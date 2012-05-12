package com.live4music.client.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

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
	private static Button[] searchCheckBoxGenresArr = new Button[6];
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
	private static Composite searchCompositeDBProgressContainer;
	
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
	private static Composite manageCompositeDBProgressContainer;
	private TabItem importDataTabItem;
	private Composite importDataComposite;
	private Label lblProccessing;
	private Label searchLabelDBProgressBar;
	private TabItem homeTabItem;
	private Composite homeComposite;
	private Label lblSearchMusicImage;
	private Label lblSalesHistoryImage;
	private Label lblManageStockImage;
	private Label lblEmployeesImage;
	private Label lblOrdersImage;
	private Label lblImportDataImage;
	private Label lblSearchMusic;
	private Label lblSales;
	private Label lblManageStock;
	private Label lblEmployees;
	private Label lblOrders;
	private Label lblImportData;
	private TabItem OrdersRequestsTabItem;
	private Composite ordersComposite;

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
					homeTabItem = new TabItem(mainTabFolder, SWT.NONE);
					homeTabItem.setText("Home");
					{
						homeComposite = new Composite(mainTabFolder, SWT.NONE);
						homeComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
						homeComposite.setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(0, 0, 0));
						homeTabItem.setControl(homeComposite);
						{
							lblSearchMusicImage = new Label(homeComposite, SWT.NONE);
							lblSearchMusicImage.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblSearchMusicImage.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(1);
								}
							});
							lblSearchMusicImage.setAlignment(SWT.CENTER);
							lblSearchMusicImage.setBounds(128, 10, 196, 196);
							Image musicIcon = new Image(display,relPath+"music.png");
							lblSearchMusicImage.setImage(musicIcon);
						}
						{
							lblSalesHistoryImage = new Label(homeComposite, SWT.NONE);
							lblSalesHistoryImage.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblSalesHistoryImage.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(2);
								}
							});
							lblSalesHistoryImage.setAlignment(SWT.CENTER);
							lblSalesHistoryImage.setBounds(390, 10, 196, 196);
							Image historyIcon = new Image(display,relPath+"sale.png");
							lblSalesHistoryImage.setImage(historyIcon);
						}
						{
							lblManageStockImage = new Label(homeComposite, SWT.NONE);
							lblManageStockImage.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblManageStockImage.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(3);
								}
							});
							lblManageStockImage.setAlignment(SWT.CENTER);
							lblManageStockImage.setBounds(652, 10, 196, 196);
							Image stockIcon = new Image(display,relPath+"stock.png");
							lblManageStockImage.setImage(stockIcon);
						}
						{
							lblEmployeesImage = new Label(homeComposite, SWT.NONE);
							lblEmployeesImage.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblEmployeesImage.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(4);
								}
							});
							lblEmployeesImage.setAlignment(SWT.CENTER);
							lblEmployeesImage.setBounds(128, 272, 196, 196);
							Image employeesIcon = new Image(display,relPath+"employees.png");
							lblEmployeesImage.setImage(employeesIcon);
						}
						{
							lblOrdersImage = new Label(homeComposite, SWT.NONE);
							lblOrdersImage.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblOrdersImage.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(5);
								}
							});
							lblOrdersImage.setAlignment(SWT.CENTER);
							lblOrdersImage.setBounds(390, 272, 196, 196);
							Image ordersIcon = new Image(display,relPath+"orders.png");
							lblOrdersImage.setImage(ordersIcon);
						}	
						{
							lblImportDataImage = new Label(homeComposite, SWT.NONE);
							lblImportDataImage.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblImportDataImage.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(6);
								}
							});
							lblImportDataImage.setAlignment(SWT.CENTER);
							lblImportDataImage.setBounds(652, 272, 196, 196);
							Image importIcon = new Image(display,relPath+"import.png");
							lblImportDataImage.setImage(importIcon);
						}						
						{
							lblSearchMusic = new Label(homeComposite, SWT.NONE);
							lblSearchMusic.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(1);
								}
							});
							lblSearchMusic.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblSearchMusic.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
							lblSearchMusic.setAlignment(SWT.CENTER);
							lblSearchMusic.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
							lblSearchMusic.setBounds(128, 212, 196, 35);
							lblSearchMusic.setText("Search 4 Music");
						}
						{
							lblSales = new Label(homeComposite, SWT.NONE);
							lblSales.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(2);
								}
							});
							lblSales.setText("Current Sales");
							lblSales.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
							lblSales.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
							lblSales.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblSales.setAlignment(SWT.CENTER);
							lblSales.setBounds(390, 212, 196, 35);
						}
						{
							lblManageStock = new Label(homeComposite, SWT.NONE);
							lblManageStock.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(3);
								}
							});
							lblManageStock.setText("Manage Stock");
							lblManageStock.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
							lblManageStock.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
							lblManageStock.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblManageStock.setAlignment(SWT.CENTER);
							lblManageStock.setBounds(652, 212, 196, 35);
						}
						{
							lblEmployees = new Label(homeComposite, SWT.NONE);
							lblEmployees.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(4);
								}
							});
							lblEmployees.setText("Employees");
							lblEmployees.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
							lblEmployees.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
							lblEmployees.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblEmployees.setAlignment(SWT.CENTER);
							lblEmployees.setBounds(128, 474, 196, 35);
						}
						{
							lblOrders = new Label(homeComposite, SWT.NONE);
							lblOrders.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(5);
								}
							});
							lblOrders.setText("Orders / Requests");
							lblOrders.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
							lblOrders.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
							lblOrders.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblOrders.setAlignment(SWT.CENTER);
							lblOrders.setBounds(390, 474, 196, 35);
						}
						{
							lblImportData = new Label(homeComposite, SWT.NONE);
							lblImportData.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseDown(MouseEvent arg0) {
									mainTabFolder.setSelection(6);
								}
							});
							lblImportData.setText("Import Data");
							lblImportData.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
							lblImportData.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
							lblImportData.setCursor(org.eclipse.wb.swt.SWTResourceManager.getCursor(SWT.CURSOR_HAND));
							lblImportData.setAlignment(SWT.CENTER);
							lblImportData.setBounds(652, 474, 196, 35);
						}
					}
				}

				{
					/*
					 * Search Tab
					 * ==========
					 * Contains search fields, results and stock and sale actions
					 */
					searchTabItem = new TabItem(mainTabFolder, SWT.NONE);
					searchTabItem.setText("Search 4 Music");
					searchTabItem.setToolTipText("Search for Albums");
					{
						searchTabComposite = new Composite(mainTabFolder, SWT.NONE);
						searchTabComposite.setLayout(null);
						searchTabItem.setControl(searchTabComposite);
						{
							searchGroupOptions = new Group(searchTabComposite, SWT.NONE);
							searchGroupOptions.setLayout(null);
							searchGroupOptions.setText("Step 1 - Search 4 Albums");
							searchGroupOptions.setBounds(10, 10, 383, 514);
							searchGroupOptions.setFont(defaultFont);
							{
								searchBulletByAlbum = new Button(searchGroupOptions, SWT.RADIO | SWT.LEFT);
								searchBulletByAlbum.setText("Search by album ID:");
								searchBulletByAlbum.setBounds(10, 16, 129, 22);
								searchBulletByAlbum.setSelection(true);
								searchBulletByAlbum.setFont(defaultFont);
							}
							{
								searchBulletOtherParameters = new Button(searchGroupOptions, SWT.RADIO | SWT.LEFT);
								searchBulletOtherParameters.setText("Search by other parameters:");
								searchBulletOtherParameters.setBounds(10, 44, 173, 22);
								searchBulletOtherParameters.setFont(defaultFont);
							}
							{
								searchTextBoxAlbumID = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxAlbumID.setBounds(190, 17, 182, 22);
								searchTextBoxAlbumID.setToolTipText("Enter album ID");
								searchTextBoxAlbumID.setFont(defaultFont);
							}
							{
								searchCheckBoxAlbumName = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxAlbumName.setText("Album name:");
								searchCheckBoxAlbumName.setBounds(10, 72, 102, 22);
								searchCheckBoxAlbumName.setFont(defaultFont);
							}
							{
								searchCheckBoxArtist = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxArtist.setText("Artist:");
								searchCheckBoxArtist.setBounds(10, 113, 102, 21);
								searchCheckBoxArtist.setFont(defaultFont);
							}
							{
								searchCheckBoxYear = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxYear.setText("Year from:");
								searchCheckBoxYear.setBounds(10, 150, 102, 23);
								searchCheckBoxYear.setFont(defaultFont);
							}
							{
								searchCheckBoxSongNames = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxSongNames.setText("Song name(s):");
								searchCheckBoxSongNames.setBounds(10, 322, 102, 22);
								searchCheckBoxSongNames.setToolTipText("Enter song names / partial names separated by semicolons\n"+
										"e.g. 'first song name;second song name;third song name'");
								searchCheckBoxSongNames.setFont(defaultFont);
							}
							{
								searchCheckBoxGenres = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenres.setText("Genre(s):");
								searchCheckBoxGenres.setBounds(10, 190, 82, 22);
								searchCheckBoxGenres.setFont(defaultFont);
							}
							{
								searchTextBoxAlbumName = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxAlbumName.setBounds(190, 73, 182, 22);
								searchTextBoxAlbumName.setFont(defaultFont);
							}
							{
								searchTextBoxArtist = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxArtist.setBounds(190, 113, 182, 22);
								searchTextBoxArtist.setFont(defaultFont);
							}
							{
								searchTextBoxYearFrom = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxYearFrom.setBounds(190, 151, 70, 22);
								searchTextBoxYearFrom.setFont(defaultFont);
							}
							{
								searchLabelYearTo = new Label(searchGroupOptions, SWT.NONE);
								searchLabelYearTo.setText("To:");
								searchLabelYearTo.setBounds(266, 154, 30, 16);
								searchLabelYearTo.setFont(defaultFont);
							}
							{
								searchTextBoxYearTo = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxYearTo.setBounds(302, 151, 70, 22);
								searchTextBoxYearTo.setFont(defaultFont);
							}
							{
								searchTextBoxSongNames = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxSongNames.setBounds(190, 323, 182, 22);
								searchTextBoxSongNames.setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[0] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[0].setText("Electronic");
								searchCheckBoxGenresArr[0].setBounds(10, 222, 78, 16);
								searchCheckBoxGenresArr[0].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[1] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[1].setText("Blues");
								searchCheckBoxGenresArr[1].setBounds(10, 248, 78, 16);
								searchCheckBoxGenresArr[1].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[2] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[2].setText("Pop");
								searchCheckBoxGenresArr[2].setBounds(112, 222, 78, 16);
								searchCheckBoxGenresArr[2].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[3] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[3].setText("Jazz");
								searchCheckBoxGenresArr[3].setBounds(112, 248, 78, 16);
								searchCheckBoxGenresArr[3].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[4] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[4].setText("Classical");
								searchCheckBoxGenresArr[4].setBounds(212, 222, 73, 16);
								searchCheckBoxGenresArr[4].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[5] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[5].setText("Metal");
								searchCheckBoxGenresArr[5].setBounds(212, 248, 79, 16);
								searchCheckBoxGenresArr[5].setFont(defaultFont);
							}
							/*{
								searchCheckBoxGenresArr[6] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[6].setText("Rock");
								searchCheckBoxGenresArr[6].setBounds(312, 222, 78, 16);
								searchCheckBoxGenresArr[6].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[7] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[7].setText("World");
								searchCheckBoxGenresArr[7].setBounds(312, 248, 78, 16);
								searchCheckBoxGenresArr[7].setFont(defaultFont);
							}
							{
								searchCheckBoxGenresArr[8] = new Button(searchGroupOptions, SWT.CHECK | SWT.LEFT);
								searchCheckBoxGenresArr[8].setText("Hip-Hop");
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
								searchCheckBoxGenreOther.setBounds(10, 280, 70, 22);
								searchCheckBoxGenreOther.setFont(defaultFont);
							}
							{
								searchTextBoxGenreOther = new Text(searchGroupOptions, SWT.BORDER);
								searchTextBoxGenreOther.setBounds(190, 281, 182, 22);
								searchTextBoxGenreOther.setFont(defaultFont);
							}
							{
								searchButtonClear = new Button(searchGroupOptions, SWT.PUSH | SWT.CENTER);
								searchButtonClear.setText("Clear Fields");
								searchButtonClear.setBounds(10, 471, 173, 33);
								searchButtonClear.setFont(defaultFont);
							}
							{
								searchButtonSearch = new Button(searchGroupOptions, SWT.PUSH | SWT.CENTER);
								searchButtonSearch.setText("Search");
								searchButtonSearch.setBounds(190, 471, 182, 33);
								searchButtonSearch.setFont(defaultFont);
							}
							{
								searchCompositeStockField = new Composite(searchGroupOptions, SWT.NONE);
								searchCompositeStockField.setLayout(null);
								searchCompositeStockField.setBounds(10, 364, 358, 90);
								{
									searchLabelStock = new Label(searchCompositeStockField, SWT.NONE);
									searchLabelStock.setText("Stock:");
									searchLabelStock.setBounds(0, 3, 58, 16);
									searchLabelStock.setFont(defaultFont);
								}
								{
									searchBulletInStockInStore = new Button(searchCompositeStockField, SWT.RADIO | SWT.LEFT);
									searchBulletInStockInStore.setText("In store");
									searchBulletInStockInStore.setBounds(180, 56, 70, 22);
									searchBulletInStockInStore.setFont(defaultFont);
								}
								{
									searchBulletInStockInNetwork = new Button(searchCompositeStockField, SWT.RADIO | SWT.LEFT);
									searchBulletInStockInNetwork.setText("In network");
									searchBulletInStockInNetwork.setBounds(180, 28, 86, 22);
									searchBulletInStockInNetwork.setFont(defaultFont);
								}
								{
									searchBulletInStockAll = new Button(searchCompositeStockField, SWT.RADIO | SWT.LEFT);
									searchBulletInStockAll.setText("All");
									searchBulletInStockAll.setBounds(180, 0, 70, 22);
									searchBulletInStockAll.setSelection(true);
									searchBulletInStockAll.setFont(defaultFont);
								}
							}
						}
						{
							searchGroupResults = new Group(searchTabComposite, SWT.NONE);
							searchGroupResults.setLayout(null);
							searchGroupResults.setText("Step 2 - Select Album");
							searchGroupResults.setBounds(399, 10, 414, 514);
							searchGroupResults.setFont(defaultFont);
							{
								searchTableAlbumResults = new Table(searchGroupResults, SWT.BORDER | SWT.FULL_SELECTION
										| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE); // single row selection
								searchTableAlbumResults.setBounds(13, 23, 389, 258);
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
								searchTableColumnAlbumName.setText("Name");
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
								searchTableSongResults.setBounds(13, 323, 389, 181);
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
								searchTableColumnSongName.setText("Song Name");
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
								searchButtonShowSongs = new Button(searchGroupResults, SWT.PUSH | SWT.CENTER);
								searchButtonShowSongs.setText("View Album's Songs");
								searchButtonShowSongs.setBounds(13, 287, 389, 30);
								searchButtonShowSongs.setFont(defaultFont);
							}
						}
						{
							searchGroupStockInfo = new Group(searchTabComposite, SWT.NONE);
							searchGroupStockInfo.setLayout(null);
							searchGroupStockInfo.setText("Step 3 - Check Stock");
							searchGroupStockInfo.setBounds(819, 10, 146, 217);
							searchGroupStockInfo.setFont(defaultFont);
							{
								searchLabelStockInfoStoreStock = new Label(searchGroupStockInfo, SWT.NONE);
								searchLabelStockInfoStoreStock.setText("Store stock:");
								searchLabelStockInfoStoreStock.setBounds(10, 21, 126, 19);
								searchLabelStockInfoStoreStock.setFont(defaultFont);
							}
							{
								searchLabelStockInfoLocation = new Label(searchGroupStockInfo, SWT.NONE);
								searchLabelStockInfoLocation.setText("Location: ");
								searchLabelStockInfoLocation.setBounds(10, 56, 126, 19);
								searchLabelStockInfoLocation.setFont(defaultFont);
							}
							{
								searchLabelStockInfoPrice = new Label(searchGroupStockInfo, SWT.NONE);
								searchLabelStockInfoPrice.setText("Price:");
								searchLabelStockInfoPrice.setBounds(10, 92, 126, 19);
								searchLabelStockInfoPrice.setFont(defaultFont);
							}
							{
								searchButtonGetStockInfo = new Button(searchGroupStockInfo, SWT.PUSH | SWT.CENTER);
								searchButtonGetStockInfo.setText("Check Stock");
								searchButtonGetStockInfo.setBounds(10, 129, 126, 36);
								searchButtonGetStockInfo.setFont(defaultFont);
							}
							{
								searchButtonStockInfoOrder = new Button(searchGroupStockInfo, SWT.PUSH | SWT.CENTER);
								searchButtonStockInfoOrder.setLocation(10, 171);
								searchButtonStockInfoOrder.setSize(126, 36);
								searchButtonStockInfoOrder.setText("Order to Stock");
								searchButtonStockInfoOrder.setFont(defaultFont);
							}
						}
						{
							searchGroupSaleInfo = new Group(searchTabComposite, SWT.NONE);
							searchGroupSaleInfo.setLayout(null);
							searchGroupSaleInfo.setText("Step 4 - Sell");
							searchGroupSaleInfo.setBounds(819, 233, 146, 139);
							searchGroupSaleInfo.setFont(defaultFont);
							{
								searchLabelSaleInfoQuantity = new Label(searchGroupSaleInfo, SWT.NONE);
								searchLabelSaleInfoQuantity.setText("Sell quantity:");
								searchLabelSaleInfoQuantity.setBounds(10, 22, 126, 22);
								searchLabelSaleInfoQuantity.setFont(defaultFont);
							}
							{
								searchTextBoxSaleInfoQuantity = new Text(searchGroupSaleInfo, SWT.BORDER);
								searchTextBoxSaleInfoQuantity.setText("1");
								searchTextBoxSaleInfoQuantity.setBounds(10, 55, 126, 22);
								searchTextBoxSaleInfoQuantity.setFont(defaultFont);
							}
							{
								searchButtonSaleInfoSale = new Button(searchGroupSaleInfo, SWT.PUSH | SWT.CENTER);
								searchButtonSaleInfoSale.setText("Sell");
								searchButtonSaleInfoSale.setBounds(10, 93, 126, 36);
								searchButtonSaleInfoSale.setFont(defaultFont);
							}
						}
						{
							searchCompositeDBProgressContainer = new Composite(searchTabComposite, SWT.EMBEDDED);
							searchCompositeDBProgressContainer.setBounds(819, 378, 146, 124);
							{
								searchLabelDBProgressBar = new Label(searchCompositeDBProgressContainer, SWT.NONE);
								searchLabelDBProgressBar.setAlignment(SWT.CENTER);
								searchLabelDBProgressBar.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
								searchLabelDBProgressBar.setLocation(0, 0);
								searchLabelDBProgressBar.setSize(146, 124);
								searchLabelDBProgressBar.setText("Proccessing Please Wait");
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
					saleTabItem.setText("Current Sales");
					saleTabItem.setToolTipText("Manage current sale");
					{
						saleCompositeMain = new Composite(mainTabFolder, SWT.NONE);
						saleCompositeMain.setLayout(null);
						saleTabItem.setControl(saleCompositeMain);
						{
							saleGroupSaleDetails = new Group(saleCompositeMain, SWT.NONE);
							saleGroupSaleDetails.setLayout(null);
							saleGroupSaleDetails.setText("Current Sale Information");
							saleGroupSaleDetails.setBounds(10, 10, 955, 86);
							saleGroupSaleDetails.setFont(defaultFont);
							{
								saleLabelSalesmanIDName = new Label(saleGroupSaleDetails, SWT.NONE);
								saleLabelSalesmanIDName.setText("Salesman:");
								saleLabelSalesmanIDName.setBounds(10, 27, 75, 18);
								saleLabelSalesmanIDName.setFont(defaultFont);
							}
							{
								saleComboSalesmanIDNameInput = new Combo(saleGroupSaleDetails, SWT.READ_ONLY);
								saleComboSalesmanIDNameInput.setBounds(10, 51, 309, 21);
								saleComboSalesmanIDNameInput.setFont(defaultFont);
							}							
							{
								saleLabelSaleDate = new Label(saleGroupSaleDetails, SWT.NONE);
								saleLabelSaleDate.setText("Date of sale:");
								saleLabelSaleDate.setBounds(361, 27, 92, 18);
								saleLabelSaleDate.setFont(defaultFont);
							}
							{
								saleLabelSaleTime = new Label(saleGroupSaleDetails, SWT.NONE);
								saleLabelSaleTime.setText("Time of sale:");
								saleLabelSaleTime.setBounds(526, 27, 86, 18);
								saleLabelSaleTime.setFont(defaultFont);
							}
							{
								saleLabelDateInput = new Label(saleGroupSaleDetails, SWT.BORDER);
								saleLabelDateInput.setBounds(361, 53, 130, 18);
								saleLabelDateInput.setText(MainFuncs.getDate());
								saleLabelDateInput.setFont(defaultFont);
							}
							{
								saleLabelTimeInput = new Label(saleGroupSaleDetails, SWT.BORDER);
								saleLabelTimeInput.setBounds(526, 53, 130, 18);
								saleLabelTimeInput.setText(MainFuncs.getTime());
								saleLabelTimeInput.setFont(defaultFont);
							}
						}
						{
							saleTableSaleItems = new Table(saleCompositeMain, SWT.BORDER | SWT.FULL_SELECTION
									| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
							saleTableSaleItems.setBounds(10, 102, 955, 346);
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
							saleTableColumnAlbumName.setText("Name");
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
							saleLabelTotalPriceValue.setBounds(871, 461, 94, 22);
							saleLabelTotalPriceValue.setFont(defaultFont);
						}
						{
							saleLabelTotalPrice = new Label(saleCompositeMain, SWT.NONE);
							saleLabelTotalPrice.setText("Total price:");
							saleLabelTotalPrice.setBounds(778, 462, 87, 22);
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
							saleButtonMakeSale.setBounds(872, 489, 93, 35);
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
					stockTabItem.setText("Manage Stock");
					stockTabItem.setToolTipText("View and manage orders and requests");
					{
						stockTabComposite = new Composite(mainTabFolder, SWT.NONE);
						stockTabComposite.setLayout(null);
						stockTabItem.setControl(stockTabComposite);
						{
							stockGroupOrderForm = new Group(stockTabComposite, SWT.NONE);
							stockGroupOrderForm.setLayout(null);
							stockGroupOrderForm.setText("Order Albums");
							stockGroupOrderForm.setBounds(10, 10, 955, 514);
							stockGroupOrderForm.setFont(defaultFont);
							{
								stockLabelAlbumID = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelAlbumID.setText("Album ID:");
								stockLabelAlbumID.setBounds(10, 18, 74, 22);
								stockLabelAlbumID.setFont(defaultFont);
							}
							{
								stockLabelAlbumIDInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelAlbumIDInput.setBounds(10, 43, 193, 22);
								stockLabelAlbumIDInput.setFont(defaultFont);
							}
							{
								stockLabelDate = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelDate.setText("Order date:");
								stockLabelDate.setBounds(10, 72, 74, 22);
								stockLabelDate.setFont(defaultFont);
							}
							{
								stockLabelOrderDateInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelOrderDateInput.setBounds(10, 97, 193, 22);
								stockLabelOrderDateInput.setText(MainFuncs.getDate());
								stockLabelOrderDateInput.setFont(defaultFont);
							}
							{
								stockButtonCheckAvailability = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonCheckAvailability.setText("Check Availability at Other Stores");
								stockButtonCheckAvailability.setBounds(10, 332, 193, 36);
								stockButtonCheckAvailability.setFont(defaultFont);
							}
							{
								stockLabelOrderFromStore = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelOrderFromStore.setText("Album Availability at Stores:");
								stockLabelOrderFromStore.setBounds(216, 18, 214, 24);
								stockLabelOrderFromStore.setFont(defaultFont);
							}
							{
								stockTableOrderAvailableStores = new Table(stockGroupOrderForm, SWT.BORDER | SWT.FULL_SELECTION
										| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
								stockTableOrderAvailableStores.setBounds(216, 43, 729, 451);
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
								stockLabelQuantityInStock.setBounds(10, 179, 182, 22);
								stockLabelQuantityInStock.setFont(defaultFont);
							}
							{
								stockLabelQuantityInStockInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelQuantityInStockInput.setBounds(10, 202, 193, 22);
								stockLabelQuantityInStockInput.setFont(defaultFont);
							}
							{
								stockLabelPrice = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelPrice.setText("Price:");
								stockLabelPrice.setBounds(10, 125, 182, 22);
								stockLabelPrice.setFont(defaultFont);
							}
							{
								stockLabelStorePriceInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelStorePriceInput.setBounds(10, 150, 193, 22);
								stockLabelStorePriceInput.setFont(defaultFont);
							}
							{
								stockLabelStorageLocation = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelStorageLocation.setText("Location:");
								stockLabelStorageLocation.setBounds(10, 281, 182, 22);
								stockLabelStorageLocation.setFont(defaultFont);
							}
							{
								stockLabelStorageLocationInput = new Label(stockGroupOrderForm, SWT.BORDER);
								stockLabelStorageLocationInput.setBounds(10, 304, 193, 22);
								stockLabelStorageLocationInput.setFont(defaultFont);
							}
							{
								stockButtonPlaceOrder = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonPlaceOrder.setText("Order From Selected Store");
								stockButtonPlaceOrder.setBounds(11, 374, 192, 36);
								stockButtonPlaceOrder.setFont(defaultFont);
							}
							{
								stockButtonClearOrder = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonClearOrder.setText("Clear Fields");
								stockButtonClearOrder.setBounds(10, 458, 193, 36);
								stockButtonClearOrder.setFont(defaultFont);
							}
							{
								stockLabelQuantityToOrder = new Label(stockGroupOrderForm, SWT.NONE);
								stockLabelQuantityToOrder.setText("Quantity to order:");
								stockLabelQuantityToOrder.setBounds(10, 230, 182, 22);
								stockLabelQuantityToOrder.setFont(defaultFont);
							}
							{
								stockTextBoxQuantityToOrder = new Text(stockGroupOrderForm, SWT.BORDER);
								stockTextBoxQuantityToOrder.setText("1");
								stockTextBoxQuantityToOrder.setBounds(10, 253, 193, 22);
								stockTextBoxQuantityToOrder.setFont(defaultFont);
							}
							{
								stockButtonPlaceOrderSupplier = new Button(stockGroupOrderForm, SWT.PUSH | SWT.CENTER);
								stockButtonPlaceOrderSupplier.setText("Order from Supplier");
								stockButtonPlaceOrderSupplier.setBounds(10, 416, 193, 36);
								stockButtonPlaceOrderSupplier.setFont(defaultFont);
							}
						}
						{
//							int numOfColumns = 6; //7
							
							/*
							stockTableColumnOrdersCompletionDate = new TableColumn(stockTableOrders, SWT.NONE);
							stockTableColumnOrdersCompletionDate.setText("Completion Date");
							stockTableColumnOrdersCompletionDate.setResizable(true);
							stockTableColumnOrdersCompletionDate.setMoveable(true);
							stockTableColumnOrdersCompletionDate.setWidth(tableWidth / numOfColumns);
							*/
						}
						{
//							int numOfColumns = 6; // 7
							
							/*
							stockTableColumnRequestsCompletionDate = new TableColumn(stockTableRequests, SWT.NONE);
							stockTableColumnRequestsCompletionDate.setText("Completion Date");
							stockTableColumnRequestsCompletionDate.setResizable(true);
							stockTableColumnRequestsCompletionDate.setMoveable(true);
							stockTableColumnRequestsCompletionDate.setWidth(tableWidth / numOfColumns);
							*/
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
					managementTabItem.setText("Employees");
					managementTabItem.setToolTipText("View and manage employees, update database");
					{
						manageMainComposite = new Composite(mainTabFolder, SWT.NONE);
						manageMainComposite.setLayout(null);
						managementTabItem.setControl(manageMainComposite);
						{
							manageLabelEmployees = new Label(manageMainComposite, SWT.NONE);
							manageLabelEmployees.setText("Employees:");
							manageLabelEmployees.setBounds(10, 10, 86, 22);
							manageLabelEmployees.setFont(defaultFont);
						}
						{
							manageTableEmployees = new Table(manageMainComposite, SWT.BORDER | SWT.FULL_SELECTION
									| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
							manageTableEmployees.setBounds(10, 34, 955, 257);
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
							manageGroupEditEmployee.setBounds(10, 297, 953, 227);
							manageGroupEditEmployee.setFont(defaultFont);
							{
								manageLabelEmployeeID = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeID.setText("ID:");
								manageLabelEmployeeID.setBounds(10, 76, 25, 22);
								manageLabelEmployeeID.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeIDInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeIDInput.setBounds(10, 101, 226, 22);
								manageTextBoxEmployeeIDInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeBirth = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeBirth.setText("Date of birth:");
								manageLabelEmployeeBirth.setBounds(246, 76, 78, 22);
								manageLabelEmployeeBirth.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeBirthInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeBirthInput.setBounds(246, 101, 226, 22);
								manageTextBoxEmployeeBirthInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeFName = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeFName.setText("First name:");
								manageLabelEmployeeFName.setBounds(482, 24, 62, 22);
								manageLabelEmployeeFName.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeFNameInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeFNameInput.setBounds(482, 48, 226, 22);
								manageTextBoxEmployeeFNameInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeLName = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeLName.setText("Last name:");
								manageLabelEmployeeLName.setBounds(718, 24, 60, 22);
								manageLabelEmployeeLName.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeLNameInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeLNameInput.setBounds(718, 48, 226, 22);
								manageTextBoxEmployeeLNameInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeEmploymentDate = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeEmploymentDate.setText("Date of employment:");
								manageLabelEmployeeEmploymentDate.setBounds(10, 24, 112, 22);
								manageLabelEmployeeEmploymentDate.setFont(defaultFont);
							}
							{
								manageLabelEmployeeAddress = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeAddress.setText("Address:");
								manageLabelEmployeeAddress.setBounds(482, 76, 51, 22);
								manageLabelEmployeeAddress.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeAddressInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeAddressInput.setBounds(482, 101, 462, 22);
								manageTextBoxEmployeeAddressInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeStoreID = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeStoreID.setText("Employing store ID:");
								manageLabelEmployeeStoreID.setBounds(246, 24, 106, 22);
								manageLabelEmployeeStoreID.setFont(defaultFont);
							}
							{
								manageLabelEmployeeEmployeeStoreIDInput = new Label(manageGroupEditEmployee, SWT.BORDER);
								manageLabelEmployeeEmployeeStoreIDInput.setBounds(246, 48, 226, 22);
								manageLabelEmployeeEmployeeStoreIDInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeeEmploymentDateInput = new Label(manageGroupEditEmployee, SWT.BORDER);
								manageLabelEmployeeEmploymentDateInput.setBounds(10, 48, 226, 22);
								manageLabelEmployeeEmploymentDateInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeePhone = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeePhone.setText("Phone:");
								manageLabelEmployeePhone.setBounds(10, 129, 60, 22);
								manageLabelEmployeePhone.setFont(defaultFont);
							}
							{
								manageLabelEmployeeCellPhone = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeeCellPhone.setText("Cellular Phone:");
								manageLabelEmployeeCellPhone.setBounds(246, 129, 102, 22);
								manageLabelEmployeeCellPhone.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeePhoneInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeePhoneInput.setBounds(10, 151, 226, 22);
								manageTextBoxEmployeePhoneInput.setFont(defaultFont);
							}
							{
								manageTextBoxEmployeeCellPhoneInput = new Text(manageGroupEditEmployee, SWT.BORDER);
								manageTextBoxEmployeeCellPhoneInput.setBounds(246, 151, 226, 22);
								manageTextBoxEmployeeCellPhoneInput.setFont(defaultFont);
							}
							{
								manageLabelEmployeePosition = new Label(manageGroupEditEmployee, SWT.NONE);
								manageLabelEmployeePosition.setText("Position:");
								manageLabelEmployeePosition.setBounds(482, 129, 60, 22);
								manageLabelEmployeePosition.setFont(defaultFont);
							}
							{
								manageComboEmployeePositionInput = new Combo(manageGroupEditEmployee, SWT.READ_ONLY);
								manageComboEmployeePositionInput.setBounds(482, 151, 462, 23);
								manageComboEmployeePositionInput.setFont(defaultFont);
							}
							{
								manageButtonEmployeeNew = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeNew.setText("New");
								manageButtonEmployeeNew.setBounds(10, 187, 150, 30);
								manageButtonEmployeeNew.setFont(defaultFont);
							}
							{
								manageButtonEmployeeInsert = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeInsert.setText("Insert");
								manageButtonEmployeeInsert.setBounds(166, 187, 150, 30);
								manageButtonEmployeeInsert.setFont(defaultFont);
							}
							{
								manageButtonEmployeeEdit = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeEdit.setText("Edit");
								manageButtonEmployeeEdit.setBounds(322, 187, 150, 30);
								manageButtonEmployeeEdit.setFont(defaultFont);
							}
							{
								manageButtonEmployeeSave = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeSave.setText("Save");
								manageButtonEmployeeSave.setBounds(482, 187, 150, 30);
								manageButtonEmployeeSave.setFont(defaultFont);
							}
							{
								manageButtonEmployeeRemove = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeRemove.setText("Remove Employee");
								manageButtonEmployeeRemove.setBounds(638, 187, 150, 30);
								manageButtonEmployeeRemove.setFont(defaultFont);
							}
							{
								manageButtonEmployeeNoSave = new Button(manageGroupEditEmployee, SWT.PUSH | SWT.CENTER);
								manageButtonEmployeeNoSave.setText("Exit Without Saving");
								manageButtonEmployeeNoSave.setBounds(794, 187, 150, 30);
								manageButtonEmployeeNoSave.setFont(defaultFont);
							}
						}
					}
				}
				{
					OrdersRequestsTabItem = new TabItem(mainTabFolder, SWT.NONE);
					OrdersRequestsTabItem.setText("Orders / Requests");
					{
						ordersComposite = new Composite(mainTabFolder, SWT.NONE);
						OrdersRequestsTabItem.setControl(ordersComposite);
						{
							stockButtonCancelOrder = new Button(ordersComposite, SWT.PUSH | SWT.CENTER);
							stockButtonCancelOrder.setBounds(379, 500, 101, 24);
							stockButtonCancelOrder.setText("Cancel Order");
							stockButtonCancelOrder.setFont(defaultFont);
						}
						{
							stockButtonRemoveOrder = new Button(ordersComposite, SWT.PUSH | SWT.CENTER);
							stockButtonRemoveOrder.setBounds(272, 500, 101, 24);
							stockButtonRemoveOrder.setText("Remove Order");
							stockButtonRemoveOrder.setFont(defaultFont);
						}
						{
							stockButtonRefreshOrders = new Button(ordersComposite, SWT.PUSH | SWT.CENTER);
							stockButtonRefreshOrders.setBounds(165, 500, 101, 24);
							stockButtonRefreshOrders.setText("Refresh");
							stockButtonRefreshOrders.setFont(defaultFont);
						}
						{
						stockTableOrders = new Table(ordersComposite, SWT.BORDER | SWT.FULL_SELECTION
								| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
						stockTableOrders.setBounds(10, 35, 470, 459);
						stockTableOrders.setHeaderVisible(true);
						stockTableOrders.setLinesVisible(true);
						stockTableOrders.setFont(defaultFont);
						int tableWidth = stockTableOrders.getClientArea().width - getBorderWidth()*2;
						int numOfColumns = 6;
						
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
						{
							stockButtonApproveRequest = new Button(ordersComposite, SWT.PUSH | SWT.CENTER);
							stockButtonApproveRequest.setBounds(864, 500, 101, 24);
							stockButtonApproveRequest.setText("Approve Request");
							stockButtonApproveRequest.setFont(defaultFont);
						}
						{
							stockButtonDenyRequest = new Button(ordersComposite, SWT.PUSH | SWT.CENTER);
							stockButtonDenyRequest.setBounds(757, 500, 101, 24);
							stockButtonDenyRequest.setText("Deny Request");
							stockButtonDenyRequest.setFont(defaultFont);
						}
						{
							stockButtonRefreshRequests = new Button(ordersComposite, SWT.PUSH | SWT.CENTER);
							stockButtonRefreshRequests.setBounds(650, 500, 101, 24);
							stockButtonRefreshRequests.setText("Refresh");
							stockButtonRefreshRequests.setFont(defaultFont);
						}
						}
						{
							
						stockTableRequests = new Table(ordersComposite, SWT.BORDER | SWT.FULL_SELECTION
								| SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
						stockTableRequests.setBounds(495, 35, 470, 459);
						stockTableRequests.setHeaderVisible(true);
						stockTableRequests.setLinesVisible(true);
						stockTableRequests.setFont(defaultFont);
						int tableWidth = stockTableRequests.getClientArea().width - getBorderWidth()*2;
						int numOfColumns = 6;
						
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
						{
							stockLabelRequests = new Label(ordersComposite, SWT.NONE);
							stockLabelRequests.setLocation(495, 10);
							stockLabelRequests.setSize(64, 20);
							stockLabelRequests.setText("Requests:");
							stockLabelRequests.setFont(defaultFont);
						}
						{
							stockLabelOrders = new Label(ordersComposite, SWT.NONE);
							stockLabelOrders.setLocation(10, 10);
							stockLabelOrders.setSize(60, 20);
							stockLabelOrders.setText("Orders:");
							stockLabelOrders.setFont(defaultFont);
						}
						}
					}
				}
				{
					importDataTabItem = new TabItem(mainTabFolder, SWT.NONE);
					importDataTabItem.setText("Import Data");
					{
						importDataComposite = new Composite(mainTabFolder, SWT.NONE);
						importDataTabItem.setControl(importDataComposite);
						{
							manageGroupDBSManage = new Group(importDataComposite, SWT.NONE);
							manageGroupDBSManage.setLocation(10, 10);
							manageGroupDBSManage.setSize(955, 129);
							manageGroupDBSManage.setLayout(null);
							manageGroupDBSManage.setText("Import New Data");
							manageGroupDBSManage.setFont(defaultFont);
							{
								manageLabelDBSUpdate = new Label(manageGroupDBSManage, SWT.NONE);
								manageLabelDBSUpdate.setText("Select update file:");
								manageLabelDBSUpdate.setBounds(12, 21, 96, 18);
								manageLabelDBSUpdate.setFont(defaultFont);
							}
							{
								manageTextBoxDBSUpdateFileInput = new Text(manageGroupDBSManage, SWT.BORDER);
								manageTextBoxDBSUpdateFileInput.setBounds(12, 42, 933, 22);
								manageTextBoxDBSUpdateFileInput.setEnabled(false);
								manageTextBoxDBSUpdateFileInput.setFont(defaultFont);
							}
							{
								manageButtonDBSBrowse = new Button(manageGroupDBSManage, SWT.PUSH | SWT.CENTER);
								manageButtonDBSBrowse.setText("Browse...");
								manageButtonDBSBrowse.setBounds(753, 70, 74, 23);
								manageButtonDBSBrowse.setFont(defaultFont);
							}
							{
								manageButtonDBSUpdate = new Button(manageGroupDBSManage, SWT.PUSH | SWT.CENTER);
								manageButtonDBSUpdate.setText("Update Database");
								manageButtonDBSUpdate.setBounds(833, 70, 112, 23);
								manageButtonDBSUpdate.setFont(defaultFont);
							}
							{
								manageCompositeDBProgressContainer = new Composite(manageGroupDBSManage, SWT.EMBEDDED);
								manageCompositeDBProgressContainer.setBounds(223, 70, 401, 59);
								{
									lblProccessing = new Label(manageCompositeDBProgressContainer, SWT.NONE);
									lblProccessing.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
									lblProccessing.setAlignment(SWT.CENTER);
									lblProccessing.setBounds(10, 10, 381, 39);
									lblProccessing.setText("Proccessing - Please Wait");
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

	public static Composite getManageCompositeDBProgressContainer() {
		return manageCompositeDBProgressContainer;
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
