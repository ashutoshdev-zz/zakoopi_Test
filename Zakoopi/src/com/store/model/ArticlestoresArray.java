package com.store.model;

import com.google.gson.annotations.SerializedName;

public class ArticlestoresArray {

	@SerializedName("id")
	private String id;
	@SerializedName("article_id")
	private String article_id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("name")
	private String name;
	@SerializedName("review")
	private String review;
	ArticleDetails article;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public ArticleDetails getArticle() {
		return article;
	}

	public void setArticle(ArticleDetails article) {
		this.article = article;
	}

}
