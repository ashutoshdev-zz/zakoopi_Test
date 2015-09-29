package com.zakoopi.searchstore.model;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

	@SerializedName("id")
	private String id;
	@SerializedName("store_name")
	private String store_name;
	@SerializedName("market_name")
	private String market_name;
	
	@SerializedName("slug")
	private String slug;
	
	
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
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
	public String getMarket_name() {
		return market_name;
	}
	public void setMarket_name(String market_name) {
		this.market_name = market_name;
	}
	
}
