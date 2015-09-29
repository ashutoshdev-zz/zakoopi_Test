package com.cam.model;

import com.google.gson.annotations.SerializedName;

public class LastUpdateDetail {

	@SerializedName("id")
	private String id;

	@SerializedName("model")
	private String model;

	@SerializedName("timestamp")
	private String timestamp;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
