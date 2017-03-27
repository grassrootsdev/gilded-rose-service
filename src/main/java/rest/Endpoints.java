package rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import exception.TransactionException;
import inventory.InventoryProvider;
import models.BuyResponse;
import models.Item;

@RestController
public class Endpoints {

	private static final String PURCHASE_LINK_TEMPLATE = "http://%s/items/%s";
	@Autowired
	InventoryProvider inventoryProvider;

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public List<Item> items(HttpServletRequest req) throws TransactionException {

		String host = req.getHeader("Host");
		List<Item> items = inventoryProvider.getItems();
		return updatePurchasePath(items, host);
	}

	@RequestMapping(value = "/items/{itemName}", method = RequestMethod.POST)
	public BuyResponse buy(@PathVariable String itemName) throws TransactionException {
		BuyResponse response = inventoryProvider.buy(itemName);
		if (response != null) {
			return response;
		} else {
			throw new TransactionException("Failed to make purchase, please try again later");
		}
	}

	private List<Item> updatePurchasePath(List<Item> items, String host) {
		for (Item item : items) {
			if (item != null) { // since we own db, not testing for null/empty name
				item.purchaseLink = String.format(PURCHASE_LINK_TEMPLATE, host, item.name);
			}
		}
		return items;
	}
	
	protected void setInventoryProvider(InventoryProvider inventoryProvider){
		this.inventoryProvider = inventoryProvider;
	}

}
