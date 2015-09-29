package com.zakoopi.search;

import java.util.ArrayList;

public class SearchUpdate {

	ArrayList<updateSearch> lastUpdates;
	
	public ArrayList<updateSearch> getUpdate(){
		return lastUpdates;
		
	}
	
	public void setLastUpdate(ArrayList<updateSearch> lastUpdates){
		this.lastUpdates = lastUpdates;
	}
}
