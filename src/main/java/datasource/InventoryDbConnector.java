package datasource;

import java.util.List;

import datasource.InventoryDbConnectorMock.DbResponse;
import exception.TransactionException;
import models.Item;

/**
 * Interface for connecting to downstream Inventory datasource
 * @author cgrass
 *
 */
public interface InventoryDbConnector {

	public DbResponse buyItem(String itemName) throws TransactionException;
	public List<Item> getItems() throws TransactionException;
}
