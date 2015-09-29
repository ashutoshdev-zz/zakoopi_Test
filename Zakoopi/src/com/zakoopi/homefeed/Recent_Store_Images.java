package com.zakoopi.homefeed;

import com.google.gson.annotations.SerializedName;

public class Recent_Store_Images {

	@SerializedName("id")
	private String id;

	@SerializedName("store_id")
	private String store_id;

	@SerializedName("android_api_img")
	private String android_api_img;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}

}
