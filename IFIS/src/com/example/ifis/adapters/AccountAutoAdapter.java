package com.example.ifis.adapters;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.ifis.R;
import com.example.ifis.model.AccountModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Filter;

public class AccountAutoAdapter extends ArrayAdapter<AccountModel> {
    private final String MY_DEBUG_TAG = "AccountModelAdapter";
    private ArrayList<AccountModel> items;
    private ArrayList<AccountModel> itemsAll;
    private ArrayList<AccountModel> suggestions;
    private int viewResourceId;

    @SuppressWarnings("unchecked")
	public AccountAutoAdapter(Context context, int viewResourceId, ArrayList<AccountModel> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<AccountModel>) items.clone();
        this.suggestions = new ArrayList<AccountModel>();
        this.viewResourceId = viewResourceId;
    }
    
    @Override
    public AccountModel getItem(int position) {
    	// TODO Auto-generated method stub
    	return super.getItem(position);
    }
    
    

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.accout_dropdown_cell, null);
        }
        AccountModel AccountModel = items.get(position);
      
        if (AccountModel != null) {
            TextView AccountModelNameLabel = (TextView) v.findViewById(R.id.accNameTV);
            if (AccountModelNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView AccountModel Name:"+AccountModel.getName());
                AccountModelNameLabel.setText(AccountModel.getAccountName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((AccountModel)(resultValue)).getAccountName(); 
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
        	FilterResults filterResults = new FilterResults();
            if(constraint != null) {
                suggestions.clear();
                for (AccountModel AccountModel : itemsAll) {
                    if(AccountModel.getAccountName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(AccountModel);
                    }
                
                    
                }
//              FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
//                return filterResults;
            } else {
//                return new FilterResults();
                filterResults.values=itemsAll;
                filterResults.count=itemsAll.size(); 
            }
            return filterResults;
        }
        @SuppressWarnings("unchecked")
		@Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            
			ArrayList<AccountModel> filteredList = (ArrayList<AccountModel>) results.values;
            
            if(results != null && results.count > 0) {
                clear();
                for (AccountModel c : filteredList) {
                    add(c);
                }   
                notifyDataSetChanged();
            }
           
        }
    };
}
