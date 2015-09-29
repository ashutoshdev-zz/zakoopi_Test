package com.zakoopi.lookbookView;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class RecentLookBookCardShow {

	@SerializedName("id")
	private String id;
	
	@SerializedName("lookbook_id")
	private String lookbook_id;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("tags")
	private String tags;
	
	@SerializedName("android_api_img")
	private String android_api_img;
	
	@SerializedName("photo_path")
	private String photo_path;
	
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
	
	public String getPhoto_path() {
		return photo_path;
	}

	public void setPhoto_path(String photo_path) {
		this.photo_path = photo_path;
	}

	ArrayList<LookbookTagStore> stores;
	
	public ArrayList<LookbookTagStore> getStores() {
		return stores;
	}

	public void setStores(ArrayList<LookbookTagStore> stores) {
		this.stores = stores;
	}

	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getLookbookId(){
		return lookbook_id;
	}
	
	public void setLookbookId(String lookbook_id){
		this.lookbook_id = lookbook_id;
	}
	
	public String getdescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getTags(){
		return tags;
	}
	
	public void setTags(String tags){
		this.tags = tags;
	}
	
	public String getPic(){
		return android_api_img;
	}
	
	public void setPic(String android_api_img){
		this.android_api_img = android_api_img;
	}
}
