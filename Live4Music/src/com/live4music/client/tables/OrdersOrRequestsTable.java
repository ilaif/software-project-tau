package com.live4music.client.tables;

import java.util.*;

/**
 * 
 * orders / requests table data structure
 * 
 * holds order items for orders or requests table type
 */
public class OrdersOrRequestsTable extends Table {
	
	private Map<Integer,OrdersOrRequestsTableItem> orders = new HashMap<Integer,OrdersOrRequestsTableItem>();
	private boolean isOrder; // holds order / request

	/**
	 * constructor for orders / requests table
	 * isOrder holds true for orders table and false for requests table
	 */
	public OrdersOrRequestsTable(boolean isOrder){
		super("OrdersOrRequestsTable");
		this.isOrder = isOrder;
	}
	
	/**
	 * constructor for orders / requests table with orders
	 */
	public OrdersOrRequestsTable(boolean isOrder, HashMap<Integer,OrdersOrRequestsTableItem> orders){
		super("OrdersOrRequestsTable");
		this.orders = orders;
	}
	
	////////////////////////////////
	// adders setters and getters //
	////////////////////////////////
	
	/**
	 * add order / request to table
	 * @param order
	 */
	public void addOrder(OrdersOrRequestsTableItem order){
		this.orders.put(order.getOrderID(),order);
	}
	
	/**
	 * add explicit order / request to table
	 * @param orderID
	 * @param orderingStoreID
	 * @param supplyingStoreID
	 * @param albumID
	 * @param quantity
	 * @param date
	 * @param status
	 */
	public void addOrder(int orderID, int orderingStoreID, int supplyingStoreID,
			long albumID, int quantity, String date, OrderStatusEnum status){
		OrdersOrRequestsTableItem order = new OrdersOrRequestsTableItem(orderID, orderingStoreID,
				supplyingStoreID, albumID, quantity, date, status);
		this.orders.put(orderID, order);
	}
	
	/**
	 * get order / request with given order id
	 * @param orderID
	 * @return
	 */
	public OrdersOrRequestsTableItem getOrder(int orderID){
		return this.orders.get(orderID);
	}

	public Map<Integer,OrdersOrRequestsTableItem> getOrders() {
		return orders;
	}

	public void setOrders(Map<Integer,OrdersOrRequestsTableItem> orders) {
		this.orders = orders;
	}

	public boolean isOrder() {
		return isOrder;
	}
}
