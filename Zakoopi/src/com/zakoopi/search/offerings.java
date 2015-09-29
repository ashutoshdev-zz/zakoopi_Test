package com.zakoopi.search;

import com.google.gson.annotations.SerializedName;

public class offerings implements Comparable<offerings>{

	@SerializedName("id")
	private String id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("category")
	private String category;
	
	@SerializedName("slug")
	private String slug;
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String get_id() {
		return id;
	}
	
	public void set_id(String _id) {
		this.id = _id;
	}
	
	public String get_name(){
		return name;
	}
	
	public void set_name(String _name){
		this.name = _name;
	}
	
	public String get_category() {
		return category;
	}
	
	public void set_category(String _category) {
		this.category = _category;
	}
	
	@Override
	public int compareTo(offerings offer) {
		// TODO Auto-generated method stub
		return offer.get_id().compareTo(id);
	}

}
