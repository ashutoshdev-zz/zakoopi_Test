package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreofferingDetais {

	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
	@SerializedName("gender")
	private String gender;
	@SerializedName("category")
	private String category;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
