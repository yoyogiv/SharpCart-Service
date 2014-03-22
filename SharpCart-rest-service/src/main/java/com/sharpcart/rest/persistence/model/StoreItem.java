package com.sharpcart.rest.persistence.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A shopping item
 */
@Entity
@Table(name="StoreItem")
public class StoreItem {
	private Long id;
	
	private Store store;
	
	private ShoppingItem shoppingItem;
	
	private float price;
	
	private float quantity;
	
	private Date updated;

	/**
	 * @param store
	 * @param shoppingItem
	 * @param price
	 * @param quantity
	 * @param updated
	 */
	public StoreItem(Store store, ShoppingItem shoppingItem, float price,
			float quantity, Date updated) {
		this.store = store;
		this.shoppingItem = shoppingItem;
		this.price = price;
		this.quantity = quantity;
		this.updated = updated;
	}
	
	public StoreItem()
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

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the quantity
	 */
	public float getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
