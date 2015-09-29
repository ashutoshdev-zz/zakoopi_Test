package com.zakoopi.userfeed.model;

import com.google.gson.annotations.SerializedName;

public class UserStoreData {

	@SerializedName("id")
	private String id;
	
	@SerializedName("store_name")
	private String store_name;
	
	@SerializedName("slug")
	private String slug;
	
	@SerializedName("store_address")
	private String store_address;
	
	@SerializedName("area")
	private String area;
	
	@SerializedName("market")
	private String market;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getStore_address() {
		return store_address;
	}

	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}
}
