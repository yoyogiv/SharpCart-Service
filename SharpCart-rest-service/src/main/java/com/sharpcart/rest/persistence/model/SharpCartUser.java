package com.sharpcart.rest.persistence.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.print.attribute.HashAttributeSet;

@Entity
@Table(name="SharpCartUser")
public class SharpCartUser {
	
	private Long id;
	
	private Set<Store> stores;
	
	private String zip;
	
	private String familySize;
	
	private String userName;
	
	private String password;
	
	private Date	userInformationLastUpdate;
	
	private Set<UserShoppingItem> regularShoppingItems;
	
	private Set<UserExtraShoppingItem> extraShoppingItems;
	
	private Date	activeShoppingListLastUpdate;	

	public SharpCartUser()
	{	
		this.regularShoppingItems = new HashSet<UserShoppingItem>();
		this.extraShoppingItems = new HashSet<UserExtraShoppingItem>();	
		this.stores = new HashSet<Store>();
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the stores
	 */
	@ManyToMany
	public Set<Store> getStores() {
		return stores;
	}


	/**
	 * @param stores the stores to set
	 */
	private void setStores(Set<Store> stores) {
		this.stores = stores;
	}
	
	public void addStores(Store store)
	{
		this.getStores().add(store);
	}
	
	public void addStores(Set<Store> stores)
	{
		this.getStores().addAll(stores);
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the familySize
	 */
	public String getFamilySize() {
		return familySize;
	}
	/**
	 * @param familySize the familySize to set
	 */
	public void setFamilySize(String familySize) {
		this.familySize = familySize;
	}
	/**
	 * @return the userName
	 */
	@Column(unique=true) //User name is unique
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userInformationLastUpdate
	 */
	public Date getUserInformationLastUpdate() {
		return userInformationLastUpdate;
	}

	/**
	 * @param userInformationLastUpdate the userInformationLastUpdate to set
	 */
	public void setUserInformationLastUpdate(Date userInformationLastUpdate) {
		this.userInformationLastUpdate = userInformationLastUpdate;
	}

	/**
	 * @return the set of regular shopping items currently inside the user sharp list
	 */
	@OneToMany(cascade = {CascadeType.ALL},orphanRemoval=true)
    @JoinTable(name = "UserSharpListRegularItems", joinColumns = {
            @JoinColumn(name = "userId")}, inverseJoinColumns = {
            @JoinColumn(name = "userRegularShoppingItemId")})
	public Set<UserShoppingItem> getRegularShoppingItems() {
		return regularShoppingItems;
	}

	/**
	 * @param regularShoppingItems the regularShoppingItems to set
	 */
	private void setRegularShoppingItems(Set<UserShoppingItem> regularShoppingItems) {
		this.regularShoppingItems = regularShoppingItems;
	}

	public void addRegularShoppingItem(UserShoppingItem userRegularShoppingItem)
	{
		this.getRegularShoppingItems().add(userRegularShoppingItem);
	}
	
	public void addRegularShoppingItem(Set<UserShoppingItem> userRegularShoppingItem)
	{
		this.getRegularShoppingItems().addAll(userRegularShoppingItem);
	}
	
	/**
	 * @return the extraShoppingItems currently inside the user sharp list
	 */
	
	@OneToMany(cascade = {CascadeType.ALL},orphanRemoval=true)
    @JoinTable(name = "UserSharpListExtraItems", joinColumns = {
            @JoinColumn(name = "userId")}, inverseJoinColumns = {
            @JoinColumn(name = "userExtraShoppingItemId")})
	public Set<UserExtraShoppingItem> getExtraShoppingItems() {
		return extraShoppingItems;
	}
	
	/**
	 * @param extraShoppingItems the extraShoppingItems to set
	 */
	
	private void setExtraShoppingItems(Set<UserExtraShoppingItem> extraShoppingItems) {
		this.extraShoppingItems = extraShoppingItems;
	}
	
	public void addExtraShoppingItem(UserExtraShoppingItem userExtraShoppingItem)
	{
		this.getExtraShoppingItems().add(userExtraShoppingItem);
	}
	
	public void addExtraShoppingItem(Set<UserExtraShoppingItem> userExtraShoppingItem)
	{
		this.getExtraShoppingItems().addAll(userExtraShoppingItem);
	}
	
	/**
	 * @return the activeShoppingListLastUpdate
	 */
	public Date getActiveShoppingListLastUpdate() {
		return activeShoppingListLastUpdate;
	}

	/**
	 * @param activeShoppingListLastUpdate the activeShoppingListLastUpdate to set
	 */
	public void setActiveShoppingListLastUpdate(Date activeShoppingListLastUpdate) {
		this.activeShoppingListLastUpdate = activeShoppingListLastUpdate;
	}
	
	public boolean clearSet()
	{
		while (regularShoppingItems.iterator().hasNext())
		{
			regularShoppingItems.remove(regularShoppingItems.iterator().next());
		}
		
		
		while (extraShoppingItems.iterator().hasNext())
		{
			extraShoppingItems.remove(extraShoppingItems.iterator().next());
		}
		
		
		return this.regularShoppingItems.isEmpty()&&this.extraShoppingItems.isEmpty();
	}
}
