package com.example.ifis.model;

import java.io.Serializable;

public class AccountModel implements Serializable {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String accountName;
     private  String accountId;
     
     
     
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	} 
    
      
    

}
