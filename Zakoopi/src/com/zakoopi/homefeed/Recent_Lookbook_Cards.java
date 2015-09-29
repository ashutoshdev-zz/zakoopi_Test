package com.zakoopi.homefeed;

import com.google.gson.annotations.SerializedName;

public class Recent_Lookbook_Cards {

	@SerializedName("id")
	private String id;

	@SerializedName("android_api_img")
	private String android_api_img;

	@SerializedName("lookbook_id")
	private String lookbook_id;

	@SerializedName("description")
	private String description;
	
	@SerializedName("tags")
	private String tags;
	
	
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

	public String getPhoto_path() {
		return android_api_img;
	}

	public void setPhoto_path(String android_api_img) {
		this.android_api_img = android_api_img;
	}

	public String getLookbook_id() {
		return lookbook_id;
	}

	public void setLookbook_id(String lookbook_id) {
		this.lookbook_id = lookbook_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getTags(){
		return tags;
	}
	
	public void setTags(String tags){
		this.tags = tags;
	}

}
