package com.sharpcart.rest.model;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.Store;

import java.nio.channels.SeekableByteChannel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

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
		Configuration configuration = new AnnotationConfiguration ().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
		applySettings(configuration.getProperties());
		factory = configuration.buildSessionFactory(builder.build());
		
		session = factory.openSession();
		
		//grab stores from database
		session.beginTransaction();
		
		Query query = session.createQuery("from Store");
		List<Store> storeList = query.list();
		
		session.getTransaction().commit();
		session.close();
		
		for (Store store : storeList)
		{
			if (store!=null)
			{
				stores.add(store);
				System.out.println("Store Name: "+store.getName());
			}
		}
  }

  /*
   * Test that we can add a user to the database using hibernate
   */
  @Test
  public void addNewUserToDatabase() {
	  
		  //init user
		user = new SharpCartUser();
		user.setUserName("testUser@gmail.com");
		user.setPassword("123456");
		user.setZip("78681");
		user.setFamilySize("3");
		user.setStores(stores);
	
	  //Save user to database
	  session = factory.openSession();
	  session.beginTransaction();
	  session.save(user);
	  session.getTransaction().commit();
	  
	  //Validate that user was added
	  session.beginTransaction();
	  Query query = session.createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  session.getTransaction().commit();
	  
	  assertNotNull(user);
	  
	  session.close();
	  
	  
  }

  /*
   * Test that we can update a user in the database using hibernate
   */
  @Test
  public void updateUserInDatabase() {
	  
	  //Remove a store from the stores set
	  Store storeToRemove = null;
	  
	  for (Store store : stores)
	  {
		  if (store.getName().equalsIgnoreCase("Sams Club"))
		  {
			  storeToRemove = store;
		  }
	  }
	  
	  if (storeToRemove!=null)
		  stores.remove(storeToRemove);
	  
	  //Get user from Database
	  session = factory.openSession();
	  session.beginTransaction();
	  Query query = session.createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  
	  //Update user stores
	  user.setStores(stores);
	  
	  //Update user in database
	  session.update(user);
	  session.getTransaction().commit();
	  
	  //Validate that user no longer has removed store
	  session.beginTransaction();
	  query = session.createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  user = (SharpCartUser)query.uniqueResult();
	  session.getTransaction().commit();
	  
	  stores = user.getStores();
	  assertFalse(stores.contains(storeToRemove));
	  
	  session.close();

  }
  
  /*
   * Test that we can remove a user from the database using hibernate
   */
  @Test
  public void removeUserFromDatabase() {
	  //Get user from Database
	  session = factory.openSession();
	  
	  session.beginTransaction();
	  Query query = session.createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  session.getTransaction().commit();
	  
	  //Delte user from database
	  if (user!=null)
	  {
		  session.beginTransaction();
		  session.delete(user);
		  session.getTransaction().commit();
	  }
	  
	  //Check the user has been deleted
	  session.beginTransaction();
	  query = session.createQuery("from SharpCartUser where userName = :userName");
	  query.setString("userName", "testUser@gmail.com");
	  user = (SharpCartUser)query.uniqueResult();
	  session.getTransaction().commit();
	  
	  //assert that user now equals null
	  assertEquals(null, user);
	  
	  //close session
	  session.close();

  }
}
