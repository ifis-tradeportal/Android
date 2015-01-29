package com.example.ifis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.example.ifis.service.CheckService;
import com.example.ifis.service.StockAutoCompleteService;
import com.example.ifis.service.StockListFromDbService;
import com.example.ifis.service.NewOrderService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ifis.adapters.AccountAutoAdapter;
import com.example.ifis.adapters.SpinnerCustomAdapter;
import com.example.ifis.model.AccountModel;
import com.example.ifis.model.Stock;

public class OrderEntryActivity extends Activity {
	String[] buyORSell = {
		      "BUY",
		      "SELL"		     
		  };
	
	public  ArrayList<AccountModel> CustomListViewValuesArr = new ArrayList<AccountModel>();
    TextView output = null;
    TextView sellLt;
    TextView buyLt;
    SpinnerCustomAdapter adapterSp;
    OrderEntryActivity activity = null;
	Stock sstk;
	String accID="";
	AutoCompleteTextView autoText;
	ArrayAdapter<String> adapter;
	String buyOrSell="BUY";
	EditText accET;
	private Filter filter;
	private static final int ADDRESS_TRESHOLD = 2;
	ProgressBar stockProgress;
	EditText priceET;
	TextView stockNameTV;
	TextView lastTV;
	TextView bidTV;
	TextView changeTV;
	TextView askTV;
	TextView lotSizeTV;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_entry);
		accET= (EditText) findViewById(R.id.accDisp1);
		priceET= (EditText) findViewById(R.id.orderPriceET);
		buyLt=(TextView) findViewById(R.id.buyLtTV);
		sellLt=(TextView) findViewById(R.id.sellLtTV);
		stockProgress=(ProgressBar) findViewById(R.id.stockProcess);
		final Button buySell =  (Button) findViewById(R.id.buyORsellBtn);
		//set data for the accounts dropdown
		setAccountsData();
		//new DownloadAccountsTask().execute("hy1");
		final Resources res = getResources(); 
		//clearAutoText();
		//custom addapter object for spinner
		adapterSp = new SpinnerCustomAdapter(this, R.layout.spinner_row, CustomListViewValuesArr,res);
		accSearchSetup();
		setTitle("Trade");
		//radio button setup	
		RadioGroup group=(RadioGroup) findViewById(R.id.radioGroup1);
		final RadioButton rBuy=(RadioButton)findViewById(R.id.radio0);
		final RadioButton rSell=(RadioButton)findViewById(R.id.radio1);
		
		
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() 
           {
            public void onCheckedChanged(RadioGroup group, int checkedId) 
               {
                // TODO Auto-generated method stub
                if(rBuy.isChecked())
                  {
                	buyOrSell="BUY";
                	//buySell.setBackgroundColor(OrderEntryActivity.this.getResources().getColor(R.color.buyGreen));
                	buySell.setText("BUY");
                	buySell.setBackgroundDrawable(getResources().getDrawable(R.drawable.greenbutton));
                  }
                else if(rSell.isChecked())
                  {
                	buyOrSell="SELL";
                	buySell.setBackgroundDrawable(getResources().getDrawable(R.drawable.redbutton));
                	buySell.setText("SELL");
                  }
             }
        });

        
        
		

		//test views of stock description
		 stockNameTV= (TextView) findViewById(R.id.stockNameTv);
		 lastTV= (TextView) findViewById(R.id.lastTvValue);
		 bidTV= (TextView) findViewById(R.id.bidTvValue);
		 changeTV= (TextView) findViewById(R.id.changeTv);
		 askTV= (TextView) findViewById(R.id.askTvValue);
		 lotSizeTV= (TextView) findViewById(R.id.lotSizeTvValue);
		 stockProgress.setVisibility(View.INVISIBLE);
		

		//dummy values for testing
		
		stockNameTV.setText("-");
		//lastTV.setText("Last :"+" -");
		//bidTV.setText("Bid :"+" -");
		
		changeTV.setText("Change :"+" - %");
		changeTV.setTextColor(this.getResources().getColor(R.color.buyGreen));
		//askTV.setText("Ask :"+" -");
		//lotSizeTV.setText("Lot Size :"+" -");
		
		//Auto complete text Setup
		
		autoText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoText.setThreshold(ADDRESS_TRESHOLD);
		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		
		autoText.setDropDownHeight((int)(height*0.4));
		autoText.addTextChangedListener(new TextWatcher() {
			
            private boolean shouldAutoComplete = true;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shouldAutoComplete = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                	new AdapterUpdaterTask().execute(); 	
                }
            }
            
        });
		
		autoText.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			        // TODO Auto-generated method stub
				 String s= (String)arg0.getItemAtPosition(arg2);
				 s = s.substring(s.indexOf("(") + 1);
				 s = s.substring(0, s.indexOf(")"));
				 
				 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				 imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				 //sstk.setStockId(s);
				 clearScreen();
				 new FillStockDetailsTask().execute(s);
			                        }
			                });
		
		
		filter = new Filter() {
		      @Override
		      protected void publishResults(CharSequence constraint,
		          FilterResults results) {
		      }

		      @Override
		      protected FilterResults performFiltering(CharSequence constraint) {
		        Log.i("Filter",
		            "Filter:" + constraint + " thread: " + Thread.currentThread());
		        if (constraint != null && constraint.length() > ADDRESS_TRESHOLD) {
		          Log.i("Filter", "doing a search ..");
		          new AdapterUpdaterTask().execute();
		        }
		        return null;
		      }
		    };
		    
		    adapter = new ArrayAdapter<String>(this,
		            android.R.layout.simple_dropdown_item_1line) {
		          public android.widget.Filter getFilter() {
		            return filter;
		          }
		        };
		        	
		        autoText.setAdapter(adapter);
		        adapter.setNotifyOnChange(false);    
		     touchTheLables();   

		        
	}

	private void touchTheLables() {
		// TODO Auto-generated method stub
		lastTV.setOnTouchListener(new View.OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					String price=lastTV.getText().toString();
					priceET.setText(price);
		            }
				
				Log.i("click text", "kakak");
				return false;
			}
		});
		
		bidTV.setOnTouchListener(new View.OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					String price=bidTV.getText().toString();
					priceET.setText(price);
		            }
				
				Log.i("click text", "kakak");
				return false;
			}
		});
		
		askTV.setOnTouchListener(new View.OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					String price=askTV.getText().toString();
					priceET.setText(price);
		            }
				
				Log.i("click text", "kakak");
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_entry, menu);
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		
	   // super.onBackPressed(); // Comment this super call to avoid calling finish()
	}
	
	
	public void accSearchSetup(){
		accET.setOnTouchListener(new View.OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					clickAccSrch();
		            }
				
				Log.i("click text", "kakak");
				return false;
			}
		});
	}
	
	public void didClickAccSrch(View v){
		clickAccSrch();
	}
	
	@SuppressWarnings("unchecked")
	public void clickAccSrch(){
		   final Dialog dialog = new Dialog(OrderEntryActivity.this);

	        dialog.setContentView(R.layout.accounts_popup);
	        dialog.setTitle("Search Accounts");
	        final ArrayList<AccountModel> alist = (ArrayList<AccountModel>) getParent()
					.getIntent().getSerializableExtra("Accounts");
			ArrayList<AccountModel> alist2 = (ArrayList<AccountModel>) alist
					.clone();
			alist2.remove(0);
			final AccountAutoAdapter accAutoAdptr = new AccountAutoAdapter(OrderEntryActivity.this,
					R.layout.accout_dropdown_cell, alist2);

	        final EditText fpUsrName=(EditText)dialog.findViewById(R.id.AccSrchET);
	        ListView accLv= (ListView) dialog.findViewById(R.id.AccListView);
	        accLv.setTextFilterEnabled(true);
	        accLv.setAdapter(accAutoAdptr);
	        fpUsrName.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
					accAutoAdptr.getFilter().filter(s);
				}
			});
	        
	        accLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					//TextView accDis=(TextView) dialog.findViewById(R.id.accDisplayET);
					AccountModel a = (AccountModel) arg0.getItemAtPosition(arg2);
					String accId = a.getAccountId();
					accID=accId;
					accET.setText(a.getAccountName().toString());
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					 imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					//new DowloadCustodyStocksTask().execute(accId);
					 new AccountLmts().execute(accID);
					dialog.dismiss();
				}
			});
	        dialog.show();
	        
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("entry", "on act resume");
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderEntryActivity.this);
		String acc=preferences.getString(ConstantsStrings.FILL_ACC_PREF, "");
		String stkCode=preferences.getString(ConstantsStrings.FILL_STOCK_PREF, "");
		String accNme=preferences.getString("test", "");
		if(!acc.equals("")){
			Log.i("acc", acc);
			Log.i("stk",stkCode);
			Log.i("accnme",accNme);
			
			clearScreen();
			autoText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
//			autoText.setText("");
//			
//			autoText.post(new Runnable() {
//			    public void run() {
//			    	autoText.dismissDropDown();
//			    }
//			});
			
			new FillStockDetailsTask().execute(stkCode);
			accET.setText(accNme);
			accID=acc;
			
			preferences.edit().remove(ConstantsStrings.FILL_ACC_PREF).commit();
			preferences.edit().remove(ConstantsStrings.FILL_ACC_NAME_PREF).commit();
			preferences.edit().remove(ConstantsStrings.FILL_STOCK_PREF).commit();
			
		}
		
		setAccountsData();
		adapterSp = new SpinnerCustomAdapter(this, R.layout.spinner_row, CustomListViewValuesArr,getResources());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		Log.i("entry", "on act result");
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i("entry", "on act restart");
		super.onRestart();
	}
	
	public void didClickBuyButton(View v){
		boolean validate= this.validatePage();
		if(validate){
			Intent i = new Intent(OrderEntryActivity.this,OrderConfirmActivity.class);
			//priceET= (EditText) findViewById(R.id.orderPriceET);
			String price= priceET.getText().toString();
			i.putExtra("price", price);
			EditText qtyET= (EditText) findViewById(R.id.qtyET);
			String qty= qtyET.getText().toString();
			i.putExtra("accID", accID);
			i.putExtra("qty", qty);
			i.putExtra("Accounts", CustomListViewValuesArr);
			i.putExtra("orderType", buyOrSell);
			i.putExtra("seletcteStock", sstk);
			startActivity(i);	
		}
		
		
	}
	
	private void clearScreen(){
		
		TextView stockNameTV= (TextView) findViewById(R.id.stockNameTv);
		TextView lastTV= (TextView) findViewById(R.id.lastTvValue);
		TextView bidTV= (TextView) findViewById(R.id.bidTvValue);
		TextView changeTV= (TextView) findViewById(R.id.changeTv);
		TextView askTV= (TextView) findViewById(R.id.askTvValue);
		TextView lotSizeTV= (TextView) findViewById(R.id.lotSizeTvValue);
	
		stockNameTV.setText("-");
		lastTV.setText(" -");
		bidTV.setText(" -");	
		changeTV.setText("Change :"+" - %");
		changeTV.setTextColor(this.getResources().getColor(R.color.buyGreen));
		askTV.setText(" -");
		lotSizeTV.setText(" -");
		
		EditText priceET= (EditText) findViewById(R.id.orderPriceET);
		priceET.getText().clear();
		//String price= priceET.getText().toString();
		EditText qtyET= (EditText) findViewById(R.id.qtyET);
		qtyET.getText().clear();
		
	}
	
	private void setAccountsData() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		
		if(i.hasExtra("Accounts")){
			CustomListViewValuesArr=(ArrayList<AccountModel>) getParent().getIntent().getSerializableExtra("Accounts");
			//this.clearScreen();
			
		}
		else{
		CustomListViewValuesArr=(ArrayList<AccountModel>) getParent().getIntent().getSerializableExtra("Accounts");
		}
	}
	
	private boolean validatePage(){
		boolean valid=true;
		TextView stockNameTV= (TextView) findViewById(R.id.stockNameTv);
		String s= stockNameTV.getText().toString();
		EditText priceET= (EditText) findViewById(R.id.orderPriceET);
		String price= priceET.getText().toString();
		EditText qtyET= (EditText) findViewById(R.id.qtyET);
		String qty= qtyET.getText().toString();
		
		if(s.equals("-")){
			valid= false;
			Toast t = Toast.makeText(OrderEntryActivity.this, "No Stock Selected", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return valid;
		}
		
		if(accID.equals("")){
			valid=false;
			Toast t = Toast.makeText(OrderEntryActivity.this, "Select account", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return valid;
		}
		
		if(price.isEmpty()||qty.isEmpty()||price.equals(".")||(Double.valueOf(price)==0)){
			valid=false;
			Toast t = Toast.makeText(OrderEntryActivity.this, "Enter qty & Price", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return valid;	
		}
		
//		if(checkClinteTrateLmt(buyLt.getText().toString(), priceET.getText().toString(), qtyET.getText().toString(), sstk.getExchangeRate())==2){
//			Toast t = Toast.makeText(OrderEntryActivity.this, "limit ok", Toast.LENGTH_SHORT);
//			return true;
//		}
//		else{
//			Toast t = Toast.makeText(OrderEntryActivity.this, "limit exceeded", Toast.LENGTH_SHORT);
//			return false;
//		}
		return valid;
		
	}
	
	public void clearAutoText(){
		String value="";
		final AutoCompleteTextView et = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);;
		et.setText("");
		final Drawable x = getResources().getDrawable(R.drawable.ic_action_close);//your x image, this one from standard android images looks pretty good actually
		//x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		//et.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
		et.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if (et.getCompoundDrawables()[2] == null) {
		            return false;
		        }
		        if (event.getAction() != MotionEvent.ACTION_UP) {
		            return false;
		        }
		        if (event.getX() > et.getWidth() - et.getPaddingRight() - x.getIntrinsicWidth()) {
		            et.setText("");
		            et.setCompoundDrawables(null, null, null, null);
		        }
		        return false;
		    }			
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		autoText.dismissDropDown();
//		autoText.getAdapter().
		Log.i("entry", "paused");
		super.onPause();
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
	        		Intent i = new Intent(OrderEntryActivity.this,HomeTabsActivity.class);
	        		i.putExtra("1", 1);
	        		i.putExtra("Accounts", CustomListViewValuesArr);
	        		startActivityForResult(i, 102);
	        		//finish();
	        	}
	        	
	        	else if(message.equals(ConstantsStrings.MUTIPLE_LOGIN)||message.equals(ConstantsStrings.SESSION_TIMEOUT)){
	        		Intent i = new Intent(OrderEntryActivity.this,LoginActivity.class);
	        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
	        	}
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	public int checkClinteTrateLmt(String strTradeLmt,String price,String qty,String exgRtt){
		double leftOverLmt=0;
		if(Double.valueOf(exgRtt)>0){
			leftOverLmt=Double.valueOf(strTradeLmt)-((Double.valueOf(price)*Double.valueOf(qty))/Double.valueOf(exgRtt));
			if(leftOverLmt>0){
				return 1;
			}
			else return 2;
		}
		else return 0;
	}
	
	
	
		
	public class AdapterUpdaterTask extends AsyncTask<Void, Void, Void> {
	    List<String> simpleAddresses = new ArrayList<String>();

	    @Override
	    protected Void doInBackground(Void... voids) {
	      Log.i("UPDATE", "1");
	      try {
	        simpleAddresses = StockAutoCompleteService.getAddressesFromText(autoText.getText().toString());
	      } catch (NullPointerException e) {
	      }
	      Log.i("UPDATE", "2");
	      return null;
	    }

	    @SuppressWarnings("unchecked")
		@Override
	    protected void onPostExecute(Void aVoid) {
	      Log.i("UPDATE", "3");

	      try{
	    	  if(simpleAddresses.size()>=0){
		    	  int size = simpleAddresses.size();
		    	  adapter = (ArrayAdapter<String>) autoText.getAdapter();
		    	  adapter.clear();
			        Log.i("ADAPTER_SIZE", "" + size);
			        for (int i = 0; i < size; i++) {
			          adapter.add(simpleAddresses.get(i));
			          Log.i("ADDED", simpleAddresses.get(i));
			        }
			        Log.i("UPDATE", "4");

			        adapter.notifyDataSetChanged();
			        autoText.getHandler().post(new Runnable() {
			            @Override
			            public void run() {
			                autoText.showDropDown();
			            }
			        });
		    	  
		      }
	    	  
	      }catch(Exception e){
	    	  adapter.clear();
	      }
     
	      super.onPostExecute(aVoid);
	    }
	  }

	
	public class FillStockDetailsTask extends AsyncTask<String, Void, Stock> {
	    Stock stk;

	    @Override
	    protected void onPreExecute() {
	    	// TODO Auto-generated method stub
	    	stockProgress.setVisibility(View.VISIBLE);
	    	super.onPreExecute();
	    }
	    @Override
	    protected Stock doInBackground(String... args) {
	      Log.i("UPDATE", "1");
	      try {
	        sstk = StockListFromDbService.getStockDetails(args[0]);
	      } catch (NullPointerException e) {
	      }
	      Log.i("UPDATE", "2");
	      return null;
	    }

	    @Override
	    protected void onPostExecute(Stock aVoid) {
	      Log.i("UPDATE", "3");

	      try{
	    	  stockProgress.setVisibility(View.INVISIBLE);
	    	TextView stockNameTV= (TextView) findViewById(R.id.stockNameTv);
	  		TextView lastTV= (TextView) findViewById(R.id.lastTvValue);
	  		TextView bidTV= (TextView) findViewById(R.id.bidTvValue);
	  		TextView changeTV= (TextView) findViewById(R.id.changeTv);
	  		TextView askTV= (TextView) findViewById(R.id.askTvValue);
	  		TextView lotSizeTV= (TextView) findViewById(R.id.lotSizeTvValue);
	  		TextView exgTV= (TextView) findViewById(R.id.destinationTV);  
	    	
	  		double last= Double.parseDouble(sstk.getLastDonePrice());
			String lstStr= String.format("%.3f", last);
			
			double bid= Double.parseDouble(sstk.getBidPrice());
			String bidStr= String.format("%.3f", bid);
			
			double ask= Double.parseDouble(sstk.getAskPrice());
			String askStr= String.format("%.3f", ask);
			
	  		
	    	stockNameTV.setText(sstk.getStockName());
	  		lastTV.setText(lstStr);
	  		bidTV.setText(bidStr);
	  		exgTV.setText(sstk.getExchange());
	  		changeTV.setText("Change :"+" 0.010 %");
	  		changeTV.setTextColor(OrderEntryActivity.this.getResources().getColor(R.color.buyGreen));
	  		askTV.setText(askStr);
	  		lotSizeTV.setText(sstk.getLotSize());
	  		autoText.clearComposingText();	  		
	  		
    	  
	      }catch(Exception e){
	    	  adapter.clear();
	      }
  
	      super.onPostExecute(aVoid);
	    }
	  }
	
	private class AccountLmts extends AsyncTask<String, Void, String[]>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//dialog = ProgressDialog.show(SettingsActivity.this, "", "Checking Connections...", true,false);
		}
		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderEntryActivity.this);
    		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
			String[] limts= NewOrderService.GetClientAccountTradeLimit(sID, params[0]);
			return limts;
			
		}
		@Override
		protected void onPostExecute(String[] result) {
			if(result[0].equals("OK")){
				sellLt.setText(result[2]);
				buyLt.setText(result[1]);
			}
		}
	}
	
}
