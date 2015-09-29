package com.zakoopi.userfeed.model;

import com.google.gson.annotations.SerializedName;

public class UserFeedPojo {

	@SerializedName("lookimg")
	private String lookimg;
	@SerializedName("img1")
	private String img1;
	@SerializedName("img2")
	private String img2;
	@SerializedName("likes")
	private String likes;
	@SerializedName("hits")
	private String hits;
	@SerializedName("hits_text")
	private String hits_text;
	@SerializedName("view_count")
	private String view_count;
	@SerializedName("title")
	private String title;
	@SerializedName("mode")
	private String mode;
	@SerializedName("store_name")
	private String store_name;
	@SerializedName("store_location")
	private String store_location;
	@SerializedName("review")
	private String review;
	@SerializedName("image_count")
	private String image_count;
	@SerializedName("idd")
	private String idd;
	@SerializedName("description")
	private String description;
	@SerializedName("is_liked")
	private String is_liked;
	@SerializedName("slug")
	private String slug;
	@SerializedName("rated_color")
	private String rated_color;
	@SerializedName("rated")
	private String rated;
	@SerializedName("isnew")
	private String isnew;
	@SerializedName("comment_count")
	private String comment_count;
	@SerializedName("medium_img_w")
	private String medium_img_w;
	@SerializedName("medium_img_h")
	private String medium_img_h;
	@SerializedName("user_name")
	private String user_name;
	@SerializedName("user_img")
	private String user_img;
	@SerializedName("user_id")
	private String user_id;
	@SerializedName("status")
	private String status;
	@SerializedName("store_id")
	private String store_id;
	
	public UserFeedPojo(String mode, String lookimg, String img1, String img2, String likes, String hits, String title, 
			 String store_name, String store_location, String review, String image_count, 
			String idd, String description, String is_liked, String slug, String rated_color,
			String rated, String isnew,String comment_count, String medium_img_w, String medium_img_h
			,String user_name,String user_img,String user_id, String hits_text, String view_count,String status,String store_id) {
				super();
				
				this.lookimg = lookimg;
				this.img1 = img1;
				this.img2 = img2;
				this.likes = likes;
				this.hits = hits;
				this.title = title;
				this.mode = mode;
				this.store_name = store_name;
				this.store_location = store_location;
				this.review = review;
				this.image_count = image_count;
				this.idd = idd;
				this.description = description;
				this.is_liked = is_liked;
				this.slug = slug;
				this.rated_color = rated_color; 
				this.rated= rated;
				this.isnew = isnew;
				this.comment_count = comment_count;
				this.medium_img_w = medium_img_w;
				this.medium_img_h = medium_img_h;
				this.user_name = user_name;
				this.user_img = user_img;
				this.user_id = user_id;
				this.hits_text = hits_text; 
				this.view_count = view_count;
				this.status = status;
				this.store_id = store_id;
			}
	
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLookimg() {
		return lookimg;
	}
	public void setLookimg(String lookimg) {
		this.lookimg = lookimg;
	}
	
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	
	public String getHits() {
		return hits;
	}
	public void setHits(String hits) {
		this.hits = hits;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	
	public String getStore_location() {
		return store_location;
	}
	public void setStore_location(String store_location) {
		this.store_location = store_location;
	}
	
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
	public String getImage_count() {
		return image_count;
	}
	public void setImage_count(String image_count) {
		this.image_count = image_count;
	}
	
	public String getIdd() {
		return idd;
	}
	public void setIdd(String idd) {
		this.idd = idd;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIs_liked() {
		return is_liked;
	}
	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}
	
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public String getRated_color() {
		return rated_color;
	}
	public void setRated_color(String rated_color) {
		this.rated_color = rated_color;
	}
	
	public String getRated() {
		return rated;
	}
	public void setRated(String rated) {
		this.rated = rated;
	}
	
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	
	public String getComment_count() {
		return comment_count;
	}
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getMedium_img_w() {
		return medium_img_w;
	}
	public void setMedium_img_w(String medium_img_w) {
		this.medium_img_w = medium_img_w;
	}

	public String getMedium_img_h() {
		return medium_img_h;
	}
	public void setMedium_img_h(String medium_img_h) {
		this.medium_img_h = medium_img_h;
	}
	
	public String getHits_text() {
		return hits_text;
	}
	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
	}

	public String getView_count() {
		return view_count;
	}
	public void setView_count(String view_count) {
		this.view_count = view_count;
	}
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStore_id() {
		return store_id;
	}


	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
}
