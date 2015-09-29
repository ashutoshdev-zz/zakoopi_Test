package com.zakoopi.lookbookView;

import com.google.gson.annotations.SerializedName;

public class RecentLookBookComment {

	@SerializedName("id")
	private String id;
	
	@SerializedName("lookbook_id")
	private String lookbook_id;
	
	@SerializedName("user_id")
	private String user_id;
	
	@SerializedName("comment")
	private String comment;
	
	RecentLookCommentUser user;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLookbook_id() {
		return lookbook_id;
	}

	public void setLookbook_id(String lookbook_id) {
		this.lookbook_id = lookbook_id;
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

	public RecentLookCommentUser getUser() {
		return user;
	}

	public void setUser(RecentLookCommentUser user) {
		this.user = user;
	}

	
}
