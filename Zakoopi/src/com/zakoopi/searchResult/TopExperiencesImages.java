package com.zakoopi.searchResult;

import com.google.gson.annotations.SerializedName;

public class TopExperiencesImages {

	@SerializedName("article_id")
	private String article_id;
	
	@SerializedName("android_api_img")
	private String android_api_img;
	
	@SerializedName("tiny_img")
	private String tiny_img;
	
	@SerializedName("medium_img")
	private String medium_img;
	
	@SerializedName("large_img")
	private String large_img;
	
	@SerializedName("medium_img_w")
	private String medium_img_w;
	
	public String getMedium_img_w() {
		return medium_img_w;
	}

	public void setMedium_img_w(String medium_img_w) {
		this.medium_img_w = medium_img_w;
	}

	public String getMedium_img_h() {
		return medium_img_h;
	}

	public void setMedium_img_h(String medium_img_h) {
		this.medium_img_h = medium_img_h;
	}

	@SerializedName("medium_img_h")
	private String medium_img_h;
	
	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}
	
	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}

	public String getTiny_img() {
		return tiny_img;
	}

	public void setTiny_img(String tiny_img) {
		this.tiny_img = tiny_img;
	}

	public String getMedium_img() {
		return medium_img;
	}

	public void setMedium_img(String medium_img) {
		this.medium_img = medium_img;
	}

	public String getLarge_img() {
		return large_img;
	}

	public void setLarge_img(String large_img) {
		this.large_img = large_img;
	}

	
}
