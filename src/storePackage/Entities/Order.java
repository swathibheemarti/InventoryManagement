package storePackage.Entities;

import java.sql.Date;

public class Order {
	
	private String _orderID;
	private String _itemID;
	private String _supplier;
	private Integer _quantity;
	private String _itemName;
	private Double _price;
	private String _orderStatus;
	private String _orderDate;
	
	/**
	 * @return the _orderID
	 */
	public String get_orderID() {
		return _orderID;
	}
	/**
	 * @param _orderID the _orderID to set
	 */
	public void set_orderID(String _orderID) {
		this._orderID = _orderID;
	}
	/**
	 * @return the _itemID
	 */
	public String get_itemID() {
		return _itemID;
	}
	/**
	 * @param _itemID the _itemID to set
	 */
	public void set_itemID(String _itemID) {
		this._itemID = _itemID;
	}
	/**
	 * @return the _supplier
	 */
	public String get_supplier() {
		return _supplier;
	}
	/**
	 * @param _supplier the _supplier to set
	 */
	public void set_supplier(String _supplier) {
		this._supplier = _supplier;
	}
	/**
	 * @return the _quantity
	 */
	public Integer get_quantity() {
		return _quantity;
	}
	/**
	 * @param _quantity the _quantity to set
	 */
	public void set_quantity(Integer _quantity) {
		this._quantity = _quantity;
	}
	/**
	 * @return the _itemName
	 */
	public String get_itemName() {
		return _itemName;
	}
	/**
	 * @param _itemName the _itemName to set
	 */
	public void set_itemName(String _itemName) {
		this._itemName = _itemName;
	}
	/**
	 * @return the _price
	 */
	public Double get_price() {
		return _price;
	}
	/**
	 * @param _price the _price to set
	 */
	public void set_price(Double _price) {
		this._price = _price;
	}
	/**
	 * @return the _orderStatus
	 */
	public String get_orderStatus() {
		return _orderStatus;
	}
	/**
	 * @param _orderStatus the _orderStatus to set
	 */
	public void set_orderStatus(String _orderStatus) {
		this._orderStatus = _orderStatus;
	}
	/**
	 * @return the _orderDate
	 */
	public String get_orderDate() {
		return _orderDate;
	}
	/**
	 * @param string the _orderDate to set
	 */
	public void set_orderDate(String string) {
		this._orderDate = string;
	}
		
}
