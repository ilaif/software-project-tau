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
	
	private static int START_INDEX = 1001;
	private static int N = 147000;
	private static int LOOP = 1000;
	private static int quantity = 100;
	private static final String STOCK_FILLER = "d:\\stores_stock_filler_transaction";
	private static BufferedWriter outputFile = null;
	
	public static void writeFillingTrans() {
		Debug.log("*** writing sql filler ***", DebugOutput.STDOUT);
		
		for(int j = START_INDEX; j < N+START_INDEX; j+= LOOP) {
			String trans = "";
			
			// values for each store
			int lower = j;
			int upper = j+LOOP;
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
//				lower += N/5;
//				upper += N/5;
//				
//				trans += "\n";
			}
			
			filler(trans, j);
		}
	}
	
	private static void filler(String message, int i) {
		try {
			
			outputFile = new BufferedWriter(new FileWriter(STOCK_FILLER+i+".sql"));
			outputFile.write(message);
			outputFile.flush();
			outputFile.close();
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
