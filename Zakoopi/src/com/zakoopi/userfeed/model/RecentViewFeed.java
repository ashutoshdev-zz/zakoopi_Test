package com.zakoopi.userfeed.model;

import java.util.ArrayList;

public class RecentViewFeed {

	ArrayList<RecentViewData> data = new ArrayList<RecentViewData>();

	public ArrayList<RecentViewData> getData() {
		return data;
	}

	public void setData(ArrayList<RecentViewData> data) {
		this.data = data;
	}
}
