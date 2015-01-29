package com.example.ifis.utilities;

import java.util.Comparator;

import com.example.ifis.model.OrderBook;

public class OrderByRefID implements Comparator<OrderBook> {

	@Override
	public int compare(OrderBook arg0, OrderBook arg1) {
		// TODO Auto-generated method stub
		int ref1=Integer.valueOf(arg0.getRefNo());
		int ref2=Integer.valueOf(arg1.getRefNo());
		int value1=compare(ref1, ref2);
			
		return value1;
	}
	
	private static int compare(int	 a, int b) {
	    return a < b ? -1
	         : a > b ? 1
	         : 0;
	  }

}