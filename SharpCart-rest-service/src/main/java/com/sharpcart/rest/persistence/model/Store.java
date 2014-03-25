package com.sharpcart.rest.persistence.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * A store
 */
@Entity
@Table(name="Store")
public class Store {

	private Long id;

	private String name;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String zip;
	
	private String imageLocation;
	
	private Set<SharpCartUser> sharpCartUsers;

	/**
	 * @param name
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param imageLocation
	 */
	public Store(String name, String street, String city,
			String state, String zip, String imageLocation) {
		this.name = name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.imageLocation = imageLocation;
		this.sharpCartUsers = new HashSet<SharpCartUser>();
	}
	
	public Store()
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
	 * @return the name
	 */
	@Column(unique=true) //Store name is unique
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
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
	 * @return the imageLocation
	 */
	public String getImageLocation() {
		return imageLocation;
	}

	/**
	 * @param imageLocation the imageLocation to set
	 */
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	/**
	 * @return the sharpCartUsers
	 */
	@ManyToMany(mappedBy = "stores")
	public Set<SharpCartUser> getSharpCartUsers() {
		return sharpCartUsers;
	}

	/**
	 * @param sharpCartUsers the sharpCartUsers to set
	 */
	public void setSharpCartUsers(Set<SharpCartUser> sharpCartUsers) {
		this.sharpCartUsers = sharpCartUsers;
	}
	
}
