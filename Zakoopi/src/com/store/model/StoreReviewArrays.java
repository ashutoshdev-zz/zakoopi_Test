package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreReviewArrays {

	@SerializedName("id")
	private String id;
	@SerializedName("user_id")
	private String user_id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("review")
	private String review;
	@SerializedName("hits_text")
	private String hits_text;
	@SerializedName("hits")
	private String hits;
	public String getHits_text() {
		return hits_text;
	}
	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
	}
	public String getHits() {
		return hits;
	}
	public void setHits(String hits) {
		this.hits = hits;
	}
	@SerializedName("likes_count")
	private String likes_count;
	@SerializedName("is_liked")
	private String is_liked;
	@SerializedName("rated")
	private String rated;
	@SerializedName("rated_color")
	private String rated_color; 
	@SerializedName("status")
	private String status; 
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRated_color() {
		return rated_color;
	}
	public void setRated_color(String rated_color) {
		this.rated_color = rated_color;
	}
	StoreReviewUsers user;
	
	public String getRated() {
		return rated;
	}
	public void setRated(String rated) {
		this.rated = rated;
	}
	
	
	public StoreReviewUsers getUser() {
		return user;
	}
	public void setUser(StoreReviewUsers user) {
		this.user = user;
	}
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

	public String getLikes_count() {
		return likes_count;
	}
	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}
	public String getIs_liked() {
		return is_liked;
	}
	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}
}
