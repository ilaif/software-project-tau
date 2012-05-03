package com.live4music.server.queries;

import com.live4music.client.ui.Main;

/**
 * created by Ariel
 * 
 * Order available stores query data structure
 * ===========================================
 * holds order available stores query information:
 * - album ID
 */
public class OrderAvailableStoresQuery extends Query {
	private long albumID;
	
	public OrderAvailableStoresQuery() throws QueryErrorException{
		super("OrderAvailableStoresQuery");
		
		// set the album id field
		QueryErrorException qee = new QueryErrorException("Album ID must be an integer greater than 0",this.getQueryType());
		String albumIDText = Main.getStockLabelAlbumIDInput().getText();
		
		if (albumIDText == null || albumIDText.length() == 0) throw qee; // the album id text is null or empty
		try{
			long albumID = Long.parseLong(albumIDText);
			if (albumID <= 0) throw qee; // the album id text is an integer <= 0
			else this.albumID = albumID; // SET ALBUM ID
		}catch(NumberFormatException nfe){throw qee;} // the album id text is not an integer
	}

	public long getAlbumID() {
		return albumID;
	}
}
