package com.zakoopi.searchResult;

import com.google.gson.annotations.SerializedName;

public class TopMarketArticle {

	@SerializedName("article_id")
	private String article_id;
	
	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SerializedName("store_id")
	private String store_id;
	
	@SerializedName("name")
	private String name;
}
