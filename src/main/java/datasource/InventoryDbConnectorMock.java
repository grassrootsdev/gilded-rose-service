package datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import models.Item;

/**
 * Mocks connection to the Inventory db. Allows consumer to list Items for sale, then buy if desired.
 * We'd want to ensure atomicity with any call to buy item, but probably easier to do that with 
 * stored proc at db layer.
 * NOTE - I decided to make an interface, in case we want to swap the mock out for real db connector
 */
public class InventoryDbConnectorMock implements InventoryDbConnector{

	/**
	 * simple switch statement to simulate sql transaction. ignores desire for atomicity
	 * this would also include all sql exception processing, and any logging associated with taht
	 * @param itemName
	 * @return
	 */
	public DbResponse buyItem(String itemName) {
		DbResponse response = new DbResponse();
		
		switch (itemName) {
		case "FirstItem":
			response.success = true;
			response.message = "Success";
			break;
		case "SecondItem":
			response.success = false;
			response.message = "Out of stock";
			break;
		case "ThirdItem":
			response.success = true;
			response.message = "Success";
			break;
		default:
			response.success = false;
			response.message = "Item unavailable";
			break;
		}
		return response;
	}
	
	/**
	 * mock db call to select all Items. Assumes any data validation is done at db layer, e.g.,
	 * values are positive, within some expected threshold. If we didn't own the db, validation
	 * should be done at this layer.
	 * @return
	 */
	public List<Item> getItems(){
		List<Item> items = new ArrayList<Item>();
    	items.add(new Item("FirstItem", "The description of my first item", 120));
    	items.add(new Item("SecondItem", "The description of my second item", 400));
    	items.add(new Item("ThirdItem", "The description of my third item", 1));
		
		return items;
	}

	/*
	 * would use slightly different intermediate type if talking to real db
	 * NOTE - depending on complexity of db, might want message as enum
	 */
	public class DbResponse{
		public boolean success;
		public UUID txnId;
		public String message;
		
		public DbResponse(){
			this.txnId = UUID.randomUUID();
		}
	}
}
