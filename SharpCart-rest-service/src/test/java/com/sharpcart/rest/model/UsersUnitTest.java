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
import com.sharpcart.rest.persistence.model.UsZipCode;
import com.sharpcart.rest.persistence.model.UserExtraShoppingItem;
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
			}
		}
	  } catch (final HibernateException ex)
	  {
		  ex.printStackTrace();
		  DAO.getInstance().rollback();
	  }
	  
	  assertEquals(5,stores.size());
	  
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
	  user.setFamilySize("3");
	  user.addStores(stores);
	  user.setUserInformationLastUpdate(new Date());
	  
	  //setup zip code
  	  try {
	  		DAO.getInstance().begin();
	  		query = DAO.getInstance().getSession().createQuery("from UsZIPCode where zip = :userZipCode");
	  		query.setInteger("userZipCode", 78681);
	  		UsZipCode userZIPCode = (UsZipCode) query.uniqueResult();
	  		user.setZip(userZIPCode);
	  	  } catch (HibernateException ex)
	  	  {
	  		  DAO.getInstance().rollback();
	  		  ex.printStackTrace();
	  	  }
  	  
	  assertEquals(5,user.getStores().size());
	  
	  //create some regular user shopping items
	  UserShoppingItem item1 = new UserShoppingItem();
	  UserShoppingItem item2 = new UserShoppingItem();
	  
	  //init item-1
	  item1.setQuantity(1);
	  
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
	  
	  try {
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from ShoppingItem where id = :shoppingItemId");
		  query.setLong("shoppingItemId", 3);
		  final ShoppingItem item2_ShoppingItem = (ShoppingItem)query.uniqueResult();
		  DAO.getInstance().commit();
		  
		  Assert.notNull(item2_ShoppingItem,"can not add a null item to user active sharp list");
		  
		  item2.setShoppingItem(item2_ShoppingItem);
	  } catch (HibernateException ex)
	  {
		ex.printStackTrace();  
	  }
	  
	  DAO.getInstance().close();
	  
	  user.addRegularShoppingItem(item1);
	  user.addRegularShoppingItem(item2);

	  assertEquals(2, user.getRegularShoppingItems().size());
	  
	  //add an extra item 
	  UserExtraShoppingItem userExtraShoppingItem = new UserExtraShoppingItem();
	  
	  userExtraShoppingItem.setId(500L);
	  userExtraShoppingItem.setName("Love");
	  userExtraShoppingItem.setCategoryId(23);
	  userExtraShoppingItem.setCategory("extra");
	  userExtraShoppingItem.setImageLocation("/ShoppingItems/Images/Extra.jpg");
	  userExtraShoppingItem.setQuantity(1);
	  
	  user.addExtraShoppingItem(userExtraShoppingItem);
	  
	  assertEquals(1, user.getExtraShoppingItems().size());
	  
	  //update user active shopping list last update time stamp
	  user.setActiveShoppingListLastUpdate(new Date());
	  
	  //Save user to database
	  try{
		  DAO.getInstance().begin();
		  DAO.getInstance().getSession().save(user);
		  DAO.getInstance().commit();
	  } catch (HibernateException ex)
	  {
		  DAO.getInstance().rollback();
		  ex.printStackTrace();
	  }
	  
	  //Validate that user was added
	  DAO.getInstance().begin();
	  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  SharpCartUser newUser = (SharpCartUser)query.uniqueResult();
	  DAO.getInstance().getSession().flush();
	  DAO.getInstance().commit();
	  
	  assertNotNull(newUser);
	  assertEquals(1,newUser.getExtraShoppingItems().size());
	  assertEquals(2,newUser.getRegularShoppingItems().size());
	  assertEquals(5,newUser.getStores().size());
	  
	  DAO.getInstance().close();  
  }

  /*
   * Test that we can update a user in the database using hibernate
   */
  @Test
  public void updateUserInDatabase() {
	  
	  Query query;
	  SharpCartUser user = null;
	  
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
	  try {
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  query.setString("userName", "testUser@gmail.com");
		  user = (SharpCartUser)query.uniqueResult();
		  DAO.getInstance().commit();
	  } catch (HibernateException ex)
	  {
		  DAO.getInstance().rollback();
		  ex.printStackTrace();
	  }
	  
	  assertNotNull("you cannot update a null user", user);
	  
	  //Update user stores
	  user.getStores().clear();
	  user.addStores(stores); 
	  user.setUserInformationLastUpdate(new Date());
	  
	  //create a demo active sharp list
	  Set<UserShoppingItem> demoSharpList = new HashSet<UserShoppingItem>();
	  
	  UserShoppingItem item1 = new UserShoppingItem();
	  
	  //init item-1
	  item1.setQuantity(1);
	  
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
		  DAO.getInstance().rollback();
		  ex.printStackTrace();
	  }
	 
	  user.getRegularShoppingItems().clear();
	  
	  user.addRegularShoppingItem(item1);
	  
	  user.setActiveShoppingListLastUpdate(new Date());
	  
	  //Update user in database
	  try{
		  DAO.getInstance().begin();
		  DAO.getInstance().getSession().update(user);
		  DAO.getInstance().commit();
	  } catch (HibernateException ex)
	  {
		  DAO.getInstance().rollback();
		  ex.printStackTrace();
	  }
	  
	  //Validate that user no longer has removed store
	  try{
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  query.setString("userName", "testUser@gmail.com");
		  user = (SharpCartUser)query.uniqueResult();
		  DAO.getInstance().commit();
	  } catch (HibernateException ex)
	  {
		DAO.getInstance().rollback();
		ex.printStackTrace();
	  }
	  
	  stores = user.getStores();
	  assertFalse(stores.contains(storeToRemove));
	  
	  //validate that user now only has one item in their list and that the item id is 35
	  assertEquals(1,user.getRegularShoppingItems().size());
	  assertEquals(35, ((UserShoppingItem)user.getRegularShoppingItems().iterator().next()).getShoppingItem().getId().longValue());
	  
	  //validate that user has only 4 stores
	  assertEquals(4,user.getStores().size());
	  
	  DAO.getInstance().close();

  }
  
  /*
   * Test that we can remove a user from the database using hibernate
   */
  @Test
  public void removeUserFromDatabase() {
	  Query query;
	  SharpCartUser user=null;
	  
	  //Get user from Database
	  try {
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  query.setString("userName", "testUser@gmail.com");
		  user = (SharpCartUser)query.uniqueResult();
		  DAO.getInstance().commit();
	  } catch (HibernateException ex)
	  {
		  DAO.getInstance().rollback();
		  ex.printStackTrace();
	  }
	  
	  //Delete user from database
	  if (user!=null)
	  {
		  DAO.getInstance().begin();
		  DAO.getInstance().getSession().delete(user);
		  DAO.getInstance().commit();
		  DAO.getInstance().close();
		  
		  user=null;
	  }
	  
	  //Check the user has been deleted
	  try {
		  DAO.getInstance().begin();
		  query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  query.setString("userName", "testUser@gmail.com");
		  user = (SharpCartUser)query.uniqueResult();
		  DAO.getInstance().commit();
	  } catch (HibernateException ex)
	  {
		  DAO.getInstance().rollback();
		  ex.printStackTrace();
	  }
	  
	  //assert that user now equals null
	  assertEquals(null, user);
	  
	  //close session
	  DAO.getInstance().close();
  }
}
