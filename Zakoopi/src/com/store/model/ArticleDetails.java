package com.store.model;

import com.google.gson.annotations.SerializedName;

public class ArticleDetails {

	@SerializedName("title")
	private String title;
	@SerializedName("likes_count")
	private String likes_count;
	@SerializedName("is_liked")
	private String is_liked;
	@SerializedName("id")
	private String id;
	@SerializedName("slug")
	private String slug;
	@SerializedName("description")
	private String description;
	@SerializedName("is_new")
	private String is_new;
	@SerializedName("hits_text")
	private String hits_text;
	@SerializedName("article_comment_count")
	private String article_comment_count;
	@SerializedName("hits")
	private String hits;
	
	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	ArticleCoverImages cover_img;
	ArticleUser user;
	public ArticleCoverImages getCover_img() {
		return cover_img;
	}

	public void setCover_img(ArticleCoverImages cover_img) {
		this.cover_img = cover_img;
	}

	
	
	public ArticleUser getUser() {
		return user;
	}

	public void setUser(ArticleUser user) {
		this.user = user;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIs_new() {
		return is_new;
	}

	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}

	public String getHits_text() {
		return hits_text;
	}

	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
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
}
