package com.zakoopi.user.model;

public class UserInfoPojo {

	String user_age;
	String user_city;
	String user_gender;
	String user_name;
	String url_img;
	String zakoopi_points;
	String lookbook_count;
	String like_count;
	String review_count;
	String fb_link;
	String twitter_link;
	String website_link;
	String lookbook_draft_count;
	String uid;

	public UserInfoPojo(String user_age, String user_city, String user_gender,
			String user_name, String url_img, String zakoopi_points,
			String lookbook_count, String like_count, String review_count,
			String fb_link, String twitter_link, String website_link,
			String lookbook_draft_count, String uid) {
		super();
		this.user_age = user_age;
		this.user_city = user_city;
		this.user_gender = user_gender;
		this.user_name = user_name;
		this.url_img = url_img;
		this.zakoopi_points = zakoopi_points;
		this.lookbook_count = lookbook_count;
		this.like_count = like_count;
		this.review_count = review_count;
		this.fb_link = fb_link;
		this.twitter_link = twitter_link;
		this.website_link = website_link;
		this.lookbook_draft_count = lookbook_draft_count;
		this.uid = uid;
	}

	public String getUser_age() {
		return user_age;
	}

	public void setUser_age(String user_age) {
		this.user_age = user_age;
	}

	public String getUser_city() {
		return user_city;
	}

	public void setUser_city(String user_city) {
		this.user_city = user_city;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUrl_img() {
		return url_img;
	}

	public void setUrl_img(String url_img) {
		this.url_img = url_img;
	}

	public String getZakoopi_points() {
		return zakoopi_points;
	}

	public void setZakoopi_points(String zakoopi_points) {
		this.zakoopi_points = zakoopi_points;
	}

	public String getLookbook_count() {
		return lookbook_count;
	}

	public void setLookbook_count(String lookbook_count) {
		this.lookbook_count = lookbook_count;
	}

	public String getLike_count() {
		return like_count;
	}

	public void setLike_count(String like_count) {
		this.like_count = like_count;
	}

	public String getReview_count() {
		return review_count;
	}

	public void setReview_count(String review_count) {
		this.review_count = review_count;
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

	public String getWebsite_link() {
		return website_link;
	}

	public void setWebsite_link(String website_link) {
		this.website_link = website_link;
	}

	public String getLookbook_draft_count() {
		return lookbook_draft_count;
	}

	public void setLookbook_draft_count(String lookbook_draft_count) {
		this.lookbook_draft_count = lookbook_draft_count;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
