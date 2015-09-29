package com.zakoopi.search;

import com.google.gson.annotations.SerializedName;

public class marketsTop implements Comparable<marketsTop>{

	@SerializedName("id")
	private String id;
	
	@SerializedName("market_name")
	private String market_name;
	
	@SerializedName("android_api_image")
	private String android_api_image;
	
	@SerializedName("url_slug")
	private String url_slug;
	
	
	public String getUrl_slug() {
		return url_slug;
	}

	public void setUrl_slug(String url_slug) {
		this.url_slug = url_slug;
	}

	public String get_id(){
		return id;
	}
	
	public void set_id(String _id){
		this.id = _id;
	}
	
	public String get_market_name(){
		return market_name;
	}
	
	public void set_market_name(String _market_name){
		this.market_name = _market_name;
	}
	
	public String get_android_api_image(){
		return android_api_image;
	}
	
	public void set_android_api_image(String _android_api_image){
		this.android_api_image = _android_api_image;
	}
	
	@Override
	public int compareTo(marketsTop market) {
		// TODO Auto-generated method stub
		return market.get_id().compareTo(id);
	}

}
