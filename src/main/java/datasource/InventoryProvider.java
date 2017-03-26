package datasource;

import java.util.UUID;

import models.BuyResponse;

/*
 * methods in this class would obviously need some db lock and sync if inventory is limited
 * or we need control
 */
public class InventoryProvider {

	public static final String SUCCESS_RESPONSE_TEMPLATE = "Thank you for purchasing %s";
	public static final String FAIL_RESPONSE_TEMPLATE = "Sorry, we were unable to process your request for %s. Detailed information: %s";

	/**
	 * simple switch statement to simulate sql transaction 
	 * @param itemName
	 * @return
	 */
	private DbResponse dbResponse(String itemName) {
		DbResponse response = new DbResponse();
		
		switch (itemName) {
		case "FirstItem":
			response.success = true;
			response.message = "Success";
			break;
		case "outofstockitem":
			response.success = false;
			response.message = "Out of stock";
			break;
		default:
			response.success = false;
			response.message = "Item unavailable";
			break;
		}
		return response;
	}

	public BuyResponse buy(String itemName) {
		BuyResponse buyResponse = new BuyResponse();
		DbResponse dbResponse = dbResponse(itemName);		
		buyResponse.txnId = dbResponse.txnId;
		
		if (dbResponse.success) {
			buyResponse.success = true;
			buyResponse.message = String.format(SUCCESS_RESPONSE_TEMPLATE, itemName);
		} else {
			buyResponse.success = false;
			buyResponse.message = String.format(FAIL_RESPONSE_TEMPLATE, itemName, dbResponse.message);
		}
		
		return buyResponse;
	}
	
	private class DbResponse{
		public boolean success;
		public UUID txnId;
		public String message;
		
		public DbResponse(){
			this.txnId = UUID.randomUUID();
		}
	}
}
