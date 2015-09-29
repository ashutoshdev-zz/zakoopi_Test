package com.zakoopi.search;

public class AllProduct {

	int _id;
	String _productName;
	String _productCategory;
	String slug;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public AllProduct() {

	}

	public AllProduct(int id, String productName, String productCategory,
			String slug) {
		this._id = id;
		this._productName = productName;
		this._productCategory = productCategory;
	}

	public AllProduct(String productName, String productCategory,
			String slug) {
		this._productName = productName;
		this._productCategory = productCategory;
		this.slug = slug;
	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public String getProductName() {
		return this._productName;
	}

	public void setProductName(String productName) {
		this._productName = productName;
	}

	public String getProductCategory() {
		return this._productCategory;
	}

	public void setProductCategory(String productCategory) {
		this._productCategory = productCategory;
	}

}
