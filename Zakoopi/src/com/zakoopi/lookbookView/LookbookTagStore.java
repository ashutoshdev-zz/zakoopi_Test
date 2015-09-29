package com.zakoopi.lookbookView;

import com.google.gson.annotations.SerializedName;

public class LookbookTagStore {

	@SerializedName("id")
	private String id;
	
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

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	@SerializedName("store_name")
	private String store_name;
	
	@SerializedName("market")
	private String market;
}
