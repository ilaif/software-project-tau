package com.live4music.client.tables;

/**
 * 
 * Sale table item data structure for the sale table
 * 
 * holds sale information:
 * - album id
 * - album name
 * - quantity
 * - price per item
 */
public class SaleTableItem {
	
	private long albumID;
	private String albumName;
	private int quantity;
	private int pricePerItem;
	
	/**
	 * constructor for sale item
	 * @param albumID
	 * @param albumName
	 * @param quantity
	 * @param price
	 */
	public SaleTableItem(long albumID, String albumName, int quantity, int price){
		this.albumID = albumID;
		this.albumName = albumName;
		this.quantity = quantity;
		this.pricePerItem = price;
	}

	public long getAlbumID() {
		return albumID;
	}

	public String getAlbumName() {
		return albumName;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getPricePerItem() {
		return pricePerItem;
	}

	/**
	 * returns the total price for the album (quantity * album price)
	 * @return
	 */
	public int getTotalPerItem() {
		return pricePerItem*quantity;
	}
}
