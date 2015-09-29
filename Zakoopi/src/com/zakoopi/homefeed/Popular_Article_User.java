package com.zakoopi.homefeed;

import com.google.gson.annotations.SerializedName;

public class Popular_Article_User {

	@SerializedName("id")
	private String id;

	@SerializedName("first_name")
	private String first_name;

	@SerializedName("last_name")
	private String last_name;

	@SerializedName("android_api_img")
	private String android_api_img;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}

}
