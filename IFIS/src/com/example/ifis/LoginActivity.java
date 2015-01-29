package com.example.ifis;





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.example.ifis.model.AccountModel;
import com.example.ifis.service.AuthenticateUserService;
import com.example.ifis.service.GetAccountsService;
import com.example.ifis.utilities.RandomSessionString;

//import com.google.android.gcm.GCMRegistrar;
import com.parse.Parse;
import com.parse.PushService;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public  ArrayList<AccountModel> CustomListViewValuesArr = new ArrayList<AccountModel>();
	public static Context contextOfApplication;
	EditText usernameET;
	EditText passwordET;
	ProgressDialog dialog;
	MyApplication aMyApplication;
	 AsyncTask<Void, Void, Void> mRegisterTask;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		contextOfApplication = getApplicationContext();
        aMyApplication = (MyApplication) getApplicationContext();
        
        
    
        
        ParseAnalytics.trackAppOpened(getIntent());
   
      	       
        // Check if Internet present
        if (!aMyApplication.isConnectingToInternet()) {
             
            //MyApplicationConnection is not present
            aMyApplication.showAlertDialog(LoginActivity.this,
                    "Internet Connection Error",
                    "Please connect to Internet connection", false);
            // stop executing code by return
            return;
        }
        
        // Make sure the device has the proper dependencies.
      //  GCMRegistrar.checkDevice(this);
        
        // Make sure the manifest permissions was properly set 
      //  GCMRegistrar.checkManifest(this);   
        
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                ConstantsStrings.DISPLAY_MESSAGE_ACTION));
        
        // Get GCM registration id
//        final String regId = GCMRegistrar.getRegistrationId(this);
        
        // Check if regid already presents
//        if (regId.equals("")) {
//             
//            // Register with GCM            
//        //    GCMRegistrar.register(this, ConstantsStrings.GOOGLE_SENDER_ID);
//             
//        } else {
//             
//            // Device is already registered on GCM Server
//            if (GCMRegistrar.isRegisteredOnServer(this)) {
//                 
//                // Skips registration.              
//                Toast.makeText(getApplicationContext(), 
//                              "Already registered with GCM Server", 
//                              Toast.LENGTH_LONG).
//                              show();
//             
//            } else {
//                 
//                // Try to register again, but not in the UI thread.
//                // It's also necessary to cancel the thread onDestroy(),
//                // hence the use of AsyncTask instead of a raw thread.
//                 
//                final Context context = this;
//                mRegisterTask = new AsyncTask<Void, Void, Void>() {
// 
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                         
//                        // Register on our server
//                        // On server creates a new user
//                        //aMyApplication.register(context, name, email, regId);
//                         
//                        return null;
//                    }
// 
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        mRegisterTask = null;
//                    }
// 
//                };
//                 
//                // execute AsyncTask
//                mRegisterTask.execute(null, null, null);
//            }
//   
//        }
//        
//        String kk= GCMRegistrar.getRegistrationId(this);
//        kk=kk+"";
		
		usernameET= (EditText) findViewById(R.id.usernameEditText);
		passwordET = (EditText) findViewById(R.id.passwordEditText);
		
		TextView changeTV=(TextView) findViewById(R.id.changeSvcTV);
		changeTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				didClickChangeService();
			}
		});
		
		TextView tv = (TextView)findViewById(R.id.changePwdTV);
		tv.setOnClickListener(new View.OnClickListener() {
			   @Override
			   public void onClick(View v) {
				   Log.i("change pass", "onclick");
				   final Dialog dialog = new Dialog(LoginActivity.this);

			        dialog.setContentView(R.layout.forgot_password_dialog);
			        dialog.setTitle("Forget Password");

			        final EditText fpUsrName=(EditText)dialog.findViewById(R.id.fpUerNameET);
			        
			        final Button fpSubmit=(Button)dialog.findViewById(R.id.fpSubmitBtn);
			        Button btnCancel=(Button)dialog.findViewById(R.id.fpCancelBtn);
			        fpSubmit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String fpUsr=fpUsrName.getText().toString();
							if(fpUsr.length()==0){
								fpUsrName.setHint("Enter UserName");
							}
							else{
								dialog.dismiss();
							}
							
						}
					});
			        
			        btnCancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
						}
					});
			        
			        dialog.show();
			   }
			});
		
		
	}
	
	public static Context getContextOfApplication(){
	    return contextOfApplication;
	    //this method to get shared prefs in non-activiy classes
	}
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
             
            String newMessage = intent.getExtras().getString(ConstantsStrings.EXTRA_MESSAGE);
             Log.i("received login****", newMessage);
            // Waking up mobile if it is sleeping
            aMyApplication.acquireWakeLock(getApplicationContext());
             
            // Display message on the screen
            //lblMessage.append(newMessage + "");        
             
            Toast.makeText(getApplicationContext(), 
                            newMessage, 
                           Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            aMyApplication.releaseWakeLock();
        }
    };
     
    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);
             
            //Clear internal resources.
           // GCMRegistrar.onDestroy(this);
             
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", 
                      "> " + e.getMessage());
        }
        super.onDestroy();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==ConstantsStrings.SESSION_TIME_OUT){
			showAlert("Session has Timed out, please login again!");
		}	
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
	
	public void didClickLoginButton(View v){
    	String username = usernameET.getText().toString();
    	String password = passwordET.getText().toString();
    	
    	if(username.length() > 0 && password.length() >0 ){
    		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
			SharedPreferences.Editor editor = sharedPref.edit();
			RandomSessionString rs = new RandomSessionString();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			String sID= rs.getSessionId();
			Log.i("session login",sID );
			editor.putString(ConstantsStrings.sessionPref, sID);			
			editor.putString(ConstantsStrings.userPref, username);
			//editor.putString(KeyStatic.EMPLOYEE_ROLE, employee.getRole());
			editor.commit();
    		new CheckCredentialsTask().execute(username, password,sID);    		
    	}else{
    		
    		if(username.length() >0){
    			passwordET.setHintTextColor(getResources().
    					getColor(R.color.red));
    			passwordET.setText("");
    			passwordET.setHint("Enter Password");
    		}else if (password.length() > 0){
    			usernameET.setHintTextColor(getResources().
    					getColor(R.color.red));
        		usernameET.setText("");
        		usernameET.setHint("Enter Username");
    		}else{
    			usernameET.setHintTextColor(getResources().
    					getColor(R.color.red));
    			passwordET.setHintTextColor(getResources().
    					getColor(R.color.red));
        		usernameET.setText("");
        		passwordET.setText("");
        		usernameET.setHint("Enter Username");
        		passwordET.setHint("Enter Password");
    		}
    	}
    	
    }
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	
	public void clickChangePassword(View v){
		 Log.i("change pass", "method");
		 Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.forgot_password_dialog);
        dialog.setTitle("Custom Alert Dialog");

//        final EditText uName=(EditText)dialog.findViewById(R.id.fpUerNameET);
//        Button save=(Button)dialog.findViewById(R.id.fpSubmitBtn);
//        Button btnCancel=(Button)dialog.findViewById(R.id.fpCancelBtn);
        dialog.show();
	}
	
	public void showAlert(final String message){
		new AlertDialog.Builder(this)
	    .setTitle("Time Out!")
	    .setMessage(message)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	dialog.dismiss();
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	private void regiterParse(String code) {
		
		
		ParseInstallation.getCurrentInstallation().saveInBackground();
      
		List<String> channels= new ArrayList<String>();
		channels.add("");
		//channels.add(code.toString());
		channels.add(code);
		ParseInstallation.getCurrentInstallation().put("channels", channels);
        ParseInstallation.getCurrentInstallation().put("age", 255);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast toast = Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					e.printStackTrace();
					Toast toast = Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
        
        ParseObject deviceList = new ParseObject("DeviceList");
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("DeviceList");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DeviceList");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //objectsWereRetrievedSuccessfully(objects);
                	Log.i("pars return", "success");
                	String test=objects.get(0).getString("TR_Code");
                	Log.i("pars tr", test);
                } else {
                    //objectRetrievalFailed();
                	Log.i("pars return", "fail");
                }
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
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
				SharedPreferences.Editor editor = sharedPref.edit();
				String newipStr= newIPET.getText().toString();
				editor.putString(ConstantsStrings.IP_SELECTED_SHARE,"http://"+ newipStr+"/");
				editor.commit();
				//new CheckConnection().execute("test");
				dialog.dismiss();
				
			}
		});
		dft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
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
	
	public void addUserCall(String uID,String devID){
		String IP = "192.168.174.40"; //IP CASA
	    //private static String IP = "10.0.2.2"; //IP LOCALHOST

	    String DIRECCION_GET = "http://" + IP + ":8080/de.nitin.jersey.todo/rest/mobileUsers/remove/4";
	    String DIRECCION_POST = "http://" + IP + ":8080/ServidorLoc/rest/json/service/post";
	    HttpClient httpclient = null;
	    String jaxrsmessage = null;
	    try{
	    	httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(DIRECCION_GET);
            request.addHeader("Content-Type", "text/plain");
            request.addHeader("Accept", "text/plain");
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            jaxrsmessage = read(instream);
            String k=instream.toString();
            String l=k+"";
	    }catch (Exception e){
	    		    	
	    }
	    
	    
	}
	private static String read(InputStream instream) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			BufferedReader r = new BufferedReader(new InputStreamReader(
					instream));
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}

			instream.close();
			r.close();

		} catch (IOException e) {
			Log.e("reading jax rs", "error: " + e.toString()
					+ "\nMessage: " + e.getMessage());
		}
		return sb.toString();

	}
	
	@SuppressLint("CommitPrefEdits")
	private class DownloadAccountsTask extends AsyncTask<String, Void, ArrayList<AccountModel>>{
    	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();			
		}
		
    	@Override
		protected ArrayList<AccountModel> doInBackground(String...arg) {
    		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
    		String sID=preferences.getString(ConstantsStrings.sessionPref, "");
			return GetAccountsService.getAccounts(arg[0],sID) ;
		}
		
		@Override
		protected void onPostExecute(ArrayList<AccountModel> list){
			
			if(list!=null){
				
				CustomListViewValuesArr=list;
				String kl= CustomListViewValuesArr.get(0).getAccountName();
				String k1l= CustomListViewValuesArr.get(0).getAccountId();
				String sos=kl+k1l;				
				Intent i = new Intent(LoginActivity.this,HomeTabsActivity.class);
				i.putExtra("Accounts", CustomListViewValuesArr);		
				dialog.dismiss();
				SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
				preferences.edit().remove("loginTime").commit();
				startActivity(i);
				
			}
			else{
				dialog.dismiss();
				Toast t =Toast.makeText(LoginActivity.this, "Connection Error. Failed to load Accounts", Toast.LENGTH_SHORT);
				t.show();
			}
					
		}    	
    }
	
	
	private class CheckCredentialsTask extends AsyncTask<String, Void,HashMap<Integer, String>> {

    	private Exception invalidLogin;
    	
    	@Override
		protected void onPreExecute(){
    		//dialog= new ProgressDialog(LoginActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
    		dialog=ProgressDialog.show(LoginActivity.this, "", "Please wait...", true,false);
		}
		
		@Override
		protected HashMap<Integer, String> doInBackground(String... params) {
			
			try{

				return AuthenticateUserService.AuthenticateUser(params[0], params[1],params[2]);
				
			}catch(Exception ex){
				invalidLogin = ex;
				return null;
			}
		}
    	
		@Override
		protected void onPostExecute(HashMap<Integer, String> result){
			
			
			if(result.containsKey(0)){
				dialog.dismiss();
				Toast t = Toast.makeText(LoginActivity.this, "Username or password is invalid", Toast.LENGTH_SHORT);
				t.show();
			}
			
			else if(result.containsKey(-1)){
				dialog.dismiss();
				Toast t = Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_SHORT);
				t.show();			
			} 	
			
			else if(result.containsKey(1)) {				
				new DownloadAccountsTask().execute(usernameET.getText().toString());
				String trCode=result.get(1);
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(ConstantsStrings.TR_CODE_PREF, trCode);
				editor.commit();
	    		String sID=sharedPref.getString(ConstantsStrings.NOTIF_PREF, "ON");
				if(sID.equals("ON")){
					regiterParse(trCode);
				}else{
					regiterParse("");
				}
				
//				usernameET.setText("");
			passwordET.setText("");
//				usernameET.setHint("Enter username");
				passwordET.setHint("Enter password");
//				usernameET.setHintTextColor(getResources().
//						getColor(R.color.hinttextcolor));
//				passwordET.setHintTextColor(getResources().
//						getColor(R.color.hinttextcolor));
												
			}
		}


    }
}
