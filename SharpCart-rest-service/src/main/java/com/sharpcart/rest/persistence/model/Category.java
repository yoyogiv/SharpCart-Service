package com.sharpcart.rest.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * Shopping item category
 */
@Entity
@Table(name="Category")
public class Category {
	private Long id;
	
	private String name;
	
	private String imageLocation;
	
	private int priority;
	
	
	/**
	 * @param name
	 * @param imageLocation
	 * @param priority
	 */
	public Category(String name, String imageLocation, int priority) {
		this.name = name;
		this.imageLocation = imageLocation;
		this.priority = priority;
	}
	
	//Empty constructor
	public Category()
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
	@Column(unique=true) //Category name is unique for each category
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
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
