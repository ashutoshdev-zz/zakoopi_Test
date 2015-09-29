package com.zakoopi.lookbookView;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.zakoopi.homefeed.Recent_Lookbook_Cards;
import com.zakoopi.user.model.UserDetails;

public class LookbookRecent {

	@SerializedName("id")
	String id;
	@SerializedName("title")
	String title;
	@SerializedName("lookbookcomment_count")
	String lookbookcomment_count;
	@SerializedName("hits_text")
	String hits_text;
	@SerializedName("likes_count")
	String likes_count;
	@SerializedName("is_liked")
	String is_liked;
	@SerializedName("slug")
	String slug;
	

	@SerializedName("user_id")
	String user_id;

	UserDetails user;
	// User user;
	ArrayList<RecentLookBookCardShow> cards;

	ArrayList<RecentLookBookComment> lookbookcomments;

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

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
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

	public String getLookbookcomment_count() {
		return lookbookcomment_count;
	}

	public void setLookbookcomment_count(String lookbookcomment_count) {
		this.lookbookcomment_count = lookbookcomment_count;
	}

	public String getHits_text() {
		return hits_text;
	}

	public void setHits_text(String hits_text) {
		this.hits_text = hits_text;
	}

	public String getLikes_count() {
		return likes_count;
	}

	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}

	public ArrayList<RecentLookBookCardShow> getCards() {
		return cards;
	}

	public void setCards(ArrayList<RecentLookBookCardShow> cards) {
		this.cards = cards;
	}

	public ArrayList<RecentLookBookComment> getLookbookcomments() {
		return lookbookcomments;
	}

	public void setLookbookcomments(
			ArrayList<RecentLookBookComment> lookbookcomments) {
		this.lookbookcomments = lookbookcomments;
	}

	public ArrayList<RecentLookBookCardShow> getRecentCards() {
		return cards;
	}

	public void setRecentCards(ArrayList<RecentLookBookCardShow> cards) {
		this.cards = cards;
	}
}
