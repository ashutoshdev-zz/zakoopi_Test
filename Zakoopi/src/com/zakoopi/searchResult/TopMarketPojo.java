package com.zakoopi.searchResult;

public class TopMarketPojo {

	
	String id;
	String store_name;
	String market_name;
	String store_image;
	public String getStore_image() {
		return store_image;
	}

	public void setStore_image(String store_image) {
		this.store_image = store_image;
	}

	public String getStore_featured_count() {
		return store_featured_count;
	}

	public void setStore_featured_count(String store_featured_count) {
		this.store_featured_count = store_featured_count;
	}
	String store_featured_count;
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
	String sub_city;
	String store_review_count;
	String overall_ratings;
	String images_count;
	String lookbooks_count;
	String rated_color;
	String is_followed;
	
	public TopMarketPojo(String id,
	String store_name,
	String market_name,
	String sub_city,
	String store_review_count,
	String overall_ratings,
	String images_count,
	String lookbooks_count,
	String rated_color,
	String is_followed,
	String store_image,
	String store_featured_count) {
		super();
		
		this.id = id;
		this.store_name = store_name;
		this.market_name =market_name;
		this.sub_city = sub_city;
		this.store_review_count = store_review_count;
		this.overall_ratings = overall_ratings;
		this.images_count = images_count;
		this.lookbooks_count = lookbooks_count;
		this.rated_color = rated_color;
		this.is_followed = is_followed;
		this.store_image = store_image;
		this.store_featured_count = store_featured_count;
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
