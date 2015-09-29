package com.zakoopi.searchResult;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.store.model.StoreCards;


public class TopMarket {

	@SerializedName("id")
	private String id;
	
	@SerializedName("store_name")
	private String store_name;
	
	@SerializedName("market_name")
	private String market_name;
	
	@SerializedName("sub_city")
	private String sub_city;
	
	@SerializedName("store_review_count")
	private String store_review_count;
	
	@SerializedName("overall_ratings")
	private String overall_ratings;
	
	@SerializedName("images_count")
	private String images_count;
	
	@SerializedName("lookbooks_count")
	private String lookbooks_count;
	
	@SerializedName("rated_color")
	private String rated_color;
	
	@SerializedName("is_followed")
	private String is_followed;
	
	ArrayList<TopMarketArticle> article_stores = new ArrayList<TopMarketArticle>();	
	ArrayList<StoreImagesModel> store_images = new ArrayList<StoreImagesModel>();
	ArrayList<StoreCards> store_cards = new ArrayList<StoreCards>();
	public ArrayList<StoreCards> getStore_cards() {
		return store_cards;
	}

	public void setStore_cards(ArrayList<StoreCards> store_cards) {
		this.store_cards = store_cards;
	}

	public ArrayList<StoreImagesModel> getStore_images() {
		return store_images;
	}

	public void setStore_images(ArrayList<StoreImagesModel> store_images) {
		this.store_images = store_images;
	}

	public ArrayList<StoreRelatedLookbooks> getRelated_lookbooks() {
		return related_lookbooks;
	}

	public void setRelated_lookbooks(
			ArrayList<StoreRelatedLookbooks> related_lookbooks) {
		this.related_lookbooks = related_lookbooks;
	}

	ArrayList<StoreRelatedLookbooks> related_lookbooks = new ArrayList<StoreRelatedLookbooks>();
	public ArrayList<TopMarketArticle> getArticle_stores() {
		return article_stores;
	}

	public void setArticle_stores(ArrayList<TopMarketArticle> article_stores) {
		this.article_stores = article_stores;
	}

	public String getMarket_name() {
		return market_name;
	}

	public void setMarket_name(String market_name) {
		this.market_name = market_name;
	}

	public String getSub_city() {
		return sub_city;
	}

	public void setSub_city(String sub_city) {
		this.sub_city = sub_city;
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

	public String getStore_review_count() {
		return store_review_count;
	}

	public void setStore_review_count(String store_review_count) {
		this.store_review_count = store_review_count;
	}

	public String getOverall_ratings() {
		return overall_ratings;
	}

	public void setOverall_ratings(String overall_ratings) {
		this.overall_ratings = overall_ratings;
	}

	public String getImages_count() {
		return images_count;
	}

	public void setImages_count(String images_count) {
		this.images_count = images_count;
	}

	public String getLookbooks_count() {
		return lookbooks_count;
	}

	public void setLookbooks_count(String lookbooks_count) {
		this.lookbooks_count = lookbooks_count;
	}

	public String getRated_color() {
		return rated_color;
	}

	public void setRated_color(String rated_color) {
		this.rated_color = rated_color;
	}

	public String getIs_followed() {
		return is_followed;
	}

	public void setIs_followed(String is_followed) {
		this.is_followed = is_followed;
	}
	
	
	
}
