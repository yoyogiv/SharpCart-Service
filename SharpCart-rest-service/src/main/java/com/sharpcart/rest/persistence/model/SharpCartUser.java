package com.sharpcart.rest.persistence.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	
	private Date	lastUpdated;
	
	/**
	 * @param stores
	 * @param zip
	 * @param familySize
	 * @param userName
	 * @param password
	 */
	public SharpCartUser(Set<Store> stores, String zip, String familySize,
			String userName, String password) {
		this.stores = stores;
		this.zip = zip;
		this.familySize = familySize;
		this.userName = userName;
		this.password = password;
		//this.stores = new HashSet<Store>();
	}

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
	 * @return the lastUpdated
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
}
