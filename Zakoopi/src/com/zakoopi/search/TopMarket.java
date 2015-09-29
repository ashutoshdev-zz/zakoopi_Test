package com.zakoopi.search;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TopMarket {

	@SerializedName("visible")
	private String visible; 
	ArrayList<marketsTop> markets;
	
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public ArrayList<marketsTop> getTopMarket() {
		return markets;
		
	} 
	
	public void setTopMarket(ArrayList<marketsTop> markets) {
		this.markets = markets;
	}
}
