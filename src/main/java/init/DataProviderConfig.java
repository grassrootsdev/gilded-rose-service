package init;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import datasource.InventoryDbConnectorMock;
import inventory.InventoryProvider;

@Configuration
public class DataProviderConfig {
	
	@Bean
	InventoryProvider inventoryProvider(){
		return new InventoryProvider();
	}
	
	@Bean
	InventoryDbConnectorMock inventoryDbConnectorMock(){
		return new InventoryDbConnectorMock();
	}

}
