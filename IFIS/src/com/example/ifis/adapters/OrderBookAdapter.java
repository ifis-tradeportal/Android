package com.example.ifis.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.ifis.model.OrderBook;
import com.example.ifis.OrderBookActivity;
import com.example.ifis.R;
import com.example.ifis.R.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


public class OrderBookAdapter extends BaseAdapter implements Filterable {
	
	Activity activity;
	List<OrderBook> dataList;
	List<OrderBook> originalList;
	List<String> flag;
	private LayoutInflater inflater = null;
	
	public OrderBookAdapter(Activity a,  List<OrderBook> list){
		activity = a;
		dataList = list;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public OrderBookAdapter (Activity a,  List<OrderBook> list,List<String> sList){
		activity = a;
		dataList = list;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		flag=sList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public OrderBook getItem(int position) {
		return (OrderBook)dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		ViewHolder vh;
		if (view == null){
			view = inflater.inflate(R.layout.order_book_cell,parent, false);
			vh = new ViewHolder();
			vh.stock = (TextView) view.findViewById(id.stockTV);
			vh.buySell = (TextView) view.findViewById(id.ActionTV);
			vh.price = (TextView) view.findViewById(id.priceTV);
			vh.oQty = (TextView) view.findViewById(id.oQtyTV);
			vh.status = (TextView) view.findViewById(id.statusTV);
			vh.fQty = (TextView) view.findViewById(id.filledTV);
			vh.acc=(TextView) view.findViewById(id.AccountTV);
			view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
		
		OrderBook ob = getItem(position);
		vh.stock.setText(ob.getStock());
		if(ob.getBS().toUpperCase().equals("BUY")){
			vh.buySell.setText(" (B)");
			vh.buySell.setTextColor(view.getResources().getColor(R.color.buyGreen));
		}
		else if(ob.getBS().toUpperCase().equals("SELL")){
			vh.buySell.setText(" (S)");
			vh.buySell.setTextColor(view.getResources().getColor(R.color.cellRed));			
		}
		
		double f= Double.parseDouble(ob.getOrderPrice());
		String pr= String.format("%.3f", f);
		vh.price.setText(pr);
		
		String ss=ob.getRefNo();
		
		
		
		int oq= Integer.parseInt(ob.getOrderQty());
		String qtyStr=String.format("%,d",oq);
		//vh.oQty.setText(String.format("%,d", oq));
		vh.oQty.setText(qtyStr);
		String status=ob.getOrderStatus().toString().toUpperCase();
		switch (status) {
		case "CANCELLED":
			vh.status.setText("CXL");
			break;
		case "CHANGED":
			vh.status.setText("CHG");
			break;
		case "FILLED":
			vh.status.setText("FILL");
			break;
		case "PARKED":
			vh.status.setText("PARK");
			break;
		case "PARTIALLY FILLED":
			vh.status.setText("PART");
			break;
		case "PENDING CHANGED":
			vh.status.setText("PCHG");
			break;
		case "PENDING CANCEL":
			vh.status.setText("PCXL");
			break;
		case "PENDING QUEUE":
			vh.status.setText("PQ");
			break;
		case "REJECTED":
			vh.status.setText("RJCT");
			break;
		case "UNSOLICITED CANCEL":
			vh.status.setText("CXL");
			break;
		case "QUEUE":
			vh.status.setText("Q");
			break;
		case "PART CHANGED":
			vh.status.setText("CHG");
			break;
			
		case "PART CANCELLED":
			vh.status.setText("CXL");
			break;		

		default:
			vh.status.setText(status);
			break;
		}
		vh.acc.setText(ob.getClientAC().toString());
		int qq=Integer.parseInt(ob.getMatchQty());
		String fQtyStr=String.format("%,d", qq);
//		vh.fQty.setText(fQtyStr);
		vh.fQty.setText(fQtyStr);
		
		if(flag.get(0).equals(ss)){
			vh.status.setTextColor(view.getResources().getColor(R.color.blue));
			vh.acc.setTextColor(view.getResources().getColor(R.color.blue));
			vh.stock.setTextColor(view.getResources().getColor(R.color.blue));
			vh.fQty.setTextColor(view.getResources().getColor(R.color.blue));
			vh.oQty.setTextColor(view.getResources().getColor(R.color.blue));
			vh.price.setTextColor(view.getResources().getColor(R.color.blue));
		}
		else{
			
		}
		

		return view;
	}
	
	public void updateDataList(List<OrderBook> list){
		dataList = list;
	}
	
	public List<OrderBook> getList(){
		return dataList;
	}

	public class ViewHolder{
		
		TextView stock;
		TextView buySell;
		TextView price;
		TextView oQty;
		TextView status;
		TextView fQty;
		TextView acc;
	
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                dataList = (List<OrderBook>) results.values; // has the filtered values
                notifyDataSetChanged(); // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<OrderBook> FilteredArrList = new ArrayList<OrderBook>();

                if (originalList == null) {
                	originalList = new ArrayList<OrderBook>(dataList); // saves the original data in mOriginalValues
                }

                /********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalList.size(); i++) {
                        OrderBook data = originalList.get(i);
                        if ((data.getStock().toLowerCase().contains(constraint.toString())) ||
                        	(data.getStockName().toLowerCase().contains(constraint.toString())) ||
                        	(data.getClientAC().toLowerCase().contains(constraint.toString()))) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
	}
	
	
}
