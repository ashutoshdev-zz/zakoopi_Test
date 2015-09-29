package com.zakoopi.searchResult;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class StoreDetail {

	@SerializedName("stores_count")
	private String stores_count;
	@SerializedName("articles_count")
	private String articles_count;
	ArrayList<TopMarket> stores = new ArrayList<TopMarket>();
	ArrayList<TopExperiences> articles = new ArrayList<TopExperiences>();
	

	public String getStores_count() {
		return stores_count;
	}

	public void setStores_count(String stores_count) {
		this.stores_count = stores_count;
	}

	public String getArticles_count() {
		return articles_count;
	}

	public void setArticles_count(String articles_count) {
		this.articles_count = articles_count;
	}

	public ArrayList<TopMarket> getStores() {
		return stores;
	}

	public void setStores(ArrayList<TopMarket> stores) {
		this.stores = stores;
	}

	public ArrayList<TopExperiences> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<TopExperiences> articles) {
		this.articles = articles;
	}

}
