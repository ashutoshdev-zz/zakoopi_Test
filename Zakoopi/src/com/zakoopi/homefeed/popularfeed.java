package com.zakoopi.homefeed;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class popularfeed implements Comparable<popularfeed>{

	@SerializedName("id")
	private String id;

	@SerializedName("key")
	private String key;

	@SerializedName("model")
	private String model;

	@SerializedName("order")
	private String order;

	Popular_ArticleData articleData;
	Popular_Lookbookdata lookbookData;
	Popular_StoreReviewData storeReviewData;
	Popular_Teamsdata teamData;

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

	public Popular_ArticleData getArticaldata() {
		return articleData;
	}

	public void setArticaldata(Popular_ArticleData articaldata) {
		this.articleData = articaldata;
	}

	public Popular_Lookbookdata getLookbookdata() {
		return lookbookData;
	}

	public void setLookbookdata(Popular_Lookbookdata lookbookdata) {
		this.lookbookData = lookbookdata;
	}

	public Popular_StoreReviewData getStorereviewdata() {
		return storeReviewData;
	}

	public void setStorereviewdata(Popular_StoreReviewData storereviewdata) {
		this.storeReviewData = storereviewdata;
	}

	public Popular_Teamsdata getTeamsdata() {
		return teamData;
	}

	public void setTeamsdata(Popular_Teamsdata teamsdata) {
		this.teamData = teamsdata;
	}

	@Override
	public int compareTo(popularfeed feed) {
		// TODO Auto-generated method stub
		return feed.get_id().compareTo(id);
	}

}
