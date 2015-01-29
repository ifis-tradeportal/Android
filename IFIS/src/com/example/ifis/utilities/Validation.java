package com.example.ifis.utilities;

public class Validation {
	
	public static int isAmendable(String newQty,int oldQty,int matchQty,String newPrice,String oldPrice){
		int qty=Integer.parseInt(newQty);
		if((qty>matchQty) &&(qty<=oldQty)) return 1;
		else return -1;
	}

}
