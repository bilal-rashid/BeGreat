package com.guards.attendance.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.guards.attendance.R;
import com.guards.attendance.recievers.PulseReciever;
import com.guards.attendance.toolbox.SmsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Bilal Rashid on 1/16/2018.
 */

public class AppUtils {
    public String getContactName(final String phoneNumber, Context context) {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }
        if (contactName.length()<1)
            contactName = "" + phoneNumber;

        return contactName;
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    //        StringBuilder smsBuilder = new StringBuilder();
//        final String SMS_URI_INBOX = "content://sms/inbox";
//        final String SMS_URI_ALL = "content://sms/";
//        try {
//            Uri uri = Uri.parse(SMS_URI_INBOX);
//            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
//            Cursor cur = getContentResolver().query(uri, projection, "address='+923335298488'", null, "date desc");
//            if (cur.moveToFirst()) {
//                int index_Address = cur.getColumnIndex("address");
//                int index_Person = cur.getColumnIndex("person");
//                int index_Body = cur.getColumnIndex("body");
//                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("type");
//                do {
//                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String strbody = cur.getString(index_Body);
//                    long longDate = cur.getLong(index_Date);
//                    int int_Type = cur.getInt(index_Type);
//
//                    smsBuilder.append("[ ");
//                    smsBuilder.append(strAddress + ", ");
//                    smsBuilder.append(intPerson + ", ");
//                    smsBuilder.append(strbody + ", ");
//                    smsBuilder.append(longDate + ", ");
//                    smsBuilder.append(int_Type);
//                    smsBuilder.append(" ]\n\n");
//                } while (cur.moveToNext());
//
//                if (!cur.isClosed()) {
//                    cur.close();
//                    cur = null;
//                }
//            } else {
//                smsBuilder.append("no result!");
//            } // end if
//
//        } catch (SQLiteException ex) {
//            Log.d("SQLiteException", ex.getMessage());
//        }
//        Log.d("TAAAG",smsBuilder.toString());
    public static int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }
    public static void showSnackBar(View v, String message) {
        if (v != null && !TextUtils.isEmpty(message)) {
            Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundResource(R.color.colorSnackBar);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorSnackBarText));
            snackbar.show();
        }
    }
    public static String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c.getTime());
    }
    public static String getDateAndTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        return df.format(c.getTime());
    }
    public static void vibrate(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);
    }
    public static void sendSMS(String phoneNo, String msg,final Context context) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "Failed! sms not sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            break;
                        case Activity.RESULT_CANCELED:
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, sentPI, deliveredPI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void startPulse(Context context) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent ll24 = new Intent(context, PulseReciever.class);
        PendingIntent recurringLl24 = PendingIntent.getBroadcast(context, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + Constants.INTERVAL,
                Constants.INTERVAL, recurringLl24);
    }
    public static void stopPulse(Context context) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent ll24 = new Intent(context, PulseReciever.class);
        PendingIntent recurringLl24 = PendingIntent.getBroadcast(context, 0, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(recurringLl24);
    }
    public static void changeProfile(Context context) {
        AudioManager am;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

//For Normal mode
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
    }
    public static void IncreaseSound(Context context) {
        AudioManager am;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
    }
    public static void playSound(Context context){
//        final MediaPlayer mp = MediaPlayer.create(context, R.raw.buzzer);
//        mp.start();
//        mp.setVolume(100,100);
    }
    public static boolean isInternetAvailable(final Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conn.getActiveNetworkInfo();
        if (activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    public static void sendCheckin(String phoneNo, String msg, final Context context, final SmsListener listener) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            listener.onCheckinSuccess();
                            break;
                        default:
                            Toast.makeText(context, "Failed! sms not sent",
                                    Toast.LENGTH_SHORT).show();
                            listener.onCheckinFailure();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            break;
                        case Activity.RESULT_CANCELED:
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, sentPI, deliveredPI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void sendCheckout(String phoneNo, String msg, final Context context, final SmsListener listener) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            listener.onCheckoutSuccess();
                            break;
                        default:
                            Toast.makeText(context, "Failed! sms not sent",
                                    Toast.LENGTH_SHORT).show();
                            listener.onCheckoutFailure();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            break;
                        case Activity.RESULT_CANCELED:
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, sentPI, deliveredPI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendNormalText(String phoneNo, String msg, final Context context) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                    new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            break;
                        default:
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            break;
                        case Activity.RESULT_CANCELED:
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, "dsdsdsd", sentPI, deliveredPI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
