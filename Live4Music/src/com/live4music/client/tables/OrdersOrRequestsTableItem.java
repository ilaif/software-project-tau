package com.live4music.client.tables;

/**
 * created by Ariel
 * 
 * Orders / Requests table item
 * ============================
 * holds order / request information:
 * - order id
 * - ordering store id
 * - supplying store id
 * - album id
 * - quantity
 * - order date
 * - status
 */
public class OrdersOrRequestsTableItem {
	private int orderID;
	private int orderingStoreID;
	private int supplyingStoreID;
	private long albumID;
	private int quantity;
	private String date;
	private OrderStatusEnum status;
	
	/**
	 * constructor for orders / requests table item
	 * @param orderID
	 * @param orderingStoreID
	 * @param supplyingStoreID
	 * @param albumID
	 * @param quantity
	 * @param date
	 * @param status
	 */
	public OrdersOrRequestsTableItem(int orderID, int orderingStoreID, int supplyingStoreID,
			long albumID, int quantity, String date, OrderStatusEnum status){
		this.orderID = orderID;
		this.orderingStoreID = orderingStoreID;
		this.supplyingStoreID = supplyingStoreID;
		this.albumID = albumID;
		this.quantity = quantity;
		this.date = date;
		this.status = status;
	}

	/////////////////////////
	// setters and getters //
	/////////////////////////
	
	public int getOrderingStoreID() {
		return orderingStoreID;
	}

	public void setOrderingStoreID(int orderingStoreID) {
		this.orderingStoreID = orderingStoreID;
	}

	public int getSupplyingStoreID() {
		return supplyingStoreID;
	}

	public void setSupplyingStoreID(int supplyingStoreID) {
		this.supplyingStoreID = supplyingStoreID;
	}

	public long getAlbumID() {
		return albumID;
	}

	public void setAlbumID(long albumID) {
		this.albumID = albumID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
}
