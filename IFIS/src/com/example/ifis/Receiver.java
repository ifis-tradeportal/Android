package com.example.ifis;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class Receiver extends ParsePushBroadcastReceiver  {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		String pushdt=null;
		String uriString = null;
		ParseAnalytics.trackAppOpened(intent);
        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            pushdt=pushData.toString();
            uriString = pushData.optString("alert");
        } catch (JSONException e) {
            Log.v("com.parse.ParsePushReceiver", "Unexpected JSONException when receiving push data: ", e);
        }
		Log.e("context is", context.getPackageName());
		Log.e("intent", intent.getPackage());
		Toast.makeText(context, uriString, 700).show();
		
		updateMyActivity(context,uriString);
	    super.onReceive(context, intent);
	    
	}
	
	static void updateMyActivity(Context context, String message) {
		
		        Intent intent = new Intent("unique_name");
		        
		        
		        //put whatever data you want to send, if any
		        intent.putExtra("message", message);
		
		        //send broadcast
		        context.sendBroadcast(intent);
		    }
	
	
	
	@Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        getNotification(context, intent);
        
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	        ComponentName topActivity = tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(context.getPackageName())) {
	            //return true;
	        	Log.i("push 2", "bolngs here");
	        	 Intent i = new Intent(context, LoginActivity.class);
	             i.putExtras(intent.getExtras());
	             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	             context.startActivity(i);
	        }
	    }
	    else{
	    	Log.i("push 2", "not bolngs here");
	    }

	    
        

        
        Log.e("context", context.getPackageResourcePath().toString());
        Log.e("context", intent.getPackage().toString());
       
    }
	
	

}