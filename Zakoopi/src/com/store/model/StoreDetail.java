package com.store.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class StoreDetail {

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
	@SerializedName("city_name")
	private String city_name;
	@SerializedName("pin_code")
	private String pin_code;
	@SerializedName("store_timing_from")
	private String store_timing_from;
	@SerializedName("store_timing_to")
	private String store_timing_to;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longitude")
	private String longitude;
	@SerializedName("closed_day")
	private String closed_day;
	@SerializedName("facebook_link")
	private String facebook_link;
	@SerializedName("website")
	private String website;
	@SerializedName("twitter_link")
	private String twitter_link;
	@SerializedName("overall_ratings")
	private String overall_ratings;
	@SerializedName("google_plus_link")
	private String google_plus_link;
	@SerializedName("instagram_link")
	private String instagram_link;
	@SerializedName("likes_count")
	private String likes_count;
	@SerializedName("is_liked")
	private String is_liked;
	@SerializedName("rated_color")
	private String rated_color;
	@SerializedName("telephone")
	private String telephone;
	@SerializedName("slug")
	private String slug;
	@SerializedName("store_rating_count")
	private String store_rating_count;
	@SerializedName("is_followed")
	private String is_followed;
	ArrayList<ArticlestoresArray> article_stores;
	ArrayList<RelatedlookbookArrays> related_lookbooks;
	ArrayList<StoreImageArrays> store_images;
	ArrayList<StoreofferingArrays> store_offerings;
	ArrayList<StoreReviewArrays> store_reviews;
	ArrayList<StoreRatings> store_ratings;
	ArrayList<StoreCards> store_cards;
	
	public ArrayList<StoreCards> getStore_cards() {
		return store_cards;
	}

	public void setStore_cards(ArrayList<StoreCards> store_cards) {
		this.store_cards = store_cards;
	}

	public String getStore_rating_count() {
		return store_rating_count;
	}

	public void setStore_rating_count(String store_rating_count) {
		this.store_rating_count = store_rating_count;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	
	

	public String getIs_followed() {
		return is_followed;
	}

	public void setIs_followed(String is_followed) {
		this.is_followed = is_followed;
	}
	

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	

	public ArrayList<StoreRatings> getStore_ratings() {
		return store_ratings;
	}

	public void setStore_ratings(ArrayList<StoreRatings> store_ratings) {
		this.store_ratings = store_ratings;
	}

	public ArrayList<ArticlestoresArray> getArticle_stores() {
		return article_stores;
	}

	public void setArticle_stores(ArrayList<ArticlestoresArray> article_stores) {
		this.article_stores = article_stores;
	}

	public ArrayList<RelatedlookbookArrays> getRelated_lookbooks() {
		return related_lookbooks;
	}

	public void setRelated_lookbooks(
			ArrayList<RelatedlookbookArrays> related_lookbooks) {
		this.related_lookbooks = related_lookbooks;
	}

	public ArrayList<StoreImageArrays> getStore_images() {
		return store_images;
	}

	public void setStore_images(ArrayList<StoreImageArrays> store_images) {
		this.store_images = store_images;
	}

	public ArrayList<StoreofferingArrays> getStore_offerings() {
		return store_offerings;
	}

	public void setStore_offerings(ArrayList<StoreofferingArrays> store_offerings) {
		this.store_offerings = store_offerings;
	}

	public ArrayList<StoreReviewArrays> getStore_reviews() {
		return store_reviews;
	}

	public void setStore_reviews(ArrayList<StoreReviewArrays> store_reviews) {
		this.store_reviews = store_reviews;
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

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getPin_code() {
		return pin_code;
	}

	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}

	public String getStore_timing_from() {
		return store_timing_from;
	}

	public void setStore_timing_from(String store_timing_from) {
		this.store_timing_from = store_timing_from;
	}

	public String getStore_timing_to() {
		return store_timing_to;
	}

	public void setStore_timing_to(String store_timing_to) {
		this.store_timing_to = store_timing_to;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getClosed_day() {
		return closed_day;
	}

	public void setClosed_day(String closed_day) {
		this.closed_day = closed_day;
	}

	public String getFacebook_link() {
		return facebook_link;
	}

	public void setFacebook_link(String facebook_link) {
		this.facebook_link = facebook_link;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTwitter_link() {
		return twitter_link;
	}

	public void setTwitter_link(String twitter_link) {
		this.twitter_link = twitter_link;
	}

	public String getOverall_ratings() {
		return overall_ratings;
	}

	public void setOverall_ratings(String overall_ratings) {
		this.overall_ratings = overall_ratings;
	}

	public String getGoogle_plus_link() {
		return google_plus_link;
	}

	public void setGoogle_plus_link(String google_plus_link) {
		this.google_plus_link = google_plus_link;
	}

	public String getInstagram_link() {
		return instagram_link;
	}

	public void setInstagram_link(String instagram_link) {
		this.instagram_link = instagram_link;
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
	
	public String getRated_color() {
		return rated_color;
	}

	public void setRated_color(String rated_color) {
		this.rated_color = rated_color;
	}

}
