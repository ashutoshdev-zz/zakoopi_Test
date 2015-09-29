package com.zakoopi.userfeed.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.zakoopi.homefeed.Recent_Lookbook_Cards;

public class User_LookbookData {

	@SerializedName("id")
	private String id;
	
	@SerializedName("title")
	private String title;

	@SerializedName("slug")
	private String slug;

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("tags")
	private String tags;

	@SerializedName("lookbookcomment_count")
	private String lookbookcomment_count;

	@SerializedName("likes_count")
	private String likes_count;

	@SerializedName("hits_text")
	private String hits_text;

	@SerializedName("is_liked")
	private String is_liked;
	
	@SerializedName("view_count")
	private String view_count;

	Lookbook_User_Data user;
	
	public Lookbook_User_Data getUser() {
		return user;
	}

	public void setUser(Lookbook_User_Data user) {
		this.user = user;
	}

	ArrayList<User_lookbook_Cards> cards;

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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLookbookcomment_count() {
		return lookbookcomment_count;
	}

	public void setLookbookcomment_count(String lookbookcomment_count) {
		this.lookbookcomment_count = lookbookcomment_count;
	}

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

	public String getView_count() {
		return view_count;
	}

	public void setView_count(String view_count) {
		this.view_count = view_count;
	}

	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	public ArrayList<User_lookbook_Cards> getCards() {
		return cards;
	}

	public void setCards(ArrayList<User_lookbook_Cards> cards) {
		this.cards = cards;
	}
}
