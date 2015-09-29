package com.zakoopi.helper;

public class TopMarketPojoDisPage {

	String market_name;
	String url_slug;
	String market_image;
	
	public TopMarketPojoDisPage(String market_name, String url_slug,
			String market_image) {
		super();
		this.market_name = market_name;
		this.url_slug = url_slug;
		this.market_image = market_image;
	}
	
	
	
	public String getMarket_name() {
		return market_name;
	}
	public void setMarket_name(String market_name) {
		this.market_name = market_name;
	}
	public String getUrl_slug() {
		return url_slug;
	}
	public void setUrl_slug(String url_slug) {
		this.url_slug = url_slug;
	}
	public String getMarket_image() {
		return market_image;
	}
	public void setMarket_image(String market_image) {
		this.market_image = market_image;
	}
	
}
