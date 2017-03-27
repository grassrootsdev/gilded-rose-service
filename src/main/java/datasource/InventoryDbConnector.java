package datasource;

import java.util.List;

import datasource.InventoryDbConnectorMock.DbResponse;
import models.Item;

public interface InventoryDbConnector {

	public DbResponse buyItem(String itemName);
	public List<Item> getItems();
}
