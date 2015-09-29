package com.zakoopi.article.model;

import com.google.gson.annotations.SerializedName;

public class ArticleImage {

	@SerializedName("android_api_img")
	private String android_api_img;

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}
}
