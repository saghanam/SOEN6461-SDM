package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
    
    public static Customer login(String customer_id, String pwd, Connection con) {
		try {
			System.out.println(customer_id+"" + pwd);
			Class.forName("com.mysql.cj.jdbc.Driver");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select pwd from auth where customer_id='" + customer_id + "'");
			if (rs.next()) {
				if (rs.getString(1).equals(pwd)) {
					try {
						Statement stmt2 = con.createStatement();
						ResultSet rs2 = stmt
								.executeQuery("SELECT * from customers WHERE customer_id='" + customer_id + "'");
						rs2.next();
						String name = rs2.getString(2);
						int c_age = rs2.getInt(3);
						String c_mail = rs2.getString(4);
						int c_phone = rs2.getInt(5);
						int bill = rs2.getInt(6);

						Customer ct = new Customer(customer_id, name, c_age, c_mail, c_phone, bill);
						UserSession.getInstance(name,customer_id);
						return ct;

					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace(System.out);
						return null;
					}

				} else {

					return null;
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;

		}
	}

}
