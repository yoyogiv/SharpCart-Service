package com.sharpcart.rest.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A shopping item
 */
@Entity
@Table(name="ShoppingItem")

public class ShoppingItem implements Comparable<ShoppingItem>{
	private Long id;

	private String name;
	
	private String description;
	
	private Category category;
	
	private Unit unit;
	
	private String imageLocation;
	
	private float unitToItemConversionRatio;
	
	
	public ShoppingItem(String name, String description,
			Category category, Unit unit, String imageLocation,
			float unitToItemConversionRatio) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.unit = unit;
		this.imageLocation = imageLocation;
		this.unitToItemConversionRatio = unitToItemConversionRatio;
	}

	//empty constructor
	public ShoppingItem(){
		
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
	 * @return the description
	 */
	@Column(unique=true) //Shopping item description is unique for every item
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the category
	 */
	@ManyToOne
	@JoinColumn(name="CategoryId")
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the unit
	 */
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="UnitId")
	public Unit getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
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
	 * @return the unitToItemConversionRatio
	 */
	public float getUnitToItemConversionRatio() {
		return unitToItemConversionRatio;
	}

	/**
	 * @param unitToItemConversionRatio the unitToItemConversionRatio to set
	 */
	public void setUnitToItemConversionRatio(float unitToItemConversionRatio) {
		this.unitToItemConversionRatio = unitToItemConversionRatio;
	}

	@Override
	public int compareTo(ShoppingItem o) {
		return this.id.compareTo(o.getId());
	}
	
	
}
