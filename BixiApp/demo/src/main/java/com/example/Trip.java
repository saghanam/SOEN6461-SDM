package com.example;

import java.time.LocalDateTime;

public class Trip {
	
	private String trip_id;
    private Bike bike;
    private Customer customer;
    private LocalDateTime trip_start;
    private LocalDateTime trip_end;
    private int tt_mod;
    private int tbill;
    private boolean activeFlag;
    
    public String getTrip_Id() {
    	return this.trip_id;
    }
	
    public Bike getBike() {
    	return this.bike;
    }
    
    public Customer getCustomer() {
    	return this.customer;
    }
    
    public LocalDateTime getTrip_Start() {
    	return this.trip_start;
    }   
    
    public LocalDateTime getTrip_End() {
    	return this.trip_end;
    }
    
    public int getTT_Mod() {
    	return this.tt_mod;
    }
    
    public boolean getActive_Flag() {
    	return this.activeFlag;
    }
    
    public void setTrip_Id( String trip_id) {
    	this.trip_id = trip_id;
    }
	
    public void setBike_Id(Bike bike) {
    	this.bike= bike;
    }
    
    public void setCustomer_Id(Customer customer) {
    	this.customer = customer;
    }
    
    public void setTrip_Start(LocalDateTime trip_start) {
    	this.trip_start = trip_start;
    }   
    
    public void setTrip_End(LocalDateTime trip_end) {
    	this.trip_end = trip_end;
    }
    
    public void setTT_Mod(int tt_mod) {
    	this.tt_mod = tt_mod;
    }
    
    public void setActive_Flag(boolean aFlag) {
    	this.activeFlag = aFlag;
    }

}
