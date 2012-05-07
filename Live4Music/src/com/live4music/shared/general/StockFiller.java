package com.live4music.shared.general;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.live4music.client.ui.StaticProgramTables;
import com.live4music.shared.general.Debug.DebugOutput;


/**
 * created for creating sql transaction to update stores stocks
 */
public class StockFiller {
	
	private static int START_INDEX = 16000;
	private static int N = 1000;
	private static int quantity = 100;
	private static final String STOCK_FILLER = "stores_stock_filler_transaction.sql";
	private static BufferedWriter outputFile = null;
	
	public static void writeFillingTrans() {
		Debug.log("*** writing sql filler ***", DebugOutput.STDOUT);
		
		String trans = "";
		
		// values for each store
		int lower = START_INDEX;
		int upper = N+START_INDEX;
		for (int store_id: StaticProgramTables.stores.getStores().keySet()){
			// initiate command
			trans +=	"INSERT INTO stock(album_id, store_id, quantity)\n"+
						"SELECT album_id,"+store_id+","+quantity+"\n"+
						"FROM albums\n"+
						"WHERE ((album_id >= "+lower+") and (album_id <= "+upper+"));\n\n";
			
			// insert storage location
			for (int i = lower; i <= upper; i++){
				trans +=	"UPDATE stock\n"+
							"SET storage_location="+getRandLocation()+"\n"+
							"WHERE ((album_id="+i+") and (store_id="+store_id+"));\n";
			}
			
			// update bounds
			lower += N/5;
			upper += N/5;
			
			trans += "\n";
		}
		
		filler(trans);
	}
	
	private static void filler(String message) {
		try {
			if (outputFile == null) 
			{
				outputFile = new BufferedWriter(new FileWriter(STOCK_FILLER));
			}		
			outputFile.write(message);
			outputFile.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	private static long getRandLocation() {
		long precision = 1000000000 * 100; 
		long ret = ((new Random()).nextLong() % precision);
		while (ret <= 0)
			ret = ((new Random()).nextLong() % precision);
		return ret;
	}
}
