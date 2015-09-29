package com.zakoopi.searchstore.model;

public class SearchPojo {

	String id;
	String storename;
	String storeadd;
	String storecity;
	String storslug;
	
	public String getStorslug() {
		return storslug;
	}
	public void setStorslug(String storslug) {
		this.storslug = storslug;
	}
	public SearchPojo(){
		
	}
	public SearchPojo(String id,String st,String st1,String st2,String storslug){
		this.id=id;
		this.storename=st;
		this.storeadd = st1;
		this.storecity =st2;
		this.storslug = storslug;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getStoreadd() {
		return storeadd;
	}
	public void setStoreadd(String storeadd) {
		this.storeadd = storeadd;
	}
	public String getStorecity() {
		return storecity;
	}
	public void setStorecity(String storecity) {
		this.storecity = storecity;
	}
	
}
