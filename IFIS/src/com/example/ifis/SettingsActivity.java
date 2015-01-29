package com.example.ifis;

import java.util.ArrayList;
import java.util.Date;

import com.example.ifis.model.AccountModel;
import com.example.ifis.service.ChangePasswordService;
import com.example.ifis.service.CheckService;
import com.example.ifis.adapters.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends Activity {

	String userID;
	ProgressDialog dialog;
	SettingsAdapter adapter;
	Switch notificationSwitch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setTitle("Settings");
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
		userID=preferences.getString(ConstantsStrings.userPref, "");
		String genOptions[]={"Change Password","Change Service","Forget Device"};
		ListView setingList1= (ListView) findViewById(R.id.genSettingList);
		adapter = new SettingsAdapter(SettingsActivity.this);
		
//		ArrayAdapter<String> adapter = new ArrayAdapter(this, 
//                android.R.layout.simple_list_item_1, 
//                genOptions);
		setingList1.setAdapter(adapter);
	
		setingList1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int item,
					long arg3) {
				// TODO Auto-generated method stub
				switch (item) {
				case 0:
					didClickChangePassword();
					break;
				case 1:
					didClickChangeService();
					break;
				case 2:
					forgetThisDevice();
					
						
				default:
					break;
				}
				
				
			}

			
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void didClickChangePassword(){
		final Dialog dialog = new Dialog(this);
		
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Change Password");
        EditText uid= (EditText) dialog.findViewById(R.id.pwduserIDTV);
		uid.setText(userID);
        final EditText editText=(EditText)dialog.findViewById(R.id.editText);
        Button save=(Button)dialog.findViewById(R.id.save);
        Button btnCancel=(Button)dialog.findViewById(R.id.cancel);
        //click on save button
        dialog.show();
        save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				changePwdSubmint(dialog);

			}
		});
        //
        btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		
	}
	
	public void didClickChangeService(){
//		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
//		SharedPreferences.Editor editor = sharedPref.edit();
//		editor.putString(ConstantsStrings.IP_SELECTED_SHARE, "test ip");
//		editor.commit();
		
//		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
//		preferences.edit().remove(ConstantsStrings.IP_SELECTED_SHARE).commit();
		ConstantsStrings cs = new ConstantsStrings();
		String currIP=cs.getSelectedIP();
		final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_service_dialog);
        dialog.setTitle("Change Service");
        
        TextView currenIPTV= (TextView) dialog.findViewById(R.id.currentIPTV);
        
        String curip=currIP.substring(7,(currIP.length()-1));
        currenIPTV.setText(curip);
        final EditText newIPET=(EditText)dialog.findViewById(R.id.newIPET);
        
        Button dft=(Button)dialog.findViewById(R.id.defaultIPbtn);
        Button saveIP=(Button)dialog.findViewById(R.id.saveIPbtn);
        Button cncl=(Button)dialog.findViewById(R.id.cnclIPbtn);
        //click on save button
        dialog.show();
        
        saveIP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
				SharedPreferences.Editor editor = sharedPref.edit();
				String newipStr= newIPET.getText().toString();
				editor.putString(ConstantsStrings.IP_SELECTED_SHARE,"http://"+ newipStr+"/");
				editor.commit();
				new CheckConnection().execute("test");
				dialog.dismiss();
				
			}
		});
		dft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
				preferences.edit().remove(ConstantsStrings.IP_SELECTED_SHARE).commit();
				dialog.dismiss();
			}
		});
		cncl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		
	}
	
	private void forgetThisDevice() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean changePwdSubmint(Dialog d){
		TextView uid= (TextView) d.findViewById(R.id.pwduserIDTV);
		uid.setText(userID);
		EditText oldpwd= (EditText) d.findViewById(R.id.oldPwdET);
		EditText newpwd= (EditText) d.findViewById(R.id.newPwd1ET);
		EditText newpwd2= (EditText) d.findViewById(R.id.newPwd2ET);
		//String uidStr= uid.getText().toString();
		String oldPwStr= oldpwd.getText().toString();
		String newPwStr= newpwd.getText().toString();
		String newPw2Str= newpwd2.getText().toString();
		
		if(oldPwStr.length()>0&& newPwStr.length()>0 && newPw2Str.length()>0 ){
			if(newPwStr.equals(newPw2Str)){
				new changePwdTask().execute(userID,oldPwStr,newPwStr);
				d.dismiss();
				return true;
			}
			else{
				newpwd.setText("");
				newpwd.setHint("mismatch");
				newpwd2.setText("");
				newpwd2.setHint("mismatch");
				return false;
			}
		}
		else{
			Toast t= Toast.makeText(SettingsActivity.this, "fill all fields", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return false;
		}
		
	}
	
	private class changePwdTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return ChangePasswordService.changePassword(params[0], params[1], params[2]);
			
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result){
				Toast t =Toast.makeText(SettingsActivity.this, "password change successful", Toast.LENGTH_SHORT);
				t.show();
			}
			else{
				Toast t =Toast.makeText(SettingsActivity.this, "password change FAIL!!", Toast.LENGTH_SHORT);
				t.show();		
			}
		}
	}
	
	private class CheckConnection extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(SettingsActivity.this, "", "Checking Connections...", true,false);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return CheckService.CheckSession(params[0]);
			
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if(result){
				showAlert("Connection Successful! IP changed");
			}
			else{
//				SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
//				preferences.edit().remove(ConstantsStrings.IP_SELECTED_SHARE).commit();
//				showAlert("Connecion failed! IP set to Default");		
			}
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (MyApplication.wasInBackground) {
			SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
    		long outTime=preferences.getLong("outTime", 0);
    		long inTime = new Date().getTime();
    		Log.i("time ", String.valueOf(outTime)+"/"+String.valueOf(inTime));
    		long diff=(inTime-outTime);
    		if(diff>ConstantsStrings.SESSION_TIME){
    			Toast t=Toast.makeText(getApplicationContext(),
    					" Time Out !\n\nlogin Again.", Toast.LENGTH_SHORT);
    			t.setGravity(Gravity.CENTER, 0, 0);
    			t.show();
    			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
    			pref.edit().remove("outTime").commit();
    			Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivityForResult(i, ConstantsStrings.SESSION_TIME_OUT);
    		}
    		else{
    			SharedPreferences sharedPref = PreferenceManager
    					.getDefaultSharedPreferences(SettingsActivity.this);
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
	        		Intent i = new Intent(SettingsActivity.this,HomeTabsActivity.class);
	        		i.putExtra("1", 1);
	        		i.putExtra("Accounts", CustomListViewValuesArr);
	        		startActivityForResult(i, 102);
	        		//finish();
	        	}
	        	
	        	else if(message.equals(ConstantsStrings.ORDER_FAIL)){
	        		Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
	        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
	        	}
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
}
