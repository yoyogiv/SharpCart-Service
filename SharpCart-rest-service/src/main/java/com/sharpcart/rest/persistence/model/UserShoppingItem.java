package com.sharpcart.rest.persistence.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="UserShoppingItem")
public class UserShoppingItem {
	
	private Long id;
	private ShoppingItem shoppingItem;
	private SharpCartUser user;
	private double quantity;
	
	public UserShoppingItem()
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
	 * @return the shoppingItem
	 */
	@ManyToOne
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
	 * @return the user
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sharpCartUserId",nullable = false)
	public SharpCartUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(SharpCartUser user) {
		this.user = user;
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
	
}
