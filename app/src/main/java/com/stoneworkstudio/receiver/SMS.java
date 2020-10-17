package com.stoneworkstudio.receiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import com.stoneworkstudio.database.DatabaseAssetHelper;

import java.lang.reflect.Method;
import java.util.List;

public class SMS extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private String destinationNumber;
    private Integer simSlot;

    public SMS(){
        this.simSlot=0;
    }
    public SMS(Integer simSlot){
        this.simSlot = simSlot;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                this.destinationNumber =messages[0].getOriginatingAddress();
                Log.v("SMS",message);
                if(message.toUpperCase().startsWith("TKN#")){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    String tokenNumber = message.substring(4,message.length());
                    DatabaseAssetHelper obj = new DatabaseAssetHelper(context);
                    Cursor cursor = obj.tokenExists(tokenNumber);
                    if(cursor.getCount()==1){
                        sendSMSByManager(this.destinationNumber, this.simSlot,"Message: "+message+" for Token verification is received. ***Registered***", context);
                    }else{
                        sendSMSByManager(this.destinationNumber, this.simSlot,"Message: "+message+" for Token verification is received. ***Not Registered***", context);
                    }
                }
                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
            }
        }
    }
    final public void sendSMSByManager(String destinationNumber, Integer simSlot, String smsContent, Context context) {
        SubscriptionManager subscriptionManager = SubscriptionManager.from(context.getApplicationContext());
        @SuppressLint("MissingPermission") List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            Log.v("SMS","subscriptionId:"+subscriptionId);
        }

        SmsManager smsmanager = SmsManager.getSmsManagerForSubscriptionId(subscriptionInfoList.get(simSlot).getSubscriptionId());
        smsmanager.sendTextMessage(destinationNumber, null ,smsContent, null, null);
        Log.v("SMS","SMS sent");
        //smsmanager.sendTextMessage("+923219517478", null ,re, null, null);

    }
    final public void sendSMS(int simSlot, String toNum, String centerNum, String smsText, String sentIntent, String deliveryIntent, Context context){
        try {


        String name;
        if (simSlot == 0) {
            name = "isms";
            // for model : "Philips T939" name = "isms0"
        } else if (simSlot == 1) {
            name = "isms2";
        } else {
            throw new Exception("can not get service which for sim '" + simSlot + "', only 0,1 accepted as values");
        }
        Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
        method.setAccessible(true);
        Object param = method.invoke(null, name);

        method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
        method.setAccessible(true);
        Object stubObj = method.invoke(null, param);
        if (Build.VERSION.SDK_INT < 18) {
            method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
            method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
        } else {
            method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
            method.invoke(stubObj, context.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
        }

    }catch (Exception ex){

        }
    }

}