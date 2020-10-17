package com.stoneworkstudio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.stoneworkstudio.constants.Constants;
import com.stoneworkstudio.service.SMSService;

public class AutoStart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent myIntent = new Intent(context, SMSService.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //context.startForegroundService(myIntent);
            context.startService(myIntent);
        } else {
            context.startService(myIntent);
        }
        Toast.makeText(context, "SMS_Service Autostarted Successfully", Toast.LENGTH_SHORT).show();

    }

}