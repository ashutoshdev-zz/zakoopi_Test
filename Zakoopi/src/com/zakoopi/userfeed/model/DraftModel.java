package com.zakoopi.userfeed.model;

import java.util.ArrayList;

public class DraftModel {

	ArrayList<DraftFeedData> data = new ArrayList<DraftFeedData>();

	public ArrayList<DraftFeedData> getData() {
		return data;
	}

	public void setData(ArrayList<DraftFeedData> data) {
		this.data = data;
	}
}
