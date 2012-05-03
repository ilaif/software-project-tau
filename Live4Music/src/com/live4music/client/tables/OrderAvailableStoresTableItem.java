package com.live4music.client.tables;

/**
 * created by Ariel
 * 
 * order's available stores table item
 * ===================================
 * holds information about available store to order from:
 * - store id
 * - city
 * - quantity
 */
public class OrderAvailableStoresTableItem {
	private int storeID;
	private String city;
	private int quantity;
	
	/**
	 * constructor for order's available store
	 * @param storeID
	 * @param city
	 * @param quantity
	 */
	public OrderAvailableStoresTableItem(int storeID, String city, int quantity){
		this.storeID = storeID;
		this.city = city;
		this.quantity = quantity;
	}

	public int getStoreID() {
		return storeID;
	}

	public String getCity() {
		return city;
	}

	public int getQuantity() {
		return quantity;
	}
}
