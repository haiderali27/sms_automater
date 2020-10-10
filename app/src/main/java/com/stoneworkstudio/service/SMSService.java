package com.stoneworkstudio.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.stoneworkstudio.sms.SMS;

public class SMSService extends Service {
    private static SMS smsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        smsReceiver = new SMS();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_DELIVER");
        filter.addAction("android.intent.action.DATA_SMS_RECEIVED");
        filter.addAction("aandroid.permission.READ_PHONE_STATE");
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        //filter.addAction("your_action_strings"); //further more
        //filter.addAction("your_action_strings"); //further more
        registerReceiver(smsReceiver, filter);
        Log.v("MainService","SMSService is created successfully");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("MainService","SMSService is started Successfully");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}
