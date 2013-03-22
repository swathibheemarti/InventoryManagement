package storePackage.Entities;

public class Item {
	
	private String _itemID;
	private String _itemName;
	private Double _itemCost;
	private Integer _quantity;
	private String _supplier;
	private String _suppID;
	
	public String get_itemID() {
		return _itemID;
	}
	public void set_itemID(String _itemID) {
		this._itemID = _itemID;
	}
	public String get_itemName() {
		return _itemName;
	}
	public void set_itemName(String _itemName) {
		this._itemName = _itemName;
	}
	public Double get_itemCost() {
		return _itemCost;
	}
	public void set_itemCost(Double _itemCost) {
		this._itemCost = _itemCost;
	}
	public Integer get_quantity() {
		return _quantity;
	}
	public void set_quantity(Integer _quantity) {
		this._quantity = _quantity;
	}
	public String get_supplier() {
		return _supplier;
	}
	public void set_supplier(String _supplier) {
		this._supplier = _supplier;
	}
	public String get_suppID() {
		return _suppID;
	}
	public void set_suppID(String _suppID) {
		this._suppID = _suppID;
	}
		
}
