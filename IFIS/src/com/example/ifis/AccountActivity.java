package com.example.ifis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.example.ifis.adapters.AccountAutoAdapter;
import com.example.ifis.adapters.CustodyStockAdapter;
import com.example.ifis.adapters.OrderBookAdapter;
import com.example.ifis.adapters.SpinnerCustomAdapter;
import com.example.ifis.model.AccountModel;
import com.example.ifis.model.CustodyStock;
import com.example.ifis.model.OrderBook;
import com.example.ifis.service.CustodyStockService;
import com.example.ifis.service.OrderBookService;
import com.example.ifis.utilities.OrderBookFilter;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AccountActivity extends Activity {

	ArrayList<CustodyStock> custodyStockList;
	SpinnerCustomAdapter adapterSp;
	ArrayList<AccountModel> CustomListViewValues;
	ArrayList<AccountModel> accListArr;
	String accID;
	String accName;
	String stockCode;
	Spinner accSpnr;
	ProgressDialog dialog;
	CustodyStockAdapter adapter;
	ListView stockListView;
	AutoCompleteTextView accAutoTV;
	AccountAutoAdapter accAutoAdapter;
	EditText stockET;
	EditText accDis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		Resources res = getResources();
		CustomListViewValues = new ArrayList<AccountModel>();
		stockListView = (ListView) findViewById(R.id.stocklistView1);
		stockListView.setEmptyView(findViewById(R.id.emptyTV2));
		stockListView.setTextFilterEnabled(true);
		stockET = (EditText) findViewById(R.id.stkAccET);
		setAccountsData();
		accDis=(EditText)findViewById(R.id.accDisplayET);
		setUPaccET();
		
		

		final ArrayList<AccountModel> alist = (ArrayList<AccountModel>) getParent()
				.getIntent().getSerializableExtra("Accounts");
		ArrayList<AccountModel> alist2 = (ArrayList<AccountModel>) alist
				.clone();
		alist2.remove(0);
		accAutoAdapter = new AccountAutoAdapter(AccountActivity.this,
				R.layout.accout_dropdown_cell, alist2);

		adapterSp = new SpinnerCustomAdapter(this, R.layout.spinner_row,
				CustomListViewValues, res);
		
		stockListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(AccountActivity.this,OrderEntryActivity.class);
//				i.putExtra("Accounts", alist);
//				startActivity(i);
//				ArrayList<AccountModel> CustomListViewValuesArr=(ArrayList<AccountModel>) getIntent().getSerializableExtra("Accounts");
        		Intent i = new Intent();
//        		i.putExtra("2", 2);
//        		i.putExtra("Accounts", CustomListViewValuesArr);
//        		startActivityForResult(i, 102);
        	
        		
        		CustodyStock cs = (CustodyStock) arg0.getItemAtPosition(arg2);
        		stockCode= cs.getStockCode();
        		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AccountActivity.this);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(ConstantsStrings.FILL_ACC_PREF, accID);
				//editor.putString(ConstantsStrings.FILL_ACC_NAME_PREF, "test");
				editor.putString("test", accName);
				editor.putString(ConstantsStrings.FILL_STOCK_PREF, stockCode);
				
				editor.commit();
				switchTabInActivity(0);
			}
		});


		stockET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String ssss= "I would like to request you to grant me half day leave tommorwo 26th jan 2015 as i have to attend a round of interview." +
						"i hope you accept my sincire request";

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (accID != null) {
					String qry = s.toString();
					adapter.getFilter().filter(qry);
				} else {
					Toast t = Toast.makeText(AccountActivity.this,
							"please selecet account", Toast.LENGTH_SHORT);
					t.show();
				}

			}
		});

	}

	private void setUPaccET() {
		accDis.setOnTouchListener(new View.OnTouchListener() {
			
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
	
	public void switchTabInActivity(int indexTabToSwitchTo) {
		HomeTabsActivity parentActivity;
		parentActivity = (HomeTabsActivity) this.getParent();
		parentActivity.switchTab(indexTabToSwitchTo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		// super.onBackPressed(); // Comment this super call to avoid calling
		// finish()
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setAccountsData();
		adapterSp = new SpinnerCustomAdapter(this, R.layout.spinner_row,
				CustomListViewValues, getResources());
	}
	
	public void didClickAccSrch(View v){
		clickAccSrch();
	}
	@SuppressWarnings("unchecked")
	public void clickAccSrch(){
		   final Dialog dialog = new Dialog(AccountActivity.this);

	        dialog.setContentView(R.layout.accounts_popup);
	        dialog.setTitle("Search Accounts");
	        final ArrayList<AccountModel> alist = (ArrayList<AccountModel>) getParent()
					.getIntent().getSerializableExtra("Accounts");
			ArrayList<AccountModel> alist2 = (ArrayList<AccountModel>) alist
					.clone();
			alist2.remove(0);
			final AccountAutoAdapter accAutoAdptr = new AccountAutoAdapter(AccountActivity.this,
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
					String accn=a.getAccountName().toString();
					accName=accn;
					accDis.setText(a.getAccountName().toString());
					
					new DowloadCustodyStocksTask().execute(accId);
					dialog.dismiss();
				}
			});
	        
	        

	        dialog.show();
	        
	}

	public void showAlert(final String message) {
		new AlertDialog.Builder(this)
				.setTitle("Status")
				.setMessage(message)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								if (message
										.equals(ConstantsStrings.ORDER_SUCCESS)) {

									ArrayList<AccountModel> CustomListViewValuesArr = (ArrayList<AccountModel>) getIntent()
											.getSerializableExtra("Accounts");

									Intent i = new Intent(AccountActivity.this,
											HomeTabsActivity.class);
									i.putExtra("1", 1);
									i.putExtra("Accounts",
											CustomListViewValuesArr);
									startActivityForResult(i, 102);
									// finish();
								}

								else if (message
										.equals(ConstantsStrings.SESSION_FAIL_STRING)
										|| message
												.equals(ConstantsStrings.SESSION_TIMEOUT)) {
									Intent i = new Intent(AccountActivity.this,
											LoginActivity.class);
									i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivityForResult(i,
											ConstantsStrings.SESSION_FAIL_CODE);
								}
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	public void didClickSearchBtn(View v) {
		if (accAutoTV.getVisibility() == View.INVISIBLE) {
			accAutoTV.setVisibility(View.VISIBLE);
			accAutoTV.requestFocus();
			if (accAutoTV.requestFocus()) {
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

			}

			accSpnr.setVisibility(View.INVISIBLE);
		} else {
			setAccountsData();
			accAutoTV.setVisibility(View.INVISIBLE);
			accSpnr.setVisibility(View.VISIBLE);
		}

	}

	@SuppressWarnings("unchecked")
	private void setAccountsData() {
		// TODO Auto-generated method stub
		Intent i = getIntent();

		if (i.hasExtra("Accounts")) {
			accListArr = new ArrayList<AccountModel>();
			CustomListViewValues = (ArrayList<AccountModel>) getParent()
					.getIntent().getSerializableExtra("Accounts");
			accListArr = (ArrayList<AccountModel>) getParent().getIntent()
					.getSerializableExtra("Accounts");
			// this.clearScreen();

		} else {
			accListArr = new ArrayList<AccountModel>();
			CustomListViewValues = (ArrayList<AccountModel>) getParent()
					.getIntent().getSerializableExtra("Accounts");
			accListArr = (ArrayList<AccountModel>) getParent().getIntent()
					.getSerializableExtra("Accounts");
		}
	}

	private class DowloadCustodyStocksTask extends
			AsyncTask<String, Void, HashMap<String, ArrayList<CustodyStock>>> {
		@Override
		protected void onPreExecute() {
			// emptyTV.setText("");
			dialog = ProgressDialog.show(AccountActivity.this, "",
					"Please wait...", true, false);

			// do initialization of required objects objects here
		};

		@Override
		protected HashMap<String, ArrayList<CustodyStock>> doInBackground(
				String... arg) {

			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(AccountActivity.this);
			String sID = preferences
					.getString(ConstantsStrings.sessionPref, "");
			String uID = preferences.getString(ConstantsStrings.userPref, "");
			return CustodyStockService.getCustodyStocks(sID, arg[0], "");
		}

		@Override
		protected void onPostExecute(
				HashMap<String, ArrayList<CustodyStock>> list) {
			dialog.dismiss();
			if (list == null) {
				showAlert(ConstantsStrings.CONNECTION_ERROR_STR);

			}

			if (list != null) {
				if (list.containsKey("E")) {
					showAlert(ConstantsStrings.SESSION_FAIL_STRING);

				} else {
					custodyStockList = list.get("OK");
					if (list.size() >= 0) {
						adapter = new CustodyStockAdapter(AccountActivity.this,
								list.get("OK"));
						stockListView.setAdapter(adapter);
					}

				}

			}

		}

	}

}
