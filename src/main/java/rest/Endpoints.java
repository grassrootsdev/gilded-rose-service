package rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import datasource.InventoryProvider;
import models.BuyResponse;
import models.Item;

@RestController
public class Endpoints {

	@Autowired
	InventoryProvider inventoryProvider;
	
    @RequestMapping(value="/items", method=RequestMethod.GET)
    public List<Item> items(){
    	List<Item> items = new ArrayList<Item>();
    	Item item = new Item("FirstItem", "The description of my first item", 120);
    	items.add(item);
    	
    	return items;
    }
    
    @RequestMapping(value="/items/{itemName}", method=RequestMethod.POST)
    public BuyResponse buy(@PathVariable String itemName){
    	return inventoryProvider.buy(itemName);
    }    
	
}
