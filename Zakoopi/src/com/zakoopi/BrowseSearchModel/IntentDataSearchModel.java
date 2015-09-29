package com.zakoopi.BrowseSearchModel;

import com.google.gson.annotations.SerializedName;

public class IntentDataSearchModel {

	@SerializedName("entity")
	String entity;
	@SerializedName("id")
	String id;
	@SerializedName("slug")
	String slug;
	@SerializedName("code")
	String code;
	
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
