package com.live4music.client.tables;

import java.util.*;

/**
 * 
 * order's available stores data structure
 * 
 * holds all stores that are available for an order, generated from query 
 */
public class OrderAvailableStoresTable extends Table {
	
	private Map<Integer,OrderAvailableStoresTableItem> availableStores = new HashMap<Integer,OrderAvailableStoresTableItem>();
	
	/**
	 * constructor for order's available stores
	 */
	public OrderAvailableStoresTable(){
		super("OrderAvailableStoresTable");
	}
	
	/**
	 * constructor for order's available stores with given table
	 * @param availableStores
	 */
	public OrderAvailableStoresTable(HashMap<Integer,OrderAvailableStoresTableItem> availableStores){
		super("OrderAvailableStoresTable");
		this.availableStores = availableStores;
	}
	
	/////////////////////////////////
	// adders, getters and setters //
	/////////////////////////////////
	
	/**
	 * adds given store to table
	 * @param OrderAvailableStoresTableItem 
	 */
	public void addStore(OrderAvailableStoresTableItem store){
		this.availableStores.put(store.getStoreID(), store);
	}
	
	/**
	 * adds given explicit store to table
	 * @param storeID
	 * @param city
	 * @param quantity
	 * @param price
	 */
	public void addStore(int storeID, String city, int quantity){
		OrderAvailableStoresTableItem store = new OrderAvailableStoresTableItem(storeID,
				city, quantity);
		this.availableStores.put(storeID, store);
	}
	
	/**
	 * get store's tuple in table
	 * @param storeID
	 * @return
	 */
	public OrderAvailableStoresTableItem getStore(int storeID){
		return this.getStore(storeID);
	}

	public Map<Integer, OrderAvailableStoresTableItem> getAvailableStores() {
		return availableStores;
	}

	public void setAvailableStores(
			Map<Integer, OrderAvailableStoresTableItem> availableStores) {
		this.availableStores = availableStores;
	}
}
