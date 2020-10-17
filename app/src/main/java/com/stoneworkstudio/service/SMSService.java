package com.stoneworkstudio.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.stoneworkstudio.receiver.SMS;

import java.util.Calendar;


public class SMSService extends Service {
    private static SMS smsReceiver;
    private  static int counter;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        smsReceiver = new SMS();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_DELIVER");
        filter.addAction("android.intent.action.DATA_SMS_RECEIVED");
        filter.addAction("android.permission.READ_PHONE_STATE");
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        //filter.addAction("your_action_strings"); //further more
        //filter.addAction("your_action_strings"); //further more
       // Notification notification = new Notification();
      //  startForeground(1, notification);
        registerReceiver(smsReceiver, filter);
        Toast.makeText(this, "SMS_Service created successfully", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "SMS_Service started successfully with id:"+startId, Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        /*
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android

        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

     */
        Intent restartIntent = new Intent(getApplicationContext(), this.getClass());
        restartIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, restartIntent, 0);
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //Toast.makeText(getApplicationContext(), "Start Alarm", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
        Toast.makeText(this, "SMS_Service destroyed", Toast.LENGTH_SHORT).show();

    }
}
