package com.stoneworkstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

import com.stoneworkstudio.database.DataBaseHelper;
import com.stoneworkstudio.database.DatabaseAssetHelper;
import com.stoneworkstudio.service.SMSService;
import com.stoneworkstudio.sms.SMS;

public class MainActivity extends AppCompatActivity {

   // SMS sms = new SMS("+923000806458");
    private TextView messageDisplayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageDisplayTextView = (TextView) findViewById(R.id.messageDisplay);
        messageDisplayTextView.setText("This is new Text");
        //final SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage("+923219517478",null,"andriod sms",null,null);
        SmsManager smsmanager = SmsManager.getDefault();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.BROADCAST_SMS},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                1);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_SMS},
                1);



        startService(new Intent(this, SMSService.class));

    }
}
