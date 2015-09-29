package com.zakoopi.article.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Article {

	@SerializedName("id")
	private String id;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("slug")
	private String slug;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("is_new")
	private String is_new;
	
	@SerializedName("likes_count")
	private String likes_count;
	
	@SerializedName("is_liked")
	private String is_liked;
	
	@SerializedName("hits_text")
	private String hits_text;
	
	@SerializedName("article_comment_count")
	private String article_comment_count;
	
	ArrayList<ArticleImage> article_images = new ArrayList<ArticleImage>();
	ArrayList<ArticleComment> article_comments = new ArrayList<ArticleComment>();
	ArticleUser user = new ArticleUser();
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}

	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
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

	public ArrayList<ArticleComment> getArticle_comments() {
		return article_comments;
	}

	public void setArticle_comments(ArrayList<ArticleComment> article_comments) {
		this.article_comments = article_comments;
	}

	public ArrayList<ArticleImage> getArticle() {
		return article_images;
	}

	public void setArticle(ArrayList<ArticleImage> article) {
		this.article_images = article;
	}
	
	
}
