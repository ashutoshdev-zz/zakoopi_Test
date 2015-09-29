package com.zakoopi.searchstore.model;

import com.google.gson.annotations.SerializedName;

public class StoreidDetailModel {

	@SerializedName("id")
	String id;
	@SerializedName("store_name")
	String store_name;
	@SerializedName("market_name")
	String market_name;
	
	public String getMarket_name() {
		return market_name;
	}

	public void setMarket_name(String market_name) {
		this.market_name = market_name;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	
	public String getStore_id() {
		return id;
	}

	public void setStore_id(String store_id) {
		this.id = store_id;
	}
}
