package com.example.ifis.model;

import java.io.Serializable;
import java.util.HashMap;

public class Stock implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	String stockId;
	String stockName;
	String lastDonePrice;
	String bidPrice;
	String askPrice;
	String lotSize;
	String exchangeRate;
	String currencyName;
	String currencyCode;
	String exchange;
	String prevClosedPrice;
	
	public Stock(String stockId, String stockName) {
		super();
		this.stockId = stockId;
		this.stockName = stockName;	

		//put("stockId",stockId);
		//put("stockName",stockName);
	}
	
	
	

	public Stock(String stockName, String lastDonePrice,
			String bidPrice, String askPrice, String lotSize,
			String exchangeRate, String currencyName, String currencyCode,
			String exchange, String prevClosedPrice) {
		super();
		
		this.stockName = stockName;
		this.lastDonePrice = lastDonePrice;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
		this.lotSize = lotSize;
		this.exchangeRate = exchangeRate;
		this.currencyName = currencyName;
		this.currencyCode = currencyCode;
		this.exchange = exchange;
		this.prevClosedPrice = prevClosedPrice;
	
	}

	
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	public String getLastDonePrice() {
		return lastDonePrice;
	}

	public void setLastDonePrice(String lastDonePrice) {
		this.lastDonePrice = lastDonePrice;
	}

	public String getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}

	public String getLotSize() {
		return lotSize;
	}

	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getPrevClosedPrice() {
		return prevClosedPrice;
	}

	public void setPrevClosedPrice(String prevClosedPrice) {
		this.prevClosedPrice = prevClosedPrice;
	}
	
	

}
