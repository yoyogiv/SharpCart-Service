package com.sharpcart.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sharpcart.rest.dao.DAO;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.ShoppingItem;
import com.sharpcart.rest.persistence.model.Store;
import com.sharpcart.rest.persistence.model.StoreItem;

@Controller
public class GroceryItemsController {
	private static Logger LOG = LoggerFactory.getLogger(GroceryItemsController.class);
	
	public GroceryItemsController()
	{
		
	}
	
	/*
	 * Retrieve unavailable grocery itesm
	 */
    @RequestMapping(value="/aggregators/groceryItems/unavailable",method = RequestMethod.POST)
    @ResponseBody
    public List<ShoppingItem> getUnavailableGroceryItems(@RequestParam(value="userName", required=true) String userName) {
    	
    	final ArrayList<ShoppingItem> unavilableItems = new ArrayList<ShoppingItem>();
    	final ArrayList<ShoppingItem> unavilableItemsCleaned = new ArrayList<ShoppingItem>();
    	
    	SharpCartUser user = null;
    	Query query;
    	
    	//Get user from database
    	try {
  	  	DAO.getInstance().begin();
	  	  	query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  	  	query.setString("userName", userName);
	  	  	user = (SharpCartUser)query.uniqueResult();
	  	  	DAO.getInstance().commit();
    	} catch (final HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
    	
  	  	Assert.notNull(user, "user doesnt exist in the database");
  	  	
    	//Get all unavailable items for user stores
    	if (user!=null)
    	{
    		List<StoreItem> tempStoreItems;
    		final int numberOfStores = user.getStores().size();
    		
    		for (final Store store : user.getStores())
    		{ 			
    			try {
	    			DAO.getInstance().begin();
	    			query = DAO.getInstance().getSession().createQuery("from StoreItem where storeId = :storeId and price = 0");
	    			query.setLong("storeId", store.getId());
	    			tempStoreItems = query.list();
	    			DAO.getInstance().commit();
	    					
	    			for (final StoreItem item : tempStoreItems)
	    			{
	    				unavilableItems.add(item.getShoppingItem());
	    			}   			
    			} catch (final HibernateException ex)
    			{
    				DAO.getInstance().rollback();
    				ex.printStackTrace();
    			}
    		}
    		
    		//Remove any item that doesnt show up numberOfStores times in the list
    		Collections.sort(unavilableItems);
    		
    		int index = 0;
    		for (int i=0;i<unavilableItems.size()-1;i++)
    		{
    			if(unavilableItems.get(i).getId() == unavilableItems.get(i+1).getId())
    				index++;
    			else
    				index=0;
    			
    			if (index==numberOfStores-1)
    				unavilableItemsCleaned.add(unavilableItems.get(i));
    		}
    	}
    	
    	DAO.getInstance().close();
    	
    	return unavilableItemsCleaned;
    }
}
