package com.store.model;

import com.google.gson.annotations.SerializedName;

public class ArticleCoverImages {

	@SerializedName("article_id")
	private String article_id;
	@SerializedName("title")
	private String title;
	@SerializedName("medium_img")
	private String medium_img;
	@SerializedName("medium_img_w")
	private String medium_img_w;
	@SerializedName("medium_img_h")
	private String medium_img_h;
	
	public String getMedium_img() {
		return medium_img;
	}

	public void setMedium_img(String medium_img) {
		this.medium_img = medium_img;
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



	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
