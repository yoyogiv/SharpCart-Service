package com.sharpcart.rest.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * Shopping item unit
 */
@Entity
@Table(name="Unit")
public class Unit {
	private Long id;
	
	private String name;
	
	
	/**
	 * @param name
	 */
	public Unit(String name) {
		this.name = name;
	}
	
	//Empty constructor
	public Unit()
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
	@Column(unique=true) //Unit name is unique for each category
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
