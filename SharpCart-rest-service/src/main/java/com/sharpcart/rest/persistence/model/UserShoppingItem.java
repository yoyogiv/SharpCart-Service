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
public class UserShoppingItem implements Comparable<UserShoppingItem>{
	
	private Long id;
	private ShoppingItem shoppingItem;
	private SharpCartUser user;
	private double quantity;
	
	public UserShoppingItem()
	{
		shoppingItem = new ShoppingItem();
		user = new SharpCartUser();
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
	@JoinColumn(name="shoppingItemId",nullable=false)
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
	@ManyToOne
	@JoinColumn(name="userId",nullable=false)
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

	@Override
	public int compareTo(UserShoppingItem o) {
		return this.shoppingItem.getId().compareTo(o.getShoppingItem().getId());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shoppingItem == null) ? 0 : shoppingItem.getId().hashCode());
		result = prime * result + ((user == null) ? 0 : user.getUserName().hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		UserShoppingItem other = (UserShoppingItem) obj;
		
		if (shoppingItem == null) {
			if (other.shoppingItem != null)
				return false;
			
		} else if (!shoppingItem.getId().equals(other.shoppingItem.getId()))
			return false;
		
		if (user == null) {
			if (other.user != null)
				return false;
			
		} else if (!user.getId().equals(other.user.getUserName()))
			return false;
		
		return true;
	}	
	
}
