package com.live4music.client.tables;

import java.util.*;

/**
 * created by Ariel
 * 
 * Current sale table data structure
 * =================================
 * holds information on current sale:
 * - sale id
 * - salesman
 * - sale date
 * - sale time
 * - list of sale items (SaleTableItems)
 */
public class SaleTable extends Table {
	private EmployeesTableItem salesman;
	private String date;			//TODO format may be changed later on
	private String time;
	private Map<Long,SaleTableItem> saleItems = new HashMap<Long,SaleTableItem>();
	
	/**
	 * constructor for the sale table
	 * @param saleID
	 */
	public SaleTable(){
		super("SaleTable");
	}
	
	/**
	 * constructor for the sale table with all values
	 * @param salesman
	 * @param date
	 * @param saleItems
	 */
	public SaleTable(EmployeesTableItem salesman, String date, HashMap<Long,SaleTableItem> saleItems){
		super("SaleTable");
		this.salesman = salesman;
		this.date = date;
		this.saleItems = saleItems;
	}
	
	/**
	 * add sale item to sale table
	 * @param saleItem
	 */
	public void addSaleItem(SaleTableItem saleItem){
		this.saleItems.put(saleItem.getAlbumID(), saleItem);
	}
	
	/**
	 * getter for sale item by album id
	 * returns null if doesn't exist in sale table
	 * @param albumID
	 * @return
	 */
	public SaleTableItem getSaleItem(long albumID){
		return this.saleItems.get(albumID);
	}

	public EmployeesTableItem getSalesman() {
		return salesman;
	}

	public void setSalesman(EmployeesTableItem salesman) {
		this.salesman = salesman;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Map<Long, SaleTableItem> getSaleItems() {
		return saleItems;
	}

	public void setSaleItems(Map<Long, SaleTableItem> saleItems) {
		this.saleItems = saleItems;
	}
	
	/**
	 * returns the total price for the sale
	 * @return
	 */
	public int getTotalSalePrice(){
		int sum = 0;
		for(SaleTableItem sti: saleItems.values()){
			sum += sti.getTotalPerItem();
		}
		return sum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
