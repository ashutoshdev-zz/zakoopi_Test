package com.zakoopi.search;

import com.google.gson.annotations.SerializedName;

public class trendsTop implements Comparable<trendsTop>{

	@SerializedName("id")
	private String id;
	
	@SerializedName("trend_name")
	private String trend_name;
	
	@SerializedName("android_api_image")
	private String android_api_image;
	
	@SerializedName("slug")
	private String slug;
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String get_id(){
		return id;
	}
	
	public void set_id(String _id){
		this.id = _id;
	}
	
	public String get_trend_name(){
		return trend_name;
	}
	
	public void set_trend_name(String _trend_name){
		this.trend_name = _trend_name;
	}
	
	public String get_android_api_image(){
		return android_api_image;
	}
	
	public void set_android_api_image(String _android_api_image){
		this.android_api_image = _android_api_image;
	}
	
	@Override
	public int compareTo(trendsTop trends) {
		
		return trends.get_id().compareTo(id);
	}

}
