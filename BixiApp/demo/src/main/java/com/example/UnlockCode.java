package com.example;

import java.time.LocalDateTime;

public class UnlockCode {
	private int unlock_code;
	private LocalDateTime start;
	
	public UnlockCode(int uc, LocalDateTime strt) {
		this.unlock_code = uc;
		this.start = strt;
	}
	
	public int getUnlock_Code() {
		return this.unlock_code;
	}
	
	public LocalDateTime getStart() {
		return this.start;
	}
	
	public void setUnlock_Code(int u_code) {
		this.unlock_code = u_code;
	}
	
	public void setStart(LocalDateTime dts) {
		this.start = dts;
	}

}
