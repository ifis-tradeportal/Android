package com.example.ifis;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.example.ifis.*;
//import com.google.android.gcm.GCMRegistrar;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
//import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application implements
		ActivityLifecycleCallbacks, ComponentCallbacks2 {

	private final int MAX_ATTEMPTS = 5;
	private final int BACKOFF_MILLI_SECONDS = 2000;
	private final Random random = new Random();
	
	
	void register(final Context context, String name, String email, final String regId) {
           	
	       Log.i(ConstantsStrings.TAG, "registering device (regId = " + regId + ")");
	        
	       String serverUrl = ConstantsStrings.YOUR_SERVER_URL;
	        
	       Map<String, String> params = new HashMap<String, String>();
	       params.put("regId", regId);
	       params.put("name", name);
	       params.put("email", email);
	        
	       
	      
	       long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
	        
	       // Once GCM returns a registration id, we need to register on our server
	       // As the server might be down, we will retry it a couple
	       // times.
	       for (int i = 1; i <= MAX_ATTEMPTS; i++) {
	            
	           Log.d(ConstantsStrings.TAG, "Attempt #" + i + " to register");
	            
	           try {
	               //Send Broadcast to Show message on screen
	               displayMessageOnScreen(context, context.getString(
	                       R.string.server_registering, i, MAX_ATTEMPTS));
	               
	               // Post registration values to web server
	               post(serverUrl, params); 
	               //GCMRegistrar.setRegisteredOnServer(context, true);	                
	               //Send Broadcast to Show message on screen
	               String message = context.getString(R.string.server_registered);
	               displayMessageOnScreen(context, message);
	                
	               return;
	           } catch (IOException e) {
	                
	               // Here we are simplifying and retrying on any error; in a real
	               // application, it should retry only on unrecoverable errors
	               // (like HTTP error code 503).
	                
	               Log.e(ConstantsStrings.TAG, "Failed to register on attempt " + i + ":" + e);
	                
	               if (i == MAX_ATTEMPTS) {
	                   break;
	               }
	               try {
	                    
	                   Log.d(ConstantsStrings.TAG, "Sleeping for " + backoff + " ms before retry");
	                   Thread.sleep(backoff);
	                    
	               } catch (InterruptedException e1) {
	                   // Activity finished before we complete - exit.
	                   Log.d(ConstantsStrings.TAG, "Thread interrupted: abort remaining retries!");
	                   Thread.currentThread().interrupt();
	                   return;
	               }
	                
	               // increase backoff exponentially
	               backoff *= 2;
	           }
	       }
	        
	       String message = context.getString(R.string.server_register_error,
	               MAX_ATTEMPTS);
	        
	       //Send Broadcast to Show message on screen
	       displayMessageOnScreen(context, message);
	   }

	    // Unregister this account/device pair within the server.
	    void unregister(final Context context, final String regId) {
	         
	       Log.i(ConstantsStrings.TAG, "unregistering device (regId = " + regId + ")");
	        
	       String serverUrl = ConstantsStrings.YOUR_SERVER_URL + "/unregister";
	       Map<String, String> params = new HashMap<String, String>();
	       params.put("regId", regId);
	        
	       try {
	           post(serverUrl, params);
	         //  GCMRegistrar.setRegisteredOnServer(context, false);
	           String message = context.getString(R.string.server_unregistered);
	           displayMessageOnScreen(context, message);
	       } catch (IOException e) {
	            
	           // At this point the device is unregistered from GCM, but still
	           // registered in the our server.
	           // We could try to unregister again, but it is not necessary:
	           // if the server tries to send a message to the device, it will get
	           // a "NotRegistered" error message and should unregister the device.
	            
	           String message = context.getString(R.string.server_unregister_error,
	                   e.getMessage());
	           displayMessageOnScreen(context, message);
	       }
	   }

	   // Issue a POST request to the server.
	   private static void post(String endpoint, Map<String, String> params)
	           throws IOException {    
	        
	       URL url;
	       try {
	            
	           url = new URL(endpoint);
	            
	       } catch (MalformedURLException e) {
	           throw new IllegalArgumentException("invalid url: " + endpoint);
	       }
	        
	       StringBuilder bodyBuilder = new StringBuilder();
	       Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	        
	       // constructs the POST body using the parameters
	       while (iterator.hasNext()) {
	           Entry<String, String> param = iterator.next();
	           bodyBuilder.append(param.getKey()).append('=')
	                   .append(param.getValue());
	           if (iterator.hasNext()) {
	               bodyBuilder.append('&');
	           }
	       }
	        
	       String body = bodyBuilder.toString();
	        
	       Log.v(ConstantsStrings.TAG, "Posting '" + body + "' to " + url);
	        
	       byte[] bytes = body.getBytes();
	        
	       HttpURLConnection conn = null;
	       try {
	            
	           Log.e("URL", "> " + url);
	            
	           conn = (HttpURLConnection) url.openConnection();
	           conn.setDoOutput(true);
	           conn.setUseCaches(false);
	           conn.setFixedLengthStreamingMode(bytes.length);
	           conn.setRequestMethod("POST");
	           conn.setRequestProperty("Content-Type",
	                   "application/x-www-form-urlencoded;charset=UTF-8");
	           // post the request
	           OutputStream out = conn.getOutputStream();
	           out.write(bytes);
	           out.close();
	            
	           // handle the response
	           int status = conn.getResponseCode();
	            
	           // If response is not success
	           if (status != 200) {
	                
	             throw new IOException("Post failed with error code " + status);
	           }
	       } finally {
	    	   
	           if (conn != null) {
	               conn.disconnect();
	           }
	       }
	     }
	    
	    
	    
	   // Checking for all possible internet providers
	   public boolean isConnectingToInternet(){
	        
	       ConnectivityManager connectivity = 
	                            (ConnectivityManager) getSystemService(
	                             Context.CONNECTIVITY_SERVICE);
	         if (connectivity != null)
	         {
	             NetworkInfo[] info = connectivity.getAllNetworkInfo();
	             if (info != null)
	                 for (int i = 0; i < info.length; i++)
	                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                     {
	                         return true;
	                     }
	         }
	         return false;
	   }
	    
	  // Notifies UI to display a message.
	  void displayMessageOnScreen(Context context, String message) {
	         
	       Intent intent = new Intent(ConstantsStrings.DISPLAY_MESSAGE_ACTION);
	       intent.putExtra(ConstantsStrings.EXTRA_MESSAGE, message);
	       context.sendBroadcast(intent);
	       
	   }
	    
	    
	  //Function to display simple Alert Dialog
	  public void showAlertDialog(Context context, String title, String message,
	           Boolean status) {
	       AlertDialog alertDialog = new AlertDialog.Builder(context).create();

	       // Set Dialog Title
	       alertDialog.setTitle(title);

	       // Set Dialog Message
	       alertDialog.setMessage(message);

	       if(status != null)
	           // Set alert dialog icon
	          // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

	       // Set OK Button
	       alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
	                
	           }
	       });

	       // Show Alert Message
	       alertDialog.show();
	   }
	    
	   private PowerManager.WakeLock wakeLock;
	    
	   public  void acquireWakeLock(Context context) {
	       if (wakeLock != null) wakeLock.release();

	       PowerManager pm = (PowerManager) 
	                         context.getSystemService(Context.POWER_SERVICE);
	        
	       wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
	               PowerManager.ACQUIRE_CAUSES_WAKEUP |
	               PowerManager.ON_AFTER_RELEASE, "WakeLock");
	        
	       wakeLock.acquire();
	   }

	   public  void releaseWakeLock() {
	       if (wakeLock != null) wakeLock.release(); wakeLock = null;
	   }
	
	
	
	
	

//	ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();
	
	private static String TAG = MyApplication.class.getName();
	
	public static String stateOfLifeCycle = "";

	public static boolean wasInBackground = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("onCreate", "out time clearesd");
		registerActivityLifecycleCallbacks(this);
		SharedPreferences preferences = getSharedPreferences("outTime", 0);
		preferences.edit().remove("outTime").commit();

		
		 
        Parse.initialize(this, "2mVs11lyRGq8ysIeCsMUGu9NQNyfZih9kkcNEOQQ", "uwwZohVdr9XUBu7fWad3s6U6gYxyGXbEAFAytJiK");
		
		
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle arg1) {
		wasInBackground = false;
		stateOfLifeCycle = "Create";
		Log.i("onAcCreate", "out time clearesd activityCreated");
//		registerActivityLifecycleCallbacks(this);
//		SharedPreferences preferences = getSharedPreferences("outTime", 0);
//		preferences.edit().remove("outTime").commit();
	}

	@Override
	public void onActivityStarted(Activity activity) {
		stateOfLifeCycle = "Start";
	}

	@Override
	public void onActivityResumed(Activity activity) {
		stateOfLifeCycle = "Resume";
	}

	@Override
	public void onActivityPaused(Activity activity) {
		stateOfLifeCycle = "Pause";
	}

	@Override
	public void onActivityStopped(Activity activity) {
		stateOfLifeCycle = "Stop";
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle arg1) {
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		wasInBackground = false;
		stateOfLifeCycle = "Destroy";
	}

	@Override
	public void onTrimMemory(int level) {
		if (stateOfLifeCycle.equals("Stop")) {
			wasInBackground = true;
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(MyApplication.this);
			SharedPreferences.Editor editor = sharedPref.edit();
			long now = new Date().getTime();
			editor.putLong("outTime", now);
			Log.i("log in time", String.valueOf(now));
			editor.commit();
		}
		super.onTrimMemory(level);
	}

//	class ScreenOffReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			wasInBackground = true;
//			SharedPreferences sharedPref = PreferenceManager
//					.getDefaultSharedPreferences(MyApplication.this);
//			SharedPreferences.Editor editor = sharedPref.edit();
//			long now = new Date().getTime();
//			editor.putLong("outTime", now);
//			Log.i("log in time", String.valueOf(now));
//			editor.commit();
//		}
//	}

}