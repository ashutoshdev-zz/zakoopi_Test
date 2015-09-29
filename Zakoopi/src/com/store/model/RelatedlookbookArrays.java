package com.store.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class RelatedlookbookArrays {

	@SerializedName("id")
	private String id;
	@SerializedName("title")
	private String title;
	@SerializedName("lookbookcomment_count")
	private String lookbookcomment_count;
	@SerializedName("likes_count")
	private String likes_count;
	@SerializedName("hits_text")
	private String hits_text;
	@SerializedName("slug")
	private String slug;
	@SerializedName("user_id")
	private String user_id;
	@SerializedName("is_liked")
	private String is_liked;
	@SerializedName("view_count")
	private String view_count;
	
	public String getView_count() {
		return view_count;
	}

	public void setView_count(String view_count) {
		this.view_count = view_count;
	}

	LookbookUser user;
	ArrayList<LookbookCards> cards;
	
	public String getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}

	public String getHits_text() {
		return hits_text;
	}

	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	

	public ArrayList<LookbookCards> getCards() {
		return cards;
	}

	public void setCards(ArrayList<LookbookCards> cards) {
		this.cards = cards;
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

	public String getLookbookcomment_count() {
		return lookbookcomment_count;
	}

	public void setLookbookcomment_count(String lookbookcomment_count) {
		this.lookbookcomment_count = lookbookcomment_count;
	}

	


	public LookbookUser getUser() {
		return user;
	}

	public void setUser(LookbookUser user) {
		this.user = user;
	}

	
}
