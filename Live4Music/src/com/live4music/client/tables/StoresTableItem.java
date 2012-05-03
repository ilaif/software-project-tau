package com.live4music.client.tables;

/**
 * created by Ariel
 * 
 * store table item
 * ================
 * holds store information initialized at the beginning of the program:
 * - store id
 * - city
 * - address
 * - phone
 * - manager id
 */
public class StoresTableItem {
	private int storeID;
	private String city;
	private String address;
	private String phone;
	private int managerID;
	
	/**
	 * constructor for store table item
	 * @param storeID
	 */
	public StoresTableItem(int storeID){
		this.storeID = storeID;
	}
	
	/**
	 * constructor for store table item with all fields 
	 * @param storeID
	 * @param city
	 * @param address
	 * @param phone
	 * @param managerID
	 */
	public StoresTableItem(int storeID, String city, String address, String phone, int managerID){
		this.storeID = storeID;
		this.city = city;
		this.address = address;
		this.phone = phone;
		this.managerID = managerID;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	public int getStoreID() {
		return storeID;
	}

}
