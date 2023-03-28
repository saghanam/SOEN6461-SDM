package com.example;

import java.util.ArrayList;

public class Station {
	private String station_code;
	private ArrayList<Dock> docks;
	
	public String getStation_Code() {
		return this.station_code;
	}
	
	public ArrayList<Dock> getDocks(){
		return this.docks;
	}
	
	public void setStation_Code(String st_code) {
		this.station_code = st_code;
	}
	
	public void addDock(Dock dk) {
		this.docks.add(dk);
	}
	
	public void removeDock(Dock dk) {
		this.docks.remove(dk);
	}
	
}
