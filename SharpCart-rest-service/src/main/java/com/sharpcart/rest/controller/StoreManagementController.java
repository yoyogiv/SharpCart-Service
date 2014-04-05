package com.sharpcart.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class StoreManagementController {
	private static Logger LOG = LoggerFactory.getLogger(StoreManagementController.class);
	
	public StoreManagementController()
	{
		
	}
	
	/*
	 * Retrieve available stores for a specific ZIP code 
	 */
    @RequestMapping(value="/aggregators/store/servingZIPCode",method = RequestMethod.GET)
    @ResponseBody
    public List<Store> getServicingZipCode(@RequestParam(value="zipCode", required=true) String zipCode) {
    	List<Store> servingStores = new ArrayList<Store>();
    	
    	try{
    		DAO.getInstance().begin();
    		Query query = DAO.getInstance().getSession().createQuery("from Store store where :zipCode in elements(store.servicingZipCodes)");
    		query.setInteger("zipCode",Integer.valueOf(zipCode));
    		servingStores = query.list();
    		DAO.getInstance().commit();
    	} catch (HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
    	
		DAO.getInstance().close();
				
    	return servingStores;
    }	
}
