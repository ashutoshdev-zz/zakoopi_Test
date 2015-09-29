package com.zakoopi.homefeed;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class recentfeed implements Comparable<recentfeed>{

	@SerializedName("id")
	private String id;

	@SerializedName("key")
	private String key;

	@SerializedName("model")
	private String model;

	@SerializedName("order")
	private String order;

	Recent_ArticleData articleData;
	Recent_Lookbookdata lookbookData;
	Recent_StoreReviewData storeReviewData;
	Recent_Teamsdata teamData;

	public String get_id() {
		return id;
	}

	public void set_id(String _id) {
		this.id = _id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
		Log.e("jjjjjjmodels", this.model);
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Recent_ArticleData getArticaldata() {
		return articleData;
	}

	public void setArticaldata(Recent_ArticleData articaldata) {
		this.articleData = articaldata;
	}

	public Recent_Lookbookdata getLookbookdata() {
		return lookbookData;
	}

	public void setLookbookdata(Recent_Lookbookdata lookbookdata) {
		this.lookbookData = lookbookdata;
	}

	public Recent_StoreReviewData getStorereviewdata() {
		return storeReviewData;
	}

	public void setStorereviewdata(Recent_StoreReviewData storereviewdata) {
		this.storeReviewData = storereviewdata;
	}

	public Recent_Teamsdata getTeamsdata() {
		return teamData;
	}

	public void setTeamsdata(Recent_Teamsdata teamsdata) {
		this.teamData = teamsdata;
	}

	@Override
	public int compareTo(recentfeed feed) {
		// TODO Auto-generated method stub
		return feed.get_id().compareTo(id);
	}

}

