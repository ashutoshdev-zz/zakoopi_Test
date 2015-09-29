package com.zakoopi.about.model;

import com.google.gson.annotations.SerializedName;

public class AboutDetailModel {
	@SerializedName("content")
	String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
