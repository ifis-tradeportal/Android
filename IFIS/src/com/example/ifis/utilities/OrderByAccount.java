package com.example.ifis.utilities;
import java.util.Comparator;

import com.example.ifis.model.OrderBook;


public class OrderByAccount implements Comparator<OrderBook> {

	@Override
	public int compare(OrderBook arg0, OrderBook arg1) {
		// TODO Auto-generated method stub
		
		int value1=arg0.getClientAC().compareTo(arg1.getClientAC());
		if (value1 == 0) {
			int value2=arg0.getRefNo().compareTo(arg1.getRefNo());
			return value2;
		}
		
		return value1;
	}

}
