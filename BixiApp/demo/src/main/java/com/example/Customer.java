package com.example;

public class Customer {
	
	private String customer_id;
    private String c_name;
    private int c_age;
    private String c_mail;
    private int c_phone;
    private int outstanding_bill;
    
    public Customer(String customer_id, String c_name, int c_age, String c_mail, int c_phone, int bill) {
    	this.customer_id = customer_id;
    	this.c_age = c_age;
    	this.c_mail = c_mail;
    	this.c_name = c_name;
    	this.c_phone = c_phone;
    	this.outstanding_bill = bill;
    }
    
    public String getCustomer_Id() {
    	return this.customer_id;    	
    }
    
    public String getC_Name() {
    	return this.c_name;
    }
    
    public String getC_Mail() {
    	return this.c_mail;
    }
    
    
    public int getC_Age() {
    	return this.c_age;
    }
    
    public int getC_Phone() {
    	return this.c_phone;
    }
    
    public int getOutstanding_Bill() {
    	return this.outstanding_bill;
    }
    
    public void setCustomer_Id(String customer_id) {
    	this.customer_id = customer_id;    	
    }
    
    public void setC_Name(String c_name) {
    	this.c_name = c_name;
    }
    
    public void setC_Mail(String c_mail) {
    	this.c_mail=c_mail;
    }
    
    public void setC_Age(int c_age) {
    	this.c_age = c_age;
    }
    
    public void setC_Phone(int c_phone) {
    	this.c_phone=c_phone;
    }
    
    public void setOutstanding_Bill(int o_bill) {
    	this.outstanding_bill=o_bill;
    }

}
