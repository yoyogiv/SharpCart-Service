package com.sharpcart.rest.model;

public class ShoppingListItem implements Comparable<ShoppingListItem> {
    private Long id;
    private String name;
    private double price_per_unit;
    private double quantity;
    private String unit;
    private String category;
    private String description;
    private Long shopping_item_category_id;
    private Long shopping_item_unit_id;
    private double conversion_ratio;
    private double price;
    private double total_price;
    private double package_quantity;
    private String default_unit_in_db;
    private String is_using_default_unit;
    private String in_db;
    private boolean in_cart = false;
    private String image_location;
    private boolean is_deleted;
    private boolean best_price_per_unit = false;
    
    public ShoppingListItem()
    {
    	this.id=0L;
    	this.name = "";
    	this.price_per_unit = 0;
    	this.quantity = 0;
    	this.unit = "";
    	this.category="";
    	this.description="";
    	this.shopping_item_category_id = 0L;
    	this.shopping_item_unit_id = 0L;
    	this.conversion_ratio = -1;
    	this.price = 0;
    	this.total_price = 0;
    	this.package_quantity =0;
    	this.default_unit_in_db = "";
    	this.is_using_default_unit ="";
    	this.in_db="";
    	this.in_cart = false;
    	this.image_location="/ShoppingItem/images/default.png";
    	this.is_deleted = false;
    	this.best_price_per_unit = false;
    }

	@Override
    public int compareTo(final ShoppingListItem arg0) {

		return name.compareTo(arg0.getName());
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final Long id) {
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
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the price_per_unit
	 */
	public double getPrice_per_unit() {
		return price_per_unit;
	}

	/**
	 * @param price_per_unit the price_per_unit to set
	 */
	public void setPrice_per_unit(final double price_per_unit) {
		this.price_per_unit = price_per_unit;
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
	public void setQuantity(final double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(final String unit) {
		this.unit = unit;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(final String category) {
		this.category = category;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the shopping_item_category_id
	 */
	public Long getShopping_item_category_id() {
		return shopping_item_category_id;
	}

	/**
	 * @param shopping_item_category_id the shopping_item_category_id to set
	 */
	public void setShopping_item_category_id(final Long shopping_item_category_id) {
		this.shopping_item_category_id = shopping_item_category_id;
	}

	/**
	 * @return the shopping_item_unit_id
	 */
	public Long getShopping_item_unit_id() {
		return shopping_item_unit_id;
	}

	/**
	 * @param shopping_item_unit_id the shopping_item_unit_id to set
	 */
	public void setShopping_item_unit_id(final Long shopping_item_unit_id) {
		this.shopping_item_unit_id = shopping_item_unit_id;
	}

	/**
	 * @return the conversion_ratio
	 */
	public double getConversion_ratio() {
		return conversion_ratio;
	}

	/**
	 * @param conversion_ratio the conversion_ratio to set
	 */
	public void setConversion_ratio(final double conversion_ratio) {
		this.conversion_ratio = conversion_ratio;
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
	public void setPrice(final double price) {
		this.price = price;
	}

	/**
	 * @return the total_price
	 */
	public double getTotal_price() {
		return total_price;
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(final double total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the package_quantity
	 */
	public double getPackage_quantity() {
		return package_quantity;
	}

	/**
	 * @param package_quantity the package_quantity to set
	 */
	public void setPackage_quantity(final double package_quantity) {
		this.package_quantity = package_quantity;
	}

	/**
	 * @return the default_unit_in_db
	 */
	public String getDefault_unit_in_db() {
		return default_unit_in_db;
	}

	/**
	 * @param default_unit_in_db the default_unit_in_db to set
	 */
	public void setDefault_unit_in_db(final String default_unit_in_db) {
		this.default_unit_in_db = default_unit_in_db;
	}

	/**
	 * @return the is_using_default_unit
	 */
	public String getIs_using_default_unit() {
		return is_using_default_unit;
	}

	/**
	 * @param is_using_default_unit the is_using_default_unit to set
	 */
	public void setIs_using_default_unit(final String is_using_default_unit) {
		this.is_using_default_unit = is_using_default_unit;
	}

	/**
	 * @return the in_db
	 */
	public String getIn_db() {
		return in_db;
	}

	/**
	 * @param in_db the in_db to set
	 */
	public void setIn_db(final String in_db) {
		this.in_db = in_db;
	}

	/**
	 * @return the image_location
	 */
	public String getImage_location() {
		return image_location;
	}

	/**
	 * @param image_location the image_location to set
	 */
	public void setImage_location(final String image_location) {
		this.image_location = image_location;
	}

	/**
	 * @return the is_deleted
	 */
	public boolean isIs_deleted() {
		return is_deleted;
	}

	/**
	 * @param is_deleted the is_deleted to set
	 */
	public void setIs_deleted(final boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	/**
	 * @return the best_price_per_unit
	 */
	public boolean isBest_price_per_unit() {
		return best_price_per_unit;
	}

	/**
	 * @param best_price_per_unit the best_price_per_unit to set
	 */
	public void setBest_price_per_unit(final boolean best_price_per_unit) {
		this.best_price_per_unit = best_price_per_unit;
	}

	/**
	 * @return the in_cart
	 */
	public boolean isIn_cart() {
		return in_cart;
	}

	/**
	 * @param in_cart the in_cart to set
	 */
	public void setIn_cart(final boolean in_cart) {
		this.in_cart = in_cart;
	}

}
