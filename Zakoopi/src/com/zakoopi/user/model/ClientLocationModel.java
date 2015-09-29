package com.zakoopi.user.model;

import com.google.gson.annotations.SerializedName;

public class ClientLocationModel {

	@SerializedName("id")
	String id;
	@SerializedName("city_name")
	String city_name;
	@SerializedName("slug")
	String slug;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
