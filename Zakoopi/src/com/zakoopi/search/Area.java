package com.zakoopi.search;

import java.util.ArrayList;

import android.util.Log;

public class Area {

	ArrayList<marketsSearch> markets;
	
	public ArrayList<marketsSearch> getMarkets() {
		
		return markets;
		
		
		
	}
	
	public void setMarket(ArrayList<marketsSearch> markets) {
		this.markets = markets;
		Log.e("AREA CLASS", "class");
	}
}
