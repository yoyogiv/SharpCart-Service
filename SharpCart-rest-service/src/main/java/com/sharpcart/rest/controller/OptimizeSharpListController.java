package com.sharpcart.rest.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sharpcart.rest.dao.DAO;
import com.sharpcart.rest.model.SharpList;
import com.sharpcart.rest.model.ShoppingListItem;
import com.sharpcart.rest.model.StorePrices;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.Store;
import com.sharpcart.rest.persistence.model.StoreItem;

@Controller
@RequestMapping("/aggregators/optimize")
public class OptimizeSharpListController {

    private static Logger LOG = LoggerFactory.getLogger(OptimizeSharpListController.class);

    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<StorePrices> optimizeSharpList(@RequestBody final SharpList sharpList) {
    	List<StorePrices> optimizedSharpList = new ArrayList<StorePrices>();
    	Query query;
    	SharpCartUser user = null;
    	Set<Store> stores = new HashSet<Store>();
    	
    	//Get list of stores for user
    	try {
  	  		DAO.getInstance().begin();
	  	  	query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  	  	query.setString("userName", sharpList.getUserName());
	  	  	user = (SharpCartUser)query.uniqueResult();
	  	  	DAO.getInstance().commit();
    	} catch (final HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
    	
    	if (user!=null)
    	{
    		stores = user.getStores();
    		
	    	//for each store get the prices and total cost for the user sharp list items
	    	for (Store store : stores)
	    	{
	    		StorePrices storePrices = new StorePrices();
	    		
	    		storePrices.setId(store.getId());
	    		storePrices.setName(store.getName());
	    		storePrices.setStore_image_location(store.getImageLocation());
	    		
	    		//get prices for items
	    		List<ShoppingListItem> groceryItems = new ArrayList<ShoppingListItem>();
	    		
	    		for (ShoppingListItem item : sharpList.getMainSharpList())
	    		{
	    			StoreItem storeItem = null;
	    	    	try {
		    	  	  	DAO.getInstance().begin();
		    		  	query = DAO.getInstance().getSession().createQuery("from StoreItem where id = :storeId and shoppingItemId = :shoppingItemId");
		    		  	query.setLong("storeId", store.getId());
		    		  	query.setLong("shoppingItemId",item.getId());
		    		  	storeItem = (StoreItem)query.uniqueResult();
		    		  	DAO.getInstance().commit();
	    	    	} catch (final HibernateException ex)
	    	    	{
	    	    		DAO.getInstance().rollback();
	    	    		ex.printStackTrace();
	    	    	}
	    	    	
	    	    	if (storeItem!=null)
	    	    	{
		    	    	//Now that we have the store item we can generate our ShoppingListItem from it
		    	    	ShoppingListItem shoppingListItem = new ShoppingListItem();
		    	    	/*
		    	    	 * Id
		    	    	 * Name
		    	    	 * Description
		    	    	 * Unit name
		    	    	 * Unit id
		    	    	 * Category name
		    	    	 * Category id
		    	    	 * Conversion Ratio
		    	    	 * Image Location
		    	    	 * Quantity
		    	    	 * 
		    	    	 * Price
		    	    	 * Package quantity
		    	    	 * Price per unit
		    	    	 * Total price
		    	    	 * 
		    	    	 */
		    	    	shoppingListItem.setId(storeItem.getShoppingItem().getId());
		    	    	shoppingListItem.setName(storeItem.getShoppingItem().getName());
		    	    	shoppingListItem.setDescription(storeItem.getShoppingItem().getDescription());
		    	    	shoppingListItem.setUnit(storeItem.getShoppingItem().getUnit().getName());
		    	    	shoppingListItem.setShopping_item_unit_id(storeItem.getShoppingItem().getUnit().getId());
		    	    	shoppingListItem.setCategory(storeItem.getShoppingItem().getCategory().getName());
		    	    	shoppingListItem.setShopping_item_category_id(storeItem.getShoppingItem().getCategory().getId());
		    	    	shoppingListItem.setConversion_ratio(storeItem.getShoppingItem().getUnitToItemConversionRatio());
		    	    	shoppingListItem.setImage_location(storeItem.getShoppingItem().getImageLocation());
		    	    	
		    	    	//quantity 
		    	    	shoppingListItem.setQuantity(item.getQuantity());
		    	    	
		    	    	//price related information
		    	    	shoppingListItem.setPrice(storeItem.getPrice());
		    	    	shoppingListItem.setPackage_quantity(storeItem.getQuantity());
		    	    	if (storeItem.getPrice()>0)
		    	    		shoppingListItem.setPrice_per_unit(storeItem.getPrice()/storeItem.getQuantity());
		    	    	shoppingListItem.setTotal_price(storeItem.getPrice()*item.getQuantity());
		    	    	
		    	    	//add item to store prices grocery items list
		    	    	groceryItems.add(shoppingListItem);
		    	    	
	    	    	}
	    	    	
	    	    	//add list of grocery items to store prices object
	    	    	storePrices.setItems(groceryItems);
	    	    	
	    	    	//calculate total cost
	    	    	float totalCost = 0;
	    	    	for (ShoppingListItem groceryItem : groceryItems)
	    	    	{
	    	    		totalCost+=groceryItem.getTotal_price();
	    	    	}
	    	    	
	    	    	storePrices.setTotal_cost(totalCost);
	    		}
	    		
	    		
	    		
	    		//add to optimized list
	    		optimizedSharpList.add(storePrices);
	    	}
    	}
    	
    	DAO.getInstance().close();
    	
    	return optimizedSharpList;
    }

}
