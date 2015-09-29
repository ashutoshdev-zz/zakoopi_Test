package com.zakoopi.search;

public class AllArea {

	int _id;
	String _marketName;
	String _productCategory;
	String myslug;

	public String getMyslug() {
		return myslug;
	}

	public void setMyslug(String myslug) {
		this.myslug = myslug;
	}

	public AllArea() {

	}

	public AllArea(int id, String marketName) {
		this._id = id;
		this._marketName = marketName;
		// this._productCategory = productCategory;
	}

	public AllArea(String marketName, String myslug ) {
		this._marketName = marketName;
		this.myslug = myslug;
		// this._productCategory = productCategory;
	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public String getMarketName() {
		return this._marketName;
	}

	public void setMarketName(String marketName) {
		this._marketName = marketName;
	}

	/*
	 * public String getProductCategory(){ return this._productCategory; }
	 * 
	 * public void setProductCategory(String productCategory){
	 * this._productCategory = productCategory; }
	 */
}
