package com.sharpcart.rest.persistence.model;

import java.util.Date;
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
	
	private Set<UserShoppingItem> activeShoppingList;
	
	private Date	activeShoppingListLastUpdate;	

	public SharpCartUser()
	{
		
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
	public void setStores(Set<Store> stores) {
		this.stores = stores;
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
	 * @return the activeShoppingList
	 */
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "user")
	/*
	@JoinTable (
			name="ActiveSharpLists",
	        joinColumns =  @JoinColumn(name = "sharpCartUser_id"),
	        inverseJoinColumns = @JoinColumn(name = "userShoppingItem_id")
			)
	*/
	public Set<UserShoppingItem> getActiveShoppingList() {
		return activeShoppingList;
	}

	/**
	 * @param activeShoppingList the activeShoppingList to set
	 */
	public void setActiveShoppingList(Set<UserShoppingItem> activeShoppingList) {
		this.activeShoppingList = activeShoppingList;
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
}
