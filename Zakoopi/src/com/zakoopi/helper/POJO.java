package com.zakoopi.helper;


public class POJO {

	String username;
	String userimg;
	String lookimg;
	String img1;
	String img2;
	String likes;
	String hits;
	String title;
	String mode;
	String store_name;
	String store_location;
	String store_rate;
	String review;
	String image_count;
	String idd;
	String description;
	String is_new;
	String is_liked;
	String slug;
	String rated_color;
	String rated;
	String userid;
	String comment_count;
	String img_width;
	String img_height;
	String feed_id;
	String status;
	String hits_count;
	String store_id;
	
	public POJO(String mode, String username, String userimg, String lookimg,
			String img1, String img2, String likes, String hits, String title,
			String store_name, String store_location, String store_rate,
			String review, String image_count, String idd, String description,
			String is_new, String is_liked, String slug, String rated_color, 
			String rated, String userid,String comment_count, String img_width, 
			String img_height,String feed_id, String status, String hits_count,
			String store_id) {
		super();
		//,String status
		this.username = username;
		this.userimg = userimg;
		this.lookimg = lookimg;
		this.img1 = img1;
		this.img2 = img2;
		this.likes = likes;
		this.hits = hits;
		this.title = title;
		this.mode = mode;
		this.store_name = store_name;
		this.store_location = store_location;
		this.store_rate = store_rate;
		this.review = review;
		this.image_count = image_count;
		this.idd = idd;
		this.description = description;
		this.is_new = is_new;
		this.is_liked = is_liked;
		this.slug = slug;
		this.rated_color = rated_color;
		this.rated = rated;
		this.userid = userid;
		this.comment_count = comment_count;
		this.img_width = img_width;
		this.img_height = img_height;
		this.feed_id = feed_id;
		this.status = status;
		this.hits_count = hits_count;
		this.store_id = store_id;
	}
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feed_id) {
		this.feed_id = feed_id;
	}

	public String getImg_width() {
		return img_width;
	}

	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}

	public String getImg_height() {
		return img_height;
	}

	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}

	
	
	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public String getRated_color() {
		return rated_color;
	}

	public void setRated_color(String rated_color) {
		this.rated_color = rated_color;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getIs_new() {
		return is_new;
	}

	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}

	public String getis_liked() {
		return is_liked;
	}

	public void setis_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	public String getIdd() {
		return idd;
	}

	public void setIdd(String idd) {
		this.idd = idd;
	}

	

	public String getImage_count() {
		return image_count;
	}

	public void setImage_count(String image_count) {
		this.image_count = image_count;
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

	public String getStore_rate() {
		return store_rate;
	}

	public void setStore_rate(String store_rate) {
		this.store_rate = store_rate;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIsnew() {
		return is_new;
	}

	public void setIsnew(String is_new) {
		this.is_new = is_new;
	}
	
	public String getHits_count() {
		return hits_count;
	}


	public void setHits_count(String hits_count) {
		this.hits_count = hits_count;
	}
	
	public String getStore_id() {
		return store_id;
	}


	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

}
