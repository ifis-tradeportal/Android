package com.example.ifis.model;

import java.io.Serializable;

public class CustodyStock implements Serializable {
	String stockName;
	String stockCode;
	String location;
	
	
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	String total;
	

}
