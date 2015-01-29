package com.example.ifis.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.example.ifis.model.OrderBook;

public class OrderBookFilter {
	public static ArrayList<OrderBook> getFilledList(ArrayList<OrderBook> list){
		ArrayList<OrderBook> newList = new ArrayList<>();
		for (int i=0;i<list.size();i++){
			OrderBook ob = list.get(i);
			if(ob.getOrderStatus().toUpperCase().equals("FILLED")){
				newList.add(ob);
			}
		}
		return newList;
		
	}
	
	public static ArrayList<OrderBook> getQueuedList(ArrayList<OrderBook> list){
		ArrayList<OrderBook> newList = new ArrayList<>();
		for (int i=0;i<list.size();i++){
			OrderBook ob = list.get(i);
			if(ob.getOrderStatus().toUpperCase().equals("QUEUE")){
				newList.add(ob);
			}
		}
		return newList;
	}
	
	
	public static ArrayList<OrderBook> sortByAccount(ArrayList<OrderBook> list){
		Collections.sort(list,new OrderByAccount());
		return list;

	}
	
	public static ArrayList<OrderBook> sortByRefId(ArrayList<OrderBook> list){
		Collections.sort(list,new OrderByRefID());
		return list;

	}
	
	public static ArrayList<OrderBook> sortByStk(ArrayList<OrderBook> list){
		Collections.sort(list,new OrderByStock());
		return list;

	}
	
	public static ArrayList<OrderBook> sortByStatus(ArrayList<OrderBook> list){
		Collections.sort(list,new OrderByStatus());
		return list;

	}
	
	public static ArrayList<OrderBook> sortByPrice(ArrayList<OrderBook> list){
		Collections.sort(list,new OrderByPrice());
		return list;

	}
	
	

}
