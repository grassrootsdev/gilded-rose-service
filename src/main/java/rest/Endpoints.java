package rest;

import java.util.List;

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

	@Autowired
	InventoryProvider inventoryProvider;
	
    @RequestMapping(value="/items", method=RequestMethod.GET)
    public List<Item> items(){
    	return inventoryProvider.getItems();
    }
    
    @RequestMapping(value="/items/{itemName}", method=RequestMethod.POST)
    public BuyResponse buy(@PathVariable String itemName) throws TransactionException{
    	BuyResponse response = inventoryProvider.buy(itemName);
    	if(response != null){
    		return response;
    	}else{
    		throw new TransactionException("Failed to make purchase");
    	}
    }
	
}
