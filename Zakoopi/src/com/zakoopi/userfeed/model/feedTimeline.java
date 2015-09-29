package com.zakoopi.userfeed.model;

import com.google.gson.annotations.SerializedName;

public class feedTimeline {

	@SerializedName("id")
	private String id;
	
	@SerializedName("key")
	private String key;
	
	@SerializedName("model")
	private String model;
	
	@SerializedName("user_id")
	private String user_id;
	
	User_LookbookData LookbooksData;
	public User_LookbookData getLookbooksData() {
		return LookbooksData;
	}

	public void setLookbooksData(User_LookbookData lookbooksData) {
		LookbooksData = lookbooksData;
	}

	public UserStoreReviewData getStoreReviewsData() {
		return StoreReviewsData;
	}

	public void setStoreReviewsData(UserStoreReviewData storeReviewsData) {
		StoreReviewsData = storeReviewsData;
	}

	public UserArticleData getArticlesData() {
		return ArticlesData;
	}

	public void setArticlesData(UserArticleData articlesData) {
		ArticlesData = articlesData;
	}

	UserStoreReviewData StoreReviewsData;
	UserArticleData ArticlesData;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
}
