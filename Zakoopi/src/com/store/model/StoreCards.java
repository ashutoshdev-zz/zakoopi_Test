package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreCards {

	@SerializedName("id")
	private String id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("card_id")
	private String card_id;
	
	Card card;
	
	
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

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	
}
