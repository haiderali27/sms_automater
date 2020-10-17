package com.stoneworkstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stoneworkstudio.constants.Constants;
import com.stoneworkstudio.service.SMSService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   // SMS sms = new SMS("+923000806458");
    private static TextView messageDisplayTextView;
    private Button checkServiceStatusButton;

    private boolean isSvcRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkServiceStatusButton = (Button) findViewById(R.id.checkServiceStatus);
        checkServiceStatusButton.setOnClickListener(this);
        messageDisplayTextView = (TextView) findViewById(R.id.messageDisplay);
        messageDisplayTextView.setText("Application Started");
        //final SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage("+923219517478",null,"andriod sms",null,null);
        SmsManager smsmanager = SmsManager.getDefault();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.FOREGROUND_SERVICE},1 );
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BROADCAST_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
        Intent intent = new Intent(this, SMSService.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(intent);
    }

    @Override
    public void onClick(View view) {

    }
}
