package com.sharpcart.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.sharpcart.rest.model.UserProfile;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.Store;
import com.sharpcart.rest.utilities.PasswordHash;
import com.sharpcart.rest.utilities.SharpCartConstants;

@Controller
//@RequestMapping("/aggregators/user")
public class UserManagementController {
    private static Logger LOG = LoggerFactory.getLogger(UserManagementController.class);
	private Session session;
	private SessionFactory factory;
	
	public UserManagementController()
	{
		Configuration configuration = new AnnotationConfiguration ().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
		applySettings(configuration.getProperties());
		factory = configuration.buildSessionFactory(builder.build());
	}
	
	/*
	 * Register a new user
	 */
    @RequestMapping(value="/aggregators/user/register",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String registerNewUser(@RequestBody final UserProfile jsonUser) {
    	
    	String result = SharpCartConstants.SERVER_ERROR_CODE;
    	
    	//debug
    	LOG.info("User Name: "+jsonUser.getUserName()); //name
    	LOG.info("User Password: "+jsonUser.getPassword()); //password
    	LOG.info("User Zip: "+jsonUser.getZip()); //zip
    	LOG.info("User Family Size: "+jsonUser.getFamilySize()); //family size
    	LOG.info("User Stores: "+jsonUser.getStores()); //stores
    	
    	//hash user password
    	try {
    		jsonUser.setPassword(PasswordHash.createHash(jsonUser.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//check if user already exits in the system
	  	session = factory.openSession();
		  
	  	session.beginTransaction();
	  	Query query = session.createQuery("from SharpCartUser where userName = :userName");
	  	query.setString("userName", jsonUser.getUserName());
	  	SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  	session.getTransaction().commit();
	  	
	  	//if we have a user with the same user name in the database we return "exists"
	  	if (user!=null)
	  		result = SharpCartConstants.USER_EXISTS_IN_DB_CODE;
	  	else //add new user to database
	  	{
	  	  /* The user we created from the JSON file is NOT the same user we
	  	   * can add to the database, namely they use different "stores" variable.
	  	   * In order to be able to save the JSON user in the database we need to convert it
	  	   * to our persistence user model
	  	   */
	  	  SharpCartUser persistanceUser = new SharpCartUser();
	  	  persistanceUser.setUserName(jsonUser.getUserName());
	  	  persistanceUser.setPassword(jsonUser.getPassword());
	  	  persistanceUser.setZip(jsonUser.getZip());
	  	  persistanceUser.setFamilySize(jsonUser.getFamilySize());
	  	  
	  	  //Convert the JSON stores string to a set of store objects
	  	  String stores[] = jsonUser.getStores().split("-");
	  	  
	  	  //grab stores from database
	  	  session.beginTransaction();	
	  	  query = session.createQuery("from Store");
	  	  List<Store> storeList = query.list();	
	  	  session.getTransaction().commit();
		  
	  	  Set<Store> userStores = new HashSet<Store>();
	  	  
	  	  for (String storeId : stores)
	  	  {
	  		  for (Store store : storeList)
	  		  {
	  			  if (store.getId()==Long.valueOf(storeId))
	  			  {
	  				  userStores.add(store);
	  			  }
	  		  }
	  	  }
	  	  
	  	  persistanceUser.setStores(userStores);
	  	  
	  	  //save user into database
	  	  session.beginTransaction();
		  session.save(persistanceUser);
		  session.getTransaction().commit();
		  
		  result = SharpCartConstants.RECORD_CREATED;
	  	}
	  	
	  	//close session
	  	session.close();
	  	
	  	//debug
    	LOG.info("Return Code: "+result); 
    	
    	return result;
    }
    
    /*
     * User login.
     * It is important to mention that this is not truly login but more of a check if the user/password
     * exist in the system. The decision what to do with this information is up to the client side.
     */
    @RequestMapping(value="/aggregators/user/login", method = RequestMethod.POST)
    @ResponseBody
    public String loginUser(@RequestParam(value="userName", required=true) String userName,
    						@RequestParam(value="password", required=true) String password) {
    	
    	String result = SharpCartConstants.ACCESS_DENIED;
    	
    	//Check if user name is in database
	  	session = factory.openSession();
		  
	  	session.beginTransaction();
	  	Query query = session.createQuery("from SharpCartUser where userName = :userName");
	  	query.setString("userName", userName);
	  	SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  	session.getTransaction().commit();
	  	
	  	//if the user doesnt exist in the database deny access
	  	if (user==null)
	  	{
	  		result = SharpCartConstants.ACCESS_DENIED;
	  	} else //user is in database, now its time to validate password
	  	{	
	  		try {
				if (PasswordHash.validatePassword(password, user.getPassword()))
				{
					result = SharpCartConstants.SUCCESS;
				} else // user is in database but provided password is incorrect
				{
					result = SharpCartConstants.USER_EXISTS_IN_DB_CODE;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = SharpCartConstants.SERVER_ERROR_CODE;
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = SharpCartConstants.SERVER_ERROR_CODE;
			}
	  	}
	  	
        return result;
    }
    
	/*
	 * Register a new user
	 */
    @RequestMapping(value="/aggregators/user/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateUser(@RequestBody final UserProfile jsonUser) {
    	
    	String result = SharpCartConstants.SERVER_ERROR_CODE;
    	
    	//debug
    	LOG.info("User Name: "+jsonUser.getUserName()); //name
    	LOG.info("User Password: "+jsonUser.getPassword()); //password
    	LOG.info("User Zip: "+jsonUser.getZip()); //zip
    	LOG.info("User Family Size: "+jsonUser.getFamilySize()); //family size
    	LOG.info("User Stores: "+jsonUser.getStores()); //stores
    	
    	//check if user already exits in the system
	  	session = factory.openSession();
		  
	  	session.beginTransaction();
	  	Query query = session.createQuery("from SharpCartUser where userName = :userName");
	  	query.setString("userName", jsonUser.getUserName());
	  	SharpCartUser user = (SharpCartUser)query.uniqueResult();
	  	session.getTransaction().commit();
	  	
	  	//if we dont have the user in our database we return access denied
	  	if (user==null)
	  		result = SharpCartConstants.ACCESS_DENIED;
	  	else //update user in database
	  	{
	  	  /* The user we created from the JSON file is NOT the same user we
	  	   * can add to the database, namely they use different "stores" variable.
	  	   * In order to be able to save the JSON user in the database we need to convert it
	  	   * to our persistence user model
	  	   */

	  		user.setZip(jsonUser.getZip());
	  		user.setFamilySize(jsonUser.getFamilySize());
	  	  
	  		//Convert the JSON stores string to a set of store objects
	  		String stores[] = jsonUser.getStores().split("-");
	  	  
	  		//grab stores from database
	  		session.beginTransaction();	
	  		query = session.createQuery("from Store");
	  		List<Store> storeList = query.list();	
	  		session.getTransaction().commit();
		  
	  		Set<Store> userStores = new HashSet<Store>();
	  	  
	  		for (String storeId : stores)
	  		{
	  			for (Store store : storeList)
	  			{
	  			  if (store.getId()==Long.valueOf(storeId))
	  			  {
	  				  userStores.add(store);
	  			  }
	  			}
	  		}
	  	  
	  		user.setStores(userStores);
	  	  
	  		//save user into database
	  		session.beginTransaction();
	  		session.update(user);
	  		session.getTransaction().commit();
		  
	  		result = SharpCartConstants.RECORD_CREATED;
	  	}
	  	
	  	//close session
	  	session.close();
	  	
	  	//debug
    	LOG.info("Return Code: "+result); 
    	
    	return result;
    }
}
