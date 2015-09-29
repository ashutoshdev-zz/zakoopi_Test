package com.store.model;

public class StoreLookbookPojo {

	String mode;
	String username;
	String userimg;
	String lookimg;
	String img1;
	String img2;
	String likes_count;
	String hits;
	String title;
	String image_count;
	String mode_id;
	String is_new;
	String is_liked;
	String slug;
	String userid;
	String comment_count;
	String description;
	String medium_img_w;
	String hits_text;
	
	public String getHits_text() {
		return hits_text;
	}

	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
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

	String medium_img_h;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StoreLookbookPojo(String mode,String username, String userimg, String lookimg,
			String img1, String img2, String likes_count, String hits,
			String title, String image_count, String mode_id,
			String is_new, String is_liked, String slug,
			String userid, String comment_count,String description, String medium_img_w,
	String medium_img_h,String hits_text) {
		super();
		this.mode = mode;
		this.username = username;
		this.userimg = userimg;
		this.lookimg = lookimg;
		this.img1 = img1;
		this.img2 = img2;
		this.likes_count = likes_count;
		this.hits = hits;
		this.title = title;
		this.image_count = image_count;
		this.mode_id = mode_id;
		this.is_new = is_new;
		this.is_liked = is_liked;
		this.slug = slug;
		this.userid = userid;
		this.comment_count = comment_count;
		this.description = description;
		this.medium_img_w = medium_img_w;
		this.medium_img_h = medium_img_h;
		this.hits_text = hits_text;
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

	public String getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
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

	public String getImage_count() {
		return image_count;
	}

	public void setImage_count(String image_count) {
		this.image_count = image_count;
	}

	public String getMode_id() {
		return mode_id;
	}

	public void setMode_id(String mode_id) {
		this.mode_id = mode_id;
	}

	

	public String getIs_new() {
		return is_new;
	}

	public void setIs_new(String is_new) {
		this.is_new = is_new;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	
	
	
	
	
}
