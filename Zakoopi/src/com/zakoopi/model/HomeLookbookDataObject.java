package com.zakoopi.model;

public class HomeLookbookDataObject {

	private String txtUserName;
	private String txtLookbookView;
	//private String txtLookbookTitle;
	
	public HomeLookbookDataObject(String txtUserName1, String txtLookbookView1) {
		txtUserName = txtUserName1;
		txtLookbookView = txtLookbookView1;
	}
	
	public String gettxtUserName(){
		return txtUserName;
		
	}
	
	public void settxtUserName(String txtUserName) {
		this.txtUserName = txtUserName;
	}
	
	public String gettxtLookbookView(){
		return txtLookbookView;
		
	}
	
	public void settxtLookbookView(String txtLookbookView) {
		this.txtLookbookView = txtLookbookView;
	}
	
	
}
