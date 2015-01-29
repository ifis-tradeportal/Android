package com.example.ifis;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.ifis.model.OrderInfo;
import com.example.ifis.model.AccountModel;
import com.example.ifis.model.Stock;
import com.example.ifis.service.CheckUserPwdService;

import com.example.ifis.service.NewOrderService;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OrderConfirmActivity extends Activity {
	boolean ondialog=false;
	OrderInfo order;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);		
		Stock sstk =(Stock) getIntent().getSerializableExtra("seletcteStock");
		Bundle bundle = getIntent().getExtras();
		setTitle("Order Confirmation");
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderConfirmActivity.this);		
		String uID=preferences.getString(ConstantsStrings.userPref, "");
		TextView clientAccTV=(TextView) findViewById(R.id.clientAccTV);
		TextView stockCodeTV=(TextView) findViewById(R.id.stockCodeTV);
		TextView stockNameTv=(TextView) findViewById(R.id.stockNameTV);
		TextView priceTV=(TextView) findViewById(R.id.priceTV);
		TextView qtyyTV=(TextView) findViewById(R.id.qtyyTV);
		TextView tlAmtTV=(TextView) findViewById(R.id.tlAmtTV);
		TextView setCurrencyTV=(TextView) findViewById(R.id.setCurrencyTV);
		TextView odrTypeTV=(TextView) findViewById(R.id.odrTypeTV);
		TextView routeDesTV=(TextView) findViewById(R.id.routeDesTV);
		
		//dummy values
		
		String tID=bundle.getString("accID");
		clientAccTV.setText(tID); 
		
		String symbol=sstk.getStockId().toString();
		stockCodeTV.setText(symbol);
		stockNameTv.setText(sstk.getStockName());
		stockNameTv.setTypeface(null, Typeface.BOLD);
		
		double priceD=Double.parseDouble(bundle.getString("price"));
		BigDecimal price= new BigDecimal(bundle.getString("price"));
		int qty=Integer.parseInt(bundle.getString("qty"));
		double amt=(priceD*qty);
		
		String priceDisplay=new DecimalFormat("#0.##").format(price);
		priceTV.setText(priceDisplay); 
		qtyyTV.setText(String.format("%,d", qty));

		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
		formatter.applyPattern("#,###,##0.00");
		tlAmtTV.setText(formatter.format(amt));
		tlAmtTV.setTypeface(null, Typeface.BOLD);
		
		setCurrencyTV.setText(sstk.getCurrencyName());
		String orderType=bundle.getString("orderType");
		if(orderType.equals("BUY")){
			odrTypeTV.setText(orderType);
			odrTypeTV.setTextColor(this.getResources().getColor(R.color.buyGreen));
		}
		else{
			odrTypeTV.setText(orderType);
			odrTypeTV.setTextColor(this.getResources().getColor(R.color.cellRed));
			
		}
		
		odrTypeTV.setTypeface(null, Typeface.BOLD);
		routeDesTV.setText(sstk.getExchange());
		
		//Creating Order object
		order= new OrderInfo();
		order.setTradeAccID(tID);
		order.setSymbol(symbol);
		order.setQuantity(qty);
		order.setPrice(price);
		if(orderType.equals("BUY"))
			order.setSide("1");
		else if(orderType.equals("SELL"))
			order.setSide("2");
		order.setOrderType("2");
		order.setTradeOfficer(uID);
		order.setExchange(sstk.getExchange());
		order.setTimeInForce("0");
		order.setSettCurr(sstk.getCurrencyName());
		order.setSplInstruction("");
		order.setUpdateBy(uID);
		order.setExgRate(sstk.getExchangeRate());
		order.setStockCurrency(sstk.getCurrencyName());
		order.setStockLocation("SGX");
		order.setForceOrderStatus(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_confirm, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (MyApplication.wasInBackground) {
			SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderConfirmActivity.this);
    		long outTime=preferences.getLong("outTime", 0);
    		long inTime = new Date().getTime();
    		Log.i("time ", String.valueOf(outTime)+"/"+String.valueOf(inTime));
    		long diff=(inTime-outTime);
    		if(diff>ConstantsStrings.SESSION_TIME){
    			Toast t=Toast.makeText(getApplicationContext(),
    					" Time Out !\n\nlogin Again.", Toast.LENGTH_SHORT);
    			t.setGravity(Gravity.CENTER, 0, 0);
    			t.show();
    			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(OrderConfirmActivity.this);
    			pref.edit().remove("outTime").commit();
    			Intent i = new Intent(OrderConfirmActivity.this,LoginActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivityForResult(i, ConstantsStrings.SESSION_TIME_OUT);
    		}
    		else{
    			SharedPreferences sharedPref = PreferenceManager
    					.getDefaultSharedPreferences(OrderConfirmActivity.this);
    			SharedPreferences.Editor editor = sharedPref.edit();
    			long now = new Date().getTime();
    			editor.putLong("outTime", now);
    			Log.i("log in time", String.valueOf(now));
    			editor.commit();
    			Toast.makeText(getApplicationContext(),
    					"back to foreground", Toast.LENGTH_SHORT)
    					.show();
    		}
			
			MyApplication.wasInBackground = false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			Intent i = new Intent(OrderConfirmActivity.this,LoginActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void didClickConfirm(View v){
		
		EditText passwordET = (EditText) findViewById(R.id.passwordEditText1);
		String password = passwordET.getText().toString();
    	
    	if(password.length() >0 ){
    		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderConfirmActivity.this);
    		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
    		String uID=preferences.getString(ConstantsStrings.userPref, "");
    		new CheckPwdTask().execute(uID, password,sID);    		
    	}else{
    		passwordET.setHintTextColor(getResources().
					getColor(R.color.red));
			passwordET.setText("");
			passwordET.setHint("Enter Password");
 
    }
	
	}
	
	public void didClickCancel(View v){
		Toast.makeText(getApplicationContext(), "Order Canceled",
				   Toast.LENGTH_SHORT).show();
		finish();
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	public void showAlert(final String message){
		new AlertDialog.Builder(this)
	    .setTitle("Status")
	    .setMessage(message)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	if(message.equals(ConstantsStrings.ORDER_SUCCESS)){
	        		@SuppressWarnings("unchecked")
					ArrayList<AccountModel> CustomListViewValuesArr=(ArrayList<AccountModel>) getIntent().getSerializableExtra("Accounts");
	        		Intent i = new Intent(OrderConfirmActivity.this,HomeTabsActivity.class);
	        		i.putExtra("1", 1);
	        		i.putExtra("Accounts", CustomListViewValuesArr);
	        		startActivityForResult(i, 102);
	        
	        	}
	        	
	        	else if(message.equals(ConstantsStrings.ORDER_FAIL)){
	        		Intent i = new Intent(OrderConfirmActivity.this,LoginActivity.class);
	        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
	        	}
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	
	private class NewOrderTask extends AsyncTask<OrderInfo, Void, Boolean>{
    	
    	@Override
		protected Boolean doInBackground(OrderInfo...arg) {
    		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderConfirmActivity.this);
    		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
			return NewOrderService.makeNewOrder(order, sID);
		}		
		@Override
		protected void onPostExecute(Boolean result){

			if(result){
				dialog.dismiss();
				showAlert(ConstantsStrings.ORDER_SUCCESS);
			}
			else{
				dialog.dismiss();
				showAlert(ConstantsStrings.ORDER_FAIL);
				
			}
		}  	
    }
	
private class CheckPwdTask extends AsyncTask<String, Void, Integer>{
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(OrderConfirmActivity.this, "", "Please wait...", true,false);
		super.onPreExecute();
	}
    	
	@Override
		protected Integer doInBackground(String...arg) {
			return CheckUserPwdService.CheckUserPwd(arg[0], arg[1]);
		}
		
		@Override
		protected void onPostExecute(Integer result){
			if(result==0){
				dialog.dismiss();
				showAlert("invalid password");
			}
			else if(result==-1){
				dialog.dismiss();
				showAlert(ConstantsStrings.CONNECTION_ERROR_STR);
			}
			else if (result==1) {
				
				new NewOrderTask().execute(order);
	
			}
			
		}
    	
    }


	
	
}
