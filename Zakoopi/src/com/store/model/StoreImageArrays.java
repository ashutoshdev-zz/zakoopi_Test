package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreImageArrays {

	@SerializedName("id")
	private String id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("desc")
	private String desc;
	@SerializedName("android_api_img")
	private String android_api_img;
	
	@SerializedName("tiny_img")
	private String tiny_img;
	@SerializedName("medium_img")
	private String medium_img;
	@SerializedName("large_img")
	private String large_img;
	
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}
}
