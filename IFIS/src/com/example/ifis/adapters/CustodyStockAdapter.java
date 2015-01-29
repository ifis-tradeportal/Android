package com.example.ifis.adapters;

import java.util.ArrayList;
import java.util.List;

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
import com.example.ifis.R;
import com.example.ifis.R.id;
import com.example.ifis.adapters.OrderBookAdapter.ViewHolder;
import com.example.ifis.model.CustodyStock;
import com.example.ifis.model.OrderBook;

public class CustodyStockAdapter extends BaseAdapter implements Filterable {
	
	Activity activity;
	List<CustodyStock> dataList;
	List<CustodyStock> originalList;
	private LayoutInflater inflater = null;
	
	public CustodyStockAdapter(Activity a,  List<CustodyStock> list){
		activity = a;
		dataList = list;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public CustodyStock getItem(int position) {
		return (CustodyStock)dataList.get(position);
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
			view = inflater.inflate(R.layout.custom_stock_cell,parent, false);
			vh = new ViewHolder();
			vh.stockCode = (TextView) view.findViewById(id.stkCodeACCTV);
			vh.stockName = (TextView) view.findViewById(id.stkNameACCTV);
			vh.location = (TextView) view.findViewById(id.stkLocACCTV);
			vh.available = (TextView) view.findViewById(id.stkAvlblACCTV);
			view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
		
		CustodyStock stk = getItem(position);
		vh.stockCode.setText(stk.getStockCode());
		vh.stockName.setText(stk.getStockName());
		vh.location.setText("");
		vh.available.setText(stk.getTotal());
		return view;
	}
	
	public void updateDataList(List<CustodyStock> list){
		dataList = list;
	}
	
	public List<CustodyStock> getList(){
		return dataList;
	}

	public class ViewHolder{
		
		TextView stockName;
		TextView stockCode;
		TextView location;
		TextView available;
	
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                dataList = (List<CustodyStock>) results.values; // has the filtered values
                notifyDataSetChanged(); // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<CustodyStock> FilteredArrList = new ArrayList<CustodyStock>();

                if (originalList == null) {
                	originalList = new ArrayList<CustodyStock>(dataList); // saves the original data in mOriginalValues
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
                        CustodyStock data = originalList.get(i);
                        if ((data.getStockCode().toLowerCase().contains(constraint.toString())) ||
                        	(data.getStockName().toLowerCase().contains(constraint.toString()))) {
                            FilteredArrList.add(data);
                        }
                    }
                    
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
	}
	
	
}