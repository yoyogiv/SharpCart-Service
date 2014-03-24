package com.sharpcart.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sharpcart.rest.dao.DAO;
import com.sharpcart.rest.model.UserProfile;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.ShoppingItem;
import com.sharpcart.rest.persistence.model.Store;

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
    	
    	List<ShoppingItem> unavilableItems = new ArrayList<ShoppingItem>();
    	SharpCartUser user = null;
    	Query query;
    	
    	//Get list of stores for user
    	try {
  	  	DAO.getInstance().begin();
	  	  	query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  	  	query.setString("userName", userName);
	  	  	user = (SharpCartUser)query.uniqueResult();
	  	  	DAO.getInstance().commit();
    	} catch (HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
    	
  	  	Assert.notNull(user, "user doesnt exist in the database");
  	  	
    	//Get all unavailable items for user stores
    	if (user!=null)
    	{
    		List<ShoppingItem> tempStoreItems;
    		
    		for (Store store : user.getStores())
    		{
    			LOG.info("Store Id: "+store.getId());
    			
    			try {
	    			DAO.getInstance().begin();
	    			query = DAO.getInstance().getSession().createQuery("from StoreItem where storeId = :storeId and price = 0");
	    			query.setLong("storeId", store.getId());
	    			tempStoreItems = (List<ShoppingItem>)query.list();
	    			DAO.getInstance().commit();
	    			
	    			unavilableItems.addAll(tempStoreItems);
    			} catch (HibernateException ex)
    			{
    				DAO.getInstance().rollback();
    				ex.printStackTrace();
    			}
    			
    		}
    		
    		//Remove items that at least one of the user stores has
    		
    	}
    	
    	DAO.getInstance().close();
    	
    	return unavilableItems;
    }
}
