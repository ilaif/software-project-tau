package com.live4music.server.db;

import java.lang.annotation.Retention;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import com.live4music.client.tables.AlbumsResultsTableItem;
import com.live4music.client.tables.SaleTable;
import com.live4music.client.tables.SaleTableItem;
import com.live4music.client.tables.TablesExamples;
import com.live4music.client.ui.DBActionFailureEnum;
import com.live4music.client.ui.GuiUpdatesInterface;
import com.live4music.client.ui.StaticProgramTables;
import com.live4music.shared.general.Debug;
import com.live4music.shared.general.Debug.DebugOutput;


/**
 *	This class contains the Runnable classes for handling the GUI tab "SALE".
 * 	Each class corresponds to a single method in DBConnectionInterface class.
 */
public class DBConnectionSale { 
	
	/**
	 * Corresponds to DBConnectionInterface's "public static void makeSale(SaleTable sale);"
	 */
	public class MakeSale implements Runnable{
		private SaleTable sale;
		
		public MakeSale(SaleTable sale) {
			this.sale = sale;
		}



		
		public void run() {
			Debug.log("DBConnectionSale.MakeSale thread is started",DebugOutput.FILE,DebugOutput.STDOUT);
			
			List<String> queryList = new ArrayList<String>();
			
			// UPDATE TABLE "STOCK"
			for (SaleTableItem saleTableItem : sale.getSaleItems().values()) {
				queryList.add("UPDATE stock SET quantity=quantity-"+saleTableItem.getQuantity()+
			   			   " WHERE (album_id="+saleTableItem.getAlbumID()+") AND (store_id="+StaticProgramTables.getThisStore().getStoreID()+")");

			}
			
			// Delete quantity = 0
			queryList.add("DELETE FROM stock WHERE quantity=0"); 
			
			
			// UPDATE TABLE "SALES"
			String [] timeArr = sale.getTime().split(":");
			String [] dateArr = sale.getDate().split("/");
			String toDateString = "'"+timeArr[0]+":"+timeArr[1]+" "+dateArr[0]+"/"+dateArr[1]+"/"+dateArr[2]+"','%H:%i %d/%m/%Y'";
			String insertQuery = "INSERT INTO sales(store_id, salesman_id, sale_time)" +
								" VALUES("+StaticProgramTables.getThisStore().getStoreID()+","+sale.getSalesman().getEmployeeID()+", STR_TO_DATE("+toDateString+"))";
			queryList.add(insertQuery);
						
			
			// UPDATE TABLE "ALBUM_SALES"
			for (SaleTableItem saleTableItem : sale.getSaleItems().values()) {
				queryList.add( "INSERT INTO album_sales(sale_id, album_id, quantity) " +
						"VALUES((select max(sale_id) from sales), "+saleTableItem.getAlbumID()+", "+saleTableItem.getQuantity()+")");

			}
			
			if (DBAccessLayer.executeCommandsAtomic(queryList) != queryList.size()){
				GuiUpdatesInterface.notifyDBFailure(DBActionFailureEnum.MAKE_SALE_FAILURE);
				Debug.log("DBConnectionSale.MakeSale [ERROR]: Failed to execute changes to DB");
				return;
				
			}
			Debug.log("DBConnectionSale.MakeSale: Done working with DB, calling GUI's initSaleTable");
			GuiUpdatesInterface.initSaleTable();			
		}		
	}

}
