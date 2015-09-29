package com.zakoopi.model;

public class HotDealMarketDataObject {

	private String txtMarketName;
	
	public HotDealMarketDataObject(String txtMarketName1) {
		txtMarketName = txtMarketName1;
	}
	
	public String gettxtMarketName(){
		return txtMarketName;
		
	}
	
	public void settxtMarketName(String txtMarketName) {
		this.txtMarketName = txtMarketName;
	}
	
}
