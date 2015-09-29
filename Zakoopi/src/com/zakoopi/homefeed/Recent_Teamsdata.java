package com.zakoopi.homefeed;

import com.google.gson.annotations.SerializedName;

public class Recent_Teamsdata {

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("hits")
	private String hits;

	@SerializedName("likes_count")
	private String likes_count;

	@SerializedName("android_api_img")
	private String android_api_img;
	
	@SerializedName("slug")
	private String slug;


	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public String getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}

	
}
