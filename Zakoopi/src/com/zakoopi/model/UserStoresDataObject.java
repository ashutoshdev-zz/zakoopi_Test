package com.zakoopi.model;

public class UserStoresDataObject {

	private String txtStoreName;
	private String txtLookbookView;
	
	public UserStoresDataObject(String txtStoreName1, String txtLookbookView1) {
		txtStoreName = txtStoreName1;
		txtLookbookView = txtLookbookView1;
	}
	
	public String gettxtStoreName(){
		return txtStoreName;
		
	}
	
	public void settxtStoreName(String txtStoreName) {
		this.txtStoreName = txtStoreName;
	}
	
	public String gettxtLookbookView(){
		return txtLookbookView;
		
	}
	
	public void settxtLookbookView(String txtLookbookView) {
		this.txtLookbookView = txtLookbookView;
	}
	
	
}
