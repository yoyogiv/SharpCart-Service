package com.sharpcart.rest.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
	
	private List<UsZipCode> servicingZipCodes;
	
	private String imageLocation;
	
	private String onSaleFlyerURL;
	
	private double lat;
	
	private double lng;

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
		this.servicingZipCodes = new ArrayList<UsZipCode>();
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
	 * @return the servicingZipCodes
	 */
	@ManyToMany(cascade = {CascadeType.ALL},fetch=FetchType.EAGER)
    @JoinTable(	name = "StoreServicingZipCode", 
    			joinColumns = {@JoinColumn(name = "storeId")}, 
    			inverseJoinColumns = {@JoinColumn(name = "zipCode")})
	public List<UsZipCode> getServicingZipCodes() {
		return servicingZipCodes;
	}

	/**
	 * @param servicingZipCodes the servicingZipCodes to set
	 */
	public void setServicingZipCodes(List<UsZipCode> servicingZipCodes) {
		this.servicingZipCodes = servicingZipCodes;
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
	 * @return the onSaleFlyerURL
	 */
	public String getOnSaleFlyerURL() {
		return onSaleFlyerURL;
	}

	/**
	 * @param onSaleFlyerURL the onSaleFlyerURL to set
	 */
	public void setOnSaleFlyerURL(String onSaleFlyerURL) {
		this.onSaleFlyerURL = onSaleFlyerURL;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	
}
