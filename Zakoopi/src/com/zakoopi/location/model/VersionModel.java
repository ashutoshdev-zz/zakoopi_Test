package com.zakoopi.location.model;

import com.google.gson.annotations.SerializedName;

public class VersionModel {

	@SerializedName("data")
	private String data;

	public String getVersion() {
		return data;
	}

	public void setVersion(String version) {
		this.data = version;
	}
}
