package com.zakoopi.userfeed.model;

import java.util.ArrayList;

public class UserFeed {

	ArrayList<feedTimeline> feedTimeline;

	public ArrayList<feedTimeline> getFeedTimeline() {
		return feedTimeline;
	}

	public void setFeedTimeline(ArrayList<feedTimeline> feedTimeline) {
		this.feedTimeline = feedTimeline;
	}
}
