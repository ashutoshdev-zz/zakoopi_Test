package com.zakoopi.location.model;

import com.google.gson.annotations.SerializedName;

public class ClientLocation {
	@SerializedName("city_name")
	private String city_name;
	
	@SerializedName("slug")
	private String slug;
	
	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	
	
	
}
