package com.live4music.client.tables;

import java.util.*;

/**
 * created by Ariel
 * 
 * stores table data structure
 * ===========================
 * holds informtaion about all network stores
 */
public class StoresTable extends Table {
	private Map<Integer,StoresTableItem> stores = new HashMap<Integer,StoresTableItem>();
	
	/**
	 * constructor for stores table
	 */
	public StoresTable(){
		super("StoresTable");
	}
	
	/**
	 * adder for store item
	 * @param store
	 */
	public void addStore(StoresTableItem store){
		this.stores.put(store.getStoreID(),	store);
	}
	
	/**
	 * adder for store item with all store item fields
	 * @param storeID
	 * @param city
	 * @param address
	 * @param phone
	 * @param managerID
	 */
	public void addStore(int storeID, String city, String address, String phone, int managerID){
		StoresTableItem store = new StoresTableItem(storeID,city,address,phone,managerID);
		this.stores.put(storeID, store);
	}
	
	/**
	 * getter for store item
	 * @param storeID
	 * @return
	 */
	public StoresTableItem getStore(int storeID){
		return this.stores.get(storeID);
	}
	
	/**
	 * getter for stores list
	 * @return
	 */
	public Map<Integer,StoresTableItem> getStores(){
		return stores; 
	}
}
