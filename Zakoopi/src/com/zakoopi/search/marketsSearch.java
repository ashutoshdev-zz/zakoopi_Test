package com.zakoopi.search;

import com.google.gson.annotations.SerializedName;

public class marketsSearch implements Comparable<marketsSearch>{

	@SerializedName("id")
	private String id;
	
	@SerializedName("market_name")
	private String market_name;
	
	@SerializedName("url_slug")
	private String url_slug;
	
	
	public String getUrl_slug() {
		return url_slug;
	}

	public void setUrl_slug(String url_slug) {
		this.url_slug = url_slug;
	}
	
	public String get_id() {
		return id;
	}
	
	public void set_id(String _id) {
		this.id = _id;
	}
	
	public String get_market_name(){
		return market_name;
	}
	
	public void set_name(String _market_name){
		this.market_name = _market_name;
	}
	
	
	@Override
	public int compareTo(marketsSearch offer) {
		// TODO Auto-generated method stub
		return offer.get_id().compareTo(id);
	}

}
