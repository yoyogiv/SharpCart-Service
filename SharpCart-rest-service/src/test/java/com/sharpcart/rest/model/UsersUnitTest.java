package com.sharpcart.rest.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.sharpcart.rest.dao.DAO;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.ShoppingItem;
import com.sharpcart.rest.persistence.model.Store;
import com.sharpcart.rest.persistence.model.UserShoppingItem;

public class UsersUnitTest {

	Set<Store> stores = new HashSet<Store>();
	Session session;
	SessionFactory factory;
	SharpCartUser user;
	
	public UsersUnitTest()
	{

	}
	
  /*
   * Before we start the test we need to :
   * 1. create a hibernate session
   * 2. pull stores from database
   * 
   */
  @Before
  public void setupUnitUnderTest() {
	  try {
		  
		DAO.getInstance().begin();	
		final Query query = DAO.getInstance().getSession().createQuery("from Store");
		final List<Store> storeList = query.list();
		
		DAO.getInstance().commit();
		
		for (final Store store : storeList)
		{
			if (store!=null)
			{
				stores.add(store);
				System.out.println("Store Name: "+store.getName());
			}
		}
	  } catch (final HibernateException ex)
	  {
		  ex.printStackTrace();
		  DAO.getInstance().rollback();
	  }
	  
	  DAO.getInstance().close();
  }

  /*
   * Test that we can add a user to the database using hibernate
   */
  @Test
  public void addNewUserToDatabase() {
	  
	  Query query;
	  
	  //init user
	  user = new SharpCartUser();
	  user.setUserName("testUser@gmail.com");
	  user.setPassword("123456");
	  user.setZip("78681");
	  user.setFamilySize("3");
	  user.setStores(stores);
	  user.setUserInformationLastUpdate(new Date());
	  
	  //create a demo active sharp list
	  HashSet<UserShoppingItem> demoSharpList = new HashSet<UserShoppingItem>();
	  
	  UserShoppingItem item1 = new UserShoppingItem();
	  UserShoppingItem item2 = new UserShoppingItem();
	  
	  //init item-1
	  item1.setQuantity(1);
	  item1.setUser(user);
	  
	  try{
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from ShoppingItem where id = :shoppingItemId");
		  query.setLong("shoppingItemId", 2);
		  final ShoppingItem item1_ShoppingItem = (ShoppingItem)query.uniqueResult();
		  DAO.getInstance().commit();
		  
		  Assert.notNull(item1_ShoppingItem,"can not add a null item to user active sharp list");
		  
		  item1.setShoppingItem(item1_ShoppingItem);
	
	  } catch (HibernateException ex)
	  {
		  ex.printStackTrace();
	  }
	  
	  //init item-2
	  item2.setQuantity(1);
	  item2.setUser(user);
	  
	  DAO.getInstance().begin();
	  query = DAO.getInstance().getSession().createQuery("from ShoppingItem where id = :shoppingItemId");
	  query.setLong("shoppingItemId", 3);
	  final ShoppingItem item2_ShoppingItem = (ShoppingItem)query.uniqueResult();
	  DAO.getInstance().commit();
	  
	  Assert.notNull(item2_ShoppingItem,"can not add a null item to user active sharp list");
	  
	  item2.setShoppingItem(item2_ShoppingItem);
	  
	  demoSharpList.add(item1);
	  demoSharpList.add(item2);
	  
	  user.setActiveShoppingList(demoSharpList);
	  user.setActiveShoppingListLastUpdate(new Date());
	
	  //Save user to database
	  DAO.getInstance().begin();
	  DAO.getInstance().getSession().save(user);
	  DAO.getInstance().commit();
	  
	  //Validate that user was added
	  DAO.getInstance().begin();
	  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  final SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  DAO.getInstance().commit();
	  
	  assertNotNull(user);
	  
	  DAO.getInstance().close();
  }

  /*
   * Test that we can update a user in the database using hibernate
   */
  @Test
  public void updateUserInDatabase() {
	  
	  Query query;
	  
	  //Remove a store from the stores set
	  Store storeToRemove = null;
	  
	  for (final Store store : stores)
	  {
		  if (store.getName().equalsIgnoreCase("Sams Club"))
		  {
			  storeToRemove = store;
		  }
	  }
	  
	  if (storeToRemove!=null)
		  stores.remove(storeToRemove);
	  
	  //Get user from Database
	  DAO.getInstance().begin();
	  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  DAO.getInstance().commit();
	  
	  //Update user stores
	  user.setStores(stores); 
	  user.setUserInformationLastUpdate(new Date());
	  
	  //create a demo active sharp list
	  Set<UserShoppingItem> demoSharpList = new HashSet<UserShoppingItem>();
	  
	  UserShoppingItem item1 = new UserShoppingItem();
	  
	  //init item-1
	  item1.setQuantity(1);
	  item1.setUser(user);
	  
	  try{
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from ShoppingItem where id = :shoppingItemId");
		  query.setLong("shoppingItemId", 35);
		  final ShoppingItem item1_ShoppingItem = (ShoppingItem)query.uniqueResult();
		  DAO.getInstance().commit();
		  
		  Assert.notNull(item1_ShoppingItem,"can not add a null item to user active sharp list");
		  Assert.notNull(item1_ShoppingItem.getId(),"can not add a shopping item without a valid id");
		  
		  item1.setShoppingItem(item1_ShoppingItem);
	
	  } catch (HibernateException ex)
	  {
		  ex.printStackTrace();
	  }
	  
	  demoSharpList.add(item1);
	  
	  //update user active sharp list  
	  assertFalse(!user.clearSet());
	  user.setActiveShoppingList(demoSharpList);
	  user.setActiveShoppingListLastUpdate(new Date());
	  
	  //Update user in database
	  DAO.getInstance().begin();
	  DAO.getInstance().getSession().update(user);
	  DAO.getInstance().commit();
	  
	  //make sure that our user is null before we get it again from the database
	  user = null;
	  
	  //Validate that user no longer has removed store
	  DAO.getInstance().begin();
	  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  user = (SharpCartUser)query.uniqueResult();
	  DAO.getInstance().commit();
	  
	  stores = user.getStores();
	  assertFalse(stores.contains(storeToRemove));
	  
	  //validate that user now only has one item in their list
	  assertEquals(1,user.getActiveShoppingList().size());
	  assertEquals(35, ((UserShoppingItem)user.getActiveShoppingList().iterator().next()).getShoppingItem().getId().longValue());
	  
	  DAO.getInstance().close();

  }
  
  /*
   * Test that we can remove a user from the database using hibernate
   */
  @Test
  public void removeUserFromDatabase() {
	  //Get user from Database
	  DAO.getInstance().begin();
	  Query query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  DAO.getInstance().commit();
	  
	  //Delte user from database
	  if (user!=null)
	  {
		  DAO.getInstance().begin();
		  DAO.getInstance().getSession().delete(user);
		  DAO.getInstance().commit();
	  }
	  
	  //Check the user has been deleted
	  DAO.getInstance().begin();
	  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  user = (SharpCartUser)query.uniqueResult();
	  DAO.getInstance().commit();
	  
	  //assert that user now equals null
	  assertEquals(null, user);
	  
	  //close session
	  DAO.getInstance().close();

  }
}
