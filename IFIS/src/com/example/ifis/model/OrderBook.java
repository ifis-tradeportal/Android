package com.example.ifis.model;

import java.io.Serializable;

public class OrderBook implements Serializable{
	
	String refNo;
	String stock;
	String clientAC;
	String exchange;
	String BS;
	String orderStatus;
	String orderQty;
	String MatchQty;
	String OrderPrice;
	String AvePrice;
	String OrderDateTime;
	String SenderSubID;
	String SettlementCurr;
	String SingleOrderID;
	String ExchangeOrderID;
	String StockName;
	
	
	
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getClientAC() {
		return clientAC;
	}
	public void setClientAC(String clientAC) {
		this.clientAC = clientAC;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getBS() {
		return BS;
	}
	public void setBS(String bS) {
		BS = bS;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}
	public String getMatchQty() {
		return MatchQty;
	}
	public void setMatchQty(String matchQty) {
		MatchQty = matchQty;
	}
	public String getOrderPrice() {
		return OrderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		OrderPrice = orderPrice;
	}
	public String getAvePrice() {
		return AvePrice;
	}
	public void setAvePrice(String avePrice) {
		AvePrice = avePrice;
	}
	public String getOrderDateTime() {
		return OrderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		OrderDateTime = orderDateTime;
	}
	public String getSenderSubID() {
		return SenderSubID;
	}
	public void setSenderSubID(String senderSubID) {
		SenderSubID = senderSubID;
	}
	public String getSettlementCurr() {
		return SettlementCurr;
	}
	public void setSettlementCurr(String settlementCurr) {
		SettlementCurr = settlementCurr;
	}
	public String getSingleOrderID() {
		return SingleOrderID;
	}
	public void setSingleOrderID(String singleOrderID) {
		SingleOrderID = singleOrderID;
	}
	public String getExchangeOrderID() {
		return ExchangeOrderID;
	}
	public void setExchangeOrderID(String exchangeOrderID) {
		ExchangeOrderID = exchangeOrderID;
	}
	public String getStockName() {
		return StockName;
	}
	public void setStockName(String stockName) {
		StockName = stockName;
	}
	
	
	
	
	
	

}
