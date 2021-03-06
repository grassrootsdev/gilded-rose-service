package inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import datasource.InventoryDbConnector;
import datasource.InventoryDbConnectorMock.DbResponse;
import exception.TransactionException;
import models.BuyResponse;
import models.Item;

public class InventoryProvider {

	public static final String SUCCESS_RESPONSE_TEMPLATE = "Thank you for purchasing %s";
	public static final String FAIL_RESPONSE_TEMPLATE = "Sorry, we were unable to process your request for %s. Detailed information: %s";

	@Autowired
	private InventoryDbConnector dbConnector;
	
	public void setDbConnector(InventoryDbConnector dbConnector) {
		this.dbConnector = dbConnector;
	}

	public BuyResponse buy(String itemName) throws TransactionException {
		
		DbResponse dbResponse = dbConnector.buyItem(itemName);		
		BuyResponse buyResponse = prepareResponse(itemName, dbResponse);
		
		return buyResponse;
	}

	private BuyResponse prepareResponse(String itemName, DbResponse dbResponse) {
		BuyResponse buyResponse = new BuyResponse();
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
	
	/**
	 * Return List<Item> from downstream source.
	 * NOTE - Depending on biz requirements, could do filtering/manipulation here
	 * @return
	 * @throws TransactionException 
	 */
	public List<Item> getItems() throws TransactionException{
		return dbConnector.getItems();
	}
}
