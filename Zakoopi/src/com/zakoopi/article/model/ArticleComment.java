package com.zakoopi.article.model;

import com.google.gson.annotations.SerializedName;

public class ArticleComment {

	@SerializedName("id")
	private String id;
	
	@SerializedName("article_id")
	private String article_id;
	
	@SerializedName("user_id")
	private String user_id;
	
	@SerializedName("comment")
	private String comment;
	
	ArticleCommentUser user;
	
	public ArticleCommentUser getUser() {
		return user;
	}

	public void setUser(ArticleCommentUser user) {
		this.user = user;
	}

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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
