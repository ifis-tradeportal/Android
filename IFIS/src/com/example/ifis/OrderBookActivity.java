package com.example.ifis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.example.ifis.R.drawable;
import com.example.ifis.adapters.OrderBookAdapter;
import com.example.ifis.model.OrderBook;
import com.example.ifis.model.AccountModel;
import com.example.ifis.service.GetAccountsService;
import com.example.ifis.service.OrderBookService;
import com.example.ifis.utilities.OrderBookFilter;
import com.example.ifis.utilities.SegmentedRadioGroup;
import com.parse.ParsePushBroadcastReceiver;




import android.R.bool;
import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView.OnCloseListener;

public class OrderBookActivity extends Activity {
	ArrayList<OrderBook> orderBookList;
	ListView orderList;
	ProgressBar pb;
	TextView emptyTV;
	
	TextView accNameTV;
	TextView stkTV;
	TextView odrPriceTV;
	TextView statusTV;
	
	
	SegmentedRadioGroup segmentText;
	OrderBookAdapter adapterFilled;
	OrderBookAdapter adapterQueued;
	OrderBookAdapter adapter;
	OrderBookAdapter adapterRefOrder;
	OrderBookAdapter adapterAccOrder;
	OrderBookAdapter adapterStkOrder;
	OrderBookAdapter adapterPriceOrder;
	OrderBookAdapter adapterStatusOder;
	ProgressDialog dialog;
	SearchView sv;
	Context context;
	ArrayList<String> flagList;
	boolean notify=false;
	public boolean refreshFlag=true;
	AsyncTask<ArrayList<String>, Void, HashMap<String,ArrayList<OrderBook>>> booktask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		ActionBar a= getParent().getActionBar();
		context= getApplicationContext();
		refreshFlag=false;
		int aa=a.getNavigationItemCount() ;		
		Log.i("sss", String.valueOf(aa));
		setContentView(R.layout.activity_order_book);
		pb= (ProgressBar) findViewById(R.id.progressBarList);
		pb.setVisibility(ProgressBar.INVISIBLE);
		
		emptyTV=(TextView) findViewById(R.id.emtyListTV);
		
		//lables
		stkTV = (TextView) findViewById(R.id.textView1);
		accNameTV = (TextView) findViewById(R.id.textView2);
		odrPriceTV =(TextView) findViewById(R.id.textView3);
		statusTV= (TextView) findViewById(R.id.textView5);
		
		
		flagList= new ArrayList<>();
		flagList.add("xxx");
		
		booktask = new DowloadOrderBookTask();
		booktask.execute(flagList);
		//new DowloadOrderBookTask().execute(flagList);
		orderList =(ListView) findViewById(R.id.orderListView);
		orderList.setEmptyView(findViewById(R.id.emtyListTV));
		orderList.setTextFilterEnabled(true);
		segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
	    segmentText.setOnCheckedChangeListener(new OnCheckedChangeListener() 
           {
            public void onCheckedChanged(RadioGroup group, int checkedId) 
               {
                // TODO Auto-generated method stub
                if(checkedId==R.id.button_one)
                  {
                	if(!(adapter==null)){
                		orderList.setAdapter(adapter);
                	}
                  }
                else if(checkedId==R.id.button_two)
                  {
                	if(!(adapterFilled==null)){
                		orderList.setAdapter(adapterFilled);
                	}
                  }
                else if(checkedId==R.id.button_three){
                	if(!(adapterQueued==null)){
                		orderList.setAdapter(adapterQueued);
                	}
                }
             }
        });
	    View v= orderList.getChildAt(0);
	    
	    orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				OrderBookAdapter adapter = (OrderBookAdapter) adapterView.getAdapter();
				OrderBook p = adapter.getItem(position);
				Intent i = new Intent(OrderBookActivity.this, OrderDetailsActivity.class);
				i.putExtra("order", p);
				i.putExtra("position", position);
				startActivityForResult(i, 100);
			}
		});
	    
	    orderList.setAdapter(adapter);
	    View vl= orderList.getChildAt(0);
	    //setting up searchView
	    setupSearchView();
	    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
	    {
	        public boolean onQueryTextChange(String newText) 
	        {
	            // this is your adapter that will be filtered
	        	Log.i("test change", "okay"); 	
	        	adapter.getFilter().filter(newText);	        	
	        	int ss =adapter.getCount();
	            return true;
	            
	        }

	        public boolean onQueryTextSubmit(String query) 
	        {
	            // this is your adapter that will be filtered
	            return false;
	        }
	    };
	    sv.setOnQueryTextListener(queryTextListener);
	    
	    
	    //on touch of labels 
	   // sortOnLabelTouch();
	    
	}
	
	
	private void setupSearchView() {
		// TODO Auto-generated method stub
		sv= (SearchView) findViewById(R.id.searchView1);
		sv.setQueryHint("Acc.No/Code/Name");
		sv.setOnSearchClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	
	            segmentText.check(R.id.button_one);
	            segmentText.setVisibility(View.INVISIBLE);
	            
	        }
		});
		
		//ImageView closeButton = (ImageView)sv.findViewById();
		sv.setOnCloseListener(new OnCloseListener() {
	        @Override
	        public boolean onClose() {
	            segmentText.setVisibility(View.VISIBLE);
	            
	            return false;
	        }
	    });
		
	}


	public void sortOnLabelTouch(){
		stkTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!(adapterFilled == null)) {
					orderList.setAdapter(adapterStkOrder);
				}

			}
		});
		accNameTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!(adapterFilled == null)) {
					orderList.setAdapter(adapterAccOrder);
				}

			}
		});
		odrPriceTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!(adapterFilled == null)) {
					orderList.setAdapter(adapterPriceOrder);
				}

			}
		});
		statusTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!(adapterFilled == null)) {
					orderList.setAdapter(adapterStatusOder);
				}

			}
		});
	    
	}
	
	@Override
	public void onBackPressed()
	{
		if(!sv.isIconified()){
			sv.setIconified(true);
			sv.setIconified(true);
		}

	   // super.onBackPressed(); // Comment this super call to avoid calling finish()
	}
	
	public void showAlert(final String message){
		new AlertDialog.Builder(this)
	    .setTitle("Status")
	    .setMessage(message)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	if(message.equals(ConstantsStrings.ORDER_SUCCESS)){
	        		
	        		ArrayList<AccountModel> CustomListViewValuesArr=(ArrayList<AccountModel>) getIntent().getSerializableExtra("Accounts");
	        		//getIntent().getExtras().ge
	        		//finish();
	        		Intent i = new Intent(OrderBookActivity.this,HomeTabsActivity.class);
	        		i.putExtra("1", 1);
	        		i.putExtra("Accounts", CustomListViewValuesArr);
	        		startActivityForResult(i, 102);
	        		//finish();
	        	}
	        	
	        	else if(message.equals(ConstantsStrings.SESSION_FAIL_STRING)){
	        		Intent i = new Intent(OrderBookActivity.this,LoginActivity.class);
	        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
	        	}
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	@SuppressWarnings("unchecked")
	public void didClickRefresh(View v){
		
		//booktask = new DowloadOrderBookTask();
		booktask.cancel(true);
		sv.setIconified(true);
		sv.setIconified(true);
		orderBookList.clear();
		flagList=new ArrayList<String>();
		flagList.add("");
		//booktask.execute(flagList);
		new DowloadOrderBookTask().execute(flagList);
		adapter.notifyDataSetChanged();
		segmentText.check(R.id.button_one);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getParent().getMenuInflater().inflate(R.menu.home_tabs, menu);
		return true;
	}
	

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		//context.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
		context.registerReceiver(mReceiver, new IntentFilter("unique_name"));
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {

	    	notify=true;
	        // Extract data included in the Intent
	        String message = intent.getStringExtra("message");
	        Log.i("in orderbook update", message);
	         
	        //new OrderBookActivity().new DowloadOrderBookTask().execute();
	        //do other stuff here
	        
	
	        //orderBookList.clear();
	        flagList= new ArrayList<String>();
	        String idd=message.substring(7,11);
	        Log.i("id is-----", idd);
	        flagList.add(message.substring(7,11));
	        booktask.execute(flagList);
			//new DowloadOrderBookTask().execute(flagList);
			adapter.notifyDataSetChanged();
	        //new DowloadOrderBookTask2().execute();
 
	        
	    }
	};
	
	private ParsePushBroadcastReceiver mReceiver= new ParsePushBroadcastReceiver(){
		 @Override
		    public void onReceive(Context context, Intent intent) {

		    	notify=true;
		        // Extract data included in the Intent
		        String message = intent.getStringExtra("message");
		        Log.i("in orderbook update", message);
		        
		        booktask=new DowloadOrderBookTask();
		        //new OrderBookActivity().new DowloadOrderBookTask().execute();
		        //do other stuff here
		        //orderBookList.clear();
		        booktask.cancel(true);
		        flagList= new ArrayList<String>();
		        flagList.clear();
		        int length=flagList.size();
		        String idd=message.substring(10,16);
		        Log.i("id is-----", idd);
		        Log.i("length of array", String.valueOf(length));
		        flagList.add(idd);
		        //flagList.add(message);
		        if(booktask.getStatus().equals(AsyncTask.Status.RUNNING))
		        {
		        	booktask.cancel(true);
		        	Log.i("async", "canceled");
		        }
		        booktask.execute(flagList);
				//new DowloadOrderBookTask().execute(flagList);
				//adapter.notifyDataSetChanged();
		        //new DowloadOrderBookTask2().execute();
	 
		       // Toast.makeText(context, "in book", Toast.LENGTH_LONG).show();
		    }
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==ConstantsStrings.RESULT_DO_REFRESH){
			flagList=new ArrayList<>();
			
			orderBookList.clear();
			flagList.add("");
			//booktask.execute(flagList);
			new DowloadOrderBookTask().execute(flagList);
			adapter.notifyDataSetChanged();
			segmentText.check(R.id.button_one);
			
			
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	
	private class DowloadOrderBookTask extends AsyncTask<ArrayList<String>, Void, HashMap<String,ArrayList<OrderBook>>>{
		@Override
	    protected void onPreExecute()
	    {
			Log.i("bookTask", "pre");
			if(!notify)
			dialog = ProgressDialog.show(OrderBookActivity.this, "", "Please wait...", true,false);
			notify=false;
	    }
		
    	@Override
		protected HashMap<String,ArrayList<OrderBook>> doInBackground(ArrayList<String>...arg) {
    		Log.i("bookTask", "background");
    		flagList = new ArrayList<String>();
    		flagList = arg[0];
    		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderBookActivity.this);
    		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
    		String uID=preferences.getString(ConstantsStrings.userPref, "");
			return OrderBookService.getOrderBook(uID,sID) ;
		}
		
		@Override
		protected void onPostExecute(HashMap<String,ArrayList<OrderBook>> list){
			Log.i("bookTask", "post");
			if(!notify)
			dialog.dismiss();
			if (list == null) {
				showAlert(ConstantsStrings.CONNECTION_ERROR_STR);

			}

			if (list != null) {
				if (list.containsKey("E")) {
					showAlert(ConstantsStrings.SESSION_FAIL_STRING);

				} else {
					orderBookList = list.get("OK");
					if (list.size() >= 0) {
						
						adapter = new OrderBookAdapter(OrderBookActivity.this,
								list.get("OK"),flagList);

						orderList.setAdapter(adapter);
						//orderList.setAdapter(adapter);
						//orderList.getChildAt(0).setBackgroundDrawable();
						View v= orderList.getChildAt(0);

						adapterFilled = new OrderBookAdapter(
								OrderBookActivity.this,
								OrderBookFilter.getFilledList(list.get("OK")),flagList);
						adapterQueued = new OrderBookAdapter(
								OrderBookActivity.this,
								OrderBookFilter.getQueuedList(list.get("OK")),flagList);
//						adapterAccOrder = new OrderBookAdapter(
//								OrderBookActivity.this,
//								OrderBookFilter.sortByAccount(list.get("OK")),flagList);
//						adapterStatusOder = new OrderBookAdapter(
//								OrderBookActivity.this,
//								OrderBookFilter.sortByStatus(list.get("OK")),flagList);
//						adapterStkOrder = new OrderBookAdapter(
//								OrderBookActivity.this,
//								OrderBookFilter.sortByStk(list.get("OK")),flagList);
//						adapterPriceOrder = new OrderBookAdapter(
//								OrderBookActivity.this,
//								OrderBookFilter.sortByPrice(list.get("OK")),flagList);
					}

				}

			}

		}
		
		
    	
    }
}
