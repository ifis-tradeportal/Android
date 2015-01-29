//package com.example.ifis;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.NotificationManagerCompat;
//import android.util.Log;
//
//import com.example.ifis.R;
//import com.google.android.gcm.GCMBaseIntentService;
// 
//public class GCMIntentService extends GCMBaseIntentService {
// 
//    private static final String TAG = "GCMIntentService";
//     
//    private MyApplication aMyApplication = null;
// 
//    public GCMIntentService() {
//        // Call extended class Constructor GCMBaseIntentService
//        super(ConstantsStrings.GOOGLE_SENDER_ID);
//    }
// 
//    /**
//     * Method called on device registered
//     **/
////    @Override
////    protected void onRegistered(Context context, String registrationId) {
////         
////        //Get Global MyApplication Class object (see application tag in AndroidManifest.xml)
////        if(aMyApplication == null)
////           aMyApplication = (MyApplication) getApplicationContext();
////         
////        Log.i(TAG, "Device registered: regId = " + registrationId);
////        aMyApplication.displayMessageOnScreen(context, 
////                                           "Your device registred with GCM");
////        Log.d("NAME", "");
////        aMyApplication.register(context,"name",
////                               "email", registrationId);
////    }
// 
//    /**
//     * Method called on device unregistred
//     /* */
////    @Override
////    protected void onUnregistered(Context context, String registrationId) {
////        if(aMyApplication == null)
////            aMyApplication = (MyApplication) getApplicationContext();
////        Log.i(TAG, "Device unregistered");
////        aMyApplication.displayMessageOnScreen(context, 
////                                            "gcm un registered");
////        aMyApplication.unregister(context, registrationId);
////    }
// 
//    /**
//     * Method called on Receiving a new message from GCM server
//     * */
////    @Override
////    protected void onMessage(Context context, Intent intent) {
////         
////        if(aMyApplication == null)
////            aMyApplication = (MyApplication) getApplicationContext();
////         
////        Log.i(TAG, "Received message");
////        String message = intent.getExtras().getString("message");
////         
////        aMyApplication.displayMessageOnScreen(context, message);
////        // notifies user
////        //generateNotification(context, "#1123 STEL.SI is FILLED");
////        generateNotification(context, message);
////        updateMyActivity(context,message);
////    }
//    
//    
//    static void updateMyActivity(Context context, String message) {
//
//        Intent intent = new Intent("unique_name");
//
//        //put whatever data you want to send, if any
//        intent.putExtra("message", message);
//
//        //send broadcast
//        context.sendBroadcast(intent);
//    }
// 
//    /**
//     * Method called on receiving a deleted message
//     * */
//    @Override
////    protected void onDeletedMessages(Context context, int total) {
////         
////        if(aMyApplication == null)
////            aMyApplication = (MyApplication) getApplicationContext();
////         
////        Log.i(TAG, "Received deleted messages notification");
////        String message = getString(R.string.gcm_deleted, total);
////        aMyApplication.displayMessageOnScreen(context, message);
////        // notifies user
////        generateNotification(context, message);
////    }
// 
//    /**
//     * Method called on Error
//     * */
////    @Override
////    public void onError(Context context, String errorId) {
////         
////        if(aMyApplication == null)
////            aMyApplication = (MyApplication) getApplicationContext();
////         
////        Log.i(TAG, "Received error: " + errorId);
////        aMyApplication.displayMessageOnScreen(context, 
////                                   getString(R.string.gcm_error, errorId));
////    }
// 
//    @Override
////    protected boolean onRecoverableError(Context context, String errorId) {
////         
////        if(aMyApplication == null)
////            aMyApplication = (MyApplication) getApplicationContext();
////         
////        // log message
////        Log.i(TAG, "Received recoverable error: " + errorId);
////        aMyApplication.displayMessageOnScreen(context, 
////                        getString(R.string.gcm_recoverable_error,
////                        errorId));
////        return super.onRecoverableError(context, errorId);
////    }
// 
//    /**
//     * Create a notification to inform the user that server has sent a message.
//     */
////    @SuppressWarnings("deprecation")
////	private static void generateNotification(Context context, String message) {
////     
////        int icon = R.drawable.ifis_logo;
////        long when = System.currentTimeMillis();
////         
////        NotificationManager notificationManager = (NotificationManager)
////                context.getSystemService(Context.NOTIFICATION_SERVICE);
////        Notification notification = new Notification(icon, message, when);
////         
////        //String title = context.getString(R.string.app_name);
////        String title ="Order Status Updated";
////        Intent notificationIntent = new Intent(context, LoginActivity.class);
////        // set intent so it does not start a new activity
////        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
////                Intent.FLAG_ACTIVITY_SINGLE_TOP);
////        PendingIntent intent =
////                PendingIntent.getActivity(context, 0, notificationIntent, 0);
////        notification.setLatestEventInfo(context, title, message, intent);
////        notification.flags |= Notification.FLAG_AUTO_CANCEL;
////         
////        
////        // Play default notification sound
////        notification.defaults |= Notification.DEFAULT_SOUND;
////         
////        // Vibrate if vibrate is enabled
////        notification.defaults |= Notification.DEFAULT_VIBRATE;
////        
////        notificationManager.notify(1, notification);      
//// 
////    }
// 
//}
