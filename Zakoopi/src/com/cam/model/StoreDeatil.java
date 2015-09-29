package com.cam.model;

import com.google.gson.annotations.SerializedName;

public class StoreDeatil {

	@SerializedName("id")
	private String id;
	@SerializedName("store_name")
	private String store_name;

	@SerializedName("slug")
	private String slug;

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
		return slug;
	}

	public void setMarket(String market) {
		this.slug = market;
	}

}
