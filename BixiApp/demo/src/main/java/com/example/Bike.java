package com.example;

public class Bike {
	private String bike_id;
	private String model_id;
	
	public Bike(String bike_id, String model_id) {
		this.bike_id = bike_id;
		this.model_id = model_id;
	}
	
	public String getBike_Id() {
		return this.bike_id;
	}
	
	public String getModel_Id() {
		return this.model_id;
	}
	
	public void setBikeId(String bikeId) {
		this.bike_id = bike_id;
	}
	
	public void setModel_Id(String model_id) {
		this.model_id = model_id;
	}

}
