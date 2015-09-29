package com.zakoopi.search;

import com.google.gson.annotations.SerializedName;

public class updateSearch implements Comparable<updateSearch>{

	@SerializedName("id")
	private String id;
	
	@SerializedName("model")
	private String model;
	
	@SerializedName("timestamp")
	private String timestamp;
	
	public String get_id(){
		return id;
		
	}
	
	public void set_id(String _id){
		this.id = _id;
	}
	
	public String get_model(){
		return model;
	}
	
	public void set_model(String _model){
		this.model = _model;
	}
	
	public String get_timestamp(){
		return timestamp;
	}
	
	public void set_timestamp(String _timestamp){
		this.timestamp = _timestamp;
	}
	
	@Override
	public int compareTo(updateSearch update) {
		// TODO Auto-generated method stub
		return update.get_id().compareTo(id);
	}

}
