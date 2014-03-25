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
	    			StoreItem storeItem;
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
	    	    	
	    	    	//Now that we have the store item we can generate our ShoppingListItem from it
	    	    	ShoppingListItem shoppingListItem = new ShoppingListItem();
	    	    	
	    		}
	    		
	    		//calculate total cost
	    		
	    		//add to optimized list
	    		optimizedSharpList.add(storePrices);
	    	}
    	}
    	
    	DAO.getInstance().close();
    	
    	return optimizedSharpList;
    }

}
