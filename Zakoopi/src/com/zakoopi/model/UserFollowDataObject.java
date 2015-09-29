package com.zakoopi.model;

public class UserFollowDataObject {

	private String txtStoreName;
	private String txtStoreLocation;
	
	public UserFollowDataObject(String txtStoreName1, String txtStoreLocation1) {
		txtStoreName = txtStoreName1;
		txtStoreLocation = txtStoreLocation1;
	}
	
	public String gettxtStoreName(){
		return txtStoreName;
		
	}
	
	public void settxtStoreName(String txtStoreName) {
		this.txtStoreName = txtStoreName;
	}
	
	public String gettxtStoreLocation(){
		return txtStoreLocation;
		
	}
	
	public void settxtStoreLocation(String txtStoreLocation) {
		this.txtStoreLocation = txtStoreLocation;
	}
}
