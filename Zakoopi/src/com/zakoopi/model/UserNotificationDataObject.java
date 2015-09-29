package com.zakoopi.model;

public class UserNotificationDataObject {

	private String txtNotification;
	
	public UserNotificationDataObject(String txtNotification1) {
		txtNotification = txtNotification1;
	}
	
	public String gettxtNotification(){
		return txtNotification;
		
	}
	
	public void settxtNotification(String txtNotification) {
		this.txtNotification = txtNotification;
	}
	
}
