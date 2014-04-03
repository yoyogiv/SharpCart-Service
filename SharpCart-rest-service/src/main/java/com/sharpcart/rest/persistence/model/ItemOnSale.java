package com.sharpcart.rest.persistence.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint ;

@Entity
@Table(
		name="ItemOnSale",
		uniqueConstraints =@UniqueConstraint(columnNames = {"storeId", "shoppingItemId"})
)
public class ItemOnSale {

	private Long id;
	
	private double price;
	
	private double pricePerUnit;
	
	private double quantity;
	
	private Date startDate;
	
	private Date endDate;
	
	private Store store;
	
	private ShoppingItem shoppingItem;
	
	public ItemOnSale()
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
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the pricePerUnit
	 */
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	/**
	 * @param pricePerUnit the pricePerUnit to set
	 */
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the store
	 */
	@ManyToOne
	@JoinColumn(name="storeId")
	public Store getStore() {
		return store;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * @return the shoppingItem
	 */
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="shoppingItemId")
	public ShoppingItem getShoppingItem() {
		return shoppingItem;
	}

	/**
	 * @param shoppingItem the shoppingItem to set
	 */
	public void setShoppingItem(ShoppingItem shoppingItem) {
		this.shoppingItem = shoppingItem;
	}
	
}
