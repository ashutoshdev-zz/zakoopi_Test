package com.zakoopi.homefeed;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Popular_ArticleData {

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("hits_text")
	private String hits_text;
	
	@SerializedName("hits")
	private String hits;

	@SerializedName("likes_count")
	private String likes_count;

	ArrayList<Popular_Article_Images> article_images;
	Popular_Article_User user;

	@SerializedName("description")
	private String description;

	@SerializedName("is_liked")
	private String is_liked;
	
	@SerializedName("slug")
	private String slug;

	@SerializedName("is_new")
	private String is_new;
	
	@SerializedName("article_comment_count")
	private String article_comment_count;
	
	public String getHits_text() {
		return hits_text;
	}

	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	

	public String getArticle_comment_count() {
		return article_comment_count;
	}

	public void setArticle_comment_count(String article_comment_count) {
		this.article_comment_count = article_comment_count;
	}

	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsNew() {
		return is_new;
	}

	public void setIsNew(String is_new) {
		this.is_new = is_new;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}

	public ArrayList<Popular_Article_Images> getArticle_images() {
		return article_images;
	}

	public void setArticle_images(
			ArrayList<Popular_Article_Images> article_images) {
		this.article_images = article_images;
	}

	public Popular_Article_User getUser() {
		return user;
	}

	public void setUser(Popular_Article_User user) {
		this.user = user;
	}
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

}
