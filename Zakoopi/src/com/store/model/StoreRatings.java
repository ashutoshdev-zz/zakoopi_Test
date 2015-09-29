package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreRatings {

	@SerializedName("id")
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@SerializedName("user_id")
	private String user_id;
	
	@SerializedName("store_id")
	private String store_id;
	
	@SerializedName("rating")
	private String rating;
}
