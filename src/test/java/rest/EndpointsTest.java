package rest;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import exception.TransactionException;
import inventory.InventoryProvider;
import models.BuyResponse;
import models.Item;

public class EndpointsTest {

	private static String HOST = "localhost:8090";
	private static final String PURCHASE_LINK_TEMPLATE = "http://%s/items/%s";

	private Endpoints endpoints = new Endpoints();
	private InventoryProvider inventoryProvider;
	private List<Item> downstreamItems = new ArrayList<Item>();
	private HttpServletRequest req = EasyMock.createMock(HttpServletRequest.class);

	@Before
	public void setup() {
		inventoryProvider = EasyMock.createMock(InventoryProvider.class);
		endpoints.setInventoryProvider(inventoryProvider);
		downstreamItems.add(new Item("FirstItem", "Desc1", 1));
		downstreamItems.add(new Item("SecondItem", "Desc2", 2));
		downstreamItems.add(new Item("ThirdItem", "Desc3", 3));
	}

	@Test
	public void testGetItems() throws TransactionException{
		EasyMock.expect(inventoryProvider.getItems()).andReturn(downstreamItems).once();
		EasyMock.expect(req.getHeader("Host")).andReturn(HOST);
		EasyMock.replay(inventoryProvider, req);
		List<Item> responseItems = endpoints.items(req);
		for (Item responseItem : responseItems) {
			Item downstreamItem = getMatchingItem(responseItem);
			assertTrue(validateItem(responseItem, downstreamItem));
		}
	}
	
	@Test
	public void testBuy() throws TransactionException{
		String itemName = "FirstItem";
		
		BuyResponse buyResponse = EasyMock.createMock(BuyResponse.class);
		buyResponse.success = true;
		buyResponse.txnId = UUID.randomUUID();
		
		EasyMock.expect(inventoryProvider.buy(itemName)).andReturn(buyResponse).once();
		
		EasyMock.replay(inventoryProvider, buyResponse);
		BuyResponse apiResponse = endpoints.buy(itemName);
		assertTrue(apiResponse.success == buyResponse.success);
		assertTrue(apiResponse.txnId.equals(buyResponse.txnId));
	}
	
	@Test
	public void testFailedBuy() throws TransactionException{
		String itemName = "FirstItem";
		
		BuyResponse buyResponse = EasyMock.createMock(BuyResponse.class);
		buyResponse.success = false;
		buyResponse.txnId = UUID.randomUUID();
		
		EasyMock.expect(inventoryProvider.buy(itemName)).andReturn(buyResponse).once();
		
		EasyMock.replay(inventoryProvider, buyResponse);

		BuyResponse apiResponse = endpoints.buy(itemName);
		assertTrue(apiResponse.success == buyResponse.success);
		assertTrue(apiResponse.txnId.equals(buyResponse.txnId));
	}
	
	@Test(expected = TransactionException.class)
	public void testExceptionBuy() throws TransactionException{
		String itemName = "FirstItem";
		
		EasyMock.expect(inventoryProvider.buy(itemName)).andThrow(new TransactionException("Failure")).once();
		
		EasyMock.replay(inventoryProvider);

		endpoints.buy(itemName);
		throw new RuntimeException("Should not reach here");
	}
	
	@Test(expected = TransactionException.class)
	public void testNullResponseBuy() throws TransactionException{
		String itemName = "FirstItem";
		
		EasyMock.expect(inventoryProvider.buy(itemName)).andReturn(null).once();
		
		EasyMock.replay(inventoryProvider);

		endpoints.buy(itemName);
		throw new RuntimeException("Should not reach here");
	}

	// ensure no unexpected data manipulation
	private boolean validateItem(Item responseItem, Item downstreamItem) {
		if (responseItem.name.equals(downstreamItem.name)) {
			if (responseItem.description.equals(downstreamItem.description)) {
				if (responseItem.price == downstreamItem.price) {
					String finalPurchaseLink = responseItem.purchaseLink;
					if (finalPurchaseLink.equals(String.format(PURCHASE_LINK_TEMPLATE, HOST, downstreamItem.name))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//could also implement via map, but this was simple
	private Item getMatchingItem(Item actualItem) {
		Item dsItem = null;
		for (Item item : downstreamItems) {
			if (item.name.equals(actualItem.name)) {
				dsItem = item;
			}
		}
		return dsItem;
	}
}
