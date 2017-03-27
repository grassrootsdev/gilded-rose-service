package inventory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import datasource.InventoryDbConnector;
import datasource.InventoryDbConnectorMock.DbResponse;
import models.BuyResponse;

public class InventoryProviderTest {
	private InventoryProvider provider;
	private InventoryDbConnector dbConnector;
	
	private final static String VALID_ITEM_1 = "FirstItem";
	private final static String INVALID_ITEM = "invalid";

	@Before
	public void setup() {
		provider = new InventoryProvider();
		dbConnector = EasyMock.createMock(InventoryDbConnector.class);
		provider.setDbConnector(dbConnector);
	}

	@Test
	public void testSuccessBuy() {
		UUID txnId = UUID.randomUUID();
		DbResponse response = EasyMock.createMock(DbResponse.class);
		response.success = true;
		response.txnId = txnId;
		EasyMock.expect(dbConnector.buyItem(VALID_ITEM_1)).andReturn(response).once();
		EasyMock.replay(dbConnector, response);

		BuyResponse buyResponse = provider.buy(VALID_ITEM_1);
		assertTrue(buyResponse.txnId.equals(txnId));
		assertTrue(buyResponse.success);
	}
	
	@Test
	public void testFailBuy() {
		UUID txnId = UUID.randomUUID();
		DbResponse response = EasyMock.createMock(DbResponse.class);
		response.success = false;
		response.txnId = txnId;
		EasyMock.expect(dbConnector.buyItem(INVALID_ITEM)).andReturn(response).once();
		EasyMock.replay(dbConnector, response);

		BuyResponse buyResponse = provider.buy(INVALID_ITEM);
		assertTrue(buyResponse.txnId.equals(txnId));
		assertFalse(buyResponse.success);
	}
	
	@Ignore
	@Test
	public void testGetItems(){
		//not necessary since currently no data manipulation takes place
	}
}
