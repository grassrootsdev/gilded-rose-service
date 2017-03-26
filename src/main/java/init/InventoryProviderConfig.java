package init;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import datasource.InventoryProvider;

@Configuration
public class InventoryProviderConfig {
	
	@Bean
	InventoryProvider inventoryProvider(){
		return new InventoryProvider();
	}

}
