package com.zakoopi.searchstore.model;

public class StoreTagPojo {

	String storename;
	String storeid;
	
	String marketname;
	String ratecounter;
	String btn1;
	String btn2;
	String btn3;
	String btn4;
	String btn5;
	String btn6;
	String btn7;
	String btn8;
	String btn9;
	
	public StoreTagPojo(String storeid,String storename,String marketname,String ratecounter,String btn1,
			String btn2,String btn3,String btn4,String btn5,String btn6,String btn7,
			String btn8,String btn9){
		super();
		this.storename=storename;
		this.storeid=storeid;
		this.marketname=marketname;
		this.ratecounter=ratecounter;
		this.btn1=btn1;
		this.btn2=btn2;
		this.btn3=btn3;
		this.btn4=btn4;
		this.btn5=btn5;
		this.btn6=btn6;
		this.btn7=btn7;
		this.btn8=btn8;
		this.btn9=btn9;
		
	}
	
	public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getMarketname() {
		return marketname;
	}
	public void setMarketname(String marketname) {
		this.marketname = marketname;
	}
	public String getRatecounter() {
		return ratecounter;
	}
	public void setRatecounter(String ratecounter) {
		this.ratecounter = ratecounter;
	}
	public String isBtn1() {
		return btn1;
	}
	public void setBtn1(String btn1) {
		this.btn1 = btn1;
	}
	public String isBtn2() {
		return btn2;
	}
	public void setBtn2(String btn2) {
		this.btn2 = btn2;
	}
	public String isBtn3() {
		return btn3;
	}
	public void setBtn3(String btn3) {
		this.btn3 = btn3;
	}
	public String isBtn4() {
		return btn4;
	}
	public void setBtn4(String btn4) {
		this.btn4 = btn4;
	}
	public String isBtn5() {
		return btn5;
	}
	public void setBtn5(String btn5) {
		this.btn5 = btn5;
	}
	public String isBtn6() {
		return btn6;
	}
	public void setBtn6(String btn6) {
		this.btn6 = btn6;
	}
	public String isBtn7() {
		return btn7;
	}
	public void setBtn7(String btn7) {
		this.btn7 = btn7;
	}
	public String isBtn8() {
		return btn8;
	}
	public void setBtn8(String btn8) {
		this.btn8 = btn8;
	}
	public String isBtn9() {
		return btn9;
	}
	public void setBtn9(String btn9) {
		this.btn9 = btn9;
	}
	
}
