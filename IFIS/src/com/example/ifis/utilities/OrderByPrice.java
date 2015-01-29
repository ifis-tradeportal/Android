package com.example.ifis.utilities;

import java.util.Comparator;

import com.example.ifis.model.OrderBook;

public class OrderByPrice implements Comparator<OrderBook> {

	@Override
	public int compare(OrderBook arg0, OrderBook arg1) {
		// TODO Auto-generated method stub
		double p0=Double.valueOf(arg0.getOrderPrice());
		double p1=Double.valueOf(arg1.getOrderPrice());
		int value1=compare(p0,p1);
		if (value1 == 0) {
			int value2=arg0.getRefNo().compareTo(arg1.getRefNo());
			return value2;
		}
		
		return value1;
	}
	
	private static int compare(double a, double b) {
	    return a < b ? -1
	         : a > b ? 1
	         : 0;
	  }


}