package com.zakoopi.user.model;

import com.google.gson.annotations.SerializedName;

public class UserDetails {

	@SerializedName("id")
	private String id;
	
	@SerializedName("email")
	private String email;
	
	@SerializedName("first_name")
	private String first_name;
	
	@SerializedName("last_name")
	private String last_name;
	
	@SerializedName("location")
	private String location;
	
	@SerializedName("uid")
	private String uid;
	
	@SerializedName("age")
	private String age;
	
	@SerializedName("gender")
	private String gender;
	
	@SerializedName("fb_link")
	private String fb_link;
	
	@SerializedName("twitter_link")
	private String twitter_link;
	
	@SerializedName("other_website")
	private String other_website;
	
	@SerializedName("points")
	private String points;
	
	@SerializedName("lookbook_draft_count")
	private String lookbook_draft_count;
	
	public String getLookbook_draft_count() {
		return lookbook_draft_count;
	}

	public void setLookbook_draft_count(String lookbook_draft_count) {
		this.lookbook_draft_count = lookbook_draft_count;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	@SerializedName("pro_review_count")
	private String pro_review_count;
	
	@SerializedName("pro_likes_count")
	private String pro_likes_count;
	
	@SerializedName("android_api_img")
	private String android_api_img;
	
	@SerializedName("lookbook_active_count")
	private String lookbook_active_count;
	
	@SerializedName("about")
	private String about;
	
	
	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	public String getLookbook_active_count() {
		return lookbook_active_count;
	}

	public void setLookbook_active_count(String lookbook_active_count) {
		this.lookbook_active_count = lookbook_active_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFb_link() {
		return fb_link;
	}

	public void setFb_link(String fb_link) {
		this.fb_link = fb_link;
	}

	public String getTwitter_link() {
		return twitter_link;
	}

	public void setTwitter_link(String twitter_link) {
		this.twitter_link = twitter_link;
	}

	public String getOther_website() {
		return other_website;
	}

	public void setOther_website(String other_website) {
		this.other_website = other_website;
	}

	

	public String getPro_review_count() {
		return pro_review_count;
	}

	public void setPro_review_count(String pro_review_count) {
		this.pro_review_count = pro_review_count;
	}

	public String getPro_likes_count() {
		return pro_likes_count;
	}

	public void setPro_likes_count(String pro_likes_count) {
		this.pro_likes_count = pro_likes_count;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}

	
}
