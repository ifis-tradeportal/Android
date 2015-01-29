package com.example.ifis;

import java.lang.reflect.Field;
import java.util.Currency;
import java.util.Date;


import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Browser.BookmarkColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class HomeTabsActivity extends TabActivity {

	private Intent lastIntent;
	public static boolean refresh;
	public static Context contextOfApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_tabs);
		contextOfApplication = getApplicationContext();
	
		setTitle("IFIS");	
		try {
			  ViewConfiguration config = ViewConfiguration.get(this);
			  Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

			  if (menuKeyField != null) {
			    menuKeyField.setAccessible(true);
			    menuKeyField.setBoolean(config, false);
			  }
			}
			catch (Exception e) {
			  // presumably, not relevant
			}
		
        final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        TabWidget widget = tabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.ifis_tabs);
        }

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    //tv.setTextColor(Color.parseColor("#ACB700"));
                    tv.setTextSize(12);
                }

                
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                //tv.setTextColor(Color.parseColor("#ACB700"));
                tv.setTextSize(14);

            }
        });

        TabSpec tab1 = tabHost.newTabSpec("First Tab");
        
        TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabSpec tab3 = tabHost.newTabSpec("Third tab");

        
       // Set the Tab name and Activity
       // that will be opened when particular Tab will be selected
        tab1.setIndicator("Trade",getResources().getDrawable(R.drawable.trade));
        tab1.setContent(new Intent(this,OrderEntryActivity.class));
        
        tab2.setIndicator("Order Book");
        tab2.setContent(new Intent(this,OrderBookActivity.class));

        tab3.setIndicator("Account");
        tab3.setContent(new Intent(this,AccountActivity.class));
        
        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
		
	}
	
	public static Context getContextOfApplication(){
	    return contextOfApplication;
	    //this method to get shared prefs in non-activiy classes
	}
	
	@Override
	public void onBackPressed()
	{

	   // super.onBackPressed(); // Comment this super call to avoid calling finish()
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_tabs, menu);
		return true;
	}

	public static boolean getRefreshflag(){
		return refresh;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_logout:
			Intent i = new Intent(HomeTabsActivity.this,LoginActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
			
			break;
		case R.id.action_settings:
			Intent i2 = new Intent(HomeTabsActivity.this,SettingsActivity.class);
			startActivity(i2);

		default:
			break;
		}
		if (id == R.id.action_logout) {
			Intent i = new Intent(HomeTabsActivity.this,LoginActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivityForResult(i, ConstantsStrings.SESSION_FAIL_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HomeTabsActivity.this);
		SharedPreferences.Editor editor = sharedPref.edit();
		long now= new Date().getTime();
		String nowStr=String.valueOf(now);
		editor.putLong("loginTime", now);
		Log.i("log in time", String.valueOf(now));
		editor.commit();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (MyApplication.wasInBackground) {
			SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(HomeTabsActivity.this);
    		long outTime=preferences.getLong("outTime", 0);
    		long inTime = new Date().getTime();
    		Log.i("time ", String.valueOf(outTime)+"/"+String.valueOf(inTime));
    		long diff=(inTime-outTime);
    		if(diff>ConstantsStrings.SESSION_TIME){
    			Toast t=Toast.makeText(getApplicationContext(),
    					" Time Out !\n\nlogin Again.", Toast.LENGTH_SHORT);
    			t.setGravity(Gravity.CENTER, 0, 0);
    			t.show();
    			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(HomeTabsActivity.this);
    			pref.edit().remove("outTime").commit();
    			Intent i = new Intent(HomeTabsActivity.this,LoginActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivityForResult(i, ConstantsStrings.SESSION_TIME_OUT);
    		}
    		else{
    			SharedPreferences sharedPref = PreferenceManager
    					.getDefaultSharedPreferences(HomeTabsActivity.this);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(HomeTabsActivity.this);
		preferences.edit().remove("loginTime").commit();
		ConstantsStrings cs= new ConstantsStrings();
		String ip=cs.getSelectedIP();
		Log.i("selected ip", ip);
		final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        TabWidget widget = tabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.ifis_tabs);
        }
		TabHost tabhost = getTabHost();
	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++) 
	    {
	        TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	       // tv.setTextColor(Color.parseColor("#ACB700"));
	    }
	    Intent i = getIntent();
	    
	    if(i.hasExtra("1")){
	    	tabhost.setCurrentTab(1);
	    	Log.i("A1", "coming from confirm");	
	    	i.removeExtra("1");
	    }
//	    if(i.hasExtra("2")){
//	    	tabhost.setCurrentTab(0);
//	    	Log.i("A1", "comming from account");	
//	    	i.removeExtra("2");
//	    }
//	    
	    
	}
	
	public void switchTab(int tab){
		TabHost tabhost = getTabHost();
		tabhost.setCurrentTab(tab);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i("kaka", "on activity result");
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==102){
			final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
			tabHost.setCurrentTab(1);
			Log.i("kaka", "on activity result2");
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		super.onNewIntent(intent);
		this.lastIntent = this.getIntent();
	}
}
