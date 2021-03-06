package com.sharpcart.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.sharpcart.rest.model.SharpList;
import com.sharpcart.rest.model.ShoppingListItem;
import com.sharpcart.rest.model.StorePrices;
import com.sharpcart.rest.model.UserProfile;
import com.sharpcart.rest.persistence.model.SharpCartUser;
import com.sharpcart.rest.persistence.model.ShoppingItem;
import com.sharpcart.rest.persistence.model.Store;
import com.sharpcart.rest.persistence.model.StoreItem;
import com.sharpcart.rest.persistence.model.UsZipCode;
import com.sharpcart.rest.persistence.model.UserExtraShoppingItem;
import com.sharpcart.rest.persistence.model.UserShoppingItem;
import com.sharpcart.rest.utilities.PasswordHash;
import com.sharpcart.rest.utilities.SharpCartConstants;

@Controller
//@RequestMapping("/aggregators/user")
public class UserManagementController {
    private static Logger LOG = LoggerFactory.getLogger(UserManagementController.class);
	
    private Query query;
	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public UserManagementController()
	{
		
	}
	
	/*
	 * Register a new user
	 */
    @RequestMapping(value="/aggregators/user/register",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String registerNewUser(@RequestBody final UserProfile jsonUser) {
    	
    	String result = SharpCartConstants.SERVER_ERROR_CODE;
    	
    	SharpCartUser user = null;
    	
    	//debug
    	/*
    	LOG.info("User Name: "+jsonUser.getUserName()); //name
    	LOG.info("User Password: "+jsonUser.getPassword()); //password
    	LOG.info("User Zip: "+jsonUser.getZip()); //zip
    	LOG.info("User Family Size: "+jsonUser.getFamilySize()); //family size
    	LOG.info("User Stores: "+jsonUser.getStores()); //stores
    	*/
    	
    	//hash user password
    	try {
    		jsonUser.setPassword(PasswordHash.createHash(jsonUser.getPassword()));
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//check if user already exits in the system
    	try {
	    	DAO.getInstance().begin();
		  	query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  	query.setString("userName", jsonUser.getUserName());
		  	user = (SharpCartUser)query.uniqueResult();
		  	DAO.getInstance().commit();
    	} catch (final HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
	  	
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
	  	  final SharpCartUser persistanceUser = new SharpCartUser();
	  	  persistanceUser.setUserName(jsonUser.getUserName());
	  	  persistanceUser.setPassword(jsonUser.getPassword());
	  	  
	  	  //create a zip object for the user
	  	  try {
	  		DAO.getInstance().begin();
	  		query = DAO.getInstance().getSession().createQuery("from UsZipCode where zip = :userZipCode");
	  		query.setInteger("userZipCode", Integer.valueOf(jsonUser.getZip()));
	  		UsZipCode userZIPCode = (UsZipCode) query.uniqueResult();
	  		DAO.getInstance().commit();
	  		
	  		persistanceUser.setZip(userZIPCode);
	  	  } catch (HibernateException ex)
	  	  {
	  		  DAO.getInstance().rollback();
	  		  ex.printStackTrace();
	  	  }
	  	  
	  	  persistanceUser.setFamilySize(jsonUser.getFamilySize());
	  	  
	  	  
	  	  //Convert the JSON stores string to a set of store objects
	  	  //final String stores[] = jsonUser.getStores().split("-");
	  	  
	  	  //grab stores from database
	  	  try {
	  		  /*
		  	  DAO.getInstance().begin();
		  	  query = DAO.getInstance().getSession().createQuery("from Store");
		  	  final List<Store> storeList = query.list();	
		  	  DAO.getInstance().commit();
			  
		  	  final HashSet<Store> userStores = new HashSet<Store>();
		  	  
		  	  for (final String storeId : stores)
		  	  {
		  		  for (final Store store : storeList)
		  		  {
		  			  if (store.getId()==Long.valueOf(storeId))
		  			  {
		  				  userStores.add(store);
		  			  }
		  		  }
		  	  }
		  	  
		  	  persistanceUser.addStores(userStores);
		  	  */
	  	  	  
	  	  	  persistanceUser.addStores(jsonUser.getStores());
	  	  	  
		  	  if (!jsonUser.getLastUpdated().equals(new Date(0)))
		  	  {
				persistanceUser.setUserInformationLastUpdate(df.parse(jsonUser.getLastUpdated()));

		  	  } else
		  	  {
		  		  persistanceUser.setUserInformationLastUpdate(new Date());
		  	  }
		  	  
		  	  //persistanceUser.setRegularShoppingItems(null);
		  	  persistanceUser.setActiveShoppingListLastUpdate(null);
		  	  
		  	  //save user into database
		  	  DAO.getInstance().begin();
			  DAO.getInstance().getSession().save(persistanceUser);
			  DAO.getInstance().commit();
			  
			  result = SharpCartConstants.RECORD_CREATED;
	  	  } catch (final HibernateException | ParseException ex)
	  	  {
	  		  DAO.getInstance().rollback();
	  		  ex.printStackTrace();
	  	  }
	  	}
	  	
	  	//close session
	  	DAO.getInstance().close();
	  	
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
    	 SharpCartUser user = null;
    	 
    	//Check if user name is in database
    	try {
	    	DAO.getInstance().begin();
		  	query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  	query.setString("userName", userName);
		  	user = (SharpCartUser)query.uniqueResult();
		  	DAO.getInstance().commit();
		  	
		  	DAO.getInstance().close();
    	} catch (final HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
    	
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
			} catch (final NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = SharpCartConstants.SERVER_ERROR_CODE;
			} catch (final InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = SharpCartConstants.SERVER_ERROR_CODE;
			}
	  	}
	  	
        return result;
    }
    
	/*
	 * Update a user
	 */
    @RequestMapping(value="/aggregators/user/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserProfile updateUser(@RequestBody final UserProfile jsonUser) {
    	
    	Assert.notNull(jsonUser,"jsonUser cannot be null for an update operation");
    	
    	String result = SharpCartConstants.SERVER_ERROR_CODE;
    	SharpCartUser user = null;
    	UserProfile updatedJsonUser = new UserProfile();
    	
    	//debug	
    	LOG.info("User Name: "+jsonUser.getUserName()); //name
    	LOG.debug("User Password: "+jsonUser.getPassword()); //password
    	LOG.debug("User Zip: "+jsonUser.getZip()); //zip
    	LOG.debug("User Family Size: "+jsonUser.getFamilySize()); //family size
    	LOG.debug("User Stores: "+jsonUser.getStores()); //stores
    	LOG.info("Last Updated: "+jsonUser.getLastUpdated()); //last updated
    	 
    	
    	//check if user already exits in the system
    	try {
	    	DAO.getInstance().begin();
		  	query = DAO.getInstance().getSession().createQuery("from SharpCartUser where userName = :userName");
		  	query.setString("userName", jsonUser.getUserName());
		  	user = (SharpCartUser)query.uniqueResult();
		  	DAO.getInstance().commit(); 	
    	} catch (final HibernateException ex)
    	{
    		DAO.getInstance().rollback();
    		ex.printStackTrace();
    	}
    	
	  	//if we dont have the user in our database we return access denied
	  	if (user==null)
	  		result = SharpCartConstants.ACCESS_DENIED;
	  	else //update user in database
	  	{
	  		//verify user password
	  		try {
				if ((jsonUser.getPassword()!=null) && PasswordHash.validatePassword(jsonUser.getPassword(), user.getPassword()))
				{
					//only update the database if the device profile is newer
					try {
							if (user.getUserInformationLastUpdate().before(df.parse(jsonUser.getLastUpdated())))
							{
								LOG.info("Device user information is newer, copying it to database");
								
							  /* The user we created from the JSON file is NOT the same user we
							   * can add to the database, namely they use different "stores" variable.
							   * In order to be able to save the JSON user in the database we need to convert it
							   * to our persistence user model
							   */
								
						  	  try {
							  		DAO.getInstance().begin();
							  		query = DAO.getInstance().getSession().createQuery("from UsZipCode where zip = :userZipCode");
							  		query.setInteger("userZipCode", Integer.valueOf(jsonUser.getZip()));
							  		UsZipCode userZIPCode = (UsZipCode) query.uniqueResult();
							  		DAO.getInstance().commit();
							  		
							  		user.setZip(userZIPCode);
							  	  } catch (HibernateException ex)
							  	  {
							  		  DAO.getInstance().rollback();
							  		  ex.printStackTrace();
							  	  }
						  	  
								user.setFamilySize(jsonUser.getFamilySize());
								
								user.getStores().clear();
								user.addStores(jsonUser.getStores());
								user.setUserInformationLastUpdate(new Date());
								
								//save user into database
								try {
									DAO.getInstance().begin();
									DAO.getInstance().getSession().update(user);
									DAO.getInstance().commit();
								} catch (HibernateException ex)
								{
									DAO.getInstance().rollback();
									ex.printStackTrace();
								}
								
								//since the device profile is newer we return it back to the device
								updatedJsonUser = jsonUser;
								
							} else //the information in the database is newer so we need to update the device
							{
								LOG.info("Database user information is newer, copying it to device");
								
								updatedJsonUser.setUserName(user.getUserName());
								updatedJsonUser.setPassword("");
								updatedJsonUser.setZip(String.valueOf(user.getZip().getZip()));
								updatedJsonUser.setFamilySize(user.getFamilySize());
								
								updatedJsonUser.setStores(user.getStores());
								
								updatedJsonUser.setLastUpdated(df.format(user.getUserInformationLastUpdate()));
								
								//debug
						    	LOG.info("Updated User Name: "+updatedJsonUser.getUserName()); //name
						    	LOG.debug("Updated User Password: "+updatedJsonUser.getPassword()); //password
						    	LOG.debug("Updated User Zip: "+updatedJsonUser.getZip()); //zip
						    	LOG.debug("Updated User Family Size: "+updatedJsonUser.getFamilySize()); //family size
						    	LOG.debug("Updated User Stores: "+updatedJsonUser.getStores()); //stores
						    	LOG.info("Updated Last Updated: "+updatedJsonUser.getLastUpdated()); //last updated
							}
						} catch (final NumberFormatException | ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				} else // user is in database but provided password is incorrect
				{
					result = SharpCartConstants.ACCESS_DENIED;
					LOG.debug("User Name: "+jsonUser.getUserName()); //name
					LOG.debug("User Password: "+jsonUser.getFamilySize()); //password
				}
			} catch (final NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = SharpCartConstants.SERVER_ERROR_CODE;
			} catch (final InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = SharpCartConstants.SERVER_ERROR_CODE;
			} catch (final HibernateException ex)
			{
				DAO.getInstance().rollback();
				result = SharpCartConstants.SERVER_ERROR_CODE;
				ex.printStackTrace();
			}
	  	}
	  	
	  	//close session
	  	DAO.getInstance().close();  	
    	
    	return updatedJsonUser;
    }
    
    @RequestMapping(value="/aggregators/user/syncSharpList",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SharpList syncSharpList(@RequestBody final SharpList sharpList) {
   
    	Query query;
    	SharpCartUser user = null;
    	SharpList syncedSharpList = sharpList; //we start by assuming that there will be no need to update the user sharp list on the device
    	Set<UserShoppingItem> tempShoppingItemSet = new HashSet<UserShoppingItem>();
    	Set<UserExtraShoppingItem> tempExtraItemSet = new HashSet<UserExtraShoppingItem>();
    	
    	//get user from database
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
    	
    	Assert.notNull(user,"sync sharp list can not work without a valid user object");
    	
    	if (user!=null)
    	{	
    		//check if database user sharplist is newer or older than device version
    		try {
				if ((user.getActiveShoppingListLastUpdate()==null)||
						(user.getActiveShoppingListLastUpdate().getTime()<(df.parse(sharpList.getLastUpdated()).getTime())))	
					{
						LOG.info("Database user sharp list is older than device, updating database."+
								"Database: "+user.getActiveShoppingListLastUpdate().getTime()+
								" Device: "+df.parse(sharpList.getLastUpdated()).getTime());
					
		    			//if it is older, update database with device version and delete any older information				  	
		    			for (ShoppingListItem item : sharpList.getMainSharpList())
		    			{
		    				//convert a ShoppingListItem into a UserShoppingItem
		    				UserShoppingItem userShoppingItem = new UserShoppingItem();
		    				ShoppingItem shoppingItem = new ShoppingItem();
		    			
		    				userShoppingItem.setQuantity(item.getQuantity()); //set quantity
		    			
		    				//find a shopping item in the database using the item id
		    				if (!item.getCategory().equalsIgnoreCase("extra")) //check if this is an "extra" item
		    				{
		    					LOG.info("Regular Item Name: "+item.getName());
		    					
			    				try {
			    					DAO.getInstance().begin();
			    					query = DAO.getInstance().getSession().createQuery("from ShoppingItem where id = :shoppingItemId");
			    					query.setLong("shoppingItemId", item.getId());
			    					shoppingItem = (ShoppingItem)query.uniqueResult();
			    					DAO.getInstance().commit();
			    				
			    					userShoppingItem.setShoppingItem(shoppingItem);
			    					
				    				//add user shopping item to set
				    				tempShoppingItemSet.add(userShoppingItem);
				    				
			    				} catch (HibernateException ex)
			    				{
			    					DAO.getInstance().rollback();
			    					ex.printStackTrace();
			    					return syncedSharpList; //if even one of the grocery items can not be saved we need to exit the method
			    				}
		    				} else //this is an extra item
		    				{
		    					LOG.info("Extra Item Name: "+item.getName());
		    					
		    					UserExtraShoppingItem userExtraShoppingItem = new UserExtraShoppingItem();
		    					
		    					userExtraShoppingItem.setId(item.getId());
		    					userExtraShoppingItem.setName(item.getName());
		    					userExtraShoppingItem.setCategoryId(23);
		    					userExtraShoppingItem.setCategory("extra");
		    					userExtraShoppingItem.setImageLocation(item.getImage_location());
		    					userExtraShoppingItem.setQuantity(item.getQuantity());
		    					  
		    					tempExtraItemSet.add(userExtraShoppingItem);  
		    				}
		    			}
		    		
		    			//update user active sharp list and the returned synced sharp list
		    			Assert.isTrue(user.clearSet());
		    		
		    			user.addRegularShoppingItem(tempShoppingItemSet);
		    			user.addExtraShoppingItem(tempExtraItemSet);
		    			
		    			syncedSharpList.setMainSharpList(sharpList.getMainSharpList());
		    			
		    			//copy time stamp from device to database
		    			LOG.info("Copying time stamp from device to database: "+sharpList.getLastUpdated());
		    			
						user.setActiveShoppingListLastUpdate(df.parse(sharpList.getLastUpdated()));
						syncedSharpList.setLastUpdated(sharpList.getLastUpdated());
		    		
						//Save user to database
						try {
							DAO.getInstance().begin();
							DAO.getInstance().getSession().merge(user);
							DAO.getInstance().commit();
						} catch (HibernateException ex)
						{
							DAO.getInstance().rollback();
							ex.printStackTrace();
						}		
					} else if (user.getActiveShoppingListLastUpdate().getTime()>df.parse(sharpList.getLastUpdated()).getTime()) //database user sharp list is newer than device
					{
						LOG.info("Database user sharp list is newer than device, updating device."+
								"Database: "+user.getActiveShoppingListLastUpdate().getTime()+
								" Device: "+df.parse(sharpList.getLastUpdated()).getTime());
						
						//iterate over database items and generate a list<ShoppingListItem>
						List<ShoppingListItem> tempShoppingListItemList = new ArrayList<ShoppingListItem>();
						
						//start with regular items
						for (UserShoppingItem userShoppingItem : user.getRegularShoppingItems())
						{
							ShoppingListItem shoppingListItem = new ShoppingListItem();
							
							//copy information from UserShoppingItem into ShoppingListItem
							shoppingListItem.setQuantity(userShoppingItem.getQuantity()); //quantity
							shoppingListItem.setId(userShoppingItem.getShoppingItem().getId()); //id
							shoppingListItem.setName(userShoppingItem.getShoppingItem().getName()); //name
							shoppingListItem.setDescription(userShoppingItem.getShoppingItem().getDescription()); //description
							shoppingListItem.setShopping_item_unit_id(userShoppingItem.getShoppingItem().getUnit().getId()); //unit id
							shoppingListItem.setUnit(userShoppingItem.getShoppingItem().getUnit().getName()); //unit name
							shoppingListItem.setShopping_item_category_id(userShoppingItem.getShoppingItem().getCategory().getId()); //category id
							shoppingListItem.setCategory(userShoppingItem.getShoppingItem().getCategory().getName()); //category name
							shoppingListItem.setConversion_ratio(userShoppingItem.getShoppingItem().getUnitToItemConversionRatio()); //conversion ratio
							shoppingListItem.setImage_location(userShoppingItem.getShoppingItem().getImageLocation()); //image location
							
							//add item to temp list
							tempShoppingListItemList.add(shoppingListItem);
						}
						
						//than look for extra items
						for (UserExtraShoppingItem userExtraShoppingItem : user.getExtraShoppingItems())
						{
							ShoppingListItem extraShoppingListItem = new ShoppingListItem();
							
							//copy information from UserShoppingItem into ShoppingListItem
							extraShoppingListItem.setQuantity(userExtraShoppingItem.getQuantity()); //quantity
							extraShoppingListItem.setId(userExtraShoppingItem.getId()); //id
							extraShoppingListItem.setName(userExtraShoppingItem.getName()); //name
							extraShoppingListItem.setDescription(userExtraShoppingItem.getName());//description			
							extraShoppingListItem.setShopping_item_category_id(23L); //category id
							extraShoppingListItem.setCategory("extra"); //category name
							extraShoppingListItem.setConversion_ratio(-1); //conversion ratio
							extraShoppingListItem.setImage_location(userExtraShoppingItem.getImageLocation()); //image location
							
							//add item to temp list
							tempShoppingListItemList.add(extraShoppingListItem);
						}
						
		    			//add items from database to returned json
		    			syncedSharpList.setMainSharpList(tempShoppingListItemList);
		    			
		    			//copy time stamp from database to device
		    			LOG.info("Copying time stamp from database to device: "+df.format(user.getActiveShoppingListLastUpdate()));
		    			
		    			syncedSharpList.setLastUpdated(df.format(user.getActiveShoppingListLastUpdate()));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}
    	
    	DAO.getInstance().close();
    	
    	return syncedSharpList;
    }
    
    
}
