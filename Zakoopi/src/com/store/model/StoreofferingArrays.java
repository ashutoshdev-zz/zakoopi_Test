package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreofferingArrays {

	@SerializedName("id")
	private String id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("offering_id")
	private String offering_id;
	@SerializedName("price_range_from")
	private String price_range_from;
	@SerializedName("price_range_to")
	private String price_range_to;
	StoreofferingDetais offering;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getOffering_id() {
		return offering_id;
	}

	public void setOffering_id(String offering_id) {
		this.offering_id = offering_id;
	}

	public String getPrice_range_from() {
		return price_range_from;
	}

	public void setPrice_range_from(String price_range_from) {
		this.price_range_from = price_range_from;
	}

	public String getPrice_range_to() {
		return price_range_to;
	}

	public void setPrice_range_to(String price_range_to) {
		this.price_range_to = price_range_to;
	}

	public StoreofferingDetais getOffering() {
		return offering;
	}

	public void setOffering(StoreofferingDetais offering) {
		this.offering = offering;
	}
}
