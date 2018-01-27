package com.guards.attendance.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import com.guards.attendance.models.Packet;

import java.util.HashMap;

/**
 * Created by Bilal Rashid on 1/27/2018.
 */

public class SmsUtils {
    public static HashMap<String,String> getAllGuards(Context context){
        HashMap<String,String> hashMap = new HashMap<>();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{ "address", "body","date"};
            Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");
                do {
                    String strAddress = cur.getString(index_Address);
                    String strbody = cur.getString(index_Body);
                    if(strbody.contains("\"" + Constants.UNIQUE_ID +"\"")) {
                        hashMap.put(strAddress, GsonUtils.fromJson(strbody, Packet.class).emp_id);
                    }
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
        return hashMap;
    }
}
