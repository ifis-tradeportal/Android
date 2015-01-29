package com.example.ifis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.example.ifis.adapters.OrderBookAdapter;
import com.example.ifis.model.OrderBook;
import com.example.ifis.model.AccountModel;
import com.example.ifis.service.AmendOrderService;
import com.example.ifis.service.CancelOrderService;
import com.example.ifis.service.OrderBookService;
import com.example.ifis.service.OrderStatusService;
import com.example.ifis.utilities.OrderBookFilter;
import com.example.ifis.utilities.Validation;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailsActivity extends Activity {
	OrderBook ob;
	String recID;
	boolean clickCancel=false;
	boolean clickAmend=false;
	String newPrice;
	String newQty;
	String oldPrice;
	int oldQty;
	int matchQty;
	ProgressDialog dialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_order_details);
		setTitle("Order Details");
		
		Intent i = getIntent();
		if(i.hasExtra("order")){
			ob= (OrderBook) i.getSerializableExtra("order");
			recID= ob.getRefNo().toString();
			if(!displayButtons(ob.getOrderStatus().toString())){
				findViewById(R.id.twoButtons).setVisibility(View.GONE);
			}			
		}
		setTitle("Order Details");
		
		TextView stockNameTV= (TextView) findViewById(R.id.headingDetailTV);
		stockNameTV.setText(ob.getStockName().toString());
		
		TextView stockTV= (TextView) findViewById(R.id.stockTVd);
		stockTV.setText(ob.getStock().toString());	
		
		TextView ref= (TextView) findViewById(R.id.refNoTV);
		ref.setText(ob.getRefNo().toString());	
		
		TextView op= (TextView) findViewById(R.id.orderPriceDTV);
		float f= Float.parseFloat(ob.getOrderPrice());
		oldPrice=String.valueOf(f);
		String pr= String.format("%.3f", f);
		op.setText(pr);	
		
		TextView qf= (TextView) findViewById(R.id.qtyFilledTVd);
		int qq=Integer.parseInt(ob.getMatchQty());
		matchQty=qq;
		qf.setText(String.format("%,d", qq));
		
		TextView qt= (TextView) findViewById(R.id.qtyTVd);
		int oq= Integer.parseInt(ob.getOrderQty());
		oldQty=oq;
		qt.setText(String.format("%,d", oq));
		
		TextView accTV= (TextView) findViewById(R.id.accNoTVd);
		accTV.setText(ob.getClientAC().toString());
		
		TextView sts= (TextView) findViewById(R.id.statusTVd);
		sts.setText(ob.getOrderStatus().toString());
		
		TextView st= (TextView) findViewById(R.id.subTimeTVd);
		String odrTime=ob.getOrderDateTime().toString().replace('T', ' ');
		st.setText(odrTime);
		
		TextView act= (TextView) findViewById(R.id.actionTVd);
		String bs=ob.getBS();
		
		
		if(bs.toUpperCase().equals(ConstantsStrings.TRADE_SIDE_BUY)){
			act.setText(ob.getBS().toUpperCase().toString());
			act.setTextColor(this.getResources().getColor(R.color.buyGreen));
		}
		else{
			act.setText(ob.getBS().toUpperCase().toString());
			act.setTextColor(this.getResources().getColor(R.color.cellRed));
			
		}
				
		TextView ex= (TextView) findViewById(R.id.exgTVd);
		ex.setText(ob.getExchange().toString());		
				
		TextView avg= (TextView) findViewById(R.id.avgPriceTVd);
		avg.setText(ob.getAvePrice().toString());		
		TextView cur= (TextView) findViewById(R.id.currTVd);
		cur.setText(ob.getSettlementCurr().toString());	
		
	}
	
	public boolean displayButtons(String satus){
		List<String>  l= new ArrayList<>();
		l.add(ConstantsStrings.ORDER_STATUS_QUEUE);
		l.add(ConstantsStrings.ORDER_STATUS_PARTIALLY_FILLED);
		l.add(ConstantsStrings.ORDER_STATUS_REPLACED);
		l.add(ConstantsStrings.ORDER_STATUS_PART_CHANGE);
		if(l.contains(satus.toUpperCase())) return true;
		else return false;		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_details, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (MyApplication.wasInBackground) {
			SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this);
    		long outTime=preferences.getLong("outTime", 0);
    		long inTime = new Date().getTime();
    		Log.i("time ", String.valueOf(outTime)+"/"+String.valueOf(inTime));
    		long diff=(inTime-outTime);
    		if(diff>ConstantsStrings.SESSION_TIME){
    			Toast t=Toast.makeText(getApplicationContext(),
    					" Time Out !\n\nlogin Again.", Toast.LENGTH_SHORT);
    			t.setGravity(Gravity.CENTER, 0, 0);
    			t.show();
    			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this);
    			pref.edit().remove("outTime").commit();
    			Intent i = new Intent(OrderDetailsActivity.this,LoginActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivityForResult(i, ConstantsStrings.SESSION_TIME_OUT);
    		}
    		else{
    			SharedPreferences sharedPref = PreferenceManager
    					.getDefaultSharedPreferences(OrderDetailsActivity.this);
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			Intent i = new Intent(OrderDetailsActivity.this,LoginActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void didClickCancel(View v){		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetailsActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Cancel Order");
        alertDialog.setMessage("Are you sure you want to cancel this order?");
       
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
        			
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                    	new OrderStatusTask().execute();
                    }
                });
        
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        alertDialog.show();				
	}
	
	public void didClickAmend(View v){
		clickAmend=true;
		final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.amend_dialog);
        dialog.setTitle("Amend Order");
		
        TextView oldpriceTV= (TextView) dialog.findViewById(R.id.amendOPTV);
        TextView oQtyTV= (TextView) dialog.findViewById(R.id.amendOQTV);
        TextView mQtyTV= (TextView) dialog.findViewById(R.id.amendMQTV);
        
        oldpriceTV.setText(oldPrice);
        oQtyTV.setText(String.valueOf(oldQty));
        mQtyTV.setText(String.valueOf(matchQty));
        
        
        Button amendOk=(Button)dialog.findViewById(R.id.Amendok);
        Button btnCancel=(Button)dialog.findViewById(R.id.amendCancel);
        dialog.show();
        amendOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				amendOrder(dialog);				
			}
		});
        btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
	}
	
	public void amendOrder(Dialog d){
		
		EditText priceET= (EditText) d.findViewById(R.id.amendNewPET);
		EditText qtyET= (EditText) d.findViewById(R.id.amendNewQET);
		
		 newQty= qtyET.getText().toString();
       newPrice=priceET.getText().toString();
      // Write your code here to execute after dialog
  	if((newQty.trim().length()<=0)&&(newPrice.trim().length()<=0)){
  		
  		showAlertbox("please enter price and qty");
  	}
  	else{	
  		if((newPrice.trim().length()>0) &&(newQty.trim().length()<=0)){
  			newQty=String.valueOf(oldQty);
  			new OrderStatusTask2().execute();

  		}
  		else {
  			int x=	Validation.isAmendable(newQty, oldQty, matchQty, newPrice, "");
  			if(x==1){
//  				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//  				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
  				if(newPrice.trim().length()<=0){
  					newPrice=oldPrice;
  					new OrderStatusTask2().execute(); 
  				}
  				else new OrderStatusTask2().execute(); 				
  			}
  			else{
  				showAlert("invalid qty");
  			}
  		}
  	}
		
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
	        		Intent i = new Intent(OrderDetailsActivity.this,HomeTabsActivity.class);
	        		i.putExtra("1", 1);
	        		i.putExtra("Accounts", CustomListViewValuesArr);
	        		startActivityForResult(i, 102);
	        		//finish();
	        	}
	        	
	        	else if(message.equals(ConstantsStrings.MUTIPLE_LOGIN)){
	        		Intent i = new Intent(OrderDetailsActivity.this,LoginActivity.class);
	        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
	        	}
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
private class OrderStatusTask extends AsyncTask<String, Void, Integer>{
    	
	@Override
	protected void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();
	dialog = ProgressDialog.show(OrderDetailsActivity.this, "", "Please wait...", true,false);
	}
    	@Override
		protected Integer doInBackground(String...arg) {
    		//((ProgressBar) findViewById(R.id.CancelprogressBar)).setVisibility(ProgressBar.VISIBLE);
    		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this);
    		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
    		String uID=preferences.getString(ConstantsStrings.userPref, "");
			return OrderStatusService.getOrderStatus(recID, sID) ;
		}
		
		@Override
		protected void onPostExecute(Integer strStatus){
				
			if((strStatus == 0 || strStatus == 1 || strStatus == 5)){
				new CancelOrderTask().execute();		
			}
			else if(strStatus==-2){
				dialog.dismiss();
				showAlert(ConstantsStrings.MUTIPLE_LOGIN);
			}
			else if(strStatus==-1){
				dialog.dismiss();
				showAlert(ConstantsStrings.CONNECTION_ERROR_STR);
			}
			else{
				dialog.dismiss();
				Toast t = Toast.makeText(OrderDetailsActivity.this, "Can NOT Cancel pleas check status again",Toast.LENGTH_LONG );
				t.show();				
			}			
			}
				
		}
private class CancelOrderTask extends AsyncTask<String, Void, Integer>{
	
	@Override
	protected Integer doInBackground(String...arg) {
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this);
		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
		String uID=preferences.getString(ConstantsStrings.userPref, "");
		return  CancelOrderService.cancelOrder(recID, sID, uID);
	}
	
	@Override
	protected void onPostExecute(Integer res){
		if(res==1){
			Toast.makeText(getApplicationContext(), "Order Canceled sucessfully !",
					   Toast.LENGTH_SHORT).show();
			setResult(ConstantsStrings.RESULT_DO_REFRESH);
			finish();
		} 
		else if(res==-2) {
			dialog.dismiss();
			showAlert(ConstantsStrings.MUTIPLE_LOGIN);	
		}
		else if(res==-1){
			dialog.dismiss();
			showAlert(ConstantsStrings.CONNECTION_ERROR_STR);
		}
		else{
			dialog.dismiss();
			Toast.makeText(getApplicationContext(), "Cancel FAIL! Connection error",
					   Toast.LENGTH_SHORT).show();
		}
			
		}
			
	}


private class AmendOrderTask extends AsyncTask<String, Void, Integer>{
	
	@Override
	protected Integer doInBackground(String...arg) {
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this);
		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
		String uID=preferences.getString(ConstantsStrings.userPref, "");
		return  AmendOrderService.amendOrder(recID, sID, uID, newQty, newPrice, "2");
	}
	
	@Override
	protected void onPostExecute(Integer res){
		if(res==1){
			Toast.makeText(getApplicationContext(), "Order Amended sucessfully !",
					   Toast.LENGTH_SHORT).show();
			setResult(ConstantsStrings.RESULT_DO_REFRESH);
			finish();
			
		} 
		else if(res==-2) {
			dialog.dismiss();
			showAlert(ConstantsStrings.MUTIPLE_LOGIN);
			
		}
		else if(res==-1){
			dialog.dismiss();
			showAlert(ConstantsStrings.CONNECTION_ERROR_STR);
		}
		else{
			dialog.dismiss();
			Toast.makeText(getApplicationContext(), "Amend FAIL!Connection Error",
					   Toast.LENGTH_SHORT).show();			
		}		
		}
			
	}
private class OrderStatusTask2 extends AsyncTask<String, Void, Integer>{
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = ProgressDialog.show(OrderDetailsActivity.this, "", "Please wait...", true,false);
	}
	
	@Override
	protected Integer doInBackground(String...arg) {
		//((ProgressBar) findViewById(R.id.CancelprogressBar)).setVisibility(ProgressBar.VISIBLE);
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this);
		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
		String uID=preferences.getString(ConstantsStrings.userPref, "");
		return OrderStatusService.getOrderStatus(recID, sID) ;
	}
	
	@Override
	protected void onPostExecute(Integer strStatus){
			
		if((strStatus == 0 || strStatus == 1 || strStatus == 5)){
			new AmendOrderTask().execute();
			
		}else if(strStatus==-2){
			dialog.dismiss();
			showAlert(ConstantsStrings.MUTIPLE_LOGIN);
	
		}
		else if(strStatus==-1){
			dialog.dismiss();
			showAlert(ConstantsStrings.CONNECTION_ERROR_STR);
		}
		else{
			dialog.dismiss();
			Toast t = Toast.makeText(OrderDetailsActivity.this, "Can NOT Amend pleas check status again",Toast.LENGTH_LONG );
			t.show();		
		}
		}
			
	}


@SuppressWarnings("deprecation")
public void showAlertbox(String erroMessage){
AlertDialog alertDialog = new AlertDialog.Builder(
                OrderDetailsActivity.this).create();

      // Setting Dialog Title
   alertDialog.setTitle("Error Message");

    // Setting Dialog Message
     alertDialog.setMessage(erroMessage);


     // Setting OK Button
     alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

       }
        });

  // Showing Alert Message
 alertDialog.show();

}
		
		
    
}
