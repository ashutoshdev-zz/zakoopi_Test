package com.store.model;

public class MyDataModel {

	MyReviewModel StoreReviews;
	MyRateModel StoreRatings;
	
	public MyRateModel getStoreRatings() {
		return StoreRatings;
	}
	public void setStoreRatings(MyRateModel storeRatings) {
		StoreRatings = storeRatings;
	}
	public MyReviewModel getStoreReviews() {
		return StoreReviews;
	}
	public void setStoreReviews(MyReviewModel storeReviews) {
		StoreReviews = storeReviews;
	}

	
}
