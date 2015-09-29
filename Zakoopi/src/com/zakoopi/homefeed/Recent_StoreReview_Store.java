package com.zakoopi.homefeed;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Recent_StoreReview_Store {

	@SerializedName("id")
	private String id;

	@SerializedName("store_name")
	private String store_name;

	@SerializedName("store_address")
	private String store_address;

	@SerializedName("area")
	private String area;

	
	@SerializedName("market")
	private String market;

	@SerializedName("city")
	private String city;

	@SerializedName("overall_ratings")
	private String overall_ratings;

	@SerializedName("likes_count")
	private String likes_count;
	
	@SerializedName("slug")
	private String slug;
	
	@SerializedName("rated_color")
	private String rated_color;
	
   public String getRated_color() {
		return rated_color;
	}
	public void setRated_color(String rated_color) {
		this.rated_color = rated_color;
	}
public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
ArrayList<Recent_Store_Images>store_images;
	

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
	public String getStore_address() {
		return store_address;
	}
	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOverall_ratings() {
		return overall_ratings;
	}
	public void setOverall_ratings(String overall_ratings) {
		this.overall_ratings = overall_ratings;
	}
	public String getLikes_count() {
		return likes_count;
	}
	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}
	public ArrayList<Recent_Store_Images> getStore_images() {
		return store_images;
	}
	public void setStore_images(ArrayList<Recent_Store_Images> store_images) {
		this.store_images = store_images;
	}
}
