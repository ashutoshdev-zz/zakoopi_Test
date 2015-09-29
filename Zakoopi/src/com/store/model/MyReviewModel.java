package com.store.model;

import com.google.gson.annotations.SerializedName;

public class MyReviewModel {

	@SerializedName("id")
	private String id;
	@SerializedName("user_id")
	private String user_id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("review")
	private String review;
	
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
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
}
