package com.example;

public class Dock {
	private String dock_id;
    private UnlockCode unlock_code;
    private Bike bike; 
    
    public String getDock_Id() {
    	return this.dock_id;
    }
    
    public UnlockCode getUnlock_Code() {
    	return this.unlock_code;
    }
    
    public Bike getBike() {
    	return this.bike;
    }
    
    public void setDock_Id(String dock_id) {
    	this.dock_id = dock_id;
    }
    
    public void setUnlock_Code(UnlockCode uc_code) {
    	this.unlock_code = uc_code;
    }
    
    public void setBike(Bike bk) {
    	this.bike = bk;
    }
}
