package com.example.ifis;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class ConstantsStrings {
	
	//public static final String userID = "hy1";
	public static final String pswd 						= "1234";
	public static final String wrongPswd 					= "xyzz";
	//public static final String IP_109 					= "http://192.168.174.109/";
	public static final String IP_109 						= "http://118.189.2.46:7010/";
	public static final String IP_PUBLIC_46 				= "http://118.189.2.46:10901/";
	public static final String NAME_SPACE_OMS				="http://OMS/";
	// public static final String sessionID="test";
	public static final String SELECT_ACCOUNT 				= "Select Account";
	public static final String sessionPref 					= "session";
	public static final String userPref 					= "userID";
	public static final String SESSION_TIMEOUT				   = "Session Timout please try again!";

	public static final String ORDER_STATUS_PENDING_QUEUE      = "PENDING QUEUE";
    public static final String ORDER_STATUS_QUEUE              = "QUEUE";
    public static final String ORDER_STATUS_FILLED             = "FILLED";
    public static final String ORDER_STATUS_PARTIALLY_FILLED   = "PARTIALLY FILLED";
    public static final String ORDER_STATUS_REJECTED           = "REJECTED";
    public static final String ORDER_STATUS_PENDING_REPLACE    = "PENDING CHANGED";
    public static final String ORDER_STATUS_PENDING_CANCEL     = "PENDING CANCEL";
    public static final String ORDER_STATUS_PART_CANCEL        = "PART CANCELLED";
    public static final String ORDER_STATUS_CANCELLED          = "CANCELLED";
    public static final String ORDER_STATUS_REPLACED           = "CHANGED";
    public static final String ORDER_STATUS_PART_CHANGE        = "PART CHANGED";
    public static final String ORDER_STATUS_DONE_FOR_DAY       = "DONE FOR DAY";
    
    public static final long SESSION_TIME					   = 25000;
    
    public static final String TRADE_SIDE_BUY                  = "BUY";
    public static final String TRADE_SIDE_SELL                 = "SELL";
    public static final int SESSION_TIME_OUT				   = 88;
    public static final int RESULT_DO_REFRESH                  = 101;
    public static final String ORDER_FAIL		               = "Order Failed,Please try again!";
    public static final String ORDER_SUCCESS		           = "Order Made Sucessfully!";
    public static final int SESSION_FAIL_CODE                  = 108;
    public static final String SESSION_FAIL_STRING		       = "Invalid session. Please login again.";
    public static final String MUTIPLE_LOGIN			       = "Multiple Login Error! Please login again.";
    public static final String IP_SELECTED_SHARE			   = "selectedIP";
    public static final String CONNECTION_ERROR_STR			   = "Connection Error!";
    
    public static final String TR_CODE_PREF 					= "trCode";
    public static final String NOTIF_PREF	 					= "notif";
    public static final String FILL_ACC_PREF	 					= "fillac";
    public static final String FILL_STOCK_PREF	 					= "fillStk";
    public static final String FILL_ACC_NAME_PREF	 					= "fillStk";
    /***gcm config*/
    
    
    // CONSTANTS
    static final String YOUR_SERVER_URL =  
                          "YOUR_SERVER_URL/gcm_server_files/register.php";
     
    // Google project id
    public static final String GOOGLE_SENDER_ID = "1010940929689";
    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";
    
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.example.notificationtest.DISPLAY_MESSAGE";
    
    public static final String EXTRA_MESSAGE = "message";
    
    Context applicationContext = LoginActivity.getContextOfApplication();
    //Context applicationContext = HomeTabsActivity.getContextOfApplication();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    
    public String getSelectedIP(){
    	
    return	prefs.getString(IP_SELECTED_SHARE, IP_109);
    
    }
    
}
